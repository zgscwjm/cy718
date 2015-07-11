package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

public class HotIdeasGamesContent2Activity extends FragmentActivity implements OnClickListener{
	@ViewInject(R.id.hot_ideas_games_content2_back)
	private LinearLayout back;
	/**
	 * hot_ideas_games_content2_btn 
	 * guanzhugame 关注比赛
	 */
	@ViewInject(R.id.hot_ideas_games_content2_btn)
	private Button guanzhugame;
	@ViewInject(R.id.hot_ideas_games_content2_man_my)
	private LinearLayout wode;
	/**
	 * hot_ideas_games_content2_zhikuman_other1 智库专家1
	 */
	@ViewInject(R.id.hot_ideas_games_content2_zhikuman_other1)
	private ImageView hot_ideas_games_content2_zhikuman_other1;
	/**
	 * hot_ideas_games_content2_zhikuman_other2 智库专家2
	 */
	@ViewInject(R.id.hot_ideas_games_content2_zhikuman_other2)
	private ImageView hot_ideas_games_content2_zhikuman_other2;
	/**
	 * hot_ideas_games_content2_zhikuman_other3 智库专家3
	 */
	@ViewInject(R.id.hot_ideas_games_content2_zhikuman_other3)
	private ImageView hot_ideas_games_content2_zhikuman_other3;
	/**
	 * hot_ideas_games_content2_guanzhuman_other1 关注成员1
	 */
	@ViewInject(R.id.hot_ideas_games_content2_guanzhuman_other1)
	private ImageView hot_ideas_games_content2_guanzhuman_other1;
	/**
	 * hot_ideas_games_content2_guanzhuman_other2 关注成员2
	 */
	@ViewInject(R.id.hot_ideas_games_content2_guanzhuman_other2)
	private ImageView hot_ideas_games_content2_guanzhuman_other2;
	/**
	 * hot_ideas_games_content2_guanzhuman_other3 关注成员3
	 */
	@ViewInject(R.id.hot_ideas_games_content2_guanzhuman_other3)
	private ImageView hot_ideas_games_content2_guanzhuman_other3;
	/**
	 * hot_ideas_games_content2_headimg 上面展示图
	 */
	@ViewInject(R.id.hot_ideas_games_content2_headimg)
	private ImageView hot_ideas_games_content2_headimg;
	/**
	 * hot_ideas_games_content2_content 比赛内容
	 */
	@ViewInject(R.id.hot_ideas_games_content2_content)
	private TextView hot_ideas_games_content2_content;
	/**
	 * hot_ideas_games_content2_gametime 
	 * gametime 比赛时间
	 */
	@ViewInject(R.id.hot_ideas_games_content2_gametime)
	private TextView gametime;
	/**
	 * hot_ideas_games_content2_man_img 创办者头像
	 */
	@ViewInject(R.id.hot_ideas_games_content2_man_img)
	private ImageView hot_ideas_games_content2_man_img;
	/**
	 * hot_ideas_games_content2_man_name 创办者名字
	 */
	@ViewInject(R.id.hot_ideas_games_content2_man_name)
	private TextView hot_ideas_games_content2_man_name;
	/**
	 * hot_ideas_games_num zuopinnum 作品数量
	 */
	@ViewInject(R.id.hot_ideas_games_num)
	private TextView zuopinnum;
	Intent intent;
	Dialog jiazaidialog;
	AsyncHttpClient client;
	RequestParams params;
	String sid;//比赛id
	BitmapUtils bitmapUtils;
	Map<String, Object> map;
	List<Map<String, Object>> listmap;
	/**
	 * collection:2(已经关注)|1(未进行关注)|3(未登录)    id:大赛id 
	 * images:比赛封面图          introduce:比赛介绍         time:征集时间  
	 * memimg:会员头像         nickname:会员昵称         memid:会员id  
	 * zhik:1(智库专家存在)|0(智库专家不存在);不存在智库专家zhiklist返回0 
	 * zhiklist:二维数组           id:会员id        memimage:会员头像  
	 * put:1(关注成员)|0(没有关注成员);不存在关注成员    putlist返回0
	 * putlist:二维数组           id:会员id     memimage:会员头像  zuop参赛作品数量
	 */
	String zhiklist,id,zhik,time,put,nickname,putlist,introduce,images,collection,memimg,memid,zuop;
	String huiyuanid;
	int choose = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hot_ideas_games_content2);
		ViewUtils.inject(this);
		init();
		showdialogup();
		initdata();
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(HotIdeasGamesContent2Activity.this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void initdata() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", sid);
		params.put("id", IsTrue.userId);
		client.post(MyUrl.bsinger, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response+"JJJJJJJJJJJ");
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
							System.out.println(listmap+"EEEEEEEEEEEEEEEEEEEEEEEEEE");
							for (int j = 0; j < listmap.size(); j++) {
							if (listmap.size()==1) {
								hot_ideas_games_content2_zhikuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_zhikuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
							}else if (listmap.size()==2) {
								hot_ideas_games_content2_zhikuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_zhikuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content2_zhikuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_zhikuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
							}else if (listmap.size()==3) {
								hot_ideas_games_content2_zhikuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_zhikuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content2_zhikuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_zhikuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
								hot_ideas_games_content2_zhikuman_other3.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_zhikuman_other3, ImageAddress.Stringhead+listmap.get(2).get("memimage").toString());
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
							System.out.println(listmap+"VVVVVVVVVVVVVVVVVVV");
							for (int j = 0; j < listmap.size(); j++) {
							if (listmap.size()==1) {
								hot_ideas_games_content2_guanzhuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_guanzhuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
							}else if (listmap.size()==2) {
								hot_ideas_games_content2_guanzhuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_guanzhuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content2_guanzhuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_guanzhuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
							}else if (listmap.size()==3) {
								hot_ideas_games_content2_guanzhuman_other1.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_guanzhuman_other1, ImageAddress.Stringhead+listmap.get(0).get("memimage").toString());
								hot_ideas_games_content2_guanzhuman_other2.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_guanzhuman_other2, ImageAddress.Stringhead+listmap.get(1).get("memimage").toString());
								hot_ideas_games_content2_guanzhuman_other3.setVisibility(View.VISIBLE);
								bitmapUtils.display(hot_ideas_games_content2_guanzhuman_other3, ImageAddress.Stringhead+listmap.get(2).get("memimage").toString());
							}
							}
						}
						bitmapUtils.display(hot_ideas_games_content2_headimg, ImageAddress.cbit+images);
						hot_ideas_games_content2_content.setText(introduce);
						gametime.setText(time);
						zuopinnum.setText("点击查看参赛作品("+zuop+")");
						bitmapUtils.display(hot_ideas_games_content2_man_img, ImageAddress.Stringhead+memimg);
						hot_ideas_games_content2_man_name.setText(nickname);
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
				jiazaidialog.dismiss();
				Toast.makeText(HotIdeasGamesContent2Activity.this, errorResponse+"", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String,Object>>();
		intent = getIntent();
		sid = intent.getExtras().getString("sid");
		back.setOnClickListener(this);
		guanzhugame.setOnClickListener(this);
		wode.setOnClickListener(this);
		hot_ideas_games_content2_zhikuman_other1.setOnClickListener(this);
		hot_ideas_games_content2_zhikuman_other2.setOnClickListener(this);
		hot_ideas_games_content2_zhikuman_other3.setOnClickListener(this);
		hot_ideas_games_content2_guanzhuman_other1.setOnClickListener(this);
		hot_ideas_games_content2_guanzhuman_other2.setOnClickListener(this);
		hot_ideas_games_content2_guanzhuman_other3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hot_ideas_games_content2_back:
			finish();
			break;
		case R.id.hot_ideas_games_content2_btn://关注的比赛
			if (IsTrue.userId==0) {
				Toast.makeText(HotIdeasGamesContent2Activity.this, "您还未登陆", Toast.LENGTH_SHORT).show();
//				IsTrue.guanzhugame = 1;
//				intent = new Intent(HotIdeasGamesContent2Activity.this,HomeActivity.class);
//				startActivity(intent);
//				finish();
			}else {
				guanzhu();
			}
//			intent = new Intent(HotIdeasGamesContent2Activity.this,ChatActivity.class);
//			startActivity(intent);
			break;
		case R.id.hot_ideas_games_content2_man_my://创办者
			int i = Integer.parseInt(memid);
			if (i==IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContent2Activity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContent2Activity.this,OtherDetailsActivity.class);
				intent.putExtra("id", memid);
				startActivity(intent);
			}
			break;
		case R.id.hot_ideas_games_content2_zhikuman_other1://其他的人
