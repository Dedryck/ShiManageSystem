package com.zqu.NationalityManningCertificateModule;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.zqu.MysqlConnectiontest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-21:31
 * @description: 查询功能——船只国籍配员证书资料
 */
public class QueryNationalityManningCertificateModule {
    private static TableView<ShipNationalityManningCertificate> tableView;

    private static ComboBox<String> comboBox;
    private static TextField textField;
    private static DatePicker datePicker;

    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("查询界面");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        stage.setWidth(1024);
        stage.setHeight(768);

        BorderPane root = new BorderPane(); // 更改为BorderPane布局

        // 初始化 tableView
        tableView = new TableView<>();
        comboBox = new ComboBox<>();
        textField = new TextField();
        textField.setPromptText("输入搜索内容");
        datePicker = new DatePicker();

        // 返回上一级按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);

        backButton.setOnAction(e -> {
            parentStage.show();
            stage.hide();
        });

        // 下拉框、搜索框和日期选择器
//        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("船名", "船检登记号", "船舶国籍配员证书编号", "换证通知时间", "下次换证时间");
//        TextField textField = new TextField();
//        DatePicker datePicker = new DatePicker();
        datePicker.setVisible(false);

        comboBox.setOnAction(e -> {
            String selected = comboBox.getValue();
            if (selected.equals("换证通知时间") || selected.equals("下次换证时间")) {
                textField.setVisible(false);
                datePicker.setVisible(true);
            } else {
                datePicker.setVisible(false);
                textField.setVisible(true);
            }

            filterData(); // 调用数据筛选方法
        });


        // 文本框内容改变事件处理
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(); // 调用数据筛选方法
        });

// 日期选择器日期改变事件处理
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterData(); // 调用数据筛选方法
        });


        // 视图
        TableColumn<ShipNationalityManningCertificate, String> shipNameCol = new TableColumn<>("船名");
        shipNameCol.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<ShipNationalityManningCertificate, String> registrationNumberCol = new TableColumn<>("船检登记号");
        registrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRegistrationNumber"));

        TableColumn<ShipNationalityManningCertificate, String> certificateNumberCol = new TableColumn<>("国籍配员证书编号");
        certificateNumberCol.setCellValueFactory(new PropertyValueFactory<>("nationalityManningCertificateNumber"));

        TableColumn<ShipNationalityManningCertificate, String> ownerCol = new TableColumn<>("船舶所有人");
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("shipOwner"));

        TableColumn<ShipNationalityManningCertificate, String> shipRegNumberCol = new TableColumn<>("船舶登记号");
        shipRegNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        TableColumn<ShipNationalityManningCertificate, Date> nextRenewalDateCol = new TableColumn<>("下次换证时间");
        nextRenewalDateCol.setCellValueFactory(new PropertyValueFactory<>("nextCertificateRenewalDate"));

        TableColumn<ShipNationalityManningCertificate, Date> renewalNotificationDateCol = new TableColumn<>("换证通知时间");
        renewalNotificationDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalNotificationDate"));

        TableColumn<ShipNationalityManningCertificate, Date> validityEndDateCol = new TableColumn<>("国籍配员证书使用有效期至");
        validityEndDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateValidityEndDate"));

        TableColumn<ShipNationalityManningCertificate, Date> issueDateCol = new TableColumn<>("发证日期");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<ShipNationalityManningCertificate, String> renewalHistoryCol = new TableColumn<>("国籍配员证书换证时间记录");
        renewalHistoryCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalHistory"));

// 将所有列添加到表格视图
        tableView.getColumns().addAll(
                shipNameCol, registrationNumberCol, certificateNumberCol, ownerCol,
                shipRegNumberCol, nextRenewalDateCol, renewalNotificationDateCol,
                validityEndDateCol, issueDateCol, renewalHistoryCol
        );
        reloadData();



        // 水平盒子用于放置组合框、搜索框和日期选择器，居中对齐
        HBox searchBox = new HBox(10, comboBox, textField, datePicker);
        searchBox.setAlignment(Pos.CENTER);

        // 将搜索盒子置于顶部
        root.setTop(searchBox);

        // 表格视图置于中间
        root.setCenter(tableView);

        // 底部放置“返回上一级”按钮，为了确保它在窗体左下角，我们使用StackPane
        StackPane bottomPane = new StackPane(backButton);
        bottomPane.setAlignment(Pos.BOTTOM_LEFT); // 按钮定位到左下角
        root.setBottom(bottomPane);

        // 创建场景，设置舞台并显示
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // 隐藏父窗体
        parentStage.hide();
    }


    public static void reloadData() {
        List<ShipNationalityManningCertificate> data = MysqlConnectiontest.QueryNationalityManningModuleData();
        tableView.setItems(FXCollections.observableArrayList(data));
    }


    public static void filterData() {
        String selectedCriteria = comboBox.getValue();
        String searchText = textField.getText();
        LocalDate selectedDate = datePicker.getValue();

        // 获取当前TableView中的所有数据
        List<ShipNationalityManningCertificate> dataList = MysqlConnectiontest.QueryNationalityManningModuleData();
        FilteredList<ShipNationalityManningCertificate> filteredList = new FilteredList<>(FXCollections.observableArrayList(dataList));

        Predicate<ShipNationalityManningCertificate> predicate;

        if ("换证通知时间".equals(selectedCriteria) || "下次换证时间".equals(selectedCriteria)) {
            if (selectedDate != null) {
                // 获取日期范围：从选定日期的前30天到后30天
                LocalDate startDate = selectedDate.minusDays(30);
                LocalDate endDate = selectedDate.plusDays(30);

                // 根据日期范围设置过滤条件
                predicate = certificate -> {
                    LocalDate date = "换证通知时间".equals(selectedCriteria) ?
                            convertToLocalDate(certificate.getCertificateRenewalNotificationDate()) :
                            convertToLocalDate(certificate.getNextCertificateRenewalDate());
                    return date != null && !date.isBefore(startDate) && !date.isAfter(endDate);
                };
            } else {
                predicate = certificate -> true; // 不过滤，显示所有数据
            }
        } else {
            if (searchText == null || searchText.isEmpty()) {
                predicate = certificate -> true; // 不过滤，显示所有数据
            } else {
                // 根据文本设置过滤条件
                predicate = certificate -> {
                    switch (selectedCriteria) {
                        case "船名":
                            return certificate.getShipName().toLowerCase().contains(searchText.toLowerCase());
                        case "船检登记号":
                            return certificate.getShipInspectionRegistrationNumber().toLowerCase().contains(searchText.toLowerCase());
                        case "船舶国籍配员证书编号":
                            return certificate.getNationalityManningCertificateNumber().toLowerCase().contains(searchText.toLowerCase());
                        default:
                            return true;
                    }
                };
            }
        }

        // 应用过滤条件
        filteredList.setPredicate(predicate);
        tableView.setItems(filteredList);
    }



//    java.util.Date 类型转换为 java.time.LocalDate 类型
    private static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert != null ? new java.sql.Date(dateToConvert.getTime()).toLocalDate() : null;
    }







}
