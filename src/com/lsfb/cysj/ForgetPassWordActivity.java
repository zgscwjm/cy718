package com.lsfb.cysj;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.base.BaseActivity;
import com.lsfb.cysj.utils.Show;
import com.lsfb.cysj.view.CircleImageView;
import com.lsfb.cysj.view.Code;

public class ForgetPassWordActivity extends BaseActivity {
	int state = 1;// 1代表验证手机，2代表手机确认，3代表重置密码
	ImageButton btnforgetBacking;// 返回上一层
	ImageButton ibVerificationCodeForget;// 验证手机时候的验证码
	Button btnNext;// 下一步
	LinearLayout llforgetVerifyPhone;// 验证手机的线性布局
	LinearLayout llforgetPhoneSure;// 手机确认的线性布局
	LinearLayout llforgetResetPassword;// 重置密码的线性布局
	// 标签的颜色
	// 1
	/**
	 * 圆图标 civVerifyPhone
	 */
	CircleImageView civVerifyPhone;// 圆图标
	TextView tvVerifyPhone;// 圆里面的数字
	TextView tvVerifyPhonetext;// 文字
	// 2
	CircleImageView civPhoneSure;// 圆图标
	TextView tvPhoneSure;// 圆里面的数字
	TextView tvPhoneSuretext;// 文字
	// 3
	CircleImageView civResetPassword;// 圆图标
	TextView tvResetPassword;// 圆里面的数字
	TextView tvResetPasswordtext;// 文字
	
	EditText  etForgetPhoneNum,etforgetAccountForget,etforgetPassWordForget,etforgetRePassWordAgain;

	Button btngetVerificationCodeForget;// 获取验证码
	private TimeCount time;// 验证码刷新时间
	
	String phone;
	String sms;
	
