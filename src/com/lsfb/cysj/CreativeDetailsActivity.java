package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.JuBaoDialog;
import com.lsfb.cysj.Dialog.PingLunDialog;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.viedo.C;
import com.lsfb.cysj.viedo.HttpGetProxy;
import com.lsfb.cysj.viedo.ProxyUtils;
import com.lsfb.cysj.view.HorizontalListView;
import com.lsfb.cysj.view.LazyScrollView;
import com.lsfb.cysj.view.ListViewForScrollView;
import com.lsfb.cysj.view.MyGridView;
import com.lsfb.cysj.view.ResDialog;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 创意详情
 * @author Administrator
 *
 */
public class CreativeDetailsActivity extends Activity {
	ImageButton ibCreativeDetailsbacking;
	MyGridView gridView;// 内容区域展示图片的listVIew
	BaseAdapter image_GridView;// 内容区域展示图片的adapter
	BaseAdapter baseadapter;// 评论用的adapter
	private HorizontalListView ll_CreateDetails;// 点赞的线性布局
	ListViewForScrollView listview_comments;// 评论用的listVIew
	private LinearLayout share, jubao;// 分享
	Intent intent;
	AsyncHttpClient client;
	RequestParams params;
	private final static String TAG = "testVideoPlayer";

	private VideoView videoview;
	private MediaController mediaController;
	private HttpGetProxy proxy;
	private long startTimeMills;
	// private String proxyUrl;
	String nameid = null;// 对应某行的id
	/**
	 * title 创意题目 content:创意内容(0无内容) images:多图(0无图片) video:视频(0无视频) price:创意价值
	 * sc:会员收藏数量 count:评论数量 nickid:发布会员id nickname:发布会员昵称
	 * uimage:发布会员头像(0会员未设置头像) zhishu:发布会员总指数 numsc:2(有值存在) id：创意id num:2(有值存在)
	 * delid 删除 2能删 1 不能 zanpd:1已赞|0未赞 scpd:1已收藏|0未收藏
	 */
	String title, content, images, video, price, sc, count, nickid, nickname,
			uimage, zhishu, id, numsc, num, delid, zanpd, scpd;
	/**
	 * nimage:收藏会员头像 zhishu2:收藏会员指数 memid:收藏会员id listsc 二维数组 urls 分享链接
	 */
	String nimage, zhishu2, memid, listsc, urls;
	/**
	 * rtime:时间 wid:评论id content2:评论内容 mid:会员id image:会员头像 nickname2:会员昵称
	 * zhishu3:会员总指数 mdel:1(当前登录会员评论的信息)|0(其他会员评论的信息)注:mdel主要用于删除自己的评论
	 */
	String rtime, wid, content2, mid, image, nickname2, zhishu3, mdel;
	private ImageView imghead;// 头像
	private TextView username;// 用户名
	private TextView chuangyizhishu;// 创意指数
	private TextView type;// 创意类型
	private TextView chuangyiprice;// 创意价值
	private TextView chuangyitext;// 创意内容
	private LinearLayout duogetouxiang;// 多个头像
	private View xian, xian1, xian2, xian3, xian4;
	Dialog jiazaidialog;
	String oriVideoUrl;
	private TextView delete;// 删除
	private ImageView like;// 喜欢
	private TextView liketext;// 喜欢内容
	private ImageView pinglun;
	private TextView pingluntext;
	private ImageView down_zanimg;
	private TextView down_zantext;
	private LinearLayout down_zan;
	private LinearLayout likeall;
	private LinearLayout downxing;// 收藏星
	private ImageView downimgxing;// 收藏星
	Map<String, Object> map;
	List<Map<String, Object>> listmap;
	List<Map<String, Object>> pinglunlist;
	Map<String, Object> map2;// 下面评论
	List<Map<String, Object>> listmap2;// 下面评论
	BaseAdapter adapterlist;
	int quxiaodianzannum = 0;
	int dianzannum = 0;
	private ImageView shareimg;// 分享图标
	private LinearLayout downpinglun;// 评论
	private ImageView downpinglunimg;// 评论图标
	private LinearLayout pinglunup;// 上面的评论
	private LazyScrollView scrollview;
	String jiazhilist;// 进入页面显示是否有评论
	int numcount = 0;
	
