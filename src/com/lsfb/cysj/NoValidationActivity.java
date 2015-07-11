package com.lsfb.cysj;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.Tools;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class NoValidationActivity extends Activity {
	ImageButton ibNoValidationbacking;
	Button btnThink_tank;// 验证按钮
	String num = "";// 判断是学校还是政企
	private String[] items = new String[] { "选择本地图片", "拍照" };
	// private String[] items = new String[] { "选择本地图片" };
	private String uploadFile = "";
	private String actionUrl = MyUrl.Stringyanzheng;
	private String newName = "image.jpg";

	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "isImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(NoValidationActivity.this, "上传失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(NoValidationActivity.this, "上传成功",
								Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_validation);
		init();
		ibNoValidationbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnThink_tank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		Intent intent = getIntent();
		num = intent.getStringExtra("check");
	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("上传验证图片")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (Tools.hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于操作取消时候
		if (resultCode != NoValidationActivity.this.RESULT_CANCELED) {
			// IMAGE_REQUEST_CODE = 0；相册
			// CAMERA_REQUEST_CODE = 1;拍照
			// RESULT_REQUEST_CODE = 2;结果
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				Log.d("11111111111111111111111111", "1");
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					Log.d("11111111111111111111111111", "2");
					File tempFile = new File(
							Environment.getExternalStorageDirectory(),
							IMAGE_FILE_NAME);
					// uploadFile = Uri.fromFile(tempFile).toString();
					startPhotoZoom(Uri.fromFile(tempFile));

				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}

				break;
			// RESULT_REQUEST_CODE
			case RESULT_REQUEST_CODE:
				Log.d("11111111111111111111111111", "3");
				if (data != null) {

					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
			boolean res = photo.compress(Bitmap.CompressFormat.PNG, 100,
					logoStream);// 将图像读取到logoStream中
			byte[] logoBuf = logoStream.toByteArray();// 将图像保存到byte[]中
			Bitmap temp = BitmapFactory.decodeByteArray(logoBuf, 0,
					logoBuf.length);// 将图像从byte[]中读取生成Bitmap 对象 temp
			saveMyBitmap("tttt", temp);// 将图像保存到SD卡中
			Drawable drawable = new BitmapDrawable(photo);
			// IsTrue.drawable = drawable;
			new Thread() {
				@Override
				public void run() {
					RequestParams params = new RequestParams();
					params.addBodyParameter("uid", IsTrue.userId + "");
					params.addBodyParameter("check", num);
					params.addBodyParameter("institutions_temporary_image",
							new File(uploadFile));
					uploadMethod(params, actionUrl);
				}
			}.start();

		}
	}

	public void uploadMethod(final RequestParams params, final String uploadHost) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// msgTextview.setText("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							// msgTextview.setText("upload: " + current + "/"+
							// total);
						} else {
							// msgTextview.setText("reply: " + current + "/"+
							// total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// msgTextview.setText("reply: " + responseInfo.result);
						// civ.setImageDrawable(IsTrue.drawable);
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = responseInfo.result;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// msgTextview.setText(error.getExceptionCode() + ":" +
						// msg);
					}
				});
	}

	public void saveMyBitmap(String bitName, Bitmap mBitmap) {
		File f = new File("/sdcard/" + bitName + ".png");
		uploadFile = "/sdcard/" + bitName + ".png";
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		ibNoValidationbacking = (ImageButton) findViewById(R.id.ibNoValidationbacking);
		btnThink_tank = (Button) findViewById(R.id.btnThink_tank);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.no_validation, menu);
		return true;
	}

}
