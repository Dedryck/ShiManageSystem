package com.zqu.WaterwayDuesPaymentModule;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-14:57
 * @description:
 */
public class ModifyWaterwayDuesPaymentWindow {

    public static void show(WaterwayDuesPayment payment,Stage parentStage) {
        // 创建新窗体
        Stage stage = new Stage();
        stage.setTitle("修改航道费缴纳信息");
        stage.setWidth(780);
        stage.setHeight(700);
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小



        // 使用GridPane布局
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // 添加标签和文本框
        Label shipNameLabel = new Label("船名:");

        TextField shipNameTextField = new TextField();
        grid.add(shipNameLabel, 0, 1);
        grid.add(shipNameTextField, 1, 1);

        // 船检登记号
        Label shipRegistrationNumberLabel = new Label("船检登记号:");
        TextField shipRegistrationNumberTextField = new TextField();
        grid.add(shipRegistrationNumberLabel, 0, 2);
        grid.add(shipRegistrationNumberTextField, 1, 2);

// 航道费（元/月）
        Label waterwayFeePerMonthLabel = new Label("航道费（元/月）:");
        TextField waterwayFeePerMonthTextField = new TextField();
        grid.add(waterwayFeePerMonthLabel, 0, 3);
        grid.add(waterwayFeePerMonthTextField, 1, 3);

// 填表日期
        Label dateOfFillingLabel = new Label("填表日期:");
        DatePicker dateOfFillingPicker = new DatePicker();
        grid.add(dateOfFillingLabel, 0, 4);
        grid.add(dateOfFillingPicker, 1, 4);
        // 设置日期选择限制、根据当前时间限制
        dateOfFillingPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                // 禁用所有超过今天的日期
                setDisable(empty || date.compareTo(today) > 0);
                setStyle("-fx-background-color: #ffc0cb;"); // 可以设置被禁用日期的样式，这里使用浅粉色作为示例
            }
        });

// 航道费合计
        Label totalWaterwayFeeLabel = new Label("航道费合计:");
        TextField totalWaterwayFeeTextField = new TextField();
        grid.add(totalWaterwayFeeLabel, 0, 5);
        grid.add(totalWaterwayFeeTextField, 1, 5);

// 航道费缴纳的记录
        Label paymentRecordLabel = new Label("航道费缴纳记录:");
        TextArea paymentRecordTextArea = new TextArea();
        paymentRecordTextArea.setPrefRowCount(3); // 设置TextArea的行数
        grid.add(paymentRecordLabel, 0, 6);
        grid.add(paymentRecordTextArea, 1, 6);
        paymentRecordTextArea.setMinHeight(50); // 设置文本区域的最小高度
        paymentRecordTextArea.setPrefHeight(80); // 设置文本区域的首选高度
        paymentRecordTextArea.setPrefWidth(200); // 设置文本区域的首选宽度
        paymentRecordTextArea.setWrapText(true); // 启用自动换行

        // 添加按钮
        Button addBtn = new Button("修改信息");
        addBtn.setStyle(buttonStyle);

        Button cancelBtn = new Button("取消");
        cancelBtn.setStyle(buttonStyle);


        // 设置文本框的内容
        shipNameTextField.setText(payment.getShipName());
        shipRegistrationNumberTextField.setText(payment.getShipRegistrationNumber());
        waterwayFeePerMonthTextField.setText(payment.getWaterwayFeePerMonth() != null ? payment.getWaterwayFeePerMonth().toPlainString() : "");
        totalWaterwayFeeTextField.setText(payment.getTotalWaterwayFee() != null ? payment.getTotalWaterwayFee().toPlainString() : "");
        dateOfFillingPicker.setValue(convertToLocalDate(payment.getDateOfFilling()));
        paymentRecordTextArea.setText(payment.getPaymentRecord());

        // 添加信息按钮的实现（根据需要实现）
        // 添加信息按钮的事件处理
        addBtn.setOnAction(e -> {


            // 检查文本框内容是否为空
            if (shipNameTextField.getText().isEmpty() || shipRegistrationNumberTextField.getText().isEmpty() ||
                    waterwayFeePerMonthTextField.getText().isEmpty() || dateOfFillingPicker.getValue() == null ||
                    totalWaterwayFeeTextField.getText().isEmpty() || paymentRecordTextArea.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "所填修改内容不能为空！").showAndWait();
                return;
            }

            // 检查船只基本资料表中船名和船检登记号是否存在
            if (!MysqlConnectiontest.checkIfVesselExists(shipNameTextField.getText(), shipRegistrationNumberTextField.getText())) {
                new Alert(Alert.AlertType.ERROR, "所填船名和船检登记号有误！").showAndWait();
                return;
            }

            // 检查航道费缴纳情况表中是否已有相同数据
            if (!MysqlConnectiontest.checkExistsWaterwayDuesPayment(shipNameTextField.getText(), shipRegistrationNumberTextField.getText())) {
                new Alert(Alert.AlertType.ERROR, "不存在相同船名数据").showAndWait();
                return;
            }

            // 尝试将文本转换为BigDecimal，捕获可能的NumberFormatException
            BigDecimal waterwayFeePerMonth;
            BigDecimal totalWaterwayFee;
            try {
                waterwayFeePerMonth = new BigDecimal(waterwayFeePerMonthTextField.getText().trim());
                totalWaterwayFee = new BigDecimal(totalWaterwayFeeTextField.getText().trim());
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "请输入有效的数字").showAndWait();
                return;
            }

            // 获取其余文本框和日期选择器的内容
            String shipName = shipNameTextField.getText();
            String shipRegistrationNumber = shipRegistrationNumberTextField.getText();
            LocalDate localDateOfFilling = dateOfFillingPicker.getValue();
            java.sql.Date sqlDateOfFilling = java.sql.Date.valueOf(localDateOfFilling);
            java.util.Date dateOfFilling = new java.util.Date(sqlDateOfFilling.getTime());
            String paymentRecord = paymentRecordTextArea.getText();

            // 创建并显示确认修改的对话框
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要修改该船只的修改航道费缴纳信息吗？", ButtonType.YES, ButtonType.NO);
            confirmationAlert.setTitle("确认修改");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                // 用户点击了"是"，执行更新操作
                WaterwayDuesPayment updatedPayment = new WaterwayDuesPayment(
                        shipName, shipRegistrationNumber, waterwayFeePerMonth, dateOfFilling, totalWaterwayFee, paymentRecord
                );

                // 调用更新方法
                MysqlConnectiontest.UpdateDataWaterwayDuesPayment(updatedPayment);

                // 显示操作结果
                new Alert(Alert.AlertType.INFORMATION, "信息修改成功！").showAndWait();
                ModifyWaterwayDuesPaymentModule.reflash();
                stage.close();
                parentStage.show();

            } else {
                // 用户点击了"否"，不执行任何操作
            }
        });


        // 取消按钮的事件处理
        cancelBtn.setOnAction(e -> {
            parentStage.show();
            stage.close();
        });

        grid.add(addBtn, 0, 9);
        grid.add(cancelBtn, 1, 9);

        // 设置场景和显示
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

//    将 java.util.Date 转换为 java.time.LocalDate。这个方法通过创建一个 java.sql.Date 实例（它是 java.util.Date 的子类），然后直接调用 toLocalDate() 方法来实现转换。这确实是一个更简洁直接的解决方案。
    private static LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert != null) {
            return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
        }
        return null;
    }




}
