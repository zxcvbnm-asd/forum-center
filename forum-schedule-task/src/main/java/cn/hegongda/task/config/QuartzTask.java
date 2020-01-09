package cn.hegongda.task.config;

import cn.hegongda.service.ScheduleTask;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        ScheduleTask task = (ScheduleTask) jobDataMap.get("task");
        task.execute();
    }
}