	String shareContent;
	Dialog dialog;
	
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creative_details);
		// new File(C.getBufferDir()).mkdirs();// 创建预加载文件的文件夹
		// ProxyUtils.clearCacheFile(C.getBufferDir());// 清除前面的预加载文件
		init();
		data();
		ibCreativeDetailsbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		oriVideoUrl = null;
		ProxyUtils.clearCacheFile(C.getBufferDir());// 清除前面的预加载文件
		return super.onKeyDown(keyCode, event);
	}

	private void initvideo() {
		// 初始化VideoView
		mediaController = new MediaController(this);
		videoview.setMediaController(mediaController);
		videoview.setOnPreparedListener(mOnPreparedListener);
		// 初始化代理服务器
		proxy = new HttpGetProxy(9980);
		proxy.asynStartProxy();
		oriVideoUrl = ImageAddress.Stringchuangyi + video;
		// String[] urls = proxy.getLocalURL(oriVideoUrl);
		// String mp4Url = urls[0];
		// proxyUrl = urls[1];
		boolean enablePrebuffer = true;// 纯粹对比测试
		if (enablePrebuffer) {// 使用预加载
			try {
				String prebufferFilePath = proxy.prebuffer(oriVideoUrl,
						5 * 1024 * 1024);
				Log.e(TAG, "预加载文件：" + prebufferFilePath);
			} catch (Exception ex) {
				Log.e(TAG, ex.toString());
				Log.e(TAG, ProxyUtils.getExceptionMessage(ex));
			}
			delayToStartPlay.sendEmptyMessageDelayed(0, 8000);// 留8000ms预加载
		} else
			// 不使用预加载
			delayToStartPlay.sendEmptyMessageDelayed(0, 0);

		// 一直显示MediaController
		showController.sendEmptyMessageDelayed(0, 1000);
	}

	// @Override
	// protected void onStop() {
	// finish();
	// super.onStop();
	// }
	private Handler delayToStartPlay = new Handler() {
		public void handleMessage(Message msg) {
			startTimeMills = System.currentTimeMillis();
			videoview.setVideoPath(oriVideoUrl);
		}
	};
	private Handler showController = new Handler() {
		public void handleMessage(Message msg) {
			mediaController.show(0);
		}
	};
	private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			videoview.start();
			long duration = System.currentTimeMillis() - startTimeMills;
		}
	};

	private void data() {
		showdialogup();
		initdata();
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 首先在您的Activity中添加如下成员变量
//				final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//				// 设置分享内容
//				mController.setShareContent(""+shareContent);
//				// 是否只有已登录用户才能打开分享选择页
//		        mController.openShare(CreativeDetailsActivity.this, false);
				
				ShareSDK.initSDK(CreativeDetailsActivity.this);
				OnekeyShare oks = new OnekeyShare();
				oks.disableSSOWhenAuthorize();
				// 分享时Notification的图标和文字
				oks.setNotification(R.drawable.logo2,
						getString(R.string.app_name));
				// text是分享文本，所有平台都需要这个字段
				// if (App.shareIndex < strShare.length) {
				// oks.setText(strShare[App.shareIndex++]);
				oks.setText(""+shareContent);
				Log.e("msg", ""+shareContent);
				// } else {
//				App.shareIndex = 0;
				// }
				oks.show(CreativeDetailsActivity.this);
				oks.setAddress("");
			}
		});
			
			
