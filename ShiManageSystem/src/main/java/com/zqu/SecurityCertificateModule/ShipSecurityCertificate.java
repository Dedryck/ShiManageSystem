package com.zqu.SecurityCertificateModule;
import java.util.Date;


/**
 * @aurhor Dedryck
 * @create 2023-12-07-10:14
 * @description:用于提取属性
 */
public class ShipSecurityCertificate {
    // 船名
    private String shipName; // 对应 `船名`

    // 船检登记号
    private String shipInspectionRegistrationNumber; // 对应 `船检登记号`

    // 安检证书编号
    private String securityCertificateNumber; // 对应 `安检证书编号`，主键

    // 船舶所有人
    private String shipOwner; // 对应 `船舶所有人`

    // 船舶登记号
    private String shipRegistrationNumber; // 对应 `船舶登记号`

    // 检查机关
    private String inspectionAuthority; // 对应 `检查机关`

    // 下次检查时间
    private Date nextInspectionDate; // 对应 `下次检查时间`

    // 通知时间
    private Date notificationDate; // 对应 `通知时间`

    // 安检证书使用有效期至
    private Date certificateValidityEndDate; // 对应 `安检证书使用有效期至`

    // 发证日期
    private Date issueDate; // 对应 `发证日期`

    // 船只检验情况记录
    private String inspectionRecord; // 对应 `船只检验情况记录`


    public ShipSecurityCertificate(String shipName, String shipInspectionRegistrationNumber, String securityCertificateNumber, String shipOwner, String shipRegistrationNumber, String inspectionAuthority, Date nextInspectionDate, Date notificationDate, Date certificateValidityEndDate, Date issueDate, String inspectionRecord) {
        this.shipName = shipName;
        this.shipInspectionRegistrationNumber = shipInspectionRegistrationNumber;
        this.securityCertificateNumber = securityCertificateNumber;
        this.shipOwner = shipOwner;
        this.shipRegistrationNumber = shipRegistrationNumber;
        this.inspectionAuthority = inspectionAuthority;
        this.nextInspectionDate = nextInspectionDate;
        this.notificationDate = notificationDate;
        this.certificateValidityEndDate = certificateValidityEndDate;
        this.issueDate = issueDate;
        this.inspectionRecord = inspectionRecord;
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

    public String getSecurityCertificateNumber() {
        return securityCertificateNumber;
    }

    public void setSecurityCertificateNumber(String securityCertificateNumber) {
        this.securityCertificateNumber = securityCertificateNumber;
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

    public String getInspectionAuthority() {
        return inspectionAuthority;
    }

    public void setInspectionAuthority(String inspectionAuthority) {
        this.inspectionAuthority = inspectionAuthority;
    }

    public Date getNextInspectionDate() {
        return nextInspectionDate;
    }

    public void setNextInspectionDate(Date nextInspectionDate) {
        this.nextInspectionDate = nextInspectionDate;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
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

    public String getInspectionRecord() {
        return inspectionRecord;
    }

    public void setInspectionRecord(String inspectionRecord) {
        this.inspectionRecord = inspectionRecord;
    }


}
