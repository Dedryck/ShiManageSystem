package com.zqu.UserQueryModule;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-15:39
 * @description:
 */
public class CertificateInfo {
    // 共有字段
    private String shipName; // 船名
    private String shipRegistrationNumber; // 船检登记号
    private String shipOwner; // 船舶所有人
    private String shipRegisterNumber; // 船舶登记号

    // 国籍配员证书字段
    private String nationalityCrewCertificateNumber; // 国籍配员证书编号
    private String nextCrewChangeDate; // 国籍配员下次换证时间
    private String crewChangeNoticeDate; // 国籍配员换证通知时间
    private String crewCertificateValidity; // 国籍配员证书使用有效期至
    private String crewCertificateIssueDate; // 国籍配员发证日期
    private String crewCertificateChangeRecord; // 国籍配员证书换证时间记录

    // 船只安检证书字段
    private String safetyInspectionCertificateNumber; // 安检证书编号
    private String inspectionAuthority; // 检查机关
    private String nextInspectionDate; // 下次检查时间
    private String safetyInspectionNoticeDate; // 安检通知时间
    private String safetyCertificateValidity; // 安检证书有效期至
    private String safetyCertificateIssueDate; // 安检证书发证日期
    private String shipInspectionRecord; // 安检情况记录

    // 船只检验证书字段
    private String inspectionCertificateNumber; // 检验证编号
    private String inspectionType; // 船舶检验类型
    private String nextInspectionCertificateDate; // 检验证下次检验时间
    private String inspectionCertificateNoticeDate; // 检验证通知时间
    private String inspectionAgency; // 检验证检验机关
    private String inspectionCertificateValidity; // 检验证使用有效期至
    private String inspectionCertificateIssueDate; // 检验证发证日期
    private String inspectionSituationRecord; // 船只检验情况记录

    // 船只营运证书字段
    private String operationCertificateNumber; // 营运证编号
    private String operatorLicenseNumber; // 经营人许可证号码
    private String managerLicenseNumber; // 管理人许可证号码
    private String operationCertificateIssueAuthority; // 发证机关
    private String operationCertificateValidity; // 营运证使用有效期至
    private String operationCertificateIssueDate; // 营运证发证日期

