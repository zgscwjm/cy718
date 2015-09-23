package com.lsfb.cysj.app;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.R.bool;
import android.graphics.drawable.Drawable;

/**
 * 静态变量类
 * @author Administrator
 *
 */
public class IsTrue {
	public static String szImei=null;//手机设备唯一标识
	public static Set<String> tags = null;//推送标签
	public static Integer tabnum = 0;// 切换HomeACtivity
	public static Integer fabuchuangyi = 0;// 发布创意
	public static Integer fabubisai = 0;//发布比赛
//	public static Integer guanzhugame = 0;//热门比赛关注比赛
//	public static boolean fabulogin = false;//发布默认未登录
	public static Integer woyaochanxiu = 0;//我要禅修
	public static boolean search = false;//返回搜索
	public static boolean isSgin = false;// 判断用户是否登录
	public static int userId = 0;// 用户ID
	public static boolean qifuchange = true;//祈福人员添加删除
//	public static String Stringiamgename = "";//头像名字
	public static Drawable drawable =null;//头像
	public static boolean shangxiang = true;//上香诵经
	public static String Stringnickname = "";//昵称 
	public static String Stringnumber = "";//创创号
	public static String Stringznumber = "";//创意世界智库号(0)
	public static String Stringzhishu = "";//我的创意指数
	public static String Stringmoney = "";//我的创创币
	public static String Stringimage = "";//会员头像
	public static String Stringsignatur = "";//签名(未设置)
	public static String Stringbirthday = "";//会员生日(未设置)
	public static String Stringhome = "";//所在地(未设置)
	public static String Stringhome1 = "";//中国
	public static String Stringhome2 = "";//四川省
	public static String Stringhome3= "";//成都市
	public static String Stringhome4= "";//金牛区
	public static String Stringsex = "";//性别(男、女、未设置)
	public static String Stringphone = "";//手机号(未设置)
	public static String Stringschool = "";//学校
	public static int intDengji = 4;//等级
	public static int intZqyz = 1;//等级
	public static boolean booleanisschool = false;//学校验证标识(0未验证|其他值为验证了的)
	public static String Stringgovernment = "";//政企
	public static boolean booleanisgovernment = false;//政企验证标识(0未验证|其他值为验证了的)
	public static boolean changeimg = false;//上传图片或者组图
	public static int upimg = 0;
	public static String moreimg = "";//更多图片
	public static boolean exit = false;//退出和返回的区别
//	public static boolean dongtaigame = false;//动态比赛返回
	public static String Stringidstrarea0 = "";//国际ID
	public static String Stringidstrarea1 = "";//省份ID
	public static String Stringidstrarea2 = "";//市ID
	public static String Stringidstrarea3 = "";//区ID
	public static boolean dianzan = true;//创意详情底部点赞，默认可以点击
	public static Integer fabuotheridea = 0;//发布不同的创意
	public static Integer xuanzeleixing = 0;//选择发布创意的类型
	public static boolean city = false;//创意世界大赛city 报名中默认false
	public static Integer upmore = 0;//上传多图和视频，变量值指创意世界大赛和热门比赛
	public static Integer kantu = 0; //查看图片
	public static Integer zhishu = 0 ;// 我的创意指数
	public static Integer quxiaoguanzhu = 0 ;//取消关注
	public static boolean hotzhiku = false;//热门智库未登录
	public static Integer yichan = 0;//创意世界优秀遗产发布
	public static boolean isYichan=false;
	// 字符串转码
	public  static String transformation(String s) {
		char[] shortArray = s.toCharArray();
		char[] charArray = new char[shortArray.length];
		for (int i = 0; i < shortArray.length; i++) {
			charArray[i] = (char) shortArray[i];
		}
		String str = new String(charArray);
		return str;
	}

	// 网络连接有未知的错误，这个涉及到连接池，用于搜索的时候报未知的错误
	public static class HttpConnectionManager {

		private static HttpParams httpParams;
		private static ClientConnectionManager connectionManager;

		/**
		 * 最大连接数
		 */
		public final static int MAX_TOTAL_CONNECTIONS = 800;
		/**
		 * 获取连接的最大等待时间
		 */
		public final static int WAIT_TIMEOUT = 60000;
		/**
		 * 每个路由最大连接数
		 */
		public final static int MAX_ROUTE_CONNECTIONS = 400;
		/**
		 * 连接超时时间
		 */
		public final static int CONNECT_TIMEOUT = 10000;
		/**
		 * 读取超时时间
		 */
		public final static int READ_TIMEOUT = 10000;

		static {
			httpParams = new BasicHttpParams();
			// 设置最大连接数
			ConnManagerParams.setMaxTotalConnections(httpParams,
					MAX_TOTAL_CONNECTIONS);
			// 设置获取连接的最大等待时间
			ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
			// 设置每个路由最大连接数
			ConnPerRouteBean connPerRoute = new ConnPerRouteBean(
					MAX_ROUTE_CONNECTIONS);
			ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
					connPerRoute);
			// 设置连接超时时间
			HttpConnectionParams.setConnectionTimeout(httpParams,
					CONNECT_TIMEOUT);
			// 设置读取超时时间
			HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			connectionManager = new ThreadSafeClientConnManager(httpParams,
					registry);
		}

		public static HttpClient getHttpClient() {
			return new DefaultHttpClient(connectionManager, httpParams);
		}

	}

	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(17[0,7])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();

	}
}
