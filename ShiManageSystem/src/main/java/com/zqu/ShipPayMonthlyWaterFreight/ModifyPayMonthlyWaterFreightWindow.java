package com.zqu.ShipPayMonthlyWaterFreight;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-20:53
 * @description: 修改的窗体
 */
public class ModifyPayMonthlyWaterFreightWindow {
    public static void show(Stage parentStage, ShipPayMonthlyWater data) {

            Stage stage = new Stage();
            stage.setTitle("编辑水运费信息");
            stage.setWidth(780);
            stage.setHeight(700);

            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(25, 25, 25, 25));

            // 添加标签和文本框
            TextField shipNameField = new TextField(data.getShipName());
            gridPane.add(new Label("船名:"), 0, 0);
            gridPane.add(shipNameField, 1, 0);

            TextField shipRegistrationNumberField = new TextField(data.getShipRegistrationNumber());
            gridPane.add(new Label("船检登记号:"), 0, 1);
            gridPane.add(shipRegistrationNumberField, 1, 1);

            TextField channelFeeField = new TextField(data.getChannelFee().toString());
            gridPane.add(new Label("航道费:"), 0, 2);
            gridPane.add(channelFeeField, 1, 2);

            DatePicker formFillingDatePicker = new DatePicker();
        //            !!!!下面这三条语句可以实现日期转化
        java.sql.Date sqlDate = new java.sql.Date(data.getFormFillingDate().getTime());
            LocalDate localDate = sqlDate.toLocalDate();
            formFillingDatePicker.setValue(localDate);
        // 自定义日期单元工厂以限制选择的日期
        formFillingDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // 禁用今天之后的所有日期
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // 可以设置被禁用日期的样式，这里使用浅粉色作为示例
                }
            }
        });

            gridPane.add(new Label("填表日期:"), 0, 3);
            gridPane.add(formFillingDatePicker, 1, 3);

            TextField totalWaterTransportFeeField = new TextField(data.getTotalWaterTransportFee().toString());
            gridPane.add(new Label("水运费合计:"), 0, 4);
            gridPane.add(totalWaterTransportFeeField, 1, 4);

        // 使用 TextArea 替换水运费缴纳记录的 TextField
        TextArea waterTransportFeeRecordArea = new TextArea(data.getWaterTransportFeeRecord());
        waterTransportFeeRecordArea.setMinHeight(50);
        waterTransportFeeRecordArea.setPrefHeight(80);
        waterTransportFeeRecordArea.setPrefWidth(200);
        waterTransportFeeRecordArea.setWrapText(true);
        gridPane.add(new Label("水运费缴纳记录:"), 0, 5);
        gridPane.add(waterTransportFeeRecordArea, 1, 5);

        // 添加“保存”和“取消”按钮
        Button btnSave = new Button("保存");
        btnSave.setOnAction(e -> {
            // 检查文本框是否为空
            if (shipNameField.getText().isEmpty() || shipRegistrationNumberField.getText().isEmpty() ||
                    channelFeeField.getText().isEmpty() || formFillingDatePicker.getValue() == null ||
                    totalWaterTransportFeeField.getText().isEmpty() || waterTransportFeeRecordArea.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "修改内容不可为空");
                return;
            }

            // 检查船名和船检登记号是否存在于船只基本表中
            if (!MysqlConnectiontest.checkIfVesselExists(shipNameField.getText(), shipRegistrationNumberField.getText())) {
                showAlert(Alert.AlertType.WARNING, "船名和船检登记号有误！");
                return;
            }

            // 检查是否存在于“船只按月缴纳水运费基本信息表”中
            if (!MysqlConnectiontest.checkIfExistsShipPayMonthlyWaterFreight(shipNameField.getText(), shipRegistrationNumberField.getText())) {
                showAlert(Alert.AlertType.WARNING, "你所修改的船名和船检登记号有误！");
                return;
            }

            // 确认修改提示
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要修改该船只按月缴纳水运费基本信息吗？", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                // 尝试录入数据
                try {
                    java.sql.Date formFillingSqlDate = java.sql.Date.valueOf(formFillingDatePicker.getValue());

                    ShipPayMonthlyWater payment = new ShipPayMonthlyWater(
                            shipNameField.getText(),
                            shipRegistrationNumberField.getText(),
                            new BigDecimal(channelFeeField.getText()),
                            formFillingSqlDate,
                            new BigDecimal(totalWaterTransportFeeField.getText()),
                            waterTransportFeeRecordArea.getText()
                    );

                    MysqlConnectiontest.UpdateDataShipPayMonthlyWaterFreight(payment);
                    showAlert(Alert.AlertType.INFORMATION, "记录更新成功！");
                    stage.close();
                    parentStage.show();
                    ModifyPayMonthlyWaterFreightModule.refreshTableView();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "更新失败：" + ex.getMessage());
                }
            }
        });

        Button btnCancel = new Button("取消");
        btnCancel.setOnAction(e -> {
            stage.close(); // 关闭当前窗口
            parentStage.show();
        });

        HBox hBoxButtons = new HBox(10); // 按钮之间的间距为10
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.getChildren().addAll(btnSave, btnCancel);

        // 将按钮组添加到界面中
        VBox vBox = new VBox(10); // 组件之间的间距为10
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(gridPane, hBoxButtons);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();


    }

    private static void showAlert(Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
