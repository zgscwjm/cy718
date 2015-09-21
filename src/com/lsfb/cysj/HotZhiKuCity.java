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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.HotIdeasGameFragment;
import com.lsfb.cysj.view.ResDialog;

public class HotZhiKuCity extends FragmentActivity implements OnClickListener {
	ResDialog dialog;// 弹出框
	HttpClient httpClient;
	@ViewInject(R.id.hot_zhiku_city_back)
	private LinearLayout back;

	List<Map<String, Object>> listguojia;// 国家级别
	List<Map<String, Object>> listChina;// 省份级别
	List<Map<String, Object>> listbeijin;// 市区级别
	List<Map<String, Object>> listtiananmen;// 县区级别
	String strarea0 = "";
	String strarea1 = "";
	String strarea2 = "";
	String strarea3 = "";
	String idstrarea0 = "";
	String idstrarea1 = "";
	String idstrarea2 = "";
	String idstrarea3 = "";

	LinearLayout hot_zhiku_city_queding;// 确定
	Spinner spinner_country;// 选择国家
	Spinner spinner_shengfen;// 选择省份
	Spinner spinner_shiqu;// 选择市区
	Spinner spinner_quxian;// 选择区县
	BaseAdapter baseAdapter_country;// 国家adapter
	BaseAdapter baseAdapter_shengfen;// 省份adapter
	BaseAdapter baseAdapter_shiqu;// 市adapter
	BaseAdapter baseAdapter_quxian;// 区县份adapter
	Button btn_country;// 国际
	Button btn_shengfen;// 省份
	Button btn_shiqu;// 成都市
	Button btn_quxian;// 县区

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 清空地址
		strarea0 = "";
		strarea1 = "";
		strarea2 = "";
		strarea3 = "";
		super.onDestroy();
	}

	@Override
	public Context createPackageContext(String packageName, int flags)
			throws NameNotFoundException {
		// TODO Auto-generated method stub
		return super.createPackageContext(packageName, flags);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				// Json:
				// 二维数组
				// region_info_id:地区id
				// region_info_name:地区名称

				String str = msg.obj.toString();
				JSONArray jsonArray;
				try {
					listguojia = new ArrayList<Map<String, Object>>();
					listChina = new ArrayList<Map<String, Object>>();// 省份级别
					listbeijin = new ArrayList<Map<String, Object>>();// 市区级别
					listtiananmen = new ArrayList<Map<String, Object>>();// 县区级别
					btn_shengfen.setText("");
					btn_shiqu.setText("");
					btn_quxian.setText("");
					jsonArray = new JSONArray(str);
					for (int i = 0; i < jsonArray.length(); ++i) {
						JSONObject temp = (JSONObject) jsonArray.get(i);
						Map<String, Object> map = new HashMap<String, Object>();
						// map.put("id", temp.getString("id"));
						map.put("region_info_id",
								temp.getString("region_info_id"));
						map.put("region_info_name",
								temp.getString("region_info_name"));
						listguojia.add(map);
					}
					spinner_country.setAdapter(baseAdapter_country);
					baseAdapter_shengfen.notifyDataSetChanged();
					baseAdapter_shiqu.notifyDataSetChanged();
					baseAdapter_quxian.notifyDataSetChanged();
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
				// Json:
				// 二维数组
				// region_info_id:地区id
				// region_info_name:地区名称

				String str = msg.obj.toString();
				Log.d("++++++++", str);

				try {
					JSONObject jsonObject = new JSONObject(str);
					int num = Integer.parseInt(jsonObject.getString("num")
							.toString());

					listChina = new ArrayList<Map<String, Object>>();// 省份级别
					listbeijin = new ArrayList<Map<String, Object>>();// 市区级别
					listtiananmen = new ArrayList<Map<String, Object>>();// 县区级别
					btn_shengfen.setText("");
					btn_shiqu.setText("");
					btn_quxian.setText("");
					switch (num) {
					case 1:

						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject
								.getString("list").toString());

						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("region_info_id",
									temp.getString("region_info_id"));
							map.put("region_info_name",
									temp.getString("region_info_name"));
							listChina.add(map);
						}
						break;
					default:
						break;
					}

					spinner_shengfen.setAdapter(baseAdapter_shengfen);
					baseAdapter_shengfen.notifyDataSetChanged();
					baseAdapter_shiqu.notifyDataSetChanged();
					baseAdapter_quxian.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};

	Handler handlerss = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				// Json:
				// 二维数组
				// region_info_id:地区id
				// region_info_name:地区名称

				String str = msg.obj.toString();
				Log.d("++++++++", str);

				try {
					JSONObject jsonObject = new JSONObject(str);
					int num = Integer.parseInt(jsonObject.getString("num")
							.toString());

					listbeijin = new ArrayList<Map<String, Object>>();// 市区级别
					listtiananmen = new ArrayList<Map<String, Object>>();// 县区级别
					btn_shiqu.setText("");
					btn_quxian.setText("");
					switch (num) {
					case 1:

						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject
								.getString("list").toString());

						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("region_info_id",
									temp.getString("region_info_id"));
							map.put("region_info_name",
									temp.getString("region_info_name"));
							listbeijin.add(map);
						}
						break;
					default:
						break;
					}

					spinner_shiqu.setAdapter(baseAdapter_shiqu);
					baseAdapter_shiqu.notifyDataSetChanged();
					baseAdapter_quxian.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};
	Handler handlersss = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				// Json:
				// 二维数组
				// region_info_id:地区id
				// region_info_name:地区名称

				String str = msg.obj.toString();
				Log.d("++++++++", str);

				try {
					JSONObject jsonObject = new JSONObject(str);
					int num = Integer.parseInt(jsonObject.getString("num")
							.toString());
					listtiananmen = new ArrayList<Map<String, Object>>();// 省份级别

					btn_quxian.setText("");
					switch (num) {
					case 1:

						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject
								.getString("list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("region_info_id",
									temp.getString("region_info_id"));
							map.put("region_info_name",
									temp.getString("region_info_name"));
							listtiananmen.add(map);
						}
						break;
					default:
						break;
					}

					spinner_quxian.setAdapter(baseAdapter_quxian);
					baseAdapter_quxian.notifyDataSetChanged();
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
		setContentView(R.layout.hot_zhiku_city);
		ViewUtils.inject(this);

		init();

		hot_zhiku_city_queding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 数据是使用Intent返回
				strarea0 = btn_country.getText().toString();
				strarea1 = btn_shengfen.getText().toString();
				strarea2 = btn_shiqu.getText().toString();
				strarea3 = btn_quxian.getText().toString();
				if (strarea1.equals("--请选择--")) {
					strarea1 = "";
				}
				if (strarea2.equals("--请选择--")) {
					strarea2 = "";
				}
				if (strarea3.equals("--请选择--")) {
					strarea3 = "";
				}
				Intent intent = new Intent();
				// 把返回数据存入Intent
				intent.putExtra("result0", idstrarea0);
				intent.putExtra("result1", idstrarea1);
				intent.putExtra("result2", idstrarea2);
				intent.putExtra("result3", idstrarea3);
				IsTrue.Stringidstrarea0 = idstrarea0;
				IsTrue.Stringidstrarea1 = idstrarea1;
				IsTrue.Stringidstrarea2 = idstrarea2;
				IsTrue.Stringidstrarea3 = idstrarea3;
				Log.i("zgscwjm", "开始");
				// intent.putExtra("results", strnum);
				// 设置返回数据
				HotZhiKuCity.this.setResult(HotIdeasGameFragment.RESULT_OK,
						intent);
				// 关闭Activity
				HotZhiKuCity.this.finish();
			}
		});
		baseAdapter_country = new BaseAdapter() {
			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub

				v = LayoutInflater.from(HotZhiKuCity.this).inflate(
						R.layout.jubao_spinner_item, null);
				TextView tvTextView = (TextView) v
						.findViewById(R.id.ideas_msg_jubao_spr_text);

				tvTextView.setText(listguojia.get(position)
						.get("region_info_name").toString());
				idstrarea0 = listguojia.get(position).get("region_info_id")
						.toString();
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
				return listguojia.size();
			}
		};
		baseAdapter_shengfen = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub

				v = LayoutInflater.from(HotZhiKuCity.this).inflate(
						R.layout.jubao_spinner_item, null);
				TextView tvTextView = (TextView) v
						.findViewById(R.id.ideas_msg_jubao_spr_text);

				tvTextView.setText(listChina.get(position)
						.get("region_info_name").toString());

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
				return listChina.size();
			}
		};
		baseAdapter_shiqu = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub

				v = LayoutInflater.from(HotZhiKuCity.this).inflate(
						R.layout.jubao_spinner_item, null);
				TextView tvTextView = (TextView) v
						.findViewById(R.id.ideas_msg_jubao_spr_text);

				tvTextView.setText(listbeijin.get(position)
						.get("region_info_name").toString());

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
				return listbeijin.size();
			}
		};
		baseAdapter_quxian = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub

				v = LayoutInflater.from(HotZhiKuCity.this).inflate(
						R.layout.jubao_spinner_item, null);
				TextView tvTextView = (TextView) v
						.findViewById(R.id.ideas_msg_jubao_spr_text);

				tvTextView.setText(listtiananmen.get(position)
						.get("region_info_name").toString());

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
				return listtiananmen.size();
			}
		};
		spinner_country
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						// String content = arr[position];

						Spinner spinner = (Spinner) parent;

						btn_country.setText(listguojia.get(position)
								.get("region_info_name").toString());
						idstrarea0 = listguojia.get(position)
								.get("region_info_id").toString();
						shengfen(listguojia.get(position).get("region_info_id")
								.toString());
						Log.i("zgscwjm", "ids:"+idstrarea0);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Toast.makeText(getApplicationContext(), "没有改变的处理",
						// Toast.LENGTH_SHORT).show();

					}
				});

		spinner_shengfen
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						// String content = arr[position];

						Spinner spinner = (Spinner) parent;

						btn_shengfen.setText(listChina.get(position)
								.get("region_info_name").toString());
						idstrarea1 = listChina.get(position)
								.get("region_info_id").toString();
						shengfens(listChina.get(position).get("region_info_id")
								.toString());

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Toast.makeText(getApplicationContext(), "没有改变的处理",
						// Toast.LENGTH_SHORT).show();

					}
				});
		spinner_shiqu
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						// String content = arr[position];

						Spinner spinner = (Spinner) parent;

						btn_shiqu.setText(listbeijin.get(position)
								.get("region_info_name").toString());
						idstrarea2 = listbeijin.get(position)
								.get("region_info_id").toString();
						shengfensss(listbeijin.get(position)
								.get("region_info_id").toString());

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Toast.makeText(getApplicationContext(), "没有改变的处理",
						// Toast.LENGTH_SHORT).show();

					}
				});
		spinner_quxian
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						// String content = arr[position];

						Spinner spinner = (Spinner) parent;

						btn_quxian.setText(listtiananmen.get(position)
								.get("region_info_name").toString());
						idstrarea3 = listtiananmen.get(position)
								.get("region_info_id").toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Toast.makeText(getApplicationContext(), "没有改变的处理",
						// Toast.LENGTH_SHORT).show();

					}
				});
		chushihua();
	}

	protected void shengfen(final String id) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringchooses);
					Log.i("zgscwjm", "id is chain?"+id);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("id", id));
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

	protected void shengfens(final String id) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringchooses);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("id", id));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlerss.sendMessage(msg);

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

	protected void shengfensss(final String id) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {
					HttpPost post = new HttpPost(MyUrl.Stringchooses);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("id", id));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlersss.sendMessage(msg);

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
		public TextView tvTextView;

	}

	private void chushihua() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {
					HttpPost post = new HttpPost(MyUrl.Stringchoose);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

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

	private void init() {
		listguojia = new ArrayList<Map<String, Object>>();
		listChina = new ArrayList<Map<String, Object>>();// 省份级别
		listbeijin = new ArrayList<Map<String, Object>>();// 市区级别
		listtiananmen = new ArrayList<Map<String, Object>>();// 县区级别
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		hot_zhiku_city_queding = (LinearLayout) findViewById(R.id.hot_zhiku_city_queding);
		back.setOnClickListener(this);
	//	queding.setOnClickListener(this);
		spinner_country = (Spinner) findViewById(R.id.spinner_country);
		spinner_shengfen = (Spinner) findViewById(R.id.spinner_shengfen);
		spinner_shiqu = (Spinner) findViewById(R.id.spinner_shiqu);
		spinner_quxian = (Spinner) findViewById(R.id.spinner_quxian);
		btn_country = (Button) findViewById(R.id.btn_country);// 国际
		btn_shengfen = (Button) findViewById(R.id.btn_shengfen);// 省份
		btn_shiqu = (Button) findViewById(R.id.btn_shiqu);// 成都市
		btn_quxian = (Button) findViewById(R.id.btn_quxian);// 县区
		// list.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// 数据是使用Intent返回
			Intent intent = new Intent();
			strarea0 = "";
			strarea1 = "";
			strarea2 = "";
			strarea3 = "";
			// 把返回数据存入Intent
			intent.putExtra("result0", strarea0);
			intent.putExtra("result1", strarea1);
			intent.putExtra("result2", strarea2);
			intent.putExtra("result3", strarea3);
			// intent.putExtra("results", strnum);
			// 设置返回数据
			HotZhiKuCity.this.setResult(RESULT_OK, intent);
			// 关闭Activity
			HotZhiKuCity.this.finish();
			finish();
			return false;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hot_zhiku_city_back:
			// 数据是使用Intent返回
			Intent intent = new Intent();
			// 把返回数据存入Intent
			strarea0 = "";
			strarea1 = "";
			strarea2 = "";
			strarea3 = "";
			intent.putExtra("result0", strarea0);
			intent.putExtra("result1", strarea1);
			intent.putExtra("result2", strarea2);
			intent.putExtra("result3", strarea3);
			// intent.putExtra("results", strnum);
			// 设置返回数据
			HotZhiKuCity.this.setResult(RESULT_OK, intent);
			// 关闭Activity
			 HotZhiKuCity.this.finish();
			//finish();
			break;
		case R.id.hot_zhiku_city_queding:
			Log.i("zgscwjm", "ok button down");
//			Spinner spinner_country;// 选择国家
//			Spinner spinner_shengfen;// 选择省份
//			Spinner spinner_shiqu;// 选择市区
//			Spinner spinner_quxian;
			
//			Intent intent2 = new Intent();
//			Log.i("zgscwjm", "res"+idstrarea0);
//			intent2.putExtra("result0", idstrarea0);
//			intent2.putExtra("result1", idstrarea1);
//			intent2.putExtra("result2", idstrarea2);
//			intent2.putExtra("result3", idstrarea3);
//			
//			HotZhiKuCity.this.setResult(1, intent2);
//			// 关闭Activity
//			 HotZhiKuCity.this.finish();
//			
			break;
		default:
			break;
		}
	}
}
