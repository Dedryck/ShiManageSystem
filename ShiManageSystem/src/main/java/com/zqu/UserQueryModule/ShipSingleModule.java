package com.zqu.UserQueryModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-21:45
 * @description: 用户查询查找船只的所有关联资料
 */
public class ShipSingleModule {
    public static TableView<ShipSingleInfo> tableView = new TableView<>(); // Object应替换为您的数据模型类
    private static List<ShipSingleInfo> allCertificateInfo = new ArrayList<>();


    public static void show(Stage parentStage){
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("用户查询查找船只的所有关联资料"); // 设置窗体标题
        stage.setWidth(1024);
        stage.setHeight(768);
        String buttonStyle = "-fx-font-size: 14px;"; // 您可以调整这里的数值来改变字体大小

        // 创建按钮
        Button btnBack = new Button("<---返回上一级");
        btnBack.setStyle(buttonStyle);
        Button btnPrint = new Button("打印");
        btnPrint.setStyle(buttonStyle);
//        Button btnCancel = new Button("刷新");
//        btnCancel.setStyle(buttonStyle);

// 创建标签、搜索框和打印按钮
        Label lblSearch = new Label("船名搜索:");
        TextField txtSearch = new TextField();
        txtSearch.setPrefWidth(200);
        // 设置按钮动作（示例）
        btnBack.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 关闭当前窗体

//        btnCancel.setOnAction(event -> {
//            // 清除表格内的数据
//            tableView.getItems().clear();
//            // 重新加载allCertificateInfo中的数据
//            loadData();
//        });
        setupTableViewColumns();
        loadData();

        btnPrint.setOnAction(event -> {
            try {
                saveCSV(tableView);
            } catch (IOException e) {
                e.printStackTrace();
                // 处理异常，比如显示错误消息
            }
        });


        // 设置文本字段的监听器
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // 如果搜索框为空，重新加载所有数据
                loadData();
            } else {
                // 搜索框不为空，进行模糊搜索
                performSearch(newValue.trim());
            }
        });

        HBox topBox = new HBox(10, lblSearch, txtSearch, btnPrint);
        topBox.setAlignment(Pos.CENTER);

        // 创建底部区域，包含返回按钮
        HBox bottomBox = new HBox(btnBack);
        bottomBox.setAlignment(Pos.BOTTOM_LEFT);

        // 创建中心区域，放置表格
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        borderPane.setTop(topBox);
        borderPane.setBottom(bottomBox);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }



    public static void setupTableViewColumns() {
        // 船名
        TableColumn<ShipSingleInfo, String> colShipName = new TableColumn<>("船名");
        colShipName.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        // 船主姓名
        TableColumn<ShipSingleInfo, String> colOwnerName = new TableColumn<>("船主姓名");
        colOwnerName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        // 身份证号
        TableColumn<ShipSingleInfo, String> colOwnerId = new TableColumn<>("身份证号");
        colOwnerId.setCellValueFactory(new PropertyValueFactory<>("ownerId"));

        // 联系电话
        TableColumn<ShipSingleInfo, String> colContactNumber = new TableColumn<>("联系电话");
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        // 详细住址
        TableColumn<ShipSingleInfo, String> colAddress = new TableColumn<>("详细住址");
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        // 船舶类型
        TableColumn<ShipSingleInfo, String> colShipType = new TableColumn<>("船舶类型");
        colShipType.setCellValueFactory(new PropertyValueFactory<>("shipType"));

        // 船籍港
        TableColumn<ShipSingleInfo, String> colPortOfRegistry = new TableColumn<>("船籍港");
        colPortOfRegistry.setCellValueFactory(new PropertyValueFactory<>("portOfRegistry"));

        // 建造厂
        TableColumn<ShipSingleInfo, String> colShipyard = new TableColumn<>("建造厂");
        colShipyard.setCellValueFactory(new PropertyValueFactory<>("shipyard"));

        // 总长
        TableColumn<ShipSingleInfo, Double> colLength = new TableColumn<>("总长");
        colLength.setCellValueFactory(new PropertyValueFactory<>("length"));

        // 型宽
        TableColumn<ShipSingleInfo, Double> colWidth = new TableColumn<>("型宽");
        colWidth.setCellValueFactory(new PropertyValueFactory<>("width"));

        // 型深
        TableColumn<ShipSingleInfo, Double> colDepth = new TableColumn<>("型深");
        colDepth.setCellValueFactory(new PropertyValueFactory<>("depth"));

        // 总吨
        TableColumn<ShipSingleInfo, Double> colGrossTonnage = new TableColumn<>("总吨");
        colGrossTonnage.setCellValueFactory(new PropertyValueFactory<>("grossTonnage"));

        // 净吨
        TableColumn<ShipSingleInfo, Double> colNetTonnage = new TableColumn<>("净吨");
        colNetTonnage.setCellValueFactory(new PropertyValueFactory<>("netTonnage"));

        // 主机功率
        TableColumn<ShipSingleInfo, Double> colMainEnginePower = new TableColumn<>("主机功率");
        colMainEnginePower.setCellValueFactory(new PropertyValueFactory<>("mainEnginePower"));

        // 载重吨
        TableColumn<ShipSingleInfo, Double> colDeadweightTonnage = new TableColumn<>("载重吨");
        colDeadweightTonnage.setCellValueFactory(new PropertyValueFactory<>("deadweightTonnage"));

        // 航行区域
        TableColumn<ShipSingleInfo, String> colNavigationArea = new TableColumn<>("航行区域");
        colNavigationArea.setCellValueFactory(new PropertyValueFactory<>("navigationArea"));

        // 备注
        TableColumn<ShipSingleInfo, String> colRemarks = new TableColumn<>("备注");
        colRemarks.setCellValueFactory(new PropertyValueFactory<>("remarks"));

        // 船检登记号
        TableColumn<ShipSingleInfo, String> colShipInspectionRegistrationNumber = new TableColumn<>("船检登记号");
        colShipInspectionRegistrationNumber.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRegistrationNumber"));

        // 营运证编号
        TableColumn<ShipSingleInfo, String> colOperationCertificateNumber = new TableColumn<>("营运证编号");
        colOperationCertificateNumber.setCellValueFactory(new PropertyValueFactory<>("operationCertificateNumber"));

        // 发证机关
        TableColumn<ShipSingleInfo, String> colIssuingAuthority = new TableColumn<>("发证机关");
        colIssuingAuthority.setCellValueFactory(new PropertyValueFactory<>("issuingAuthority"));

        // 检验证编号
        TableColumn<ShipSingleInfo, String> colInspectionCertificateNumber = new TableColumn<>("检验证编号");
        colInspectionCertificateNumber.setCellValueFactory(new PropertyValueFactory<>("inspectionCertificateNumber"));

        // 船舶检验类型
        TableColumn<ShipSingleInfo, String> colShipInspectionType = new TableColumn<>("船舶检验类型");
        colShipInspectionType.setCellValueFactory(new PropertyValueFactory<>("shipInspectionType"));

        // 安检证书编号
        TableColumn<ShipSingleInfo, String> colSafetyInspectionCertificateNumber = new TableColumn<>("安检证书编号");
        colSafetyInspectionCertificateNumber.setCellValueFactory(new PropertyValueFactory<>("safetyInspectionCertificateNumber"));

        // 检查机关
        TableColumn<ShipSingleInfo, String> colInspectionAgency = new TableColumn<>("检查机关");
        colInspectionAgency.setCellValueFactory(new PropertyValueFactory<>("inspectionAgency"));

        // 国籍配员证书编号
        TableColumn<ShipSingleInfo, String> colNationalityCrewCertificateNumber = new TableColumn<>("国籍配员证书编号");
        colNationalityCrewCertificateNumber.setCellValueFactory(new PropertyValueFactory<>("nationalityCrewCertificateNumber"));

        // 航道费
        TableColumn<ShipSingleInfo, Double> colChannelFee = new TableColumn<>("航道费");
        colChannelFee.setCellValueFactory(new PropertyValueFactory<>("channelFee"));

        // 航道费合计
        TableColumn<ShipSingleInfo, Double> colTotalChannelFee = new TableColumn<>("航道费合计");
        colTotalChannelFee.setCellValueFactory(new PropertyValueFactory<>("totalChannelFee"));

        // 水运费合计
        TableColumn<ShipSingleInfo, Double> colTotalWaterTransportFee = new TableColumn<>("水运费合计");
        colTotalWaterTransportFee.setCellValueFactory(new PropertyValueFactory<>("totalWaterTransportFee"));

        // 水运费缴纳记录
        TableColumn<ShipSingleInfo, String> colWaterTransportFeePaymentRecord = new TableColumn<>("水运费缴纳记录");
        colWaterTransportFeePaymentRecord.setCellValueFactory(new PropertyValueFactory<>("waterTransportFeePaymentRecord"));

        // 将所有列添加到tableView
        tableView.getColumns().addAll(colShipName, colOwnerName, colOwnerId, colContactNumber, colAddress,
                colShipType, colPortOfRegistry, colShipyard, colLength, colWidth, colDepth,
                colGrossTonnage, colNetTonnage, colMainEnginePower, colDeadweightTonnage,
                colNavigationArea, colRemarks, colShipInspectionRegistrationNumber,
                colOperationCertificateNumber, colIssuingAuthority, colInspectionCertificateNumber,
                colShipInspectionType, colSafetyInspectionCertificateNumber, colInspectionAgency,
                colNationalityCrewCertificateNumber, colChannelFee, colTotalChannelFee,
                colTotalWaterTransportFee, colWaterTransportFeePaymentRecord);
    }

    private static void loadData() {
        // 如果静态变量中没有数据，或者需要强制刷新数据，才从数据库获取数据
        if (allCertificateInfo.isEmpty()) {
            // 假设 MysqlConnectiontest 是您的数据库连接类，并且 fetchEffectiveCertificateInfo 是用于获取数据的方法
            MysqlConnectiontest dbConnection = new MysqlConnectiontest();
            allCertificateInfo = dbConnection.shipAllData();
        }
        tableView.setItems(FXCollections.observableArrayList(allCertificateInfo));
    }


    private static void performSearch(String searchText) {
        List<ShipSingleInfo> filteredList = new ArrayList<>();
        for (ShipSingleInfo info : allCertificateInfo) {
            if (info.getShipName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(info);
            }
        }
        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }


    private static void saveCSV(TableView<ShipSingleInfo> tableView) throws IOException {
        FileChooser fileChooser = new FileChooser();
        // 设置初始文件名
        fileChooser.setInitialFileName("ShipSingleInfo.csv");
        // 设置文件类型过滤器
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        // 显示保存文件对话框
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
                // 写入UTF-8 BOM
                writer.write('\ufeff');
                // 写入标题行
                String header = "船名,船主姓名,身份证号,联系电话,详细住址,船舶类型,船籍港,建造厂,总长,型宽,型深,总吨,净吨,主机功率,载重吨,航行区域,备注,船检登记号,营运证编号,发证机关,检验证编号,船舶检验类型,安检证书编号,检查机关,国籍配员证书编号,航道费,航道费合计,水运费合计,水运费缴纳记录";
                writer.write(header);
                writer.newLine();

                // 遍历表格中的每一行数据
                for (ShipSingleInfo info : tableView.getItems()) {
                    String row = String.join(",",
                            info.getShipName(),
                            info.getOwnerName(),
                            info.getOwnerId(),
                            info.getContactNumber(),
                            info.getAddress(),
                            info.getShipType(),
                            info.getPortOfRegistry(),
                            info.getShipyard(),
                            String.valueOf(info.getLength()),
                            String.valueOf(info.getWidth()),
                            String.valueOf(info.getDepth()),
                            String.valueOf(info.getGrossTonnage()),
                            String.valueOf(info.getNetTonnage()),
                            String.valueOf(info.getMainEnginePower()),
                            String.valueOf(info.getDeadweightTonnage()),
                            info.getNavigationArea(),
                            info.getRemarks(),
                            info.getShipInspectionRegistrationNumber(),
                            info.getOperationCertificateNumber(),
                            info.getIssuingAuthority(),
                            info.getInspectionCertificateNumber(),
                            info.getShipInspectionType(),
                            info.getSafetyInspectionCertificateNumber(),
                            info.getInspectionAgency(),
                            info.getNationalityCrewCertificateNumber(),
                            String.valueOf(info.getChannelFee()),
                            String.valueOf(info.getTotalChannelFee()),
                            String.valueOf(info.getTotalWaterTransportFee()),
                            info.getWaterTransportFeePaymentRecord()
                    );
                    writer.write(row);
                    writer.newLine();
                }
            }
        }
    }


}
