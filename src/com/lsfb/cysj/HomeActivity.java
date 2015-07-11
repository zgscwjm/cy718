package com.lsfb.cysj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.fragment.IdeasFragment;
import com.readystatesoftware.viewbadger.BadgeView;

public class HomeActivity extends FragmentActivity implements OnClickListener {

	public static final int TAB_IDEAS = 0;
	public static final int TAB_FRIENDS = 1;
	public static final int TAB_TRENDSGAME = 2;
	public static final int TAB_MY = 3;
	Intent intent;
	/**
	 * main viewpager 上面的页面
	 */
	@ViewInject(R.id.main)
	private ViewPager viewPager;
	/**
	 * btn_cyl friends 创友录
	 */
	@ViewInject(R.id.btn_cyl)
	private RadioButton friends;
	/**
	 * btn_cyx ideas 创意信
	 */
	@ViewInject(R.id.btn_cyx)
	private RadioButton ideas;
	/**
	 * btn_game trends_game 动态比赛
	 */
	@ViewInject(R.id.btn_game)
	private RadioButton trends_game;
	/**
	 * btn_my mybtn 我的
	 */
	@ViewInject(R.id.btn_my)
	private RadioButton mybtn;
	/**
	 * home_bt_num num 消息提醒条数
	 */
	@ViewInject(R.id.home_bt_num)
	private Button num;
	SharedPreferences sp;
	MyReceiver myReceiver;
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
		}}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_main);
		ViewUtils.inject(this);
		// 极光初始化
		JPushInterface.init(this);
		JPushInterface.setDebugMode(true);
		// 动态注册推送服务
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(JPushInterface.ACTION_NOTIFICATION_RECEIVED);
		intentFilter.addAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
		intentFilter.addAction(JPushInterface.ACTION_MESSAGE_RECEIVED);
		intentFilter.addCategory(getPackageName());
		registerReceiver(myReceiver, intentFilter);
		// 下拉样式
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
				HomeActivity.this, R.layout.customer_notitfication_layout,
				R.id.icon, R.id.title, R.id.text); // 指定定制的 Notification Layout

		builder.statusBarDrawable = R.drawable.logo2; // 指定最顶层状态栏小图标
		builder.layoutIconDrawable = R.drawable.logo2; // 指定下拉状态栏时显示的通知图标
		JPushInterface.setPushNotificationBuilder(2, builder);
		sp = getSharedPreferences("count", MODE_WORLD_READABLE);
		int count = sp.getInt("count", 0);
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面
		if (count == 0) {
			intent = new Intent();
			intent.setClass(getApplicationContext(), Loading.class);
			startActivity(intent);
			finish();
		}
		Editor editor = sp.edit();
		// 存入数据
		editor.putInt("count", ++count);
		// 提交修改
		editor.commit();

		init();
		Listener();
	}
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		if (IsTrue.tabnum == 3) {
			viewPager.setCurrentItem(TAB_TRENDSGAME);
			IsTrue.tabnum = 0;
		} else if (IsTrue.tabnum == 4) {//发布创意
			viewPager.setCurrentItem(TAB_MY);
			IsTrue.fabuchuangyi = 1;
			IsTrue.tabnum = 0;
		}else if (IsTrue.tabnum == 5) {//我要禅修
			viewPager.setCurrentItem(TAB_MY);
			IsTrue.woyaochanxiu = 1;
			IsTrue.tabnum = 0;
		}else if (IsTrue.tabnum == 6) {//发起比赛
			viewPager.setCurrentItem(TAB_MY);
			IsTrue.fabubisai = 1;
			IsTrue.tabnum = 0;
		}else if (IsTrue.zhishu == 1) {//我的创意指数排行
			viewPager.setCurrentItem(TAB_MY);
			IsTrue.zhishu = 0;
		}else if (IsTrue.quxiaoguanzhu == 1) {
			viewPager.setCurrentItem(TAB_TRENDSGAME);
			IsTrue.quxiaoguanzhu = 0;
		}else if(IsTrue.hotzhiku == true){
			viewPager.setCurrentItem(TAB_MY);
			IsTrue.hotzhiku = false;
		}else if (IsTrue.yichan == 1) {//创意世界优秀遗产发布
			viewPager.setCurrentItem(TAB_MY);
		}
		super.onResume();
	}

	private void Listener() {

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int id) {
				switch (id) {
				case TAB_IDEAS:
					ideas.setChecked(true);
					break;
				case TAB_FRIENDS:
					friends.setChecked(true);
					break;
				case TAB_TRENDSGAME:
					trends_game.setChecked(true);
					break;
				case TAB_MY:
					mybtn.setChecked(true);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void init() {
		FragmentAdapter adapter = new FragmentAdapter(
				getSupportFragmentManager(), 4, 0);

		viewPager.setAdapter(adapter);
		friends.setOnClickListener(this);
		ideas.setOnClickListener(this);
		trends_game.setOnClickListener(this);
		mybtn.setOnClickListener(this);
		num.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cyx:
			viewPager.setCurrentItem(TAB_IDEAS);
			ScrollView ideas_vertical = (ScrollView) viewPager
					.findViewById(R.id.ideas_vertical);
			ideas_vertical.smoothScrollTo(0, 0);
			break;
		case R.id.btn_cyl:
			viewPager.setCurrentItem(TAB_FRIENDS);
			BadgeView badgeView = new BadgeView(getApplicationContext(), num);
			badgeView.setText("1");
			badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			badgeView.show();
			break;
		case R.id.btn_game:
			viewPager.setCurrentItem(TAB_TRENDSGAME);
			break;
		case R.id.btn_my:
			viewPager.setCurrentItem(TAB_MY);
			break;

		default:
			break;
		}

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (IsTrue.exit == true) {
//			if (IsTrue.dongtaigame = true) {
////				IsTrue.dongtaigame = false;
//				IsTrue.exit = false;
////				finish();
//				intent = new Intent(HomeActivity.this,SearchActivity.class);
//				startActivity(intent);
//			}
			finish();
		} else {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
