package com.dept.web.general.util.excel;

import java.io.File;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.dept.web.general.util.ReflectUtils;
import com.dept.web.general.util.TimeUtil;

public class ExcelHelper {

	public static final String UID="serialVersionUID"; 
	private static Logger logger = Logger.getLogger(ExcelHelper.class);
	
	public static void writeExcel(String file,List list,Class clazz) throws Exception {
		Field[] fields=clazz.getDeclaredFields();
		List flist=new ArrayList();
		for(int i=0;i<fields.length;i++){
			if(fields[i].getName().equals(UID)){
				continue;
			}
			flist.add(fields[i].getName());
		}
		writeExcel(file,list,clazz,flist,null);
	}
	
	public static void writeExcel(String file,List list,Class clazz,List<String> fields,List<String> titles) throws Exception {
		OutputStream os=getOutputStream(file);
		jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(os);
		jxl.write.WritableSheet ws = wwb.createSheet("Sheet1", 0);
		jxl.write.Label label=null;
		int start=0;
		if(titles!=null&&titles.size()>0){
			for(int j=0;j<titles.size();j++){ 
				label=new jxl.write.Label(j,0,titles.get(j));
				ws.addCell(label);
			}
			start++;
		}
		for(int i=start;i<list.size()+start;i++){
			Object o=list.get(i-start);
			if(o==null){
				continue;
			}
			for(int j=0;j<fields.size();j++){
				String value="";
				if(fields.equals("serialVersionUID")){
					continue;
				}
				try {
					value=ReflectUtils.invokeGetMethod(clazz, o, fields.get(j)).toString();
					
					
					if(fields.get(j)!=null&&fields.get(j).equals("isDay")){
						value = (value.equals("1")) ? "是" : "否";
					}
					if(fields.get(j)!=null&&fields.get(j).equals("repOrder")){
						value = "第" + value + "期";
					}
					
					/** 充值列表：状态用汉字 */
					if(clazz.getName().endsWith("UserRecharge")){
						if(fields.get(j)!=null&&fields.get(j).equals("status")){
							if(value.equals("0")){
								value="失败";
							}else if(value.equals("1")){
								value="成功";
							}
						}
					}
					/** 提现列表：状态用汉字 */
					if(clazz.getName().endsWith("UserWithdraw")){
						if(fields.get(j)!=null&&fields.get(j).equals("status")){
							if(value.equals("3")){
								value="待审核";
							}else if(value.equals("5")){
								value="成功";
							}else{
								value="审核不通过";
							}
						}
					}
					
					/** 理财列表：状态用汉字 */
					if(clazz.getName().endsWith("Borrow")){
						if(fields.get(j)!=null&&fields.get(j).equals("repaymentStyle")){
							if(value.equals("1")){
								value="等额本息";
							}else if(value.equals("2")){
								value="到期还本还息";
							}else if(value.equals("3")){
								value="按月付息到期还本";
							}
						}
						if(fields.get(j)!=null&&fields.get(j).equals("borrowType")){
							if(value.equals("1")){
								value="新手标";
							}else if(value.equals("2")){
								value="月标";
							}else if(value.equals("3")){
								value="天标";
							}
						}
						if(fields.get(j)!=null&&fields.get(j).equals("validTime")){
							value = value +"天";
						}
						if(fields.get(j)!=null&&fields.get(j).equals("status")){
							if(value.equals("0")){
								value="等待初审";
							}else if(value.equals("1")){
								value="立即购买";
							}else if(value.equals("2")){
								value="满标待审";
							}else if(value.equals("3")){
								value="复审通过";
							}else if(value.equals("5")){
								value="还款中";
							}else if(value.equals("6")){
								value="已还款";
							}else if(value.equals("7")){
								value="已过期";
							}else if(value.equals("11")){
								value="已完成";
							}else if(value.equals("41")){
								value="初审不通过";
							}else if(value.equals("42")){
								value="复审不通过";
							}
						}
					}	
					
					/** 还款列表：状态用汉字 */
					if(clazz.getName().endsWith("BorrowRepayment")){
						if(fields.get(j)!=null&&fields.get(j).equals("status")){
							if(value.equals("0")){
								value="未还款";
							}else if(value.equals("1")){
								value="已还款";
							}
						}
						if(fields.get(j)!=null&&fields.get(j).equals("repayment_style")){
							if(value.equals("1")){
								value="等额本息";
							}else if(value.equals("2")){
								value="到期还本还息";
							}else if(value.equals("3")){
								value="按月付息到期还本";
							}
						}
						if(fields.get(j)!=null&&fields.get(j).equals("borrow_type")){
							if(value.equals("1")){
								value="微微金融 邦诺盈";
							}else if(value.equals("2")){
								value="微微金融 邦即盈";
							}else if(value.equals("3")){
								value="微微金融 邦稳盈";
							}else if(value.equals("4")){
								value="微微金融 邦安盈";
							}else if(value.equals("5")){
								value="微微金融 信托";
							}else if(value.equals("6")){
								value="微微金融 资管";
							}else{
								value="微微金融 基金";
							}
						}
						
					}
//					1  未使用 2 已返还 3 已过期 4 已使用
					if(clazz.getName().endsWith("HongbaoLog")){
						if(fields.get(j)!=null&&fields.get(j).equals("status")){
							if(value.equals("1")){
								value="未使用";
							}else if(value.equals("2")){
								value="已返还";
							}else if(value.equals("3")){
								value="已过期";
							}else if(value.equals("4")){
								value="已使用";
							}
						}if(fields.get(j)!=null&&fields.get(j).equals("htype")){
							if(value.equals("1")){
								value="注册送";
							}else{
								value="推荐送";
							}
						}
					}
					
				} catch (Exception e) {
					
				}
				if(fields.get(j)!=null&&isTime(fields.get(j))){
					if(value.isEmpty()){
						value="";
					}else{
						value=TimeUtil.getStr1(value);
					}
				}
				

				
				label=new jxl.write.Label(j,i, value); 
				ws.addCell(label);
			}
		}
		wwb.write();
		wwb.close();
	}
	
