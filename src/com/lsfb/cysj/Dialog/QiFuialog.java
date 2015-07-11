package com.lsfb.cysj.Dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.MeditationActivity;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QiFuialog extends Dialog {
	String name;
	Context context;
	private Button close;
	/**
	 * queren 确认钩钩 img1 清除第一个叉叉 img2 清除第二个叉叉
	 */
	private ImageButton queren, img1, img2;
	/**
	 * qifu_name 姓名 text1 内容1 text2 内容2
	 */
	private TextView text1, text2;
	private EditText qifu_name;
	private ListView list;
	public PriorityListener listener;
	BaseAdapter adapter;
	AsyncHttpClient client;
	String addname;
	RequestParams params;
	HashMap<String, Object> map;
	List<HashMap<String, Object>> listmap;
	int length;

	public QiFuialog(Context context) {
		super(context);
		this.context = context;
	}

	public QiFuialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
	/**
     * 自定义Dialog监听器
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
	public QiFuialog(Context context, int theme,PriorityListener listener){
		super(context, theme);
		this.context = context;
		this.listener = listener;
	}
	 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.qifu);
		init();
		data();
		addman();
		close.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		queren.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addname = qifu_name.getText().toString().trim();
				if ("".equals(addname)) {
					Toast.makeText(getContext(), "输入不能为空", Toast.LENGTH_SHORT)
							.show();
				} else {
					send();
					// receive();
				}
			}
		});
	}

	// 添加人数
	private void addman() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId+"");
		client.post(MyUrl.StringMeditation, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							String num = response.getString("num");
							int i = Integer.parseInt(num);
							if (i == 2) {
								String lists = response.getString("list");
								JSONArray array = new JSONArray(lists);
								length = array.length();
								// fid 亲友id fname 亲友名称
								for (int j = 0; j < array.length(); j++) {
									JSONObject object = (JSONObject) array
											.get(j);
									map = new HashMap<String, Object>();
									map.put("fid", object.getString("fid"));
									map.put("fname", object.getString("fname"));
									listmap.add(map);
								}
								adapter.notifyDataSetChanged();
							} else if (i == 1) {
								// Toast.makeText(getContext(), "还未添加",
								// Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT)
								.show();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}
				});
	}

	protected void receive() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", 9);
		client.post(MyUrl.StringMeditation, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT)
								.show();
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
				});
	}

	protected void send() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId+"");
		params.put("name", addname);
		client.post(MyUrl.meritsadd, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
					} else if (i == 2) {
						dismiss();
						IsTrue.qifuchange = true;
						listener.refreshPriorityUI(addname);
					} else if (i == 3) {
						Toast.makeText(getContext(), "添加已满10位",
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
				Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void data() {
		adapter = new BaseAdapter() {

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(getContext()).inflate(
							R.layout.qifu_list_item, null);
					holder = new ViewHolder();
					holder.names = (TextView) convertView
							.findViewById(R.id.qifu_text1);
					holder.imgbtn = (ImageButton) convertView
							.findViewById(R.id.qifu_img1);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.names.setText(listmap.get(position).get("fname")
						.toString());
				holder.imgbtn
						.setOnClickListener(new android.view.View.OnClickListener() {

							@Override
							public void onClick(View v) {
								final String id = listmap.get(position)
										.get("fid").toString();
								client = new AsyncHttpClient();
								params = new RequestParams();
								params.put("uid", IsTrue.userId+"");
								params.put("id", id);
								client.post(MyUrl.meritsdel, params,
										new JsonHttpResponseHandler() {
											@Override
											public void onSuccess(
													int statusCode,
													Header[] headers,
													JSONObject response) {
												try {
													String num = response
															.getString("num");
													int i = Integer
															.parseInt(num);
													if (i == 1) {
													} else if (i == 2) {
														IsTrue.qifuchange = false;
														listener.refreshPriorityUI(position+"");
														dismiss();
													}
												} catch (JSONException e) {
													e.printStackTrace();
												}
												super.onSuccess(statusCode,
														headers, response);
											}

											@Override
											public void onFailure(
													int statusCode,
													Header[] headers,
													Throwable throwable,
													JSONObject errorResponse) {
												Toast.makeText(getContext(),
														"请求错误",
														Toast.LENGTH_SHORT)
														.show();
												super.onFailure(statusCode,
														headers, throwable,
														errorResponse);
											}
										});
							}
						});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return length;
			}
		};
		list.setAdapter(adapter);
	}

	private void init() {
		close = (Button) findViewById(R.id.qifu_btn);
		queren = (ImageButton) findViewById(R.id.qifu_queren);
		qifu_name = (EditText) findViewById(R.id.qifu_name);
		list = (ListView) findViewById(R.id.qifu_list);
		listmap = new ArrayList<HashMap<String, Object>>();
		// text1 = (TextView) findViewById(R.id.qifu_text1);
		// text2 = (TextView) findViewById(R.id.qifu_text2);
		// img1 = (ImageButton) findViewById(R.id.qifu_img1);
		// img2 = (ImageButton) findViewById(R.id.qifu_img2);
	}

	static class ViewHolder {
		TextView names;
		ImageButton imgbtn;
	}
}
