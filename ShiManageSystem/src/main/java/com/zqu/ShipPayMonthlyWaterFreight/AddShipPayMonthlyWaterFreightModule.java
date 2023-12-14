package com.zqu.ShipPayMonthlyWaterFreight;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-19:15
 * @description: 添加功能
 */
public class AddShipPayMonthlyWaterFreightModule {
    public static void show(Stage parentStage) {
        // 创建新窗口
        Stage stage = new Stage();
        stage.setTitle("添加缴纳水运费基本信息");
        String labelStyle = "-fx-font-size: 18px;"; // 调整标签字体大小

        // 创建网格布局
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // 将整个网格居中
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // 设置列宽，以便标签和输入框可以在各自的列中居中
        ColumnConstraints columnOneConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT); // 右对齐标签
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstrains.setHalignment(HPos.LEFT); // 左对齐输入框
        grid.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains); // 添加列约束

        // 创建标签和输入控件
        Label lblShipName = new Label("船名:");
        lblShipName.setStyle(labelStyle);
        TextField txtShipName = new TextField();

        Label lblShipRegistrationNumber = new Label("船检登记号:");
        lblShipRegistrationNumber.setStyle(labelStyle);
        TextField txtShipRegistrationNumber = new TextField();

        Label lblChannelFee = new Label("航道费:");
        lblChannelFee.setStyle(labelStyle);
        TextField txtChannelFee = new TextField();

        Label lblFormFillingDate = new Label("填表日期:");
        lblFormFillingDate.setStyle(labelStyle);
        DatePicker dpFormFillingDate = new DatePicker(LocalDate.now());
        dpFormFillingDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });

        Label lblTotalWaterTransportFee = new Label("水运费合计:");
        lblTotalWaterTransportFee.setStyle(labelStyle);
        TextField txtTotalWaterTransportFee = new TextField();

        Label lblWaterTransportFeeRecord = new Label("水运费缴纳记录:");
        lblWaterTransportFeeRecord.setStyle(labelStyle);
        TextArea txtWaterTransportFeeRecord = new TextArea();
        txtWaterTransportFeeRecord.setMinHeight(50);
        txtWaterTransportFeeRecord.setPrefHeight(80);
        txtWaterTransportFeeRecord.setPrefWidth(200);
        txtWaterTransportFeeRecord.setWrapText(true);

        // 将标签和输入控件添加到网格布局中
        grid.add(lblShipName, 0, 0);
        grid.add(txtShipName, 1, 0);
        grid.add(lblShipRegistrationNumber, 0, 1);
        grid.add(txtShipRegistrationNumber, 1, 1);
        grid.add(lblChannelFee, 0, 2);
        grid.add(txtChannelFee, 1, 2);
        grid.add(lblFormFillingDate, 0, 3);
        grid.add(dpFormFillingDate, 1, 3);
        grid.add(lblTotalWaterTransportFee, 0, 4);
        grid.add(txtTotalWaterTransportFee, 1, 4);
        grid.add(lblWaterTransportFeeRecord, 0, 5);
        grid.add(txtWaterTransportFeeRecord, 1, 5);

        // 添加按钮
        Button btnAdd = new Button("添加信息");
        btnAdd.setStyle(labelStyle);
        Button btnCancel = new Button("取消");
        btnCancel.setStyle(labelStyle);
        grid.add(btnAdd, 0, 9);
        grid.add(btnCancel, 1, 9);
        GridPane.setHalignment(btnAdd, HPos.CENTER);
        GridPane.setHalignment(btnCancel, HPos.CENTER);

        // 设置取消按钮的事件处理器
        btnCancel.setOnAction(e -> {
            stage.close();
            parentStage.show();
        });

        // 在show方法中为btnAdd按钮添加事件处理器
        btnAdd.setOnAction(e -> {
            // 检测文本框是否为空
            if (txtShipName.getText().isEmpty() || txtShipRegistrationNumber.getText().isEmpty() ||
                    txtChannelFee.getText().isEmpty() || txtTotalWaterTransportFee.getText().isEmpty() ||
                    txtWaterTransportFeeRecord.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "表单错误!", "所输入内容不可为空！");
                return;
            }

            // 检查船名和船检登记号是否存在
            if (!MysqlConnectiontest.checkIfVesselExists(txtShipName.getText(), txtShipRegistrationNumber.getText())) {
                showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "验证失败", "你所输入的船名和船检登记号有误！");
                return;
            }

            // 检查船名和船检登记号是否已在水运费表中存在
            if (MysqlConnectiontest.checkIfExistsShipPayMonthlyWaterFreight(txtShipName.getText(), txtShipRegistrationNumber.getText())) {
                showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "重复数据", "你所输入的船名和船检登记号信息已存在！");
                return;
            }

            // 弹出确认添加信息的对话框
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要添加该信息吗？", ButtonType.YES, ButtonType.NO);
            confirmAlert.setHeaderText(null);
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    // 创建ShipPayMonthlyWater对象
                    ShipPayMonthlyWater payment = new ShipPayMonthlyWater(
                            txtShipName.getText(),
                            txtShipRegistrationNumber.getText(),
                            new BigDecimal(txtChannelFee.getText()),
                            java.sql.Date.valueOf(dpFormFillingDate.getValue()),
                            new BigDecimal(txtTotalWaterTransportFee.getText()),
                            txtWaterTransportFeeRecord.getText()
                    );

                    // 调用方法添加数据
                    MysqlConnectiontest.AddDataShipPayMonthlyWaterFreight(payment);

                    showAlert(Alert.AlertType.INFORMATION, grid.getScene().getWindow(), "添加成功", "添加该数据成功！");
                    // 清除文本框内的数据
                    txtShipName.clear();
                    txtShipRegistrationNumber.clear();
                    txtChannelFee.clear();
                    txtTotalWaterTransportFee.clear();
                    txtWaterTransportFeeRecord.clear();
                    dpFormFillingDate.setValue(LocalDate.now()); // 重置日期选择器为当前日期

                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "错误", "操作失败：" + ex.getMessage());
                }
            }
        });

        // 创建场景并设置到舞台
        Scene scene = new Scene(grid, 780, 700);
        stage.setScene(scene);

        // 显示窗口
        stage.show();
    }

    // 添加一个辅助方法来显示警告
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
