package com.zqu.UserQueryModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-19:42
 * @description:
 */
public class QueryShipPayMentWindow {
    public static TableView<ShipPaymentInfo> tableView = new TableView<>(); // Object应替换为您的数据模型类
    private static List<ShipPaymentInfo> allCertificateInfo = new ArrayList<>();

    public static void show(Stage parentStage){
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("用户查询各类证书功能"); // 设置窗体标题
        stage.setWidth(1024);
        stage.setHeight(768);
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        // 创建按钮
        Button btnBack = new Button("<---返回上一级");
        btnBack.setStyle(buttonStyle);



        // 设置按钮动作（示例）
        btnBack.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 关闭当前窗体
        // 创建标签、搜索框和打印按钮
        Label lblShipName = new Label("船名");
//        搜索框
        TextField txtSearch = new TextField();
        txtSearch.setPrefWidth(200); // 设置搜索框长度
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });

        Button btnPrint = new Button("打印");
        btnPrint.setStyle(buttonStyle);
        btnPrint.setOnAction(event -> {
            try {
                saveDataAsCSV(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


// 创建表格列并绑定到ShipPaymentInfo类的属性
        TableColumn<ShipPaymentInfo, String> colShipName = new TableColumn<>("船名");
        colShipName.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<ShipPaymentInfo, String> colRegistrationNumber = new TableColumn<>("船检登记号");
        colRegistrationNumber.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));

        TableColumn<ShipPaymentInfo, BigDecimal> colChannelFee = new TableColumn<>("航道费");
        colChannelFee.setCellValueFactory(new PropertyValueFactory<>("channelFee"));

        TableColumn<ShipPaymentInfo, Date> colChannelFeeDate = new TableColumn<>("航道费填表日期");
        colChannelFeeDate.setCellValueFactory(new PropertyValueFactory<>("channelFeeDate"));

        TableColumn<ShipPaymentInfo, BigDecimal> colTotalChannelFee = new TableColumn<>("航道费合计");
        colTotalChannelFee.setCellValueFactory(new PropertyValueFactory<>("totalChannelFee"));

        TableColumn<ShipPaymentInfo, String> colChannelFeeRecord = new TableColumn<>("航道费缴纳记录");
        colChannelFeeRecord.setCellValueFactory(new PropertyValueFactory<>("channelFeeRecord"));

        TableColumn<ShipPaymentInfo, Date> colTransportationFeeDate = new TableColumn<>("水运费填表日期");
        colTransportationFeeDate.setCellValueFactory(new PropertyValueFactory<>("transportationFeeDate"));

        TableColumn<ShipPaymentInfo, BigDecimal> colTotalTransportationFee = new TableColumn<>("水运费合计");
        colTotalTransportationFee.setCellValueFactory(new PropertyValueFactory<>("totalTransportationFee"));

        TableColumn<ShipPaymentInfo, String> colTransportationFeeRecord = new TableColumn<>("水运费缴纳记录");
        colTransportationFeeRecord.setCellValueFactory(new PropertyValueFactory<>("transportationFeeRecord"));

        // 将列添加到tableView
        tableView.getColumns().addAll(colShipName, colRegistrationNumber, colChannelFee, colChannelFeeDate, colTotalChannelFee, colChannelFeeRecord, colTransportationFeeDate, colTotalTransportationFee, colTransportationFeeRecord);


        reloadData();

        // 组合按键布局
        HBox hBoxTop = new HBox(10); // 间距为10
        hBoxTop.setAlignment(Pos.CENTER);
        hBoxTop.getChildren().addAll(lblShipName, txtSearch, btnPrint);

        // 底部按钮布局
        HBox hBoxBottom = new HBox(); // 用于底部按钮
        hBoxBottom.setAlignment(Pos.BOTTOM_LEFT);
        hBoxBottom.getChildren().add(btnBack);

        // 设置BorderPane布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hBoxTop);
        borderPane.setCenter(tableView); // 设置中间为tableView
        borderPane.setBottom(hBoxBottom);

        // 设置并显示场景
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }


    private static void reloadData() {
        if (allCertificateInfo.isEmpty()) {
            allCertificateInfo = MysqlConnectiontest.getShipPaymentInfo();
        }
        tableView.setItems(FXCollections.observableArrayList(allCertificateInfo));
    }


    private static void filterData(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList(allCertificateInfo));
        } else {
            List<ShipPaymentInfo> filteredList = new ArrayList<>();
            for (ShipPaymentInfo info : allCertificateInfo) {
                if (info.getShipName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(info);
                }
            }
            tableView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }



    private static void saveDataAsCSV(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("ShipPaymentInfo.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
                // 写入UTF-8 BOM
                writer.write('\ufeff');
                // 写入标题行
                String header = "船名,船检登记号,航道费,航道费填表日期,航道费合计,航道费缴纳记录,水运费填表日期,水运费合计,水运费缴纳记录";
                writer.write(header);
                writer.newLine();

                // 遍历表格中的每一行数据
                for (ShipPaymentInfo info : tableView.getItems()) {
                    String row = info.getShipName() + "," +
                            info.getRegistrationNumber() + "," +
                            info.getChannelFee().toString() + "," +
                            formatDateString(info.getChannelFeeDate()) + "," +
                            info.getTotalChannelFee().toString() + "," +
                            info.getChannelFeeRecord() + "," +
                            formatDateString(info.getTransportationFeeDate()) + "," +
                            info.getTotalTransportationFee().toString() + "," +
                            info.getTransportationFeeRecord();
                    writer.write(row);
                    writer.newLine();
                }
            }
        }
    }

    private static String formatDateString(Date date) {
        return (date != null) ? date.toString() : "";
    }







}
