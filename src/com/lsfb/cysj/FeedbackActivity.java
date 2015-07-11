package com.lsfb.cysj;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.lsfb.cysj.view.ResDialog;

public class FeedbackActivity extends Activity {
	ImageButton ibbacking;
	private EditText msg;
	private Button tijiao;
	HttpUtils httpUtils;
	RequestParams params;
	Dialog jiazaidialog;
	String stringmsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedbakc);
		init();
		ibbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		tijiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				showdialogup();
				stringmsg = msg.getText().toString().trim();
				if (stringmsg.equals("")) {
					Toast.makeText(getApplicationContext(), "请填写内容",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					httpUtils = new HttpUtils();
					params = new RequestParams();
					params.addBodyParameter("uid", IsTrue.userId+"");
					params.addBodyParameter("content", stringmsg);
					httpUtils.send(HttpMethod.POST, MyUrl.liu, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException error, String msg) {
//							jiazaidialog.dismiss();
							Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
//							jiazaidialog.dismiss();
							String list = responseInfo.result;
							try {
								JSONObject object = new JSONObject(list);
								String num = object.getString("num").toString();
								if (num.equals("1")) {
									Toast.makeText(getApplicationContext(),"提交失败", Toast.LENGTH_SHORT).show();
								}else if (num.equals("2")) {
									msg.setText("");
								}else if (num.equals("3")) {
									Toast.makeText(getApplicationContext(),"已经留言了", Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
	}
	protected void showdialogup() {
		jiazaidialog = new ResDialog(getApplicationContext(), R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void init() {
		ibbacking = (ImageButton) findViewById(R.id.ibFeedbackbacking);
		msg = (EditText) findViewById(R.id.jianyi_msg);
		tijiao = (Button) findViewById(R.id.btnThink_tank);
	}
}
