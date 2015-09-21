package com.lsfb.cysj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage.ChatType;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.MessageAdapter;
import com.lsfb.cysj.adapter.ZhiKuListAdapter;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.app.Myapplication;
import com.lsfb.cysj.model.ZhiKuModel;
import com.lsfb.cysj.utils.SharedPrefsUtil;
import com.lsfb.cysj.view.ResDialog;

/**
 * 
 * 创意世界智库.详情,创意世界 进入页面，群组管理，大厅聊天室进入界面,改为在该界面聊天
 * 
 * @author
 * 
 */
public class IdeasWorldZhiKuDetails extends FragmentActivity implements
		OnClickListener {
	Intent intent;
	/**
	 * ideas_world_zhiku_details_result result 创意世界优秀结果
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_result)
	private Button result;
	/**
	 * ideas_world_zhiku_details_heritage heritage 创意世界优秀遗产
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_heritage)
	private Button heritage;
	/**
	 * city_details_linearlayout1_img6 more1 更多1
	 */
	@ViewInject(R.id.city_details_linearlayout1_img6)
	private RelativeLayout more1;
	/**
	 * city_details_linearlayout2_img6 more2 更多2
	 */
	@ViewInject(R.id.city_details_linearlayout2_img6)
	private RelativeLayout more2;
	/**
	 * city_details_linearlayout3_img6 more3 更多3
	 */
	@ViewInject(R.id.city_details_linearlayout3_img6)
	private RelativeLayout more3;
	/**
	 * ideas_world_zhiku_details_shezhi shezhi 设置
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_shezhi)
	private LinearLayout shezhi;
	/**
	 * ideas_world_zhiku_details_dating dating 大厅 dating
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_dating)
	private LinearLayout dating;
	@ViewInject(R.id.ideas_world_zhiku_details_back)
	private LinearLayout back;
	/**
	 * city_details_linearlayout_img1 主席团图表一
	 */
	@ViewInject(R.id.city_details_linearlayout_img1)
	private ImageView city_details_linearlayout_img1;
	/**
	 * city_details_linearlayout_img2 主席团图表2
	 */
	@ViewInject(R.id.city_details_linearlayout_img2)
	private ImageView city_details_linearlayout_img2;
	/**
	 * city_details_linearlayout_img3 主席团图表3
	 */
	@ViewInject(R.id.city_details_linearlayout_img3)
	private ImageView city_details_linearlayout_img3;
	/**
	 * city_details_linearlayout_img4 主席团图表4
	 */
	@ViewInject(R.id.city_details_linearlayout_img4)
	private ImageView city_details_linearlayout_img4;
	/**
	 * city_details_linearlayout_img5 主席团图表5
	 */
	@ViewInject(R.id.city_details_linearlayout_img5)
	private ImageView city_details_linearlayout_img5;
	/**
	 * city_details_linearlayout2_img1 理事会图标一
	 */
	@ViewInject(R.id.city_details_linearlayout2_img1)
	private ImageView city_details_linearlayout2_img1;
	/**
	 * city_details_linearlayout2_img2理事会图标2
	 */
	@ViewInject(R.id.city_details_linearlayout2_img2)
	private ImageView city_details_linearlayout2_img2;
	/**
	 * city_details_linearlayout2_img3 理事会图标3
	 */
	@ViewInject(R.id.city_details_linearlayout2_img3)
	private ImageView city_details_linearlayout2_img3;
	/**
	 * city_details_linearlayout2_img4 理事会图标4
	 */
	@ViewInject(R.id.city_details_linearlayout2_img4)
	private ImageView city_details_linearlayout2_img4;
	/**
	 * city_details_linearlayout2_img5 理事会图标5
	 */
	@ViewInject(R.id.city_details_linearlayout2_img5)
	private ImageView city_details_linearlayout2_img5;
	/**
	 * city_details_linearlayout3_img1 专家顾问团图标一
	 */
	@ViewInject(R.id.city_details_linearlayout3_img1)
	private ImageView city_details_linearlayout3_img1;
	/**
	 * city_details_linearlayout3_img2 专家顾问团图标2
	 */
	@ViewInject(R.id.city_details_linearlayout3_img2)
	private ImageView city_details_linearlayout3_img2;
	/**
	 * city_details_linearlayout3_img3专家顾问团图标3
	 */
	@ViewInject(R.id.city_details_linearlayout3_img3)
	private ImageView city_details_linearlayout3_img3;
	/**
	 * city_details_linearlayout3_img4专家顾问团图标4
	 */
	@ViewInject(R.id.city_details_linearlayout3_img4)
	private ImageView city_details_linearlayout3_img4;
	/**
	 * city_details_linearlayout3_img5 专家顾问团图标5
	 */
	@ViewInject(R.id.city_details_linearlayout3_img5)
	private ImageView city_details_linearlayout3_img5;

	/**
	 * ideas_world_zhiku_details_scrollview 显示最上面
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_scrollview)
	private ScrollView ideas_world_zhiku_details_scrollview;
	/**
	 * ideas_world_zhiku_details_apply addzhiku 申请加入智库
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_apply)
	private Button addzhiku;

	/**
	 * ideas_world_zhiku_details_apply 推荐加入好友
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_recommend)
	private Button addTuijian;

	/**
	 * zhuangtai 弹窗
	 */
	@ViewInject(R.id.zhuangtai)
	private LinearLayout zhuangtai;
	@ViewInject(R.id.ideas_world_zhiku_details_city)
	private TextView city;
	PopupWindow popupwindow;
	HttpUtils httpUtils;
	RequestParams params;
	String tid;// 智库id
	Dialog jiazaidialog;
	BitmapUtils bitmapUtils;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	ArrayList<HashMap<String, Object>> listimg1;
	ArrayList<HashMap<String, Object>> listimg2;
	ArrayList<HashMap<String, Object>> listimg3;
	ArrayList<HashMap<String, Object>> popview;
	private LinearLayout zhikuguanli;// 大厅弹窗，展示聊天室的视图
	List<ZhiKuModel> zhikuList;
	ListView lvZhiKu;

	private InputMethodManager manager;

	/**
	 * chat_send 发送消息
	 */
	@ViewInject(R.id.chat_send)
	private Button chat_send;

	/**
	 * chat_text 聊天文字
	 */
	@ViewInject(R.id.chat_text)
	private EditText chat_text;

	@ViewInject(R.id.chatGroupName)
	private TextView chatGroupName;

	private MessageAdapter adapter;
	String groupId;// 群id

	@ViewInject(R.id.chat_list)
	private ListView chat_list;

	private int chatType;
	public static final int CHATTYPE_GROUP = 3;// 為聊天室
	
	public static final int NO_CHAT = 2001;// 不是会员不能聊天
	

	private final int pagesize = 20;

	// 给谁发送消息
	private EMConversation conversation;
	NewMessageBroadcastReceiver msgReceiver;
	public static final int REFRESH_CHAT = 101;

	ZhiKuModel zhikuModel;

	@SuppressLint("HandlerLeak")
	public Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 改变聊天室id
			case REFRESH_CHAT:
				Log.d("groupId", "" + groupId);
				conversation = EMChatManager.getInstance().getConversation(
						groupId);
				adapter = new MessageAdapter(IdeasWorldZhiKuDetails.this,
						groupId, CHATTYPE_GROUP);
				chat_list.setAdapter(adapter);
				adapter.refreshSelectLast();
				break;
				
			case NO_CHAT:
				chat_send.setVisibility(View.GONE);
				chat_text.setText("本智库成员才能发言");		
				chat_text.setEnabled(false);
				
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideas_world_zhiku_details);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ViewUtils.inject(this);
		init();
		data();

		// 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		EMChat.getInstance().setAppInited();
		init();
		// 获取聊天室id,显示聊天室内容
		// if (zhikuList != null && zhikuList.size() != 0) {
		// groupId = zhikuList.get(0).id;
		// conversation = EMChatManager.getInstance().getConversation(groupId);
		// adapter = new MessageAdapter(IdeasWorldZhiKuDetails.this, groupId,
		// CHATTYPE_GROUP);
		// chat_list.setAdapter(adapter);
		// adapter.refreshSelectLast();
		// }
	}

	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter("tid", tid);
		httpUtils.send(HttpMethod.POST, MyUrl.thsinger, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String list = responseInfo.result;
						jiazaidialog.dismiss();
						System.out.println("MMM:"+list);
						try {
							JSONObject object = new JSONObject(list);
							System.out.println(object + "SSS");
							String name = object.getString("name").toString();
							city.setText(name);
							String zhuxi = object.getString("zhuxi").toString();
							String sid = object.getString("zid").toString();

							SharedPrefsUtil.putValue(getApplicationContext(),
									"sid", sid);
							SharedPrefsUtil.putValue(getApplicationContext(),
									"zid", sid);

							if (zhuxi.equals("0")) {
							} else if (zhuxi.equals("1")) {
								zhuxituan(object);
							}
							String lsh = object.getString("lsh").toString();
							if (lsh.equals("0")) {
							} else if (lsh.equals("1")) {
								lishihui(object);
							}
							String gwt = object.getString("gwt").toString();
							if (gwt.equals("0")) {
							} else if (gwt.equals("1")) {
								Log.d("zhuanjiatuan", "zhuanjiatuan");
								zhuanjiatuan(object);
							}
							String zkzb = object.getString("zkzb1").toString();
							int users = object.getInt("users");//是会员才可以聊天							
							if(2!=users){								
								hand.sendEmptyMessage(NO_CHAT);
							}							
							
							if (zkzb.equals("0")) {
								Log.d("0", "0");
							} else {
								Log.d("1", "1");
								popview(object);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void zhuanjiatuan(JSONObject object) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			String gwtlist = object.getString("gwtlist").toString();
			JSONArray array = new JSONArray(gwtlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object2.getString("uid").toString());
				map.put("image", object2.getString("image").toString());
				listmap.add(map);
			}
			listimg3 = listmap;
			bitmapUtils = new BitmapUtils(getApplicationContext());
			switch (listmap.size()) {
			case 1:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				break;
			case 2:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				break;
			case 3:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout3_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				break;
			case 4:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout3_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout3_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				break;
			case 5:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout3_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout3_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				city_details_linearlayout3_img5.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img5,
						ImageAddress.Stringhead
								+ listmap.get(4).get("image").toString());
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void lishihui(JSONObject object) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			String lshlist = object.getString("lshlist").toString();
			JSONArray array = new JSONArray(lshlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object2.getString("uid").toString());
				map.put("image", object2.getString("image").toString());
				listmap.add(map);
			}
			listimg2 = listmap;
			bitmapUtils = new BitmapUtils(getApplicationContext());
			switch (listmap.size()) {
			case 1:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				break;
			case 2:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				break;
			case 3:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout2_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				break;
			case 4:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout2_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout2_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				break;
			case 5:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout2_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout2_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				city_details_linearlayout2_img5.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img5,
						ImageAddress.Stringhead
								+ listmap.get(4).get("image").toString());
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void zhuxituan(JSONObject object) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			String zxlist = object.getString("zxlist").toString();
			JSONArray array = new JSONArray(zxlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object2.getString("uid").toString());
				map.put("image", object2.getString("image").toString());
				listmap.add(map);
			}
			listimg1 = listmap;
			bitmapUtils = new BitmapUtils(getApplicationContext());
			switch (listmap.size()) {
			case 1:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				break;
			case 2:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				break;
			case 3:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				break;
			case 4:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				break;
			case 5:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				city_details_linearlayout_img5.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img5,
						ImageAddress.Stringhead
								+ listmap.get(4).get("image").toString());
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void showdialogup() {
		jiazaidialog = new ResDialog(IdeasWorldZhiKuDetails.this,
				R.style.MyDialog, "正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		listimg1 = new ArrayList<HashMap<String, Object>>();
		listimg2 = new ArrayList<HashMap<String, Object>>();
		listimg3 = new ArrayList<HashMap<String, Object>>();
		popview = new ArrayList<HashMap<String, Object>>();

		chat_send.setOnClickListener(this);
		result.setOnClickListener(this);
		heritage.setOnClickListener(this);
		more1.setOnClickListener(this);
		more2.setOnClickListener(this);
		more3.setOnClickListener(this);
		shezhi.setOnClickListener(this);
		dating.setOnClickListener(this);
		addzhiku.setOnClickListener(this);
		addTuijian.setOnClickListener(this);
		back.setOnClickListener(this);
		city_details_linearlayout_img1.setOnClickListener(this);
		city_details_linearlayout_img2.setOnClickListener(this);
		city_details_linearlayout_img3.setOnClickListener(this);
		city_details_linearlayout_img4.setOnClickListener(this);
		city_details_linearlayout_img5.setOnClickListener(this);
		city_details_linearlayout2_img1.setOnClickListener(this);
		city_details_linearlayout2_img2.setOnClickListener(this);
		city_details_linearlayout2_img3.setOnClickListener(this);
		city_details_linearlayout2_img4.setOnClickListener(this);
		city_details_linearlayout2_img5.setOnClickListener(this);
		city_details_linearlayout3_img1.setOnClickListener(this);
		city_details_linearlayout3_img2.setOnClickListener(this);
		city_details_linearlayout3_img3.setOnClickListener(this);
		city_details_linearlayout3_img4.setOnClickListener(this);
		city_details_linearlayout3_img5.setOnClickListener(this);
		intent = getIntent();
		tid = intent.getExtras().getString("zhikuid").toString();
		SharedPrefsUtil.putValue(IdeasWorldZhiKuDetails.this, "tid", tid);
		zhikuguanli = (LinearLayout) LayoutInflater.from(
				getApplicationContext()).inflate(R.layout.popuptwindow, null);
	}

	@Override
	protected void onResume() {
		ideas_world_zhiku_details_scrollview.smoothScrollTo(0, 0);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		String uid = null;
		switch (v.getId()) {
		case R.id.chat_send:// 发送消息,确定
			Log.d("send", "send");
			String s = chat_text.getText().toString();
			sendText(s);
			break;
		case R.id.ideas_world_zhiku_details_back:
			finish();
			break;
		case R.id.ideas_world_zhiku_details_result:// 创意世界优秀结果
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					BestResultsActivity.class);
			intent.putExtra("tid", tid);
			startActivity(intent);
			break;
		case R.id.ideas_world_zhiku_details_heritage:// 创意世界优秀遗产
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					WorldHeritageActivity.class);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout1_img6:// 更多1
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					IdeasZhiKuMan.class);
			intent.putExtra("zhikuid", tid);
			startActivity(intent);
			break;
		case R.id.ideas_world_zhiku_details_shezhi:// 设置
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					IdeasWorldZhiKuDetailsSheZhi.class);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img1:// 其他人的信息
			uid = listimg1.get(0).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img2:// 其他人的信息
			uid = listimg1.get(1).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img3:// 其他人的信息
			uid = listimg1.get(2).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img4:// 其他人的信息
			uid = listimg1.get(3).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img5:// 其他人的信息
			uid = listimg1.get(4).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img1:// 其他人的信息
			uid = listimg2.get(0).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img2:// 其他人的信息
			uid = listimg2.get(1).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img3:// 其他人的信息
			uid = listimg2.get(2).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img4:// 其他人的信息
			uid = listimg2.get(3).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img5:// 其他人的信息
			uid = listimg2.get(4).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img1:// 其他人的信息
			uid = listimg3.get(0).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img2:// 其他人的信息
			uid = listimg3.get(1).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img3:// 其他人的信息
			uid = listimg3.get(2).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img4:// 其他人的信息
			uid = listimg3.get(3).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img5:// 其他人的信息
			uid = listimg3.get(4).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.ideas_world_zhiku_details_dating:// 大厅弹窗，聊天室
			Log.d("dating", "dating");
			// getZhiKuList();//获取聊天室列表数据

			if (null != popupwindow) {
				if (popupwindow.isShowing()) {
					popupwindow.dismiss();

				} else {
					popupwindow.showAsDropDown(zhuangtai, 0, 6);
				}

			} else {
				initPopuptWindow();
				popupwindow.showAsDropDown(zhuangtai, 0, 6);
			}
			break;
		case R.id.ideas_world_zhiku_details_apply:// 申请加入智库
			addzhiku();
			break;

		case R.id.ideas_world_zhiku_details_recommend:// 推荐加入好友
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					MyFriendsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	// 推荐加入好友
	private void addHaoYou() {

	}

	// 申请加入智库
	private void addzhiku() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter("zid", tid);
		httpUtils.send(HttpMethod.POST, MyUrl.Stringzhikurenzhen, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getApplicationContext(),
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
								Toast.makeText(getApplicationContext(),
										"同级了,不能申请", Toast.LENGTH_SHORT).show();
							} else if (num.equals("2")) {
								shenqing();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void shenqing() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		httpUtils.send(HttpMethod.POST, MyUrl.Stringjumpzhikurenzhen, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						jiazaidialog.dismiss();
						String list = responseInfo.result;
						try {
							JSONObject object = new JSONObject(list);
							int num = Integer.parseInt(object.getString("num")
									.toString());
							switch (num) {
							case 1:
								Toast.makeText(getApplicationContext(),
										"认证条件不满足", Toast.LENGTH_SHORT).show();
								break;
							case 3:
								Toast.makeText(getApplicationContext(), "已认证",
										Toast.LENGTH_SHORT).show();
								break;
							case 2:
								String regions = object.getString("regions")
										.toString();
								String home = object.getString("home")
										.toString();
								String als = object.getString("als").toString();
								Intent intent = new Intent(
										getApplicationContext(),
										ThinkTankCertificationDetailsActivity.class);
								intent.putExtra("regions", regions);
								intent.putExtra("home", home);
								intent.putExtra("als", als);
								startActivity(intent);
								finish();
								break;
							default:
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 获取智库列表,//获取聊天室列表
	 */
	/*
	 * private void getZhiKuList() { RequestParams params = new RequestParams();
	 * params.addBodyParameter("uid", IsTrue.userId + "");
	 * params.addBodyParameter( "tid",
	 * SharedPrefsUtil.getStringValue(getApplicationContext(), "tid", "") + "");
	 * 
	 * httpUtils.send(HttpMethod.POST, MyUrl.thzulist, params, new
	 * RequestCallBack<String>() {
	 * 
	 * @Override public void onFailure(HttpException arg0, String arg1) { //
	 * TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public void onSuccess(ResponseInfo<String> responseInfo) { //
	 * TODO Auto-generated method stub String data = responseInfo.result; try {
	 * JSONObject object = new JSONObject(data); String num =
	 * object.getString("num").toString(); if (num.equals("1")) {
	 * Toast.makeText(getApplicationContext(), "当前无数据或数据为空", Toast.LENGTH_SHORT)
	 * .show(); } else if (num.equals("2")) { zhikuList = new
	 * ArrayList<ZhiKuModel>(); ZhiKuListAdapter adapter = new ZhiKuListAdapter(
	 * getApplicationContext()); ZhiKuModel zhikuModel = new ZhiKuModel();
	 * JSONArray listZhiku = object .getJSONArray("list"); // JSONArray js=new
	 * JSONArray("list"); zhikuList.clear(); for (int i = 0; i <
	 * listZhiku.length(); i++) { zhikuModel.id = listZhiku.getJSONObject(i)
	 * .get("id").toString(); zhikuModel.memberid = listZhiku
	 * .getJSONObject(i).get("memberid") .toString(); zhikuModel.name =
	 * listZhiku .getJSONObject(i).get("name") .toString();
	 * 
	 * zhikuList.add(zhikuModel); lvZhiKu.setAdapter(new ZhiKuListAdapter(
	 * getApplicationContext(), zhikuList)); Log.d("listZhiku"+i,
	 * listZhiku.getJSONObject(i).get("name").toString()); } } } catch
	 * (JSONException e) { e.printStackTrace(); } } });
	 * 
	 * }
	 */

	/**
	 * 大厅弹窗视图数据加载
	 * 
	 * @param object
	 */
	protected void popview(JSONObject object) {
		Log.d("listZhiku", "dating");
		try {
			zhikuList = new ArrayList<ZhiKuModel>();
			// ZhiKuListAdapter adapter = new ZhiKuListAdapter(
			// getApplicationContext());
			int zkzb2 = object.getInt("zkzb2");
			int zkzb3 = object.getInt("zkzb3");

			// JSONArray listZhiku = object.getJSONArray("zkzblist1");
			JSONObject listZhiku = object.getJSONObject("zkzblist1");

//			JSONArray listZhiku2 = object.getJSONArray("zkzblist2");
//			JSONArray listZhiku3 = object.getJSONArray("zkzblist3");

			// JSONArray js=new JSONArray("list");
			zhikuList.clear();

			// for (int i = 0; i < listZhiku.length(); i++) {
			zhikuModel = new ZhiKuModel();
			zhikuModel.id = listZhiku.get("library_group_hx").toString();
			zhikuModel.memberid = listZhiku.get("library_group_id").toString();
			zhikuModel.name = listZhiku.get("library_group_name").toString();

			zhikuList.add(zhikuModel);
			Log.d("listZhiku", listZhiku.get("library_group_name").toString());
			// }

			if (1 == zkzb2) {
				JSONArray listZhiku2 = object.getJSONArray("zkzblist2");
				for (int i = 0; i < listZhiku.length(); i++) {
					zhikuModel = new ZhiKuModel();
					zhikuModel.id = listZhiku2.getJSONObject(i)
							.get("library_group_hx").toString();
					zhikuModel.memberid = listZhiku2.getJSONObject(i)
							.get("library_group_id").toString();
					zhikuModel.name = listZhiku2.getJSONObject(i)
							.get("library_group_name").toString();

					zhikuList.add(zhikuModel);
				}

			}

			if (1 == zkzb3) {
				JSONArray listZhiku3 = object.getJSONArray("zkzblist3");
				for (int i = 0; i < listZhiku.length(); i++) {
					zhikuModel = new ZhiKuModel();
					zhikuModel.id = listZhiku3.getJSONObject(i)
							.get("library_group_hx").toString();
					zhikuModel.memberid = listZhiku3.getJSONObject(i)
							.get("library_group_id").toString();
					zhikuModel.name = listZhiku3.getJSONObject(i)
							.get("library_group_name").toString();

					zhikuList.add(zhikuModel);
				}

			}

			groupId = zhikuList.get(0).id;
			Log.d("groupId", "" + groupId);
			hand.sendEmptyMessage(REFRESH_CHAT);

			Myapplication.zhikuList = zhikuList;
			// for(int i=0;i<zhikuList.size();i++ ){
			// Log.d("namea"+i,zhikuList.get(i).name);
			// }

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// // 获取自定义布局文件pop.xml的视图
		// View customView = getLayoutInflater().inflate(R.layout.popuptwindow,
		// null, false);
		popupwindow = new PopupWindow(zhikuguanli, 200,
				LayoutParams.WRAP_CONTENT);
		popupwindow.showAsDropDown(zhuangtai, 0, 8);
		// 使其聚集
		popupwindow.setFocusable(true);
		// 设置允许在外点击消失
		popupwindow.setOutsideTouchable(true);
		// 刷新状态（必须刷新否则无效）
		popupwindow.update();
		zhikuguanli.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});

		// 组管理，跳转至聊天室列表
		Button button = (Button) zhikuguanli
				.findViewById(R.id.popuptwindow_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 进入聊天室列表页面
				startActivity(new Intent(IdeasWorldZhiKuDetails.this,
						PublicChatRoomsActivity.class));
			}
		});

		// 智库列表

		// for(int i=0;i<zhikuList.size();i++ ){
		// Log.d("name"+i,zhikuList.get(i).name);
		//
		// }
		lvZhiKu = (ListView) zhikuguanli.findViewById(R.id.lvZhiKu);

		if (null != zhikuList) {
			lvZhiKu.setAdapter(new ZhiKuListAdapter(getApplicationContext(),
					zhikuList));
		}

		// 设置item点击事件
		lvZhiKu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				chatGroupName.setText(zhikuList.get(position).name);
				groupId = zhikuList.get(position).id;
				hand.sendEmptyMessage(REFRESH_CHAT);
				popupwindow.dismiss();
				initChat();
				Log.d("list",
						"" + zhikuList.size() + "-id-"
								+ zhikuList.get(position).id);

				// final EMChatRoom room = adapter.getItem(position);
				// startActivity(new Intent(IdeasWorldZhiKuDetails.this,
				// ChatRoomActivity.class).putExtra("chatType", 3)
				// .putExtra("groupId", zhikuList.get(position).id));

			}
		});

	}

	/**
	 * 初始化聊天室
	 */
	private void initChat() {
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 从本地加载群聊列表
		List<EMGroup> grouplists = EMGroupManager.getInstance().getAllGroups();
		if (grouplists.size() != 0) {
			System.out.println(grouplists + "HHHHHHHHHHHHHHHHHHHH");
		}

		// 獲取聊天室id
		// Intent intent = new Intent();
		// intent = getIntent();
		// groupId = intent.getExtras().getString("groupId");
		// chat_head.setText(intent.getExtras().getString("name"));

		chatType = 3;// intent.getExtras().getInt("chatType");// chattype==3

		Log.d("chattype", groupId + "-chatType-" + chatType);
		onChatRoomViewCreation(groupId);
		onConversationInit(groupId);
		onListViewCreation(groupId);
		// show forward message if the message is not null
		// String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
		// if (forward_msg_id != null) {
		// // 显示发送要转发的消息
		// forwardMessage(forward_msg_id);
		// }

	}

	protected void onChatRoomViewCreation(final String toChatUsername) {

		final ProgressDialog pd = ProgressDialog
				.show(this, "", "Joining......");
		EMChatManager.getInstance().joinChatRoom(toChatUsername,
				new EMValueCallBack<EMChatRoom>() {

					@Override
					public void onSuccess(EMChatRoom value) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								pd.dismiss();
								EMChatRoom room = EMChatManager.getInstance()
										.getChatRoom(toChatUsername);
								if (room != null) {
									// chat_head.setText(room.getName());
								} else {
									// chat_head.setText(toChatUsername);
								}
								// EMLog.d(TAG, "join room success : " +
								// room.getName());

								onConversationInit(toChatUsername);

								onListViewCreation(toChatUsername);
							}
						});
					}

					@Override
					public void onError(final int error, String errorMsg) {
						// TODO Auto-generated method stub
						// EMLog.d(TAG, "join room failure : " + error);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								pd.dismiss();
							}
						});
						finish();
					}
				});
	}

	protected void onConversationInit(final String toChatUsername) {

		conversation = EMChatManager.getInstance().getConversationByType(
				toChatUsername, EMConversationType.ChatRoom);

		// 把此会话的未读数置为0
		conversation.markAllMessagesAsRead();

		// 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
		// 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
		final List<EMMessage> msgs = conversation.getAllMessages();
		int msgCount = msgs != null ? msgs.size() : 0;
		if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
			String msgId = null;
			if (msgs != null && msgs.size() > 0) {
				msgId = msgs.get(0).getMsgId();
			}
			conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
		}

		EMChatManager.getInstance().addChatRoomChangeListener(
				new EMChatRoomChangeListener() {

					@Override
					public void onChatRoomDestroyed(String roomId,
							String roomName) {
						if (roomId.equals(toChatUsername)) {
							finish();
						}
					}

					@Override
					public void onMemberJoined(String roomId, String participant) {
					}

					@Override
					public void onMemberExited(String roomId, String roomName,
							String participant) {

					}

					@Override
					public void onMemberKicked(String roomId, String roomName,
							String participant) {
						if (roomId.equals(toChatUsername)) {
							String curUser = EMChatManager.getInstance()
									.getCurrentUser();
							if (curUser.equals(participant)) {
								EMChatManager.getInstance().leaveChatRoom(
										toChatUsername);
								finish();
							}
						}
					}

				});
	}

	protected void onListViewCreation(String toChatUsername) {
		adapter = new MessageAdapter(IdeasWorldZhiKuDetails.this,
				toChatUsername, chatType);
		// 显示消息
		chat_list.setAdapter(adapter);

		chat_list.setOnScrollListener(new ListScrollListener());
		adapter.refreshSelectLast();

		chat_list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				// more.setVisibility(View.GONE);
				// iv_emoticons_normal.setVisibility(View.VISIBLE);
				// iv_emoticons_checked.setVisibility(View.INVISIBLE);
				// emojiIconContainer.setVisibility(View.GONE);
				// btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
	}

	/**
	 * listview滑动监听listener
	 * 
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				/*
				 * if (view.getFirstVisiblePosition() == 0 && !isloading &&
				 * haveMoreData && conversation.getAllMessages().size() != 0) {
				 * isloading = true; loadmorePB.setVisibility(View.VISIBLE); //
				 * sdk初始化加载的聊天记录为20条，到顶时去db里获取更多 List<EMMessage> messages;
				 * EMMessage firstMsg = conversation.getAllMessages().get(0);
				 * try { // 获取更多messges，调用此方法的时候从db获取的messages //
				 * sdk会自动存入到此conversation中 if (chatType == CHATTYPE_SINGLE)
				 * messages =
				 * conversation.loadMoreMsgFromDB(firstMsg.getMsgId(),
				 * pagesize); else messages =
				 * conversation.loadMoreGroupMsgFromDB(firstMsg.getMsgId(),
				 * pagesize); } catch (Exception e1) {
				 * loadmorePB.setVisibility(View.GONE); return; } try {
				 * Thread.sleep(300); } catch (InterruptedException e) { } if
				 * (messages.size() != 0) { // 刷新ui if (messages.size() > 0) {
				 * adapter.refreshSeekTo(messages.size() - 1); }
				 * 
				 * if (messages.size() != pagesize) haveMoreData = false; } else
				 * { haveMoreData = false; }
				 * loadmorePB.setVisibility(View.GONE); isloading = false;
				 * 
				 * }
				 */
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 转发消息
	 * 
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(
				forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			// 获取消息内容，发送消息
			String content = ((TextMessageBody) forward_msg.getBody())
					.getMessage();
			sendText(content);
			break;
		case IMAGE:
			// 发送图片
			String filePath = ((ImageMessageBody) forward_msg.getBody())
					.getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					// 不存在大图发送缩略图
					// filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				// sendPicture(filePath);
			}
			break;
		default:
			break;
		}

		if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
			EMChatManager.getInstance().leaveChatRoom(forward_msg.getTo());
		}
	}

	/**
	 * 发送文本
	 * 
	 * @param s
	 */
	private void sendText(String content) {
		if (content.length() > 0) {
			// System.out.println(groupId + "1111111111111111" + content);
			// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
			// EMConversation conversation = EMChatManager.getInstance()
			// .getConversation(groupId);

			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 如果是群聊，设置chattype,默认是单聊
			message.setChatType(ChatType.GroupChat);
			TextMessageBody txtBody = new TextMessageBody(content);
			// 设置消息body
			message.addBody(txtBody);
			// 设置要发给谁,用户username或者群聊groupid
			message.setReceipt(groupId);
			// 把messgage加到conversation中
			conversation.addMessage(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
			// adapter.notifyDataSetChanged();
			// chat_list.setAdapter(adapter);
			// chat_list.setSelection(chat_list.getCount() - 1);
			chat_list.setAdapter(adapter);
			adapter.refreshSelectLast();
			chat_text.setText("");
			setResult(RESULT_OK);

		} else {
			Toast.makeText(getApplicationContext(), "输入不能为空",
					Toast.LENGTH_SHORT).show();
		}
	}

	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 注销广播
			abortBroadcast();

			// 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
			String msgId = intent.getStringExtra("msgid");
			// 发送方
			String username = intent.getStringExtra("from");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(username);
			// 如果是群聊消息，获取到group id
			if (message.getChatType() == ChatType.GroupChat) {
				// username = message.getTo();
				groupId = message.getTo();
			}
			if (!groupId.equals(groupId)) {
				// 消息不是发给当前会话，return
				return;
			}
			conversation.addMessage(message);
			adapter.refreshSelectLast();
			// adapter.notifyDataSetChanged();
			chat_list.setAdapter(adapter);
			// chat_list.setSelection(chat_list.getCount() - 1);
		}
	}

}
