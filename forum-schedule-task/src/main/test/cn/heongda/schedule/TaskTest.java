package cn.heongda.schedule;

import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.DateBuilder.evenHourDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class TaskTest {

    @Test
    public void test1() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail job = newJob(Hello.class).withIdentity("myjob","myjob")
                .build();

        SimpleTrigger trigger = newTrigger().withIdentity("myTrigger", "group1").startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

    }

    public static void main(String[] args) throws SchedulerException {
        // 获区schedule
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 创建jobdetail用于要执行的具体任务
        JobDetail job = newJob(Hello.class)
                .withIdentity("myjob","myjob")
                .usingJobData("name","刘伟东")
                .usingJobData("age",25)
                .build();

        // 创建trigger，指定具体的定时策略
        SimpleTrigger trigger = newTrigger().
                withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever())
                .withPriority(5)
                .build();
        SimpleTrigger trigger2 = newTrigger().
                withIdentity("myTrigger2", "group1")
                .startAt(evenHourDate(null))  // 在下一个小时整点触发
                .forJob("myjob","myjob")
                .withSchedule(simpleSchedule().withIntervalInSeconds(10)
                        .repeatForever()
                .withMisfireHandlingInstructionNextWithExistingCount())
                .withPriority(5)
                .endAt(dateOf(10,25,22))  // 执行结束时间

                .build();

        // 将任务与定时时间放入到schedule中
        scheduler.scheduleJob(job,trigger2);

        // 开始进行执行
        scheduler.start();
    }
}
