package com.lsfb.cysj;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class Img extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img);
		ImageView img = (ImageView) findViewById(R.id.kanimg);
		Intent intent = getIntent();
		String imgs = intent.getExtras().getString("imgs").toString();
		BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
		bitmapUtils.display(img, ImageAddress.yic+imgs);
	}
}
