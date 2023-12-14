package com.zqu.NationalityManningCertificateModule;

import java.util.Date;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-20:23
 * @description: 定义数据表的变量
 */
public class ShipNationalityManningCertificate {

    // 船名
    private String shipName; // VARCHAR(30)

    // 船检登记号
    private String shipInspectionRegistrationNumber; // VARCHAR(30)

    // 国籍配员证书编号
    private String nationalityManningCertificateNumber; // VARCHAR(50), PRIMARY KEY

    // 船舶所有人
    private String shipOwner; // VARCHAR(30)

    // 船舶登记号
    private String shipRegistrationNumber; // VARCHAR(50)

    // 下次换证时间
    private Date nextCertificateRenewalDate; // DATE

    // 换证通知时间
    private Date certificateRenewalNotificationDate; // DATE

    // 国籍配员证书使用有效期至
    private Date certificateValidityEndDate; // DATE

    // 发证日期
    private Date issueDate; // DATE

    // 国籍配员证书换证时间记录
    private String certificateRenewalHistory; // TEXT

    // Getters and Setters for all the fields



    public ShipNationalityManningCertificate(String shipName, String shipInspectionRegistrationNumber, String nationalityManningCertificateNumber, String shipOwner, String shipRegistrationNumber, Date nextCertificateRenewalDate, Date certificateRenewalNotificationDate, Date certificateValidityEndDate, Date issueDate, String certificateRenewalHistory) {
        this.shipName = shipName;
        this.shipInspectionRegistrationNumber = shipInspectionRegistrationNumber;
        this.nationalityManningCertificateNumber = nationalityManningCertificateNumber;
        this.shipOwner = shipOwner;
        this.shipRegistrationNumber = shipRegistrationNumber;
        this.nextCertificateRenewalDate = nextCertificateRenewalDate;
        this.certificateRenewalNotificationDate = certificateRenewalNotificationDate;
        this.certificateValidityEndDate = certificateValidityEndDate;
        this.issueDate = issueDate;
        this.certificateRenewalHistory = certificateRenewalHistory;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipInspectionRegistrationNumber() {
        return shipInspectionRegistrationNumber;
    }

    public void setShipInspectionRegistrationNumber(String shipInspectionRegistrationNumber) {
        this.shipInspectionRegistrationNumber = shipInspectionRegistrationNumber;
    }

    public String getNationalityManningCertificateNumber() {
        return nationalityManningCertificateNumber;
    }

    public void setNationalityManningCertificateNumber(String nationalityManningCertificateNumber) {
        this.nationalityManningCertificateNumber = nationalityManningCertificateNumber;
    }

    public String getShipOwner() {
        return shipOwner;
    }

    public void setShipOwner(String shipOwner) {
        this.shipOwner = shipOwner;
    }

    public String getShipRegistrationNumber() {
        return shipRegistrationNumber;
    }

    public void setShipRegistrationNumber(String shipRegistrationNumber) {
        this.shipRegistrationNumber = shipRegistrationNumber;
    }

    public Date getNextCertificateRenewalDate() {
        return nextCertificateRenewalDate;
    }

    public void setNextCertificateRenewalDate(Date nextCertificateRenewalDate) {
        this.nextCertificateRenewalDate = nextCertificateRenewalDate;
    }

    public Date getCertificateRenewalNotificationDate() {
        return certificateRenewalNotificationDate;
    }

    public void setCertificateRenewalNotificationDate(Date certificateRenewalNotificationDate) {
        this.certificateRenewalNotificationDate = certificateRenewalNotificationDate;
    }

    public Date getCertificateValidityEndDate() {
        return certificateValidityEndDate;
    }

    public void setCertificateValidityEndDate(Date certificateValidityEndDate) {
        this.certificateValidityEndDate = certificateValidityEndDate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getCertificateRenewalHistory() {
        return certificateRenewalHistory;
    }

    public void setCertificateRenewalHistory(String certificateRenewalHistory) {
        this.certificateRenewalHistory = certificateRenewalHistory;
    }
}

