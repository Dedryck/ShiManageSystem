package com.zqu.NationalityManningCertificateModule;

import com.zqu.InspectionCertificateModule.Vessel;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-08-13:14
 * @description: 修改功能-->船只国籍配员证书基本资料的
 */
public class ModifyNationalitySecurityModule {

    private static TableView<ShipNationalityManningCertificate> table = new TableView<>();
    private static ObservableList<ShipNationalityManningCertificate> data = FXCollections.observableArrayList();


    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("修改船只国籍配员证书基本资料模块");
        stage.setWidth(1024);
        stage.setHeight(768);
        BorderPane borderPane = new BorderPane();
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小



        Button returnButton = new Button("<---返回上一级");
        returnButton.setStyle(buttonStyle);

        returnButton.setOnAction(event -> {
            stage.hide();
            parentStage.show();
        });


// 船名
        TableColumn<ShipNationalityManningCertificate, String> shipNameCol = new TableColumn<>("船名");
        shipNameCol.setCellValueFactory(new PropertyValueFactory<>("shipName"));

// 船检登记号
        TableColumn<ShipNationalityManningCertificate, String> shipInspectionRegistrationNumberCol = new TableColumn<>("船检登记号");
        shipInspectionRegistrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRegistrationNumber"));

// 国籍配员证书编号
        TableColumn<ShipNationalityManningCertificate, String> nationalityManningCertificateNumberCol = new TableColumn<>("国籍配员证书编号");
        nationalityManningCertificateNumberCol.setCellValueFactory(new PropertyValueFactory<>("nationalityManningCertificateNumber"));

// 船舶所有人
        TableColumn<ShipNationalityManningCertificate, String> shipOwnerCol = new TableColumn<>("船舶所有人");
        shipOwnerCol.setCellValueFactory(new PropertyValueFactory<>("shipOwner"));

// 船舶登记号
        TableColumn<ShipNationalityManningCertificate, String> shipRegistrationNumberCol = new TableColumn<>("船舶登记号");
        shipRegistrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

// 下次换证时间
        TableColumn<ShipNationalityManningCertificate, java.sql.Date> nextCertificateRenewalDateCol = new TableColumn<>("下次换证时间");
        nextCertificateRenewalDateCol.setCellValueFactory(new PropertyValueFactory<>("nextCertificateRenewalDate"));

// 换证通知时间
        TableColumn<ShipNationalityManningCertificate, java.sql.Date> certificateRenewalNotificationDateCol = new TableColumn<>("换证通知时间");
        certificateRenewalNotificationDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalNotificationDate"));

// 国籍配员证书使用有效期至
        TableColumn<ShipNationalityManningCertificate, java.sql.Date> certificateValidityEndDateCol = new TableColumn<>("国籍配员证书使用有效期至");
        certificateValidityEndDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateValidityEndDate"));

// 发证日期
        TableColumn<ShipNationalityManningCertificate, java.sql.Date> issueDateCol = new TableColumn<>("发证日期");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

// 国籍配员证书换证时间记录
        TableColumn<ShipNationalityManningCertificate, String> certificateRenewalHistoryCol = new TableColumn<>("国籍配员证书换证时间记录");
        certificateRenewalHistoryCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalHistory"));

// 将所有列添加到表格视图
        table.getColumns().addAll(shipNameCol, shipInspectionRegistrationNumberCol, nationalityManningCertificateNumberCol, shipOwnerCol,
                shipRegistrationNumberCol, nextCertificateRenewalDateCol, certificateRenewalNotificationDateCol,
                certificateValidityEndDateCol, issueDateCol, certificateRenewalHistoryCol);



        // 在这里调用 LoadDataQueryCertificate 方法来填充数据

// 在这里调用 LoadDataQueryCertificate 方法来填充数据
        List<ShipNationalityManningCertificate> list = MysqlConnectiontest.LoadDataNationalityManning();

// 将 List 转换为 ObservableList
        data = FXCollections.observableArrayList(list);
        refreshTableView();


        table.setRowFactory(tv -> {
            TableRow<ShipNationalityManningCertificate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    ShipNationalityManningCertificate clickedRow = row.getItem();
                    // 打开新窗体
                    ModifyNationalitySecurityWindow.show(stage, clickedRow);
                    // 隐藏当前窗体
                    stage.hide();
                }
            });
            return row;
        });


        // 将返回按钮放在VBox中，以便在底部居左显示
        VBox bottomBox = new VBox(returnButton);
        bottomBox.setAlignment(Pos.BOTTOM_LEFT); // 设置按钮对齐方式为左下角
        borderPane.setBottom(bottomBox); // 将按钮添加到布局底部
        // 将表格视图添加到布局中央
        borderPane.setCenter(table);

        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);

        stage.show();
        parentStage.hide();
    }


    private static void refreshTableView() {
        // 获取 List 类型的数据
        List<ShipNationalityManningCertificate> list = MysqlConnectiontest.LoadDataNationalityManning();

        // 将 List 转换为 ObservableList
        data = FXCollections.observableArrayList(list);

        // 将 ObservableList 设置为表格的数据源
        table.setItems(data);
    }

    public static void refreshTable() {
        refreshTableView();
    }






}
