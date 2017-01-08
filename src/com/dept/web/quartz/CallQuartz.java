package com.dept.web.quartz;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dept.web.general.util.DateUtils;
import com.dept.web.quartz.vo.QuartzVo;

/**
 * 
 * @ClassName:     CallQuartz.java
 * @Description:   调用任务
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-9-4 下午4:13:50
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class CallQuartz {
    private static Logger logger = Logger.getLogger(CallQuartz.class);
    @Autowired 
    public void ReVerifyBorrow(){
        
        System.out.print("复审线程执行"+DateUtils.getNowTimeStr());

    }
    
    @Autowired 
    public void exRepay(){
        
        System.out.print("还款线程执行"+DateUtils.getNowTimeStr());

    }
    
    @Autowired 
    public void overdueBorrow(){
        
        System.out.print("过期产品线程执行"+DateUtils.getNowTimeStr());

    }
    
    /**
     * 获取任务
     * @return
     * @author xingle
     * @data 2014-8-14 下午12:59:58
     */
    private Map<String, List<QuartzVo>> getNewJobs() {
        //返回的map
//        Map<String,List<QuartzVo>> returnMap=new HashMap<String,List<QuartzVo>>();
//        List<QuartzVo> returnLs = new ArrayList<QuartzVo>();
//        //文件列表
//        List<QuartzVo> fileLs = new ArrayList<QuartzVo>();
//        List<String> fileNameLs = new ArrayList<String>();
//        BufferedReader ins = null;
//        File f = new File("/opt/qbm/test/tasklist1.txt");
//        try {
//            
//            int i = QuartzVo.Id++;
//            ins = new BufferedReader(new FileReader(f));
//            String line = "";
//            while ((line = ins.readLine()) != null) {
//                //增加一个是否执行标识，0 未执行
//                //line = line+"|0";
//                String[] task = line.split("\\|");
//                
//                QuartzVo quartzVo = new QuartzVo();
//                String id = "quarzJob_"+ i;
//                quartzVo.setId(id);
//                quartzVo.setJobTitle(task[0]);
//                quartzVo.setJcallpath(task[1]);
//                quartzVo.setJobcron(task[2]);
//                quartzVo.setS_date(task[3]);
//                quartzVo.setCycle(task[4]);
//                                
//                fileLs.add(quartzVo);
//                fileNameLs.add(quartzVo.getJobTitle());            
//        }
//            ins.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        String flag = "";        
//        int fileNum = fileLs.size();
//        int quarzNum = QuartzVo.quartzMap.size();
//        if(fileNum > quarzNum){
//            flag = "1";
//            for(int i =0;i<fileNum;i++){
//                QuartzVo fileVo = fileLs.get(i);
//                if(!QuartzVo.quartzMap.keySet().contains(fileVo.getJobTitle())){
//                    QuartzVo.quartzList.add(fileVo);
//                    //要增加的
//                    returnLs.add(fileVo);
//                }
//            }
//        }
//        else if(fileNum<quarzNum){
//            flag = "2";
//            for(int i = 0;i<QuartzVo.quartzList.size() ; i++){
//                if(!fileNameLs.contains(QuartzVo.quartzList.get(i).getJobTitle())){
//                    //需要要删除的任务
//                    returnLs.add(QuartzVo.quartzList.get(i));
//                }
//            }
//            for(int i = 0;i<QuartzVo.quartzList.size() ; i++){
//                QuartzVo vo = QuartzVo.quartzList.get(i);
//                for(int j = 0;j<returnLs.size();j++){
//                    if(vo.getId().equals(returnLs.get(j).getId())){
//                        QuartzVo.quartzList.remove(i);
//                    }
//                }
//            }
//        }
//        
//        returnMap.put(flag, returnLs);
//        return returnMap;
        return null;
    }

}