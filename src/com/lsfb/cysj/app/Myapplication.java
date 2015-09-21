package com.lsfb.cysj.app;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.easemob.chat.EMChat;
import com.lidroid.xutils.HttpUtils;
import com.lsfb.cysj.base.User;
import com.lsfb.cysj.model.ZhiKuModel;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class Myapplication extends Application {
	private static Myapplication instance;
	
	public static List<ZhiKuModel> zhikuList;// 智库列表
	public static List<Map<String, Object>> fensiList;// 我的粉丝列表
	public static Context context;
	public static HttpUtils httpUtils = new HttpUtils();

	@Override
	public void onCreate() {
		super.onCreate();
		context=getApplicationContext();
		instance = this;
		EMChat.getInstance().init(getApplicationContext());
		/**
		 * debugMode == true 时为打开，sdk 会在log里输入调试信息
		 * 
		 * @param debugMode
		 *            在做代码混淆的时候需要设置成falsea
		 */
		EMChat.getInstance().setDebugMode(true);// 在做打包混淆时，要关闭debug模式，如果未被关闭，则会出现程序无法运行问题
		// appContext = this;
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName.equalsIgnoreCase("com.easemob.chatuidemo")) {
			Log.e("", "enter the service process!");
			// "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	// public Map<String, User> getContactList() {
	// return hxSDKHelper.getContactList();
	// }
	public static Myapplication getInstance() {
		return instance;
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

}
