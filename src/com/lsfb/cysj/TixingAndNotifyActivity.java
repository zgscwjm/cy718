package com.lsfb.cysj;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.utils.Show;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 提醒与通知，详细
 * 
 * @author admin
 * 
 */
public class TixingAndNotifyActivity extends Activity implements
		OnClickListener {
	ImageButton ib_InvisibilityAndSecurity_backing;
	ImageButton imgHotnews, imgXitongnews, imgYonghunews;
	
	boolean bHotnews=true;
	boolean bXitongnews=true;
	boolean bYonghunews=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tixing_and_notify);
		init();
		ib_InvisibilityAndSecurity_backing
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
	}

	private void init() {
		// TODO Auto-generated method stub
		ib_InvisibilityAndSecurity_backing = (ImageButton) findViewById(R.id.ib_InvisibilityAndSecurity_backing);

		imgHotnews = (ImageButton) findViewById(R.id.imgHotnews);
		imgYonghunews = (ImageButton) findViewById(R.id.imgYonghunews);
		imgXitongnews = (ImageButton) findViewById(R.id.imgXitongnews);
		
		imgHotnews.setOnClickListener(this);
		imgYonghunews.setOnClickListener(this);
		imgXitongnews.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invisibility_and_security, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgHotnews:
			if(bHotnews){
				Show.toast(getApplicationContext(), "已关闭");
				v.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
			}else{
				Show.toast(getApplicationContext(), "已开启");
				v.setBackgroundResource(R.drawable.tixingandtongzhi1_03);
			}
				bHotnews=!bHotnews;
			
			break;
		case R.id.imgYonghunews:
			if(bYonghunews){
				Show.toast(getApplicationContext(), "已关闭");
				v.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
			}else{
				Show.toast(getApplicationContext(), "已开启");
				v.setBackgroundResource(R.drawable.tixingandtongzhi1_03);
			}
			bYonghunews=!bYonghunews;
			
			break;
		case R.id.imgXitongnews:
			if(bXitongnews){
				Show.toast(getApplicationContext(), "已关闭");
				v.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
			}else{
				Show.toast(getApplicationContext(), "已开启");
				v.setBackgroundResource(R.drawable.tixingandtongzhi1_03);
			}
			bXitongnews=!bXitongnews;
			
			break;

		default:
			break;
		}
	}

}
