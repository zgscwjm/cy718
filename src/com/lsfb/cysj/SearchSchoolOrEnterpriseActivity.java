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

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.MyChuangChuangCurrencyActivity.ViewHolder;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchSchoolOrEnterpriseActivity extends Activity {
	ImageButton ib_SearchSchoolOrEnterprise_backing;// 返回
	String check;// 判断是选的学校还是政企
	EditText et_SearchSchoolOrEnterprise;// 搜索框
	Button btn_SearchSchoolOrEnterprise_Sure;// 确定
	Button btn_SearchSchoolOrEnterprise_Search;// 搜索
	ListView lv_SearchSchoolOrEnterprise;// 显示具体信息的listVIew
	BaseAdapter baseAdapter;
	HttpClient httpClient;
	List<Map<String, Object>> list;
	Handler handlersearch = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 有值存在
			// num:2
			// list:二维数组
			// cid:学校政企id
			// cname:学校政企名称
			// cimage:学校政企图片
			// 无值存在
			// num:1

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				System.err.println(str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					String num = jsonObject.getString("num");
					if (num.equals("1")) {
						Toast.makeText(SearchSchoolOrEnterpriseActivity.this,
								"暂无此信息", Toast.LENGTH_SHORT).show();
						list = new ArrayList<Map<String, Object>>();
					} else if (num.equals("2")) {
						JSONArray jsonArray = new JSONArray(
								jsonObject.getString("list"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject json = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("cid", json.getString("cid"));
							map.put("cname", json.getString("cname"));
							map.put("cimage", json.getString("cimage"));
							list.add(map);
						}
					}
					baseAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_school_or_enterprise);
		init();
		Intent intent = getIntent();
		check = intent.getStringExtra("check");
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(final int positon, View v, ViewGroup arg2) {
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(
							SearchSchoolOrEnterpriseActivity.this).inflate(
							R.layout.searchschool, null);
					holder.iamgeView = (ImageView) v
							.findViewById(R.id.iv_listview_search);
					holder.textView = (TextView) v
							.findViewById(R.id.tv_listview_search);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				holder.textView.setText(list.get(positon).get("cname")
						.toString());
				BitmapUtils bitmapUtils = new BitmapUtils(
						SearchSchoolOrEnterpriseActivity.this);

				bitmapUtils.display(holder.iamgeView, ImageAddress.StringSearch
						+ list.get(positon).get("cimage").toString());
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						et_SearchSchoolOrEnterprise.setText(list.get(positon)
								.get("cname").toString());
					}
				});
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
		lv_SearchSchoolOrEnterprise.setAdapter(baseAdapter);
		btn_SearchSchoolOrEnterprise_Search
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						search();
					}
				});
		btn_SearchSchoolOrEnterprise_Sure
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// 数据是使用Intent返回
						Intent intent = new Intent();
						// 把返回数据存入Intent
						intent.putExtra("result", et_SearchSchoolOrEnterprise
								.getText().toString());

						// intent.putExtra("results", strnum);
						// 设置返回数据
						SearchSchoolOrEnterpriseActivity.this.setResult(
								RESULT_OK, intent);
						// 关闭Activity
						SearchSchoolOrEnterpriseActivity.this.finish();
					}
				});
		ib_SearchSchoolOrEnterprise_backing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// 把返回数据存入Intent
				intent.putExtra("result","未设置");

				// intent.putExtra("results", strnum);
				// 设置返回数据
				SearchSchoolOrEnterpriseActivity.this.setResult(
						RESULT_OK, intent);
				// 关闭Activity
				SearchSchoolOrEnterpriseActivity.this.finish();
			}
		});
	}

	protected void search() {
		// TODO Auto-generated method stub
		/** 隐藏软键盘 **/
		View view = SearchSchoolOrEnterpriseActivity.this.getWindow()
				.peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) SearchSchoolOrEnterpriseActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringSearch);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("name",
							et_SearchSchoolOrEnterprise.getText().toString()));
					params.add(new BasicNameValuePair("check", check));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlersearch.sendMessage(msg);

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

	public class ViewHolder {
		public ImageView iamgeView;
		public TextView textView;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent();
			// 把返回数据存入Intent
			intent.putExtra("result","未设置");

			// intent.putExtra("results", strnum);
			// 设置返回数据
			SearchSchoolOrEnterpriseActivity.this.setResult(
					RESULT_OK, intent);
			// 关闭Activity
			SearchSchoolOrEnterpriseActivity.this.finish();
			return false;
		}
		return false;
	}
	private void init() {
		// TODO Auto-generated method stub
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		lv_SearchSchoolOrEnterprise = (ListView) findViewById(R.id.lv_SearchSchoolOrEnterprise);
		btn_SearchSchoolOrEnterprise_Search = (Button) findViewById(R.id.btn_SearchSchoolOrEnterprise_Search);
		btn_SearchSchoolOrEnterprise_Sure = (Button) findViewById(R.id.btn_SearchSchoolOrEnterprise_Sure);
		et_SearchSchoolOrEnterprise = (EditText) findViewById(R.id.et_SearchSchoolOrEnterprise);
		ib_SearchSchoolOrEnterprise_backing = (ImageButton) findViewById(R.id.ib_SearchSchoolOrEnterprise_backing);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_school_or_enterprise, menu);
		return true;
	}

}
