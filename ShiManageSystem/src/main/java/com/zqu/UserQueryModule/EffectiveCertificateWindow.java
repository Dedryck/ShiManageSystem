package com.zqu.UserQueryModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-20:39
 * @description:
 */
public class EffectiveCertificateWindow {
    public static TableView<EffectiveCertificateInfo> tableView = new TableView<>(); // Object应替换为您的数据模型类
    private static List<EffectiveCertificateInfo> allCertificateInfo = new ArrayList<>();

    private static ComboBox<String> certificateTypeComboBox = new ComboBox<>(
            FXCollections.observableArrayList("中间检验", "危险品", "国籍证书", "坞内检验", "油污证书", "港澳证明")
    );
    private static ComboBox<String> monthComboBox = new ComboBox<>(
            FXCollections.observableArrayList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月")
    );

    public static void show(Stage parentStage){
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("用户查询相关证书的有效日期"); // 设置窗体标题
        stage.setWidth(1024);
        stage.setHeight(768);
        String buttonStyle = "-fx-font-size: 14px;"; // 您可以调整这里的数值来改变字体大小

        // 创建按钮
        Button btnBack = new Button("<---返回上一级");
        btnBack.setStyle(buttonStyle);
//        Button btnPrint = new Button("打印");
//        btnPrint.setStyle(buttonStyle);
        Button btnCancel = new Button("刷新");
        btnCancel.setStyle(buttonStyle);


        // 设置按钮动作（示例）
        btnBack.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 关闭当前窗体
        // 创建标签、搜索框和打印按钮
        // 创建下拉框

        btnCancel.setOnAction(event -> {
            // 清除表格内的数据
            tableView.getItems().clear();
// 清空下拉框的选择
            certificateTypeComboBox.getSelectionModel().clearSelection();
            monthComboBox.getSelectionModel().clearSelection();
            // 重新加载allCertificateInfo中的数据
            loadData();
        });


        certificateTypeComboBox.setOnAction(event -> filterData());
        monthComboBox.setOnAction(event -> filterData());


        setupTableViewColumns();
        loadData();

        // 创建顶部区域，包含"打印"按钮和两个下拉框
        HBox topBox = new HBox(10, certificateTypeComboBox, monthComboBox,btnCancel);
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

    private static void setupTableViewColumns() {
        // 船名
        TableColumn<EffectiveCertificateInfo, String> colShipName = new TableColumn<>("船名");
        colShipName.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        // 中间检验办理日期
        TableColumn<EffectiveCertificateInfo, Date> colIntermediateInspectionHandlingDate = new TableColumn<>("中间检验办理日期");
        colIntermediateInspectionHandlingDate.setCellValueFactory(new PropertyValueFactory<>("intermediateInspectionHandlingDate"));

        // 中间检验证书有效期至
        TableColumn<EffectiveCertificateInfo, Date> colIntermediateInspectionCertificateExpiryDate = new TableColumn<>("中间检验证书有效期至");
        colIntermediateInspectionCertificateExpiryDate.setCellValueFactory(new PropertyValueFactory<>("intermediateInspectionCertificateExpiryDate"));

        // 危险品办理日期
        TableColumn<EffectiveCertificateInfo, Date> colDangerousGoodsHandlingDate = new TableColumn<>("危险品办理日期");
        colDangerousGoodsHandlingDate.setCellValueFactory(new PropertyValueFactory<>("dangerousGoodsHandlingDate"));

        // 危险品证书有效期至
        TableColumn<EffectiveCertificateInfo, Date> colDangerousGoodsCertificateExpiryDate = new TableColumn<>("危险品证书有效期至");
        colDangerousGoodsCertificateExpiryDate.setCellValueFactory(new PropertyValueFactory<>("dangerousGoodsCertificateExpiryDate"));

        // 各证书有效期至
        TableColumn<EffectiveCertificateInfo, Date> colVariousCertificatesExpiryDate = new TableColumn<>("各证书有效期至");
        colVariousCertificatesExpiryDate.setCellValueFactory(new PropertyValueFactory<>("variousCertificatesExpiryDate"));

        // 国籍证书办理日期
        TableColumn<EffectiveCertificateInfo, Date> colNationalityCertificateHandlingDate = new TableColumn<>("国籍证书办理日期");
        colNationalityCertificateHandlingDate.setCellValueFactory(new PropertyValueFactory<>("nationalityCertificateHandlingDate"));

        // 国籍证书有效期至
        TableColumn<EffectiveCertificateInfo, Date> colNationalityCertificateExpiryDate = new TableColumn<>("国籍证书有效期至");
        colNationalityCertificateExpiryDate.setCellValueFactory(new PropertyValueFactory<>("nationalityCertificateExpiryDate"));

        // 坞内检验办理日期
        TableColumn<EffectiveCertificateInfo, Date> colDockyardInspectionHandlingDate = new TableColumn<>("坞内检验办理日期");
        colDockyardInspectionHandlingDate.setCellValueFactory(new PropertyValueFactory<>("dockyardInspectionHandlingDate"));

        // 坞内检验证书有效期至
        TableColumn<EffectiveCertificateInfo, Date> colDockyardInspectionCertificateExpiryDate = new TableColumn<>("坞内检验证书有效期至");
        colDockyardInspectionCertificateExpiryDate.setCellValueFactory(new PropertyValueFactory<>("dockyardInspectionCertificateExpiryDate"));

        // 油污证书办理日期
        TableColumn<EffectiveCertificateInfo, Date> colOilPollutionHandlingDate = new TableColumn<>("油污证书办理日期");
        colOilPollutionHandlingDate.setCellValueFactory(new PropertyValueFactory<>("oilPollutionHandlingDate"));

        // 油污证书有效期至
        TableColumn<EffectiveCertificateInfo, Date> colOilPollutionCertificateExpiryDate = new TableColumn<>("油污证书有效期至");
        colOilPollutionCertificateExpiryDate.setCellValueFactory(new PropertyValueFactory<>("oilPollutionCertificateExpiryDate"));

        // 港澳证明办理日期
        TableColumn<EffectiveCertificateInfo, Date> colHongKongMacaoHandlingDate = new TableColumn<>("港澳证明办理日期");
        colHongKongMacaoHandlingDate.setCellValueFactory(new PropertyValueFactory<>("hongKongMacaoHandlingDate"));

        // 港澳证明有效期至
        TableColumn<EffectiveCertificateInfo, Date> colHongKongMacaoCertificateExpiryDate = new TableColumn<>("港澳证明有效期至");
        colHongKongMacaoCertificateExpiryDate.setCellValueFactory(new PropertyValueFactory<>("hongKongMacaoCertificateExpiryDate"));

        // 将所有列添加到tableView中
        tableView.getColumns().addAll(
                colShipName, colIntermediateInspectionHandlingDate, colIntermediateInspectionCertificateExpiryDate,
                colDangerousGoodsHandlingDate, colDangerousGoodsCertificateExpiryDate, colVariousCertificatesExpiryDate,
                colNationalityCertificateHandlingDate, colNationalityCertificateExpiryDate,
                colDockyardInspectionHandlingDate, colDockyardInspectionCertificateExpiryDate,
                colOilPollutionHandlingDate, colOilPollutionCertificateExpiryDate,
                colHongKongMacaoHandlingDate, colHongKongMacaoCertificateExpiryDate
        );
    }

    private static void loadData() {
        // 如果静态变量中没有数据，或者需要强制刷新数据，才从数据库获取数据
        if (allCertificateInfo.isEmpty()) {
            // 假设 MysqlConnectiontest 是您的数据库连接类，并且 fetchEffectiveCertificateInfo 是用于获取数据的方法
            MysqlConnectiontest dbConnection = new MysqlConnectiontest();
            allCertificateInfo = dbConnection.fetchEffectiveCertificateInfo();
        }
        tableView.setItems(FXCollections.observableArrayList(allCertificateInfo));
    }

    private static void filterData() {
        String selectedCertificateType = certificateTypeComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();

        int selectedMonthNumber = selectedMonth != null ? Integer.parseInt(selectedMonth.replaceAll("[^0-9]", "")) - 1 : -1;

        List<EffectiveCertificateInfo> filteredList = allCertificateInfo.stream()
                .filter(info -> {
                    boolean typeMatch = false;
                    boolean monthMatch = false;

                    // 检查证书类型，并根据类型选择对应的日期字段
                    Date relevantDate = null;
                    if ("中间检验".equals(selectedCertificateType)) {
                        relevantDate = info.getIntermediateInspectionCertificateExpiryDate();
                    } else if ("危险品".equals(selectedCertificateType)) {
                        relevantDate = info.getDangerousGoodsCertificateExpiryDate();
                    } else if ("国籍证书".equals(selectedCertificateType)) {
                        relevantDate = info.getNationalityCertificateExpiryDate();
                    } else if ("坞内检验".equals(selectedCertificateType)) {
                        relevantDate = info.getDockyardInspectionCertificateExpiryDate();
                    } else if ("油污证书".equals(selectedCertificateType)) {
                        relevantDate = info.getOilPollutionCertificateExpiryDate();
                    } else if ("港澳证明".equals(selectedCertificateType)) {
                        relevantDate = info.getHongKongMacaoCertificateExpiryDate();
                    }
// ... 为其他证书类型添加相应的条件


                    // 如果选定了证书类型，但没有相关日期，则直接返回false
                    if (selectedCertificateType != null && relevantDate == null) {
                        return false;
                    }

                    // 检查月份是否匹配
                    if (selectedMonthNumber != -1 && relevantDate != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(relevantDate);
                        int infoMonth = cal.get(Calendar.MONTH);
                        monthMatch = infoMonth == selectedMonthNumber;
                    } else if (selectedMonthNumber == -1) {
                        // 如果没有选择月份，不考虑月份匹配
                        monthMatch = true;
                    }

                    // 如果没有选定证书类型，不考虑类型匹配
                    typeMatch = selectedCertificateType == null || relevantDate != null;

                    return monthMatch && typeMatch;
                })
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }






}
