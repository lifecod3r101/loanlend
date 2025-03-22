package com.credable.loanlend.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "loaninfo")
public class LoanInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "loanid")
    String loanId;
    @Column(name = "loanamount")
    Double loanAmount;
    @Column(name = "loanrequeststatus", columnDefinition = "ENUM('PENDING','ISSUED','FAILED')")
    String loanRequestStatus;
    @ManyToOne
    @JoinColumn(name = "customerid", nullable = false)
    CustomerInfo customerInfo;
    @CreationTimestamp
    Instant loanInfoCreateTime;
    @UpdateTimestamp
    Instant loanInfoUpdateTime;

    public LoanInfo() {
    }

    public LoanInfo(String loanId, Double loanAmount, String loanRequestStatus, CustomerInfo customerInfo, Instant loanInfoCreateTime, Instant loanInfoUpdateTime) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.loanRequestStatus = loanRequestStatus;
        this.customerInfo = customerInfo;
        this.loanInfoCreateTime = loanInfoCreateTime;
        this.loanInfoUpdateTime = loanInfoUpdateTime;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanRequestStatus() {
        return loanRequestStatus;
    }

    public void setLoanRequestStatus(String loanRequestStatus) {
        this.loanRequestStatus = loanRequestStatus;
    }
}
