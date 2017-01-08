 package com.dept.web.quartz.vo;
 
 import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
 
 
 /**
  * 
  * @ClassName:     QuartzVo.java
  * @Description:   执行任务
  *
  * @author         cannavaro
  * @version        V1.0 
  * @Date           2014-9-4 下午4:14:21
  * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
  */
 public class QuartzVo implements Job{
     
     public static int Id = 0;
     
     private static Logger logger = Logger.getLogger(QuartzVo.class);
     //缓存中任务列表
     public static List<QuartzVo> quartzList = new ArrayList<QuartzVo>();
     //缓存中任务map
     public static Map<String, QuartzVo> quartzMap = new HashMap<String, QuartzVo>();
     
         
     /**
      * id
      */
     public String id ;
     /**
      * 任务名称
      */
     public String jobTitle;
     /**
      * 调度路径
      */
     public String  jcallpath;
     /**
      * 触发表达式
      */
     public String jobcron;
     /**
     * @Fields s_date : 开始时间
     */
     public String s_date;
     /**
     * @Fields cycle : 循环标示：1 循环；2 单次
     */
     public String cycle;
     
     public String getId() {
         return id;
     }
     public void setId(String id) {
         this.id = id;
     }
     public String getJobTitle() {
         return jobTitle;
     }
     public void setJobTitle(String jobTitle) {
         this.jobTitle = jobTitle;
     }
     public String getJcallpath() {
         return jcallpath;
     }
     public void setJcallpath(String jcallpath) {
         this.jcallpath = jcallpath;
     }
     public String getJobcron() {
         return jobcron;
     }
     public void setJobcron(String jobcron) {
         this.jobcron = jobcron;
     }
     public String getCycle() {
         return cycle;
     }
     public void setCycle(String cycle) {
         this.cycle = cycle;
     }
     
     public String getS_date() {
         return s_date;
     }
     public void setS_date(String s_date) {
         this.s_date = s_date;
     }

     @Override
     public void execute(JobExecutionContext context) throws JobExecutionException {
         String jobName = context.getJobDetail().getName();
//         logger.debug("定时任务【" + jobName + "】 将要执行 start！！");
         QuartzVo quartzVo = QuartzVo.quartzMap.get(jobName);
         String inurl = quartzVo.getJcallpath();
         URL url = null;
         HttpURLConnection conn = null;
         try {
             url = new URL(inurl);
             conn = (HttpURLConnection) url.openConnection();
         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
//             System.out.println("***************** 连接失败,程序地址 : " + inurl);
             e.printStackTrace();
         }
         try {
             if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                 System.out.println("****************** 调度失败!!,程序地址 : " + inurl);
             } else {
//                 System.out.println("定时任务【" + jobName + "】" + "已完成调度，程序地址:inurl);
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         
     }
     
     
// 	/**
// 	 * 
// 	 * @Description:  获取当前期号
// 	 * @param:        @return   
// 	 * @return:       String   
// 	 * @throws
// 	 */
// 	public static String getSaleIssue() {
// 		
// 		 String responseMsg = "";
// 			
// 		 HttpClient httpClient = new HttpClient();
// 		 
// 		 String url="http://localhost:8080/apiweb/getissue.do";
// 		 
// 		 GetMethod getMethod = new GetMethod(url);
// 		 getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
// 		 
// 		 try {
// 			
// 			 httpClient.executeMethod(getMethod);
// 			 byte[] responseBody = getMethod.getResponseBody();
// 			 responseMsg = new String(responseBody);
// 			 
// 			 return responseMsg ;
// 			 
// 		} catch (HttpException e) {
// 			e.printStackTrace();
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		} finally{
// 			getMethod.releaseConnection();
// 		}
// 		 
// 		 return null;
// 	}

 
 }