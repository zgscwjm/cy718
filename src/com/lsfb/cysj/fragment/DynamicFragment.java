package com.lsfb.cysj.fragment;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;

import com.lsfb.cysj.VideoActivity;
import com.lsfb.cysj.Dialog.PingLunDialog;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.IdeasFriendsFragment.ViewHolder;
import com.lsfb.cysj.viedo.C;
import com.lsfb.cysj.viedo.HttpGetProxy;
import com.lsfb.cysj.viedo.ProxyUtils;
import com.lsfb.cysj.view.ListViewForScrollView;
import com.lsfb.cysj.view.MyGridView;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

/**
 * 我-动态fragment
 * @author Administrator
 *
 */
public class DynamicFragment extends Fragment implements IXListViewListener {

	private View rootView;
	HttpClient httpClient;
	AsyncHttpClient client;
	RequestParams params;
	String classData;
	String strmid;
	private XListView listview;
	Intent intent;
	BitmapUtils bitmapUtils;
	List<Map<String, Object>> list;
	ResDialog dialog;
	// List<String> listimage;
	BaseAdapter imgAdapter, urlAdapter, baseAdapter;
	TextView tvzan;// 临时缓存赞的显示
	JSONObject jsonObjectpinglun;// 评论
	int count = 0;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// num:2(优值存在)
			// list:二维数组
			// rtime:时间
			// sid:动态id
			// nickname:会员昵称
			// memimages:会员头像(0会员未设置头像)
			// content:动态
			// images:动态图片(0没有动态图片)
			// video:动态视频
			// zan:赞数量(无值存在返回0)
			// zand:1(已赞)|0(未赞)
			// plnum:评论数量(无值存在返回0,pl键名不会返回)
			// pl:二维数组
			// nickname:会员昵称
			// mid:会员id
			// content:评论内容
			// udel:1可删|0不可删
			// num:1(无值存在)

