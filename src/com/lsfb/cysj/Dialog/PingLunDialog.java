package com.lsfb.cysj.Dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Utils;

public class PingLunDialog extends Dialog {
	Context context;
	Intent intent;
	String id;//当前创意id 
	String source;//source: 1(创意作品) |2(比赛作品) |3(创优圈)
	String content;//输入的内容
	PriorityListener listener;
	AsyncHttpClient client;
	RequestParams params;
	private EditText text;
	private Button btn;
	Map<String, Object> map;
	List<Map<String, Object>> listmap;

	public PingLunDialog(Context context) {
		super(context);
		this.context = context;
	}

	public PingLunDialog(Context context, int theme, String id , String source) {
		super(context, theme);
		this.context = context;
		this.id = id;
		this.source = source;
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
		public void refreshPriorityUI(List<Map<String, Object>> listmap);
	}

	/**
	 * 带监听器参数的构造函数
	 * @param source 
	 * @param id 
	 */
	public PingLunDialog(Context context, int theme, String id, String source, PriorityListener listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
		this.id = id;
		this.source = source;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ideas_msg_pinglun);
		setCanceledOnTouchOutside(true);
		init();
		data();
	}

	private void data() {
		btn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				content = text.getText().toString().trim();
				if (content.equals("")) {
					Toast.makeText(getContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (id.trim().equals("") || source.trim().equals("")) {
					Toast.makeText(getContext(), "出错了", Toast.LENGTH_SHORT).show();
					return;
				}
				if (Utils.isFastDoubleClick()) {  
			        return;  
			    }  
				tijiaomsg();
			}
		});
//		cacle.setOnClickListener(new android.view.View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dismiss();
//			}
//		});
	}

	protected void tijiaomsg() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", id);
		params.put("source", source);
		if (IsTrue.userId == 0) {
			return;
		}
		params.put("uid", IsTrue.userId);
		params.put("content", content);
		client.post(MyUrl.origacommentadd, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i==1) {
						Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
					}else if (i==2) {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							map = new HashMap<String, Object>();
							map.put("rtime", object.getString("rtime").toString());
							map.put("wid", object.getString("wid").toString());
							map.put("content", object.getString("content").toString());
							map.put("mid", object.getString("mid").toString());
							map.put("image", object.getString("image").toString());
							map.put("nickname", object.getString("nickname").toString());
							map.put("zhishu", object.getString("zhishu").toString());
							map.put("mdel", object.getString("mdel").toString());
							listmap.add(map);
						}
						listener.refreshPriorityUI(listmap);
						dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getContext(), errorResponse+"", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String,Object>>();
		text = (EditText) findViewById(R.id.ideas_msg_pinglun_text);
		btn = (Button) findViewById(R.id.ideas_msg_pinglun_btn);
//		cacle = (Button) findViewById(R.id.ideas_msg_pinglun_cacle);
	}
}
