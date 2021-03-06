package com.lsfb.cysj.fragment;

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

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.GalleryImageActivity;
import com.lsfb.cysj.GameWorksActivity;
import com.lsfb.cysj.GuanZhuMan;
import com.lsfb.cysj.HotIdeasGamesContent2Activity;
import com.lsfb.cysj.HotIdeasGamesContentActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.TicketNoUseFragment.ViewHolder;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsGames extends Fragment implements IXListViewListener {
	private View rootView;
	XListView friends_games_list;
	BaseAdapter baseAdapter;
	HttpClient httpClient;
	List<Map<String, Object>> list;
	int count = 0;
	BitmapUtils bitmapUtils;
	Intent intent;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {

				String str = msg.obj.toString();
				// 有值返回一维数组
				// num:2
				// list:二维数组
				// title:比赛题目
				// id:比赛id
				// image:比赛封面图
				// introduce:比赛介绍
				// number:比赛人数
				// maxnumber:比赛总人数
				// 无值返回一维数组
				// num:1
				// 注:图片地址
				// http://cysj.coolmoban.com/Public/images/cbit/

				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
//						Toast.makeText(getActivity(), "没有关注任何比赛",
//								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						list = new ArrayList<Map<String, Object>>();
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("id", temp.getString("id"));
							map.put("image", temp.getString("image"));
							map.put("introduce", temp.getString("introduce"));
							map.put("title", temp.getString("title"));
							map.put("number", temp.getString("number"));
							map.put("maxnumber", temp.getString("maxnumber"));
							map.put("class", temp.getString("class"));

							list.add(map);

						}
						break;

					default:
						break;
					}
					friends_games_list.setAdapter(baseAdapter);
					baseAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
				// 返回一维数组
				// num:2
				// list:二维数组
				// id:入场券id
				// ewm:二维码图片
				// state:入场券状态(0未使用|1已使用|2已过期)
				// title:大赛标题
				// address:大赛地址
				// time:比赛时间
				// price:参赛金额
				// 无值返回
				// num:1
				// 注:图片地址
				// http://cysj.coolmoban.com/Public/

				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(getActivity(), "亲，下面没有关注的比赛了",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());

						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("id", temp.getString("id"));
							map.put("image", temp.getString("image"));
							map.put("introduce", temp.getString("introduce"));
							map.put("title", temp.getString("title"));
							map.put("number", temp.getString("number"));
							map.put("maxnumber", temp.getString("maxnumber"));
							map.put("class", temp.getString("class"));
							list.add(map);

						}
						break;

					default:
						break;
					}

					baseAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.friends_games, container,
					false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();
		count = 0;
		chushihua();
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(final int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(getActivity()).inflate(
							R.layout.friends_games_list_item, null);
					holder.imageview = (ImageView) v
							.findViewById(R.id.friends_games_list_item_img);
					holder.title = (TextView) v
							.findViewById(R.id.friends_games_list_item_title);
					holder.text = (TextView) v
							.findViewById(R.id.friends_games_list_item_text);
					holder.num = (TextView) v
							.findViewById(R.id.friends_games_list_item_num);
					holder.num2 = (TextView) v
							.findViewById(R.id.friends_games_list_item_num2);
					
					holder.classfy = (TextView) v
							.findViewById(R.id.hot_ideas_games_item_class);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				bitmapUtils.display(holder.imageview, ImageAddress.cbit
						+ list.get(position).get("image").toString());

				holder.title
						.setText(list.get(position).get("title").toString());
				holder.text.setText(list.get(position).get("introduce")
						.toString());
				holder.num.setText(list.get(position).get("number").toString());
				holder.num2.setText(list.get(position).get("maxnumber")
						.toString());
				
				
				holder.num
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(getActivity(),GuanZhuMan.class);
						startActivity(intent);	 
					}
				});

				holder.num2
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//参赛作品
						Intent intent=new Intent(getActivity(),GameWorksActivity.class);
						intent.putExtra("sid", list.get(position).get("id")
						.toString());
						startActivity(intent);
					}
				});

				
				// 成员,作品详情进入

				String classfy = list.get(position).get("class")
						.toString();
				if ("1".equals(classfy)) {
					holder.classfy.setText("投资项目创意");
				} else if ("2".equals(classfy)) {
					holder.classfy.setText("任务人才创意");
				} else {
					holder.classfy.setText("公益明星创意");
				}
				
				
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
							intent = new Intent(getActivity(),HotIdeasGamesContentActivity.class);
							intent.putExtra("sid", list.get(position).get("id").toString());
							startActivity(intent);
					}
				});
				return v;
			}

			@Override
			public long getItemId(int i) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int i) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};

		return rootView;

	}

	private void chushihua() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringLookGame);

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
		private TextView title;
		private TextView text;
		private TextView num;
		private TextView num2;
		private TextView classfy;//
	}

	private void init() {
		// TODO Auto-generated method stub
		friends_games_list = (XListView) rootView.findViewById(R.id.friends_games_list);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		bitmapUtils = new BitmapUtils(getActivity());
		friends_games_list.setPullLoadEnable(true);
		friends_games_list.setXListViewListener(this);
	}

	private void onLoad() {
		friends_games_list.stopRefresh();
		friends_games_list.stopLoadMore();
		friends_games_list.setRefreshTime("刚刚");
	}

	private void loadRemnantListItem() {
		// 滚到加载余下的数据
		// 动态的改变listAdapter.getCount()的返回值
		// 使用Handler调用listAdapter.notifyDataSetChanged();更新数据

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringLookGames);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					
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
				chushihua();
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
