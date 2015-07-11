package com.lsfb.cysj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.lsbf.cysj.R;

public class Loading extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
		loading.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