    public CertificateInfo(String shipName, String shipRegistrationNumber, String shipOwner, String shipRegisterNumber, String nationalityCrewCertificateNumber, String nextCrewChangeDate, String crewChangeNoticeDate, String crewCertificateValidity, String crewCertificateIssueDate, String crewCertificateChangeRecord, String safetyInspectionCertificateNumber, String inspectionAuthority, String nextInspectionDate, String safetyInspectionNoticeDate, String safetyCertificateValidity, String safetyCertificateIssueDate, String shipInspectionRecord, String inspectionCertificateNumber, String inspectionType, String nextInspectionCertificateDate, String inspectionCertificateNoticeDate, String inspectionAgency, String inspectionCertificateValidity, String inspectionCertificateIssueDate, String inspectionSituationRecord, String operationCertificateNumber, String operatorLicenseNumber, String managerLicenseNumber, String operationCertificateIssueAuthority, String operationCertificateValidity, String operationCertificateIssueDate) {
        this.shipName = shipName;
        this.shipRegistrationNumber = shipRegistrationNumber;
        this.shipOwner = shipOwner;
        this.shipRegisterNumber = shipRegisterNumber;
        this.nationalityCrewCertificateNumber = nationalityCrewCertificateNumber;
        this.nextCrewChangeDate = nextCrewChangeDate;
        this.crewChangeNoticeDate = crewChangeNoticeDate;
        this.crewCertificateValidity = crewCertificateValidity;
        this.crewCertificateIssueDate = crewCertificateIssueDate;
        this.crewCertificateChangeRecord = crewCertificateChangeRecord;
        this.safetyInspectionCertificateNumber = safetyInspectionCertificateNumber;
        this.inspectionAuthority = inspectionAuthority;
        this.nextInspectionDate = nextInspectionDate;
        this.safetyInspectionNoticeDate = safetyInspectionNoticeDate;
        this.safetyCertificateValidity = safetyCertificateValidity;
        this.safetyCertificateIssueDate = safetyCertificateIssueDate;
        this.shipInspectionRecord = shipInspectionRecord;
        this.inspectionCertificateNumber = inspectionCertificateNumber;
        this.inspectionType = inspectionType;
        this.nextInspectionCertificateDate = nextInspectionCertificateDate;
        this.inspectionCertificateNoticeDate = inspectionCertificateNoticeDate;
        this.inspectionAgency = inspectionAgency;
        this.inspectionCertificateValidity = inspectionCertificateValidity;
        this.inspectionCertificateIssueDate = inspectionCertificateIssueDate;
        this.inspectionSituationRecord = inspectionSituationRecord;
        this.operationCertificateNumber = operationCertificateNumber;
        this.operatorLicenseNumber = operatorLicenseNumber;
        this.managerLicenseNumber = managerLicenseNumber;
        this.operationCertificateIssueAuthority = operationCertificateIssueAuthority;
        this.operationCertificateValidity = operationCertificateValidity;
        this.operationCertificateIssueDate = operationCertificateIssueDate;
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

    public String getShipOwner() {
        return shipOwner;
    }

    public void setShipOwner(String shipOwner) {
        this.shipOwner = shipOwner;
    }

    public String getShipRegisterNumber() {
        return shipRegisterNumber;
    }

    public void setShipRegisterNumber(String shipRegisterNumber) {
        this.shipRegisterNumber = shipRegisterNumber;
    }

    public String getNationalityCrewCertificateNumber() {
        return nationalityCrewCertificateNumber;
    }

    public void setNationalityCrewCertificateNumber(String nationalityCrewCertificateNumber) {
        this.nationalityCrewCertificateNumber = nationalityCrewCertificateNumber;
    }

    public String getNextCrewChangeDate() {
        return nextCrewChangeDate;
    }

    public void setNextCrewChangeDate(String nextCrewChangeDate) {
        this.nextCrewChangeDate = nextCrewChangeDate;
    }

    public String getCrewChangeNoticeDate() {
        return crewChangeNoticeDate;
    }

    public void setCrewChangeNoticeDate(String crewChangeNoticeDate) {
        this.crewChangeNoticeDate = crewChangeNoticeDate;
    }

    public String getCrewCertificateValidity() {
        return crewCertificateValidity;
    }

    public void setCrewCertificateValidity(String crewCertificateValidity) {
        this.crewCertificateValidity = crewCertificateValidity;
    }

    public String getCrewCertificateIssueDate() {
        return crewCertificateIssueDate;
    }

    public void setCrewCertificateIssueDate(String crewCertificateIssueDate) {
        this.crewCertificateIssueDate = crewCertificateIssueDate;
    }

    public String getCrewCertificateChangeRecord() {
        return crewCertificateChangeRecord;
    }

    public void setCrewCertificateChangeRecord(String crewCertificateChangeRecord) {
        this.crewCertificateChangeRecord = crewCertificateChangeRecord;
    }

    public String getSafetyInspectionCertificateNumber() {
        return safetyInspectionCertificateNumber;
    }

    public void setSafetyInspectionCertificateNumber(String safetyInspectionCertificateNumber) {
        this.safetyInspectionCertificateNumber = safetyInspectionCertificateNumber;
    }

    public String getInspectionAuthority() {
        return inspectionAuthority;
    }

    public void setInspectionAuthority(String inspectionAuthority) {
        this.inspectionAuthority = inspectionAuthority;
    }

    public String getNextInspectionDate() {
        return nextInspectionDate;
    }

    public void setNextInspectionDate(String nextInspectionDate) {
        this.nextInspectionDate = nextInspectionDate;
    }

    public String getSafetyInspectionNoticeDate() {
        return safetyInspectionNoticeDate;
    }

    public void setSafetyInspectionNoticeDate(String safetyInspectionNoticeDate) {
        this.safetyInspectionNoticeDate = safetyInspectionNoticeDate;
    }

    public String getSafetyCertificateValidity() {
        return safetyCertificateValidity;
    }

    public void setSafetyCertificateValidity(String safetyCertificateValidity) {
        this.safetyCertificateValidity = safetyCertificateValidity;
    }

    public String getSafetyCertificateIssueDate() {
        return safetyCertificateIssueDate;
    }

    public void setSafetyCertificateIssueDate(String safetyCertificateIssueDate) {
        this.safetyCertificateIssueDate = safetyCertificateIssueDate;
    }

    public String getShipInspectionRecord() {
        return shipInspectionRecord;
    }

    public void setShipInspectionRecord(String shipInspectionRecord) {
        this.shipInspectionRecord = shipInspectionRecord;
    }

    public String getInspectionCertificateNumber() {
        return inspectionCertificateNumber;
    }

    public void setInspectionCertificateNumber(String inspectionCertificateNumber) {
        this.inspectionCertificateNumber = inspectionCertificateNumber;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getNextInspectionCertificateDate() {
        return nextInspectionCertificateDate;
    }

    public void setNextInspectionCertificateDate(String nextInspectionCertificateDate) {
        this.nextInspectionCertificateDate = nextInspectionCertificateDate;
    }

    public String getInspectionCertificateNoticeDate() {
        return inspectionCertificateNoticeDate;
    }

    public void setInspectionCertificateNoticeDate(String inspectionCertificateNoticeDate) {
        this.inspectionCertificateNoticeDate = inspectionCertificateNoticeDate;
    }

    public String getInspectionAgency() {
        return inspectionAgency;
    }

    public void setInspectionAgency(String inspectionAgency) {
        this.inspectionAgency = inspectionAgency;
    }

    public String getInspectionCertificateValidity() {
        return inspectionCertificateValidity;
    }

    public void setInspectionCertificateValidity(String inspectionCertificateValidity) {
        this.inspectionCertificateValidity = inspectionCertificateValidity;
    }

    public String getInspectionCertificateIssueDate() {
        return inspectionCertificateIssueDate;
    }

    public void setInspectionCertificateIssueDate(String inspectionCertificateIssueDate) {
        this.inspectionCertificateIssueDate = inspectionCertificateIssueDate;
    }

    public String getInspectionSituationRecord() {
        return inspectionSituationRecord;
    }

    public void setInspectionSituationRecord(String inspectionSituationRecord) {
        this.inspectionSituationRecord = inspectionSituationRecord;
    }

    public String getOperationCertificateNumber() {
        return operationCertificateNumber;
    }

    public void setOperationCertificateNumber(String operationCertificateNumber) {
        this.operationCertificateNumber = operationCertificateNumber;
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

    public String getOperationCertificateIssueAuthority() {
        return operationCertificateIssueAuthority;
    }

    public void setOperationCertificateIssueAuthority(String operationCertificateIssueAuthority) {
        this.operationCertificateIssueAuthority = operationCertificateIssueAuthority;
    }

    public String getOperationCertificateValidity() {
        return operationCertificateValidity;
    }

    public void setOperationCertificateValidity(String operationCertificateValidity) {
        this.operationCertificateValidity = operationCertificateValidity;
    }

    public String getOperationCertificateIssueDate() {
        return operationCertificateIssueDate;
    }

    public void setOperationCertificateIssueDate(String operationCertificateIssueDate) {
        this.operationCertificateIssueDate = operationCertificateIssueDate;
    }
}

