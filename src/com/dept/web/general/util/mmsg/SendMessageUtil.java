package com.dept.web.general.util.mmsg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dept.web.context.Global;
import com.jianzhou.sdk.BusinessService;

/**
 * 
 * @ClassName:     SendMessageUtil.java
 * @Description:   发送短信彩信工具
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-10-10 上午10:57:48
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class SendMessageUtil {
	
	protected static final Logger LOGGER = Logger.getLogger(SendMessageUtil.class);
	
	
public static boolean sendSMSE(String phone, String content) throws Exception{
//		String url = "http://www.ztsms.cn:8800/sendSms.do?";
		String url=Global.getValue("sendSMSUrl");

//		String account = "tuoyuan"; //登录名称
		String account =Global.getValue("sendSMSName");
		String password= Global.getValue("sendSMSPwd");

		String smsUrl = url+"account="+account+"&password="+password+"&mobile="+phone+"&content="+content;
		
		URL getUrl;
		try {
			getUrl = new URL(smsUrl);
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
	          // 建立与服务器的连接，并未发送数据
	          connection.connect();
	          // 发送数据到服务器并使用Reader读取返回的数据
	          BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	         // 1代表发送短信成功,xxxxxxxx代表消息编号
	          // 0发送短信失败,xxxxxxxx代表消息编号
	          // 余额不够或扣费错误
	          String lines;
	          while ((lines = reader.readLine()) != null) {
	                  System.out.println(lines);
	          }
	          reader.close();
	          // 断开连接
	          connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true; 
	}
	
	/**
	 * 
	 * @Description:  发送短信
	 * @param:        @param phone
	 * @param:        @param content
	 * @param:        @return
	 * @param:        @throws InterruptedException   
	 * @return:       boolean   
	 * @throws
	 */
	public static boolean sendSMS(String phone, String content) throws InterruptedException{
		
		BusinessService bs = new BusinessService();
		bs.setWebService("http://www.jianzhou.sh.cn/JianzhouSMSWSServer/services/BusinessService");
		
		String msgname = "sdk_baicai";
		
		String msgpassword = "264527";
		
		String sign = "【钱保姆】";
		
		
		int restr = bs.sendBatchMessage(msgname, msgpassword, phone, content+sign);
		
		if(restr>0){
			
			return true;
			
		}else{
			
			return false;
		}
	}
	
	/**
	 * 
	 * @Description:  发送短信
	 * @param:        @param phone
	 * @param:        @param content
	 * @param:        @return
	 * @param:        @throws InterruptedException   
	 * @return:       boolean   
	 * @throws
	 */
	public static boolean sendSMSByHuaxin(String phone, String text) throws Exception{
		
		
		//String Text=URLEncoder.encode("您的验证码：8859【华信】","utf-8");
//		String Text="您的验证码：8859【华信】";
		String Url=Global.getValue("sendSMSUrl");
		
		HttpClient client=new HttpClient();
		PostMethod post=new PostMethod(Url);
		post.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair userid=new NameValuePair("userid","");
		NameValuePair account=new NameValuePair("account",Global.getValue("sendSMSName"));
		NameValuePair password=new NameValuePair("password",Global.getValue("sendSMSPwd"));
		NameValuePair mobile=new NameValuePair("mobile",phone);
		NameValuePair content=new NameValuePair("content",text);
		NameValuePair sendTime=new NameValuePair("sendTime","");
		NameValuePair extno=new NameValuePair("extno","");
		post.setRequestBody(new NameValuePair[]{userid,account,password,mobile,content,sendTime,extno});
		int statu=client.executeMethod(post);
		System.out.println("statu="+statu);
		String str=post.getResponseBodyAsString();
		
		System.out.println(str);
		
//		HttpMethod method=new PostMethod(Url);//带参数的Url
//		method.setRequestHeader("Content-type", "text/xml; charset=utf-8");
//		client.executeMethod(method);
//		String str = method.getResponseBodyAsString();
//		System.out.println("result="+str);
		
		try {
			//将字符转化为XML
			Document doc=DocumentHelper.parseText(str);
			//获取根节点
			Element rootElt=doc.getRootElement();
			//获取根节点下的子节点的值
			String returnstatus=rootElt.elementText("returnstatus").trim();
			String message=rootElt.elementText("message").trim();
			String remainpoint=rootElt.elementText("remainpoint").trim();
			String taskID=rootElt.elementText("taskID").trim();
			String successCounts=rootElt.elementText("successCounts").trim();
			
			LOGGER.debug("返回状态为："+returnstatus);
			LOGGER.debug("返回信息提示："+message);
			LOGGER.debug("返回余额："+remainpoint);
			LOGGER.debug("返回任务批次："+taskID);
			LOGGER.debug("返回成功条数："+successCounts);
		} catch (Exception e) {
			System.out.println(e);
		}
		return true;
	}
	
	
	public static void main(String[] args) throws Exception {
		
//		sendSMS("13735803606", "投资成功：您已投资100元至<项目名>，您的资金在投标结束前将被冻结。");
//		sendSMS("1373580360`6", "您已成功提现<101元>，资金将于明日进入您的账户。请及时关注您的账户。");
//		sendSMS("13735803606", "您投资的项目<项目名>已回款，您出借的本金<202元>与收益<22元>已成功转入您的钱保姆账户，请您查收。");
//		sendSMS("13735803606", "您已成功向钱保姆账户充值<100元>，欲了解更多项目详情请登陆192.168.1.213:8080。");
		
		sendSMSE("18268812509", "【钱功夫】尊敬的用户您好，您投资的产品测试,于2016-04-05开始计息，投资期限为30天。");
	}

}
