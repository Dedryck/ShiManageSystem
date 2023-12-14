package com.zqu;

import com.zqu.System_management_module.UserOperationLogger;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.awt.*;

/**
 * @aurhor Dedryck
 * @create 2023-11-18-20:48
 * @description:GUI界面嵌套
 */

public class GUIFX {
    // 类成员变量，用于存储用户名和密码字段的引用
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    public void run(Stage primaryStage) {
        primaryStage.setTitle("船只资料管理系统");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        // 创建布局
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        // 调整标签文本以使它们对齐
        Label usernameLabel = new Label("用户名:   "); // 在冒号后添加空格
        Label passwordLabel = new Label("密   码:   "); // 在冒号后添加相同数量的空格
// 使用CSS设置标签的字体大小和颜色
        usernameLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );");
        passwordLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );");

        // 添加标签和文本框到布局
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);

        // 登录和注册按钮
        HBox buttonBox = new HBox(10);
        Button loginButton = new Button("登录");
        loginButton.setStyle("-fx-background-color: #0078d7; -fx-text-fill: white;"); // 设置背景颜色和文本颜色
//        loginButton.setStyle("-fx-text-fill: black;"); // 设置文本颜色为黑色
        loginButton.setPrefWidth(100); // 设置宽度
        loginButton.setPrefHeight(30); // 设置高度
        loginButton.setDefaultButton(true); // 将登录按钮设置为默认按钮


        Button registerButton = new Button("注册");
        // 设置按钮的大小
        registerButton.setPrefWidth(100); // 设置宽度
        registerButton.setPrefHeight(30); // 设置高度
        buttonBox.getChildren().addAll(loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        gridPane.add(buttonBox, 0, 2, 2, 1); // 跨越两列
//        登录界面的具体操作

        loginButton.setOnAction(e -> handleLogin(primaryStage)); // 设置事件处理
        // 当在用户名或密码框中按下回车键时，也触发登录事件
        usernameField.setOnAction(e -> handleLogin(primaryStage));
        passwordField.setOnAction(e -> handleLogin(primaryStage));
        // 登录成功操作按钮！！！

        // 注册按钮事件处理
        registerButton.setOnAction(e -> showRegistrationForm(primaryStage));

        // 创建场景
        Scene loginScene = new Scene(gridPane);

        String css = this.getClass().getResource("/style.css").toExternalForm(); // 加载 CSS 文件
        loginScene.getStylesheets().add(css); // 将 CSS 应用于场景

        // 应用背景样式
        gridPane.getStyleClass().add("background"); // 为 gridPane 添加 CSS 类

        // 设置场景
        primaryStage.setScene(loginScene);
        // 显示窗口
        primaryStage.show();

    }

    //注册按钮事件处理具体代码
    private void showRegistrationForm(Stage primaryStage) {
        Stage registerStage = new Stage();
        registerStage.initModality(Modality.WINDOW_MODAL);
        registerStage.initOwner(primaryStage);
        registerStage.setTitle("注册新用户");

        GridPane registerPane = new GridPane();
        registerPane.setAlignment(Pos.CENTER);
        registerPane.setHgap(10);
        registerPane.setVgap(10);

        // 添加输入字段和标签
        TextField userIdField = new TextField();
        userIdField.setPromptText("用户ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("密码");
        TextField nameField = new TextField();
        nameField.setPromptText("员工姓名");
        TextField phoneField = new TextField();
        phoneField.setPromptText("联系电话");

        // 设置标签的字体大小
        Label userIdLabel = new Label("用户ID:");
        userIdLabel.setStyle("-fx-font-size: 16px;");
        Label passwordLabel = new Label("密码:");
        passwordLabel.setStyle("-fx-font-size: 16px;");
        Label nameLabel = new Label("员工姓名:");
        nameLabel.setStyle("-fx-font-size: 16px;");
        Label phoneLabel = new Label("联系电话:");
        phoneLabel.setStyle("-fx-font-size: 16px;");

        // 权限选择框
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("普通员工"); // 只添加一个选项
        roleComboBox.setValue("普通员工"); // 设置默认值

        // 注册按钮
        Button submitButton = new Button("提交");
        submitButton.setStyle("-fx-font-size: 16px;");
        submitButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();
            String role = roleComboBox.getValue();

            if (userId.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || role == null) {
                showAlert("警告", "请填写所有必填字段。", Alert.AlertType.WARNING);
            } else {
                boolean isRegistered = MysqlConnectiontest.registerNewUser(userId, password, name, phone, role);

                if (!isRegistered) {
                    showAlert("警告", "该用户ID已存在，请重新选择", Alert.AlertType.WARNING);
                } else {
                    showAlert("成功", "恭喜 " + userId + " 用户注册成功！", Alert.AlertType.INFORMATION);
                    passwordField.clear();
                    usernameField.setText(userId);
                    registerStage.close();
                    primaryStage.show();
                }
            }
        });


        // 添加组件到布局
        registerPane.add(new Label("用户ID:"), 0, 0);
        registerPane.add(userIdField, 1, 0);
        registerPane.add(new Label("密码:"), 0, 1);
        registerPane.add(passwordField, 1, 1);
        registerPane.add(new Label("员工姓名:"), 0, 2);
        registerPane.add(nameField, 1, 2);
        registerPane.add(new Label("联系电话:"), 0, 3);
        registerPane.add(phoneField, 1, 3);
        registerPane.add(new Label("权限:"), 0, 4);
        registerPane.add(roleComboBox, 1, 4);
        registerPane.add(submitButton, 1, 5);
        // 在打开注册窗口之前隐藏上级窗口
        primaryStage.hide();
        // 创建返回按钮
        Button backButton = new Button("<返回上一级");
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> {
            // 在这里实现返回上一级窗口的逻辑
            registerStage.close();
            primaryStage.show(); // 假设primaryStage是你想返回的上一级窗口
        });

        // 设置按钮的位置（第六行，第一列）
        registerPane.add(backButton, 1, 13);

        Scene scene = new Scene(registerPane, 800, 600);
        registerStage.setScene(scene);
        registerStage.show();
        // 当注册窗口关闭时，重新显示上级窗口
        registerStage.setOnCloseRequest(e -> primaryStage.show());
    }

    // 显示警告对话框的方法
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    // 新增方法来清空密码框
    public void clearPasswordField() {
        passwordField.clear();
    }


    // 登录处理方法
    private void handleLogin(Stage primaryStage) {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();

        boolean isAuthenticated = MysqlConnectiontest.authenticateUser(inputUsername, inputPassword);

        if (isAuthenticated) {
            UserInfo userInfo = MysqlConnectiontest.getUserInfo(inputUsername);
            if (userInfo != null) {
//                this.adminUserId = userInfo.getUserId(); // 设置当前登录的管理员用户ID
//                this.adminUserId = userId; // 设置当前登录的管理员用户ID
                Session.setCurrentAdminUserId(userInfo.getUserID()); // 设置当前登录的管理员用户ID
                // 记录用户登录操作
                UserOperationLogger logger = new UserOperationLogger();
                String detail = "用户 " + userInfo.getUserID() + " 登录系统。";
                logger.logOperation(Session.getCurrentAdminUserId(), "用户登录", detail);

                primaryStage.hide();
                UserDashboard.show(primaryStage, userInfo, this);
            } else {
                showAlert("错误", "无法获取用户信息", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("登录失败", "用户名或密码错误", Alert.AlertType.ERROR);
        }
    }



}