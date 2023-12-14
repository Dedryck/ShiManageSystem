package com.zqu.System_management_module;

import com.zqu.MysqlConnectiontest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * @aurhor Dedryck
 * @create 2023-11-28-20:23
 * @description:
 */
public class UserOperationLogger {
    public void logOperation(String userId, String operationType, String details) {
        // 获取数据库连接信息
        MysqlConnectiontest.ConnectionInfo connInfo = new MysqlConnectiontest().getConnectionInfo();
        String url = connInfo.getUrl();
        String username = connInfo.getUsername();
        String password = connInfo.getPassword();

        String sql = "INSERT INTO 用户操作记录表 (用户ID, 操作类型, 操作细节) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, operationType);
            pstmt.setString(3, details);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // 实际应用中可能需要更复杂的错误处理
        }
    }
//自动清除记录
    public void deleteOldOperations(int daysOld) {
    // 获取数据库连接信息
    MysqlConnectiontest.ConnectionInfo connInfo = new MysqlConnectiontest().getConnectionInfo();
    String url = connInfo.getUrl();
    String username = connInfo.getUsername();
    String password = connInfo.getPassword();

    String sql = "DELETE FROM 用户操作记录表 WHERE DATEDIFF(NOW(), 操作时间) > ?";

    try (Connection conn = DriverManager.getConnection(url, username, password);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, daysOld);
        int deletedRows = pstmt.executeUpdate();
        System.out.println("删除了 " + deletedRows + " 条超过 " + daysOld + " 天的操作记录。");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }



}
