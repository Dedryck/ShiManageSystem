package com.zqu;

import com.zqu.AdministratorModule.Boat;
import com.zqu.InspectionCertificateModule.Vessel;
import com.zqu.NationalityManningCertificateModule.ShipNationalityManningCertificate;
import com.zqu.SecurityCertificateModule.ShipSecurityCertificate;
import com.zqu.ShipOperationCertificate.VesselOperationCertificate;
import com.zqu.ShipPayMonthlyWaterFreight.ShipPayMonthlyWater;
import com.zqu.System_management_module.User;
import com.zqu.System_management_module.UserManagementModule;
import com.zqu.UserQueryModule.CertificateInfo;
import com.zqu.UserQueryModule.EffectiveCertificateInfo;
import com.zqu.UserQueryModule.ShipPaymentInfo;
import com.zqu.UserQueryModule.ShipSingleInfo;
import com.zqu.WaterwayDuesPaymentModule.WaterwayDuesPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


/**
 * @aurhor Dedryck
 * @create 2023-11-19-8:59
 * @description:MysqlConnection
 */
public class MysqlConnectiontest {
    private static final String url = "jdbc:mysql://localhost:3306/船只资料数据库?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String username = "root";
    private static final String password = "root";
    private static UserManagementModule userManagementModuleRef;


