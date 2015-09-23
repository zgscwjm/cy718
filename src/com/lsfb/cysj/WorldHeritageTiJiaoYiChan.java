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
import com.lsfb.cysj.FaBuFriendsActivity.GridAdapter;
import com.lsfb.cysj.FaBuFriendsActivity.GridAdapter.ViewHolder;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Bimp;
import com.lsfb.cysj.utils.FileUtils;
import com.lsfb.cysj.utils.ImageItem;
import com.lsfb.cysj.view.ResDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
	
	
	@ViewInject(R.id.upImg)
	private GridView gvUp;

	private GridAdapter gridAdapter;

	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	
	private static final int SELECT_PICTURE = 1; // 从图库中选择图片
	private static final int SELECT_CAMER = 2; // 用相机拍摄照片
	Bitmap bitmap = null;
	Uri uri = null;
	String path = "";
	Dialog jiazaidialog;
	String upname,uptext;
	HttpUtils httpUtils;
	RequestParams params;
	Context con;
	public static final int TAKE_PICTURE = 0x000001;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.world_heritage_tijiaoyichan);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		pop = new PopupWindow(this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		con=this.getApplicationContext();
		init();
	}
	private void init() {
		IsTrue.yichan = 0;
		back.setOnClickListener(this);
		img.setOnClickListener(this);
		tijiao.setOnClickListener(this);
				gvUp.setSelector(new ColorDrawable(Color.TRANSPARENT));

		gridAdapter = new GridAdapter(con);
		gridAdapter.update();
		gvUp.setAdapter(gridAdapter);
		gvUp.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				IsTrue.changeimg = false;
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					ll_popup.startAnimation(AnimationUtils.loadAnimation(con,
							R.anim.activity_translate_in));
					//pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(con, GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
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
			if (IsTrue.moreimg == null || IsTrue.moreimg.trim().equals("")) {
				//Log.e("zgscwjm", "moreimgis:"+IsTrue.moreimg);
				Toast.makeText(getApplicationContext(), "请添加图片",
						Toast.LENGTH_SHORT).show();
				return;
			}
			showdialogup();
			upmsg();
			break;
		case R.id.world_heritage_tijiaoyichan_img:
			
		Intent	intent = new Intent(WorldHeritageTiJiaoYiChan.this,
					UpMoreImge.class);
			IsTrue.isYichan=true;
			startActivity(intent);
			//showChoosePhotoDialog();
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
		params.addBodyParameter("images", IsTrue.moreimg);
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
						IsTrue.moreimg = "";
						Bimp.tempSelectBitmap.clear();
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
		Log.e("zgscwjm", "moreimgis:"+IsTrue.moreimg);
		
switch (requestCode) {

	case TAKE_PICTURE:
		if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
			
			String fileName = String.valueOf(System.currentTimeMillis());
			Bitmap bm = (Bitmap) data.getExtras().get("data");
			if(TextUtils.isEmpty(fileName))
				return;
			FileUtils.saveBitmap(bm, fileName);
			ImageItem takePhoto = new ImageItem();
			takePhoto.setBitmap(bm);
			Bimp.tempSelectBitmap.add(takePhoto);
			
		}
		break;
		
//	  case TAKE_VIDEO:
//		  video = data.getExtras().getString("video");// 得到新Activity 关闭后返回的数据
//			more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
//		break;
	}
//		if (requestCode == 1) {
//			video = data.getExtras().getString("video");// 得到新Activity 关闭后返回的数据
//			more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
//		}	
		
	
		
		
//		switch (requestCode) {
//		case SELECT_CAMER:
//			String sdStatus = Environment.getExternalStorageState();
//			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//				Log.i("TestFile",
//						"SD card is not avaiable/writeable right now.");
//				return;
//			}
//			String name = new DateFormat().format("yyyyMMdd_hhmmss",
//					Calendar.getInstance(Locale.CHINA))
//					+ ".jpg";
//			if (data == null || data.equals("")) {
//				return;
//			}
//			Bundle bundle = data.getExtras();
//			bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//
//			FileOutputStream b = null;
//			// ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
//			File file = new File("/sdcard/myImage/");
//			file.mkdirs();// 创建文件夹
//			String fileName = "/sdcard/myImage/" + name;
//
//			try {
//				b = new FileOutputStream(fileName);
//				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					b.flush();
//					b.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			path = fileName;
//			img.setImageBitmap(bitmap);
//			break;
//		case SELECT_PICTURE:
//			// try{
//			if (resultCode == RESULT_OK) {
//				uri = data.getData();
//				ContentResolver cr = this.getContentResolver();
//				try {
//					if (bitmap != null) {
//						bitmap.recycle();
//						bitmap = BitmapFactory.decodeStream(cr
//								.openInputStream(uri));
//					}
//					String[] proj = { MediaStore.Images.Media.DATA };
//					Cursor cursor = managedQuery(uri, proj, null, null, null);
//					// 按我个人理解 这个是获得用户选择的图片的索引值
//					int column_index = cursor
//							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//					cursor.moveToFirst();
//					// 最后根据索引值获取图片路径
//					path = cursor.getString(column_index);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				img.setImageURI(uri);
//			} else {
//				Toast.makeText(WorldHeritageTiJiaoYiChan.this, "选择图片失败,请重新选择",
//						Toast.LENGTH_SHORT).show();
//			}
//			break;
//		}
	}
	
	
	
	
	
	
	
	
	class GridAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return Bimp.tempSelectBitmap.size() ;
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
//				holder.image.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.uploadimg_fb));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					gridAdapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}

	}
	
	
	
	
}