//		share.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// Share.share(urls);
//				// 首先在您的Activity中添加如下成员变量
//				// final UMSocialService mController =
//				// UMServiceFactory.getUMSocialService("com.umeng.share");
//				// 设置分享内容
//				// mController.setShareContent(urls);
//				// mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
//				// SHARE_MEDIA.WEIXIN_CIRCLE,
//				// SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA,
//				// SHARE_MEDIA.TENCENT,
//				// SHARE_MEDIA.DOUBAN,
//				// SHARE_MEDIA.RENREN);
//				mController.getConfig().setPlatformOrder(SHARE_MEDIA.RENREN,
//						SHARE_MEDIA.DOUBAN, SHARE_MEDIA.TENCENT,
//						SHARE_MEDIA.SINA);
//
//				// 添加短信
//				SmsHandler smsHandler = new SmsHandler();
//				smsHandler.addToSocialSDK();
//
//				// 添加email
//				EmailHandler emailHandler = new EmailHandler();
//				emailHandler.addToSocialSDK();
//
//				// 添加有道云笔记平台
////				UMYNoteHandler yNoteHandler = new UMYNoteHandler(CreativeDetailsActivity.this);
////				yNoteHandler.addToSocialSDK();
//
//				// 添加易信平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
//				UMYXHandler yixinHandler = new UMYXHandler(CreativeDetailsActivity.this,"yxc0614e80c9304c11b0391514d09f13bf");
//				// 关闭分享时的等待Dialog
//				yixinHandler.enableLoadingDialog(false);
//				// 把易信添加到SDK中
//				yixinHandler.addToSocialSDK();
//
//				// 易信朋友圈平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
//				UMYXHandler yxCircleHandler = new UMYXHandler(CreativeDetailsActivity.this,"yxc0614e80c9304c11b0391514d09f13bf");
//				yxCircleHandler.setToCircle(true);
//				yxCircleHandler.addToSocialSDK();
//
//				// 添加来往
//				UMLWHandler umlwHandler = new UMLWHandler(CreativeDetailsActivity.this, "laiwangd497e70d4","d497e70d4c3e4efeab1381476bac4c5e");
//				umlwHandler.addToSocialSDK();
//				umlwHandler.setMessageFrom("友盟分享组件");
//
//				// 添加来往动态
//				UMLWHandler umlwDynamicHandler = new UMLWHandler(CreativeDetailsActivity.this, "laiwangd497e70d4","d497e70d4c3e4efeab1381476bac4c5e");
//				umlwDynamicHandler.addToSocialSDK();
//				umlwDynamicHandler.setMessageFrom("友盟分享组件");
////				mController.openShare(CreativeDetailsActivity.this, false);
//				//国外分享
//				// 添加Facebook分享
////				 UMFacebookHandler mFacebookHandler = UMFacebookHandler(CreativeDetailsActivity.this);
////				 mFacebookHandler.addToSocialSDK();
//
////				 添加Twitter分享
//				 mController.getConfig().supportAppPlatform(CreativeDetailsActivity.this,SHARE_MEDIA.TWITTER,"这里你构造mController填写的字符串参数", true) ;
//
//				// 添加Instagram分享
//				// 构建Instagram的Handler
//				UMInstagramHandler instagramHandler = new UMInstagramHandler(
//						CreativeDetailsActivity.this);
//				// 将instagram添加到sdk中
//				instagramHandler.addToSocialSDK();
//				// 本地图片
//				UMImage localImage = new UMImage(CreativeDetailsActivity.this,
//						R.drawable.logo);
//				// 设置分享到Instagram的内容，
//				// 注意由于instagram客户端的限制，目前该平台只支持纯图片分享，文字、音乐、url图片等都无法分享。
//				InstagramShareContent instagramShareContent = new InstagramShareContent(
//						localImage);
//				// 设置Instagram的分享内容
//				mController.setShareMedia(instagramShareContent);
//				//添加印象笔记分享
//				// 添加evernote平台
//				UMEvernoteHandler evernoteHandler = new UMEvernoteHandler(CreativeDetailsActivity.this);
//				evernoteHandler.addToSocialSDK();
//				//添加WhatsApp分享
//				// 添加WhatsApp平台
//				UMWhatsAppHandler whatsAppHandler = new UMWhatsAppHandler(CreativeDetailsActivity.this);
//				whatsAppHandler.addToSocialSDK();
//				//添加Line分享
//				// 添加LINE平台
//				UMLineHandler lineHandler = new UMLineHandler(CreativeDetailsActivity.this);
//				lineHandler.addToSocialSDK();
//				//添加Tumblr分享 
//				// 添加Tumblr平台
//				UMTumblrHandler tumblrHandler = new UMTumblrHandler(CreativeDetailsActivity.this);
//				tumblrHandler.addToSocialSDK();
//				// 添加KaKao平台
//				UMKakaoHandler kakaoHandler = new UMKakaoHandler(CreativeDetailsActivity.this);
//				kakaoHandler.addToSocialSDK();
//				// 添加Flickr平台
//				UMFlickrHandler flickrHandler = new UMFlickrHandler(CreativeDetailsActivity.this);
//				flickrHandler.addToSocialSDK();
//				// 添加pinterest平台
//				UMPinterestHandler pinterestHandler = new UMPinterestHandler(CreativeDetailsActivity.this,"1439206");
//				pinterestHandler.addToSocialSDK();
//				// 添加LinkedIn平台
//				UMLinkedInHandler linkedInHandler = new UMLinkedInHandler(CreativeDetailsActivity.this);
//				linkedInHandler.addToSocialSDK();
//				// 添加Pocket平台
//				UMPocketHandler pocketHandler = new UMPocketHandler(CreativeDetailsActivity.this);
//				pocketHandler.addToSocialSDK();
//				//添加G+分享
//				mController.getConfig().supportAppPlatform(CreativeDetailsActivity.this, SHARE_MEDIA.GOOGLEPLUS,"这里你构造mController填写的字符串参数", true) ;
//
//
//				//传递分享数据开始
//				QQShareContent qqShareContent = new QQShareContent();
//				// 设置分享文字
//				qqShareContent
//						.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
//				// 设置分享title
//				qqShareContent.setTitle("hello, title");
//				// 设置分享图片
//				qqShareContent.setShareImage(new UMImage(
//						CreativeDetailsActivity.this, R.drawable.logo));
//				// 设置点击分享内容的跳转链接
//				qqShareContent.setTargetUrl(urls);
//				mController.setShareMedia(qqShareContent);
//
//				QZoneShareContent qzone = new QZoneShareContent();
//				// 设置分享文字
//				qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
//				// 设置点击消息的跳转URL
//				qzone.setTargetUrl(urls);
//				// 设置分享内容的标题
//				// qzone.setTitle("QZone title");
//				// 设置分享图片
//				// qzone.setShareImage(urlImage);
//				mController.setShareMedia(qzone);
//
//				// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//				UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
//						CreativeDetailsActivity.this, "100424468",
//						"c7394704798a158208a74ab60104f0ba");
//				qqSsoHandler.addToSocialSDK();
//
//				// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//				QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
//						CreativeDetailsActivity.this, "100424468",
//						"c7394704798a158208a74ab60104f0ba");
//				qZoneSsoHandler.addToSocialSDK();
//
//				SnsPostListener snsPostListener = new SnsPostListener() {
//
//					@Override
//					public void onStart() {
////						Toast.makeText(CreativeDetailsActivity.this, "分享开始",
////								Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onComplete(SHARE_MEDIA platform, int eCode,
//							SocializeEntity entity) {
//						if (eCode == StatusCode.ST_CODE_SUCCESSED) {
////							Toast.makeText(CreativeDetailsActivity.this,
////									"分享成功", Toast.LENGTH_SHORT).show();
//							shareimg.setBackgroundResource(R.drawable.fenxiang_ed);
//						} else {
//							Toast.makeText(CreativeDetailsActivity.this,
//									"分享失败", Toast.LENGTH_SHORT).show();
//							shareimg.setBackgroundResource(R.drawable.fenxiang);
//						}
//
//					}
//				};
//				mController.registerListener(snsPostListener);
//				mController.openShare(CreativeDetailsActivity.this, false);

				 
				 
