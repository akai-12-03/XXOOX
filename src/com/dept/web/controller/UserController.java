package com.dept.web.controller;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.dao.model.AdminUser;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.ExportExcel;
import com.dept.web.general.util.MD5;
import com.dept.web.service.UserService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 用户管理
 * 
 * @ClassName: UserController
 * @Description:
 *
 * @author cannavaro
 * @version V1.0
 * @Date 2015年4月20日 下午6:37:54 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class UserController extends WebController {

	@Autowired
	private UserService userService;

	@RequestMapping("admin/member/webuser")
	public String showWebuser(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		String type = params.get("ctype");

		map.addAttribute("msg", params.get("msg"));

		try {

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);

			if (StringUtil.isNotEmpty(params.get("page"))) {

				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}

			pageRequest.setFilters(params);

			if (type.equals("web")) {

				Page<User> userpage = userService.queryUser(pageRequest);
				List<User> userList = userpage.getResult();
				for (User user : userList) {
					if (user.getInviteUserId() != null
							&& !"".equals(user.getInviteUserId())) {
						User user1 = userService.queryByUserId(Long
								.valueOf(user.getInviteUserId()));
						if (user1 != null) {
							user.setInviteUsername(user1.getUsername());
						}
					}
				}
				userpage.setResult(userList);
				map.addAttribute("userpage", userpage);

				map.addAttribute(
						"totalPage",
						PageUtils.computeLastPageNumber(
								userpage.getTotalCount(),
								userpage.getPageSize()));

				map.addAttribute("page", pageRequest.getPageNumber());

				map.addAttribute("ctype", type);

				map.addAttribute("username", params.get("username"));

				map.addAttribute("mobile", params.get("mobile"));

				map.addAttribute("realname", params.get("realname"));

				map.addAttribute("isTouzi", params.get("isTouzi"));

				map.addAttribute("msg", params.get("msg"));

				return "member/member_list";

			} else if (type.equals("back")) {

				Page<AdminUser> userpage = userService
						.queryBackUser(pageRequest);

				map.addAttribute("userpage", userpage);

				map.addAttribute(
						"totalPage",
						PageUtils.computeLastPageNumber(
								userpage.getTotalCount(),
								userpage.getPageSize()));

				map.addAttribute("page", pageRequest.getPageNumber());

				map.addAttribute("ctype", type);

				return "member/backmember_list";
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("admin/member/createbackuser")
	public String createUser(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> params = getParamMap(request);

		String ctype = params.get("ctype");

		map.addAttribute("ctype", ctype);

		if (StringUtils.isEmpty(ctype)) {

			map.addAttribute("ctype", "add");

			return "member/backmember";

		} else if (ctype.equals("update")) {

			String userid = params.get("uid");

			if (StringUtils.isEmpty(userid)) {

				map.addAttribute("ctype", "add");

				return "member/backmember";

			} else {

				AdminUser user = userService.queryAdminUserById(Long
						.valueOf(userid));

				map.addAttribute("admuser", user);

				map.addAttribute("ctype", "upd");

				return "member/backmember";
			}
		} else if (ctype.equals("add")) {

			// 检查是否用户名重复
			boolean isRep = userService
					.isRepeatUsername(params.get("username"));
			String status = request.getParameter("status");

			if (isRep) {

				map.addAttribute("msg", "用户名已经存在");

				return "member/backmember";

			} else if (status == null) {
				map.addAttribute("msg", "状态不能为空");

				return "member/backmember";
			} else {

				AdminUser user = new AdminUser();

				MD5 md5 = new MD5();

				user.setUsername(params.get("username"));
				user.setPasswordHash(md5.getMD5ofStr(params.get("password")));
				user.setEmail(params.get("email"));
				user.setMobile(params.get("mobile"));
				user.setStatus(Integer.valueOf(params.get("status")));

				user.setCreatedAt(DateUtils.getNowTimeStr());

				userService.createAdminUser(user);

				map.addAttribute("msg", "添加后台用户成功");

				return "redirect:webuser.html?ctype=back";

			}

		} else if (ctype.equals("upd")
				&& StringUtils.isNotEmpty(params.get("uid"))) {

			AdminUser user = userService.queryAdminUserById(Long.valueOf(params
					.get("uid")));

			user.setUsername(params.get("username"));
			user.setEmail(params.get("email"));
			user.setMobile(params.get("mobile"));
			user.setStatus(Integer.valueOf(params.get("status")));

			user.setUpdatedAt(DateUtils.getNowTimeStr());

			userService.updateAdminUser(user);

			map.addAttribute("msg", "更新后台用户成功");

			return "redirect:webuser.html?ctype=back";

		}

		return null;
	}

	/**
	 * 删除后台用户
	 * 
	 * @Title: delAdminUser
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @param uid
	 * @param @param opt
	 * @param @throws Exception 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping("admin/member/delAdminUser")
	public void delAdminUser(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam long uid,
			@RequestParam String opt) throws Exception {

		if (opt.equals("del")) {

			AdminUser user = userService.queryAdminUserById(uid);

			userService.delAdminUser(user);

			out(response, 1);

		} else {

			out(response, 2);
		}

	}

	@RequestMapping("admin/member/ideuser")
	public String ideuser(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam long uid)
			throws Exception {

		Map<String, String> params = getParamMap(request);

		String optype = params.get("opt");

		User user = userService.queryWebUserById(uid);

		if (StringUtils.isEmpty(optype)) {

			map.addAttribute("webuser", user);

			return "member/webmember";

		} else {

			String realname = params.get("realname");

			String idcard = params.get("idCard");

			if (StringUtils.isEmpty(realname) || StringUtils.isEmpty(idcard)) {

				map.addAttribute("webuser", user);

				return "member/webmember";

			} else {

				user.setRealname(realname);
				user.setIdCard(idcard);
				user.setRealVerifyStatus(1);
				user.setUpdatedAt(DateUtils.getNowTimeStr());

				userService.updateWebUser(user);

				map.addAttribute("msg", "实名认证成功");

				return "redirect:webuser.html?ctype=web";
			}
		}

	}

	/**
	 * 启用停用后台用户
	 * 
	 * @Title: verifyAdminUser
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @param uid
	 * @param @param op
	 * @param @throws Exception 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping("admin/member/verifyAdminUser")
	public void verifyAdminUser(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam long uid,
			@RequestParam String opt) throws Exception {

		try {

			AdminUser user = userService.queryAdminUserById(uid);

			if (opt.equals("start")) {

				user.setStatus(10);

			} else if (opt.equals("stop")) {

				user.setStatus(0);
			}

			int count = userService.updateAdminUser(user);

			if (count > 0) {

				out(response, 1);

			} else {

				out(response, 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			out(response, 0);
		}
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
	@RequestMapping("admin/member/webuserExcel")
	public String account_excel(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);

		String cType = params.get("cType");

		// 根据用户搜索信息导出excel记录
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			// 下面是对中文文件名的处理
			response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
			ExportExcel<User> ex2 = new ExportExcel<User>();
			String[] alias = new String[] { "Id", "username", "mobile",
					"realname", "idCard", "inviteUsername",
					"createdAtStr", "realVerify","account","capital","interest"};
			String[] names = new String[] { "编号", "用户名", "手机号", "真实姓名",
					"身份证", "推荐人", "注册时间", "是否实名","总投资额","未偿还金额","未偿还利息" };
			List<User> userList = userService.queryUser(params);
			User user;
			for (int i = 0; i <userList.size(); i++) {
				user = userList.get(i);
				if (user != null) {
					user.setId(Long.valueOf(i) + 1);
					user.setCreatedAtStr(DateUtils.dateStr(user.getCreatedAt(), "yyyy-MM-dd hh:mm:ss"));
					if (user.getInviteUserId() != null
							&& !"".equals(user.getInviteUserId())) {
						User user1 = userService.queryByUserId(Long
								.valueOf(user.getInviteUserId()));
						if (user1 != null) {
							user.setInviteUsername(user1.getUsername());
						}
					}
					if(user.getRealVerifyStatus()!=null&&user.getRealVerifyStatus()==1)
					{
						user.setRealVerify("已认证");
					}
				}
			}
			String fname = java.net.URLEncoder.encode("前台用户记录表", "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
			response.setContentType("application/msexcel");// 定义输出类型
			ex2.exportExcel(names, userList, os, alias);
			response.setCharacterEncoding("UTF-8");
			os.flush();
			os.close();
			map.addAttribute("cType", params.get("cType"));
			map.addAttribute("username", params.get("username"));
			map.addAttribute("realname", params.get("realname"));
			map.addAttribute("mobile", params.get("mobile"));
			map.addAttribute("isTouzi", params.get("isTouzi"));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	
	//前台用户投资统计
	@RequestMapping("admin/member/userTenderAnalysis")
	public String userTenderAnalysis(ModelMap map, HttpServletRequest request,
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

			Page<User> userpage = userService.queryUserTendInfo(pageRequest);
			List<User> userList = userpage.getResult();
			userpage.setResult(userList);
			map.addAttribute("userpage", userpage);
			map.addAttribute(
					"totalPage",
					PageUtils.computeLastPageNumber(
							userpage.getTotalCount(),
							userpage.getPageSize()));
			map.addAttribute("page", pageRequest.getPageNumber());
			map.addAttribute("username", params.get("username"));
			map.addAttribute("mobile", params.get("mobile"));
			map.addAttribute("realname", params.get("realname"));
			map.addAttribute("analysisMonth", params.get("analysisMonth"));
			map.addAttribute("analysisYear", params.get("analysisYear"));
			map.addAttribute("msg", params.get("msg"));
			return "member/member_tenderlist";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//前台用户投资统计导出Excel
	@RequestMapping("admin/member/userTenderAnalysisExcel")
	public String userTenderAnalysisExcel(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);

		// 根据用户搜索信息导出excel记录
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			// 下面是对中文文件名的处理
			response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
			ExportExcel<User> ex2 = new ExportExcel<User>();
			String[] alias = new String[] { "Id", "username", "mobile",
					"realname", "idCard", "tendertime", "account"};
			String[] names = new String[] { "编号", "用户名", "手机号", "真实姓名",
					"身份证", "投资年月", "投资总额" };
			List<User> userList = userService.queryUserTendInfo(params);
			User user;
			for (int i = 0; i <userList.size(); i++) {
				user = userList.get(i);
				if (user != null) {
					user.setId(Long.valueOf(i) + 1);
				}
			}
			String fname = java.net.URLEncoder.encode("前台用户投资统计表", "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
			response.setContentType("application/msexcel");// 定义输出类型
			ex2.exportExcel(names, userList, os, alias);
			response.setCharacterEncoding("UTF-8");
			os.flush();
			os.close();
			map.addAttribute("username", params.get("username"));
			map.addAttribute("realname", params.get("realname"));
			map.addAttribute("mobile", params.get("mobile"));
			map.addAttribute("analysisMonth", params.get("analysisMonth"));
			map.addAttribute("analysisYear", params.get("analysisYear"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}