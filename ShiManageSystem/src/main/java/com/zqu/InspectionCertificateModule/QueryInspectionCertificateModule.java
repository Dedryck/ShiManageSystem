package com.zqu.InspectionCertificateModule;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * @aurhor Dedryck
 * @create 2023-12-06-14:23
 * @description:查询验证证书相关功能
 */
public class QueryInspectionCertificateModule {
    private static TableView<Vessel> table = new TableView<>();
    private static ObservableList<Vessel> data = FXCollections.observableArrayList();

    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("查询检验证书信息");
        DatePicker datePicker = new DatePicker();
        datePicker.setVisible(false); // 默认隐藏

        // 创建下拉框和搜索框
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("船名", "船检登记号", "船舶检验证编号", "下次检验时间", "通知时间");

        TextField searchField = new TextField();
        searchField.setMaxWidth(200); // 限定搜索框的长度


        HBox topContainer = new HBox(10, comboBox, searchField, datePicker);
        topContainer.setAlignment(Pos.CENTER); // 居中对齐
        topContainer.setPadding(new Insets(10));

// 船名
        TableColumn<Vessel, String> vesselNameCol = new TableColumn<>("船名");
        vesselNameCol.setCellValueFactory(new PropertyValueFactory<>("vesselName"));

// 船检登记号
        TableColumn<Vessel, String> registrationNumberCol = new TableColumn<>("船检登记号");
        registrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));

// 检验证编号
        TableColumn<Vessel, String> certificateNumberCol = new TableColumn<>("检验证编号");
        certificateNumberCol.setCellValueFactory(new PropertyValueFactory<>("certificateNumber"));

// 船舶所有人
        TableColumn<Vessel, String> ownerCol = new TableColumn<>("船舶所有人");
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));

// 船舶登记号
        TableColumn<Vessel, String> vesselRegistrationNumberCol = new TableColumn<>("船舶登记号");
        vesselRegistrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("vesselRegistrationNumber"));

// 船舶检验类型
        TableColumn<Vessel, String> inspectionTypeCol = new TableColumn<>("船舶检验类型");
        inspectionTypeCol.setCellValueFactory(new PropertyValueFactory<>("inspectionType"));

// 下次检验时间
        TableColumn<Vessel, java.sql.Date> nextInspectionDateCol = new TableColumn<>("下次检验时间");
        nextInspectionDateCol.setCellValueFactory(new PropertyValueFactory<>("nextInspectionDate"));

// 通知时间
        TableColumn<Vessel, java.sql.Date> notificationDateCol = new TableColumn<>("通知时间");
        notificationDateCol.setCellValueFactory(new PropertyValueFactory<>("notificationDate"));

// 检验机关
        TableColumn<Vessel, String> inspectionAuthorityCol = new TableColumn<>("检验机关");
        inspectionAuthorityCol.setCellValueFactory(new PropertyValueFactory<>("inspectionAuthority"));

// 检验证使用有效期至
        TableColumn<Vessel, java.sql.Date> certificateValidityCol = new TableColumn<>("检验证使用有效期至");
        certificateValidityCol.setCellValueFactory(new PropertyValueFactory<>("certificateValidity"));

// 发证日期
        TableColumn<Vessel, java.sql.Date> issueDateCol = new TableColumn<>("发证日期");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

// 船只检验情况记录
        TableColumn<Vessel, String> inspectionRecordCol = new TableColumn<>("船只检验情况记录");
        inspectionRecordCol.setCellValueFactory(new PropertyValueFactory<>("inspectionRecord"));

// 将所有列添加到表格视图
        table.getColumns().addAll(vesselNameCol, registrationNumberCol, certificateNumberCol, ownerCol,
                vesselRegistrationNumberCol, inspectionTypeCol, nextInspectionDateCol, notificationDateCol,
                inspectionAuthorityCol, certificateValidityCol, issueDateCol, inspectionRecordCol);


        // 在这里调用 LoadDataQueryCertificate 方法来填充数据

        data = MysqlConnectiontest.LoadDataQueryCertificate();
        refreshTableView();
//        table.setItems(data);



        // 创建返回按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setFont(new Font("Arial", 16));
        backButton.setOnAction(e -> {
            stage.hide();
            parentStage.show();
        });


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 检查下拉框是否选择了有效选项
            String selectedType = comboBox.getValue();
            if (selectedType != null && !selectedType.isEmpty()) {
                // 搜索框非空时执行搜索
                if (newValue != null && !newValue.isEmpty()) {
                    performSearch(selectedType, newValue, null);
                } else {
                    // 如果搜索框为空，则刷新视图
                    refreshTableView();
                }
            }
        });



// 保持 comboBox 的 setOnAction 不变
        comboBox.setOnAction(event -> {
            String selected = comboBox.getValue();
            if ("下次检验时间".equals(selected) || "通知时间".equals(selected)) {
                datePicker.setVisible(true);
                searchField.setDisable(true); // 禁用搜索框
            } else {
                datePicker.setVisible(false);
                searchField.setDisable(false); // 启用搜索框
            }

            // 触发搜索
            triggerSearch(comboBox.getValue(), searchField.getText(), datePicker.getValue());

        });

        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            if (date == null) {
                refreshTableView();
            } else {
                triggerSearch(comboBox.getValue(), searchField.getText(), date);
            }
        });

        // 使用 BorderPane 来布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topContainer); // 将顶部容器置顶
        borderPane.setCenter(table); // 将表格视图放在中间
        borderPane.setBottom(backButton); // 将返回按钮放在底部
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT); // 将返回按钮对齐到左下角

        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

    private static void performSearch(String searchType, String searchText, LocalDate date) {
        ObservableList<Vessel> filteredData = FXCollections.observableArrayList();

        for (Vessel vessel : data) {
            boolean match = false;
            switch (searchType) {
                case "船名":
                    match = vessel.getVesselName().toLowerCase().contains(searchText.toLowerCase());
                    break;
                case "船检登记号":
                    match = vessel.getRegistrationNumber().toLowerCase().contains(searchText.toLowerCase());
                    break;
                case "船舶检验证编号":
                    match = vessel.getCertificateNumber().toLowerCase().contains(searchText.toLowerCase());
                    break;
                case "下次检验时间":
                    match = vessel.getNextInspectionDate().equals(java.sql.Date.valueOf(date));
                    break;
                case "通知时间":
                    match = vessel.getNotificationDate().equals(java.sql.Date.valueOf(date));
                    break;
            }
            if (match) {
                filteredData.add(vessel);
            }
        }

        table.setItems(filteredData);
    }


    private static void refreshTableView() {
        data = MysqlConnectiontest.LoadDataQueryCertificate();
        table.setItems(data);
    }

    private static void triggerSearch(String searchType, String searchText, LocalDate date) {
        if ("下次检验时间".equals(searchType) || "通知时间".equals(searchType)) {
            if (date != null) {
                performSearch(searchType, null, date);
            } else {
                refreshTableView();
            }
        } else {
            performSearch(searchType, searchText, null);
        }
    }







}
