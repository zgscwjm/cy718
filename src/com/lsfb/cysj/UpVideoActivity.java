package com.lsfb.cysj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.lsbf.cysj.R;
import com.lsfb.cysj.UpMoreImge.GridAdapter;
import com.lsfb.cysj.UpMoreImge.GridAdapter.ViewHolder;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Bimp;
import com.lsfb.cysj.utils.FileUtils;
import com.lsfb.cysj.utils.ImageItem;
import com.lsfb.cysj.utils.PublicWay;
import com.lsfb.cysj.utils.Show;
import com.lsfb.cysj.view.ResDialog;

/**
 * 上传视频,增加录制
 * @author Administrator
 *
 */
public class UpVideoActivity extends FragmentActivity {
	private LinearLayout upvideo;
	private ImageView iv_upvideo;
	private VideoView Video_show;
	String url = "";
	TextView tv_upvideo;
	private View parentView;
	
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimapphoto;
	
	Map<String, Object> map;
	List<Map<String, Object>> lists;
	int num = 0;
	Dialog jiazaidialog;
	String stringimg="";
	
	private static final int TAKE_VIDEO = 0x000002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upvideo);
		parentView = getLayoutInflater().inflate(R.layout.upvideo, null);
		init();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_VIDEO:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {				
//				String fileName = String.valueOf(System.currentTimeMillis());
//				Bitmap bm = (Bitmap) data.getExtras().get("data");
//				FileUtils.saveBitmap(bm, fileName);
//				ImageItem takePhoto = new ImageItem();
//				takePhoto.setBitmap(bm);
//				Bimp.tempSelectBitmap.add(takePhoto);
				
				Uri uri = data.getData();
				// url = uri.toString();
				Cursor c = getContentResolver().query(uri,
						new String[] { MediaStore.MediaColumns.DATA }, null, null,
						null);// 根据返回的URI，查找数据库，获取视频的路径
				if (c != null && c.moveToFirst()) {
					url = c.getString(0);
					System.err.println(url);

				}

				Video_show.setVisibility(View.VISIBLE);
				Video_show.setVideoPath(url);// 设置了要播放的视频文件位置。
				Video_show.setMediaController(new MediaController(this));//
				// 设置了一个播放控制器。
				Video_show.start();// 程序运行时自动开始播放视频。
				Video_show.requestFocus(); // 播放窗口为当前窗口
			}
			break;
		case  Activity.RESULT_OK:
			Uri uri = data.getData();
			// url = uri.toString();
			Cursor c = getContentResolver().query(uri,
					new String[] { MediaStore.MediaColumns.DATA }, null, null,
					null);// 根据返回的URI，查找数据库，获取视频的路径
			if (c != null && c.moveToFirst()) {
				url = c.getString(0);
				System.err.println(url);

			}

			Video_show.setVisibility(View.VISIBLE);
			Video_show.setVideoPath(url);// 设置了要播放的视频文件位置。
			Video_show.setMediaController(new MediaController(this));//
			// 设置了一个播放控制器。
			Video_show.start();// 程序运行时自动开始播放视频。
			Video_show.requestFocus(); // 播放窗口为当前窗口
			break;
			
		}
//		if (resultCode == Activity.RESULT_OK) {
//			Uri uri = data.getData();
//			// url = uri.toString();
//			Cursor c = getContentResolver().query(uri,
//					new String[] { MediaStore.MediaColumns.DATA }, null, null,
//					null);// 根据返回的URI，查找数据库，获取视频的路径
//			if (c != null && c.moveToFirst()) {
//				url = c.getString(0);
//				System.err.println(url);
//
//			}
//
//			Video_show.setVisibility(View.VISIBLE);
//			Video_show.setVideoPath(url);// 设置了要播放的视频文件位置。
//			Video_show.setMediaController(new MediaController(this));//
//			// 设置了一个播放控制器。
//			Video_show.start();// 程序运行时自动开始播放视频。
//			Video_show.requestFocus(); // 播放窗口为当前窗口
//		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 // 创建退出对话框  
            AlertDialog isExit = new AlertDialog.Builder(this).create();  
            // 设置对话框标题  
            isExit.setTitle("系统提示");  
            // 设置对话框消息  
            isExit.setMessage("确定要退出吗");  
            // 添加选择按钮并注册监听  
            isExit.setButton("确定", listener);  
            isExit.setButton2("取消", listener);  
            // 显示对话框  
            isExit.show();  
		}
		return true;
	}
	 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
