package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.GameWorksActivity;
import com.lsfb.cysj.GuanZhuMan;
import com.lsfb.cysj.HotIdeasGamesContent2Activity;
import com.lsfb.cysj.HotIdeasGamesContentActivity;
import com.lsfb.cysj.HotZhiKuCity;
import com.lsfb.cysj.Dialog.ApplyDialog;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

/**
 * 热门创意比赛,item:比赛详情,创意详情：HotIdeasGamesContentActivity
 * 
 * @author Administrator
 * 
 */
public class HotIdeasGameFragment extends Fragment implements
		IXListViewListener, OnClickListener {

	private static String[] nums = new String[] { "223", "224", "225", "226",
			"222", "221", "220", "227", "228" };
	private View rootView;
	private XListView list;
	BaseAdapter adapter;
	Intent intent;
	String ip;
	AsyncHttpClient client;
	RequestParams params;
	private TextView text;
	String lists;
	Map<String, Object> map;
	List<Map<String, Object>> listmap;
	Dialog jiazaidialog;
	int count = 0;
	List<EMGroup> grouplist;// 群列表

	private TextView city, type;

	String result0 = "";
	String result1 = "";
	String result2 = "";
	String classs = "";
	String countries = "";
	String province = "";
	String scity = "";

	CreativeFragment fragmebt_ideas_world;// 创意世界的Fragment

	public static final int RESULT_OK = 1022;
	public static final int STYLE = 1023;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.hot_ideas_game, container,
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
		return rootView;
	}

	protected void showdialogup() {
		jiazaidialog = new ResDialog(getActivity(), R.style.MyDialog,
				"正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void initdata(String classs, String countries, String province,
			String city) {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("ip", ip);
		params.put("class", classs);
		params.put("countries", countries);
		params.put("province", province);
		params.put("city", city);

		client.post(MyUrl.blist, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						text.setVisibility(View.VISIBLE);
						text.setText("目前还没信息哦");
					} else if (i == 2) {
						text.setVisibility(View.GONE);
						lists = response.getString("list").toString();
						date();
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
				// Toast.makeText(getActivity(), errorResponse + "",
				// Toast.LENGTH_SHORT).show();
				jiazaidialog.dismiss();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	static class ViewHolder {
		ImageView img;// 头像
		TextView head;// 题目
		TextView content;// 内容
		TextView num1;// 数字一
		TextView num2;// 数字二
		TextView classfy;//

		LinearLayout llChengyuan;// 成员
		LinearLayout llZuopin;// 作品

	}

	private void date() {
		// id:大赛id
		// title:大赛题目
		// image:大赛封面图
		// introduce:比赛介绍
		// number:活跃人数
		// maxnumber:总人数
		try {
			JSONArray array = new JSONArray(lists);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("title", object.getString("title").toString());
				map.put("image", object.getString("image").toString());
				map.put("introduce", object.getString("introduce").toString());
				map.put("number", object.getString("number").toString());
				map.put("maxnumber", object.getString("maxnumber").toString());
				map.put("class", object.getString("class").toString());

				listmap.add(map);
			}
			System.out.println(listmap + "IIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
			adapter = new BaseAdapter() {
				@Override
				public View getView(final int position, View convertView,
						ViewGroup parent) {
					ViewHolder viewHolder = null;
					if (convertView == null) {
						convertView = LayoutInflater.from(getActivity())
								.inflate(R.layout.hot_ideas_games_item, null);
						viewHolder = new ViewHolder();
						viewHolder.img = (ImageView) convertView
								.findViewById(R.id.hot_ideas_games_item_img);
						viewHolder.head = (TextView) convertView
								.findViewById(R.id.hot_ideas_games_item_head);
						viewHolder.content = (TextView) convertView
								.findViewById(R.id.hot_ideas_games_item_content);
						viewHolder.num1 = (TextView) convertView
								.findViewById(R.id.hot_ideas_games_item_num1);
						viewHolder.num2 = (TextView) convertView
								.findViewById(R.id.hot_ideas_games_item_num2);
						viewHolder.classfy = (TextView) convertView
								.findViewById(R.id.hot_ideas_games_item_class);

						viewHolder.llChengyuan = (LinearLayout) convertView
								.findViewById(R.id.llChengyuan);
						viewHolder.llZuopin = (LinearLayout) convertView
								.findViewById(R.id.llZuopin);

						convertView.setTag(viewHolder);
					} else {
						viewHolder = (ViewHolder) convertView.getTag();
					}
					BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(viewHolder.img, ImageAddress.cbit
							+ listmap.get(position).get("image").toString());
					viewHolder.head.setText(listmap.get(position).get("title")
							.toString());
					viewHolder.content.setText(listmap.get(position)
							.get("introduce").toString());
					viewHolder.num1.setText(listmap.get(position).get("number")
							.toString());
					viewHolder.num2.setText(listmap.get(position)
							.get("maxnumber").toString());

					// 成员,作品详情进入

					String classfy = listmap.get(position).get("class")
							.toString();
					if ("1".equals(classfy)) {
						viewHolder.classfy.setText("投资项目创意");
					} else if ("2".equals(classfy)) {
						viewHolder.classfy.setText("任务人才创意");
					} else {
						viewHolder.classfy.setText("公益明星创意");
					}

					viewHolder.llChengyuan
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent=new Intent(getActivity(),GuanZhuMan.class);
									startActivity(intent);	 
								}
							});

					viewHolder.llZuopin
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									//参赛作品
									Intent intent=new Intent(getActivity(),GameWorksActivity.class);
									intent.putExtra("sid", listmap.get(position).get("id")
									.toString());
									startActivity(intent);
								}
							});

					convertView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showdialogup();
							client = new AsyncHttpClient();
							params = new RequestParams();
							params.put("sid", listmap.get(position).get("id")
									.toString());
							params.put("uid", IsTrue.userId);
							client.post(MyUrl.bsinger, params,
									new JsonHttpResponseHandler() {
										@Override
										public void onSuccess(int statusCode,
												Header[] headers,
												JSONObject response) {
											try {
												String num = response
														.getString("num");
												int i = Integer.parseInt(num);
												if (i == 1) {

												} else if (i == 2) {
													String list = response
															.getString("list");
													JSONObject object = new JSONObject(
															list);
													String collection = object
															.getString("collection");
													if (collection.equals("1")
															|| collection
																	.equals("3")) {
														intent = new Intent(
																getActivity(),
																HotIdeasGamesContent2Activity.class);
														intent.putExtra(
																"sid",
																listmap.get(
																		position)
																		.get("id")
																		.toString());
														startActivity(intent);
													} else if (collection
															.equals("2")) {
														intent = new Intent(
																getActivity(),
																HotIdeasGamesContentActivity.class);
														intent.putExtra(
																"sid",
																listmap.get(
																		position)
																		.get("id")
																		.toString());
														startActivity(intent);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
											jiazaidialog.dismiss();
											super.onSuccess(statusCode,
													headers, response);
										}

										@Override
										public void onFailure(int statusCode,
												Header[] headers,
												Throwable throwable,
												JSONObject errorResponse) {
											jiazaidialog.dismiss();
											// Toast.makeText(getActivity(),
											// errorResponse + "",
											// Toast.LENGTH_SHORT).show();
											super.onFailure(statusCode,
													headers, throwable,
													errorResponse);
										}
									});
						}
					});
					// if (position == 0) {
					// view.setOnClickListener(new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// intent = new
					// Intent(getActivity(),HotIdeasGamesContentActivity.class);
					// startActivity(intent);
					// }
					// });
					// } else {
					// view.setOnClickListener(new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// intent = new
					// Intent(getActivity(),HotIdeasGamesContent2Activity.class);
					// startActivity(intent);
					// }
					// });
					// }
					return convertView;
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String, Object>>();
		list = (XListView) rootView.findViewById(R.id.hot_ideas_games_list);
		list.setPullLoadEnable(true);
		list.setXListViewListener(this);
		text = (TextView) rootView.findViewById(R.id.hot_ideas_games_text);

		city = (TextView) rootView.findViewById(R.id.ideas_world_son_button1);
		city.setOnClickListener(this);
		type = (TextView) rootView.findViewById(R.id.ideas_world_son_button2);
		type.setOnClickListener(this);

		fragmebt_ideas_world = (CreativeFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.fragmebt_ideas_world);

		showdialogup();
		getip();
		if (IsTrue.userId == 0) {

		} else {
			grouplist = EMGroupManager.getInstance().getAllGroups();// 需异步处理,获取群聊列表
		}
	}

	private void getip() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, "http://city.ip138.com/ip2city.asp",
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// 从反馈的结果中提取出IP地址
						int start = arg0.result.toString().indexOf("[");
						int end = arg0.result.toString()
								.indexOf("]", start + 1);
						ip = arg0.result.toString().substring(start + 1, end);
						initdata("", "", "", "");
					}
				});
	}

	@Override
	public void onRefresh() {
		count = 0;
		listmap = new ArrayList<Map<String, Object>>();
		initdata("", "", "", "");
		adapter.notifyDataSetChanged();
		onLoad();
	}

	@Override
	public void onResume() {
		// ideas_vertical.smoothScrollTo(0, 0);
		result0 = IsTrue.Stringidstrarea0;
		result1 = IsTrue.Stringidstrarea1;
		result2 = IsTrue.Stringidstrarea2;
		System.err.println("重复没有··········");
		// fragmebt_ideas_world.changeDate(classs, result0, result1, result2);
		super.onResume();
	}

	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime("刚刚");
	}

	@Override
	public void onLoadMore() {
		LoadMoremsg();
		adapter.notifyDataSetChanged();
		onLoad();
	}

	private void LoadMoremsg() {
		client = new AsyncHttpClient();
		params.put("page", ++count + "");
		params.put("ip", ip);
		client.post(MyUrl.blistpage, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(getActivity(), "亲,已经没有啦",
								Toast.LENGTH_SHORT).show();
					} else if (i == 2) {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", object.getString("id").toString());
							map.put("title", object.getString("title")
									.toString());
							map.put("image", object.getString("image")
									.toString());
							map.put("introduce", object.getString("introduce")
									.toString());
							map.put("number", object.getString("number")
									.toString());
							map.put("maxnumber", object.getString("maxnumber")
									.toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// Toast.makeText(getActivity(), errorResponse + "",
				// Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.ideas_world_son_button1:// 选择城市
			intent = new Intent(getActivity(), HotZhiKuCity.class);

			startActivityForResult(intent, 1022);
			// startActivity(intent);
			break;
		case R.id.ideas_world_son_button2:// 创意类型
			showDialog();
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bundle b;
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			b = data.getExtras(); // data为B中回传的Intent
			String result0 = b.getString("result0");// str即为回传的值
			String result1 = b.getString("result1");// str即为回传的值
			String result2 = b.getString("result2");// str即为回传的值
			String result3 = b.getString("result3");// str即为回传的值

			if (TextUtils.isEmpty(result0) && TextUtils.isEmpty(result1)
					&& TextUtils.isEmpty(result2)) {
				Log.d("citys", result0 + ":" + result1 + ":" + result2 + ":"
						+ result3);
				initdata("", result0, result1, result2);

			}
			break;

		default:
			break;
		}
	}

	private void showDialog() {
		Dialog dialog = new ApplyDialog(getActivity(), R.style.MyDialog,
				new ApplyDialog.PriorityListener() {

					@Override
					public void refreshPriorityUI(String string, String string2) {
						// TODO Auto-generated method stub
						classs = string2;
						fragmebt_ideas_world.changeDate(classs, result0,
								result1, result2);
					}
				});
		dialog.show();
	}
}
