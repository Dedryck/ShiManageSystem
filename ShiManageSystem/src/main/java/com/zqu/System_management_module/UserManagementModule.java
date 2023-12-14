package com.zqu.System_management_module;

import com.zqu.MysqlConnectiontest;
import com.zqu.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DefaultStringConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import com.zqu.System_management_module.User;


import static com.zqu.MysqlConnectiontest.isUserIdExistInTable;
import static com.zqu.MysqlConnectiontest.loadDataIntoTable;

/**
 * @aurhor Dedryck
 * @create 2023-11-21-21:09
 * @description:
 */
public class UserManagementModule {
    // 定义枚举来表示不同的操作状态
    private enum ActionState {
        NONE, ADDING_USER, EDITING_PERMISSION, DELETING_USER
    }
    // 当前的操作状态
    private ActionState currentActionState = ActionState.NONE;

    private Button addUserButton, editPermissionButton, deleteUserButton;
    private Button confirmButton, cancelButton;
    private HBox buttonBox, confirmCancelBox;
    private TableView<User> table; // 将table变量提升为类成员变量

    private ObservableList<User> previousTableData; // 用于保存上一个表格数据状态

    private TableColumn<User, String> userIdCol; // 假设“用户ID”是一个类成员变量

    private boolean isPermissionEditMode = false; // 用于跟踪是否处于“修改权限”模式

    private boolean isAddUserMode = false;//用于跟踪是否处于“添加用户”模式


    // 增加成员变量
    private TableColumn<User, String> roleCol; // 权限列
//
//    下面是用于删除用户逻辑功能按键
    private ComboBox<String> searchComboBox;//搜索下拉框
    private TextField searchTextField;//搜索文本框
    private Button showDeleteRecordsButton;//删除用户记录框
    // 创建 MysqlConnectiontest 的实例
    private MysqlConnectiontest mysqlConnectiontest = new MysqlConnectiontest();

    private Stage deleteRecordsStage; // 引用子视图的窗口
    private TableView<User> deletedRecordsTableView; // 引用用户记录的表格视图
    private Stage parentStage; // 添加此行作为成员变量
    private Stage changePasswordStage;
    private String adminUserId; // 当前管理员的用户ID




//对于第二个界面的设定“用户管理”
    public void showUserManagementUI(Stage parentStage) {
    this.parentStage = parentStage; // 在这里设置parentStage

    Stage stage = new Stage();
    stage.setTitle("用户管理");

    table = new TableView<>();
    table.setEditable(false); // 初始化时设置表格为不可编辑
    setupTableColumns(table); // 设置表格列
    table.setItems(loadDataIntoTable()); // 填充表格数据
    table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // 取消横向滚动条的自动弹出


        // 初始化按钮
    initializeButtons();
    // 初始化搜索框和按钮
    initializeSearchAndDeleteRecordUI();

//        容器放置处！！！！！
    // 创建顶部的搜索框和下拉选择框的HBox
    HBox searchBox = new HBox();
    searchBox.setPadding(new Insets(10, 10, 10, 10));
    searchBox.setSpacing(10);
    searchBox.setAlignment(Pos.CENTER_LEFT);
    searchBox.getChildren().addAll(searchComboBox, searchTextField);

// 创建顶部的删除记录按钮的HBox
    HBox deleteRecordBox = new HBox();
    deleteRecordBox.setPadding(new Insets(10));
    deleteRecordBox.getChildren().add(showDeleteRecordsButton);
    deleteRecordBox.setAlignment(Pos.CENTER_RIGHT);

// 创建按钮的HBox
    HBox buttonBox = new HBox(10, addUserButton, editPermissionButton, deleteUserButton);
    buttonBox.setAlignment(Pos.CENTER);

// 创建确认和取消按钮的HBox
    confirmCancelBox.setAlignment(Pos.CENTER);
    confirmCancelBox.setVisible(false);

// 创建一个弹性空间填充物，将删除按钮推到右边
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

// 创建一个新的HBox来容纳搜索框、按钮和删除记录按钮
    HBox topControls = new HBox();
    topControls.getChildren().addAll(searchBox, buttonBox, spacer, deleteRecordBox);
//        容器放置处!!!!

    // 设置按钮的事件处理器
    setupButtonActions(table);

//        新增修改密码按钮
    // 新增按钮
    Button changeOtherUserPasswordButton = new Button("修改其他用户密码");
    changeOtherUserPasswordButton.setOnAction(e -> showChangePasswordUI());

    // 将新按钮添加到界面中
//        返回上一级的按钮!!!
    Button backButton = new Button("<--返回上一级");
    backButton.setMinHeight(30); // 增加按钮的高度
    backButton.setMaxWidth(100); // 按钮宽度设置
    backButton.setOnAction(e -> {
        stage.close();
        parentStage.show(); // 当当前窗口关闭时，重新显示父级窗口
    });
    // 为每个按钮设置最小宽度和高度
    addUserButton.setMinSize(100, 40);
    editPermissionButton.setMinSize(100, 40);
    deleteUserButton.setMinSize(100, 40);
    changeOtherUserPasswordButton.setMinSize(100, 40);
    backButton.setMinSize(100, 40);


    // 创建一个新的HBox来放置返回按钮，并设置对齐为左下角
    HBox backButtonBox = new HBox(10, changeOtherUserPasswordButton,backButton);
    backButtonBox.setAlignment(Pos.BOTTOM_LEFT);

    // 创建主VBox容器，包括新的顶部控制组件、表格和返回按钮
    VBox mainVBox = new VBox(topControls, confirmCancelBox, table, backButtonBox);
    mainVBox.setSpacing(10);

    // 隐藏搜索框和删除记录按钮
    searchComboBox.setVisible(false);
    searchTextField.setVisible(false);
    showDeleteRecordsButton.setVisible(false);

    //窗体显示具体操作；
    Scene scene = new Scene(mainVBox, 1024, 650);
    stage.setScene(scene);

    // 设置舞台为模态窗口，并指定父级舞台
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(parentStage);
    // 设置界面尺寸
    // 当新舞台显示时，隐藏父级舞台
    parentStage.hide();

    stage.show();
}

