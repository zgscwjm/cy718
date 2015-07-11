package com.lsfb.cysj.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.lidroid.xutils.ViewUtils;
import com.lsbf.cysj.R;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base);
		ViewUtils.inject(this);
	}

}
