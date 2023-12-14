package com.zqu.NationalityManningCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Date;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-08-16:19
 * @description: 删除功能——国籍配员证书基本信息表
 */
public class DeleteNationalityManningSecurityModule {
    private static TableView<ShipNationalityManningCertificate> tableView;


    public static void show(Stage parentStage) {
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("船只国籍配员证书资料管理模块");

        // 创建返回按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

        // 创建删除、确认、取消按钮
        Button deleteButton = new Button("删除功能");
        Button confirmButton = new Button("确认");
        Button cancelButton = new Button("取消");
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

        // 创建返回按钮的HBox
        HBox backButtonBox = new HBox();
        backButtonBox.setAlignment(Pos.BOTTOM_LEFT); // 对齐到左下角
        backButtonBox.getChildren().add(backButton);

        // 创建中央按钮的HBox
        HBox centerButtonsBox = new HBox(10); // 间距为10
        centerButtonsBox.setAlignment(Pos.CENTER); // 居中对齐
        centerButtonsBox.getChildren().addAll(deleteButton, confirmButton, cancelButton);

        // 删除按钮的事件处理
        deleteButton.setOnAction(e -> {
            ObservableList<ShipNationalityManningCertificate> selectedItems = tableView.getSelectionModel().getSelectedItems();

                confirmButton.setVisible(true);
                cancelButton.setVisible(true);
        });

        cancelButton.setOnAction(e -> {
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
        });

        // 确认按钮的事件处理
        confirmButton.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "此操作不可逆，你确定要继续删除操作吗？", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // 执行删除操作
                    ObservableList<ShipNationalityManningCertificate> selectedItems = tableView.getSelectionModel().getSelectedItems();
                    boolean deleteSuccess = true;

                    for (ShipNationalityManningCertificate item : selectedItems) {
                        String shipName = item.getShipName();
                        String shipInspectionRegNumber = item.getShipInspectionRegistrationNumber();

                        // 调用删除方法
                        boolean success = MysqlConnectiontest.DeleteNationalityManningMoudleData(shipName, shipInspectionRegNumber);
                        if (!success) {
                            deleteSuccess = false; // 如果任何一个删除失败，则整体失败
                            break;
                        }
                    }

                    if (deleteSuccess) {
                        // 删除成功，显示提示信息并刷新数据
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "删除成功");
                        successAlert.show();
                        reloadData();
                    } else {
                        // 删除失败，显示提示信息
                        Alert failureAlert = new Alert(Alert.AlertType.ERROR, "删除失败");
                        failureAlert.show();
                    }

                    confirmButton.setVisible(false);
                    cancelButton.setVisible(false);
                }
            });
        });



        // 初始化 tableView
        tableView = new TableView<>();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


// 定义表格的列
        TableColumn<ShipNationalityManningCertificate, String> shipNameCol = new TableColumn<>("船名");
        shipNameCol.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<ShipNationalityManningCertificate, String> shipInspectionRegNumberCol = new TableColumn<>("船检登记号");
        shipInspectionRegNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRegistrationNumber"));

        TableColumn<ShipNationalityManningCertificate, String> nationalityManningCertNumberCol = new TableColumn<>("国籍配员证书编号");
        nationalityManningCertNumberCol.setCellValueFactory(new PropertyValueFactory<>("nationalityManningCertificateNumber"));

        TableColumn<ShipNationalityManningCertificate, String> shipOwnerCol = new TableColumn<>("船舶所有人");
        shipOwnerCol.setCellValueFactory(new PropertyValueFactory<>("shipOwner"));

        TableColumn<ShipNationalityManningCertificate, String> shipRegistrationNumberCol = new TableColumn<>("船舶登记号");
        shipRegistrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        TableColumn<ShipNationalityManningCertificate, Date> nextCertificateRenewalDateCol = new TableColumn<>("下次换证时间");
        nextCertificateRenewalDateCol.setCellValueFactory(new PropertyValueFactory<>("nextCertificateRenewalDate"));

        TableColumn<ShipNationalityManningCertificate, Date> certificateRenewalNotificationDateCol = new TableColumn<>("换证通知时间");
        certificateRenewalNotificationDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalNotificationDate"));

        TableColumn<ShipNationalityManningCertificate, Date> certificateValidityEndDateCol = new TableColumn<>("国籍配员证书使用有效期至");
        certificateValidityEndDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateValidityEndDate"));

        TableColumn<ShipNationalityManningCertificate, Date> issueDateCol = new TableColumn<>("发证日期");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<ShipNationalityManningCertificate, String> certificateRenewalHistoryCol = new TableColumn<>("国籍配员证书换证时间记录");
        certificateRenewalHistoryCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalHistory"));

// 把列添加到表格视图中
        tableView.getColumns().addAll(shipNameCol, shipInspectionRegNumberCol, nationalityManningCertNumberCol, shipOwnerCol, shipRegistrationNumberCol, nextCertificateRenewalDateCol, certificateRenewalNotificationDateCol, certificateValidityEndDateCol, issueDateCol, certificateRenewalHistoryCol);
        reloadData();

// 布局
        // 使用 BorderPane 布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(centerButtonsBox); // 将中央按钮组置于顶部
        borderPane.setCenter(tableView); // 将表格视图置于中央
        borderPane.setBottom(backButtonBox); // 将返回按钮置于底部

        // 创建场景，将布局应用到场景中
        Scene scene = new Scene(borderPane, 1024, 768);

        // 设置舞台
        stage.setScene(scene);
        stage.show();


    }

    public static void reloadData() {
        List<ShipNationalityManningCertificate> data = MysqlConnectiontest.QueryNationalityManningModuleData();
        tableView.setItems(FXCollections.observableArrayList(data));
    }

}
