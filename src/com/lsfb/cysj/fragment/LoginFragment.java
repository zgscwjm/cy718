package com.lsfb.cysj.fragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.lsbf.cysj.R;
import com.lsfb.cysj.EditDataActivity;
import com.lsfb.cysj.ForgetPassWordActivity;
import com.lsfb.cysj.HomeActivity;
import com.lsfb.cysj.HotIdeasGamesContent2Activity;
import com.lsfb.cysj.MeditationActivity;
import com.lsfb.cysj.RegisteredActivity;
import com.lsfb.cysj.ReleaseGameActivity;
import com.lsfb.cysj.ReleaseIdeasActivity;
import com.lsfb.cysj.SearchActivity;
import com.lsfb.cysj.WorldHeritageTiJiaoYiChan;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;


public class LoginFragment extends Fragment {
	ResDialog dialog;
	SharedPreferences preferences;
	String APPID;

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (IsTrue.isSgin) {
			llSign.setVisibility(View.GONE);
		} else {
			llSign.setVisibility(View.VISIBLE);
		}
		// 传值进去初始化
		MyFragment myFragment = (MyFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.myFragment);
		myFragment.setchu();
		super.onResume();
	}

	SharedPreferences.Editor editor;// 缓存账号和密码
	Button btnsign;// 登录
	Button btnfeggetPassWord;// 忘记密码
	Button btnRegistration;// 注册
	private View rootView;
	LinearLayout llSign;// 登录界面
	EditText etSignInAccountPhoneNum;// 登录账号
	EditText etSignInAccountPassWord;// 密码
	HttpClient httpClient;
	Handler handlerSign = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Json:
			// 一维数组
			// num:1(密码错误);2(登陆成功);3(账号错误)
			// loginid:登陆成功返回回来的会员id号  ,uid
			dialog.dismiss();
			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				Log.d("11111111111111111111111111111111", str + "");
				try {
					JSONObject jsonObject = new JSONObject(str);
					Integer num = Integer.parseInt(jsonObject.getString("num"));
					
					
					Log.d("222222222222222222", num + "");

					switch (num) {
					case 1:
						Toast.makeText(getActivity(), "账号密码输入错误",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						/** 隐藏软键盘 **/
						View view = getActivity().getWindow().peekDecorView();
						if (view != null) {
							InputMethodManager inputmanger = (InputMethodManager) getActivity()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							inputmanger.hideSoftInputFromWindow(
									view.getWindowToken(), 0);
						}
						Toast.makeText(getActivity(), "登录成功",
								Toast.LENGTH_SHORT).show();
						System.err.println(IsTrue.fabuchuangyi
								+ IsTrue.woyaochanxiu + IsTrue.fabubisai);
						if (IsTrue.fabuchuangyi == 1) {// 跳转发布创意
							Intent intent = new Intent(getActivity(),
									ReleaseIdeasActivity.class);
							startActivity(intent);
							IsTrue.exit = false;
							IsTrue.search = true;
							IsTrue.fabuchuangyi = 0;
						}
						if (IsTrue.woyaochanxiu == 1) {// 跳转禅修
							Intent intent = new Intent(getActivity(),
									MeditationActivity.class);
							startActivity(intent);
							IsTrue.exit = false;
							IsTrue.search = true;
							IsTrue.woyaochanxiu = 0;
						}
						if (IsTrue.fabubisai == 1) {// 跳转比赛
							Intent intent = new Intent(getActivity(),
									ReleaseGameActivity.class);
							startActivity(intent);
							IsTrue.exit = false;
							IsTrue.search = true;
							IsTrue.fabubisai = 0;
						}
						if (IsTrue.yichan == 1) {// 跳转世界遗产
							Intent intent = new Intent(getActivity(),
									WorldHeritageTiJiaoYiChan.class);
							startActivity(intent);
							IsTrue.yichan = 0;
						}
						// if (IsTrue.guanzhugame == 1) {
						// Intent intent = new
						// Intent(getActivity(),HotIdeasGamesContent2Activity.class);
						// startActivity(intent);
						// IsTrue.guanzhugame = 0 ;
						// }
						// 缓存账号和密码
						editor.putString("zhanghao", etSignInAccountPhoneNum
								.getText().toString());
						editor.putString("mima", etSignInAccountPassWord
								.getText().toString());
						editor.commit();
						String arr = jsonObject.getString("arr");
						Log.i("zgscwjm", "wjm+arr:"+arr);
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
						Log.d("会员的头像", IsTrue.Stringimage);
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
						IsTrue.intDengji=jsonObjectarr.getInt("memdj");
						IsTrue.intZqyz=jsonObjectarr.getInt("zqyz");
						
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
						IsTrue.userId = Integer.parseInt(jsonObject
								.getString("loginid"));
						llSign.setVisibility(View.GONE);
						EMChatManager.getInstance().login(
								"cysj"+jsonObject
								.getString("loginid"),
								etSignInAccountPassWord.getText().toString(),
								new EMCallBack() {// 回调
									@Override
									public void onSuccess() {
//										runOnUiThread(new Runnable() {
//											public void run() {
												EMGroupManager.getInstance()
														.loadAllGroups();
												EMChatManager.getInstance()
														.loadAllConversations();
												Log.d("main", "登陆聊天服务器成功！");
//											}
//										});
									}

									@Override
									public void onProgress(int progress,
											String status) {

									}

									@Override
									public void onError(int code, String message) {
										Log.d("main", "登陆聊天服务器失败！");
									}
								});
						MyFragment myFragment = (MyFragment) getActivity()
								.getSupportFragmentManager().findFragmentById(
										R.id.myFragment);
						myFragment.setchu();
						break;
					case 3:
						Toast.makeText(getActivity(), "账号密码输入错误",
								Toast.LENGTH_SHORT).show();
						break;
					case 4:
						/** 隐藏软键盘 **/
						View view4 = getActivity().getWindow().peekDecorView();
						if (view4 != null) {
							InputMethodManager inputmanger = (InputMethodManager) getActivity()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							inputmanger.hideSoftInputFromWindow(
									view4.getWindowToken(), 0);
						}
						Toast.makeText(getActivity(), "登录成功",
								Toast.LENGTH_SHORT).show();
						// 缓存账号和密码
						editor.putString("zhanghao", etSignInAccountPhoneNum
								.getText().toString());
						editor.putString("mima", etSignInAccountPassWord
								.getText().toString());
						editor.commit();
						IsTrue.isSgin = false;
						IsTrue.userId = Integer.parseInt(jsonObject
								.getString("loginid"));
						llSign.setVisibility(View.VISIBLE);
						Intent intent = new Intent(getActivity(),
								EditDataActivity.class);
						startActivity(intent);
						break;
					case 5:
						Toast.makeText(getActivity(), "您的账号已被锁定",
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_ideas, container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.activity_sign_in, container,
					false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		// 初始化唯一标识
		APPID = Settings.Secure.getString(getActivity().getContentResolver(),
				Settings.Secure.ANDROID_ID);
		init();
		if (IsTrue.isSgin) {
			llSign.setVisibility(View.GONE);
		} else {
			llSign.setVisibility(View.VISIBLE);
		}
		etSignInAccountPhoneNum
				.setText(preferences.getString("zhanghao", null));
		etSignInAccountPassWord.setText(preferences.getString("mima", null));
		btnsign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("".equals(etSignInAccountPhoneNum.getText().toString()
						.trim())) {
					Toast.makeText(getActivity(), "手机号码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("".equals(etSignInAccountPassWord.getText().toString()
						.trim())) {
					Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				dialog = new ResDialog(getActivity(), R.style.MyDialog, "正在登录",
						R.drawable.loads);
				dialog.show();
				/**
				 * 极光推送标签
				 */
				JPushInterface.resumePush(getActivity());
				IsTrue.szImei = null;
				JPushInterface.setTags(getActivity(), null, null);
				IsTrue.szImei = Settings.Secure.getString(getActivity()
						.getContentResolver(), Settings.Secure.ANDROID_ID);
				IsTrue.tags = new HashSet<String>();
				IsTrue.tags.add(IsTrue.szImei);
				JPushInterface.setTags(getActivity(), IsTrue.tags, null);
				new Thread() {
					@Override
					public void run() {
						try {

							HttpPost post = new HttpPost(MyUrl.StringSgin);

							List<NameValuePair> params = new ArrayList<NameValuePair>();

							params.add(new BasicNameValuePair("name",
									etSignInAccountPhoneNum.getText()
											.toString()));
							params.add(new BasicNameValuePair("pwd",
									etSignInAccountPassWord.getText()
											.toString()));

							post.setEntity(new UrlEncodedFormEntity(params,
									HTTP.UTF_8));

							HttpResponse response = httpClient.execute(post);

							if (response.getStatusLine().getStatusCode() == 200) {

								String str = EntityUtils.toString(response
										.getEntity());
								Message msg = new Message();
								msg.what = 0x123;
								msg.obj = str;
								handlerSign.sendMessage(msg);

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
		});
		btnfeggetPassWord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ForgetPassWordActivity.class);
				startActivity(intent);
			}
		});
		btnRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						RegisteredActivity.class);
				startActivity(intent);
			}
		});

		return rootView;
	}

	private void init() {
		// TODO Auto-generated method stub
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		btnsign = (Button) rootView.findViewById(R.id.btnsign);
		btnRegistration = (Button) rootView.findViewById(R.id.btnRegistration);
		btnfeggetPassWord = (Button) rootView
				.findViewById(R.id.btnfeggetPassWord);
		llSign = (LinearLayout) rootView.findViewById(R.id.llSign);
		etSignInAccountPhoneNum = (EditText) rootView
				.findViewById(R.id.etSignInAccountPhoneNum);
		etSignInAccountPassWord = (EditText) rootView
				.findViewById(R.id.etSignInAccountPassWord);
		preferences = getActivity().getSharedPreferences("crazyit",
				getActivity().MODE_WORLD_WRITEABLE);
		editor = preferences.edit();
	}

}
