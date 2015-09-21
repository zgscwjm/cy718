package com.lsfb.cysj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.ApplyDialog;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.DateTimePickDialogUtil;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发起比赛
 * 
 * @author Administrator
 * 
 */
public class ReleaseGameActivity extends FragmentActivity implements
		OnClickListener {
	@ViewInject(R.id.release_game_back)
	private LinearLayout back;
	/**
	 * release_game_faqi faqi 发起比赛
	 */
	@ViewInject(R.id.release_game_faqi)
	private LinearLayout faqi;
	@ViewInject(R.id.release_game_btn)
	private Button btn_type;
	@ViewInject(R.id.release_game_title)
	private EditText release_game_title;
	/**
	 * release_ideas_picture picture 头像图
	 */
	@ViewInject(R.id.release_ideas_picture)
	private ImageView picture;
	/**
	 * release_ideas_content 比赛内容介绍
	 */
	@ViewInject(R.id.release_ideas_content)
	private EditText release_ideas_content;
	/**
	 * release_game_img img 封面图
	 */
	@ViewInject(R.id.release_game_img)
	private ImageView img;
	/**
	 * release_ideas_starttime starttime 开始时间
	 */
	@ViewInject(R.id.release_ideas_starttime)
	private TextView starttime;
	@ViewInject(R.id.release_ideas_endtime)
	private TextView endtime;
	String type, type2;// type创意类型 type2发送的数字
	Bitmap bitmap = null;
	Uri uri = null;
	String path = "";// 封面图片地址
	String path2 = "";// 图片
	String time;
	AsyncHttpClient client;
	RequestParams params;
	Dialog jiazaidialog;

	public static final int SELECT_PICTURE = 1; // 从图库中选择图片
	public static final int SELECT_CAMER = 2; // 用相机拍摄照片
	/**
	 * ip:当前发布大赛的ip地址 title:比赛题目 image:封面图名称 images:比赛内页图 time1:比赛开始时间
	 * time2:比赛结束时间 state:比赛可见不可见(1可见|2不可见) aclass:比赛类型(1投资项目创意|2任务人才创意|3公益明星创意)
	 * introduce:比赛介绍
	 */
	String ip, title, image, images, time1, time2, state, aclass, introduce;
	String endtime1, kaishisj1;
	int imgchange = 0;// 图片或者封面图
	int xianshi = 0;// 显示与不显示
	HttpUtils httpUtils;

	String sid, hid;

	public static final int UP_HX = 2001;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UP_HX:
				client = new AsyncHttpClient();
				params = new RequestParams();
				Log.d("sid", "" + sid + ":" + hid);
				params.put("sid", sid);
				params.put("hid", hid);
				 
				client.post(MyUrl.uphx, params, new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						
						Log.d("ok", "ok"+response.toString());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Log.d("nook", "ok");
					}

				});

				// params.addBodyParameter("hid", hid+"");
				// params.addBodyParameter("sid", sid+"" );
				// httpUtils.send(HttpMethod.POST, MyUrl.uphx, params,
				// new RequestCallBack<String>() {
				//
				// @Override
				// public void onFailure(HttpException arg0,
				// String arg1) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void onSuccess(ResponseInfo<String> arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// });

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.release_game);
		ViewUtils.inject(this);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init();
		httpUtils = new HttpUtils();

	}

	private void init() {
		getip();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		time = formatter.format(curDate);
		back.setOnClickListener(this);
		btn_type.setOnClickListener(this);
		img.setOnClickListener(this);
		faqi.setOnClickListener(this);
		starttime.setOnClickListener(this);
		endtime.setOnClickListener(this);
		picture.setOnClickListener(this);
		type = btn_type.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.release_game_back:
			finish();
			break;
		case R.id.release_game_btn:// 发起比赛的类型
			showDialog();
			break;
		case R.id.release_ideas_picture:// 头像图
			imgchange = 2;
			imgdialog();
			break;
		case R.id.release_game_img:// 封面图
			imgchange = 1;
			// imgdialog();
			showChoosePhotoDialog();

			break;
		case R.id.release_game_faqi:// 发起比赛
			title = release_game_title.getText().toString().trim();
			introduce = release_ideas_content.getText().toString().trim();
			if (type.equals("选择发布创意比赛类型")) {
				Toast.makeText(getApplicationContext(), "选择发布创意比赛类型",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (title.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入标题",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (path == null || path.trim().equals("")) {
				Toast.makeText(getApplicationContext(), "请上传封面图",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (introduce.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入比赛内容介绍",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// if (path2 == null || path2.trim().equals("")) {
			// Toast.makeText(getApplicationContext(), "请上传图片",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			onetime();
			if (starttime.getText().toString().equals("开始比赛时间")) {
				Toast.makeText(getApplicationContext(), "开始时间没有选择",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				twotime();
			}

			break;
		case R.id.release_ideas_starttime:// 开始比赛时间
			starttime();
			break;
		case R.id.release_ideas_endtime:// 结束比赛时间
			endtime();
			break;
		default:
			break;
		}
	}

	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(ReleaseGameActivity.this);
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
				// Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
				// getImage.addCategory(Intent.CATEGORY_OPENABLE);
				// getImage.setType("image/*");
				// startActivityForResult(getImage, SELECT_PICTURE);
				bitmap = null;
				uri = null;
			}
		});
		builder.create().show();
	}

	private void getip() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, "http://city.ip138.com/ip2city.asp",
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// 从反馈的结果中提取出IP地址
						int start = arg0.result.toString().indexOf("[");
						int end = arg0.result.toString()
								.indexOf("]", start + 1);
						ip = arg0.result.toString().substring(start + 1, end);
					}
				});
	}

	private void twotime() {
		endtime1 = endtime.getText().toString();
		kaishisj1 = starttime.getText().toString();
		if (endtime1.equals("结束比赛时间")) {
			Toast.makeText(getApplicationContext(), "日期没有选择",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			String year = endtime1.substring(0, 4);
			String startyear = kaishisj1.substring(0, 4);
			int iyear = Integer.parseInt(year);
			int jstartyear = Integer.parseInt(startyear);
			System.out.println(iyear + "EEEE???" + jstartyear);
			if (iyear > jstartyear) {
				faqidialog();
			} else if (jstartyear == iyear) {
				String month = endtime1.substring(5, 7);
				String startmonth = kaishisj1.substring(5, 7);
				int imonth = Integer.parseInt(month);
				int jmonth = Integer.parseInt(startmonth);
				System.out.println(imonth + "RRRRDDD" + jmonth);
				if (imonth > jmonth) {
					faqidialog();
				} else if (jmonth == imonth) {
					String day = endtime1.substring(8, 10);
					String startday = kaishisj1.substring(8, 10);
					int iday = Integer.parseInt(day);
					int jstartday = Integer.parseInt(startday);
					System.out.println(iday + "QQQQJJJJ" + jstartday);
					if (iday >= jstartday) {
						faqidialog();
					} else {
						Toast.makeText(getApplicationContext(), "日期不对",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} else if (imonth < jmonth) {
					Toast.makeText(getApplicationContext(), "月份不对",
							Toast.LENGTH_SHORT).show();
					return;
				}
			} else if (iyear < jstartyear) {
				Toast.makeText(getApplicationContext(), "年份不对",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
	}

	private void onetime() {
		kaishisj1 = starttime.getText().toString();
		if (kaishisj1.equals("开始比赛时间")) {
			Toast.makeText(getApplicationContext(), "日期没有选择",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			String year = time.substring(0, 4);
			String startyear = kaishisj1.substring(0, 4);
			int iyear = Integer.parseInt(year);
			int jstartyear = Integer.parseInt(startyear);
			System.out.println(iyear + "???" + jstartyear);
			if (jstartyear > iyear) {

			} else if (jstartyear == iyear) {
				String month = time.substring(5, 7);
				String startmonth = kaishisj1.substring(5, 7);
				int imonth = Integer.parseInt(month);
				int jmonth = Integer.parseInt(startmonth);
				System.out.println(imonth + "DDD" + jmonth);
				if (jmonth > imonth) {

				} else if (jmonth == imonth) {
					String day = time.substring(8, 10);
					String startday = kaishisj1.substring(8, 10);
					int iday = Integer.parseInt(day);
					int jstartday = Integer.parseInt(startday);
					System.out.println(iday + "JJJJ" + jstartday);
					if (jstartday >= iday) {
					} else {
						Toast.makeText(getApplicationContext(), "日期不对",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} else if (jmonth < imonth) {
					Toast.makeText(getApplicationContext(), "月份不对",
							Toast.LENGTH_SHORT).show();
					return;
				}
			} else if (jstartyear < iyear) {
				Toast.makeText(getApplicationContext(), "年份不对",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		System.out.println(kaishisj1 + "EEEEEEEEEEEEEEEEEEEEEEEEEE");

	}

	private void endtime() {
		DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
				ReleaseGameActivity.this, time);
		dateTimePicKDialog.dateTimePicKDialog(endtime);
	}

	private void starttime() {
		DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
				ReleaseGameActivity.this, time);
		dateTimePicKDialog.dateTimePicKDialog(starttime);
	}

	// 发布
	private void faqidialog() {
		AlertDialog.Builder builder = new Builder(ReleaseGameActivity.this);
		builder.setTitle("是否显示");
		builder.setPositiveButton("显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				xianshi = 1;
				showdialogup();
				sendfabu();
			}
		});
		builder.setNegativeButton("不显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				xianshi = 2;
				showdialogup();
				sendfabu();
			}
		});
		builder.create().show();
	}

	protected void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在上传...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	protected void sendfabu() {
		String startyear = kaishisj1.substring(0, 4);
		String startmonth = kaishisj1.substring(5, 7);
		String startday = kaishisj1.substring(8, 10);
		String starttimeup = startyear + ":" + startmonth + ":" + startday;
		String year = endtime1.substring(0, 4);
		String month = endtime1.substring(5, 7);
		String day = endtime1.substring(8, 10);
		String endtimeup = year + ":" + month + ":" + day;
		client = new AsyncHttpClient();
		params = new RequestParams();
		try {
			File fileimg = new File(path);
			File filepicture = new File(path);// path2
			params.put("uid", IsTrue.userId + "");
			params.put("ip", ip);
			params.put("title", title);
			params.put("match_data_image", fileimg);
			params.put("match_data_images", filepicture);
			params.put("introduce", introduce);
			params.put("time1", starttimeup);
			params.put("time2", endtimeup);
			params.put("state", xianshi);
			params.put("aclass", type2);
			System.out.println(IsTrue.userId + ":" + ip + ":" + title + ":"
					+ fileimg + ":" + filepicture + ":" + introduce + ":"
					+ starttimeup + ":" + endtimeup + ":" + xianshi + ":"
					+ type2);
			client.post(MyUrl.badd, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					System.out.println(response + "SSSSSSSSSSSSSSSSSSS");
					try {
						String num = response.getString("num");
						int i = Integer.parseInt(num);
						if (i == 1) {
							Toast.makeText(getApplicationContext(), "发布失败",
									Toast.LENGTH_SHORT).show();
							jiazaidialog.dismiss();
						} else if (i == 2) {
							sid = response.getString("sid");
							hid = "" + (int) (Math.random() * 10000);
							// 点发起时创建公开群
						
							try {
								String[] members=new String[0];
								EMGroupManager.getInstance().createPublicGroup(
										hid, "", members, false);
							} catch (EaseMobException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}// 需异步处理

							handler.sendEmptyMessage(UP_HX);

							jiazaidialog.dismiss();
							Toast.makeText(getApplicationContext(), "发布成功",
									Toast.LENGTH_SHORT).show();
							finish();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

					super.onSuccess(statusCode, headers, response);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					jiazaidialog.dismiss();
					Toast.makeText(getApplicationContext(),
							errorResponse.toString(), Toast.LENGTH_SHORT)
							.show();
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * 获取要被添加的成员
	 * 
	 * @return
	 */
//	private List<String> getToBeAddMembers() {
//		List<String> members = new ArrayList<String>();
//		int length = contactAdapter.isCheckedArray.length;
//		for (int i = 0; i < length; i++) {
//			String username = contactAdapter.getItem(i).getUsername();
//			if (contactAdapter.isCheckedArray[i] && !exitingMembers.contains(username)) {
//				members.add(username);
//			}
//		}
//
//		return members;
//	}

	private void imgdialog() {
		AlertDialog.Builder builder = new Builder(ReleaseGameActivity.this);
		builder.setTitle("选择图片来源");
		builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent getImageByCamera = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				startActivityForResult(getImageByCamera, 1);
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
				startActivityForResult(intent, 2);
				bitmap = null;
				uri = null;
			}
		});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (imgchange == 1) {
		switch (requestCode) {
		// case 1:
		// String sdStatus = Environment.getExternalStorageState();
		// if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
		// Log.i("TestFile",
		// "SD card is not avaiable/writeable right now.");
		// return;
		// }
		// String name = new DateFormat().format("yyyyMMdd_hhmmss",
		// Calendar.getInstance(Locale.CHINA)) + ".jpg";
		// if (data == null || data.equals("")) {
		// return;
		// }
		// Bundle bundle = data.getExtras();
		// bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
		//
		// FileOutputStream b = null;
		// // ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
		// File file = new File("/sdcard/myImage/");
		// file.mkdirs();// 创建文件夹
		// String fileName = "/sdcard/myImage/" + name;
		//
		// try {
		// b = new FileOutputStream(fileName);
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// b.flush();
		// b.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// path = fileName;
		// img.setImageBitmap(bitmap);
		// break;
		// case 2:
		// if (resultCode == RESULT_OK) {
		// uri = data.getData();
		// ContentResolver cr = this.getContentResolver();
		// try {
		// if (bitmap != null) {
		// bitmap.recycle();
		// bitmap = BitmapFactory.decodeStream(cr
		// .openInputStream(uri));
		// }
		// String[] proj = { MediaStore.Images.Media.DATA };
		// Cursor cursor = managedQuery(uri, proj, null, null,
		// null);
		// // 按我个人理解 这个是获得用户选择的图片的索引值
		// int column_index = cursor
		// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// cursor.moveToFirst();
		// // 最后根据索引值获取图片路径
		// path = cursor.getString(column_index);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// img.setImageURI(uri);
		// } else {
		// Toast.makeText(ReleaseGameActivity.this, "选择图片失败,请重新选择",
		// Toast.LENGTH_SHORT).show();
		// }
		// break;
		// }
		// } else if (imgchange == 2) {
		// switch (requestCode) {
		// case 1:
		// String sdStatus = Environment.getExternalStorageState();
		// if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
		// Log.i("TestFile",
		// "SD card is not avaiable/writeable right now.");
		// return;
		// }
		// String name = new DateFormat().format("yyyyMMdd_hhmmss",
		// Calendar.getInstance(Locale.CHINA)) + ".jpg";
		// if (data == null || data.equals("")) {
		// return;
		// }
		// Bundle bundle = data.getExtras();
		// bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
		//
		// FileOutputStream b = null;
		// // ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
		// File file = new File("/sdcard/myImage/");
		// file.mkdirs();// 创建文件夹
		// String fileName = "/sdcard/myImage/" + name;
		//
		// try {
		// b = new FileOutputStream(fileName);
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// b.flush();
		// b.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// path2 = fileName;
		// picture.setImageBitmap(bitmap);
		// break;
		// case 2:
		// if (resultCode == RESULT_OK) {
		// uri = data.getData();
		// ContentResolver cr = this.getContentResolver();
		// try {
		// if (bitmap != null) {
		// bitmap.recycle();
		// bitmap = BitmapFactory.decodeStream(cr
		// .openInputStream(uri));
		// }
		// String[] proj = { MediaStore.Images.Media.DATA };
		// Cursor cursor = managedQuery(uri, proj, null, null,
		// null);
		// // 按我个人理解 这个是获得用户选择的图片的索引值
		// int column_index = cursor
		// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// cursor.moveToFirst();
		// // 最后根据索引值获取图片路径
		// path2 = cursor.getString(column_index);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// picture.setImageURI(uri);
		// } else {
		// Toast.makeText(ReleaseGameActivity.this, "选择图片失败,请重新选择",
		// Toast.LENGTH_SHORT).show();
		// }
		// break;

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
					Log.d("path", path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				img.setImageURI(uri);
				Log.d("uri", "" + uri);
			} else {
				Toast.makeText(ReleaseGameActivity.this, "选择图片失败,请重新选择",
						Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
		// }

	}

	private void showDialog() {
		IsTrue.xuanzeleixing = 2;
		Dialog dialog = new ApplyDialog(this, R.style.MyDialog,
				new ApplyDialog.PriorityListener() {

					@Override
					public void refreshPriorityUI(String string, String string2) {
						type = string;
						type2 = string2;
						btn_type.setText(string);
					}
				});
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
}
