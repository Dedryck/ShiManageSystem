package com.zqu.UserQueryModule;

import java.util.Date;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-20:22
 * @description: 证书有效期联查
 */
public class EffectiveCertificateInfo {

    //    船名
    private String shipName;

    // 中间检验办理日期
    private Date intermediateInspectionHandlingDate;
    // 中间检验证书有效期至
    private Date intermediateInspectionCertificateExpiryDate;

    // 危险品办理日期
    private Date dangerousGoodsHandlingDate;
    // 危险品证书有效期至
    private Date dangerousGoodsCertificateExpiryDate;

    // 各证书有效期至
    private Date variousCertificatesExpiryDate;

    // 国籍证书办理日期
    private Date nationalityCertificateHandlingDate;
    // 国籍证书有效期至
    private Date nationalityCertificateExpiryDate;

    // 坞内检验办理日期
    private Date dockyardInspectionHandlingDate;
    // 坞内检验证书有效期至
    private Date dockyardInspectionCertificateExpiryDate;

    // 油污证书办理日期
    private Date oilPollutionHandlingDate;
    // 油污证书有效期至
    private Date oilPollutionCertificateExpiryDate;

    // 港澳证明办理日期
    private Date hongKongMacaoHandlingDate;
    // 港澳证明有效期至
    private Date hongKongMacaoCertificateExpiryDate;

    public EffectiveCertificateInfo(String shipName, Date intermediateInspectionHandlingDate, Date intermediateInspectionCertificateExpiryDate, Date dangerousGoodsHandlingDate, Date dangerousGoodsCertificateExpiryDate, Date variousCertificatesExpiryDate, Date nationalityCertificateHandlingDate, Date nationalityCertificateExpiryDate, Date dockyardInspectionHandlingDate, Date dockyardInspectionCertificateExpiryDate, Date oilPollutionHandlingDate, Date oilPollutionCertificateExpiryDate, Date hongKongMacaoHandlingDate, Date hongKongMacaoCertificateExpiryDate) {
        this.shipName = shipName;
        this.intermediateInspectionHandlingDate = intermediateInspectionHandlingDate;
        this.intermediateInspectionCertificateExpiryDate = intermediateInspectionCertificateExpiryDate;
        this.dangerousGoodsHandlingDate = dangerousGoodsHandlingDate;
        this.dangerousGoodsCertificateExpiryDate = dangerousGoodsCertificateExpiryDate;
        this.variousCertificatesExpiryDate = variousCertificatesExpiryDate;
        this.nationalityCertificateHandlingDate = nationalityCertificateHandlingDate;
        this.nationalityCertificateExpiryDate = nationalityCertificateExpiryDate;
        this.dockyardInspectionHandlingDate = dockyardInspectionHandlingDate;
        this.dockyardInspectionCertificateExpiryDate = dockyardInspectionCertificateExpiryDate;
        this.oilPollutionHandlingDate = oilPollutionHandlingDate;
        this.oilPollutionCertificateExpiryDate = oilPollutionCertificateExpiryDate;
        this.hongKongMacaoHandlingDate = hongKongMacaoHandlingDate;
        this.hongKongMacaoCertificateExpiryDate = hongKongMacaoCertificateExpiryDate;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public Date getIntermediateInspectionHandlingDate() {
        return intermediateInspectionHandlingDate;
    }

    public void setIntermediateInspectionHandlingDate(Date intermediateInspectionHandlingDate) {
        this.intermediateInspectionHandlingDate = intermediateInspectionHandlingDate;
    }

    public Date getIntermediateInspectionCertificateExpiryDate() {
        return intermediateInspectionCertificateExpiryDate;
    }

    public void setIntermediateInspectionCertificateExpiryDate(Date intermediateInspectionCertificateExpiryDate) {
        this.intermediateInspectionCertificateExpiryDate = intermediateInspectionCertificateExpiryDate;
    }

    public Date getDangerousGoodsHandlingDate() {
        return dangerousGoodsHandlingDate;
    }

    public void setDangerousGoodsHandlingDate(Date dangerousGoodsHandlingDate) {
        this.dangerousGoodsHandlingDate = dangerousGoodsHandlingDate;
    }

    public Date getDangerousGoodsCertificateExpiryDate() {
        return dangerousGoodsCertificateExpiryDate;
    }

    public void setDangerousGoodsCertificateExpiryDate(Date dangerousGoodsCertificateExpiryDate) {
        this.dangerousGoodsCertificateExpiryDate = dangerousGoodsCertificateExpiryDate;
    }

    public Date getVariousCertificatesExpiryDate() {
        return variousCertificatesExpiryDate;
    }

    public void setVariousCertificatesExpiryDate(Date variousCertificatesExpiryDate) {
        this.variousCertificatesExpiryDate = variousCertificatesExpiryDate;
    }

    public Date getNationalityCertificateHandlingDate() {
        return nationalityCertificateHandlingDate;
    }

    public void setNationalityCertificateHandlingDate(Date nationalityCertificateHandlingDate) {
        this.nationalityCertificateHandlingDate = nationalityCertificateHandlingDate;
    }

    public Date getNationalityCertificateExpiryDate() {
        return nationalityCertificateExpiryDate;
    }

    public void setNationalityCertificateExpiryDate(Date nationalityCertificateExpiryDate) {
        this.nationalityCertificateExpiryDate = nationalityCertificateExpiryDate;
    }

    public Date getDockyardInspectionHandlingDate() {
        return dockyardInspectionHandlingDate;
    }

    public void setDockyardInspectionHandlingDate(Date dockyardInspectionHandlingDate) {
        this.dockyardInspectionHandlingDate = dockyardInspectionHandlingDate;
    }

    public Date getDockyardInspectionCertificateExpiryDate() {
        return dockyardInspectionCertificateExpiryDate;
    }

    public void setDockyardInspectionCertificateExpiryDate(Date dockyardInspectionCertificateExpiryDate) {
        this.dockyardInspectionCertificateExpiryDate = dockyardInspectionCertificateExpiryDate;
    }

    public Date getOilPollutionHandlingDate() {
        return oilPollutionHandlingDate;
    }

    public void setOilPollutionHandlingDate(Date oilPollutionHandlingDate) {
        this.oilPollutionHandlingDate = oilPollutionHandlingDate;
    }

    public Date getOilPollutionCertificateExpiryDate() {
        return oilPollutionCertificateExpiryDate;
    }

    public void setOilPollutionCertificateExpiryDate(Date oilPollutionCertificateExpiryDate) {
        this.oilPollutionCertificateExpiryDate = oilPollutionCertificateExpiryDate;
    }

    public Date getHongKongMacaoHandlingDate() {
        return hongKongMacaoHandlingDate;
    }

    public void setHongKongMacaoHandlingDate(Date hongKongMacaoHandlingDate) {
        this.hongKongMacaoHandlingDate = hongKongMacaoHandlingDate;
    }

    public Date getHongKongMacaoCertificateExpiryDate() {
        return hongKongMacaoCertificateExpiryDate;
    }

    public void setHongKongMacaoCertificateExpiryDate(Date hongKongMacaoCertificateExpiryDate) {
        this.hongKongMacaoCertificateExpiryDate = hongKongMacaoCertificateExpiryDate;
    }


}
