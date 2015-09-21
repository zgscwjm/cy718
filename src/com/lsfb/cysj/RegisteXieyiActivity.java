package com.lsfb.cysj;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.base.BaseActivity;
import com.lsfb.cysj.utils.Show;

/**
 * 用户注册协议
 * @author Administrator
 *
 */
public class RegisteXieyiActivity extends BaseActivity {
	ImageButton ibbacking;// 返回
	private TimeCount time;// 验证码刷新时间
	Button btngetVerificationCode;// 获取验证码
	Button btnsign, btnUserXieyi;// 注册
	EditText etMemberAccountNum;// 手机号码
	EditText etMemberAccount;// 输入的验证码
	EditText etMemberPassWord;// 第一次输入密码
	EditText etMemberRePassWord;// 第二次输入密码
	HttpClient httpClient;	
	WebView  webview;
	
	HttpUtils httpUtils;
	RequestParams params;
	
	Handler handlerSign = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Json:
			// 一维数组
			// num:1(注册失败);2(注册成功);3(验证码不正确)
			// loginid:注册成功返回回来的会员id号

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				Log.d("11111111111111111111111111111111", str + "");
				try {
					JSONObject jsonObject = new JSONObject(str);
					Integer num = Integer.parseInt(jsonObject.getString("num"));
					Log.d("222222222222222222", num + "");

					switch (num) {
					case 1:
						Toast.makeText(RegisteXieyiActivity.this, "注册失败",
								Toast.LENGTH_LONG).show();
						break;
					case 2:

						/** 隐藏软键盘 **/
						View view = getWindow().peekDecorView();
						if (view != null) {
							InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							inputmanger.hideSoftInputFromWindow(
									view.getWindowToken(), 0);
						}
						Toast.makeText(RegisteXieyiActivity.this, "注册成功",
								Toast.LENGTH_LONG).show();
						IsTrue.isSgin = true;
						IsTrue.userId = Integer.parseInt(jsonObject
								.getString("loginid"));
						Intent intent = new Intent(RegisteXieyiActivity.this,
								EditDataActivity.class);
						startActivity(intent);

						finish();
						break;
					case 3:
						Toast.makeText(getApplicationContext(), "验证码不相等",
								Toast.LENGTH_LONG).show();
						break;
					case 4:
						Toast.makeText(getApplicationContext(), "账号已存在",
								Toast.LENGTH_LONG).show();
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
			// Json:
			// 一维数组
			// statue:1(存在号码);2(不存在号码)
			// sms:当statue为1时返回0不做任何判断，当statue为2时，根据西面的code值提示对应信息

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(str);
					String strsms = jsonObject.getString("sms");
					Log.d("出问题的原因", strsms);
					Integer num = Integer.parseInt(jsonObject
							.getString("statue"));
					switch (num) {
					case 1:
						Toast.makeText(RegisteXieyiActivity.this, "手机号已注册",
								Toast.LENGTH_LONG).show();
						break;
					case 2:
						time.start();// 开始计时
						Toast.makeText(RegisteXieyiActivity.this,
								"手机号未注册，已发短信验证码", Toast.LENGTH_LONG).show();
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
		setContentView(R.layout.activity_register_xieyi);
//		init();
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		ibbacking = (ImageButton) findViewById(R.id.btnMemberBacking);
		
		ibbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		   webview=(WebView) findViewById(R.id.webXieyi);
			httpUtils = new HttpUtils();
			params = new RequestParams();
//			params.addBodyParameter("uid", IsTrue.userId + "");
			httpUtils.send(HttpMethod.POST, MyUrl.registerXieyi, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							Show.toast(getApplicationContext(), "加载失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							// TODO Auto-generated method stub
							String list = responseInfo.result;
							System.out.println("OOOOOOOOOOOOOOO:"+list);
						 
								JSONObject object;
								try {
									object = new JSONObject(list);
									
									String url = object.getString("url");
									if(!TextUtils.isEmpty(url)){
										webview.loadUrl(url);
										   //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
										webview.setWebViewClient(new WebViewClient(){
									           @Override
									        public boolean shouldOverrideUrlLoading(WebView view, String url) {
									            // TODO Auto-generated method stub
									               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
									             view.loadUrl(url);
									            return true;
									        }
									       });
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
						}
				
			}
				
			);
		   
		  
		
//		btngetVerificationCode.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				/** 隐藏软键盘 **/
//				View view = getWindow().peekDecorView();
//				if (view != null) {
//					InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
//							0);
//				}
//
//				String strPhoneNum = etMemberAccountNum.getText().toString();
//				if ("".equals(strPhoneNum.trim())) {
//					Toast.makeText(RegisteXieyiActivity.this, "手机号码不能为空",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//
//				Boolean booleans = IsTrue.isMobileNum(strPhoneNum);
//				if (booleans) {
//					new Thread() {
//						@Override
//						public void run() {
//							try {
//
//								HttpPost post = new HttpPost(
//										MyUrl.StringVerificationCode);
//								String strNumOne = etMemberAccountNum.getText()
//										.toString();
//								List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//								params.add(new BasicNameValuePair("name",
//										strNumOne));
//
//								post.setEntity(new UrlEncodedFormEntity(params,
//										HTTP.UTF_8));
//
//								HttpResponse response = httpClient
//										.execute(post);
//
//								if (response.getStatusLine().getStatusCode() == 200) {
//
//									String str = EntityUtils.toString(response
//											.getEntity());
//									Message msg = new Message();
//									msg.what = 0x123;
//									msg.obj = str;
//									handler.sendMessage(msg);
//
//								}
//
//							} catch (UnsupportedEncodingException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (ClientProtocolException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}.start();
//				} else {
//					Toast.makeText(RegisteXieyiActivity.this, "手机号码输入错误",
//							Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		});
//		btnsign.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if ("".equals(etMemberAccountNum.getText().toString().trim())) {
//					Toast.makeText(RegisteXieyiActivity.this, "手机号码不能为空",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if ("".equals(etMemberAccount.getText().toString().trim())) {
//					Toast.makeText(RegisteXieyiActivity.this, "验证码不能为空",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if ("".equals(etMemberPassWord.getText().toString().trim())) {
//					Toast.makeText(RegisteXieyiActivity.this, "密码不能为空",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if ("".equals(etMemberRePassWord.getText().toString().trim())) {
//					Toast.makeText(RegisteXieyiActivity.this, "第二次输入密码不能为空",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (!etMemberPassWord.getText().toString().trim()
//						.equals(etMemberRePassWord.getText().toString().trim())) {
//					Toast.makeText(RegisteXieyiActivity.this, "两次输入密码不一致，请重新输入",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				new Thread() {
//					@Override
//					public void run() {
//						try {
//
//							HttpPost post = new HttpPost(MyUrl.Stringzhuce);
//
//							List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//							params.add(new BasicNameValuePair("name",
//									etMemberAccountNum.getText().toString()));
//							params.add(new BasicNameValuePair("yzm",
//									etMemberAccount.getText().toString()));
//							params.add(new BasicNameValuePair("pwd",
//									etMemberPassWord.getText().toString()));
//
//							post.setEntity(new UrlEncodedFormEntity(params,
//									HTTP.UTF_8));
//
//							HttpResponse response = httpClient.execute(post);
//
//							if (response.getStatusLine().getStatusCode() == 200) {
//
//								String str = EntityUtils.toString(response
//										.getEntity());
//								Message msg = new Message();
//								msg.what = 0x123;
//								msg.obj = str;
//								handlerSign.sendMessage(msg);
//
//							}
//
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ClientProtocolException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}.start();
//			}
//		});
//
//		
//		//点击进入用户注册协议
//		btnUserXieyi.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//					 
//			}
//		});

	}

	private void init() {
		// TODO Auto-generated method stub
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		ibbacking = (ImageButton) findViewById(R.id.btnMemberBacking);
		btngetVerificationCode = (Button) findViewById(R.id.btngetVerificationCode);
		btnsign = (Button) findViewById(R.id.btnsign);
		time = new TimeCount(120000, 1000);// 构造CountDownTimer对象
		etMemberAccountNum = (EditText) findViewById(R.id.etMemberAccountNum);// 手机号码
		etMemberAccount = (EditText) findViewById(R.id.etMemberAccount);// 输入的验证码
		etMemberPassWord = (EditText) findViewById(R.id.etMemberPassWord);// 第一次输入密码
		etMemberRePassWord = (EditText) findViewById(R.id.etMemberRePassWord);// 第二次输入密码
		btnUserXieyi = (Button) findViewById(R.id.btnUserXieyi);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registered, menu);
		return true;
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btngetVerificationCode.setText("重新验证");
			btngetVerificationCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btngetVerificationCode.setClickable(false);
			btngetVerificationCode.setText("      " + millisUntilFinished
					/ 1000 + "秒" + "      ");
		}
	}
}
