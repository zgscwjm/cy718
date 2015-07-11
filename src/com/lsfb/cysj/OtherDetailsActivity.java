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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.CreativeFragments;
import com.lsfb.cysj.fragment.DynamicFragment;
import com.lsfb.cysj.fragment.OtherHomePageFragment;
import com.lsfb.cysj.view.CircleImageView;

public class OtherDetailsActivity extends FragmentActivity {
	ImageButton ibbacking;// 返回
	TextView tvHomePage;// 主页
	TextView tvDynamic;// 动态
	TextView tvCreative;// 创意
	// NoViewPage noViewPage;
	LinearLayout ll_my_details_Edit_Data;// 编辑资料
	LinearLayout homePageFragment_show;
	LinearLayout dynamicFragment_show;
	LinearLayout creativeFragment_show;
	CreativeFragments fragmebt_creatuve;
	OtherHomePageFragment fragment_OtherHomePageFragment;
	DynamicFragment dynamicFragment;
	CircleImageView civ_otherdetails;// 头像
	TextView tv_Otherdetails_name;// 名字
	TextView tv_Otherdetails_signatur;// 个性签名
	Button btn_Otherdetails_Look;// 关注和取消关注
	HttpClient httpClient;
	String strotherNum;
	String otherheadaddress;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				Log.d("11111111111111111111111111111111", str + "");
				try {
					JSONObject jsonObject = new JSONObject(str);
					String strimage = jsonObject.getString("images").toString();
					BitmapUtils bitmapUtils = new BitmapUtils(
							OtherDetailsActivity.this);
					otherheadaddress = ImageAddress.Stringhead + strimage;
					bitmapUtils.display(civ_otherdetails, otherheadaddress);
					tv_Otherdetails_name.setText(jsonObject.getString(
							"nickname").toString());
					tv_Otherdetails_signatur.setText(jsonObject.getString(
							"qianm").toString());
					String strsignatur = jsonObject.getString("qianm")
							.toString();
					if (strsignatur.length() <= 16) {
						tv_Otherdetails_signatur.setText(strsignatur);
					} else {
						tv_Otherdetails_signatur.setText(strsignatur.substring(
								0, 16));
					}
					String strfocus = jsonObject.getString("focus").toString();
					switch (Integer.parseInt(strfocus)) {
					case 1:
						btn_Otherdetails_Look.setText("关注");
						break;
					case 2:
						btn_Otherdetails_Look.setText("取消关注");
						break;
					default:
						break;
					}
					// 传值过去给Fragment
					fragment_OtherHomePageFragment.setDate(jsonObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	Handler handlerguanzhu = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				System.err.println(str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.getString("num")
							.toString())) {
					case 1:
						Toast.makeText(OtherDetailsActivity.this, "关注失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(OtherDetailsActivity.this, "关注成功",
								Toast.LENGTH_SHORT).show();
						btn_Otherdetails_Look.setText("取消关注");
						break;
					case 3:
						Toast.makeText(OtherDetailsActivity.this, "已经关注了",
								Toast.LENGTH_SHORT).show();
						break;
					case 4:
						Toast.makeText(OtherDetailsActivity.this, "已经是好友了",
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
	Handler handlernoguanzhu = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.getString("num")
							.toString())) {
					case 1:
						Toast.makeText(OtherDetailsActivity.this, "取消关注失败)",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(OtherDetailsActivity.this, "取消关注成功",
								Toast.LENGTH_SHORT).show();
						btn_Otherdetails_Look.setText("关注");
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
		setContentView(R.layout.activity_other_details);
		init();
		ibbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 改变颜色
		changecolor();
		// 私信
		ll_my_details_Edit_Data.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OtherDetailsActivity.this,
						NewsActivity.class);
				intent.putExtra("id", strotherNum);
				intent.putExtra("headaddress", otherheadaddress);
				startActivity(intent);
			}
		});
		btn_Otherdetails_Look.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (IsTrue.userId == 0) {
					Toast.makeText(OtherDetailsActivity.this, "您还没有登录，请先登录",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String context = ((Button) v).getText().toString();
				if (context.equals("关注")) {
					guanzhu();
				} else if (context.equals("取消关注")) {
					noguanzhu();
				}
			}
		});
		chushihua();
	}

	protected void noguanzhu() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.stringNoLook);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("mid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("uid", strotherNum));

					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlernoguanzhu.sendMessage(msg);

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

	protected void guanzhu() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.stringLook);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("mid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("uid", strotherNum));

					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlerguanzhu.sendMessage(msg);

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

	private void chushihua() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringOtherDetails);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("mid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("uid", strotherNum));

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

	private void changecolor() {
		// TODO Auto-generated method stub
		tvHomePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvHomePage.setBackgroundResource(R.drawable.shape);
				tvHomePage.setTextColor(OtherDetailsActivity.this
						.getResources().getColorStateList(R.color.white));
				tvDynamic.setBackgroundResource(R.color.white);
				tvDynamic.setTextColor(OtherDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				tvCreative.setBackgroundResource(R.color.white);
				tvCreative.setTextColor(OtherDetailsActivity.this
						.getResources().getColorStateList(R.color.greymy));
				homePageFragment_show.setVisibility(View.VISIBLE);
				dynamicFragment_show.setVisibility(View.GONE);
				creativeFragment_show.setVisibility(View.GONE);

				// noViewPage.setCurrentItem(0);
			}
		});
		tvDynamic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvDynamic.setBackgroundResource(R.drawable.shape);
				tvDynamic.setTextColor(OtherDetailsActivity.this.getResources()
						.getColorStateList(R.color.white));
				tvHomePage.setBackgroundResource(R.color.white);
				tvHomePage.setTextColor(OtherDetailsActivity.this
						.getResources().getColorStateList(R.color.greymy));
				tvCreative.setBackgroundResource(R.color.white);
				tvCreative.setTextColor(OtherDetailsActivity.this
						.getResources().getColorStateList(R.color.greymy));
				homePageFragment_show.setVisibility(View.GONE);
				dynamicFragment_show.setVisibility(View.VISIBLE);
				creativeFragment_show.setVisibility(View.GONE);
				// noViewPage.setCurrentItem(1);
			}
		});
		tvCreative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvCreative.setBackgroundResource(R.drawable.shape);
				tvCreative.setTextColor(OtherDetailsActivity.this
						.getResources().getColorStateList(R.color.white));
				tvHomePage.setBackgroundResource(R.color.white);
				tvHomePage.setTextColor(OtherDetailsActivity.this
						.getResources().getColorStateList(R.color.greymy));
				tvDynamic.setBackgroundResource(R.color.white);
				tvDynamic.setTextColor(OtherDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				homePageFragment_show.setVisibility(View.GONE);
				dynamicFragment_show.setVisibility(View.GONE);
				creativeFragment_show.setVisibility(View.VISIBLE);
				// noViewPage.setCurrentItem(2);
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		// noViewPage = (NoViewPage) findViewById(R.id.nvp_change);
		// FragmentAdapter adapter = new FragmentAdapter(
		// getSupportFragmentManager(), 3, 1);
		// noViewPage.setAdapter(adapter);
		Intent intent = getIntent();
		strotherNum = intent.getStringExtra("id");
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		btn_Otherdetails_Look = (Button) findViewById(R.id.btn_Otherdetails_Look);
		civ_otherdetails = (CircleImageView) findViewById(R.id.civ_otherdetails);
		tv_Otherdetails_name = (TextView) findViewById(R.id.tv_Otherdetails_name);
		tv_Otherdetails_signatur = (TextView) findViewById(R.id.tv_Otherdetails_signatur);
		ibbacking = (ImageButton) findViewById(R.id.ibmyotherbacking);
		tvHomePage = (TextView) findViewById(R.id.tvHomePage_Other);
		tvDynamic = (TextView) findViewById(R.id.tvDynamic_Other);
		tvCreative = (TextView) findViewById(R.id.tvCreative_Other);
		ll_my_details_Edit_Data = (LinearLayout) findViewById(R.id.ll_other_details_Edit_Data);
		homePageFragment_show = (LinearLayout) findViewById(R.id.homePageFragment_show_Other);
		dynamicFragment_show = (LinearLayout) findViewById(R.id.dynamicFragment_show_Other);
		creativeFragment_show = (LinearLayout) findViewById(R.id.creativeFragment_show_Other);
		fragmebt_creatuve = (CreativeFragments) OtherDetailsActivity.this
				.getSupportFragmentManager().findFragmentById(
						R.id.fragmebt_creatuves);
		fragmebt_creatuve.changeclass("2", strotherNum, IsTrue.userId + "");
		fragment_OtherHomePageFragment = (OtherHomePageFragment) OtherDetailsActivity.this
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_OtherHomePageFragment);
		dynamicFragment = (DynamicFragment) OtherDetailsActivity.this
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_dynamicOther);
		dynamicFragment.setClassData("2", strotherNum);
		fragment_OtherHomePageFragment.zhiku();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.other_details, menu);
		return true;
	}

}
