package com.lsfb.cysj;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.id;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * 设置
 * @author Administrator
 *
 */
public class SetUpTheActivity extends Activity {
	ImageButton ibSetupThebacking;// 返回
	RelativeLayout rlSetUpFeedBack;// 意见反馈
	RelativeLayout rl_setup_Invisibility_and_security;// 隐私与安全
	RelativeLayout rl_Aboutus, rlTixing,rlzfRenzheng;// 关于我们

	Button btnOutLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up_the);
		init();
		ibSetupThebacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rlSetUpFeedBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetUpTheActivity.this,
						FeedbackActivity.class);
				startActivity(intent);
			}
		});
		rl_setup_Invisibility_and_security
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SetUpTheActivity.this,
								InvisibilityAndSecurityActivity.class);
						startActivity(intent);
					}
				});
		rl_Aboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetUpTheActivity.this,
						AboutUsActivity.class);
				startActivity(intent);
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		ibSetupThebacking = (ImageButton) findViewById(R.id.ibSetupThebacking);
		rlSetUpFeedBack = (RelativeLayout) findViewById(R.id.rlSetUpFeedBack);
		rl_setup_Invisibility_and_security = (RelativeLayout) findViewById(R.id.rl_setup_Invisibility_and_security);
		rl_Aboutus = (RelativeLayout) findViewById(R.id.rl_Aboutus);

		rlTixing = (RelativeLayout) findViewById(R.id.rlTixing);
		
		rlzfRenzheng = (RelativeLayout) findViewById(R.id.rlzfRenzheng);

		btnOutLogin = (Button) findViewById(R.id.btnOutLogin);
		btnOutLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				Intent home = new Intent(Intent.ACTION_MAIN);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				home.addCategory(Intent.CATEGORY_HOME);
				startActivity(home);

			}
		});

		rlTixing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetUpTheActivity.this,
						TixingAndNotifyActivity.class);
				startActivity(intent);
				 

			}
		});
		
		//政府认证
		rlzfRenzheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetUpTheActivity.this,
						ZqjgRenzhengActivity.class);
				startActivity(intent); 
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_up_the, menu);
		return true;
	}

}