    private void setupUserTable() {
        TableColumn<User, String> userIdCol = new TableColumn<>("用户ID");
        userIdCol.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        // 为其他列做类似的设置


        // 更新表格数据
        updateTableData();
    }


    private void updateTableData() {
        // 从数据库中获取所有用户信息并显示
        // 使用 MysqlConnectiontest 的方法来获取数据
    }

//    “用户模块”下的添加功能
    private void addUser(TableView<User> table) {
        // 检查最后一行是否为空，如果为空，则不添加新行
        table.setEditable(true); // 启用编辑功能
//        userIdCol.setCellFactory(column -> new UserIdCell());
        isAddUserMode = true; // 开启添加用户模式
        userIdCol.setCellFactory(column -> new TextFieldTableCell<User, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                if (isAddUserMode && getIndex() == getTableView().getItems().size() - 1) {
                    super.startEdit();
                }
            }
        });

        // 在添加用户功能中禁用权限列的编辑功能
        roleCol.setEditable(false);
        if (!table.getItems().isEmpty()) {
            User lastUser = table.getItems().get(table.getItems().size() - 1);
            if (lastUser.getUserId() == null || lastUser.getUserId().trim().isEmpty()) {
                // 如果最后一行的用户ID为空，则不添加新行，并可能给出提示
                showAlert("提示", "请先完成当前行的编辑再添加新用户！", Alert.AlertType.INFORMATION);
                return;
            }
        }
        // 保存当前表格数据到previousTableData变量
        previousTableData = FXCollections.observableArrayList(table.getItems());


        // 添加新用户
        User newUser = new User("", "", "普通员工", "", "", "");
        table.getItems().add(newUser);
        table.scrollTo(newUser); // 滚动到新行
//        table.edit(table.getItems().size() - 1, userIdCol); // 开始编辑新行的“用户ID”列
        // 这里有所改动：我们需要在UI线程上稍后执行编辑操作，以确保表格完全更新
        Platform.runLater(() -> {
            // 开始编辑新行的“用户ID”列
            int newRow = table.getItems().size() - 1; // 新行的索引
            table.requestFocus(); // 请求表格的焦点
            table.getSelectionModel().select(newRow); // 选择新行
            table.getFocusModel().focus(newRow, userIdCol); // 聚焦新行的“用户ID”列
            table.edit(newRow, userIdCol); // 开始编辑
        });
    }


//    设置“用户管理”模块的表格列的属性显示。。。。。。
    private void setupTableColumns(TableView<User> table) {

        userIdCol = new TableColumn<>("用户ID"); // 初始化类成员变量
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        // 权限列
        roleCol = new TableColumn<>("权限");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        //下面功能是对“修改权限”中--->“权限“修改的判定操作
        roleCol.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), "管理员", "普通员工"));
        roleCol.setOnEditCommit(event -> {
            // 权限列编辑提交事件的处理逻辑
            String newValue = event.getNewValue();
            User user = event.getRowValue();
            if (newValue.equals("管理员") || newValue.equals("普通员工")) {
                user.setRole(newValue);
                MysqlConnectiontest.updateUserInformation(user);
            } else {
                showAlert("无效的权限", "请输入有效的权限：管理员、普通员工。", Alert.AlertType.ERROR);
                // 这里重新设置为旧值可能需要在Platform.runLater中调用以确保线程安全
                Platform.runLater(() -> {
                    event.getTableView().getItems().set(event.getTablePosition().getRow(), user);
                    table.refresh();
                });
            }
        });

//


        // 员工姓名列
        TableColumn<User, String> nameCol = new TableColumn<>("员工姓名");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellFactory(column -> new TextFieldTableCell<User, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                int rowIndex = this.getIndex();
                if ((isAddUserMode && rowIndex == table.getItems().size() - 1) || isPermissionEditMode) {
                    super.startEdit();
                }
            }
        });


        // 工作岗位列
        TableColumn<User, String> positionCol = new TableColumn<>("工作岗位");
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
//        positionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        positionCol.setCellFactory(column -> new TextFieldTableCell<User, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                int rowIndex = this.getIndex();
                if ((isAddUserMode && rowIndex == table.getItems().size() - 1) || isPermissionEditMode) {
                    super.startEdit();
                }
            }
        });


        // 工作职能列
        TableColumn<User, String> functionCol = new TableColumn<>("工作职能");
        functionCol.setCellValueFactory(new PropertyValueFactory<>("function"));
//        functionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        functionCol.setCellFactory(column -> new TextFieldTableCell<User, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                int rowIndex = this.getIndex();
                if ((isAddUserMode && rowIndex == table.getItems().size() - 1) || isPermissionEditMode) {
                    super.startEdit();
                }
            }
        });


        // 联系电话列
        TableColumn<User, String> phoneCol = new TableColumn<>("联系电话");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(column -> new TextFieldTableCell<User, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                int rowIndex = this.getIndex();
                if ((isAddUserMode && rowIndex == table.getItems().size() - 1) || isPermissionEditMode) {
                    super.startEdit();
                }
            }
        });



        //        修改“工作岗位”列的编辑提交事件处理逻辑
        positionCol.setOnEditCommit(event -> {
            String newPosition = event.getNewValue();
            User user = event.getRowValue();

            if ("收费员".equals(newPosition)) {
                user.setFunction("收费权");
            } else if ("监督员".equals(newPosition)) {
                user.setFunction("监督权");
            } else if ("".equals(newPosition)) {
                user.setFunction(""); // 如果输入为空或“无”，则清空工作职能
            } else if("无".equals(newPosition)){
                user.setFunction("无");
            } else {
                // 如果输入既不是“收费员”也不是“监督员”，显示错误提示
                showAlert("输入错误", "请输入有效的岗位名称（例如 '收费员'、'监督员'、'无'）或留空。", Alert.AlertType.ERROR);
                event.consume(); // 阻止事件继续进行，防止错误的值被提交
                return; // 退出事件处理程序
            }
            user.setPosition(newPosition); // 显式更新工作岗位
            MysqlConnectiontest.updateUserInformation(user); // 更新数据库
            table.refresh(); // 刷新表格
        });
