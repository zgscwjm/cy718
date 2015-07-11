package com.lsfb.cysj;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.PublicWay;

public class UpVideoActivity extends FragmentActivity {
	private LinearLayout upvideo;
	private ImageView iv_upvideo;
	private VideoView Video_show;
	String url = "";
	TextView tv_upvideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upvideo);
		init();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			// url = uri.toString();
			Cursor c = getContentResolver().query(uri,
					new String[] { MediaStore.MediaColumns.DATA }, null, null,
					null);// 根据返回的URI，查找数据库，获取视频的路径
			if (c != null && c.moveToFirst()) {
				url = c.getString(0);
				System.err.println(url);

			}

			Video_show.setVisibility(View.VISIBLE);
			Video_show.setVideoPath(url);// 设置了要播放的视频文件位置。
			Video_show.setMediaController(new MediaController(this));//
			// 设置了一个播放控制器。
			Video_show.start();// 程序运行时自动开始播放视频。
			Video_show.requestFocus(); // 播放窗口为当前窗口
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 // 创建退出对话框  
            AlertDialog isExit = new AlertDialog.Builder(this).create();  
            // 设置对话框标题  
            isExit.setTitle("系统提示");  
            // 设置对话框消息  
            isExit.setMessage("确定要退出吗");  
            // 添加选择按钮并注册监听  
            isExit.setButton("确定", listener);  
            isExit.setButton2("取消", listener);  
            // 显示对话框  
            isExit.show();  
		}
		return true;
	}
	 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
//	            	for (int i = 0; i < PublicWay.activityList.size(); i++) {
//	    				if (null != PublicWay.activityList.get(i)) {
//	    					PublicWay.activityList.get(i).finish();
//	    				}
//	    			}
	            	Intent intent = new Intent();
					intent.putExtra("video", "");
					setResult(RESULT_OK, intent);

					finish();
//	    			System.exit(0);
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
	public void uploadMethod(RequestParams params, String uploadHost) {
		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// msgTextview.setText("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							tv_upvideo
									.setText("上传进度: " + current + "/" + total);
						} else {
							tv_upvideo
									.setText("上传进度: " + current + "/" + total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// msgTextview.setText("reply: " + responseInfo.result);
						Toast.makeText(UpVideoActivity.this, "上传成功",
								Toast.LENGTH_SHORT).show();
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result.toString());
							Intent intent = new Intent();
							intent.putExtra("video", jsonObject.getString("img"));
							setResult(RESULT_OK, intent);

							finish();
							System.err.println(responseInfo.result);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// msgTextview.setText(error.getExceptionCode() + ":" +
						// msg);

						Toast.makeText(UpVideoActivity.this,
								error.getExceptionCode() + "",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void init() {
		upvideo = (LinearLayout) findViewById(R.id.upmorevideo);
		iv_upvideo = (ImageView) findViewById(R.id.iv_upvideo);
		Video_show = (VideoView) findViewById(R.id.Video_show);
		Video_show.setVisibility(View.GONE);
		tv_upvideo = (TextView) findViewById(R.id.tv_upvideo);
		upvideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// handler = new Handler();
				// handler.post(runnable);
				if (url.equals("")) {
					Toast.makeText(getApplicationContext(), "您还没选择视频", Toast.LENGTH_SHORT).show();
					return;
				}
				RequestParams params = new RequestParams();

				params.addBodyParameter("image", new File(url));
				String URL = null;
				if (IsTrue.upmore == 1) {
					URL = MyUrl.StringUP;
				}else if (IsTrue.upmore == 2) {
					URL = MyUrl.uploadfile;
				}
				System.out.println(URL+"HHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
				uploadMethod(params, URL);
			}
		});
		iv_upvideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("video/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(
						Intent.createChooser(intent, "请选择一个要上传的文件"), 1);
			}
		});
	}

}
