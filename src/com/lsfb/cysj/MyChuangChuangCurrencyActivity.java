package com.lsfb.cysj;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

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

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;
import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.MyIndexOfHistoryActivity.ViewHolder;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MyChuangChuangCurrencyActivity extends Activity implements
		IXListViewListener {
	ImageButton ibMyChuangChuangCurrencybacking;// 返回
	XListView listviewMychuangchuangCurrency;
	BaseAdapter baseAdapter;
	HttpClient httpClient;
	Intent intent;
	String ccb = null;
	List<Map<String, Object>> list;
	int count = 0;
	ResDialog dialog;
	Button btnChongzhi;
	TextView tvZuoyong;
	
	// 商户PID
		public static final String PARTNER = "2088801085323871";
		// 商户收款账号
		public static final String SELLER = "chuang-chuang1@csoow.com";
		// 商户私钥，pkcs8格式
		public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKpLowNNuxIPXTjm"+
	"P7pNiPNkCEJNxv47J6NvrSSI6l+SHtHovfKFwr93HeEb4bmBsoY7QZdZpRyRysOI"+
	"4n7Hib7LiWyMPWU1Q2K+9w5hpBXlz9XJwgteO9pC1x2A/eHts7j4RA1fkButjSxu"+
	"SRKg87hDieRMKQtvj0azyDj+QjxJAgMBAAECgYBQ3zib8gcb7YX05otvGzElfOTb"+
	"L/qHc2pAmGFeT1MWbdsLDn9eNB2nNZdP9yC8Kxav3mYozR63MZlcQJ5nXLVRyqa5"+
	"Xnfdw96EoDr/wS9DZVOBqr43GDSh02oNUn+8kLwMdMRHkzRYlMOQu5mJeNky8G05"+
	"zbCQlEEXzaoDzQk9fQJBAN/He7O4Ow8iVPcgsyrhehywHokeXbBa6JAk+N/gM+CE"+
	"7H6LRdEur7g2OGizAk/jKPNcMzZuS77/Ir9FatzGIacCQQDC0Lz5gqugqTano0sA"+
	"BRLNgCi2Ra34zFKeLKeffKAfOW7Bp8Q6p5Hmvx2uqFtDep7Hs2kfDdkbljf0AKJM"+
	"DRCPAkAMcw6guvkeKGzNqtYM5qpiejHYswXHT+dsTYJDAjggn4SArcLellhUST/u"+
	"IzdXtm2KzHBU8OHp6EvIlFYTnjo1AkEAkGctTD1BfmsvKf9uHmukTlMK2mC33c2G"+
	"B9zNuvgjsEFgCYeTem6vRTywgcAlNdV0UE56Qxx+q2Yjv2eg5YJhnQJAMOkyTV1w"+
	"alqqdjZZvAq12t7ltdHP9SdSoK+MkAGarONoBqSMOJH5ibqy6FEuoE7BPgrKEkpt"+
	"w2C+ba7XRYd3qA==";
		// 支付宝公钥
		public static final String RSA_PUBLIC = "";
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

					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(MyChuangChuangCurrencyActivity.this, "支付成功",
								Toast.LENGTH_SHORT).show();
					} else {
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(MyChuangChuangCurrencyActivity.this, "支付结果确认中",
									Toast.LENGTH_SHORT).show();

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(MyChuangChuangCurrencyActivity.this, "支付失败",
									Toast.LENGTH_SHORT).show();

						}
					}
					break;
				}
				case SDK_CHECK_FLAG: {
					Toast.makeText(MyChuangChuangCurrencyActivity.this, "检查结果为：" + msg.obj,
							Toast.LENGTH_SHORT).show();
					break;
				}
				default:
					break;
				}
			};
		};
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				dialog.dismiss();
				String str = msg.obj.toString();
				// Json:
				// 返回一维数组
				// 有值存在
				// num:2存在指数信息
				// lit:二维数组
				// source:指数来源
				// time:指数变化时间
				// value:指数变化值
				// 无值存在
				// num:1没有指数信息
				list = new ArrayList<Map<String, Object>>();
				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(MyChuangChuangCurrencyActivity.this,
								"没有任何创创币信息", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("source", temp.getString("source"));
							map.put("time", temp.getString("time"));
							map.put("value", temp.getString("value"));
							list.add(map);

						}
						break;
						
					default:
						break;
					}
					listviewMychuangchuangCurrency.setAdapter(baseAdapter);
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

				String str = msg.obj.toString();
				// Json:
				// 返回一维数组
				// 有值存在
				// num:2存在指数信息
				// lit:二维数组
				// source:指数来源
				// time:指数变化时间
				// value:指数变化值
				// 无值存在
				// num:1没有指数信息

				Log.d("2222222222222222222222222", str);
				try {
					if (!str.equals("null")) {
						JSONObject jsonObject = new JSONObject(str);
						switch (Integer.parseInt(jsonObject.get("num")
								.toString())) {
						case 1:

							break;
						case 2:
							JSONArray jsonArray = new JSONArray(jsonObject.get(
									"list").toString());
							for (int i = 0; i < jsonArray.length(); ++i) {
								JSONObject temp = (JSONObject) jsonArray.get(i);
								Map<String, Object> map = new HashMap<String, Object>();
								// map.put("id", temp.getString("id"));
								map.put("source", temp.getString("source"));
								map.put("time", temp.getString("time"));
								map.put("value", temp.getString("value"));
								list.add(map);

							}
							break;

						default:
							break;
						}
						baseAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	private TextView rlMycurrency_ccb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_chuang_chuang_currency);
		init();
		rlMycurrency_ccb.setText("金额："+ccb);
		count = 0;
		ibMyChuangChuangCurrencybacking
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		// 初始化适配器
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(MyChuangChuangCurrencyActivity.this)
							.inflate(R.layout.indexofhistory, null);
					holder.tvHead = (TextView) v
							.findViewById(R.id.tv_indexofHistory_source);
					holder.tvTime = (TextView) v
							.findViewById(R.id.tv_indexofHistory_time);
					holder.tvNum = (TextView) v
							.findViewById(R.id.tv_indexofHistory_value);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				holder.tvHead.setText(list.get(position).get("source")
						.toString());
				holder.tvTime
						.setText(list.get(position).get("time").toString());
				holder.tvNum
						.setText(list.get(position).get("value").toString());
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
		chushihua();
	}

	private void init() {
		// TODO Auto-generated method stub
		ibMyChuangChuangCurrencybacking = (ImageButton) findViewById(R.id.ibMyChuangChuangCurrencybacking);
		listviewMychuangchuangCurrency = (XListView) findViewById(R.id.listviewMychuangchuangCurrency);
		rlMycurrency_ccb = (TextView) findViewById(R.id.rlMycurrency_ccb);
		btnChongzhi= (Button) findViewById(R.id.btnChongzhi);
		btnChongzhi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 订单
				String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "20.00");

				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();

				Runnable payRunnable = new Runnable() {

					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(MyChuangChuangCurrencyActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);

						Message msg = new Message();
						msg.what = SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};

				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			}
		});
		tvZuoyong= (TextView) findViewById(R.id.tvZuoyong);
		
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		listviewMychuangchuangCurrency.setPullLoadEnable(true);
		listviewMychuangchuangCurrency.setXListViewListener(this);
