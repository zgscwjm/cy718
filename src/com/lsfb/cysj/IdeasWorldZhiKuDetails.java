package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;

public class IdeasWorldZhiKuDetails extends FragmentActivity implements
		OnClickListener {
	Intent intent;
	/**
	 * ideas_world_zhiku_details_result result 创意世界优秀结果
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_result)
	private Button result;
	/**
	 * ideas_world_zhiku_details_heritage heritage 创意世界优秀遗产
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_heritage)
	private Button heritage;
	/**
	 * city_details_linearlayout1_img6 more1 更多1
	 */
	@ViewInject(R.id.city_details_linearlayout1_img6)
	private RelativeLayout more1;
	/**
	 * city_details_linearlayout2_img6 more2 更多2
	 */
	@ViewInject(R.id.city_details_linearlayout2_img6)
	private RelativeLayout more2;
	/**
	 * city_details_linearlayout3_img6 more3 更多3
	 */
	@ViewInject(R.id.city_details_linearlayout3_img6)
	private RelativeLayout more3;
	/**
	 * ideas_world_zhiku_details_shezhi shezhi 设置
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_shezhi)
	private LinearLayout shezhi;
	/**
	 * ideas_world_zhiku_details_dating dating 大厅 dating
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_dating)
	private LinearLayout dating;
	@ViewInject(R.id.ideas_world_zhiku_details_back)
	private LinearLayout back;
	/**
	 * city_details_linearlayout_img1 主席团图表一
	 */
	@ViewInject(R.id.city_details_linearlayout_img1)
	private ImageView city_details_linearlayout_img1;
	/**
	 * city_details_linearlayout_img2 主席团图表2
	 */
	@ViewInject(R.id.city_details_linearlayout_img2)
	private ImageView city_details_linearlayout_img2;
	/**
	 * city_details_linearlayout_img3 主席团图表3
	 */
	@ViewInject(R.id.city_details_linearlayout_img3)
	private ImageView city_details_linearlayout_img3;
	/**
	 * city_details_linearlayout_img4 主席团图表4
	 */
	@ViewInject(R.id.city_details_linearlayout_img4)
	private ImageView city_details_linearlayout_img4;
	/**
	 * city_details_linearlayout_img5 主席团图表5
	 */
	@ViewInject(R.id.city_details_linearlayout_img5)
	private ImageView city_details_linearlayout_img5;
	/**
	 * city_details_linearlayout2_img1 理事会图标一
	 */
	@ViewInject(R.id.city_details_linearlayout2_img1)
	private ImageView city_details_linearlayout2_img1;
	/**
	 * city_details_linearlayout2_img2理事会图标2
	 */
	@ViewInject(R.id.city_details_linearlayout2_img2)
	private ImageView city_details_linearlayout2_img2;
	/**
	 * city_details_linearlayout2_img3 理事会图标3
	 */
	@ViewInject(R.id.city_details_linearlayout2_img3)
	private ImageView city_details_linearlayout2_img3;
	/**
	 * city_details_linearlayout2_img4 理事会图标4
	 */
	@ViewInject(R.id.city_details_linearlayout2_img4)
	private ImageView city_details_linearlayout2_img4;
	/**
	 * city_details_linearlayout2_img5 理事会图标5
	 */
	@ViewInject(R.id.city_details_linearlayout2_img5)
	private ImageView city_details_linearlayout2_img5;
	/**
	 * city_details_linearlayout3_img1 专家顾问团图标一
	 */
	@ViewInject(R.id.city_details_linearlayout3_img1)
	private ImageView city_details_linearlayout3_img1;
	/**
	 * city_details_linearlayout3_img2 专家顾问团图标2
	 */
	@ViewInject(R.id.city_details_linearlayout3_img2)
	private ImageView city_details_linearlayout3_img2;
	/**
	 * city_details_linearlayout3_img3专家顾问团图标3
	 */
	@ViewInject(R.id.city_details_linearlayout3_img3)
	private ImageView city_details_linearlayout3_img3;
	/**
	 * city_details_linearlayout3_img4专家顾问团图标4
	 */
	@ViewInject(R.id.city_details_linearlayout3_img4)
	private ImageView city_details_linearlayout3_img4;
	/**
	 * city_details_linearlayout3_img5 专家顾问团图标5
	 */
	@ViewInject(R.id.city_details_linearlayout3_img5)
	private ImageView city_details_linearlayout3_img5;

	/**
	 * ideas_world_zhiku_details_scrollview 显示最上面
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_scrollview)
	private ScrollView ideas_world_zhiku_details_scrollview;
	/**
	 * ideas_world_zhiku_details_apply addzhiku 申请加入智库
	 */
	@ViewInject(R.id.ideas_world_zhiku_details_apply)
	private Button addzhiku;
	/**
	 * zhuangtai 弹窗
	 */
	@ViewInject(R.id.zhuangtai)
	private LinearLayout zhuangtai;
	@ViewInject(R.id.ideas_world_zhiku_details_city)
	private TextView city;
	PopupWindow popupwindow;
	HttpUtils httpUtils;
	RequestParams params;
	String tid;// 智库id
	Dialog jiazaidialog;
	BitmapUtils bitmapUtils;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	ArrayList<HashMap<String, Object>> listimg1;
	ArrayList<HashMap<String, Object>> listimg2;
	ArrayList<HashMap<String, Object>> listimg3;
	ArrayList<HashMap<String, Object>> popview;
	private LinearLayout zhikuguanli;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideas_world_zhiku_details);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ViewUtils.inject(this);
		init();
		data();
	}

	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter("tid", tid);
		httpUtils.send(HttpMethod.POST, MyUrl.thsinger, params,
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
						String list = responseInfo.result;
						jiazaidialog.dismiss();
						System.out.println(list + "MMM");
						try {
							JSONObject object = new JSONObject(list);
							System.out.println(object + "SSS");
							String name = object.getString("name").toString();
							city.setText(name);
							String zhuxi = object.getString("zhuxi").toString();
							if (zhuxi.equals("0")) {
							} else if (zhuxi.equals("1")) {
								zhuxituan(object);
							}
							String lsh = object.getString("lsh").toString();
							if (lsh.equals("0")) {
							} else if (lsh.equals("1")) {
								lishihui(object);
							}
							String gwt = object.getString("gwt").toString();
							if (gwt.equals("0")) {
							} else if (gwt.equals("1")) {
								zhuanjiatuan(object);
							}
							String zkzb = object.getString("zkzb").toString();
							if (zkzb.equals("0")) {
							}else {
								popview(object);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void zhuanjiatuan(JSONObject object) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			String gwtlist = object.getString("gwtlist").toString();
			JSONArray array = new JSONArray(gwtlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object2.getString("uid").toString());
				map.put("image", object2.getString("image").toString());
				listmap.add(map);
			}
			listimg3 = listmap;
			bitmapUtils = new BitmapUtils(getApplicationContext());
			switch (listmap.size()) {
			case 1:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				break;
			case 2:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				break;
			case 3:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout3_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				break;
			case 4:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout3_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout3_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				break;
			case 5:
				city_details_linearlayout3_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout3_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout3_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout3_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				city_details_linearlayout3_img5.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout3_img5,
						ImageAddress.Stringhead
								+ listmap.get(4).get("image").toString());
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void lishihui(JSONObject object) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			String lshlist = object.getString("lshlist").toString();
			JSONArray array = new JSONArray(lshlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object2.getString("uid").toString());
				map.put("image", object2.getString("image").toString());
				listmap.add(map);
			}
			listimg2 = listmap;
			bitmapUtils = new BitmapUtils(getApplicationContext());
			switch (listmap.size()) {
			case 1:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				break;
			case 2:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				break;
			case 3:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout2_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				break;
			case 4:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout2_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout2_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				break;
			case 5:
				city_details_linearlayout2_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout2_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout2_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout2_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				city_details_linearlayout2_img5.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout2_img5,
						ImageAddress.Stringhead
								+ listmap.get(4).get("image").toString());
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void zhuxituan(JSONObject object) {
		listmap = new ArrayList<HashMap<String, Object>>();
		try {
			String zxlist = object.getString("zxlist").toString();
			JSONArray array = new JSONArray(zxlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object2.getString("uid").toString());
				map.put("image", object2.getString("image").toString());
				listmap.add(map);
			}
			listimg1 = listmap;
			bitmapUtils = new BitmapUtils(getApplicationContext());
			switch (listmap.size()) {
			case 1:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				break;
			case 2:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				break;
			case 3:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				break;
			case 4:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				break;
			case 5:
				city_details_linearlayout_img1.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img1,
						ImageAddress.Stringhead
								+ listmap.get(0).get("image").toString());
				city_details_linearlayout_img2.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img2,
						ImageAddress.Stringhead
								+ listmap.get(1).get("image").toString());
				city_details_linearlayout_img3.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img3,
						ImageAddress.Stringhead
								+ listmap.get(2).get("image").toString());
				city_details_linearlayout_img4.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img4,
						ImageAddress.Stringhead
								+ listmap.get(3).get("image").toString());
				city_details_linearlayout_img5.setVisibility(View.VISIBLE);
				bitmapUtils.display(city_details_linearlayout_img5,
						ImageAddress.Stringhead
								+ listmap.get(4).get("image").toString());
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void showdialogup() {
		jiazaidialog = new ResDialog(IdeasWorldZhiKuDetails.this,
				R.style.MyDialog, "正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		listimg1 = new ArrayList<HashMap<String,Object>>();
		listimg2 = new ArrayList<HashMap<String,Object>>();
		listimg3 = new ArrayList<HashMap<String,Object>>();
		popview = new ArrayList<HashMap<String,Object>>();
		result.setOnClickListener(this);
		heritage.setOnClickListener(this);
		more1.setOnClickListener(this);
		more2.setOnClickListener(this);
		more3.setOnClickListener(this);
		shezhi.setOnClickListener(this);
		dating.setOnClickListener(this);
		addzhiku.setOnClickListener(this);
		back.setOnClickListener(this);
		city_details_linearlayout_img1.setOnClickListener(this);
		city_details_linearlayout_img2.setOnClickListener(this);
		city_details_linearlayout_img3.setOnClickListener(this);
		city_details_linearlayout_img4.setOnClickListener(this);
		city_details_linearlayout_img5.setOnClickListener(this);
		city_details_linearlayout2_img1.setOnClickListener(this);
		city_details_linearlayout2_img2.setOnClickListener(this);
		city_details_linearlayout2_img3.setOnClickListener(this);
		city_details_linearlayout2_img4.setOnClickListener(this);
		city_details_linearlayout2_img5.setOnClickListener(this);
		city_details_linearlayout3_img1.setOnClickListener(this);
		city_details_linearlayout3_img2.setOnClickListener(this);
		city_details_linearlayout3_img3.setOnClickListener(this);
		city_details_linearlayout3_img4.setOnClickListener(this);
		city_details_linearlayout3_img5.setOnClickListener(this);
		intent = getIntent();
		tid = intent.getExtras().getString("zhikuid").toString();
		zhikuguanli = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.popuptwindow, null);
	}

	@Override
	protected void onResume() {
		ideas_world_zhiku_details_scrollview.smoothScrollTo(0, 0);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		String uid = null;
		switch (v.getId()) {
		case R.id.ideas_world_zhiku_details_back:
			finish();
			break;
		case R.id.ideas_world_zhiku_details_result:// 创意世界优秀结果
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					BestResultsActivity.class);
			intent.putExtra("tid", tid);
			startActivity(intent);
			break;
		case R.id.ideas_world_zhiku_details_heritage:// 创意世界优秀遗产
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					WorldHeritageActivity.class);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout1_img6:// 更多1
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					IdeasZhiKuMan.class);
			intent.putExtra("zhikuid", tid);
			startActivity(intent);
			break;
		case R.id.ideas_world_zhiku_details_shezhi:// 设置
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					IdeasWorldZhiKuDetailsSheZhi.class);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img1:// 其他人的信息
			uid = listimg1.get(0).get("uid").toString();			
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img2:// 其他人的信息
			uid = listimg1.get(1).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img3:// 其他人的信息
			uid = listimg1.get(2).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img4:// 其他人的信息
			uid = listimg1.get(3).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout_img5:// 其他人的信息
			uid = listimg1.get(4).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img1:// 其他人的信息
			uid = listimg2.get(0).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img2:// 其他人的信息
			uid = listimg2.get(1).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img3:// 其他人的信息
			uid = listimg2.get(2).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img4:// 其他人的信息
			uid = listimg2.get(3).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout2_img5:// 其他人的信息
			uid = listimg2.get(4).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img1:// 其他人的信息
			uid = listimg3.get(0).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img2:// 其他人的信息
			uid = listimg3.get(1).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img3:// 其他人的信息
			uid = listimg3.get(2).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img4:// 其他人的信息
			uid = listimg3.get(3).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.city_details_linearlayout3_img5:// 其他人的信息
			uid = listimg3.get(4).get("uid").toString();
			intent = new Intent(IdeasWorldZhiKuDetails.this,
					OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.ideas_world_zhiku_details_dating:// 大厅弹窗
			if (null != popupwindow) {
				popupwindow.dismiss();
				return;
			} else {
				initPopuptWindow();
				popupwindow.showAsDropDown(zhuangtai, 0, 6);
			}
			break;
		case R.id.ideas_world_zhiku_details_apply://申请加入智库
			addzhiku();
			break;
		default:
			break;
		}
	}
	private void addzhiku() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("zid", tid);
		httpUtils.send(HttpMethod.POST, MyUrl.Stringzhikurenzhen, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						Toast.makeText(getApplicationContext(), "同级了,不能申请", Toast.LENGTH_SHORT).show();
					}else if (num.equals("2")) {
						shenqing();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void shenqing() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.Stringjumpzhikurenzhen, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				try {
					JSONObject object = new JSONObject(list);
					int num = Integer.parseInt(object.getString("num")
							.toString());
					switch (num) {
					case 1:
						Toast.makeText(getApplicationContext(),
								"认证条件不满足", Toast.LENGTH_SHORT).show();
						break;
					case 3:
						Toast.makeText(getApplicationContext(),
								"已认证", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						String regions = object.getString("regions").toString();
						String home = object.getString("home").toString();
						String als = object.getString("als").toString();
						Intent intent = new Intent(
								getApplicationContext(),
								ThinkTankCertificationDetailsActivity.class);
						intent.putExtra("regions", regions);
						intent.putExtra("home", home);
						intent.putExtra("als", als);
						startActivity(intent);
						finish();
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void popview(JSONObject object) {
		popview = new ArrayList<HashMap<String,Object>>();
		try {
			String zkzblist = object.getString("zkzblist").toString();
			JSONArray array = new JSONArray(zkzblist);
			System.out.println(array+">>>>>>>>>>>>");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("library_group_id", object2.getString("library_group_id").toString());
				map.put("library_group_uaid", object2.getString("library_group_uaid").toString());
				map.put("library_group_name", object2.getString("library_group_name").toString());
				map.put("library_members_liid", object2.getString("library_members_liid").toString());
				map.put("library_members_hx", object2.getString("library_members_hx").toString());
				popview.add(map);
				
				Button button2 = new Button(getApplicationContext());
				int px2 = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
								.getDisplayMetrics());
				button2.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, px2));
				button2.setBackgroundResource(R.color.greyxian);
				Button button = new Button(getApplicationContext());
				button.setPadding(0, 0, 0, 0);
				int px = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
								.getDisplayMetrics());
				button.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, px));
				button.setTextColor(getResources().getColorStateList(
						R.color.greybig));
				button.setBackgroundResource(R.color.white);
				button.setText(popview.get(i).get("library_group_name").toString());
				button.setTextSize(16);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						intent = new Intent(IdeasWorldZhiKuDetails.this,
								ZhiKuManageActivity.class);
						intent.putExtra("tid", tid);
						startActivity(intent);
