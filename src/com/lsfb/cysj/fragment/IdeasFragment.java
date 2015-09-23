package com.lsfb.cysj.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.jpush.android.api.e;

import com.alipay.sdk.authjs.c;
import com.dharani.swipegesture.ListAdapter;
import com.dharani.swipegesture.ListViewSwipeGesture;
import com.dharani.swipegesture.dumpclass;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.core.EMConnectionManager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsbf.cysj.R;
import com.lsbf.cysj.R.id;
import com.lsfb.cysj.ChatActivity;
import com.lsfb.cysj.ChatRoomActivity;
import com.lsfb.cysj.HomeActivity;
import com.lsfb.cysj.HotIdeasGamesContentActivity;
import com.lsfb.cysj.HotNews;
import com.lsfb.cysj.IdeasRankingActivity;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.NewsActivity;
import com.lsfb.cysj.SearchActivity;
import com.lsfb.cysj.XiTongMsg;
import com.lsfb.cysj.adapter.ChatAllHistoryAdapter;
import com.lsfb.cysj.adapter.ChatHistoryAdapterw;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.app.Myapplication;
import com.lsfb.cysj.bannerview.AdGalleryHelper;
import com.lsfb.cysj.bannerview.Advertising;
import com.lsfb.cysj.base.MConversationHistory;
import com.lsfb.cysj.utils.SharedPrefsUtil;
import com.lsfb.cysj.utils.Show;
import com.lsfb.cysj.view.CircleImageView;
import com.lsfb.cysj.view.ListViewForScrollView;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.SilderListView;
import com.lsfb.cysj.view.SliderView;
import com.readystatesoftware.viewbadger.BadgeView;

/**
 * 创意信
 * 
 * @author yanwei
 * 
 */
@SuppressLint("ResourceAsColor")
public class IdeasFragment extends Fragment implements OnClickListener {
	private static String[] nums = new String[] { "222", "223", "224", "225",
			"226", "227", "228" };
	private View rootView;
	Intent intent;

	/**
	 * head 自定义圆形头像
	 */
	private CircleImageView head;
	/**
	 * head_num 头像上面的数字
	 */
	private Button head_num;
	/**
	 * head_num2 头像下面的数字
	 */
	private TextView head_num2;
	/**
	 * head_search 搜索框里面的内容
	 */

	/**
	 * img_memdj 等级图片
	 */
	private ImageView img_memdj;

	private TextView head_search;
	/**
	 * search 搜索
	 */
	private RelativeLayout search;
	/**
	 * search_img 搜索图片
	 */
	private ImageView search_img;
	/**
	 * head_page viewpager ����ҳ
	 */
	// private ViewPager head_page;

	/**
	 * ideas_Ranking 创意指数排行
	 */
	private LinearLayout ideas_Ranking;
	/**
	 * ideas_listview 创意下面的消息
	 */
	// public ListViewForScrollView ideas_listview;
	public SilderListView ideas_listview;
	/**
	 * data ���
	 */
	private List<Map<String, Object>> data;
	/**
	 * zhikufragment �ǿ����
	 */
	private ZhikuFragment zhikufragment;

	private ChatHistoryAdapterw chatHistoryAdapterw;

	ScrollView ideas_vertical;
	BaseAdapter adapter;

	RelativeLayout galleryContainer; // 广告业滚动
	LayoutInflater inflater;
	AdGalleryHelper mGalleryHelper;
	HttpUtils httpUtils;
	RequestParams params;
	Dialog jiazaidialog;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	private ImageView img1, img2, img3;
	private TextView title1, title2, title3;
	private TextView text1, text2, text3;
	private TextView time1, time2, time3;
	private RelativeLayout news1;
	private RelativeLayout news2;
	private RelativeLayout news3;
	String sysxx;// 系统消息
	private RelativeLayout mian_ideas_num;
	private ImageView mian_ideas_num2;
	private Button delmsg;
	String url = null;
	String url2 = null;// 热门新闻