//	            	for (int i = 0; i < PublicWay.activityList.size(); i++) {
//	    				if (null != PublicWay.activityList.get(i)) {
//	    					PublicWay.activityList.get(i).finish();
//	    				}
//	    			}
	            	Intent intent = new Intent();
					intent.putExtra("video", "");
					setResult(RESULT_OK, intent);

					finish();
//	    			System.exit(0);
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
	public void uploadMethod(RequestParams params, String uploadHost) {
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
							tv_upvideo
									.setText("上传进度: " + current + "/" + total);
						} else {
							tv_upvideo
									.setText("上传进度: " + current + "/" + total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// msgTextview.setText("reply: " + responseInfo.result);
						Toast.makeText(UpVideoActivity.this, "上传成功",
								Toast.LENGTH_SHORT).show();
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result.toString());
							Intent intent = new Intent();
							intent.putExtra("video", jsonObject.getString("img"));
							setResult(FaBuFriendsActivity.TAKE_VIDEO, intent);//RESULT_OK

							finish();
							System.err.println(responseInfo.result);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// msgTextview.setText(error.getExceptionCode() + ":" +
						// msg);

						Toast.makeText(UpVideoActivity.this,
								error.getExceptionCode() + "",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

//	private void init() {
//		upvideo = (LinearLayout) findViewById(R.id.upmorevideo);
//		iv_upvideo = (ImageView) findViewById(R.id.iv_upvideo);
//		Video_show = (VideoView) findViewById(R.id.Video_show);
//		Video_show.setVisibility(View.GONE);
//		tv_upvideo = (TextView) findViewById(R.id.tv_upvideo);
//		upvideo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// handler = new Handler();
//				// handler.post(runnable);
//				if (url.equals("")) {
//					Toast.makeText(getApplicationContext(), "您还没选择视频", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				RequestParams params = new RequestParams();
//
//				params.addBodyParameter("image", new File(url));
//				String URL = null;
//				if (IsTrue.upmore == 1) {
//					URL = MyUrl.StringUP;
//				}else if (IsTrue.upmore == 2) {
//					URL = MyUrl.uploadfile;
//				}
//				System.out.println(URL+"HHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//				uploadMethod(params, URL);
//			}
//		});
//		iv_upvideo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//				intent.setType("video/*");
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				startActivityForResult(
//						Intent.createChooser(intent, "请选择一个要上传的文件"), 1);
//			}
//		});
//	}
	
	
	private void init() {  
		upvideo = (LinearLayout) findViewById(R.id.upmorevideo);
		iv_upvideo = (ImageView) findViewById(R.id.iv_upvideo);
		Video_show = (VideoView) findViewById(R.id.Video_show);
		Video_show.setVisibility(View.GONE);
		tv_upvideo = (TextView) findViewById(R.id.tv_upvideo);	
		iv_upvideo.setOnClickListener(new OnClickListener() {
			
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							ll_popup.startAnimation(AnimationUtils.loadAnimation(
									UpVideoActivity.this,
									R.anim.activity_translate_in));
							pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
							
//							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//							intent.setType("video/*");
//							intent.addCategory(Intent.CATEGORY_OPENABLE);
//							startActivityForResult(
//									Intent.createChooser(intent, "请选择一个要上传的文件"), 1);
						}
					});
		upvideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// handler = new Handler();
				// handler.post(runnable);
				if (url.equals("")) {
					Toast.makeText(getApplicationContext(), "您还没选择视频", Toast.LENGTH_SHORT).show();
					return;
				}
				RequestParams params = new RequestParams();

				params.addBodyParameter("image", new File(url));
				String URL = null;
				IsTrue.upmore = 1;
				if (IsTrue.upmore == 1) {
					URL = MyUrl.StringUP;
				}else if (IsTrue.upmore == 2) {
					URL = MyUrl.uploadfile;
				}
				System.out.println(URL+"HHHHHHHHHHHHHHHHHHHHHHHHHHHHH");				
				
				uploadMethod(params, URL);
				
				
//				if (url.equals("")) {
//					Toast.makeText(getApplicationContext(), "您还没选择视频", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				RequestParams params = new RequestParams();
//
//				params.addBodyParameter("image", new File(url));
//				String URL = null;
//				if (IsTrue.upmore == 1) {
//					URL = MyUrl.StringUP;
//				}else if (IsTrue.upmore == 2) {
//					URL = MyUrl.uploadfile;
//				}
//				System.out.println(URL+"HHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//				uploadMethod(params, URL);
				
				
//				IsTrue.changeimg = false;
//				if (arg2 == Bimp.tempSelectBitmap.size()) {
//					ll_popup.startAnimation(AnimationUtils.loadAnimation(
//							UpVideoActivity.this,
//							R.anim.activity_translate_in));
//					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//				} 
//				else {
//					Intent intent = new Intent(UpMoreImge.this,
//							GalleryActivity.class);
//					intent.putExtra("position", "1");
//					intent.putExtra("ID", arg2);
//					startActivity(intent);
//				}
			}
		});
		
		
		
		map = new HashMap<String, Object>();
		lists = new ArrayList<Map<String,Object>>();
