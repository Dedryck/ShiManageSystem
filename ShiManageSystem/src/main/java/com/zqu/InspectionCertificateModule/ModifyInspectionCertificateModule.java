package com.zqu.InspectionCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @aurhor Dedryck
 * @create 2023-12-06-14:23
 * @description:修改检验证书的基本信息
 */
public class ModifyInspectionCertificateModule {

    private static TableView<Vessel> table = new TableView<>();
    private static ObservableList<Vessel> data = FXCollections.observableArrayList();


    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("修改船只检验证书信息窗口");
        // 添加初始化表格数据的逻辑
        initializeTableData();
        String buttonStyle = "-fx-font-size: 16px;";

        BorderPane borderPane = new BorderPane();

        // 返回按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> {
            stage.hide(); // 隐藏当前窗体
            parentStage.show(); // 显示父窗体
        });

        VBox bottomBox = new VBox(backButton);
        bottomBox.setAlignment(Pos.BOTTOM_LEFT);
        borderPane.setBottom(bottomBox);



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

        data = MysqlConnectiontest.LoadDataQueryCertificate();
        table.setItems(data);



        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!table.getSelectionModel().getSelectedItems().isEmpty())) {
                Vessel selectedVessel = table.getSelectionModel().getSelectedItem();
                ModifyInspectionCertificateWindow.show(selectedVessel, stage, ModifyInspectionCertificateModule::initializeTableData);
                stage.hide(); // 如果需要，隐藏当前窗体
            }
        });





        borderPane.setCenter(table);

        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);

        parentStage.hide(); // 隐藏父窗体
        stage.show(); // 显示新窗体
    }

    public static void initializeTableData() {
        // 从数据库加载数据到表格中
        data = MysqlConnectiontest.LoadDataQueryCertificate();
        table.setItems(data);
    }

}
