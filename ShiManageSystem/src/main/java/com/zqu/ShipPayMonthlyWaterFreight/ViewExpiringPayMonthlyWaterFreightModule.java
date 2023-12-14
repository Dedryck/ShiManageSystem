package com.zqu.ShipPayMonthlyWaterFreight;

import com.zqu.MysqlConnectiontest;
import com.zqu.WaterwayDuesPaymentModule.WaterwayDuesPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-10:46
 * @description: 查询快到期
 */
public class ViewExpiringPayMonthlyWaterFreightModule {

    public static TableView<ShipPayMonthlyWater> tableView = new TableView<>();
    private static ObservableList<ShipPayMonthlyWater> allData; // 用于存储所有数据



    public static void show(Stage parentStage) {
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("查询即将到期的缴纳水运费基本信息");
        stage.setWidth(1024);
        stage.setHeight(768);

        // 创建新的按钮
        Button remindButton = new Button("提醒");
        Button remindAllButton = new Button("全部提醒");

        Button backButton = new Button("<---返回上一级");
        backButton.setStyle("-fx-font-size: 16px;");
//        backButton.setLayoutX(10); // 设置按钮的X坐标
//        backButton.setLayoutY(730); // 设置按钮的Y坐标
        backButton.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 点击按钮关闭当前窗口

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

        // 使用HBox布局放置这两个按钮
        HBox buttonBox = new HBox(10, remindButton, remindAllButton); // 10是按钮之间的间隔
        buttonBox.setAlignment(Pos.CENTER); // 设置HBox中内容居中

        // 使用BorderPane布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(buttonBox); // 将HBox放在顶部

        borderPane.setCenter(tableView); // 将tableView放在中间

        // 创建用于存放返回按钮的容器，并设置其位置
        BorderPane backButtonContainer = new BorderPane();
        backButtonContainer.setLeft(backButton);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT); // 设置按钮在容器的左下角
        borderPane.setBottom(backButtonContainer); // 将返回按钮容器放在底部

        // 创建场景并设置到舞台
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }


    //    用于加载'航道费缴纳情况'数据
    // 初始化或更新表格数据
    private static void reloadData() {
        // 首先清空表格现有的数据
        tableView.setItems(FXCollections.observableArrayList());
        // 从数据库加载数据
        List<ShipPayMonthlyWater> certificates = MysqlConnectiontest.ViewExpiringShipPayMonthlyWaterFreight();

        // 将数据转换为ObservableList并保存
        allData = FXCollections.observableArrayList(certificates);

        // 更新表格数据
        tableView.setItems(allData);
    }
}
