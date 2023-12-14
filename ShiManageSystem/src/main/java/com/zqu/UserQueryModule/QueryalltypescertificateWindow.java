package com.zqu.UserQueryModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @aurhor Dedryck
 * @create 2023-12-10-15:22
 * @description: 查询实现窗口
 */
public class QueryalltypescertificateWindow {
    public static TableView<CertificateInfo> tableView = new TableView<>(); // Object应替换为您的数据模型类
    private static List<CertificateInfo> allCertificateInfo = new ArrayList<>();

    public static void show(Stage parentStage){
        // 创建一个新的Stage
        Stage stage = new Stage();
        stage.setTitle("用户查询各类证书功能"); // 设置窗体标题
        stage.setWidth(1024);
        stage.setHeight(768);
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小

        // 创建按钮
        Button btnBack = new Button("<---返回上一级");
        btnBack.setStyle(buttonStyle);



        // 设置按钮动作（示例）
        btnBack.setOnAction(event -> {
            stage.close();
            parentStage.show();
        }); // 关闭当前窗体



        // 船名
        TableColumn<CertificateInfo, String> shipNameColumn = new TableColumn<>("船名");
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        // 船检登记号
        TableColumn<CertificateInfo, String> shipRegistrationNumberColumn = new TableColumn<>("船检登记号");
        shipRegistrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        // 船舶所有人
        TableColumn<CertificateInfo, String> shipOwnerColumn = new TableColumn<>("船舶所有人");
        shipOwnerColumn.setCellValueFactory(new PropertyValueFactory<>("shipOwner"));

        // 船舶登记号
        TableColumn<CertificateInfo, String> shipRegisterNumberColumn = new TableColumn<>("船舶登记号");
        shipRegisterNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipRegisterNumber"));

        // 国籍配员证书编号
        TableColumn<CertificateInfo, String> nationalityCrewCertificateNumberColumn = new TableColumn<>("国籍配员证书编号");
        nationalityCrewCertificateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("nationalityCrewCertificateNumber"));

        // 下次换证时间
        TableColumn<CertificateInfo, String> nextCrewChangeDateColumn = new TableColumn<>("下次换证时间");
        nextCrewChangeDateColumn.setCellValueFactory(new PropertyValueFactory<>("nextCrewChangeDate"));

// 换证通知时间
        TableColumn<CertificateInfo, String> crewChangeNoticeDateColumn = new TableColumn<>("换证通知时间");
        crewChangeNoticeDateColumn.setCellValueFactory(new PropertyValueFactory<>("crewChangeNoticeDate"));

// 国籍配员证书使用有效期至
        TableColumn<CertificateInfo, String> crewCertificateValidityColumn = new TableColumn<>("国籍配员证书使用有效期至");
        crewCertificateValidityColumn.setCellValueFactory(new PropertyValueFactory<>("crewCertificateValidity"));

// 国籍配员发证日期
        TableColumn<CertificateInfo, String> crewCertificateIssueDateColumn = new TableColumn<>("国籍配员发证日期");
        crewCertificateIssueDateColumn.setCellValueFactory(new PropertyValueFactory<>("crewCertificateIssueDate"));

// 国籍配员证书换证时间记录
        TableColumn<CertificateInfo, String> crewCertificateChangeRecordColumn = new TableColumn<>("国籍配员证书换证时间记录");
        crewCertificateChangeRecordColumn.setCellValueFactory(new PropertyValueFactory<>("crewCertificateChangeRecord"));

// 安检证书编号
        TableColumn<CertificateInfo, String> safetyInspectionCertificateNumberColumn = new TableColumn<>("安检证书编号");
        safetyInspectionCertificateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("safetyInspectionCertificateNumber"));

// 检查机关
        TableColumn<CertificateInfo, String> inspectionAuthorityColumn = new TableColumn<>("检查机关");
        inspectionAuthorityColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionAuthority"));

// 下次检查时间
        TableColumn<CertificateInfo, String> nextInspectionDateColumn = new TableColumn<>("下次检查时间");
        nextInspectionDateColumn.setCellValueFactory(new PropertyValueFactory<>("nextInspectionDate"));

// 安检通知时间
        TableColumn<CertificateInfo, String> safetyInspectionNoticeDateColumn = new TableColumn<>("安检通知时间");
        safetyInspectionNoticeDateColumn.setCellValueFactory(new PropertyValueFactory<>("safetyInspectionNoticeDate"));

// 安检证书有效期至
        TableColumn<CertificateInfo, String> safetyCertificateValidityColumn = new TableColumn<>("安检证书有效期至");
        safetyCertificateValidityColumn.setCellValueFactory(new PropertyValueFactory<>("safetyCertificateValidity"));

// 安检证书发证日期
        TableColumn<CertificateInfo, String> safetyCertificateIssueDateColumn = new TableColumn<>("安检证书发证日期");
        safetyCertificateIssueDateColumn.setCellValueFactory(new PropertyValueFactory<>("safetyCertificateIssueDate"));

