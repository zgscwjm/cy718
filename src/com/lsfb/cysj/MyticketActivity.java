package com.lsfb.cysj;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.color;
import com.lsbf.cysj.R.id;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.base.NoViewPage;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyticketActivity extends FragmentActivity {
	ImageButton ibMyticketbacking;
	TextView tvticketNoUse;// 未使用
	View tvticketNoUseXian;

	TextView tvticketAlreadyUse;// 已使用
	View tvticketAlreadyUseXian;

	TextView tvticketAlreadyoverdue;// 已过期
	View tvticketAlreadyoverdueXian;

	NoViewPage ViewPageMyticket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myticket);
		init();
		ibMyticketbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		changecolor();
	}

	private void changecolor() {
		// TODO Auto-generated method stub
		tvticketNoUse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				tvticketNoUse.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.blueMain));
				tvticketNoUseXian.setBackgroundResource(R.color.blueMain);
				tvticketAlreadyUse.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.greymy));
				tvticketAlreadyUseXian.setBackgroundResource(R.color.white);
				tvticketAlreadyoverdue.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.greymy));
				tvticketAlreadyoverdueXian.setBackgroundResource(R.color.white);

				ViewPageMyticket.setCurrentItem(0);
			}
		});
		tvticketAlreadyUse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvticketNoUse.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.greymy));
				tvticketNoUseXian.setBackgroundResource(R.color.white);
				tvticketAlreadyUse.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.blueMain));
				tvticketAlreadyUseXian.setBackgroundResource(R.color.blueMain);
				tvticketAlreadyoverdue.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.greymy));
				tvticketAlreadyoverdueXian.setBackgroundResource(R.color.white);
				
				ViewPageMyticket.setCurrentItem(1);
			}
		});
		tvticketAlreadyoverdue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvticketNoUse.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.greymy));
				tvticketNoUseXian.setBackgroundResource(R.color.white);
				tvticketAlreadyUse.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.greymy));
				tvticketAlreadyUseXian.setBackgroundResource(R.color.white);
				tvticketAlreadyoverdue.setTextColor(MyticketActivity.this
						.getResources().getColorStateList(
								R.color.blueMain));
				tvticketAlreadyoverdueXian.setBackgroundResource(R.color.blueMain);
				
				ViewPageMyticket.setCurrentItem(2);
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		ibMyticketbacking = (ImageButton) findViewById(R.id.ibMyticketbacking);

		tvticketNoUse = (TextView) findViewById(R.id.tvticketNoUse);
		tvticketNoUseXian = (View) findViewById(R.id.tvticketNoUseXian);

		tvticketAlreadyUse = (TextView) findViewById(R.id.tvticketAlreadyUse);
		tvticketAlreadyUseXian = (View) findViewById(R.id.tvticketAlreadyUseXian);

		tvticketAlreadyoverdue = (TextView) findViewById(R.id.tvticketAlreadyoverdue);
		tvticketAlreadyoverdueXian = (View) findViewById(R.id.tvticketAlreadyoverdueXian);

		ViewPageMyticket=(NoViewPage)findViewById(R.id.ViewPageMyticket);
		FragmentAdapter adapter = new FragmentAdapter(
				getSupportFragmentManager(),3,7);
		ViewPageMyticket.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.myticket, menu);
		return true;
	}

}
