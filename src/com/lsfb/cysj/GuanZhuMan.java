package com.lsfb.cysj;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ListViewForScrollView;

public class GuanZhuMan extends FragmentActivity implements OnClickListener {

	@ViewInject(R.id.guanzhu_man_back)
	private LinearLayout back;
	/**
	 * guanzhu_man_search search 请输入名字或创创号快速查找
	 */
	@ViewInject(R.id.guanzhu_man_search)
	private EditText search;
	@ViewInject(R.id.guanzhu_man_scrollview)
	private ScrollView scrollView;
	/**
	 * chairman guanzhu_man_chairman 发起者
	 */
	@ViewInject(R.id.guanzhu_man_chairman)
	private ListViewForScrollView chairman;
	/**
	 * guanzhu_man_director director 专家
	 */
	@ViewInject(R.id.guanzhu_man_director)
	private ListViewForScrollView director;
	/**
	 * guanzhu_man_counselor man 成员
	 */
	@ViewInject(R.id.guanzhu_man_counselor)
	private ListViewForScrollView man;
	BaseAdapter fqradapter, zjadapter, cyadapter;
	String strgameId;
	HttpClient httpClient;
	List<Map<String, Object>> fqrlist;
	List<Map<String, Object>> zjlist;
	List<Map<String, Object>> cylist;
	BitmapUtils bitmapUtils;
	EditText guanzhu_man_search;// 搜索
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// dialog.dismiss();
				String str = msg.obj.toString();
				System.err.println(str.toString());
				// Json:
				// 有值返回一维数组
				// zj:1(有专家)|0(没有专家)
				// zjlist:一维数组
				// memimage:会员头像
				// memnickname:会员昵称
				// memindex:会员指数
				// memid:会员id
				// cy:1(有成员)|0(没有成员)
				// cylist:一维数组
				// memimage:会员头像
				// memnickname:会员昵称
				// memindex:会员指数
				// memid:会员id
				// fqrlist:一维数组(发起人)
				// memimage:会员头像
				// memnickname:会员昵称
				// memindex:会员指数
				// memid:会员id
				try {
					JSONObject jsonObject = new JSONObject(str);
					JSONArray jsonArrayfqr = new JSONArray(jsonObject
							.getString("fqrlist").toString());
					for (int i = 0; i < jsonArrayfqr.length(); i++) {
						JSONObject tempfqr = (JSONObject) jsonArrayfqr.get(i);
						Map<String, Object> mapfqr = new HashMap<String, Object>();
						// mapfqr.put("id", tempfqr.getString("id"));
						mapfqr.put("memimage", tempfqr.getString("memimage"));
						mapfqr.put("memindex", tempfqr.getString("memindex"));
						mapfqr.put("memnickname",
								tempfqr.getString("memnickname"));
						mapfqr.put("memid", tempfqr.getString("memid"));
						fqrlist.add(mapfqr);
					}
					int zjnum = Integer.parseInt(jsonObject.getString("zj")
							.toString());
					switch (zjnum) {
					case 0:

						break;
					case 1:
						JSONArray jsonArrayzj = new JSONArray(jsonObject
								.getString("zjlist").toString());
						for (int j = 0; j < jsonArrayzj.length(); j++) {
							JSONObject tempzj = (JSONObject) jsonArrayzj.get(j);
							Map<String, Object> mapzj = new HashMap<String, Object>();
							// mapzj.put("id", tempzj.getString("id"));
							mapzj.put("memimage", tempzj.getString("memimage"));
							mapzj.put("memindex", tempzj.getString("memindex"));
							mapzj.put("memnickname",
									tempzj.getString("memnickname"));
							mapzj.put("memid", tempzj.getString("memid"));
							zjlist.add(mapzj);
						}
						break;
					default:
						break;
					}
					int cynum = Integer.parseInt(jsonObject.getString("cy")
							.toString());
					switch (cynum) {
					case 0:

						break;
					case 1:
						JSONArray jsonArraycy = new JSONArray(jsonObject
								.getString("cylist").toString());
						for (int k = 0; k < jsonArraycy.length(); k++) {
							JSONObject tempcy = (JSONObject) jsonArraycy.get(k);
							Map<String, Object> mapcy = new HashMap<String, Object>();
							// mapcy.put("id", tempcy.getString("id"));
							mapcy.put("memimage", tempcy.getString("memimage"));
							mapcy.put("memindex", tempcy.getString("memindex"));
							mapcy.put("memid", tempcy.getString("memid"));
							mapcy.put("memnickname",
									tempcy.getString("memnickname"));
							cylist.add(mapcy);
						}
						break;
					default:
						break;
					}
					fqradapter.notifyDataSetChanged();
					cyadapter.notifyDataSetChanged();
					zjadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	Handler handlersearch = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// dialog.dismiss();
				String str = msg.obj.toString();
				System.err.println(str.toString());
//				zj:1(有专家)|0(没有专家)
//				zjlist:一维数组
//				        memimage:会员头像
//				        memnickname:会员昵称
//				        memindex:会员指数
//				        memid:会员id
//				cy:1(有成员)|0(没有成员)
//				cylist:一维数组
//				        memimage:会员头像
//				        memnickname:会员昵称
//				        memindex:会员指数
//				        memid:会员id

				try {
					JSONObject jsonObject = new JSONObject(str);
					zjlist = new ArrayList<Map<String, Object>>();
					cylist = new ArrayList<Map<String, Object>>();
					int zjnum = Integer.parseInt(jsonObject.getString("zj")
							.toString());
					switch (zjnum) {
					case 0:

						break;
					case 1:
						JSONArray jsonArrayzj = new JSONArray(jsonObject
								.getString("zjlist").toString());
						for (int j = 0; j < jsonArrayzj.length(); j++) {
							JSONObject tempzj = (JSONObject) jsonArrayzj.get(j);
							Map<String, Object> mapzj = new HashMap<String, Object>();
							// mapzj.put("id", tempzj.getString("id"));
							mapzj.put("memimage", tempzj.getString("memimage"));
							mapzj.put("memindex", tempzj.getString("memindex"));
							mapzj.put("memnickname",
									tempzj.getString("memnickname"));
							mapzj.put("memid", tempzj.getString("memid"));
							zjlist.add(mapzj);
						}
						break;
					default:
						break;
					}
					int cynum = Integer.parseInt(jsonObject.getString("cy")
							.toString());
					switch (cynum) {
					case 0:

						break;
					case 1:
						JSONArray jsonArraycy = new JSONArray(jsonObject
								.getString("cylist").toString());
						for (int k = 0; k < jsonArraycy.length(); k++) {
							JSONObject tempcy = (JSONObject) jsonArraycy.get(k);
							Map<String, Object> mapcy = new HashMap<String, Object>();
							// mapcy.put("id", tempcy.getString("id"));
							mapcy.put("memimage", tempcy.getString("memimage"));
							mapcy.put("memindex", tempcy.getString("memindex"));
							mapcy.put("memid", tempcy.getString("memid"));
							mapcy.put("memnickname",
									tempcy.getString("memnickname"));
							cylist.add(mapcy);
						}
						break;
					default:
						break;
					}
					
					cyadapter.notifyDataSetChanged();
					zjadapter.notifyDataSetChanged();
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
		setContentView(R.layout.guanzhu_man);
		// 默认不弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ViewUtils.inject(this);
		init();
		Intent intent = getIntent();
		strgameId = intent.getStringExtra("sid");
		chairman();
		director();
		man();
		chushihua();
		guanzhu_man_search.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
				// TODO Auto-generated method stub
				/** 隐藏软键盘 **/
				if (keyCode == KeyEvent.KEYCODE_ENTER) {// 修改回车键功能
					View view = GuanZhuMan.this.getWindow().peekDecorView();
					if (view != null) {
						InputMethodManager inputmanger = (InputMethodManager) GuanZhuMan.this
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(
								view.getWindowToken(), 0);
					}
					search();

				}
				return false;
			}
		});
	}

	protected void search() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringguanzhusearch);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("sid", strgameId));
					params.add(new BasicNameValuePair("name",
							guanzhu_man_search.getText().toString().trim()));
					System.err.println("SID是：" + strgameId);
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlersearch.sendMessage(msg);

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

	private void chushihua() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringguanzhu);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("sid", strgameId));
					System.err.println("SID是：" + strgameId);
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

	// 成员
	private void man() {
		cyadapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.guanzhu_man_item, null);
				ImageView img = (ImageView) view
						.findViewById(R.id.guanzhu_man_item_img);
				TextView name = (TextView) view
						.findViewById(R.id.guanzhu_man_item_name);
				TextView text = (TextView) view
						.findViewById(R.id.guanzhu_man_item_text);
				bitmapUtils.display(
						img,
						ImageAddress.Stringhead
								+ cylist.get(position).get("memimage")
										.toString());
				name.setText(cylist.get(position).get("memnickname").toString());
				text.setText(cylist.get(position).get("memindex").toString());
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return cylist.size();
			}
		};
		man.setAdapter(cyadapter);
	}

	// 专家
	private void director() {
		zjadapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.guanzhu_man_item, null);
				ImageView img = (ImageView) view
						.findViewById(R.id.guanzhu_man_item_img);
				TextView name = (TextView) view
						.findViewById(R.id.guanzhu_man_item_name);
				TextView text = (TextView) view
						.findViewById(R.id.guanzhu_man_item_text);
				bitmapUtils.display(
						img,
						ImageAddress.Stringhead
								+ zjlist.get(position).get("memimage")
										.toString());
				name.setText(zjlist.get(position).get("memnickname").toString());
				text.setText(zjlist.get(position).get("memindex").toString());
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return zjlist.size();
			}
		};
		director.setAdapter(zjadapter);
	}

	// 发起者
	private void chairman() {
		fqradapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.guanzhu_man_item, null);
				ImageView img = (ImageView) view
						.findViewById(R.id.guanzhu_man_item_img);
				TextView name = (TextView) view
						.findViewById(R.id.guanzhu_man_item_name);
				TextView text = (TextView) view
						.findViewById(R.id.guanzhu_man_item_text);
				bitmapUtils.display(
						img,
						ImageAddress.Stringhead
								+ fqrlist.get(position).get("memimage")
										.toString());
				name.setText(fqrlist.get(position).get("memnickname")
						.toString());
				text.setText(fqrlist.get(position).get("memindex").toString());
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return fqrlist.size();
			}
		};
		chairman.setAdapter(fqradapter);
	}

	private void init() {
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		fqrlist = new ArrayList<Map<String, Object>>();
		zjlist = new ArrayList<Map<String, Object>>();
		cylist = new ArrayList<Map<String, Object>>();
		guanzhu_man_search = (EditText) findViewById(R.id.guanzhu_man_search);
		bitmapUtils = new BitmapUtils(GuanZhuMan.this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guanzhu_man_back:
			GuanZhuMan.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		scrollView.smoothScrollBy(0, 0);// 不让listview加载在最上面
		super.onResume();
	}

}