//
//        “工作职位”文本框实现
        functionCol.setOnEditCommit(event -> {
            String newFunction = event.getNewValue();
            User user = event.getRowValue();

            if ("收费权".equals(newFunction)) {
                user.setPosition("收费员");
            } else if ("监督权".equals(newFunction)) {
                user.setPosition("监督员");
            } else if ("".equals(newFunction)) {
                user.setPosition("");
            } else if ("无".equals(newFunction)) {
                user.setPosition("无");
            } else {
                showAlert("输入错误", "请输入有效的工作职能（例如 '收费权'、'监督权'、'无'）或留空。", Alert.AlertType.ERROR);
                event.consume(); // 阻止事件继续进行，防止错误的值被提交
                return; // 退出事件处理程序
            }

            user.setFunction(newFunction); // 更新工作职能
            MysqlConnectiontest.updateUserInformation(user); // 更新数据库
            table.refresh(); // 刷新表格
        });


        userIdCol.setPrefWidth(100); // 设置用户ID列的首选宽度
        roleCol.setPrefWidth(150); // 设置权限列的首选宽度
        nameCol.setPrefWidth(120); // 设置员工姓名列的首选宽度
        positionCol.setPrefWidth(150); // 设置工作岗位列的首选宽度
        functionCol.setPrefWidth(150); // 设置工作职能列的首选宽度
        phoneCol.setPrefWidth(120); // 设置联系电话列的首选宽度


//对于权限编辑的具体代码实现
        // 对于权限编辑的具体代码实现
        roleCol.setCellFactory(column -> {
            return new TextFieldTableCell<User, String>(new DefaultStringConverter()) {
                private TextField textField;

                @Override
                public void startEdit() {
                    super.startEdit();
                    if (textField == null) {
                        createTextField();
                    }
                    setText(null);
                    setGraphic(textField);
                    textField.selectAll();
                }
                private void createTextField() {
                    textField = new TextField(getString());
                    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                    textField.setOnKeyPressed(event -> {
                        if (event.getCode() == KeyCode.ENTER) {
                            String text = textField.getText();
                            if (text.equals("管理员") || text.equals("普通员工")) {
                                commitEdit(text);
                                getTableView().refresh(); // 强制表格刷新
                                event.consume(); // 阻止事件进一步传播
                            } else {
                                showAlert("无效的权限", "请输入有效的权限：管理员、普通员工。", Alert.AlertType.ERROR);
                                cancelEdit();
                            }
                        } else if (event.getCode() == KeyCode.ESCAPE) {
                            textField.setText(getString());
                            cancelEdit();
                            event.consume(); // 阻止事件进一步传播1
                        }
                    });
                }


                @Override
                public void commitEdit(String newValue) {
                    if (newValue != null && (newValue.equals("管理员") || newValue.equals("普通员工"))) {
                        super.commitEdit(newValue);
                        updateItem(newValue, false); // 调用updateItem来更新单元格显示
                    } else {
                        cancelEdit(); // 如果新值不符合条件，则取消编辑
                    }
                }

                private String getString() {
                    return getItem() == null ? "" : getItem().toString();
                }
            };
        });




        table.getColumns().clear(); // 清除之前添加的所有列


        // 添加所有列到表格
        table.getColumns().addAll(userIdCol, roleCol, nameCol, positionCol, functionCol, phoneCol);
    }


    private boolean addUserToDatabase(TableView<User> table) {
        // 获取最后一行的数据
        User newUser = table.getItems().get(table.getItems().size() - 1);

        // 这里应该有数据验证的代码
        // ...

        // 将新用户的数据添加到数据库
        return MysqlConnectiontest.insertNewUser(newUser);// 将新用户的数据添加到数据库
    }


