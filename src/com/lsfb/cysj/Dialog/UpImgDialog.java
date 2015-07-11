package com.lsfb.cysj.Dialog;

import com.lsbf.cysj.R;
import com.lsfb.cysj.UpMoreImge;
import com.lsfb.cysj.UpVideoActivity;
import com.lsfb.cysj.Dialog.FaBuDialog.PriorityListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UpImgDialog extends Dialog {
	Context context;
	private TextView text1, text2;
	Intent intent;

	public UpImgDialog(Context context) {
		super(context);
		this.context = context;
	}

	public UpImgDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.upimg_dialog);
		setCanceledOnTouchOutside(true);
		init();
		data();
	}

	private void data() {
		text1.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(getContext(),UpMoreImge.class);
				context.startActivity(intent);
				dismiss();
			}
		});
		text2.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(getContext(),UpVideoActivity.class);
				context.startActivity(intent);
				dismiss();
			}
		});
	}

	private void init() {
		text1 = (TextView) findViewById(R.id.upimg_text1);
		text2 = (TextView) findViewById(R.id.upimg_text2);
	}

}
