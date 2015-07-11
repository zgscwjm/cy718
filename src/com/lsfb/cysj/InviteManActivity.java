package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.CheckboxAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

public class InviteManActivity extends FragmentActivity implements
		OnClickListener {
	private static String[] nums = new String[] { "223", "224", "225", "226",
			"222", "221", "220", "227", "228", "223", "224", "225", "226" };
	@ViewInject(R.id.inviteman_back)
	private LinearLayout back;
	@ViewInject(R.id.inviteman_list)
	private ListView list;
	@ViewInject(R.id.inviteman_text)
	private TextView text;
	@ViewInject(R.id.inviteman_finish)
	private LinearLayout finish;
	@ViewInject(R.id.inviteman_search)
	private EditText search;
	@ViewInject(R.id.inviteman_man)
	private TextView inviteman_man;
	CheckboxAdapter adapter;
	Intent intent;
	String sid;// 上个页面传过来的比赛id
	String cla;// 邀请人员
	/**
	 * uid 会员id nickname 会员昵称 image 会员头像 index 会员指数
	 */
	String uid, nickname, image, index;
	AsyncHttpClient client;
	RequestParams params;
	// Map<String, Object> map;
	// List<Map<String, Object>> listmap;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	HashMap<String, Object> maps;// 选择的人
	Dialog jiazaidialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inviteman);
		// 默认不弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ViewUtils.inject(this);
		init();
		data();
		search.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId ==EditorInfo.IME_ACTION_SEARCH){
					// 先隐藏键盘
					((InputMethodManager)(InputMethodManager) InviteManActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
					InviteManActivity.this
					.getCurrentFocus()
					.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
				String name = search.getText().toString().trim();
				if (name.equals("")) {
					listmap = new ArrayList<HashMap<String,Object>>();
					data();
				} else {
					listmap = new ArrayList<HashMap<String,Object>>();
					searchename(name);
				}
				}
				return false;
			}
				
		});
	}

	private void data() {
		showdialogup();
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", sid);
		params.put("uid", IsTrue.userId);
		params.put("cla", cla);
		client.post(MyUrl.invmem, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					if (num.equals("1")) {
						text.setVisibility(View.VISIBLE);
					} else if (num.equals("2")) {
						String lists = response.getString("list");
						text.setVisibility(View.GONE);
						JSONArray array = new JSONArray(lists);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("uid", object.getString("uid").toString());
							map.put("nickname", object.getString("nickname")
									.toString());
							map.put("image", object.getString("image")
									.toString());
							map.put("index", object.getString("index")
									.toString());
							listmap.add(map);
						}
						// xianshidata();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter = new CheckboxAdapter(getApplicationContext(),
						listmap);
				list.setAdapter(adapter);
				jiazaidialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(InviteManActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				jiazaidialog.dismiss();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	static class ViewHolder {
		ImageView headimg;
		TextView name;
		TextView num;
		CheckBox checkBox;
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		maps = new HashMap<String, Object>();
		// listmap = new ArrayList<Map<String, Object>>();
		intent = getIntent();
		sid = intent.getExtras().getString("sid");
		cla = intent.getExtras().getString("cla");
		if (cla.equals("2")) {
			inviteman_man.setText("邀请专家");
		}
		back.setOnClickListener(this);
		finish.setOnClickListener(this);
	}
	protected void searchename(String name) {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", sid);
		params.put("uid", IsTrue.userId);
		params.put("cla", cla);
		params.put("name", name);
		client.post(MyUrl.invmem, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response + "这是什么");
				try {
					String num = response.getString("num");
					if (num.equals("1")) {
						text.setVisibility(View.VISIBLE);
					} else if (num.equals("2")) {
						text.setVisibility(View.GONE);
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("uid", object.getString("uid").toString());
							map.put("nickname", object.getString("nickname")
									.toString());
							map.put("image", object.getString("image")
									.toString());
							map.put("index", object.getString("index")
									.toString());
							listmap.add(map);
						}
						// xianshidata();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter = new CheckboxAdapter(getApplicationContext(),
						listmap);
				System.out.println(listmap.size()+"SSSSSSSSZZZZZZZZZZZZ");
				list.setAdapter(adapter);
				jiazaidialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(InviteManActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				jiazaidialog.dismiss();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.inviteman_back:
			finish();
			break;
		case R.id.inviteman_finish:// 完成
			listmap = new ArrayList<HashMap<String, Object>>();
			String manids = "";
			HashMap<Integer, Boolean> state = adapter.state;
			String options = "选择的项是:" + state;
			for (int j = 0; j < adapter.getCount(); j++) {
				if (state.get(j) != null) {
					maps = (HashMap<String, Object>) adapter.getItem(j);
					String username = maps.get("nickname").toString();
					String id = maps.get("uid").toString();
					options += "\n" + id + "." + username;
					listmap.add(maps);
				}
			}
			// 显示选择内容
			// Toast.makeText(getApplicationContext(), options,
			// Toast.LENGTH_LONG).show();
			if (listmap.size() == 0) {
				return;
			}
			for (int j = 0; j < listmap.size(); j++) {
				String manid = listmap.get(j).get("uid").toString();
				if (j + 1 == listmap.size()) {
					manids += manid;
				} else {
					manids += manid + ",";
				}
			}
			System.out.println(manids + "JJJJJJ" + listmap.size());
			yaoqing(manids);
			break;
		default:
			break;
		}
	}

	private void yaoqing(String manids) {
		showdialogup();
		client = new AsyncHttpClient();
		params = new RequestParams();
		client.post(MyUrl.invmempost, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					if (num.equals("2")) {
						Toast.makeText(getApplicationContext(), "邀请成功",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(), errorResponse + "",
						Toast.LENGTH_LONG).show();
				jiazaidialog.dismiss();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
}