//		intent = getIntent();
//		ccb =getIntent().getStringExtra("ccb").toString();
		ccb =  getIntent().getExtras().getString("ccb").toString();
		
		tvZuoyong.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(MyChuangChuangCurrencyActivity.this,ChuangbiZuoyongActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	
	
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

	public class ViewHolder {
		private TextView tvHead;
		private TextView tvTime;
		private TextView tvNum;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index_of_history, menu);
		return true;
	}

	private void onLoad() {
		listviewMychuangchuangCurrency.stopRefresh();
		listviewMychuangchuangCurrency.stopLoadMore();
		listviewMychuangchuangCurrency.setRefreshTime("刚刚");
	}

	public void chushihua() {
		// TODO Auto-generated method stub
		dialog = new ResDialog(MyChuangChuangCurrencyActivity.this, R.style.MyDialog,
				"努力加载中", R.drawable.loads);
		dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringMychuangchuang);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));

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

	private void loadRemnantListItem() {
		// 滚到加载余下的数据
		// 动态的改变listAdapter.getCount()的返回值
		// 使用Handler调用listAdapter.notifyDataSetChanged();更新数据

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringMychuangchuangs);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
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

	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				count = 0;
				chushihua();
				baseAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadRemnantListItem();
				baseAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

}
