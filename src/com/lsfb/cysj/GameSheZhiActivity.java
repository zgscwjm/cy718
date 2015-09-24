package com.lsfb.cysj;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.JuBaoDialog;
import com.lsfb.cysj.app.ActivityManagerApplication;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Utils;
import com.lsfb.cysj.view.ResDialog;

/**
 * 設置管理，在此取消關注
 * @author Administrator
 *
 */
public class GameSheZhiActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.game_shezhi_back)
	private LinearLayout back;
	/**
	 * game_shezhi_img imgkai 消息开关
	 */
	@ViewInject(R.id.game_shezhi_img)
	private ImageView imgkai;
	/**
	 * game_shezhi_jubao jubao 举报
	 */
	@ViewInject(R.id.game_shezhi_jubao)
	private RelativeLayout jubao;
	/**
	 * game_shezhi_addfriends addfriends 邀请成员
	 */
	@ViewInject(R.id.game_shezhi_addfriends)
	private RelativeLayout addfriends;
	/**
	 * game_shezhi_addman addman 添加智库专家
	 */
	@ViewInject(R.id.game_shezhi_addman)
	private RelativeLayout addman;
	/**
	 * game_shezhi_quxiao quxiao 取消关注
	 */
	@ViewInject(R.id.game_shezhi_quxiao)
	private Button quxiao;
	Intent intent;
	String sid;//大赛id
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	Dialog jiazaidialog;
	String isremind = 0+"";
	
	String memid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_shezhi);
		ViewUtils.inject(this);
		ActivityManagerApplication.addDestoryActivity(this, 2+"");
		init();
		data();
	}
	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("sid", sid);
		httpUtils.send(HttpMethod.POST, MyUrl.bitset, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(getApplicationContext(),
						error.getExceptionCode() + ":" + msg,
						Toast.LENGTH_SHORT).show();
				jiazaidialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String list = responseInfo.result;
				System.out.println(list+"GGGG");
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						imgkai.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
						isremind = 0+"";
					}else if (num.equals("2")) {
						String list2 = object.getString("list");
						JSONObject object2 = new JSONObject(list2);
						isremind = object2.getString("isremind").toString();
						if (isremind.equals("0")) {
							imgkai.setBackgroundResource(R.drawable.tixingandtongzhi1_03);
							isremind = 1+"";
						}else if (isremind.equals("1")) {
							imgkai.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
							isremind = 0+"";
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
			}
		});
	}
	private void init() {
		intent = getIntent();
		sid = intent.getExtras().getString("sid");
		back.setOnClickListener(this);
		addfriends.setOnClickListener(this);
		addman.setOnClickListener(this);
		quxiao.setOnClickListener(this);
		jubao.setOnClickListener(this);
		imgkai.setOnClickListener(this);
		memid=intent.getExtras().getString("memid");
		if (Integer.parseInt(memid)==IsTrue.userId) {
			quxiao.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.game_shezhi_back:
			finish();
			break;
		case R.id.game_shezhi_addfriends://邀请成员
			intent = new Intent(GameSheZhiActivity.this,InviteManActivity.class);
			intent.putExtra("sid",sid);
			intent.putExtra("cla", 1+"");
			startActivity(intent);
			break;
		case R.id.game_shezhi_addman://邀请专家
			intent = new Intent(GameSheZhiActivity.this,InviteManActivity.class);
			intent.putExtra("sid",sid);
			intent.putExtra("cla", 2+"");
			startActivity(intent);
			break;
		case R.id.game_shezhi_quxiao://取消比赛
			if (Utils.isFastDoubleClick()) {
				return;
			}
			showdialogup();
			quxiaogame();
			break;
		case R.id.game_shezhi_jubao://举报
			jubaoDialog();
			break;
		case R.id.game_shezhi_img://开关
			showdialogup();
			msg(isremind);
			break;
		default:
			break;
		}
	}
	private void msg(String string) {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("sid", sid);
		params.addBodyParameter("isremind", string);
		System.out.println(sid+"VVV"+string);
		httpUtils.send(HttpMethod.POST, MyUrl.bitsetpost, params, new RequestCallBack<String>() {

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
				System.out.println(list+"BBBB");
				jiazaidialog.dismiss();
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
					}else if (num.equals("2")) {
						if (isremind.equals("1")) {
							imgkai.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
							isremind = 0+"";
						}else if(isremind.equals("0")){
							imgkai.setBackgroundResource(R.drawable.tixingandtongzhi1_03);
							isremind = 1+"";
						}
					}else if (num.equals("3")) {
					}
					else if (num.equals("4")) {
//						if (isremind.equals("1")) {
//							imgkai.setBackgroundResource(R.drawable.tixingandtongzhi1_03_ed);
//							isremind = 0+"";
//						}else if(isremind.equals("0")){
//							imgkai.setBackgroundResource(R.drawable.tixingandtongzhi1_03);
//							isremind = 1+"";
//						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void jubaoDialog() {
		Dialog dialog = new JuBaoDialog(this, R.style.MyDialog, sid,4+"");
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);		
	}
	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void quxiaogame() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("sid", sid);
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.qxbit, params, new RequestCallBack<String>() {

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
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num");
					if (num.equals("2")) {
						IsTrue.quxiaoguanzhu = 1;
//						intent = new Intent(GameSheZhiActivity.this,HomeActivity.class);
//						startActivity(intent);
						ActivityManagerApplication.destoryActivity("HotIdeasGamesContentActivity");
						Intent intent =new Intent(getApplicationContext(),HotIdeasGamesContent2Activity.class);
						intent.putExtra("sid", sid);
						startActivity(intent);
						finish();
						
					}else if (num.equals("1")) {
						Toast.makeText(getApplicationContext(),
								"取消失败",
								Toast.LENGTH_SHORT).show();
					}else if (num.equals("3")) {
						Toast.makeText(getApplicationContext(),
								"会员id或比赛id丢失(无值)",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
