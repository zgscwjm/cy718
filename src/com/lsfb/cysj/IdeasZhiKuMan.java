package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.ChairmanAdapter;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.model.ChairmanModel;
import com.lsfb.cysj.view.ResDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 创意世界领袖智库成员
 * @author Administrator
 *
 */
public class IdeasZhiKuMan extends FragmentActivity implements OnClickListener {
	Context context;
	private static String[] nums = new String[] { "222", "223", "224", "225",
			"226", "227", "228" };
	/**
	 * ideas_zhiku_man_chairman chairman 主席团
	 */
	@ViewInject(R.id.ideas_zhiku_man_chairman)
	private ListView chairman;
	/**
	 * ideas_zhiku_man_director director 理事会
	 */
	@ViewInject(R.id.ideas_zhiku_man_director)
	private ListView director;
	/**
	 * ideas_zhiku_man_counselor counselor 顾问团
	 */
	@ViewInject(R.id.ideas_zhiku_man_counselor)
	private ListView counselor;
	/**
	 * ideas_zhiku_man_member member 成员
	 */
	@ViewInject(R.id.ideas_zhiku_man_member)
	private ListView member;
	/**
	 * ideas_zhiku_man_scrollview scrollView
	 */
	@ViewInject(R.id.ideas_zhiku_man_scrollview)
	private ScrollView scrollView;
	/**
	 * ideas_zhiku_man_list list 筛选之后显示
	 */
	@ViewInject(R.id.ideas_zhiku_man_list)
	private ListView list;
	@ViewInject(R.id.ideas_zhiku_man_back)
	private LinearLayout back;
	@ViewInject(R.id.shaixuan)
	private LinearLayout shaixuan;
	PopupWindow popupwindow;
	PopupWindow popupwindow2;
	Intent intent;
	BaseAdapter listadapter;
	BaseAdapter adapter1;
	BaseAdapter adapter2;
	BaseAdapter adapter3;
	BaseAdapter adapter4;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	String tid;// 智库id
	Dialog jiazaidialog;

