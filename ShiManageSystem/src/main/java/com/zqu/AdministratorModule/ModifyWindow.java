package com.zqu.AdministratorModule;

import com.zqu.MysqlConnectiontest;
import com.zqu.WindowCloseConfirmation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-01-21:14
 * @description:修改窗口页面
 */
public class ModifyWindow {
    private static Stage parentStage;
    private static TableView<Boat> tableView = new TableView<>(); // 将 tableView 作为静态成员变量

    public static void display(Stage parentStage) {
        ModifyWindow.parentStage = parentStage; // 存储对上一级窗口的引用

        Stage stage = new Stage();
        stage.setTitle("修改船只基本资料");
        stage.setWidth(1024);
        stage.setHeight(768);

        // 创建一个类似的表格视图
//        TableView tableView = new TableView();

        BorderPane borderPane = new BorderPane();

        // 创建按钮布局
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(10));

        Button modifyInfoButton = new Button("修改信息");
        Button confirmButton = new Button("确定");
        Button cancelButton = new Button("取消");
        // 初始时将“确认”和“取消”按钮设为不可见
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);


//       下面是文本框事件触发
        // 在修改信息按钮的事件处理程序中，根据需要设置“确认”和“取消”按钮为可见
        modifyInfoButton.setOnAction(e -> {
            confirmButton.setVisible(true);
            cancelButton.setVisible(true);
            //TODO:实现修改船只基本资料的逻辑
        });

        cancelButton.setOnAction(e -> {
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
            // 调用设置文本框不可编辑的函数

            // 如果需要，退出表格的编辑状态
            // tableView.edit(-1, null); // 退出表格的编辑状态
        });

        tableView.setRowFactory(tv -> {
            TableRow<Boat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Boat rowData = row.getItem();
                    new BoatEditWindow(rowData,ModifyWindow.parentStage); // 这里创建实例，它将自动打开编辑窗口
                    ModifyWindow.parentStage.hide();

                }
            });
            return row;
        });



        // 使用AnchorPane作为根容器
        AnchorPane root = new AnchorPane();

        // 设置表格视图的约束，使其填充整个窗口
        AnchorPane.setTopAnchor(tableView, 60.0);
        AnchorPane.setRightAnchor(tableView, 0.0);
        AnchorPane.setBottomAnchor(tableView, 50.0);
        AnchorPane.setLeftAnchor(tableView, 0.0);





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
            ModifyWindow.parentStage.show(); // 显示上一级窗口
        });
        // 将按钮添加到按钮布局
        buttonLayout.getChildren().addAll(modifyInfoButton, confirmButton, cancelButton);
        // 将按钮放置在AnchorPane中的适当位置
        AnchorPane.setBottomAnchor(backButton, 10.0); // 确定按钮的底部锚点
        AnchorPane.setLeftAnchor(backButton, 10.0); // 确定按钮的右侧锚点
        // 添加按钮到root
        root.getChildren().addAll(tableView, buttonLayout, backButton);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        // 应用窗口关闭确认提示
        WindowCloseConfirmation.applyCloseConfirmation(stage);

        parentStage.hide();
        stage.show();
    }


    // 用于刷新表格数据的方法
    public static void refreshTableViewData() {
        List<Boat> boatList = MysqlConnectiontest.readBoatData();
        ObservableList<Boat> boats = FXCollections.observableArrayList(boatList);
        tableView.setItems(boats);
    }








}
