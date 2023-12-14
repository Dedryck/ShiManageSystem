package com.zqu.WaterwayDuesPaymentModule;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-16:18
 * @description: 删除模块
 */
public class DeleteWaterwayDuesPaymentModule {
    private static TableView<WaterwayDuesPayment> tableView = new TableView<>();
    private static ObservableList<WaterwayDuesPayment> data = FXCollections.observableArrayList();

    public static void show(Stage parentStage) {

        // 创建新的Stage
        Stage stage = new Stage();
        stage.setTitle("删除航道费缴纳情况");
        stage.setWidth(1024);
        stage.setHeight(768);
        tableView.getColumns().clear(); // 清除所有现有的列
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // 按钮定义
        Button deleteButton = new Button("删除功能");
        deleteButton.setStyle("-fx-font-size: 14px;");
        Button confirmButton = new Button("确认");
        confirmButton.setStyle("-fx-font-size: 14px;");
        Button cancelButton = new Button("取消");
        cancelButton.setStyle("-fx-font-size: 14px;");
        // 最初设置确认和取消按钮不可见
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

        // 删除按钮的事件处理
        deleteButton.setOnAction(e -> {
            confirmButton.setVisible(true);
            cancelButton.setVisible(true);
        });

        // 确认按钮的事件处理
        confirmButton.setOnAction(e -> {
            // 获取所有选中的行
            ObservableList<WaterwayDuesPayment> selectedItems = FXCollections.observableArrayList(tableView.getSelectionModel().getSelectedItems());

            if (!selectedItems.isEmpty()) {
                // 弹出确认对话框
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "此删除操作不可逆，你确认要继续操作吗？", ButtonType.YES, ButtonType.NO);
                confirmationAlert.setTitle("确认删除");
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.YES) {
                    // 用户确认删除
                    boolean deleteSuccess = true;
                    for (WaterwayDuesPayment item : selectedItems) {
                        try {
                            MysqlConnectiontest.DeleteDataWaterwayDuesPayment(item.getShipName(), item.getShipRegistrationNumber());
                        } catch (Exception ex) {
                            System.out.println("删除失败: " + ex.getMessage());
                            deleteSuccess = false;
                            break;
                        }
                    }

                    if (deleteSuccess) {
                        new Alert(Alert.AlertType.INFORMATION, "删除成功！").showAndWait();
                        reloadData(); // 重新加载数据来刷新表格
                    } else {
                        new Alert(Alert.AlertType.ERROR, "部分或全部删除失败，请检查日志").showAndWait();
                    }
                }
            }

            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
        });


        // 取消按钮的事件处理
        cancelButton.setOnAction(e -> {
            tableView.getSelectionModel().clearSelection();
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
        });

        // 按钮布局
        HBox buttonBox = new HBox(10, deleteButton, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));


        // 创建返回按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> {
            // 隐藏当前窗体并显示上级窗体
            stage.hide();
            parentStage.show();
        });


        // TODO: 初始化tableView，例如添加列等
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // 设置列的大小策略

        // 定义列
        TableColumn<WaterwayDuesPayment, String> shipNameColumn = new TableColumn<>("船名");
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<WaterwayDuesPayment, String> shipRegistrationNumberColumn = new TableColumn<>("船检登记号");
        shipRegistrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        TableColumn<WaterwayDuesPayment, BigDecimal> waterwayFeePerMonthColumn = new TableColumn<>("航道费（元/月）");
        waterwayFeePerMonthColumn.setCellValueFactory(new PropertyValueFactory<>("waterwayFeePerMonth"));

        TableColumn<WaterwayDuesPayment, Date> dateOfFillingColumn = new TableColumn<>("填表日期");
        dateOfFillingColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfFilling"));

        TableColumn<WaterwayDuesPayment, BigDecimal> totalWaterwayFeeColumn = new TableColumn<>("航道费合计");
        totalWaterwayFeeColumn.setCellValueFactory(new PropertyValueFactory<>("totalWaterwayFee"));

        TableColumn<WaterwayDuesPayment, String> paymentRecordColumn = new TableColumn<>("航道费缴纳记录");
        paymentRecordColumn.setCellValueFactory(new PropertyValueFactory<>("paymentRecord"));

        // 将列添加到表格中
        tableView.getColumns().addAll(shipNameColumn, shipRegistrationNumberColumn, waterwayFeePerMonthColumn, dateOfFillingColumn, totalWaterwayFeeColumn, paymentRecordColumn);
        reloadData();



        // 使用BorderPane布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(buttonBox);
        borderPane.setCenter(tableView);
        borderPane.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);

        // 创建场景并设置到舞台
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }


    private static void reloadData() {
        List<WaterwayDuesPayment> dataList = MysqlConnectiontest.LoadDataWaterwayDuesPayment();
        data.clear(); // 清除旧数据
        data.addAll(dataList); // 添加新数据
        tableView.setItems(data); // 将数据设置到TableView
    }
}
