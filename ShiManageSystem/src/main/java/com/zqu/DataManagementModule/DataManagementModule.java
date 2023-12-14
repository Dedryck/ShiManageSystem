package com.zqu.DataManagementModule;

import com.zqu.UserInfo;
import com.zqu.UserQueryModule.QueryAllTypesCertificateModule;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @aurhor Dedryck
 * @create 2023-11-29-9:40
 * @description:资料查询及其管理功能
 */
public class DataManagementModule {
    public static void showDataManagement(Stage parentStage, UserInfo userInfo) {
        Stage stage = new Stage();
        stage.setTitle("资料查询及管理");
        stage.setWidth(800);
        stage.setHeight(600);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // 创建按钮
        Button userQueryButton = new Button("用户查询资料功能");
        userQueryButton.setFont(new Font("Arial", 16)); // 设置字体和大小
        Button adminQueryButton = new Button("资料管理及查询功能");
        adminQueryButton.setFont(new Font("Arial", 16)); // 设置字体和大小
        Button backButton = new Button("<-返回上一级");
        backButton.setFont(new Font("Arial", 16)); // 设置字体和大小

        // 添加按钮到gridPane
        gridPane.add(userQueryButton, 0, 0);

//        用户查询资料功能按钮实现
        userQueryButton.setOnAction(event -> {
            // 在这里添加用户查询资料功能的实现逻辑
            // 您可以调用其他方法、打开新窗口或执行任何您需要的操作
            // 例如，您可以弹出一个对话框，显示用户查询的结果
            QueryAllTypesCertificateModule.show(stage);
            stage.hide();
        });


        // 只有当用户是管理员时才添加管理员查询按钮
        if ("管理员".equals(userInfo.getRole())) {
            gridPane.add(adminQueryButton, 0, 1);

            adminQueryButton.setOnAction(e -> {
                stage.close(); // 关闭当前窗口
                DataManageAndCheckModule.showModuleManagement(stage);
            });

        }

        gridPane.add(backButton, 0, 3); // 将按钮添加到左下角

        // 按钮事件处理
        backButton.setOnAction(e -> {
            stage.close(); // 关闭当前窗口
            parentStage.show(); // 显示上一级窗口
        });

        // 设置场景
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);

        // 隐藏上一级窗口并显示当前窗口
        parentStage.hide();
        stage.show();
    }
}
