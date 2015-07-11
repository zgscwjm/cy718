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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;

public class JuBaoDialog extends Dialog {
	Context context;
	Intent intent;
	String id;
	String jubaonum;
	PriorityListener listener;
	private Spinner spr;
	private EditText text;
	private Button btn;
	AsyncHttpClient client;
	RequestParams params;
	Map<String, Object> map;
	List<Map<String, Object>> listmap;
	ArrayAdapter<String> adapter;
	List<String> listtype;
	List<String> listtype2;
	private String contexttext = null;//举报内容
	String classid = null;//举报类型id
	public JuBaoDialog(Context context) {
		super(context);
		this.context = context;
	}

	public JuBaoDialog(Context context, int theme,String id,String jubaonum) {
		super(context, theme);
		this.context = context;
		this.id = id;
		this.jubaonum = jubaonum;
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
		public void refreshPriorityUI(String string,String string2);
	}

	/**
	 * 带监听器参数的构造函数
	 */
	public JuBaoDialog(Context context, int theme, PriorityListener listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ideas_msg_jubao);
		setCanceledOnTouchOutside(true);
		init();
		data();
	}

	private void data() {
		client = new AsyncHttpClient();
		client.post(MyUrl.reportlcass, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response+"JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");
				try {
					String num = response.getString("num");
					Integer i = Integer.parseInt(num);
					System.out.println(i+"GHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
					if (i==1) {
						return;
					}else if(i==2){
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							map = new HashMap<String, Object>();
							map.put("name", object.getString("report_class_name"));
							map.put("id", object.getString("report_class_id"));
							listmap.add(map);
						}
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							listtype = new ArrayList<String>();
							listtype.add(object.getString("report_class_name"));
							listtype2.addAll(listtype);
						}
						adapter = new ArrayAdapter<String>(getContext(), R.layout.jubao_spinner_item,R.id.ideas_msg_jubao_spr_text,listtype2);
						spr.setAdapter(adapter);
						spr.setOnItemSelectedListener(new OnItemSelectedListener() {

							private String typename = null;

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								typename = listtype2.get(arg2);
								classid = listmap.get(arg2).get("id").toString();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getContext(), "请求错误"+errorResponse, Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		btn.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn.setClickable(false);
				contexttext = text.getText().toString().trim();
				if (contexttext.equals("")) {
					Toast.makeText(getContext(), "内容未完整", Toast.LENGTH_SHORT).show();
					return;
				}
				upjubao(jubaonum);
			}
		});
	}

	protected void upjubao(String string) {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("content", contexttext);//举报内容
		params.put("classid", classid);//举报类型id
		params.put("uid", IsTrue.userId);//用户id
		params.put("source", string);//source:1(创意作品)|2(比赛作品)|3(会员)|4(比赛)
		params.put("sid", id);//举报作品id
		System.out.println("举报内容"+contexttext+"举报类型id"+classid+"用户id"+IsTrue.userId+"举报作品id"+id);
		client.post(MyUrl.reportadd, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					Integer i = Integer.parseInt(num);
					if (i==1) {
						Toast.makeText(getContext(), "举报失败", Toast.LENGTH_SHORT).show();
						btn.setClickable(true);
					}else if (i==2) {
						Toast.makeText(getContext(), "举报成功", Toast.LENGTH_SHORT).show();
						btn.setClickable(true);
						dismiss();
					}
					else if (i==3) {
						Toast.makeText(getContext(), "举报信息相同", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getContext(), errorResponse.toString(), Toast.LENGTH_SHORT).show();
				btn.setClickable(true);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void init() {
		listtype2 = new ArrayList<String>();
		listtype = new ArrayList<String>();
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String,Object>>();
		spr = (Spinner) findViewById(R.id.ideas_msg_jubao_spr);
		text = (EditText) findViewById(R.id.ideas_msg_jubao_text);
		btn = (Button) findViewById(R.id.ideas_msg_jubao_btn);
	}
}
