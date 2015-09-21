package com.lsfb.cysj;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.app.Myapplication;
import com.lsfb.cysj.base.BaseActivity;
import com.lsfb.cysj.fragment.HotIdeasGameFragment;
import com.lsfb.cysj.fragment.IdeasFriendsFragment;
import com.lsfb.cysj.fragment.TrendsGameFragment;
import com.lsfb.cysj.utils.SharedPrefsUtil;
import com.lsfb.cysj.view.ListViewForScrollView;
import com.lsfb.cysj.view.ResDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

/**
 * 创意信搜索界面
 * 
 * @author yanwei
 * 
 */
public class SearchActivity extends FragmentActivity implements OnClickListener {
	Intent intent;
	/**
	 * search_button1 我有创意
	 */
	@ViewInject(R.id.search_button1)
	private Button search_button1;
	/**
	 * search_button2 我发起比赛
	 */
	@ViewInject(R.id.search_button2)
	private Button search_button2;
	/**
	 * search_button3 加入智库
	 */
	@ViewInject(R.id.search_button3)
	private Button search_button3;
	/**
	 * search_button4我找创意
	 */
	@ViewInject(R.id.search_button4)
	private Button search_button4;
	/**
	 * search_button5 我要参赛
	 */
	@ViewInject(R.id.search_button5)
	private Button search_button5;
	/**
	 * search_button6 我要禅修
	 */
	@ViewInject(R.id.search_button6)
	private Button search_button6;
	/**
	 * cancel 取消 search_ll
	 */
	@ViewInject(R.id.search_ll)
	private LinearLayout cancel;
	/**
	 * head_search2 search2 搜索
	 */
	@ViewInject(R.id.head_search2)
	private EditText search2;
	@ViewInject(R.id.search_one)
	private LinearLayout search_one;
	@ViewInject(R.id.search_two)
	private LinearLayout search_two;
	@ViewInject(R.id.search_scrollview)
	private ScrollView search_scrollview;
	@ViewInject(R.id.search_msg)
	private TextView search_msg;
	/**
	 * search_xinwen xinwen 新闻
	 */
	@ViewInject(R.id.search_xinwen)
	private LinearLayout xinwen;
	/**
	 * search_zhuanjia zhuanjia 智库专家
	 */
	@ViewInject(R.id.search_zhuanjia)
	private LinearLayout zhuanjia;
	/**
	 * search_dieas ideas 创意世界
	 */
	@ViewInject(R.id.search_dieas)
	private LinearLayout ideas;
	/**
	 * search_friends search_friends 创友圈
	 */
	@ViewInject(R.id.search_friends)
	private LinearLayout search_friends;
	/**
	 * search_games search_games 热门大赛
	 */
	@ViewInject(R.id.search_games)
	private LinearLayout search_games;
	/**
	 * xianxia_game 线下比赛
	 */
	@ViewInject(R.id.xianxia_game)
	private LinearLayout xianxia_game;
	@ViewInject(R.id.search_zhuanjia_view)
	private View view1;
	@ViewInject(R.id.search_dieas_view)
	private View view2;
	@ViewInject(R.id.search_friends_view)
	private View view3;
	@ViewInject(R.id.search_games_view)
	private View view4;
	@ViewInject(R.id.xianxia_game_view)
	private View view5;
	@ViewInject(R.id.search_xinwen_more)
	private TextView more1;
	@ViewInject(R.id.search_zhuanjia_more)
	private TextView more2;
	@ViewInject(R.id.search_dieas_more)
	private TextView more3;
	@ViewInject(R.id.search_friends_more)
	private TextView more4;
	@ViewInject(R.id.search_games_more)
	private TextView more5;
	@ViewInject(R.id.xianxia_game_more)
	private TextView more6;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap1;
	ArrayList<HashMap<String, Object>> listmap2;
	ArrayList<HashMap<String, Object>> listmap3;
	ArrayList<HashMap<String, Object>> listmap4;
	ArrayList<HashMap<String, Object>> listmap5;
	ArrayList<HashMap<String, Object>> listmap6;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	Dialog jiazaidialog;
	BaseAdapter adapter;
	private ListViewForScrollView xinwen_list;
	private ListViewForScrollView zhuanjia_list;
	private ListViewForScrollView ideas_list;
	private ListViewForScrollView friends_list;
	private ListViewForScrollView game_list;
	private ListViewForScrollView xia_list;
	String wenwenmap = null;
	String zhuajiamap = null;
	String ideasmap = null;
	String friendsmap = null;
	String gamemap = null;
	String xianxiamap = null;
	String searchname = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ViewUtils.inject(this);
		init();
		search2.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) (InputMethodManager) SearchActivity.this
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(SearchActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					String name = search2.getText().toString().trim();
					if (name.equals("")) {
						// listmap = new ArrayList<HashMap<String,Object>>();
						// data();
						search_one.setVisibility(View.VISIBLE);
						search_two.setVisibility(View.VISIBLE);
						search_scrollview.setVisibility(View.GONE);
					} else {
						listmap1 = new ArrayList<HashMap<String, Object>>();
						listmap2 = new ArrayList<HashMap<String, Object>>();
						listmap3 = new ArrayList<HashMap<String, Object>>();
						listmap4 = new ArrayList<HashMap<String, Object>>();
						listmap5 = new ArrayList<HashMap<String, Object>>();
						listmap6 = new ArrayList<HashMap<String, Object>>();
						searchename(name);
						searchname = name;
					}
				}
				return false;
			}

		});
	}

	protected void searchename(String name) {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("name", name);
		httpUtils.send(HttpMethod.POST, MyUrl.singer, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						jiazaidialog.dismiss();
						String list = responseInfo.result;
						System.out.println(list);
						try {
							JSONObject object = new JSONObject(list);
							String news = object.getString("news").toString();
							String zhi = object.getString("zhi").toString();
							String chuang = object.getString("chuang")
									.toString();
							String you = object.getString("you").toString();
							String bit = object.getString("bit").toString();
							String xia = object.getString("xia").toString();
							if (news.equals("1") && zhi.equals("1")
									&& chuang.equals("1") && you.equals("1")
									&& bit.equals("1")) {
								search_one.setVisibility(View.GONE);
								search_two.setVisibility(View.GONE);
								search_msg.setVisibility(View.VISIBLE);
								search_scrollview.setVisibility(View.GONE);
							} else {
								search_one.setVisibility(View.GONE);
								search_two.setVisibility(View.GONE);
								search_msg.setVisibility(View.GONE);
								search_scrollview.setVisibility(View.VISIBLE);
								if (news.equals("1")) {
									// news:2有值存在|1无值存在(新闻)
									xinwen.setVisibility(View.GONE);
									view1.setVisibility(View.GONE);
								} else {
									xinwen.setVisibility(View.VISIBLE);
									view1.setVisibility(View.VISIBLE);
									String newscount = object.getString(
											"newscount").toString();
									int a = Integer.parseInt(newscount);
									String newslist = object.getString(
											"newslist").toString();
									xinwen(newslist, a);
									if (a > 10) {
										more1.setVisibility(View.VISIBLE);
									} else {
										more1.setVisibility(View.GONE);
									}
								}
								if (zhi.equals("1")) {
									// zhi: 2有值存在|1无值存在(智库专家)
									zhuanjia.setVisibility(View.GONE);
									view2.setVisibility(View.GONE);
								} else {
									zhuanjia.setVisibility(View.VISIBLE);
									view2.setVisibility(View.VISIBLE);
									String zhicount = object.getString(
											"zhicount").toString();
									int a = Integer.parseInt(zhicount);
									String zhilist = object
											.getString("zhilist").toString();
									zhuanjia(zhilist, a);
									if (a > 10) {
										more2.setVisibility(View.VISIBLE);
									} else {
										more2.setVisibility(View.GONE);
									}
								}
								if (chuang.equals("1")) {
									// chuang:2有值存在|1无值存在(创意世界)
									ideas.setVisibility(View.GONE);
									view3.setVisibility(View.GONE);
								} else {
									ideas.setVisibility(View.VISIBLE);
									view3.setVisibility(View.VISIBLE);
									String chuangcount = object.getString(
											"chuangcount").toString();
									int a = Integer.parseInt(chuangcount);
									String chuanglist = object.getString(
											"chuanglist").toString();
									chuangyi(chuanglist, a);
									if (a > 10) {
										more3.setVisibility(View.VISIBLE);
									} else {
										more3.setVisibility(View.GONE);
									}
								}
								if (you.equals("1")) {
									// you:2有值存在|1无值存在(创友圈)
									search_friends.setVisibility(View.GONE);
									view4.setVisibility(View.GONE);
								} else {
									search_friends.setVisibility(View.VISIBLE);
									view4.setVisibility(View.VISIBLE);
									String youcount = object.getString(
											"youcount").toString();
									int a = Integer.parseInt(youcount);
									String youlist = object
											.getString("youlist").toString();
									you(youlist, a);
									if (a > 10) {
										more4.setVisibility(View.VISIBLE);
									} else {
										more4.setVisibility(View.GONE);
									}
								}
								if (bit.equals("1")) {
									// bit:2有值存在|1无值存在(热门大赛)
									search_games.setVisibility(View.GONE);
									view5.setVisibility(View.GONE);
								} else {
									search_games.setVisibility(View.VISIBLE);
									view5.setVisibility(View.VISIBLE);
									String bitcount = object.getString(
											"bitcount").toString();
									int a = Integer.parseInt(bitcount);
									String bitlist = object
											.getString("bitlist").toString();
									games(bitlist, a);
									if (a > 10) {
										more5.setVisibility(View.VISIBLE);
									} else {
										more5.setVisibility(View.GONE);
									}
								}
								if (xia.equals("1")) {
									// bit:2有值存在|1无值存在(热门大赛)
									xianxia_game.setVisibility(View.GONE);
									view5.setVisibility(View.GONE);
								} else {
									xianxia_game.setVisibility(View.VISIBLE);
									view5.setVisibility(View.VISIBLE);
									String xiacount = object.getString(
											"xiacount").toString();
									int a = Integer.parseInt(xiacount);
									String xialist = object
											.getString("xialist").toString();
									xianxiagames(xialist, a);
									if (a > 10) {
										more6.setVisibility(View.VISIBLE);
									} else {
										more6.setVisibility(View.GONE);
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void xianxiagames(String xialist, int a) {
		listmap6 = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(xialist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("title", object.getString("title").toString());
				map.put("time", object.getString("time").toString());
				map.put("image", object.getString("image").toString());
				listmap6.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		xianxiamap = xialist;
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.zhikuzhuanjia_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.zhikuzhuanjia_img);
					holder.name = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_name);
					holder.time = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String img = listmap6.get(position).get("image").toString();
				if (img.equals("0")) {

				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, img);
				}
				holder.name.setText(listmap6.get(position).get("title")
						.toString());
				holder.time.setText(listmap6.get(position).get("time")
						.toString());
				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public int getCount() {
				return listmap6.size();
			}
		};
		xia_list.setAdapter(adapter);
	}

	protected void games(String bitlist, int a) {
		listmap5 = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(bitlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("title", object.getString("title").toString());
				map.put("time", object.getString("time").toString());
				map.put("image", object.getString("image").toString());
				listmap5.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		gamemap = bitlist;
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.zhikuzhuanjia_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.zhikuzhuanjia_img);
					holder.name = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_name);
					holder.time = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String img = listmap5.get(position).get("image").toString();
				if (img.equals("0")) {

				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, img);
				}
				holder.name.setText(listmap5.get(position).get("title")
						.toString());
				holder.time.setText(listmap5.get(position).get("time")
						.toString());
				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public int getCount() {
				return listmap5.size();
			}
		};
		game_list.setAdapter(adapter);
	}

	protected void you(String youlist, int a) {
		listmap4 = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(youlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("name", object.getString("name").toString());
				map.put("content", object.getString("content").toString());
				map.put("time", object.getString("time").toString());
				map.put("image", object.getString("image").toString());
				listmap4.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(listmap4 + "you");
		friendsmap = youlist;
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.zhikuzhuanjia_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.zhikuzhuanjia_img);
					holder.name = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_name);
					holder.time = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String img = listmap4.get(position).get("image").toString();
				if (img.equals("0")) {

				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, img);
				}
				holder.name.setText(listmap4.get(position).get("content")
						.toString());
				holder.time.setText(listmap4.get(position).get("time")
						.toString());
				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public int getCount() {
				return listmap4.size();
			}
		};
		friends_list.setAdapter(adapter);
	}

	protected void chuangyi(String chuanglist, int a) {
		listmap3 = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(chuanglist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("title", object.getString("title").toString());
				map.put("time", object.getString("time").toString());
				map.put("image", object.getString("image").toString());
				listmap3.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(listmap3 + "chuangyi");
		ideasmap = chuanglist;
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.zhikuzhuanjia_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.zhikuzhuanjia_img);
					holder.name = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_name);
					holder.time = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String img = listmap3.get(position).get("image").toString();
				if (img.equals("0")) {

				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, img);
				}
				holder.name.setText(listmap3.get(position).get("title")
						.toString());
				holder.time.setText(listmap3.get(position).get("time")
						.toString());
				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public int getCount() {
				return listmap3.size();
			}
		};
		ideas_list.setAdapter(adapter);
	}

	protected void zhuanjia(String zhilist, int a) {
		listmap2 = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(zhilist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("name", object.getString("name").toString());
				map.put("image", object.getString("image").toString());
				listmap2.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		zhuajiamap = zhilist;
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.zhikuzhuanjia_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.zhikuzhuanjia_img);
					holder.name = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_name);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String img = listmap2.get(position).get("image").toString();
				if (img.equals("0")) {

				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, img);
				}
				holder.name.setText(listmap2.get(position).get("name")
						.toString());
				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public int getCount() {
				return listmap2.size();
			}
		};
		zhuanjia_list.setAdapter(adapter);
	}

	protected void xinwen(String newslist, int a) {
		listmap1 = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(newslist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("title", object.getString("title").toString());
				map.put("time", object.getString("time").toString());
				map.put("image", object.getString("image").toString());
				listmap1.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		xianxiamap = newslist;
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.zhikuzhuanjia_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.zhikuzhuanjia_img);
					holder.name = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_name);
					holder.time = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String img = listmap1.get(position).get("image").toString();
				if (img.equals("0")) {

				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, img);
				}
				holder.name.setText(listmap1.get(position).get("title")
						.toString());
				holder.time.setText(listmap1.get(position).get("time")
						.toString());
				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public int getCount() {
				return listmap1.size();
			}
		};
		xinwen_list.setAdapter(adapter);
	}

	static class ViewHolder {
		ImageView img;
		TextView name;
		TextView time;
		TextView text;
		TextView content;
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap1 = new ArrayList<HashMap<String, Object>>();
		listmap2 = new ArrayList<HashMap<String, Object>>();
		listmap3 = new ArrayList<HashMap<String, Object>>();
		listmap4 = new ArrayList<HashMap<String, Object>>();
		listmap5 = new ArrayList<HashMap<String, Object>>();
		listmap6 = new ArrayList<HashMap<String, Object>>();
		search_button1.setOnClickListener(this);
		search_button2.setOnClickListener(this);
		search_button3.setOnClickListener(this);
		search_button4.setOnClickListener(this);
		search_button5.setOnClickListener(this);
		search_button6.setOnClickListener(this);
		cancel.setOnClickListener(this);

		xinwen_list = (ListViewForScrollView) findViewById(R.id.search_xinwen_list);
		// 专家
		zhuanjia_list = (ListViewForScrollView) findViewById(R.id.search_zhuanjia_list);
		// 创意世界
		ideas_list = (ListViewForScrollView) findViewById(R.id.search_dieas_list);
		// 创友圈
		friends_list = (ListViewForScrollView) findViewById(R.id.search_friends_list);
		// 热门大赛
		game_list = (ListViewForScrollView) findViewById(R.id.search_games_list);
		// 线下大赛
		xia_list = (ListViewForScrollView) findViewById(R.id.xianxia_game_list);

		// 添加点击事件
//		OnItemClick itemClick = new OnItemClick();
		xinwen_list.setOnItemClickListener(new XinwenClick());
		zhuanjia_list.setOnItemClickListener(new ZhuanjiaClick());
		ideas_list.setOnItemClickListener(new IdeasClick());
		friends_list.setOnItemClickListener(new FriendsClick());
		game_list.setOnItemClickListener(new GameClick());
		xia_list.setOnItemClickListener(new XiaClick());

		more1.setOnClickListener(this);
		more2.setOnClickListener(this);
		more3.setOnClickListener(this);
		more4.setOnClickListener(this);
		more5.setOnClickListener(this);
		more6.setOnClickListener(this);
	}
	
	class XinwenClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			Intent 	intent=new Intent(Myapplication.context,HotNews.class);
			startActivity(intent);
		}
	}	
	class ZhuanjiaClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			Intent 	intent=new Intent(Myapplication.context,OtherDetailsActivity.class);
			intent.putExtra("id", listmap2.get(position).get("id").toString());
			startActivity(intent);
			
		}
	}	
	class IdeasClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			Intent 	intent=new Intent(Myapplication.context,CreativeDetailsActivity.class);
			intent.putExtra("id", listmap3.get(position).get("id").toString());
			startActivity(intent);
			
		}
	}	
	class GameClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			Intent 	intent=new Intent(Myapplication.context,HotIdeasGamesContentActivity.class);
			intent.putExtra("sid", listmap5.get(position).get("id").toString());
			startActivity(intent);
			
		}
	}	
	class FriendsClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			Intent 	intent=new Intent(Myapplication.context,HomeActivity.class);
			intent.putExtra("pageto", 3);
			 SharedPrefsUtil.putValue(Myapplication.context, "pageto", 3);
			HomeActivity.viewPager.setCurrentItem(2);
			startActivity(intent);
			
		}
	}	
	class XiaClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			Intent 	intent=new Intent(Myapplication.context,GameDetailsActivity.class);
			intent.putExtra("sid", listmap6.get(position).get("id").toString());
			intent.putExtra("title", listmap6.get(position).get("title").toString());
			startActivity(intent);
			
		}
	}	
	

