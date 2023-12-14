package com.zqu.WaterwayDuesPaymentModule;

import com.zqu.MysqlConnectiontest;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-9:48
 * @description: 添加航道费缴纳情况表的基本信息
 */
public class AddWaterwayDuesPaymentModule {

    public static void show(Stage parentStage) {
        // 创建新窗体
        Stage stage = new Stage();
        stage.setTitle("添加航道费缴纳信息");
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
        Button addBtn = new Button("添加信息");
        addBtn.setStyle(buttonStyle);

        Button cancelBtn = new Button("取消");
        cancelBtn.setStyle(buttonStyle);


        // 添加信息按钮的实现（根据需要实现）
        // 添加信息按钮的事件处理
        addBtn.setOnAction(e -> {
            // 检查文本框内容是否为空
            if (shipNameTextField.getText().isEmpty() || shipRegistrationNumberTextField.getText().isEmpty() ||
                    waterwayFeePerMonthTextField.getText().isEmpty() || dateOfFillingPicker.getValue() == null ||
                    totalWaterwayFeeTextField.getText().isEmpty() || paymentRecordTextArea.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "所填内容不能为空！").showAndWait();
                return;
            }

            // 检查船只基本资料表中船名和船检登记号是否存在
            if (!MysqlConnectiontest.checkIfVesselExists(shipNameTextField.getText(), shipRegistrationNumberTextField.getText())) {
                new Alert(Alert.AlertType.ERROR, "所填船名和船检登记号有误！").showAndWait();
                return;
            }

            // 检查航道费缴纳情况表中是否已有相同数据
            if (MysqlConnectiontest.checkExistsWaterwayDuesPayment(shipNameTextField.getText(), shipRegistrationNumberTextField.getText())) {
                new Alert(Alert.AlertType.ERROR, "已存在相同船名数据").showAndWait();
                return;
            }

            // 弹出确认框
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要添加该数据吗？", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // 构造WaterwayDuesPayment对象并插入数据
                    WaterwayDuesPayment payment = new WaterwayDuesPayment(
                            shipNameTextField.getText(),
                            shipRegistrationNumberTextField.getText(),
                            new BigDecimal(waterwayFeePerMonthTextField.getText()),
                            java.sql.Date.valueOf(dateOfFillingPicker.getValue()),
                            new BigDecimal(totalWaterwayFeeTextField.getText()),
                            paymentRecordTextArea.getText()
                    );
                    MysqlConnectiontest.InsertDataWaterwayDuesPayment(payment);
                    new Alert(Alert.AlertType.INFORMATION, "数据添加成功！").showAndWait(); // 关闭当前窗体并显示父窗体
                    parentStage.show();
                    stage.close();
                }
            });
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

}
