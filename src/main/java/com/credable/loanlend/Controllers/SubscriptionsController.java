package com.credable.loanlend.Controllers;

import com.credable.loanlend.DataRepositories.CustomerDataRepository;
import com.credable.loanlend.Models.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/add")
    public ResponseEntity<?> subscribeCustomer(@RequestParam("customerNumber") String customerNumber, @RequestParam("customerName") String customerName, @RequestParam("customerEmail") String customerEmail, @RequestParam("customerPhone") String customerPhone) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCustomerName(customerName);
        customerInfo.setCustomerEmail(customerEmail);
        customerInfo.setCustomerNumber(customerNumber);
        customerInfo.setCustomerPhone(customerPhone);
        customerDataRepository.save(customerInfo);
        return ResponseEntity.status(HttpStatus.OK).body(customerInfo);
    }
}
