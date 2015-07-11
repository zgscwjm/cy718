package com.lsfb.cysj;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.MyIndexOfHistoryActivity.ViewHolder;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyChuangChuangCurrencyActivity extends Activity implements
		IXListViewListener {
	ImageButton ibMyChuangChuangCurrencybacking;// 返回
	XListView listviewMychuangchuangCurrency;
	BaseAdapter baseAdapter;
	HttpClient httpClient;
	Intent intent;
	String ccb = null;
	List<Map<String, Object>> list;
	int count = 0;
	ResDialog dialog;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				dialog.dismiss();
				String str = msg.obj.toString();
				// Json:
				// 返回一维数组
				// 有值存在
				// num:2存在指数信息
				// lit:二维数组
				// source:指数来源
				// time:指数变化时间
				// value:指数变化值
				// 无值存在
				// num:1没有指数信息
				list = new ArrayList<Map<String, Object>>();
				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(MyChuangChuangCurrencyActivity.this,
								"没有任何创创币信息", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("source", temp.getString("source"));
							map.put("time", temp.getString("time"));
							map.put("value", temp.getString("value"));
							list.add(map);

						}
						break;
						
					default:
						break;
					}
					listviewMychuangchuangCurrency.setAdapter(baseAdapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	Handler handlers = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {

				String str = msg.obj.toString();
				// Json:
				// 返回一维数组
				// 有值存在
				// num:2存在指数信息
				// lit:二维数组
				// source:指数来源
				// time:指数变化时间
				// value:指数变化值
				// 无值存在
				// num:1没有指数信息

				Log.d("2222222222222222222222222", str);
				try {
					if (!str.equals("null")) {
						JSONObject jsonObject = new JSONObject(str);
						switch (Integer.parseInt(jsonObject.get("num")
								.toString())) {
						case 1:

							break;
						case 2:
							JSONArray jsonArray = new JSONArray(jsonObject.get(
									"list").toString());
							for (int i = 0; i < jsonArray.length(); ++i) {
								JSONObject temp = (JSONObject) jsonArray.get(i);
								Map<String, Object> map = new HashMap<String, Object>();
								// map.put("id", temp.getString("id"));
								map.put("source", temp.getString("source"));
								map.put("time", temp.getString("time"));
								map.put("value", temp.getString("value"));
								list.add(map);

							}
							break;

						default:
							break;
						}
						baseAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	private TextView rlMycurrency_ccb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_chuang_chuang_currency);
		init();
		rlMycurrency_ccb.setText("金额："+ccb);
		count = 0;
		ibMyChuangChuangCurrencybacking
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		// 初始化适配器
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(MyChuangChuangCurrencyActivity.this)
							.inflate(R.layout.indexofhistory, null);
					holder.tvHead = (TextView) v
							.findViewById(R.id.tv_indexofHistory_source);
					holder.tvTime = (TextView) v
							.findViewById(R.id.tv_indexofHistory_time);
					holder.tvNum = (TextView) v
							.findViewById(R.id.tv_indexofHistory_value);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				holder.tvHead.setText(list.get(position).get("source")
						.toString());
				holder.tvTime
						.setText(list.get(position).get("time").toString());
				holder.tvNum
						.setText(list.get(position).get("value").toString());
				return v;
			}

			@Override
			public long getItemId(int i) {
				// TODO Auto-generated method stub
				return i;
			}

			@Override
			public Object getItem(int i) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		chushihua();
	}

	private void init() {
		// TODO Auto-generated method stub
		ibMyChuangChuangCurrencybacking = (ImageButton) findViewById(R.id.ibMyChuangChuangCurrencybacking);
		listviewMychuangchuangCurrency = (XListView) findViewById(R.id.listviewMychuangchuangCurrency);
		rlMycurrency_ccb = (TextView) findViewById(R.id.rlMycurrency_ccb);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		listviewMychuangchuangCurrency.setPullLoadEnable(true);
		listviewMychuangchuangCurrency.setXListViewListener(this);
		intent = getIntent();
		ccb = intent.getExtras().getString("ccb").toString();
	}

	public class ViewHolder {
		private TextView tvHead;
		private TextView tvTime;
		private TextView tvNum;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index_of_history, menu);
		return true;
	}

	private void onLoad() {
		listviewMychuangchuangCurrency.stopRefresh();
		listviewMychuangchuangCurrency.stopLoadMore();
		listviewMychuangchuangCurrency.setRefreshTime("刚刚");
	}

	public void chushihua() {
		// TODO Auto-generated method stub
		dialog = new ResDialog(MyChuangChuangCurrencyActivity.this, R.style.MyDialog,
				"努力加载中", R.drawable.loads);
		dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringMychuangchuang);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));

					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handler.sendMessage(msg);

					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void loadRemnantListItem() {
		// 滚到加载余下的数据
		// 动态的改变listAdapter.getCount()的返回值
		// 使用Handler调用listAdapter.notifyDataSetChanged();更新数据

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringMychuangchuangs);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlers.sendMessage(msg);
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				count = 0;
				chushihua();
				baseAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadRemnantListItem();
				baseAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

}
