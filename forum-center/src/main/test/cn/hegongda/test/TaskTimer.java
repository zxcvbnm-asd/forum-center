package cn.hegongda.test;

import cn.hegongda.utils.DateUtils;
import org.junit.Test;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskTimer {

    private static Timer timer = new Timer();
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
           /* TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("aha"+Thread.currentThread().getName());
                }
            };
            execute(task,i * 2000);*/

           Runnable task = new Runnable() {
               @Override
               public void run() {
                   System.out.println("哈哈"+Thread.currentThread().getName());
               }
           };
            testSchdeul(task,i * 100);
        }

    }


    public static void execute(TimerTask task, long millins){

        timer.schedule(task, millins);
    }

    public static void testSchdeul(Runnable task, long time){

        executorService.schedule(task,time, TimeUnit.MILLISECONDS);
    }


    @Test
    public void test() {

        Date date = new Date();
        String dateStr = DateUtils.format(date);
        System.out.println(dateStr);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date time = calendar.getTime();
        System.out.println(DateUtils.format(time));

        Map<String, String> map = new HashMap<>();
        map.put("name","liu");
        map.put("sex","nane");
        Set<String> strings = map.keySet();
        for (String string : strings) {
            System.out.println(string);
        }

    }


    @Test
    public void test2(){
       Calendar calendar = Calendar.getInstance();
       calendar.add(Calendar.MONTH,-12);
        Date time = calendar.getTime();
        System.out.println(time);

    }

    @Test
    public void testTime() {
         String name = "liuweiod";
        String[] split = name.split(",");
        System.out.println(split[0]);
    }
}
