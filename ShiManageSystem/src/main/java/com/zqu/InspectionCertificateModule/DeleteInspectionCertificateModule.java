package com.zqu.InspectionCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-06-21:18
 * @description:船只检验证书删除操作
 */
public class DeleteInspectionCertificateModule {
    private static TableView<Vessel> table = new TableView<>();
    private static ObservableList<Vessel> data = FXCollections.observableArrayList();

    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("删除船只检验证书信息窗口");
        // 添加初始化表格数据的逻辑

        String buttonStyle = "-fx-font-size: 16px;";

        BorderPane borderPane = new BorderPane();

        // 返回按钮
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> {
            stage.hide(); // 隐藏当前窗体
            parentStage.show(); // 显示父窗体
        });

        VBox bottomBox = new VBox(backButton);
        bottomBox.setAlignment(Pos.BOTTOM_LEFT);
        borderPane.setBottom(bottomBox);

        // 初始化表格为不可选择状态
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        table.setDisable(true); // 初始禁用表格的选择



        TableColumn<Vessel, String> vesselNameCol = new TableColumn<>("船名");
        vesselNameCol.setCellValueFactory(new PropertyValueFactory<>("vesselName"));

// 船检登记号
        TableColumn<Vessel, String> registrationNumberCol = new TableColumn<>("船检登记号");
        registrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));

// 检验证编号
        TableColumn<Vessel, String> certificateNumberCol = new TableColumn<>("检验证编号");
        certificateNumberCol.setCellValueFactory(new PropertyValueFactory<>("certificateNumber"));

// 船舶所有人
        TableColumn<Vessel, String> ownerCol = new TableColumn<>("船舶所有人");
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));

// 船舶登记号
        TableColumn<Vessel, String> vesselRegistrationNumberCol = new TableColumn<>("船舶登记号");
        vesselRegistrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("vesselRegistrationNumber"));

// 船舶检验类型
        TableColumn<Vessel, String> inspectionTypeCol = new TableColumn<>("船舶检验类型");
        inspectionTypeCol.setCellValueFactory(new PropertyValueFactory<>("inspectionType"));

// 下次检验时间
        TableColumn<Vessel, java.sql.Date> nextInspectionDateCol = new TableColumn<>("下次检验时间");
        nextInspectionDateCol.setCellValueFactory(new PropertyValueFactory<>("nextInspectionDate"));

// 通知时间
        TableColumn<Vessel, java.sql.Date> notificationDateCol = new TableColumn<>("通知时间");
        notificationDateCol.setCellValueFactory(new PropertyValueFactory<>("notificationDate"));

// 检验机关
        TableColumn<Vessel, String> inspectionAuthorityCol = new TableColumn<>("检验机关");
        inspectionAuthorityCol.setCellValueFactory(new PropertyValueFactory<>("inspectionAuthority"));

// 检验证使用有效期至
        TableColumn<Vessel, java.sql.Date> certificateValidityCol = new TableColumn<>("检验证使用有效期至");
        certificateValidityCol.setCellValueFactory(new PropertyValueFactory<>("certificateValidity"));

// 发证日期
        TableColumn<Vessel, java.sql.Date> issueDateCol = new TableColumn<>("发证日期");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

// 船只检验情况记录
        TableColumn<Vessel, String> inspectionRecordCol = new TableColumn<>("船只检验情况记录");
        inspectionRecordCol.setCellValueFactory(new PropertyValueFactory<>("inspectionRecord"));

// 将所有列添加到表格视图
        table.getColumns().addAll(vesselNameCol, registrationNumberCol, certificateNumberCol, ownerCol,
                vesselRegistrationNumberCol, inspectionTypeCol, nextInspectionDateCol, notificationDateCol,
                inspectionAuthorityCol, certificateValidityCol, issueDateCol, inspectionRecordCol);

        data = MysqlConnectiontest.LoadDataQueryCertificate();
        table.setItems(data);

        // 按钮初始化
        Button deleteOperationButton = new Button("删除操作");
        Button confirmButton = new Button("确认");
        Button cancelButton = new Button("取消");

        // 初始设置按钮不可见
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

        // 按钮事件逻辑
        deleteOperationButton.setOnAction(e -> {
//            table.setDisable(false); // 启用表格选择
            confirmButton.setVisible(true);
            cancelButton.setVisible(true);
        });

        cancelButton.setOnAction(e -> {
//            table.setDisable(true); // 禁用表格选择
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
        });

        confirmButton.setOnAction(e -> {
            // 确认删除对话框
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "你确定删除用户的信息吗？此操作不可逆！", ButtonType.YES, ButtonType.NO);
            confirmAlert.setTitle("确认删除");
            confirmAlert.setHeaderText(null);
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                // 用户确认删除
                List<Vessel> selectedVessels = new ArrayList<>(table.getSelectionModel().getSelectedItems());
                boolean isDeleted = false;
                for (Vessel vessel : selectedVessels) {
                    isDeleted |= MysqlConnectiontest.DeleteInspectionCertificate(vessel.getVesselName(), vessel.getRegistrationNumber());
                }
                // 刷新表格数据
                data = MysqlConnectiontest.LoadDataQueryCertificate();
                table.setItems(data);

                // 显示删除成功提示
                if (isDeleted) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "删除成功！");
                    successAlert.setTitle("操作成功");
                    successAlert.setHeaderText(null);
                    successAlert.showAndWait();
                } else {
                    Alert failureAlert = new Alert(Alert.AlertType.ERROR, "删除失败！");
                    failureAlert.setTitle("操作失败");
                    failureAlert.setHeaderText(null);
                    failureAlert.showAndWait();
                }
            }
        });



        // 按钮容器布局
        HBox buttonBox = new HBox(10, deleteOperationButton, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // 顶部容器（包含按钮容器）
        VBox topContainer = new VBox(10, buttonBox);
        topContainer.setAlignment(Pos.TOP_CENTER);
        topContainer.setPadding(new Insets(10));
        borderPane.setTop(topContainer);


        borderPane.setCenter(table);

        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);

        parentStage.hide(); // 隐藏父窗体
        stage.show(); // 显示新窗体
    }

}
