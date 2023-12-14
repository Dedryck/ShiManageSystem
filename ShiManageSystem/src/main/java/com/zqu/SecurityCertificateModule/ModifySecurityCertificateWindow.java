package com.zqu.SecurityCertificateModule;
import com.zqu.MysqlConnectiontest;
import com.zqu.WaterwayDuesPaymentModule.ModifyWaterwayDuesPaymentModule;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static com.zqu.MysqlConnectiontest.UpdateSecurityCertificateData;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-14:17
 * @description: 显示修改船只安检证书的窗口
 */
public class ModifySecurityCertificateWindow {

    // 定义一个回调接口
    public interface OnCertificateUpdatedListener {
        void onUpdated();
    }

    public static void show(ShipSecurityCertificate certificate, Stage parentStage, OnCertificateUpdatedListener updateListener) {

        parentStage.hide(); // 隐藏上级窗体
        Stage window = new Stage();
        window.setTitle("修改安检证书信息窗口");
        window.initModality(Modality.APPLICATION_MODAL); // 设置为模态窗口
        window.setWidth(780);
        window.setHeight(700);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER); // 设置网格在窗体中居中

        // 使用转换方法设置DatePicker的值
        // 创建DatePicker控件
        DatePicker nextInspectionDatePicker = new DatePicker();
        DatePicker notificationDatePicker = new DatePicker();
        DatePicker certificateValidityEndDatePicker = new DatePicker();
        DatePicker issueDatePicker = new DatePicker();

        // 创建标签和文本框，为每个属性添加一个标签和文本框
        Label shipNameLabel = new Label("船名:");
        TextField shipNameField = new TextField(certificate.getShipName());
        shipNameField.setMaxWidth(200);

        Label shipInspectionRegistrationNumberLabel = new Label("船检登记号:");
        TextField shipInspectionRegistrationNumberField = new TextField(certificate.getShipInspectionRegistrationNumber());
        shipInspectionRegistrationNumberField.setMaxWidth(200);

        Label securityCertificateNumberLabel = new Label("安检证书编号:");
        TextField securityCertificateNumberField = new TextField(certificate.getSecurityCertificateNumber());
        securityCertificateNumberField.setMaxWidth(200);

        Label shipOwnerLabel = new Label("船舶所有人:");
        TextField shipOwnerField = new TextField(certificate.getShipOwner());
        shipOwnerField.setMaxWidth(200);

        Label shipRegistrationNumberLabel = new Label("船舶登记号:");
        TextField shipRegistrationNumberField = new TextField(certificate.getShipRegistrationNumber());
        shipRegistrationNumberField.setMaxWidth(200);

        Label inspectionAuthorityLabel = new Label("检查机关:");
        TextField inspectionAuthorityField = new TextField(certificate.getInspectionAuthority());
        inspectionAuthorityField.setMaxWidth(200);

        Label nextInspectionDateLabel = new Label("下次检查时间:");

        Label notificationDateLabel = new Label("通知时间:");

        Label certificateValidityEndDateLabel = new Label("安检证书使用有效期至:");

        Label issueDateLabel = new Label("发证日期:");

        // 使用转换方法设置DatePicker的值
        nextInspectionDatePicker.setValue(convertToLocalDate(certificate.getNextInspectionDate()));
        notificationDatePicker.setValue(convertToLocalDate(certificate.getNotificationDate()));
        certificateValidityEndDatePicker.setValue(convertToLocalDate(certificate.getCertificateValidityEndDate()));
        issueDatePicker.setValue(convertToLocalDate(certificate.getIssueDate()));

        Label inspectionRecordLabel = new Label("船只检验情况记录:");
        TextArea inspectionRecordArea = new TextArea(certificate.getInspectionRecord());
        inspectionRecordArea.setMinHeight(50);
        inspectionRecordArea.setPrefHeight(80);
        inspectionRecordArea.setPrefWidth(200);
        inspectionRecordArea.setWrapText(true);