	HttpUtils httpUtils;
	RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pass_word);
		init();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		btnforgetBacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 初始化验证码
		ibVerificationCodeForget.setImageBitmap(Code.getInstance()
				.createBitmap());
		ibVerificationCodeForget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 刷新验证码
				ibVerificationCodeForget.setImageBitmap(Code.getInstance()
						.createBitmap());
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				state++;
				switch (state) {
				case 1:  //第一次点击保存手机号码
					phone=etForgetPhoneNum.getText().toString().trim();
					break;
				case 2://验证手机好和验证码
					//第一次点击保存手机号码
					phone=etForgetPhoneNum.getText().toString().trim();
					
					llforgetVerifyPhone.setVisibility(View.GONE);
					llforgetPhoneSure.setVisibility(View.VISIBLE);
					// 修改颜色
					civVerifyPhone.setImageResource(R.color.greyforget);
					tvVerifyPhone.setBackgroundResource(R.color.greyforget);
					tvVerifyPhonetext.setTextColor(ForgetPassWordActivity.this
							.getResources().getColorStateList(
									R.color.greyforget));

					civPhoneSure.setImageResource(R.color.blueMain);
					tvPhoneSure.setBackgroundResource(R.color.blueMain);
					tvPhoneSuretext
							.setTextColor(ForgetPassWordActivity.this
									.getResources().getColorStateList(
											R.color.blueMain));
				
					
					break;
				case 3://点第二次
					String yanzhengma=etforgetAccountForget.getText().toString().trim();
					if(!yanzhengma.equals(sms)){
						Show.toast(getApplicationContext(), "验证码不对");
						return; 
					}					
					llforgetPhoneSure.setVisibility(View.GONE);
					llforgetResetPassword.setVisibility(View.VISIBLE);
					// 修改颜色
					civPhoneSure.setImageResource(R.color.greyforget);
					tvPhoneSure.setBackgroundResource(R.color.greyforget);
					tvPhoneSuretext.setTextColor(ForgetPassWordActivity.this
							.getResources().getColorStateList(
									R.color.greyforget));

					civResetPassword.setImageResource(R.color.blueMain);
					tvResetPassword.setBackgroundResource(R.color.blueMain);
					tvResetPasswordtext
							.setTextColor(ForgetPassWordActivity.this
									.getResources().getColorStateList(
											R.color.blueMain));

					btnNext.setText("完成");
					break;
					
				case 4://点击完成					
				String password=etforgetPassWordForget.getText().toString().trim();
				String passwordAgain=etforgetRePassWordAgain.getText().toString().trim();
					if(TextUtils.isEmpty(password) ||TextUtils.isEmpty(passwordAgain)){
						Show.toast(getApplicationContext(), "输入不能为空");
						return;
					}
					if(!password.equals(passwordAgain)){
						Show.toast(getApplicationContext(), "两次输入不一致");
						return;
					}
					
					params.addBodyParameter("name", phone+ "");
					params.addBodyParameter("pwd", password+ "");
					httpUtils.send(HttpMethod.POST, MyUrl.getyanzhengma, params,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// TODO Auto-generated method stub
									
								}						
				 	  }
					);
					
					break;
				default:
					break;
				}
			}
		});

		//获取验证码
		btngetVerificationCodeForget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				params.addBodyParameter("name", phone+ "");
				httpUtils.send(HttpMethod.POST, MyUrl.getyanzhengma, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub
								Show.toast(getApplicationContext(), "获取验证码失败");
							}

							@Override
							public void onSuccess(ResponseInfo<String> responseInfo) {
								// TODO Auto-generated method stub
								String list = responseInfo.result;
								System.out.println(list + "OOOOOOOOOOOOOOO");
								JSONObject object;							 
									try {
										object = new JSONObject(list);
										int state = object.getInt("state");
										String sms = object.getString("sms");
										String tel = object.getString("tel");
										
										if(1==state){
											Show.toast(getApplicationContext(), "账号不存在");
											return;
										}
										
										
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
									 
							} 
				  }
				);
				
				time.start();				
			}
		});
	}

	// 初始化
	private void init() {
		// TODO Auto-generated method stub
		btnforgetBacking = (ImageButton) findViewById(R.id.btnforgetBacking);
		ibVerificationCodeForget = (ImageButton) findViewById(R.id.ibVerificationCodeForget);
		btnNext = (Button) findViewById(R.id.btnNext);
		llforgetVerifyPhone = (LinearLayout) findViewById(R.id.llforgetVerifyPhone);
		llforgetPhoneSure = (LinearLayout) findViewById(R.id.llforgetPhoneSure);
		llforgetResetPassword = (LinearLayout) findViewById(R.id.llforgetResetPassword);
		// 标签
		civVerifyPhone = (CircleImageView) findViewById(R.id.civVerifyPhone);// 圆图标
		tvVerifyPhone = (TextView) findViewById(R.id.tvVerifyPhone);// 圆里面的数字
		tvVerifyPhonetext = (TextView) findViewById(R.id.tvVerifyPhonetext);// 文字
		// 2
		civPhoneSure = (CircleImageView) findViewById(R.id.civPhoneSure);// 圆图标
		tvPhoneSure = (TextView) findViewById(R.id.tvPhoneSure);// 圆里面的数字
		tvPhoneSuretext = (TextView) findViewById(R.id.tvPhoneSuretext);// 文字
		// 3
		civResetPassword = (CircleImageView) findViewById(R.id.civResetPassword);// 圆图标
		tvResetPassword = (TextView) findViewById(R.id.tvResetPassword);// 圆里面的数字
		tvResetPasswordtext = (TextView) findViewById(R.id.tvResetPasswordtext);// 文字
		
		etforgetPassWordForget = (EditText) findViewById(R.id.etforgetPassWordForget);// 文字
		
		etForgetPhoneNum=(EditText) findViewById(R.id.etForgetPhoneNum);//  输入手机号码
		etforgetRePassWordAgain=(EditText) findViewById(R.id.etforgetRePassWordAgain);// 
		
		etforgetAccountForget=(EditText) findViewById(R.id.etforgetAccountForget);//  输入手机号码

		btngetVerificationCodeForget = (Button) findViewById(R.id.btngetVerificationCodeForget);
		time = new TimeCount(120000, 1000);// 构造CountDownTimer对象
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forget_pass_word, menu);
		return true;
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btngetVerificationCodeForget.setText("重新验证");
			btngetVerificationCodeForget.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btngetVerificationCodeForget.setClickable(false);
			btngetVerificationCodeForget.setText("            "
					+ millisUntilFinished / 1000 + "秒" + "            ");
		}
	}
}
