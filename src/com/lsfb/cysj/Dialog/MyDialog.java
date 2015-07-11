package com.lsfb.cysj.Dialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Utils;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyDialog extends Dialog {
	String name;
	String gamemoney;
	public Context context;
	private TextView apply_money;
	EditText apply_name, apply_photo, apply_num;
	Button btn, apply_num_btn;
	String phonenum;// 电话号码
	String zhucephone = "";// 注册电话
	String zhucename;// 注册名字
	String zhuceyanzheng;// 注册验证
	String loginid;// 注册成功返回的id
	AsyncHttpClient client;
	RequestParams params;
	String gameId;
	String states2 = "";// 验证码
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

	public MyDialog(Context context) {
		super(context);
		this.context = context;
	}

	public MyDialog(Context context, int theme, String gamemoney, String gameid) {
		super(context, theme);
		this.context = context;
		this.gamemoney = gamemoney;
		this.gameId = gameid;
	}

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
					Toast.makeText(context, "成功支付" + gamemoney + "元",
							Toast.LENGTH_LONG).show();
					checkmoney();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(context, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
						.show();
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
		params.put("uid",loginid+ "");
		params.put("sid", gameId);
		client.post(MyUrl.Stringcheckgame, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.err.println(response);
				try {
					int intnum = Integer.parseInt(response.getString("num")
							.toString());
					switch (intnum) {
					case 1:
						Toast.makeText(context, "报名失败", Toast.LENGTH_SHORT)
								.show();
						break;
					case 2:
						Toast.makeText(context, "报名成功", Toast.LENGTH_SHORT)
								.show();
						dismiss();
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
				Toast.makeText(getContext(), errorResponse.toString(),
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.apply);
		init();
		data();
	}

	private void data() {
		apply_money.setText("¥" + gamemoney);
		apply_num_btn
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						phonenum = apply_photo.getText().toString().trim();
						if (phonenum.equals("")) {
							Toast.makeText(getContext(), "号码不能为空",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Boolean booleans = IsTrue.isMobileNum(phonenum);
						if (booleans) {
							if (Utils.isFastDoubleClick() == true) {
								return;
							}
							yanzheng();
						} else {
							Toast.makeText(getContext(), "号码错误",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		btn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {

				phonenum = apply_photo.getText().toString().trim();
				zhucename = apply_name.getText().toString().trim();
				zhuceyanzheng = apply_num.getText().toString().trim();
				if (zhucename.equals("")) {
					Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (phonenum.equals("")) {
					Toast.makeText(getContext(), "号码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (phonenum.equals(zhucephone)) {
				} else {
					Toast.makeText(getContext(), "和验证的号码不相同",
							Toast.LENGTH_SHORT).show();
					return;
				}
				System.out.println(zhuceyanzheng + "SSSS" + states2);
				if (zhuceyanzheng.equals("") || !zhuceyanzheng.equals(states2)) {
					Toast.makeText(getContext(), "注册验证码错误", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				zhuce();
			}
		});
	}

	protected void zhuce() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("tel", phonenum);
		params.put("yzm", zhuceyanzheng);
		params.put("name", zhucename);
		params.put("yzms", states2);
		client.post(MyUrl.registers, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// Toast.makeText(getContext(), response.toString(),
				// Toast.LENGTH_SHORT).show();
				System.out.println(response);
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(getContext(), "注册失败", Toast.LENGTH_SHORT)
								.show();
					} else if (i == 2) {
						Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT)
								.show();
						loginid = response.getString("loginid").toString();
						chongzhi();
					} else if (i == 3) {
						Toast.makeText(getContext(), "验证码不正确",
								Toast.LENGTH_SHORT).show();
					} else if (i == 4) {
						Toast.makeText(getContext(), "账号已注册",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getContext(), errorResponse.toString(),
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
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
				PayTask alipay = new PayTask((Activity) context);
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

	protected void yanzheng() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("name", phonenum);
		client.post(MyUrl.register, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.err.println(response);
				try {
					String state = response.getString("state");
					int i = Integer.parseInt(state);
					if (i == 1) {
						Toast.makeText(getContext(), "账号已存在",
								Toast.LENGTH_SHORT).show();
					} else if (i == 2) {
						zhucephone = response.getString("tel").toString();
						states2 = response.getString("states2").toString();
						Toast.makeText(getContext(), "可以注册" + zhucephone,
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getContext(), errorResponse.toString(),
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void init() {
		apply_money = (TextView) findViewById(R.id.apply_money);
		btn = (Button) findViewById(R.id.apply_btn);
		apply_photo = (EditText) findViewById(R.id.apply_photo);
		apply_name = (EditText) findViewById(R.id.apply_name);
		apply_num = (EditText) findViewById(R.id.apply_num);
		apply_num_btn = (Button) findViewById(R.id.apply_num_btn);
		setCanceledOnTouchOutside(true);
	}
}
