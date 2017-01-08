package com.dept.web.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.dept.web.quartz.vo.QuartzVo;




/**
 * 
 * @ClassName:     QuartzManager.java
 * @Description:   任务管理
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-9-4 下午4:13:14
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class QuartzManager {
    private static Logger logger = Logger.getLogger(QuartzManager.class);
    
    //Create an uninitialized StdSchedulerFactory.
    private static SchedulerFactory sf = new StdSchedulerFactory();
    
    private static String TRIGGER_GROUP_NAME = "quartzTrigger";
    
    /**
     * 添加任务
     * @param jobName
     * @param job
     * @param time
     * @author xingle
     * @data 2014-8-14 下午7:45:09
     */
    public static void addJob(String jobName,QuartzVo job,String time){        
        try {
            Scheduler scheduler = sf.getScheduler();
            
            //任务名，任务组，任务执行类
            JobDetail jobDetail = new JobDetail(jobName, jobName, job.getClass());

            if("1".equals(job.getCycle())){//循环
                CronTrigger trigger = new CronTrigger(jobName, jobName);
                trigger.setCronExpression(time);
                scheduler.scheduleJob(jobDetail, trigger);
            }
            else{//单次
                String s_Date = job.getS_date(); 
                //logger.debug("*****时间："+s_Date);
                
                SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                Date startTime = formate.parse(s_Date);    
                //logger.debug("*****时间："+startTime);
                SimpleTrigger trigger = new SimpleTrigger(jobName, jobName, startTime);
                scheduler.scheduleJob(jobDetail, trigger);    
            }
            if(!scheduler.isShutdown()){
                scheduler.start();
            }
            //logger.debug("*********【添加】定时任务【"+jobName+"】 加载完成！*****************");
            
            
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除任务
     * @param jobName
     * @author xingle
     * @data 2014-8-14 下午7:45:16
     */
    public static void removeJob(String jobName){
         try {
            Scheduler sched = sf.getScheduler();
            sched.pauseTrigger(jobName,jobName);//停止触发器
            sched.unscheduleJob(jobName,jobName);//移除触发器
            sched.deleteJob(jobName, jobName);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
         //logger.debug("*********定时任务【"+jobName+"】 已删除完成！*****************");
    }
     
}