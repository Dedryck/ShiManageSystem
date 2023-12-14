package com.zqu.UserQueryModule;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-14:51
 * @description: 用户查询功能
 */
public class QueryAllTypesCertificateModule {

    public static void show(Stage parentStage){
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("用户查询功能界面"); // 设置窗体标题
        stage.setWidth(800);
        stage.setHeight(600);
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        // 创建按钮
        Button btnBack = new Button("<--返回上一级");
        btnBack.setStyle(buttonStyle);

        Button btnQueryCertificates = new Button("船名查询对应的各类证书");
        btnQueryCertificates.setStyle(buttonStyle);

        Button btnQueryPayments = new Button("船名查询对应的缴费情况");
        btnQueryPayments.setStyle(buttonStyle);
        Button btnQueryValidDates = new Button("时间段查询相关证书的有效日期");
        btnQueryValidDates.setStyle(buttonStyle);
        Button btnQueryShipData = new Button("船名查找船只的所有关联资料");
        btnQueryShipData.setStyle(buttonStyle);

        // 设置按钮动作（示例）
        btnBack.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 关闭当前窗体

        btnQueryCertificates.setOnAction(event -> {
            QueryalltypescertificateWindow.show(stage);
            stage.hide();
        });

//        船名查询对应的缴费情况
        btnQueryPayments.setOnAction(event -> {
            QueryShipPayMentWindow.show(stage);
            stage.hide();
        });

        btnQueryValidDates.setOnAction(event -> {
            EffectiveCertificateWindow.show(stage);
            stage.hide();
        });

        btnQueryShipData.setOnAction(event -> {
            ShipSingleModule.show(stage);
            stage.hide();
        });

        // 设置布局
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox(); // 用于中间按钮
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(btnQueryCertificates, btnQueryPayments, btnQueryValidDates, btnQueryShipData);

        HBox hBox = new HBox(); // 用于底部按钮
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.getChildren().add(btnBack);

        borderPane.setCenter(vBox);
        borderPane.setBottom(hBox);

        // 设置并显示场景
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
}
