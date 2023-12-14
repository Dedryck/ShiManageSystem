package com.zqu;

import com.zqu.DataManagementModule.DataManagementModule;
import com.zqu.ShipOperationCertificate.CertificateReminderModule;
import com.zqu.ShipOperationCertificate.VesselOperationCertificate;
import com.zqu.System_management_module.UserManagementModule;
import com.zqu.System_management_module.UserOperationLogger;
import com.zqu.System_management_module.UserOperationQuery;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @aurhor Dedryck
 * @create 2023-11-19-20:34
 * @description:显示具体GUI窗体的界面。
 */


public class UserDashboard {
    public static String buttonStyle = "-fx-font-size: 16px;"; //

    public static void show(Stage loginStage, UserInfo userInfo, GUIFX guiFX) {
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("用户信息");



        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // 用户信息标签
        Label userIDLabel = new Label("账号：" + userInfo.getUserID());
        Label nameLabel = new Label("姓名：" + userInfo.getName());
        Label roleLabel = new Label("职位：" + userInfo.getRole());
//        Comic Sans MS：这是一种非常流行的手写风格字体，给人一种轻松愉快的感觉。
//        Bradley Hand ITC：这是一种模仿手写笔迹的字体，具有个性和温暖的感觉。
//        Kristen ITC：这种字体外形圆润可爱，适合给予文本一种友好、轻松的外观。
//        Chalkduster：这是一种模仿粉笔书写的字体，给人一种轻松和创意的感觉。
//        Papyrus：虽然它具有一定的正式感，但其独特的边缘和结构也使其具有一种异国情调
        // 设置字体大小
        Font labelFont = new Font("Comic Sans MS", 18); // 例如设置为16号字体
        userIDLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        roleLabel.setFont(labelFont);
        gridPane.add(userIDLabel, 0, 0);
        gridPane.add(nameLabel, 1, 0);
        gridPane.add(roleLabel, 2, 0);

        // 管理员功能
        if ("管理员".equals(userInfo.getRole())) {
            // 系统管理模块按钮
            Button systemManagementButton = new Button("系统管理模块");
            systemManagementButton.setStyle(buttonStyle);
            systemManagementButton.setOnAction(e -> showSystemManagementModule(dashboardStage, userInfo));
            gridPane.add(systemManagementButton, 1, 1);

            // 收费管理系统按钮
            Button chargeSystemButton = new Button("收费管理系统");
            chargeSystemButton.setStyle(buttonStyle);
            chargeSystemButton.setOnAction(e -> {
                // 实现进入收费管理系统的逻辑
            });
            gridPane.add(chargeSystemButton, 0, 1, 3, 1); // 放在第二行，跨三列
        }

        // 功能按钮
        addButtonFunctionality(gridPane, userInfo, guiFX, loginStage, dashboardStage);

        // 设置场景和展示
        Scene scene = new Scene(gridPane, 800, 600);
        dashboardStage.setScene(scene);
        dashboardStage.show();
    }
    //显示修改密码的对话框||



//    修改密码事宜操作：