	// ChairmanAdapter chairmanAdapter;
	// List<ChairmanModel> chairmanModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideas_zhiku_man);
		ViewUtils.inject(this);
		init();
		initdata();
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void initdata() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("sid", tid);
		httpUtils.send(HttpMethod.POST, MyUrl.thlists, params,
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
							String zx = object.getString("zx").toString();
							if (zx.equals("2")) {
								String zxlist = object.getString("zxlist")
										.toString();
								datechairman(zxlist);// 主席团
							}
							String ls = object.getString("ls").toString();
							if (ls.equals("2")) {
								String lslist = object.getString("lslist")
										.toString();
								datedirector(lslist);// 理事会
							}
							String zj = object.getString("zj").toString();
							if (zj.equals("2")) {
								String zjlist = object.getString("zjlist")
										.toString();
								datecounselor(zjlist);// 顾问团
							}
							String cy = object.getString("cy").toString();
							if (cy.equals("2")) {
								String cylist = object.getString("cylist")
										.toString();
								datemember(cylist);// 成员
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	protected void onResume() {
		scrollView.smoothScrollBy(0, 0);// 不让listview加载最上边
		super.onResume();
	}

	// 成员
	private void datemember(String cylist) {
		// chairmanModel = new ArrayList<ChairmanModel>();
		// chairmanAdapter = new ChairmanAdapter(this);
		// chairman.setAdapter(chairmanAdapter);
		// chairmanAdapter.setData(chairmanModel);
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(cylist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("name", object.getString("name").toString());
				map.put("image", object.getString("image").toString());
				map.put("index", object.getString("index").toString());
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter4 = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.ideas_world_man, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.ideas_world_man_img);
					holder.name = (TextView) view
							.findViewById(R.id.ideas_world_man_item_text);
					holder.num = (TextView) view
							.findViewById(R.id.ideas_world_man_item_num);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				bitmapUtils = new BitmapUtils(getApplicationContext());
				bitmapUtils.display(holder.img, ImageAddress.Stringhead
						+ listmap.get(position).get("image").toString());
				holder.name.setText(listmap.get(position).get("name")
						.toString());
				holder.num.setText(listmap.get(position).get("index")
						.toString());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent = new Intent(IdeasZhiKuMan.this,
								OtherDetailsActivity.class);
						intent.putExtra("id", listmap.get(position).get("id")
								.toString());
						startActivity(intent);
					}
				});
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return nums.length;
			}
		};
		member.setAdapter(adapter4);
	}

	// 顾问团
	private void datecounselor(String zjlist) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(zjlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("name", object.getString("name").toString());
				map.put("image", object.getString("image").toString());
				map.put("index", object.getString("index").toString());
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter3 = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.ideas_world_man, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.ideas_world_man_img);
					holder.name = (TextView) view
							.findViewById(R.id.ideas_world_man_item_text);
					holder.num = (TextView) view
							.findViewById(R.id.ideas_world_man_item_num);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				bitmapUtils = new BitmapUtils(getApplicationContext());
				bitmapUtils.display(holder.img, ImageAddress.Stringhead
						+ listmap.get(position).get("image").toString());
				holder.name.setText(listmap.get(position).get("name")
						.toString());
				holder.num.setText(listmap.get(position).get("index")
						.toString());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent = new Intent(IdeasZhiKuMan.this,
								OtherDetailsActivity.class);
						intent.putExtra("id", listmap.get(position).get("id")
								.toString());
						startActivity(intent);
					}
				});
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		counselor.setAdapter(adapter3);
	}

	// 理事会
	private void datedirector(String lslist) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(lslist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("name", object.getString("name").toString());
				map.put("image", object.getString("image").toString());
				map.put("index", object.getString("index").toString());
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter2 = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.ideas_world_man, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.ideas_world_man_img);
					holder.name = (TextView) view
							.findViewById(R.id.ideas_world_man_item_text);
					holder.num = (TextView) view
							.findViewById(R.id.ideas_world_man_item_num);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				bitmapUtils = new BitmapUtils(getApplicationContext());
				bitmapUtils.display(holder.img, ImageAddress.Stringhead
						+ listmap.get(position).get("image").toString());
				holder.name.setText(listmap.get(position).get("name")
						.toString());
				holder.num.setText(listmap.get(position).get("index")
						.toString());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent = new Intent(IdeasZhiKuMan.this,
								OtherDetailsActivity.class);
						intent.putExtra("id", listmap.get(position).get("id")
								.toString());
						startActivity(intent);
					}
				});
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		director.setAdapter(adapter2);
	}

	// 主席团
	private void datechairman(String zxlist) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(zxlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("name", object.getString("name").toString());
				map.put("image", object.getString("image").toString());
				map.put("index", object.getString("index").toString());
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter1 = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.ideas_world_man, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.ideas_world_man_img);
					holder.name = (TextView) view
							.findViewById(R.id.ideas_world_man_item_text);
					holder.num = (TextView) view
							.findViewById(R.id.ideas_world_man_item_num);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				bitmapUtils = new BitmapUtils(getApplicationContext());
				bitmapUtils.display(holder.img, ImageAddress.Stringhead
						+ listmap.get(position).get("image").toString());
				holder.name.setText(listmap.get(position).get("name")
						.toString());
				holder.num.setText(listmap.get(position).get("index")
						.toString());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent = new Intent(IdeasZhiKuMan.this,
								OtherDetailsActivity.class);
						intent.putExtra("id", listmap.get(position).get("id")
								.toString());
						startActivity(intent);
					}
				});
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		chairman.setAdapter(adapter1);
	}

	static class ViewHolder {
		ImageView img;
		TextView name;
		TextView type;
		TextView num;
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		intent = getIntent();
		tid = intent.getExtras().getString("zhikuid").toString();
		back.setOnClickListener(this);
		shaixuan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ideas_zhiku_man_back:
			finish();
			break;
		case R.id.shaixuan:// 筛选弹窗
//			if (null != popupwindow2) {
//				popupwindow2.dismiss();
//				return;
//			} else {
				initPopuptWindow();
				popupwindow2.showAsDropDown(back, 0, 0);
//			}
			break;
		default:
			break;
		}
	}

	private void initPopuptWindow() {
		// 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(
				R.layout.shaixuanpopuptwindow, null, false);
		customView.setFocusable(true); // 这个很重要
		customView.setFocusableInTouchMode(true);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow2 = new PopupWindow(customView, 200,
				LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		// popupwindow.setAnimationStyle(R.style.AnimationFade);
		popupwindow2.setFocusable(true);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow2 != null && popupwindow2.isShowing()) {
					popupwindow2.dismiss();
					popupwindow2 = null;
				}
				return false;
			}
		});
		customView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					popupwindow2.dismiss();
					popupwindow2 = null;
					return true;
				}
				return false;
			}
		});
		final TextView shaixuan1 = (TextView) customView.findViewById(R.id.shaixuan1);
		final TextView shaixuan2 = (TextView) customView.findViewById(R.id.shaixuan2);
		final TextView shaixuan3 = (TextView) customView.findViewById(R.id.shaixuan3);
		shaixuan1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shaixuan1.setTextColor(getResources().getColorStateList(R.color.blueMain));
				shaixuan2.setTextColor(getResources().getColorStateList(R.color.greyxian));
				shaixuan3.setTextColor(getResources().getColorStateList(R.color.greyxian));
				zuzhipoupwindow();
			}
		});
		shaixuan2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shaixuan1.setTextColor(getResources().getColorStateList(R.color.greyxian));
				shaixuan2.setTextColor(getResources().getColorStateList(R.color.blueMain));
				shaixuan3.setTextColor(getResources().getColorStateList(R.color.greyxian));
				lingyupoupwindow();
			}
		});
		shaixuan3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shaixuan1.setTextColor(getResources().getColorStateList(R.color.greyxian));
				shaixuan2.setTextColor(getResources().getColorStateList(R.color.greyxian));
				shaixuan3.setTextColor(getResources().getColorStateList(R.color.blueMain));
				hangyepoupwindow();
			}
		});
	}

	protected void hangyepoupwindow() {
		// 获取自定义布局文件pop.xml的视图
		final View customView = getLayoutInflater().inflate(
				R.layout.shaixuan_list, null, false);
		// final LinearLayout view = (LinearLayout)
		// customView.findViewById(R.id.shaixuan_list_length);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 200,
				LayoutParams.WRAP_CONTENT, true);
		popupwindow.showAsDropDown(back, 202, 243);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		// popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}

				return false;
			}
		});
		listmap = new ArrayList<HashMap<String, Object>>();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("claid", 3 + "");
		httpUtils.send(HttpMethod.POST, MyUrl.bankcla, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String lists = responseInfo.result;
						System.out.println(lists + "sss");
						try {
							JSONObject object = new JSONObject(lists);
							String num = object.getString("num").toString();
							if (num.equals("1")) {

							} else {
								String list = object.getString("list")
										.toString();
								JSONArray array = new JSONArray(list);
								for (int i = 0; i < array.length(); i++) {
									JSONObject object2 = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object2.getString("id")
											.toString());
									map.put("type", object2.getString("type")
											.toString());
									map.put("name", object2.getString("name")
											.toString());
									listmap.add(map);
									Button button2 = new Button(
											getApplicationContext());
									int px2 = (int) TypedValue.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 1,
											getResources().getDisplayMetrics());
									button2.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.FILL_PARENT, px2));
									button2.setBackgroundResource(R.color.greyxian);
									Button button = new Button(
											getApplicationContext());
									button.setPadding(0, 0, 0, 0);
									int px = (int) TypedValue.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 30,
											getResources().getDisplayMetrics());
									button.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.FILL_PARENT, px));
									button.setTextColor(getResources()
											.getColorStateList(R.color.greybig));
									button.setBackgroundResource(R.color.white);
									button.setText(listmap.get(i).get("name")
											.toString());
									button.setTextSize(16);
									final int a = i;
									button.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											shaixuan1(5,listmap.get(a).get("id").toString());
											popupwindow.dismiss();
											popupwindow.dismiss();
										}
									});
									((ViewGroup) customView).addView(button);
									((ViewGroup) customView).addView(button2);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void lingyupoupwindow() {
		// 获取自定义布局文件pop.xml的视图
		final View customView = getLayoutInflater().inflate(
				R.layout.shaixuan_list, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 200,
				LayoutParams.WRAP_CONTENT, true);
		popupwindow.showAsDropDown(back, 202, 121);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		// popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}

				return false;
			}
		});
		listmap = new ArrayList<HashMap<String, Object>>();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("claid", 2 + "");
		httpUtils.send(HttpMethod.POST, MyUrl.bankcla, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String lists = responseInfo.result;
						System.out.println(lists + "sss");
						try {
							JSONObject object = new JSONObject(lists);
							String num = object.getString("num").toString();
							if (num.equals("1")) {

							} else {
								String list = object.getString("list")
										.toString();
								JSONArray array = new JSONArray(list);
								for (int i = 0; i < array.length(); i++) {
									JSONObject object2 = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object2.getString("id")
											.toString());
									map.put("type", object2.getString("type")
											.toString());
									map.put("name", object2.getString("name")
											.toString());
									listmap.add(map);
									Button button2 = new Button(
											getApplicationContext());
									int px2 = (int) TypedValue.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 1,
											getResources().getDisplayMetrics());
									button2.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.FILL_PARENT, px2));
									button2.setBackgroundResource(R.color.greyxian);
									Button button = new Button(
											getApplicationContext());
									button.setPadding(0, 0, 0, 0);
									int px = (int) TypedValue.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 30,
											getResources().getDisplayMetrics());
									button.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.FILL_PARENT, px));
									button.setTextColor(getResources()
											.getColorStateList(R.color.greybig));
									button.setBackgroundResource(R.color.white);
									button.setText(listmap.get(i).get("name")
											.toString());
									button.setTextSize(16);
									final int a = i;
									button.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											shaixuan1(0,listmap.get(a).get("id").toString());
											popupwindow.dismiss();
										}
									});
									((ViewGroup) customView).addView(button);
									((ViewGroup) customView).addView(button2);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	protected void zuzhipoupwindow() {
		// 获取自定义布局文件pop.xml的视图
		final View customView = getLayoutInflater().inflate(
				R.layout.shaixuan_zuzhi, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 200,
				LayoutParams.WRAP_CONTENT, true);
		popupwindow.showAsDropDown(back, 202, 0);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		// popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		final TextView button1 = (TextView) customView
				.findViewById(R.id.shaixuan_zuzhi_btn1);
		final TextView button2 = (TextView) customView
				.findViewById(R.id.shaixuan_zuzhi_btn2);
		final TextView button3 = (TextView) customView
				.findViewById(R.id.shaixuan_zuzhi_btn3);
		final TextView button4 = (TextView) customView
				.findViewById(R.id.shaixuan_zuzhi_btn4);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button1.setTextColor(getResources().getColorStateList(R.color.blueMain));
				button2.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button3.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button4.setTextColor(getResources().getColorStateList(R.color.greyxian));
				shaixuan1(1,null);
				popupwindow.dismiss();
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button1.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button2.setTextColor(getResources().getColorStateList(R.color.blueMain));
				button3.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button4.setTextColor(getResources().getColorStateList(R.color.greyxian));
				shaixuan1(2,null);
				popupwindow.dismiss();
			}
		});
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button1.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button2.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button3.setTextColor(getResources().getColorStateList(R.color.blueMain));
				button4.setTextColor(getResources().getColorStateList(R.color.greyxian));
				shaixuan1(3,null);
				popupwindow.dismiss();
			}
		});
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button1.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button2.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button3.setTextColor(getResources().getColorStateList(R.color.greyxian));
				button4.setTextColor(getResources().getColorStateList(R.color.blueMain));
				shaixuan1(4,null);
				popupwindow.dismiss();
			}
		});
	}

	protected void shaixuan1(final int i,String string) {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("sid", tid);
		if (i==0) {
			params.addBodyParameter("field", string);
		}else if (i==5) {
			params.addBodyParameter("ind", string);
		}else if (i==1||i==2||i==3||i==4) {
			params.addBodyParameter("zid", i + "");
		}
		httpUtils.send(HttpMethod.POST, MyUrl.thlistss, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String list = responseInfo.result;
						System.out.println(list + "NN");
						popupwindow2.dismiss();
						try {
							JSONObject object = new JSONObject(list);
							String cy = object.getString("cy").toString();
							if (cy.equals("1")) {

							} else {
								String cylist = object.getString("cylist")
										.toString();
								if (i==0) {
									scrollView.setVisibility(View.GONE);
									shaixuanlist(cylist);
								}else if (i==5) {
									scrollView.setVisibility(View.GONE);
									shaixuanlist(cylist);
								}else if (i==1||i==2||i==3||i==4) {
									switch (i) {
									case 1:
										datechairman(cylist);
										adapter1.notifyDataSetChanged();
										break;
									case 2:
										datedirector(cylist);
										adapter2.notifyDataSetChanged();
										break;
									case 3:
										datecounselor(cylist);
										adapter3.notifyDataSetChanged();
										break;
									case 4:
										datemember(cylist);
										adapter4.notifyDataSetChanged();
										break;
									default:
										break;
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void shaixuanlist(String cylist) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray array = new JSONArray(cylist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("name", object.getString("name").toString());
				map.put("image", object.getString("image").toString());
				map.put("index", object.getString("index").toString());
				map.put("level", object.getString("level").toString());
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		listadapter = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.ideas_world_man, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.ideas_world_man_img);
					holder.name = (TextView) view
							.findViewById(R.id.ideas_world_man_item_text);
					holder.type = (TextView) view
							.findViewById(R.id.ideas_world_man_item_type);
					holder.num = (TextView) view
							.findViewById(R.id.ideas_world_man_item_num);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				bitmapUtils = new BitmapUtils(getApplicationContext());
				bitmapUtils.display(holder.img, ImageAddress.Stringhead
						+ listmap.get(position).get("image").toString());
				holder.name.setText(listmap.get(position).get("name")
						.toString());
				String level = listmap.get(position).get("level")
						.toString();
				if (level.equals("1")) {
					holder.type.setText("主席团");
				}else if (level.equals("2")) {
					holder.type.setText("理事会");
				}else if (level.equals("3")) {
					holder.type.setText("顾问团");
				}else if (level.equals("4")) {
					holder.type.setText("成员");
				}
				holder.num.setText(listmap.get(position).get("index")
						.toString());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent = new Intent(IdeasZhiKuMan.this,
								OtherDetailsActivity.class);
						intent.putExtra("id", listmap.get(position).get("id")
								.toString());
						startActivity(intent);
					}
				});
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		list.setAdapter(listadapter);
	}
}
