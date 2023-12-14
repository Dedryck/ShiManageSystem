package com.zqu;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @aurhor Dedryck
 * @create 2023-11-29-11:00
 * @description:
 */
public class OperationRecord {
    private StringProperty operationId = new SimpleStringProperty();
    private StringProperty userId = new SimpleStringProperty();
    private StringProperty operationType = new SimpleStringProperty();
    private StringProperty operationTime = new SimpleStringProperty();
    private StringProperty operationDetail = new SimpleStringProperty();

    public String getOperationId() {
        return operationId.get();
    }

    public StringProperty operationIdProperty() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId.set(operationId);
    }

    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public String getOperationType() {
        return operationType.get();
    }

    public StringProperty operationTypeProperty() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType.set(operationType);
    }

    public String getOperationTime() {
        return operationTime.get();
    }

    public StringProperty operationTimeProperty() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime.set(operationTime);
    }

    public String getOperationDetail() {
        return operationDetail.get();
    }

    public StringProperty operationDetailProperty() {
        return operationDetail;
    }

    public void setOperationDetail(String operationDetail) {
        this.operationDetail.set(operationDetail);
    }
}
