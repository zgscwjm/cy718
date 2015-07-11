package com.lsfb.cysj;

import com.lsbf.cysj.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectPicPopupWindow extends Activity implements OnClickListener{
	Intent intent;
	private Button canera,photo,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_popupwindows);
		init();
	}
	private void init() {
		canera = (Button) findViewById(R.id.item_popupwindows_camera);
		canera.setOnClickListener(this);
		photo = (Button) findViewById(R.id.item_popupwindows_Photo);
		photo.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.item_popupwindows_cancel);
		cancel.setOnClickListener(this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		//选择完或者拍完照后会在这里处理，然后我们继续使用setResult返回Intent以便可以传递数据和调用
		if (data.getExtras() != null)
			intent.putExtras(data.getExtras());
		if (data.getData()!= null)
			intent.setData(data.getData());
		setResult(1, intent);
		finish();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_popupwindows_camera:
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
		case R.id.item_popupwindows_Photo:
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
		case R.id.item_popupwindows_cancel:
			finish();
			break;
		default:
			break;
		}
	}

}
