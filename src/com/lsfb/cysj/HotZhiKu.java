package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

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
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

public class HotZhiKu extends FragmentActivity implements OnClickListener,
		IXListViewListener {
	Intent intent;
	BaseAdapter baseAdapter;
	private static String[] citys = new String[] { "成都智库", "北京智库", "上海智库",
			"绵阳智库", "广州智库", "深圳智库", "内蒙古智库" };
	/**
	 * hot_zhiku_list 热门智库列表
	 */
	// @ViewInject(R.id.hot_zhiku_list)
	// private ListView hot_zhiku_list;
	/**
	 * hot_back 返回主界面
	 */
	@ViewInject(R.id.hot_back)
	private LinearLayout hot_back;
	/**
	 * hot_zhiku_city region 地区选择
	 */
	@ViewInject(R.id.hot_zhiku_city)
	private TextView region;
	@ViewInject(R.id.hot_zhiku_text)
	private TextView text;
	/**
	 * name:智库名称 countries:国家 id province:省份id city:城市id qux:区县id 注：可选提交
	 */
	String name, countries, province, city, qux;
	/**
	 * id:智库id img:智库图片(0还未设置图片) name2:智库名称 sum:智库总人数 num:智库人数 jjie:智库介绍
	 */
	String id, img, name2, sum, num, jjie;
	private XListView listview;
	HttpUtils httpUtils;
	RequestParams params;
	Dialog jiazaidialog;
	HashMap<String, Object> map;
	ArrayList<Map<String, Object>> listmap;
	int count = 0;// 分页加载
	private EditText search;
	String namesearch = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hot_zhiku);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ViewUtils.inject(this);
		init();
		adapter();
		
		Log.i("zgscwjm", "jianrenwei");
		date("");
		search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) (InputMethodManager) HotZhiKu.this
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(HotZhiKu.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					namesearch = search.getText().toString().trim();
					if (namesearch.equals("")) {
						listmap = new ArrayList<Map<String, Object>>();
						date(namesearch);
					} else {
						listmap = new ArrayList<Map<String, Object>>();
						date(namesearch);
					}
				}
				return false;
			}

		});
	}

	private void adapter() {
		baseAdapter = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.hot_zhiku_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.hot_zhiku_item_img);
					holder.city = (TextView) view
							.findViewById(R.id.hot_zhiku_item_head);
					holder.num1 = (TextView) view
							.findViewById(R.id.hot_zhiku_item_man);
					holder.num2 = (TextView) view
							.findViewById(R.id.hot_zhiku_item_num);
					holder.text = (TextView) view
							.findViewById(R.id.hot_zhiku_item_buttom);
					holder.btn = (Button) view
							.findViewById(R.id.hot_zhiku_item_button);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				if (listmap.get(position).get("img").equals("0")) {
					holder.img.setBackgroundResource(R.drawable.logo);
				} else {
					BitmapUtils bitmapUtils = new BitmapUtils(
							getApplicationContext());
					bitmapUtils.display(holder.img, ImageAddress.think
							+ listmap.get(position).get("img"));
				}
				holder.city.setText(listmap.get(position).get("name")
						.toString());
				holder.num1
						.setText(listmap.get(position).get("sum").toString());
				holder.num2
						.setText(listmap.get(position).get("num").toString());
				holder.text.setText(listmap.get(position).get("jjie")
						.toString());
				holder.btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (IsTrue.userId == 0) {
							intent = new Intent(HotZhiKu.this,
									HomeActivity.class);
							IsTrue.hotzhiku = true;
							startActivity(intent);
							finish();
						} else {
							showdialogup();
							httpUtils = new HttpUtils();
							params = new RequestParams();
							params.addBodyParameter("uid", IsTrue.userId+"");
							httpUtils.send(HttpMethod.POST, MyUrl.Stringjumpzhikurenzhen, params, new RequestCallBack<String>() {

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
									System.out.println(list+"NNNNNNNNNNNNNNNN");
									try {
										JSONObject object = new JSONObject(list);
										String num = object.getString("num").toString();
										if (num.equals("1")) {
											Toast.makeText(getApplicationContext(), "指数不足5000，基础资料不完善", Toast.LENGTH_SHORT).show();
										}else if (num.equals("2")) {
											intent = new Intent(HotZhiKu.this,
													HotZhiKuShenQing.class);
											intent.putExtra("zhikuid", listmap.get(position)
													.get("id").toString());
											startActivity(intent);
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
						}
					}
				});
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (IsTrue.userId == 0) {
							intent = new Intent(HotZhiKu.this,
									HomeActivity.class);
							IsTrue.hotzhiku = true;
							startActivity(intent);
							finish();
						}else {
						intent = new Intent(HotZhiKu.this,IdeasWorldZhiKuDetails.class);
						intent.putExtra("zhikuid", listmap.get(position)
								.get("id").toString());
						startActivity(intent);
						}
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
		listview.setAdapter(baseAdapter);
	}

	private void date(String namesearch) {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("name", namesearch);
		params.addBodyParameter("countries", IsTrue.Stringidstrarea0);
		params.addBodyParameter("province", IsTrue.Stringidstrarea1);
		params.addBodyParameter("city", IsTrue.Stringidstrarea2);
		params.addBodyParameter("qux", IsTrue.Stringidstrarea3);
		
		Log.i("zgscwjm",IsTrue.Stringidstrarea0);
		System.out.println(namesearch + "E" + IsTrue.Stringidstrarea0 + "W"
				+ IsTrue.Stringidstrarea1 + "BB" + IsTrue.Stringidstrarea2
				+ "NN" + IsTrue.Stringidstrarea3);
		httpUtils.send(HttpMethod.POST, MyUrl.thlist, params,
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
						String lists = responseInfo.result;
						System.out.println(lists);
						try {
							JSONObject object = new JSONObject(lists);
							
							Log.i("zgscwjm", lists);
							
							String num = object.getString("num").toString();
							if (num.equals("1")) {
								text.setVisibility(View.VISIBLE);
							} else if (num.equals("2")) {
								String listcity = object.getString("list")
										.toString();
								JSONArray array = new JSONArray(listcity);
								for (int i = 0; i < array.length(); i++) {
									text.setVisibility(View.GONE);
									JSONObject object2 = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object2.getString("id"));
									map.put("img", object2.getString("img"));
									map.put("name", object2.getString("name"));
									map.put("sum", object2.getString("sum"));
									map.put("num", object2.getString("num"));
									map.put("jjie", object2.getString("jjie"));
									listmap.add(map);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						baseAdapter.notifyDataSetChanged();
					}
				});
		
		jiazaidialog.dismiss();
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	static class ViewHolder {
		ImageView img;
		TextView city;
		TextView num1;
		TextView num2;
		TextView text;
		Button btn;
	}

	private void init() {
		hot_back.setOnClickListener(this);
		region.setOnClickListener(this);
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String, Object>>();
		listview = (XListView) findViewById(R.id.hot_zhiku_list);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		search = (EditText) findViewById(R.id.hot_zhiku_search);
		IsTrue.Stringidstrarea0 = "";
		IsTrue.Stringidstrarea1 = "";
		IsTrue.Stringidstrarea2 = "";
		IsTrue.Stringidstrarea3 = "";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			//listmap = new ArrayList<Map<String, Object>>();
			IsTrue.Stringidstrarea0=data.getStringExtra("result0");
		
			IsTrue.Stringidstrarea1=data.getStringExtra("result1");
			IsTrue.Stringidstrarea2=data.getStringExtra("result2");
			IsTrue.Stringidstrarea3=data.getStringExtra("result3");
			
			Log.i("zgscwjm", "data1:"+data.getStringExtra("result0"));
			
			date(namesearch);
			
			onRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hot_back:// 返回箭头
			finish();
			break;
		case R.id.hot_zhiku_city:// 地区选择
			intent = new Intent(HotZhiKu.this, HotZhiKuCity.class);
			startActivityForResult(intent, 1);
		default:
			break;
		}
	}

	private void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		count = 0;
		listmap = new ArrayList<Map<String, Object>>();
		date("");
		onLoad();
	}

	@Override
	public void onLoadMore() {
		LoadMoremsg();
		onLoad();
	}

	private void LoadMoremsg() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("page", ++count + "");
		params.addBodyParameter("name", namesearch);
		params.addBodyParameter("countries", IsTrue.Stringidstrarea0);
		params.addBodyParameter("province", IsTrue.Stringidstrarea1);
		params.addBodyParameter("city", IsTrue.Stringidstrarea2);
		params.addBodyParameter("qux", IsTrue.Stringidstrarea3);
		httpUtils.send(HttpMethod.POST, MyUrl.thlistpage, params,
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
						String lists = responseInfo.result;
						System.out.println(lists + "YYYYYY");
						try {
							JSONObject object = new JSONObject(lists);
							String num = object.getString("num").toString();
							if (num.equals("1")) {
								Toast.makeText(getApplicationContext(), "没有了亲",
										Toast.LENGTH_SHORT).show();
							} else if (num.equals("2")) {
								String listcity = object.getString("list")
										.toString();
								JSONArray array = new JSONArray(listcity);
								for (int i = 0; i < array.length(); i++) {
									text.setVisibility(View.GONE);
									JSONObject object2 = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object2.getString("id"));
									map.put("img", object2.getString("img"));
									map.put("name", object2.getString("name"));
									map.put("sum", object2.getString("sum"));
									map.put("num", object2.getString("num"));
									map.put("jjie", object2.getString("jjie"));
									listmap.add(map);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						baseAdapter.notifyDataSetChanged();
					}
				});
	}
}