    // 用户验证方法
    public static boolean authenticateUser(String inputUsername, String inputPassword) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM 系统用户表 WHERE 用户ID = ? AND 密码 = ? AND (软删除标记字段 != 1 OR 软删除标记字段 IS NULL)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, inputUsername);
                preparedStatement.setString(2, encryptPassword(inputPassword)); // 对输入密码进行加密

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // true if user exists and password is correct
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false; // Login failed due to an exception
        }
    }

    //    下面添加新的类.you
    public static UserInfo getUserInfo(String inputUsername) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT 用户ID, 员工姓名, 权限 FROM 系统用户表 WHERE 用户ID = ? AND (软删除标记字段 != 1 OR 软删除标记字段 IS NULL)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, inputUsername);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String userID = resultSet.getString("用户ID");
                        String name = resultSet.getString("员工姓名");
                        String role = resultSet.getString("权限");
                        return new UserInfo(userID, name, role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //修改代码操作：.you
    public static boolean updatePassword(String userID, String oldPassword, String newPassword) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // 首先验证旧密码是否正确
            String query = "SELECT * FROM 系统用户表 WHERE 用户ID = ? AND 密码 = ? AND (软删除标记字段 != 1 OR 软删除标记字段 IS NULL)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userID);
                preparedStatement.setString(2, encryptPassword(oldPassword));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        return false; // 旧密码不正确
                    }
                }
            }

            // 旧密码验证通过，更新密码
            String updateQuery = "UPDATE 系统用户表 SET 密码 = ? WHERE 用户ID = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, encryptPassword(newPassword));
                updateStatement.setString(2, userID);

                int affectedRows = updateStatement.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    //密码加密方法.you
    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }




    //字节转换为十六进制字符串的辅助方法。you
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    //   注册登录按钮具体实现
    //   注册登录按钮具体实现！！！！！！！！

    // 注册新用户的方法。you
    public static boolean registerNewUser(String userId, String password, String name, String phone, String role) {
        try (Connection connection = DriverManager.getConnection(url, username, MysqlConnectiontest.password)) {
            // 检查用户ID是否已存在
            if (isUserIdExists(userId, connection)) {
                System.out.println("用户ID已存在");
                return false; // 返回 false 表示注册失败
            }

            // 插入用户信息
            insertUserInfo(userId, password, name, phone, role, connection);
            System.out.println("注册成功");
            return true; // 返回 true 表示注册成功
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false; // 发生异常时返回 false
        }
    }



    // 检查用户ID是否已存在.you
    private static boolean isUserIdExists(String userId, Connection connection) throws SQLException {
        String checkUserSql = "SELECT * FROM 系统用户表 WHERE 用户ID = ?";
        try (PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql)) {
            checkUserStmt.setString(1, userId);
            try (ResultSet resultSet = checkUserStmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }
//在用户模块---添加用户-->用于检测是否存在用户的一个封装类
    public static boolean checkUserIdExists(String userId) {
        try (Connection connection = getConnection()) {
            return isUserIdExists(userId, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 或者根据您的错误处理策略进行处理
        }
    }


//    插入用户信息类
// 更新 insertUserInfo 方法以去除邀请码相关逻辑.you
    private static void insertUserInfo(String userId, String password, String name, String phone, String role, Connection connection) throws SQLException, NoSuchAlgorithmException {
        String insertUserSql = "INSERT INTO 系统用户表 (用户ID, 密码, 员工姓名, 联系电话, 权限) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement userStatement = connection.prepareStatement(insertUserSql)) {
            userStatement.setString(1, userId);
            userStatement.setString(2, encryptPassword(password));
            userStatement.setString(3, name);
            userStatement.setString(4, phone);
            userStatement.setString(5, role);
            userStatement.executeUpdate();
            System.out.println("注册成功");
        }
    }

    // 新增方法用于获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


    public static ObservableList<User> loadDataIntoTable() {
        String sql = "SELECT 用户ID, 权限, 员工姓名, 工作岗位, 工作职能, 联系电话 FROM 系统用户表 WHERE 软删除标记字段 != 1 OR 软删除标记字段 IS NULL";
        ObservableList<User> data = FXCollections.observableArrayList();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                data.add(new User(
                        rs.getString("用户ID"),
                        rs.getString("员工姓名"),
                        rs.getString("权限"),
                        rs.getString("工作岗位"),
                        rs.getString("工作职能"),
                        rs.getString("联系电话")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 可以选择在这里处理错误，例如显示错误消息
            System.out.println("数据库检索错误: " + e.getMessage()); // 打印错误信息
        }

        return data;
    }

//    对于在用户管理里面添加用户的方法
    public static boolean insertNewUser(User newUser) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO 系统用户表 (用户ID, 员工姓名, 权限, 联系电话, 工作岗位, 工作职能, 密码) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, newUser.getUserId());
            pstmt.setString(2, newUser.getName());
            pstmt.setString(3, newUser.getRole());
            pstmt.setString(4, newUser.getPhone());
            pstmt.setString(5, newUser.getPosition());
            pstmt.setString(6, newUser.getFunction());
            pstmt.setString(7, encryptPassword("1")); // 这里设置一个默认密码

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查表格中是否存在相同的用户ID
    public static boolean isUserIdExistInTable(String userId) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM 系统用户表 WHERE 用户ID = ?")) {

            pstmt.setString(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // 如果存在相同的用户ID，返回true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 如果发生异常或未找到相同的用户ID，返回false
    }

    // MysqlConnectiontest 类中

    // “修改权限”更新用户信息的方法
    public static boolean updateUserInformation(User user) {
        String sql = "UPDATE 系统用户表 SET 权限 = ?, 员工姓名 = ?, 联系电话 = ?, 工作岗位 = ?, 工作职能 = ? WHERE 用户ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getRole());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getPosition());
            pstmt.setString(5, user.getFunction());
            pstmt.setString(6, user.getUserId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //添加软删除方法
    public static boolean softDeleteUser(String userId) {
        // 这里我们假设软删除标记字段类型为TINYINT，1代表true，0代表false
        String sql = "UPDATE 系统用户表 SET 软删除标记字段 = ?, 删除时间 = NOW() WHERE 用户ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 第一个问号是软删除标记字段
            pstmt.setInt(1, 1); // 设置软删除标记为1
            // 第二个问号是用户ID
            pstmt.setString(2, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //恢复软删除的方法
    public static boolean restoreUser(String userId) {
        String sql = "UPDATE 系统用户表 SET 软删除标记字段 = NULL, 删除时间 = NULL WHERE 用户ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // 检查 userManagementModuleRef 是否不为 null
                if (userManagementModuleRef != null) {
                    userManagementModuleRef.clearTables(); // 先清除表格数据

                    userManagementModuleRef.refreshTables(); // 调用刷新表格的方法
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // 新增一个静态方法来获取连接信息
    public static ConnectionInfo getStaticConnectionInfo1() {
        return new ConnectionInfo(url, username, password);
    }

//    专门用于修改其他用户的密码
    public static boolean updatePassword_changeothers(String userId, String newPassword) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // 更新密码
            String updateQuery = "UPDATE 系统用户表 SET 密码 = ? WHERE 用户ID = ? AND (软删除标记字段 != 1 OR 软删除标记字段 IS NULL)";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, encryptPassword(newPassword));
                updateStatement.setString(2, userId);

                int affectedRows = updateStatement.executeUpdate();
                return affectedRows > 0; // 如果影响的行数大于0，则表示更新成功
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false; // 发生异常或更新失败时返回false
    }


    public ConnectionInfo getConnectionInfo() {
        return new ConnectionInfo(url, username, password);
    }
    public static class ConnectionInfo {
        private final String url;
        private final String username;
        private final String password;

        public ConnectionInfo(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

//用于删除超过30天的记录
    public static boolean removeOldSoftDeletedRecords() {
        String sql = "DELETE FROM 系统用户表 WHERE 软删除标记字段 = ? AND TIMESTAMPDIFF(DAY, 删除时间, NOW()) > 30";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 1); // 设置软删除标记为1

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    //实现永久删除用户
    public static boolean permanentlyDeleteUser(String userId) {
        String sql = "DELETE FROM 系统用户表 WHERE 用户ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setUserManagementModuleRef(UserManagementModule ref) {
        userManagementModuleRef = ref;
    }

    // 重新加载已删除用户到“已删除用户的记录”中的方法
    public static ObservableList<User> loadDeletedUsersIntoTable() {
        ObservableList<User> deletedUsers = FXCollections.observableArrayList();
        String sql = "SELECT 用户ID, 员工姓名, 权限, 工作岗位, 联系电话, 工作职能 FROM 系统用户表 WHERE 软删除标记字段 = 1";

        try (Connection conn = getConnection(); // 假设 getConnection() 是您用于获取数据库连接的方法
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userId = rs.getString("用户ID");
                String name = rs.getString("员工姓名");
                String role = rs.getString("权限");
                String position = rs.getString("工作岗位");
                String phone = rs.getString("联系电话");
                String function = rs.getString("工作职能");

                // 创建User对象并添加到列表中
                User user = new User(userId, name, role, position, function, phone);
                deletedUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedUsers;
    }

//    加载“用户具体操作”数据用法
    public static void loadOperationRecords(TableView<OperationRecord> tableView) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(MysqlConnectiontest.url, MysqlConnectiontest.username, MysqlConnectiontest.password);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM 用户操作记录表";
            rs = stmt.executeQuery(sql);

            ObservableList<OperationRecord> data = FXCollections.observableArrayList();
            while (rs.next()) {
//                UserOperationQuery.OperationRecord record = new UserOperationQuery.OperationRecord();
                OperationRecord record = new OperationRecord();
                record.setOperationId(rs.getString("操作记录ID"));
                record.setUserId(rs.getString("用户ID"));
                record.setOperationType(rs.getString("操作类型"));
                record.setOperationTime(rs.getString("操作时间"));
                record.setOperationDetail(rs.getString("操作细节"));
                data.add(record);
            }
            tableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            // 错误处理
        }
    }


    public static boolean insertBoatData(
            String ownerName, String ownerId, String ownerPhone, String ownerAddress,
            String boatName, String boatType, String boatMaterial, String boatEngineType,
            String boatPortOfRegistry, String boatBuilder, String boatRegistrationNo,
            String boatOperationCertificateNo, LocalDate entryDate, LocalDate exitDate,
            LocalDate constructionDate, Double length, Double width, Double depth,
            Double grossTonnage, Double netTonnage, Double enginePower,
            Double carryingCapacity, String sailingArea, String remarks, byte[] photo, String boatInspectionRegNo) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO 船只基本资料表 (船主姓名, 身份证号, 联系电话, 详细住址, 船名, 船舶类型, 船体材料, 机型, 船籍港, 建造厂, 船舶登记号, 营运证号, 入户时间, 迁出时间, 建成时间, 总长, 型宽, 型深, 总吨, 净吨, 主机功率, 载重吨, 航行区域, 备注, 船舶相片, 船检登记号) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ownerName);
                pstmt.setString(2, ownerId);
                pstmt.setString(3, ownerPhone);
                pstmt.setString(4, ownerAddress);
                pstmt.setString(5, boatName);
                pstmt.setString(6, boatType);
                pstmt.setString(7, boatMaterial);
                pstmt.setString(8, boatEngineType);
                pstmt.setString(9, boatPortOfRegistry);
                pstmt.setString(10, boatBuilder);
                pstmt.setString(11, boatRegistrationNo);
                pstmt.setString(12, boatOperationCertificateNo);
                pstmt.setDate(13, Date.valueOf(entryDate));
                pstmt.setDate(14, Date.valueOf(exitDate));
                pstmt.setDate(15, Date.valueOf(constructionDate));
                pstmt.setDouble(16, length);
                pstmt.setDouble(17, width);
                pstmt.setDouble(18, depth);
                pstmt.setDouble(19, grossTonnage);
                pstmt.setDouble(20, netTonnage);
                pstmt.setDouble(21, enginePower);
                pstmt.setDouble(22, carryingCapacity);
                pstmt.setString(23, sailingArea);
                pstmt.setString(24, remarks);
                pstmt.setBytes(25, photo);
                pstmt.setString(26, boatInspectionRegNo); // 新增参数设置
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//      调取“船只基本资料表”的数据
    public static List<Boat> readBoatData() {
        List<Boat> boats = new ArrayList<>();
        String sql = "SELECT * FROM 船只基本资料表";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Boat boat = new Boat();
                boat.setId(rs.getInt("序号"));
                boat.setOwnerName(rs.getString("船主姓名"));
                boat.setIdentityCard(rs.getString("身份证号"));
                boat.setContactNumber(rs.getString("联系电话"));
                boat.setAddress(rs.getString("详细住址"));
                boat.setBoatName(rs.getString("船名"));
                boat.setBoatType(rs.getString("船舶类型"));
                boat.setHullMaterial(rs.getString("船体材料"));
                boat.setEngineType(rs.getString("机型"));
                boat.setPortOfRegistry(rs.getString("船籍港"));
                boat.setBuilder(rs.getString("建造厂"));
                boat.setRegistrationNumber(rs.getString("船舶登记号"));
                boat.setOperationCertificateNumber(rs.getString("营运证号"));
                boat.setEntryDate(rs.getDate("入户时间"));
                boat.setExitDate(rs.getDate("迁出时间"));
                boat.setCompletionDate(rs.getDate("建成时间"));
                boat.setLength(rs.getBigDecimal("总长"));
                boat.setWidth(rs.getBigDecimal("型宽"));
                boat.setDepth(rs.getBigDecimal("型深"));
                boat.setGrossTonnage(rs.getBigDecimal("总吨"));
                boat.setNetTonnage(rs.getBigDecimal("净吨"));
                boat.setMainEnginePower(rs.getBigDecimal("主机功率"));
                boat.setCarryingCapacity(rs.getBigDecimal("载重吨"));
                boat.setNavigationArea(rs.getString("航行区域"));
                boat.setNotes(rs.getString("备注"));
                boat.setPhoto(rs.getBlob("船舶相片"));
                boat.setRegistrationInspectionNumber(rs.getString("船检登记号"));

                boats.add(boat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常
        }
        return boats;
    }


//    用于搜索“船只基本资料表”内数据的方法，同时也实现了模糊查询
    public static List<Boat> searchBoatData(String criteria, String searchText) {
        String sql = "SELECT * FROM 船只基本资料表 WHERE ";
        switch (criteria) {
            case "船名":
                sql += "船名 LIKE ?";
                break;
            case "船检登记号":
                sql += "船检登记号 LIKE ?";
                break;
            case "船舶类型":
                sql += "船舶类型 LIKE ?";
                break;
            case "船舶营运证号":
                sql += "营运证号 LIKE ?";
                break;
            case "船主姓名":
                sql += "船主姓名 LIKE ?";
                break;
            default:
                return new ArrayList<>(); // 如果没有有效的搜索标准，则返回空列表
        }

        List<Boat> boats = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchText + "%"); // 设置查询参数，实现模糊搜索

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Boat boat = new Boat();
                    boat.setId(rs.getInt("序号"));
                    boat.setOwnerName(rs.getString("船主姓名"));
                    boat.setIdentityCard(rs.getString("身份证号"));
                    boat.setContactNumber(rs.getString("联系电话"));
                    boat.setAddress(rs.getString("详细住址"));
                    boat.setBoatName(rs.getString("船名"));
                    boat.setBoatType(rs.getString("船舶类型"));
                    boat.setHullMaterial(rs.getString("船体材料"));
                    boat.setEngineType(rs.getString("机型"));
                    boat.setPortOfRegistry(rs.getString("船籍港"));
                    boat.setBuilder(rs.getString("建造厂"));
                    boat.setRegistrationNumber(rs.getString("船舶登记号"));
                    boat.setOperationCertificateNumber(rs.getString("营运证号"));
                    boat.setEntryDate(rs.getDate("入户时间"));
                    boat.setExitDate(rs.getDate("迁出时间"));
                    boat.setCompletionDate(rs.getDate("建成时间"));
                    boat.setLength(rs.getBigDecimal("总长"));
                    boat.setWidth(rs.getBigDecimal("型宽"));
                    boat.setDepth(rs.getBigDecimal("型深"));
                    boat.setGrossTonnage(rs.getBigDecimal("总吨"));
                    boat.setNetTonnage(rs.getBigDecimal("净吨"));
                    boat.setMainEnginePower(rs.getBigDecimal("主机功率"));
                    boat.setCarryingCapacity(rs.getBigDecimal("载重吨"));
                    boat.setNavigationArea(rs.getString("航行区域"));
                    boat.setNotes(rs.getString("备注"));
                    boat.setPhoto(rs.getBlob("船舶相片"));
                    boat.setRegistrationInspectionNumber(rs.getString("船检登记号"));
                    boats.add(boat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常
        }
        return boats;
    }


    // 将 java.util.Date 转换为 java.time.LocalDate
    private static LocalDate convertToLocalDateFromUtilDate(java.util.Date date) {
        return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }
    private static java.sql.Date convertToSqlDate(LocalDate localDate) {
        return localDate != null ? java.sql.Date.valueOf(localDate) : null;
    }


    //更新数据
    public static boolean updateBoatData(Boat boat, byte[] imageBytes){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE 船只基本资料表 SET 船主姓名=?, 身份证号=?, 联系电话=?, 详细住址=?, 船名=?, 船舶类型=?, 船体材料=?, 机型=?, 船籍港=?, 建造厂=?, 船舶登记号=?, 营运证号=?, 入户时间=?, 迁出时间=?, 建成时间=?, 总长=?, 型宽=?, 型深=?, 总吨=?, 净吨=?, 主机功率=?, 载重吨=?, 航行区域=?, 备注=?, 船舶相片=?, 船检登记号=? WHERE 序号=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, boat.getOwnerName());
                pstmt.setString(2, boat.getIdentityCard());
                pstmt.setString(3, boat.getContactNumber());
                pstmt.setString(4, boat.getAddress());
                pstmt.setString(5, boat.getBoatName());
                pstmt.setString(6, boat.getBoatType());
                pstmt.setString(7, boat.getHullMaterial());
                pstmt.setString(8, boat.getEngineType());
                pstmt.setString(9, boat.getPortOfRegistry());
                pstmt.setString(10, boat.getBuilder());
                pstmt.setString(11, boat.getRegistrationNumber());
                pstmt.setString(12, boat.getOperationCertificateNumber());
                // 然后在准备 PreparedStatement 时使用这个方法
                // 在准备 PreparedStatement 时使用
                // 在准备 PreparedStatement 时使用
                pstmt.setDate(13, MysqlConnectiontest.convertToSqlDate(MysqlConnectiontest.convertToLocalDateFromUtilDate(boat.getEntryDate())));
                pstmt.setDate(14, MysqlConnectiontest.convertToSqlDate(MysqlConnectiontest.convertToLocalDateFromUtilDate(boat.getExitDate())));
                pstmt.setDate(15, MysqlConnectiontest.convertToSqlDate(MysqlConnectiontest.convertToLocalDateFromUtilDate(boat.getCompletionDate())));
                pstmt.setBigDecimal(16, boat.getLength());
                pstmt.setBigDecimal(17, boat.getWidth());
                pstmt.setBigDecimal(18, boat.getDepth());
                pstmt.setBigDecimal(19, boat.getGrossTonnage());
                pstmt.setBigDecimal(20, boat.getNetTonnage());
                pstmt.setBigDecimal(21, boat.getMainEnginePower());
                pstmt.setBigDecimal(22, boat.getCarryingCapacity());
                pstmt.setString(23, boat.getNavigationArea());
                pstmt.setString(24, boat.getNotes());

                // 处理图片数据
                // 在设置 PreparedStatement 参数之后
                if (imageBytes != null) {
                    Blob photoBlob = conn.createBlob();
                    photoBlob.setBytes(1, imageBytes);
                    pstmt.setBlob(25, photoBlob);
                } else {
                    pstmt.setNull(25, Types.BLOB);
                }

//                pstmt.setBlob(25, photoBlob != null ? photoBlob : pstmt.getBlob(25));

                pstmt.setString(26, boat.getRegistrationInspectionNumber());
                pstmt.setInt(27, boat.getId()); // 设置序号作为更新条件

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 删除船只的方法
    public static void deleteBoats(List<Boat> boatsToDelete) {
        String sql = "DELETE FROM 船只基本资料表 WHERE 序号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Boat boat : boatsToDelete) {
                pstmt.setInt(1, boat.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常
        }
    }


//    用于“船只营运证书”的查询
    // 方法来获取VesselOperationCertificate数据
    public static List<VesselOperationCertificate> getVesselOperationCertificates() {
        List<VesselOperationCertificate> certificates = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM 船只营运证书基本表 WHERE DATEDIFF(营运证使用有效期至, CURDATE()) <= 30")) {

            while (rs.next()) {
                VesselOperationCertificate certificate = new VesselOperationCertificate(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("营运证编号"),
                        rs.getString("船舶所有人"),
                        rs.getString("船舶登记号"),
                        rs.getString("经营人许可证号码"),
                        rs.getString("管理人许可证号码"),
                        rs.getString("发证机关"),
                        rs.getDate("营运证使用有效期至") != null ? rs.getDate("营运证使用有效期至").toLocalDate() : null,
                        rs.getDate("发证日期") != null ? rs.getDate("发证日期").toLocalDate() : null
                );
                certificates.add(certificate);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return certificates;
    }

//    对于新增插入“船只营运证书”的数据
    public static boolean addVesselOperationCertificate(VesselOperationCertificate certificate) {
    String sql = "INSERT INTO 船只营运证书基本表 (船名, 船检登记号, 营运证编号, 船舶所有人, 船舶登记号, 经营人许可证号码, 管理人许可证号码, 发证机关, 营运证使用有效期至, 发证日期) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(url, username, password);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, certificate.getVesselName());
        pstmt.setString(2, certificate.getRegistrationNumber());
        pstmt.setString(3, certificate.getCertificateNumber());
        pstmt.setString(4, certificate.getOwner());
        pstmt.setString(5, certificate.getVesselRegistrationNumber());
        pstmt.setString(6, certificate.getOperatorLicenseNumber());
        pstmt.setString(7, certificate.getManagerLicenseNumber());
        pstmt.setString(8, certificate.getIssuingAuthority());
//        System.out.println("ValidityDate: " + certificate.getValidityDate());
//        System.out.println("IssueDate: " + certificate.getIssueDate());

        pstmt.setDate(9, certificate.getValidityDate() != null ? Date.valueOf(certificate.getValidityDate()) : null);
        pstmt.setDate(10, certificate.getIssueDate() != null ? Date.valueOf(certificate.getIssueDate()) : null);

        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}




//    用于“船只营运证书”所有的数据
    public static List<VesselOperationCertificate> GetVesselOperationCertificates() {
    List<VesselOperationCertificate> certificates = new ArrayList<>();
    String sql = "SELECT 船名, 船检登记号, 营运证编号, 船舶所有人, 船舶登记号, 经营人许可证号码, 管理人许可证号码, 发证机关, 营运证使用有效期至, 发证日期 FROM 船只营运证书基本表";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VesselOperationCertificate certificate = new VesselOperationCertificate(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("营运证编号"),
                        rs.getString("船舶所有人"),
                        rs.getString("船舶登记号"),
                        rs.getString("经营人许可证号码"),
                        rs.getString("管理人许可证号码"),
                        rs.getString("发证机关"),
                        rs.getDate("营运证使用有效期至") != null ? rs.getDate("营运证使用有效期至").toLocalDate() : null,
                        rs.getDate("发证日期") != null ? rs.getDate("发证日期").toLocalDate() : null
                );

                certificates.add(certificate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certificates;
    }


    // 更新船只营运证书基本表的方法
    public static void modifyCertificateSQL(VesselOperationCertificate certificate) {
        String sql = "UPDATE 船只营运证书基本表 SET 营运证编号 = ?, 船舶所有人 = ?, 船舶登记号 = ?, 经营人许可证号码 = ?, 管理人许可证号码 = ?, 发证机关 = ?, 营运证使用有效期至 = ?, 发证日期 = ? WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置SQL参数
            pstmt.setString(1, certificate.getCertificateNumber());
            pstmt.setString(2, certificate.getOwner());
            pstmt.setString(3, certificate.getVesselRegistrationNumber());
            pstmt.setString(4, certificate.getOperatorLicenseNumber());
            pstmt.setString(5, certificate.getManagerLicenseNumber());
            pstmt.setString(6, certificate.getIssuingAuthority());
            pstmt.setDate(7, Date.valueOf(certificate.getValidityDate()));
            pstmt.setDate(8, Date.valueOf(certificate.getIssueDate()));
            pstmt.setString(9, certificate.getVesselName());
            pstmt.setString(10, certificate.getRegistrationNumber());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    检测“船只营运证书基本表”中是否存在船只的方法
    public static boolean CheckIfShipExists(String vesselName, String registrationNumber) {
        System.out.println("Checking existence in 船只营运证书基本表 for Vessel: " + vesselName + ", Registration Number: " + registrationNumber);

        // 此处的SQL语句应该与你的数据库表结构相对应
        String sql = "SELECT COUNT(*) FROM 船只营运证书基本表 WHERE 船名 = ? AND 船检登记号 = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vesselName);
            pstmt.setString(2, registrationNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // 如果找到匹配的记录则返回true
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // 如果没有找到匹配的记录或发生异常，则返回false
    }



    public static boolean deleteVesselOperationCertificate(String vesselName, String registrationNumber) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "DELETE FROM 船只营运证书基本表 WHERE 船名 = ? AND 船检登记号 = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, vesselName);
                pstmt.setString(2, registrationNumber);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


//  检验证书的一个构造器
    public static void AddInspectionCertificate(Vessel vessel) {
        String sql = "INSERT INTO 船只检验证书基本表 (船名, 船检登记号, 检验证编号, 船舶所有人, 船舶登记号, " +
                "船舶检验类型, 下次检验时间, 通知时间, 检验机关, 检验证使用有效期至, 发证日期, 船只检验情况记录) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vessel.getVesselName());
            pstmt.setString(2, vessel.getRegistrationNumber());
            pstmt.setString(3, vessel.getCertificateNumber());
            pstmt.setString(4, vessel.getOwner());
            pstmt.setString(5, vessel.getVesselRegistrationNumber());
            pstmt.setString(6, vessel.getInspectionType());
            pstmt.setDate(7, vessel.getNextInspectionDate());
            pstmt.setDate(8, vessel.getNotificationDate());
            pstmt.setString(9, vessel.getInspectionAuthority());
            pstmt.setDate(10, vessel.getCertificateValidity());
            pstmt.setDate(11, vessel.getIssueDate());
            pstmt.setString(12, vessel.getInspectionRecord());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 检测“船只检验证书基本表”中是否存在相同船名和船检登记号的船只的方法
    public static boolean checkIfVesselExistsInInspectionCertificates(String vesselName, String registrationNumber) {
        System.out.println("Checking existence in 船只检验证书基本表 for Vessel: " + vesselName + ", Registration Number: " + registrationNumber);

        String sql = "SELECT COUNT(*) FROM 船只检验证书基本表 WHERE 船名 = ? AND 船检登记号 = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vesselName);
            pstmt.setString(2, registrationNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // 如果找到匹配的记录则返回true
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // 如果没有找到匹配的记录或发生异常，则返回false
    }

//用于“船只检验证书基本表”填充表格的信息
    public static ObservableList<Vessel> LoadDataQueryCertificate() {
        ObservableList<Vessel> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM 船只检验证书基本表";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vessel vessel = new Vessel(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("检验证编号"),
                        rs.getString("船舶所有人"),
                        rs.getString("船舶登记号"),
                        rs.getString("船舶检验类型"),
                        rs.getDate("下次检验时间"),
                        rs.getDate("通知时间"),
                        rs.getString("检验机关"),
                        rs.getDate("检验证使用有效期至"),
                        rs.getDate("发证日期"),
                        rs.getString("船只检验情况记录")
                );
                data.add(vessel);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }


//“船只检验证书基本表”修改方法
    public static boolean ModifyInspectionCertificateData(String vesselName, String registrationNumber, String certificateNumber, String owner, String vesselRegistrationNumber, String inspectionType, LocalDate notificationDate, String inspectionAuthority, LocalDate certificateValidity, LocalDate issueDate, String inspectionRecord) {
    // 准备SQL更新语句
        String sql = "UPDATE 船只检验证书基本表 SET 检验证编号 = ?, 船舶所有人 = ?, 船舶登记号 = ?, 船舶检验类型 = ?, 通知时间 = ?, 检验机关 = ?, 检验证使用有效期至 = ?, 发证日期 = ?, 船只检验情况记录 = ? WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(MysqlConnectiontest.url, MysqlConnectiontest.username, MysqlConnectiontest.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数
            pstmt.setString(1, certificateNumber);
            pstmt.setString(2, owner);
            pstmt.setString(3, vesselRegistrationNumber);
            pstmt.setString(4, inspectionType);
            pstmt.setDate(5, java.sql.Date.valueOf(notificationDate));
            pstmt.setString(6, inspectionAuthority);
            pstmt.setDate(7, java.sql.Date.valueOf(certificateValidity));
            pstmt.setDate(8, java.sql.Date.valueOf(issueDate));
            pstmt.setString(9, inspectionRecord);
            pstmt.setString(10, vesselName);
            pstmt.setString(11, registrationNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


//    对于“船只检验证书基本表”的删除操作
    public static boolean DeleteInspectionCertificate(String vesselName, String registrationNumber) {
        String sql = "DELETE FROM 船只检验证书基本表 WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vesselName);
            pstmt.setString(2, registrationNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

//对于"船只安检证书基本表"的添加信息
    public static void addShipSecurityCertificate(ShipSecurityCertificate certificate) {
        String sql = "INSERT INTO 船只安检证书基本表 (船名, 船检登记号, 安检证书编号, 船舶所有人, 船舶登记号, 检查机关, 下次检查时间, 通知时间, 安检证书使用有效期至, 发证日期, 船只检验情况记录) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, certificate.getShipName());
            pstmt.setString(2, certificate.getShipInspectionRegistrationNumber());
            pstmt.setString(3, certificate.getSecurityCertificateNumber());
            pstmt.setString(4, certificate.getShipOwner());
            pstmt.setString(5, certificate.getShipRegistrationNumber());
            pstmt.setString(6, certificate.getInspectionAuthority());
            pstmt.setDate(7, new java.sql.Date(certificate.getNextInspectionDate().getTime()));
            pstmt.setDate(8, new java.sql.Date(certificate.getNotificationDate().getTime()));
            pstmt.setDate(9, new java.sql.Date(certificate.getCertificateValidityEndDate().getTime()));
            pstmt.setDate(10, new java.sql.Date(certificate.getIssueDate().getTime()));
            pstmt.setString(11, certificate.getInspectionRecord());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//用于检测是否在“船只安检证书基本表”中已存在船名和船检登记号
    public static boolean CheckIfExistsSecurityCertificate(String shipName, String shipInspectionRegistrationNumber) {
        String sql = "SELECT COUNT(*) FROM 船只安检证书基本表 WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipName);
            pstmt.setString(2, shipInspectionRegistrationNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


//用于加载数据————“船只安检证书基本表”
    public static List<ShipSecurityCertificate> loadDataSecurityCertificate() {
        List<ShipSecurityCertificate> certificates = new ArrayList<>();

        String sql = "SELECT * FROM 船只安检证书基本表";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ShipSecurityCertificate certificate = new ShipSecurityCertificate(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("安检证书编号"),
                        rs.getString("船舶所有人"),
                        rs.getString("船舶登记号"),
                        rs.getString("检查机关"),
                        rs.getDate("下次检查时间"),
                        rs.getDate("通知时间"),
                        rs.getDate("安检证书使用有效期至"),
                        rs.getDate("发证日期"),
                        rs.getString("船只检验情况记录")
                );
                certificates.add(certificate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return certificates;
    }


    // 更新船只安检证书信息
    public static boolean UpdateSecurityCertificateData(String shipName, String shipInspectionRegistrationNumber, String securityCertificateNumber, String shipOwner, String shipRegistrationNumber, String inspectionAuthority, Date nextInspectionDate, Date notificationDate, Date certificateValidityEndDate, Date issueDate, String inspectionRecord) {
        String sql = "UPDATE 船只安检证书基本表 SET 安检证书编号 = ?, 船舶所有人 = ?, 船舶登记号 = ?, 检查机关 = ?, 下次检查时间 = ?, 通知时间 = ?, 安检证书使用有效期至 = ?, 发证日期 = ?, 船只检验情况记录 = ? WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, securityCertificateNumber);
            pstmt.setString(2, shipOwner);
            pstmt.setString(3, shipRegistrationNumber);
            pstmt.setString(4, inspectionAuthority);
            pstmt.setDate(5, new java.sql.Date(nextInspectionDate.getTime()));
            pstmt.setDate(6, new java.sql.Date(notificationDate.getTime()));
            pstmt.setDate(7, new java.sql.Date(certificateValidityEndDate.getTime()));
            pstmt.setDate(8, new java.sql.Date(issueDate.getTime()));
            pstmt.setString(9, inspectionRecord);
            pstmt.setString(10, shipName);
            pstmt.setString(11, shipInspectionRegistrationNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//对于“船只安检证书基本表”中的数据进行一个删除
    public static boolean DeleteSecurityCertificateData(String shipName, String shipInspectionRegistrationNumber) {
        String sql = "DELETE FROM 船只安检证书基本表 WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipName);
            pstmt.setString(2, shipInspectionRegistrationNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    //“船只安检证书基本表”中的属性“安检证书使用有效期至”还有30天不到到期符合的数据
    public static List<ShipSecurityCertificate> viewExpiringSecurityCertificateData() {
        List<ShipSecurityCertificate> expiringCertificates = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        String sql = "SELECT * FROM 船只安检证书基本表 WHERE 安检证书使用有效期至 >= ? AND 安检证书使用有效期至 <= ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置查询参数
            pstmt.setDate(1, java.sql.Date.valueOf(currentDate));
            pstmt.setDate(2, java.sql.Date.valueOf(currentDate.plusDays(30)));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ShipSecurityCertificate certificate = new ShipSecurityCertificate(
                            rs.getString("船名"),
                            rs.getString("船检登记号"),
                            rs.getString("安检证书编号"),
                            rs.getString("船舶所有人"),
                            rs.getString("船舶登记号"),
                            rs.getString("检查机关"),
                            rs.getDate("下次检查时间"),
                            rs.getDate("通知时间"),
                            rs.getDate("安检证书使用有效期至"),
                            rs.getDate("发证日期"),
                            rs.getString("船只检验情况记录")
                    );
                    expiringCertificates.add(certificate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expiringCertificates;
    }



    // 检查“国籍配员证书基本信息表”中是否有相同的“船名”和“船检登记号”
    public static boolean checkNationalityManningCertificate(String vesselName, String registrationNumber) {
        System.out.println("Checking existence in Nationality Manning Certificate for Vessel: " + vesselName + ", Registration Number: " + registrationNumber);

        String sql = "SELECT COUNT(*) FROM 国籍配员证书基本信息表 WHERE 船名 = ? AND 船检登记号 = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vesselName);
            pstmt.setString(2, registrationNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // 如果找到匹配的记录则返回true
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // 如果没有找到匹配的记录或发生异常，则返回false
    }

    // 添加数据方法对表“国籍配员证书基本信息表”中添加数据
    public static boolean AddNationalityManningCertificate(ShipNationalityManningCertificate certificate) {
        String sql = "INSERT INTO 国籍配员证书基本信息表 (船名, 船检登记号, 国籍配员证书编号, 船舶所有人, 船舶登记号, 下次换证时间, 换证通知时间, 国籍配员证书使用有效期至, 发证日期, 国籍配员证书换证时间记录) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, certificate.getShipName());
            pstmt.setString(2, certificate.getShipInspectionRegistrationNumber());
            pstmt.setString(3, certificate.getNationalityManningCertificateNumber());
            pstmt.setString(4, certificate.getShipOwner());
            pstmt.setString(5, certificate.getShipRegistrationNumber());
            pstmt.setDate(6, new Date(certificate.getNextCertificateRenewalDate().getTime()));
            pstmt.setDate(7, new Date(certificate.getCertificateRenewalNotificationDate().getTime()));
            pstmt.setDate(8, new Date(certificate.getCertificateValidityEndDate().getTime()));
            pstmt.setDate(9, new Date(certificate.getIssueDate().getTime()));
            pstmt.setString(10, certificate.getCertificateRenewalHistory());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }




//    用于查询““国籍配员证书基本信息表”中的全部数据------>加载数据
    public static List<ShipNationalityManningCertificate> QueryNationalityManningModuleData() {
        List<ShipNationalityManningCertificate> certificates = new ArrayList<>();
        String sql = "SELECT 船名, 船检登记号, 国籍配员证书编号, 船舶所有人, 船舶登记号, 下次换证时间, 换证通知时间, 国籍配员证书使用有效期至, 发证日期, 国籍配员证书换证时间记录 FROM 国籍配员证书基本信息表";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ShipNationalityManningCertificate certificate = new ShipNationalityManningCertificate(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("国籍配员证书编号"),
                        rs.getString("船舶所有人"),
                        rs.getString("船舶登记号"),
                        rs.getDate("下次换证时间"),
                        rs.getDate("换证通知时间"),
                        rs.getDate("国籍配员证书使用有效期至"),
                        rs.getDate("发证日期"),
                        rs.getString("国籍配员证书换证时间记录")
                );

                certificates.add(certificate);
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return certificates;
    }


//    用于加载数据表“”国籍配员证书基本信息表“”之中的数据并填充
    public static List<ShipNationalityManningCertificate> LoadDataNationalityManning() {
        List<ShipNationalityManningCertificate> certificates = new ArrayList<>();
        String sql = "SELECT 船名, 船检登记号, 国籍配员证书编号, 船舶所有人, 船舶登记号, 下次换证时间, 换证通知时间, 国籍配员证书使用有效期至, 发证日期, 国籍配员证书换证时间记录 FROM 国籍配员证书基本信息表";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ShipNationalityManningCertificate certificate = new ShipNationalityManningCertificate(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("国籍配员证书编号"),
                        rs.getString("船舶所有人"),
                        rs.getString("船舶登记号"),
                        rs.getDate("下次换证时间"),
                        rs.getDate("换证通知时间"),
                        rs.getDate("国籍配员证书使用有效期至"),
                        rs.getDate("发证日期"),
                        rs.getString("国籍配员证书换证时间记录")
                );

                certificates.add(certificate);
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return certificates;
    }





    //    更新/修改“国籍配员证书基本信息表”中的信息。
    public static boolean ModifyNationalityManningModuleData(ShipNationalityManningCertificate certificate) {
        String sql = "UPDATE 国籍配员证书基本信息表 SET 国籍配员证书编号 = ?, 船舶所有人 = ?, 船舶登记号 = ?, 下次换证时间 = ?, 换证通知时间 = ?, 国籍配员证书使用有效期至 = ?, 发证日期 = ?, 国籍配员证书换证时间记录 = ? WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, certificate.getNationalityManningCertificateNumber());
            pstmt.setString(2, certificate.getShipOwner());
            pstmt.setString(3, certificate.getShipRegistrationNumber());
            pstmt.setDate(4, new java.sql.Date(certificate.getNextCertificateRenewalDate().getTime()));
            pstmt.setDate(5, new java.sql.Date(certificate.getCertificateRenewalNotificationDate().getTime()));
            pstmt.setDate(6, new java.sql.Date(certificate.getCertificateValidityEndDate().getTime()));
            pstmt.setDate(7, new java.sql.Date(certificate.getIssueDate().getTime()));
            pstmt.setString(8, certificate.getCertificateRenewalHistory());
            pstmt.setString(9, certificate.getShipName());
            pstmt.setString(10, certificate.getShipInspectionRegistrationNumber());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    // 删除“国籍配员证书基本信息表”中的数据
    public static boolean DeleteNationalityManningMoudleData(String shipName, String shipInspectionRegistrationNumber) {
        String sql = "DELETE FROM 国籍配员证书基本信息表 WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipName);
            pstmt.setString(2, shipInspectionRegistrationNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



//    查询“国籍配员证书基本信息表”快要到期的方法
    public static List<ShipNationalityManningCertificate> ViewExpiringNationalityManningModuleData() {
    String sql = "SELECT * FROM 国籍配员证书基本信息表 WHERE DATEDIFF(国籍配员证书使用有效期至, CURDATE()) <= 30";

    List<ShipNationalityManningCertificate> certificates = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ShipNationalityManningCertificate certificate = new ShipNationalityManningCertificate(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("国籍配员证书编号"),
                        rs.getString("船舶所有人"),
                        rs.getString("船舶登记号"),
                        rs.getDate("下次换证时间"),
                        rs.getDate("换证通知时间"),
                        rs.getDate("国籍配员证书使用有效期至"),
                        rs.getDate("发证日期"),
                        rs.getString("国籍配员证书换证时间记录")
                );

                certificates.add(certificate);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return certificates;
    }




    // 用于向数据库中插入新的航道费缴纳记录
    public static void InsertDataWaterwayDuesPayment(WaterwayDuesPayment payment) {
        String sql = "INSERT INTO 航道费缴纳情况表 (船名, 船检登记号, 航道费, 填表日期, 航道费合计, 航道费缴纳记录) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, payment.getShipName());
            pstmt.setString(2, payment.getShipRegistrationNumber());
            pstmt.setBigDecimal(3, payment.getWaterwayFeePerMonth());
            pstmt.setDate(4, new java.sql.Date(payment.getDateOfFilling().getTime()));
            pstmt.setBigDecimal(5, payment.getTotalWaterwayFee());
            pstmt.setString(6, payment.getPaymentRecord());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("A new record has been inserted successfully.");
            } else {
                System.out.println("A record insertion failed.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


//    用来加载"航道费缴纳情况表"内的全部数据
    public static List<WaterwayDuesPayment> LoadDataWaterwayDuesPayment() {
        List<WaterwayDuesPayment> payments = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM 航道费缴纳情况表")) {

            while (rs.next()) {
                String shipName = rs.getString("船名");
                String shipRegistrationNumber = rs.getString("船检登记号");
                BigDecimal waterwayFeePerMonth = rs.getBigDecimal("航道费");
                Date dateOfFilling = rs.getDate("填表日期");
                BigDecimal totalWaterwayFee = rs.getBigDecimal("航道费合计");
                String paymentRecord = rs.getString("航道费缴纳记录");

                WaterwayDuesPayment payment = new WaterwayDuesPayment(shipName, shipRegistrationNumber, waterwayFeePerMonth, dateOfFilling, totalWaterwayFee, paymentRecord);
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    // 更新数据库中的航道费缴纳情况表航道费缴纳记录
    public static void UpdateDataWaterwayDuesPayment(WaterwayDuesPayment payment) {
        // 更新语句，使用船名和船检登记号作为条件
        String sql = "UPDATE 航道费缴纳情况表 SET 航道费 = ?, 填表日期 = ?, 航道费合计 = ?, 航道费缴纳记录 = ? WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置PreparedStatement的参数
            pstmt.setBigDecimal(1, payment.getWaterwayFeePerMonth());
            pstmt.setDate(2, new java.sql.Date(payment.getDateOfFilling().getTime()));
            pstmt.setBigDecimal(3, payment.getTotalWaterwayFee());
            pstmt.setString(4, payment.getPaymentRecord());
            pstmt.setString(5, payment.getShipName());
            pstmt.setString(6, payment.getShipRegistrationNumber());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("A record has been updated successfully.");
            } else {
                System.out.println("Record update failed.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }



    // 检查“航道费缴纳情况表”中是否存在相同的“船名”和“船检登记号”
    public static boolean checkExistsWaterwayDuesPayment(String shipName, String shipRegistrationNumber) {
        System.out.println("Checking existence for Waterway Dues Payment Record: " + shipName + ", Registration Number: " + shipRegistrationNumber);

        String sql = "SELECT COUNT(*) FROM 航道费缴纳情况表 WHERE 船名 = ? AND 船检登记号 = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipName);
            pstmt.setString(2, shipRegistrationNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // 如果找到匹配的记录则返回true
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // 如果没有找到匹配的记录或发生异常，则返回false
    }




    // 删除航道费缴纳情况表中的记录
    public static void DeleteDataWaterwayDuesPayment(String shipName, String shipRegistrationNumber) {
        // 删除语句，使用船名和船检登记号作为删除条件
        String sql = "DELETE FROM 航道费缴纳情况表 WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置PreparedStatement的参数
            pstmt.setString(1, shipName);
            pstmt.setString(2, shipRegistrationNumber);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("A record has been deleted successfully.");
            } else {
                System.out.println("Record deletion failed or record does not exist.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }




//    用来数据表“航道费缴纳情况表”加载“填表日期”加上30天小于当今日期的
    public static List<WaterwayDuesPayment> ViewExpiringLoadDataWaterwayDuesPayment() {
        List<WaterwayDuesPayment> payments = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             // 使用SQL查询语句来筛选出填表日期加30天大于当前日期的记录
             ResultSet rs = stmt.executeQuery("SELECT * FROM 航道费缴纳情况表 WHERE DATE_ADD(填表日期, INTERVAL 30 DAY) <= CURDATE()")) {

            while (rs.next()) {
                String shipName = rs.getString("船名");
                String shipRegistrationNumber = rs.getString("船检登记号");
                BigDecimal waterwayFeePerMonth = rs.getBigDecimal("航道费");
                Date dateOfFilling = rs.getDate("填表日期");
                BigDecimal totalWaterwayFee = rs.getBigDecimal("航道费合计");
                String paymentRecord = rs.getString("航道费缴纳记录");

                WaterwayDuesPayment payment = new WaterwayDuesPayment(shipName, shipRegistrationNumber, waterwayFeePerMonth, dateOfFilling, totalWaterwayFee, paymentRecord);
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }






    // 插入数据——船只按月缴纳水运费基本信息表
    public static void AddDataShipPayMonthlyWaterFreight(ShipPayMonthlyWater payment) {
        String sql = "INSERT INTO 船只按月缴纳水运费基本信息表 (船名, 船检登记号, 航道费, 填表日期, 水运费合计, 水运费缴纳记录) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, payment.getShipName());
            pstmt.setString(2, payment.getShipRegistrationNumber());
            pstmt.setBigDecimal(3, payment.getChannelFee());
            pstmt.setDate(4, new java.sql.Date(payment.getFormFillingDate().getTime()));
            pstmt.setBigDecimal(5, payment.getTotalWaterTransportFee());
            pstmt.setString(6, payment.getWaterTransportFeeRecord());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("A new record has been inserted successfully.");
            } else {
                System.out.println("A record insertion failed.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // 读取“船只按月缴纳水运费基本信息表”中的全部信息
    public static List<ShipPayMonthlyWater> LoadDataShipPayMonthlyWaterFreight() {
        List<ShipPayMonthlyWater> payments = new ArrayList<>();
        String sql = "SELECT 船名, 船检登记号, 航道费, 填表日期, 水运费合计, 水运费缴纳记录 FROM 船只按月缴纳水运费基本信息表";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String shipName = rs.getString("船名");
                String shipRegistrationNumber = rs.getString("船检登记号");
                BigDecimal channelFee = rs.getBigDecimal("航道费");
                Date formFillingDate = new Date(rs.getDate("填表日期").getTime());
                BigDecimal totalWaterTransportFee = rs.getBigDecimal("水运费合计");
                String waterTransportFeeRecord = rs.getString("水运费缴纳记录");

                ShipPayMonthlyWater payment = new ShipPayMonthlyWater(shipName, shipRegistrationNumber, channelFee, formFillingDate, totalWaterTransportFee, waterTransportFeeRecord);
                payments.add(payment);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return payments;
    }



    // 更新“船只按月缴纳水运费基本信息表”中的数据
    public static void UpdateDataShipPayMonthlyWaterFreight(ShipPayMonthlyWater payment) {
        String sql = "UPDATE 船只按月缴纳水运费基本信息表 SET 航道费 = ?, 填表日期 = ?, 水运费合计 = ?, 水运费缴纳记录 = ? WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, payment.getChannelFee());
            pstmt.setDate(2, new java.sql.Date(payment.getFormFillingDate().getTime()));
            pstmt.setBigDecimal(3, payment.getTotalWaterTransportFee());
            pstmt.setString(4, payment.getWaterTransportFeeRecord());
            pstmt.setString(5, payment.getShipName());
            pstmt.setString(6, payment.getShipRegistrationNumber());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No record was updated.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 检查是否在数据表“船只按月缴纳水运费基本信息表”中存在特定的“船名”和“船检登记号”
    public static boolean checkIfExistsShipPayMonthlyWaterFreight(String shipName, String shipRegistrationNumber) {
        System.out.println("Checking existence for Ship: " + shipName + ", Registration Number: " + shipRegistrationNumber);

        String sql = "SELECT COUNT(*) FROM 船只按月缴纳水运费基本信息表 WHERE 船名 = ? AND 船检登记号 = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipName);
            pstmt.setString(2, shipRegistrationNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // 如果找到匹配的记录则返回true
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // 如果没有找到匹配的记录或发生异常，则返回false
    }



    // 删除“船只按月缴纳水运费基本信息表”中的特定数据
    public static boolean DeleteDataShipPayMonthlyWaterFreight(String shipName, String shipRegistrationNumber) {
        String sql = "DELETE FROM 船只按月缴纳水运费基本信息表 WHERE 船名 = ? AND 船检登记号 = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipName);
            pstmt.setString(2, shipRegistrationNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // 如果影响的行数大于0，则返回true
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // 如果没有删除记录或发生异常，则返回false
    }



    // 筛选出填表日期加上30天大于今天日期的记录
    public static List<ShipPayMonthlyWater> ViewExpiringShipPayMonthlyWaterFreight() {
        List<ShipPayMonthlyWater> payments = new ArrayList<>();
        String sql = "SELECT 船名, 船检登记号, 航道费, 填表日期, 水运费合计, 水运费缴纳记录 FROM 船只按月缴纳水运费基本信息表 WHERE DATE_ADD(填表日期, INTERVAL 30 DAY) <= CURDATE()";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String shipName = rs.getString("船名");
                String shipRegistrationNumber = rs.getString("船检登记号");
                BigDecimal channelFee = rs.getBigDecimal("航道费");
                Date formFillingDate = new Date(rs.getDate("填表日期").getTime());
                BigDecimal totalWaterTransportFee = rs.getBigDecimal("水运费合计");
                String waterTransportFeeRecord = rs.getString("水运费缴纳记录");

                ShipPayMonthlyWater payment = new ShipPayMonthlyWater(shipName, shipRegistrationNumber, channelFee, formFillingDate, totalWaterTransportFee, waterTransportFeeRecord);
                payments.add(payment);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return payments;
    }




    //  检查船只基本表中是否有存在的“船名”和“船检登记号”的数据是否合理
    public static boolean checkIfVesselExists(String vesselName, String registrationNumber) {
        System.out.println("Checking existence for Vessel: " + vesselName + ", Registration Number: " + registrationNumber);

        String sql = "SELECT COUNT(*) FROM 船只基本资料表 WHERE 船名 = ? AND 船检登记号 = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vesselName);
            pstmt.setString(2, registrationNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // 如果找到匹配的记录则返回true
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // 如果没有找到匹配的记录或发生异常，则返回false
    }




//    显示Mysql“联合证书视图”视图表内的数据
    public static List<CertificateInfo> ViewQueryCertificateModule() {
        List<CertificateInfo> certificateInfoList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM 联合证书视图")) {

            while (rs.next()) {
                CertificateInfo info = new CertificateInfo(
                        rs.getString("船名"),
                        rs.getString("船检登记号"),
                        rs.getString("船舶所有人") != null ? rs.getString("船舶所有人") : "",
                        rs.getString("船舶登记号") != null ? rs.getString("船舶登记号") : "",
                        rs.getString("国籍配员证书编号") != null ? rs.getString("国籍配员证书编号") : "",
                        rs.getString("国籍配员下次换证时间") != null ? rs.getString("国籍配员下次换证时间") : "",
                        rs.getString("国籍配员换证通知时间") != null ? rs.getString("国籍配员换证通知时间") : "",
                        rs.getString("国籍配员证书使用有效期至") != null ? rs.getString("国籍配员证书使用有效期至") : "",
                        rs.getString("国籍配员发证日期") != null ? rs.getString("国籍配员发证日期") : "",
                        rs.getString("国籍配员证书换证时间记录") != null ? rs.getString("国籍配员证书换证时间记录") : "",
                        rs.getString("安检证书编号") != null ? rs.getString("安检证书编号") : "",
                        rs.getString("检查机关") != null ? rs.getString("检查机关") : "",
                        rs.getString("下次检查时间") != null ? rs.getString("下次检查时间") : "",
                        rs.getString("安检通知时间") != null ? rs.getString("安检通知时间") : "",
                        rs.getString("安检证书有效期至") != null ? rs.getString("安检证书有效期至") : "",
                        rs.getString("安检证书发证日期") != null ? rs.getString("安检证书发证日期") : "",
                        rs.getString("安检情况记录") != null ? rs.getString("安检情况记录") : "",
                        rs.getString("检验证编号") != null ? rs.getString("检验证编号") : "",
                        rs.getString("船舶检验类型") != null ? rs.getString("船舶检验类型") : "",
                        rs.getString("检验证下次检验时间") != null ? rs.getString("检验证下次检验时间") : "",
                        rs.getString("检验证通知时间") != null ? rs.getString("检验证通知时间") : "",
                        rs.getString("检验证检验机关") != null ? rs.getString("检验证检验机关") : "",
                        rs.getString("检验证使用有效期至") != null ? rs.getString("检验证使用有效期至") : "",
                        rs.getString("检验证发证日期") != null ? rs.getString("检验证发证日期") : "",
                        rs.getString("检验证情况记录") != null ? rs.getString("检验证情况记录") : "",
                        rs.getString("营运证编号") != null ? rs.getString("营运证编号") : "",
                        rs.getString("经营人许可证号码") != null ? rs.getString("经营人许可证号码") : "",
                        rs.getString("管理人许可证号码") != null ? rs.getString("管理人许可证号码") : "",
                        rs.getString("营运证发证机关") != null ? rs.getString("营运证发证机关") : "",
                        rs.getString("营运证使用有效期至") != null ? rs.getString("营运证使用有效期至") : "",
                        rs.getString("营运证发证日期") != null ? rs.getString("营运证发证日期") : ""
                );
                certificateInfoList.add(info);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certificateInfoList;
    }




//    读取“船只缴费总览视图”内的相应数据

    public static List<ShipPaymentInfo> getShipPaymentInfo() {
        List<ShipPaymentInfo> shipPaymentInfoList = new ArrayList<>();
        String query = "SELECT * FROM 船只缴费总览视图";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String shipName = rs.getString("船名") != null ? rs.getString("船名") : "";
                String registrationNumber = rs.getString("船检登记号") != null ? rs.getString("船检登记号") : "";
                BigDecimal channelFee = rs.getBigDecimal("航道费") != null ? rs.getBigDecimal("航道费") : BigDecimal.ZERO;
                Date channelFeeDate = rs.getDate("航道费填表日期");
                BigDecimal totalChannelFee = rs.getBigDecimal("航道费合计") != null ? rs.getBigDecimal("航道费合计") : BigDecimal.ZERO;
                String channelFeeRecord = rs.getString("航道费缴纳记录") != null ? rs.getString("航道费缴纳记录") : "";
                Date transportationFeeDate = rs.getDate("水运费填表日期");
                BigDecimal totalTransportationFee = rs.getBigDecimal("水运费合计") != null ? rs.getBigDecimal("水运费合计") : BigDecimal.ZERO;
                String transportationFeeRecord = rs.getString("水运费缴纳记录") != null ? rs.getString("水运费缴纳记录") : "";

                ShipPaymentInfo shipPaymentInfo = new ShipPaymentInfo(shipName, registrationNumber, channelFee, channelFeeDate, totalChannelFee, channelFeeRecord, transportationFeeDate, totalTransportationFee, transportationFeeRecord);
                shipPaymentInfoList.add(shipPaymentInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shipPaymentInfoList;
    }









//    多表联查证书有效期
    public List<EffectiveCertificateInfo> fetchEffectiveCertificateInfo() {
        List<EffectiveCertificateInfo> certificateInfos = new ArrayList<>(); // 这里初始化certificateInfos列表

        String sql = "SELECT * FROM `证书有效期和办理时间视图`";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                EffectiveCertificateInfo info = new EffectiveCertificateInfo(
                        rs.getString("船名") != null ? rs.getString("船名") : "",
                        rs.getDate("中间检验办理日期") != null ? rs.getDate("中间检验办理日期") : null,
                        rs.getDate("中间检验证书有效期至") != null ? rs.getDate("中间检验证书有效期至") : null,
                        rs.getDate("危险品办理日期") != null ? rs.getDate("危险品办理日期") : null,
                        rs.getDate("危险品证书有效期至") != null ? rs.getDate("危险品证书有效期至") : null,
                        rs.getDate("各证书有效期至") != null ? rs.getDate("各证书有效期至") : null,
                        rs.getDate("国籍证书办理日期") != null ? rs.getDate("国籍证书办理日期") : null,
                        rs.getDate("国籍证书有效期至") != null ? rs.getDate("国籍证书有效期至") : null,
                        rs.getDate("坞内检验办理日期") != null ? rs.getDate("坞内检验办理日期") : null,
                        rs.getDate("坞内检验证书有效期至") != null ? rs.getDate("坞内检验证书有效期至") : null,
                        rs.getDate("油污证书办理日期") != null ? rs.getDate("油污证书办理日期") : null,
                        rs.getDate("油污证书有效期至") != null ? rs.getDate("油污证书有效期至") : null,
                        rs.getDate("港澳证明办理日期") != null ? rs.getDate("港澳证明办理日期") : null,
                        rs.getDate("港澳证明有效期至") != null ? rs.getDate("港澳证明有效期至") : null
                );
                certificateInfos.add(info); // 添加到列表中
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return certificateInfos; // 返回填充好的列表
    }



//    读取视图表“船只信息总览”中的全部数据
    public List<ShipSingleInfo> shipAllData() {
        List<ShipSingleInfo> shipList = new ArrayList<>();
        String sql = "SELECT * FROM 船只信息总览";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                shipList.add(new ShipSingleInfo(
                        rs.getString("船名") != null ? rs.getString("船名") : "",
                        rs.getString("船主姓名") != null ? rs.getString("船主姓名") : "",
                        rs.getString("身份证号") != null ? rs.getString("身份证号") : "",
                        rs.getString("联系电话") != null ? rs.getString("联系电话") : "",
                        rs.getString("详细住址") != null ? rs.getString("详细住址") : "",
                        rs.getString("船舶类型") != null ? rs.getString("船舶类型") : "",
                        rs.getString("船籍港") != null ? rs.getString("船籍港") : "",
                        rs.getString("建造厂") != null ? rs.getString("建造厂") : "",
                        rs.getObject("总长") != null ? rs.getDouble("总长") : 0.0,
                        rs.getObject("型宽") != null ? rs.getDouble("型宽") : 0.0,
                        rs.getObject("型深") != null ? rs.getDouble("型深") : 0.0,
                        rs.getObject("总吨") != null ? rs.getDouble("总吨") : 0.0,
                        rs.getObject("净吨") != null ? rs.getDouble("净吨") : 0.0,
                        rs.getObject("主机功率") != null ? rs.getDouble("主机功率") : 0.0,
                        rs.getObject("载重吨") != null ? rs.getDouble("载重吨") : 0.0,
                        rs.getString("航行区域") != null ? rs.getString("航行区域") : "",
                        rs.getString("备注") != null ? rs.getString("备注") : "",
                        rs.getString("船检登记号") != null ? rs.getString("船检登记号") : "",
                        rs.getString("营运证编号") != null ? rs.getString("营运证编号") : "",
                        rs.getString("发证机关") != null ? rs.getString("发证机关") : "",
                        rs.getString("检验证编号") != null ? rs.getString("检验证编号") : "",
                        rs.getString("船舶检验类型") != null ? rs.getString("船舶检验类型") : "",
                        rs.getString("安检证书编号") != null ? rs.getString("安检证书编号") : "",
                        rs.getString("检查机关") != null ? rs.getString("检查机关") : "",
                        rs.getString("国籍配员证书编号") != null ? rs.getString("国籍配员证书编号") : "",
                        rs.getObject("航道费") != null ? rs.getDouble("航道费") : 0.0,
                        rs.getObject("航道费合计") != null ? rs.getDouble("航道费合计") : 0.0,
                        rs.getObject("水运费合计") != null ? rs.getDouble("水运费合计") : 0.0,
                        rs.getString("水运费缴纳记录") != null ? rs.getString("水运费缴纳记录") : ""
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shipList;
    }


























}




