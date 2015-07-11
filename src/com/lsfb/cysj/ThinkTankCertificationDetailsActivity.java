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
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ThinkTankCertificationDetailsActivity extends Activity {
	ImageButton ibThinkTankCertificationDetailsbacking;// 返回
	TextView tvThinkTankCertificationDetailsorganizations;// 按组织
	TextView tvThinkTankCertificationDetailsfields;// 按领域
	TextView tvThinkTankCertificationDetailsindustrys;// 按行业
	TextView tvThinkTankCertificationDetailsareas;// 按地区
	EditText et_tvThinkTankCertificationDetailsname;// 姓名
	EditText et_ThinkTankCertificationDetailsintroduce;// 个人介绍
	Button btnThink_tank_details;// 提交申请
	HttpClient httpClient;
	ResDialog dialog;
	String straddressId = "";
	String als = "";
	// String[] organizations = new String[] { "主席团", "理事会", "顾问团" };// 按组织
	// String[] fields = new String[] { "全部", "领导人", "科学家", "艺术家", "企业家", "慈善家",
	// "精英", "商会明显", "校友名人", "其他" };// 按领域
	// String[] industrys = new String[] { "全部", "农林牧副业", "自然资源", "制造业", "能源供应",
	// "环境保护", "建筑", "批发零售", "交通运输", "食宿", "信息通信", "金融保险", "房地产",
	// "专业科学技术", "行政和辅助", "公共管理和国防", "教育", "生活健康", "文学艺术娱乐", "服务业",
	// "国际组织活动", "其他行业" };// 按领域
	List<Map<String, Object>> listori;
	List<Map<String, Object>> listli;
	List<Map<String, Object>> listhy;
	String oris[];
	String lis[];
	String hys[];
	String oriId;
	String liId;
	String hyId;
	Handler handlerchushihua = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 一维数组
			// num:2(保存成功)|1(保存失败)

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				dialog.dismiss();
				System.err.println(str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					int ori = Integer.parseInt(jsonObject.getString("ori")
							.toString());
					switch (ori) {
					case 1:

						break;
					case 2:
						String strori = jsonObject.getString("orilist")
								.toString();
						JSONArray jsonArrayori = new JSONArray(strori);
						oris = new String[jsonArrayori.length()];
						for (int i = 0; i < jsonArrayori.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) jsonArrayori
									.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("id", jsonObject2.getString("id"));
							map.put("name", jsonObject2.getString("name"));
							map.put("type", jsonObject2.getString("type"));
							listori.add(map);
							oris[i] = jsonObject2.getString("name");
						}
						break;
					default:
						break;
					}
					int li = Integer.parseInt(jsonObject.getString("li")
							.toString());
					switch (li) {
					case 1:

						break;
					case 2:
						String strli = jsonObject.getString("lilist")
								.toString();
						JSONArray jsonArrayli = new JSONArray(strli);
						lis = new String[jsonArrayli.length()];

						for (int i = 0; i < jsonArrayli.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) jsonArrayli
									.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("id", jsonObject2.getString("id"));
							map.put("name", jsonObject2.getString("name"));
							map.put("type", jsonObject2.getString("type"));
							listli.add(map);
							lis[i] = jsonObject2.getString("name");
						}
						break;
					default:
						break;
					}
					int hy = Integer.parseInt(jsonObject.getString("hy")
							.toString());
					switch (hy) {
					case 1:

						break;
					case 2:
						String strhy = jsonObject.getString("hylist")
								.toString();
						JSONArray jsonArrayhy = new JSONArray(strhy);
						hys = new String[jsonArrayhy.length()];
						for (int i = 0; i < jsonArrayhy.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) jsonArrayhy
									.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("id", jsonObject2.getString("id"));
							map.put("name", jsonObject2.getString("name"));
							map.put("type", jsonObject2.getString("type"));
							listhy.add(map);
							hys[i] = jsonObject2.getString("name");
						}
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 一维数组
			// num:2(保存成功)|1(保存失败)

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				System.err.println(str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					int num = Integer.parseInt(jsonObject.getString("num")
							.toString());
					switch (num) {
					case 1:
						Toast.makeText(
								ThinkTankCertificationDetailsActivity.this,
								"申请失败", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(
								ThinkTankCertificationDetailsActivity.this,
								"申请成功", Toast.LENGTH_SHORT).show();
						finish();
						break;
					case 3:
						Toast.makeText(
								ThinkTankCertificationDetailsActivity.this,
								"请勿重新申请", Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
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
		setContentView(R.layout.activity_think_tank_certification_details);
		init();
		ibThinkTankCertificationDetailsbacking
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		// 按组织
		tvThinkTankCertificationDetailsorganizations
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(
								ThinkTankCertificationDetailsActivity.this)
						// .setTitle("列表框")
						// .setNegativeButton("确定", null)
								.setItems(oris,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int i) {
												// TODO Auto-generated method
												// stub
												tvThinkTankCertificationDetailsorganizations
														.setText(oris[i]);
												oriId = listori.get(i)
														.get("id").toString();
												dialog.dismiss();
											}
										}).show();
					}
				});
		// 按领域
		tvThinkTankCertificationDetailsfields
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(
								ThinkTankCertificationDetailsActivity.this)
						// .setTitle("列表框")
						// .setNegativeButton("确定", null)
								.setItems(lis,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int i) {
												// TODO Auto-generated method
												// stub
												tvThinkTankCertificationDetailsfields
														.setText(lis[i]);
												liId = listli.get(i).get("id")
														.toString();
												dialog.dismiss();
											}
										}).show();
					}
				});
		// 按行业
		tvThinkTankCertificationDetailsindustrys
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(
								ThinkTankCertificationDetailsActivity.this)
						// .setTitle("列表框")
						// .setNegativeButton("确定", null)
								.setItems(hys,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int i) {
												// TODO Auto-generated method
												// stub
												tvThinkTankCertificationDetailsindustrys
														.setText(hys[i]);
												hyId = listhy.get(i).get("id")
														.toString();
												dialog.dismiss();
											}
										}).show();
					}
				});
		// 地址
		if (als.equals("2")) {
			
		} else {
		tvThinkTankCertificationDetailsareas
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								ThinkTankCertificationDetailsActivity.this,
								HotZhiKuCity.class);
						startActivityForResult(intent, 1);
					}
				});
		
		}
		// 提交申请
		btnThink_tank_details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (et_tvThinkTankCertificationDetailsname.getText().toString()
						.trim().equals("")) {
					Toast.makeText(ThinkTankCertificationDetailsActivity.this,
							"姓名不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (et_ThinkTankCertificationDetailsintroduce.getText()
						.toString().trim().equals("")) {
					Toast.makeText(ThinkTankCertificationDetailsActivity.this,
							"个人介绍不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (tvThinkTankCertificationDetailsorganizations.getText()
						.toString().trim().equals("未选择")) {
					Toast.makeText(ThinkTankCertificationDetailsActivity.this,
							"组织还未选择", Toast.LENGTH_SHORT).show();
					return;
				}
				if (tvThinkTankCertificationDetailsfields.getText().toString()
						.trim().equals("未选择")) {
					Toast.makeText(ThinkTankCertificationDetailsActivity.this,
							"领域还未选择", Toast.LENGTH_SHORT).show();
					return;
				}
				if (tvThinkTankCertificationDetailsindustrys.getText()
						.toString().trim().equals("未选择")) {
					Toast.makeText(ThinkTankCertificationDetailsActivity.this,
							"行业还未选择", Toast.LENGTH_SHORT).show();
					return;
				}
				// if (tvThinkTankCertificationDetailsareas.getText().toString()
				// .trim().equals("未选择")) {
				// Toast.makeText(ThinkTankCertificationDetailsActivity.this,
				// "地区还未选择",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }
				submit();
			}
		});
		chushihua();
	}

	private void chushihua() {
		// TODO Auto-generated method stub
		dialog = new ResDialog(ThinkTankCertificationDetailsActivity.this,
				R.style.MyDialog, "数据加载中", R.drawable.loads);
		dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(
							MyUrl.Stringzhikuzhuanjiachushihua);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlerchushihua.sendMessage(msg);

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

	protected void submit() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.thapply);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					System.out.println("uid:"+IsTrue.userId+"name:"+et_tvThinkTankCertificationDetailsname.getText().toString()+"bewrite:"+et_ThinkTankCertificationDetailsintroduce.getText().toString()+"org:"+oriId
							+"field:"+liId+"ind:"+hyId+"home:"+straddressId);
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("name",et_tvThinkTankCertificationDetailsname.getText().toString()));
					params.add(new BasicNameValuePair("bewrite",et_ThinkTankCertificationDetailsintroduce.getText().toString()));
					params.add(new BasicNameValuePair("org",oriId ));
					params.add(new BasicNameValuePair("field",liId));
					params.add(new BasicNameValuePair("ind",hyId));
					params.add(new BasicNameValuePair("home", straddressId));
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
		// TODO Auto-generated method stub
		listori = new ArrayList<Map<String, Object>>();
		listli = new ArrayList<Map<String, Object>>();
		listhy = new ArrayList<Map<String, Object>>();
		ibThinkTankCertificationDetailsbacking = (ImageButton) findViewById(R.id.ibThinkTankCertificationDetailsbacking);
		tvThinkTankCertificationDetailsorganizations = (TextView) findViewById(R.id.tvThinkTankCertificationDetailsorganizations);
		tvThinkTankCertificationDetailsfields = (TextView) findViewById(R.id.tvThinkTankCertificationDetailsfields);
		tvThinkTankCertificationDetailsindustrys = (TextView) findViewById(R.id.tvThinkTankCertificationDetailsindustrys);
		tvThinkTankCertificationDetailsareas = (TextView) findViewById(R.id.tvThinkTankCertificationDetailsareas);
		et_tvThinkTankCertificationDetailsname = (EditText) findViewById(R.id.et_tvThinkTankCertificationDetailsname);
		et_ThinkTankCertificationDetailsintroduce = (EditText) findViewById(R.id.et_ThinkTankCertificationDetailsintroduce);
		btnThink_tank_details = (Button) findViewById(R.id.btnThink_tank_details);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		Intent intent = getIntent();
		straddressId = intent.getStringExtra("regions").toString();
		als = intent.getExtras().getString("als").toString();
		String strAddresshome = intent.getStringExtra("home").toString();
		String[] strAddress = strAddresshome.split("\\|");
		// tvThinkTankCertificationDetailsareas.setText(IsTrue.Stringhome1
		// + IsTrue.Stringhome2 + IsTrue.Stringhome3 + IsTrue.Stringhome4);
		System.err.println(strAddress);
		System.err.println(intent.getStringExtra("home").toString());
		System.err.println(strAddress.length);
		switch (strAddress.length) {
		case 1:
			tvThinkTankCertificationDetailsareas.setText(strAddress[0]);
			break;
		case 2:
			tvThinkTankCertificationDetailsareas.setText(strAddress[0]
					+ strAddress[1]);
			break;
		case 3:
			tvThinkTankCertificationDetailsareas.setText(strAddress[0]
					+ strAddress[1] + strAddress[2]);
			break;
		case 4:
			tvThinkTankCertificationDetailsareas.setText(strAddress[0]
					+ strAddress[1] + strAddress[2] + strAddress[3]);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//地区返回的时候 会变成未选择
//		if (tvThinkTankCertificationDetailsareas.getText().toString()
//				.equals("未选择")) {
//			Intent intent = getIntent();
//			straddressId = intent.getStringExtra("regions").toString();
//			String strAddresshome = intent.getStringExtra("home").toString();
//			String[] strAddress = strAddresshome.split("\\|");
//			// tvThinkTankCertificationDetailsareas.setText(IsTrue.Stringhome1
//			// + IsTrue.Stringhome2 + IsTrue.Stringhome3 + IsTrue.Stringhome4);
//			System.err.println(strAddress);
//			System.err.println(intent.getStringExtra("home").toString());
//			System.err.println(strAddress.length);
//			switch (strAddress.length) {
//			case 1:
//				tvThinkTankCertificationDetailsareas.setText(strAddress[0]);
//				break;
//			case 2:
//				tvThinkTankCertificationDetailsareas.setText(strAddress[0]
//						+ strAddress[1]);
//				break;
//			case 3:
//				tvThinkTankCertificationDetailsareas.setText(strAddress[0]
//						+ strAddress[1] + strAddress[2]);
//				break;
//			case 4:
//				tvThinkTankCertificationDetailsareas.setText(strAddress[0]
//						+ strAddress[1] + strAddress[2] + strAddress[3]);
//				break;
//			default:
//				break;
//			}
//		}
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			String result0 = data.getExtras().getString("result0");
			String result1 = data.getExtras().getString("result1");
			String result2 = data.getExtras().getString("result2");
			String result3 = data.getExtras().getString("result3");
			// resultnum = data.getExtras().getString("results");
			if (result0.equals("")) {
				tvThinkTankCertificationDetailsareas.setText("未选择");
			} else {
				tvThinkTankCertificationDetailsareas.setText(result0 + result1
						+ result2 + result3);
			}
			straddressId = IsTrue.Stringidstrarea0 + ","
					+ IsTrue.Stringidstrarea1 + "," + IsTrue.Stringidstrarea2
					+ "," + IsTrue.Stringidstrarea3;
			break;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.think_tank_certification_details, menu);
		return true;
	}

}
