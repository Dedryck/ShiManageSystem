package com.zqu.DataManagementModule;

import com.zqu.AdministratorModule.BoatBasicModule;
import com.zqu.InspectionCertificateModule.InspectionCertificateWindow;
import com.zqu.NationalityManningCertificateModule.NationalityManningCertificateWindow;
import com.zqu.SecurityCertificateModule.ShipSecurityCertificateWindow;
import com.zqu.ShipOperationCertificate.VesselOperationCertificateData;
import com.zqu.ShipPayMonthlyWaterFreight.ShipPayMonthlyWaterFreightWindow;
import com.zqu.UserInfo;
import com.zqu.WaterwayDuesPaymentModule.ShipWaterwayDuesPaymentModule;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * @author Dedryck
 * @create 2023-11-29-15:03
 * @description: 资料管理和查询模块
 */
public class DataManageAndCheckModule {
    public static String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

    public static void showModuleManagement(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("模块管理");
        stage.setWidth(800);
        stage.setHeight(600);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // 创建模块按钮
        Button boatBasicButton = new Button("船只基本资料管理模块");
        boatBasicButton.setStyle(buttonStyle);
        Button boatOperationalButton = new Button("船只营运证书资料管理模块");
        boatOperationalButton.setStyle(buttonStyle);
        Button boatInspectionButton = new Button("船只检验证书资料管理模块");
        boatInspectionButton.setStyle(buttonStyle);
        Button boatSafetyButton = new Button("船只安检证书资料管理模块");
        boatSafetyButton.setStyle(buttonStyle);
        Button boatCrewButton = new Button("船只国籍配员证书资料管理模块");
        boatCrewButton.setStyle(buttonStyle);
        Button boatChannelFeeButton = new Button("船只缴纳航道费情况管理模块");
        boatChannelFeeButton.setStyle(buttonStyle);
        Button boatShippingFeeButton = new Button("船只缴纳水运费情况管理模块");
        boatShippingFeeButton.setStyle(buttonStyle);

        gridPane.add(boatBasicButton, 0, 0);
        gridPane.add(boatOperationalButton, 0, 1);
        gridPane.add(boatInspectionButton, 0, 2);
        gridPane.add(boatSafetyButton, 0, 3);
        gridPane.add(boatCrewButton, 0, 4);
        gridPane.add(boatChannelFeeButton, 0, 5);
        gridPane.add(boatShippingFeeButton, 0, 6);

        // 设置模块按钮的事件处理
        boatBasicButton.setOnAction(event -> {
            // 处理船只基本资料管理模块按钮的点击事件
            BoatBasicModule.showBoatBasicModule(stage); // 打开船只基本资料管理窗口

        });

        boatOperationalButton.setOnAction(event -> {
            // 处理船只营运证书资料管理模块按钮的点击事件
            VesselOperationCertificateData certificateData = new VesselOperationCertificateData(stage);
            certificateData.showWindow();
            stage.hide(); // 隐藏上级窗口
        });

        boatInspectionButton.setOnAction(event -> {
            // 处理船只检验证书资料管理模块按钮的点击事件
            InspectionCertificateWindow.show(stage); // 打开新窗体

        });

        boatSafetyButton.setOnAction(event -> {
            // 处理船只安检证书资料管理模块按钮的点击事件
            ShipSecurityCertificateWindow.show(stage);


        });

        boatCrewButton.setOnAction(event -> {
            // 处理船只国籍配员证书资料管理模块按钮的点击事件
            NationalityManningCertificateWindow.show(stage);
        });

        boatChannelFeeButton.setOnAction(event -> {
            // 处理船只缴纳航道费情况管理模块按钮的点击事件
            ShipWaterwayDuesPaymentModule.show(stage);
            stage.hide();

        });

        boatShippingFeeButton.setOnAction(event -> {
            // 处理船只缴纳水运费情况管理模块按钮的点击事件
            ShipPayMonthlyWaterFreightWindow.show(stage);
            stage.hide();

        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane); // 将gridPane设置为BorderPane的中心

        Button backButton = new Button("<-返回上一级");
        backButton.setFont(new Font("Arial", 16));
//        gridPane.add(backButton, 0, 8);

        HBox bottomBox = new HBox(backButton);
        bottomBox.setAlignment(Pos.BOTTOM_LEFT); // 对齐到左下角
        borderPane.setBottom(bottomBox); // 设置到BorderPane的底部

        backButton.setOnAction(e -> {
            stage.close(); // 关闭模块窗体
            parentStage.show(); // 显示上级窗口
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        stage.show();
    }
}
