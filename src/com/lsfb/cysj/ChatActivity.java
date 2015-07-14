package com.lsfb.cysj;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Direct;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
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
import com.lsfb.cysj.NewsActivity.DateAdapter;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;

public class ChatActivity extends FragmentActivity implements OnClickListener {
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

	private int chatType;
	public static final int CHATTYPE_GROUP = 2;

	// 给谁发送消息
	private EMConversation conversation;

	private DateAdapter dateAdapter;
	String groupId;// 群id
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		ViewUtils.inject(this);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
		NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		EMChat.getInstance().setAppInited();
		init();
		conversation = EMChatManager.getInstance().getConversation(groupId);
		dateAdapter = new DateAdapter();
		chat_list.setAdapter(dateAdapter);
		chat_list.setSelection(chat_list.getCount() - 1);
	}
	
	public class DateAdapter extends BaseAdapter{
		String nickname = null;
		String image = null;
			@Override
			public View getView(int position, View view, ViewGroup parent) {
				EMMessage message = conversation.getAllMessages().get(position);
//				EMMessage message = EMChatManager.getInstance()
//						.getConversation(groupId).getAllMessages().get(position);
				ViewHolder holder = null;
				if (message.direct == EMMessage.Direct.RECEIVE) {
					//接收方
					if (view == null) {
						holder = new ViewHolder();
						view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.newdemo, null);
						holder.imghead = (ImageView) view.findViewById(R.id.ivnewdemo);
						holder.text = (TextView) view.findViewById(R.id.tvnewdemo);
						holder.picture = (ImageView) view.findViewById(R.id.ivnewpicture);
						holder.video = (ImageView) view.findViewById(R.id.newvideo);
						holder.voiecs = (ImageView) view.findViewById(R.id.newsVoiec);
						holder.shibai = (ImageView) view.findViewById(R.id.shibai);
						holder.name = (TextView) view.findViewById(R.id.jieshouname);
						view.setTag(holder);
					}else {
						holder = (ViewHolder) view.getTag();
					}
					String id = message.getFrom();
					String id2 = id.substring(4);
					HttpUtils httpUtils = new HttpUtils();
					RequestParams params = new RequestParams();
					params.addBodyParameter("id", id2);
					httpUtils.send(HttpMethod.POST, ImageAddress.memberimage, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							String list = responseInfo.result;
							System.out.println(list);
							try {
								JSONObject object = new JSONObject(list);
								nickname = object.getString("nickname").toString();
								image = object.getString("image").toString();
								System.out.println(nickname+"OOO"+image);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
					BitmapUtils bitmapUtils = new BitmapUtils(ChatActivity.this);
					bitmapUtils.display(holder.imghead, ImageAddress.Stringhead+image);
					//设置用户头像
//					setUserAvatar(message, holder.imghead);
					holder.name.setText(nickname);
					if (message.getType() == EMMessage.Type.TXT) {
						holder.text.setText(((TextMessageBody)message.getBody()).getMessage());
					}
				}else{
					//发送方
					if (view == null) {
						holder = new ViewHolder();
						view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.newdemos, null);
						holder.imghead = (ImageView) view.findViewById(R.id.ivnewdemos);
						holder.text = (TextView) view.findViewById(R.id.tvnewdemos);
						holder.picture = (ImageView) view.findViewById(R.id.ivnewpictures);
						holder.video = (ImageView) view.findViewById(R.id.newvideos);
						holder.voiecs = (ImageView) view.findViewById(R.id.newsVoiecs);
						holder.shibai = (ImageView) view.findViewById(R.id.sendshibai);
						holder.name = (TextView) view.findViewById(R.id.sendname);
						view.setTag(holder);
					}else {
						holder = (ViewHolder) view.getTag();
					}
					BitmapUtils bitmapUtils = new BitmapUtils(ChatActivity.this);
					bitmapUtils.display(holder.imghead, ImageAddress.Stringhead+IsTrue.Stringimage);
					holder.name.setText(IsTrue.Stringnickname);
					if (message.getType() == EMMessage.Type.TXT) {
						holder.text.setText(((TextMessageBody)message.getBody()).getMessage());
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
//				if (messages != null && position < messages.length) {
//					return messages[position];
//				}
//				return null;
				return conversation.getAllMessages().get(position);
//				return EMChatManager.getInstance()
//						.getConversation(groupId).getAllMessages().get(position);
			}

			@Override
			public int getCount() {
//				return messages.length;
				return conversation.getAllMessages().size();
//				return EMChatManager.getInstance()
//						.getConversation(groupId).getAllMessages().size();
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
//				username = message.getTo();
				groupId = message.getTo();
			}
			if (!groupId.equals(groupId)) {
				// 消息不是发给当前会话，return
				return;
			}
			conversation.addMessage(message);
			dateAdapter.notifyDataSetChanged();
			chat_list.setAdapter(dateAdapter);
			chat_list.setSelection(chat_list.getCount() - 1);
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
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(groupId);

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
			dateAdapter.notifyDataSetChanged();
			chat_list.setAdapter(dateAdapter);
			chat_list.setSelection(chat_list.getCount() - 1);
			chat_text.setText("");
			setResult(RESULT_OK);
			// 发送消息
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onError(int arg0, String arg1) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(ChatActivity.this, "发送失败",
									Toast.LENGTH_SHORT).show();
						}
					});
				}

				@Override
				public void onProgress(int arg0, String arg1) {

				}

				@Override
				public void onSuccess() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(ChatActivity.this, "发送成功",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "输入不能为空",
					Toast.LENGTH_SHORT).show();
		}
	}

		/**
		 * 显示用户头像
		 * @param message
		 * @param imageView
		 */
