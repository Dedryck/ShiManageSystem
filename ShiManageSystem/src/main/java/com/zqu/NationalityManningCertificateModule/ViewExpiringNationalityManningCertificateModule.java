package com.zqu.NationalityManningCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-08-21:25
 * @description: 查看到期---船只国籍配员证书资料提醒
 */
public class ViewExpiringNationalityManningCertificateModule {
    private static TableView<ShipNationalityManningCertificate> tableView;

    public static void show(Stage parentStage) {

        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        // 创建新的Stage
        Stage stage = new Stage();
        stage.setTitle("国籍配员证书到期提醒警报");

        // 主布局
        BorderPane borderPane = new BorderPane();

        // 返回上一级按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

        // 提醒按钮
        Button remindButton = new Button("提醒");
        // 提醒全部按钮
        Button remindAllButton = new Button("提醒全部");

        // 初始化 tableView
        tableView = new TableView<>();

// 视图
        TableColumn<ShipNationalityManningCertificate, String> shipNameCol = new TableColumn<>("船名");
        shipNameCol.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<ShipNationalityManningCertificate, String> registrationNumberCol = new TableColumn<>("船检登记号");
        registrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRegistrationNumber"));

        TableColumn<ShipNationalityManningCertificate, String> certificateNumberCol = new TableColumn<>("国籍配员证书编号");
        certificateNumberCol.setCellValueFactory(new PropertyValueFactory<>("nationalityManningCertificateNumber"));

        TableColumn<ShipNationalityManningCertificate, String> ownerCol = new TableColumn<>("船舶所有人");
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("shipOwner"));

        TableColumn<ShipNationalityManningCertificate, String> shipRegNumberCol = new TableColumn<>("船舶登记号");
        shipRegNumberCol.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        TableColumn<ShipNationalityManningCertificate, Date> nextRenewalDateCol = new TableColumn<>("下次换证时间");
        nextRenewalDateCol.setCellValueFactory(new PropertyValueFactory<>("nextCertificateRenewalDate"));

        TableColumn<ShipNationalityManningCertificate, Date> renewalNotificationDateCol = new TableColumn<>("换证通知时间");
        renewalNotificationDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalNotificationDate"));

        TableColumn<ShipNationalityManningCertificate, Date> validityEndDateCol = new TableColumn<>("国籍配员证书使用有效期至");
        validityEndDateCol.setCellValueFactory(new PropertyValueFactory<>("certificateValidityEndDate"));

        TableColumn<ShipNationalityManningCertificate, Date> issueDateCol = new TableColumn<>("发证日期");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<ShipNationalityManningCertificate, String> renewalHistoryCol = new TableColumn<>("国籍配员证书换证时间记录");
        renewalHistoryCol.setCellValueFactory(new PropertyValueFactory<>("certificateRenewalHistory"));

// 将所有列添加到表格视图
        tableView.getColumns().addAll(
                shipNameCol, registrationNumberCol, certificateNumberCol, ownerCol,
                shipRegNumberCol, nextRenewalDateCol, renewalNotificationDateCol,
                validityEndDateCol, issueDateCol, renewalHistoryCol
        );
        reloadData();





        // 按钮容器
        HBox buttonBox = new HBox(10, remindButton, remindAllButton);
        buttonBox.setAlignment(Pos.TOP_CENTER);

        // 表格视图置于中间
        borderPane.setCenter(tableView);
        // 将按钮添加到布局中
        borderPane.setBottom(backButton);
        borderPane.setTop(buttonBox);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);

        // 设置场景和尺寸
        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);

        // 显示Stage
        stage.show();
    }
    public static void reloadData() {
        List<ShipNationalityManningCertificate> data = MysqlConnectiontest.ViewExpiringNationalityManningModuleData();
        tableView.setItems(FXCollections.observableArrayList(data));
    }

}