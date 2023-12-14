package com.zqu.System_management_module;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.zqu.OperationRecord;import com.zqu.OperationRecord;

import java.time.format.DateTimeFormatter;


/**
 * @aurhor Dedryck
 * @create 2023-11-29-10:47
 * @description:用于“用户操作查询”的具体实现
 */
public class UserOperationQuery {
    private ComboBox<String> searchComboBox; // 搜索下拉框
    private TextField searchTextField; // 搜索文本框
    private TableView<OperationRecord> tableView; // 操作记录视图
    private DatePicker datePicker; // 添加一个DatePicker作为成员变量

    public void showOperationQuery(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("用户操作查询");
        stage.setWidth(850);
        stage.setHeight(650);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(10);

        // 创建返回按钮
        Button backButton = new Button("<--返回上一级");
        backButton.setMinHeight(50); // 增加按钮的高度
        backButton.setMinWidth(200); // 增加按钮的宽度
        gridPane.add(backButton, 0, 4);
        GridPane.setHalignment(backButton, HPos.LEFT);
        backButton.setOnAction(e -> {
            stage.close();
            parentStage.show();
        });

        // 创建搜索选择标签
        Label searchChoiceLabel = new Label("搜索选择：");

        // 初始化下拉框和搜索框
        searchComboBox = new ComboBox<>();
        searchComboBox.getItems().addAll("用户ID", "操作类型", "操作时间");
        searchComboBox.setMaxWidth(150); // 设置下拉框的最大宽度

        searchTextField = new TextField();
        searchTextField.setMaxWidth(200); // 设置搜索框的最大宽度

        // 初始化DatePicker
        datePicker = new DatePicker();
        datePicker.setMaxWidth(150); // 设置与搜索框相同的宽度

        // 创建HBox用于紧密排列下拉框和搜索框
        HBox searchBox = new HBox(5);
        searchBox.getChildren().addAll(searchComboBox, searchTextField); // 初始时添加搜索框

//        searchBox.getChildren().add(searchComboBox);
//        searchBox.getChildren().add(searchTextField); // 默认添加搜索框

        // 设置下拉框的值变化监听器
        searchComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if ("操作时间".equals(newValue)) {
                searchBox.getChildren().remove(searchTextField);
                if (!searchBox.getChildren().contains(datePicker)) {
                    searchBox.getChildren().add(datePicker);
                }
            } else {
                searchBox.getChildren().remove(datePicker);
                if (!searchBox.getChildren().contains(searchTextField)) {
                    searchBox.getChildren().add(searchTextField);
                }
            }
        });

        // 设置搜索框和日期选择器的事件监听器
        searchTextField.textProperty().addListener((obs, oldVal, newVal) -> filterTableData(newVal));

        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) { // 检查日期是否改变
                searchTextField.clear();
                reloadTableData(); // 重新加载数据
                filterTableData(null); // 基于新日期进行筛选
            }
        });

        // 将标签和搜索相关组件添加到布局
        gridPane.add(searchChoiceLabel, 0, 0);
        gridPane.add(searchBox, 1, 0);

        // 设置GridPane的列约束以使TableView填满水平空间
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.SOMETIMES);
        gridPane.getColumnConstraints().addAll(col1, col2, col3);

        // 创建视图并添加到布局
        tableView = createTableView();
        gridPane.add(tableView, 0, 1, 3, 3);

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);

        // 隐藏上一级窗口并显示当前窗口
        parentStage.hide();
        stage.show();
    }

// filterTableData 方法和 createTableView 方法保持不变

    private TableView<OperationRecord> createTableView() {
        TableView<OperationRecord> tableView = new TableView<>();
        // 创建列
        TableColumn<OperationRecord, String> operationIdColumn = new TableColumn<>("操作记录ID");
        operationIdColumn.setCellValueFactory(new PropertyValueFactory<>("operationId"));

        TableColumn<OperationRecord, String> userIdColumn = new TableColumn<>("用户ID");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<OperationRecord, String> operationTypeColumn = new TableColumn<>("操作类型");
        operationTypeColumn.setCellValueFactory(new PropertyValueFactory<>("operationType"));

        TableColumn<OperationRecord, String> operationTimeColumn = new TableColumn<>("操作时间");
        operationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("operationTime"));

        TableColumn<OperationRecord, String> operationDetailColumn = new TableColumn<>("操作细节");
        operationDetailColumn.setCellValueFactory(new PropertyValueFactory<>("operationDetail"));

        tableView.getColumns().addAll(operationIdColumn, userIdColumn, operationTypeColumn, operationTimeColumn, operationDetailColumn);

//         从数据库加载数据
        MysqlConnectiontest.loadOperationRecords(tableView);
// 设置列宽以填充表格空间
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // 创建并添加列到视图，例如 "操作记录ID", "用户ID" 等
        // 注意：你需要根据实际的数据模型来创建这些列

        return tableView;
    }




//    用于选择框搜索内容
    private void filterTableData(String searchText) {
        if (tableView == null || tableView.getItems() == null) {
            return;
        }
        // 如果搜索框为空，则重新加载所有数据
        if ((searchText == null || searchText.isEmpty()) && !("操作时间".equals(searchComboBox.getValue()))) {
            reloadTableData();
            return;
        }

        String selectedOption = searchComboBox.getValue();
        if (selectedOption == null) {
            return;
        }

        ObservableList<OperationRecord> filteredData = FXCollections.observableArrayList();
        String selectedDate = (datePicker != null && datePicker.getValue() != null) ? datePicker.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;

        for (OperationRecord record : tableView.getItems()) {
            boolean match = false;
            if ("操作时间".equals(selectedOption) && selectedDate != null) {
                // 假设operationTime是 'YYYY-MM-DD HH:MM:SS' 格式
                String operationDatePart = record.getOperationTime() != null
                        ? record.getOperationTime().split(" ")[0]
                        : "";
                match = operationDatePart.equals(selectedDate);
//                match = record.getOperationTime() != null && record.getOperationTime().equals(selectedDate);
            } else if ("用户ID".equals(selectedOption)) {
                match = record.getUserId() != null && record.getUserId().toLowerCase().contains(searchText.toLowerCase());
            } else if ("操作类型".equals(selectedOption)) {
                match = record.getOperationType() != null && record.getOperationType().toLowerCase().contains(searchText.toLowerCase());
            }

            if (match) {
                filteredData.add(record);
            }
        }

        tableView.setItems(filteredData);

        if (filteredData.isEmpty()) {
            showAlert("未找到", "你输入的值不在记录中");
            searchTextField.clear();
            reloadTableData(); // 重新加载或刷新表格数据
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

//    private boolean search(String searchText) {
//        String selectedOption = searchComboBox.getValue();
//        for (OperationRecord record : tableView.getItems()) {
//            boolean match = false;
//
//            switch (selectedOption) {
//                case "用户ID":
//                    match = record.getUserId().equals(searchText);
//                    break;
//                case "操作类型":
//                    match = record.getOperationType().equalsIgnoreCase(searchText);
//                    break;
//                case "操作时间":
//                    match = record.getOperationTime().equalsIgnoreCase(searchText);
//                    break;
//                // 可以添加其他选项的匹配逻辑
//            }
//
//            if (match) {
//                tableView.getSelectionModel().select(record);
//                tableView.scrollTo(record);
//                return true;
//            }
//        }
//        return false;
//    }
    private void reloadTableData() {
        MysqlConnectiontest.loadOperationRecords(tableView);
    }




}
