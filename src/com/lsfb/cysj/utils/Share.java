package com.lsfb.cysj.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.lsbf.cysj.R;
import com.lsfb.cysj.CreativeDetailsActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.instagram.controller.UMInstagramHandler;
import com.umeng.socialize.instagram.media.InstagramShareContent;
import com.umeng.socialize.laiwang.controller.UMLWHandler;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.yixin.controller.UMYXHandler;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;

public class Share {
	static Context context;
	public static void share(String url){
		// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
//		mController.setShareContent(urls);
//		 mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
//                    SHARE_MEDIA.DOUBAN,
//                    SHARE_MEDIA.RENREN);
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
                SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);


		// 添加短信
		 SmsHandler smsHandler = new SmsHandler();
		 smsHandler.addToSocialSDK();
		 
		// 添加email
		 EmailHandler emailHandler = new EmailHandler();
		 emailHandler.addToSocialSDK();

		// 添加有道云笔记平台
		 UMYNoteHandler yNoteHandler = new UMYNoteHandler(context);
		 yNoteHandler.addToSocialSDK();

		// 添加易信平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
		 UMYXHandler yixinHandler = new UMYXHandler(context,
		                 "yxc0614e80c9304c11b0391514d09f13bf");
		 // 关闭分享时的等待Dialog
		 yixinHandler.enableLoadingDialog(false);
		 // 把易信添加到SDK中
		 yixinHandler.addToSocialSDK();

		 // 易信朋友圈平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
		 UMYXHandler yxCircleHandler = new UMYXHandler(context,
		                 "yxc0614e80c9304c11b0391514d09f13bf");
		 yxCircleHandler.setToCircle(true);
		 yxCircleHandler.addToSocialSDK();
		 
		//添加来往      
		 UMLWHandler umlwHandler = new UMLWHandler(context, "laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
		 umlwHandler.addToSocialSDK();
		 umlwHandler.setMessageFrom("友盟分享组件");

		 //添加来往动态        
		 UMLWHandler umlwDynamicHandler = new UMLWHandler(context, "laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
		 umlwDynamicHandler.addToSocialSDK();
		 umlwDynamicHandler.setMessageFrom("友盟分享组件");
		 mController.openShare((Activity) context, false);

		 //添加Facebook分享
//		 UMFacebookHandler mFacebookHandler = UMFacebookHandler(context);
//		 mFacebookHandler.addToSocialSDK();
		 
		 //添加Twitter分享
//		 mController.getConfig().supportAppPlatform(context, SHARE_MEDIA.TWITTER,
//				 "这里你构造mController填写的字符串参数", true) ; 

		// 构建Instagram的Handler
		 UMInstagramHandler instagramHandler = new UMInstagramHandler(context);
		 // 将instagram添加到sdk中
		 instagramHandler.addToSocialSDK();
		 // 本地图片
		 UMImage localImage = new UMImage(context, R.drawable.logo);
		 // 设置分享到Instagram的内容， 注意由于instagram客户端的限制，目前该平台只支持纯图片分享，文字、音乐、url图片等都无法分享。
		 InstagramShareContent instagramShareContent = new InstagramShareContent(localImage);
		 // 设置Instagram的分享内容
		 mController.setShareMedia(instagramShareContent);

		 
		QQShareContent qqShareContent = new QQShareContent();
		//设置分享文字
		qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
		//设置分享title
		qqShareContent.setTitle("hello, title");
		//设置分享图片
		qqShareContent.setShareImage(new UMImage(context, R.drawable.logo));
		//设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(url);
		mController.setShareMedia(qqShareContent);
		
		QZoneShareContent qzone = new QZoneShareContent();
		//设置分享文字
		qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
		//设置点击消息的跳转URL
		qzone.setTargetUrl(url);
		//设置分享内容的标题
//		qzone.setTitle("QZone title");
		//设置分享图片
//		qzone.setShareImage(urlImage);
		mController.setShareMedia(qzone);
		

		//参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();  
		
		//参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
		
		
		SnsPostListener snsPostListener = new SnsPostListener() {
			
			@Override
			public void onStart() {
				Toast.makeText(context, "分享开始",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				if(eCode == StatusCode.ST_CODE_SUCCESSED){
                    Toast.makeText(context, "分享成功",Toast.LENGTH_SHORT).show();
//                    shareimg.setBackgroundResource(R.drawable.fenxiang_ed);
                }else{
                    Toast.makeText(context, "分享失败",Toast.LENGTH_SHORT).show();
                }

			}
		};
//		mController.openShare(context, false);
		mController.registerListener(snsPostListener);
		
		
		
		//自定义分享界面
//		mController.postShare(context, arg1, arg2)
		// 设置分享图片, 参数2为图片的url地址
//		mController.setShareMedia(new UMImage(getActivity(),"http://www.umeng.com/images/pic/banner_module_social.png"));
//		intent = new Intent();
//		intent.setAction(Intent.ACTION_SEND);
//		intent.putExtra(Intent.EXTRA_TEXT, urls);
//		intent.setType("text/plain");
//		// startActivity(intent);
//		startActivityForResult(intent, 2);
	}
}