	public final List<MConversationHistory> mConversationList = new ArrayList<MConversationHistory>();

	ArrayList<HashMap<String, Object>> listmaps;
	protected static final int MSG_OK = -553;

	private ListView cmn_list_view;
	private ListAdapter listAdapter;
	private ArrayList<dumpclass> listdata;
	/*
	 * 获取环信聊天记录
	 */
	// ChatAllHistoryAdapter chatHistoryAdapter;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OK:
				adapter();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_ideas, container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.mian_ideas, container, false);

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
		// showdialogup();
		date();
		// adapter();
		return rootView;
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(getActivity(), R.style.MyDialog,
				"正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 获取环信会话记录
	 */
	void getHxHuihua() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		// ideas_listview = (SilderListView) getView().findViewById(R.id.list);

		ideas_listview = (SilderListView) getView().findViewById(
				R.id.ideas_listview);
		getHuanxinInfo(conversationList);

		// chatHistoryAdapter = new ChatAllHistoryAdapter(getActivity(), 1,
		// conversationList, list);
		// // 设置adapter
		//
		// ideas_listview.setAdapter(chatHistoryAdapter);
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation

		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0
						&& conversation.getType() != EMConversationType.ChatRoom) {
					// if(conversation.getType() !=
					// EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
					// }
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;
						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}

				});
	}

	/**
	 * 系统消息，聊天会话消息
	 */
	private void adapter() {
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getActivity()).inflate(
							R.layout.ideas_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.ideas_list_head);
					holder.title = (TextView) view
							.findViewById(R.id.ideas_list_msg);
					holder.num = (TextView) view
							.findViewById(R.id.ideas_list_content);// 消息内容
					holder.time = (TextView) view
							.findViewById(R.id.ideas_list_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				holder.num.setText(listmaps.get(position).get("newmsg")
						.toString());
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (IsTrue.userId == 0) {

						} else {
							intent = new Intent(getActivity(),
									NewsActivity.class);
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
				// if (IsTrue.userId == 0)
				if (null != listmaps)
					return listmaps.size();
				else
					return 0;
				// return nums.length;
			}
		};

		// ideas_listview.setAdapter(adapter);
	}

	private void date() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		httpUtils.send(HttpMethod.POST, MyUrl.index, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						if (null != jiazaidialog)
							jiazaidialog.dismiss();
						Toast.makeText(getActivity(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null != jiazaidialog)
							jiazaidialog.dismiss();
						String list = responseInfo.result;
						System.out.println(list + "OOOOOOOOOOOOOOO");
						try {
							JSONObject object = new JSONObject(list);
							String banner = object.getString("banner");
							JSONArray array = new JSONArray(banner);
							for (int i = 0; i < array.length(); i++) {
								JSONObject object2 = (JSONObject) array.get(i);
								map = new HashMap<String, Object>();
								map.put("title", object2.getString("title")
										.toString());
								map.put("image", object2.getString("image")
										.toString());
								listmap.add(map);
							}
							String news2 = object.getString("news2").toString();
							news2(news2);// 佛学新闻
							String news1 = object.getString("news1").toString();
							news1(news1);// 热门新闻
							String sys = object.getString("sys").toString();
							if (sys.equals("0")) {
								// 不在登录状态
								news3.setVisibility(View.GONE);
							} else { // 登录状态， 有系统消息
								news3.setVisibility(View.VISIBLE);
								getHxHuihua();
								// sysxx = object.getString("sysxx").toString();
								// systemmsg(sysxx,sys);
							}
							if (IsTrue.userId == 0) {
							} else {

								String userindex = object
										.getString("userindex").toString();// 返回+1
								Log.d("userindex", IsTrue.intZqyz + "");

								img_memdj.setVisibility(View.VISIBLE);
								Log.d("zgscwjm", "test");
								if (IsTrue.intZqyz == 2) {
									img_memdj
											.setBackgroundResource(R.drawable.img_qiye);
								} else {
									switch (IsTrue.intDengji) {
									case 1:
										img_memdj
												.setBackgroundResource(R.drawable.z1);
										break;
									case 2:
										img_memdj
												.setBackgroundResource(R.drawable.z2);
										break;
									case 3:
										img_memdj
												.setBackgroundResource(R.drawable.z3);
										break;
									case 4:
										img_memdj
												.setBackgroundResource(R.drawable.z4);
										Log.i("zgscwjm", "img_memdj");
										break;
									}
								}

								SharedPrefsUtil.putValue(Myapplication.context,
										"todayIndex", userindex);

								int a = Integer.parseInt(userindex.substring(1));
								if (a < 0) {

									head_num.setVisibility(View.VISIBLE);
									head_num.setText(userindex);
								} else if (a == 0) {
									head_num.setVisibility(View.GONE);
								} else if (a > 0) {
									head_num.setVisibility(View.VISIBLE);
									head_num.setText(userindex);// "+" +
								}
								// if (userindex.equals("0")) {
								// head_num.setVisibility(View.GONE);
								// }else {
								// head_num.setVisibility(View.VISIBLE);
								// head_num.setText("+"+userindex);
								// }
								String userimage = object
										.getString("userimage").toString();
								BitmapUtils bitmapUtils = new BitmapUtils(
										getActivity());
								bitmapUtils.display(head,
										ImageAddress.Stringhead + userimage);
								String usercountindex = object.getString(
										"usercountindex").toString();
								if (usercountindex.equals("0")) {
									head_num2.setVisibility(View.GONE);
								} else {
									head_num2.setVisibility(View.VISIBLE);
									head_num2.setText(usercountindex);
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Banner();
					}

				});
	}

	/**
	 * 显示消息，登录状态下
	 * 
	 * @param sysxx
	 * @param num
	 */
	protected void systemmsg(String sysxx, String num) {
		System.out.println(sysxx + ">>>>>>>>>>>>>>>>>>>>");
		listmaps = new ArrayList<HashMap<String, Object>>();
		listmaps.clear();
		listmaps.removeAll(listmaps);
		try {
			JSONArray array = new JSONArray(sysxx);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("time", object.getString("time").toString());
				map.put("newmsg", object.getString("newmsg").toString());
				listmaps.add(map);
			}
			handler.sendEmptyMessage(MSG_OK);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		BadgeView badgeView = new BadgeView(getActivity(), img3);
		badgeView.setText(num);
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView.show();
		time3.setText(listmaps.get(0).get("time").toString());
	}

	protected void news1(String news1) {
		try {
			JSONObject object = new JSONObject(news1);
			String classnews = object.getString("class").toString();
			String title = object.getString("title").toString();
			String time = object.getString("time").toString();
			url2 = object.getString("url").toString();
			text1.setText(title);
			time1.setText(time);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void news2(String news2) {
		try {
			JSONObject object = new JSONObject(news2);
			String classnews = object.getString("class").toString();
			String title = object.getString("title").toString();
			String time = object.getString("time").toString();
			url = object.getString("url").toString();
			text2.setText(title);
			time2.setText(time);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void Banner() {
		// ============初始化缓存路径，以及AfinalBitmap的初始化，可以忽略
		String cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File f = getActivity().getExternalCacheDir();
			if (null == f) {

				cacheDir = Environment.getExternalStorageDirectory().getPath()
						+ File.separator + getActivity().getPackageName()
						+ File.separator + "cache";
			} else {
				cacheDir = f.getPath();
			}
		} else {
			File f = getActivity().getCacheDir();
			cacheDir = f.getPath();
		}
		FinalBitmap.create(getActivity(), cacheDir + File.separator + "images"
				+ File.separator, 0.3f, 1024 * 1024 * 100, 10);
		
		Advertising[] adsss = new Advertising[listmap.size()];
		for (int i = 0; i < listmap.size(); i++) {
			Advertising advertising = new Advertising(ImageAddress.Banner
					+ listmap.get(i).get("image"), "", listmap.get(i)
					.get("title").toString());
			adsss[i] = advertising;
		}
		// 将AdGalleryHelper添加到布局文件中
		galleryContainer = (RelativeLayout) rootView
				.findViewById(R.id.mian_ideas_head);
		mGalleryHelper = new AdGalleryHelper(getActivity(), adsss, 5000);
		galleryContainer.addView(mGalleryHelper.getLayout());

		// /将AdGalleryHelper添加到布局文件中
		mGalleryHelper.startAutoSwitch();
	}

	static class ViewHolder {
		ImageView img;
		TextView title;
		TextView num;
		TextView time;
	}

	/**
	 * init
	 */
	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		head = (CircleImageView) rootView.findViewById(R.id.head);
		head.setOnClickListener(this);
		head_num = (Button) rootView.findViewById(R.id.head_num);
		head_num2 = (TextView) rootView.findViewById(R.id.head_num2);
		head_search = (TextView) rootView.findViewById(R.id.head_search);
		head_search.setOnClickListener(this);

		img_memdj = (ImageView) rootView.findViewById(R.id.img_memdj);

		search = (RelativeLayout) rootView.findViewById(R.id.search);
		search.setOnClickListener(this);
		search_img = (ImageView) rootView.findViewById(R.id.search_img);

		ideas_listview = (SilderListView) rootView
				.findViewById(R.id.ideas_listview);

		final String st2 = getResources().getString(
				R.string.Cant_chat_with_yourself);

		ideas_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				EMConversation conversation = conversationList.get(position);
				// EMConversation conversation = (EMConversation)
				// chatHistoryAdapterw.getItem(position);
				// MConversationHistory
				// mConversationHistory=(MConversationHistory)
				// chatHistoryAdapterw.getItem(position);
				String username = conversation.getUserName();

				// if
				// (username.equals(DemoApplication.getInstance().getUserName()))
				// Toast.makeText(getActivity(), st2, 0).show();
				// else {
				// 进入聊天页面
				// Intent intent = new Intent(getActivity(),
				// ChatActivity.class);
				if (conversation.getType() == EMConversationType.Chat) {

					// Intent intent = new Intent(getActivity(),
					// ChatRoomActivity.class);
					//
					//
					// intent.putExtra("groupId", username);
					// startActivity(intent);

					Intent intent = new Intent(getActivity(),
							NewsActivity.class);
					intent.putExtra("id", mConversationList.get(position)
							.getSid());
					intent.putExtra("headaddress",
							mConversationList.get(position).getImg());
					startActivity(intent);

				} else {
					
					// it is group chat
					
					AsyncHttpClient client = new AsyncHttpClient();
					com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
					params.put("uid", IsTrue.userId);
					params.put("sid", mConversationList.get(position).getSid());
					final String sid=mConversationList.get(position).getSid();
					client.post(MyUrl.bsinger, params,
							new JsonHttpResponseHandler() {

								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									 final String huanxname;
										
										final String huanxlistnum;
										final String zhiklistnum;
										final String zuop;
										final String clasid;
										final String countmoney;
										final String groupId;
									try {
										String num = response.getString("num");
										int i = Integer.parseInt(num);
										
										 if (i == 2) {
											
											huanxname = response
													.getString("huanxname");
											countmoney = response
													.getString("countmoney");
											Log.i("zgscwjm", "countmoney:"
													+ countmoney);
											String list = response
													.getString("list");


											JSONObject object = new JSONObject(
													list);

											clasid = object.getString("class");
											groupId = object.getString("huanxqun").toString();// 环信群
											zuop = object.getString("zuop").toString();// 参赛作品数量
											huanxlistnum = object.getJSONArray(
													"huanxlist").length()
													+ "";
											zhiklistnum = object.getJSONArray(
													"zhiklist").length()
													+ "";
											Intent intent = new Intent(getActivity(),
													ChatActivity.class);
											intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
											intent.putExtra("groupId", groupId);

											intent.putExtra("name", huanxname);
											intent.putExtra("sid", sid);
											intent.putExtra("huanxlistnum", huanxlistnum);
											intent.putExtra("zhiklist", zhiklistnum);
											intent.putExtra("zuop", zuop);
											intent.putExtra("clasid", clasid);
											intent.putExtra("countmoney", countmoney);
											
											startActivity(intent);	

											}
										
									} catch (JSONException e) {
										e.printStackTrace();
									}
									super.onSuccess(statusCode, headers,
											response);
								}

								
							});

				

					// intent.putExtra("groupId", groupId);
					// intent.putExtra("name", huanxname);
					// intent.putExtra("sid", sid);
					// intent.putExtra("huanxlistnum", huanxlistnum);
					// intent.putExtra("zhiklist", zhiklistnum);
					// intent.putExtra("zuop", zuop);
					// intent.putExtra("clasid", clasid);
					// intent.putExtra("countmoney", countmoney);
					//

				
				}

				// }

				// else {
				// Intent intent = new Intent(getActivity(),
				// ChatActivity.class);
				// // it is single chat
				// intent.putExtra("userId", username);
				// Log.d("groupId:chat", "" + username);
				// startActivity(intent);
				// }

			}
			// }
		});

		img1 = (ImageView) rootView.findViewById(R.id.mian_ideas_img1);
		title1 = (TextView) rootView.findViewById(R.id.mian_ideas_title1);
		text1 = (TextView) rootView.findViewById(R.id.mian_ideas_text1);
		time1 = (TextView) rootView.findViewById(R.id.mian_ideas_time1);
		img2 = (ImageView) rootView.findViewById(R.id.mian_ideas_img2);
		title2 = (TextView) rootView.findViewById(R.id.mian_ideas_title2);
		text2 = (TextView) rootView.findViewById(R.id.mian_ideas_text2);
		time2 = (TextView) rootView.findViewById(R.id.mian_ideas_time2);

		// img3 = (ImageView) rootView.findViewById(R.id.mian_ideas_img3);
		// mian_ideas_num = (RelativeLayout) rootView
		// .findViewById(R.id.mian_ideas_num);
		// mian_ideas_num2 = (ImageView) rootView
		// .findViewById(R.id.mian_ideas_num2);
		// title3 = (TextView) rootView.findViewById(R.id.mian_ideas_title3);
		// text3 = (TextView) rootView.findViewById(R.id.mian_ideas_text3);
		// time3 = (TextView) rootView.findViewById(R.id.mian_ideas_time3);

		// 换为右滑删除
		cmn_list_view = (ListView) rootView.findViewById(R.id.cmn_list_view);
		listdata = new ArrayList<dumpclass>();
		InitializeValues();
		final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(
				cmn_list_view, swipeListener, getActivity());
		touchListener.SwipeType = ListViewSwipeGesture.Double; // Set two
																// options at
																// background of
																// list item

		cmn_list_view.setOnTouchListener(touchListener);

		news1 = (RelativeLayout) rootView.findViewById(R.id.mian_ideas_news1);
		news2 = (RelativeLayout) rootView.findViewById(R.id.mian_ideas_news2);
		news3 = (RelativeLayout) rootView.findViewById(R.id.mian_ideas_news3);
		news1.setOnClickListener(this);
		news2.setOnClickListener(this);
		news3.setOnClickListener(this);
		// head_page = (ViewPager) rootView.findViewById(R.id.head_page);
		// head_page.setOnClickListener(this);

		// mian_ideas_head = (RelativeLayout) rootView
		// .findViewById(R.id.mian_ideas_head);
		// mian_ideas_head.setOnClickListener(this);

		// img = (ViewFlow) rootView.findViewById(R.id.mian_ideas_img);
		// img.setOnClickListener(this);
		// guanggaotext = (TextView)
		// rootView.findViewById(R.id.mian_ideas_text);
		// guanggaotext.setOnClickListener(this);
		// dian = (CircleFlowIndicator) rootView
		// .findViewById(R.id.mian_ideas_dian);
		// dian.setOnClickListener(this);

		ideas_Ranking = (LinearLayout) rootView
				.findViewById(R.id.ideas_Ranking);
		ideas_Ranking.setOnClickListener(this);
		zhikufragment = (ZhikuFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.zhikufragment);
		zhikufragment.data();
		if (IsTrue.userId == 0) {
		} else {
			zhikufragment.hengdata();
		}
		// 不让listview加载在最上边
		ideas_vertical = (ScrollView) rootView
				.findViewById(R.id.ideas_vertical);
		ideas_vertical.smoothScrollTo(0, 0);
		// delmsg = (Button) rootView.findViewById(R.id.delmsg);
		// delmsg.setOnClickListener(this);
	}

	private void InitializeValues() {
		// TODO Auto-generated method stub
		listdata.add(new dumpclass("  众创创"));
		/*
		 * listdata.add(new dumpclass("two")); listdata.add(new
		 * dumpclass("three")); listdata.add(new dumpclass("four"));
		 * listdata.add(new dumpclass("five")); listdata.add(new
		 * dumpclass("six")); listdata.add(new dumpclass("seven"));
		 * listdata.add(new dumpclass("Eight"));
		 */

		listAdapter = new ListAdapter(getActivity(), listdata);
		cmn_list_view.setAdapter(listAdapter);
	}

	ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void FullSwipeListView(int position) {
			// TODO Auto-generated method stub
			// Toast.makeText(getActivity(),"Action_2",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void HalfSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "Action_1", Toast.LENGTH_SHORT)
					.show();

			showdialogup();
			deletemag();
		}

		@Override
		public void LoadDataForScroll(int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
			for (int i : reverseSortedPositions) {
				listdata.remove(i);
				listAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub
			intent = new Intent(getActivity(), XiTongMsg.class);
			// intent.putExtra("sysxx", sysxx);
			startActivity(intent);
			// startActivity(new
			// Intent(getApplicationContext(),TestActivity.class));
		}

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mGalleryHelper)
			mGalleryHelper.stopAutoSwitch();
	}

	@Override
	public void onResume() {
		ideas_vertical.smoothScrollTo(0, 0);
		super.onResume();
	}

	// private void initBanner(ArrayList<String> imageUrlList) {
	//
	// mViewFlow.setAdapter(new ImagePagerAdapter(this, imageUrlList,
	// linkUrlArray, titleList).setInfiniteLoop(true));
	// mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
	// // 我的ImageAdapter实际图片张数为3
	//
	// mViewFlow.setFlowIndicator(mFlowIndicator);
	// mViewFlow.setTimeSpan(4500);
	// mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
	// mViewFlow.startAutoFlowTimer(); // 启动自动播放
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_search:// 搜索
			intent = new Intent(getActivity(), SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.ideas_Ranking:// 创意指数排行
			if (IsTrue.userId == 0) {
				intent = new Intent(getActivity(), HomeActivity.class);
				IsTrue.zhishu = 1;
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), IdeasRankingActivity.class);
				startActivity(intent);
			}
			break;
		// case R.id.mian_ideas_head://创意世界大赛
		// intent = new Intent(getActivity(), IdeasWorldGameActivity.class);
		// startActivity(intent);
		// break;
		case R.id.head:// 我的，左上角头像
			// if (IsTrue.userId == 0) {
			// // Show.toast(getActivity(), "请登录后再试");
			// HomeActivity.viewPager.setCurrentItem(3);
			// // FragmentTransaction ft =
			// // getFragmentManager().beginTransaction();
			// // ft.replace(IdeasFragment.this, new LoginFragment());
			// // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			// // ft.commit();// 提交
			// return;
			// }
			HomeActivity.viewPager.setCurrentItem(3);
			// intent = new Intent(getActivity(), MyDetailsActivity.class);
			// startActivity(intent);
			break;
		case R.id.mian_ideas_news1:// 热门新闻
			intent = new Intent(getActivity(), HotNews.class);
			intent.putExtra("class", 1 + "");
			intent.putExtra("url2", url2);
			startActivity(intent);
			break;
		case R.id.mian_ideas_news2:// 佛学新闻
			intent = new Intent(getActivity(), HotNews.class);
			intent.putExtra("class", 2 + "");
			intent.putExtra("url", url);
			startActivity(intent);
			break;
		case R.id.mian_ideas_news3:// 众创创
			intent = new Intent(getActivity(), XiTongMsg.class);
			// intent.putExtra("sysxx", sysxx);
			startActivity(intent);
			break;
		// case R.id.delmsg:// 删除系统消息
		// showdialogup();
		// deletemag();
		// break;
		default:
			break;
		}

	}

	private void deletemag() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		httpUtils.send(HttpMethod.POST, MyUrl.messagedel, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getActivity(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						jiazaidialog.dismiss();
						String list = responseInfo.result;
						try {
							JSONObject object = new JSONObject(list);
							String num = object.getString("num").toString();
							if (num.equals("1")) {

							} else if (num.equals("2")) {
								news3.setVisibility(View.GONE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		// if (chatHistoryAdapter != null)
		// chatHistoryAdapter.notifyDataSetChanged();
	}

	public List<MConversationHistory> getHuanxinInfo(
			final List<EMConversation> conversationList) {
		String strParm = "";

		for (EMConversation emConversation : conversationList) {

			Log.d("zgscwjm--hx", emConversation.getType() + "");
			if (emConversation.getType() == EMConversationType.Chat) {
				strParm = strParm + "d="
						+ emConversation.getUserName().substring(4) + "-";
			} else if (EMConversationType.GroupChat == emConversation.getType()) {

				strParm += "q=" + emConversation.getUserName() + "-";
			}
		}

		Log.i("zgscwjm--hx", strParm);
		// final List<MConversationHistory> mConversationList = new
		// ArrayList<MConversationHistory>();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("allID", strParm);
		httpUtils.send(HttpMethod.POST, MyUrl.indexhuanxin, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						try {

							// ideas_listview.setAdapter(null);

							mConversationList.clear();

							Log.i("zgscwjm--hx", arg0.result);

							JSONArray object = new JSONArray(arg0.result);
							Log.i("zgscwjm", "json leng" + object.length());
							for (int i = 0; i < object.length(); i++) {

								MConversationHistory conversationHistory = new MConversationHistory();
								JSONObject con = object.getJSONObject(i);
								conversationHistory.setSid(con.getString("cid"));
								conversationHistory.setImg(con.getString("img"));
								conversationHistory.setHuanxname(con
										.getString("title"));
								conversationHistory.setType(con.getInt("type"));
								Log.d("zgscwjm", "xx");
								mConversationList.add(conversationHistory);
								// handler.sendEmptyMessage(100001);
							}
							// chatHistoryAdapter = new
							// ChatAllHistoryAdapter(getActivity(), 1,
							// conversationList, mConversationList,new
							// BitmapUtils(IdeasFragment.this.getActivity().getApplicationContext()));
							// 设置adapter

							//
							ChatHistoryAdapterw chatHistoryAdapterw = new ChatHistoryAdapterw(
									getActivity(), conversationList,
									mConversationList, new BitmapUtils(
											IdeasFragment.this.getActivity()
													.getApplicationContext()));
							ideas_listview.setAdapter(chatHistoryAdapterw);

							// ideas_listview.setAdapter(chatHistoryAdapter);

							// ideas_listview.setAdapter(chatHistoryAdapter);
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("fuck", Log.getStackTraceString(e));
						}

					}
				});

		Log.d("zgscwjm", "list" + mConversationList.size());
		return mConversationList;
	}

}
