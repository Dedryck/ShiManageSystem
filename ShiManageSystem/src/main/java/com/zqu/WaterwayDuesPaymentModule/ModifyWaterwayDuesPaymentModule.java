package com.zqu.WaterwayDuesPaymentModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-14:41
 * @description: 修改窗体
 */
public class ModifyWaterwayDuesPaymentModule {


    // 创建视图表
    private static TableView<WaterwayDuesPayment> tableView = new TableView<>();
    private static ObservableList<WaterwayDuesPayment> data = FXCollections.observableArrayList();


    public static void show(Stage parentStage) {

            // 创建新的Stage
            Stage stage = new Stage();
            stage.setTitle("修改航道费缴纳情况");
            stage.setWidth(1024);
            stage.setHeight(768);
            tableView.getColumns().clear(); // 清除所有现有的列


//            tableView = new TableView<>();
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


        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableView.getSelectionModel().isEmpty()) {
                WaterwayDuesPayment selectedPayment = tableView.getSelectionModel().getSelectedItem();
                ModifyWaterwayDuesPaymentWindow.show(selectedPayment, stage);
                stage.hide();
            }
        });






        // 使用BorderPane布局
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView); // 将tableView放在中间
        borderPane.setBottom(backButton); // 将backButton放在底部

        // 设置backButton在底部左侧
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

    public static void reflash(){
        reloadData();
    }





}
