package com.lsfb.cysj;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.MyDialog;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

public class GameDetailsActivity extends FragmentActivity implements
		OnClickListener {
	/**
	 * 本实例演示如何在Android中播放网络上的视频，这里牵涉到视频传输协议，视频编解码等知识点
	 * 
	 * @author Administrator Android当前支持两种协议来传输视频流一种是Http协议，另一种是RTSP协议
	 *         Http协议最常用于视频下载等，但是目前还不支持边传输边播放的实时流媒体 同时，在使用Http协议
	 *         传输视频时，需要根据不同的网络方式来选择合适的编码方式，
	 *         比如对于GPRS网络，其带宽只有20kbps,我们需要使视频流的传输速度在此范围内。
	 *         比如，对于GPRS来说，如果多媒体的编码速度是400kbps，那么对于一秒钟的视频来说，就需要20秒的时间。这显然是无法忍受的
	 *         Http下载时，在设备上进行缓存，只有当缓存到一定程度时，才能开始播放。
	 * 
	 *         所以，在不需要实时播放的场合，我们可以使用Http协议
	 * 
	 *         RTSP：Real Time Streaming Protocal，实时流媒体传输控制协议。
	 *         使用RTSP时，流媒体的格式需要是RTP。 RTSP和RTP是结合使用的，RTP单独在Android中式无法使用的。
	 * 
	 *         RTSP和RTP就是为实时流媒体设计的，支持边传输边播放。
	 * 
	 *         同样的对于不同的网络类型（GPRS，3G等），RTSP的编码速度也相差很大。根据实际情况来
	 * 
	 *         使用前面介绍的三种方式，都可以播放网络上的视频，唯一不同的就是URI
	 * 
	 *         本例中使用VideoView来播放网络上的视频
	 */
	/**
	 * game_details_web 网络访问
	 */
	@ViewInject(R.id.game_details_web)
	private WebView webview;
	@ViewInject(R.id.game_details_back)
	private LinearLayout back;
	private ProgressDialog progressDialog;
	@ViewInject(R.id.game_details_btn)
	private Button baoming;
	@ViewInject(R.id.game_details_video)
	private VideoView video;
	@ViewInject(R.id.game_details_linearlayout_time)
	private TextView baomingtime;
	@ViewInject(R.id.game_details_linearlayout_money)
	private TextView baomingmoney;
	@ViewInject(R.id.game_details_title)
	private TextView game_details_title;
	Dialog dialog;
	Intent intent;
	String namesid = "";
	AsyncHttpClient client;
	RequestParams params;
	String gameid = "";
	String gamevedio = "";
	String gameurl = "";
	String gametime = "";
	String gamemoney = "";
	String orderInfo = ""; // 订单信息
	// 商户PID
	public static final String PARTNER = "2088801085323871";
	// 商户收款账号
	public static final String SELLER = "chuang-chuang1@csoow.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKpLowNNuxIPXTjmP7pNiPNkCEJNxv47J6NvrSSI6l+SHtHovfKFwr93HeEb4bmBsoY7QZdZpRyRysOI4n7Hib7LiWyMPWU1Q2K+9w5hpBXlz9XJwgteO9pC1x2A/eHts7j4RA1fkButjSxuSRKg87hDieRMKQtvj0azyDj+QjxJAgMBAAECgYBQ3zib8gcb7YX05otvGzElfOTbL/qHc2pAmGFeT1MWbdsLDn9eNB2nNZdP9yC8Kxav3mYozR63MZlcQJ5nXLVRyqa5Xnfdw96EoDr/wS9DZVOBqr43GDSh02oNUn+8kLwMdMRHkzRYlMOQu5mJeNky8G05zbCQlEEXzaoDzQk9fQJBAN/He7O4Ow8iVPcgsyrhehywHokeXbBa6JAk+N/gM+CE7H6LRdEur7g2OGizAk/jKPNcMzZuS77/Ir9FatzGIacCQQDC0Lz5gqugqTano0sABRLNgCi2Ra34zFKeLKeffKAfOW7Bp8Q6p5Hmvx2uqFtDep7Hs2kfDdkbljf0AKJMDRCPAkAMcw6guvkeKGzNqtYM5qpiejHYswXHT+dsTYJDAjggn4SArcLellhUST/uIzdXtm2KzHBU8OHp6EvIlFYTnjo1AkEAkGctTD1BfmsvKf9uHmukTlMK2mC33c2GB9zNuvgjsEFgCYeTem6vRTywgcAlNdV0UE56Qxx+q2Yjv2eg5YJhnQJAMOkyTV1walqqdjZZvAq12t7ltdHP9SdSoK+MkAGarONoBqSMOJH5ibqy6FEuoE7BPgrKEkptw2C+ba7XRYd3qA==";
	// public static final String RSA_PUBLIC =
	// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();
				System.err.println(resultStatus);
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(GameDetailsActivity.this,
							"成功支付" + gamemoney + "元", Toast.LENGTH_LONG).show();
					checkmoney();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(GameDetailsActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(GameDetailsActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(GameDetailsActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	protected void checkmoney() {
		// TODO Auto-generated method stub
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId + "");
		params.put("sid", namesid);
		System.out.println(IsTrue.userId+"SSSS"+namesid);
		client.post(MyUrl.Stringcheckgame, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						System.err.println(response);
						try {
							int intnum = Integer.parseInt(response.getString(
									"num").toString());
							switch (intnum) {
							case 1:
								Toast.makeText(GameDetailsActivity.this,
										"报名失败", Toast.LENGTH_SHORT).show();
								break;
							case 2:
								Toast.makeText(GameDetailsActivity.this,
										"报名成功", Toast.LENGTH_SHORT).show();

								break;
							case 3:
								Toast.makeText(GameDetailsActivity.this,
										"已经报名了", Toast.LENGTH_SHORT).show();

								break;
							default:
								break;
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Toast.makeText(GameDetailsActivity.this,
								errorResponse.toString(), Toast.LENGTH_SHORT)
								.show();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}
				});
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	protected void chongzhi() {
		// TODO Auto-generated method stub
		orderInfo = getOrderInfo("充值", "充值的具体金额", gamemoney);
		// orderInfo = getOrderInfo("充值", "充值的具体金额", "0.01");
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				PayTask alipay = new PayTask(GameDetailsActivity.this);
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_details);
		ViewUtils.inject(this);
		// showProgressDialog("提示", "正在加载......");
		init();
		data();
	}

	private void data() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", namesid);
		client.post(MyUrl.linesinger, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					gameid = response.getString("id").toString();
					gamevedio = response.getString("vedio").toString();
					gameurl = response.getString("url").toString();
					gametime = response.getString("time").toString();
					gamemoney = response.getString("money").toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				video.setMediaController(new MediaController(
						GameDetailsActivity.this));
				Uri uri = Uri.parse(ImageAddress.Stringgame + gamevedio);
				video.setVideoURI(uri);
				video.requestFocus();
				webview.setOnClickListener(GameDetailsActivity.this);
				// 设置WebView属性，能够执行Javascript脚本
				webview.getSettings().setJavaScriptEnabled(true);
				// 加载需要显示的网页

				webview.loadUrl(gameurl);
				// 设置Web视图
				webview.setWebViewClient(new HelloWebViewClient());
				baomingtime.setText(gametime);
				baomingmoney.setText(gamemoney);
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(),
						errorResponse.toString(), Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void showdialog() {
		// LayoutInflater inflater = LayoutInflater.from(this);
		// View v = inflater.inflate(R.layout.dialogview, null);
		//
		// LinearLayout layout = (LinearLayout)
		// v.findViewById(R.id.dialog_view);
		//
		// // main.xml中的ImageView
		// ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		// // 加载动画
		// Animation hyperspaceJumpAnimation =
		// AnimationUtils.loadAnimation(this,
		// R.anim.animation);
		// // 使用ImageView显示动画
		// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		//
		// dialog = new Dialog(GameDetailsActivity.this,
		// R.style.FullHeightDialog);
		// dialog.setCancelable(true);
		// dialog.show();
		// dialog.setContentView(layout, new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT));
		dialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		dialog.show();
	}

	// private void showProgressDialog(String title, String message) {
	// if (progressDialog == null) {
	//
	// progressDialog = ProgressDialog.show(GameDetailsActivity.this, title,
	// message,
	// false, true);
	// } else if (progressDialog.isShowing()) {
	// progressDialog.setTitle(title);
	// progressDialog.setMessage(message);
	// }
	//
	// progressDialog.show();
	// }

	/*
	 * 隐藏提示加载
	 */
	// public void hideProgressDialog() {
	//
	// if (progressDialog != null && progressDialog.isShowing()) {
	// progressDialog.dismiss();
	// }
	//
	// }
	private void init() {
		back.setOnClickListener(this);
		baoming.setOnClickListener(this);
		intent = getIntent();
		namesid = intent.getStringExtra("sid").toString();
		showdialog();
		String title = intent.getStringExtra("title").toString();
		game_details_title.setText(title);
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onPageFinished(WebView view, String url) {
			if (dialog.isShowing()) {

				dialog.dismiss();
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.game_details_back:
			finish();
			break;
		case R.id.game_details_btn:// 报名
			if (IsTrue.userId == 0) {
				showDialog();
			} else {
//			chongzhi();
			checkmoney();
			}
			break;
		default:
			break;
		}
	}

	private void showDialog() {
		Dialog dialog = new MyDialog(this, R.style.MyDialog, gamemoney, namesid);
		dialog.show();
	}
}
