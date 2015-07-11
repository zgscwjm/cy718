package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PollResultsListActivity extends FragmentActivity implements OnClickListener{
	@ViewInject(R.id.pollresults_list_back)
	private LinearLayout back;
	HttpUtils httpUtils;
	RequestParams params;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pollresults_list);
		ViewUtils.inject(this);
		init();
		data();
	}
	private void data() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
	}
	private void init() {
		back.setOnClickListener(this);
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pollresults_list_back:
			finish();
			break;

		default:
			break;
		}
	}
}
