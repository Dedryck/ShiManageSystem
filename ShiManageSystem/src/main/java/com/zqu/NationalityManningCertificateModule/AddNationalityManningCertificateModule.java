package com.zqu.NationalityManningCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-20:16
 * @description: 添加功能船只国籍配员证书资料管理模块
 */
public class AddNationalityManningCertificateModule {
    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("添加船只国籍配员证书基本资料");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        stage.setWidth(780);
        stage.setHeight(700);

        Button addButton = new Button("添加信息");
        Button cancelButton = new Button("取消");
        addButton.setStyle(buttonStyle);
        cancelButton.setStyle(buttonStyle);




        // 取消按钮事件处理
        cancelButton.setOnAction(event -> {
            parentStage.show();
            stage.hide();
        });

        // 使用 GridPane 布局
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        // 定义标签和文本框
        // 船名
        Label shipNameLabel = new Label("船名: ");
        TextField shipNameField = new TextField();
        gridPane.add(shipNameLabel, 0, 0);
        gridPane.add(shipNameField, 1, 0);

// 船检登记号
        Label shipInspectionRegistrationNumberLabel = new Label("船检登记号: ");
        TextField shipInspectionRegistrationNumberField = new TextField();
        gridPane.add(shipInspectionRegistrationNumberLabel, 0, 1);
        gridPane.add(shipInspectionRegistrationNumberField, 1, 1);

// 国籍配员证书编号
        Label nationalityManningCertificateNumberLabel = new Label("国籍配员证书编号: ");
        TextField nationalityManningCertificateNumberField = new TextField();
        gridPane.add(nationalityManningCertificateNumberLabel, 0, 2);
        gridPane.add(nationalityManningCertificateNumberField, 1, 2);

// 船舶所有人
        Label shipOwnerLabel = new Label("船舶所有人: ");
        TextField shipOwnerField = new TextField();
        gridPane.add(shipOwnerLabel, 0, 3);
        gridPane.add(shipOwnerField, 1, 3);

// 船舶登记号
        Label shipRegistrationNumberLabel = new Label("船舶登记号: ");
        TextField shipRegistrationNumberField = new TextField();
        gridPane.add(shipRegistrationNumberLabel, 0, 4);
        gridPane.add(shipRegistrationNumberField, 1, 4);

// 下次换证时间
        Label nextCertificateRenewalDateLabel = new Label("下次换证时间: ");
        DatePicker nextCertificateRenewalDatePicker = new DatePicker();
        gridPane.add(nextCertificateRenewalDateLabel, 0, 5);
        gridPane.add(nextCertificateRenewalDatePicker, 1, 5);

// 换证通知时间
        Label certificateRenewalNotificationDateLabel = new Label("换证通知时间: ");
        DatePicker certificateRenewalNotificationDatePicker = new DatePicker();
        gridPane.add(certificateRenewalNotificationDateLabel, 0, 6);
        gridPane.add(certificateRenewalNotificationDatePicker, 1, 6);

// 国籍配员证书使用有效期至
        Label certificateValidityEndDateLabel = new Label("国籍配员证书使用有效期至: ");
        DatePicker certificateValidityEndDatePicker = new DatePicker();
        gridPane.add(certificateValidityEndDateLabel, 0, 7);
        gridPane.add(certificateValidityEndDatePicker, 1, 7);

// 发证日期
        Label issueDateLabel = new Label("发证日期: ");
        DatePicker issueDatePicker = new DatePicker();
        gridPane.add(issueDateLabel, 0, 8);
        gridPane.add(issueDatePicker, 1, 8);

// 国籍配员证书换证时间记录
        Label certificateRenewalHistoryLabel = new Label("国籍配员证书换证时间记录: ");
        TextArea certificateRenewalHistoryField = new TextArea();
        gridPane.add(certificateRenewalHistoryLabel, 0, 9);
        gridPane.add(certificateRenewalHistoryField, 1, 9);
        certificateRenewalHistoryField.setMinHeight(50); // 设置文本区域的最小高度
        certificateRenewalHistoryField.setPrefHeight(80); // 设置文本区域的首选高度
        certificateRenewalHistoryField.setPrefWidth(200); // 设置文本区域的首选宽度
        certificateRenewalHistoryField.setWrapText(true); // 启用自动换行
        certificateRenewalHistoryLabel.setContentDisplay(ContentDisplay.RIGHT);



