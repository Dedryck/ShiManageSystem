package com.zqu.SecurityCertificateModule;


import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-13:45
 * @description:修改安检证书的类
 */
public class ModifySecurityCertificateModule {
    public static TableView<ShipSecurityCertificate> table;
    private static boolean isTableAdded = false; // 标志，用于跟踪是否已添加表格

    public static void show(Stage parentStage) {
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小
//        reflash();
        // 创建新窗体
        Stage modifyStage = new Stage();
        modifyStage.setTitle("修改安检证书");
        modifyStage.setWidth(1024);
        modifyStage.setHeight(768);

        // 创建布局和控件
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.BOTTOM_CENTER); // 确保布局从窗体底部开始布局控件
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);
        // 如果表格未被添加，则创建并设置表格

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

        isTableAdded = true;


        // 设置“返回上一级”按钮的行为
        backButton.setOnAction(e -> {
            table.getSelectionModel().clearSelection(); // 清除选中的行
            modifyStage.close();
            parentStage.show();

        });


        // 在 ModifySecurityCertificateModule 类中添加此代码
        table.setRowFactory(tv -> {
            TableRow<ShipSecurityCertificate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    ShipSecurityCertificate clickedRow = row.getItem();
                    ModifySecurityCertificateWindow.show(clickedRow, modifyStage, () -> {
                        // 这里添加刷新表格的代码
                        reloadData();
                        table.getSelectionModel().clearSelection(); // 清除选中的行

//                        modifyStage.hide();
                    });
                    modifyStage.hide();
                }
            });
            return row;
        });



        // 使用AnchorPane来定位按钮
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(backButton);
        AnchorPane.setBottomAnchor(backButton, 10.0); // 设置按钮距底部的距离
        AnchorPane.setLeftAnchor(backButton, 10.0);   // 设置按钮距左侧的距离

        // 将表格和AnchorPane添加到VBox布局中
        layout.getChildren().addAll(table, anchorPane);
        VBox.setVgrow(table, Priority.ALWAYS); // 使表格填充额外空间

        // 设置场景和显示
        Scene scene = new Scene(layout);
        modifyStage.setScene(scene);
        modifyStage.show();
    }

    public static void reloadData() {
        // 从数据库加载数据
        List<ShipSecurityCertificate> certificates = MysqlConnectiontest.loadDataSecurityCertificate();


        // 将数据转换为ObservableList
        ObservableList<ShipSecurityCertificate> observableList = FXCollections.observableArrayList(certificates);

        // 更新表格数据
        table.setItems(observableList);

    }

    public static void reflash(){
        table.getSelectionModel().clearSelection(); // 清除选中的行
    }
}
