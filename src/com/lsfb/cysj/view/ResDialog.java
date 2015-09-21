package com.lsfb.cysj.view;



import com.lsbf.cysj.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResDialog extends Dialog {
	private Context context;
	private String msg;
	private int imgname;

	public ResDialog(Context context, int theme, String msg, int imgname) { 
		super(context, theme);
		this.context = context;
		this.msg = msg;
		this.imgname = imgname;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.TRANSPARENT);
		this.setContentView(R.layout.resdialog);
		TextView res = (TextView) this.findViewById(R.id.bewri);
		res.setText(msg);
		ProgressBar ii = (ProgressBar) this.findViewById(R.id.customProgressBar);
		if (imgname == 0) {
			ii.setVisibility(View.GONE);
		} else {
			ii.setVisibility(View.VISIBLE);
		}
	}
}
