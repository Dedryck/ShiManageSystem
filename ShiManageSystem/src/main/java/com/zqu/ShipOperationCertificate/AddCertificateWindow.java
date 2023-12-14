package com.zqu.ShipOperationCertificate;


import com.zqu.MysqlConnectiontest;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-03-17:04
 * @description:营运证书添加窗口
 */
public class AddCertificateWindow {
    private Stage parentStage;
    // 添加成员变量以引用所有文本框和日期选择器
    private TextField vesselNameTextField;
    private TextField registrationNumberTextField;
    private TextField certificateNumberTextField;
    private TextField ownerTextField;
    private TextField vesselRegistrationNumberTextField;
    private TextField operatorLicenseNumberTextField;
    private TextField managerLicenseNumberTextField;
    private TextField issuingAuthorityTextField;
    private DatePicker validityDatePicker;
    private DatePicker issueDatePicker;

    public AddCertificateWindow(Stage parentStage) {
        this.parentStage = parentStage;
        // 初始化所有文本框和日期选择器
        vesselNameTextField = new TextField();
        registrationNumberTextField = new TextField();
        certificateNumberTextField = new TextField();
        ownerTextField = new TextField();
        vesselRegistrationNumberTextField = new TextField();
        operatorLicenseNumberTextField = new TextField();
        managerLicenseNumberTextField = new TextField();
        issuingAuthorityTextField = new TextField();
        validityDatePicker = new DatePicker();
        issueDatePicker = new DatePicker();
    }

    public void show() {
        Stage newWindow = new Stage();
        newWindow.setTitle("新增船只营运证书");
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        int textFieldWidth = 200; // 或者根据需要设置其他值


//        按键存放地
        // 创建并添加各个字段的标签和文本框
        layout.getChildren().add(createLabelAndTextField("船名", textFieldWidth, vesselNameTextField));
        layout.getChildren().add(createLabelAndTextField("船检登记号", textFieldWidth, registrationNumberTextField));
        layout.getChildren().add(createLabelAndTextField("营运证编号", textFieldWidth, certificateNumberTextField));
        layout.getChildren().add(createLabelAndTextField("船舶所有人", textFieldWidth, ownerTextField));
        layout.getChildren().add(createLabelAndTextField("船舶登记号", textFieldWidth, vesselRegistrationNumberTextField));
        layout.getChildren().add(createLabelAndTextField("经营人许可证号码", textFieldWidth, operatorLicenseNumberTextField));
        layout.getChildren().add(createLabelAndTextField("管理人许可证号码", textFieldWidth, managerLicenseNumberTextField));
        layout.getChildren().add(createLabelAndTextField("发证机关", textFieldWidth, issuingAuthorityTextField));
        layout.getChildren().add(createLabelAndDatePicker("营运证使用有效期至", validityDatePicker));
        layout.getChildren().add(createLabelAndDatePicker("发证日期", issueDatePicker));



        Button addButton = new Button("添加");
        addButton.setFont(new Font("Arial", 16)); // 设置更大的字体

        // 设置 addButton 的事件处理器
        Button cancelButton = new Button("取消");
        cancelButton.setFont(new Font("Arial", 16)); // 设置更大的字体

        addButton.setOnAction(e -> {

            // 创建确认对话框
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "是否添加这些信息？", ButtonType.YES, ButtonType.NO);
            confirmAlert.setHeaderText(null);
            confirmAlert.setTitle("确认添加");
            // 显示对话框并等待用户响应
            Optional<ButtonType> result = confirmAlert.showAndWait();
// 检查用户点击了哪个按钮
            if (result.isPresent() && result.get() == ButtonType.YES) {
                // 收集用户输入的数据
                String vesselName = vesselNameTextField.getText();
                String registrationNumber = registrationNumberTextField.getText();
                String certificateNumber = certificateNumberTextField.getText();
                String owner = ownerTextField.getText();
                String vesselRegistrationNumber = vesselRegistrationNumberTextField.getText();
                String operatorLicenseNumber = operatorLicenseNumberTextField.getText();
                String managerLicenseNumber = managerLicenseNumberTextField.getText();
                String issuingAuthority = issuingAuthorityTextField.getText();
                LocalDate validityDate = validityDatePicker.getValue();
                LocalDate issueDate = issueDatePicker.getValue();

                // 首先检查船只是否存在
                if (MysqlConnectiontest.checkIfVesselExists(vesselName, registrationNumber)) {
                    // 如果船只存在，则创建VesselOperationCertificate对象并尝试添加
                    VesselOperationCertificate certificate = new VesselOperationCertificate(
                            vesselName,
                            registrationNumber,
                            certificateNumber,
                            owner,
                            vesselRegistrationNumber,
                            operatorLicenseNumber,
                            managerLicenseNumber,
                            issuingAuthority,
                            validityDate,
                            issueDate
                    );

                    boolean success = MysqlConnectiontest.addVesselOperationCertificate(certificate);

                    if (success) {
                        new Alert(Alert.AlertType.INFORMATION, "添加成功").showAndWait();
                        // 清空表单
                        clearForm();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "添加失败，请检查输入数据").showAndWait();
                    }
                } else {
                    // 如果船只不存在，则向用户显示错误消息
                    new Alert(Alert.AlertType.ERROR, "无效的船名或船检登记号，请检查是否正确").showAndWait();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION, "添加失败").showAndWait();
            }
        });

        cancelButton.setOnAction(e -> {
            newWindow.close();
            parentStage.show();
        });

        // 按钮容器
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER); // 设置按钮居中
        buttonContainer.getChildren().addAll(addButton, cancelButton);

        layout.getChildren().addAll(addButton, cancelButton);
    // 将按钮容器添加到主布局
        layout.getChildren().add(buttonContainer);


        // 使用ScrollPane包裹主布局以支持滚动
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // 宽度适配内容
        scrollPane.setFitToHeight(true); // 高度适配内容

        Scene scene = new Scene(scrollPane, 800, 600);
        newWindow.setScene(scene);

        // 隐藏原始窗体
        parentStage.hide();

        // 显示新窗体
        newWindow.show();
    }

    // 清空表单的方法
    private void clearForm() {
        vesselNameTextField.clear();
        registrationNumberTextField.clear();
        certificateNumberTextField.clear();
        ownerTextField.clear();
        vesselRegistrationNumberTextField.clear();
        operatorLicenseNumberTextField.clear();
        managerLicenseNumberTextField.clear();
        issuingAuthorityTextField.clear();
        validityDatePicker.setValue(null);
        issueDatePicker.setValue(null);
    }


    // 创建带标签的文本框的辅助方法
    private HBox createLabelAndTextField(String label, int textFieldWidth, TextField textField) {
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.setMaxWidth(400);

        Label lbl = new Label(label);
        lbl.setMinWidth(100);
        textField.setPrefWidth(textFieldWidth);

        hBox.getChildren().addAll(lbl, textField);
        return hBox;
    }

    // 创建带标签的日期选择器的辅助方法
    private HBox createLabelAndDatePicker(String label, DatePicker datePicker) {
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.setMaxWidth(400);

        Label lbl = new Label(label);
        lbl.setMinWidth(100);

        hBox.getChildren().addAll(lbl, datePicker);
        return hBox;
    }




}
