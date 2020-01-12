package cn.hegongda.task.config;

import cn.hegongda.service.common.ScheduleTask;
import org.quartz.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 用于添任务
 */
public class QuartzManager {

    private Scheduler scheduler;

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * @param jobName      job名
     * @param jobGroup     job所属组
     * @param triggerName  trigger名
     * @param triggerGroup trigger组
     * @param cron         定时时间
     * @param jobCalss     任务class
     * @param quartzTask   执行的任务内容
     */
    public void addJob(String jobName, String jobGroup, String triggerName,
                       String triggerGroup, String cron, Class jobCalss, ScheduleTask quartzTask) {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("task", quartzTask);

            // 设置工作job
            JobDetail job = newJob(jobCalss).withIdentity(jobName, jobGroup)
                    .usingJobData(jobDataMap)
                    .build();


            // 设置trigger
            CronTrigger cronTrigger = newTrigger().withIdentity(triggerName, triggerGroup)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();

            // 添加
            scheduler.scheduleJob(job, cronTrigger);

            // 启动
            if (scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    /**
     * 启动调度容器
     */
    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);  // 直接停止运行
        }
    }

    /**
     * 关闭调度容器
     */
    public void shutDown() {
        try {
            scheduler.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     *
     * @param jobName
     * @param jobGroup        jobkey
     * @param triggerName
     * @param triggerGroup    triggerKey
     * @param cron            新的定时任务
     */
    public void modifyJobTime(String jobName, String jobGroup, String triggerName,
                              String triggerGroup, String cron ){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            // 根据triggerKey获取trigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            if (trigger == null ) {
               return ;
            }

            // 获取新的时间，当时间不同时进行替换
            String oldTime = trigger.getCronExpression();
            if ( !oldTime.equalsIgnoreCase(cron)) {
                // 新建触发器进行执行
                trigger = newTrigger().withIdentity(triggerName, triggerGroup)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .startNow()
                        .build();
                // 重新执行
                scheduler.rescheduleJob(triggerKey , trigger) ;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 移除任务
     * @param jobName
     * @param jobGroup
     * @param triggerName
     * @param triggerGroup
     */
    public void removeJob(String jobName, String jobGroup, String triggerName,
                          String triggerGroup){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            scheduler.pauseTrigger(triggerKey);  // 暂停触发器
            scheduler.unscheduleJob(triggerKey);  // 停止触发器
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));  // 移除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
