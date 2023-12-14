package com.zqu.System_management_module;
import java.util.Timer;
import java.util.TimerTask;
/**
 * @aurhor Dedryck
 * @create 2023-11-28-21:26
 * @description:
 */
public class CleanupTask extends TimerTask{
    UserOperationLogger logger = new UserOperationLogger();

    public void run() {
        // 删除超过180天的记录
        logger.deleteOldOperations(180);
    }

    public static void startCleanupTask() {
        Timer timer = new Timer();
        long delay = 0;
        long period = 1000 * 60 * 60 * 24; // 每天运行一次
        timer.schedule(new CleanupTask(), delay, period);
    }

}
