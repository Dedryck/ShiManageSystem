package com.zqu.AdministratorModule;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

/**
 * @aurhor Dedryck
 * @create 2023-12-01-19:20
 * @description:用于提取相应的数据
 */
public class Boat {
    private Integer id; // 序号
    private String ownerName; // 船主姓名
    private String identityCard; // 身份证号
    private String contactNumber; // 联系电话
    private String address; // 详细住址
    private String boatName; // 船名
    private String boatType; // 船舶类型
    private String hullMaterial; // 船体材料
    private String engineType; // 机型
    private String portOfRegistry; // 船籍港
    private String builder; // 建造厂
    private String registrationNumber; // 船舶登记号
    private String operationCertificateNumber; // 营运证号
    private Date entryDate; // 入户时间
    private Date exitDate; // 迁出时间
    private Date completionDate; // 建成时间
    private BigDecimal length; // 总长
    private BigDecimal width; // 型宽
    private BigDecimal depth; // 型深
    private BigDecimal grossTonnage; // 总吨
    private BigDecimal netTonnage; // 净吨
    private BigDecimal mainEnginePower; // 主机功率
    private BigDecimal carryingCapacity; // 载重吨
    private String navigationArea; // 航行区域
    private String notes; // 备注
    private Blob photo; // 船舶相片
    private String registrationInspectionNumber; // 船检登记号


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getBoatType() {
        return boatType;
    }

    public void setBoatType(String boatType) {
        this.boatType = boatType;
    }

    public String getHullMaterial() {
        return hullMaterial;
    }

    public void setHullMaterial(String hullMaterial) {
        this.hullMaterial = hullMaterial;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getPortOfRegistry() {
        return portOfRegistry;
    }

    public void setPortOfRegistry(String portOfRegistry) {
        this.portOfRegistry = portOfRegistry;
    }

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getOperationCertificateNumber() {
        return operationCertificateNumber;
    }

    public void setOperationCertificateNumber(String operationCertificateNumber) {
        this.operationCertificateNumber = operationCertificateNumber;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getGrossTonnage() {
        return grossTonnage;
    }

    public void setGrossTonnage(BigDecimal grossTonnage) {
        this.grossTonnage = grossTonnage;
    }

    public BigDecimal getNetTonnage() {
        return netTonnage;
    }

    public void setNetTonnage(BigDecimal netTonnage) {
        this.netTonnage = netTonnage;
    }

    public BigDecimal getMainEnginePower() {
        return mainEnginePower;
    }

    public void setMainEnginePower(BigDecimal mainEnginePower) {
        this.mainEnginePower = mainEnginePower;
    }

    public BigDecimal getCarryingCapacity() {
        return carryingCapacity;
    }

    public void setCarryingCapacity(BigDecimal carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }

    public String getNavigationArea() {
        return navigationArea;
    }

    public void setNavigationArea(String navigationArea) {
        this.navigationArea = navigationArea;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public String getRegistrationInspectionNumber() {
        return registrationInspectionNumber;
    }

    public void setRegistrationInspectionNumber(String registrationInspectionNumber) {
        this.registrationInspectionNumber = registrationInspectionNumber;
    }
}
