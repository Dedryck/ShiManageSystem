package com.zqu.AdministratorModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;


/**
 * @aurhor Dedryck
 * @create 2023-12-02-14:09
 * @description:对应修改信息窗口的显示。
 */
public class BoatEditWindow {
    private ImageView boatImageView;
    private Stage parentStage; // 添加一个字段来存储父窗口的引用

    private Stage stage;
    private Boat boat;
//    修改唯一修改确定符，很需要
    private Integer boatId;
    private byte[] imageBytes; // 用于存储图片字节数据



    public BoatEditWindow(Boat boat, Stage parentStage) {
        this.boat = boat;
        this.parentStage = parentStage;
        this.stage = new Stage();
        openEditWindow();
    }
    //用于显示修改页面的窗体
    public void openEditWindow() {
        stage.setTitle("船只基本资料修改");

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
        boatImageView = new ImageView();
        boatImageView.setFitHeight(200);
        boatImageView.setFitWidth(200);
        boatImageView.setPreserveRatio(true);

// 如果数据库中有图片，加载并显示图片

        grid.add(boatImageView, 1, 25); // 添加到网格布局中
        photoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择图片");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                boatImageView.setImage(image);
                // 还可以将图片保存到数据库中
                // 将图片转换为字节数组并设置到 Boat 对象中
                imageBytes = convertImageToByteArray(file); // 更新类级别的变量
            }
        });
        grid.add(photoButton, 2, 25);   // 上传按钮添加到网格布局中

        Label boatInspectionRegNoLabel = new Label("船检登记号:");
        grid.add(boatInspectionRegNoLabel, 0, 24); // 注意修改行号以适应布局
        TextField boatInspectionRegNoField = new TextField();
        grid.add(boatInspectionRegNoField, 1, 24);

        // 创建取消按钮
        Button cancelButton = new Button("取消修改");

        // 创建保存按钮并设置事件处理
        Button saveButton = new Button("保存修改");

        this.boatId = boat.getId();

        // 设置字段值，使用boat对象的getter方法
        ownerNameField.setText(boat.getOwnerName());
        ownerIdField.setText(boat.getIdentityCard()); // 使用身份证字段
        ownerPhoneField.setText(boat.getContactNumber());
        ownerAddressField.setText(boat.getAddress());
        boatNameField.setText(boat.getBoatName());
        boatTypeField.setText(boat.getBoatType());
        boatMaterialField.setText(boat.getHullMaterial()); // 船体材料
        boatEngineTypeField.setText(boat.getEngineType());
        boatPortOfRegistryField.setText(boat.getPortOfRegistry());
        boatBuilderField.setText(boat.getBuilder());
        boatRegistrationNoField.setText(boat.getRegistrationNumber()); // 船舶登记号
        boatOperationCertificateNoField.setText(boat.getOperationCertificateNumber()); // 营运证号
        entryDateField.setValue(convertToLocalDate(boat.getEntryDate()));
        exitDateField.setValue(convertToLocalDate(boat.getExitDate()));
        constructionDateField.setValue(convertToLocalDate(boat.getCompletionDate()));
        lengthField.setText(formatBigDecimal(boat.getLength()));
        widthField.setText(formatBigDecimal(boat.getWidth()));
        depthField.setText(formatBigDecimal(boat.getDepth()));
        grossTonnageField.setText(formatBigDecimal(boat.getGrossTonnage()));
        netTonnageField.setText(formatBigDecimal(boat.getNetTonnage()));
        enginePowerField.setText(formatBigDecimal(boat.getMainEnginePower()));
        carryingCapacityField.setText(formatBigDecimal(boat.getCarryingCapacity()));
        sailingAreaField.setText(boat.getNavigationArea());
        remarksField.setText(boat.getNotes());
        boatInspectionRegNoField.setText(boat.getRegistrationInspectionNumber());

        // 设置日期选择器的值
        entryDateField.setValue(convertToLocalDate(boat.getEntryDate()));
        exitDateField.setValue(convertToLocalDate(boat.getExitDate()));
        constructionDateField.setValue(convertToLocalDate(boat.getCompletionDate()));

        // 尝试设置图片，如果有的话
        if (boat.getPhoto() != null) {
            // 这里需要根据您如何处理 Blob 和 Image 转换来实现
            // 示例代码略
            try {
                // 将Blob转换为InputStream
                InputStream is = boat.getPhoto().getBinaryStream();
                Image image = new Image(is);
                boatImageView.setImage(image);
                is.close(); // 关闭输入流
            } catch (SQLException e) {
                // 处理异常，例如显示错误消息或写入日志
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


//        按键实现
        // 在 BoatEditWindow 类中
        saveButton.setOnAction(e -> {
            try {
                // 从输入字段获取数据并更新 boat 对象
                boat.setOwnerName(ownerNameField.getText());
                boat.setIdentityCard(ownerIdField.getText());
                boat.setContactNumber(ownerPhoneField.getText());
                boat.setAddress(ownerAddressField.getText());
                boat.setBoatName(boatNameField.getText());
                boat.setBoatType(boatTypeField.getText());
                boat.setHullMaterial(boatMaterialField.getText());
                boat.setEngineType(boatEngineTypeField.getText());
                boat.setPortOfRegistry(boatPortOfRegistryField.getText());
                boat.setBuilder(boatBuilderField.getText());
                boat.setRegistrationNumber(boatRegistrationNoField.getText());
                boat.setOperationCertificateNumber(boatOperationCertificateNoField.getText());
                boat.setEntryDate(Date.from(entryDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                boat.setExitDate(Date.from(exitDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                boat.setCompletionDate(Date.from(constructionDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                boat.setLength(new BigDecimal(lengthField.getText()));
                boat.setWidth(new BigDecimal(widthField.getText()));
                boat.setDepth(new BigDecimal(depthField.getText()));
                boat.setGrossTonnage(new BigDecimal(grossTonnageField.getText()));
                boat.setNetTonnage(new BigDecimal(netTonnageField.getText()));
                boat.setMainEnginePower(new BigDecimal(enginePowerField.getText()));
                boat.setCarryingCapacity(new BigDecimal(carryingCapacityField.getText()));
                boat.setNavigationArea(sailingAreaField.getText());
                boat.setNotes(remarksField.getText());
                boat.setRegistrationInspectionNumber(boatInspectionRegNoField.getText());
                boat.setId(boatId); // 设置船只的唯一标识符（序号）


                // 调用更新方法
                // 调用更新方法，并传递图片数据
                if (MysqlConnectiontest.updateBoatData(boat, imageBytes)) {
                    showAlert(Alert.AlertType.INFORMATION, "保存成功", "船只信息已成功更新。");
                    ModifyWindow.refreshTableViewData(); // 刷新父级窗口的数据

                    stage.close(); // 关闭编辑窗口
//                    ModifyWindow.display(parentStage); // 使用存储的父窗口引用来重新打开窗口
                } else {
                    showAlert(Alert.AlertType.ERROR, "保存失败", "无法更新船只信息。");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "保存失败", "无法更新船只信息。");
            }
        });



        cancelButton.setOnAction(e -> {
            // 关闭当前窗口
            stage.close();
        });

        // 创建一个HBox布局用于放置按钮
        HBox buttonBox = new HBox(10); // 间距为10
        buttonBox.setAlignment(Pos.CENTER); // 设置对齐方式为居中

        // 将按钮添加到HBox
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        // 将HBox添加到GridPane的底部
        grid.add(buttonBox, 0, 26, 2, 1); // 合并两列


        // 创建场景并设置其根节点为 ScrollPane
        Scene scene = new Scene(scrollPane, 780, 700);

        // 设置舞台并显示
        stage.setScene(scene);


        stage.show();
    }


    // 辅助方法：将Date转换为LocalDate
    // 辅助方法：将 java.util.Date 或 java.sql.Date 转换为 java.time.LocalDate
    private LocalDate convertToLocalDate(Date date) {
        return date != null ? new java.sql.Date(date.getTime()).toLocalDate() : null;
    }


    // 辅助方法: 格式化 BigDecimal 字段
    private String formatBigDecimal(BigDecimal value) {
        return value != null ? value.toString() : "";
    }

    // 辅助方法来显示提示信息
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    对图片数据的处理
    private byte[] convertImageToByteArray(File file) {
        try {
            BufferedImage bImage = ImageIO.read(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos); // 根据您的图片格式可能需要更改
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




//                    ownerNameField.clear();
//                    ownerIdField.clear();
//                    ownerPhoneField.clear();
//                    ownerAddressField.clear();
//                    boatNameField.clear();
//                    boatTypeField.clear();
//                    boatMaterialField.clear();
//                    boatEngineTypeField.clear();
//                    boatPortOfRegistryField.clear();
//                    boatBuilderField.clear();
//                    boatRegistrationNoField.clear();
//                    boatOperationCertificateNoField.clear();
//                    entryDateField.setValue(null);
//                    exitDateField.setValue(null);
//                    constructionDateField.setValue(null);
//                    lengthField.clear();
//                    widthField.clear();
//                    depthField.clear();
//                    grossTonnageField.clear();
//                    netTonnageField.clear();
//                    enginePowerField.clear();
//                    carryingCapacityField.clear();
//                    sailingAreaField.clear();
//                    remarksField.clear();
//                    boatInspectionRegNoField.clear();
//                    boatImageView.setImage(null); // 清除图片视图





}
