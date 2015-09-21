package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.HeimingdanAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.base.BaseActivity;
import com.lsfb.cysj.base.User;
import com.lsfb.cysj.model.HeimingdanModel;
import com.lsfb.cysj.utils.Constant;
import com.lsfb.cysj.utils.Show;

/**
 * 
 * 黑名单
 * */
public class AcHeiMingdan extends BaseActivity implements OnClickListener {
	private EditText usertxt, nextPass;
	public ProgressDialog pd;
	Context con;
	String phone, username;
	
	List<HeimingdanModel>hList;

	HttpUtils httpUtils;
	RequestParams params;
	ListView listView; 
	HeimingdanAdapter adapter;

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.MODIFY_PASSWORD_OK:
				Show.toast(con, R.string.modify_password_ok);
				// if (!SharedPrefsUtil.getBooleanValue(con, "subIsLogin",
				// false)) {
				// Intent intent = new Intent(con, AcTiYan.class);
				// startActivity(intent);
				// }
				// finish();
				break;
			case Constant.MODIFY_PASSWORD_FAIL:
				Show.toast(con, R.string.modify_password_fail);
				break;

			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_heimingdan);
		con = AcHeiMingdan.this;
		phone = getIntent().getStringExtra("phone");
		usertxt = (EditText) findViewById(R.id.etPassword);
		nextPass = (EditText) findViewById(R.id.password);

		// findViewById(R.id.btn_findpass).setOnClickListener(this);
		findViewById(R.id.btnBack).setOnClickListener(this);
		
		  listView=(ListView) findViewById(R.id.lvHeimingdan);
		// findViewById(R.id.btnSure).setOnClickListener(this);

		initData();

	}
	
	void  refreshView(List<HeimingdanModel>list){
		  adapter=new HeimingdanAdapter(AcHeiMingdan.this, list);
		  listView.setAdapter(adapter);
		
	}

	void initData() {
		hList=new ArrayList<HeimingdanModel>();
		
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");

		httpUtils.send(HttpMethod.POST, MyUrl.heimingdan, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Show.toast(getApplicationContext(), "获取数据失败或黑名单为空");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						String data = responseInfo.result;
						Log.d("pwddata", "" + data);
						try {
							JSONObject obj = new JSONObject(data);
							int num = obj.getInt("num");
							if (2 == num) {
//								Show.toast(getApplicationContext(), "黑名单为空");
								//黑名单不为空，显示黑名单
								String  list=obj.getString("list");
								
								JSONArray jArray=new JSONArray(list);
								hList.clear();
								for(int i=0;i<jArray.length();i++){
									JSONObject jList =(JSONObject) jArray.get(i);
									HeimingdanModel heimingdanModel=new HeimingdanModel();
									heimingdanModel.uid= jList.getString("uid");
									heimingdanModel.nickname= jList.getString("nickname");									
									heimingdanModel.memdj= jList.getString("memdj");
									heimingdanModel.images= jList.getString("images");		
									hList.add(heimingdanModel);
									
								}	
								
								refreshView(hList);
								
							} else {
								Show.toast(getApplicationContext(), "黑名单为空");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;

		case R.id.btnSure: //
			String password = usertxt.getText().toString().trim();
			String nextPassword = nextPass.getText().toString().trim();

			if (!checkUser(password, nextPassword))
				return;

			// User user = new User();
			// user.setMobile(phone);
			// user.setPassword(password);

			modifyPassword(nextPassword);

			break;
		// case R.id.btn_findpass:
		// username = usertxt.getText().toString();
		//
		// break;
		}
	}

	/**
	 * 修改密码
	 * */
	void modifyPassword(String pwd) {

		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter("pwd", pwd + "");

		httpUtils.send(HttpMethod.POST, MyUrl.uppwdname, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Show.toast(getApplicationContext(), "修改密码失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						String data = responseInfo.result;
						Log.d("pwddata", "" + data);

					}
				});

		// App.netManager.updatePassword(user, handler);
		// new NetManager().updatePassword(user, new NetManager.ICallBack() {
		//
		// @Override
		// public void onSuccess() {
		// Show.toast(con, R.string.modify_password_ok);
		// Intent intent = new Intent(con, AcLogin.class);
		// startActivity(intent);
		// finish();
		//
		// }
		//
		// @Override
		// public void onFailed(String error) {
		// Show.toast(con, R.string.modify_password_fail);
		// finish();
		// }
		//
		// });

	}

	public boolean checkUser(String pass, String nextPass) {

		if (!("".equals(pass))) {

			if (pass.length() < 6) {
				Show.toast(this, R.string.error_invalid_password);
				// password.setError(getResources().getString(
				// R.string.error_invalid_password));
				// password.requestFocus();
				return false;
			} else {
				if (pass.equals(nextPass)) {
					return true;
				} else {
					Show.toast(this, R.string.pass_not_match);
					return false;
				}

			}
		} else {
			Show.toast(this, R.string.input_not_empty);
			return false;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else
			//
			return super.onKeyDown(keyCode, event);
	}

}
