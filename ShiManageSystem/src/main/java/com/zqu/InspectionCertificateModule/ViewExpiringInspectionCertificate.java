package com.zqu.InspectionCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @aurhor Dedryck
 * @create 2023-12-06-21:56
 * @description:显示到期证书
 */
public class ViewExpiringInspectionCertificate {

    private static TableView<Vessel> table = new TableView<>();
    private static ObservableList<Vessel> data = FXCollections.observableArrayList();

    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("即将到期的船只检验证书");
        stage.setWidth(1024);
        stage.setHeight(768);

        BorderPane borderPane = new BorderPane();


        // 船名
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


        // 在这里调用 LoadDataQueryCertificate 方法来填充数据

        data = MysqlConnectiontest.LoadDataQueryCertificate();
        table.setItems(data);

        // 返回上一级按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setOnAction(event -> {
            parentStage.show(); // 显示父窗体
            stage.close(); // 关闭当前窗体
        });

        VBox bottomBox = new VBox(backButton);
        bottomBox.setAlignment(Pos.BOTTOM_LEFT);
        borderPane.setBottom(bottomBox);

        // 提醒按钮和全部提醒按钮
        Button remindButton = new Button("提醒");
        Button remindAllButton = new Button("全部提醒");

        // 提醒按钮逻辑 (需要根据实际情况填充)
        remindButton.setOnAction(event -> {
            // 提醒逻辑
        });

        // 全部提醒按钮逻辑 (需要根据实际情况填充)
        remindAllButton.setOnAction(event -> {
            // 全部提醒逻辑
        });

        HBox buttonBox = new HBox(10, remindButton, remindAllButton);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(table); // 将表格视图放在中间
        buttonBox.setPadding(new Insets(10));

        borderPane.setTop(buttonBox);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        parentStage.hide(); // 隐藏父窗体
        stage.show(); // 显示新窗体
    }
}
