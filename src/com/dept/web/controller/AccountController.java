package com.dept.web.controller;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.dept.web.context.Global;
import com.dept.web.controller.sq.utils.Common;
import com.dept.web.controller.sq.utils.HttpClientUtil;
import com.dept.web.controller.sq.utils.RsaHelper;
import com.dept.web.dao.BankCardDao;
import com.dept.web.dao.BankDao;
import com.dept.web.dao.MarketDao;
import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.Bank;
import com.dept.web.dao.model.BankCard;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.HongbaoLog;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.dao.model.UserWithdraw;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.ExportExcel;
import com.dept.web.general.util.NewsDateUtils;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.AccountService;
import com.dept.web.service.HongbaoLogService;
import com.dept.web.service.UserService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 账户相关
 * 
 * @ClassName: AccountController
 * @Description:
 *
 * @author cannavaro
 * @version V1.0
 * @Date 2015年4月26日 上午2:45:21 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class AccountController extends WebController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private HongbaoLogService hongbaoLogService;

	@Autowired
	private BankDao bankDao;

	@Autowired
	private BankCardDao bankCardDao;
	
	@Autowired
	private MarketDao marketDao;

	@RequestMapping("admin/upInvite")
	public String upInvite(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, String inviteId, Long userId)
			throws Exception {
		// 首先判断该用户和推荐人是否存在
		if (inviteId != null) {
			User inviteUser = userService.queryByUserName(inviteId);
			if (inviteUser != null) {
				// 说明推荐人是存在 根据用户iD修改推荐人id
				int i = userService.updateInviteIdByUserId(userId,
						inviteUser.getId());
				if (i >= 0) {
					out(response, 1);// 修改成功
				} else {
					out(response, 2);// 修改失败
				}
			} else {
				out(response, 3);// 3推荐人不存在 请重新填写
			}
		}

		return null;
	}

	/**
	 * 提现列表
	 * 
	 * @Title: withdraw_list
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/withdraw_list")
	public String withdraw_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		try {
			map.addAttribute("msg", request.getParameter("msg"));
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);

			if (StringUtil.isNotEmpty(params.get("page"))) {

				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}

			if (StringUtils.isNotEmpty(params.get("startTime"))) {

				String startTime = params.get("startTime");

				params.put("startTime", startTime);
			}

			if (StringUtils.isNotEmpty(params.get("endTime"))) {

				String endTime = params.get("endTime");

				params.put("endTime", endTime);
			}

			pageRequest.setFilters(params);

			Page<UserWithdraw> withdrawpage = accountService
					.queryUserWithdraw(pageRequest);

			map.addAttribute("withdrawpage", withdrawpage);

			map.addAttribute(
					"totalPage",
					PageUtils.computeLastPageNumber(
							withdrawpage.getTotalCount(),
							withdrawpage.getPageSize()));

			map.addAttribute("page", pageRequest.getPageNumber());

			map.addAttribute("status", params.get("status"));
			map.addAttribute("orderId", params.get("orderId"));
			map.addAttribute("realname", params.get("realname"));
			map.addAttribute("startTime", params.get("startTime"));
			map.addAttribute("endTime", params.get("endTime"));
			map.addAttribute("username", params.get("username"));
		} catch (Exception e) {

			e.printStackTrace();
		}

		return "account/withdraw_list";

	}

	/**
	 * 审核提现
	 * 
	 * @Title: withdraw_verify
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @param wid
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/withdraw_verify")
	public String withdraw_verify(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam long wid)
			throws Exception {

		AdminUser adminuser = getCurrUser(request, response);

		Map<String, String> params = getParamMap(request);

		UserWithdraw uw = accountService.queryWithdrawById(wid);

		UserAccount ua = accountService.queryAccountByUser(uw.getCreatedBy());

		// BankCard bankcard = userService.queryUserBank(uw.getCreatedBy());

		map.addAttribute("uw", uw);

		map.addAttribute("ua", ua);

		// map.addAttribute("bankcard", bankcard);

		map.addAttribute("msg", params.get("msg"));

		if (StringUtils.isEmpty(params.get("opt"))) {

			map.addAttribute("wid", String.valueOf(wid));

			map.addAttribute("uw", uw);

			map.addAttribute("ua", ua);

			// map.addAttribute("bankcard", bankcard);

			return "account/view_withdraw";

		} else if (params.get("opt").equals("upd")) {

			if (uw.getStatus() != 0) {

				map.addAttribute("msg", "该提现申请已经被审核过");

				map.addAttribute("wid", String.valueOf(wid));

				map.addAttribute("uw", uw);

				map.addAttribute("ua", ua);

				// map.addAttribute("bankcard", bankcard);

				return "redirect:withdraw_verify.html";
			}

			String verifystatus = params.get("status");

			String verifyremark = params.get("remark");

			if (ua.getMoneyWithdraw() >= uw.getMoneyWithdraw()) {

				if (StringUtils.isNotEmpty(verifystatus)
						&& StringUtils.isNotEmpty(verifyremark)) {
					String LoanNoList = uw.getOrderId();
					String PlatformMoneymoremore = Global
							.getValue("qdd_PlatformMoneymoremore");
					String Remark1 = "提现审核";
					String Remark2 = "";
					String Remark3 = String.valueOf(adminuser.getId());
					String AuditType = "";
					String SubmitURLPrefix = Global.getValue("qdd_submitUrl");
					String SubmitURL = "";
					String myProject = Common.myProject;
					if (myProject.equals("test")) {
						SubmitURL = SubmitURLPrefix
								+ "/loan/toloantransferaudit.action";
					} else {
						SubmitURL = "https://audit." + SubmitURLPrefix
								+ "/loan/toloantransferaudit.action";
					}
					String ReturnURL = Global.getValue("qdd_notifyCmsUrl")
							+ "/qddApi/LoanTransferAuditReturn.html";
					String NotifyURL = Global.getValue("qdd_notifyCmsUrl")
							+ "/qddApi/LoanTransferAuditNotify.html";

					String privatekey = Common.privateKeyPKCS8;
					//
					if (Integer.valueOf(verifystatus) == 2) {// 审核不通过
						AuditType = "6";
					} else {
						AuditType = "5";
					}
					String dataStr = LoanNoList + PlatformMoneymoremore
							+ AuditType + Remark1 + Remark2 + Remark3
							+ ReturnURL + NotifyURL;
					System.out.println("dataStr===========" + dataStr);
					// 签名
					RsaHelper rsa = RsaHelper.getInstance();
					String SignInfo = rsa.signData(dataStr, privatekey);
					System.out.println("SignInfo===========" + SignInfo);
					Map<String, String> req = new HashMap<String, String>();
					req.put("LoanNoList", LoanNoList);
					req.put("PlatformMoneymoremore", PlatformMoneymoremore);
					req.put("AuditType", AuditType);
					req.put("Remark1", Remark1);
					req.put("Remark2", Remark2);
					req.put("Remark3", Remark3);
					req.put("ReturnURL", ReturnURL);
					req.put("NotifyURL", NotifyURL);
					req.put("SignInfo", SignInfo);
					uw.setStatus(0);
					uw.setRemark(verifyremark);
					uw.setUpdatedAt(DateUtils.getNowTimeStr().toString());
					uw.setUpdatedBy(adminuser.getId());
					accountService.verifyWithDraw(uw);
					String[] result = HttpClientUtil.doPostQueryCmd(SubmitURL,
							req);
					System.out.println("result0=====" + result[0]);
					System.out.println("result1=====" + result[1]);
					JSONObject obj = JSONObject.parseObject(result[1]);
					String Message = obj.getString("Message");
					map.addAttribute("msg", Message);
					return "redirect:/admin/withdraw_list.html";
				} else {

					map.addAttribute("msg", "请输入备注");

					map.addAttribute("wid", String.valueOf(wid));

					map.addAttribute("uw", uw);

					map.addAttribute("ua", ua);

					// map.addAttribute("bankcard", bankcard);

					return "redirect:withdraw_verify.html";
				}
			} else {

				map.addAttribute("msg", "冻结金额不足");

				map.addAttribute("wid", String.valueOf(wid));

				map.addAttribute("uw", uw);

				map.addAttribute("ua", ua);

				// map.addAttribute("bankcard", bankcard);

				return "redirect:withdraw_verify.html";
			}
		}

		return null;

	}

	/**
	 * 导出excel
	 * 
	 * @Title: recharge_list
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account_excel")
	public String account_excel(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);

		String eType = params.get("eType");

		// 根据用户搜索信息导出excel记录
		try {

			if (StringUtils.isNotEmpty(params.get("startTime"))) {

				String startTime = params.get("startTime").replace("/", "-");

				params.put("startTime", startTime);
			}

			if (StringUtils.isNotEmpty(params.get("endTime"))) {

				String endTime = params.get("endTime").replace("/", "-");

				params.put("endTime", endTime);
			}

			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			// 下面是对中文文件名的处理
			response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式

			if (eType.equals("cz")) {
				params.put("status", params.get("status"));
				ExportExcel<UserRecharge> ex2 = new ExportExcel<UserRecharge>();
				String[] alias = { "编号", "用户名", "真实姓名", "充值金额(元)", "订单号",
						"充值时间", "充值状态", "支付平台" };// excel的列头
				String[] names = { "id", "username", "realname",
						"moneyRecharge", "orderId", "updatedAt", "statusStr",
						"paySourceStr" };
				List<UserRecharge> rechargeList = accountService
						.queryListUserRecharge(params);
				UserRecharge usr;
				for (int i = 0; i < rechargeList.size(); i++) {
					usr = rechargeList.get(i);
					if (usr != null) {
						usr.setCreatedAt(DateUtils.dateStr2(usr.getCreatedAt(),
								"yyyy-MM-dd hh:mm:ss"));
						usr.setUpdatedAt(DateUtils.dateStr2(usr.getUpdatedAt(),
								"yyyy-MM-dd hh:mm:ss"));
						usr.setStatusStr(usr.getStatus());
						usr.setPaySourceStr(usr.getPaySource());
						rechargeList.set(i, usr);
					}
				}
				String fname = java.net.URLEncoder.encode("充值记录表", "UTF-8");
				response.setHeader(
						"Content-Disposition",
						"attachment;filename="
								+ new String(fname.getBytes("UTF-8"), "GBK")
								+ ".xls");
				response.setContentType("application/msexcel");// 定义输出类型
				ex2.exportExcel(alias, rechargeList, os, names);
				// ExcelUtil.exportExcel(rechargeList, alias, "充值记录表", 60000,
				// os);
				os.flush();
				os.close();
			} else if (eType.equals("tx")) {
				ExportExcel<UserWithdraw> ex2 = new ExportExcel<UserWithdraw>();
				String[] alias = { "编号", "用户名", "真实姓名", "提现金额(元)", "提现申请时间",
						"审核人", "审核时间", "审核备注", "状态" };// excel的列头
				String[] names = { "id", "username", "realname",
						"moneyWithdraw", "createdAt", "updatedByUsername",
						"updatedAt", "remark", "statusStr" };
				List<UserWithdraw> userWithdrawList = accountService
						.queryListUserWithdraw(params);
				UserWithdraw usr;
				for (int i = 0; i < userWithdrawList.size(); i++) {
					usr = userWithdrawList.get(i);
					if (usr != null) {
						usr.setCreatedAt(DateUtils.dateStr2(usr.getCreatedAt(),
								"yyyy-MM-dd hh:mm:ss"));
						usr.setUpdatedAt(DateUtils.dateStr2(usr.getUpdatedAt(),
								"yyyy-MM-dd hh:mm:ss"));
						usr.setStatusStr(usr.getStatus());
						userWithdrawList.set(i, usr);
					}
				}
				String fname = java.net.URLEncoder.encode("提现记录表", "UTF-8");
				response.setHeader(
						"Content-Disposition",
						"attachment;filename="
								+ new String(fname.getBytes("UTF-8"), "GBK")
								+ ".xls");
				response.setContentType("application/msexcel");// 定义输出类型
				ex2.exportExcel(alias, userWithdrawList, os, names);
				// ExcelUtil.exportExcel(rechargeList, alias, "充值记录表", 60000,
				// os);
				os.flush();
				os.close();
			} else if (eType.equals("hb")) {
				ExportExcel<Hongbao> ex2 = new ExportExcel<Hongbao>();
				String[] alias = { "编号", "用户名", "项目名称", "红包类型", "红包状态",
						"红包金额(元)", "红包发放时间", "红包过期时间", "红包使用时间" };// excel的列头
				String[] names = { "id", "username", "name", "type", "status",
						"money", "updatetime", "endtime", "usetime" };
				List<Hongbao> hongbaoList = userService
						.queryHongbaoList(params);
				Hongbao usr;
				for (int i = 0; i < hongbaoList.size(); i++) {
					usr = hongbaoList.get(i);
					if (usr != null) {
						usr.setId(Long.valueOf(i) + 1);
						usr.setAddtime(DateUtils.dateStr2(usr.getAddtime(),
								"yyyy-MM-dd hh:mm:ss"));
						usr.setUpdatetime(DateUtils.dateStr2(
								usr.getUpdatetime(), "yyyy-MM-dd hh:mm:ss"));
						usr.setUsetime(DateUtils.dateStr2(usr.getUsetime(),
								"yyyy-MM-dd hh:mm:ss"));
						usr.setEndtime(DateUtils.dateStr2(usr.getEndtime(),
								"yyyy-MM-dd hh:mm:ss"));
						usr.setStatusStr(usr.getStatus());
						usr.setTypeStr(usr.getType());
						hongbaoList.set(i, usr);
					}
				}
				String fname = java.net.URLEncoder.encode("红包使用记录表", "UTF-8");
				response.setHeader(
						"Content-Disposition",
						"attachment;filename="
								+ new String(fname.getBytes("UTF-8"), "GBK")
								+ ".xls");
				response.setContentType("application/msexcel");// 定义输出类型
				ex2.exportExcel(alias, hongbaoList, os, names);
				os.flush();
				os.close();
				map.addAttribute("type", params.get("type"));
			} else if (eType.equals("card")) {
				ExportExcel<BankCard> ex2 = new ExportExcel<BankCard>();
				String[] alias = { "编号", "用户名","真实姓名", "所属银行", "银行卡", "添加时间" };// excel的列头
				String[] names = { "id", "username", "realname", "bankName",
						"cardNo", "createdAtName" };
				List<BankCard> bankCardList = accountService
						.queryBankCard(params);
				BankCard bankCard;
				for (int i = 0; i < bankCardList.size(); i++) {
					bankCard = bankCardList.get(i);
					if (bankCard != null) {
						bankCard.setCreatedAtName(DateUtils.dateStr(bankCard.getCreatedAt(),
								"yyyy-MM-dd hh:mm:ss"));
						
						
					}
				}
				String fname = java.net.URLEncoder.encode("用户银行卡列表", "UTF-8");
				response.setHeader(
						"Content-Disposition",
						"attachment;filename="
								+ new String(fname.getBytes("UTF-8"), "GBK")
								+ ".xls");
				response.setContentType("application/msexcel");// 定义输出类型
				ex2.exportExcel(alias, bankCardList, os, names);
				// ExcelUtil.exportExcel(rechargeList, alias, "充值记录表", 60000,
				// os);
				os.flush();
				os.close();
			}

			map.addAttribute("status", params.get("status"));
			map.addAttribute("realname", params.get("realname"));
			map.addAttribute("startTime", params.get("startTime"));
			map.addAttribute("endTime", params.get("endTime"));
			map.addAttribute("username", params.get("username"));

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 充值列表
	 * 
	 * @Title: recharge_list
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/recharge_list")
	public String recharge_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		try {

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);

			if (StringUtil.isNotEmpty(params.get("page"))) {

				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}

			if (StringUtils.isNotEmpty(params.get("status"))) {

				String status = params.get("status");

				params.put("status", status);
			}

			if (StringUtils.isNotEmpty(params.get("startTime"))) {

				String startTime = params.get("startTime");

				params.put("startTime", startTime);
			}

			if (StringUtils.isNotEmpty(params.get("endTime"))) {

				String endTime = params.get("endTime");

				params.put("endTime", endTime);
			}

			pageRequest.setFilters(params);

			Page<UserRecharge> rechargepage = accountService
					.queryUserRecharge(pageRequest);

			map.addAttribute("rechargepage", rechargepage);

			map.addAttribute(
					"totalPage",
					PageUtils.computeLastPageNumber(
							rechargepage.getTotalCount(),
							rechargepage.getPageSize()));

			map.addAttribute("page", pageRequest.getPageNumber());

			map.addAttribute("status", params.get("status"));
			map.addAttribute("orderNo", params.get("orderNo"));
			map.addAttribute("realname", params.get("realname"));
			map.addAttribute("startTime", params.get("startTime"));
			map.addAttribute("endTime", params.get("endTime"));
			map.addAttribute("username", params.get("username"));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "account/recharge_list";
	}

	/**
	 * 用户资金记录
	 * 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account/account_list")
	public String account_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		map.addAttribute("msg", params.get("msg"));

		try {

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);

			if (StringUtil.isNotEmpty(params.get("page"))) {

				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}

			pageRequest.setFilters(params);

			Page<UserAccountLog> accountLog = accountService
					.queryUserAccountLogPage(pageRequest);

			map.addAttribute("accountLog", accountLog);

			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(
					accountLog.getTotalCount(), accountLog.getPageSize()));

			map.addAttribute("page", pageRequest.getPageNumber());

			map.addAttribute("username", params.get("username"));

			map.addAttribute("realname", params.get("realname"));

			map.addAttribute("startTime", params.get("startTime"));

			map.addAttribute("endTime", params.get("endTime"));

			map.addAttribute("status", params.get("status"));

			return "account/account_list";

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 线下充值第一步查找用户
	 * 
	 * @Title: charge_offline
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account/charge_offline")
	public String charge_offline(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		String type = params.get("opt");

		String uid = params.get("uid");

		map.addAttribute("msg", params.get("msg"));
		if (StringUtils.isEmpty(type)) {

			try {

				PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
				populate(pageRequest, request);
				pageRequest.setPageSize(10);

				if (StringUtil.isNotEmpty(params.get("page"))) {

					pageRequest.setPageNumber(Integer.valueOf(params
							.get("page")));
				}

				pageRequest.setFilters(params);

				Page<UserAccount> accountpage = accountService
						.queryUserAccountPage(pageRequest);

				map.addAttribute("accountpage", accountpage);

				map.addAttribute(
						"totalPage",
						PageUtils.computeLastPageNumber(
								accountpage.getTotalCount(),
								accountpage.getPageSize()));

				map.addAttribute("page", pageRequest.getPageNumber());

				map.addAttribute("username", params.get("username"));

				map.addAttribute("mobile", params.get("mobile"));

				map.addAttribute("userId", params.get("userId"));

				map.addAttribute("realname", params.get("realname"));

				return "account/member_list";

			} catch (Exception e) {

				e.printStackTrace();
			}
		} else if (type.equals("view") && StringUtils.isNotEmpty(uid)) {

			UserAccount acc = accountService.queryAccountByUser(Long
					.valueOf(uid));

			map.addAttribute("account", acc);

			return "account/offline_recharge_view";

		} else if (type.equals("add") && StringUtils.isNotEmpty(uid)) {

			UserRecharge ur = new UserRecharge();

			Random random = new Random(System.currentTimeMillis());
			int iRandom = random.nextInt(10000) + 10000000;
			String out_trade_no = "XAZP"
					+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
					+ "-" + String.valueOf(iRandom) + "-" + uid;

			String money = params.get("offline_money");

			String webname = Global.getValue("webname");
			AdminUser currUser = this.getCurrUser(request, response);
			map.addAttribute("currUser", currUser);
			ur.setOrderId(out_trade_no);
			ur.setAccount(webname);
			ur.setCardNo("xxxxxx");
			ur.setMoneyRecharge(Double.valueOf(money));
			ur.setRemark(webname + "通过线下充值" + money + "元" + "由ID为"
					+ currUser.getId() + "用户充值");
			ur.setThirdPlatform(2L);
			ur.setThirdPlatformOrderId(3L);
			ur.setPaySource("Offline");
			ur.setStatus(0);
			ur.setCreatedBy(Long.valueOf(uid));
			ur.setCreatedAt(DateUtils.getNowTimeStr().toString());
			ur.setCreatedIp(IPUtils.getRemortIP(request));
			accountService.createRecharge(ur);

			map.addAttribute("msg", "添加线下充值成功!");

			return "redirect:recharge_offline_list.html?status=0";

		}

		return null;
	}

	/**
	 * 审核线下充值
	 * 
	 * @Title: recharge_offline_verify
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account/recharge_offline_list")
	public String recharge_offline_list(ModelMap map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Map<String, String> params = getParamMap(request);

		map.addAttribute("msg", params.get("msg"));

		try {

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);

			if (StringUtil.isNotEmpty(params.get("page"))) {

				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}

			pageRequest.setFilters(params);

			Page<UserRecharge> rechargePage = accountService
					.queryOfflineRechargePage(pageRequest);

			map.addAttribute("rechargePage", rechargePage);

			map.addAttribute(
					"totalPage",
					PageUtils.computeLastPageNumber(
							rechargePage.getTotalCount(),
							rechargePage.getPageSize()));

			map.addAttribute("page", pageRequest.getPageNumber());

			map.addAttribute("username", params.get("username"));

			map.addAttribute("realname", params.get("realname"));

			map.addAttribute("startTime", params.get("startTime"));

			map.addAttribute("endTime", params.get("endTime"));

			map.addAttribute("status", params.get("status"));

			return "account/offline_recharge_list";

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 线下充值审核
	 * 
	 * @Title: recharge_offline_verify
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @param rid
	 * @param @param remark
	 * @param @param status
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account/recharge_offline_verify")
	public String recharge_offline_verify(ModelMap map,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam long rid, @RequestParam String remark,
			@RequestParam int status) throws Exception {

		UserRecharge ur = accountService.queryOfflineRechargeById(rid);

		Map<String, String> params = getParamMap(request);

		String ctype = params.get("opt");

		if (StringUtils.isEmpty(ctype)) {

			map.addAttribute("recharge", ur);

			return "account/offline_recharge_verify_view";

		} else if (ctype.equals("ver")) {

			if (ur.getStatus() == 0) {

				AdminUser user = getCurrUser(request, response);

				ur.setStatus(status);
				ur.setPayResult(remark);
				ur.setUpdatedAt(DateUtils.getNowTimeStr().toString());
				ur.setUpdatedBy(user.getId());
				;

				accountService.updateByStatus(ur);

				if (status == 1) {

					UserAccount account = accountService.queryAccountByUser(ur
							.getCreatedBy());

					account.setMoneyTotal(account.getMoneyTotal()
							+ ur.getMoneyRecharge());
					account.setMoneyUsable(account.getMoneyUsable()
							+ ur.getMoneyRecharge());

					accountService.updateAccount(account);

					UserAccount useracc = accountService
							.queryAccountByUser(account.getUserId());

					UserAccountLog ual = new UserAccountLog();
					try {
						// 首次用户充值超过1000送给推荐人20元的
						if (ur.getMoneyRecharge() >= 1000) {
							// 所有通过该链接注册的用户在e聚财网完成注册并在e聚财网首次成功充值1000元或以上金额后，您可以获得20元的奖励资金。
							Long userId = ur.getCreatedBy();
							User userT = userService.queryByUserId(userId);
							if (userT.getInviteUserId() != null
									&& !userT.getInviteUserId().equals("")) {
								Long inviteUserId = Long.parseLong(userT
										.getInviteUserId());
								// 查询推荐用户是否是第一次充值 若为是 那么资金加20
								int count = userService
										.getIsFirstRechargeByUserId(userId);

								if (count <= 1) {
									// 判断推荐注册的用户属于第一次充值
									// 那么就给推荐人inviteUserId加个红包记录
									HongbaoLog hongbaoLog = new HongbaoLog();
									hongbaoLog.setUserId(inviteUserId);
									hongbaoLog.setMoney(new BigDecimal(Global
											.getValue("reg_hongbao")));
									// htype 1 注册送 2邀请送 status1已经发送 状态2已用
									hongbaoLog.setStatus(1);
									hongbaoLog.setCreatedAt(NewsDateUtils
											.getNowTimeStr());
									hongbaoLog.setHtype(1);
									hongbaoLog.setCreatedBy(Integer
											.parseInt(userId.toString()));
									// 过期天数为0时 表示无期限限制
									hongbaoLog.setExpiredDays(0);
									hongbaoLogService
											.createUserHongbaoLog(hongbaoLog);
								}
							}
						}

						// 新建资金记录
						ual.setUserId(useracc.getUserId());
						ual.setType(1);
						ual.setMoneyOperate(ur.getMoneyRecharge());
						ual.setMoneyTotal(useracc.getMoneyTotal());
						ual.setMoneyUsable(useracc.getMoneyUsable());
						ual.setMoneyWithdraw(useracc.getMoneyWithdraw());
						ual.setMoneyInsure(useracc.getMoneyInsure());
						ual.setRemark("通过" + Global.getValue("webname")
								+ "线下充值" + ur.getMoneyRecharge() + "元");
						ual.setCreatedAt(DateUtils.getNowTimeStr());
						ual.setCreatedIp(IPUtils.getRemortIP(request));
						ual.setMoneyCollection(useracc.getMoneyCollection());
						ual.setMoneyTenderFreeze(useracc.getMoneyTenderFreeze());

						accountService.createUserAccountLog(ual);

						map.addAttribute("msg", "线下充值审核成功!");

						return "redirect:recharge_offline_list.html?status=1";
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					map.addAttribute("msg", "线下充值审核不通过操作成功!");

					return "redirect:recharge_offline_list.html?status=2";
				}

			} else {

				return null;
			}
		}

		return null;

	}

	/**
	 * 线下扣款第一步查找用户
	 * 
	 * @Title: deduct_offline
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account/deduct_offline")
	public String deduct_offline(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		String type = params.get("opt");

		String uid = params.get("uid");

		map.addAttribute("msg", params.get("msg"));

		if (StringUtils.isEmpty(type)) {

			try {

				PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
				populate(pageRequest, request);
				pageRequest.setPageSize(10);

				if (StringUtil.isNotEmpty(params.get("page"))) {

					pageRequest.setPageNumber(Integer.valueOf(params
							.get("page")));
				}

				pageRequest.setFilters(params);

				Page<UserAccount> accountpage = accountService
						.queryUserAccountPage(pageRequest);

				map.addAttribute("accountpage", accountpage);

				map.addAttribute(
						"totalPage",
						PageUtils.computeLastPageNumber(
								accountpage.getTotalCount(),
								accountpage.getPageSize()));

				map.addAttribute("page", pageRequest.getPageNumber());

				map.addAttribute("username", params.get("username"));

				map.addAttribute("mobile", params.get("mobile"));

				map.addAttribute("realname", params.get("realname"));

				return "account/deduct_member_list";

			} catch (Exception e) {

				e.printStackTrace();
			}
		} else if (type.equals("view") && StringUtils.isNotEmpty(uid)) {

			UserAccount acc = accountService.queryAccountByUser(Long
					.valueOf(uid));

			map.addAttribute("account", acc);

			return "account/offline_deduct_view";

		} else if (type.equals("add") && StringUtils.isNotEmpty(uid)) {

			UserAccount account = accountService.queryAccountByUser(Long
					.valueOf(uid));

			String money = params.get("offline_money");

			if (account.getMoneyUsable() >= Double.valueOf(money)) {

				UserRecharge ur = new UserRecharge();

				Random random = new Random(System.currentTimeMillis());
				int iRandom = random.nextInt(10000) + 10000000;
				String out_trade_no = "XAZPKK"
						+ new SimpleDateFormat("yyyyMMddHHmmss")
								.format(new Date()) + "-"
						+ String.valueOf(iRandom) + "-" + uid;

				String webname = Global.getValue("webname");

				ur.setOrderId(out_trade_no);
				ur.setAccount(webname);
				ur.setCardNo("xxxxxx");
				ur.setMoneyRecharge(Double.valueOf(money));
				ur.setRemark(webname + "通过线下扣款" + money + "元");
				ur.setThirdPlatform(2L);
				ur.setThirdPlatformOrderId(3L);
				ur.setPaySource("Deduct_Offline");
				ur.setStatus(0);
				ur.setCreatedBy(Long.valueOf(uid));
				ur.setCreatedAt(DateUtils.getNowTimeStr().toString());
				ur.setCreatedIp(IPUtils.getRemortIP(request));

				accountService.createRecharge(ur);

				map.addAttribute("msg", "添加线下扣款成功!");

				return "redirect:deduct_offline_list.html?status=0";

			} else {

				map.addAttribute("msg", "用户可用余额不足");

				return "redirect:deduct_offline_list.html?status=0";

			}

		}

		return null;
	}

	/**
	 * 审核线下扣款
	 * 
	 * @Title: deduct_offline_list
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account/deduct_offline_list")
	public String deduct_offline_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		map.addAttribute("msg", params.get("msg"));

		try {

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);

			if (StringUtil.isNotEmpty(params.get("page"))) {

				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}

			pageRequest.setFilters(params);

			Page<UserRecharge> rechargePage = accountService
					.queryOfflineDeductPage(pageRequest);

			map.addAttribute("rechargePage", rechargePage);

			map.addAttribute(
					"totalPage",
					PageUtils.computeLastPageNumber(
							rechargePage.getTotalCount(),
							rechargePage.getPageSize()));

			map.addAttribute("page", pageRequest.getPageNumber());

			map.addAttribute("username", params.get("username"));

			map.addAttribute("realname", params.get("realname"));

			map.addAttribute("startTime", params.get("startTime"));

			map.addAttribute("endTime", params.get("endTime"));

			map.addAttribute("status", params.get("status"));

			return "account/offline_deduct_list";

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 线下扣款审核
	 * 
	 * @Title: deduct_offline_verify
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @param rid
	 * @param @param remark
	 * @param @param status
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/account/deduct_offline_verify")
	public String deduct_offline_verify(ModelMap map,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam long rid, @RequestParam String remark,
			@RequestParam int status) throws Exception {

		UserRecharge ur = accountService.queryOfflineDeductById(rid);

		Map<String, String> params = getParamMap(request);

		String ctype = params.get("opt");

		if (StringUtils.isEmpty(ctype)) {

			map.addAttribute("recharge", ur);

			return "account/offline_deduct_verify_view";

		} else if (ctype.equals("ver")) {

			if (ur.getStatus() == 0) {

				UserAccount account = accountService.queryAccountByUser(ur
						.getCreatedBy());

				if (account.getMoneyUsable() >= ur.getMoneyRecharge()) {

					AdminUser user = getCurrUser(request, response);

					ur.setStatus(status);
					ur.setPayResult(remark);
					ur.setUpdatedAt(DateUtils.getNowTimeStr().toString());
					ur.setUpdatedBy(user.getId());
					;

					accountService.updateByStatus(ur);

					if (status == 1) {

						account.setMoneyTotal(account.getMoneyTotal()
								- ur.getMoneyRecharge());
						account.setMoneyUsable(account.getMoneyUsable()
								- ur.getMoneyRecharge());

						accountService.updateAccount(account);

						UserAccount useracc = accountService
								.queryAccountByUser(account.getUserId());

						UserAccountLog ual = new UserAccountLog();

						// 新建资金记录
						ual.setUserId(useracc.getUserId());
						ual.setType(12);
						ual.setMoneyOperate(ur.getMoneyRecharge());
						ual.setMoneyTotal(useracc.getMoneyTotal());
						ual.setMoneyUsable(useracc.getMoneyUsable());
						ual.setMoneyWithdraw(useracc.getMoneyWithdraw());
						ual.setMoneyInsure(useracc.getMoneyInsure());
						ual.setRemark("通过" + Global.getValue("webname")
								+ "线下扣款" + ur.getMoneyRecharge() + "元");
						ual.setCreatedAt(DateUtils.getNowTimeStr());
						ual.setCreatedIp(IPUtils.getRemortIP(request));
						ual.setMoneyCollection(useracc.getMoneyCollection());
						ual.setMoneyTenderFreeze(useracc.getMoneyTenderFreeze());

						accountService.createUserAccountLog(ual);

						map.addAttribute("msg", "线下扣款审核成功!");

						return "redirect:deduct_offline_list.html?status=1";

					} else {

						map.addAttribute("msg", "线下扣款审核不通过操作成功!");

						return "redirect:deduct_offline_list.html?status=2";
					}

				} else {

					map.addAttribute("msg", "用户可用余额不足，操作失败");

					map.addAttribute("recharge", ur);

					return "account/offline_deduct_verify_view";
				}

			} else {

				map.addAttribute("msg", "线下充值审核不通过操作成功!");

				return "redirect:deduct_offline_list.html?status=2";
			}

		} else {

			return null;
		}
	}

	/**
	 * 用户银行卡管理
	 * 
	 * @Title: withdraw_list
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("admin/bankCard_list")
	public String bankCard_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		try {

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);

			if (StringUtil.isNotEmpty(params.get("page"))) {

				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}

			if (StringUtils.isNotEmpty(params.get("startTime"))) {

				String startTime = params.get("startTime");

				params.put("startTime", startTime);
			}

			if (StringUtils.isNotEmpty(params.get("endTime"))) {

				String endTime = params.get("endTime");

				params.put("endTime", endTime);
			}

			pageRequest.setFilters(params);

			Page<BankCard> bankCardpage = accountService
					.queryBankCard(pageRequest);

			map.addAttribute("bankCardpage", bankCardpage);

			map.addAttribute(
					"totalPage",
					PageUtils.computeLastPageNumber(
							bankCardpage.getTotalCount(),
							bankCardpage.getPageSize()));

			map.addAttribute("page", pageRequest.getPageNumber());

			map.addAttribute("realname", params.get("realname"));
			map.addAttribute("startTime", params.get("startTime"));
			map.addAttribute("endTime", params.get("endTime"));
			map.addAttribute("username", params.get("username"));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "account/bankCard_list";

	}

	@RequestMapping("admin/bankCard_update")
	public String bankCard_update(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		String id = params.get("id");
		String type = params.get("type");

		if (type == null) {
			BankCard bankCard = accountService.queryUserBankCard(Long
					.valueOf(id));
			map.addAttribute("bankCard", bankCard);

			List<Bank> bankList = bankDao.queryAllBank();
			map.put("bankList", bankList);

			return "account/bankCard_update";

		} else {
			if (type.equals("upd")) {
				String bankId = params.get("bankId");
				String cardNo = params.get("cardNo");

				// 银行卡号正则验证：16或19位数字
				Pattern pattern = Pattern.compile("^(\\d{16}|\\d{19})$");
				Matcher matcher = pattern.matcher(cardNo);
				String msg = "";
				BankCard bankCard = accountService.queryUserBankCard(Long
						.valueOf(id));
				if (!matcher.matches()) {
					msg = "请输入正确的银行卡号";
					map.put("msg", msg);

					map.addAttribute("bankCard", bankCard);

					List<Bank> bankList = bankDao.queryAllBank();
					map.put("bankList", bankList);
					return "account/bankCard_update";
				}

				// 查询该银行卡是否已经被别人绑定
				if (!bankCard.getCardNo().equals(cardNo)) {
					if (bankCardDao.getBankCardByCardNo(cardNo) != null) {
						msg = "该银行卡已经被绑定,请核对";
						map.put("msg", msg);

						map.addAttribute("bankCard", bankCard);

						List<Bank> bankList = bankDao.queryAllBank();
						map.put("bankList", bankList);

						return "account/bankCard_update";
					}
				}

				Bank b = bankDao.queryAllBank(Long.valueOf(bankId));
				map.put("b", b);

				BankCard bc = new BankCard();
				bc.setId(Long.valueOf(id));
				bc.setBankId(Long.valueOf(bankId));
				bc.setBankName(b.getBankName());
				bc.setCardNo(cardNo);

				accountService.updateBankCard(bc);
				map.addAttribute("msg", "更新成功");

			} else {
				bankCardDao.deleteByBankCardId(Integer.valueOf(id));
				map.addAttribute("msg", "删除成功");
			}
		}

		return "redirect:/admin/bankCard_list.html";

	}

	@RequestMapping("admin/hongbao_list")
	public String hongbao(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws IllegalAccessException,
			InvocationTargetException {
		PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
		populate(pageRequest, request);
		pageRequest.setPageSize(10);
		Map<String, String> params = getParamMap(request);
		if (StringUtil.isNotEmpty(params.get("page"))) {
			pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
		}
		pageRequest.setFilters(params);
		Page<Hongbao> hongbaopage = userService.getHongbaoBypage(pageRequest);
		map.addAttribute("hongbaoPage", hongbaopage);
		map.addAttribute("totalPage", PageUtils.computeLastPageNumber(
				hongbaopage.getTotalCount(), hongbaopage.getPageSize()));
		map.addAttribute("status", params.get("status"));
		map.addAttribute("username", params.get("username"));
		map.addAttribute("type", params.get("type"));
		return "account/hongbao_list";
	}
	
	
	@RequestMapping("admin/market")
	public String market(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		try {
			marketDao.delMarketByMarketId(3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("---------------");
		
		return "";
}

}