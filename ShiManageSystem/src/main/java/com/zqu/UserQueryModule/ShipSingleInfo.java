package com.zqu.UserQueryModule;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-21:45
 * @description:
 */
public class ShipSingleInfo {

    // 船名
    private String shipName;

    // 船主姓名
    private String ownerName;

    // 身份证号
    private String ownerId;

    // 联系电话
    private String contactNumber;

    // 详细住址
    private String address;

    // 船舶类型
    private String shipType;

    // 船籍港
    private String portOfRegistry;

    // 建造厂
    private String shipyard;

    // 总长
    private double length;

    // 型宽
    private double width;

    // 型深
    private double depth;

    // 总吨
    private double grossTonnage;

    // 净吨
    private double netTonnage;

    // 主机功率
    private double mainEnginePower;

    // 载重吨
    private double deadweightTonnage;

    // 航行区域
    private String navigationArea;

    // 备注
    private String remarks;

    // 船检登记号
    private String shipInspectionRegistrationNumber;

    // 营运证编号
    private String operationCertificateNumber;

    // 发证机关
    private String issuingAuthority;

    // 检验证编号
    private String inspectionCertificateNumber;

    // 船舶检验类型
    private String shipInspectionType;

    // 安检证书编号
    private String safetyInspectionCertificateNumber;

    // 检查机关
    private String inspectionAgency;

    // 国籍配员证书编号
    private String nationalityCrewCertificateNumber;

    // 航道费
    private double channelFee;

    // 航道费合计
    private double totalChannelFee;

    // 水运费合计
    private double totalWaterTransportFee;

    // 水运费缴纳记录
    private String waterTransportFeePaymentRecord;

    public ShipSingleInfo(String shipName, String ownerName, String ownerId, String contactNumber, String address, String shipType, String portOfRegistry, String shipyard, double length, double width, double depth, double grossTonnage, double netTonnage, double mainEnginePower, double deadweightTonnage, String navigationArea, String remarks, String shipInspectionRegistrationNumber, String operationCertificateNumber, String issuingAuthority, String inspectionCertificateNumber, String shipInspectionType, String safetyInspectionCertificateNumber, String inspectionAgency, String nationalityCrewCertificateNumber, double channelFee, double totalChannelFee, double totalWaterTransportFee, String waterTransportFeePaymentRecord) {
        this.shipName = shipName;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.contactNumber = contactNumber;
        this.address = address;
        this.shipType = shipType;
        this.portOfRegistry = portOfRegistry;
        this.shipyard = shipyard;
        this.length = length;
        this.width = width;
        this.depth = depth;
        this.grossTonnage = grossTonnage;
        this.netTonnage = netTonnage;
        this.mainEnginePower = mainEnginePower;
        this.deadweightTonnage = deadweightTonnage;
        this.navigationArea = navigationArea;
        this.remarks = remarks;
        this.shipInspectionRegistrationNumber = shipInspectionRegistrationNumber;
        this.operationCertificateNumber = operationCertificateNumber;
        this.issuingAuthority = issuingAuthority;
        this.inspectionCertificateNumber = inspectionCertificateNumber;
        this.shipInspectionType = shipInspectionType;
        this.safetyInspectionCertificateNumber = safetyInspectionCertificateNumber;
        this.inspectionAgency = inspectionAgency;
        this.nationalityCrewCertificateNumber = nationalityCrewCertificateNumber;
        this.channelFee = channelFee;
        this.totalChannelFee = totalChannelFee;
        this.totalWaterTransportFee = totalWaterTransportFee;
        this.waterTransportFeePaymentRecord = waterTransportFeePaymentRecord;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getPortOfRegistry() {
        return portOfRegistry;
    }

    public void setPortOfRegistry(String portOfRegistry) {
        this.portOfRegistry = portOfRegistry;
    }

    public String getShipyard() {
        return shipyard;
    }

    public void setShipyard(String shipyard) {
        this.shipyard = shipyard;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getGrossTonnage() {
        return grossTonnage;
    }

    public void setGrossTonnage(double grossTonnage) {
        this.grossTonnage = grossTonnage;
    }

    public double getNetTonnage() {
        return netTonnage;
    }

    public void setNetTonnage(double netTonnage) {
        this.netTonnage = netTonnage;
    }

    public double getMainEnginePower() {
        return mainEnginePower;
    }

    public void setMainEnginePower(double mainEnginePower) {
        this.mainEnginePower = mainEnginePower;
    }

    public double getDeadweightTonnage() {
        return deadweightTonnage;
    }

    public void setDeadweightTonnage(double deadweightTonnage) {
        this.deadweightTonnage = deadweightTonnage;
    }

    public String getNavigationArea() {
        return navigationArea;
    }

    public void setNavigationArea(String navigationArea) {
        this.navigationArea = navigationArea;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getShipInspectionRegistrationNumber() {
        return shipInspectionRegistrationNumber;
    }

    public void setShipInspectionRegistrationNumber(String shipInspectionRegistrationNumber) {
        this.shipInspectionRegistrationNumber = shipInspectionRegistrationNumber;
    }

    public String getOperationCertificateNumber() {
        return operationCertificateNumber;
    }

    public void setOperationCertificateNumber(String operationCertificateNumber) {
        this.operationCertificateNumber = operationCertificateNumber;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getInspectionCertificateNumber() {
        return inspectionCertificateNumber;
    }

    public void setInspectionCertificateNumber(String inspectionCertificateNumber) {
        this.inspectionCertificateNumber = inspectionCertificateNumber;
    }

    public String getShipInspectionType() {
        return shipInspectionType;
    }

    public void setShipInspectionType(String shipInspectionType) {
        this.shipInspectionType = shipInspectionType;
    }

    public String getSafetyInspectionCertificateNumber() {
        return safetyInspectionCertificateNumber;
    }

    public void setSafetyInspectionCertificateNumber(String safetyInspectionCertificateNumber) {
        this.safetyInspectionCertificateNumber = safetyInspectionCertificateNumber;
    }

    public String getInspectionAgency() {
        return inspectionAgency;
    }

    public void setInspectionAgency(String inspectionAgency) {
        this.inspectionAgency = inspectionAgency;
    }

    public String getNationalityCrewCertificateNumber() {
        return nationalityCrewCertificateNumber;
    }

    public void setNationalityCrewCertificateNumber(String nationalityCrewCertificateNumber) {
        this.nationalityCrewCertificateNumber = nationalityCrewCertificateNumber;
    }

    public double getChannelFee() {
        return channelFee;
    }

    public void setChannelFee(double channelFee) {
        this.channelFee = channelFee;
    }

    public double getTotalChannelFee() {
        return totalChannelFee;
    }

    public void setTotalChannelFee(double totalChannelFee) {
        this.totalChannelFee = totalChannelFee;
    }

    public double getTotalWaterTransportFee() {
        return totalWaterTransportFee;
    }

    public void setTotalWaterTransportFee(double totalWaterTransportFee) {
        this.totalWaterTransportFee = totalWaterTransportFee;
    }

    public String getWaterTransportFeePaymentRecord() {
        return waterTransportFeePaymentRecord;
    }

    public void setWaterTransportFeePaymentRecord(String waterTransportFeePaymentRecord) {
        this.waterTransportFeePaymentRecord = waterTransportFeePaymentRecord;
    }
}

