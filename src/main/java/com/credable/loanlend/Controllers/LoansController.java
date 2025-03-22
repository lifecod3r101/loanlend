package com.credable.loanlend.Controllers;

import com.credable.loanlend.DataRepositories.CustomerDataRepository;
import com.credable.loanlend.DataRepositories.LoanDataRepository;
import com.credable.loanlend.Models.CustomerInfo;
import com.credable.loanlend.Models.LoanInfo;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/loans")
public class LoansController {
    @Autowired
    CustomerDataRepository customerDataRepository;
    @Autowired
    LoanDataRepository loanDataRepository;

    @PostMapping("/request")
    public ResponseEntity<?> makeLoanRequest(@RequestParam("customerNumber") String customerNumber, @RequestParam("requestedAmount") Double requestedAmount) {
        LoanInfo loanInfo = new LoanInfo();
        loanInfo.setLoanAmount(requestedAmount);
        loanInfo.setLoanRequestStatus("PENDING");
        CustomerInfo customerInfo = customerDataRepository.findByCustomerNumber(customerNumber);
        loanInfo.setCustomerInfo(customerInfo);
        loanDataRepository.save(loanInfo);
        return ResponseEntity.status(HttpStatus.OK).body(loanInfo);
    }

    @GetMapping("/status/{loanId}")
    public ResponseEntity<?> getLoanStatus(@PathVariable("loanId") String loanId) {
        LoanInfo loanInfo = loanDataRepository.findById(loanId).get();
        return ResponseEntity.status(HttpStatus.OK).body(loanInfo);
    }

    @PostMapping("/issue")
    public ResponseEntity<?> issueLoan(@RequestParam("loanId") String loanId, @RequestParam("customerNumber") String customerNumber) throws Exception {
        LoanInfo loanInfo = loanDataRepository.findById(loanId).get();
        getCustomerLoanScoreAndLimit(customerNumber).getBody();
        loanInfo.setLoanRequestStatus("ISSUED");
        return ResponseEntity.status(HttpStatus.OK).body(loanInfo);
    }

    public ResponseEntity<?> getCustomerLoanScoreAndLimit(String customerNumber) throws Exception {
        String queryScoreInitiateUrlString = "https://scoringtest.credable.io/api/v1/scoring/initiateQueryScore/".concat(customerNumber);
        OkHttpClient client = new OkHttpClient();
        Request queryScoreInitiateRequest = new Request.Builder().url(queryScoreInitiateUrlString).build();
        Response queryScoreInitiateResponse = client.newCall(queryScoreInitiateRequest).execute();
        Response queryScoreResponse = null;
        if (queryScoreInitiateResponse.isSuccessful()) {
            String queryScoreToken = queryScoreInitiateResponse.body().string();
            String queryScoreUrlString = "https://scoringtest.credable.io/api/v1/scoring/queryScore/".concat(queryScoreToken);
            Request queryScoreRequest = new Request.Builder().url(queryScoreUrlString).build();
            queryScoreResponse = client.newCall(queryScoreRequest).execute();
        }
        return ResponseEntity.status(HttpStatus.OK).body(queryScoreResponse.body().string());
    }


    public ResponseEntity<?> createClient(String endpointUrl, String serviceName, String username, String password) throws Exception {
        String clientCreateUrl = "https://scoringtest.credable.io/api/v1/client/createClient";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("url", endpointUrl)
                .add("name", serviceName)
                .add("username", username)
                .add("password", password)
                .build();
        Request clientCreateRequest = new Request.Builder().url(clientCreateUrl).post(requestBody).build();
        Response clientCreateResponse = client.newCall(clientCreateRequest).execute();
        return ResponseEntity.status(HttpStatus.OK).body(clientCreateResponse.body().string());
    }
}
