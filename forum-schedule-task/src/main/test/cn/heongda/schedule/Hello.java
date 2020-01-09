package cn.heongda.schedule;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Hello implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Hello word");
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Object name = jobDataMap.get("name");
        System.out.println(name);
    }

}
