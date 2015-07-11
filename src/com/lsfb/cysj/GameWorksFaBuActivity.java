package com.lsfb.cysj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GameWorksFaBuActivity extends FragmentActivity implements
		OnClickListener {
	Intent intent;
	String clasid;// 分类数值
	@ViewInject(R.id.gameworks_back)
	private LinearLayout back;
	@ViewInject(R.id.gameworks_fabu)
	private LinearLayout tijiao;
	@ViewInject(R.id.gameworks_btn)
	private Button type;
	@ViewInject(R.id.gameworks_picture)
	private ImageView picture;
	@ViewInject(R.id.gameworks_more)
	private ImageView more;
	@ViewInject(R.id.gameworks_title)
	private EditText gameworks_title;
	@ViewInject(R.id.gameworks_content)
	private EditText gameworks_content;
	AsyncHttpClient client;
	RequestParams params;
	private static final int SELECT_PICTURE = 1; // 从图库中选择图片
	private static final int SELECT_CAMER = 2; // 用相机拍摄照片
	Bitmap bitmap = null;
	String path = "";
	String video = "";
	Uri uri = null;
	Dialog jiazaidialog;
	/**
	 * sid:大赛id title:提交作品题目 content:提交作品内容 uid:提交作品人的id(会员id)
	 * state:是否显示(1显示|2不显示) match_data_image:作品封面图 match_data_images:作品多图(可选)
	 * video作品视频(可选)
	 */
	String sid,title,content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameworks);
		// 默认不弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ViewUtils.inject(this);
		init();
		data();
	}

	private void data() {
		if (clasid.equals("1")) {
			type.setText("投资项目创意");
		} else if (clasid.equals("2")) {
			type.setText("任务人才创意");
		} else if (clasid.equals("3")) {
			type.setText("公益明星创意");
		}
	}

	private void init() {
		IsTrue.moreimg = "";
		IsTrue.upmore = 0;
		intent = getIntent();
		clasid = intent.getExtras().getString("clasid").toString();
		sid = intent.getExtras().getString("sid").toString();
		back.setOnClickListener(this);
		tijiao.setOnClickListener(this);
		picture.setOnClickListener(this);
		more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gameworks_back:
			finish();
			break;
		case R.id.gameworks_fabu:// 提交作品
			title = gameworks_title.getText().toString().trim();
			content = gameworks_content.getText().toString().trim();
			if (title.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入标题",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (content.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入作品内容",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (path == null || path.trim().equals("")) {
				Toast.makeText(getApplicationContext(), "请添加图片",
						Toast.LENGTH_SHORT).show();
				return;
			}
			tijiao();
			break;
		case R.id.gameworks_picture:// 上传封面图
			showChoosePhotoDialog();
			break;
		case R.id.gameworks_more:// 上传更多
			IsTrue.upmore = 2;
			MoreDialog();
			break;
		default:
			break;
		}
	}

	private void MoreDialog() {
		AlertDialog.Builder builder = new Builder(GameWorksFaBuActivity.this);
		builder.setTitle("更多资料上传");
		builder.setPositiveButton("上传多图",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						intent = new Intent(GameWorksFaBuActivity.this,
								UpMoreImge.class);
						startActivity(intent);
						video = "";
						more.setBackgroundResource(R.drawable.uploadimg_fb);
					}
				});
		builder.setNegativeButton("上传视频",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						intent = new Intent(GameWorksFaBuActivity.this,
								UpVideoActivity.class);
						startActivityForResult(intent, 4);
						IsTrue.moreimg = "";
						more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
					}
				});
		builder.create().show();
	}

	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(GameWorksFaBuActivity.this);
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
			picture.setImageBitmap(bitmap);
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
				picture.setImageURI(uri);
			} else {
				Toast.makeText(GameWorksFaBuActivity.this, "选择图片失败,请重新选择",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case 3:
			// String result = data.getExtras().getString("imgs");//得到新Activity
			// 关闭后返回的数据
			// System.out.println(result+"PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
			break;
		case 4:
			video = data.getExtras().getString("video");// 得到新Activity 关闭后返回的数据
			break;
		default:
			break;
		}
	}

	private void tijiao() {
		AlertDialog.Builder builder = new Builder(GameWorksFaBuActivity.this);
		builder.setTitle("是否显示");
		builder.setPositiveButton("显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showdialogup();
				fabumsg(1);
			}
		});
		builder.setNegativeButton("不显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showdialogup();
				fabumsg(2);
			}
		});
		builder.create().show();
	}

	protected void fabumsg(int i) {
		client = new AsyncHttpClient();
		params = new RequestParams();
		try {
			File file = new File(path);
			params.put("match_works_image", file);
			params.put("sid", sid);
			params.put("title", title);
			params.put("content", content);
			if (IsTrue.userId == 0) {
				Toast.makeText(GameWorksFaBuActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
				return;
			}
			params.put("uid", IsTrue.userId);
			params.put("state", i+"");
			params.put("images", IsTrue.moreimg);
			params.put("video", video);
			System.out.println(IsTrue.moreimg+"TTTTTTTTTTTTTTTTTTTTTTTT");
			client.post(MyUrl.workadd, params, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					System.out.println(response+"LLLLLLLLL");
					try {
						String num = response.getString("num");
						if (num.equals("1")) {
							Toast.makeText(GameWorksFaBuActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
						}else if (num.equals("2")) {
							Toast.makeText(GameWorksFaBuActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
							finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					jiazaidialog.dismiss();
					super.onSuccess(statusCode, headers, response);
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					jiazaidialog.dismiss();
					Toast.makeText(GameWorksFaBuActivity.this, errorResponse + "",
							Toast.LENGTH_SHORT).show();
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在上传...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
}