        // 添加控件到网格中
        grid.add(shipNameLabel, 0, 0);
        grid.add(shipNameField, 1, 0);
        GridPane.setHalignment(shipNameLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(shipNameField, HPos.CENTER);


        grid.add(shipInspectionRegistrationNumberLabel, 0, 1);
        grid.add(shipInspectionRegistrationNumberField, 1, 1);
        GridPane.setHalignment(shipInspectionRegistrationNumberLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(shipInspectionRegistrationNumberField, HPos.CENTER);

        grid.add(securityCertificateNumberLabel, 0, 2);
        grid.add(securityCertificateNumberField, 1, 2);
        GridPane.setHalignment(securityCertificateNumberLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(securityCertificateNumberField, HPos.CENTER);

        grid.add(shipOwnerLabel, 0, 3);
        grid.add(shipOwnerField, 1, 3);
        GridPane.setHalignment(shipOwnerLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(shipOwnerField, HPos.CENTER);

        grid.add(shipRegistrationNumberLabel, 0, 4);
        grid.add(shipRegistrationNumberField, 1, 4);
        GridPane.setHalignment(shipRegistrationNumberLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(shipRegistrationNumberField, HPos.CENTER);

        grid.add(inspectionAuthorityLabel, 0, 5);
        grid.add(inspectionAuthorityField, 1, 5);
        GridPane.setHalignment(inspectionAuthorityLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(inspectionAuthorityField, HPos.CENTER);

        grid.add(nextInspectionDateLabel, 0, 6);
        grid.add(nextInspectionDatePicker, 1, 6);
        GridPane.setHalignment(nextInspectionDateLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(nextInspectionDatePicker, HPos.CENTER);

        grid.add(notificationDateLabel, 0, 7);
        grid.add(notificationDatePicker, 1, 7);
        GridPane.setHalignment(notificationDateLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(notificationDatePicker, HPos.CENTER);

        grid.add(certificateValidityEndDateLabel, 0, 8);
        grid.add(certificateValidityEndDatePicker, 1, 8);
        GridPane.setHalignment(certificateValidityEndDateLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(certificateValidityEndDatePicker, HPos.CENTER);

        grid.add(issueDateLabel, 0, 9);
        grid.add(issueDatePicker, 1, 9);
        GridPane.setHalignment(issueDateLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(issueDatePicker, HPos.CENTER);

        grid.add(inspectionRecordLabel, 0, 10);
        grid.add(inspectionRecordArea, 1, 10);
        GridPane.setHalignment(inspectionRecordLabel, HPos.CENTER); // 设置水平居中
        GridPane.setHalignment(inspectionRecordArea, HPos.CENTER);


        // 创建按钮
        Button modifyButton = new Button("修改信息");
        Button cancelButton = new Button("取消");



        // 修改按钮的事件处理逻辑
        modifyButton.setOnAction(e -> {
            // 1. 检查文本框内容是否为空
            if (shipNameField.getText().trim().isEmpty() ||
                    shipInspectionRegistrationNumberField.getText().trim().isEmpty() ||
                    securityCertificateNumberField.getText().trim().isEmpty() ||
                    shipOwnerField.getText().trim().isEmpty() ||
                    shipRegistrationNumberField.getText().trim().isEmpty() ||
                    inspectionAuthorityField.getText().trim().isEmpty() ||
                    inspectionRecordArea.getText().trim().isEmpty()) {

                new Alert(Alert.AlertType.WARNING, "修改内容不可为空！").showAndWait();
                return;
            }

            // 2. 检查船只基本表中是否存在指定的船名和船检登记号
            if (!MysqlConnectiontest.checkIfVesselExists(shipNameField.getText(), shipInspectionRegistrationNumberField.getText())) {
                new Alert(Alert.AlertType.ERROR, "你修改的船名和船检登记号有误").showAndWait();
                return;
            }

            // 3. 检测是否在船只安检证书基本表中已存在船名和船检登记号
            if (!MysqlConnectiontest.CheckIfExistsSecurityCertificate(shipNameField.getText(), shipInspectionRegistrationNumberField.getText())) {
                new Alert(Alert.AlertType.ERROR, "你修改的船名和船检登记号已存在船只安检证书基本表中").showAndWait();
                return;
            }

            // 弹出确认对话框
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定更新该用户的船只安检证书基本信息吗？", ButtonType.YES, ButtonType.NO);
            confirmationAlert.setTitle("确认更新");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // 检查用户的选择
            if (result.isPresent() && result.get() == ButtonType.YES) {
                // 用户确认更新
                boolean updateSuccess = MysqlConnectiontest.UpdateSecurityCertificateData(
                        shipNameField.getText(),
                        shipInspectionRegistrationNumberField.getText(),
                        securityCertificateNumberField.getText(),
                        shipOwnerField.getText(),
                        shipRegistrationNumberField.getText(),
                        inspectionAuthorityField.getText(),
                        convertToSqlDate(nextInspectionDatePicker.getValue()),
                        convertToSqlDate(notificationDatePicker.getValue()),
                        convertToSqlDate(certificateValidityEndDatePicker.getValue()),
                        convertToSqlDate(issueDatePicker.getValue()),
                        inspectionRecordArea.getText()
                );

                if (updateSuccess) {
                    new Alert(Alert.AlertType.INFORMATION, "安检证书信息已更新").showAndWait();
                    if (updateListener != null) {
                        updateListener.onUpdated(); // 调用回调
                    }
                    window.close(); // 关闭当前窗体
                    parentStage.show(); // 显示上一级窗体

                } else {
                    new Alert(Alert.AlertType.ERROR, "更新失败，请检查输入数据").showAndWait();
                }
            }
        });



        cancelButton.setOnAction(e -> {
            ModifySecurityCertificateModule.reflash();
            window.close();
            parentStage.show(); // 显示上一级窗体

        });

        grid.add(modifyButton, 0, 11); // 11 这个数字可能需要根据您的实际布局调整
        grid.add(cancelButton, 1, 11);
        GridPane.setHalignment(modifyButton,  HPos.CENTER);
        GridPane.setHalignment(cancelButton,  HPos.CENTER);

        // 设置场景和显示
        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.show();

    }

    // 辅助方法：转换 java.util.Date 到 java.time.LocalDate
    private static LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert != null) {
            return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
        }
        return null;
    }



//    // 辅助方法：转换 LocalDate 到 java.sql.Date
    private static java.sql.Date convertToSqlDate(LocalDate localDate) {
        return (localDate != null) ? java.sql.Date.valueOf(localDate) : null;
    }



}
