package com.zqu.UserQueryModule;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-19:35
 * @description: 船只缴费总览视图的变量
 */
public class ShipPaymentInfo {
    private String shipName; // 船名
    private String registrationNumber; // 船检登记号
    private BigDecimal channelFee; // 航道费
    private Date channelFeeDate; // 航道费填表日期
    private BigDecimal totalChannelFee; // 航道费合计
    private String channelFeeRecord; // 航道费缴纳记录
    private Date transportationFeeDate; // 水运费填表日期
    private BigDecimal totalTransportationFee; // 水运费合计
    private String transportationFeeRecord; // 水运费缴纳记录

    public ShipPaymentInfo(String shipName, String registrationNumber, BigDecimal channelFee, Date channelFeeDate, BigDecimal totalChannelFee, String channelFeeRecord, Date transportationFeeDate, BigDecimal totalTransportationFee, String transportationFeeRecord) {
        this.shipName = shipName;
        this.registrationNumber = registrationNumber;
        this.channelFee = channelFee;
        this.channelFeeDate = channelFeeDate;
        this.totalChannelFee = totalChannelFee;
        this.channelFeeRecord = channelFeeRecord;
        this.transportationFeeDate = transportationFeeDate;
        this.totalTransportationFee = totalTransportationFee;
        this.transportationFeeRecord = transportationFeeRecord;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public BigDecimal getChannelFee() {
        return channelFee;
    }

    public void setChannelFee(BigDecimal channelFee) {
        this.channelFee = channelFee;
    }

    public Date getChannelFeeDate() {
        return channelFeeDate;
    }

    public void setChannelFeeDate(Date channelFeeDate) {
        this.channelFeeDate = channelFeeDate;
    }

    public BigDecimal getTotalChannelFee() {
        return totalChannelFee;
    }

    public void setTotalChannelFee(BigDecimal totalChannelFee) {
        this.totalChannelFee = totalChannelFee;
    }

    public String getChannelFeeRecord() {
        return channelFeeRecord;
    }

    public void setChannelFeeRecord(String channelFeeRecord) {
        this.channelFeeRecord = channelFeeRecord;
    }

    public Date getTransportationFeeDate() {
        return transportationFeeDate;
    }

    public void setTransportationFeeDate(Date transportationFeeDate) {
        this.transportationFeeDate = transportationFeeDate;
    }

    public BigDecimal getTotalTransportationFee() {
        return totalTransportationFee;
    }

    public void setTotalTransportationFee(BigDecimal totalTransportationFee) {
        this.totalTransportationFee = totalTransportationFee;
    }

    public String getTransportationFeeRecord() {
        return transportationFeeRecord;
    }

    public void setTransportationFeeRecord(String transportationFeeRecord) {
        this.transportationFeeRecord = transportationFeeRecord;
    }
}
