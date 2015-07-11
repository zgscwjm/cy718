package com.lsfb.cysj.Dialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsbf.cysj.R.id;
import com.lsfb.cysj.GameWorksActivity;
import com.lsfb.cysj.Dialog.QiFuialog.PriorityListener;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Utils;
import com.lsfb.cysj.view.CenterTextview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddDialog extends Dialog {
	Context context;
	Intent intent;
	String sid;//智库id
	PriorityListener listener;
	private Button btn;
	private EditText name;
	AsyncHttpClient client;
	RequestParams params;

	public AddDialog(Context context) {
		super(context);
		this.context = context;
	}

	public AddDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	/**
	 * 自定义Dialog监听器
	 * 
	 * @author Kael.Chen
	 * 
	 */
	public interface PriorityListener {
		/**
		 * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
		 */
		public void refreshPriorityUI(String string);
	}

	/**
	 * 带监听器参数的构造函数
	 */
	public AddDialog(Context context, int theme, PriorityListener listener,String sid) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
		this.sid = sid;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.add_dialog);
		setCanceledOnTouchOutside(true);
		init();
		data();
	}
	
	private void data() {
		btn.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.isFastDoubleClick()) {  
			        return;  
			    }  
				String qian = name.getText().toString().trim();
				if (qian.equals("")) {
					return;
				}
				client = new AsyncHttpClient();
				params = new RequestParams();
				params.put("sid", sid);
				params.put("uid", IsTrue.userId);
				params.put("money", qian);
				client.post(MyUrl.thzuadd, params, new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						System.out.println(response+"IIIIIIIIII");
						try {
							String num = response.getString("num");
							if (num.equals("1")) {
								Toast.makeText(context, "添加失败",
										Toast.LENGTH_SHORT).show();
							}else if (num.equals("2")) {
								Toast.makeText(context, "添加成功",
										Toast.LENGTH_SHORT).show();
								dismiss();
							}else if (num.equals("3")) {
								Toast.makeText(context, "已经存在该组名",
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
						Toast.makeText(context, errorResponse + "",
								Toast.LENGTH_SHORT).show();
						super.onFailure(statusCode, headers, throwable, errorResponse);
					}
				});
			}
		});
	}

	private void init() {
		name = (EditText) findViewById(R.id.add_dialog_name);
		btn = (Button) findViewById(R.id.add_dialog_queding);
	}
}
