package com.zqu.AdministratorModule;


import com.zqu.MysqlConnectiontest;
import com.zqu.WindowCloseConfirmation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Blob;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @aurhor Dedryck
 * @create 2023-12-03-9:28
 * @description:用于基本表的删除操作
 */
public class DeleteBoatModule {
    private static Stage parentStage;//作为返回上一级的参数

    public DeleteBoatModule(Stage parentStage) {
        this.parentStage = parentStage;
    }
    public static void display(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("删除用户信息");
        stage.setWidth(1024);
        stage.setHeight(768);

        TableView<Boat> tableView = new TableView<>();

        // 设置选择模式为多选
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // 使用AnchorPane作为根容器
        AnchorPane root = new AnchorPane();

        // 设置表格视图的约束，使其填充整个窗口
        AnchorPane.setTopAnchor(tableView, 60.0);
        AnchorPane.setRightAnchor(tableView, 0.0);
        AnchorPane.setBottomAnchor(tableView, 50.0);
        AnchorPane.setLeftAnchor(tableView, 0.0);

        // 添加按钮
        Button deleteButton = new Button("点击进入删除操作");
        Button confirmButton = new Button("确认");
        Button cancelButton = new Button("取消");

        // 设置确认和取消按钮的初始可见性
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

        // 删除按钮的事件处理
        deleteButton.setOnAction(e -> {
            // 显示确认和取消按钮


            confirmButton.setVisible(true);
            cancelButton.setVisible(true);
        });

        // 取消按钮的事件处理
        cancelButton.setOnAction(e -> {
            // 隐藏确认和取消按钮
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
        });

        // 确认按钮的事件处理
        confirmButton.setOnAction(e -> {
            // TODO: 实现删除逻辑
            // 获取所有选中的行
            ObservableList<Boat> selectedBoats = tableView.getSelectionModel().getSelectedItems();
            // 进行删除操作前，可能需要复制一份选中项的列表，以免在删除过程中出现并发修改异常
            List<Boat> boatsToDelete = new ArrayList<>(selectedBoats);

            if (!boatsToDelete.isEmpty()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("确认删除");
                confirmAlert.setHeaderText("即将删除选中的船只");
                confirmAlert.setContentText("确定要删除选中的船只吗？");

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    MysqlConnectiontest.deleteBoats(boatsToDelete);

                    // 更新视图或显示删除成功消息
                    // TODO: 更新视图或其他操作
                    // 重新加载数据
                    List<Boat> updatedBoatList = MysqlConnectiontest.readBoatData();
                    tableView.setItems(FXCollections.observableArrayList(updatedBoatList));

                    // 显示删除成功的消息
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("操作成功");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("选中的船只已成功删除！");
                    successAlert.showAndWait();
                }
            }
        });

        // 创建一个HBox来放置按钮
        HBox buttonBox = new HBox(10); // 10是按钮之间的间距
        buttonBox.setAlignment(Pos.CENTER); // 将按钮居中对齐
        buttonBox.getChildren().addAll(deleteButton, confirmButton, cancelButton);
        // 设置HBox在AnchorPane中的位置
        AnchorPane.setTopAnchor(buttonBox, 10.0); // 10是距离顶部的距离
        AnchorPane.setLeftAnchor(buttonBox, 0.0);  // 按钮盒子左侧对齐到父容器的左边
        AnchorPane.setRightAnchor(buttonBox, 0.0); // 按钮盒子右侧对齐到父容器的右边






        // 调用 readBoatData 方法并设置数据
        List<Boat> boatList = MysqlConnectiontest.readBoatData();
        ObservableList<Boat> boats = FXCollections.observableArrayList(boatList);
        tableView.setItems(boats);

        // 创建并定义列
        TableColumn<Boat, Number> idCol = new TableColumn<>("序号");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Boat, String> ownerNameCol = new TableColumn<>("船主姓名");
        ownerNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        TableColumn<Boat, String> identityCardCol = new TableColumn<>("身份证号");
        identityCardCol.setCellValueFactory(new PropertyValueFactory<>("identityCard"));

        TableColumn<Boat, String> contactNumberCol = new TableColumn<>("联系电话");
        contactNumberCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        TableColumn<Boat, String> addressCol = new TableColumn<>("详细住址");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Boat, String> boatNameCol = new TableColumn<>("船名");
        boatNameCol.setCellValueFactory(new PropertyValueFactory<>("boatName"));

        TableColumn<Boat, String> boatTypeCol = new TableColumn<>("船舶类型");
        boatTypeCol.setCellValueFactory(new PropertyValueFactory<>("boatType"));

        TableColumn<Boat, String> hullMaterialCol = new TableColumn<>("船体材料");
        hullMaterialCol.setCellValueFactory(new PropertyValueFactory<>("hullMaterial"));

        TableColumn<Boat, String> engineTypeCol = new TableColumn<>("机型");
        engineTypeCol.setCellValueFactory(new PropertyValueFactory<>("engineType"));

        TableColumn<Boat, String> portOfRegistryCol = new TableColumn<>("船籍港");
        portOfRegistryCol.setCellValueFactory(new PropertyValueFactory<>("portOfRegistry"));