//“用户模块”按键的具体实现操作！
    private void setupButtonActions(TableView<User> table) {
//        添加按钮的实现
        addUserButton.setOnAction(e -> {
            isAddUserMode = true;
            isPermissionEditMode = false;
            currentActionState = ActionState.ADDING_USER;//复用确认和取消按键
            addUser(table);
            updateButtonVisibility(addUserButton);
            showConfirmCancelButtons(true); // 显示确认和取消按钮
//            userIdCol.setCellFactory(column -> new UserIdCell());
            // 隐藏搜索框和删除记录按钮
            searchComboBox.setVisible(false);
            searchTextField.setVisible(false);
            showDeleteRecordsButton.setVisible(false);
            table.edit(table.getItems().size() - 1, userIdCol); // 开始编辑新行的“用户ID”列
//            table.refresh();

        });

//        编辑按钮逻辑实现
        editPermissionButton.setOnAction(e -> {
//            System.out.println("修改权限功能待实现");
            // TODO: 实现修改权限的逻辑
            currentActionState = ActionState.EDITING_PERMISSION;
            isAddUserMode = false;
//            System.out.println("启动修改权限模式");
            isPermissionEditMode = true;
            // 将表格设置为可编辑
            table.setEditable(true);

            // 设置userIdCol和roleCol为可编辑状态
//            userIdCol.setEditable(true);
            roleCol.setEditable(true);

//            isPermissionEditMode = true;
//            table.setEditable(true);
            // 显示确认和取消按钮
            showConfirmCancelButtons(true);

            // 隐藏其它按钮
            addUserButton.setVisible(false);

            deleteUserButton.setVisible(false);
            // 隐藏搜索框和删除记录按钮
            searchComboBox.setVisible(false);
            searchTextField.setVisible(false);
            showDeleteRecordsButton.setVisible(false);

            // 刷新表格以应用更改
            table.refresh();
        });

//        删除按钮具体的实现
        deleteUserButton.setOnAction(e -> {
            // 添加删除用户逻辑
//            System.out.println("删除用户功能待实现");
//             TODO: 实现删除用户的逻辑
            currentActionState = ActionState.DELETING_USER;

            // 设置表格的选择模式为多选
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


            // 显示搜索和删除记录相关的控件
            searchComboBox.setVisible(true);
            searchTextField.setVisible(true);
            showDeleteRecordsButton.setVisible(true);

            // 显示确认和取消按钮
            showConfirmCancelButtons(true);


        });

        // 确认按钮的行为
        confirmButton.setOnAction(e -> {
            switch (currentActionState) {
                case ADDING_USER:
                    if (isUserDataValid(table)) {
                        User newUser = table.getItems().get(table.getItems().size() - 1);
                        try{
                            addUserToDatabaseAndRefreshTable(table, newUser);
                            // 如果没有异常发生，记录操作
                            UserOperationLogger logger = new UserOperationLogger();
                            String detail = "添加了用户：" + newUser.getUserDetailsForLogging();
                            logger.logOperation(Session.getCurrentAdminUserId(), "添加用户", detail);
                        }
                        catch (Exception a){
                            // 处理添加用户失败的情况
                            a.printStackTrace();
                            showAlert("错误", "添加用户失败：" + a.getMessage(), Alert.AlertType.ERROR);
                        }

                    } else {
                        showAlert("错误", "您输入的数据无效或不全！请仔细检查。", Alert.AlertType.ERROR);
//                         让用户继续编辑数据
                        int newRow = table.getItems().size() - 1;
                        Platform.runLater(() -> {
                            table.requestFocus();
                            table.getSelectionModel().select(newRow);
                            table.getFocusModel().focus(newRow, userIdCol); // 或者选择需要聚焦的列
                            table.edit(newRow, userIdCol); // 或者选择需要编辑的列
                        });
                    }
                    // 重置 isAddUserMode 并恢复 userIdCol 的原始 CellFactory
                    isAddUserMode = false;
                    resetUserIdColCellFactory();
                    break;
                case EDITING_PERMISSION:
                    // 执行修改权限的确认逻辑
                    // TODO: 更新被修改的用户信息到数据库
                    // 获取选中的用户
                    // 确认添加用户后重新启用权限列的编辑功能
                    roleCol.setEditable(true);

                    User selectedUser = table.getSelectionModel().getSelectedItem();
                    if (selectedUser != null && isPermissionEditMode) {


                        // 显示确认对话框
                        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("修改确认");
                        confirmationAlert.setHeaderText("确定要修改 " + selectedUser.getUserId() + " 的信息吗？");
                        confirmationAlert.setContentText("点击“确认”以保存更改，或“取消”以放弃。");

                        Optional<ButtonType> result = confirmationAlert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            // 调用 MysqlConnectiontest 类的 updateUserInformation 方法更新用户信息
                            try{
                                MysqlConnectiontest.updateUserInformation(selectedUser);
                                // 记录操作
                                UserOperationLogger logger = new UserOperationLogger();
                                String detail = "修改了用户 " + selectedUser.getUserId() + " 的权限：" + selectedUser.getRole();
                                logger.logOperation(Session.getCurrentAdminUserId(), "编辑权限", detail);
                                showAlert("修改成功", "用户信息已更新！", Alert.AlertType.INFORMATION);
                                // 重新加载表格数据以显示更新后的信息
                                table.setItems(loadDataIntoTable());
                            } catch (Exception ex) {
//                                e.printStackTrace();
                                showAlert("修改失败", "无法更新用户信息！", Alert.AlertType.ERROR);

//                                throw new RuntimeException(ex);
                            }

                        }
                    }
                    break;

                case DELETING_USER:
                    // 执行删除用户的确认逻辑
                    // 获取选中的用户
                    ObservableList<User> selectedUsers = table.getSelectionModel().getSelectedItems();

                    // 检查是否有用户被选中
                    if (selectedUsers.isEmpty()) {
                        showAlert("提示", "请先选择要删除的用户！", Alert.AlertType.INFORMATION);
                        return; // 如果没有选中用户，显示提示框然后返回
                    }

                    // 如果有用户被选中，弹出确认删除的提示框
                    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmDialog.setTitle("删除用户");
                    confirmDialog.setHeaderText("确认删除");
                    confirmDialog.setContentText("确定要删除选中的用户吗？这个操作是不可逆的。");

                    Optional<ButtonType> result = confirmDialog.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        StringBuilder deletedUsersDetails = new StringBuilder();

                        // 用户点击了“确定”按钮，执行软删除
                        boolean allDeletedSuccessfully = true;
                        for (User user : new ArrayList<>(selectedUsers)) {
                            boolean success = MysqlConnectiontest.softDeleteUser(user.getUserId());
                            if (!success) {
                                allDeletedSuccessfully = false;

                                // 处理软删除失败的情况
                                showAlert("错误", "删除用户 " + user.getUserId() + " 失败！", Alert.AlertType.ERROR);
                                break;
                            }else {
                                // 构建删除用户的详细信息
                                deletedUsersDetails.append(user.getUserDetailsForLogging()).append("; ");
                            }
                        }
                        if (allDeletedSuccessfully) {
                            // 如果所有选中的用户都删除成功，则显示成功提示
                            showAlert("成功", "选中的用户已成功删除。", Alert.AlertType.INFORMATION);
                            // 清空搜索文本框内容
                            searchTextField.clear();
                            // 记录操作
                            UserOperationLogger logger = new UserOperationLogger();
                            String detail = "删除了用户：" + deletedUsersDetails.toString();
                            logger.logOperation(Session.getCurrentAdminUserId(), "删除用户", detail);

                        }
                        // 从表格视图中移除已删除的用户
                        table.getItems().removeAll(selectedUsers);
                        table.refresh();
                    }
                    // 重置UI状态
                    resetUI();

                    // 某个事件发生后，将选择模式改回单选
                    table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


                    break;
                default:
                    // 如果没有匹配的操作状态，可能是错误或者未知状态
                    showAlert("错误", "未知操作！", Alert.AlertType.ERROR);
                    resetUI();
                    // 某个事件发生后，将选择模式改回单选
                    table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                    break;
            }
            currentActionState = ActionState.NONE;
            resetUI();
        });

        // 取消按钮的行为
        cancelButton.setOnAction(e -> {
            if (currentActionState == ActionState.ADDING_USER && previousTableData != null) {
                table.setItems(previousTableData);
            }
            // 取消添加用户操作时重置权限列为可编辑
            if (currentActionState == ActionState.ADDING_USER) {
                roleCol.setEditable(true);
            }

            // 某个事件发生后，将选择模式改回单选
            table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


            // 取消逻辑可以通用，因为它通常只是重置UI
            resetUI();
            currentActionState = ActionState.NONE;
            // 重置 isAddUserMode 并恢复 userIdCol 的原始 CellFactory
            isAddUserMode = false;
            resetUserIdColCellFactory();
        });