//		upimg.setOnClickListener(this);
		pop = new PopupWindow(this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows_video,
				null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_video);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("video/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(
						Intent.createChooser(intent, "请选择一个要上传的文件"), 1);
				
//					Intent intent = new Intent(UpVideoActivity.this,
//							AlbumActivity.class);
//					startActivity(intent);
//					overridePendingTransition(R.anim.activity_translate_in,
//							R.anim.activity_translate_out);
//					pop.dismiss();
//					ll_popup.clearAnimation();
				}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
//		photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		gridAdapter = new GridAdapter(this);
//		gridAdapter.update();
//		photo.setAdapter(gridAdapter);
//		photo.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				IsTrue.changeimg = false;
//				if (arg2 == Bimp.tempSelectBitmap.size()) {
//					ll_popup.startAnimation(AnimationUtils.loadAnimation(
//							UpMoreImge.this,
//							R.anim.activity_translate_in));
//					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//				} else {
//					Intent intent = new Intent(UpMoreImge.this,
//							GalleryActivity.class);
//					intent.putExtra("position", "1");
//					intent.putExtra("ID", arg2);
//					startActivity(intent);
//				}
//			}
//		});
		
	}
	protected void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_VIDEO);
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
			return (Bimp.tempSelectBitmap.size() + 1);
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
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.uploadimg_fb));
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
//					gridAdapter.notifyDataSetChanged();
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
	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
