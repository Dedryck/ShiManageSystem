package com.zqu.WaterwayDuesPaymentModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
 * @create 2023-12-09-16:57
 * @description: 查看到期
 */
public class ViewExpiringWaterwayDuesPaymentModule {
    private static TableView<WaterwayDuesPayment> tableView = new TableView<>();

    private static ObservableList<WaterwayDuesPayment> data = FXCollections.observableArrayList();



    public static void show(Stage parentStage) {
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("查询航道费缴纳情况");
        stage.setWidth(1024);
        stage.setHeight(768);

        // 创建新的按钮
        Button remindButton = new Button("提醒");
        Button remindAllButton = new Button("全部提醒");

        Button backButton = new Button("<---返回上一级");
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setLayoutX(10); // 设置按钮的X坐标
        backButton.setLayoutY(730); // 设置按钮的Y坐标
        backButton.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 点击按钮关闭当前窗口

        tableView = new TableView<>();

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
    private static void reloadData() {
        List<WaterwayDuesPayment> dataList = MysqlConnectiontest.ViewExpiringLoadDataWaterwayDuesPayment();
        data.clear(); // 清除旧数据
        data.addAll(dataList); // 添加新数据
        tableView.setItems(data); // 将数据设置到TableView
    }

}
