package com.lsfb.cysj;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;

import com.lsfb.cysj.UpMoreImge.GridAdapter.ViewHolder;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 发布创友圈
 * 
 * @author Administrator
 * 
 */
public class FaBuFriendsActivity extends Activity implements OnClickListener{
	private EditText text;
	private Button more, fabuVideo;
	String content;// 上传内容
	private LinearLayout fabu, back;
	AsyncHttpClient client;
	RequestParams params;
	Dialog jiazaidialog;
	String state;// 1显示2不显示
	Intent intent;
	String video;
	Context con;

	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimapphoto;
	private View parentView;
	public static final int TAKE_PICTURE = 0x000001;
	public static final int TAKE_VIDEO = 0x000002;

	private GridView gvUp;
	private GridAdapter gridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.fabufriends);
		con = FaBuFriendsActivity.this;
		parentView = getLayoutInflater().inflate(R.layout.fabufriends, null);
		setContentView(parentView);
		init();
		data();
	}

	private void data() {
		/*
		 * 如果已经上传图片//则在次显示缩略图，没有上传则弹窗进入添加图片界面
		 */
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果已经上传图片则在次显示缩略图，没有上传则弹窗进入添加图片界面
				// if(){
				//
				// }
				MoreDialog();
			}
		});
		//
		fabuVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果已经上传图片则在次显示缩略图，没有上传则弹窗进入添加图片界面
				// if(){
				//
				// }
				MoreDialog();
			}
		});

		fabu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				content = text.getText().toString().trim();
				if (content.equals("")) {
					Toast.makeText(getApplicationContext(), "请输入内容",
							Toast.LENGTH_SHORT).show();
				} else {
					fabudialog();
				}
			}
		});
	}

	protected void fabudialog() {
		AlertDialog.Builder builder = new Builder(FaBuFriendsActivity.this);
		builder.setTitle("是否显示");
		builder.setPositiveButton("显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showdialogup();
				state = 1 + "";
				fabumsg();
			}
		});
		builder.setNegativeButton("不显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showdialogup();
				state = 2 + "";
				fabumsg();
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

	protected void MoreDialog() {
		AlertDialog.Builder builder = new Builder(FaBuFriendsActivity.this);
		builder.setTitle("更多资料上传");
		builder.setPositiveButton("上传多图",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						intent = new Intent(FaBuFriendsActivity.this,
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
						intent = new Intent(FaBuFriendsActivity.this,
								UpVideoActivity.class);
						startActivityForResult(intent, 1);
						IsTrue.moreimg = "";
						more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
					}
				});
		builder.create().show();
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == 1) {
//			video = data.getExtras().getString("video");// 得到新Activity 关闭后返回的数据
//			more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	protected void fabumsg() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId);
		params.put("content", content);
		params.put("state", state);
		if (video == null) {
		} else {
			params.put("video", video);
		}
		if (IsTrue.moreimg.trim().equals("")) {
		} else {
			params.put("image", IsTrue.moreimg);
		}
		System.out.println(content + "TTTTTTTTTTT" + state + "SSSSSSSSS"
				+ video + "DDDDDDDD" + IsTrue.moreimg);
		client.post(MyUrl.dtadd, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(getApplicationContext(), "发布失败",
								Toast.LENGTH_SHORT).show();
						jiazaidialog.dismiss();
					} else if (i == 2) {
						Toast.makeText(getApplicationContext(), "发布成功",
								Toast.LENGTH_SHORT).show();
						jiazaidialog.dismiss();
						video = "";
						content = "";
						state = "";
						IsTrue.moreimg = "";
						Bimp.tempSelectBitmap.clear();
						
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
				Toast.makeText(getApplicationContext(),
						errorResponse.toString(), Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void init() {
		text = (EditText) findViewById(R.id.fabu_friends_content);
		more = (Button) findViewById(R.id.fabu_friends_more);
		fabuVideo = (Button) findViewById(R.id.fabu_video);
		fabu = (LinearLayout) findViewById(R.id.fabu_friends_fabu);
		back = (LinearLayout) findViewById(R.id.fabu_friends_back);
		gvUp = (GridView) findViewById(R.id.upImg);

		pop = new PopupWindow(this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
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
				Intent intent = new Intent(con, AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in,
						R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
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
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(con, GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	protected void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
			
		  case TAKE_VIDEO:
			  video = data.getExtras().getString("video");// 得到新Activity 关闭后返回的数据
				more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
			break;
			
//			if (requestCode == 1) {
//				video = data.getExtras().getString("video");// 得到新Activity 关闭后返回的数据
//				more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
//			}	
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
