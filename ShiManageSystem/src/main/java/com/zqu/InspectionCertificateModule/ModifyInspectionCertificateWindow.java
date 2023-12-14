package com.zqu.InspectionCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-06-16:28
 * @description:修改检查证书的具体实现的窗口
 */
public class ModifyInspectionCertificateWindow {

    public static void show(Vessel vessel, Stage parentStage, Runnable refreshParentView) {
        Stage stage = new Stage();
        stage.setTitle("修改船只检验证书信息");
        String buttonStyle = "-fx-font-size: 16px;";


        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));


        // 设置GridPane的列宽度
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.RIGHT); // 右对齐标签

        ColumnConstraints column2 = new ColumnConstraints(200); // 设置第二列宽度为200
        gridPane.getColumnConstraints().addAll(column1, column2);

        // 窗体的布局和逻辑
        // 船名
        Label vesselNameLabel = new Label("船名:");
        TextField vesselNameField = new TextField(vessel.getVesselName());
        gridPane.add(vesselNameLabel, 0, 0);
        gridPane.add(vesselNameField, 1, 0);

        // 船检登记号
        Label registrationNumberLabel = new Label("船检登记号:");
        TextField registrationNumberField = new TextField(vessel.getRegistrationNumber());
        gridPane.add(registrationNumberLabel, 0, 1);
        gridPane.add(registrationNumberField, 1, 1);

        // 检验证编号
        Label certificateNumberLabel = new Label("检验证编号:");
        TextField certificateNumberField = new TextField(vessel.getCertificateNumber());
        gridPane.add(certificateNumberLabel, 0, 2);
        gridPane.add(certificateNumberField, 1, 2);

        // 船舶所有人
        Label ownerLabel = new Label("船舶所有人:");
        TextField ownerField = new TextField(vessel.getOwner());
        gridPane.add(ownerLabel, 0, 3);
        gridPane.add(ownerField, 1, 3);

        // 船舶登记号
        Label vesselRegistrationNumberLabel = new Label("船舶登记号:");
        TextField vesselRegistrationNumberField = new TextField(vessel.getVesselRegistrationNumber());
        gridPane.add(vesselRegistrationNumberLabel, 0, 4);
        gridPane.add(vesselRegistrationNumberField, 1, 4);

// 船舶检验类型
        Label inspectionTypeLabel = new Label("船舶检验类型:");
        TextField inspectionTypeField = new TextField(vessel.getInspectionType());
        gridPane.add(inspectionTypeLabel, 0, 5);
        gridPane.add(inspectionTypeField, 1, 5);

// 通知时间 (使用 DatePicker)
        Label notificationDateLabel = new Label("通知时间:");
        DatePicker notificationDateField = new DatePicker(vessel.getNotificationDate().toLocalDate());
        gridPane.add(notificationDateLabel, 0, 7);
        gridPane.add(notificationDateField, 1, 7);

// 检验机关
        Label inspectionAuthorityLabel = new Label("检验机关:");
        TextField inspectionAuthorityField = new TextField(vessel.getInspectionAuthority());
        gridPane.add(inspectionAuthorityLabel, 0, 8);
        gridPane.add(inspectionAuthorityField, 1, 8);

// 检验证使用有效期至 (使用 DatePicker)
        Label certificateValidityLabel = new Label("检验证使用有效期至:");
        DatePicker certificateValidityField = new DatePicker(vessel.getCertificateValidity().toLocalDate());
        gridPane.add(certificateValidityLabel, 0, 9);
        gridPane.add(certificateValidityField, 1, 9);

// 发证日期 (使用 DatePicker)
        Label issueDateLabel = new Label("发证日期:");
        DatePicker issueDateField = new DatePicker(vessel.getIssueDate().toLocalDate());
        gridPane.add(issueDateLabel, 0, 10);
        gridPane.add(issueDateField, 1, 10);

