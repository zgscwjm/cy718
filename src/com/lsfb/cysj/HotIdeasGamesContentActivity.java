package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ActivityManagerApplication;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HotIdeasGamesContentActivity extends FragmentActivity implements OnClickListener{
	Intent intent;
	/**
	 * game_shezhi 
	 * shezhi 设置
	 */
	@ViewInject(R.id.game_shezhi)
	private Button shezhi;
	/**
	 * hot_ideas_games_content_addfriends 
	 * addfriends 添加朋友
	 */
	@ViewInject(R.id.hot_ideas_games_content_addfriends)
	private RelativeLayout addfriends;
	/**
	 * hot_ideas_games_content_img1 
	 * guanzhuman 关注成员
	 */
	@ViewInject(R.id.hot_ideas_games_content_img1)
	private RelativeLayout guanzhuman;
	/**
	 * hot_ideas_games_content_img3 
	 * guanzhu 关注成员
	 */
	@ViewInject(R.id.hot_ideas_games_content_img3)
	private RelativeLayout guanzhu;
	/**
	 * hot_ideas_games_content_zuopin 
	 * zuopin 作品
	 */
	@ViewInject(R.id.hot_ideas_games_content_zuopin)
	private RelativeLayout zuopin;
	@ViewInject(R.id.hot_ideas_games_content_img7)
	private RelativeLayout my;
	@ViewInject(R.id.hot_ideas_games_content_back)
	private LinearLayout back;
	@ViewInject(R.id.hot_ideas_games_content_news)
	private Button news;
	/**
	 * hot_ideas_games_content_zhikuman_other1 智库专家1
	 */
	@ViewInject(R.id.hot_ideas_games_content_zhikuman_other1)
	private ImageView hot_ideas_games_content_zhikuman_other1;
	/**
	 * hot_ideas_games_content_zhikuman_other2智库专家2
	 */
	@ViewInject(R.id.hot_ideas_games_content_zhikuman_other2)
	private ImageView hot_ideas_games_content_zhikuman_other2;
	/**
	 * hot_ideas_games_content_zhikuman_other3 智库专家3
	 */
	@ViewInject(R.id.hot_ideas_games_content_zhikuman_other3)
	private ImageView hot_ideas_games_content_zhikuman_other3;
	/**
	 * hot_ideas_games_content_guanzhuman_other1 关注人1
	 */
	@ViewInject(R.id.hot_ideas_games_content_guanzhuman_other1)
	private ImageView hot_ideas_games_content_guanzhuman_other1;
	/**
	 * hot_ideas_games_content_guanzhuman_other2 关注人2
	 */
	@ViewInject(R.id.hot_ideas_games_content_guanzhuman_other2)
	private ImageView hot_ideas_games_content_guanzhuman_other2;
	/**
	 * hot_ideas_games_content_guanzhuman_other3 关注人3
	 */
	@ViewInject(R.id.hot_ideas_games_content_guanzhuman_other3)
	private ImageView hot_ideas_games_content_guanzhuman_other3;
	/**
	 * hot_ideas_games_content_img5 参赛作品
	 */
	@ViewInject(R.id.hot_ideas_games_content_img5)
	private RelativeLayout hot_ideas_games_content_img5;
	/**
	 * hot_ideas_games_content_headimg 顶部图片
	 */
	@ViewInject(R.id.hot_ideas_games_content_headimg)
	private ImageView hot_ideas_games_content_headimg;
	/**
	 * hot_ideas_games_content_content 内容
	 */
	@ViewInject(R.id.hot_ideas_games_content_content)
	private TextView hot_ideas_games_content_content;
	/**
	 * hot_ideas_games_content_gametime gametime 比赛时间
	 */
	@ViewInject(R.id.hot_ideas_games_content_gametime)
	private TextView gametime;
	/**
	 * hot_ideas_games_content_man_img 创办者头像
	 */
	@ViewInject(R.id.hot_ideas_games_content_man_img)
	private ImageView hot_ideas_games_content_man_img;
	/**
	 * hot_ideas_games_content_man_name 创办者
	 */
	@ViewInject(R.id.hot_ideas_games_content_man_name)
	private TextView hot_ideas_games_content_man_name;
	/**
	 * hot_ideas_games_content_zuoping 点击查看参赛作品
	 */
	@ViewInject(R.id.hot_ideas_games_content_zuoping)
	private TextView hot_ideas_games_content_zuoping;
	/**
	 * hot_ideas_games_content_myman 要求我的成员
	 */
	@ViewInject(R.id.hot_ideas_games_content_myman)
	private RelativeLayout hot_ideas_games_content_myman;
	/**
	 * hot_ideas_games_content_zuoping zuopinnum 参赛作品数
	 */
	@ViewInject(R.id.hot_ideas_games_content_zuoping)
	private TextView zuopinnum;
	/**
	 * collection:2(已经关注)|1(未进行关注)|3(未登录)    id:大赛id 
	 * images:比赛封面图          introduce:比赛介绍         time:征集时间  
	 * memimg:会员头像         nickname:会员昵称         memid:会员id  
	 * zhik:1(智库专家存在)|0(智库专家不存在);不存在智库专家zhiklist返回0 
	 * zhiklist:二维数组           id:会员id        memimage:会员头像  
	 * put:1(关注成员)|0(没有关注成员);不存在关注成员    putlist返回0
	 * putlist:二维数组           id:会员id     memimage:会员头像 zuop参赛作品数量
	 */
	String zhiklist,id,zhik,time,put,zuop,nickname,putlist,introduce,images,collection,memimg,memid;
	String sid;//比赛id
	BitmapUtils bitmapUtils;
	Map<String, Object> map;
	List<Map<String, Object>> listmap;
	AsyncHttpClient client;
	RequestParams params;
	Dialog jiazaidialog;
	String huiyuanid;
	int choose = 0;
	String groupId = null;
	String name = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hot_ideas_games_content);
		ViewUtils.inject(this);
		ActivityManagerApplication.addDestoryActivity(this, 1+"");
		init();
		showdialogup();
		data();
	}
	private void showdialogup() {
		jiazaidialog = new ResDialog(HotIdeasGamesContentActivity.this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void data() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId);
		params.put("sid", sid);
		client.post(MyUrl.bsinger, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response+"OOOOOOOO");
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i==1) {
						
					}else if (i==2) {
						bitmapUtils = new BitmapUtils(getApplicationContext());
						String list = response.getString("list");
						JSONObject object = new JSONObject(list);
						collection = object.getString("collection").toString();
						id = object.getString("id").toString();
						images = object.getString("images").toString();//比赛封面图
						introduce = object.getString("introduce").toString();//比赛内容介绍
						time = object.getString("time").toString();//征集时间
						memimg = object.getString("memimg").toString();//会员头像
						nickname = object.getString("nickname").toString();//会员昵称
						memid = object.getString("memid").toString();//会员id
						zhik = object.getString("zhik").toString();//智库专家
						if (zhik.equals("1")) {
							String lists = object.getString("zhiklist").toString();
							JSONArray array = new JSONArray(lists);
							map = new HashMap<String, Object>();
							listmap = new ArrayList<Map<String,Object>>();
							for (int j = 0; j < array.length(); j++) {
								JSONObject object2 = (JSONObject) array.get(j);
								map = new HashMap<String, Object>();
								map.put("id", object2.getString("id").toString());
								map.put("memimage", object2.getString("memimage").toString());
								listmap.add(map);
							}
							System.out.println(listmap+"SSSSSSSSSSSSSSSS");
							for (int j = 0; j < listmap.size(); j++) {
							if (listmap.size()==1) {
								hot_ideas_games_content_zhikuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_zhikuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
							}else if (listmap.size()==2) {
								hot_ideas_games_content_zhikuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_zhikuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content_zhikuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_zhikuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
							}else if (listmap.size()==3) {
								hot_ideas_games_content_zhikuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_zhikuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content_zhikuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_zhikuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
								hot_ideas_games_content_zhikuman_other3.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_zhikuman_other3, ImageAddress.Stringhead+listmap.get(2).get("memimage").toString());
							}
							}
						}
						put = object.getString("put").toString();//关注会员
						zuop = object.getString("zuop").toString();//参赛作品数量
						if (put.equals("1")) {
							String lists = object.getString("putlist").toString();
							JSONArray array = new JSONArray(lists);
							map = new HashMap<String, Object>();
							listmap = new ArrayList<Map<String,Object>>();
							for (int j = 0; j < array.length(); j++) {
								JSONObject object2 = (JSONObject) array.get(j);
								map = new HashMap<String, Object>();
								map.put("id", object2.getString("id").toString());
								map.put("memimage", object2.getString("memimage").toString());
								listmap.add(map);
							}
							System.out.println(listmap+"EEEEEEEEEEEEEEEEEEEEEEEEEE");
							for (int j = 0; j < listmap.size(); j++) {
							if (listmap.size()==1) {
								hot_ideas_games_content_guanzhuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_guanzhuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
							}else if (listmap.size()==2) {
								hot_ideas_games_content_guanzhuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_guanzhuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content_guanzhuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_guanzhuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
							}else if (listmap.size()==3) {
								hot_ideas_games_content_guanzhuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_guanzhuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content_guanzhuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_guanzhuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
								hot_ideas_games_content_guanzhuman_other3.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content_guanzhuman_other3, ImageAddress.Stringhead+listmap.get(2).get("memimage").toString());
							}
							}
						}
						bitmapUtils.display(hot_ideas_games_content_headimg, ImageAddress.cbit+images);
						hot_ideas_games_content_content.setText(introduce);
						gametime.setText(time);
						zuopinnum.setText("点击查看参赛作品("+zuop+")");
						bitmapUtils.display(hot_ideas_games_content_man_img, ImageAddress.Stringhead+memimg);
						hot_ideas_games_content_man_name.setText(nickname);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(HotIdeasGamesContentActivity.this, errorResponse+"", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String,Object>>();
		intent = getIntent();
		sid = intent.getExtras().getString("sid");
		shezhi.setOnClickListener(this);
		addfriends.setOnClickListener(this);
		guanzhu.setOnClickListener(this);
		zuopin.setOnClickListener(this);
		news.setOnClickListener(this);
		back.setOnClickListener(this);
		guanzhuman.setOnClickListener(this);
		my.setOnClickListener(this);
		hot_ideas_games_content_img5.setOnClickListener(this);
		hot_ideas_games_content_zhikuman_other1.setOnClickListener(this);
		hot_ideas_games_content_zhikuman_other2.setOnClickListener(this);
		hot_ideas_games_content_zhikuman_other3.setOnClickListener(this);
		hot_ideas_games_content_guanzhuman_other1.setOnClickListener(this);
		hot_ideas_games_content_guanzhuman_other2.setOnClickListener(this);
		hot_ideas_games_content_guanzhuman_other3.setOnClickListener(this);
		hot_ideas_games_content_man_img.setOnClickListener(this);
		hot_ideas_games_content_zuoping.setOnClickListener(this);
		hot_ideas_games_content_myman.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hot_ideas_games_content_back:
			finish();
			break;
		case R.id.game_shezhi://比赛设置
			intent = new Intent(HotIdeasGamesContentActivity.this,GameSheZhiActivity.class);
			intent.putExtra("sid",sid);
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_myman://邀请我的成员
			intent = new Intent(HotIdeasGamesContentActivity.this,InviteManActivity.class);
			intent.putExtra("sid",sid);
			intent.putExtra("cla", 1+"");
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_addfriends://添加智库专家
			intent = new Intent(HotIdeasGamesContentActivity.this,InviteManActivity.class);
			intent.putExtra("sid",sid);
			intent.putExtra("cla", 2+"");
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_img3://关注成员
			intent = new Intent(HotIdeasGamesContentActivity.this,GuanZhuMan.class);
			intent.putExtra("sid",sid);
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_img1://关注成员
			intent = new Intent(HotIdeasGamesContentActivity.this,GuanZhuMan.class);
			intent.putExtra("sid",sid);
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_zuopin://参赛作品
			intent = new Intent(HotIdeasGamesContentActivity.this,GameWorksActivity.class);
			intent.putExtra("sid",sid);
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_img5://参赛作品
			intent = new Intent(HotIdeasGamesContentActivity.this,GameWorksActivity.class);
			intent.putExtra("sid",sid);
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_zuoping://参赛作品
			intent = new Intent(HotIdeasGamesContentActivity.this,GameWorksActivity.class);
			intent.putExtra("sid",sid);
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_news://家居创意比赛  进入聊天
			new Thread() {
				public void run() {
					try {
						List<EMGroup> grouplist = EMGroupManager.getInstance()
								.getGroupsFromServer();// 获取群聊列表
						System.out.println(grouplist.size() + "SSSSSSSSSSSSS");
						for (int i = 0; i < grouplist.size(); i++) {
							String groupId = grouplist.get(i).getGroupId();
							System.out.println(groupId + "MMM");
						}
						groupId = grouplist.get(0).getGroupId();
						name = grouplist.get(0).getGroupName();
						// 根据群聊ID从服务器获取群聊信息
						EMGroup group = EMGroupManager.getInstance()
								.getGroupFromServer(groupId);
						List<String> s = group.getMembers();// 获取群成员
						String ss = group.getOwner();// 获取群主
						System.out.println(ss + "LLLLLLLLLLLLLL" + s);
					} catch (EaseMobException e1) {
						e1.printStackTrace();
					}
				};
			}.start();
			if (groupId == null ||name == null) {
				return;
			}
			intent = new Intent(HotIdeasGamesContentActivity.this,ChatActivity.class);
			intent.putExtra("groupId", groupId);
			intent.putExtra("name", name);
			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content_man_img://创办者
			int i = Integer.parseInt(memid);
			if (i==IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContentActivity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
				intent.putExtra("id", memid);
				startActivity(intent);
			}
			break;
		case R.id.hot_ideas_games_content_img7://创办者
			int j = Integer.parseInt(memid);
			if (j==IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContentActivity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
				intent.putExtra("id", memid);
				startActivity(intent);
			}
			break;
		case R.id.hot_ideas_games_content_zhikuman_other1://其他人的信息
//			intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
//			startActivity(intent);
			choose = 0;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content_zhikuman_other2://其他人的信息
//			intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
//			startActivity(intent);
			choose = 1;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content_zhikuman_other3://其他人的信息
//			intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
//			startActivity(intent);
			choose = 2;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content_guanzhuman_other1://其他人的信息
			choose = 0;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content_guanzhuman_other2://其他人的信息
			choose = 1;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content_guanzhuman_other3://其他人的信息
			choose = 2;
			choose(choose);
			break;
		default:
			break;
		}
	}
	private void choose(int i){
		if (listmap.size()==1) {
			huiyuanid = listmap.get(i).get("id").toString();
			if (Integer.parseInt(huiyuanid) == IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContentActivity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
				intent.putExtra("id", huiyuanid);
				startActivity(intent);
			}
		}else if (listmap.size()==2) {
			huiyuanid = listmap.get(i).get("id").toString();
			if (Integer.parseInt(huiyuanid) == IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContentActivity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
				intent.putExtra("id", huiyuanid);
				startActivity(intent);
			}
		}else if (listmap.size()==3) {
			huiyuanid = listmap.get(i).get("id").toString();
			if (Integer.parseInt(huiyuanid) == IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContentActivity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContentActivity.this,OtherDetailsActivity.class);
				intent.putExtra("id", huiyuanid);
				startActivity(intent);
			}
		}
	}
}