//			intent = new Intent(HotIdeasGamesContent2Activity.this,OtherDetailsActivity.class);
//			startActivity(intent);
			choose = 0;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content2_zhikuman_other2://其他的人
//			intent = new Intent(HotIdeasGamesContent2Activity.this,OtherDetailsActivity.class);
//			startActivity(intent);
			choose = 1;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content2_zhikuman_other3://其他的人
//			intent = new Intent(HotIdeasGamesContent2Activity.this,OtherDetailsActivity.class);
//			startActivity(intent);
			choose = 2;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content2_guanzhuman_other1://其他的人
			choose = 0;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content2_guanzhuman_other2://其他的人
			choose = 1;
			choose(choose);
			break;
		case R.id.hot_ideas_games_content2_guanzhuman_other3://其他的人
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
				intent = new Intent(HotIdeasGamesContent2Activity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContent2Activity.this,OtherDetailsActivity.class);
				intent.putExtra("id", huiyuanid);
				startActivity(intent);
			}
		}else if (listmap.size()==2) {
			huiyuanid = listmap.get(i).get("id").toString();
			if (Integer.parseInt(huiyuanid) == IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContent2Activity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContent2Activity.this,OtherDetailsActivity.class);
				intent.putExtra("id", huiyuanid);
				startActivity(intent);
			}
		}else if (listmap.size()==3) {
			huiyuanid = listmap.get(i).get("id").toString();
			if (Integer.parseInt(huiyuanid) == IsTrue.userId) {
				intent = new Intent(HotIdeasGamesContent2Activity.this,MyDetailsActivity.class);
				startActivity(intent);
			}else {
				intent = new Intent(HotIdeasGamesContent2Activity.this,OtherDetailsActivity.class);
				intent.putExtra("id", huiyuanid);
				startActivity(intent);
			}
		}
	}
	private void guanzhu() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId);
		params.put("sid", sid);
		client.post(MyUrl.gzbit, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					if (num.equals("1")) {
						Toast.makeText(HotIdeasGamesContent2Activity.this, "关注失败", Toast.LENGTH_SHORT).show();
					}else if (num.equals("2")) {
						intent = new Intent(HotIdeasGamesContent2Activity.this,HotIdeasGamesContentActivity.class);
						intent.putExtra("sid", sid);
						startActivity(intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(HotIdeasGamesContent2Activity.this, errorResponse.toString(), Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
}