//		private void setUserAvatar(EMMessage message, ImageView imageView){
//		    if(message.direct == Direct.SEND){
//		        //显示自己头像
//		        UserUtils.setUserAvatar(context, EMChatManager.getInstance().getCurrentUser(), imageView);
//		    }else{
//		        UserUtils.setUserAvatar(context, message.getFrom(), imageView);
//		    }
//	}

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

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				chat_head.setText(str);
			}
		};
	};
	static class ViewHolder{
		ImageView imghead;
		TextView text;
		ImageView picture;
		ImageView video;
		ImageView voiecs;
		ImageView shibai;
		TextView name;
	}
	private void init() {
		back.setOnClickListener(this);
		xiangqing.setOnClickListener(this);
		chat_button1.setOnClickListener(this);
		chat_button2.setOnClickListener(this);
		chat_button3.setOnClickListener(this);
		chat_button4.setOnClickListener(this);
		chat_button5.setOnClickListener(this);
		chat_send.setOnClickListener(this);
		// 从本地加载群聊列表
		List<EMGroup> grouplists = EMGroupManager.getInstance().getAllGroups();
		if (grouplists.size() != 0) {
			System.out.println(grouplists + "HHHHHHHHHHHHHHHHHHHH");
		}
		Intent intent = new Intent();
		intent = getIntent();
		groupId = intent.getExtras().getString("groupId");
		chat_head.setText(intent.getExtras().getString("name"));
		try {
			EMGroup group = EMGroupManager.getInstance().getGroupFromServer(groupId);
			List<String> chengyuan = group.getMembers();// 获取群成员
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
//		new Thread() {
//			public void run() {
//				try {
//					List<EMGroup> grouplist = EMGroupManager.getInstance()
//							.getGroupsFromServer();// 获取群聊列表
//					System.out.println(grouplist.size() + "SSSSSSSSSSSSS");
//					for (int i = 0; i < grouplist.size(); i++) {
//						String groupId = grouplist.get(i).getGroupId();
//						System.out.println(groupId + "MMM");
//					}
//					groupId = grouplist.get(0).getGroupId();
//					String name = grouplist.get(0).getGroupName();
//					Message msg = new Message();
//					msg.what = 0x123;
//					msg.obj = name;
//					handler.sendMessage(msg);
//					System.out.println(groupId + "UUUUUUUUUUUUUUUUU" + name);
//					// 根据群聊ID从服务器获取群聊信息
//					EMGroup group = EMGroupManager.getInstance()
//							.getGroupFromServer(groupId);
//					List<String> s = group.getMembers();// 获取群成员
//					String ss = group.getOwner();// 获取群主
//					System.out.println(ss + "LLLLLLLLLLLLLL" + s);
//				} catch (EaseMobException e1) {
//					e1.printStackTrace();
//				}
//			};
//		}.start();
		// 保存获取下来的群聊信息
		// EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
		// group.getMembers();//获取群成员
		// group.getOwner();//获取群主
		//
		// if (grouplist.size()==0) {
		// String[] s= {"cysj13","cysj14","cysj9","cysj12"};
		// //groupName：要创建的群聊的名称
		// //desc：群聊简介
		// //members：群聊成员,为空时这个创建的群组只包含自己
		// //allowInvite:是否允许群成员邀请人进群
		// try {
		// EMGroupManager.getInstance().createPrivateGroup("群聊", "什么",
		// s,true);//需异步处理
		// //群主加人调用此方法
		// // EMGroupManager.getInstance().addUsersToGroup("cysj14", );//需异步处理
		// //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
		// // EMGroupManager.getInstance().inviteUser(groupId, newmembers,
		// null);//需异步处理
		// } catch (EaseMobException e) {
		// e.printStackTrace();
		// }
		//
		// //前一种方法创建的群聊默认最大群聊用户数为200，传入maxUsers后设置自定义的最大用户数，最大为2000
		// // EMGroupManager.getInstance().createPrivateGroup(groupName, desc,
		// members,allowInvite,maxUsers);//需异步处理
		// }
	}

}