// 船只检验情况记录
        Label vesselInspectionRecordLabel = new Label("船只检验情况记录:");
        TextArea vesselInspectionRecordField = new TextArea(vessel.getInspectionRecord());
        vesselInspectionRecordField.setMinHeight(50);
        vesselInspectionRecordField.setPrefHeight(80);
        vesselInspectionRecordField.setPrefWidth(200);
        vesselInspectionRecordField.setWrapText(true);
        gridPane.add(vesselInspectionRecordLabel, 0, 11);
        gridPane.add(vesselInspectionRecordField, 1, 11);

        gridPane.setAlignment(Pos.CENTER); // 将GridPane内容居中

        // 创建 "修改信息" 和 "取消" 按钮
        Button modifyInfoButton = new Button("修改信息");
        modifyInfoButton.setStyle(buttonStyle);

        modifyInfoButton.setOnAction(event -> {
            String vesselName = vesselNameField.getText();
            String registrationNumber = registrationNumberField.getText();
            String certificateNumber = certificateNumberField.getText();
            String owner = ownerField.getText();
            String vesselRegistrationNumber = vesselRegistrationNumberField.getText();
            String inspectionType = inspectionTypeField.getText();
            LocalDate notificationDate = notificationDateField.getValue();
            String inspectionAuthority = inspectionAuthorityField.getText();
            LocalDate certificateValidity = certificateValidityField.getValue();
            LocalDate issueDate = issueDateField.getValue();
            String inspectionRecord = vesselInspectionRecordField.getText();

// 检查是否有空白字段
            if (vesselName.isEmpty() || registrationNumber.isEmpty() || certificateNumber.isEmpty() || owner.isEmpty() ||
                    vesselRegistrationNumber.isEmpty() || inspectionType.isEmpty() || notificationDate == null ||
                    inspectionAuthority.isEmpty() || certificateValidity == null || issueDate == null || inspectionRecord.isEmpty()) {
                showAlert("错误", "所有字段都需要填写！");
                return;
            }
            // 检查船只是否存在
            if (!MysqlConnectiontest.checkIfVesselExists(vesselName, registrationNumber)) {
                showAlert("错误", "在船只基本资料表中未找到相应船只！");
                return;
            }

            if (!MysqlConnectiontest.checkIfVesselExistsInInspectionCertificates(vesselName, registrationNumber)) {
                showAlert("错误", "在船只检验证书基本表中未找到相应船只！");
                return;
            }

            // 确认对话框
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要修改该用户的船只检验证书信息吗？", ButtonType.YES, ButtonType.NO);
            confirmAlert.setTitle("确认修改");
            confirmAlert.setHeaderText(null);
            Optional<ButtonType> result = confirmAlert.showAndWait();

            // 更新数据
            if (result.isPresent() && result.get() == ButtonType.YES) {
                // 用户确认修改，继续执行更新操作
                boolean success = MysqlConnectiontest.ModifyInspectionCertificateData(vesselName, registrationNumber, certificateNumber, owner, vesselRegistrationNumber, inspectionType, notificationDate, inspectionAuthority, certificateValidity, issueDate, inspectionRecord);

                if (success) {
                    showAlert("成功", "信息更新成功！");
                    refreshParentView.run(); // 刷新父窗口视图
                    parentStage.show(); // 显示父窗口
                    stage.close(); // 关闭当前窗口
                } else {
                    showAlert("错误", "信息更新失败！");
                }
            } else {
                // 用户选择取消，不执行更新操作
                return;
            }
        });




        Button cancelButton = new Button("取消");
        cancelButton.setStyle(buttonStyle);
        cancelButton.setOnAction(event -> {
            stage.close(); // 关闭当前窗体，返回上一级窗体
            parentStage.show();
        });


        HBox buttonBox = new HBox(10, modifyInfoButton, cancelButton); // 在一行中放置按钮
        buttonBox.setAlignment(Pos.CENTER); // 居中对齐

// 将按钮盒子添加到界面的底部
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane); // 中间放置GridPane
        borderPane.setBottom(buttonBox); // 底部放置按钮
        BorderPane.setAlignment(buttonBox, Pos.CENTER); // 居中对齐按钮盒子
        BorderPane.setMargin(buttonBox, new Insets(10, 0, 100, 0)); // 设置按钮盒子的边距


        Scene scene = new Scene(borderPane, 780, 700); // 根据需要设置布局
        stage.setScene(scene);

        stage.show();
    }
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
