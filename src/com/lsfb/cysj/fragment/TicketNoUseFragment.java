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

import cn.jpush.android.util.h;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.GalleryImageActivity;
import com.lsfb.cysj.MyChuangChuangCurrencyActivity;
import com.lsfb.cysj.MyCollectionActivity;
import com.lsfb.cysj.MyCollectionActivity.ViewHolder;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TicketNoUseFragment extends Fragment implements IXListViewListener {
	private View rootView;
	XListView listviewNoUse;
	BaseAdapter baseAdapter;
	HttpClient httpClient;
	List<Map<String, Object>> list;
	int count = 0;
	BitmapUtils bitmapUtils;
	Handler handler = new Handler() {
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
//						Toast.makeText(getActivity(), "没有未使用的入场卷",
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
							map.put("ewm", temp.getString("ewm"));
							map.put("state", temp.getString("state"));
							map.put("title", temp.getString("title"));
							map.put("price", temp.getString("price"));
							map.put("address", temp.getString("address"));
							map.put("time", temp.getString("time"));
							list.add(map);
						}
						System.out.println(list);
						break;

					default:
						break;
					}
					listviewNoUse.setAdapter(baseAdapter);
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
						Toast.makeText(getActivity(), "亲，下面没有入场卷了",
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
							map.put("ewm", temp.getString("ewm"));
							map.put("state", temp.getString("state"));
							map.put("title", temp.getString("title"));
							map.put("price", temp.getString("price"));
							map.put("address", temp.getString("address"));
							map.put("time", temp.getString("time"));
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
			rootView = inflater
					.inflate(R.layout.ticket_nouse, container, false);
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
							R.layout.listview_nouse, null);
					holder.imageview = (ImageView) v
							.findViewById(R.id.ivlistViewNoUse);
					holder.tvHead = (TextView) v
							.findViewById(R.id.tv_nouseHead);
					holder.tvaddress = (TextView) v
							.findViewById(R.id.tv_nouseaddress);
					holder.tvtime = (TextView) v
							.findViewById(R.id.tv_nousetime);
					holder.tvmoney = (TextView) v
							.findViewById(R.id.tv_nousemoney);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				bitmapUtils.display(holder.imageview,
						ImageAddress.StringRuchangjuan
								+ list.get(position).get("ewm").toString());
				holder.imageview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								GalleryImageActivity.class);
						intent.putExtra("imagekan",
								list.get(position).get("ewm").toString());
						startActivity(intent);
					}
				});
				holder.tvHead.setText("比赛标题:"
						+ list.get(position).get("title").toString());
				holder.tvaddress.setText("比赛地址:"
						+ list.get(position).get("address").toString());
				holder.tvtime.setText("比赛时间:"
						+ list.get(position).get("time").toString());
				holder.tvmoney.setText("参赛金额:￥"
						+ list.get(position).get("price").toString());
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

					HttpPost post = new HttpPost(MyUrl.Stringruchuangjuan);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("state", 0+""));
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
		private TextView tvaddress;
		private TextView tvtime;
		private TextView tvmoney;
	}

	private void init() {
		// TODO Auto-generated method stub
		listviewNoUse = (XListView) rootView.findViewById(R.id.listviewNoUse);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		bitmapUtils = new BitmapUtils(getActivity());
		listviewNoUse.setPullLoadEnable(true);
		listviewNoUse.setXListViewListener(this);
	}

	private void onLoad() {
		listviewNoUse.stopRefresh();
		listviewNoUse.stopLoadMore();
		listviewNoUse.setRefreshTime("刚刚");
	}

	private void loadRemnantListItem() {
		// 滚到加载余下的数据
		// 动态的改变listAdapter.getCount()的返回值
		// 使用Handler调用listAdapter.notifyDataSetChanged();更新数据

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringruchuangjuans);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("state", 0+""));
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
