package com.zqu.ShipOperationCertificate;

import com.zqu.MysqlConnectiontest;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-02-20:37
 * @description:营运证书资料管理
 */
public class VesselOperationCertificateData {

    private Stage parentStage;

    public VesselOperationCertificateData(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void showWindow() {
        Stage window = new Stage();
        window.setTitle("营运证书资料管理");

        Button addNewCertificateButton = new Button("录入新增船只营运证书");
        Button queryCertificateButton = new Button("查询相关的营运证书的基本信息");
        Button modifyCertificateButton = new Button("修改船只营运证书的基本信息");
        Button deleteCertificateButton = new Button("删除船只营运证书");
        Button CertificateReminderButton = new Button("即将到期的船只运营证书");
        addNewCertificateButton.setStyle("-fx-font-size: 14px;");
        queryCertificateButton.setStyle("-fx-font-size: 14px;");
        modifyCertificateButton.setStyle("-fx-font-size: 14px;");
        deleteCertificateButton.setStyle("-fx-font-size: 14px;");
        CertificateReminderButton.setStyle("-fx-font-size: 14px;");

        VBox centerButtons = new VBox(10, addNewCertificateButton, queryCertificateButton, modifyCertificateButton, deleteCertificateButton, CertificateReminderButton);
        centerButtons.setAlignment(Pos.CENTER);


        addNewCertificateButton.setOnAction(event -> {
              AddCertificateWindow addCertificateWindow = new AddCertificateWindow(window);
              addCertificateWindow.show();
        });

        queryCertificateButton.setOnAction(event -> {
            //            TODO:
            QueryCertificateModule queryModule = new QueryCertificateModule();
            queryModule.showWindow(window);

        });

        modifyCertificateButton.setOnAction(event -> {
            //            TODO:
            // 创建ModifyCertificateModule的实例，传递当前的Stage作为父窗口
            ModifyCertificateModule modifyModule = new ModifyCertificateModule(window);
            // 显示新窗口
            modifyModule.showWindow();

        });

        deleteCertificateButton.setOnAction(event -> {
            //            TODO:
            DeleteCertificateModule deleteModule = new DeleteCertificateModule(window);
            deleteModule.showWindow();
        });


//        证书到期提醒
        CertificateReminderButton.setOnAction(event -> {
            // 获取证书列表
            List<VesselOperationCertificate> certificates = MysqlConnectiontest.getVesselOperationCertificates();
            // 创建一个包含这些数据的 Optional 对象
            Optional<List<VesselOperationCertificate>> certificatesOpt = Optional.of(certificates);
            // 调用修改后的方法，传递当前活动窗口和证书列表
            CertificateReminderModule.showCertificateReminder(window, certificatesOpt);
        });






        Button returnButton = new Button("<---返回上一级窗口");
        returnButton.setStyle("-fx-font-size: 16px;");
        returnButton.setOnAction(event -> {
            window.close();
            parentStage.show();
        });

        BorderPane layout = new BorderPane();
        layout.setCenter(centerButtons);
        layout.setBottom(returnButton);
        BorderPane.setAlignment(returnButton, Pos.BOTTOM_LEFT);

        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();
    }
}
