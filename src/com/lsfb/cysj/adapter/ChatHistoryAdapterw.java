package com.lsfb.cysj.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chatuidemo.utils.UsersUtils;
import com.easemob.util.EMLog;
import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.base.MConversationHistory;
import com.lsfb.cysj.utils.Constant;
import com.lsfb.cysj.utils.SmileUtils;
import com.lsfb.cysj.view.SliderView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class ChatHistoryAdapterw extends BaseAdapter {

	private static final String TAG = "ChatAllHistoryAdapterw";
	private LayoutInflater mInflater;
	private Context context;
	private BitmapUtils bitmapUtils;
	private List<EMConversation> conversationList;
	private SimpleDateFormat sdf;

	private List<MConversationHistory> mMessageItems;

	public ChatHistoryAdapterw(Activity activity,
			List<EMConversation> conversationList,
			List<MConversationHistory> mMessageItems, BitmapUtils bitmapUtils) {
		this.mInflater = activity.getLayoutInflater();
		this.mMessageItems = mMessageItems;
		this.bitmapUtils = bitmapUtils;
		this.context = activity.getApplicationContext();
		this.conversationList = conversationList;
		sdf = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		Log.d("fuck", mMessageItems.size() + "abc");

		return mMessageItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return conversationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Log.i("zgscwjm--fuck", position + "w");
		ViewHolder holder;
		SliderView sliderView = (SliderView) convertView;
		if (sliderView == null) {
			View itemView = mInflater.inflate(R.layout.row_chat_history, null);
			sliderView = new SliderView(context);
			sliderView.setContentView(itemView);
			holder = new ViewHolder(sliderView);
			sliderView.setTag(holder);
		} else {
			holder = (ViewHolder) sliderView.getTag();
		}

		// 得到数据列

		MConversationHistory item = mMessageItems.get(position);
		sliderView.shrink();

		// 设置数据
		

		EMConversation conversation = (EMConversation) getItem(position);
		// 获取用户username或者群组groupid
		String username = conversation.getUserName();
		holder.name.setText(mMessageItems != null ? mMessageItems.get(position)
				.getHuanxname() : username);
		if (conversation.getType() == EMConversationType.GroupChat) {
			// 群聊消息，显示群聊头像
			// holder.avatar.setImageResource(R.drawable.group_icon);
			if (mMessageItems != null && mMessageItems.size() > 0) {

				bitmapUtils.display(holder.avatar, ImageAddress.cbit
						+ mMessageItems.get(position).getImg());
			} else {
				holder.avatar.setImageResource(R.drawable.group_icon);
			}

		} else if (conversation.getType() == EMConversationType.Chat) {

			bitmapUtils.display(holder.avatar, ImageAddress.Stringhead
					+ mMessageItems.get(position).getImg());
			EMChatRoom room = EMChatManager.getInstance().getChatRoom(username);

			holder.name.setText(mMessageItems != null ? mMessageItems.get(
					position).getHuanxname() : username);
		} else {
			UsersUtils.setUserAvatar(context, username, holder.avatar);
			if (username.equals(Constant.GROUP_USERNAME)) {
				holder.name.setText("群聊");

			} else if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
				holder.name.setText("申请与通知");
			}
		}

		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			holder.unreadLabel.setText(String.valueOf(conversation
					.getUnreadMsgCount()));
			holder.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			holder.unreadLabel.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			holder.message.setText(
					SmileUtils.getSmiledText(context,
							getMessageDigest(lastMessage, context)),
					BufferType.SPANNABLE);

			holder.time.setText(sdf.format(new Date(lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				holder.msgState.setVisibility(View.VISIBLE);
			} else {
				holder.msgState.setVisibility(View.GONE);
			}
		}
		
		holder.deleteHolder.setOnClickListener(onClickListener);
		return sliderView;
	}

	public static class ViewHolder {
		public ImageView avatar;
		public TextView unreadLabel;
		public TextView name;
		public TextView time;
		public ImageView msgState;
		public TextView message;
		public ViewGroup deleteHolder;
		public RelativeLayout list_item_layout;
		public TextView deletTextView;

		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
			name = (TextView) view.findViewById(R.id.name);
			unreadLabel = (TextView) view.findViewById(R.id.unread_msg_number);
			message = (TextView) view.findViewById(R.id.message);
			time = (TextView) view.findViewById(R.id.time);
			avatar = (ImageView) view.findViewById(R.id.avatar);
			msgState = (ImageView) view.findViewById(R.id.msg_state);
			list_item_layout = (RelativeLayout) view
					.findViewById(R.id.list_item_layout);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
			deletTextView=(TextView) view.findViewById(R.id.delete);
//			deleteHolder.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
			
			
		}

	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture)
					+ imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call)
						+ txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			EMLog.e(TAG, "unknow type");
			return "";
		}

		return digest;
	}

public	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(context, "this is del", Toast.LENGTH_SHORT).show();
		}

	};

}
