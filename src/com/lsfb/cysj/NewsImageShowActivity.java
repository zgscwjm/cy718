package com.lsfb.cysj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;

public class NewsImageShowActivity extends Activity {
	ImageView iv_news_image_show;
	String image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_image_show);
		init();
		BitmapUtils bitmapUtils = new BitmapUtils(NewsImageShowActivity.this);
		bitmapUtils.display(iv_news_image_show, image);
	}

	private void init() {
		// TODO Auto-generated method stub
		iv_news_image_show = (ImageView) findViewById(R.id.iv_news_image_show);
		Intent intent = getIntent();
		image = intent.getStringExtra("imgs");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_image_show, menu);
		return true;
	}

}
