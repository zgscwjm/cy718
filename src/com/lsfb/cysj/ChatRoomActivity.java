package com.lsfb.cysj;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
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
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
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
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.utils.ExpressionUtil;

/**
 * 聊天室界面
 * 
 * @author Administrator
 * 
 */
public class ChatRoomActivity extends FragmentActivity implements
		OnClickListener {

	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private final int pagesize = 20;

	@ViewInject(R.id.chat_back)
	private LinearLayout back;
	@ViewInject(R.id.chat_xiangqing)
	private TextView xiangqing;
	/**
	 * chat_button1 支持作品
	 */
	@ViewInject(R.id.chat_button1)
	private TextView chat_button1;
	/**
	 * chat_button2 提交作品
	 */
	@ViewInject(R.id.chat_button2)
	private TextView chat_button2;
	/**
	 * chat_button3 评论作品
	 */
	@ViewInject(R.id.chat_button3)
	private TextView chat_button3;
	/**
	 * chat_button4 邀请好友
	 */
	@ViewInject(R.id.chat_button4)
	private TextView chat_button4;
	/**
	 * chat_button5 邀请专家
	 */
	@ViewInject(R.id.chat_button5)
	private TextView chat_button5;
	/**
	 * chat_text 聊天文字
	 */
	@ViewInject(R.id.chat_text)
	private EditText chat_text;
	/**
	 * chat_send 发送消息
	 */
	@ViewInject(R.id.chat_send)
	private Button chat_send;
	@ViewInject(R.id.chat_head)
	private TextView chat_head;
	@ViewInject(R.id.chat_list)
	private ListView chat_list;
	@ViewInject(R.id.chat_biaoqing)
	private ImageButton biaoqing;
	@ViewInject(R.id.chat_more)
	private LinearLayout chat_more;
	@ViewInject(R.id.chat_ll_btn_container)
	private LinearLayout other;
	boolean othersend = false;

	private int chatType;
	public static final int CHATTYPE_GROUP = 3;// 為聊天室
	public String playMsgId;

	// 给谁发送消息
	private EMConversation conversation;
	private Boolean isOpenll_face_container;// 是否打开表情
	private Boolean isOpenll_btn_container;// 是否更多功能

	private InputMethodManager manager;
	NewMessageBroadcastReceiver msgReceiver;

	// private DateAdapter dateAdapter;
	private MessageAdapter adapter;
	String groupId;// 群id
	Dialog builder;
	private int[] imageIds = new int[] { R.drawable.f000, R.drawable.f001,
			R.drawable.f002, R.drawable.f003, R.drawable.f004, R.drawable.f005,
			R.drawable.f006, R.drawable.f007, R.drawable.f008, R.drawable.f009,
			R.drawable.f010, R.drawable.f011, R.drawable.f012, R.drawable.f013,
			R.drawable.f014, R.drawable.f015, R.drawable.f016, R.drawable.f017,
			R.drawable.f018, R.drawable.f019, R.drawable.f020, R.drawable.f021,
			R.drawable.f022, R.drawable.f023, R.drawable.f024, R.drawable.f025,
			R.drawable.f026, R.drawable.f027, R.drawable.f028, R.drawable.f029,
			R.drawable.f030, R.drawable.f031, R.drawable.f032, R.drawable.f033,
			R.drawable.f034, R.drawable.f035, R.drawable.f036, R.drawable.f037,
			R.drawable.f038, R.drawable.f039, R.drawable.f040, R.drawable.f041,
			R.drawable.f042, R.drawable.f043, R.drawable.f044, R.drawable.f045,
			R.drawable.f046, R.drawable.f047, R.drawable.f048, R.drawable.f049,
			R.drawable.f050, R.drawable.f051, R.drawable.f052, R.drawable.f053,
			R.drawable.f054, R.drawable.f055, R.drawable.f056, R.drawable.f057,
			R.drawable.f058, R.drawable.f059, R.drawable.f060, R.drawable.f061,
			R.drawable.f062, R.drawable.f063, R.drawable.f064, R.drawable.f065,
			R.drawable.f066, R.drawable.f067, R.drawable.f068, R.drawable.f069,
			R.drawable.f070, R.drawable.f071, R.drawable.f072, R.drawable.f073,
			R.drawable.f074, R.drawable.f075, R.drawable.f076, R.drawable.f077,
			R.drawable.f078, R.drawable.f079, R.drawable.f080, R.drawable.f081,
			R.drawable.f082, R.drawable.f083, R.drawable.f084, R.drawable.f085,
			R.drawable.f086, R.drawable.f087, R.drawable.f088, R.drawable.f089,
			R.drawable.f090, R.drawable.f091, R.drawable.f092, R.drawable.f093,
			R.drawable.f094, R.drawable.f095, R.drawable.f096, R.drawable.f097,
			R.drawable.f098, R.drawable.f099, R.drawable.f100, R.drawable.f101,
			R.drawable.f102, R.drawable.f103, R.drawable.f104, R.drawable.f105, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_room);
		ViewUtils.inject(this);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
		  msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		EMChat.getInstance().setAppInited();
		init();
		conversation = EMChatManager.getInstance().getConversation(groupId);
		adapter = new MessageAdapter(ChatRoomActivity.this, groupId,
				CHATTYPE_GROUP);
		chat_list.setAdapter(adapter);
		adapter.refreshSelectLast();
		// dateAdapter = new DateAdapter();
		// chat_list.setAdapter(adapter);
		// chat_list.setSelection(chat_list.getCount() - 1);
		
	}

	public class DateAdapter extends BaseAdapter {
		String nickname = null;
		String image = null;

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			EMMessage message = conversation.getAllMessages().get(position);
			// EMMessage message = EMChatManager.getInstance()
			// .getConversation(groupId).getAllMessages().get(position);
			ViewHolder holder = null;
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 接收方
				if (view == null) {
					holder = new ViewHolder();
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.newdemo, null);
					holder.imghead = (ImageView) view
							.findViewById(R.id.ivnewdemo);
					holder.text = (TextView) view.findViewById(R.id.tvnewdemo);
					holder.picture = (ImageView) view
							.findViewById(R.id.ivnewpicture);
					holder.video = (ImageView) view.findViewById(R.id.newvideo);
					holder.voiecs = (ImageView) view
							.findViewById(R.id.newsVoiec);
					holder.shibai = (ImageView) view.findViewById(R.id.shibai);
					holder.name = (TextView) view
							.findViewById(R.id.jieshouname);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String id = message.getFrom();
				String id2 = id.substring(4);
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("id", id2);
				httpUtils.send(HttpMethod.POST, ImageAddress.memberimage,
						params, new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {

							}

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								String list = responseInfo.result;
								System.out.println(list);
								try {
									JSONObject object = new JSONObject(list);
									nickname = object.getString("nickname")
											.toString();
									image = object.getString("image")
											.toString();
									System.out
											.println(nickname + "OOO" + image);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
				BitmapUtils bitmapUtils = new BitmapUtils(ChatRoomActivity.this);
				bitmapUtils.display(holder.imghead, ImageAddress.Stringhead
						+ image);
				// 设置用户头像
				// setUserAvatar(message, holder.imghead);
				holder.name.setText(nickname);
				// holder.name.setText(message.getFrom());
				if (message.getType() == EMMessage.Type.TXT) {
					TextMessageBody textMessageBody = (TextMessageBody) message
							.getBody();
					// holder.text.setText(((TextMessageBody) message.getBody())
					// .getMessage());
					String zhengze = "f0[0-9]{2}|f10[0-7]"; //
					// 正则表达式，用来判断消息内是否有表情
					// String zhengze = "bq[0-9]{2}|"; // 正则表达式，用来判断消息内是否有表情
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(ChatRoomActivity.this,
									textMessageBody.getMessage(), zhengze);
					holder.text.setText(spannableString);
				}
			} else if (message.direct == EMMessage.Direct.SEND) {
				// 发送方
				if (view == null) {
					holder = new ViewHolder();
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.newdemos, null);
					holder.imghead = (ImageView) view
							.findViewById(R.id.ivnewdemos);
					holder.text = (TextView) view.findViewById(R.id.tvnewdemos);
					holder.picture = (ImageView) view
							.findViewById(R.id.ivnewpictures);
					holder.video = (ImageView) view
							.findViewById(R.id.newvideos);
					holder.voiecs = (ImageView) view
							.findViewById(R.id.newsVoiecs);
					holder.shibai = (ImageView) view
							.findViewById(R.id.sendshibai);
					holder.name = (TextView) view.findViewById(R.id.sendname);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				BitmapUtils bitmapUtils = new BitmapUtils(ChatRoomActivity.this);
				bitmapUtils.display(holder.imghead, ImageAddress.Stringhead
						+ IsTrue.Stringimage);
				holder.name.setText(IsTrue.Stringnickname);
				// holder.name.setText(message.getFrom());
				if (message.getType() == EMMessage.Type.TXT) {
					TextMessageBody textMessageBody = (TextMessageBody) message
							.getBody();
					// holder.text.setText(((TextMessageBody) message.getBody())
					// .getMessage());
					String zhengze = "f0[0-9]{2}|f10[0-7]"; //
					// 正则表达式，用来判断消息内是否有表情
					// String zhengze = "bq[0-9]{2}|"; // 正则表达式，用来判断消息内是否有表情
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(ChatRoomActivity.this,
									textMessageBody.getMessage(), zhengze);
					holder.text.setText(spannableString);
				}
			}
			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			// return conversation.getAllMessages().get(position);
			return position;
		}

		@Override
		public int getCount() {
			return conversation.getAllMessages().size();
		}
	};

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

	/**
	 * 发送文本
	 * 
	 * @param s
	 */
	private void sendText(String content) {
		if (content.length() > 0) {
			System.out.println(groupId + "1111111111111111" + content);
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

	/**
	 * 显示用户头像
	 * 
	 * @param message
	 * @param imageView
	 */
	// private void setUserAvatar(EMMessage message, ImageView imageView){
	// if(message.direct == Direct.SEND){
	// //显示自己头像
	// UserUtils.setUserAvatar(context,
	// EMChatManager.getInstance().getCurrentUser(), imageView);
	// }else{
	// UserUtils.setUserAvatar(context, message.getFrom(), imageView);
	// }
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_back:
			finish();
			break;
		case R.id.chat_send:// 发送消息
			String s = chat_text.getText().toString();
			sendText(s);
			break;
		case R.id.chat_more:// 其他
			if (othersend) {
				other.setVisibility(View.GONE);
				othersend = false;
			} else {
				other.setVisibility(View.VISIBLE);
				othersend = true;
			}
			break;
		case R.id.chat_biaoqing:// 表情
			// 打开表情
			if (!isOpenll_face_container) {
				createExpressionDialog();
				// ll_face_container.setVisibility(View.VISIBLE);
				isOpenll_face_container = true;
				// ll_btn_container.setVisibility(View.GONE);
				isOpenll_btn_container = false;
			} else {
				// ll_face_container.setVisibility(View.GONE);
				isOpenll_face_container = false;
			}
			break;
		case R.id.chat_button1:// 支持作品
			chat_button1.setBackgroundResource(R.drawable.shape);
			chat_button1.setTextColor(this.getResources().getColorStateList(
					R.color.white));
			chat_button2.setBackgroundResource(R.drawable.shapedefault);
			chat_button2.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button3.setBackgroundResource(R.drawable.shapedefault);
			chat_button3.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button4.setBackgroundResource(R.drawable.shapedefault);
			chat_button4.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button5.setBackgroundResource(R.drawable.shapedefault);
			chat_button5.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			break;
		case R.id.chat_button2:// 提交作品
			chat_button2.setBackgroundResource(R.drawable.shape);
			chat_button2.setTextColor(this.getResources().getColorStateList(
					R.color.white));
			chat_button1.setBackgroundResource(R.drawable.shapedefault);
			chat_button1.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button3.setBackgroundResource(R.drawable.shapedefault);
			chat_button3.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button4.setBackgroundResource(R.drawable.shapedefault);
			chat_button4.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button5.setBackgroundResource(R.drawable.shapedefault);
			chat_button5.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			break;
		case R.id.chat_button3:// 评价作品
			chat_button3.setBackgroundResource(R.drawable.shape);
			chat_button3.setTextColor(this.getResources().getColorStateList(
					R.color.white));
			chat_button2.setBackgroundResource(R.drawable.shapedefault);
			chat_button2.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button1.setBackgroundResource(R.drawable.shapedefault);
			chat_button1.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button4.setBackgroundResource(R.drawable.shapedefault);
			chat_button4.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button5.setBackgroundResource(R.drawable.shapedefault);
			chat_button5.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			break;
		case R.id.chat_button4:// 邀请好友
			chat_button4.setBackgroundResource(R.drawable.shape);
			chat_button4.setTextColor(this.getResources().getColorStateList(
					R.color.white));
			chat_button2.setBackgroundResource(R.drawable.shapedefault);
			chat_button2.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button3.setBackgroundResource(R.drawable.shapedefault);
			chat_button3.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button1.setBackgroundResource(R.drawable.shapedefault);
			chat_button1.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button5.setBackgroundResource(R.drawable.shapedefault);
			chat_button5.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			break;
		case R.id.chat_button5:// 邀请专家
			chat_button5.setBackgroundResource(R.drawable.shape);
			chat_button5.setTextColor(this.getResources().getColorStateList(
					R.color.white));
			chat_button2.setBackgroundResource(R.drawable.shapedefault);
			chat_button2.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button3.setBackgroundResource(R.drawable.shapedefault);
			chat_button3.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button4.setBackgroundResource(R.drawable.shapedefault);
			chat_button4.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			chat_button1.setBackgroundResource(R.drawable.shapedefault);
			chat_button1.setTextColor(this.getResources().getColorStateList(
					R.color.blueMain));
			break;
		default:
			break;
		}
	}

	private void createExpressionDialog() {
		builder = new Dialog(ChatRoomActivity.this);
		GridView gridView = createGridView();
		builder.setContentView(gridView);
		builder.setTitle("默认表情");
		builder.show();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),
						imageIds[i % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(ChatRoomActivity.this,
						bitmap);
				String str = null;
				if (i < 10) {
					str = "f00" + i;
				} else if (i < 100) {
					str = "f0" + i;
				} else {
					str = "f" + i;
				}
				SpannableString spannableString = new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 4,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				chat_text.append(spannableString);
				builder.dismiss();
			}
		});
	}

	/**
	 * 生成一个表情对话框中的gridview
	 * 
	 * @return
	 */
	private GridView createGridView() {
		final GridView view = new GridView(this);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成107个表情的id，封装

		for (int i = 0; i < imageIds.length; i++) {
			try {
				if (i < 10) {
					Field field = R.drawable.class.getDeclaredField("f00" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else if (i < 100) {
					Field field = R.drawable.class.getDeclaredField("f0" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else {
					Field field = R.drawable.class.getDeclaredField("f" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.team_layout_single_expression_cell,
				new String[] { "image" }, new int[] { R.id.image });
		view.setAdapter(simpleAdapter);
		view.setNumColumns(6);
		view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		return view;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				chat_head.setText(str);
			}
		};
	};

	public ListView getListView() {
		return chat_list;
	}

	static class ViewHolder {
		ImageView imghead;
		TextView text;
		ImageView picture;
		ImageView video;
		ImageView voiecs;
		ImageView shibai;
		TextView name;
	}

	private void init() {
		isOpenll_face_container = false;
		isOpenll_btn_container = false;
		// isOpenll_btn_speak = false;
		// isOpenll_mPlayer = false;
		back.setOnClickListener(this);
		xiangqing.setOnClickListener(this);
		chat_button1.setOnClickListener(this);
		chat_button2.setOnClickListener(this);
		chat_button3.setOnClickListener(this);
		chat_button4.setOnClickListener(this);
		chat_button5.setOnClickListener(this);
		chat_send.setOnClickListener(this);
		biaoqing.setOnClickListener(this);
		chat_more.setOnClickListener(this);

		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 从本地加载群聊列表
		List<EMGroup> grouplists = EMGroupManager.getInstance().getAllGroups();
		if (grouplists.size() != 0) {
			System.out.println(grouplists + "HHHHHHHHHHHHHHHHHHHH");
		}

		// 獲取聊天室id
		Intent intent = new Intent();
		intent = getIntent();
		groupId = intent.getExtras().getString("groupId");
		chat_head.setText(intent.getExtras().getString("name"));

		chatType = intent.getExtras().getInt("chatType");// chattype==3

		Log.d("chattype", groupId + "-chatType-" + chatType);
		onChatRoomViewCreation(groupId);
		onConversationInit(groupId);
		onListViewCreation(groupId);
		// show forward message if the message is not null
		String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
		if (forward_msg_id != null) {
			// 显示发送要转发的消息
			forwardMessage(forward_msg_id);
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
									chat_head.setText(room.getName());
								} else {
									chat_head.setText(toChatUsername);
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
		adapter = new MessageAdapter(ChatRoomActivity.this, toChatUsername,
				chatType);
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

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(msgReceiver);
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

}
