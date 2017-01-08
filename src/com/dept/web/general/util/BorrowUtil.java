package com.dept.web.general.util;

import java.util.Date;

import com.dept.web.context.Constant;
import com.dept.web.dao.model.Borrow;
import com.dept.web.general.interest.EndInterestCalculator;
import com.dept.web.general.interest.InterestCalculator;
import com.dept.web.general.interest.MonthEqualCalculator;
import com.dept.web.general.interest.MonthInterestCalculator;

/**
 * 
 * 
 * @ClassName:     BorrowUtil
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月25日 下午3:42:54 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowUtil {

    /**
     * 计算还款时间
     * @Title: getRepayTime 
     * @Description: TODO
     * @param @param model
     * @param @param period
     * @param @return 设定文件 
     * @return Date 返回类型 
     * @throws
     */
    public static Date getRepayTime(Borrow model,int period) {
        Date d=DateUtils.getDate(model.getVerifyTime());
        Date repayDate=DateUtils.getLastSecIntegralTime(d);
        if(model.getIsDay()==1){
            repayDate=DateUtils.rollDay(repayDate, model.getTimeLimit());
            return repayDate;
        }else{
            //一次性还款
            if(model.getRepaymentStyle()==Constant.BORROW_REPAYMENT_STYLE_DQHBHX){
                repayDate=DateUtils.rollMon(repayDate, model.getTimeLimit());
            }else{
                repayDate=DateUtils.rollMon(repayDate, period);
            }
            return repayDate;
        }
    }
    
    
    /**
     * 
     * @Description:  计算还款利息
     * @param:        @param money  借款金额
     * @param:        @param apr    利率
     * @param:        @param timelimit  期限
     * @param:        @param style  还款类型
     * @param:        @return   
     * @return:       InterestCalculator   
     * @throws
     */
    public static InterestCalculator getInterestCalculator(double money,double apr, int timelimit,int style){

        InterestCalculator ic = null;
        
        if(style==Constant.BORROW_REPAYMENT_STYLE_DQHBHX){
            ic =new EndInterestCalculator(money,apr,timelimit,InterestCalculator.TYPE_MONTH_END);
            
        }else if(style==Constant.BORROW_REPAYMENT_STYLE_MYHX){
            ic =new MonthInterestCalculator(money,apr,timelimit);
        }else{
            ic =new MonthEqualCalculator(money,apr,timelimit);
        }
        
        ic.each();
        
        return ic;
    } 
	
}
