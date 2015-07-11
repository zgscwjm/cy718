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
import com.lsfb.cysj.CreaticeIndexRankingAreaActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.IdeasRankingRegionProvince.ViewHolder;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.annotation.SuppressLint;
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

public class IdeasRankingRegionCity extends Fragment implements
IXListViewListener {
	HttpClient httpClient;
	List<Map<String, Object>> list;
	int count = 0;
	BitmapUtils bitmapUtils;
	private View rootView;
	/**
	 * ideas_ranking_friends_num num 排行
	 */

	private XListView listview;
	BaseAdapter baseAdapter;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// dialog.dismiss();
				String str = msg.obj.toString();
				// Json:
				// 有值返回一维数组
				// num:2
				// list:二维数组
				// uid:好友id
				// nickname:昵称
				// image:好友头像
				// uindex:会员指数
				// ming:排名数字
				// membs:我所在的排名(2我|1是)
				// 无值返回
				// num:1

				list = new ArrayList<Map<String, Object>>();
				System.err.println(str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(getActivity(), "没有任何政府信息",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", temp.getString("id"));
							map.put("name", temp.getString("name"));
							map.put("image", temp.getString("image"));
							map.put("index", temp.getString("index"));
							map.put("ming", temp.getString("ming"));
							map.put("membs", temp.getString("membs"));
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
	Handler handlers = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// dialog.dismiss();
				String str = msg.obj.toString();
				// Json:
				// 有值返回一维数组
				// num:2
				// list:二维数组
				// uid:好友id
				// nickname:昵称
				// image:好友头像
				// uindex:会员指数
				// ming:排名数字
				// membs:我所在的排名(2我|1是)
				// 无值返回
				// num:1

				// list = new ArrayList<Map<String, Object>>();
				System.err.println(str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(getActivity(), "亲，下面没有信息了",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", temp.getString("id"));
							map.put("name", temp.getString("name"));
							map.put("image", temp.getString("image"));
							map.put("index", temp.getString("index"));
							map.put("ming", temp.getString("ming"));
							map.put("membs", temp.getString("membs"));
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.ideas_ranking_region_city,
					container, false);
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
		date();
		chushihua();
		;
		return rootView;

	}

	public void date() {
		baseAdapter = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View v, ViewGroup parent) {
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(getActivity()).inflate(
							R.layout.ideas_ranking_region_city_item, null);
					holder.ivdingwei = (ImageView) v
							.findViewById(R.id.ideas_ranking_region_city_list_item_dingwei);
					holder.tvimage = (ImageView) v
							.findViewById(R.id.ideas_ranking_region_city_list_item_img);
					holder.tvname = (TextView) v
							.findViewById(R.id.ideas_ranking_region_city_list_item_city);
					holder.tvNum = (TextView) v
							.findViewById(R.id.ideas_ranking_region_city_list_item_num);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}

				bitmapUtils.display(holder.tvimage,
						ImageAddress.Stringarea
								+ list.get(position).get("image").toString());
				holder.tvname
						.setText(list.get(position).get("name").toString());
				holder.tvNum
						.setText(list.get(position).get("index").toString());
				if (list.get(position).get("membs").toString().equals("2")) {
					holder.ivdingwei.setBackground(getResources().getDrawable(
							R.drawable.dingwei));
				} else {
					holder.ivdingwei.setBackground(null);
				}
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(),
								CreaticeIndexRankingAreaActivity.class);
						intent.putExtra("id", list.get(position).get("id")
								.toString());
						intent.putExtra("area", list.get(position).get("name")
								.toString());
						intent.putExtra("cid", "3");
						startActivity(intent);
					}
				});
				return v;
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
				return list.size();
			}
		};
		listview.setAdapter(baseAdapter);
	}

	public class ViewHolder {
		private ImageView ivdingwei;
		private ImageView tvimage;
		private TextView tvname;
		private TextView tvNum;
	}

	public void init() {

		listview = (XListView) rootView
				.findViewById(R.id.ideas_ranking_region_city_list);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		bitmapUtils = new BitmapUtils(getActivity());
	}

	private void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}

	public void chushihua() {
		// TODO Auto-generated method stub
		// dialog = new ResDialog(MyChuangChuangCurrencyActivity.this,
		// R.style.MyDialog,
		// "努力加载中", R.drawable.loads);
		// dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringzhishuRankingdiqu);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("cid", "3"));
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

	private void loadRemnantListItem() {
		// 滚到加载余下的数据
		// 动态的改变listAdapter.getCount()的返回值
		// 使用Handler调用listAdapter.notifyDataSetChanged();更新数据

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringzhishuRankingdiqus);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("cid", "3"));
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