//	class OnItemClick implements OnItemClickListener {
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
//			Intent intent=null;
//			switch (v.getId()) { 
//			case R.id.search_xinwen_list:
//				intent=new Intent(Myapplication.context,HotNews.class);
//				
//				break;
//			case R.id.search_zhuanjia_list:
//				intent=new Intent(Myapplication.context,OtherDetailsActivity.class);
//				intent.putExtra("id", listmap2.get(position).get("id").toString());
//				break;
//			case R.id.search_dieas_list:
//				intent=new Intent(Myapplication.context,CreativeDetailsActivity.class);
//				intent.putExtra("id", listmap3.get(position).get("id").toString());
//				break;
//			case R.id.search_games_list:
//				intent=new Intent(Myapplication.context,HotIdeasGamesContentActivity.class);
//				intent.putExtra("sid", listmap5.get(position).get("id").toString());
//				break;
//			case R.id.xianxia_game_list:
//				intent=new Intent(Myapplication.context,GameDetailsActivity.class);
//				intent.putExtra("sid", listmap6.get(position).get("id").toString());
//				intent.putExtra("title", listmap6.get(position).get("title").toString());
//				 
//				break;
//			case R.id.search_friends_list:
//				intent=new Intent(Myapplication.context,HomeActivity.class);
//				intent.putExtra("pageto", 3);
//				break; 
//
//			default:
//				break;
//			}
//			if(null!=intent)
//			startActivity(intent);
//
//		}
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if (IsTrue.dongtaigame = true) {
		// intent = new Intent(SearchActivity.this,HomeActivity.class);
		// startActivity(intent);
		// IsTrue.exit = false;
		// }
		// if (IsTrue.search = false) {
		// finish();
		// }
		IsTrue.exit = false;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_ll:// 取消
			finish();
			break;
		case R.id.xianxia_game_more:// 线下大赛更多
			intent = new Intent(SearchActivity.this, MoreSearchActivity.class);
			intent.putExtra("i", 6);
			intent.putExtra("msg", xianxiamap);
			intent.putExtra("searchname", searchname);
			startActivity(intent);
			break;
		case R.id.search_games_more:// 热门大赛更多
			intent = new Intent(SearchActivity.this, MoreSearchActivity.class);
			intent.putExtra("i", 5);
			intent.putExtra("msg", gamemap);
			intent.putExtra("searchname", searchname);
			startActivity(intent);
			break;
		case R.id.search_friends_more:// 创友圈更多
			intent = new Intent(SearchActivity.this, MoreSearchActivity.class);
			intent.putExtra("i", 4);
			intent.putExtra("msg", friendsmap);
			intent.putExtra("searchname", searchname);
			startActivity(intent);
			break;
		case R.id.search_dieas_more:// 创意世界更多
			intent = new Intent(SearchActivity.this, MoreSearchActivity.class);
			intent.putExtra("i", 3);
			intent.putExtra("msg", ideasmap);
			intent.putExtra("searchname", searchname);
			startActivity(intent);
			break;
		case R.id.search_zhuanjia_more:// 智库专家更多
			intent = new Intent(SearchActivity.this, MoreSearchActivity.class);
			intent.putExtra("i", 2);
			intent.putExtra("msg", zhuajiamap);
			intent.putExtra("searchname", searchname);
			startActivity(intent);
			break;
		case R.id.search_xinwen_more:// 新闻更多
			intent = new Intent(SearchActivity.this, MoreSearchActivity.class);
			intent.putExtra("i", 1);
			intent.putExtra("msg", xianxiamap);
			intent.putExtra("searchname", searchname);
			startActivity(intent);
			break;
		case R.id.search_button3:// 加入智库
			intent = new Intent(SearchActivity.this, HotZhiKu.class);
			startActivity(intent);
			break;
		case R.id.search_button1:// 发布创意
			if (IsTrue.userId == 0) {
				IsTrue.tabnum = 4;// 发布创意
				IsTrue.fabuchuangyi = 1;
				IsTrue.woyaochanxiu = 0;
				IsTrue.fabubisai = 0;
				finish();
				break;
			}
			intent = new Intent(SearchActivity.this, ReleaseIdeasActivity.class);
			startActivity(intent);
			break;
		case R.id.search_button2:// 我发起比赛
			if (IsTrue.userId == 0) {
				IsTrue.tabnum = 6;// 发起比赛
				IsTrue.fabubisai = 1;
				IsTrue.fabuchuangyi = 0;
				IsTrue.woyaochanxiu = 0;
				finish();
				break;
			}
			intent = new Intent(SearchActivity.this, ReleaseGameActivity.class);
			startActivity(intent);
			break;
		case R.id.search_button5:// 我要参赛 IdeasWorldActivity
			finish();
			// intent = new Intent(SearchActivity.this,HomeActivity.class);
			IsTrue.tabnum = 3;
			IsTrue.exit = false;
			// IsTrue.dongtaigame = true;
			// startActivity(intent);
			break;
		case R.id.search_button6:// 我要禅修
			if (IsTrue.userId == 0) {
				IsTrue.tabnum = 5;// 我要禅修
				IsTrue.woyaochanxiu = 1;
				IsTrue.fabuchuangyi = 0;
				IsTrue.fabubisai = 0;
				finish();
				break;
			}
			intent = new Intent(SearchActivity.this, MeditationActivity.class);
			startActivity(intent);
			break;
		// case R.id.search_button4://创意世界大赛
		// intent = new
		// Intent(SearchActivity.this,IdeasWorldGameActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.search_button4://创意世界大赛详情
		// intent = new Intent(SearchActivity.this,GameDetailsActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.search_button4://创意世界智库成员
		// intent = new Intent(SearchActivity.this,IdeasZhiKuMan.class);
		// startActivity(intent);
		// break;
		default:
			break;
		}
	}
}
