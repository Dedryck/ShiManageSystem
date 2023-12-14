package com.zqu.AdministratorModule;


import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.JobSettings;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * @aurhor Dedryck
 * @create 2023-12-01-18:57
 * @description:用于查询基本船只信息的功能。
 */
public class Basic_ship_query {
    public static void display(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("查询船只基本资料");
        stage.setWidth(1024);
        stage.setHeight(768);

        BorderPane borderPane = new BorderPane();

        // 下拉框和搜索框布局
        HBox topLayout = new HBox(10);
        topLayout.setPadding(new Insets(10, 10, 10, 10));

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("船名", "船检登记号", "船舶类型", "船舶营运证号", "船主姓名");
        comboBox.setValue("船名");

        // 在搜索框旁添加文本提示
        Label promptLabel = new Label("双击信息即可打印！");
        promptLabel.setPadding(new Insets(10, 10, 10, 10)); // 可以根据需要调整间距

        // 设置字体大小
        promptLabel.setFont(new Font("Arial", 16)); // 选择合适的字体和大小

// 设置文本颜色为红色
//        promptLabel.setTextFill(Color.RED);

        TextField textField = new TextField();
        textField.setPrefWidth(300);
        // 创建TableView
        TableView tableView = new TableView();

        // 事件监听器
        comboBox.setOnAction(event -> performSearch(comboBox, textField, tableView));
        textField.setOnAction(event -> performSearch(comboBox, textField, tableView));
//        实现动态检测文本框是否为空，从而加载数据
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                // 如果文本框为空，清空视图表并重新加载所有数据
                tableView.getItems().clear();
                tableView.setItems(FXCollections.observableArrayList(MysqlConnectiontest.readBoatData()));
            } else {
                // 如果文本框不为空，执行搜索
                performSearch(comboBox, textField, tableView);
            }
        });


//        自定义TableView工厂
        tableView.setRowFactory(tv -> {
            TableRow<Boat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Boat rowData = row.getItem();
                    openDetailsWindow(rowData, parentStage);
                }
            });
            return row;
        });


        topLayout.getChildren().addAll(comboBox, textField, promptLabel);


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
            stage.close();
            parentStage.show();
        });

        // 设置底部布局
        HBox bottomLayout = new HBox(backButton);
        bottomLayout.setAlignment(Pos.BOTTOM_LEFT);
        bottomLayout.setPadding(new Insets(10, 10, 10, 10));

        // 将组件添加到边界布局中
        borderPane.setTop(topLayout);
        borderPane.setCenter(tableView);
        borderPane.setBottom(bottomLayout);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        parentStage.hide();
        stage.show();
    }



    private static void performSearch(ComboBox<String> comboBox, TextField textField, TableView<Boat> tableView) {
        String selectedCriteria = comboBox.getValue();// 获取ComboBox当前选中的项
        String searchText = textField.getText();// 获取TextField中的文本

        List<Boat> boatList = MysqlConnectiontest.searchBoatData(selectedCriteria, searchText);
        tableView.setItems(FXCollections.observableArrayList(boatList));
    }


