package com.dept.web.general.util;

import java.math.BigDecimal;

/**
 * 项目统一金额计算类
 * 
 * @author Administrator
 *
 */
public class MoneyUtils {
	
	
	/*-----------舍入模式说明-----------*/
	/**
	 * (进一法) ROUND_UP //总是在非 0 舍弃小数(即截断)之前增加数字。
	 */
	/**
	 * (去尾法) ROUND_DOWN 从不在舍弃(即截断)的小数之前增加数字。
	 */
	/**
	 * (正数进一法,负数去尾法) ROUND_CEILING 如果 BigDecimal 是正的，则做 ROUND_UP 操作；如果为负，则做
	 * ROUND_DOWN 操作。
	 */
	/**
	 * (正数去尾法,负数进一法) ROUND_FLOOR 如果 BigDecimal 为正，则作 ROUND_DOWN ；如果为负，则作
	 * ROUND_UP 。
	 */
	/**
	 * (四舍五入法) ROUND_HALF_UP 若舍弃部分>=.5，则作 ROUND_UP ；否则，作 ROUND_DOWN 。
	 */
	/**
	 * (大于5入,小于等于舍去)(五舍六入)//区别四舍五入为等于5的情况 ROUND_HALF_DOWN 若舍弃部分> .5，则作
	 * ROUND_UP；否则，作 ROUND_DOWN 。
	 */
	/**
	 * 银行家舍入法:(四舍六入,五分两种情况:如果前一位为奇数，则入位，否则舍去) ROUND_HALF_EVEN 如果舍弃部分左边的数字为奇数，则作
	 * ROUND_HALF_UP；如果它为偶数，则作ROUND_HALF_DOWN。
	 */
	/**
	 * ROUND_UNNECESSARY 该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。
	 */

	// 默认运算精度(小数点后保留位数)
	private static final int DEF_DIV_SCALE = BigDecimal.ROUND_FLOOR;

	// 保留小数位数
	private static final int DEF_LENGTH = 2;

	/**
	 * 加法
	 * 
	 * @param x
	 * @param y
	 * @return x+y的值
	 */
	public static double plus(double x, double y) {
		return plus(x, y, DEF_LENGTH, DEF_DIV_SCALE);
	}

	/**
	 * 减法
	 * 
	 * @param x
	 *            被减数
	 * @param y
	 *            减数
	 * @return x-y的值
	 */
	public static double subtract(double x, double y) {
		return subtract(x, y, DEF_LENGTH, DEF_DIV_SCALE);
	}

	/**
	 * 乘法
	 * 
	 * @param x
	 * @param y
	 * @return x*y的值
	 */
	public static double multiplication(double x, double y) {
		return multiplication(x, y, DEF_LENGTH, DEF_DIV_SCALE);
	}

	/**
	 * 除法
	 * 
	 * @param x
	 *            被除数
	 * @param y
	 *            除数
	 * @return x/y 后保留的值
	 */
	public static double division(double x, double y) {
		return division(x, y, DEF_LENGTH, DEF_DIV_SCALE);
	}
	
	/**
	 * 除法
	 * 
	 * @param x
	 *            被除数
	 * @param y
	 *            除数
	 * @return x/y 后保留的值
	 */
	public static double division(double x, double y,int len) {
		return division(x, y, len, DEF_DIV_SCALE);
	}

	/**
	 * 加法
	 * 
	 * @param x
	 *            被加数
	 * @param y
	 *            加数
	 * @param scale
	 *            精度
	 * @param roundingMode
	 *            舍入模式
	 * @return x+y 后保留的值
	 */
	public static double plus(double x, double y, int scale, int roundingMode) {
		BigDecimal val1 = BigDecimal.valueOf(x);
		BigDecimal val2 = BigDecimal.valueOf(y);
		BigDecimal val3 = val1.add(val2).setScale(scale, roundingMode);
		return val3.doubleValue();
	}
	
	/**
	 * 减法
	 * 
	 * @param x
	 *            被减数
	 * @param y
	 *            减数
	 * @param scale
	 *            精度
	 * @param roundingMode
	 *            舍入模式
	 * @return x-y 后保留的值
	 */
	public static double subtract(double x, double y, int scale, int roundingMode) {
		BigDecimal val1 = BigDecimal.valueOf(x);
		BigDecimal val2 = BigDecimal.valueOf(y);
		BigDecimal val3 = val1.subtract(val2).setScale(scale, roundingMode);
		return val3.doubleValue();
	}
	
	/**
	 * 加法
	 * 
	 * @param x
	 *            被乘数
	 * @param y
	 *            乘数
	 * @param scale
	 *            精度
	 * @param roundingMode
	 *            舍入模式
	 * @return x*y 后保留的值
	 */
	public static double multiplication(double x, double y, int scale, int roundingMode) {
		BigDecimal val1 = BigDecimal.valueOf(x);
		BigDecimal val2 = BigDecimal.valueOf(y);
		BigDecimal val3 = val1.multiply(val2).setScale(scale, roundingMode);
		return val3.doubleValue();
	}
	
	/**
	 * 除法
	 * 
	 * @param x
	 *            被除数
	 * @param y
	 *            除数
	 * @param scale
	 *            精度
	 * @param roundingMode
	 *            舍入模式
	 * @return x/y 后保留的值
	 */
	public static double division(double x, double y, int scale, int roundingMode) {
		if (y < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal val1 = BigDecimal.valueOf(x);
		BigDecimal val2 = BigDecimal.valueOf(y);
		BigDecimal val3 = val1.divide(val2, scale, roundingMode);
		return val3.doubleValue();
	}
	
	/**
	 * 利息计算-按天
	 * @param money 投资金额
	 * @param apr 年华收益(不除以100) 如:年华收益7.5%传入 7.5即可
	 * @param timeLimit 投资期限
	 * @return
	 */
	public static double interestDay(double money, double apr, int timeLimit){
		return multiplication(division(multiplication(money, apr),36500,10), timeLimit);
	}
	
	/**
	 * 利息计算-按月
	 * @param money 投资金额
	 * @param apr 年华收益(不除以100) 如:年华收益7.5%传入 7.5即可
	 * @param timeLimit 投资期限
	 * @return
	 */
	public static double interestMonth(double money, double apr, int timeLimit){
		return multiplication(division(multiplication(money, apr),1200,10), timeLimit);
	}

	public static void main(String[] args) {
		System.out.println(interestMonth(3000, 12, 6));
	}
}
