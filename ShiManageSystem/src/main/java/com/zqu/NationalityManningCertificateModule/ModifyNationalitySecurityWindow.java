package com.zqu.NationalityManningCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.zqu.NationalityManningCertificateModule.ShipNationalityManningCertificate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-08-14:36
 * @description:
 */
public class ModifyNationalitySecurityWindow {
    public static void show(Stage parentStage, ShipNationalityManningCertificate certificate) {
        Stage stage = new Stage();
        stage.setTitle("详细信息");
        stage.setWidth(780);
        stage.setHeight(700);

        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小


        Button ModifyButton = new Button("修改信息");
        Button cancelButton = new Button("取消");
        ModifyButton.setStyle(buttonStyle);
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

//        获取上级窗体的信息填充到文本框之中
        // 设置控件的值
// 设置文本框的值
        shipNameField.setText(certificate.getShipName());
        shipInspectionRegistrationNumberField.setText(certificate.getShipInspectionRegistrationNumber());
        nationalityManningCertificateNumberField.setText(certificate.getNationalityManningCertificateNumber());
        shipOwnerField.setText(certificate.getShipOwner());
        shipRegistrationNumberField.setText(certificate.getShipRegistrationNumber());

// 设置DatePicker的值
        nextCertificateRenewalDatePicker.setValue(certificate.getNextCertificateRenewalDate() != null ? new java.sql.Date(certificate.getNextCertificateRenewalDate().getTime()).toLocalDate() : null);
        certificateRenewalNotificationDatePicker.setValue(certificate.getCertificateRenewalNotificationDate() != null ? new java.sql.Date(certificate.getCertificateRenewalNotificationDate().getTime()).toLocalDate() : null);
        certificateValidityEndDatePicker.setValue(certificate.getCertificateValidityEndDate() != null ? new java.sql.Date(certificate.getCertificateValidityEndDate().getTime()).toLocalDate() : null);
        issueDatePicker.setValue(certificate.getIssueDate() != null ? new java.sql.Date(certificate.getIssueDate().getTime()).toLocalDate() : null);


// 设置TextArea的值
        certificateRenewalHistoryField.setText(certificate.getCertificateRenewalHistory());


        gridPane.add(ModifyButton, 0, 12);
        gridPane.add(cancelButton, 1, 12);


        ModifyButton.setOnAction(event -> {
            // 1. 检查文本框是否为空
            if (shipNameField.getText().isEmpty() ||
                    shipInspectionRegistrationNumberField.getText().isEmpty() ||
                    nationalityManningCertificateNumberField.getText().isEmpty() ||
                    shipOwnerField.getText().isEmpty() ||
                    shipRegistrationNumberField.getText().isEmpty() ||
                    nextCertificateRenewalDatePicker.getValue() == null ||
                    certificateRenewalNotificationDatePicker.getValue() == null ||
                    certificateValidityEndDatePicker.getValue() == null ||
                    issueDatePicker.getValue() == null ||
                    certificateRenewalHistoryField.getText().isEmpty()) {

                new Alert(Alert.AlertType.WARNING, "修改信息不能为空！", ButtonType.OK).showAndWait();
            } else {
                // 2. 检查数据库中是否存在相同的“船名”和“船检登记号”
                boolean exists = MysqlConnectiontest.checkNationalityManningCertificate(
                        shipNameField.getText(),
                        shipInspectionRegistrationNumberField.getText()
                );

                if (!exists) {
                    new Alert(Alert.AlertType.ERROR, "不存在相应的船只信息！", ButtonType.OK).showAndWait();
                } else {
                    // 3. 弹出确认提示
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                            "你确定要修改该船的国籍配员证书基本资料吗？",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        // 更新数据
                        boolean updateSuccess = MysqlConnectiontest.ModifyNationalityManningModuleData(
                                // 创建 Vessel 对象并填充数据
                                new ShipNationalityManningCertificate(
                                        shipNameField.getText(),
                                        shipInspectionRegistrationNumberField.getText(),
                                        nationalityManningCertificateNumberField.getText(),
                                        shipOwnerField.getText(),
                                        shipRegistrationNumberField.getText(),
                                        java.sql.Date.valueOf(nextCertificateRenewalDatePicker.getValue()),
                                        java.sql.Date.valueOf(certificateRenewalNotificationDatePicker.getValue()),
                                        java.sql.Date.valueOf(certificateValidityEndDatePicker.getValue()),
                                        java.sql.Date.valueOf(issueDatePicker.getValue()),
                                        certificateRenewalHistoryField.getText()
                                )
                        );

                        if (updateSuccess) {
                            new Alert(Alert.AlertType.INFORMATION, "更改信息成功！", ButtonType.OK).showAndWait();
                            // 关闭当前窗口，返回上一级
                            // 更新成功后，刷新上级窗口的表格视图
                            ModifyNationalitySecurityModule.refreshTable();
                            stage.hide();
                            parentStage.show();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "更新失败，请检查输入数据！", ButtonType.OK).showAndWait();
                        }
                    }
                }
            }
        });


        // 创建和设置场景

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);

        parentStage.hide();
        stage.show();
    }



}
