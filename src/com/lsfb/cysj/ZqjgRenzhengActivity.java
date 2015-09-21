package com.lsfb.cysj;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * 政企机构，详细
 * 
 * @author admin
 * 
 */
public class ZqjgRenzhengActivity extends Activity implements OnClickListener {
	ImageButton ib_InvisibilityAndSecurity_backing;
	Button btnShangchuan;

	Boolean bHotnews, bXitongnews, bYonghunews;
	// 自定义的弹出框类
	BottomPopupWindow menuWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zqjg_renzheng);
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

		btnShangchuan = (Button) findViewById(R.id.btnShangchuan);
		// imgYonghunews = (ImageButton) findViewById(R.id.imgYonghunews);
		// imgXitongnews = (ImageButton) findViewById(R.id.imgXitongnews);

		btnShangchuan.setOnClickListener(this);
		// imgYonghunews.setOnClickListener(this);
		// imgXitongnews.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invisibility_and_security, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnShangchuan:
			// 实例化SelectPicPopupWindow
			// menuWindow=new SelectPicPopupWindow();

			menuWindow = new BottomPopupWindow(ZqjgRenzhengActivity.this,
					itemsOnClick);
			// 显示窗口
			menuWindow.showAtLocation(
					ZqjgRenzhengActivity.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

			break;
		// case R.id.imgYonghunews:3
		//
		// break;
		// case R.id.imgXitongnews:
		//
		// break;

		default:
			break;
		}
	}
	
	// 为弹出窗口实现监听类,底部弹窗按钮的点击事件
	public OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				try {
					//拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
					//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.btn_pick_photo:
				
				try {
					//选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
					//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intent, 2);
				} catch (ActivityNotFoundException e) {

				}
				break;
			default:
				break;
			}

		}

	};

}
