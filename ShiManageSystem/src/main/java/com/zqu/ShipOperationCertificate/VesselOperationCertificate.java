package com.zqu.ShipOperationCertificate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

/**
 * @aurhor Dedryck
 * @create 2023-12-03-11:26
 * @description:获取船只运营证书基本表里面有！！十个属性！！
 */
public class VesselOperationCertificate {
    private String vesselName;
    private String registrationNumber;
    private String certificateNumber;
    private String owner;
    private String vesselRegistrationNumber;
    private String operatorLicenseNumber;
    private String managerLicenseNumber;
    private String issuingAuthority;
    private LocalDate validityDate;
    private LocalDate issueDate;

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

    public String getOperatorLicenseNumber() {
        return operatorLicenseNumber;
    }

    public void setOperatorLicenseNumber(String operatorLicenseNumber) {
        this.operatorLicenseNumber = operatorLicenseNumber;
    }

    public String getManagerLicenseNumber() {
        return managerLicenseNumber;
    }

    public void setManagerLicenseNumber(String managerLicenseNumber) {
        this.managerLicenseNumber = managerLicenseNumber;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public VesselOperationCertificate(String vesselName, String registrationNumber, String certificateNumber, String owner, String vesselRegistrationNumber, String operatorLicenseNumber, String managerLicenseNumber, String issuingAuthority, LocalDate validityDate, LocalDate issueDate) {
        this.vesselName = vesselName;
        this.registrationNumber = registrationNumber;
        this.certificateNumber = certificateNumber;
        this.owner = owner;
        this.vesselRegistrationNumber = vesselRegistrationNumber;
        this.operatorLicenseNumber = operatorLicenseNumber;
        this.managerLicenseNumber = managerLicenseNumber;
        this.issuingAuthority = issuingAuthority;
        this.validityDate = validityDate;
        this.issueDate = issueDate;
    }
}
