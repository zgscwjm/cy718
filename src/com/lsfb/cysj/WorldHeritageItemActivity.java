package com.lsfb.cysj;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorldHeritageItemActivity extends FragmentActivity implements OnClickListener{
	/**
	 * world_heritage_item_title 
	 * title 上面标题
	 */
	@ViewInject(R.id.world_heritage_item_title)
	private TextView title;
	/**
	 * world_heritage_item_tijiao 
	 * tijiao 提交
	 */
	@ViewInject(R.id.world_heritage_item_tijiao)
	private LinearLayout tijiao;
	/**
	 * world_heritage_item_title2 
	 * title2 下面的标题
	 */
	@ViewInject(R.id.world_heritage_item_title2)
	private TextView title2;
	/**
	 * world_heritage_item_text
	 * text 内容
	 */
	@ViewInject(R.id.world_heritage_item_text)
	private TextView text;
	/**
	 * world_heritage_item_img 
	 * img 九宫格图片
	 */
	@ViewInject(R.id.world_heritage_item_img)
	private GridView img;
	@ViewInject(R.id.world_heritage_item_back)
	private LinearLayout back;
	@ViewInject(R.id.world_heritage_item_imgs)
	private ImageView imgs;
	@ViewInject(R.id.world_heritage_item_toupiao)
	private TextView toupiao;
	HttpUtils httpUtils;
	RequestParams params;
	Intent intent;
	String id;//遗产id
	BaseAdapter adapter;
	Dialog jiazaidialog;
	String image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.world_heritage_item);
		ViewUtils.inject(this);
		init();
		adapter();
		data();
	}

	private void adapter() {
		img.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new BaseAdapter() {
			
			@SuppressLint("NewApi") @Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.world_heritage_item_img, null);
				ImageView img = (ImageView) view.findViewById(R.id.world_heritage_item_img_item);
				img.setBackground(getResources().getDrawable(R.drawable.myzkimg));
				return view;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				return null;
			}
			
			@Override
			public int getCount() {
				return 0;
			}
		};
		img.setAdapter(adapter);
	}
	protected void showdialogup() {
		jiazaidialog = new ResDialog(WorldHeritageItemActivity.this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("id", id);
		httpUtils.send(HttpMethod.POST, MyUrl.lsinger, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(),
						error.getExceptionCode() + ":" + msg,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				System.out.println(list+"NNN");
				try {
					JSONObject object = new JSONObject(list);
					String name = object.getString("name").toString();
					String introduce = object.getString("introduce").toString();
					image = object.getString("image").toString();
					title.setText(name);
					title2.setText(name);
					text.setText(introduce);
					BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(imgs, ImageAddress.yic+image);
					imgs.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							intent = new Intent(WorldHeritageItemActivity.this,Img.class);
							intent.putExtra("imgs", image);
							startActivity(intent);
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void init() {
		intent = getIntent();
		id = intent.getExtras().getString("id").toString();
		back.setOnClickListener(this);
		tijiao.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.world_heritage_item_back:
			finish();
			break;
		case R.id.world_heritage_item_tijiao:
			toupiao();
			break;
		default:
			break;
		}
	}

	private void toupiao() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("sid", id);
		params.addBodyParameter("cla", 2+"");
		httpUtils.send(HttpMethod.POST, MyUrl.ltoup, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(),
						error.getExceptionCode() + ":" + msg,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				System.out.println(list+"NNN");
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						Toast.makeText(WorldHeritageItemActivity.this, "投票失败",
								Toast.LENGTH_SHORT).show();
					} else if (num.equals("2")) {
						Toast.makeText(WorldHeritageItemActivity.this, "投票成功",
								Toast.LENGTH_SHORT).show();
					} else if (num.equals("3")) {
						Toast.makeText(WorldHeritageItemActivity.this, "明天再来投吧",
								Toast.LENGTH_SHORT).show();
					} else if (num.equals("4")) {
						Toast.makeText(WorldHeritageItemActivity.this, "创币不足",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
