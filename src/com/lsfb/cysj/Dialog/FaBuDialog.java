package com.lsfb.cysj.Dialog;

import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.ApplyDialog.PriorityListener;
import com.lsfb.cysj.app.IsTrue;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FaBuDialog extends Dialog {

	Context context;
	private TextView text1, text2;
	Intent intent;
	PriorityListener listener;

	public FaBuDialog(Context context) {
		super(context);
		this.context = context;
	}

	public FaBuDialog(Context context, int theme) {
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
		public void refreshPriorityUI(String string);
	}

	/**
	 * 带监听器参数的构造函数
	 */
	public FaBuDialog(Context context, int theme, PriorityListener listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fabu_dialog);
		setCanceledOnTouchOutside(true);
		init();
		data();
	}

	private void data() {
		text1.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int state = 1;
				listener.refreshPriorityUI(state + "");
				dismiss();
			}
		});
		text2.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int state = 2;
				listener.refreshPriorityUI(state + "");
				dismiss();
			}
		});
	}

	private void init() {
		text1 = (TextView) findViewById(R.id.fabu_dialog_text1);
		text2 = (TextView) findViewById(R.id.fabu_dialog_text2);
	}

}
