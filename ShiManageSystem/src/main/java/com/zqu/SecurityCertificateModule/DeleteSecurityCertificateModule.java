package com.zqu.SecurityCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-17:09
 * @description:删除操作模块
 */
public class DeleteSecurityCertificateModule {

    public static TableView<ShipSecurityCertificate> table;
    // 显示窗体的方法
    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("删除安检证书");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小
        stage.setWidth(1024);
        stage.setHeight(768);

        // 创建布局
        VBox layout = new VBox(10); // 使用垂直布局，元素间距10
        layout.setAlignment(Pos.CENTER);

        // 创建按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);
        Button deleteOperationButton = new Button("删除操作");
        deleteOperationButton.setStyle(buttonStyle);
        Button confirmButton = new Button("确认");
        deleteOperationButton.setStyle("-fx-font-size: 12px;");
        Button cancelButton = new Button("取消");
        cancelButton.setStyle("-fx-font-size: 12px;");


        // 初始状态：确认和取消按钮不可见
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

//        添加视图
        table = new TableView<>();
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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



        // 添加按钮事件
        backButton.setOnAction(event -> {
            // 返回上一级界面
            parentStage.show();
            stage.close();
        });

        deleteOperationButton.setOnAction(event -> {
            // 显示确认和取消按钮
            confirmButton.setVisible(true);
            cancelButton.setVisible(true);
        });

        cancelButton.setOnAction(event -> {
            // 隐藏确认和取消按钮，并取消选中的视图行
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
            // TODO: 取消选中的视图行
        });

        // TODO: 实现确认按钮的逻辑
        confirmButton.setOnAction(event -> {
            ObservableList<ShipSecurityCertificate> selectedItems = table.getSelectionModel().getSelectedItems();

            if (!selectedItems.isEmpty()) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "此删除操作不可逆，你确定要继续吗？", ButtonType.YES, ButtonType.NO);
                confirmationAlert.showAndWait();

                if (confirmationAlert.getResult() == ButtonType.YES) {
                    // 用户确认删除
                    List<ShipSecurityCertificate> toBeDeleted = new ArrayList<>(selectedItems); // 创建一个副本以避免并发修改异常
                    boolean allDeleted = true;
                    for (ShipSecurityCertificate certificate : toBeDeleted) {
                        boolean success = MysqlConnectiontest.DeleteSecurityCertificateData(certificate.getShipName(), certificate.getShipInspectionRegistrationNumber());
                        if (!success) {
                            allDeleted = false;
                        }
                    }
                    reloadData(); // 刷新视图

                    // 显示删除结果的提示
                    String message = allDeleted ? "删除成功！" : "部分记录未能成功删除。";
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, message);
                    infoAlert.showAndWait();
                }
            } else {
                Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING, "没有选中任何记录。");
                noSelectionAlert.showAndWait();
            }
        });



        // 将按钮添加到布局
//        上面的三个按钮
        HBox buttonGroup = new HBox(10); // 按钮之间的间隔设置为10
        buttonGroup.setAlignment(Pos.CENTER); // 设置居中对齐
        buttonGroup.getChildren().addAll(deleteOperationButton, confirmButton, cancelButton);


        BorderPane borderPane = new BorderPane();
        borderPane.setTop(buttonGroup); // 将按钮组置顶
        borderPane.setCenter(table); // 将表格放置在中央
        borderPane.setBottom(backButton); // 将返回按钮放在底部
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT); // 将按钮定位到左下角


        // TODO: 创建并添加视图到布局

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();

        // 隐藏上级窗体
        parentStage.hide();
    }




    public static void reloadData() {
        // 从数据库加载数据
        List<ShipSecurityCertificate> certificates = MysqlConnectiontest.loadDataSecurityCertificate();

        // 将数据转换为ObservableList
        ObservableList<ShipSecurityCertificate> observableList = FXCollections.observableArrayList(certificates);

        // 更新表格数据
        table.setItems(observableList);

    }
}
