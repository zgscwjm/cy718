/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMChatRoomChangeListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMCursorResult;
import com.easemob.chat.EMGroupInfo;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.ZhiKuListAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.app.Myapplication;
import com.lsfb.cysj.model.ZhiKuModel;
import com.lsfb.cysj.utils.SharedPrefsUtil;
import com.lsfb.cysj.utils.Show;

/**
 * 聊天室，组管理
 * 
 * @author Administrator
 * 
 */
public class PublicChatRoomsActivity extends BaseChatActivity {
	private ProgressBar pb;
	private TextView title;
	private ListView listView;
	// private ChatRoomsAdapter roomAdapter;
	private ChatRoomsAdapter adapter;

	private List<EMChatRoom> chatRoomList;
	private boolean isLoading;
	private boolean isFirstLoading = true;
	private boolean hasMoreData = true;
	private String cursor;
	private final int pagesize = 20;
	private LinearLayout footLoadingLayout;
	private ProgressBar footLoadingPB;
	private TextView footLoadingText;
	Button btnAdd;

	List<ZhiKuModel> zhikuList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_groups);

		// 搜索框
		pb = (ProgressBar) findViewById(R.id.progressBar);
		listView = (ListView) findViewById(R.id.list);
		title = (TextView) findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.chat_room));
		chatRoomList = new ArrayList<EMChatRoom>();

		// 添加聊天室

		OnClick onClick = new OnClick();
		btnAdd = (Button) findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(onClick);

		View footView = getLayoutInflater().inflate(
				R.layout.listview_footer_view, null);
		footLoadingLayout = (LinearLayout) footView
				.findViewById(R.id.loading_layout);
		footLoadingPB = (ProgressBar) footView.findViewById(R.id.loading_bar);
		footLoadingText = (TextView) footView.findViewById(R.id.loading_text);
		listView.addFooterView(footView, null, false);
		footLoadingLayout.setVisibility(View.GONE);

		// 获取及显示数据
		initData();
		// loadAndShowData();
		// getData();

		/*
		 * EMChatManager.getInstance().addChatRoomChangeListener( new
		 * EMChatRoomChangeListener() {
		 * 
		 * @Override public void onChatRoomDestroyed(String roomId, String
		 * roomName) { chatRoomList.clear(); if (adapter != null) {
		 * runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { if (adapter != null) {
		 * adapter.notifyDataSetChanged(); loadAndShowData(); } }
		 * 
		 * }); } }
		 * 
		 * @Override public void onMemberJoined(String roomId, String
		 * participant) { }
		 * 
		 * @Override public void onMemberExited(String roomId, String roomName,
		 * String participant) {
		 * 
		 * }
		 * 
		 * @Override public void onMemberKicked(String roomId, String roomName,
		 * String participant) { }
		 * 
		 * });
		 */

		// 设置item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final EMChatRoom room = (EMChatRoom) adapter.getItem(position);
				startActivity(new Intent(PublicChatRoomsActivity.this,
						ChatRoomActivity.class).putExtra("chatType", 3)
						.putExtra("groupId", room.getId()));

			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (cursor != null) {
						int lasPos = view.getLastVisiblePosition();
						if (hasMoreData && !isLoading
								&& lasPos == listView.getCount() - 1) {
							loadAndShowData();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

	}

	void initData(){
		zhikuList = Myapplication.zhikuList;
		adapter = new ChatRoomsAdapter(PublicChatRoomsActivity.this, zhikuList);
		listView.setAdapter(adapter);
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter(
				"tid",
				SharedPrefsUtil.getStringValue(getApplicationContext(), "tid",
						"") + "");

		Myapplication.httpUtils.send(HttpMethod.POST, MyUrl.thzulist, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						String list = responseInfo.result;
						try {
							JSONObject object = new JSONObject(list);
							String num = object.getString("num").toString();
							if (num.equals("2")) {
								zhikuList = new ArrayList<ZhiKuModel>();
								ZhiKuModel zhikuModel = new ZhiKuModel();
								JSONArray listZhiku = object
										.getJSONArray("list");
								zhikuList.clear();
								for (int i = 0; i < listZhiku.length(); i++) {
									zhikuModel.id = listZhiku.getJSONObject(i)
											.get("id").toString();
									zhikuModel.memberid = listZhiku
											.getJSONObject(i).get("memberid")
											.toString();
									zhikuModel.name = listZhiku
											.getJSONObject(i).get("name")
											.toString();

									zhikuList.add(zhikuModel);
									// lvZhiKu.setAdapter(new ZhiKuListAdapter(
									// getApplicationContext(), zhikuList));
									Log.d("listZhiku" + i, listZhiku
											.getJSONObject(i).get("name")
											.toString());
								}
								// 设置adapter
								// roomAdapter = new ChatRoomsAdapter(
								// PublicChatRoomsActivity.this, zhikuList);
								// listView.setAdapter(roomAdapter);

							}
							// else if (num.equals("2")) {
							// shenqing();
							// }
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

				});
	}

	private void loadAndShowData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					isLoading = true;
					final EMCursorResult<EMChatRoom> result = EMChatManager
							.getInstance().fetchPublicChatRoomsFromServer(
									pagesize, cursor);
					// 获取group list
					final List<EMChatRoom> chatRooms = result.getData();
					runOnUiThread(new Runnable() {
						public void run() {
							chatRoomList.addAll(chatRooms);
							if (chatRooms.size() != 0) {
								// 获取cursor
								cursor = result.getCursor();
								// if(chatRooms.size() == pagesize)
								// footLoadingLayout.setVisibility(View.VISIBLE);
							}
							if (isFirstLoading) {
								pb.setVisibility(View.INVISIBLE);
								isFirstLoading = false;
								// 设置adapter
								// adapter = new ChatRoomAdapter(
								// PublicChatRoomsActivity.this, 1,
								// chatRoomList);
								// listView.setAdapter(adapter);
							} else {
								if (chatRooms.size() < pagesize) {
									hasMoreData = false;
									footLoadingLayout
											.setVisibility(View.VISIBLE);
									footLoadingPB.setVisibility(View.GONE);
									footLoadingText.setText(getResources()
											.getString(
													R.string.no_more_messages));
								}
								adapter.notifyDataSetChanged();
							}
							isLoading = false;
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							isLoading = false;
							pb.setVisibility(View.INVISIBLE);
							footLoadingLayout.setVisibility(View.GONE);
							Toast.makeText(
									PublicChatRoomsActivity.this,
									getResources().getString(
											R.string.failed_to_load_data), 0)
									.show();
						}
					});
				}
			}
		}).start();
	}

	private class ChatRoomsAdapter extends BaseAdapter {
		List<ZhiKuModel> zhikuList;
		private LayoutInflater inflater;
		Button delRoom;
		Context context;

		public ChatRoomsAdapter(Context context, List<ZhiKuModel> zhikuList) {
			// TODO Auto-generated constructor stub
			this.inflater = LayoutInflater.from(context);
			this.zhikuList = zhikuList;
			this.context = context;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return zhikuList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_group, null);
			}

			final String name = zhikuList.get(position).name;
			// final String name= getItem(position).getName();
			((TextView) convertView.findViewById(R.id.name)).setText(name);

			// 刪除聊天室
			delRoom = (Button) convertView.findViewById(R.id.delRoom);
			delRoom.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(PublicChatRoomsActivity.this)
							.setTitle("确定删除吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											RequestParams params = new RequestParams();
											params.addBodyParameter("uid",
													IsTrue.userId + "");
											params.addBodyParameter(
													"sid",
													SharedPrefsUtil
															.getStringValue(
																	getApplicationContext(),
																	"sid", "")
															+ "");
											params.addBodyParameter("id",
													zhikuList.get(position).id
															+ "");// 组id
											Myapplication.httpUtils
													.send(HttpMethod.POST,
															MyUrl.delZhiKu,
															params,
															new RequestCallBack<String>() {

																@Override
																public void onFailure(
																		HttpException arg0,
																		String arg1) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	Show.toast(
																			getApplicationContext(),
																			"删除失败");
																}

																@Override
																public void onSuccess(
																		ResponseInfo<String> arg0) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	Show.toast(
																			getApplicationContext(),
																			"删除成功");
																	adapter.notifyDataSetChanged();
																}
															});
										}

									}).setNegativeButton("取消", null).show();
				}
			});

			return convertView;
		}

	}

	/**
	 * adapter
	 * 
	 */
	private class ChatRoomAdapter extends ArrayAdapter<EMChatRoom> {

		private LayoutInflater inflater;
		Button delRoom;

		public ChatRoomAdapter(Context context, int res, List<EMChatRoom> rooms) {
			super(context, res, rooms);
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_group, null);
			}
			final String name = getItem(position).getName();
			((TextView) convertView.findViewById(R.id.name)).setText(name);

			// 刪除聊天室
			delRoom = (Button) convertView.findViewById(R.id.delRoom);
			delRoom.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(PublicChatRoomsActivity.this)
							.setTitle("确定删除吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											RequestParams params = new RequestParams();
											params.addBodyParameter("uid",
													IsTrue.userId + "");
											params.addBodyParameter(
													"sid",
													SharedPrefsUtil
															.getStringValue(
																	getApplicationContext(),
																	"sid", "")
															+ "");
											params.addBodyParameter(
													"id",
													SharedPrefsUtil
															.getStringValue(
																	getApplicationContext(),
																	"zid", "")
															+ "");// 组id
											Myapplication.httpUtils
													.send(HttpMethod.POST,
															MyUrl.delZhiKu,
															params,
															new RequestCallBack<String>() {

																@Override
																public void onFailure(
																		HttpException arg0,
																		String arg1) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	Show.toast(
																			getApplicationContext(),
																			"删除失败");
																}

																@Override
																public void onSuccess(
																		ResponseInfo<String> arg0) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	Show.toast(
																			getApplicationContext(),
																			"删除成功");
																	adapter.notifyDataSetChanged();
																}
															});
										}

									}).setNegativeButton("取消", null).show();
				}
			});

			return convertView;
		}
	}

	public void back(View view) {
		finish();
	}

	class OnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_add:
				View view = LayoutInflater.from(PublicChatRoomsActivity.this)
						.inflate(R.layout.add_dir_layout, null);
				EditText etAdd = (EditText) view
						.findViewById(R.id.edit_add_dir);
				final String addName = etAdd.getText().toString();
				new AlertDialog.Builder(PublicChatRoomsActivity.this)
						.setView(view)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										RequestParams params = new RequestParams();
										params.addBodyParameter("uid",
												IsTrue.userId + "");
										params.addBodyParameter(
												"sid",
												SharedPrefsUtil
														.getStringValue(
																getApplicationContext(),
																"sid", "")
														+ "");
										params.addBodyParameter("name", addName
												+ "");
										Myapplication.httpUtils.send(
												HttpMethod.POST,
												MyUrl.addLiaotian, params,
												new RequestCallBack<String>() {

													@Override
													public void onFailure(
															HttpException arg0,
															String arg1) {
														// TODO Auto-generated
														// method stub
														Show.toast(
																getApplicationContext(),
																"添加失败");
													}

													@Override
													public void onSuccess(
															ResponseInfo<String> arg0) {
														// TODO Auto-generated
														// method stub
														Show.toast(
																getApplicationContext(),
																"添加成功");
														adapter.notifyDataSetChanged();
													}
												});
									}
								}).setNegativeButton("取消", null).show();

				break;

			default:
				break;
			}
		}

	}
}
