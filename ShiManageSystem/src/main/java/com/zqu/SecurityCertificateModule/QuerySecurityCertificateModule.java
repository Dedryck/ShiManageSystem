package com.zqu.SecurityCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.DatePicker;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-12:33
 * @description:用于查询船只安检证书的信息
 */
public class QuerySecurityCertificateModule {
    // 声明table为static全局变量
    public static TableView<ShipSecurityCertificate> table;
    private static DatePicker datePicker = new DatePicker();


    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("查询安检证书");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小
        // 创建下拉框和搜索文本框
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("船名", "船检登记号", "船舶安检证书编号", "通知检查时间", "下次检查时间");
        comboBox.getSelectionModel().selectFirst(); // 默认选择第一个选项

        // 初始化DatePicker
        datePicker.setVisible(false); // 一开始不显示DatePicker


        // 返回上一级按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> {
            parentStage.show(); // 显示上级窗体
            stage.close(); // 关闭当前窗体
        });


        // 搜索文本框
        TextField searchField = new TextField();
        searchField.setMaxWidth(200); // 设置文本框长度


        // 搜索框文本变化监听器
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                reloadData(); // 当搜索框为空时重载数据
            } else {
                filterDataBasedOnSelection(newValue, comboBox.getValue()); // 否则根据搜索框内容和下拉框选择进行筛选
            }
        });

        // 下拉框选择变化监听器
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("通知检查时间") || newValue.equals("下次检查时间")) {
                searchField.setVisible(false);
                datePicker.setVisible(true);

                if (datePicker.getValue() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String selectedDate = datePicker.getValue().format(formatter);
                    filterDataBasedOnSelection(selectedDate, newValue);
                }
            } else {
                searchField.setVisible(true);
                datePicker.setVisible(false);

                if (!searchField.getText().isEmpty()) {
                    filterDataBasedOnSelection(searchField.getText(), newValue);
                }
            }
        });

// DatePicker选择变化监听器
        // DatePicker选择变化监听器
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String selectedDate = newValue.format(formatter);
                filterDataBasedOnSelection(selectedDate, comboBox.getValue()); // 使用格式化后的日期字符串进行筛选
            } else {
                reloadData(); // 如果没有选择日期，则刷新视图
            }
        });





        // 中间的视图表格
        table = new TableView<>();
// 创建并设置表格列
        TableColumn<ShipSecurityCertificate, String> shipNameColumn = new TableColumn<>("船名");
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<ShipSecurityCertificate, String> registrationNumberColumn = new TableColumn<>("船检登记号");
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRegistrationNumber"));

        TableColumn<ShipSecurityCertificate, String> certificateNumberColumn = new TableColumn<>("安检证书编号");
        certificateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("securityCertificateNumber"));

        TableColumn<ShipSecurityCertificate, String> shipOwnerColumn = new TableColumn<>("船舶所有人");
        shipOwnerColumn.setCellValueFactory(new PropertyValueFactory<>("shipOwner"));

        TableColumn<ShipSecurityCertificate, String> shipRegistrationNumberColumn = new TableColumn<>("船舶登记号");
        shipRegistrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        TableColumn<ShipSecurityCertificate, String> inspectionAuthorityColumn = new TableColumn<>("检查机关");
        inspectionAuthorityColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionAuthority"));

        TableColumn<ShipSecurityCertificate, Date> nextInspectionDateColumn = new TableColumn<>("下次检查时间");
        nextInspectionDateColumn.setCellValueFactory(new PropertyValueFactory<>("nextInspectionDate"));

        TableColumn<ShipSecurityCertificate, Date> notificationDateColumn = new TableColumn<>("通知时间");
        notificationDateColumn.setCellValueFactory(new PropertyValueFactory<>("notificationDate"));

        TableColumn<ShipSecurityCertificate, Date> validityEndDateColumn = new TableColumn<>("安检证书使用有效期至");
        validityEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("certificateValidityEndDate"));

        TableColumn<ShipSecurityCertificate, Date> issueDateColumn = new TableColumn<>("发证日期");
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<ShipSecurityCertificate, String> inspectionRecordColumn = new TableColumn<>("船只检验情况记录");
        inspectionRecordColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionRecord"));

        // 将列添加到表格
        table.getColumns().addAll(shipNameColumn, registrationNumberColumn, certificateNumberColumn, shipOwnerColumn, shipRegistrationNumberColumn, inspectionAuthorityColumn, nextInspectionDateColumn, notificationDateColumn, validityEndDateColumn, issueDateColumn, inspectionRecordColumn);

        reloadData();


        // 下拉框和搜索框的布局
        HBox topBar = new HBox(10, comboBox, searchField);
        topBar.setAlignment(Pos.CENTER); // 置顶居中

        // 使用BorderPane作为主布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar); // 顶部是下拉框和搜索框
        borderPane.setCenter(table); // 中间是表格

        // 将DatePicker添加到布局
        topBar.getChildren().add(datePicker); // 将DatePicker添加到顶部栏

        // 返回按钮放在左下角
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        borderPane.setBottom(backButton);

        // 设置场景和舞台
        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }


    // 初始化或更新表格数据
    public static void reloadData() {
        // 从数据库加载数据
        List<ShipSecurityCertificate> certificates = MysqlConnectiontest.loadDataSecurityCertificate();

        // 将数据转换为ObservableList
        ObservableList<ShipSecurityCertificate> observableList = FXCollections.observableArrayList(certificates);

        // 更新表格数据
        table.setItems(observableList);

    }


    // filterDataBasedOnSelection用于筛选信息
    // 修改filterDataBasedOnSelection方法签名，增加searchText参数
    private static void filterDataBasedOnSelection(String searchText, String filterOption) {
        List<ShipSecurityCertificate> allCertificates = MysqlConnectiontest.loadDataSecurityCertificate();

        List<ShipSecurityCertificate> filteredList = new ArrayList<>();

        // 对于文本搜索
        if (!filterOption.equals("通知检查时间") && !filterOption.equals("下次检查时间")) {
            final String finalSearchText = searchText.toLowerCase(); // 使用传入的搜索文本

            filteredList = allCertificates.stream()
                    .filter(certificate -> {
                        switch (filterOption) {
                            case "船名":
                                return certificate.getShipName().toLowerCase().contains(finalSearchText);
                            case "船检登记号":
                                return certificate.getShipInspectionRegistrationNumber() != null && certificate.getShipInspectionRegistrationNumber().toLowerCase().contains(finalSearchText);
                            case "船舶安检证书编号":
                                return certificate.getSecurityCertificateNumber() != null && certificate.getSecurityCertificateNumber().toLowerCase().contains(finalSearchText);
                            // ... 添加其他文本搜索条件
                            default:
                                return true;
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            // 对于日期搜索
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String selectedDate = datePicker.getValue().format(formatter);

            filteredList = allCertificates.stream()
                    .filter(certificate -> {
                        if (filterOption.equals("通知检查时间")) {
                            return certificate.getNotificationDate().toString().contains(selectedDate);
                        } else {
                            return certificate.getNextInspectionDate().toString().contains(selectedDate);
                        }
                    })
                    .collect(Collectors.toList());
        }

        ObservableList<ShipSecurityCertificate> observableList = FXCollections.observableArrayList(filteredList);
        table.setItems(observableList);
    }





}
