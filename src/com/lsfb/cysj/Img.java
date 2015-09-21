package com.lsfb.cysj;

import com.alipay.mobilesecuritysdk.deviceID.LOG;
import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * 查看大图
 * 
 * @author Administrator
 * 
 */
public class Img extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img);
		ImageView img = (ImageView) findViewById(R.id.kanimg);
		Intent intent = getIntent();
		String imgs = intent.getExtras().getString("imgs").toString();
		String from = intent.getExtras().getString("from").toString();
		Log.d("imgs", ImageAddress.yic + imgs);
		BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());

		if ("IdeasFriendsFragment".equals(from)) {
			bitmapUtils.display(img, ImageAddress.Stringchuangyi + imgs);

		} else {
			bitmapUtils.display(img, ImageAddress.yic + imgs);
		}
	}
}
