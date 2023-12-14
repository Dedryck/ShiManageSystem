package com.zqu.ShipPayMonthlyWaterFreight;

import com.zqu.MysqlConnectiontest;
import com.zqu.SecurityCertificateModule.ShipSecurityCertificate;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @aurhor Dedryck
 * @create 2023-12-09-19:58
 * @description: 实现查询功能
 */
public class QueryPayMonthlyWaterFreightModule {

    public static TableView<ShipPayMonthlyWater> tableView = new TableView<>();
    private static ObservableList<ShipPayMonthlyWater> allData; // 用于存储所有数据


    //    private static
    public static void show(Stage parentStage){
        Stage stage = new Stage();
        stage.setTitle("查询缴纳水运费基本信息");
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

        // 创建下拉框
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("船名", "航道费", "水运费合计", "本年度的各个月份");

        // 创建搜索框
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("输入搜索内容");
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                reloadData(); // 当文本框为空时，重新加载并填充数据
            } else {
                // 当文本框不为空时，根据当前下拉框的选择进行数据筛选
                filterDataByText(comboBox.getValue(), newValue);
            }
        });


        // 创建子下拉框
        ComboBox<String> subComboBox = new ComboBox<>();
        for (int i = 1; i <= 12; i++) {
            subComboBox.getItems().add(i + "月");
        }
        subComboBox.setVisible(false);

        // 创建“打印”按钮
        Button btnPrint = new Button("打印");
        btnPrint.setOnAction(e -> saveAsCSV());

        // 创建水平布局，包含上述三个控件
        HBox hbox = new HBox(10, comboBox, txtSearch, subComboBox, btnPrint);
        hbox.setAlignment(Pos.CENTER); // 居中对齐
        hbox.setPadding(new Insets(10)); // 设置内边距


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

// 将tableView放在窗体中央
        borderPane.setCenter(tableView);



        // 下拉框选择事件处理器
        comboBox.setOnAction(e -> {
            String selected = comboBox.getValue();
            if ("本年度的各个月份".equals(selected)) {
                subComboBox.setVisible(true);
                txtSearch.setVisible(false);
                subComboBox.setOnAction(ev -> filterDataByMonth(subComboBox.getValue()));
            } else {
                subComboBox.setVisible(false);
                txtSearch.setVisible(true);
                txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.isEmpty()) {
                        tableView.setItems(allData);
                    } else {
                        filterDataByText(selected, newValue);
                    }
                });
            }
        });


        // 将水平布局置顶
        borderPane.setTop(hbox);

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


    // 筛选数据的方法，根据文本框中的文本进行筛选
    private static void filterDataByText(String column, String text) {
        Stream<ShipPayMonthlyWater> filteredStream = allData.stream();

        switch (column) {
            case "船名":
                filteredStream = filteredStream.filter(data -> data.getShipName().contains(text));
                break;
            case "航道费":
                filteredStream = filteredStream.filter(data -> data.getChannelFee().toString().contains(text));
                break;
            case "水运费合计":
                filteredStream = filteredStream.filter(data -> data.getTotalWaterTransportFee().toString().contains(text));
                break;
        }

        ObservableList<ShipPayMonthlyWater> filteredList = FXCollections.observableArrayList(filteredStream.collect(Collectors.toList()));
        tableView.setItems(filteredList);
    }

    // 筛选数据的方法，根据月份进行筛选
    private static void filterDataByMonth(String month) {
        int monthInt = Integer.parseInt(month.substring(0, month.length() - 1));
        ObservableList<ShipPayMonthlyWater> filteredList = allData.filtered(data -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(data.getFormFillingDate());
            return cal.get(Calendar.MONTH) + 1 == monthInt;
        });
        tableView.setItems(filteredList);
    }


    // 方法：保存表格数据为CSV文件
    private static void saveAsCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("ExportedData.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write('\ufeff');

                // 提取并写入标题行
                String headerLine = tableView.getColumns().stream()
                        .map(column -> column.getText())
                        .collect(Collectors.joining(","));
                writer.write(headerLine + "\n");

                // 遍历数据
                for (ShipPayMonthlyWater item : tableView.getItems()) {
                    String line = String.format("%s,%s,%s,%s,%s,%s\n",
                            item.getShipName(),
                            item.getShipRegistrationNumber(),
                            item.getChannelFee(),
                            item.getFormFillingDate(),
                            item.getTotalWaterTransportFee(),
                            item.getWaterTransportFeeRecord());
                    writer.write(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                // 处理异常
            }
        }
    }










}
