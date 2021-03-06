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
package com.lsfb.cysj.adapter;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;




import cn.jpush.android.api.m;












//import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chatuidemo.utils.UsersUtils;
import com.easemob.util.DateUtils;
//import com.easemob.chatuidemo.Constant;
//import com.easemob.chatuidemo.DemoHXSDKHelper;
//import com.easemob.chatuidemo.domain.RobotUser;
//import com.easemob.chatuidemo.utils.DateUtils;
//import com.easemob.chatuidemo.utils.SmileUtils;
//import com.easemob.chatuidemo.utils.UserUtils;
import com.easemob.util.EMLog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.base.HXSDKHelper;
import com.lsfb.cysj.base.MConversationHistory;
import com.lsfb.cysj.utils.Constant;
import com.lsfb.cysj.utils.SmileUtils;
//import com.lsfb.cysj.utils.UserUtils;
import com.umeng.socialize.utils.Log;
import com.lsfb.cysj.view.SliderView;
/**
 * 显示所有聊天记录adpater
 * 
 */
public class ChatAllHistoryAdapter extends ArrayAdapter<EMConversation> {

	private static final String TAG = "ChatAllHistoryAdapter";
	private LayoutInflater inflater;
	private List<EMConversation> conversationList;
	private List<EMConversation> copyConversationList;
	private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;
	BitmapUtils bitmapUtils;
	SimpleDateFormat sdf ;  
   private String HeadImagePath="http://211.149.200.125/Public/images/cbit/";
    private List<MConversationHistory> mlist;

	public ChatAllHistoryAdapter(Context context, int textViewResourceId, List<EMConversation> objects,List<MConversationHistory> list,BitmapUtils bitmapUtils) {
		super(context, textViewResourceId, objects);
		this.conversationList = objects;
		copyConversationList = new ArrayList<EMConversation>();
		copyConversationList.addAll(objects);
		inflater = LayoutInflater.from(context);
		mlist=list;
		this.bitmapUtils=bitmapUtils;
		sdf= new SimpleDateFormat("yyyy-MM-dd");
		
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		SliderView slideView = (SliderView) convertView;
		if (slideView == null) {
//			convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
			View itemView = inflater.inflate(R.layout.row_chat_history,null);
			itemView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			slideView=new SliderView(this.getContext());
			slideView.setContentView(itemView);
			holder=new ViewHolder(slideView);
			slideView.setTag(holder);
		}else {
			holder=(ViewHolder) slideView.getTag();
		}
		slideView.shrink();
		// holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			
			
			
			
		}
//		if (position % 2 == 0) {
//			holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem);
//		} else {
//			holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem_grey);
//		}

