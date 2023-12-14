package com.zqu;

import com.zqu.System_management_module.UserOperationLogger;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @aurhor Dedryck
 * @create 2023-11-18-20:49
 * @description:
 */
public class runGUI extends Application{

    private ScheduledExecutorService scheduler;

    @Override
    public void start(Stage primaryStage) {
        GUIFX guiFX = new GUIFX();
        guiFX.run(primaryStage);

        // 设置定时任务
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            boolean result = MysqlConnectiontest.removeOldSoftDeletedRecords();
            if (result) {
                System.out.println("Old soft deleted records removed.");
            }
        }, 0, 1, TimeUnit.DAYS); // 每天执行一次

        // 设置删除用户操作记录的定时任务
        scheduler.scheduleAtFixedRate(() -> {
            UserOperationLogger logger = new UserOperationLogger();
            logger.deleteOldOperations(180); // 删除超过180天的用户操作记录
        }, 0, 1, TimeUnit.DAYS); // 每天执行一次

        // 程序关闭时的处理
        primaryStage.setOnCloseRequest(event -> shutdownScheduler());
    }

    private void shutdownScheduler() {
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
    }

    public static void main(String[] args) {
        ServerConfigWindow.initializeConfig();
        launch(args);
    }


}
