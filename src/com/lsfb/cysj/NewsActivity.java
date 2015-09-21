package com.lsfb.cysj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.utils.ExpressionUtil;
/**
 * 私信，个人聊天界面
 * @author Administrator
 *
 */
public class NewsActivity extends FragmentActivity implements OnClickListener {
	@ViewInject(R.id.news_list)
	private ListView news_list;
	@ViewInject(R.id.news_back)
	private LinearLayout back;
	@ViewInject(R.id.news_popw)
	private RelativeLayout popw;
	/**
	 * news_yuyin 语音 yuyin
	 */
	@ViewInject(R.id.news_yuyin)
	private LinearLayout yuyin;
	/**
	 * news_biaoqing biaoqing 表情
	 */
	@ViewInject(R.id.news_biaoqing)
	private ImageButton biaoqing;
	/**
	 * news_more more 更多功能
	 */
	@ViewInject(R.id.news_more)
	private LinearLayout more;
	/**
	 * news_send send 发送
	 */
	@ViewInject(R.id.news_send)
	private Button send;
	/**
	 * news_text 发送内容
	 */
	@ViewInject(R.id.news_text)
	private EditText news_text;
	/**
	 * btn_take_picture camera 拍照
	 */
	@ViewInject(R.id.btn_take_picture)
	private ImageView camera;
	/**
	 * btn_picture 照片
	 */
	@ViewInject(R.id.btn_picture)
	private ImageView btn_picture;
	/**
	 * btn_shipin 视频
	 */
	@ViewInject(R.id.btn_shipin)
	private ImageView btn_shipin;
	/**
	 * btn_yuyin 语音
	 */
	@ViewInject(R.id.btn_yuyin)
	private ImageView btn_yuyin;
	/**
	 * ll_face_container 表情
	 */
	@ViewInject(R.id.ll_face_container)
	private LinearLayout ll_face_container;
	/**
	 * ll_btn_container 更多功能
	 */
	@ViewInject(R.id.ll_btn_container)
	private LinearLayout ll_btn_container;

