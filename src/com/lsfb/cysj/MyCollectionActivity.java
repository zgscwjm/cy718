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

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

//我的收藏

public class MyCollectionActivity extends Activity implements
		IXListViewListener {
	ImageButton ibMyCollectionbacking;// 返回
	XListView listviewMyCollection;
	BaseAdapter baseAdapter;
	private EditText search;
	// ResDialog dialog;
	HttpClient httpClient;
	int count = 0;
	int removeNum=0;
	BitmapUtils bitmapUtils;
	String lists;// 个人收藏返回的数据
	SwipeMenuCreator creator;
	/**
	 * id:收藏id title:收藏题目 content:收藏介绍 source:资源来源(1创意作品|2比赛作品) sid:资源id
	 * image:收藏图片(0没有图片) url:链接(0没有链接) vedio:视频(0没有视频) voice:语音(0没有语音)
	 */
	String id, title, content, source, sid, image, url, vedio, voice;
	Map<String, Object> map;
	List<Map<String, Object>> listmap;
	Handler handlerdelete = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// dialog.dismiss();
				String str = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.getString("num")
							.toString())) {
					case 1:
						Toast.makeText(MyCollectionActivity.this, "删除失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(MyCollectionActivity.this, "删除成功",
								Toast.LENGTH_SHORT).show();
						listmap.remove(removeNum);
						baseAdapter.notifyDataSetChanged();
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
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// dialog.dismiss();
				String str = msg.obj.toString();

				Log.d("2222222222222222222222222", str);
				try {
					JSONObject response = new JSONObject(str);
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(MyCollectionActivity.this, "您没有收藏任何信息",
								Toast.LENGTH_SHORT).show();
					} else if (i == 2) {
						lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						listmap = new ArrayList<Map<String, Object>>();
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							map = new HashMap<String, Object>();
							map.put("id", object.getString("id").toString());
							map.put("title", object.getString("title")
									.toString());
							map.put("content", object.getString("content")
									.toString());
							map.put("source", object.getString("source")
									.toString());
							map.put("sid", object.getString("sid").toString());
							map.put("image", object.getString("image")
									.toString());
							map.put("url", object.getString("url").toString());
							map.put("vedio", object.getString("vedio")
									.toString());
							map.put("voice", object.getString("voice")
									.toString());
							listmap.add(map);
						}
					}
					baseAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
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

				Log.d("2222222222222222222222222", str);
				try {
					JSONObject response = new JSONObject(str);
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(MyCollectionActivity.this, "亲，没有收藏信息了",
								Toast.LENGTH_SHORT).show();
					} else if (i == 2) {
						lists = response.getString("list");
						JSONArray array = new JSONArray(lists);

						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							map = new HashMap<String, Object>();
							map.put("id", object.getString("id").toString());
							map.put("title", object.getString("title")
									.toString());
							map.put("content", object.getString("content")
									.toString());
							map.put("source", object.getString("source")
									.toString());
							map.put("sid", object.getString("sid").toString());
							map.put("image", object.getString("image")
									.toString());
							map.put("url", object.getString("url").toString());
							map.put("vedio", object.getString("vedio")
									.toString());
							map.put("voice", object.getString("voice")
									.toString());
							listmap.add(map);
						}
					}
					baseAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init();
		data();
		ibMyCollectionbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		listviewMyCollection
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(final int position,
							SwipeMenu menu, int index) {
						switch (index) {

						case 0:
							delete(position);

							break;
						}
						// false : close the menu; true : not close the menu
						return false;
					}
				});
	}

	protected void delete(final int i) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.memscdelete);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					removeNum=i;
					params.add(new BasicNameValuePair("id", listmap.get(i).get("id")
							.toString() + ""));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlerdelete.sendMessage(msg);

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

	private void data() {
		initdata();
		System.out.println(listmap + "SSSSSSSSSSSSSSSSSSSSSS");
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(MyCollectionActivity.this).inflate(
							R.layout.mycollection_listview, null);
					holder.imageview = (ImageView) v
							.findViewById(R.id.ivMyCollection);
					holder.tvHead = (TextView) v
							.findViewById(R.id.tv_MycollectionHead);
					holder.tvContext = (TextView) v
							.findViewById(R.id.tv_MycollectionContext);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				bitmapUtils
						.display(holder.imageview, ImageAddress.Stringchuangyi
								+ listmap.get(position).get("image").toString());
				holder.tvHead.setText(listmap.get(position).get("title")
						.toString());
				holder.tvContext.setText(listmap.get(position).get("content")
						.toString());
				return v;
			}

			@Override
			public long getItemId(int i) {
				return 0;
			}

			@Override
			public Object getItem(int i) {
				return null;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem changeItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				changeItem.setBackground(R.color.red);
				// set item width
				changeItem.setWidth(dp2px(90));
				// set a icon
				// changeItem.setIcon(R.drawable.change);
				changeItem.setTitle("删除");
				changeItem.setTitleColor(Color.WHITE);
				changeItem.setTitleSize(18);
				// add to menu
				menu.addMenuItem(changeItem);
				// // create "delete" item
				// SwipeMenuItem deleteItem = new SwipeMenuItem(
				// getApplicationContext());
				// // set item background
				// deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
				// 0x3F, 0x25)));
				// // set item width
				// deleteItem.setWidth(dp2px(90));
				// // set a icon
				// deleteItem.setIcon(R.drawable.headimage);
				//
				// // add to menu
				// menu.addMenuItem(deleteItem);
			}

			private int dp2px(int dp) {
				// TODO d-generated method stub

				return (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
								.getDisplayMetrics());

			}
		};
		listviewMyCollection.setMenuCreator(creator);
		listviewMyCollection.setAdapter(baseAdapter);

	}

	private void initdata() {
		// dialog = new ResDialog(MyCollectionActivity.this, R.style.MyDialog,
		// "努力加载中", R.drawable.loads);
		// dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.memsc);

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

	public class ViewHolder {
		private ImageView imageview;
		private TextView tvHead;
		private TextView tvContext;

	}

	private void init() {
		bitmapUtils = new BitmapUtils(MyCollectionActivity.this);
		map = new HashMap<String, Object>();
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		listmap = new ArrayList<Map<String, Object>>();
		ibMyCollectionbacking = (ImageButton) findViewById(R.id.ibMyCollectionbacking);
		listviewMyCollection = (XListView) findViewById(R.id.listviewMyCollection);
		search = (EditText) findViewById(R.id.activity_my_collection_search);
		listviewMyCollection.setPullLoadEnable(true);
		listviewMyCollection.setXListViewListener(this);
	}

	private void onLoad() {
		listviewMyCollection.stopRefresh();
		listviewMyCollection.stopLoadMore();
		listviewMyCollection.setRefreshTime("刚刚");
	}

	private void loadRemnantListItem() {
		// 滚到加载余下的数据
		// 动态的改变listAdapter.getCount()的返回值
		// 使用Handler调用listAdapter.notifyDataSetChanged();更新数据

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.memscs);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("page", (++count) + ""));
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
				initdata();
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
