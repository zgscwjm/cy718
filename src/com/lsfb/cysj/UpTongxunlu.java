package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Bimp;
import com.lsfb.cysj.utils.FileUtils;
import com.lsfb.cysj.utils.ImageItem;
import com.lsfb.cysj.utils.PublicWay;
import com.lsfb.cysj.utils.Res;
import com.lsfb.cysj.utils.Show;
import com.lsfb.cysj.view.ResDialog;

/**
 * 上传通讯录
 * 
 * @author Administrator
 * 
 */
public class UpTongxunlu extends FragmentActivity implements OnClickListener {
	private GridAdapter gridAdapter;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimapphoto;
	private View parentView;
	private static final int TAKE_PICTURE = 0x000001;

	/**
	 * up tongxunlu 上传通讯录
	 */
	@ViewInject(R.id.btnUp)
	private Button btnUp;

	/**
	 * up tongxunlu 上传通讯录
	 */
	@ViewInject(R.id.imgBack)
	private ImageButton imgBack;

	private Bitmap bitmap;
	AsyncHttpClient client;
	RequestParams params;
	Map<String, Object> map;
	List<Map<String, Object>> lists;
	int num = 0;
	Dialog jiazaidialog;
	String stringimg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimapphoto = BitmapFactory.decodeResource(getResources(),
				R.drawable.uploadimg_fb);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.up_tongxunlu, null);
		setContentView(parentView);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		map = new HashMap<String, Object>();
		lists = new ArrayList<Map<String, Object>>();

		btnUp.setOnClickListener(this);
		imgBack.setOnClickListener(this);
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
				Intent intent = new Intent(UpTongxunlu.this,
						AlbumActivity.class);
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
		gridAdapter.update();
		super.onRestart();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
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

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				for (int i = 0; i < PublicWay.activityList.size(); i++) {
					if (null != PublicWay.activityList.get(i)) {
						PublicWay.activityList.get(i).finish();
					}
				}
				IsTrue.moreimg = "";
				// System.exit(0);
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUp:
			upTongxunlu();
			break;
		case R.id.imgBack:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 上传方法
	 * 
	 * @param paramss
	 * @param uploadHost
	 */
	public void uploadMethod(
			final com.lidroid.xutils.http.RequestParams paramss,
			final String uploadHost) {
		HttpUtils http = new HttpUtils();
		http.configTimeout(20000);
		http.send(HttpRequest.HttpMethod.POST, uploadHost, paramss,
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
						Show.toast(getApplicationContext(), "上传成功");
						num++;
						String strings = responseInfo.result;
						Log.d("upimg", strings);

						try {
							JSONObject jsonObject = new JSONObject(strings);
							if (num == Bimp.tempSelectBitmap.size()) {
								String strname = jsonObject.getString("img");
								stringimg += strname;
							} else {
								String strname = jsonObject.getString("img")
										+ ",";
								stringimg += strname;
							}
							IsTrue.moreimg = stringimg;
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (num == Bimp.tempSelectBitmap.size()) {
							jiazaidialog.dismiss();
							num = 0;
							// Intent intent=new Intent();
							// intent.putExtra("imgs", stringimg);
							// UpMoreImge.this.setResult(RESULT_OK, intent);
							for (int i = 0; i < PublicWay.activityList.size(); i++) {
								if (null != PublicWay.activityList.get(i)) {
									PublicWay.activityList.get(i).finish();
								}
							}
							// System.exit(0);
							// UpMoreImge.this.finish();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Show.toast(getApplicationContext(), "上传失败");
						System.out.println(error + msg + "VVVVVVVVVVVVVVVV");
					}
				});
	}

	private void showdialog() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在上传...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private String getPhone() {
		String strPhone = "";
		Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问所有联系人
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
				null);
		while (cursor.moveToNext()) {
			int contactsId = cursor.getInt(0);
			StringBuilder sb = new StringBuilder();
			// sb.append(contactsId);
			uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contactsId + "/data"); // 某个联系人下面的所有数据
			Cursor dataCursor = resolver.query(uri, new String[] { "mimetype",
					"data1", "data2" }, null, null, null);
			while (dataCursor.moveToNext()) {
				String data = dataCursor.getString(dataCursor
						.getColumnIndex("data1"));
				String type = dataCursor.getString(dataCursor
						.getColumnIndex("mimetype"));
				// if ("vnd.android.cursor.item/name".equals(type)) { //
				// 如果他的mimetype类型是name
				// sb.append(", name=" + data);
				// } else if ("vnd.android.cursor.item/email_v2".equals(type)) {
				// // 如果他的mimetype类型是email
				// sb.append(", email=" + data);
				// } else
				if ("vnd.android.cursor.item/phone_v2".equals(type)) { // 如果他的mimetype类型是phone
					if (sb.toString().length() <= 1)
						sb.append(data);
					else
						sb.append("," + data);
				}
			}
			strPhone = sb.toString();
			Log.d("contact", sb.toString());
			// Log.i(TAG, sb.toString());
		}
		return strPhone;
	}

	/**
	 * 上传通讯录
	 */
	private void upTongxunlu() {
		client = new AsyncHttpClient();
		client.setTimeout(20000);
		params = new RequestParams();
		String strPhone = getPhone();
		Log.d("strPhone", strPhone + "-length-" + strPhone.length());
		if (strPhone.length() <= 1) {
			Show.toast(getApplicationContext(), "通讯录为空");
			return;
		}
		params.put("uid", "" + IsTrue.userId);
		params.put("txl", getPhone());
		client.post(MyUrl.upTongxunlu, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);

				Show.toast(getApplicationContext(), "上传成功");
				finish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Show.toast(getApplicationContext(), "上传失败");
			}
		});

	}
}
