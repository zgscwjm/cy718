package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.TipToupiaoDialog;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 创意世界优秀遗产
 * @author admin
 *
 */
public class WorldHeritageActivity extends FragmentActivity implements
		OnClickListener, IXListViewListener {
	private static String[] nums = new String[] { "255", "266", "277", "299",
			"558", "444", "555", "266", "277", "299", "558", "444", "555" };
	BaseAdapter adapter;
	Intent intent;
	/**
	 * world_heritage_back 返回主界面
	 */
	@ViewInject(R.id.world_heritage_back)
	private LinearLayout world_heritage_back;
	private XListView list;
	@ViewInject(R.id.world_heritage_tijiaoyichan)
	private LinearLayout yichan;
	@ViewInject(R.id.world_heritage_text)
	private TextView text;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	int count = 0;
	AsyncHttpClient client;
	RequestParams params;
	Dialog jiazaidialog;
	TipToupiaoDialog toupiaoDialog;
	Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.world_heritage);
		ViewUtils.inject(this);
		con=WorldHeritageActivity.this;
		init();
		adapter();
		date();

	}

	private void adapter() {
		adapter = new BaseAdapter() {

			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.world_heritage_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.world_heritage_list_item_img);
					holder.head = (TextView) view
							.findViewById(R.id.world_heritage_list_item_head);
					holder.text = (TextView) view
							.findViewById(R.id.world_heritage_list_item_text);
					holder.num = (TextView) view
							.findViewById(R.id.world_heritage_list_item_num);
					holder.btn = (Button) view
							.findViewById(R.id.world_heritage_list_item_btn);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				BitmapUtils bitmapUtils = new BitmapUtils(
						getApplicationContext());
				bitmapUtils
						.display(
								holder.img,
								ImageAddress.yic
										+ listmap.get(position).get("image")
												.toString());
				holder.head.setText(listmap.get(position).get("name")
						.toString());
				holder.text.setText(listmap.get(position).get("introduce")
						.toString());
				holder.num.setText("当前票数："
						+ listmap.get(position).get("nums").toString());
				String btntext = listmap.get(position).get("tp").toString();
				if (IsTrue.userId == 0) {
					holder.btn.setText("不能投票");
				} else {
					if (btntext.equals("1")) {
						holder.btn.setText("免费投票");
						holder.btn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								
							 
								new AlertDialog.Builder(con)
								.setTitle(
										con.getString(R.string.tip_toupiao))
								.setPositiveButton(R.string.yes_toupiao,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												toupiao(listmap.get(position).get("id").toString(),position);
												 
											}
										})
								.setNegativeButton(R.string.no_toupiao, null).show();
//								
							}
						});
					} else if (btntext.equals("2")) {
						holder.btn.setText("创币投票");
						holder.btn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								
								new AlertDialog.Builder(con)
								.setTitle(
										con.getString(R.string.tip_toupiao))
								.setPositiveButton(R.string.yes_toupiao,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												toupiao(listmap.get(position).get("id")
														.toString(),position);
												 
											}
										})
								.setNegativeButton(R.string.no_toupiao, null).show();
								
							}
						});
					} else if (btntext.equals("3")) {
						holder.btn.setText("不能投票");
					}else if (btntext.equals("0")) {
						holder.btn.setText("不能投票");
					}
				}

				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (IsTrue.userId == 0) {
							Toast.makeText(getApplicationContext(), "您未登录",
									Toast.LENGTH_SHORT).show();
						} else {
							intent = new Intent(WorldHeritageActivity.this,
									WorldHeritageItemActivity.class);
							intent.putExtra("id",listmap.get(position).get("id").toString());
							startActivity(intent);
						}
					}
				});
				return view;
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
				return listmap.size();
			}
		};
		list.setAdapter(adapter);
	}

	private void toupiao(String sid,final int i) {
		showdialogup();
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.add("uid", IsTrue.userId + "");
		params.add("sid", sid);
		params.add("cla", 2 + "");
		client.post(MyUrl.ltoup, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response+"WWWW");
				try {
					String num = response.getString("num");
					String tp = response.getString("tp").toString();
					map = listmap.get(i);
					map.put("tp", tp);
					if (num.equals("1")) {
						Toast.makeText(WorldHeritageActivity.this, "投票失败",
								Toast.LENGTH_SHORT).show();
					} else if (num.equals("2")) {
						map = listmap.get(i);
						map.put("nums", Integer.parseInt(listmap.get(i).get("nums").toString())+1);
					} else if (num.equals("3")) {
						Toast.makeText(WorldHeritageActivity.this, "明天再来投吧",
								Toast.LENGTH_SHORT).show();
					} else if (num.equals("4")) {
						Toast.makeText(WorldHeritageActivity.this, "创币不足",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
				adapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(WorldHeritageActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				jiazaidialog.dismiss();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	static class ViewHolder {
		ImageView img;
		TextView head;
		TextView text;
		TextView num;
		Button btn;
	}
	private void date() {
		showdialogup();
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.add("uid", IsTrue.userId + "");
		client.post(MyUrl.llist, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
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
							map.put("image", object.get("image").toString());
							map.put("name", object.get("name").toString());
							map.put("id", object.get("id").toString());
							map.put("introduce", object.get("introduce")
									.toString());
							map.put("nums", object.get("nums").toString());
							map.put("tp", object.get("tp").toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
				adapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(WorldHeritageActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				jiazaidialog.dismiss();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	protected void showdialogup() {
		jiazaidialog = new ResDialog(WorldHeritageActivity.this,
				R.style.MyDialog, "正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		world_heritage_back.setOnClickListener(this);
		yichan.setOnClickListener(this);
		list = (XListView) findViewById(R.id.world_heritage_list);
		list.setPullLoadEnable(true);
		list.setXListViewListener(this);
		toupiaoDialog=new TipToupiaoDialog(getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.world_heritage_back:
			finish();
			break;
		case R.id.world_heritage_tijiaoyichan:// 提交遗产
			if (IsTrue.userId == 0) {
				intent = new Intent(WorldHeritageActivity.this,
						HomeActivity.class);
				IsTrue.yichan = 1;
				startActivity(intent);
				finish();
			} else {
				intent = new Intent(WorldHeritageActivity.this,
						WorldHeritageTiJiaoYiChan.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		count = 0;
		listmap = new ArrayList<HashMap<String, Object>>();
		date();
		adapter.notifyDataSetChanged();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		LoadMoremsg();
		adapter.notifyDataSetChanged();
		onLoad();
	}

	private void LoadMoremsg() {
		showdialogup();
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.add("uid", IsTrue.userId + "");
		params.add("page", ++count + "");
		client.post(MyUrl.llistpage, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response + "@@@@@@@@@@@");
				try {
					String num = response.getString("num");
					if (num.equals("1")) {
					} else if (num.equals("2")) {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("image", object.get("image").toString());
							map.put("name", object.get("name").toString());
							map.put("id", object.get("id").toString());
							map.put("introduce", object.get("introduce")
									.toString());
							map.put("nums", object.get("nums").toString());
							map.put("tp", object.get("tp").toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
				adapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(WorldHeritageActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				jiazaidialog.dismiss();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
}
