package com.zqu.AdministratorModule;

import com.zqu.WindowCloseConfirmation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
/**
 * @aurhor Dedryck
 * @create 2023-11-29-15:42
 * @description:
 */
public class BoatBasicModule {
    public static String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小
    public static void showBoatBasicModule(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("船只基本资料管理");
        stage.setWidth(800);
        stage.setHeight(600);

        // 设置边距和间隔
        int paddingValue = 20;
        int spacingValue = 10;

        // 主布局
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(paddingValue, paddingValue, paddingValue, paddingValue));

        // 创建按钮并设置字体大小
        Button backButton = new Button("<---返回上一级");
        Button addBoatButton = new Button("新增船只录入");
        Button searchPrintButton = new Button("查询及打印");
        Button modifyInfoButton = new Button("修改信息");
        Button deleteButton = new Button("删除操作");

        backButton.setStyle("-fx-font-size: 16px;");
        addBoatButton.setStyle("-fx-font-size: 16px;");
        searchPrintButton.setStyle("-fx-font-size: 16px;");
        modifyInfoButton.setStyle("-fx-font-size: 16px;");
        deleteButton.setStyle("-fx-font-size: 16px;");

        // 按钮布局
        VBox buttonBox = new VBox(spacingValue);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addBoatButton, searchPrintButton, modifyInfoButton,deleteButton);

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.BOTTOM_LEFT);

        // 将布局添加到主布局
        borderPane.setCenter(buttonBox);
        borderPane.setBottom(backButtonBox);

        backButton.setOnAction(e -> {
            stage.close(); // 关闭当前窗体
            parentStage.show(); // 显示上级窗口
        });

        // 应用关闭确认提示
        WindowCloseConfirmation.applyCloseConfirmation(stage);

        // 添加按钮事件处理程序（留空以供以后实现）
        addBoatButton.setOnAction(e -> {
            // TODO: 处理新增船只录入的逻辑
            AddBoatModule.display(stage);
        });

        searchPrintButton.setOnAction(e -> {
            // TODO: 处理查询及打印的逻辑
            Basic_ship_query.display(stage); // 打开查询窗体
        });

        modifyInfoButton.setOnAction(e -> {
            // TODO: 处理修改信息的逻辑
            ModifyWindow.display(stage); // 弹出新窗口

        });

        deleteButton.setOnAction(e ->{
//            TODO:处理删除信息的逻辑操作
            DeleteBoatModule.display(stage);
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        // 应用窗口关闭确认提示
        WindowCloseConfirmation.applyCloseConfirmation(stage);
        parentStage.hide(); // 隐藏上一级窗口
        stage.show();
    }

}
