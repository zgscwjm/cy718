package com.lsfb.cysj;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.utils.Show;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * 隐私与安全
 * @author admin
 *
 */
public class InvisibilityAndSecurityActivity extends Activity implements
		OnClickListener {
	ImageButton ib_InvisibilityAndSecurity_backing, imgYinshen;
	boolean yinshen = true;
	RelativeLayout rlModifyPwd, rlHeimingdan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invisibility_and_security);
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

		rlModifyPwd = (RelativeLayout) findViewById(R.id.rlModifyPwd);
		rlHeimingdan = (RelativeLayout) findViewById(R.id.rlHeimingdan);

		rlModifyPwd.setOnClickListener(this);
		rlHeimingdan.setOnClickListener(this);

		// imgYinshen=(ImageButton) findViewById(R.id.imgYinshen);
		// imgYinshen.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if(yinshen){
		// Show.toast(getApplicationContext(), "已关闭");
		// v.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
		// }else{
		// Show.toast(getApplicationContext(), "已开启");
		// v.setBackgroundResource(R.drawable.tixingandtongzhi1_03);
		// }
		// yinshen=!yinshen;
		// }
		// });
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
		Intent intent = null;
		switch (v.getId()) {
		case R.id.rlModifyPwd:
			  intent=new Intent(InvisibilityAndSecurityActivity.this,AcModifyPassword.class
					);
		  
			break;
		case R.id.rlHeimingdan:
			  intent=new Intent(InvisibilityAndSecurityActivity.this,AcHeiMingdan.class
					);
			 
			break;

		default:
			break;
		}
		if(null!=intent)
		startActivity(intent);
	}

}
