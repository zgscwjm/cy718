package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
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
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HotZhiKuShenQing extends Activity implements OnClickListener {
	/**
	 * zhikushenqing_names names 姓名
	 */
	@ViewInject(R.id.zhikushenqing_names)
	private EditText names;
	/**
	 * zhikushenqing_mans mans 个人介绍
	 */
	@ViewInject(R.id.zhikushenqing_mans)
	private EditText mans;
	/**
	 * zhikushenqing_zuzhis zuzhis 组织
	 */
	@ViewInject(R.id.zhikushenqing_zuzhis)
	private TextView zuzhis;
	/**
	 * zhikushenqing_lingyus lingyus 领域
	 */
	@ViewInject(R.id.zhikushenqing_lingyus)
	private TextView lingyus;
	/**
	 * zhikushenqing_hangyes hangyes 行业
	 */
	@ViewInject(R.id.zhikushenqing_hangyes)
	private TextView hangyes;
	/**
	 * zhikushenqing_tijiao tijiao 提交
	 */
	@ViewInject(R.id.zhikushenqing_tijiao)
	private Button tijiao;
	/**
	 * zhikushenqing_back back 返回
	 */
	@ViewInject(R.id.zhikushenqing_back)
	private ImageButton back;
	Intent intent;
	String hid;// 智库id
	Dialog jiazaidialog;
	HttpUtils httpUtils;
	RequestParams params;
	/**
	 * uid:会员id name:姓名 bewrite:个人介绍 org:组织id field:领域id ind:行业id hid:智库id
	 */
	String name, bewrite, org = null, field = null, ind = null;
	String orgs[], fields[], inds[];
	/**
	 * ori 组织 li 领域 hy 行业
	 */
	String ori, li, hy;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmaporg;//组织
	ArrayList<HashMap<String, Object>> listmapfield;//领域
	ArrayList<HashMap<String, Object>> listmapind;//行业

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotzhikushenqing);
		ViewUtils.inject(this);
		init();
		data();
	}

	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, MyUrl.Stringzhikuzhuanjiachushihua,
				new RequestCallBack<String>() {

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
						System.out.println(list+"LLLLLLLLLLLLL");
						try {
							JSONObject object = new JSONObject(list);
							ori = object.getString("ori").toString();
							if (ori.equals("1")) {

							} else {
								String orilist = object.getString("orilist")
										.toString();
								JSONArray array = new JSONArray(orilist);
								orgs = new String[array.length()];
								for (int i = 0; i < array.length(); i++) {
									JSONObject object2 = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object2.get("id").toString());
									map.put("type", object2.get("type")
											.toString());
									map.put("name", object2.get("name")
											.toString());
									listmaporg.add(map);
									orgs[i] = object2.get("name").toString();
								}
							}
							li = object.getString("li").toString();
							if (li.equals("1")) {

							} else {
								String lilist = object.getString("lilist")
										.toString();
								JSONArray array = new JSONArray(lilist);
								fields = new String[array.length()];
								for (int i = 0; i < array.length(); i++) {
									JSONObject object2 = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object2.get("id").toString());
									map.put("type", object2.get("type")
											.toString());
									map.put("name", object2.get("name")
											.toString());
									listmapfield.add(map);
									fields[i] = object2.get("name").toString();
								}
							}
							hy = object.getString("hy").toString();
							if (hy.equals("1")) {

							} else {
								String hylist = object.getString("hylist")
										.toString();
								JSONArray array = new JSONArray(hylist);
								inds = new String[array.length()];
								for (int i = 0; i < array.length(); i++) {
									JSONObject object2 = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object2.get("id").toString());
									map.put("type", object2.get("type")
											.toString());
									map.put("name", object2.get("name")
											.toString());
									listmapind.add(map);
									inds[i] = object2.get("name").toString();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmaporg = new ArrayList<HashMap<String, Object>>();
		listmapfield = new ArrayList<HashMap<String, Object>>();
		listmapind = new ArrayList<HashMap<String, Object>>();
		intent = getIntent();
		hid = intent.getExtras().getString("zhikuid").toString();
		zuzhis.setOnClickListener(this);
		lingyus.setOnClickListener(this);
		hangyes.setOnClickListener(this);
		tijiao.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zhikushenqing_back://返回
			finish();
			break;
		case R.id.zhikushenqing_tijiao:
			name = names.getText().toString().trim();
			bewrite = mans.getText().toString().trim();
			if (name.equals("")) {
				Toast.makeText(getApplicationContext(), "姓名未填",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (bewrite.equals("")) {
				Toast.makeText(getApplicationContext(), "个人介绍未填",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (org == null ) {
				Toast.makeText(getApplicationContext(), "请选择组织",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (field==null) {
				Toast.makeText(getApplicationContext(), "请选择领域",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (ind==null) {
				Toast.makeText(getApplicationContext(), "请选择行业",
						Toast.LENGTH_SHORT).show();
				return;
			}
			showdialogup();
			tijiaomsg();
			break;
		case R.id.zhikushenqing_zuzhis:// 按组织
			new AlertDialog.Builder(HotZhiKuShenQing.this).setItems(orgs,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int i) {
							zuzhis.setText(orgs[i]);
							org = listmaporg.get(i).get("id").toString();
							dialog.dismiss();
						}
					}).show();
			break;
		case R.id.zhikushenqing_lingyus:// 按领域
			new AlertDialog.Builder(HotZhiKuShenQing.this).setItems(fields,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int i) {
							lingyus.setText(fields[i]);
							field = listmapfield.get(i).get("id").toString();
							dialog.dismiss();
						}
					}).show();
			break;
		case R.id.zhikushenqing_hangyes:// 按行业
			new AlertDialog.Builder(HotZhiKuShenQing.this).setItems(inds,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int i) {
							hangyes.setText(inds[i]);
							ind = listmapind.get(i).get("id").toString();
							dialog.dismiss();
						}
					}).show();
			break;
		default:
			break;
		}
	}

	private void tijiaomsg() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter("name", name);
		params.addBodyParameter("bewrite", bewrite);
		params.addBodyParameter("org", org);
		params.addBodyParameter("field", field);
		params.addBodyParameter("ind", ind);
		params.addBodyParameter("hid", hid);
		System.out.println(org+"NN"+field+"MM"+ind);
		httpUtils.send(HttpMethod.POST, MyUrl.thapply, params,
				new RequestCallBack<String>() {

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
							String num = object.getString("num").toString();
							if (num.equals("1")) {
								Toast.makeText(getApplicationContext(),"申请失败",Toast.LENGTH_SHORT).show();
							}else if (num.equals("2")) {
								finish();
							}else if (num.equals("3")) {
								Toast.makeText(getApplicationContext(),"已申请了",Toast.LENGTH_SHORT).show();
							}else if (num.equals("4")) {
								Toast.makeText(getApplicationContext(),"同级智库只能加入一个",Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
