package com.lsfb.cysj;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.GroupChangeListener;
import com.easemob.exceptions.EaseMobException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;

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

	private int chatType;
	public static final int CHATTYPE_GROUP = 2;

	// 给谁发送消息
	private String toChatUsername;
	private EMConversation conversation;

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
				username = message.getTo();
			}
			if (!username.equals(username)) {
				// 消息不是发给当前会话，return
				return;
			}
			conversation.addMessage(message);
//			dateAdapter.notifyDataSetChanged();
//			news_list.setAdapter(dateAdapter);
//			news_list.setSelection(news_list.getCount() - 1);
		}
	}
	/**
	 * 发送文本
	 * 
	 * @param s
	 */
	private void sendText(String content) {
//		if (content.length() > 0) {
//			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//			// 如果是群聊，设置chattype,默认是单聊
//			if (chatType == CHATTYPE_GROUP)
//				message.setChatType(ChatType.GroupChat);
//			TextMessageBody txtBody = new TextMessageBody(content);
//			// 设置消息body
//			message.addBody(txtBody);
//			// 设置要发给谁,用户username或者群聊groupid
//			message.setReceipt(toChatUsername);
//			// 把messgage加到conversation中
//			conversation.addMessage(message);
//			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//			adapter.refreshSelectLast();
//			chat_text.setText("");
//
//			setResult(RESULT_OK);
//
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_back:
			finish();
			break;
		case R.id.chat_send:// 发送消息
			int id = v.getId();
			if (id == R.id.chat_text) {
				String s = chat_text.getText().toString();
				sendText(s);
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

	private void init() {
		back.setOnClickListener(this);
		xiangqing.setOnClickListener(this);
		chat_button1.setOnClickListener(this);
		chat_button2.setOnClickListener(this);
		chat_button3.setOnClickListener(this);
		chat_button4.setOnClickListener(this);
		chat_button5.setOnClickListener(this);
		chat_send.setOnClickListener(this);
		chat_head.setText("逗比");
		
		List<EMGroup> grouplist = EMGroupManager.getInstance().getAllGroups();//获取群聊列表
		System.out.println(grouplist.size()+"SSSSSSSSSSSSS");
		for (int i = 0; i < grouplist.size(); i++) {
			String groupId = grouplist.get(i).getGroupId();
			System.out.println(groupId+"MMM");
		}
		try {
			String groupId = grouplist.get(3).getGroupId();
			//根据群聊ID从服务器获取群聊信息
			EMGroup group =EMGroupManager.getInstance().getGroupFromServer(groupId);
			List<String> s = group.getMembers();//获取群成员
			String ss = group.getOwner();//获取群主
			System.out.println(ss+"LLLLLLLLLLLLLL"+s);
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		//保存获取下来的群聊信息
//		EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
//		group.getMembers();//获取群成员
//		group.getOwner();//获取群主
//		
//		if (grouplist.size()==0) {
//			String[] s= {"cysj13","cysj14","cysj9","cysj12"};
//			//groupName：要创建的群聊的名称
//			//desc：群聊简介
//			//members：群聊成员,为空时这个创建的群组只包含自己
//			//allowInvite:是否允许群成员邀请人进群
//			try {
//				EMGroupManager.getInstance().createPrivateGroup("群聊", "什么", s,true);//需异步处理
//				//群主加人调用此方法
////			EMGroupManager.getInstance().addUsersToGroup("cysj14", );//需异步处理
//				//私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
////			EMGroupManager.getInstance().inviteUser(groupId, newmembers, null);//需异步处理
//			} catch (EaseMobException e) {
//				e.printStackTrace();
//			}
//			
//			//前一种方法创建的群聊默认最大群聊用户数为200，传入maxUsers后设置自定义的最大用户数，最大为2000
////		EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members,allowInvite,maxUsers);//需异步处理
//		}
	}

}
