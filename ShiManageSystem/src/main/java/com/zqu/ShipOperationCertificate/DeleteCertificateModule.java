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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-05-15:27
 * @description:船只营运证书的删除功能
 */
public class DeleteCertificateModule {
    private Stage parentStage;

    public DeleteCertificateModule(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void showWindow() {
        Stage window = new Stage();
        window.setTitle("删除船只营运证书");

        // 创建按钮
        Button returnButton = new Button("<---返回上一级");
        Button deleteFunctionButton = new Button("删除功能");
        Button confirmButton = new Button("确认");
        Button cancelButton = new Button("取消");

        // 初始时设置确认和取消按钮为不可见
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

        // 设置按钮样式
        returnButton.setStyle("-fx-font-size: 16px;");
        deleteFunctionButton.setStyle("-fx-font-size: 14px;");
        confirmButton.setStyle("-fx-font-size: 14px;");
        cancelButton.setStyle("-fx-font-size: 14px;");

        // 按钮事件
        returnButton.setOnAction(event -> {
            window.close();
            parentStage.show();
        });

        deleteFunctionButton.setOnAction(event -> {
            confirmButton.setVisible(true);
            cancelButton.setVisible(true);
        });

        cancelButton.setOnAction(event -> {
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
        });


        // 创建表格视图
        TableView<VesselOperationCertificate> tableView = new TableView<>();
        setupTableView(tableView);


        confirmButton.setOnAction(event -> {
            // 获取选中的船只证书对象
            VesselOperationCertificate selectedCertificate = tableView.getSelectionModel().getSelectedItem();

            // 检查是否有选中的项
            if (selectedCertificate == null) {
                showAlert("错误", "没有选中任何船只！");
                return;
            }

            // 弹出确认对话框
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("确认删除");
            confirmAlert.setHeaderText("删除确认");
            confirmAlert.setContentText("你确定删除 " + selectedCertificate.getVesselName() + " 的船只营运证书的信息吗？");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // 如果用户确认删除
                boolean success = MysqlConnectiontest.deleteVesselOperationCertificate(
                        selectedCertificate.getVesselName(),
                        selectedCertificate.getRegistrationNumber()
                );

                if (success) {
                    showAlert("删除成功", "船只营运证书已被成功删除。");
                    reloadData(tableView); // 重新加载数据
                } else {
                    showAlert("删除失败", "无法删除指定的船只营运证书。");
                }
            }
        });




        // 按钮布局
        HBox buttonsBox = new HBox(10, deleteFunctionButton, confirmButton, cancelButton);
        buttonsBox.setAlignment(Pos.CENTER);

        // 主布局
        BorderPane layout = new BorderPane();
        layout.setTop(buttonsBox);
        layout.setCenter(tableView); // 添加表格视图到布局中间
        layout.setBottom(returnButton);
        BorderPane.setAlignment(returnButton, Pos.BOTTOM_LEFT);

        // 创建场景
        Scene scene = new Scene(layout, 1024, 768);
        window.setScene(scene);
        window.show();

        // 隐藏父窗体
        parentStage.hide();
    }



    private void setupTableView(TableView<VesselOperationCertificate> tableView) {
        // 表格设置

        tableView.setPadding(new Insets(10));

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


        // 从数据库加载数据
        ObservableList<VesselOperationCertificate> data = FXCollections.observableArrayList(
                MysqlConnectiontest.GetVesselOperationCertificates()
        );
        tableView.setItems(data);
        tableView.setPadding(new Insets(10));
    }


    private void reloadData(TableView<VesselOperationCertificate> tableView) {
        ObservableList<VesselOperationCertificate> data = FXCollections.observableArrayList(
                MysqlConnectiontest.GetVesselOperationCertificates()
        );
        tableView.setItems(data);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
