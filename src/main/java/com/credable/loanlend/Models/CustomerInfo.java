package com.credable.loanlend.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "customerinfo")
public class CustomerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customerid")
    String customerId;
    @Column(name = "customernumber")
    String customerNumber;
    @Column(name = "customername")
    String customerName;
    @Column(name = "customerphone")
    String customerPhone;
    @Column(name = "customeremail")
    String customerEmail;
    @OneToMany(mappedBy = "customerInfo")
    Set<LoanInfo> customerLoans;
    @CreationTimestamp
    Instant customerInfoCreateTime;
    @UpdateTimestamp
    Instant customerInfoUpdateTime;

    public CustomerInfo() {
    }

    public CustomerInfo(String customerId, String customerNumber, String customerName, String customerPhone, String customerEmail, Set<LoanInfo> customerLoans, Instant customerInfoCreateTime, Instant customerInfoUpdateTime) {
        this.customerId = customerId;
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerLoans = customerLoans;
        this.customerInfoCreateTime = customerInfoCreateTime;
        this.customerInfoUpdateTime = customerInfoUpdateTime;
    }

    public Instant getCustomerInfoCreateTime() {
        return customerInfoCreateTime;
    }

    public void setCustomerInfoCreateTime(Instant customerInfoCreateTime) {
        this.customerInfoCreateTime = customerInfoCreateTime;
    }

    public Instant getCustomerInfoUpdateTime() {
        return customerInfoUpdateTime;
    }

    public void setCustomerInfoUpdateTime(Instant customerInfoUpdateTime) {
        this.customerInfoUpdateTime = customerInfoUpdateTime;
    }

    public Set<LoanInfo> getCustomerLoans() {
        return customerLoans;
    }

    public void setCustomerLoans(Set<LoanInfo> customerLoans) {
        this.customerLoans = customerLoans;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
