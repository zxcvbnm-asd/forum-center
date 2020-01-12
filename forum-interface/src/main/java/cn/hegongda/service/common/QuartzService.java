package cn.hegongda.service.common;


public interface QuartzService {
        // 启动定时任务
        public void startTask(String name, String group, String cron , ScheduleTask scheduleTask);

        // 修改定时任务
        public void modifyTaskTime(String name, String group, String cron);

        // 移除定时任务
        public void removeTask(String name, String group);

        // 关闭定时任务
        public void shutDown();

}