		// 获取与此用户/群组的会话
		EMConversation conversation = getItem(position);
		// 获取用户username或者群组groupid
		String username = conversation.getUserName();
		if (conversation.getType() == EMConversationType.GroupChat) {
			// 群聊消息，显示群聊头像
			//holder.avatar.setImageResource(R.drawable.group_icon);
			if(mlist!=null&&mlist.size()>0)
			{
			
			
			bitmapUtils.display(holder.avatar,
					ImageAddress.cbit + mlist.get(position).getImg());
			}else {
				holder.avatar.setImageResource(R.drawable.group_icon);
			}
			
//			EMGroup group = EMGroupManager.getInstance().getGroup(username);
			
			
//			holder.name.setText(group != null ? group.getGroupName() : username);
		//holder.name.setText(mlist != null ? mlist.get(position).getHuanxname(): username);
	//		holder.name.setText("abcd");
		
		} else if(conversation.getType() == EMConversationType.Chat){

			bitmapUtils.display(holder.avatar,
					ImageAddress.Stringhead + mlist.get(position).getImg());
			//  holder.avatar.setImageResource(R.drawable.group_icon);
            EMChatRoom room = EMChatManager.getInstance().getChatRoom(username);

    		holder.name.setText(mlist != null ? mlist.get(position).getHuanxname(): username);
            //   holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
		}else {
		    UsersUtils.setUserAvatar(getContext(), username, holder.avatar);
			if (username.equals(Constant.GROUP_USERNAME)) {
				holder.name.setText("群聊");

			} else if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
				holder.name.setText("申请与通知");
			}
		//注
//			Map<String,RobotUser> robotMap=((DemoHXSDKHelper)HXSDKHelper.getInstance()).getRobotList();
//			if(robotMap!=null&&robotMap.containsKey(username)){
//				String nick = robotMap.get(username).getNick();
//				if(!TextUtils.isEmpty(nick)){
//					holder.name.setText(nick);
//				}else{
//					holder.name.setText(username);
//				}
//			}else{
//				UsersUtils.setUserNick(username, holder.name);
//			}
		}

		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
			holder.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			holder.unreadLabel.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			holder.message.setText(SmileUtils.getSmiledText(getContext(), getMessageDigest(lastMessage, (this.getContext()))),
					BufferType.SPANNABLE);

			
			// DateFormatUtils.format(d, pattern);
		    // holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
			
			
			holder.time.setText( sdf.format(new Date(lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
				holder.msgState.setVisibility(View.VISIBLE);
			} else {
				holder.msgState.setVisibility(View.GONE);
			}
		}

		return convertView;
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
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture) + imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息			
//			if(((DemoHXSDKHelper)HXSDKHelper.getInstance()).isRobotMenuMessage(message)){
//				digest = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getRobotMenuMessageDigest(message);
//			}else 
				if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL,false)){
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
			}else{
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

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		ImageView avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;

		
		
		public ViewGroup deleteHolder;
		
		
		
		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
		
	//	holder = new ViewHolder();
		name = (TextView) view.findViewById(R.id.name);
		unreadLabel = (TextView) view.findViewById(R.id.unread_msg_number);
		message = (TextView) view.findViewById(R.id.message);
		time = (TextView) view.findViewById(R.id.time);
		avatar = (ImageView) view.findViewById(R.id.avatar);
		msgState = view.findViewById(R.id.msg_state);
		list_item_layout = (RelativeLayout) view.findViewById(R.id.list_item_layout);
		deleteHolder=(ViewGroup) view.findViewById(R.id.holder);
		
	//	convertView.setTag(holder);
		}
	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}
	
	

	@Override
	public Filter getFilter() {
		if (conversationFilter == null) {
			conversationFilter = new ConversationFilter(conversationList);
		}
		return conversationFilter;
	}
	
	private class ConversationFilter extends Filter {
		List<EMConversation> mOriginalValues = null;

		public ConversationFilter(List<EMConversation> mList) {
			mOriginalValues = mList;
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				mOriginalValues = new ArrayList<EMConversation>();
			}
			if (prefix == null || prefix.length() == 0) {
				results.values = copyConversationList;
				results.count = copyConversationList.size();
			} else {
				String prefixString = prefix.toString();
				final int count = mOriginalValues.size();
				final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

				for (int i = 0; i < count; i++) {
					final EMConversation value = mOriginalValues.get(i);
					String username = value.getUserName();
					
					EMGroup group = EMGroupManager.getInstance().getGroup(username);
					if(group != null){
						username = group.getGroupName();
					}

					// First match against the whole ,non-splitted value
					if (username.startsWith(prefixString)) {
						newValues.add(value);
					} else{
						  final String[] words = username.split(" ");
	                        final int wordCount = words.length;

	                        // Start at index 0, in case valueText starts with space(s)
	                        for (int k = 0; k < wordCount; k++) {
	                            if (words[k].startsWith(prefixString)) {
	                                newValues.add(value);
	                                break;
	                            }
	                        }
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			conversationList.clear();
			conversationList.addAll((List<EMConversation>) results.values);
			if (results.count > 0) {
			    notiyfyByFilter = true;
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}

		}

	}
	
	
	
	
	
	@Override
	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
	    if(!notiyfyByFilter){
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
	}
}
