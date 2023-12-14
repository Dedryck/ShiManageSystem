package com.zqu;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;
/**
 * @aurhor Dedryck
 * @create 2023-11-29-16:01
 * @description:关闭窗口提醒事项（待完善）
 */
public class WindowCloseConfirmation {
    public static void applyCloseConfirmation(Stage stage) {
        stage.setOnCloseRequest(event -> {
            event.consume(); // 消费这个事件，防止窗口直接关闭
            showConfirmationDialog(stage, event);
        });
    }

    private static void showConfirmationDialog(Stage stage, WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认退出");
        alert.setHeaderText(null);
        alert.setContentText("您确定要退出吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage.close(); // 关闭窗口
        } else {
            event.consume(); // 继续保持窗口打开
        }
    }
}


// 应用关闭确认提示，实际应用
//        WindowCloseConfirmation.applyCloseConfirmation(stage);