//        对于“删除用户”这个功能的搜索文本框监听
        searchTextField.setOnAction(event -> {
            performSearch();
        });

        searchComboBox.setOnAction(event -> {
            performSearch();
        });





    }
    private void showEditButtons(boolean showOriginalButtons) {
        addUserButton.setVisible(showOriginalButtons);
        editPermissionButton.setVisible(showOriginalButtons);
        deleteUserButton.setVisible(showOriginalButtons);
        confirmButton.setVisible(!showOriginalButtons);
        cancelButton.setVisible(!showOriginalButtons);
    }

    // 仅显示特定按钮的方法
    private void showOnlyButtons(Button... buttonsToShow) {
        // 首先隐藏所有按钮
        addUserButton.setVisible(false);
        editPermissionButton.setVisible(false);
        deleteUserButton.setVisible(false);
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

        // 然后根据传入的按钮数组显示特定的按钮
        for (Button button : buttonsToShow) {
            button.setVisible(true);
        }
    }
    // 重置按钮可见性的方法
    private void resetButtonsVisibility() {
        addUserButton.setVisible(true);
        editPermissionButton.setVisible(true);
        deleteUserButton.setVisible(true);
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);
        confirmCancelBox.setVisible(false); // 确保整个按钮组也被隐藏
    }


    // 初始化按钮的方法
    private void initializeButtons() {
        addUserButton = new Button("增加用户");
        editPermissionButton = new Button("修改权限");
        deleteUserButton = new Button("删除用户");
        confirmButton = new Button("确定");
        cancelButton = new Button("取消");
        // 初始化按钮盒子
        confirmCancelBox = new HBox(10, confirmButton, cancelButton);
        confirmCancelBox.setAlignment(Pos.CENTER);
        confirmCancelBox.setVisible(false); // 默认设置为不可见
    }

    // 切换按钮可见性的方法
    private void toggleButtonVisibility(boolean showMainButtons) {
        buttonBox.setVisible(showMainButtons);
        confirmCancelBox.setVisible(!showMainButtons);
    }

    //该方法根据当前被点击的按钮来显示或隐藏其他按钮
    private void updateButtonVisibility(Button activeButton) {
        addUserButton.setVisible(activeButton == addUserButton);
        editPermissionButton.setVisible(activeButton == editPermissionButton);
        deleteUserButton.setVisible(activeButton == deleteUserButton);
    }

    private void showConfirmCancelButtons(boolean show) {
        confirmCancelBox.setVisible(show); // 控制整个按钮组的可见性
        confirmButton.setVisible(show);
        cancelButton.setVisible(show);
    }

//---用户管理----验证用户输入的数据是否有效
    private boolean isUserDataValid(TableView<User> table) {
        for (User user : table.getItems()) {
            if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
                System.out.println("用户ID为空");
                return false;
            }
            // 对于新添加的用户，可能尚未填写其他字段，所以这里对这些字段进行宽松的检查
            // 例如，只有当字段不为 null 且为空字符串时才认为数据无效
            if (user.getName() != null && user.getName().trim().isEmpty()) {
                System.out.println("姓名为空");
                return false;
            }
            if (user.getRole() != null && user.getRole().trim().isEmpty()) {
                System.out.println("权限为空");
                return false;
            }
            // ... 对其他字段进行相似的检查
            if (user.getPhone() != null && user.getPhone().trim().isEmpty()) {
                System.out.println("用户联系电话为空");
                return false;
            }
            // ... 对其他字段进行相似的检查
        }
        return true; // 所有字段均符合要求
    }

//---用户模块----添加用户到数据库并刷新表格的方法
    private void addUserToDatabaseAndRefreshTable(TableView<User> table, User newUser) {
        if (MysqlConnectiontest.insertNewUser(newUser)) {
            showAlert("添加用户", "添加 " + newUser.getUserId() + " 用户成功！", Alert.AlertType.INFORMATION);
            table.setItems(loadDataIntoTable()); // 重新加载并显示更新后的数据
            resetUI(); // 如果需要重置UI到初始状态
        } else {
            showAlert("错误", "添加用户失败！", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // 在这里实现重置UI到初始状态的逻辑，比如隐藏/显示某些按钮，清空输入字段等
    private void resetUI() {
        // 重置表格数据
        if (table != null) {
            reloadData();
//            table.refresh();
        }
        resetButtonsVisibility();
        showConfirmCancelButtons(false); // 隐藏确认和取消按钮
        // 设置表格为不可编辑
        if (table != null) {
            table.setEditable(false);
        }

        // 隐藏搜索框和删除记录按钮
        searchComboBox.setVisible(false);
        searchTextField.setVisible(false);
        showDeleteRecordsButton.setVisible(false);
        table.refresh();
        // 重置按钮的可见性
        confirmCancelBox.setVisible(false);
        // 重新加载表格数据

    }


    private TableCell<User, String> createEditableCell() {
        return new TextFieldTableCell<>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                if (isPermissionEditMode && !getTableColumn().getText().equals("权限")) {
                    super.startEdit();
                }
            }

//          对于“用户模块”----“修改权限”--功能的实现
            @Override
            public void commitEdit(String newValue) {
                User user = getTableRow().getItem();
                if (user != null && isUserChangeValid(user, newValue)) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("修改确认");
                    confirmationAlert.setHeaderText("你确定修改 " + user.getUserId() + " 的信息吗？");
                    confirmationAlert.setContentText("点击“确认”以保存更改，或“取消”以放弃。");

                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        super.commitEdit(newValue);
//                         TODO: 更新数据库
                        MysqlConnectiontest.updateUserInformation(user); // 更新数据库

                    } else {
                        cancelEdit();
                    }
                } else {
                    cancelEdit();
                }
            }

            private boolean isUserChangeValid(User user, String newValue) {
                // 这里可以添加验证逻辑，例如检查值是否重复等
                return true;
            }
        };
    }


    private boolean isUserChangeValid(User user, String newValue) {
        // 在这里编写验证逻辑，检查 newValue 是否有效
        return newValue.equals("管理员") || newValue.equals("普通员工");
    }

    // 此方法用于重新加载表格数据并在UI线程上刷新表格
    private void refreshTableData() {
        Platform.runLater(() -> {
            // 查询数据库中的数据
            ObservableList<User> updatedData = MysqlConnectiontest.loadDeletedUsersIntoTable();

            // 更新表格的数据
            table.setItems(updatedData);

            // 刷新表格
            table.refresh();
        });
    }


