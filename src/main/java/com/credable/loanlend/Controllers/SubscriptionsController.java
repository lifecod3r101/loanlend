package com.credable.loanlend.Controllers;

import com.credable.loanlend.DataRepositories.CustomerDataRepository;
import com.credable.loanlend.Models.CustomerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionsController {
    @Autowired
    CustomerDataRepository customerDataRepository;
    @Value("${soap.api.kycbase}")
    String kycBaseUrl;

    @PostMapping("/add")
    public ResponseEntity<?> subscribeCustomer(@RequestParam("customerNumber") String customerNumber, @RequestParam("customerName") String customerName, @RequestParam("customerEmail") String customerEmail, @RequestParam("customerPhone") String customerPhone) throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        if (queryCustomerKyc(customerNumber).getBody() != null) {
            customerInfo.setCustomerName(customerName);
            customerInfo.setCustomerEmail(customerEmail);
            customerInfo.setCustomerNumber(customerNumber);
            customerInfo.setCustomerPhone(customerPhone);
            customerDataRepository.save(customerInfo);
            return ResponseEntity.status(HttpStatus.OK).body(customerInfo);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Sorry. Customer does not exist in our records");
        }
    }

    public ResponseEntity<?> queryCustomerKyc(String customerNumber) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/xml");
        String kycSoap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <tns:CustomerRequest xmlns=\"http://credable.io/cbs/customer\">\n" +
                "      <tns:customerNumber>" + customerNumber + "</tns:customerNumber>\n" +
                "    </tns:CustomerRequest>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        RequestBody body = RequestBody.create(mediaType, kycSoap);
        Request request = new Request.Builder()
                .url(kycBaseUrl)
                .post(body)
                .addHeader("content-type", "text/xml")
                .build();
        Response response = client.newCall(request).execute();
        return ResponseEntity.status(HttpStatus.OK).body(response.body());
    }
}
