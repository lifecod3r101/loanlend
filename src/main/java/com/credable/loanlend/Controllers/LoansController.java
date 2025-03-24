package com.credable.loanlend.Controllers;

import com.credable.loanlend.DataRepositories.CustomerDataRepository;
import com.credable.loanlend.DataRepositories.LoanDataRepository;
import com.credable.loanlend.Models.CustomerInfo;
import com.credable.loanlend.Models.LoanInfo;
import com.credable.loanlend.Models.QueryScoreInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoansController {
    @Autowired
    CustomerDataRepository customerDataRepository;
    @Autowired
    LoanDataRepository loanDataRepository;

    @Value("${bank.auth.username}")
    String clientUserName;
    @Value("${bank.auth.password}")
    String clientUserPassword;

    @Value("${soap.api.trxbase}")
    String trxBaseUrl;

    @PostMapping("/request")
    public ResponseEntity<?> makeLoanRequest(@RequestParam("customerNumber") String customerNumber, @RequestParam("requestedAmount") Double requestedAmount) throws Exception {
        CustomerInfo existingCustomerInfo = customerDataRepository.findByCustomerNumber(customerNumber);
        List<LoanInfo> customerInfos = new ArrayList<>(existingCustomerInfo.getCustomerLoans());
        if (customerInfos.isEmpty()) {
            LoanInfo loanInfo = new LoanInfo();
            loanInfo.setLoanAmount(requestedAmount);
            loanInfo.setLoanRequestStatus("PENDING");
            String clientToken = createClient("/issue", "Loan Issue");
            QueryScoreInfo queryScoreInfo = getCustomerLoanScoreAndLimit(customerNumber, clientToken);
            if (queryScoreInfo.getLimitAmount() < requestedAmount) {
                CustomerInfo customerInfo = customerDataRepository.findByCustomerNumber(customerNumber);
                loanInfo.setCustomerInfo(customerInfo);
                loanDataRepository.save(loanInfo);
                issueLoan(loanInfo.getLoanId(), customerNumber);
                return ResponseEntity.status(HttpStatus.OK).body(loanInfo);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(queryScoreInfo.getExclusionReason());
            }
        } else if (!customerInfos.getLast().getLoanRequestStatus().equals("PENDING")) {
            LoanInfo loanInfo = new LoanInfo();
            loanInfo.setLoanAmount(requestedAmount);
            loanInfo.setLoanRequestStatus("PENDING");
            String clientToken = createClient("/issue", "Loan Issue");
            QueryScoreInfo queryScoreInfo = getCustomerLoanScoreAndLimit(customerNumber, clientToken);
            if (queryScoreInfo.getLimitAmount() < requestedAmount) {
                CustomerInfo customerInfo = customerDataRepository.findByCustomerNumber(customerNumber);
                loanInfo.setCustomerInfo(customerInfo);
                loanDataRepository.save(loanInfo);
                issueLoan(loanInfo.getLoanId(), customerNumber);
                return ResponseEntity.status(HttpStatus.OK).body(loanInfo);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(queryScoreInfo.getExclusionReason());
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Ongoing loan request");
        }
    }

    @GetMapping("/status/{loanId}")
    public ResponseEntity<?> getLoanStatus(@PathVariable("loanId") String loanId) {
        LoanInfo loanInfo = loanDataRepository.findById(loanId).get();
        return ResponseEntity.status(HttpStatus.OK).body(loanInfo);
    }

    public void issueLoan(String loanId, String customerNumber) throws Exception {
        LoanInfo loanInfo = loanDataRepository.findById(loanId).get();
        loanInfo.setLoanRequestStatus("ISSUED");
    }

    public QueryScoreInfo getCustomerLoanScoreAndLimit(String customerNumber, String clientToken) throws Exception {
        String queryScoreInitiateUrlString = "https://scoringtest.credable.io/api/v1/scoring/initiateQueryScore/".concat(customerNumber);
        OkHttpClient client = new OkHttpClient();
        Request queryScoreInitiateRequest = new Request.Builder().addHeader("client-token", clientToken).url(queryScoreInitiateUrlString).build();
        Response queryScoreInitiateResponse = client.newCall(queryScoreInitiateRequest).execute();
        Response queryScoreResponse = null;
        QueryScoreInfo queryScoreInfo = new QueryScoreInfo();
        if (queryScoreInitiateResponse.isSuccessful()) {
            String queryScoreToken = queryScoreInitiateResponse.body().string();
            String queryScoreUrlString = "https://scoringtest.credable.io/api/v1/scoring/queryScore/".concat(queryScoreToken);
            Request queryScoreRequest = new Request.Builder().addHeader("client-token", clientToken).url(queryScoreUrlString).build();
            queryScoreResponse = client.newCall(queryScoreRequest).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            queryScoreInfo = objectMapper.readValue(queryScoreResponse.body().string(), QueryScoreInfo.class);
        }
        return queryScoreInfo;
    }


    public String createClient(String endpointUrl, String serviceName) throws Exception {
        String clientCreateUrl = "https://scoringtest.credable.io/api/v1/client/createClient";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("url", endpointUrl).add("name", serviceName).add("username", clientUserName).add("password", clientUserPassword).build();
        Request clientCreateRequest = new Request.Builder().url(clientCreateUrl).post(requestBody).build();
        Response clientCreateResponse = client.newCall(clientCreateRequest).execute();
        return clientCreateResponse.body().string();
    }

    public ResponseEntity<?> queryCustomerTransactionData(String customerNumber) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/xml");
        String trxSoap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" + "  <soap:Body>\n" + "    <tns:TransactionsRequest xmlns=\"http://credable.io/cbs/transaction\">\n" + "      <tns:customerNumber>" + customerNumber + "</tns:customerNumber>\n" + "    </tns:TransactionsRequest>\n" + "  </soap:Body>\n" + "</soap:Envelope>";
        RequestBody body = RequestBody.create(mediaType, trxSoap);
        Request request = new Request.Builder().url(trxBaseUrl).post(body).addHeader("content-type", "text/xml").build();
        Response response = client.newCall(request).execute();
        return ResponseEntity.status(HttpStatus.OK).body(response.body());
    }
}