        TableColumn<Boat, String> builderCol = new TableColumn<>("建造厂");
        builderCol.setCellValueFactory(new PropertyValueFactory<>("builder"));

        TableColumn<Boat, String> registrationNumberCol = new TableColumn<>("船舶登记号");
        registrationNumberCol.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));

        TableColumn<Boat, String> operationCertificateNumberCol = new TableColumn<>("营运证号");
        operationCertificateNumberCol.setCellValueFactory(new PropertyValueFactory<>("operationCertificateNumber"));

// 日期列可能需要特殊处理以正确格式化显示
        TableColumn<Boat, Date> entryDateCol = new TableColumn<>("入户时间");
        entryDateCol.setCellValueFactory(new PropertyValueFactory<>("entryDate"));

        TableColumn<Boat, Date> exitDateCol = new TableColumn<>("迁出时间");
        exitDateCol.setCellValueFactory(new PropertyValueFactory<>("exitDate"));

        TableColumn<Boat, Date> completionDateCol = new TableColumn<>("建成时间");
        completionDateCol.setCellValueFactory(new PropertyValueFactory<>("completionDate"));

        TableColumn<Boat, BigDecimal> lengthCol = new TableColumn<>("总长");
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<Boat, BigDecimal> widthCol = new TableColumn<>("型宽");
        widthCol.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Boat, BigDecimal> depthCol = new TableColumn<>("型深");
        depthCol.setCellValueFactory(new PropertyValueFactory<>("depth"));

        TableColumn<Boat, BigDecimal> grossTonnageCol = new TableColumn<>("总吨");
        grossTonnageCol.setCellValueFactory(new PropertyValueFactory<>("grossTonnage"));

        TableColumn<Boat, BigDecimal> netTonnageCol = new TableColumn<>("净吨");
        netTonnageCol.setCellValueFactory(new PropertyValueFactory<>("netTonnage"));

        TableColumn<Boat, BigDecimal> mainEnginePowerCol = new TableColumn<>("主机功率");
        mainEnginePowerCol.setCellValueFactory(new PropertyValueFactory<>("mainEnginePower"));

        TableColumn<Boat, BigDecimal> carryingCapacityCol = new TableColumn<>("载重吨");
        carryingCapacityCol.setCellValueFactory(new PropertyValueFactory<>("carryingCapacity"));

        TableColumn<Boat, String> navigationAreaCol = new TableColumn<>("航行区域");
        navigationAreaCol.setCellValueFactory(new PropertyValueFactory<>("navigationArea"));

        TableColumn<Boat, String> notesCol = new TableColumn<>("备注");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

// 对于Blob类型的图片，您可能需要特殊的渲染器
        TableColumn<Boat, Blob> photoCol = new TableColumn<>("船舶相片");
        photoCol.setCellValueFactory(new PropertyValueFactory<>("photo"));
        photoCol.setCellFactory(column -> new TableCell<Boat, Blob>() {
            @Override
            protected void updateItem(Blob item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        Image img = blobToImage(item);
                        if (img != null) {
                            ImageView imageView = new ImageView(img);
                            imageView.setFitHeight(50); // 设置图片高度
                            imageView.setFitWidth(50); // 设置图片宽度
                            imageView.setPreserveRatio(true);
                            setGraphic(imageView);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setText("Error loading image");
                        setGraphic(null);
                    }
                }
            }

            private Image blobToImage(Blob blob) {
                try {
                    if (blob != null) {
                        return new Image(new ByteArrayInputStream(blob.getBytes(1, (int) blob.length())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 处理异常
                }
                return null;
            }
        });


        TableColumn<Boat, String> registrationInspectionNumberCol = new TableColumn<>("船检登记号");
        registrationInspectionNumberCol.setCellValueFactory(new PropertyValueFactory<>("registrationInspectionNumber"));

        tableView.getColumns().addAll(idCol, ownerNameCol, identityCardCol, contactNumberCol, addressCol, boatNameCol, boatTypeCol, hullMaterialCol, engineTypeCol, portOfRegistryCol, builderCol, registrationNumberCol, operationCertificateNumberCol, entryDateCol, exitDateCol, completionDateCol, lengthCol, widthCol, depthCol, grossTonnageCol, netTonnageCol, mainEnginePowerCol, carryingCapacityCol, navigationAreaCol, notesCol, registrationInspectionNumberCol, photoCol);



        // 返回上一级按钮
        Button backButton = new Button("<----返回上一级");
        backButton.setOnAction(e -> {
            stage.close(); // 关闭当前窗口
            parentStage.show(); // 显示上一级窗口
        });

        // 将按钮放置在AnchorPane中的适当位置
        AnchorPane.setBottomAnchor(backButton, 10.0); // 确定按钮的底部锚点
        AnchorPane.setLeftAnchor(backButton, 10.0); // 确定按钮的右侧锚点
        // 添加按钮到root
        root.getChildren().addAll(tableView, buttonBox, backButton);



        Scene scene = new Scene(root);
        stage.setScene(scene);

        WindowCloseConfirmation.applyCloseConfirmation(stage);

        parentStage.hide(); // 隐藏上一级窗口
        stage.show();
    }
}
