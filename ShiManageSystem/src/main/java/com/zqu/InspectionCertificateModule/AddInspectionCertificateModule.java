package com.zqu.InspectionCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-05-17:45
 * @description:添加船只检验证书
 */
public class AddInspectionCertificateModule {
    // 定义文本字段和日期选择器的成员变量
    private static TextField vesselNameField, registrationNumberField, certificateNumberField, vesselOwnerField,
            vesselRegistrationNumberField, vesselInspectionTypeField, inspectionAuthorityField;
    private static TextArea vesselInspectionRecordField;
    private static DatePicker nextInspectionDateField, noticeDateField, certificateValidityField, issueDateField;


    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("添加船只检验证书");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        // 调用 setupFormFields 方法并接收返回的行数
        int lastRow = setupFormFields(grid); // 获取setupFormFields方法返回的最后一行的索引



        Button addButton = new Button("添加");
        addButton.setStyle(buttonStyle);
        setupFormFields(grid);
        addButton.setOnAction(event -> {
            // 从界面获取船名和船检登记号
            // 从界面获取所有字段的值
            String vesselName = vesselNameField.getText();
            String registrationNumber = registrationNumberField.getText();
            String certificateNumber = certificateNumberField.getText();
            String owner = vesselOwnerField.getText();
            String vesselRegistrationNumber = vesselRegistrationNumberField.getText();
            String inspectionType = vesselInspectionTypeField.getText();
            String inspectionAuthority = inspectionAuthorityField.getText();
            String inspectionRecord = vesselInspectionRecordField.getText();
            LocalDate nextInspectionDateLocal = nextInspectionDateField.getValue();
            LocalDate notificationDateLocal = noticeDateField.getValue();
            LocalDate certificateValidityLocal = certificateValidityField.getValue();
            LocalDate issueDateLocal = issueDateField.getValue();

            // 检查是否有任何字段为空
            if (vesselName.isEmpty() || registrationNumber.isEmpty() || certificateNumber.isEmpty()
                    || owner.isEmpty() || vesselRegistrationNumber.isEmpty() || inspectionType.isEmpty()
                    || nextInspectionDateLocal == null || notificationDateLocal == null
                    || certificateValidityLocal == null || issueDateLocal == null
                    || inspectionAuthority.isEmpty() || inspectionRecord.isEmpty()) {
                Alert emptyFieldAlert = new Alert(Alert.AlertType.WARNING, "所需要填入的信息不能为空", ButtonType.OK);
                emptyFieldAlert.showAndWait();
                return; // 如果发现空字段，则终止操作
            }

// 在确认没有字段为空后，转换LocalDate为java.sql.Date
            java.sql.Date nextInspectionDate = java.sql.Date.valueOf(nextInspectionDateLocal);
            java.sql.Date notificationDate = java.sql.Date.valueOf(notificationDateLocal);
            java.sql.Date certificateValidity = java.sql.Date.valueOf(certificateValidityLocal);
            java.sql.Date issueDate = java.sql.Date.valueOf(issueDateLocal);



            // 首先检查在“船只基本资料表”中是否存在这个船只
            if (!MysqlConnectiontest.checkIfVesselExists(vesselName, registrationNumber)) {
                // 如果不存在，弹出提示
                Alert alert = new Alert(Alert.AlertType.WARNING, "该船名和船检登记号信息有误", ButtonType.OK);
                alert.showAndWait();
            } else {
                // 如果存在，检查在“船只检验证书基本表”中是否已经添加过这个船只
                if (MysqlConnectiontest.checkIfVesselExistsInInspectionCertificates(vesselName, registrationNumber)) {
                    // 如果已经添加过，弹出提示
                    Alert alert = new Alert(Alert.AlertType.WARNING, "该船名及其船检登记号已添加过检验证书信息", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    // 如果尚未添加，弹出确认提示
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要添加该船只检验证书信息吗？", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = confirmAlert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        // 用户确认添加，创建Vessel对象并填充数据
                        // 创建Vessel对象并填充数据
                        Vessel vessel = new Vessel(vesselName, registrationNumber, certificateNumber, owner,
                                vesselRegistrationNumber, inspectionType, nextInspectionDate,
                                notificationDate, inspectionAuthority, certificateValidity,
                                issueDate, inspectionRecord);
                        MysqlConnectiontest.AddInspectionCertificate(vessel);

                        // 显示操作成功的提示
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "船只检验证书信息已成功添加！", ButtonType.OK);
                        successAlert.showAndWait();

                        // 清除所有文本框
                        vesselNameField.clear();
                        registrationNumberField.clear();
                        certificateNumberField.clear();
                        vesselOwnerField.clear();
                        vesselRegistrationNumberField.clear();
                        vesselInspectionTypeField.clear();
                        inspectionAuthorityField.clear();
                        vesselInspectionRecordField.clear();

                        // 清除所有日期选择器
                        nextInspectionDateField.setValue(null);
                        noticeDateField.setValue(null);
                        certificateValidityField.setValue(null);
                        issueDateField.setValue(null);
                    }
                    // 如果用户选择“取消”，则不执行任何操作
                }
            }
        });


        Button cancelButton = new Button("取消");
        cancelButton.setStyle(buttonStyle);

        cancelButton.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

        // 设置按钮的水平对齐方式（如果需要）
        GridPane.setHalignment(addButton, HPos.CENTER); // 将添加按钮水平居中对齐
        GridPane.setHalignment(cancelButton, HPos.CENTER); // 将取消按钮水平居中对齐

        // 计算应该添加按钮的行数
        grid.add(addButton, 0, lastRow + 3); // 将按钮放在第二列
        grid.add(cancelButton, 1, lastRow + 3); // 将取消按钮放在下一行的第二列


        Scene scene = new Scene(grid, 780, 700);
        stage.setScene(scene);

        // 显示新窗体
        parentStage.hide();
        stage.show();
    }

    private static int setupFormFields(GridPane grid) {
        int row = 0; // 用于追踪添加组件的行数

        // 创建标签和字段，并将它们添加到grid中
        Label vesselNameLabel = new Label("船名:");
        vesselNameField = new TextField();
        grid.add(vesselNameLabel, 0, row);
        grid.add(vesselNameField, 1, row++);

        Label registrationNumberLabel = new Label("船检登记号:");
        registrationNumberField = new TextField();
        grid.add(registrationNumberLabel, 0, row);
        grid.add(registrationNumberField, 1, row++);

        Label certificateNumberLabel = new Label("检验证编号:");
        certificateNumberField = new TextField();
        grid.add(certificateNumberLabel, 0, row);
        grid.add(certificateNumberField, 1, row++);

        Label vesselOwnerLabel = new Label("船舶所有人:");
        vesselOwnerField = new TextField();
        grid.add(vesselOwnerLabel, 0, row);
        grid.add(vesselOwnerField, 1, row++);

        Label vesselRegistrationNumberLabel = new Label("船舶登记号:");
        vesselRegistrationNumberField = new TextField();
        grid.add(vesselRegistrationNumberLabel, 0, row);
        grid.add(vesselRegistrationNumberField, 1, row++);

        Label vesselInspectionTypeLabel = new Label("船舶检验类型:");
        vesselInspectionTypeField = new TextField();
        grid.add(vesselInspectionTypeLabel, 0, row);
        grid.add(vesselInspectionTypeField, 1, row++);

        Label nextInspectionDateLabel = new Label("下次检验时间:");
        nextInspectionDateField = new DatePicker();
        grid.add(nextInspectionDateLabel, 0, row);
        grid.add(nextInspectionDateField, 1, row++);

        Label noticeDateLabel = new Label("通知时间:");
        noticeDateField = new DatePicker();
        grid.add(noticeDateLabel, 0, row);
        grid.add(noticeDateField, 1, row++);

        Label inspectionAuthorityLabel = new Label("检验机关:");
        inspectionAuthorityField = new TextField();
        grid.add(inspectionAuthorityLabel, 0, row);
        grid.add(inspectionAuthorityField, 1, row++);

        Label certificateValidityLabel = new Label("检验证使用有效期至:");
        certificateValidityField = new DatePicker();
        grid.add(certificateValidityLabel, 0, row);
        grid.add(certificateValidityField, 1, row++);

        Label issueDateLabel = new Label("发证日期:");
        issueDateField = new DatePicker();
        grid.add(issueDateLabel, 0, row);
        grid.add(issueDateField, 1, row++);

        Label vesselInspectionRecordLabel = new Label("船只检验情况记录:");
        vesselInspectionRecordField = new TextArea();
        vesselInspectionRecordField.setMinHeight(50); // 设置文本区域的最小高度
        vesselInspectionRecordField.setPrefHeight(80); // 设置文本区域的首选高度
        vesselInspectionRecordField.setPrefWidth(200); // 设置文本区域的首选宽度
        vesselInspectionRecordField.setWrapText(true); // 启用自动换行
// 设置 GridPane 的列宽，确保 TextArea 不会超出界面
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints(200); // 限制第二列的宽度
        grid.getColumnConstraints().addAll(column1, column2); // 将列约束添加到 grid
        grid.add(vesselInspectionRecordLabel, 0, row);
        grid.add(vesselInspectionRecordField, 1, row, 2, 1); // 合并两列用于TextArea

        return row;
    }

}
