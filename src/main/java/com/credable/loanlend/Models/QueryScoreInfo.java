package com.credable.loanlend.Models;

public class QueryScoreInfo {
    String id;
    String customerNumber;
    Double score;
    Double limitAmount;
    String exclusion;
    String exclusionReason;

    public QueryScoreInfo() {
    }

    public QueryScoreInfo(String id, String customerNumber, Double score, Double limitAmount, String exclusion, String exclusionReason) {
        this.id = id;
        this.customerNumber = customerNumber;
        this.score = score;
        this.limitAmount = limitAmount;
        this.exclusion = exclusion;
        this.exclusionReason = exclusionReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getExclusion() {
        return exclusion;
    }

    public void setExclusion(String exclusion) {
        this.exclusion = exclusion;
    }

    public String getExclusionReason() {
        return exclusionReason;
    }

    public void setExclusionReason(String exclusionReason) {
        this.exclusionReason = exclusionReason;
    }
}