//		gridAdapter.update();
		super.onRestart();
	}
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case TAKE_PICTURE:
//			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
//				
//				String fileName = String.valueOf(System.currentTimeMillis());
//				Bitmap bm = (Bitmap) data.getExtras().get("data");
//				FileUtils.saveBitmap(bm, fileName);
//				ImageItem takePhoto = new ImageItem();
//				takePhoto.setBitmap(bm);
//				Bimp.tempSelectBitmap.add(takePhoto);
//			}
//			break;
//		}
//	}
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			 // 创建退出对话框  
//            AlertDialog isExit = new AlertDialog.Builder(this).create();  
//            // 设置对话框标题  
//            isExit.setTitle("系统提示");  
//            // 设置对话框消息  
//            isExit.setMessage("确定要退出吗");  
//            // 添加选择按钮并注册监听  
//            isExit.setButton("确定", listener);  
//            isExit.setButton2("取消", listener);  
//            // 显示对话框  
//            isExit.show();  
//		}
//		return true;
//	}
	 
    
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.upimg:
//			if (Bimp.tempSelectBitmap.size()==0) {
//				Toast.makeText(getApplicationContext(), "您还没有选择图片", Toast.LENGTH_SHORT).show();
//				break;
//			}
//			showdialog();
//			upimgs();
//			break;
//
//		default:
//			break;
//		}
//	}
//	public  void uploadMethod(final com.lidroid.xutils.http.RequestParams paramss,final String uploadHost) {  
//		HttpUtils http = new HttpUtils();
//		http.configTimeout(20000);
//        http.send(HttpRequest.HttpMethod.POST, uploadHost, paramss,new RequestCallBack<String>() {  
//                    @Override  
//                    public void onStart() {  
////                      msgTextview.setText("conn...");  
//                    }  
//                    @Override  
//                    public void onLoading(long total, long current,boolean isUploading) {  
//                        if (isUploading) {  
////                          msgTextview.setText("upload: " + current + "/"+ total);  
//                        } else {  
////                          msgTextview.setText("reply: " + current + "/"+ total);  
//                        }  
//                    }  
//                    @Override  
//                    public void onSuccess(ResponseInfo<String> responseInfo) {  
//                    	Show.toast(getApplicationContext(), "上传成功");
//                    	num++;
//                    	String strings = responseInfo.result;
//                    	Log.d("upimg", strings);
//                    	
//                    	try {
//							JSONObject jsonObject = new JSONObject(strings);
//							if (num == Bimp.tempSelectBitmap.size()) {
//								String strname = jsonObject.getString("img");
//								stringimg += strname;
//							}else {
//								String strname = jsonObject.getString("img")+",";
//								stringimg += strname;
//							}
//							IsTrue.moreimg = stringimg;
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						if (num == Bimp.tempSelectBitmap.size()) {
//							jiazaidialog.dismiss();
//							num = 0 ;
////							Intent intent=new Intent();  
////			                intent.putExtra("imgs", stringimg);  
////			                UpMoreImge.this.setResult(RESULT_OK, intent);
//			                for (int i = 0; i < PublicWay.activityList.size(); i++) {
//			    				if (null != PublicWay.activityList.get(i)) {
//			    					PublicWay.activityList.get(i).finish();
//			    				}
//			    			}
////			                System.exit(0);
////			               UpMoreImge.this.finish();
//						}
//                    }  
//                    @Override  
//                    public void onFailure(HttpException error, String msg) {  
//                    	Show.toast(getApplicationContext(), "上传失败");
//                    	System.out.println(error+msg+"VVVVVVVVVVVVVVVV");
//                    }  
//                });  
//    }  
	private void showdialog() {
		jiazaidialog = new ResDialog(this,R.style.MyDialog, "正在上传...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
//	private void upimgs() {
//		final String SDPATH = Environment.getExternalStorageDirectory()
//				+ "/Photo_LJ/";
//		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//			FileUtils.saveBitmap(Bimp.tempSelectBitmap.get(i).getBitmap(), "myimg"+i);
//			System.out.println("myimg"+i+".jpg");
//		}
//		
//		client = new AsyncHttpClient();
//		client.setTimeout(20000);
//		params = new RequestParams();
//		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//			File file = new File(SDPATH,"myimg"+i+".JPEG");
//			com.lidroid.xutils.http.RequestParams paramss = new com.lidroid.xutils.http.RequestParams();
//			paramss.addBodyParameter("more", file);
//			String URI = null;
//			IsTrue.upmore=1;
//			if (IsTrue.upmore == 1) {
//				URI = MyUrl.StringUP;
//			}else if (IsTrue.upmore == 2) {
//				URI = MyUrl.uploadfile;
//			}
//			System.out.println(URI+"HHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//			uploadMethod(paramss,URI);
//  
//		}
//	}

}
