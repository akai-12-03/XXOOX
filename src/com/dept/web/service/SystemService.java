package com.dept.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.SettingDao;
import com.dept.web.dao.model.Setting;
import com.dept.web.dao.model.SystemInfo;

@Service
public class SystemService {
	
	@Autowired
	private SettingDao settingDao;
	
	public SystemInfo getSystemInfo(){
		SystemInfo info = new SystemInfo();
		List<Setting> list = settingDao.queryAll();
		for (int i = 0; i < list.size(); i++) {
		    Setting sys = (Setting) list.get(i);
			info.addConfig(sys);
		}
		return info;
	}
	
	/**
	 * 
	 * @Title: querySettinglist 
	 * @Description: TODO
	 * @param @return 设定文件 
	 * @return List<Setting> 返回类型 
	 * @throws
	 */
	public List<Setting> querySettinglist(){
	    
	    return settingDao.queryAll();
	    
	}
	
	/**
	 * 
	 * @Title: querySettingById 
	 * @Description: TODO
	 * @param @param id
	 * @param @return 设定文件 
	 * @return Setting 返回类型 
	 * @throws
	 */
	public Setting querySettingById(long id){
	    
	    return settingDao.queryById(id);
	}
	
	/**
	 * 
	 * @Title: updateSetting 
	 * @Description: TODO
	 * @param @param set
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int updateSetting(Setting set){
	    
	    return settingDao.update(set);
	}
}
