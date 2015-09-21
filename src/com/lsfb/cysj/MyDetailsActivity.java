package com.lsfb.cysj;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.CreativeFragment;
import com.lsfb.cysj.fragment.CreativeFragments;
import com.lsfb.cysj.fragment.DynamicFragment;
import com.lsfb.cysj.fragment.MyFragment;
import com.lsfb.cysj.view.CircleImageView;
import com.lsfb.cysj.view.Tools;

/**
 *资料详情： 点好友头像进入资料详情
 * @author Administrator
 *
 */
public class MyDetailsActivity extends FragmentActivity {
	private String[] items = new String[] { "选择本地图片", "拍照" };
	// private String[] items = new String[] { "选择本地图片" };
	private String uploadFile = "";
	private String actionUrl = MyUrl.StringEditimage;
	private String newName = "image.jpg";

	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	ImageButton ibbacking;// 返回
	TextView tvHomePage;// 主页
	TextView tvDynamic;// 动态
	TextView tvCreative;// 创意
	ImageView ImgDengJiz;// 等级图片
	// NoViewPage noViewPage;
	LinearLayout ll_my_details_Edit_Data;// 编辑资料
	LinearLayout homePageFragment_show;
	LinearLayout dynamicFragment_show;
	LinearLayout creativeFragment_show;
	//ScrollView scrollView_myDetails;
	CircleImageView civ_Mydetails_head;// 头像
	TextView civ_Mydetails_name;// 名字
	TextView tv_Mydetails_signatur;// 个性签名
	CreativeFragments fragmebt_creatuve;
	DynamicFragment fragmebt_dynamic;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				Log.d("2222222222222222222222222", str);
				try {
					// num:1-->头像修改失败;
					// 2-->头像修改成功;
					// 3-->没有上场图片;
					// img:修改后的头像

					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.get("num").toString())) {
					case 1:
						Toast.makeText(MyDetailsActivity.this, "头像修改失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(MyDetailsActivity.this, "头像修改成功",
								Toast.LENGTH_SHORT).show();
						// IsTrue.Stringiamgename=jsonObject.getString("image");
						IsTrue.Stringimage=jsonObject.get("img").toString();
						civ_Mydetails_head.setImageDrawable(IsTrue.drawable);
						break;
					case 3:
						Toast.makeText(MyDetailsActivity.this, "没有上传图片",
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
		setContentView(R.layout.activity_my_details);
		init();
		ibbacking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 改变颜色
		changecolor();
		ll_my_details_Edit_Data.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyDetailsActivity.this,
						EditDataActivity.class);
				startActivity(intent);
			}
		});
		// 初始化

		BitmapUtils bitmapUtils = new BitmapUtils(MyDetailsActivity.this);
		bitmapUtils.display(civ_Mydetails_head, ImageAddress.Stringhead + IsTrue.Stringimage);
		civ_Mydetails_name.setText(IsTrue.Stringnickname);// 名字
		if (IsTrue.Stringsignatur.equals("未设置")) {
			tv_Mydetails_signatur.setText("");
		} else {
			if (IsTrue.Stringsignatur.length() <= 16) {
				tv_Mydetails_signatur.setText(IsTrue.Stringsignatur);
			} else {
				tv_Mydetails_signatur.setText(IsTrue.Stringsignatur.substring(
						0, 16));
			}
		}
		if(IsTrue.intZqyz==2)
		{
			ImgDengJiz.setImageResource(R.drawable.img_qiye);
		}else
		{
		switch (IsTrue.intDengji) {
		case 1:
			ImgDengJiz.setImageResource(R.drawable.z1);
			break;
		case 2:
			ImgDengJiz.setImageResource(R.drawable.z2);
			break;
		case 3:
			ImgDengJiz.setImageResource(R.drawable.z3);
			break;
		case 4:
			ImgDengJiz.setImageResource(R.drawable.z4);
			break;

		default:
			break;
		}
		}
		civ_Mydetails_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});

	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
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
		if (resultCode != MyDetailsActivity.this.RESULT_CANCELED) {
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
			IsTrue.drawable = drawable;
			new Thread() {
				@Override
				public void run() {
					RequestParams params = new RequestParams();
					params.addBodyParameter("uid", IsTrue.userId + "");
					params.addBodyParameter(uploadFile.replace("/", ""),
							new File(uploadFile));
					System.err.println(uploadFile);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		civ_Mydetails_name.setText(IsTrue.Stringnickname);// 名字
		if (IsTrue.Stringsignatur.equals("未设置")) {
			tv_Mydetails_signatur.setText("");
		} else {
			if (IsTrue.Stringsignatur.length() <= 16) {
				tv_Mydetails_signatur.setText(IsTrue.Stringsignatur);
			} else {
				tv_Mydetails_signatur.setText(IsTrue.Stringsignatur.substring(
						0, 16));
			}
		}
		fragmebt_creatuve.changeclass("1",IsTrue.userId+"","0");//这个方法的作用是传值进去方便判断是个人资料还是他人资料
		super.onResume();
	}

	private void changecolor() {
		// TODO Auto-generated method stub
		tvHomePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvHomePage.setBackgroundResource(R.drawable.shape);
				tvHomePage.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.white));
				tvDynamic.setBackgroundResource(R.color.white);
				tvDynamic.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				tvCreative.setBackgroundResource(R.color.white);
				tvCreative.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				homePageFragment_show.setVisibility(View.VISIBLE);
				dynamicFragment_show.setVisibility(View.GONE);
				creativeFragment_show.setVisibility(View.GONE);
				
				// noViewPage.setCurrentItem(0);
			}
		});
		tvDynamic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvDynamic.setBackgroundResource(R.drawable.shape);
				tvDynamic.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.white));
				tvHomePage.setBackgroundResource(R.color.white);
				tvHomePage.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				tvCreative.setBackgroundResource(R.color.white);
				tvCreative.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				homePageFragment_show.setVisibility(View.GONE);
				dynamicFragment_show.setVisibility(View.VISIBLE);
				creativeFragment_show.setVisibility(View.GONE);
				fragmebt_dynamic.setClassData("1","0");
				// noViewPage.setCurrentItem(1);
			
			}
		});
		tvCreative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvCreative.setBackgroundResource(R.drawable.shape);
				tvCreative.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.white));
				tvHomePage.setBackgroundResource(R.color.white);
				tvHomePage.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				tvDynamic.setBackgroundResource(R.color.white);
				tvDynamic.setTextColor(MyDetailsActivity.this.getResources()
						.getColorStateList(R.color.greymy));
				homePageFragment_show.setVisibility(View.GONE);
				dynamicFragment_show.setVisibility(View.GONE);
				creativeFragment_show.setVisibility(View.VISIBLE);
				
				// noViewPage.setCurrentItem(2);
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		// noViewPage = (NoViewPage) findViewById(R.id.nvp_change);
		// FragmentAdapter adapter = new FragmentAdapter(
		// getSupportFragmentManager(), 3, 1);
		// noViewPage.setAdapter(adapter);
		ibbacking = (ImageButton) findViewById(R.id.ibmybacking);
		tvHomePage = (TextView) findViewById(R.id.tvHomePage);
		tvDynamic = (TextView) findViewById(R.id.tvDynamic);
		tvCreative = (TextView) findViewById(R.id.tvCreative);
		ll_my_details_Edit_Data = (LinearLayout) findViewById(R.id.ll_my_details_Edit_Data);
		homePageFragment_show = (LinearLayout) findViewById(R.id.homePageFragment_show);
		dynamicFragment_show = (LinearLayout) findViewById(R.id.dynamicFragment_show);
		creativeFragment_show = (LinearLayout) findViewById(R.id.creativeFragment_show);
		civ_Mydetails_head = (CircleImageView) findViewById(R.id.civ_Mydetails_head);// 头像
		civ_Mydetails_name = (TextView) findViewById(R.id.tv_Mydetails_name);// 名字
		tv_Mydetails_signatur = (TextView) findViewById(R.id.tv_Mydetails_signatur);
		ImgDengJiz= (ImageView) findViewById(R.id.img_dengjiz);
		
		fragmebt_creatuve=(CreativeFragments) MyDetailsActivity.this
				.getSupportFragmentManager()
				.findFragmentById(R.id.fragmebt_creatuve);
		fragmebt_creatuve.changeclass("1",IsTrue.userId+"","0");//这个方法的作用是传值进去方便判断是个人资料还是他人资料
		fragmebt_dynamic=(DynamicFragment) MyDetailsActivity.this
				.getSupportFragmentManager()
				.findFragmentById(R.id.fragmebt_dynamic);
		fragmebt_dynamic.setData();
		//		scrollView_myDetails=(ScrollView)findViewById(R.id.scrollView_myDetails);
//		scrollView_myDetails.smoothScrollBy(0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_details, menu);
		return true;
	}
	
}