package cn.hegongda.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerTask {

    private static ScheduledThreadPoolExecutor executor = null;

    public void execute(Runnable runnable){
        if (executor == null ) {
            executor = new ScheduledThreadPoolExecutor(4);
            executor.scheduleWithFixedDelay(runnable, 2000, 1000 * 30, TimeUnit.MILLISECONDS);
        }
    }


    public void shutDown(){
        if (!executor.isShutdown()) {
            executor.shutdown();
            executor = null;
        }
    }
}
