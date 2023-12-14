package com.zqu.ShipPayMonthlyWaterFreight;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-10:08
 * @description: 删除模块
 */
public class DeleteShipPayMonthlyWaterFreightModule {
    public static TableView<ShipPayMonthlyWater> tableView = new TableView<>();
    private static ObservableList<ShipPayMonthlyWater> allData; // 用于存储所有数据

    public static void show(Stage parentStage){
        Stage stage = new Stage();
        stage.setTitle("删除缴纳水运费基本信息");
        String buttonStyle = "-fx-font-size: 16px;";
        stage.setWidth(1024);
        stage.setHeight(768);

        // 创建BorderPane作为主布局
        BorderPane borderPane = new BorderPane();

        // 创建“返回上一级”按钮
        Button btnBack = new Button("<---返回上一级");
        btnBack.setStyle(buttonStyle);
        btnBack.setOnAction(e -> {
            stage.close();
            parentStage.show();
        }); // 设置按钮点击事件关闭当前窗口

        // 将“返回上一级”按钮放在左下角
        BorderPane.setAlignment(btnBack, Pos.BOTTOM_LEFT);
        borderPane.setBottom(btnBack);

// 创建按钮
        Button btnDelete = new Button("删除功能");
        Button btnConfirm = new Button("确认");
        Button btnCancel = new Button("取消");

// 设置按钮样式（可根据需要调整）
        btnDelete.setStyle(buttonStyle);
        btnConfirm.setStyle(buttonStyle);
        btnCancel.setStyle(buttonStyle);

// 初始时设置"确认"和"取消"按钮不可见
        btnConfirm.setVisible(false);
        btnCancel.setVisible(false);

// 设置按钮的行为
        btnDelete.setOnAction(e -> {
            btnConfirm.setVisible(true);
            btnCancel.setVisible(true);
        });
        btnCancel.setOnAction(e -> {
            btnConfirm.setVisible(false);
            btnCancel.setVisible(false);
            tableView.getSelectionModel().clearSelection(); // 清空表格选择
        });


        // 设置"确认"按钮的行为
        btnConfirm.setOnAction(e -> {
            ObservableList<ShipPayMonthlyWater> selectedItems = tableView.getSelectionModel().getSelectedItems();

            if (selectedItems.isEmpty()) {
                // 没有选择任何行时的提示
                showAlert(Alert.AlertType.WARNING, "无效选择", "请选择有效的数据进行删除");
            } else {
                // 弹出确认删除的对话框
                if (showConfirmationDialog("确认删除", "此删除操作不可逆，你确定要继续吗？")) {
                    // 用户确认删除
                    boolean deleteSuccess = true;
                    for (ShipPayMonthlyWater item : selectedItems) {
                        // 提取每一行的船名和船检登记号
                        String shipName = item.getShipName();
                        String shipRegistrationNumber = item.getShipRegistrationNumber();

                        // 调用删除方法
                        deleteSuccess = deleteSuccess && MysqlConnectiontest.DeleteDataShipPayMonthlyWaterFreight(shipName, shipRegistrationNumber);
                    }

                    if (deleteSuccess) {
                        // 所有选中的行都删除成功
                        showAlert(Alert.AlertType.INFORMATION, "删除成功", "数据已成功删除！");
                        reloadData(); // 重新加载数据
                    } else {
                        // 至少一行删除失败
                        showAlert(Alert.AlertType.ERROR, "删除失败", "部分数据删除失败，请重试！");
                    }
                }
            }
        });

// 创建按钮的水平布局盒子
        HBox buttonBox = new HBox(10); // 10是按钮之间的间距
        buttonBox.getChildren().addAll(btnDelete, btnConfirm, btnCancel);
        buttonBox.setAlignment(Pos.CENTER); // 居中对齐按钮

// 将按钮盒子放在界面顶部
        borderPane.setTop(buttonBox);

        // 初始化tableView
        tableView = new TableView<>();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // 设置列的大小策略

// 为tableView添加列
        TableColumn<ShipPayMonthlyWater, String> shipNameColumn = new TableColumn<>("船名");
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<ShipPayMonthlyWater, String> shipRegistrationNumberColumn = new TableColumn<>("船检登记号");
        shipRegistrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        TableColumn<ShipPayMonthlyWater, BigDecimal> channelFeeColumn = new TableColumn<>("航道费");
        channelFeeColumn.setCellValueFactory(new PropertyValueFactory<>("channelFee"));

        TableColumn<ShipPayMonthlyWater, Date> formFillingDateColumn = new TableColumn<>("填表日期");
        formFillingDateColumn.setCellValueFactory(new PropertyValueFactory<>("formFillingDate"));

        TableColumn<ShipPayMonthlyWater, BigDecimal> totalWaterTransportFeeColumn = new TableColumn<>("水运费合计");
        totalWaterTransportFeeColumn.setCellValueFactory(new PropertyValueFactory<>("totalWaterTransportFee"));

        TableColumn<ShipPayMonthlyWater, String> waterTransportFeeRecordColumn = new TableColumn<>("水运费缴纳记录");
        waterTransportFeeRecordColumn.setCellValueFactory(new PropertyValueFactory<>("waterTransportFeeRecord"));

// 将列添加到tableView
        tableView.getColumns().add(shipNameColumn);
        tableView.getColumns().add(shipRegistrationNumberColumn);
        tableView.getColumns().add(channelFeeColumn);
        tableView.getColumns().add(formFillingDateColumn);
        tableView.getColumns().add(totalWaterTransportFeeColumn);
        tableView.getColumns().add(waterTransportFeeRecordColumn);

        reloadData();

// 将tableView放在窗体中央
        borderPane.setCenter(tableView);


        // 创建场景并设置到舞台
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        // 显示窗口
        stage.show();
    }

    // 初始化或更新表格数据
    private static void reloadData() {
        // 首先清空表格现有的数据
        tableView.setItems(FXCollections.observableArrayList());
        // 从数据库加载数据
        List<ShipPayMonthlyWater> certificates = MysqlConnectiontest.LoadDataShipPayMonthlyWaterFreight();

        // 将数据转换为ObservableList并保存
        allData = FXCollections.observableArrayList(certificates);

        // 更新表格数据
        tableView.setItems(allData);
    }


    // 显示确认对话框的方法
    private static boolean showConfirmationDialog(String title, String content) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle(title);
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText(content);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // 显示提醒的方法
    private static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
