package com.lsfb.cysj;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FaBuFriendsActivity extends Activity {
	private EditText text;
	private ImageView more;
	String content;// 上传内容
	private LinearLayout fabu;
	AsyncHttpClient client;
	RequestParams params;
	Dialog jiazaidialog;
	String state;//1显示2不显示
	Intent intent;
	String video;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fabufriends);
		init();
		data();
	}

	private void data() {
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MoreDialog();
			}
		});
		fabu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				content = text.getText().toString().trim();
				if (content.equals("")) {
					Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
				}
				else {
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
				state = 1+"";
				fabumsg();
			}
		});
		builder.setNegativeButton("不显示", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showdialogup();
				state = 2+"";
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode ==  1) {
			video = data.getExtras().getString("video");// 得到新Activity 关闭后返回的数据
			more.setBackgroundResource(R.drawable.uploadimg_fbvideo);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	protected void fabumsg() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId);
		params.put("content", content);
		params.put("state", state);
		if (video==null) {
		}else {
			params.put("video", video);
		}
		if (IsTrue.moreimg.trim().equals("")) {
		}else {
			params.put("image", IsTrue.moreimg);
		}
		System.out.println(content + "TTTTTTTTTTT" + state+"SSSSSSSSS"+video+"DDDDDDDD"+IsTrue.moreimg);
		client.post(MyUrl.dtadd, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 1) {
						Toast.makeText(getApplicationContext(), "发布失败", Toast.LENGTH_SHORT).show();
						jiazaidialog.dismiss();
					}else if (i==2) {
						Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
						jiazaidialog.dismiss();
						video = "";
						content = "";
						state = "";
						IsTrue.moreimg = "";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(), errorResponse.toString(), Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void init() {
		text = (EditText) findViewById(R.id.fabu_friends_content);
		more = (ImageView) findViewById(R.id.fabu_friends_more);
		fabu = (LinearLayout) findViewById(R.id.fabu_friends_fabu);
	}
}
