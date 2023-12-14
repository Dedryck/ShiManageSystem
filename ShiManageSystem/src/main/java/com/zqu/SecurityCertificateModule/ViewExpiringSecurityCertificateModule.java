package com.zqu.SecurityCertificateModule;

import com.zqu.MysqlConnectiontest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @aurhor Dedryck
 * @create 2023-12-07-17:50
 * @description: 用于安检证书到期显示到期提醒
 */
public class ViewExpiringSecurityCertificateModule {

    public static TableView<ShipSecurityCertificate> table;

//    在类中定义一个成员变量来保存下拉框当前选中的值
    private static String currentFilterOption = "";

    public static void show(Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle("查看即将到期的船只安检证书");
        String buttonStyle = "-fx-font-size: 16px;"; // 您可以调整这里的数值来改变字体大小


        BorderPane borderPane = new BorderPane();

        // 创建返回按钮并放在左下角
        Button backButton = new Button("<---返回上一级");
        backButton.setStyle(buttonStyle);

        backButton.setOnAction(e -> {
            parentStage.show(); // 显示父窗体
            stage.close(); // 关闭当前窗体
        });

        table = new TableView<>();
        // 创建并设置表格列
        TableColumn<ShipSecurityCertificate, String> shipNameColumn = new TableColumn<>("船名");
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));

        TableColumn<ShipSecurityCertificate, String> registrationNumberColumn = new TableColumn<>("船检登记号");
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipInspectionRegistrationNumber"));

        TableColumn<ShipSecurityCertificate, String> certificateNumberColumn = new TableColumn<>("安检证书编号");
        certificateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("securityCertificateNumber"));

        TableColumn<ShipSecurityCertificate, String> shipOwnerColumn = new TableColumn<>("船舶所有人");
        shipOwnerColumn.setCellValueFactory(new PropertyValueFactory<>("shipOwner"));

        TableColumn<ShipSecurityCertificate, String> shipRegistrationNumberColumn = new TableColumn<>("船舶登记号");
        shipRegistrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shipRegistrationNumber"));

        TableColumn<ShipSecurityCertificate, String> inspectionAuthorityColumn = new TableColumn<>("检查机关");
        inspectionAuthorityColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionAuthority"));

        TableColumn<ShipSecurityCertificate, Date> nextInspectionDateColumn = new TableColumn<>("下次检查时间");
        nextInspectionDateColumn.setCellValueFactory(new PropertyValueFactory<>("nextInspectionDate"));

        TableColumn<ShipSecurityCertificate, Date> notificationDateColumn = new TableColumn<>("通知时间");
        notificationDateColumn.setCellValueFactory(new PropertyValueFactory<>("notificationDate"));

        TableColumn<ShipSecurityCertificate, Date> validityEndDateColumn = new TableColumn<>("安检证书使用有效期至");
        validityEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("certificateValidityEndDate"));

        TableColumn<ShipSecurityCertificate, Date> issueDateColumn = new TableColumn<>("发证日期");
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<ShipSecurityCertificate, String> inspectionRecordColumn = new TableColumn<>("船只检验情况记录");
        inspectionRecordColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionRecord"));

        // 将列添加到表格
        table.getColumns().addAll(shipNameColumn, registrationNumberColumn, certificateNumberColumn, shipOwnerColumn, shipRegistrationNumberColumn, inspectionAuthorityColumn, nextInspectionDateColumn, notificationDateColumn, validityEndDateColumn, issueDateColumn, inspectionRecordColumn);
        reloadData();



        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT); // 对齐到底部左侧
        borderPane.setBottom(backButton);

        // 创建下拉框和日期选择器
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(
                "下次检查时间",
                "通知时间",
                "安检证书使用有效期至",
                "发证日期"
        );
        DatePicker datePicker = new DatePicker();
        // 为DatePicker添加事件监听器
        datePicker.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                // 计算前30天的日期
                LocalDate startDate = selectedDate.minus(30, ChronoUnit.DAYS);
                filterData(startDate, selectedDate);
            }else{
                reloadData();
            }
        });

        // 为下拉框添加事件监听器
        comboBox.setOnAction(event -> {
            currentFilterOption = comboBox.getValue();
            // 如果DatePicker已经选择了日期，则执行筛选
            if (datePicker.getValue() != null) {
                LocalDate selectedDate = datePicker.getValue();
                LocalDate startDate = selectedDate.minus(30, ChronoUnit.DAYS);
                filterData(startDate, selectedDate);
            }else{
                reloadData();
            }
        });




        HBox topLayout = new HBox(10, comboBox, datePicker); // 组件间隔为10
        topLayout.setAlignment(Pos.CENTER); // 居中对齐
        borderPane.setCenter(table); // 将表格放置在中央
        borderPane.setTop(topLayout); // 将HBox放在顶部

        // 设置布局到Scene并显示Stage
        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setScene(scene);
        stage.show();

        // 隐藏父窗体
        parentStage.hide();
    }

    public static void reloadData() {
        // 从数据库加载数据
        List<ShipSecurityCertificate> certificates = MysqlConnectiontest.viewExpiringSecurityCertificateData();

        // 将数据转换为ObservableList
        ObservableList<ShipSecurityCertificate> observableList = FXCollections.observableArrayList(certificates);

        // 更新表格数据
        table.setItems(observableList);

    }


    // 定义筛选数据的方法
    private static void filterData(LocalDate startDate, LocalDate endDate) {
        // 从数据库或当前表格数据中筛选数据
        List<ShipSecurityCertificate> filteredCertificates = MysqlConnectiontest.loadDataSecurityCertificate()
                .stream()
                .filter(certificate -> {
                    LocalDate dateToCompare;
                    // 根据下拉框选项确定要比较的日期
                    switch (currentFilterOption) {
                        case "下次检查时间":
                            dateToCompare = convertToLocalDate(certificate.getNextInspectionDate());
                            break;
                        case "通知时间":
                            dateToCompare = convertToLocalDate(certificate.getNotificationDate());
                            break;
                        case "安检证书使用有效期至":
                            dateToCompare = convertToLocalDate(certificate.getCertificateValidityEndDate());
                            break;
                        case "发证日期":
                            dateToCompare = convertToLocalDate(certificate.getIssueDate());
                            break;
                        // 如果需要，可以为其他选项添加更多case
                        default:
                            return false;
                    }
                    // 检查日期是否在指定范围内
                    return (dateToCompare.isAfter(startDate) || dateToCompare.isEqual(startDate))
                            && (dateToCompare.isBefore(endDate) || dateToCompare.isEqual(endDate));
                })
                .collect(Collectors.toList());

        // 更新表格数据
        ObservableList<ShipSecurityCertificate> observableList = FXCollections.observableArrayList(filteredCertificates);
        table.setItems(observableList);
    }


    private static LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        if (dateToConvert instanceof java.sql.Date) {
            // 处理 java.sql.Date 类型
            return ((java.sql.Date) dateToConvert).toLocalDate();
        } else {
            // 处理 java.util.Date 类型
            return dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
    }













}
