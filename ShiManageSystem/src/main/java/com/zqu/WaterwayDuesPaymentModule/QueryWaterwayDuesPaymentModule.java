package com.zqu.WaterwayDuesPaymentModule;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javafx.scene.control.DateCell;

import com.zqu.MysqlConnectiontest;


/**
 * @aurhor Dedryck
 * @create 2023-12-09-10:27
 * @description: 用于查询航道费缴纳情况
 */
public class QueryWaterwayDuesPaymentModule {
    // 创建TableView
    private static TableView<WaterwayDuesPayment> tableView = new TableView<>();

    // 添加一个新的 ComboBox 用于月份选择
    private static ComboBox<String> monthPicker = new ComboBox<>();
    private static ObservableList<WaterwayDuesPayment> data = FXCollections.observableArrayList();

    public static void show(Stage parentStage) {
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("查询航道费缴纳情况");
        stage.setWidth(1024);
        stage.setHeight(768);

        Button backButton = new Button("<---返回上一级");
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setLayoutX(10); // 设置按钮的X坐标
        backButton.setLayoutY(730); // 设置按钮的Y坐标
        backButton.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 点击按钮关闭当前窗口


//        这是用来装下拉框选择筛选的按键
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("船名", "填表时间月份", "航道费");

        TextField textField = new TextField();
        textField.setPrefWidth(200); // 设置文本框的长度为200

        DatePicker datePicker = new DatePicker();
//        datePicker.setVisible(false);

        Button printButton = new Button("打印");
        printButton.setOnAction(e -> saveAsCSV());
// 设置月份选择的 ComboBox
        monthPicker.setItems(FXCollections.observableArrayList(
                "1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"
        ));
        monthPicker.setVisible(false); // 初始化为不可见
        textField.setVisible(false);
        datePicker.setVisible(false);




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

        // 添加选择框（ComboBox）的选择事件监听器
        // ComboBox 选择监听器
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if ("船名".equals(newValue) || "航道费".equals(newValue)) {
                // 只显示 TextField
                textField.setVisible(true);
                monthPicker.setVisible(false);
                datePicker.setVisible(false);
                filterData(newValue, textField.getText());
            }
            if ("填表时间月份".equals(newValue)) {
                // 只显示 monthPicker
                textField.setVisible(false);
                monthPicker.setVisible(true);
                datePicker.setVisible(false);
                String monthValue = monthPicker.getValue();
                if (monthValue != null) {
                    filterData(newValue, monthValue);
                }
            }
            if ("填表日期".equals(newValue)) {
                // 只显示 DatePicker
                textField.setVisible(false);
                monthPicker.setVisible(false);
                datePicker.setVisible(true); // 应确保此处将 DatePicker 设置为可见
                datePicker.setDisable(false); // 确保 DatePicker 是启用的
                filterData(newValue, datePicker.getValue() != null ? datePicker.getValue().toString() : "");
            }

        });

// TextField 内容变化监听器
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getValue().equals("船名") || comboBox.getValue().equals("航道费")) {
                filterData(comboBox.getValue(), newValue);
            }
        });

// DatePicker 内容变化监听器
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getValue().equals("填表日期")) {
                filterData(comboBox.getValue(), newValue != null ? newValue.toString() : "");
            }
        });

// monthPicker 内容变化监听器
        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getValue().equals("填表时间月份")) {
                filterData(comboBox.getValue(), newValue);
            }
        });





        // 创建HBox用于存放搜索栏相关控件
        HBox hbox = new HBox(10); // 元素之间的间隔设置为10
        hbox.setAlignment(Pos.CENTER); // 设置HBox中内容的居中对齐
        hbox.getChildren().addAll(comboBox, textField,monthPicker, printButton); // 将控件添加到HBox

        // 使用BorderPane作为根布局
        BorderPane rootPane = new BorderPane();
        rootPane.setTop(hbox); // 将HBox放在顶部
        rootPane.setCenter(tableView); // 将TableView放在中心

        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        rootPane.setBottom(backButton); // 将backButton放在底部，并对齐到左侧

        Scene scene = new Scene(rootPane); // 使用BorderPane作为场景的根节点

        stage.setScene(scene);
        stage.show();
    }


//    用于加载'航道费缴纳情况'数据
    private static void reloadData() {
        List<WaterwayDuesPayment> dataList = MysqlConnectiontest.LoadDataWaterwayDuesPayment();
        data.clear(); // 清除旧数据
        data.addAll(dataList); // 添加新数据
        tableView.setItems(data); // 将数据设置到TableView
    }


    private static void filterData(String filterType, String filterValue) {
        // 先加载全部数据
        List<WaterwayDuesPayment> allData = MysqlConnectiontest.LoadDataWaterwayDuesPayment();
        List<WaterwayDuesPayment> filteredData = new ArrayList<>();

        // 根据不同的筛选类型来筛选数据
        switch (filterType) {
            case "船名":
            case "航道费":
                // 对于船名或航道费，执行模糊筛选
                for (WaterwayDuesPayment payment : allData) {
                    if ((filterType.equals("船名") && payment.getShipName().contains(filterValue)) ||
                            (filterType.equals("航道费") && payment.getWaterwayFeePerMonth().toString().contains(filterValue))) {
                        filteredData.add(payment);
                    }
                }
                break;
            case "填表时间月份":
                // 检查 filterValue 是否为 null
                if (filterValue != null) {
                    // 对于月份，执行筛选
                    int monthIndex = Integer.parseInt(filterValue.replace("月", "")); // 从月份字符串中提取月份数字
                    for (WaterwayDuesPayment payment : allData) {
                        if (payment.getDateOfFilling() != null &&
                                payment.getDateOfFilling().getMonth() + 1 == monthIndex) { // Date类中的月份是从0开始的
                            filteredData.add(payment);
                        }
                    }
                }
                break;

            case "填表日期":
                // 对于具体日期，执行筛选
                try {
                    Date selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(filterValue);
                    for (WaterwayDuesPayment payment : allData) {
                        if (payment.getDateOfFilling() != null &&
                                payment.getDateOfFilling().equals(selectedDate)) {
                            filteredData.add(payment);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            default:
                // 如果没有筛选条件，重新加载全部数据
                filteredData = allData;
                break;
        }

        // 更新表格数据
        data.clear();
        data.addAll(filteredData);
        tableView.setItems(data);
    }


    // 方法：将表格数据保存为CSV文件
    private static void saveAsCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("ExportedData.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
                writer.write('\ufeff'); // 写入UTF-8的BOM

                // 提取并写入标题行
                String headerLine = tableView.getColumns().stream()
                        .map(column -> padRight(column.getText(), 200)) // 假设每列标题宽度
                        .collect(Collectors.joining(","));
                writer.write(headerLine + "\n");

                // 遍历数据
                for (WaterwayDuesPayment item : tableView.getItems()) {
                    String line = String.join(",",
                            item.getShipName(),
                            item.getShipRegistrationNumber(),
                            item.getWaterwayFeePerMonth().toString(),
                            new SimpleDateFormat("yyyy-MM-dd").format(item.getDateOfFilling()),
                            item.getTotalWaterwayFee().toString(),
                            item.getPaymentRecord());
                    writer.write(line + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                // 处理异常
            }
        }
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }




}
