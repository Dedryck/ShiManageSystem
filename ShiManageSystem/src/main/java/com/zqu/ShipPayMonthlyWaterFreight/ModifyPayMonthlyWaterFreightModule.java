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

/**
 * @aurhor Dedryck
 * @create 2023-12-09-20:47
 * @description: 修改模块
 */
public class ModifyPayMonthlyWaterFreightModule {

    public static TableView<ShipPayMonthlyWater> tableView = new TableView<>();

    private static ObservableList<ShipPayMonthlyWater> allData; // 用于存储所有数据

    public static void show(Stage parentStage){
        Stage stage = new Stage();
        stage.setTitle("修改缴纳水运费基本信息");
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


        // 初始化tableView
        tableView = new TableView<>();
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

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!tableView.getSelectionModel().getSelectedCells().isEmpty())) {
                ShipPayMonthlyWater selectedData = tableView.getSelectionModel().getSelectedItem();
                if (selectedData != null) {

                    ModifyPayMonthlyWaterFreightWindow.show(stage, selectedData);
                    stage.hide();
                    tableView.getSelectionModel().clearSelection();
                }
            }
        });




// 将tableView放在窗体中央
        borderPane.setCenter(tableView);
        // 创建场景并设置到舞台
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        // 显示窗口
        stage.show();
    }

    // 初始化或更新表格数据
    public static void reloadData() {
        // 首先清空表格现有的数据
        tableView.setItems(FXCollections.observableArrayList());
        // 从数据库加载数据
        List<ShipPayMonthlyWater> certificates = MysqlConnectiontest.LoadDataShipPayMonthlyWaterFreight();

        // 将数据转换为ObservableList并保存
        allData = FXCollections.observableArrayList(certificates);

        // 更新表格数据
        tableView.setItems(allData);
    }

    public static void refreshTableView(){
        reloadData();
    }
}
