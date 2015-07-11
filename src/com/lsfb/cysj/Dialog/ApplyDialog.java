package com.lsfb.cysj.Dialog;

import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.QiFuialog.PriorityListener;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.view.CenterTextview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ApplyDialog extends Dialog {
	Context context;
	private LinearLayout text1, text2, text3;
	private TextView text11, text22, text33;
	Intent intent;
	PriorityListener listener;
	private CenterTextview type;

	public ApplyDialog(Context context) {
		super(context);
		this.context = context;
	}

	public ApplyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	/**
	 * 自定义Dialog监听器
	 * 
	 * @author Kael.Chen
	 * 
	 */
	public interface PriorityListener {
		/**
		 * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
		 */
		public void refreshPriorityUI(String string,String string2);
	}

	/**
	 * 带监听器参数的构造函数
	 */
	public ApplyDialog(Context context, int theme, PriorityListener listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ideas_type_dialog);
		setCanceledOnTouchOutside(true);
		init();
		data();
	}

	private void data() {
		if (IsTrue.xuanzeleixing == 1) {
			type.setText("选择你要发布的创意类型");
		}else if (IsTrue.xuanzeleixing == 2) {
			type.setText("选择你要发起创意比赛的类型");
		}
		text1.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String string = text11.getText().toString();
				String string2 = 1+"";
				listener.refreshPriorityUI(string,string2);
				dismiss();
			}
		});
		text2.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String string = text22.getText().toString();
				String string2 = 2+"";
				listener.refreshPriorityUI(string,string2);
				dismiss();
			}
		});
		text3.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String string = text33.getText().toString();
				String string2 = 3+"";
				listener.refreshPriorityUI(string,string2);
				dismiss();
			}
		});
	}

	private void init() {
		text1 = (LinearLayout) findViewById(R.id.ideas_type_text1);
		text2 = (LinearLayout) findViewById(R.id.ideas_type_text2);
		text3 = (LinearLayout) findViewById(R.id.ideas_type_text3);
		type = (CenterTextview) findViewById(R.id.ideas_type_text);
		text11 = (TextView) findViewById(R.id.ideas_type_text11);
		text22 = (TextView) findViewById(R.id.ideas_type_text22);
		text33 = (TextView) findViewById(R.id.ideas_type_text33);
	}
}