    private static void showChangePasswordDialog (String userID){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("修改密码");

        GridPane gridPane1 = new GridPane();
        gridPane1.setHgap(10);
        gridPane1.setVgap(10);

        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("旧密码");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("新密码");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("确认新密码");

        gridPane1.add(new Label("旧密码:"), 0, 0);
        gridPane1.add(oldPasswordField, 1, 0);
        gridPane1.add(new Label("新密码:"), 0, 1);
        gridPane1.add(newPasswordField, 1, 1);
        gridPane1.add(new Label("确认新密码:"), 0, 2);
        gridPane1.add(confirmPasswordField, 1, 2);

        dialog.getDialogPane().setContent(gridPane1);

        //添加按钮
        ButtonType changeButtonType = new ButtonType("修改", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        // 设置按钮事件
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeButtonType) {
                // 验证输入和密码更改逻辑
                String oldPassword = oldPasswordField.getText();
                String newPassword = newPasswordField.getText();
                String confirmPassword = confirmPasswordField.getText();

                if (!newPassword.equals(confirmPassword)) {
                    showAlert("错误", "新密码和确认密码不匹配。", Alert.AlertType.ERROR);
                    return null;
                }

                boolean success = MysqlConnectiontest.updatePassword(userID, oldPassword, newPassword);
                return success ? "密码修改成功。" : "密码修改失败，旧密码可能不正确。";
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(message -> showAlert("修改密码", message, Alert.AlertType.INFORMATION));
    }

    private static List<VesselOperationCertificate> certificates = new ArrayList<>();
    private static void addButtonFunctionality(GridPane gridPane, UserInfo userInfo, GUIFX guiFX, Stage loginStage, Stage dashboardStage) {
        Button changePasswordButton = new Button("修改用户密码");
        changePasswordButton.setStyle(buttonStyle);
        changePasswordButton.setOnAction(e -> showChangePasswordDialog(userInfo.getUserID()));
        // 将证书列表定义在一个更大的作用域
//        List<VesselOperationCertificate> certificates = new ArrayList<>();
        Button messageButton = new Button("用户消息提示");
        messageButton.setStyle(buttonStyle);
        if ("管理员".equals(userInfo.getRole())) {
             certificates = MysqlConnectiontest.getVesselOperationCertificates();
            if (!certificates.isEmpty()) {
                // 在这里可以设置一个文本提示或简单的样式变更
                messageButton.setText("用户消息提示 (新消息)");
                messageButton.setStyle("-fx-font-size: 16px;-fx-text-fill: red;");
            }

            messageButton.setOnAction(e -> {
                if ("管理员".equals(userInfo.getRole())) {
//                    CertificateReminderModule.showCertificateReminder(dashboardStage, certificates);
                    // 使用 Optional 包装证书列表
                    Optional<List<VesselOperationCertificate>> certificatesOpt = Optional.of(certificates);
                    CertificateReminderModule.showCertificateReminder(dashboardStage, certificatesOpt);
                } else {
                    showAlert("权限不足", "您没有权限查看此信息", Alert.AlertType.WARNING);
                }
            });
        }


        Button reloginButton = new Button("重新登录");
        reloginButton.setStyle(buttonStyle);
        reloginButton.setOnAction(e -> {
            dashboardStage.close();
            guiFX.clearPasswordField();
            loginStage.show();
        });
        Button exitButton = new Button("退出系统");
        exitButton.setStyle(buttonStyle);
        exitButton.setOnAction(e -> {
            // 记录用户退出系统操作
            UserOperationLogger logger = new UserOperationLogger();
            String userId = Session.getCurrentAdminUserId(); // 获取当前登录用户ID
            String detail = "用户 " + userId + " 退出系统。";
            logger.logOperation(userId, "退出系统", detail);
            System.exit(0);
        });
        // 创建“资料查询及管理”按钮
        Button dataManagementButton = new Button("资料查询及管理");
        dataManagementButton.setStyle(buttonStyle);
        dataManagementButton.setOnAction(e -> DataManagementModule.showDataManagement(dashboardStage, userInfo));



        int rowIndex = "管理员".equals(userInfo.getRole()) ? 2 : 1;
        gridPane.add(changePasswordButton, 0, rowIndex);
        gridPane.add(messageButton, 1, rowIndex);
        gridPane.add(reloginButton, 0, rowIndex + 1);
        gridPane.add(dataManagementButton, 1, rowIndex + 1); // 新按钮放在原“退出系统”按钮的位置
        gridPane.add(exitButton, 0, rowIndex + 2);
    }

    private static void showAlert (String title, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //添加新方法来显示！！系统管理模块！！
    public static void showSystemManagementModule(Stage parentStage, UserInfo userInfo) {
        Stage systemManagementStage = new Stage();
        systemManagementStage.setTitle("系统管理模块");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        setupSystemManagementGridPane(gridPane);// 设置 GridPane 的属性

        Button userManagementButton = new Button("用户管理");
        userManagementButton.setStyle(buttonStyle);
        userManagementButton.setOnAction(e -> {
            UserManagementModule userManagementModule = new UserManagementModule();
            userManagementModule.showUserManagementUI(systemManagementStage); // 传入当前管理窗口作为父级窗口
        });
        gridPane.add(userManagementButton, 0, 0);



        // 用户权限管理按钮
        Button userPermissionManagementButton = new Button("用户权限管理");
        userPermissionManagementButton.setStyle(buttonStyle);
        userPermissionManagementButton.setOnAction(e -> {
            // 添加用户权限管理相关的事件处理逻辑
        });
//        gridPane.add(userPermissionManagementButton, 0, 1);

        // 用户操作查询按钮
        Button userOperationQueryButton = new Button("用户操作查询");
        userOperationQueryButton.setStyle(buttonStyle);
        userOperationQueryButton.setOnAction(e -> {
            // 添加用户操作查询相关的事件处理逻辑
            new UserOperationQuery().showOperationQuery(systemManagementStage);
        });
        gridPane.add(userOperationQueryButton, 0, 2);

        // 服务器配置信息按钮
        Button serverConfigButton = new Button("服务器配置信息");
        serverConfigButton.setStyle(buttonStyle);
        serverConfigButton.setOnAction(e -> {
            // 显示服务器配置窗口
            ServerConfigWindow.display(systemManagementStage, systemManagementStage);
        });
        gridPane.add(serverConfigButton, 0, 3); // 放置服务器配置按钮


        Button backButton = new Button("<返回上一级");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> {
            systemManagementStage.close();// 关闭当前窗口
            parentStage.show(); // 显示父级窗口
        });
        gridPane.add(backButton, 0, 4);
        // 设置场景和展示

        Scene scene = new Scene(gridPane, 800, 600);
        systemManagementStage.setScene(scene);
        systemManagementStage.show();
        parentStage.hide();// 隐藏父级窗体
    }

    private static void setupSystemManagementGridPane(GridPane gridPane) {
    }

//    显示用户管理模块
    public static void showUserManagement(Stage parentStage, UserInfo userInfo) {
        UserManagementModule userManagementModule = new UserManagementModule();
        userManagementModule.showUserManagementUI(parentStage);// 显示用户管理模块
        parentStage.hide(); // 隐藏父级窗体
    }




}