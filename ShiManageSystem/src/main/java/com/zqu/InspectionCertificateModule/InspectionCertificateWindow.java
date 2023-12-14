package com.zqu.InspectionCertificateModule;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @aurhor Dedryck
 * @create 2023-12-05-17:15
 * @description:检验证书显示增删查改的窗体
 */
public class InspectionCertificateWindow {
    public static void show(Stage parentStage) {
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("船只检验证书资料管理");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小
        BorderPane borderPane = new BorderPane();



        // 创建按钮并设置事件处理
        Button backButton = new Button("<--返回上一级");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> {
            // 隐藏当前窗体并显示父窗体
            stage.hide();
            parentStage.show();
        });

        // 创建HBox用于放置返回按钮，并设置对齐到左侧
        HBox bottomBox = new HBox(backButton);
        bottomBox.setAlignment(Pos.BOTTOM_LEFT); // 对齐到左下角
        borderPane.setBottom(bottomBox); // 设置到BorderPane的底部

        Button addButton = new Button("添加船只的检验证书的基本信息");
        addButton.setStyle(buttonStyle);
        Button queryButton = new Button("查询相关的检验证书的基本信息");
        queryButton.setStyle(buttonStyle);
        Button modifyButton = new Button("修改船只检验证书的基本信息");
        modifyButton.setStyle(buttonStyle);
        Button deleteButton = new Button("删除检验证书的基本信息");
        deleteButton.setStyle(buttonStyle);
        Button viewExpiringButton = new Button("查看即将到期的船只验证书");
        viewExpiringButton.setStyle(buttonStyle);


//        添加按钮的实现
        addButton.setOnAction(event -> {
            AddInspectionCertificateModule.show(stage); // 打开新窗体

        });
//        查询按钮的实现
        queryButton.setOnAction(event -> {
            QueryInspectionCertificateModule.show(stage); // 打开新窗体
            stage.hide(); // 隐藏当前窗体
        });

//        修改按钮的实现
        modifyButton.setOnAction(event -> {
            ModifyInspectionCertificateModule.show(stage); // 打开新窗体
            stage.hide(); // 隐藏当前窗体
        });


//        删除按钮的实现
        deleteButton.setOnAction(event -> {
            DeleteInspectionCertificateModule.show(stage); // 显示删除模块的窗口
            stage.hide(); // 隐藏当前窗口
        });

//        查看即将到期的船只验证书
        viewExpiringButton.setOnAction(event -> {
            ViewExpiringInspectionCertificate.show(stage); // 显示即将到期的船只检验证书窗口
            stage.hide(); // 隐藏当前窗口
        });



        // 设置中心内容
        VBox centerBox = new VBox(10, addButton, queryButton, modifyButton, deleteButton, viewExpiringButton);
        centerBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(centerBox);

        // 创建Scene并将其设置到Stage
        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);

        // 显示新窗体并隐藏父窗体
        parentStage.hide();
        stage.show();
    }

}
