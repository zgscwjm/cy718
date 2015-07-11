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
import com.lsfb.cysj.CreativeDetailsActivity;
import com.lsfb.cysj.MyChuangChuangCurrencyActivity;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.OtherDetailsActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.CreaticeIndexRankingAreaThinkTankExpertsFragment.ViewHolder;
import com.lsfb.cysj.view.MyGridView;
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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreaticeIndexRankingAreaCreaticeFragment extends Fragment
		implements IXListViewListener {
	private View rootView;
	XListView listview_creative;
	BaseAdapter baseAdapter;
	HttpClient httpClient;
	List<Map<String, Object>> list;
	String cid;
	String qid;
	BitmapUtils bitmapUtils;
	int count = 0;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {

				String str = msg.obj.toString();
				// num:2
				// list:二维数组
				// id:创意id
				// nickname:会员名称
				// image:会员头像
				// count:评论总数
				// sc:1已收藏|0未收藏
				// content:创意内容
				// 无值返回一维数组
				// num:1
				// 注:图片地址
				// http://cysj.coolmoban.com/Public/images/member/

				list = new ArrayList<Map<String, Object>>();
				System.err.println(str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						// Toast.makeText(getActivity(),
						// "没有任何创创币信息", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", temp.getString("id"));
							map.put("memid", temp.getString("memid"));
							map.put("nickname", temp.getString("nickname"));
							map.put("image", temp.getString("image"));
							map.put("count", temp.getString("count"));
							map.put("counts", temp.getString("counts"));
							map.put("sc", temp.getString("sc"));
							map.put("content", temp.getString("content"));
							map.put("time", temp.getString("time"));
							list.add(map);

						}
						break;

					default:
						break;
					}
					listview_creative.setAdapter(baseAdapter);
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
				// Json:
				// 返回一维数组
				// 有值存在
				// num:2存在指数信息
				// lit:二维数组
				// source:指数来源
				// time:指数变化时间
				// value:指数变化值
				// 无值存在
				// num:1没有指数信息

				System.err.println(str);
				try {
					if (!str.equals("null")) {
						JSONObject jsonObject = new JSONObject(str);
						switch (Integer.parseInt(jsonObject.get("num")
								.toString())) {
						case 1:
							Toast.makeText(getActivity(), "亲，下面没有了",
									Toast.LENGTH_SHORT).show();
							break;
						case 2:
							JSONArray jsonArray = new JSONArray(jsonObject.get(
									"list").toString());
							for (int i = 0; i < jsonArray.length(); ++i) {
								JSONObject temp = (JSONObject) jsonArray.get(i);
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", temp.getString("id"));
								map.put("memid", temp.getString("memid"));
								map.put("nickname", temp.getString("nickname"));
								map.put("image", temp.getString("image"));
								map.put("count", temp.getString("count"));
								map.put("counts", temp.getString("counts"));
								map.put("sc", temp.getString("sc"));
								map.put("content", temp.getString("content"));
								map.put("time", temp.getString("time"));
								list.add(map);

							}
							break;

						default:
							break;
						}
						baseAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public void setData(String cid, String qid) {
		this.cid = cid;
		this.qid = qid;
		init();
		count = 0;
		chushihua();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(
					R.layout.creaticeindexrankingarea_creatice, container,
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
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(final int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					// v = LayoutInflater
					// .from(getActivity())
					// .inflate(
					// R.layout.video_show,
					// null);
					v = LayoutInflater.from(getActivity()).inflate(
							R.layout.image_show, null);
					holder.imageview = (ImageView) v
							.findViewById(R.id.iv_IdeasFriends);
					holder.ivshoucang = (ImageView) v
							.findViewById(R.id.iv_chuangFriendzan);
					holder.tvHead = (TextView) v
							.findViewById(R.id.tv_IdeasFriendHead);
					holder.tvContext = (TextView) v
							.findViewById(R.id.tv_IdeasFriendsContext);
					holder.tvtime = (TextView) v
							.findViewById(R.id.tv_IdeasFriendsTime);
					holder.tvshoucangnum = (TextView) v
							.findViewById(R.id.tv_IdeasFriendzhisuMum);
					holder.tvpinglunnum = (TextView) v
							.findViewById(R.id.tv_IdeasFriendpinglunNUm);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				bitmapUtils.display(holder.imageview, ImageAddress.Stringhead
						+ list.get(position).get("image").toString());
				if(list.get(position).get("sc").toString().equals("0")){
					holder.ivshoucang.setBackgroundResource(R.drawable.like_ed);
				}else{
					holder.ivshoucang.setBackgroundResource(R.drawable.like_ed_shixin);
				}
				holder.tvHead.setText(list.get(position).get("nickname").toString());
				holder.tvContext.setText(list.get(position).get("content").toString());
				holder.tvtime.setText(list.get(position).get("time").toString());
				holder.tvshoucangnum.setText(list.get(position).get("counts").toString());
				holder.tvpinglunnum.setText(list.get(position).get("count").toString());
				holder.imageview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(list.get(position).get("memid").toString().equals(IsTrue.userId+"")){
							Intent intent=new Intent(getActivity(),MyDetailsActivity.class);
							startActivity(intent);
						}else{
							Intent intent=new Intent(getActivity(),OtherDetailsActivity.class);
							intent.putExtra("id", list.get(position).get("memid").toString());
							startActivity(intent);
						}
					}
				});
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(),
								CreativeDetailsActivity.class);
						intent.putExtra("id",
								list.get(position).get("id").toString());
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

	public class ViewHolder {
		private ImageView imageview;
		private TextView tvHead;
		private TextView tvContext;
		private TextView tvtime;
		private ImageView ivshoucang;
		private TextView tvshoucangnum;
		private TextView tvpinglunnum;
	}

	private void init() {
		listview_creative = (XListView) rootView
				.findViewById(R.id.listview_creative);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		listview_creative.setPullLoadEnable(true);
		listview_creative.setXListViewListener(this);
		bitmapUtils = new BitmapUtils(getActivity());
	}

	private void onLoad() {
		listview_creative.stopRefresh();
		listview_creative.stopLoadMore();
		listview_creative.setRefreshTime("刚刚");
	}

	public void chushihua() {
		// TODO Auto-generated method stub

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(
							MyUrl.StringareaRankingchuangyi);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("cid", cid));
					params.add(new BasicNameValuePair("qid", qid));
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

					HttpPost post = new HttpPost(
							MyUrl.StringareaRankingchuangyis);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("cid", cid));
					params.add(new BasicNameValuePair("qid", qid));
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
