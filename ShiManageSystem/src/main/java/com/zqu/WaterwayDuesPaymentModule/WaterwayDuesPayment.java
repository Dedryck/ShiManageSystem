package com.zqu.WaterwayDuesPaymentModule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-9:10
 * @description: 用于提取变量“航道费缴纳情况表”
 */
public class WaterwayDuesPayment {
    // 船名
    private String shipName;

    // 船检登记号
    private String shipRegistrationNumber;

    // 航道费（元/月）
    private BigDecimal waterwayFeePerMonth;

    // 填表日期
    private Date dateOfFilling;

    // 航道费合计
    private BigDecimal totalWaterwayFee;

    // 航道费缴纳的记录（本年度每个月缴纳的情况）
    private String paymentRecord;

    public WaterwayDuesPayment(String shipName, String shipRegistrationNumber, BigDecimal waterwayFeePerMonth, Date dateOfFilling, BigDecimal totalWaterwayFee, String paymentRecord) {
        this.shipName = shipName;
        this.shipRegistrationNumber = shipRegistrationNumber;
        this.waterwayFeePerMonth = waterwayFeePerMonth;
        this.dateOfFilling = dateOfFilling;
        this.totalWaterwayFee = totalWaterwayFee;
        this.paymentRecord = paymentRecord;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipRegistrationNumber() {
        return shipRegistrationNumber;
    }

    public void setShipRegistrationNumber(String shipRegistrationNumber) {
        this.shipRegistrationNumber = shipRegistrationNumber;
    }

    public BigDecimal getWaterwayFeePerMonth() {
        return waterwayFeePerMonth;
    }

    public void setWaterwayFeePerMonth(BigDecimal waterwayFeePerMonth) {
        this.waterwayFeePerMonth = waterwayFeePerMonth;
    }

    public Date getDateOfFilling() {
        return dateOfFilling;
    }

    public void setDateOfFilling(Date dateOfFilling) {
        this.dateOfFilling = dateOfFilling;
    }

    public BigDecimal getTotalWaterwayFee() {
        return totalWaterwayFee;
    }

    public void setTotalWaterwayFee(BigDecimal totalWaterwayFee) {
        this.totalWaterwayFee = totalWaterwayFee;
    }

    public String getPaymentRecord() {
        return paymentRecord;
    }

    public void setPaymentRecord(String paymentRecord) {
        this.paymentRecord = paymentRecord;
    }
}
