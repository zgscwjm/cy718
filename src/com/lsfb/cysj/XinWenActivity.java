package com.lsfb.cysj;

import com.lsbf.cysj.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class XinWenActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xinwen);
		Intent intent = getIntent();
		String url = intent.getExtras().getString("url").toString();
		WebView wb = (WebView) findViewById(R.id.xinwenweb);
		wb.loadUrl(url);
	}
}