        addButton.setOnAction(event -> {
            // 从文本框/DatePicker/TextArea读取数据
            String shipName = shipNameField.getText();
            String shipInspectionRegistrationNumber = shipInspectionRegistrationNumberField.getText();
            String nationalityManningCertificateNumber = nationalityManningCertificateNumberField.getText();
            String shipOwner = shipOwnerField.getText();
            String shipRegistrationNumber = shipRegistrationNumberField.getText();

// 使用DatePicker获取日期，并转换为java.sql.Date
            LocalDate nextCertificateRenewalDateLocal = nextCertificateRenewalDatePicker.getValue();
            java.sql.Date nextCertificateRenewalDate = nextCertificateRenewalDateLocal != null ? java.sql.Date.valueOf(nextCertificateRenewalDateLocal) : null;

            LocalDate certificateRenewalNotificationDateLocal = certificateRenewalNotificationDatePicker.getValue();
            java.sql.Date certificateRenewalNotificationDate = certificateRenewalNotificationDateLocal != null ? java.sql.Date.valueOf(certificateRenewalNotificationDateLocal) : null;

            LocalDate certificateValidityEndDateLocal = certificateValidityEndDatePicker.getValue();
            java.sql.Date certificateValidityEndDate = certificateValidityEndDateLocal != null ? java.sql.Date.valueOf(certificateValidityEndDateLocal) : null;

            LocalDate issueDateLocal = issueDatePicker.getValue();
            java.sql.Date issueDate = issueDateLocal != null ? java.sql.Date.valueOf(issueDateLocal) : null;

// 从TextArea获取文本
            String certificateRenewalHistory = certificateRenewalHistoryField.getText();

            // 1. 检查字段是否为空
            if (shipName.isEmpty() ||
                    shipInspectionRegistrationNumber.isEmpty() ||
                    nationalityManningCertificateNumberField.getText().isEmpty() ||
                    shipOwnerField.getText().isEmpty() ||
                    shipRegistrationNumberField.getText().isEmpty() ||
                    (nextCertificateRenewalDatePicker.getValue() == null) ||
                    (certificateRenewalNotificationDatePicker.getValue() == null) ||
                    (certificateValidityEndDatePicker.getValue() == null) ||
                    (issueDatePicker.getValue() == null) ||
                    certificateRenewalHistoryField.getText().isEmpty()) {

                showAlert(AlertType.WARNING, "警告", "请输入所有必填信息！");
                return;
            }


            // 2. 检查船只基本资料表中数据
            if (!MysqlConnectiontest.checkIfVesselExists(shipName, shipInspectionRegistrationNumber)) {
                showAlert(AlertType.WARNING, "警告", "你所输入的船名和登记号有误！");
                return;
            }

            // 3. 检查国籍配员证书基本信息表中数据
            if (MysqlConnectiontest.checkNationalityManningCertificate(shipName, shipInspectionRegistrationNumber)) {
                showAlert(AlertType.WARNING, "警告", "在国籍配员证书基本信息表已存在相应数据！");
                return;
            }

            // 4. 弹出确认添加的提示
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION, "你确定要添加该信息吗？", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                // 创建ShipNationalityManningCertificate对象
                ShipNationalityManningCertificate certificate = new ShipNationalityManningCertificate(
                        shipName,
                        shipInspectionRegistrationNumber,
                        nationalityManningCertificateNumber,
                        shipOwner,
                        shipRegistrationNumber,
                        nextCertificateRenewalDate,
                        certificateRenewalNotificationDate,
                        certificateValidityEndDate,
                        issueDate,
                        certificateRenewalHistory
                );

                // 调用方法添加到数据库
                boolean success = MysqlConnectiontest.AddNationalityManningCertificate(certificate);

                if (success) {
                    // 如果数据添加成功
                    showAlert(AlertType.INFORMATION, "信息", "数据已成功添加！");

                    // 清空所有输入控件
                    shipNameField.clear();
                    shipInspectionRegistrationNumberField.clear();
                    nationalityManningCertificateNumberField.clear();
                    shipOwnerField.clear();
                    shipRegistrationNumberField.clear();
                    nextCertificateRenewalDatePicker.setValue(null);
                    certificateRenewalNotificationDatePicker.setValue(null);
                    certificateValidityEndDatePicker.setValue(null);
                    issueDatePicker.setValue(null);
                    certificateRenewalHistoryField.clear();
                } else {
                    // 如果数据添加失败
                    showAlert(AlertType.ERROR, "错误", "添加数据失败，请检查数据并重试！");
                }
            }


        });


        gridPane.add(addButton, 0, 12);
        gridPane.add(cancelButton, 1, 12);



        Scene scene = new Scene(gridPane);
        stage.setScene(scene);

        parentStage.hide();
        stage.show();
    }


    // 弹出提示框的辅助方法
    private static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