//			}
//		});
//		jubao.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (IsTrue.userId == 0) {
//					intent = new Intent(CreativeDetailsActivity.this,
//							HomeActivity.class);
//					IsTrue.tabnum = 4;
//					IsTrue.exit = true;
//					startActivity(intent);
//					finish();
//					return;
//				}
//				jubaoDialog();
//			}
//		});
		// 删除
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client = new AsyncHttpClient();
				params = new RequestParams();
				params.put("id", nameid);
				client.post(MyUrl.origdel, params,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								try {
									String num = response.getString("num");
									int i = Integer.parseInt(num);
									if (i == 1) {
										Toast.makeText(getApplicationContext(),
												"删除失败", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(getApplicationContext(),
												"删除成功", Toast.LENGTH_SHORT)
												.show();
										finish();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
								super.onSuccess(statusCode, headers, response);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								Toast.makeText(getApplicationContext(),
										"请求错误" + errorResponse,
										Toast.LENGTH_SHORT).show();
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}
						});
			}
		});
		down_zan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (nameid.equals(null) || nameid == null) {
					return;
				}
				if (IsTrue.dianzan == false && dianzannum == 2) {
					quxiaodownzan();
				} else if (IsTrue.dianzan == true && quxiaodianzannum == 2) {
					downzan();
				}
			}
		});
		allpinglun();
	}

	private void allpinglun() {
		pinglunup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pinglundialog();
			}
		});
		downpinglun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pinglundialog();
			}
		});
	}

	// 评论弹窗
	protected void pinglundialog() {
		Dialog dialog = new PingLunDialog(this, R.style.MyDialog, nameid,
				1 + "", new PingLunDialog.PriorityListener() {

					@Override
					public void refreshPriorityUI(
							List<Map<String, Object>> listmap) {
						pinglunlist = listmap;
						listview_comments.setVisibility(View.VISIBLE);
						numcount = numcount + 1;
						pingluntext.setText(numcount + "");
						if (numcount >= 1) {
							pinglun.setBackgroundResource(R.drawable.pinglun_ed);
						}
						xian2.setVisibility(View.VISIBLE);
						xian3.setVisibility(View.VISIBLE);
						xian4.setVisibility(View.VISIBLE);
						pinglunxianshi();
					}
				});
		// 底部弹出充满屏幕
		Window window = dialog.getWindow();
		LayoutParams params = new LayoutParams();
		params.gravity = Gravity.BOTTOM;
		// params.width = WindowManager.LayoutParams.FILL_PARENT;
		// params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(params);
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (requestCode == 2) {
		// shareimg.setBackgroundResource(R.drawable.fenxiang_ed);
		// }
		super.onActivityResult(requestCode, resultCode, data);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}

	}

	// 取消底部点赞
	protected void quxiaodownzan() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", nameid);
		params.put("source", 1);
		params.put("uid", IsTrue.userId);
		client.post(MyUrl.praisedel, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(getApplicationContext(), "删除点赞失败",
								Toast.LENGTH_SHORT).show();
					} else if (i == 2) {
						down_zanimg.setBackgroundResource(R.drawable.zan);
						quxiaodianzannum = 2;
						IsTrue.dianzan = true;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(), "请求错误" + errorResponse,
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	// 底部赞
	protected void downzan() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", nameid);
		params.put("source", 1);
		params.put("uid", IsTrue.userId);
		client.post(MyUrl.Stringzan, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(getApplicationContext(), "点赞失败",
								Toast.LENGTH_SHORT).show();
					} else if (i == 2) {
						down_zanimg.setBackgroundResource(R.drawable.zan_ed);
						IsTrue.dianzan = false;
						dianzannum = 2;
					} else if (i == 3) {
						Toast.makeText(getApplicationContext(), "你已经点过赞了",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(), "请求错误" + errorResponse,
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	// 必填返回的内容
	private void adddata() {
		// private ImageView imghead;//头像
		// private TextView username;//用户名
		// private TextView chuangyizhishu;//创意指数
		// private TextView type;//创意类型
		// private TextView chuangyiprice;//创意价值
		// private TextView chuangyitext;//创意内容
		// private LinearLayout duogedouxiang;//多个头像
		// int uimagei = Integer.parseInt(uimage.trim());
		if (uimage.equals("0")) {
			imghead.setBackgroundResource(R.drawable.logo);
		} else {
			BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
			bitmapUtils.display(imghead, ImageAddress.Stringhead + uimage);
		}
		username.setText(nickname);
		chuangyizhishu.setText(zhishu);
		type.setText(title);
		chuangyiprice.setText(price);
		chuangyitext.setText(content);
		if(null!=jiazaidialog  &&jiazaidialog.isShowing())
			jiazaidialog.dismiss();
		if (images.equals("0")) {
			gridView.setVisibility(View.GONE);
		} else {
			gridView.setVisibility(View.VISIBLE);
			final String imgString = images;
			final String[] splitimages = imgString.split(",");
			gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			image_GridView = new BaseAdapter() {

				@Override
				public View getView(final int position, View v, ViewGroup arg2) {
					ViewHolder holder = null;
					if (v == null) {
						holder = new ViewHolder();
						// 可以理解为从vlist获取view 之后把view返回给ListView
						v = LayoutInflater.from(CreativeDetailsActivity.this)
								.inflate(R.layout.iamge_nine, null);
						holder.img = (ImageView) v
								.findViewById(R.id.moreimages);
						v.setTag(holder);
					} else {
						holder = (ViewHolder) v.getTag();
					}
					BitmapUtils bitmapUtils = new BitmapUtils(
							getApplicationContext());
					bitmapUtils.display(holder.img, ImageAddress.Stringchuangyi
							+ splitimages[position]);
					holder.img.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(),
									GalleryImageActivity.class);
							IsTrue.kantu = 2;
							intent.putExtra("imagekan", imgString);
							startActivity(intent);
						}
					});
					return v;

				}

				@Override
				public long getItemId(int i) {
					return 0;
				}

				@Override
				public Object getItem(int i) {
					return null;
				}

				@Override
				public int getCount() {
					return splitimages.length;
				}
			};
			gridView.setAdapter(image_GridView);
		}
		if (video.equals("0")) {
			videoview.setVisibility(View.GONE);
		} else {
			videoview.setVisibility(View.VISIBLE);
			// initvideo();
			bofangvideo();
		}
		if (numsc.equals("1")) {
			duogetouxiang.setVisibility(View.GONE);
			xian.setVisibility(View.GONE);
			xian1.setVisibility(View.GONE);
		}
		if (num.equals("1")) {
			listview_comments.setVisibility(View.GONE);
			xian2.setVisibility(View.GONE);
			xian3.setVisibility(View.GONE);
			xian4.setVisibility(View.GONE);
		}
		if (delid.equals("1")) {
			delete.setVisibility(View.GONE);
		}
		// 赞和收藏
		like();
	}

	private void bofangvideo() {
		videoview.setMediaController(new MediaController(
				CreativeDetailsActivity.this));
		Uri uri = Uri.parse(ImageAddress.Stringchuangyi + video);
		videoview.setVideoURI(uri);
		videoview.requestFocus();
	}

	// 赞和收藏
	private void like() {
		if (scpd.equals("1")) {
			liketext.setText("");
			downimgxing.setBackgroundResource(R.drawable.shoucang_ed);
			like.setBackgroundResource(R.drawable.like_ed_shixin);
			liketext.setText(sc);
		} else {
			likeall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					client = new AsyncHttpClient();
					params = new RequestParams();
					params.put("sid", nameid);
					params.put("source", 1);
					params.put("uid", IsTrue.userId);
					client.post(MyUrl.Stringshoucang, params,
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									try {
										String num = response.getString("num");
										int i = Integer.parseInt(num);
										if (i == 1) {
											Toast.makeText(
													getApplicationContext(),
													"点赞失败", Toast.LENGTH_SHORT)
													.show();
										} else if (i == 2) {
											Toast.makeText(
													getApplicationContext(),
													"点赞成功", Toast.LENGTH_SHORT)
													.show();
											downimgxing
													.setBackgroundResource(R.drawable.shoucang_ed);
											like.setBackgroundResource(R.drawable.like_ed_shixin);
											int scnum = Integer.parseInt(sc);
											liketext.setText(scnum + 1 + "");
										} else if (i == 3) {
											Toast.makeText(
													getApplicationContext(),
													"已点赞", Toast.LENGTH_SHORT)
													.show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
									super.onSuccess(statusCode, headers,
											response);
								}

								@Override
								public void onFailure(int statusCode,
										Header[] headers, Throwable throwable,
										JSONObject errorResponse) {
									System.out.println(errorResponse.toString());
									super.onFailure(statusCode, headers,
											throwable, errorResponse);
								}
							});

				}
			});
			// 下面的五星
			downxing.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (nameid == null || nameid.equals(null)) {
						return;
					}
					client = new AsyncHttpClient();
					params = new RequestParams();
					params.put("sid", nameid);
					params.put("source", 1);
					params.put("uid", IsTrue.userId);
					client.post(MyUrl.Stringshoucang, params,
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									try {
										String num = response.getString("num");
										int i = Integer.parseInt(num);
										if (i == 1) {
											Toast.makeText(
													getApplicationContext(),
													"收藏失败", Toast.LENGTH_SHORT)
													.show();
										} else if (i == 2) {
											Toast.makeText(
													getApplicationContext(),
													"收藏成功", Toast.LENGTH_SHORT)
													.show();
											downimgxing
													.setBackgroundResource(R.drawable.shoucang_ed);
											like.setBackgroundResource(R.drawable.like_ed_shixin);
											int scnum = Integer.parseInt(sc);
											liketext.setText(scnum + 1 + "");
										} else if (i == 3) {
											Toast.makeText(
													getApplicationContext(),
													"已经收藏", Toast.LENGTH_SHORT)
													.show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
									super.onSuccess(statusCode, headers,
											response);
								}

								@Override
								public void onFailure(int statusCode,
										Header[] headers, Throwable throwable,
										JSONObject errorResponse) {
									System.out.println(errorResponse.toString());
									super.onFailure(statusCode, headers,
											throwable, errorResponse);
								}
							});
				}
			});
		}
	}

	// 初始化数据
	private void initdata() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId);
		params.put("id", nameid);
		client.post(MyUrl.origsinger, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response + "IIIIIIIIIIIIIIIIIIIII");
				try {
					title = response.getString("title").toString();
					content = response.getString("content").toString();
					shareContent= response.getString("urls").toString();
					images = response.getString("images").toString();
					// int imagesi = Integer.parseInt(images);
					// if (imagesi == 0) {
					//
					// }
					video = response.getString("video");
					price = response.getString("price");
					sc = response.getString("sc");
					count = response.getString("count").toString();
					numcount = Integer.parseInt(count);
					if (numcount == 0) {
						pinglun.setBackgroundResource(R.drawable.pinglun);
						pingluntext.setText(count);
					} else {
						pinglun.setBackgroundResource(R.drawable.pinglun_ed);
						pingluntext.setText(count);
					}
					nickid = response.getString("nickid");
					nickname = response.getString("nickname");
					uimage = response.getString("uimage").toString().trim();
					zhishu = response.getString("zhishu");
					id = response.getString("id");
					numsc = response.getString("numsc");
					delid = response.getString("delid");
					zanpd = response.getString("zanpd");
					urls = response.getString("urls");
					if (zanpd.equals("1")) {
						down_zanimg.setBackgroundResource(R.drawable.zan_ed);
						dianzannum = 2;
						IsTrue.dianzan = false;
					} else if (zanpd.equals("0")) {
						down_zanimg.setBackgroundResource(R.drawable.zan);
						IsTrue.dianzan = true;
						quxiaodianzannum = 2;
					}
					scpd = response.getString("scpd");
					int jaizhi = Integer.parseInt(numsc);
					if (jaizhi == 2) {
						listsc = response.getString("listsc");
						duogetouxiang.setVisibility(View.VISIBLE);
						xian.setVisibility(View.VISIBLE);
						xian1.setVisibility(View.VISIBLE);
						// 收藏用户所显示的头像
						dianzzan();
					}
					num = response.getString("num");
					int numjiazhi = Integer.parseInt(num);
					if (numjiazhi == 2) {
						xian2.setVisibility(View.VISIBLE);
						xian3.setVisibility(View.VISIBLE);
						xian4.setVisibility(View.VISIBLE);
						listview_comments.setVisibility(View.VISIBLE);
						jiazhilist = response.getString("list");
						xianshipinglun();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adddata();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(), errorResponse + "",
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	// 显示评论
	protected void xianshipinglun() {
		try {
			JSONArray array = new JSONArray(jiazhilist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map2 = new HashMap<String, Object>();
				map2.put("rtime", object.getString("rtime").toString());
				map2.put("wid", object.getString("wid").toString());
				map2.put("content", object.getString("content").toString());
				map2.put("mid", object.getString("mid").toString());
				map2.put("image", object.getString("image").toString());
				map2.put("nickname", object.getString("nickname").toString());
				map2.put("zhishu", object.getString("zhishu").toString());
				map2.put("mdel", object.getString("mdel").toString());
				listmap2.add(map2);
			}
			baseadapter = new BaseAdapter() {
				@Override
				public View getView(final int i, View v, ViewGroup arg2) {
					ViewHolder holder = null;
					if (v == null) {
						// ImageView pinglunimg;//评论头像
						// TextView pinglunzhishu;//评论指数
						// TextView pinglunname;//评论姓名
						// TextView pingluntext;//评论内容
						// TextView pingluntime;//评论时间
						// TextView pinglundel;//评论删除
						holder = new ViewHolder();
						// 可以理解为从vlist获取view 之后把view返回给ListView
						v = LayoutInflater.from(CreativeDetailsActivity.this)
								.inflate(R.layout.creativedetails_comments,
										null);
						holder.pinglunimg = (ImageView) v
								.findViewById(R.id.ll_CreateDetails_image_img);
						holder.pinglunzhishu = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_text);
						holder.pinglunname = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_name);
						holder.pingluntext = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_img_text);
						holder.pingluntime = (TextView) v
								.findViewById(R.id.ll_CreateDetails_time);
						holder.pinglundel = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_img_del);
						v.setTag(holder);
					} else {
						holder = (ViewHolder) v.getTag();
					}
					BitmapUtils bitmapUtils = new BitmapUtils(
							getApplicationContext());
					bitmapUtils.display(
							holder.pinglunimg,
							ImageAddress.Stringhead
									+ listmap2.get(i).get("image").toString());
					holder.pinglunzhishu.setText(listmap2.get(i).get("zhishu")
							.toString());
					holder.pinglunname.setText(listmap2.get(i).get("nickname")
							.toString());
					holder.pingluntext.setText(listmap2.get(i).get("content")
							.toString());
					holder.pingluntime.setText(listmap2.get(i).get("rtime")
							.toString());
					if (listmap2.get(i).get("mdel").toString().equals("1")) {
						holder.pinglundel.setVisibility(View.VISIBLE);
						holder.pinglundel
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										showdialogup();
										jiazhilist = null;
										wid = listmap2.get(i).get("wid")
												.toString();
										delpinglun();
									}
								});
					} else if (listmap2.get(i).get("mdel").toString()
							.equals("0")) {
						holder.pinglundel.setVisibility(View.GONE);
					}
					return v;
				}

				@Override
				public long getItemId(int arg0) {
					return 0;
				}

				@Override
				public Object getItem(int arg0) {
					return null;
				}

				@Override
				public int getCount() {
					return listmap2.size();
				}
			};
			listview_comments.setAdapter(baseadapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 删除评论
	private void delpinglun() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", nameid);
		params.put("source", 1);
		params.put("uid", IsTrue.userId);
		params.put("cid", wid);
		System.out.println(nameid + "SSS" + 1 + IsTrue.userId + "ssssssssss"
				+ wid);
		client.post(MyUrl.origacommentdel, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							String num = response.getString("num");
							int i = Integer.parseInt(num);
							if (i == 1) {
							} else if (i == 2) {
								if (response.getString("list") == null
										|| response.getString("list").equals(
												"null")) {
									xian2.setVisibility(View.GONE);
									xian3.setVisibility(View.GONE);
									xian4.setVisibility(View.GONE);
									listview_comments.setVisibility(View.GONE);
									numcount = 0;
									pingluntext.setText(numcount + "");
									if (numcount == 0) {
										pinglun.setBackgroundResource(R.drawable.pinglun);
									}
									jiazaidialog.dismiss();
									return;
								}
								listmap2 = new ArrayList<Map<String, Object>>();
								jiazhilist = response.getString("list");
								numcount = numcount - 1;
								pingluntext.setText(numcount + "");
								xianshipinglun();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						jiazaidialog.dismiss();
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Toast.makeText(getApplicationContext(),
								errorResponse + "", Toast.LENGTH_SHORT).show();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}
				});
	}

	// 添加评论后显示评论
	protected void pinglunxianshi() {
		baseadapter = new BaseAdapter() {
			@Override
			public View getView(final int i, View v, ViewGroup arg2) {
				ViewHolder holder = null;
				if (v == null) {
					// ImageView pinglunimg;//评论头像
					// TextView pinglunzhishu;//评论指数
					// TextView pinglunname;//评论姓名
					// TextView pingluntext;//评论内容
					// TextView pingluntime;//评论时间
					// TextView pinglundel;//评论删除
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(CreativeDetailsActivity.this)
							.inflate(R.layout.creativedetails_comments, null);
					holder.pinglunimg = (ImageView) v
							.findViewById(R.id.ll_CreateDetails_image_img);
					holder.pinglunzhishu = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_text);
					holder.pinglunname = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_name);
					holder.pingluntext = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_img_text);
					holder.pingluntime = (TextView) v
							.findViewById(R.id.ll_CreateDetails_time);
					holder.pinglundel = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_img_del);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				BitmapUtils bitmapUtils = new BitmapUtils(
						getApplicationContext());
				bitmapUtils.display(holder.pinglunimg, ImageAddress.Stringhead
						+ pinglunlist.get(i).get("image").toString());
				holder.pinglunzhishu.setText(pinglunlist.get(i).get("zhishu")
						.toString());
				holder.pinglunname.setText(pinglunlist.get(i).get("nickname")
						.toString());
				holder.pingluntext.setText(pinglunlist.get(i).get("content")
						.toString());
				holder.pingluntime.setText(pinglunlist.get(i).get("rtime")
						.toString());
				if (pinglunlist.get(i).get("mdel").toString().equals("1")) {
					holder.pinglundel.setVisibility(View.VISIBLE);
					holder.pinglundel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showdialogup();
							jiazhilist = null;
							wid = pinglunlist.get(i).get("wid").toString();
							delpinglun();
						}
					});
				} else if (pinglunlist.get(i).get("mdel").toString()
						.equals("0")) {
					holder.pinglundel.setVisibility(View.GONE);
				}
				return v;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return pinglunlist.size();
			}
		};
		listview_comments.setAdapter(baseadapter);
		// baseadapter.notifyDataSetChanged();
	}

	protected void jubaoDialog() {
		  dialog = new JuBaoDialog(this, R.style.MyDialog, nameid, 1 + "");
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		
	}

	public class ViewHolder {
		ImageView img;
		ImageView headimg;// 收藏头像
		TextView texthead;// 收藏指数
		ImageView pinglunimg;// 评论头像
		TextView pinglunzhishu;// 评论指数
		TextView pinglunname;// 评论姓名
		TextView pingluntext;// 评论内容
		TextView pingluntime;// 评论时间
		TextView pinglundel;// 评论删除
	}

	private void dianzzan() {
		try {
			JSONArray array = new JSONArray(listsc);
			for (int i = 0; i < array.length(); i++) {
				// nimage:收藏会员头像
				// zhishu:收藏会员指数
				// memid:收藏会员id
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("nimage", object.getString("nimage").toString());
				map.put("zhishu", object.getString("zhishu").toString());
				map.put("memid", object.getString("memid").toString());
				listmap.add(map);
			}
			adapterlist = new BaseAdapter() {

				@Override
				public View getView(final int position, View view,
						ViewGroup parent) {
					ViewHolder holder = null;
					if (view == null) {
						holder = new ViewHolder();
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.jointhinktank, null);
						holder.headimg = (ImageView) view
								.findViewById(R.id.ivjointhink);
						holder.texthead = (TextView) view
								.findViewById(R.id.tvjointhink);
						view.setTag(holder);
					} else {
						holder = (ViewHolder) view.getTag();
					}
					if (listmap.get(position).get("nimage").toString()
							.equals("")) {
						holder.headimg.setBackgroundResource(R.drawable.logo);
					}
					BitmapUtils bitmapUtils = new BitmapUtils(
							getApplicationContext());
					
					if(!"null".equals(listmap.get(position).get("zhishu")
							.toString())&&!TextUtils.isEmpty(listmap.get(position).get("zhishu")
							.toString())){
					bitmapUtils.display(holder.headimg, ImageAddress.Stringhead
							+ listmap.get(position).get("nimage").toString());
					
					holder.texthead.setText(listmap.get(position).get("zhishu")
							.toString());
					}
					else{
						listmap.remove(position);
					}
					holder.headimg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int intentid = Integer.parseInt(listmap
									.get(position).get("memid").toString());
							if (intentid == IsTrue.userId) {
								intent = new Intent(
										CreativeDetailsActivity.this,
										MyDetailsActivity.class);
								startActivity(intent);
							} else {
								// 跳转其他人信息
								intent = new Intent(
										CreativeDetailsActivity.this,
										OtherDetailsActivity.class);
								intent.putExtra("id", intentid + "");
								startActivity(intent);
							}
						}
					});
					return view;
				}

				@Override
				public long getItemId(int arg0) {
					return 0;
				}

				@Override
				public Object getItem(int arg0) {
					return null;
				}

				@Override
				public int getCount() {
					return listmap.size();
				}
			};
			ll_CreateDetails.setAdapter(adapterlist);
			// for (int j = 0; j < array.length(); j++) {
			// View view = LayoutInflater.from(CreativeDetailsActivity.this)
			// .inflate(R.layout.jointhinktank, null);
			// ImageView iamgeview = (ImageView) view
			// .findViewById(R.id.ivjointhink);
			// TextView textview = (TextView)
			// view.findViewById(R.id.tvjointhink);
			//
			// iamgeview.setImageResource(R.drawable.xxheader);
			// textview.setText("342");
			// textview.setTextColor(CreativeDetailsActivity.this.getResources()
			// .getColorStateList(R.color.greyhomepage));
			// view.setPadding(30, 0, 0, 0);
			// ll_CreateDetails.addView(view);
			// }
			// System.out.println(listmap+"IIIIIIIIIIIIIIIIIIIIIIIII");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String, Object>>();
		pinglunlist = new ArrayList<Map<String, Object>>();
		map2 = new HashMap<String, Object>();
		listmap2 = new ArrayList<Map<String, Object>>();
		scrollview = (LazyScrollView) findViewById(R.id.activity_creative_details_scrollview);
		ibCreativeDetailsbacking = (ImageButton) findViewById(R.id.ibCreativeDetailsbacking);
		gridView = (MyGridView) findViewById(R.id.gv_CreativeDetails);
		ll_CreateDetails = (HorizontalListView) findViewById(R.id.ll_CreateDetails);
		listview_comments = (ListViewForScrollView) findViewById(R.id.listview_comments);
		share = (LinearLayout) findViewById(R.id.activity_creative_details_share);
		jubao = (LinearLayout) findViewById(R.id.activity_creative_details_jubao);
		intent = getIntent();
		nameid = intent.getStringExtra("id").toString();
		//头像
		imghead = (ImageView) findViewById(R.id.activity_creative_details_imghead);
		imghead.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 跳转其他人信息
				intent = new Intent(
						CreativeDetailsActivity.this,
						OtherDetailsActivity.class);
				intent.putExtra("id", nickid + "");
				startActivity(intent);
			}
		});
		
		username = (TextView) findViewById(R.id.activity_creative_details_name);
		chuangyizhishu = (TextView) findViewById(R.id.activity_creative_details_zhishu);
		type = (TextView) findViewById(R.id.activity_creative_details_type);
		chuangyiprice = (TextView) findViewById(R.id.activity_creative_details_price);
		chuangyitext = (TextView) findViewById(R.id.activity_creative_details_content);
		duogetouxiang = (LinearLayout) findViewById(R.id.activity_creative_details_touxiang);
		xian = findViewById(R.id.activity_creative_details_xian);
		xian1 = findViewById(R.id.activity_creative_details_xian1);
		xian2 = findViewById(R.id.activity_creative_details_xian2);
		xian3 = findViewById(R.id.activity_creative_details_xian3);
		xian4 = findViewById(R.id.activity_creative_details_xian4);
		videoview = (VideoView) findViewById(R.id.activity_creative_details_video);
		delete = (TextView) findViewById(R.id.activity_creative_details_delete);
		likeall = (LinearLayout) findViewById(R.id.activity_creative_details_likeall);
		like = (ImageView) findViewById(R.id.activity_creative_details_like);
		liketext = (TextView) findViewById(R.id.activity_creative_details_liketext);
		pinglunup = (LinearLayout) findViewById(R.id.ll_CreativeDetails_comments);
		pinglun = (ImageView) findViewById(R.id.activity_creative_details_pinglun);
		pingluntext = (TextView) findViewById(R.id.activity_creative_details_pingluntext);
		down_zanimg = (ImageView) findViewById(R.id.activity_creative_details_down_zan_img);
		down_zantext = (TextView) findViewById(R.id.activity_creative_details_down_zan_text);
		down_zan = (LinearLayout) findViewById(R.id.activity_creative_details_down_zan);
		downxing = (LinearLayout) findViewById(R.id.activity_creative_details_down_xing);
		downimgxing = (ImageView) findViewById(R.id.activity_creative_details_down_xingimg);
		shareimg = (ImageView) findViewById(R.id.activity_creative_details_shareimg);
		downpinglun = (LinearLayout) findViewById(R.id.activity_creative_details_down_pinglun);
		downpinglunimg = (ImageView) findViewById(R.id.activity_creative_details_down_pinglunimg);
		
		jubao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IsTrue.userId == 0) {
					intent = new Intent(CreativeDetailsActivity.this,
							HomeActivity.class);
					IsTrue.tabnum = 4;
					IsTrue.exit = true;
					startActivity(intent);
					finish();
					return;
				}
				jubaoDialog();
			}
		});
	}
}