//    对于删除功能的界面设计以及布局控件
    private void initializeSearchAndDeleteRecordUI() {
        searchComboBox = new ComboBox<>();
        searchComboBox.getItems().addAll("用户ID", "员工姓名");
        searchComboBox.getSelectionModel().selectFirst(); // 默认选择第一个选项

        searchTextField = new TextField();
        searchTextField.setPromptText("输入搜索内容");
        searchTextField.setMaxWidth(200);

        showDeleteRecordsButton = new Button("删除用户记录");
        showDeleteRecordsButton.setOnAction(e -> {
            // 显示删除用户的记录
            showDeleteRecords();
        });
    }

    public UserManagementModule() {
        // 在构造函数或初始化方法中创建子视图的窗口和表格视图
        deleteRecordsStage = new Stage();
        deletedRecordsTableView = new TableView<>();
        // 设置 MysqlConnectiontest 类的静态引用
        MysqlConnectiontest.setUserManagementModuleRef(this);

    }

//    这里显示的是窗体“删除记录用户”
    private void showDeleteRecords() {
        // 1. 判断数据库表中的软删除标记字段是否为 1
        boolean softDeletedRecordsExist = checkSoftDeletedRecords(); // 编写此方法来检查软删除的记录是否存在
        // 获取当前上一级窗口
        Window parentWindow = searchComboBox.getScene().getWindow();

        // 隐藏上一级窗口
        parentWindow.hide();

        if (softDeletedRecordsExist) {
            // 2. 如果软删除记录存在，则创建子视图表格
            Stage stage = new Stage();
            stage.setTitle("已删除的用户记录");

            TableView<User> deletedRecordsTableView = new TableView<>();
            deletedRecordsTableView.setEditable(false);

            // 创建表格列，例如 "用户ID", "权限", "员工姓名", "工作岗位", "工作职能" 等
            // 创建表格列
            TableColumn<User, String> userIdColumn = new TableColumn<>("用户ID");
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

            TableColumn<User, String> nameColumn = new TableColumn<>("员工姓名");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<User, String> roleColumn = new TableColumn<>("权限");
            roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

            TableColumn<User, String> positionColumn = new TableColumn<>("工作岗位");
            positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

            TableColumn<User, String> phoneColumn = new TableColumn<>("联系电话");
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

            TableColumn<User, String> functionColumn = new TableColumn<>("工作职能");
            functionColumn.setCellValueFactory(new PropertyValueFactory<>("function"));
            TableColumn<User, Void> permanentlyDeleteColumn = new TableColumn<>("彻底删除");
            TableColumn<User, Void> recoverColumn = new TableColumn<>("恢复用户");

            // 将列添加到表格视图
//            deletedRecordsTableView.getColumns().addAll(userIdColumn, nameColumn, roleColumn, positionColumn, phoneColumn, functionColumn);

//            添加彻底删除按钮的实现！

            permanentlyDeleteColumn.setCellFactory(param -> new TableCell<User, Void>() {
                private final Button deleteButton = new Button("删除");
                {
                    deleteButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());

                        // 创建确认对话框
                        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmDialog.setTitle("确认删除");
                        confirmDialog.setHeaderText(null);
                        confirmDialog.setContentText("此删除操作不可逆转，你确定删除 " + user.getUserId() + " 用户吗？");

                        // 显示对话框并等待用户响应
                        Optional<ButtonType> result = confirmDialog.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            // 用户确认删除
                            if (MysqlConnectiontest.permanentlyDeleteUser(user.getUserId())) {
                                // 删除成功，记录操作
                                UserOperationLogger logger = new UserOperationLogger();
                                String detail = "彻底删除了用户：" + user.getUserDetailsForLogging();
                                logger.logOperation(Session.getCurrentAdminUserId(), "彻底删除用户", detail);
                                // 删除成功
                                getTableView().getItems().remove(getIndex());
//                                System.out.println("User " + user.getUserId() + " permanently deleted.");
                                // 显示成功提示框
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("删除成功");
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("用户 " + user.getUserId() + " 成功删除！");
                                successAlert.showAndWait();
                            } else {
                                return ;
                            }
                        }else {
                            return ;
                        }
                    });

                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            });


            // 添加 "恢复用户" 按钮列

            recoverColumn.setCellFactory(param -> new TableCell<>() {
                private final Button recoverButton = new Button("恢复");

                {
                    recoverButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmDialog.setTitle("确认恢复");
                        confirmDialog.setContentText("你确定要恢复用户ID为 " + user.getUserId() + " 的账号吗？");

                        Optional<ButtonType> result = confirmDialog.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            if (MysqlConnectiontest.restoreUser(user.getUserId())) {
                                // 恢复成功，记录操作
                                UserOperationLogger logger = new UserOperationLogger();
                                String detail = "恢复了用户：" + user.getUserDetailsForLogging();
                                logger.logOperation(Session.getCurrentAdminUserId(), "恢复用户", detail);
//                                System.out.println("数据库中的用户已恢复。");
                                showAlertRecover("恢复成功", "用户账号 " + user.getUserId() + " 恢复成功", Alert.AlertType.INFORMATION);
//                                refreshTableData();
//                                refreshDeletedRecordsTable();
//                                refreshCurrentViewTable(); // 刷新视图表数据
                                // 清除视图表中的所有数据
                                deletedRecordsTableView.getItems().clear();
                                // 重新加载数据
                                ObservableList<User> updatedData = querySoftDeletedUsers();
                                deletedRecordsTableView.setItems(updatedData);

                            } else {
                                System.out.println("数据库恢复操作失败。");
                                showAlertRecover("恢复失败", "无法恢复用户 " + user.getUserId(), Alert.AlertType.ERROR);
                            }
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(recoverButton);
                    }
                }
            });

            // 查询已删除的记录并将其添加到表格中，编写此方法来查询和填充数据
            List<User> softDeletedUsers = querySoftDeletedUsers(); // 编写此方法来查询已删除的用户记录
            deletedRecordsTableView.getItems().addAll(softDeletedUsers);

            // 创建 "返回上一级" 按钮
            Button backButton = new Button("<--返回上一级");
            backButton.setMinHeight(50); // 增加按钮的高度
            backButton.setMinWidth(200); // 增加按钮的宽度
            backButton.setOnAction(event -> {
                // 关闭子视图窗口
                stage.close();
                // 重新打开用户管理界面
                // 检查parentStage是否不为null
                if (this.parentStage != null) {
                    showUserManagementUI(this.parentStage); // 重新打开用户管理界面
                } else {
                    // 处理parentStage为null的情况（例如，显示错误消息或日志）
                }
            });

            deletedRecordsTableView.getColumns().clear(); // 清除现有列
            // 将列添加到表格视图
            deletedRecordsTableView.getColumns().addAll(userIdColumn, nameColumn, roleColumn, positionColumn, phoneColumn, functionColumn, recoverColumn,permanentlyDeleteColumn);

            // 创建一个水平布局来放置按钮
            HBox buttonBox = new HBox(10); // 10 是按钮之间的间距
            buttonBox.getChildren().addAll(backButton);
            buttonBox.setAlignment(Pos.BOTTOM_LEFT); // 设置按钮在左下角位置
            // 创建一个垂直布局，将表格和按钮放在同一个视图中
            VBox mainLayout = new VBox(10); // 10 是表格和按钮之间的间距
            mainLayout.getChildren().addAll(deletedRecordsTableView, buttonBox);

            // 设置主视图
            Scene scene = new Scene(mainLayout, 800, 600);
            stage.setScene(scene);
            stage.show();
        } else {
            showAlertAndReturn("提示", "没有已删除的用户记录", Alert.AlertType.INFORMATION, parentWindow);
            return; // 退出方法
        }
    }

    private void showAlertAndReturn(String title, String content, Alert.AlertType alertType, Window parentWindow) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

        // 显示上一级窗口
        if (parentWindow instanceof Stage) {
            ((Stage) parentWindow).show();
        }
    }



    private void reloadData() {
        // 使用后台线程来加载数据
        new Thread(() -> {
            ObservableList<User> data = MysqlConnectiontest.loadDataIntoTable();
            // 现在我们在JavaFX主线程上更新UI
            Platform.runLater(() -> {
                if (table != null) {
                    table.setItems(data);
                    table.refresh();
                }
            });
        }).start();
    }

