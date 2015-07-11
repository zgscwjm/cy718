package com.lsfb.cysj;

import com.lsbf.cysj.R;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	VideoView tv_chuangFriends;
	ImageButton ibVideobacking;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		tv_chuangFriends = (VideoView) findViewById(R.id.tv_chuangFriends);
		ibVideobacking = (ImageButton) findViewById(R.id.ibVideobacking);
		tv_chuangFriends.setOnErrorListener(videoErrorListener);
		System.err.println("播放视频没·····");
		Intent intent = getIntent();
		String struri = intent.getStringExtra("uri");
		Uri uri = Uri.parse(struri);

		tv_chuangFriends.setMediaController(new MediaController(
				VideoActivity.this));
		tv_chuangFriends.setVideoURI(uri);

		tv_chuangFriends.requestFocus();
		tv_chuangFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tv_chuangFriends.start();
			}
		});
		ibVideobacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public OnErrorListener videoErrorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// 对播放出错进行处理
			Toast.makeText(VideoActivity.this, "无法播放此格式", Toast.LENGTH_SHORT)
					.show();
			return true;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

}
