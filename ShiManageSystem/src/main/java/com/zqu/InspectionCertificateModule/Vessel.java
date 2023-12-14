package com.zqu.InspectionCertificateModule;

import java.sql.Date;

/**
 * @aurhor Dedryck
 * @create 2023-12-06-9:37
 * @description:
 */
public class Vessel {
    private String vesselName;
    private String registrationNumber;
    private String certificateNumber;
    private String owner;
    private String vesselRegistrationNumber;
    private String inspectionType;
    private java.sql.Date nextInspectionDate;
    private java.sql.Date notificationDate;
    private String inspectionAuthority;
    private java.sql.Date certificateValidity;
    private java.sql.Date issueDate;
    private String inspectionRecord;





    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVesselRegistrationNumber() {
        return vesselRegistrationNumber;
    }

    public void setVesselRegistrationNumber(String vesselRegistrationNumber) {
        this.vesselRegistrationNumber = vesselRegistrationNumber;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
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

    public String getInspectionAuthority() {
        return inspectionAuthority;
    }

    public void setInspectionAuthority(String inspectionAuthority) {
        this.inspectionAuthority = inspectionAuthority;
    }

    public Date getCertificateValidity() {
        return certificateValidity;
    }

    public void setCertificateValidity(Date certificateValidity) {
        this.certificateValidity = certificateValidity;
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

    public Vessel(String vesselName, String registrationNumber, String certificateNumber, String owner, String vesselRegistrationNumber, String inspectionType, Date nextInspectionDate, Date notificationDate, String inspectionAuthority, Date certificateValidity, Date issueDate, String inspectionRecord) {
        this.vesselName = vesselName;
        this.registrationNumber = registrationNumber;
        this.certificateNumber = certificateNumber;
        this.owner = owner;
        this.vesselRegistrationNumber = vesselRegistrationNumber;
        this.inspectionType = inspectionType;
        this.nextInspectionDate = nextInspectionDate;
        this.notificationDate = notificationDate;
        this.inspectionAuthority = inspectionAuthority;
        this.certificateValidity = certificateValidity;
        this.issueDate = issueDate;
        this.inspectionRecord = inspectionRecord;
    }


}
