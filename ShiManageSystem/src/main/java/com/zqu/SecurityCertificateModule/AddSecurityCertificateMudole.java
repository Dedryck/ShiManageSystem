package com.zqu.SecurityCertificateModule;

import com.zqu.InspectionCertificateModule.Vessel;
import com.zqu.MysqlConnectiontest;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-10:17
 * @description:添加船只安检证书基本资料
 */
public class AddSecurityCertificateMudole {
    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("添加船只安检证书");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // 创建标签和文本框
        // 创建标签和文本框，并将它们添加到 GridPane
        TextField shipNameField = new TextField();
        addField(gridPane, "船名", shipNameField, 0);

        TextField shipInspectionRegistrationNumberField = new TextField();
        addField(gridPane, "船检登记号", shipInspectionRegistrationNumberField, 1);

        TextField securityCertificateNumberField = new TextField();
        addField(gridPane, "安检证书编号", securityCertificateNumberField, 2);

        TextField shipOwnerField = new TextField();
        addField(gridPane, "船舶所有人", shipOwnerField, 3);

        TextField shipRegistrationNumberField = new TextField();
        addField(gridPane, "船舶登记号", shipRegistrationNumberField, 4);

        TextField inspectionAuthorityField = new TextField();
        addField(gridPane, "检查机关", inspectionAuthorityField, 5);

        DatePicker nextInspectionDatePicker = new DatePicker();
        addField(gridPane, "下次检查时间", nextInspectionDatePicker, 6);

        DatePicker notificationDatePicker = new DatePicker();
        addField(gridPane, "通知时间", notificationDatePicker, 7);

        DatePicker certificateValidityEndDatePicker = new DatePicker();
        addField(gridPane, "安检证书有效期至", certificateValidityEndDatePicker, 8);

        DatePicker issueDatePicker = new DatePicker();
        addField(gridPane, "发证日期", issueDatePicker, 9);

        TextArea inspectionRecordArea = new TextArea();
        inspectionRecordArea.setMinHeight(50);
        inspectionRecordArea.setPrefHeight(80);
        inspectionRecordArea.setPrefWidth(200);
        inspectionRecordArea.setWrapText(true);
        addField(gridPane, "船只安检情况记录", inspectionRecordArea, 10);

        // 添加按钮
        Button addButton = new Button("添加信息");
        addButton.setStyle(buttonStyle);

        addButton.setOnAction(event -> {
            // 1. 检查所有字段是否已填写
            if (shipNameField.getText().isEmpty() ||
                    shipInspectionRegistrationNumberField.getText().isEmpty() ||
                    securityCertificateNumberField.getText().isEmpty() ||
                    shipOwnerField.getText().isEmpty() ||
                    shipRegistrationNumberField.getText().isEmpty() ||
                    inspectionAuthorityField.getText().isEmpty() ||
                    nextInspectionDatePicker.getValue() == null ||
                    notificationDatePicker.getValue() == null ||
                    certificateValidityEndDatePicker.getValue() == null ||
                    issueDatePicker.getValue() == null ||
                    inspectionRecordArea.getText().isEmpty()) {

                showAlert(Alert.AlertType.ERROR, stage, "数据错误", "请输入完整的信息！");
                return;
            }

            // 2. 检查船只基本表中是否存在“船名”和“船检登记号”
            if (!MysqlConnectiontest.checkIfVesselExists(shipNameField.getText(), shipInspectionRegistrationNumberField.getText())) {
                showAlert(Alert.AlertType.ERROR, stage, "数据错误", "不存在该船名和船检登记号！");
                return;
            }

            // 3. 检查安检证书基本表中是否已存在相应的船名和船检登记号
            if (MysqlConnectiontest.CheckIfExistsSecurityCertificate(shipNameField.getText(), shipInspectionRegistrationNumberField.getText())) {
                showAlert(Alert.AlertType.ERROR, stage, "数据错误", "该船名和船检登记号的安检证书已存在！");
                return;
            }

            // 弹出确认对话框
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要添加该用户的安检证书信息吗？", ButtonType.YES, ButtonType.NO);
            confirmAlert.initOwner(stage);
            Optional<ButtonType> result = confirmAlert.showAndWait();

            // 如果用户选择“否”，则返回
            if (result.isEmpty() || result.get() != ButtonType.YES) {
                return;
            }

            // 4. 读取文本框内的数据并添加操作
            // 将日期从 LocalDate 转换为 Date
            Date nextInspectionDate = java.sql.Date.valueOf(nextInspectionDatePicker.getValue());
            Date notificationDate = java.sql.Date.valueOf(notificationDatePicker.getValue());
            Date certificateValidityEndDate = java.sql.Date.valueOf(certificateValidityEndDatePicker.getValue());
            Date issueDate = java.sql.Date.valueOf(issueDatePicker.getValue());

            // 创建一个 ShipSecurityCertificate 对象
            ShipSecurityCertificate certificate = new ShipSecurityCertificate(
                    shipNameField.getText(),
                    shipInspectionRegistrationNumberField.getText(),
                    securityCertificateNumberField.getText(),
                    shipOwnerField.getText(),
                    shipRegistrationNumberField.getText(),
                    inspectionAuthorityField.getText(),
                    nextInspectionDate,
                    notificationDate,
                    certificateValidityEndDate,
                    issueDate,
                    inspectionRecordArea.getText()
            );

            // 调用方法添加到数据库
            MysqlConnectiontest.addShipSecurityCertificate(certificate);
            showAlert(Alert.AlertType.INFORMATION, stage, "操作成功", "安检证书信息添加成功！");
            // 清空文本框和日期选择器
            shipNameField.clear();
            shipInspectionRegistrationNumberField.clear();
            securityCertificateNumberField.clear();
            shipOwnerField.clear();
            shipRegistrationNumberField.clear();
            inspectionAuthorityField.clear();
            nextInspectionDatePicker.setValue(null);
            notificationDatePicker.setValue(null);
            certificateValidityEndDatePicker.setValue(null);
            issueDatePicker.setValue(null);
            inspectionRecordArea.clear();
        });

        Button cancelButton = new Button("取消");
        cancelButton.setStyle(buttonStyle);

        cancelButton.setOnAction(event -> {
            stage.close(); // 关闭当前窗口
            parentStage.show(); // 显示父窗口
        });

        gridPane.add(addButton, 0, 11);
        gridPane.add(cancelButton, 1, 11);
        GridPane.setHalignment(addButton, HPos.RIGHT);
        GridPane.setHalignment(cancelButton, HPos.LEFT);

        Scene scene = new Scene(gridPane, 780, 700);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> parentStage.show()); // 当窗口关闭时显示父窗口

        parentStage.hide();
        stage.show();
    }

    private static void addField(GridPane gridPane, String labelText, Control field, int row) {
        Label label = new Label(labelText);
        gridPane.add(label, 0, row);
        gridPane.add(field, 1, row);
    }

    private static void showAlert(Alert.AlertType alertType, Stage owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }


}
