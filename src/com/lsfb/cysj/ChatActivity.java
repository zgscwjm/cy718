package com.lsfb.cysj;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
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
		init();
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
	}

}
