package com.zqu.AdministratorModule;

import com.zqu.WindowCloseConfirmation;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.zqu.MysqlConnectiontest; // 导入数据库连接类
import java.time.LocalDate;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @aurhor Dedryck
 * @create 2023-11-29-16:13
 * @description:用于录入船只具体信息
 */
public class AddBoatModule {
    private static byte[] imageData; // 用于存储图片数据
    private static Label uploadStatus = new Label("未上传图片"); // 初始化上传状态提示



    public static void display(Stage parentStage) {
        Stage window = new Stage();
        window.setTitle("新增船只录入");
        uploadStatus.setStyle("-fx-text-fill: blue;"); // 设置文本颜色为蓝色

        ScrollPane scrollPane = new ScrollPane(); // 创建滚动面板
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        scrollPane.setContent(grid); // 将 GridPane 添加到滚动面板中


        // 船主基本资料字段
        Label ownerNameLabel = new Label("船主姓名:");
        grid.add(ownerNameLabel, 0, 0);
        TextField ownerNameField = new TextField();
        ownerNameField.setPrefWidth(200); // 调整文本框宽度以适应新窗口大小
        grid.add(ownerNameField, 1, 0);

        Label ownerIdLabel = new Label("身份证:");
        grid.add(ownerIdLabel, 0, 1);
        TextField ownerIdField = new TextField();
        ownerNameField.setPrefWidth(200); // 调整文本框宽度以适应新窗口大小
        grid.add(ownerIdField, 1, 1);

        Label ownerPhoneLabel = new Label("联系电话:");
        grid.add(ownerPhoneLabel, 0, 2);
        TextField ownerPhoneField = new TextField();
        grid.add(ownerPhoneField, 1, 2);

        Label ownerAddressLabel = new Label("详细住址:");
        grid.add(ownerAddressLabel, 0, 3);
        TextField ownerAddressField = new TextField();
        grid.add(ownerAddressField, 1, 3);

        // 船只基本资料字段
        // ... 以此类推，创建其他标签和文本字段

        Label boatNameLabel = new Label("船只名称:");
        grid.add(boatNameLabel, 0, 4);
        TextField boatNameField = new TextField();
        grid.add(boatNameField, 1, 4);
        Label boatTypeLabel = new Label("船舶类型:");
        grid.add(boatTypeLabel, 0, 5);
        TextField boatTypeField = new TextField();
        grid.add(boatTypeField, 1, 5);

        Label boatMaterialLabel = new Label("船体材料:");
        grid.add(boatMaterialLabel, 0, 6);
        TextField boatMaterialField = new TextField();
        grid.add(boatMaterialField, 1, 6);

        Label boatEngineTypeLabel = new Label("机型:");
        grid.add(boatEngineTypeLabel, 0, 7);
        TextField boatEngineTypeField = new TextField();
        grid.add(boatEngineTypeField, 1, 7);

        Label boatPortOfRegistryLabel = new Label("船籍港:");
        grid.add(boatPortOfRegistryLabel, 0, 8);
        TextField boatPortOfRegistryField = new TextField();
        grid.add(boatPortOfRegistryField, 1, 8);

        Label boatBuilderLabel = new Label("建造厂:");
        grid.add(boatBuilderLabel, 0, 9);
        TextField boatBuilderField = new TextField();
        grid.add(boatBuilderField, 1, 9);

        Label boatRegistrationNoLabel = new Label("船舶登记号:");
        grid.add(boatRegistrationNoLabel, 0, 10);
        TextField boatRegistrationNoField = new TextField();
        grid.add(boatRegistrationNoField, 1, 10);

        Label boatOperationCertificateNoLabel = new Label("营运证号:");
        grid.add(boatOperationCertificateNoLabel, 0, 11);
        TextField boatOperationCertificateNoField = new TextField();
        grid.add(boatOperationCertificateNoField, 1, 11);

        Label entryDateLabel = new Label("入户时间:");
        grid.add(entryDateLabel, 0, 12);
        DatePicker entryDateField = new DatePicker();
        grid.add(entryDateField, 1, 12);

        Label exitDateLabel = new Label("迁出时间:");
        grid.add(exitDateLabel, 0, 13);
        DatePicker exitDateField = new DatePicker();
        grid.add(exitDateField, 1, 13);

        Label constructionDateLabel = new Label("建成时间:");
        grid.add(constructionDateLabel, 0, 14);
        DatePicker constructionDateField = new DatePicker();
        grid.add(constructionDateField, 1, 14);

// ... 继续添加剩余字段

        Label lengthLabel = new Label("总长:");
        grid.add(lengthLabel, 0, 15);
        TextField lengthField = new TextField();
        grid.add(lengthField, 1, 15);

        Label widthLabel = new Label("型宽:");
        grid.add(widthLabel, 0, 16);
        TextField widthField = new TextField();
        grid.add(widthField, 1, 16);

        Label depthLabel = new Label("型深:");
        grid.add(depthLabel, 0, 17);
        TextField depthField = new TextField();
        grid.add(depthField, 1, 17);

        Label grossTonnageLabel = new Label("总吨:");
        grid.add(grossTonnageLabel, 0, 18);
        TextField grossTonnageField = new TextField();
        grid.add(grossTonnageField, 1, 18);

        Label netTonnageLabel = new Label("净吨:");
        grid.add(netTonnageLabel, 0, 19);
        TextField netTonnageField = new TextField();
        grid.add(netTonnageField, 1, 19);

        Label enginePowerLabel = new Label("主机功率:");
        grid.add(enginePowerLabel, 0, 20);
        TextField enginePowerField = new TextField();
        grid.add(enginePowerField, 1, 20);

        Label carryingCapacityLabel = new Label("载重吨（AB级）:");
        grid.add(carryingCapacityLabel, 0, 21);
        TextField carryingCapacityField = new TextField();
        grid.add(carryingCapacityField, 1, 21);

        Label sailingAreaLabel = new Label("航行区域:");
        grid.add(sailingAreaLabel, 0, 22);
        TextField sailingAreaField = new TextField();
        grid.add(sailingAreaField, 1, 22);

        Label remarksLabel = new Label("备注:");
        grid.add(remarksLabel, 0, 23);
        TextArea remarksField = new TextArea();
        remarksField.setPrefRowCount(3);
        grid.add(remarksField, 1, 23);

        Label photoLabel = new Label("船舶相片:");
        grid.add(photoLabel, 0, 25);
        Button photoButton = new Button("上传相片");

        Label boatInspectionRegNoLabel = new Label("船检登记号:");
        grid.add(boatInspectionRegNoLabel, 0, 24); // 注意修改行号以适应布局
        TextField boatInspectionRegNoField = new TextField();
        grid.add(boatInspectionRegNoField, 1, 24);

        photoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择船舶相片");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.bmp")
            );

            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null) {
                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    // TODO: 存储imageData到数据库中
                    imageData = new byte[(int) selectedFile.length()];
                    fis.read(imageData);
                    uploadStatus.setText("图片上传成功"); // 更新状态提示
                    uploadStatus.setStyle("-fx-text-fill: green;"); // 设置文本颜色为绿色

                } catch (IOException ex) {
                    ex.printStackTrace();
                    // 显示错误信息
                    uploadStatus.setText("上传失败"); // 更新状态提示
                    uploadStatus.setStyle("-fx-text-fill: red;"); // 设置文本颜色为红色

                }
            }else{
                uploadStatus.setText("未选择图片"); // 更新状态提示
                uploadStatus.setStyle("-fx-text-fill: blue;"); // 设置文本颜色为蓝色
            }
        });


        // 创建取消按钮
        Button cancelButton = new Button("取消");
        cancelButton.setOnAction(e -> {
            // 清空所有输入字段
            ownerNameField.clear();
            ownerIdField.clear();
            ownerPhoneField.clear();
            ownerAddressField.clear();
            boatNameField.clear();
            boatTypeField.clear();
            boatMaterialField.clear();
            boatEngineTypeField.clear();
            boatPortOfRegistryField.clear();
            boatBuilderField.clear();
            boatRegistrationNoField.clear();
            boatOperationCertificateNoField.clear();
            entryDateField.setValue(null); // 清空日期选择器
            exitDateField.setValue(null);
            constructionDateField.setValue(null);
            lengthField.clear();
            widthField.clear();
            depthField.clear();
            grossTonnageField.clear();
            netTonnageField.clear();
            enginePowerField.clear();
            carryingCapacityField.clear();
            sailingAreaField.clear();
            remarksField.clear();
            boatInspectionRegNoField.clear();
            imageData = null; // 清除图片数据
            // 如果还有其他字段，继续以此类推清空
        });

        // 创建返回上一级按钮（如果需要）
        Button backButton = new Button("返回上一级");
        backButton.setOnAction(e -> {
//             TODO: 实现返回上一级的逻辑
            parentStage.show(); // 显示上级窗口
            window.close(); // 关闭当前窗口
        });


        // 创建保存按钮并设置事件处理
        Button saveButton = new Button("保存");
        saveButton.setOnAction(e -> {
            // 收集船主基本资料
            String ownerName = ownerNameField.getText();
            String ownerId = ownerIdField.getText();
            String ownerPhone = ownerPhoneField.getText();
            String ownerAddress = ownerAddressField.getText();

            // 收集船只基本资料
            String boatName = boatNameField.getText();
            String boatType = boatTypeField.getText();
            String boatMaterial = boatMaterialField.getText();
            String boatEngineType = boatEngineTypeField.getText();
            String boatPortOfRegistry = boatPortOfRegistryField.getText();
            String boatBuilder = boatBuilderField.getText();
            String boatRegistrationNo = boatRegistrationNoField.getText();
            String boatOperationCertificateNo = boatOperationCertificateNoField.getText();

//            日期字段
            LocalDate entryDate = entryDateField.getValue();
            LocalDate exitDate = exitDateField.getValue();
            LocalDate constructionDate = constructionDateField.getValue();
            String boatInspectionRegNo = boatInspectionRegNoField.getText();

// 检查日期字段是否已填写
            if (entryDate == null || exitDate == null || constructionDate == null) {
                // 显示错误提示
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("日期字段不能为空");
                alert.setContentText("请确保所有日期字段都已正确填写！");
                alert.showAndWait();
                return; // 终止事件处理
            }


            try {

// 尝试转换数值型字段
                Double length = lengthField.getText().isEmpty() ? null : Double.parseDouble(lengthField.getText());
                Double width = widthField.getText().isEmpty() ? null : Double.parseDouble(widthField.getText());
                Double depth = depthField.getText().isEmpty() ? null : Double.parseDouble(depthField.getText());
                Double grossTonnage = grossTonnageField.getText().isEmpty() ? null : Double.parseDouble(grossTonnageField.getText());
                Double netTonnage = netTonnageField.getText().isEmpty() ? null : Double.parseDouble(netTonnageField.getText());
                Double enginePower = enginePowerField.getText().isEmpty() ? null : Double.parseDouble(enginePowerField.getText());
                Double carryingCapacity = carryingCapacityField.getText().isEmpty() ? null : Double.parseDouble(carryingCapacityField.getText());

                String sailingArea = sailingAreaField.getText();
                String remarks = remarksField.getText();

                // imageData 已经在上传按钮事件中设置

                // 调用方法将数据插入数据库
                boolean insertSuccess = MysqlConnectiontest.insertBoatData(
                        ownerName, ownerId, ownerPhone, ownerAddress, boatName, boatType, boatMaterial,
                        boatEngineType, boatPortOfRegistry, boatBuilder, boatRegistrationNo,
                        boatOperationCertificateNo, entryDate, exitDate, constructionDate, length,
                        width, depth, grossTonnage, netTonnage, enginePower, carryingCapacity,
                        sailingArea, remarks, imageData, boatInspectionRegNo);

                if (insertSuccess) {
                    // 显示成功消息的对话框
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("操作成功");
                    successAlert.setHeaderText(null); // 可以不设置头部文本
                    successAlert.setContentText("数据保存成功！");
                    ownerNameField.clear();
                    ownerIdField.clear();
                    ownerPhoneField.clear();
                    ownerAddressField.clear();
                    boatNameField.clear();
                    boatTypeField.clear();
                    boatMaterialField.clear();
                    boatEngineTypeField.clear();
                    boatPortOfRegistryField.clear();
                    boatBuilderField.clear();
                    boatRegistrationNoField.clear();
                    boatOperationCertificateNoField.clear();
                    entryDateField.setValue(null);
                    exitDateField.setValue(null);
                    constructionDateField.setValue(null);
                    lengthField.clear();
                    widthField.clear();
                    depthField.clear();
                    grossTonnageField.clear();
                    netTonnageField.clear();
                    enginePowerField.clear();
                    carryingCapacityField.clear();
                    sailingAreaField.clear();
                    remarksField.clear();
                    boatInspectionRegNoField.clear();
                    imageData = null; // 清除图片数据
                    uploadStatus.setText("未上传图片"); // 重置上传状态提示
                    uploadStatus.setStyle("-fx-text-fill: blue;"); // 重置提示颜色为蓝色
                    successAlert.showAndWait(); // 显示对话框并等待用户响应
                } else {
                    // 显示错误消息的对话框
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("操作失败");
                    errorAlert.setHeaderText(null); // 可以不设置头部文本
                    errorAlert.setContentText("数据保存失败，请检查输入或稍后重试！");
                    errorAlert.showAndWait(); // 显示对话框并等待用户响应
                }
            }catch (NumberFormatException ex) {
//                System.out.println("错误的输入: 长度 - '" + lengthField.getText() + "', 宽度 - '" + widthField.getText() + "', 深度 - '" + depthField.getText() + "', ...");
                // 显示警告对话框
                // 数值字段格式错误
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("输入错误");
                alert.setHeaderText("无效的数值输入");
                alert.setContentText("请输入有效的数字。");//在“总长、型宽、型深、总吨、净吨”
                alert.showAndWait();
//                return false;
            }


        });

        // 创建一个 HBox 容器来组合按钮和标签
        HBox photoBox = new HBox(10); // 10 为按钮和标签之间的间距
        photoBox.getChildren().addAll(photoButton, uploadStatus);
        // 将 HBox 容器添加到布局中
        grid.add(photoBox, 1, 25); // 假设放在第 24 行第 2 列

        // 添加按钮到布局中
        HBox buttonBox = new HBox(10, saveButton, cancelButton, backButton); // 10 是按钮之间的间隔
        grid.add(buttonBox, 1, 26); // 假设按钮放在第 26 行

        Scene scene = new Scene(scrollPane, 780, 700); // 使用滚动面板作为场景的根节点

        window.setScene(scene);
        // 应用窗口关闭确认提示
        parentStage.hide(); // 打开当前窗口时隐藏上级窗口
        window.show();
        window.setOnCloseRequest(e -> parentStage.show()); // 当窗口关闭时显示上级窗口
    }


}
