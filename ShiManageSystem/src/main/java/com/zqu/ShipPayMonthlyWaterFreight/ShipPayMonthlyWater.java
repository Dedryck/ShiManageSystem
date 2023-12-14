package com.zqu.ShipPayMonthlyWaterFreight;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @aurhor Dedryck
 * @create 2023-12-09-19:11
 * @description: 定义变量--船只按月缴纳水运费基本信息表
 */
public class ShipPayMonthlyWater {
    // 船名，对应数据库中的 `船名` 字段
    private String shipName;

    // 船检登记号，对应数据库中的 `船检登记号` 字段
    private String shipRegistrationNumber;

    // 航道费，对应数据库中的 `航道费` 字段
    private BigDecimal channelFee;

    // 填表日期，对应数据库中的 `填表日期` 字段
    private Date formFillingDate;

    // 水运费合计，对应数据库中的 `水运费合计` 字段
    private BigDecimal totalWaterTransportFee;

    // 水运费缴纳记录，对应数据库中的 `水运费缴纳记录` 字段
    private String waterTransportFeeRecord;

    public ShipPayMonthlyWater(String shipName, String shipRegistrationNumber, BigDecimal channelFee, Date formFillingDate, BigDecimal totalWaterTransportFee, String waterTransportFeeRecord) {
        this.shipName = shipName;
        this.shipRegistrationNumber = shipRegistrationNumber;
        this.channelFee = channelFee;
        this.formFillingDate = formFillingDate;
        this.totalWaterTransportFee = totalWaterTransportFee;
        this.waterTransportFeeRecord = waterTransportFeeRecord;
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

    public BigDecimal getChannelFee() {
        return channelFee;
    }

    public void setChannelFee(BigDecimal channelFee) {
        this.channelFee = channelFee;
    }

    public Date getFormFillingDate() {
        return formFillingDate;
    }

    public void setFormFillingDate(Date formFillingDate) {
        this.formFillingDate = formFillingDate;
    }

    public BigDecimal getTotalWaterTransportFee() {
        return totalWaterTransportFee;
    }

    public void setTotalWaterTransportFee(BigDecimal totalWaterTransportFee) {
        this.totalWaterTransportFee = totalWaterTransportFee;
    }

    public String getWaterTransportFeeRecord() {
        return waterTransportFeeRecord;
    }

    public void setWaterTransportFeeRecord(String waterTransportFeeRecord) {
        this.waterTransportFeeRecord = waterTransportFeeRecord;
    }
}