			if (msg.what == 0x123) {
				dialog.dismiss();
				String str = msg.obj.toString();
				System.err.println(str);
				System.err.println(IsTrue.userId);
				try {
					JSONObject jsonObject = new JSONObject(str);
					int strnum = Integer.parseInt(jsonObject.getString("num")
							.toString());
					switch (strnum) {
					case 1:
						Toast.makeText(getActivity(), "创友圈还没有动态",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:

						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						list = new ArrayList<Map<String, Object>>();
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("rtime", temp.getString("rtime"));
							map.put("nickname", temp.getString("nickname"));
							map.put("content", temp.getString("content"));
							map.put("images", temp.getString("images"));
							map.put("video", temp.getString("video"));
							map.put("zan", temp.getString("zan"));
							map.put("plnum", temp.getString("plnum"));
							map.put("pl", temp.getString("pl"));
							map.put("sid", temp.getString("sid"));
							map.put("zand", temp.getString("zand"));
							map.put("memimages", temp.getString("memimages"));

							list.add(map);

						}
						break;

					default:
						break;
					}
					listview.setAdapter(baseAdapter);
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
				System.err.println(str);

				try {
					JSONObject jsonObject = new JSONObject(str);
					int strnum = Integer.parseInt(jsonObject.getString("num")
							.toString());
					switch (strnum) {
					case 1:
						// Toast.makeText(getActivity(), "创友圈还没有动态",
						// Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("id", temp.getString("id"));
							map.put("rtime", temp.getString("rtime"));
							map.put("nickname", temp.getString("nickname"));
							map.put("content", temp.getString("content"));
							map.put("images", temp.getString("images"));
							map.put("video", temp.getString("video"));
							map.put("zan", temp.getString("zan"));
							map.put("plnum", temp.getString("plnum"));
							map.put("pl", temp.getString("pl"));
							map.put("sid", temp.getString("sid"));
							map.put("zand", temp.getString("zand"));
							map.put("memimages", temp.getString("memimages"));

							list.add(map);

						}
						break;

					default:
						break;
					}
					baseAdapter.notifyDataSetInvalidated();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	Handler handlerdianzan = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				System.err.println(str);

				try {
					JSONObject jsonObject = new JSONObject(str);
					int strnum = Integer.parseInt(jsonObject.getString("num")
							.toString());
					switch (strnum) {
					case 1:
						Toast.makeText(getActivity(), "点赞失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(getActivity(), "点赞成功",
								Toast.LENGTH_SHORT).show();
						int strzannum = Integer.parseInt(tvzan.getText()
								.toString());

						strzannum = strzannum + 1;

						tvzan.setText(strzannum + "");

						break;
					case 3:
						Toast.makeText(getActivity(), "您已点过赞了",
								Toast.LENGTH_SHORT).show();
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_dynamic, container,
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
		return rootView;

	}

	public void setData() {
		
		data();

		chushihua();
	}

	public void setClassData(String strclass, String strmid) {
		this.classData = strclass;
		this.strmid = strmid;
		setData();
	}

	private void chushihua() {
		// TODO Auto-generated method stub
		dialog = new ResDialog(getActivity(), R.style.MyDialog, "努力加载中",
				R.drawable.loads);
		dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.stringdynamic);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("class", classData));
					params.add(new BasicNameValuePair("mid", strmid));
					System.err.println("宝泉兄是猴子请来的" + strmid);
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

	private void data() {
		baseAdapter = new BaseAdapter() {
			private final static String TAG = "testVideoPlayer";

			private VideoView videoview;
			private MediaController mediaController;
			private HttpGetProxy proxy;
			private long startTimeMills;
			String oriVideoUrl;

			@Override
			public View getView(final int position, View v, ViewGroup arg2) {
				ViewHolder holder = null;
				if (v == null) {
					// 可以理解为从vlist获取view 之后把view返回给ListView
					// v = LayoutInflater
					// .from(getActivity())
					// .inflate(
					// R.layout.video_show,
					// null);
					holder = new ViewHolder();
					v = LayoutInflater.from(getActivity()).inflate(
							R.layout.image_show, null);

					holder.imageview = (ImageView) v
							.findViewById(R.id.iv_IdeasFriends);
					holder.tvHead = (TextView) v
							.findViewById(R.id.tv_IdeasFriendHead);
					holder.tvContext = (TextView) v
							.findViewById(R.id.tv_IdeasFriendsContext);
					holder.tvTime = (TextView) v
							.findViewById(R.id.tv_IdeasFriendsTime);
					holder.tvzhisuNum = (TextView) v
							.findViewById(R.id.tv_IdeasFriendzhisuMum);
					holder.tv_IdeasFriendpinglunNUm = (TextView) v
							.findViewById(R.id.tv_IdeasFriendpinglunNUm);
					holder.image1 = (ImageView) v
							.findViewById(R.id.chuangFriendImage1);
					holder.image2 = (ImageView) v
							.findViewById(R.id.chuangFriendImage2);
					holder.image3 = (ImageView) v
							.findViewById(R.id.chuangFriendImage3);
					holder.image4 = (ImageView) v
							.findViewById(R.id.chuangFriendImage4);
					holder.image5 = (ImageView) v
							.findViewById(R.id.chuangFriendImage5);
					holder.image6 = (ImageView) v
							.findViewById(R.id.chuangFriendImage6);
					holder.image7 = (ImageView) v
							.findViewById(R.id.chuangFriendImage7);
					holder.image8 = (ImageView) v
							.findViewById(R.id.chuangFriendImage8);
					holder.image9 = (ImageView) v
							.findViewById(R.id.chuangFriendImage9);
					holder.videoView = (ImageView) v
							.findViewById(R.id.vv_chuangFriend);
					holder.iv_chuangFriendzan = (ImageView) v
							.findViewById(R.id.iv_chuangFriendzan);
					holder.lv_chuangFriendpinglun = (LinearLayout) v
							.findViewById(R.id.lv_chuangFriendpinglun);
					holder.iv_chuangFriendpinglun = (ImageView) v
							.findViewById(R.id.iv_chuangFriendpinglun);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				// //设置评论布局的颜色
				// holder.lv_chuangFriendpinglun.setBackgroundResource(R.color.greyxian);
				bitmapUtils.display(holder.imageview, ImageAddress.Stringhead
						+ list.get(position).get("memimages").toString());
				holder.tvHead.setText(list.get(position).get("nickname")
						.toString());
				holder.tvContext.setText(list.get(position).get("content")
						.toString());
				holder.tvTime.setText(list.get(position).get("rtime")
						.toString());
				holder.tvzhisuNum.setText(list.get(position).get("zan")
						.toString());
				holder.tv_IdeasFriendpinglunNUm.setText(list.get(position)
						.get("plnum").toString());

				String strimage = list.get(position).get("images").toString();
				String[] strimages = strimage.split(",");
				System.err.println(strimages.length + "图片的个数");
				switch (strimages.length) {
				case 0:
					holder.image1.setVisibility(View.GONE);
					holder.image2.setVisibility(View.GONE);
					holder.image3.setVisibility(View.GONE);
					holder.image4.setVisibility(View.GONE);
					holder.image5.setVisibility(View.GONE);
					holder.image6.setVisibility(View.GONE);
					holder.image7.setVisibility(View.GONE);
					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;

				case 1:
					holder.image1.setVisibility(View.VISIBLE);
					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);

					holder.image2.setVisibility(View.GONE);
					holder.image3.setVisibility(View.GONE);
					holder.image4.setVisibility(View.GONE);
					holder.image5.setVisibility(View.GONE);
					holder.image6.setVisibility(View.GONE);
					holder.image7.setVisibility(View.GONE);
					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;
				case 2:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);

					holder.image3.setVisibility(View.GONE);
					holder.image4.setVisibility(View.GONE);
					holder.image5.setVisibility(View.GONE);
					holder.image6.setVisibility(View.GONE);
					holder.image7.setVisibility(View.GONE);
					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;
				case 3:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);
					holder.image3.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);
					bitmapUtils.display(holder.image3,
							ImageAddress.Stringchuangyi + strimages[2]);

					holder.image4.setVisibility(View.GONE);
					holder.image5.setVisibility(View.GONE);
					holder.image6.setVisibility(View.GONE);
					holder.image7.setVisibility(View.GONE);
					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;
				case 4:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);
					holder.image3.setVisibility(View.VISIBLE);
					holder.image4.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);
					bitmapUtils.display(holder.image3,
							ImageAddress.Stringchuangyi + strimages[2]);
					bitmapUtils.display(holder.image4,
							ImageAddress.Stringchuangyi + strimages[3]);

					holder.image5.setVisibility(View.GONE);
					holder.image6.setVisibility(View.GONE);
					holder.image7.setVisibility(View.GONE);
					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;
				case 5:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);
					holder.image3.setVisibility(View.VISIBLE);
					holder.image4.setVisibility(View.VISIBLE);
					holder.image5.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);
					bitmapUtils.display(holder.image3,
							ImageAddress.Stringchuangyi + strimages[2]);
					bitmapUtils.display(holder.image4,
							ImageAddress.Stringchuangyi + strimages[3]);
					bitmapUtils.display(holder.image5,
							ImageAddress.Stringchuangyi + strimages[4]);

					holder.image6.setVisibility(View.GONE);
					holder.image7.setVisibility(View.GONE);
					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;
				case 6:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);
					holder.image3.setVisibility(View.VISIBLE);
					holder.image4.setVisibility(View.VISIBLE);
					holder.image5.setVisibility(View.VISIBLE);
					holder.image6.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);
					bitmapUtils.display(holder.image3,
							ImageAddress.Stringchuangyi + strimages[2]);
					bitmapUtils.display(holder.image4,
							ImageAddress.Stringchuangyi + strimages[3]);
					bitmapUtils.display(holder.image5,
							ImageAddress.Stringchuangyi + strimages[4]);
					bitmapUtils.display(holder.image6,
							ImageAddress.Stringchuangyi + strimages[5]);

					holder.image7.setVisibility(View.GONE);
					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;
				case 7:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);
					holder.image3.setVisibility(View.VISIBLE);
					holder.image4.setVisibility(View.VISIBLE);
					holder.image5.setVisibility(View.VISIBLE);
					holder.image6.setVisibility(View.VISIBLE);
					holder.image7.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);
					bitmapUtils.display(holder.image3,
							ImageAddress.Stringchuangyi + strimages[2]);
					bitmapUtils.display(holder.image4,
							ImageAddress.Stringchuangyi + strimages[3]);
					bitmapUtils.display(holder.image5,
							ImageAddress.Stringchuangyi + strimages[4]);
					bitmapUtils.display(holder.image6,
							ImageAddress.Stringchuangyi + strimages[5]);
					bitmapUtils.display(holder.image7,
							ImageAddress.Stringchuangyi + strimages[6]);

					holder.image8.setVisibility(View.GONE);
					holder.image9.setVisibility(View.GONE);
					break;
				case 8:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);
					holder.image3.setVisibility(View.VISIBLE);
					holder.image4.setVisibility(View.VISIBLE);
					holder.image5.setVisibility(View.VISIBLE);
					holder.image6.setVisibility(View.VISIBLE);
					holder.image7.setVisibility(View.VISIBLE);
					holder.image8.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);
					bitmapUtils.display(holder.image3,
							ImageAddress.Stringchuangyi + strimages[2]);
					bitmapUtils.display(holder.image4,
							ImageAddress.Stringchuangyi + strimages[3]);
					bitmapUtils.display(holder.image5,
							ImageAddress.Stringchuangyi + strimages[4]);
					bitmapUtils.display(holder.image6,
							ImageAddress.Stringchuangyi + strimages[5]);
					bitmapUtils.display(holder.image7,
							ImageAddress.Stringchuangyi + strimages[6]);
					bitmapUtils.display(holder.image8,
							ImageAddress.Stringchuangyi + strimages[7]);

					holder.image9.setVisibility(View.GONE);
					break;
				case 9:
					holder.image1.setVisibility(View.VISIBLE);
					holder.image2.setVisibility(View.VISIBLE);
					holder.image3.setVisibility(View.VISIBLE);
					holder.image4.setVisibility(View.VISIBLE);
					holder.image5.setVisibility(View.VISIBLE);
					holder.image6.setVisibility(View.VISIBLE);
					holder.image7.setVisibility(View.VISIBLE);
					holder.image8.setVisibility(View.VISIBLE);
					holder.image9.setVisibility(View.VISIBLE);
					bitmapUtils.display(holder.image1,
							ImageAddress.Stringchuangyi + strimages[0]);
					bitmapUtils.display(holder.image2,
							ImageAddress.Stringchuangyi + strimages[1]);
					bitmapUtils.display(holder.image3,
							ImageAddress.Stringchuangyi + strimages[2]);
					bitmapUtils.display(holder.image4,
							ImageAddress.Stringchuangyi + strimages[3]);
					bitmapUtils.display(holder.image5,
							ImageAddress.Stringchuangyi + strimages[4]);
					bitmapUtils.display(holder.image6,
							ImageAddress.Stringchuangyi + strimages[5]);
					bitmapUtils.display(holder.image7,
							ImageAddress.Stringchuangyi + strimages[6]);
					bitmapUtils.display(holder.image8,
							ImageAddress.Stringchuangyi + strimages[7]);
					bitmapUtils.display(holder.image9,
							ImageAddress.Stringchuangyi + strimages[8]);
					break;
				default:
					break;
				}
				// 视频播放
				if (!list.get(position).get("video").toString().equals("0")) {

					holder.videoView.setVisibility(View.VISIBLE);

					holder.videoView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(),
									VideoActivity.class);
							intent.putExtra("uri", ImageAddress.Stringchuangyi
									+ list.get(position).get("video")
											.toString());
							startActivity(intent);
						}
					});
				} else {
					holder.videoView.setVisibility(View.GONE);
				}

				// 赞
				if (list.get(position).get("zand").toString().equals("0")) {
					holder.iv_chuangFriendzan
							.setBackgroundResource(R.drawable.like_ed);
				} else {
					holder.iv_chuangFriendzan
							.setBackgroundResource(R.drawable.like_ed_shixin);
				}
				holder.iv_chuangFriendzan
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								TextView tv = (TextView) ((View) v.getParent())
										.findViewById(R.id.tv_IdeasFriendzhisuMum);
								tvzan = tv;
								((ImageView) v)
										.setBackgroundResource(R.drawable.like_ed_shixin);
								dianzan(list.get(position).get("sid")
										.toString());
							}

						});
				holder.iv_chuangFriendpinglun
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								pinglundialog(list.get(position).get("sid")
										.toString());
							}
						});

				try {
					holder.lv_chuangFriendpinglun.removeAllViews();
					String stringpinglun = list.get(position).get("pl")
							.toString();
					JSONArray jsonArraypinglun = new JSONArray(stringpinglun);
					System.err.println("+++" + stringpinglun);
					System.err.println("++-" + position);
					for (int i = 0; i < jsonArraypinglun.length(); i++) {
						jsonObjectpinglun = (JSONObject) jsonArraypinglun
								.get(i);
						TextView tvnickname = new TextView(getActivity());
						TextView tvcontent = new TextView(getActivity());
						tvnickname.setTextColor(getActivity().getResources()
								.getColorStateList(R.color.greynickname));
						tvcontent.setTextColor(getActivity().getResources()
								.getColorStateList(R.color.greypingluncontext));
						tvnickname.setText(jsonObjectpinglun
								.getString("nickname") + "：");
						tvcontent.setText(jsonObjectpinglun
								.getString("content"));
						LinearLayout ll = new LinearLayout(getActivity());
						ll.addView(tvnickname);
						ll.addView(tvcontent);
						ll.setPadding(10, 10, 0, 10);
						ll.setOnLongClickListener(new OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								new AlertDialog.Builder(getActivity())
										.setTitle("提示")
										.setMessage("是否删除该评论")
										.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														JSONObject json = jsonObjectpinglun;// 评论

														try {
															delpinglun(
																	list.get(
																			position)
																			.get("sid")
																			.toString(),
																	jsonObjectpinglun
																			.getString(
																					"plid")
																			.toString());
														} catch (Exception e) {
															e.printStackTrace();
														}
													}

												})
										.setNegativeButton(
												"取消",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														// 取消按钮事件

													}
												}).show();

								return false;
							}
						});
						// tv.setText(jsonObjectpinglun.getString("nickname")
						// + ":" + jsonObjectpinglun.getString("content"));
						holder.lv_chuangFriendpinglun.addView(ll);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return v;
			}

			@Override
			public long getItemId(int i) {
				return i;
			}

			@Override
			public Object getItem(int i) {
				return null;
			}

			@Override
			public int getCount() {
				return list.size();
			}
		};

		
	}

	// 删除评论
	private void delpinglun(String nameid, String wid) {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", nameid);
		params.put("source", 3);
		params.put("uid", IsTrue.userId);
		params.put("cid", wid);

		client.post(MyUrl.origacommentdel, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						System.out.println(response
								+ "????????????????????????????");

						Toast.makeText(getActivity(), "删除成功",
								Toast.LENGTH_SHORT).show();
						setData();
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Toast.makeText(getActivity(), errorResponse.toString(),
								Toast.LENGTH_SHORT).show();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}
				});
	}

	// 评论弹窗
	protected void pinglundialog(String nameid) {
		Dialog dialog = new PingLunDialog(getActivity(), R.style.MyDialog,
				nameid, 3 + "", new PingLunDialog.PriorityListener() {

					@Override
					public void refreshPriorityUI(
							List<Map<String, Object>> listmap) {
						System.err.println(listmap.toString());
					}
				});
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		setData();
	}

	public void dianzan(final String sid) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringzan);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("source", "3"));
					params.add(new BasicNameValuePair("sid", sid));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlerdianzan.sendMessage(msg);

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

	private void init() {
		new File(C.getBufferDir()).mkdirs();// 创建预加载文件的文件夹
		ProxyUtils.clearCacheFile(C.getBufferDir());// 清除前面的预加载文件
		bitmapUtils = new BitmapUtils(getActivity());
		listview = (XListView) rootView.findViewById(R.id.listview_dynamic);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		// listimage = new ArrayList<String>();
	}

	public class ViewHolder {
		private ImageView imageview;
		private TextView tvHead;
		private TextView tvContext;

		private TextView tvTime;
		private TextView tvzhisuNum;
		private TextView tv_IdeasFriendpinglunNUm;
		private ImageView image1;
		private ImageView image2;
		private ImageView image3;
		private ImageView image4;
		private ImageView image5;
		private ImageView image6;
		private ImageView image7;
		private ImageView image8;
		private ImageView image9;

		private ImageView iv_chuangFriendzan;
		private ImageView iv_chuangFriendpinglun;
		private ImageView videoView;
		private LinearLayout lv_chuangFriendpinglun;
	}

	private void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}

	private void loadRemnantListItem() {
		// 滚到加载余下的数据
		// 动态的改变listAdapter.getCount()的返回值
		// 使用Handler调用listAdapter.notifyDataSetChanged();更新数据

		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.stringdynamics);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("class", classData));
					params.add(new BasicNameValuePair("mid", strmid));
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
