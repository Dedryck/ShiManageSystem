package com.zqu.ShipOperationCertificate;

import com.zqu.MysqlConnectiontest;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * @aurhor Dedryck
 * @create 2023-12-04-14:44
 * @description:修改营运证书类
 */
public class ModifyCertificateModule {

    private Stage parentStage;
    private static TableView<VesselOperationCertificate> tableView;  // 类的成员变量


    public ModifyCertificateModule(Stage parentStage) {
        this.parentStage = parentStage;
        this.tableView = new TableView<>();  // 初始化tableView
    }

    public void showWindow() {
        Stage window = new Stage();
        window.setTitle("修改船只营运证书的基本信息");

        // 视图区域 - 使用TableView来显示数据
//        TableView<VesselOperationCertificate> tableView = new TableView<>();

        reloadData(); // 初始加载数据
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

        ObservableList<VesselOperationCertificate> data = FXCollections.observableArrayList(
//                此处调用大写的GetVesselOperationCertificates()方法，从数据库获取数据
                MysqlConnectiontest.GetVesselOperationCertificates()
        );
        tableView.setItems(data);

        // 实现双击视图信息可以打开一个新的修改窗体
        tableView.setRowFactory(tv -> {
            TableRow<VesselOperationCertificate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    VesselOperationCertificate clickedRow = row.getItem();
                    new ModifyCertificateWindow(clickedRow, parentStage, this); // 直接打开新窗体
                }
            });
            return row;
        });




        // 主布局
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(tableView); // 将TableView设置为中心节点

        Button backButton = new Button("<----返回上一级");
        backButton.setOnAction(e -> {
            window.close();
            parentStage.show();
        });
        HBox bottomButtonLayout = new HBox(backButton);
        bottomButtonLayout.setAlignment(Pos.BOTTOM_LEFT);
        bottomButtonLayout.setPadding(new Insets(10));
        mainLayout.setBottom(bottomButtonLayout);

        // 为新窗体设置场景
        Scene scene = new Scene(mainLayout, 1024, 768);
        window.setScene(scene);

        // 隐藏父窗体
        parentStage.hide();

        // 显示当前窗体
        window.show();

        // 当窗口关闭时，显示父窗体
        window.setOnCloseRequest(e -> parentStage.show());
    }
    public static void reloadData() {
        ObservableList<VesselOperationCertificate> data = FXCollections.observableArrayList(
                MysqlConnectiontest.GetVesselOperationCertificates()
        );
        tableView.setItems(data);
    }



}