	public static List[] read(String xls) throws Exception {  
		List[] data=null; 
		File file=new File(xls);
		if(file.exists()){
			data=read(file);
		}
		return data;
	}
	
	public static List[] read(File file) throws Exception {  
		List[] data=null; 
        Workbook wb = null;  
        try {  
            wb = Workbook.getWorkbook(file);  
            if (wb != null) {  
                Sheet[] sheets = wb.getSheets();
                if (sheets != null && sheets.length >= 0) {  
                	int rows = sheets[0].getRows(); 
                	data=new List[rows]; 
                    for (int j=0;j<rows;j++) {  
                    	List<String> rowData=new ArrayList();
                        Cell[] cells = sheets[0].getRow(j);  
                        if (cells != null && cells.length != 0) { 
                            for (int k=0;k<cells.length;k++) {  
                                String cell = cells[k].getContents();
                                rowData.add(cell);
                            } 
                        }  
                        data[j]=rowData;
                    } 
                } 
            }  
        } catch (Exception e) {  
        	logger.info(e.getMessage());
        } finally {  
            wb.close();  
        }  
        return data;  
    }  
	
	
	private static boolean isTime(String field){
		String[] times=new String[]{"addtime","repay_time","verify_time","repay_yestime", "verifyTime","createdAt","updatedAt","createTime","repaymentTime"};
		boolean isTime=false;
		for(String s:times){
			if(s.equals(field)){
				isTime=true;
				break;
			}
		}
		return isTime;
	}
	
	public static OutputStream getOutputStream(String file) throws Exception{
		File f = new File(file);
		f.createNewFile();
		OutputStream os=new FileOutputStream(f);
		return os;
	}

}
