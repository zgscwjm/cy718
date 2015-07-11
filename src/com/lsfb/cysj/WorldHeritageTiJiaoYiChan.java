package com.lsfb.cysj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WorldHeritageTiJiaoYiChan extends FragmentActivity implements OnClickListener{
	@ViewInject(R.id.world_heritage_tijiaoyichan_back)
	private LinearLayout back;
	@ViewInject(R.id.world_heritage_tijiaoyichan_name)
	private EditText name;
	@ViewInject(R.id.world_heritage_tijiaoyichan_img)
	private ImageView img;
	@ViewInject(R.id.release_ideas_content)
	private EditText content;
	@ViewInject(R.id.world_heritage_tijiaoyichan_tijiao)
	private LinearLayout tijiao;
	private static final int SELECT_PICTURE = 1; // 从图库中选择图片
	private static final int SELECT_CAMER = 2; // 用相机拍摄照片
	Bitmap bitmap = null;
	Uri uri = null;
	String path = "";
	Dialog jiazaidialog;
	String upname,uptext;
	HttpUtils httpUtils;
	RequestParams params;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.world_heritage_tijiaoyichan);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		init();
	}
	private void init() {
		IsTrue.yichan = 0;
		back.setOnClickListener(this);
		img.setOnClickListener(this);
		tijiao.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.world_heritage_tijiaoyichan_back:
			finish();
			break;
		case R.id.world_heritage_tijiaoyichan_tijiao:
			upname = name.getText().toString().trim();
			if (upname.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入名称",
						Toast.LENGTH_SHORT).show();
				return;
			}
			uptext = content.getText().toString().trim();
			if (uptext.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入正文",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (path == null || path.trim().equals("")) {
				Toast.makeText(getApplicationContext(), "请添加图片",
						Toast.LENGTH_SHORT).show();
				return;
			}
			showdialogup();
			upmsg();
			break;
		case R.id.world_heritage_tijiaoyichan_img:
			showChoosePhotoDialog();
			break;
		default:
			break;
		}
	}
	
	private void upmsg() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		File file = new File(path);
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("images", file);
		params.addBodyParameter("introduce", uptext);
		params.addBodyParameter("name", upname);
		httpUtils.send(HttpMethod.POST, MyUrl.ladd, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(),
						error.getExceptionCode() + ":" + msg,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String list = responseInfo.result;
				System.out.println(list+"MMM");
				jiazaidialog.dismiss();
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT).show();
					}else if (num.equals("2")) {
						Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在上传...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(WorldHeritageTiJiaoYiChan.this);
		builder.setTitle("选择图片来源");
		builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent getImageByCamera = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				startActivityForResult(getImageByCamera, SELECT_CAMER);
				bitmap = null;
				uri = null;
			}
		});
		builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, SELECT_PICTURE);
				bitmap = null;
				uri = null;
			}
		});
		builder.create().show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECT_CAMER:
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			String name = new DateFormat().format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			if (data == null || data.equals("")) {
				return;
			}
			Bundle bundle = data.getExtras();
			bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

			FileOutputStream b = null;
			// ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
			File file = new File("/sdcard/myImage/");
			file.mkdirs();// 创建文件夹
			String fileName = "/sdcard/myImage/" + name;

			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			path = fileName;
			img.setImageBitmap(bitmap);
			break;
		case SELECT_PICTURE:
			// try{
			if (resultCode == RESULT_OK) {
				uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				try {
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
					}
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(uri, proj, null, null, null);
					// 按我个人理解 这个是获得用户选择的图片的索引值
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					// 最后根据索引值获取图片路径
					path = cursor.getString(column_index);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				img.setImageURI(uri);
			} else {
				Toast.makeText(WorldHeritageTiJiaoYiChan.this, "选择图片失败,请重新选择",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}