// 船只检验情况记录
        TableColumn<CertificateInfo, String> shipInspectionRecordColumn = new TableColumn<>("船只检验情况记录");
        shipInspectionRecordColumn.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRecord"));

// 检验证编号
        TableColumn<CertificateInfo, String> inspectionCertificateNumberColumn = new TableColumn<>("检验证编号");
        inspectionCertificateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionCertificateNumber"));

// 船舶检验类型
        TableColumn<CertificateInfo, String> inspectionTypeColumn = new TableColumn<>("船舶检验类型");
        inspectionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionType"));

// 检验证下次检验时间
        TableColumn<CertificateInfo, String> nextInspectionCertificateDateColumn = new TableColumn<>("检验证下次检验时间");
        nextInspectionCertificateDateColumn.setCellValueFactory(new PropertyValueFactory<>("nextInspectionCertificateDate"));

// 检验证通知时间
        TableColumn<CertificateInfo, String> inspectionCertificateNoticeDateColumn = new TableColumn<>("检验证通知时间");
        inspectionCertificateNoticeDateColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionCertificateNoticeDate"));

// 检验证检验机关
        TableColumn<CertificateInfo, String> inspectionAgencyColumn = new TableColumn<>("检验证检验机关");
        inspectionAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionAgency"));

// 检验证使用有效期至
        TableColumn<CertificateInfo, String> inspectionCertificateValidityColumn = new TableColumn<>("检验证使用有效期至");
        inspectionCertificateValidityColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionCertificateValidity"));

// 检验证发证日期
        TableColumn<CertificateInfo, String> inspectionCertificateIssueDateColumn = new TableColumn<>("检验证发证日期");
        inspectionCertificateIssueDateColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionCertificateIssueDate"));

// 检验证情况记录
        TableColumn<CertificateInfo, String> inspectionSituationRecordColumn = new TableColumn<>("检验证情况记录");
        inspectionSituationRecordColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionSituationRecord"));

// 营运证编号
        TableColumn<CertificateInfo, String> operationCertificateNumberColumn = new TableColumn<>("营运证编号");
        operationCertificateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("operationCertificateNumber"));

// 经营人许可证号码
        TableColumn<CertificateInfo, String> operatorLicenseNumberColumn = new TableColumn<>("经营人许可证号码");
        operatorLicenseNumberColumn.setCellValueFactory(new PropertyValueFactory<>("operatorLicenseNumber"));

// 管理人许可证号码
        TableColumn<CertificateInfo, String> managerLicenseNumberColumn = new TableColumn<>("管理人许可证号码");
        managerLicenseNumberColumn.setCellValueFactory(new PropertyValueFactory<>("managerLicenseNumber"));

// 营运证发证机关
        TableColumn<CertificateInfo, String> operationCertificateIssueAuthorityColumn = new TableColumn<>("营运证发证机关");
        operationCertificateIssueAuthorityColumn.setCellValueFactory(new PropertyValueFactory<>("operationCertificateIssueAuthority"));

// 营运证使用有效期至
        TableColumn<CertificateInfo, String> operationCertificateValidityColumn = new TableColumn<>("营运证使用有效期至");
        operationCertificateValidityColumn.setCellValueFactory(new PropertyValueFactory<>("operationCertificateValidity"));

// 营运证发证日期
        TableColumn<CertificateInfo, String> operationCertificateIssueDateColumn = new TableColumn<>("营运证发证日期");
        operationCertificateIssueDateColumn.setCellValueFactory(new PropertyValueFactory<>("operationCertificateIssueDate"));