//    实现“删除用户”的搜索逻辑
    private void performSearch() {
        String searchType = searchComboBox.getValue();
        String searchText = searchTextField.getText();

        if (searchText == null || searchText.trim().isEmpty()) {
            showAlert("搜索内容不能为空");
            return; // 如果搜索文本为空，则不执行任何操作
        }
        boolean found = false;

        for (User user : table.getItems()) {
            if ("用户ID".equals(searchType) && user.getUserId().equals(searchText)) {
                focusOnTableRow(user);
                found = true;
                break;
            } else if ("员工姓名".equals(searchType) && user.getName().equals(searchText)) {
                focusOnTableRow(user);
                found = true;
                break;
            }
        }
        if (!found) {
            showAlert("抱歉，你所输入的" + searchType + "不存在，请重新输入！");
        }

    }



    private void focusOnTableRow(User user) {
        int rowIndex = table.getItems().indexOf(user);
        table.requestFocus();
        table.getSelectionModel().select(rowIndex);
        table.getFocusModel().focus(rowIndex);
        table.scrollTo(rowIndex); // 如果表格很长，这将确保所选行可见
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


//    下面三个用户恢复用户的方法
    private boolean checkSoftDeletedRecords() {
        MysqlConnectiontest.ConnectionInfo connectionInfo = mysqlConnectiontest.getConnectionInfo();

        try (
                Connection connection = DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(), connectionInfo.getPassword());
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM 系统用户表 WHERE 软删除标记字段 = 1");
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            // 检查是否有结果
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                // 如果计数大于0，则存在软删除的记录
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 如果没有软删除的记录，返回 false
        return false;
    }




    private ObservableList<User> querySoftDeletedUsers() {
        List<User> users = new ArrayList<>();
        MysqlConnectiontest.ConnectionInfo connectionInfo = mysqlConnectiontest.getConnectionInfo();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 创建数据库连接
            connection = DriverManager.getConnection(connectionInfo.getUrl(), connectionInfo.getUsername(), connectionInfo.getPassword());

            // 编写 SQL 查询语句
            String query = "SELECT * FROM 系统用户表 WHERE 软删除标记字段 = 1";

            // 创建 PreparedStatement 对象并执行查询
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            // 遍历结果集
            while (resultSet.next()) {
                String userId = resultSet.getString("用户ID"); // 假设列名是"用户ID"
                String name = resultSet.getString("员工姓名");   // 假设列名是"员工姓名"
                String role = resultSet.getString("权限");     // 假设列名是"权限"
                String position = resultSet.getString("工作岗位"); // 假设列名是"工作岗位"
                String phone = resultSet.getString("联系电话");   // 假设列名是"联系电话"
                String function = resultSet.getString("工作职能"); // 假设列名是"工作职能"

                // 使用全参数构造函数创建User对象
                User user = new User(userId, name, role, position, function, phone);

                // 将User对象添加到列表中
                users.add(user);
            }

        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭数据库连接和资源
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//        return users;
        return FXCollections.observableArrayList(users);
    }



    private void recoverUser(String userId) {
        // 编写此方法来执行恢复用户操作
        // 根据 userId 执行相应的操作来将用户从软删除状态恢复
    }

//    用于"恢复”用户按钮提示语
    private void showAlertRecover(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void refreshDeletedRecordsTable() {
        Platform.runLater(() -> {
            // 查询数据库中已删除用户的新数据集
            ObservableList<User> updatedData = querySoftDeletedUsers();

            // 更新已删除用户表格的数据
            deletedRecordsTableView.setItems(updatedData);

            // 刷新已删除用户表格
            deletedRecordsTableView.refresh();
        });
    }


    // 公共方法供外部调用以刷新表格数据
    public void refreshTables() {
//        refreshTableData();//这里面是软！=1
        refreshDeletedRecordsTable();//这里是软=1
    }

//用于刷新“已删除用户记录”的视图表
    private void refreshCurrentViewTable() {
        ObservableList<User> updatedData = FXCollections.observableArrayList();


        // 从数据库中获取数据
        try (Connection connection = DriverManager.getConnection(
                MysqlConnectiontest.getStaticConnectionInfo1().getUrl(),
                MysqlConnectiontest.getStaticConnectionInfo1().getUsername(),
                MysqlConnectiontest.getStaticConnectionInfo1().getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM 系统用户表 WHERE 软删除标记字段 = 1");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String userId = resultSet.getString("用户ID");
                String name = resultSet.getString("员工姓名");
                String role = resultSet.getString("权限");
                String position = resultSet.getString("工作岗位");
                String phone = resultSet.getString("联系电话");
                String function = resultSet.getString("工作职能");

                User user = new User(userId, name, role, position, function, phone);
                updatedData.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 更新表格数据
        Platform.runLater(() -> {
            deletedRecordsTableView.getItems().clear(); // 清除当前视图表中的所有数据

            deletedRecordsTableView.setItems(updatedData);
            deletedRecordsTableView.refresh();
        });
    }

    public void clearTables() {

    }

//用于"管理员修改用户密码)
    private void showChangePasswordUI() {
        // 检查是否已经有一个修改密码的窗口打开
        if (changePasswordStage != null && changePasswordStage.isShowing()) {
            changePasswordStage.toFront(); // 如果已经打开，就把它带到前面
            return;
        }

        changePasswordStage = new Stage();
        changePasswordStage.setTitle("修改用户密码");

        // 创建布局和控件
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // 用户ID
        Label userIdLabel = new Label("用户ID：");
        TextField userIdTextField = new TextField();
        gridPane.add(userIdLabel, 0, 0);
        gridPane.add(userIdTextField, 1, 0);

        // 新密码
        Label newPasswordLabel = new Label("请输入新密码：");
        PasswordField newPasswordField = new PasswordField();
        gridPane.add(newPasswordLabel, 0, 1);
        gridPane.add(newPasswordField, 1, 1);

        // 确认新密码
        Label confirmNewPasswordLabel = new Label("确认新密码：");
        PasswordField confirmNewPasswordField = new PasswordField();
        gridPane.add(confirmNewPasswordLabel, 0, 2);
        gridPane.add(confirmNewPasswordField, 1, 2);

        // 确定和取消按钮
        Button confirmButton = new Button("确定");
        Button cancelButton = new Button("取消");
        HBox buttonsHBox = new HBox(10, confirmButton, cancelButton);
        buttonsHBox.setAlignment(Pos.CENTER);
        gridPane.add(buttonsHBox, 0, 3, 2, 1);

        // 设置按钮事件
        confirmButton.setOnAction(e -> {
            handleChangePassword(userIdTextField.getText(), newPasswordField.getText(), confirmNewPasswordField.getText(), changePasswordStage);
            // 检查密码是否成功修改
            boolean isPasswordChanged = MysqlConnectiontest.updatePassword_changeothers(userIdTextField.getText(), newPasswordField.getText());
            if (isPasswordChanged) {
                // 密码修改成功，记录操作
                UserOperationLogger logger = new UserOperationLogger();
                String detail = "修改了用户 " + userIdTextField.getText() + " 的密码。";
                logger.logOperation(Session.getCurrentAdminUserId(), "修改密码", detail);
            }
        });
        cancelButton.setOnAction(e -> changePasswordStage.close());

        // 将gridPane放到场景中并展示
        Scene scene = new Scene(gridPane);
        changePasswordStage.setScene(scene);
        changePasswordStage.show();

        // 当窗口关闭时清除引用，允许再次打开
        changePasswordStage.setOnCloseRequest(e -> changePasswordStage = null);
    }


    //    管理员处理修改其他用户密码。
    private void handleChangePassword(String userId, String newPassword, String confirmNewPassword, Stage stage) {
        if (!newPassword.equals(confirmNewPassword)) {
            showAlertchange_otherusers_password("密码不一致", "新密码和确认密码不匹配！");
            return;
        }

        boolean result = MysqlConnectiontest.updatePassword_changeothers(userId, newPassword);
        if (result) {
            showAlertchange_otherusers_password("修改成功",  userId + "密码修改成功！");
            stage.close();
        } else {
            showAlertchange_otherusers_password("修改失败", "密码修改失败，请检查用户ID是否正确！");
        }
    }

    private void showAlertchange_otherusers_password(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void resetUserIdColCellFactory() {
        userIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    }















}

