package com.dept.web.dao.model;

import java.util.List;


/**
 * 
 * 
 * @ClassName:     AjaxJsonStr
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月28日 下午5:50:08 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class AjaxJsonStr {

    private int status;
    
    private String msg;
    
    private List<AjaxData> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<AjaxData> getData() {
        return data;
    }

    public void setData(List<AjaxData> data) {
        this.data = data;
    }


    
    
}
