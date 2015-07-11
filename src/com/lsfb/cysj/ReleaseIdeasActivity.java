package com.lsfb.cysj;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.ApplyDialog;
import com.lsfb.cysj.Dialog.FaBuDialog;
import com.lsfb.cysj.Dialog.UpImgDialog;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Res;
import com.lsfb.cysj.view.ResDialog;

public class ReleaseIdeasActivity extends FragmentActivity implements
		OnClickListener {
	/**
	 * release_ideas_btn 选择发布创意类型
	 */
	@ViewInject(R.id.release_ideas_btn)
	private Button release_ideas_btn;
	/**
	 * release_ideas_title 标题
	 */
	@ViewInject(R.id.release_ideas_title)
	private EditText release_ideas_title;
	/**
	 * release_ideas_content 正文内容
	 */
	@ViewInject(R.id.release_ideas_content)
	private EditText release_ideas_content;
	/**
	 * release_ideas_num 创意价值
	 */
	@ViewInject(R.id.release_ideas_num)
	private EditText release_ideas_num;

	@ViewInject(R.id.release_ideas_back)
	private LinearLayout back;
	@ViewInject(R.id.release_ideas_fabu)
	private LinearLayout fabu;
	/**
	 * release_ideas_picture picture 上传图片
	 */
	@ViewInject(R.id.release_ideas_picture)
	private ImageView picture;
	/**
	 * release_ideas_more more 上传更多
	 */
	@ViewInject(R.id.release_ideas_more)
	private ImageView more;
	// String type;//创意类型
	// String state;//显示不显示
	private File mPhotoFile;
	private String mPhotoPath;

	private static final int SELECT_PICTURE = 1; // 从图库中选择图片
	private static final int SELECT_CAMER = 2; // 用相机拍摄照片
	private static final String IMAGE_FILE_NAME = "img.jpg";
	/**
	 * state 1.显示 2.不显示 type 创意类型 price 创意价值 content 作品内容 title 创意作品 ip 用户登录的ip
	 * type2 创意传值数字
	 */
	String ip, ip2, title, content, price, type, type2;
	Intent intent;
	AsyncHttpClient client;
	RequestParams params;
	Bitmap bitmap = null;
	String path = "";
	String video = "";
	Uri uri = null;
	Dialog jiazaidialog;

	// String string2 = "";//上传类型数字
	// String type2 = "";//上传类型数字
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.release_ideas);
		// 默认不弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Res.init(this);
		// bimapphoto = BitmapFactory.decodeResource(getResources(),
		// R.drawable.uploadimg_fb);
		// PublicWay.activityList.add(this);
		// parentView = getLayoutInflater().inflate(R.layout.release_ideas,
		// null);
		// setContentView(parentView);
		ViewUtils.inject(this);
		if (IsTrue.userId == 0) {
			intent = new Intent(ReleaseIdeasActivity.this, HomeActivity.class);
			IsTrue.tabnum = 4;
			IsTrue.exit = true;
			finish();
			startActivity(intent);
			return;
		}
		new Thread(runnable).start();
		init();
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			GetNetIp();
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	private void init() {
		IsTrue.moreimg = "";
		IsTrue.upmore = 0;
		getip();
		release_ideas_btn.setOnClickListener(this);
		back.setOnClickListener(this);
		fabu.setOnClickListener(this);
		picture.setOnClickListener(this);
		more.setOnClickListener(this);
		type = release_ideas_btn.getText().toString().trim();
	}

	/**
	 * getLocalIpAddress 手机ip
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Toast.makeText(this, "网络出错啦", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (IsTrue.search = true) {
//			intent = new Intent(ReleaseIdeasActivity.this,SearchActivity.class);
//			startActivity(intent);
//			IsTrue.search = false;
//		}
		return super.onKeyDown(keyCode, event);
	}
	public static String GetNetIp() {
		URL infoUrl = null;
		InputStream inStream = null;
		try {
			infoUrl = new URL("http://city.ip138.com/city0.asp");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inStream, "utf-8"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();

				// 从反馈的结果中提取出IP地址
				int start = strber.indexOf("[");
				int end = strber.indexOf("]", start + 1);
				line = strber.substring(start + 1, end);
				System.out.println();
				return line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.release_ideas_btn:// 选择发布创意类型
			showDialog();
			break;
		case R.id.release_ideas_back:// 返回
			finish();
			break;
		case R.id.release_ideas_picture:// 上传图片
			showChoosePhotoDialog();
			break;
		case R.id.release_ideas_more:// 上传其他
			IsTrue.upmore = 1;
			MoreDialog();
			break;
		case R.id.release_ideas_fabu:// 发布
			// fabuDialog();
			// GetNetIp();
			title = release_ideas_title.getText().toString().trim();
			content = release_ideas_content.getText().toString().trim();
			price = release_ideas_num.getText().toString().trim();
			if (type.equals("选择发布创意类型")) {
				Toast.makeText(getApplicationContext(), "请选择作品类型",
						Toast.LENGTH_SHORT).show();
				return;
			}
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
			if (price.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入作品价值",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// System.out.println(bitmap+"***************************"+uri);
			// if (bitmap == null && uri == null) {
			// Toast.makeText(getApplicationContext(), "请添加图片",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			if (path == null || path.trim().equals("")) {
				Toast.makeText(getApplicationContext(), "请添加图片",
						Toast.LENGTH_SHORT).show();
				return;
			}
			dialog();
			break;
		default:
			break;
		}
	}

	private void getip() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, "http://city.ip138.com/ip2city.asp",new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				System.out.println(arg0 + arg0.result.toString());
				 //从反馈的结果中提取出IP地址
	            int start = arg0.result.toString().indexOf("[");
	            int end = arg0.result.toString().indexOf("]", start + 1);
	            ip = arg0.result.toString().substring(start + 1, end);
	            System.out.println(ip);
			}
		});
	}

	private void MoreDialog() {
		AlertDialog.Builder builder = new Builder(ReleaseIdeasActivity.this);
		builder.setTitle("更多资料上传");
		builder.setPositiveButton("上传多图",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						intent = new Intent(ReleaseIdeasActivity.this,
								UpMoreImge.class);
						// startActivityForResult(intent, 3);
						startActivity(intent);
						video = "";
						more.setBackgroundResource(R.drawable.uploadimg_fb);
					}
				});
		builder.setNegativeButton("上传视频",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						intent = new Intent(ReleaseIdeasActivity.this,
								UpVideoActivity.class);
						startActivityForResult(intent, 4);
						IsTrue.moreimg = "";
						more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
					}
				});
		builder.create().show();
	}

	private void dialog() {
		AlertDialog.Builder builder = new Builder(ReleaseIdeasActivity.this);
		builder.setTitle("是否显示");
		builder.setPositiveButton("显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// if (IsTrue.moreimg.trim().equals("")) {
				// if (video.trim().equals("")) {
				showdialogup();
				fabumsg();
				// }
				// }
			}
		});
		builder.setNegativeButton("不显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// if (IsTrue.moreimg.trim().equals("")) {
				// if (video.trim().equals("")) {
				showdialogup();
				fabumsg2();
				// }
				// }
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

	public void uploadMethod(
			final com.lidroid.xutils.http.RequestParams params,
			final String uploadHost) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Toast.makeText(getApplicationContext(), "succes",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(getApplicationContext(), "onFailure",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void UpDialog() {
		Dialog dialog = new UpImgDialog(this, R.style.MyDialog);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(ReleaseIdeasActivity.this);
		builder.setTitle("选择图片来源");
		builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// try {
				// Intent intent = new Intent(
				// "android.media.action.IMAGE_CAPTURE");
				// mPhotoPath = "mnt/sdcard/DCIM/Camera/" + getPhotoFileName();
				// mPhotoFile = new File(mPhotoPath);
				// if (!mPhotoFile.exists()) {
				// mPhotoFile.createNewFile();
				// }
				// intent.putExtra(MediaStore.EXTRA_OUTPUT,
				// Uri.fromFile(mPhotoFile));
				// startActivityForResult(intent, SELECT_CAMER);
				// } catch (Exception e) {
				// }
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECT_CAMER:
			// bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
			// picture.setImageBitmap(bitmap);
			// try {
			// Bundle extras = data.getExtras();
			// bitmap = (Bitmap) extras.get("data");
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// bitmap.compress(Bitmap.CompressFormat.JPEG , 50, baos);
			// byte[] mContent =baos.toByteArray();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

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
				Toast.makeText(ReleaseIdeasActivity.this, "选择图片失败,请重新选择",
						Toast.LENGTH_SHORT).show();
			}
			// Uri selectedImage = data.getData();
			// String[] filePathColumn = { MediaStore.Images.Media.DATA };
			//
			// Cursor cursor = getContentResolver().query(selectedImage,
			// filePathColumn, null, null, null);
			// cursor.moveToFirst();
			//
			// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			// String picturePath = cursor.getString(columnIndex);
			// cursor.close();
			// picture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
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

	// 发布信息，不显示
	protected void fabumsg2() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		try {
			File file = new File(path);
			params.put("works_data_image", file);
			params.put("ip", ip);
			params.put("title", title);
			params.put("class", type2);
			params.put("content", content);
			params.put("price", price);
			params.put("state", 2);
			params.put("creators", IsTrue.userId);
			params.put("images", IsTrue.moreimg);
			params.put("works_data_video", video);
			System.out.println(ip+"SSSSSSSSSSSSSSSSSS");
			client.post(MyUrl.origadd, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					System.out.println(error);
					jiazaidialog.dismiss();
					Toast.makeText(getApplicationContext(), "失败" + error,
							Toast.LENGTH_LONG).show();
				}
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					Toast.makeText(getApplicationContext(), "成功",
							Toast.LENGTH_LONG).show();
					IsTrue.moreimg = "";
					jiazaidialog.dismiss();
					finish();
				};
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发布信息,显示
	private void fabumsg() {
		HttpUtils httpUtils = new HttpUtils();
		com.lidroid.xutils.http.RequestParams requestParams = new com.lidroid.xutils.http.RequestParams();
		File file = new File(path);
		requestParams.addBodyParameter("works_data_image", file);
		requestParams.addBodyParameter("ip", ip);
		requestParams.addBodyParameter("title", title);
		requestParams.addBodyParameter("class", type2);
		requestParams.addBodyParameter("content", content);
		requestParams.addBodyParameter("price", price);
		requestParams.addBodyParameter("state", 1+"");
		requestParams.addBodyParameter("creators", IsTrue.userId+"");
		requestParams.addBodyParameter("images", IsTrue.moreimg);
		requestParams.addBodyParameter("works_data_video", video);
		System.out.println(ip + "%%%%%%%%%%%%%%%%%%%");
		httpUtils.send(HttpMethod.POST, MyUrl.origadd, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(error);
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(),error +":" +msg.toString(),
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String list = responseInfo.result;
				System.out.println(list+"KKKKKKKKKKKKKKKK");
				IsTrue.moreimg = "";
				jiazaidialog.dismiss();
				finish();
			}
		});
//		client = new AsyncHttpClient();
//		params = new RequestParams();
//		try {
//			File file = new File(path);
//			params.put("works_data_image", file);
//			params.put("ip", ip);
//			params.put("title", title);
//			params.put("class", type2);
//			params.put("content", content);
//			params.put("price", price);
//			params.put("state", 1);
//			params.put("creators", IsTrue.userId);
//			params.put("images", IsTrue.moreimg);
//			params.put("works_data_video", video);
//			System.out.println(ip + "%%%%%%%%%%%%%%%%%%%");
//			client.post(MyUrl.origadd, params, new AsyncHttpResponseHandler() {
//
//				@Override
//				public void onFailure(int statusCode, Header[] headers,
//						byte[] responseBody, Throwable error) {
//					System.out.println(error);
//					jiazaidialog.dismiss();
//					Toast.makeText(getApplicationContext(), "失败" + error,
//							Toast.LENGTH_LONG).show();
//				}
//				@Override
//				public void onSuccess(int statusCode, Header[] headers,
//						byte[] responseBody) {
//					System.out.println(responseBody.toString()+"MM"+responseBody);
//					Toast.makeText(getApplicationContext(), "成功",
//							Toast.LENGTH_LONG).show();
//					IsTrue.moreimg = "";
//					jiazaidialog.dismiss();
//					finish();
//				};
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void showDialog() {
		IsTrue.xuanzeleixing = 1;
		Dialog dialog = new ApplyDialog(this, R.style.MyDialog,
				new ApplyDialog.PriorityListener() {

					@Override
					public void refreshPriorityUI(String string, String string2) {
						type = string;
						release_ideas_btn.setText(type);
						type2 = string2;
					}
				});
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
}