// 添加所有定义的列到 TableView
        tableView.getColumns().addAll(
                shipNameColumn, shipRegistrationNumberColumn, shipOwnerColumn, shipRegisterNumberColumn,
                nationalityCrewCertificateNumberColumn, nextCrewChangeDateColumn, crewChangeNoticeDateColumn,
                crewCertificateValidityColumn, crewCertificateIssueDateColumn, crewCertificateChangeRecordColumn,
                safetyInspectionCertificateNumberColumn, inspectionAuthorityColumn, nextInspectionDateColumn,
                safetyInspectionNoticeDateColumn, safetyCertificateValidityColumn, safetyCertificateIssueDateColumn,
                shipInspectionRecordColumn, inspectionCertificateNumberColumn, inspectionTypeColumn,
                nextInspectionCertificateDateColumn, inspectionCertificateNoticeDateColumn, inspectionAgencyColumn,
                inspectionCertificateValidityColumn, inspectionCertificateIssueDateColumn, inspectionSituationRecordColumn,
                operationCertificateNumberColumn, operatorLicenseNumberColumn, managerLicenseNumberColumn,
                operationCertificateIssueAuthorityColumn, operationCertificateValidityColumn, operationCertificateIssueDateColumn
        );



        reloadData();



        // 创建标签、搜索框和打印按钮
        Label lblShipName = new Label("船名");
        TextField txtSearch = new TextField();
        txtSearch.setPrefWidth(200); // 设置搜索框长度
        Button btnPrint = new Button("打印");
        btnPrint.setStyle(buttonStyle);
        btnPrint.setOnAction(event -> {
            try {
                saveAsCSV();
            } catch (IOException e) {
                e.printStackTrace();
                // 这里可以添加错误处理代码
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                tableView.setItems(null); // 或者 tableView.setItems(FXCollections.observableArrayList(allCertificateInfo)) 显示所有数据
            } else {
                filterData(newValue);
            }
        });


        // 组合按键布局
        HBox hBoxTop = new HBox(10); // 间距为10
        hBoxTop.setAlignment(Pos.CENTER);
        hBoxTop.getChildren().addAll(lblShipName, txtSearch, btnPrint);

        // 底部按钮布局
        HBox hBoxBottom = new HBox(); // 用于底部按钮
        hBoxBottom.setAlignment(Pos.BOTTOM_LEFT);
        hBoxBottom.getChildren().add(btnBack);

        // 设置BorderPane布局
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hBoxTop);
        borderPane.setCenter(tableView); // 设置中间为tableView
        borderPane.setBottom(hBoxBottom);

        // 设置并显示场景
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }


    private static void reloadData() {
        if (allCertificateInfo.isEmpty()) {
            allCertificateInfo = MysqlConnectiontest.ViewQueryCertificateModule();
        }
        tableView.setItems(FXCollections.observableArrayList(allCertificateInfo));
    }



    private static void filterData(String shipName) {
        List<CertificateInfo> filteredList = allCertificateInfo.stream()
                .filter(c -> c.getShipName().toLowerCase().contains(shipName.toLowerCase()))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }


    private static void saveAsCSV() throws IOException {
        FileChooser fileChooser = new FileChooser();
        // 设置初始文件名
        fileChooser.setInitialFileName("Certificates.csv");
        // 设置文件类型过滤器
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        // 显示保存文件对话框
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
                // 写入标题行
                // 写入UTF-8 BOM
                writer.write('\ufeff');
                String header = "船名,船检登记号,船舶所有人,船舶登记号,国籍配员证书编号,下次换证时间,换证通知时间,国籍配员证书使用有效期至,国籍配员发证日期,国籍配员证书换证时间记录,安检证书编号,检查机关,下次检查时间,安检通知时间,安检证书有效期至,安检证书发证日期,船只检验情况记录,检验证编号,船舶检验类型,检验证下次检验时间,检验证通知时间,检验证检验机关,检验证使用有效期至,检验证发证日期,检验证情况记录,营运证编号,经营人许可证号码,管理人许可证号码,营运证发证机关,营运证使用有效期至,营运证发证日期";
                writer.write(header);
                writer.newLine();

                // 遍历表格中的每一行数据
                // 遍历表格中的每一行数据
                for (CertificateInfo info : tableView.getItems()) {
                    String row = info.getShipName() + "," +
                            info.getShipRegistrationNumber() + "," +
                            info.getShipOwner() + "," +
                            info.getShipRegisterNumber() + "," +
                            info.getNationalityCrewCertificateNumber() + "," +
                            info.getNextCrewChangeDate() + "," +
                            info.getCrewChangeNoticeDate() + "," +
                            info.getCrewCertificateValidity() + "," +
                            info.getCrewCertificateIssueDate() + "," +
                            info.getCrewCertificateChangeRecord() + "," +
                            info.getSafetyInspectionCertificateNumber() + "," +
                            info.getInspectionAuthority() + "," +
                            info.getNextInspectionDate() + "," +
                            info.getSafetyInspectionNoticeDate() + "," +
                            info.getSafetyCertificateValidity() + "," +
                            info.getSafetyCertificateIssueDate() + "," +
                            info.getShipInspectionRecord() + "," +
                            info.getInspectionCertificateNumber() + "," +
                            info.getInspectionType() + "," +
                            info.getNextInspectionCertificateDate() + "," +
                            info.getInspectionCertificateNoticeDate() + "," +
                            info.getInspectionAgency() + "," +
                            info.getInspectionCertificateValidity() + "," +
                            info.getInspectionCertificateIssueDate() + "," +
                            info.getInspectionSituationRecord() + "," +
                            info.getOperationCertificateNumber() + "," +
                            info.getOperatorLicenseNumber() + "," +
                            info.getManagerLicenseNumber() + "," +
                            info.getOperationCertificateIssueAuthority() + "," +
                            info.getOperationCertificateValidity() + "," +
                            info.getOperationCertificateIssueDate();
                    writer.write(row);
                    writer.newLine();
                }

            }
        }
    }



}