	/**
	 * news_vpager 表情库
	 */
	@ViewInject(R.id.news_vpager)
	private ViewPager news_vpager;
	/**
	 * 按住说话
	 */
	@ViewInject(R.id.btn_speak)
	private Button btn_speak;
	/**
	 * 输入文字
	 */
	@ViewInject(R.id.fl_shuruText)
	private FrameLayout fl_shuruText;
	PopupWindow popupwindow;
	Intent intent;
	ImageView sendshibai;
	private EMConversation conversation;
	private String toChatUsername = "";
	private DateAdapter dateAdapter;
	private String otherheadaddress;
	private Boolean isOpenll_face_container;// 是否打开表情
	private Boolean isOpenll_btn_container;// 是否更多功能
	private Boolean isOpenll_btn_speak;// 是否按住说话
	private Boolean isOpenll_mPlayer;// 是否播放录音
	// private MediaRecorder Recorder = new MediaRecorder();// 录音用到的类
	boolean isLongClick = false;
	MediaRecorder mediaRecorder;
	String strVoicepath;
	MediaPlayer mPlayer;
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mPlayer != null) {
			mPlayer.stop();
		}

		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ViewUtils.inject(this);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init();
		conversation = EMChatManager.getInstance().getConversation(
				toChatUsername);
		dateAdapter = new DateAdapter();
		news_list.setAdapter(dateAdapter);

		// 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
		NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		EMChat.getInstance().setAppInited();
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
			System.out.println(message+"LLLLLLLLLLL");
			dateAdapter.notifyDataSetChanged();
			news_list.setAdapter(dateAdapter);
			news_list.setSelection(news_list.getCount() - 1);
		}
	}

	private void init() {
		isOpenll_face_container = false;
		isOpenll_btn_container = false;
		isOpenll_btn_speak = false;
		isOpenll_mPlayer = false;
		back.setOnClickListener(this);
		popw.setOnClickListener(this);
		yuyin.setOnClickListener(this);
		biaoqing.setOnClickListener(this);
		more.setOnClickListener(this);
		send.setOnClickListener(this);
		news_text.setOnClickListener(this);
		camera.setOnClickListener(this);
		btn_picture.setOnClickListener(this);
		btn_shipin.setOnClickListener(this);
		btn_yuyin.setOnClickListener(this);
		news_vpager.setOnClickListener(this);
		Intent intent = getIntent();
		toChatUsername = "cysj" + intent.getStringExtra("id");
//		// 从本地获取黑名单
//		List<String> blacklist = EMContactManager.getInstance()
//				.getBlackListUsernames();
//		System.out.println(blacklist
//				+ "LLLLLLLLLLLLOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
//		String name = blacklist.get(0);
//		if (toChatUsername.equals(name)) {
//			Toast.makeText(getApplicationContext(), toChatUsername+"你已经把对方加入黑名单"+name, Toast.LENGTH_SHORT).show();
//			try {
//				EMContactManager.getInstance().deleteUserFromBlackList(toChatUsername);//需异步处理
//			} catch (EaseMobException e) {
//				e.printStackTrace();
//			}
//			finish();
//		}
		otherheadaddress = intent.getStringExtra("headaddress");
		btn_speak.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// MainActivity.this.flag = "talk";
				// MainActivity.this.setImageButtonBackground();
				System.out.println("长按。。。。。。。。。。。");
				isLongClick = true;
				// 开始录音
				record();
				return true;
			}
		});
		btn_speak.setOnTouchListener(new MyClickListener());
	}

	class MyClickListener implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			if (isLongClick)
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					// flag = "listen";
					// setImageButtonBackground();
					System.out.println("抬起。。。。。。。。。。。。。。。。");
					mediaRecorder.stop();
					isLongClick = false;

					EMConversation conversation = EMChatManager.getInstance()
							.getConversation(toChatUsername);
					EMMessage message = EMMessage
							.createSendMessage(EMMessage.Type.VOICE);
					// 如果是群聊，设置chattype,默认是单聊
					// message.setChatType(ChatType.GroupChat);
					VoiceMessageBody body = new VoiceMessageBody(new File(
							strVoicepath), 1000);
					message.addBody(body);
					message.setReceipt(toChatUsername);
					conversation.addMessage(message);
					// 刷新界面
					dateAdapter.notifyDataSetChanged();
					news_list.setAdapter(dateAdapter);
					news_list.setSelection(news_list.getCount() - 1);
					EMChatManager.getInstance().sendMessage(message,
							new EMCallBack() {

								@Override
								public void onError(int arg0, String arg1) {
									// TODO Auto-generated method stub
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(NewsActivity.this,
													"发送失败", Toast.LENGTH_SHORT)
													.show();
											sendshibai
													.setVisibility(View.VISIBLE);
										}
									});
								}

								@Override
								public void onProgress(int arg0, String arg1) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(NewsActivity.this,
													"发送成功", Toast.LENGTH_SHORT)
													.show();
										}
									});
								}
							});
					break;
				default:
					break;
				}
			return false;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK
				&& null != data) {

			// 根据URI获得图片真实的路径
			Uri selectedImage = data.getData();
			ContentResolver cr = this.getContentResolver();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(selectedImage, proj, null, null, null);
			// 按我个人理解 这个是获得用户选择的图片的索引值
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			// 最后根据索引值获取图片路径
			String path = cursor.getString(column_index);

			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(toChatUsername);
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.IMAGE);
			// //如果是群聊，设置chattype,默认是单聊
			// message.setChatType(ChatType.GroupChat);

			ImageMessageBody body = new ImageMessageBody(new File(path));
			// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
			// body.setSendOriginalImage(true);
			message.addBody(body);
			message.setReceipt(toChatUsername);
			conversation.addMessage(message);
			// 刷新界面
			dateAdapter.notifyDataSetChanged();
			news_list.setAdapter(dateAdapter);
			news_list.setSelection(news_list.getCount() - 1);
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送失败",
									Toast.LENGTH_SHORT).show();
							sendshibai.setVisibility(View.VISIBLE);
						}
					});
				}

				@Override
				public void onProgress(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送成功",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		} else if (requestCode == 2) {

			int duration = data.getIntExtra("dur", 0);
			String videoPath = data.getStringExtra("path");
			System.out.println(duration + "SSSSSSSSSSSSSSSSSSSSSSSS"
					+ videoPath);
			File file = new File(PathUtil.getInstance().getImagePath(),
					"thvideo" + System.currentTimeMillis());
			Bitmap bitmap = null;
			FileOutputStream fos = null;
			try {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
				if (bitmap == null) {
					EMLog.d("chatactivity",
							"problem load video thumbnail bitmap,use default icon");
					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.uploadimg_fbvideo);
				}
				fos = new FileOutputStream(file);

				bitmap.compress(CompressFormat.JPEG, 100, fos);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fos = null;
				}
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}

			}
			// sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

			// // 视频播放
			// Uri uri = data.getData();
			// // url = uri.toString();
			// Cursor c = getContentResolver().query(uri,new String[] {
			// MediaStore.MediaColumns.DATA }, null, null,
			// null);// 根据返回的URI，查找数据库，获取视频的路径
			// String url = null;
			// if (c != null && c.moveToFirst()) {
			// url = c.getString(0);
			// System.err.println(url);
			// } else {
			// return;
			// }
			// int duration = data.getIntExtra("dur", 0);
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(toChatUsername);
			// 创建一个文件消息
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VIDEO);
			// 如果是群聊，设置chattype,默认是单聊
			// if (chatType == CHATTYPE_GROUP)
			// message.setChatType(ChatType.GroupChat);
			// 设置接收人的username
			message.setReceipt(toChatUsername);
			// add message body
			// File file = new File(url);
			File videoFile = new File(videoPath);
			VideoMessageBody body = new VideoMessageBody(videoFile,
					file.getAbsolutePath(), duration / 1000, videoFile.length());
			// VideoMessageBody body = new
			// VideoMessageBody(file,file.getAbsolutePath(), duration / 1000,
			// file.length());
			System.out.println("videoFile:" + videoFile + "        thumbPath:"
					+ file.getAbsolutePath() + "             length:"
					+ duration / 1000 + "           videoFile.length:"
					+ videoFile.length());
			message.addBody(body);
			conversation.addMessage(message);
			// 刷新界面
			dateAdapter.notifyDataSetChanged();
			news_list.setAdapter(dateAdapter);
			news_list.setSelection(news_list.getCount() - 1);
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送失败",
									Toast.LENGTH_SHORT).show();
							sendshibai.setVisibility(View.VISIBLE);
						}
					});
				}

				@Override
				public void onProgress(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送成功",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			// 接收消息
		} else if (requestCode == 3 && resultCode == Activity.RESULT_OK
				&& null != data) {
			// 拍照
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			new DateFormat();
			String name = DateFormat.format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			Toast.makeText(this, name, Toast.LENGTH_LONG).show();
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

			FileOutputStream b = null;
			File file = getFilePath("/sdcard/Image/", name);

			String path = file.getAbsolutePath();
			try {
				b = new FileOutputStream(path);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(toChatUsername);
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.IMAGE);
			// //如果是群聊，设置chattype,默认是单聊
			// message.setChatType(ChatType.GroupChat);

			ImageMessageBody body = new ImageMessageBody(new File(path));
			// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
			// body.setSendOriginalImage(true);
			message.addBody(body);
			message.setReceipt(toChatUsername);
			conversation.addMessage(message);
			// 刷新界面
			dateAdapter.notifyDataSetChanged();
			news_list.setAdapter(dateAdapter);
			news_list.setSelection(news_list.getCount() - 1);
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送失败",
									Toast.LENGTH_SHORT).show();
							sendshibai.setVisibility(View.VISIBLE);
						}
					});
				}

				@Override
				public void onProgress(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送成功",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		}
	}

	private void sendVideo(final String filePath, final String thumbPath,
			final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VIDEO);
			// 如果是群聊，设置chattype,默认是单聊
			// if (chatType == CHATTYPE_GROUP){
			// message.setChatType(ChatType.GroupChat);
			// }else if(chatType == CHATTYPE_CHATROOM){
			// message.setChatType(ChatType.ChatRoom);
			// }
			String to = toChatUsername;
			message.setReceipt(to);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
					length, videoFile.length());
			message.addBody(body);
			System.out.println("videoFile" + videoFile + "          thumbPath"
					+ thumbPath + "             length" + length
					+ "                   videoFile.length()"
					+ videoFile.length());
			conversation.addMessage(message);
			news_list.setAdapter(dateAdapter);
			dateAdapter.notifyDataSetChanged();
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_yuyin:
			// 语音
			if (!isOpenll_btn_speak) {
				btn_speak.setVisibility(View.VISIBLE);
				fl_shuruText.setVisibility(View.GONE);
				isOpenll_btn_speak = true;

			} else {
				btn_speak.setVisibility(View.GONE);
				fl_shuruText.setVisibility(View.VISIBLE);
				isOpenll_btn_speak = false;
			}

			break;
		case R.id.btn_yuyin:// 语音
			if (!isOpenll_btn_speak) {
				btn_speak.setVisibility(View.VISIBLE);
				fl_shuruText.setVisibility(View.GONE);
				isOpenll_btn_speak = true;
	
			} else {
				btn_speak.setVisibility(View.GONE);
				fl_shuruText.setVisibility(View.VISIBLE);
				isOpenll_btn_speak = false;
			}
			break;
		case R.id.btn_speak:
			// 按住说话

			break;
		case R.id.btn_shipin:
			// 视频
			// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			// intent.setType("video/*");
			// intent.addCategory(Intent.CATEGORY_OPENABLE);
			// startActivityForResult(Intent.createChooser(intent,
			// "请选择一个要上传的文件"),
			// 2);
			Intent intent = new Intent(NewsActivity.this,
					ImageGridActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.btn_picture:
			// 图片
			Intent picture = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(picture, 1);
			break;
		case R.id.btn_take_picture:
			// 拍照
			Intent intentcamer = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intentcamer, 3);
			break;
		case R.id.news_more:
			// 打开更多功能
			if (!isOpenll_btn_container) {
				ll_btn_container.setVisibility(View.VISIBLE);
				isOpenll_btn_container = true;
				ll_face_container.setVisibility(View.GONE);
				isOpenll_face_container = false;
			} else {
				ll_btn_container.setVisibility(View.GONE);
				isOpenll_btn_container = false;
			}
			break;
		case R.id.news_biaoqing:
			// 打开表情
			if (!isOpenll_face_container) {
				createExpressionDialog();
				// ll_face_container.setVisibility(View.VISIBLE);
				isOpenll_face_container = true;
				ll_btn_container.setVisibility(View.GONE);
				isOpenll_btn_container = false;
			} else {
				ll_face_container.setVisibility(View.GONE);
				isOpenll_face_container = false;
			}

			break;
		case R.id.news_back:
			finish();
			break;
		case R.id.news_popw:
			if (null != popupwindow) {
				popupwindow.dismiss();
				return;
			} else {
				initPopuptWindow();
				popupwindow.showAsDropDown(popw, 0, 0);
				// popupwindow.showAtLocation(popw, Gravity.RIGHT, 0, 0);
			}
			break;
		case R.id.news_send:
			// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(toChatUsername);
			// 创建一条文本消息
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// // 如果是群聊，设置chattype,默认是单聊
			// message.setChatType(ChatType.GroupChat);
			// 设置消息body
			TextMessageBody txtBody = new TextMessageBody(news_text.getText()
					.toString());
			message.addBody(txtBody);
			// 设置接收人
			message.setReceipt(toChatUsername);
			// 把消息加入到此会话对象中
			conversation.addMessage(message);
			// 刷新界面
			dateAdapter.notifyDataSetChanged();
			news_list.setAdapter(dateAdapter);
			news_list.setSelection(news_list.getCount() - 1);
			news_text.setText("");
			// 发送消息
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送失败",
									Toast.LENGTH_SHORT).show();
							sendshibai.setVisibility(View.VISIBLE);
						}
					});

				}

				@Override
				public void onProgress(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(NewsActivity.this, "发送成功",
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			});
			break;
		default:
			break;
		}
	}

	private void initPopuptWindow() {
		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(
				R.layout.popuptwindowheimingdan, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		// popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}

				return false;
			}
		});
		Button button1 = (Button) customView
				.findViewById(R.id.popuptwindowheimingdan_button);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
				// 我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
				try {
					EMContactManager.getInstance().addUserToBlackList(
							toChatUsername, false);// 需异步处理
					popupwindow.dismiss();
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public class DateAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return conversation.getAllMessages().size();
		}

		@Override
		public Object getItem(int i) {
			return conversation.getAllMessages().get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int position, View v, ViewGroup arg2) {
			// TODO Auto-generated method stub
			EMMessage message = conversation.getAllMessages().get(position);

			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 这是接受方
				v = LayoutInflater.from(NewsActivity.this).inflate(
						R.layout.newdemo, null);
				ImageView imagehead = (ImageView) v
						.findViewById(R.id.ivnewdemo);
				ImageView shibai = (ImageView) v.findViewById(R.id.shibai);
				BitmapUtils bitmapUtils = new BitmapUtils(NewsActivity.this);
				bitmapUtils.display(imagehead, otherheadaddress);
				if (message.getType() == EMMessage.Type.VOICE) {
					final VoiceMessageBody voiceMessageBody = (VoiceMessageBody) message
							.getBody();
					ImageView imageViewVoice = (ImageView) v
							.findViewById(R.id.newsVoiec);
					imageViewVoice.setVisibility(View.VISIBLE);

					imageViewVoice.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							// MediaPlayer mPlayer = new MediaPlayer();
							try {
								if (!isOpenll_mPlayer) {
									mPlayer = new MediaPlayer();
									mPlayer.setDataSource(voiceMessageBody
											.getLocalUrl());
									mPlayer.prepare();
									mPlayer.start();
									isOpenll_mPlayer = true;
								} else {
									mPlayer.stop();
									isOpenll_mPlayer = false;
								}
							} catch (IOException e) {
								Log.e("播放失败", "播放失败");
							}
						}
					});
				}
				if (message.getType() == EMMessage.Type.TXT) {
					TextMessageBody textMessageBody = (TextMessageBody) message
							.getBody();
					TextView tvcontext = (TextView) v
							.findViewById(R.id.tvnewdemo);
					tvcontext.setVisibility(View.VISIBLE);
					String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(NewsActivity.this,
									textMessageBody.getMessage(), zhengze);
					tvcontext.setText(spannableString);
				}
				if (message.getType() == EMMessage.Type.VIDEO) {
					final VideoMessageBody normalFileMessageBody = (VideoMessageBody) message
							.getBody();
					ImageView ivvideo = (ImageView) v
							.findViewById(R.id.newvideo);
					ivvideo.setVisibility(View.VISIBLE);
					ivvideo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							// Uri uri = Uri.parse(normalFileMessageBody
							// .getRemoteUrl());
							// Intent intent = new Intent(NewsActivity.this,
							// VideoActivity.class);
							// System.err.println("视频地址："
							// + normalFileMessageBody.getThumbnailUrl());
							// intent.putExtra("uri", uri);
							// startActivity(intent);
							Uri uri = Uri.parse(normalFileMessageBody
									.getRemoteUrl());
							System.out.println(uri + "BBBBB");
							Intent intent = new Intent(Intent.ACTION_VIEW);

							intent.setDataAndType(uri, "video/mp4");

							startActivity(intent);
						}
					});

				}
				if (message.getType() == EMMessage.Type.IMAGE) {
					final ImageMessageBody imageMessageBody = (ImageMessageBody) message
							.getBody();
					ImageView tvpicture = (ImageView) v
							.findViewById(R.id.ivnewpicture);
					tvpicture.setVisibility(View.VISIBLE);
					bitmapUtils.display(tvpicture,
							imageMessageBody.getThumbnailUrl());
					tvpicture.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							intent = new Intent(NewsActivity.this,
									NewsImageShowActivity.class);
							intent.putExtra("imgs",
									imageMessageBody.getThumbnailUrl());
							startActivity(intent);
						}
					});
				}

			} else {
				// 这是发送方
				v = LayoutInflater.from(NewsActivity.this).inflate(
						R.layout.newdemos, null);
				ImageView imageheads = (ImageView) v
						.findViewById(R.id.ivnewdemos);
				BitmapUtils bitmapUtils = new BitmapUtils(NewsActivity.this);
				bitmapUtils.display(imageheads, ImageAddress.Stringhead
						+ IsTrue.Stringimage);
				if (message.getType() == EMMessage.Type.VOICE) {
					final VoiceMessageBody voiceMessageBody = (VoiceMessageBody) message
							.getBody();
					ImageView imageViewVoice = (ImageView) v
							.findViewById(R.id.newsVoiecs);
					imageViewVoice.setVisibility(View.VISIBLE);
					imageViewVoice.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub

							try {
								if (!isOpenll_mPlayer) {
									mPlayer = new MediaPlayer();
									mPlayer.setDataSource(voiceMessageBody
											.getLocalUrl());
									mPlayer.prepare();
									mPlayer.start();
									isOpenll_mPlayer = true;
								} else {
									mPlayer.stop();
									isOpenll_mPlayer = false;
								}
							} catch (IOException e) {
								Log.e("播放失败", "播放失败");
							}
						}
					});

				}
				if (message.getType() == EMMessage.Type.TXT) {
					TextMessageBody textMessageBody = (TextMessageBody) message.getBody();
					TextView tvcontext = (TextView) v
							.findViewById(R.id.tvnewdemos);
					sendshibai = (ImageView) v.findViewById(R.id.sendshibai);
					tvcontext.setVisibility(View.VISIBLE);
					String zhengze = "f0[0-9]{2}|f10[0-7]"; //
					// 正则表达式，用来判断消息内是否有表情
					// String zhengze = "bq[0-9]{2}|"; // 正则表达式，用来判断消息内是否有表情
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(NewsActivity.this,
									textMessageBody.getMessage(), zhengze);
					tvcontext.setText(spannableString);
				}
				if (message.getType() == EMMessage.Type.VIDEO) {
					final VideoMessageBody normalFileMessageBody = (VideoMessageBody) message
							.getBody();
					ImageView ivvideos = (ImageView) v
							.findViewById(R.id.newvideos);
					ivvideos.setVisibility(View.VISIBLE);
					ivvideos.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(NewsActivity.this,
									VideoActivity.class);
							intent.putExtra("uri",
									normalFileMessageBody.getLocalUrl());
							startActivity(intent);
						}
					});

				}
				if (message.getType() == EMMessage.Type.IMAGE) {

					final ImageMessageBody imageMessageBody = (ImageMessageBody) message
							.getBody();
					ImageView tvpicture = (ImageView) v
							.findViewById(R.id.ivnewpictures);
					tvpicture.setVisibility(View.VISIBLE);
					bitmapUtils.display(tvpicture,
							imageMessageBody.getLocalUrl());
					tvpicture.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							intent = new Intent(NewsActivity.this,
									NewsImageShowActivity.class);
							System.err.println("发送的地址:"
									+ imageMessageBody.getLocalUrl());
							intent.putExtra("imgs",
									imageMessageBody.getLocalUrl());
							startActivity(intent);
						}
					});
				}

			}
			return v;
		}

	}

	public File getFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 开始录制
	 */
	private void record() {
		mediaRecorder = new MediaRecorder();
		/**
		 * mediaRecorder.setAudioSource设置声音来源。
		 * MediaRecorder.AudioSource这个内部类详细的介绍了声音来源。
		 * 该类中有许多音频来源，不过最主要使用的还是手机上的麦克风，MediaRecorder.AudioSource.MIC
		 */
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		/**
		 * mediaRecorder.setOutputFormat代表输出文件的格式。该语句必须在setAudioSource之后，
		 * 在prepare之前。
		 * OutputFormat内部类，定义了音频输出的格式，主要包含MPEG_4、THREE_GPP、RAW_AMR……等。
		 */
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		/**
		 * mediaRecorder.setAddioEncoder()方法可以设置音频的编码
		 * AudioEncoder内部类详细定义了两种编码：AudioEncoder.DEFAULT、AudioEncoder.AMR_NB
		 */
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		/**
		 * 设置录音之后，保存音频文件的位置
		 */
		new DateFormat();
		// 文件夹是否存在 不存在就建
		makeRootDirectory("/sdcard/Voice/");
		strVoicepath = "/sdcard/Voice/"
				+ DateFormat.format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA)) + ".amr";
		mediaRecorder.setOutputFile(strVoicepath);

		/**
		 * 调用start开始录音之前，一定要调用prepare方法。
		 */
		try {
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/***
	 * 此外，还有和MediaRecorder有关的几个参数与方法，我们一起来看一下： sampleRateInHz
	 * :音频的采样频率，每秒钟能够采样的次数，采样率越高，音质越高。
	 * 给出的实例是44100、22050、11025但不限于这几个参数。例如要采集低质量的音频就可以使用4000、8000等低采样率
	 * 
	 * channelConfig ：声道设置：android支持双声道立体声和单声道。MONO单声道，STEREO立体声
	 * 
	 * recorder.stop();停止录音 recorder.reset(); 重置录音 ，会重置到setAudioSource这一步
	 * recorder.release(); 解除对录音资源的占用
	 */
	// 表情
	/**
	 * 创建一个表情选择对话框
	 */
	private void createExpressionDialog() {
		builder = new Dialog(NewsActivity.this);
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
				ImageSpan imageSpan = new ImageSpan(NewsActivity.this, bitmap);
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
				news_text.append(spannableString);
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
}
