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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.CreativeDetailsActivity;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

public class CreativeFragment extends Fragment implements IXListViewListener {

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		count = 0;
		super.onResume();
	}

	private View rootView;
	XListView listviewCreative;
	BaseAdapter baseAdapter;
	HttpClient httpClient;
	List<Map<String, Object>> list;
	int count = 0;
	// ResDialog dialog;
	BitmapUtils bitmapUtils;
	// class: 1投资项目创意|2任务人才创意|3公益明星创意
	// countries:国家id
	// province:省id
	// city:市id
	static String classs = "";
	static String countries = "";
	static String province = "";
	static String city = "";

	Handler handlershoucang = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			System.err.println("天安门");
			if (msg.what == 0x123) {
				String str = msg.obj.toString();

				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(getActivity(), "收藏失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(getActivity(), "收藏成功",
								Toast.LENGTH_SHORT).show();

						break;
					case 3:
						Toast.makeText(getActivity(), "已经收藏了，请勿重复收藏",
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
	Handler handlerzan = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				String str = msg.obj.toString();

				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(getActivity(), "点赞失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(getActivity(), "点赞成功",
								Toast.LENGTH_SHORT).show();

						break;
					case 3:
						Toast.makeText(getActivity(), "已经点赞了，请勿重复点赞",
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
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// dialog.dismiss();
				String str = msg.obj.toString();
				// Json:
				// 有值返回二维数组
				// num:2
				// list:二维数组
				// title:创意题目
				// class:创意类型
				// price:出售金额
				// nickname:会员名称
				// image:创意封面图
				// count:评论总数
				// zan:1已赞|0未赞
				// sc:1已收藏|0未收藏
				// 无值返回一维数组
				// num:1

				list = new ArrayList<Map<String, Object>>();
				Log.d("+++++++++", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(getActivity(), "没有创意信息",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						JSONArray jsonArray = new JSONArray(jsonObject.get(
								"list").toString());
						for (int i = 0; i < jsonArray.length(); ++i) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", temp.getString("id"));
							map.put("title", temp.getString("title"));
							map.put("class", temp.getString("class"));
							map.put("price", temp.getString("price"));
							map.put("nickname", temp.getString("nickname"));
							map.put("image", temp.getString("image"));
							map.put("count", temp.getString("count"));
							map.put("zan", temp.getString("zan"));
							map.put("sc", temp.getString("sc"));
							list.add(map);

						}
						break;

					default:
						break;
					}
					listviewCreative.setAdapter(baseAdapter);
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
				// Json:
				// 有值返回二维数组
				// num:2
				// list:二维数组
				// title:创意题目
				// class:创意类型
				// price:出售金额
				// nickname:会员名称
				// image:创意封面图
				// count:评论总数
				// zan:1已赞|0未赞
				// sc:1已收藏|0未收藏
				// 无值返回一维数组
				// num:1

				Log.d("333333", str);
				try {
					if (!str.equals("null")) {
						JSONObject jsonObject = new JSONObject(str);
						switch (Integer.parseInt(jsonObject.get("num")
								.toString())) {
						case 1:

							break;
						case 2:
							JSONArray jsonArray = new JSONArray(jsonObject.get(
									"list").toString());
							for (int i = 0; i < jsonArray.length(); ++i) {
								JSONObject temp = (JSONObject) jsonArray.get(i);
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", temp.getString("id"));
								map.put("title", temp.getString("title"));
								map.put("class", temp.getString("class"));
								map.put("price", temp.getString("price"));
								map.put("nickname", temp.getString("nickname"));
								map.put("image", temp.getString("image"));
								map.put("count", temp.getString("count"));
								map.put("zan", temp.getString("zan"));
								map.put("sc", temp.getString("sc"));
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

	public void changeDate(String classss, String countriess, String provinces,
			String citys) {
		classs = classss;
		countries = countriess;
		province = provinces;
		city = citys;
		System.err.println(classs + "+" + countries + "+" + province + "+"
				+ city);
		chushihua();
		System.err.println("沙湾");
		baseAdapter.notifyDataSetChanged();
		System.err.println("可爱逗");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_creative, container,
					false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		// 初始化
		init();
		count = 0;
		// 初始化适配器
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(final int position, View v, ViewGroup arg2) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					v = LayoutInflater.from(getActivity()).inflate(
							R.layout.listcreative, null);
					holder.iv_image = (ImageView) v
							.findViewById(R.id.listcreative_img);
					holder.iv_shouchang = (ImageView) v
							.findViewById(R.id.listcreative_collect_img);
					holder.iv_zan = (ImageView) v
							.findViewById(R.id.listcreative_collect_img2);
					holder.tvshouchang = (TextView) v
							.findViewById(R.id.tv_shoucang);
					holder.tvzan = (TextView) v.findViewById(R.id.tv_zan);
					holder.tv_title = (TextView) v
							.findViewById(R.id.listcreative_head);
					holder.tv_class = (TextView) v
							.findViewById(R.id.listcreative_type);
					holder.tv_price = (TextView) v
							.findViewById(R.id.listcreative_money);
					holder.tv_nickname = (TextView) v
							.findViewById(R.id.listcreative_name);
					holder.ll_listview_creative = (LinearLayout) v
							.findViewById(R.id.ll_listview_creative);
					holder.ll_listview_shoucang = (LinearLayout) v
							.findViewById(R.id.listcreative_collect);
					holder.ll_listview_zan = (LinearLayout) v
							.findViewById(R.id.listcreative_praise);
					holder.ll_listview_pinglun = (LinearLayout) v
							.findViewById(R.id.listcreative_comment);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}

				bitmapUtils.display(
						holder.iv_image,
						ImageAddress.Stringchuangyi
								+ list.get(position).get("image").toString());
				holder.tv_title.setText(list.get(position).get("title")
						.toString());
				holder.tv_class.setText("创意类型："
						+ list.get(position).get("class").toString());
				holder.tv_price.setText("出售金额："
						+ list.get(position).get("price").toString());
				holder.tv_nickname.setText("发布用户："
						+ list.get(position).get("nickname").toString());
				holder.ll_listview_creative
						.setOnClickListener(new OnClickListener() {

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
				holder.ll_listview_pinglun
						.setOnClickListener(new OnClickListener() {

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

				holder.ll_listview_shoucang
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (IsTrue.userId == 0) {
									Toast.makeText(getActivity(), "请先登录",
											Toast.LENGTH_SHORT).show();
									return;
								}
								ImageView imageView = (ImageView) v
										.findViewById(R.id.listcreative_collect_img);
								TextView textView = (TextView) v
										.findViewById(R.id.tv_shoucang);
								imageView
										.setBackgroundResource(R.drawable.shoucang_ed);
								textView.setText("已收藏");
						Map<String, Object>	map=list.get(position);
						map.put("sc","1" );
								shoucang();
							}

							private void shoucang() {
								// TODO Auto-generated method stub

								new Thread() {
									@Override
									public void run() {
										try {

											HttpPost post = new HttpPost(
													MyUrl.Stringshoucang);

											List<NameValuePair> params = new ArrayList<NameValuePair>();

											params.add(new BasicNameValuePair(
													"uid", IsTrue.userId + ""));
											params.add(new BasicNameValuePair(
													"source", "1"));
											params.add(new BasicNameValuePair(
													"sid", list.get(position)
															.get("id")
															.toString()));
											post.setEntity(new UrlEncodedFormEntity(
													params, HTTP.UTF_8));

											HttpResponse response = httpClient
													.execute(post);

											if (response.getStatusLine()
													.getStatusCode() == 200) {

												String str = EntityUtils
														.toString(response
																.getEntity());
												Message msg = new Message();
												msg.what = 0x123;
												msg.obj = str;
												handlershoucang
														.sendMessage(msg);

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
						});
				holder.ll_listview_zan
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (IsTrue.userId == 0) {
									Toast.makeText(getActivity(), "请先登录",
											Toast.LENGTH_SHORT).show();
									return;
								}
								ImageView imageView = (ImageView) v
										.findViewById(R.id.listcreative_collect_img2);
								TextView textView = (TextView) v
										.findViewById(R.id.tv_zan);
								imageView
										.setBackgroundResource(R.drawable.zan_ed);
								textView.setText("已赞");
								Map<String, Object>	map=list.get(position);
								map.put("zan","1" );
								zan();
							}

							private void zan() {
								// TODO Auto-generated method stub
								new Thread() {
									@Override
									public void run() {
										try {

											HttpPost post = new HttpPost(
													MyUrl.Stringzan);

											List<NameValuePair> params = new ArrayList<NameValuePair>();

											params.add(new BasicNameValuePair(
													"uid", IsTrue.userId + ""));
											params.add(new BasicNameValuePair(
													"source", "1"));
											params.add(new BasicNameValuePair(
													"sid", list.get(position)
															.get("id")
															.toString()));
											post.setEntity(new UrlEncodedFormEntity(
													params, HTTP.UTF_8));

											HttpResponse response = httpClient
													.execute(post);

											if (response.getStatusLine()
													.getStatusCode() == 200) {

												String str = EntityUtils
														.toString(response
																.getEntity());
												Message msg = new Message();
												msg.what = 0x123;
												msg.obj = str;
												handlerzan.sendMessage(msg);

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
						});
				if (list.get(position).get("sc").toString().equals("1")) {
					holder.iv_shouchang
							.setBackgroundResource(R.drawable.shoucang_ed);
					holder.tvshouchang.setText("已收藏");
					
				} else {
					holder.iv_shouchang
							.setBackgroundResource(R.drawable.shoucang);
					holder.tvshouchang.setText("收藏");
				}

				if (list.get(position).get("zan").toString().equals("1")) {
					holder.iv_zan.setBackgroundResource(R.drawable.zan_ed);
					holder.tvzan.setText("已赞");
				} else {
					holder.iv_zan.setBackgroundResource(R.drawable.zan);
					holder.tvzan.setText("赞");
				}
				return v;
			}

			@Override
			public long getItemId(int i) {
				// TODO Auto-generated method stub
				return i;
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
		chushihua();
		return rootView;

	}

	public class ViewHolder {
		LinearLayout ll_listview_creative;
		LinearLayout ll_listview_shoucang;
		LinearLayout ll_listview_zan;
		LinearLayout ll_listview_pinglun;
		ImageView iv_image;
		TextView tv_title;
		TextView tv_class;
		TextView tv_price;
		TextView tv_nickname;
		ImageView iv_shouchang;
		ImageView iv_zan;
		TextView tvshouchang;
		TextView tvzan;
	}

	private void init() {
		// TODO Auto-generated method stub
		listviewCreative = (XListView) rootView
				.findViewById(R.id.listviewCreative);
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		list = new ArrayList<Map<String, Object>>();
		bitmapUtils = new BitmapUtils(getActivity());
		listviewCreative.setPullLoadEnable(true);
		listviewCreative.setXListViewListener(this);
	}

	public void chushihua() {
		// TODO Auto-generated method stub
		// dialog = new ResDialog(getActivity(), R.style.MyDialog, "努力加载中",
		// R.drawable.loads);
		// dialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.Stringchuangyishow);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("class", classs));
					params.add(new BasicNameValuePair("countries", countries));
					params.add(new BasicNameValuePair("province", province));
					params.add(new BasicNameValuePair("city", city));
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

					HttpPost post = new HttpPost(MyUrl.Stringchuangyishows);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("page", (++count) + ""));
					Log.d("分页数", count + "");
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					params.add(new BasicNameValuePair("class", classs));
					params.add(new BasicNameValuePair("countries", countries));
					params.add(new BasicNameValuePair("province", province));
					params.add(new BasicNameValuePair("city", city));
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

	private void onLoad() {
		listviewCreative.stopRefresh();
		listviewCreative.stopLoadMore();
		listviewCreative.setRefreshTime("刚刚");
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