//						popupwindow.dismiss();
					}
				});
				zhikuguanli.addView(button);
				zhikuguanli.addView(button2);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// // 获取自定义布局文件pop.xml的视图
//		View customView = getLayoutInflater().inflate(R.layout.popuptwindow,
//				null, false);
		popupwindow = new PopupWindow(zhikuguanli, 200, LayoutParams.WRAP_CONTENT);
		popupwindow.showAsDropDown(zhuangtai, 0, 8);
		// 使其聚集
		popupwindow.setFocusable(true);
		// 设置允许在外点击消失
		popupwindow.setOutsideTouchable(true);
		// 刷新状态（必须刷新否则无效）
		popupwindow.update();
		zhikuguanli.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
//		Button button = (Button) zhikuguanli.findViewById(R.id.popuptwindow_button);
//		button.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				intent = new Intent(IdeasWorldZhiKuDetails.this,
//						ZhiKuManageActivity.class);
//				intent.putExtra("tid", tid);
//				startActivity(intent);
////				popupwindow.dismiss();
//			}
//		});
		// 创建PopupWindow实例,200,150分别是宽度和高度
//		popupwindow = new PopupWindow(customView, 200,
//				LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		// popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
//		customView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (popupwindow != null && popupwindow.isShowing()) {
//					popupwindow.dismiss();
//					popupwindow = null;
//				}
//
//				return false;
//			}
//		});
//		Button button1 = (Button) customView
//				.findViewById(R.id.popuptwindow_button);
//		button1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				intent = new Intent(IdeasWorldZhiKuDetails.this,
//						ZhiKuManageActivity.class);
//				startActivity(intent);
//			}
//		});
	}
}
