package com.zqu.ShipOperationCertificate;

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
import javafx.stage.Stage;

import java.time.LocalDate;

//import static com.zqu.AdministratorModule.ModifyWindow.tableView;

/**
 * @aurhor Dedryck
 * @create 2023-12-03-20:43
 * @description:查询营运证书的基本信息
 */
public class QueryCertificateModule {
    private Stage parentStage; // 主窗体的引用


    public void showWindow(Stage previousStage) {
        Stage window = new Stage();
        window.setTitle("查询营运证书的基本信息");

        // 下拉选项框
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("船只名称", "船检登记号", "船舶营运证号");
        // 创建 TextField 组件
        TextField textField = new TextField();
        textField.setMaxWidth(200); // 设置最大宽度


        // 按钮和下拉选项布局
        HBox topContainer = new HBox(10, comboBox, textField);
        topContainer.setAlignment(Pos.CENTER);
        // 设置topContainer的内边距，以增加其与下方组件的距离
        topContainer.setPadding(new Insets(3, 0, 10, 0)); // 上, 右, 下, 左 的内边距


        // 返回上一级按钮
        Button backButton = new Button("<----返回上一级");
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> {
            window.hide();
            previousStage.show();
        });

        // 视图区域 - 使用TableView替代VBox来显示数据
        TableView<VesselOperationCertificate> tableView = new TableView<>();
        tableView.setPadding(new Insets(10));

// 主布局
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topContainer);
        mainLayout.setCenter(tableView); // 将TableView设置为中心节点
        mainLayout.setBottom(backButton);

// 定义表格列，并绑定到VesselOperationCertificate类的属性
        TableColumn<VesselOperationCertificate, String> vesselNameCol = new TableColumn<>("船名");
        vesselNameCol.setCellValueFactory(new PropertyValueFactory<>("vesselName"));

        TableColumn<VesselOperationCertificate, String> registrationNumberCol = new TableColumn<>("船检登记号");
        registrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));

        TableColumn<VesselOperationCertificate, String> certificateNumberCol = new TableColumn<>("营运证编号");
        certificateNumberCol.setCellValueFactory(new PropertyValueFactory<>("certificateNumber"));

        TableColumn<VesselOperationCertificate, String> ownerCol = new TableColumn<>("船舶所有人");
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));

        TableColumn<VesselOperationCertificate, String> vesselRegistrationNumberCol = new TableColumn<>("船舶登记号");
        vesselRegistrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("vesselRegistrationNumber"));

        TableColumn<VesselOperationCertificate, String> operatorLicenseNumberCol = new TableColumn<>("经营人许可证号码");
        operatorLicenseNumberCol.setCellValueFactory(new PropertyValueFactory<>("operatorLicenseNumber"));

        TableColumn<VesselOperationCertificate, String> managerLicenseNumberCol = new TableColumn<>("管理人许可证号码");
        managerLicenseNumberCol.setCellValueFactory(new PropertyValueFactory<>("managerLicenseNumber"));

        TableColumn<VesselOperationCertificate, String> issuingAuthorityCol = new TableColumn<>("发证机关");
        issuingAuthorityCol.setCellValueFactory(new PropertyValueFactory<>("issuingAuthority"));

        TableColumn<VesselOperationCertificate, LocalDate> validityDateCol = new TableColumn<>("营运证使用有效期至");
        validityDateCol.setCellValueFactory(new PropertyValueFactory<>("validityDate"));

        TableColumn<VesselOperationCertificate, LocalDate> issueDateCol = new TableColumn<>("发证日期");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

// 将列添加到表格视图
        tableView.getColumns().addAll(
                vesselNameCol, registrationNumberCol, certificateNumberCol,
                ownerCol, vesselRegistrationNumberCol, operatorLicenseNumberCol,
                managerLicenseNumberCol, issuingAuthorityCol, validityDateCol,
                issueDateCol
        );

        ObservableList<VesselOperationCertificate> data = FXCollections.observableArrayList(
//                此处调用大写的GetVesselOperationCertificates()方法，从数据库获取数据
                MysqlConnectiontest.GetVesselOperationCertificates()
        );
        tableView.setItems(data);


        // 设置一个事件处理器来监听下拉框的变化
        comboBox.setOnAction(event -> {
            filterTableView_QueryCertificateModule(textField.getText(), comboBox.getValue(), tableView, data);
        });


        // 为TextField设置监听器，以便在文本更改时触发操作
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTableView_QueryCertificateModule(newValue, comboBox.getValue(), tableView, data);
        });


        // 设置布局边距和对齐
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(backButton, new Insets(10));

        Scene scene = new Scene(mainLayout, 1024, 768);
        window.setScene(scene);
        previousStage.hide(); // 隐藏上级窗体
        window.show(); // 显示当前窗体
    }

    // 这是一个过滤表格视图内容的方法
    public void filterTableView_QueryCertificateModule(String searchText, String filterOption, TableView<VesselOperationCertificate> tableView, ObservableList<VesselOperationCertificate> dataList) {
        if (searchText.isEmpty()) {
            // 如果搜索框内容为空，则使用原始数据集刷新视图
            tableView.setItems(dataList);
            tableView.refresh();
        } else {
            // 创建一个新的过滤后的数据列表
            ObservableList<VesselOperationCertificate> filteredData = FXCollections.observableArrayList();

            // 根据下拉框选择的过滤选项和搜索框的文本来过滤数据
            for (VesselOperationCertificate item : dataList) {
                // 这里是模糊匹配逻辑，根据实际字段进行调整
                String toCompare = "";
                switch (filterOption) {
                    case "船只名称":
                        toCompare = item.getVesselName();
                        break;
                    case "船检登记号":
                        toCompare = item.getRegistrationNumber();
                        break;
                    case  "船舶营运证号":
                        toCompare = item.getCertificateNumber();
                        break;
                    // ...添加更多的case来处理其他选项
                }

                // 如果匹配，则添加到过滤后的列表中
                if (toCompare.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredData.add(item);
                }
            }

            // 更新表格视图显示过滤后的数据
            tableView.setItems(filteredData);
        }
    }
}