//双击视图单元格，可以显示出一张具体信息并且可以对资料打印
    private static void openDetailsWindow(Boat boat, Stage parentStage) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("船只详细信息");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        int row = 0;

        // 船主姓名和船舶相片
        // 在顶部添加船主姓名和相片
        Label ownerNameLabel = new Label("船主姓名: " + boat.getOwnerName());
        grid.add(ownerNameLabel, 0, row, 2, 1); // 占用两列

        // 添加船舶相片
        ImageView imageView = new ImageView(blobToImage(boat.getPhoto()));
        imageView.setFitHeight(100); // 设置图片高度
        imageView.setFitWidth(100); // 设置图片宽度
        grid.add(imageView, 2, row++, 2, 1); // 占用两列
        row++; // 移动到下一行

        // 其他属性
        addLabelAndContent(grid, "身份证号", boat.getIdentityCard(), row, 0);
        addLabelAndContent(grid, "联系电话", boat.getContactNumber(), row++, 2);
        addLabelAndContent(grid, "详细住址", boat.getAddress(), row,0);
        addLabelAndContent(grid, "船名", boat.getBoatName(), row++,2);
        addLabelAndContent(grid, "船舶类型", boat.getBoatType(), row,0);
        addLabelAndContent(grid, "船体材料", boat.getHullMaterial(), row++,2);
        addLabelAndContent(grid, "机型", boat.getEngineType(), row,0);
        addLabelAndContent(grid, "船籍港", boat.getPortOfRegistry(), row++,2);
        addLabelAndContent(grid, "建造厂", boat.getBuilder(), row,0);
        addLabelAndContent(grid, "船舶登记号", boat.getRegistrationNumber(), row++,2);
        addLabelAndContent(grid, "营运证号", boat.getOperationCertificateNumber(), row,0);
        addLabelAndContent(grid, "入户时间", format(boat.getEntryDate()), row++,2);
        addLabelAndContent(grid, "迁出时间", format(boat.getExitDate()), row,0);
        addLabelAndContent(grid, "建成时间", format(boat.getCompletionDate()), row++,2);
        addLabelAndContent(grid, "总长", boat.getLength().toString(), row,0);
        addLabelAndContent(grid, "型宽", boat.getWidth().toString(), row++,2);
        addLabelAndContent(grid, "型深", boat.getDepth().toString(), row,0);
        addLabelAndContent(grid, "总吨", boat.getGrossTonnage().toString(), row++,2);
        addLabelAndContent(grid, "净吨", boat.getNetTonnage().toString(), row,0);
        addLabelAndContent(grid, "主机功率", boat.getMainEnginePower().toString(), row++,2);
        addLabelAndContent(grid, "载重吨", boat.getCarryingCapacity().toString(), row,0);
        addLabelAndContent(grid, "航行区域", boat.getNavigationArea(), row++,2);
        addLabelAndContent(grid, "船检登记号", boat.getRegistrationInspectionNumber(), row,0);
        addLabelAndContent(grid, "备注", boat.getNotes(), ++row,0);
        GridPane.setColumnSpan(grid.getChildren().get(grid.getChildren().size() - 1), 2); // 让最后添加的备注标签跨越两列
        GridPane.setRowSpan(grid.getChildren().get(grid.getChildren().size() - 1), 3); // 让备注标签跨越两行

        // 空白填充，用于在备注和按钮之间添加空间
        for (int i = 0; i < 3; i++) {
            Pane spacer = new Pane();
            spacer.setMinHeight(10); // 空白填充的最小高度，可以根据需要增加
            grid.add(spacer, 0, ++row, 2, 1); // 增加当前行号，让填充占据一个空间，占据两列
        }


        // 打印和取消按钮
        Button printButton = new Button("打印");
        Button cancelButton = new Button("取消");
        printButton.setOnAction(e -> printDetails(grid, printButton, cancelButton, parentStage, detailsStage));
        cancelButton.setOnAction(e -> detailsStage.close());

        HBox buttonLayout = new HBox(10, printButton, cancelButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // 按钮布局添加到网格的下一行
        grid.add(buttonLayout, 0, ++row, 2, 1); // 增加行号，让按钮布局在新的一行

        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid); // 不再需要将按钮布局加入VBox，因为它已经在GridPane中了

        layout.setAlignment(Pos.CENTER);
        // 窗体显示前设置位置
//        detailsStage.setX(100); // X位置（横坐标）
//        detailsStage.setY(100); // Y位置（纵坐标）

        Scene scene = new Scene(layout, 595, 800); // 设置场景大小为A4纸大小
        detailsStage.setScene(scene);
        // 将窗体居中显示
        detailsStage.centerOnScreen();
        detailsStage.show();
    }

    private static void addLabelAndContent(GridPane grid, String labelText, String content, int row, int col) {
        Label label = new Label(labelText + ": " + content);
        GridPane.setConstraints(label, col, row); // 定义标签在网格中的位置
        grid.getChildren().add(label); // 将标签添加到网格布局中
    }



    //    转变图片的像素
    public static Image blobToImage(Blob blob) {
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

//实现打印的逻辑
    private static void printDetails(Node node, Button printButton, Button cancelButton, Stage parentStage, Stage detailsStage) {
    // 获取默认的打印机
    Printer printer = Printer.getDefaultPrinter();
    // 如果没有默认打印机，则弹出警告
    if (printer == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "没有检测到打印机，请确认打印机已连接并且是可用的。");
        alert.showAndWait();
        return;
    }

    // 创建一个打印任务
    PrinterJob job = PrinterJob.createPrinterJob();

    if (job != null) {
        // 弹出一个打印设置对话框，让用户选择打印机并配置打印选项
        boolean proceed = job.showPrintDialog(node.getScene().getWindow());

        if (proceed) {
            // 打印前的确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "准备下一步操作确认是否继续？");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // 用户确认打印，隐藏按钮
                printButton.setVisible(false);
                cancelButton.setVisible(false);


                // 开始打印任务
                boolean printed = job.printPage(node);

                if (printed) {
                    // 成功发送到打印机，结束打印任务
                    job.endJob();
                    // 打印成功的提示
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "操作成功！");
                    // 设置一个事件监听器来处理对话框关闭事件
//                    successAlert.setOnCloseRequest(e -> {
//                        // 关闭详细信息窗口
//                        detailsStage.close();
//                        // 显示父窗口
//                        parentStage.show();
//                    });
                    successAlert.showAndWait();
                } else {
                    // 打印失败，弹出错误提示
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "操作失败！");
                    errorAlert.showAndWait();

                    // 关闭详细信息窗口并显示父窗口
                    detailsStage.close();
                    parentStage.show();
                }

                // 打印结束或失败后，显示按钮
                printButton.setVisible(true);
                cancelButton.setVisible(true);
            } else {
                // 用户取消打印
                job.cancelJob();
            }
        }
    } else {
        // 无法创建打印任务，弹出错误提示
        Alert alert = new Alert(Alert.AlertType.ERROR, "无法创建打印任务，请检查打印机设置！");
        alert.showAndWait();
    }
}




    //转化日期的形式
    private static String format(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
        return "";
    }








}
