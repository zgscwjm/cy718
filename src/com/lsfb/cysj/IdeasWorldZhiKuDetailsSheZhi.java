package com.lsfb.cysj;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;

public class IdeasWorldZhiKuDetailsSheZhi extends FragmentActivity implements OnClickListener{
	@ViewInject(R.id.ideas_world_zhiku_details_shezhi_back)
	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideas_world_zhiku_details_shezhi);
		ViewUtils.inject(this);
		init();
	}
	private void init() {
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ideas_world_zhiku_details_shezhi_back:
			finish();
			break;

		default:
			break;
		}
	}

}
