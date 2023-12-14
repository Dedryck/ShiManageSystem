package com.zqu.ShipOperationCertificate;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.zqu.MysqlConnectiontest;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-04-21:09
 * @description:用于打开修改营运证书窗体
 */
public class ModifyCertificateWindow {
    private ModifyCertificateModule modifyCertificateModuleInstance;


    public ModifyCertificateWindow(VesselOperationCertificate certificate, Stage parentStage, ModifyCertificateModule modifyCertificateModule) {
        this.modifyCertificateModuleInstance = modifyCertificateModule;

        Stage window = new Stage();
        window.setTitle("编辑船只营运证书的基本信息");
        window.setWidth(780);
        window.setHeight(700);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        // 设置列宽，以便标签和文本框对齐
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT); // 第一列（标签）向右对齐
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.LEFT); // 第二列（文本框）向左对齐
        col2.setMinWidth(200); // 设置文本框的最小宽度
        grid.getColumnConstraints().addAll(col1, col2);

        // 创建标签和文本字段
        // 其他标签和文本字段
        Label vesselNameLabel = new Label("船名:");
        TextField vesselNameField = new TextField(certificate.getVesselName());
        grid.add(vesselNameLabel, 0, 0);
        grid.add(vesselNameField, 1, 0);

        Label registrationNumberLabel = new Label("船检登记号:");
        TextField registrationNumberField = new TextField(certificate.getRegistrationNumber());
        grid.add(registrationNumberLabel, 0, 1);
        grid.add(registrationNumberField, 1, 1);

        Label certificateNumberLabel = new Label("营运证编号:");
        TextField certificateNumberField = new TextField(certificate.getCertificateNumber());
        grid.add(certificateNumberLabel, 0, 2);
        grid.add(certificateNumberField, 1, 2);

        Label ownerLabel = new Label("船舶所有人:");
        TextField ownerField = new TextField(certificate.getOwner());
        grid.add(ownerLabel, 0, 3);
        grid.add(ownerField, 1, 3);

        Label vesselRegistrationNumberLabel = new Label("船舶登记号:");
        TextField vesselRegistrationNumberField = new TextField(certificate.getVesselRegistrationNumber());
        grid.add(vesselRegistrationNumberLabel, 0, 4);
        grid.add(vesselRegistrationNumberField, 1, 4);

        Label operatorLicenseNumberLabel = new Label("经营人许可证号码:");
        TextField operatorLicenseNumberField = new TextField(certificate.getOperatorLicenseNumber());
        grid.add(operatorLicenseNumberLabel, 0, 5);
        grid.add(operatorLicenseNumberField, 1, 5);

        Label managerLicenseNumberLabel = new Label("管理人许可证号码:");
        TextField managerLicenseNumberField = new TextField(certificate.getManagerLicenseNumber());
        grid.add(managerLicenseNumberLabel, 0, 6);
        grid.add(managerLicenseNumberField, 1, 6);

        Label issuingAuthorityLabel = new Label("发证机关:");
        TextField issuingAuthorityField = new TextField(certificate.getIssuingAuthority());
        grid.add(issuingAuthorityLabel, 0, 7);
        grid.add(issuingAuthorityField, 1, 7);

        Label validityDateLabel = new Label("营运证使用有效期至:");
        TextField validityDateField = new TextField(certificate.getValidityDate().toString());
        grid.add(validityDateLabel, 0, 8);
        grid.add(validityDateField, 1, 8);

        Label issueDateLabel = new Label("发证日期:");
        TextField issueDateField = new TextField(certificate.getIssueDate().toString());
        grid.add(issueDateLabel, 0, 9);
        grid.add(issueDateField, 1, 9);



        // 按钮
        Button modifyButton = new Button("修改信息");
        Button cancelButton = new Button("取消");
        cancelButton.setOnAction(e -> window.close());
        HBox buttonLayout = new HBox(10, modifyButton, cancelButton);
        buttonLayout.setAlignment(Pos.BOTTOM_CENTER);



// “修改信息”按钮的事件处理器
        modifyButton.setOnAction(e -> {
            String vesselName = vesselNameField.getText();
            String registrationNumber = registrationNumberField.getText();
            String certificateNumber = certificateNumberField.getText();
            String owner = ownerField.getText();
            String vesselRegistrationNumber = vesselRegistrationNumberField.getText();
            String operatorLicenseNumber = operatorLicenseNumberField.getText();
            String managerLicenseNumber = managerLicenseNumberField.getText();
            String issuingAuthority = issuingAuthorityField.getText();
            LocalDate validityDate = LocalDate.parse(validityDateField.getText());
            LocalDate issueDate = LocalDate.parse(issueDateField.getText());

            // 检查文本框不为空
            if (vesselName.isEmpty() || registrationNumber.isEmpty() ||
                    certificateNumber.isEmpty() || owner.isEmpty() ||
                    vesselRegistrationNumber.isEmpty() || operatorLicenseNumber.isEmpty() ||
                    managerLicenseNumber.isEmpty() || issuingAuthority.isEmpty() ||
                    validityDateField.getText().isEmpty() || issueDateField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("信息填写不完整");
                alert.setContentText("所有字段都是必填的，请确保填写完整。");
                alert.showAndWait();
            } else if (!MysqlConnectiontest.checkIfVesselExists(vesselName, registrationNumber)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("数据不合理");
                alert.setContentText("修改的船名或船检登记号数据不合理。");
                alert.showAndWait();
            }else if (!MysqlConnectiontest.CheckIfShipExists(vesselName, registrationNumber)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("数据不存在");
                alert.setContentText("船名或船检登记号在船只营运证书基本表中不存在。");
                alert.showAndWait();
            }
            else {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("确认修改");
                confirmationAlert.setHeaderText("确定修改这些信息吗？");
                confirmationAlert.setContentText("船名：" + vesselName + "\n船检登记号：" + registrationNumber);

                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        VesselOperationCertificate certificate1 = new VesselOperationCertificate(
                                vesselName, registrationNumber, certificateNumber, owner,
                                vesselRegistrationNumber, operatorLicenseNumber, managerLicenseNumber,
                                issuingAuthority, validityDate, issueDate
                        );
                        MysqlConnectiontest.modifyCertificateSQL(certificate1);

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("操作成功");
                        successAlert.setHeaderText("信息已成功修改");
                        successAlert.showAndWait();
                        // 关闭当前窗口
                        window.close();
                        // 刷新父视图的数据
                        ModifyCertificateModule.reloadData();
                    } catch (Exception ex) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("错误");
                        errorAlert.setHeaderText("操作失败");
                        errorAlert.setContentText("请检查数据的合理性。");
                        errorAlert.showAndWait();
                        // 当窗口关闭时，显示父窗体
                        window.setOnCloseRequest(e1 -> {
                            parentStage.show();
                        });
                    }
                }
            }
        });



        grid.add(buttonLayout, 1, 10); // 按钮添加到最后一行

        Scene scene = new Scene(grid);
        window.setScene(scene);

        // 显示当前窗体并隐藏父窗体
        parentStage.hide();
        window.show();


    }
}