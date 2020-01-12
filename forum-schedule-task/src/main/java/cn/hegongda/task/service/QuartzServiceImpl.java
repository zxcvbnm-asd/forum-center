package cn.hegongda.task.service;

import cn.hegongda.service.common.QuartzService;
import cn.hegongda.service.common.ScheduleTask;
import cn.hegongda.task.config.QuartzManager;
import cn.hegongda.task.config.QuartzTask;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = QuartzService.class)
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private QuartzManager quartzManager;

    // 启动定时任务
    public void startTask(String name, String group, String cron , ScheduleTask scheduleTask){
        this.quartzManager.addJob(name, group, name, group,cron, QuartzTask.class,scheduleTask);
    }

    // 修改定时任务
    public void modifyTaskTime(String name, String group, String cron){
        this.quartzManager.modifyJobTime(name, group, name, group, cron);
    }

    // 移除定时任务
    public void removeTask(String name, String group){
        this.quartzManager.removeJob(name, group, name, group);
    }

    // 关闭定时任务
    public void shutDown(){
        this.quartzManager.shutDown();
    }
}
