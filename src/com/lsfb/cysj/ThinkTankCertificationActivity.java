package com.lsfb.cysj;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.ObjectOutputStream.PutField;
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

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Utils;
import com.lsfb.cysj.view.ResDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThinkTankCertificationActivity extends Activity {
	ImageButton ibThinkTankCertificationbacking;
	Button btnThink_tank;
	HttpClient httpClient;
	String regions;// homeId
	String home;// 地址
	String als;
	ResDialog dialog;
	Handler handler = new Handler() {
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
					int num = Integer.parseInt(jsonObject.getString("num")
							.toString());
					switch (num) {
					case 1:
						Toast.makeText(ThinkTankCertificationActivity.this,
								"认证条件不满足", Toast.LENGTH_SHORT).show();
						break;
					case 3:
						Toast.makeText(ThinkTankCertificationActivity.this,
								"已认证", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						regions = jsonObject.getString("regions").toString();
						home = jsonObject.getString("home").toString();
						als = jsonObject.getString("als").toString();
						Intent intent = new Intent(
								ThinkTankCertificationActivity.this,
								ThinkTankCertificationDetailsActivity.class);
						intent.putExtra("regions", regions);
						intent.putExtra("home", home);
						intent.putExtra("als", als);
						startActivity(intent);
						finish();
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
		setContentView(R.layout.activity_think_tank_certification);
		init();
		ibThinkTankCertificationbacking
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		btnThink_tank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				jumpActivity();
			}
		});
	}

	protected void jumpActivity() {
		// TODO Auto-generated method stub
		dialog = new ResDialog(ThinkTankCertificationActivity.this, R.style.MyDialog, "跳转中···",
				R.drawable.loads);
		dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringjumpzhikurenzhen);

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

	private void init() {
		// TODO Auto-generated method stub
		ibThinkTankCertificationbacking = (ImageButton) findViewById(R.id.ibThinkTankCertificationbacking);
		btnThink_tank = (Button) findViewById(R.id.btnThink_tank);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.think_tank_certification, menu);
		return true;
	}

}
