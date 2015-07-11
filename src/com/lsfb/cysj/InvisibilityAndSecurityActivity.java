package com.lsfb.cysj;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class InvisibilityAndSecurityActivity extends Activity {
	ImageButton ib_InvisibilityAndSecurity_backing;

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invisibility_and_security, menu);
		return true;
	}

}
