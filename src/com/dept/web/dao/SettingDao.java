package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Setting;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class SettingDao extends IbatisBaseDaoImpl<Setting, Long>{

    private static final String NAME_SPACE_SETTING = "Setting";
    
    public List<Setting> queryAll(){
        
        return getObjList(NAME_SPACE_SETTING, null, "ALL");
    }
    
    public Setting queryById(long id){
        
        return getObj(NAME_SPACE_SETTING, id, "ID");
    }
    
    public String getValueByName(String name){
    	
    	Object o = getObj(NAME_SPACE_SETTING,name,"GETVALUEBYNAME");
    	if(o!=null){
    		return o.toString();
    	}else{
    		return "";
    	}
    }

}
