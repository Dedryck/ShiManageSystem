package com.zqu.ShipOperationCertificate;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

//import static com.zqu.AdministratorModule.ModifyWindow.tableView;

/**
 * @aurhor Dedryck
 * @create 2023-12-03-11:15
 * @description:船只营运证书到期提醒
 */
public class CertificateReminderModule {
    public static void showCertificateReminder(Stage parentStage, Optional<List<VesselOperationCertificate>> certificatesOptional) {
        Stage stage = new Stage();
        stage.setTitle("证书到期提醒");

        // 返回按钮
        Button returnButton = new Button("<----返回上一级");
        returnButton.setOnAction(event -> {
            parentStage.show();
            stage.close();
        });

        // 提醒按钮
        Button reminderButton = new Button("提醒");
        // TODO: 添加提醒按钮的逻辑
        // 全部提醒按钮
        Button allReminderButton = new Button("全部提醒");

        // 创建顶部的 HBox
        HBox topContainer = new HBox(10, reminderButton,allReminderButton);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(10));

        // 创建中间的 TableView
        TableView<VesselOperationCertificate> tableView = new TableView<>();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // 提醒按钮的逻辑
        reminderButton.setOnAction(event -> {
            // TODO: 添加提醒按钮的具体逻辑
            List<VesselOperationCertificate> selectedItems = tableView.getSelectionModel().getSelectedItems();

        });

        // 全部提醒按钮的逻辑
        allReminderButton.setOnAction(event -> {
            // TODO: 添加全部提醒按钮的具体逻辑
            tableView.getSelectionModel().selectAll();
            List<VesselOperationCertificate> allItems = tableView.getItems();

        });




        // TODO: 配置 TableView，例如添加列和数据
        // 创建列并设置单元格值工厂，使用中文标签
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

        // 添加列到TableView
        tableView.getColumns().addAll(vesselNameCol, registrationNumberCol, certificateNumberCol, ownerCol, vesselRegistrationNumberCol, operatorLicenseNumberCol, managerLicenseNumberCol, issuingAuthorityCol, validityDateCol, issueDateCol);

        // 填充数据到表格视图
        // 从数据库获取数据
//        ObservableList<VesselOperationCertificate> certificates = FXCollections.observableArrayList(MysqlConnectiontest.getVesselOperationCertificates());
//        tableView.setItems(certificates);

        // 填充数据到表格视图
        if (certificatesOptional.isPresent()) {
            ObservableList<VesselOperationCertificate> certificates = FXCollections.observableArrayList(certificatesOptional.get());
            tableView.setItems(certificates);
        }





        // 设置布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topContainer);
        borderPane.setCenter(tableView);
        borderPane.setBottom(returnButton);
        BorderPane.setAlignment(returnButton, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(returnButton, new Insets(10));

        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);

        // 隐藏上级窗体，并显示当前窗体
        parentStage.hide();
        stage.show();
    }
}
