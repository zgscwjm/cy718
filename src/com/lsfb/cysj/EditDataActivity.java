package com.lsfb.cysj;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.MyFragment;

public class EditDataActivity extends Activity {
	ImageButton ib_EditData_backing;// 返回
	Button btn_EditData_Sure;// 确定
	EditText et_EditData_name;// 昵称
	TextView et_EditData_birthdays;// 生日
	RadioButton male;// 男
	RadioButton female;// 女
	String nicks="";//判断昵称发生改变没有1：不改变  2改变了
	String sex = "1";// 性别1男_2女
	TextView tvcountry;// 中国
	TextView tvprovinces;// 四川
	TextView tvcitys;// 成都市
	TextView tvarea;// 青羊区
	EditText et_EditData_phone;// 手机号
	TextView tv_EditData_schools;// 学校
	TextView tv_EditData_enterprises;// 政企
	EditText et_EditData_signatures;// 个性签名
	// String resultnum;// 地址的ID值
	HttpClient httpClient;
	static final int DATE_DIALOG_ID = 0;
	private int mYear;
	private int mMonth;
	private int mDay;
	RelativeLayout rl_EditData_address;// 所在地
	RelativeLayout Rl_EditData_schools;// 学校选择
	RelativeLayout Rl_EditData_enterprises;// 政企选择
	Handler handlerSave = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 一维数组
			// num:2(保存成功)|1(保存失败)

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(str);
					Integer num = Integer.parseInt(jsonObject.getString("num"));
					switch (num) {
					case 1:
						Toast.makeText(EditDataActivity.this, "资料保存成功",
								Toast.LENGTH_SHORT).show();
						finish();
						break;
					case 2:
						Toast.makeText(EditDataActivity.this, "资料保存成功",
								Toast.LENGTH_SHORT).show();

						String arr = jsonObject.getString("arr");
						JSONObject jsonObjectarr = new JSONObject(arr);
						IsTrue.isSgin = true;
						IsTrue.Stringnickname = jsonObjectarr
								.getString("nickname");// 昵称
						IsTrue.Stringnumber = jsonObjectarr.getString("number");// 创创号
						IsTrue.Stringznumber = jsonObjectarr
								.getString("znumber");// 创意世界智库号(0)
						IsTrue.Stringzhishu = jsonObjectarr.getString("zhishu");// 我的创意指数
						IsTrue.Stringmoney = jsonObjectarr.getString("money");// 我的创创币
						IsTrue.Stringimage = jsonObjectarr.getString("image");// 会员头像
						IsTrue.Stringsignatur = jsonObjectarr
								.getString("signatur");// 签名(未设置)
						IsTrue.Stringbirthday = jsonObjectarr
								.getString("brithday");// 会员生日(未设置)
						IsTrue.Stringhome = jsonObjectarr.getString("home");// 所在地(未设置)
						IsTrue.Stringhome1 = jsonObjectarr.getString("home1");// 中国
						IsTrue.Stringhome2 = jsonObjectarr.getString("home2");// 四川省
						IsTrue.Stringhome3 = jsonObjectarr.getString("home3");// 成都市
						IsTrue.Stringhome4 = jsonObjectarr.getString("home4");// 金牛区
						IsTrue.Stringsex = jsonObjectarr.getString("sex");// 性别(男、女、未设置)
						IsTrue.Stringphone = jsonObjectarr.getString("phone");// 手机号(未设置)
						IsTrue.Stringschool = jsonObjectarr.getString("school");// 学校
						if (jsonObjectarr.getString("isschool").equals("0")) {
							IsTrue.booleanisschool = false;
						} else {
							IsTrue.booleanisschool = true;// 学校验证标识(0未验证|其他值为验证了的)
						}
						IsTrue.Stringgovernment = jsonObjectarr
								.getString("government");// 政企
						if (jsonObjectarr.getString("isgovernment").equals("0")) {
							IsTrue.booleanisgovernment = false;
						} else {
							IsTrue.booleanisgovernment = true;// 政企验证标识(0未验证|其他值为验证了的)
						}
						finish();
						break;
					case 3:
						Toast.makeText(EditDataActivity.this, "该昵称已存在，请重新输入",
								Toast.LENGTH_SHORT).show();
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
	Handler handlerEditData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Json:
			// 一维数组
			// id:会员id
			// nickname:会员昵称
			// signatur:签名
			// brithday:会员生日(未设置)
			// home:所在地(未设置)
			// home1:国家
			// home2:省份
			// home3:城市
			// home4:区县
			// sex:男、女(未设置)
			// phone:手机号
			// school:学校名称
			// isschool:0(未绑定学校);其他数值为绑定学校
			// government:政企
			// isgovernment:0(未绑定政企);其他数值为绑定政企
			// signatur:签名

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				Log.d("11111111111111111111111111111111", str + "");
				try {
					JSONObject jsonObject = new JSONObject(str);
					et_EditData_name.setText(IsTrue.transformation(jsonObject
							.getString("nickname")));
					nicks=et_EditData_name.getText().toString();
					if (IsTrue.transformation(jsonObject.getString("brithday"))
							.equals("0000-00-00")) {
						et_EditData_birthdays.setText("未设置");
					} else {

						et_EditData_birthdays.setText(IsTrue
								.transformation(jsonObject
										.getString("brithday")));
					}
					if (IsTrue.transformation(jsonObject.getString("sex"))
							.equals("未设置")) {
						sex="1";
						male.setChecked(true);
					}else if (IsTrue.transformation(jsonObject.getString("sex"))
							.equals("男")) {
						sex="1";
						male.setChecked(true);
					} else if (IsTrue.transformation(
							jsonObject.getString("sex")).equals("女")) {
						sex="2";
						female.setChecked(true);
					}
					if (IsTrue.transformation(jsonObject.getString("home"))
							.equals("未设置")) {
						tvcountry.setText("未设置");
					} else {
						tvcountry.setText(IsTrue.transformation(jsonObject
								.getString("home1")));
						if (jsonObject.getString("home2").equals("null")) {
							tvprovinces.setText("");
						} else {
							tvprovinces.setText(IsTrue
									.transformation(jsonObject
											.getString("home2")));
						}
						if (jsonObject.getString("home3").equals("null")) {
							tvcitys.setText("");
						} else {
							tvcitys.setText(IsTrue.transformation(jsonObject
									.getString("home3")));
						}
						if (jsonObject.getString("home4").equals("null")) {
							tvarea.setText("");
						} else {
							tvarea.setText(IsTrue.transformation(jsonObject
									.getString("home4")));
						}

					}
					et_EditData_phone.setText(IsTrue.transformation(jsonObject
							.getString("phone")));
					tv_EditData_schools.setText(IsTrue
							.transformation(jsonObject.getString("school")));
					tv_EditData_enterprises
							.setText(IsTrue.transformation(jsonObject
									.getString("government")));
					et_EditData_signatures.setText(IsTrue
							.transformation(jsonObject.getString("signatur")));
					
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
		setContentView(R.layout.activity_edit_data);
		init();
		ib_EditData_backing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		chushihua();
		et_EditData_birthdays.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// 当前时间
		// updateDisplay();
		rl_EditData_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditDataActivity.this,
						HotZhiKuCity.class);
				startActivityForResult(intent, 1);
			}
		});
		male.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sex = "1";
			}
		});
		female.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sex = "2";
			}
		});
		btn_EditData_Sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/** 隐藏软键盘 **/
				View view = EditDataActivity.this.getWindow().peekDecorView();
				if (view != null) {
					InputMethodManager inputmanger = (InputMethodManager) EditDataActivity.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
				}
				if (et_EditData_name.getText().toString().equals("未设置")) {
					Toast.makeText(EditDataActivity.this, "请输入昵称",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (tvcountry.getText().toString().equals("未设置")) {
					Toast.makeText(EditDataActivity.this, "请输入地址",
							Toast.LENGTH_SHORT).show();
					return;
				}
				save();
			}
		});
		Rl_EditData_schools.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditDataActivity.this,
						SearchSchoolOrEnterpriseActivity.class);
				// check:1(学校)|2(政企)
				intent.putExtra("check", "1");
				startActivityForResult(intent, 2);
			}
		});
		Rl_EditData_enterprises.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditDataActivity.this,
						SearchSchoolOrEnterpriseActivity.class);
				// check:1(学校)|2(政企)
				intent.putExtra("check", "2");
				startActivityForResult(intent, 3);
			}
		});
	}

	protected void save() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringsaveData);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("nickname",
							et_EditData_name.getText().toString()));
					if(nicks.equals(et_EditData_name.getText().toString())){
						params.add(new BasicNameValuePair("nicks", "1"));
					}else{
						params.add(new BasicNameValuePair("nicks", "2"));
					}
					// tvcountry;// 中国
					// tvprovinces;// 四川
					// tvcitys;// 成都市
					// tvarea;// 青羊区

					String straddress;
					straddress = tvcountry.getText().toString() + "|"
							+ tvprovinces.getText().toString() + "|"
							+ tvcitys.getText().toString() + "|"
							+ tvarea.getText().toString();
					if (tvarea.getText().toString().equals("")) {
						straddress = tvcountry.getText().toString() + "|"
								+ tvprovinces.getText().toString() + "|"
								+ tvcitys.getText().toString();
					}
					if (tvcitys.getText().toString().equals("")) {
						straddress = tvcountry.getText().toString() + "|"
								+ tvprovinces.getText().toString();
					}

					if (tvprovinces.getText().toString().equals("")) {
						straddress = tvcountry.getText().toString();
					}

					params.add(new BasicNameValuePair("home", straddress));

					if (et_EditData_birthdays.getText().toString()
							.equals("未设置")) {

						params.add(new BasicNameValuePair("brithday", ""));
					} else {
						params.add(new BasicNameValuePair("brithday",
								et_EditData_birthdays.getText().toString()));
					}
					Log.d("1", "3");
					params.add(new BasicNameValuePair("sex", sex));

					if (et_EditData_phone.getText().toString()
							.equals("0000-00-00")) {

						params.add(new BasicNameValuePair("phone", ""));
					} else {
						params.add(new BasicNameValuePair("phone",
								et_EditData_phone.getText().toString()));
					}
					Log.d("1", "1");
					if (tv_EditData_schools.getText().toString().equals("未设置")) {

						params.add(new BasicNameValuePair("school", ""));
					} else {
						params.add(new BasicNameValuePair("school",
								tv_EditData_schools.getText().toString()));
					}
					Log.d("1", "2");
					if (tv_EditData_enterprises.getText().toString()
							.equals("未设置")) {

						params.add(new BasicNameValuePair("government", ""));
					} else {
						params.add(new BasicNameValuePair("government",
								tv_EditData_enterprises.getText().toString()));
					}
					Log.d("1", "4");
					if (et_EditData_signatures.getText().toString()
							.equals("未设置")) {

						params.add(new BasicNameValuePair("signatur", ""));
					} else {
						params.add(new BasicNameValuePair("signatur",
								et_EditData_signatures.getText().toString()));
					}
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlerSave.sendMessage(msg);

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

	/**
	 * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
	 * 
	 * requestCode 请求码，即调用startActivityForResult()传递过去的值 resultCode
	 * 结果码，结果码用于标识返回数据来自哪个新Activity
	 */
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
				tvcountry.setText("未设置");
			} else {
				tvcountry.setText(result0);
			}
			tvprovinces.setText(result1);// 四川
			tvcitys.setText(result2);// 成都市
			tvarea.setText(result3);// 青羊区
			break;
		case 2:
			String schools = data.getExtras().getString("result");
			tv_EditData_schools.setText(schools);
			break;
		case 3:
			String enterprises = data.getExtras().getString("result");
			tv_EditData_enterprises.setText(enterprises);
			break;
		}

	}

	private void chushihua() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringEditdata);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlerEditData.sendMessage(msg);

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
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		ib_EditData_backing = (ImageButton) findViewById(R.id.ib_EditData_backing);
		btn_EditData_Sure = (Button) findViewById(R.id.btn_EditData_Sure);
		et_EditData_name = (EditText) findViewById(R.id.et_EditData_name);
		et_EditData_birthdays = (TextView) findViewById(R.id.et_EditData_birthdays);
		et_EditData_phone = (EditText) findViewById(R.id.et_EditData_phone);
		tvcountry = (TextView) findViewById(R.id.tvcountry);
		tvprovinces = (TextView) findViewById(R.id.tvprovinces);
		tvcitys = (TextView) findViewById(R.id.tvcitys);
		tvarea = (TextView) findViewById(R.id.tvarea);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		tv_EditData_schools = (TextView) findViewById(R.id.tv_EditData_schools);
		tv_EditData_enterprises = (TextView) findViewById(R.id.tv_EditData_enterprises);
		et_EditData_signatures = (EditText) findViewById(R.id.et_EditData_signatures);

		rl_EditData_address = (RelativeLayout) findViewById(R.id.rl_EditData_address);
		Rl_EditData_schools = (RelativeLayout) findViewById(R.id.Rl_EditData_schools);
		Rl_EditData_enterprises = (RelativeLayout) findViewById(R.id.Rl_EditData_enterprises);
	}

	private void updateDisplay() {
		et_EditData_birthdays.setText(new StringBuilder().append(mYear)
				.append("-").append(mMonth + 1).append("-").append(mDay)
				.append(" "));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_data, menu);
		return true;
	}

}
