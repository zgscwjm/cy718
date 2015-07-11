package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.BestResultsActivity;
import com.lsfb.cysj.HomeActivity;
import com.lsfb.cysj.HotZhiKu;
import com.lsfb.cysj.IdeasWorldZhiKuDetails;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.OtherDetailsActivity;
import com.lsfb.cysj.WorldHeritageActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.HorizontalListView;
import com.lsfb.cysj.view.ResDialog;

public class ZhikuFragment extends Fragment implements OnClickListener {
	private View rootView;
	Intent intent;
	BaseAdapter baseAdapter;
	private static String[] nums = new String[] { "255", "266", "277", "299",
			"558", "444", "555" };
	private static String[] citys = new String[] { "成都智库", "北京智库", "上海智库",
			"绵阳智库", "广州智库", "深圳智库", "内蒙古智库" };
	/**
	 * img 创意世界智库
	 */
	private ImageView img;
	/**
	 * linearLayout1_img1 图片1
	 */
	private ImageView linearLayout1_img1;
	/**
	 * linearLayout1_img2 图片2
	 */
	private ImageView linearLayout1_img2;
	/**
	 * linearLayout1_img3 图片3
	 */
	private ImageView linearLayout1_img3;
	/**
	 * linearLayout2_img1 图片2-1
	 */
	private ImageView linearLayout2_img1;
	/**
	 * linearLayout2_img2 图片2-2
	 */
	private ImageView linearLayout2_img2;
	/**
	 * linearLayout1_img3 图片2-3
	 */
	private ImageView linearLayout2_img3;
	/**
	 * linearLayout3_img1 图片3-1
	 */
	private ImageView linearLayout3_img1;
	/**
	 * linearLayout3_img2 ר图片3-2
	 */
	private ImageView linearLayout3_img2;
	/**
	 * linearLayout3_img3 图片3-3
	 */
	private ImageView linearLayout3_img3;
	/**
	 * ideas_result 创意世界优秀结果
	 */
	private Button ideas_result;
	/**
	 * ideas_heritage 创意世界优秀遗产
	 */
	private Button ideas_heritage;
	/**
	 * horizontal_listview 横着的list
	 */
	private HorizontalListView horizontal_listview;
	/**
	 * ideas_more 更多
	 */
	private RelativeLayout ideas_more;

	/**
	 * world_zhiku 创意世界智库
	 */
	private TextView world_zhiku;
	/**
	 * world_library 创意世界智库
	 */
	private LinearLayout world_library;
	/**
	 * my_zhiku 我的智库
	 */
	private TextView my_zhiku;
	/**
	 * my_library 我的智库
	 */
	private LinearLayout my_library;
	/**
	 * zhiku 智库
	 */
	private View zhiku;
	/**
	 * my_zhiku_content 我的智库
	 */
	private View my_zhiku_content;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	Dialog jiazaidialog;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	ArrayList<HashMap<String, Object>> listmaps;
	ArrayList<HashMap<String, Object>> listimg1;
	ArrayList<HashMap<String, Object>> listimg2;
	ArrayList<HashMap<String, Object>> listimg3;
	String id;//智库id
	String image;
	private TextView horizontal_listview_text;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.ideas_world_zhiku, container,
					false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();
//		data();
		return rootView;

	}

	public void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.index, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getActivity(),
						error.getExceptionCode() + ":" + msg,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				System.out.println(list+"????");
				try {
					JSONObject object2 = new JSONObject(list);
					String sjzhi = object2.getString("sjzhi").toString();
					JSONObject object = new JSONObject(sjzhi);
					image = object.getString("image").toString();
					if (image.equals("")|| image == null || image.equals("null")) {
						
					}else {
						bitmapUtils = new BitmapUtils(getActivity());
						bitmapUtils.display(img, ImageAddress.Stringhead+image);
					}
					id = object.getString("id").toString();//智库id
					String zhuxi = object.getString("zhuxi").toString();
					if (zhuxi.equals("0")) {
						
					}else if (zhuxi.equals("1")) {
						String zxlist = object.getString("zxlist").toString();
						zhuxiimg(zxlist);
					}
					String lsh = object.getString("lsh").toString();
					if (lsh.equals("0")) {
						
					}else if (lsh.equals("1")) {
						String lshlist = object.getString("lshlist").toString();
						lishihuiimg(lshlist);
					}
					String gwt = object.getString("gwt").toString();
					if (gwt.equals("0")) {
						
					}else if (gwt.equals("1")) {
						String gwtlist = object.getString("gwtlist").toString();
						zhuanjiaimg(gwtlist);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	protected void zhuanjiaimg(String gwtlist) {
		listmap = new ArrayList<HashMap<String,Object>>();
		try {
			JSONArray array = new JSONArray(gwtlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object.getString("uid").toString());
				map.put("image", object.getString("image").toString());
				listmap.add(map);
			}
			listimg3 = listmap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (listmap.size() == 1) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout3_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout3_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
		}else if (listmap.size() == 2) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout3_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout3_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
			linearLayout3_img2.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout3_img2, ImageAddress.Stringhead+listmap.get(1).get("image").toString());
		}else if (listmap.size() == 3) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout3_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout3_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
			linearLayout3_img2.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout3_img2, ImageAddress.Stringhead+listmap.get(1).get("image").toString());
			linearLayout3_img3.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout3_img3, ImageAddress.Stringhead+listmap.get(2).get("image").toString());
		}
	}

	protected void lishihuiimg(String lshlist) {
		listmap = new ArrayList<HashMap<String,Object>>();
		try {
			JSONArray array = new JSONArray(lshlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object.getString("uid").toString());
				map.put("image", object.getString("image").toString());
				listmap.add(map);
			}
			listimg2 = listmap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (listmap.size() == 1) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout2_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout2_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
		}else if (listmap.size() == 2) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout2_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout2_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
			linearLayout2_img2.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout2_img2, ImageAddress.Stringhead+listmap.get(1).get("image").toString());
		}else if (listmap.size() == 3) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout2_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout2_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
			linearLayout2_img2.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout2_img2, ImageAddress.Stringhead+listmap.get(1).get("image").toString());
			linearLayout2_img3.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout2_img3, ImageAddress.Stringhead+listmap.get(2).get("image").toString());
		}
	}

	protected void zhuxiimg(String zxlist) {
		listmap = new ArrayList<HashMap<String,Object>>();
		try {
			JSONArray array = new JSONArray(zxlist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object.getString("uid").toString());
				map.put("image", object.getString("image").toString());
				listmap.add(map);
			}
			listimg1 = listmap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (listmap.size() == 1) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout1_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout1_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
		}else if (listmap.size() == 2) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout1_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout1_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
			linearLayout1_img2.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout1_img2, ImageAddress.Stringhead+listmap.get(1).get("image").toString());
		}else if (listmap.size() == 3) {
			bitmapUtils = new BitmapUtils(getActivity());
			linearLayout1_img1.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout1_img1, ImageAddress.Stringhead+listmap.get(0).get("image").toString());
			linearLayout1_img2.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout1_img2, ImageAddress.Stringhead+listmap.get(1).get("image").toString());
			linearLayout1_img3.setVisibility(View.VISIBLE);
			bitmapUtils.display(linearLayout1_img3, ImageAddress.Stringhead+listmap.get(2).get("image").toString());
		}
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(getActivity(), R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	public void hengdata(){
//		showdialogup();
		listmaps = new ArrayList<HashMap<String,Object>>();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.myidea, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
//				jiazaidialog.dismiss();
				Toast.makeText(getActivity(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
//				jiazaidialog.dismiss();
				String lists = responseInfo.result;
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						horizontal_listview_text.setVisibility(View.VISIBLE);
					}else if (num.equals("2")) {
						String list = object.getString("list").toString();
						JSONArray array = new JSONArray(list);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object2 = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("id", object2.getString("id").toString());
							map.put("name", object2.getString("name").toString());
							map.put("image", object2.getString("image").toString());
							map.put("number", object2.getString("number").toString());
							listmaps.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	};
	private void date_horizontal() {
		baseAdapter = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(getActivity()).inflate(
						R.layout.ideas_horizontal, null);
				ImageView imageView = (ImageView) view
						.findViewById(R.id.my_library_img);
				TextView city = (TextView) view
						.findViewById(R.id.my_library_city);
				TextView num = (TextView) view
						.findViewById(R.id.my_library_num);
				String image = listmaps.get(position).get("image").toString();
				if (image.equals("0")) {
					imageView.setBackground(getActivity().getResources()
							.getDrawable(R.drawable.logo));
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(imageView, ImageAddress.think+image);
				}
				city.setText(listmaps.get(position).get("name").toString());
				num.setText(listmaps.get(position).get("number").toString());
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
				return listmaps.size();
			}
		};
		horizontal_listview.setAdapter(baseAdapter);
	}

	private void init() {
		img = (ImageView) rootView.findViewById(R.id.img);
		img.setOnClickListener(this);
		linearLayout1_img1 = (ImageView) rootView
				.findViewById(R.id.linearLayout1_img1);
		linearLayout1_img1.setOnClickListener(this);
		linearLayout1_img2 = (ImageView) rootView
				.findViewById(R.id.linearLayout1_img2);
		linearLayout1_img2.setOnClickListener(this);
		linearLayout1_img3 = (ImageView) rootView
				.findViewById(R.id.linearLayout1_img3);
		linearLayout1_img3.setOnClickListener(this);
		linearLayout2_img1 = (ImageView) rootView
				.findViewById(R.id.linearLayout2_img1);
		linearLayout2_img1.setOnClickListener(this);
		linearLayout2_img2 = (ImageView) rootView
				.findViewById(R.id.linearLayout2_img2);
		linearLayout2_img2.setOnClickListener(this);
		linearLayout2_img3 = (ImageView) rootView
				.findViewById(R.id.linearLayout2_img3);
		linearLayout2_img3.setOnClickListener(this);
		linearLayout3_img1 = (ImageView) rootView
				.findViewById(R.id.linearLayout3_img1);
		linearLayout3_img1.setOnClickListener(this);
		linearLayout3_img2 = (ImageView) rootView
				.findViewById(R.id.linearLayout3_img2);
		linearLayout3_img2.setOnClickListener(this);
		linearLayout3_img3 = (ImageView) rootView
				.findViewById(R.id.linearLayout3_img3);
		linearLayout3_img3.setOnClickListener(this);
		ideas_result = (Button) rootView.findViewById(R.id.ideas_result);
		ideas_result.setOnClickListener(this);
		ideas_heritage = (Button) rootView.findViewById(R.id.ideas_heritage);
		ideas_heritage.setOnClickListener(this);
		world_zhiku = (TextView) rootView.findViewById(R.id.world_zhiku);
		world_library = (LinearLayout) rootView
				.findViewById(R.id.world_library);
		world_library.setOnClickListener(this);
		my_zhiku = (TextView) rootView.findViewById(R.id.my_zhiku);
		my_library = (LinearLayout) rootView.findViewById(R.id.my_library);
		my_library.setOnClickListener(this);
		ideas_more = (RelativeLayout) rootView.findViewById(R.id.ideas_more);
		ideas_more.setOnClickListener(this);
		zhiku = rootView.findViewById(R.id.zhiku);
		my_zhiku_content = rootView.findViewById(R.id.my_zhiku_content);
		horizontal_listview = (HorizontalListView) rootView
				.findViewById(R.id.horizontal_listview);
		horizontal_listview_text = (TextView) rootView.findViewById(R.id.horizontal_listview_text);
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
		listimg1 = new ArrayList<HashMap<String,Object>>();
		listimg2 = new ArrayList<HashMap<String,Object>>();
		listimg3 = new ArrayList<HashMap<String,Object>>();
	}

	@Override
	public void onClick(View v) {
		String uid = null;
		switch (v.getId()) {
		case R.id.img:// 创意世界智库
			if (IsTrue.userId == 0) {
				return;
			}else {
				intent = new Intent(getActivity(), IdeasWorldZhiKuDetails.class);
				intent.putExtra("zhikuid", id);
				startActivity(intent);
			}
			break;
		case R.id.my_library:// 我的智库
			if (IsTrue.userId == 0) {
				return;
			}else {
			zhiku.setVisibility(View.GONE);
			my_zhiku_content.setVisibility(View.VISIBLE);
			my_zhiku.setTextColor(getActivity().getResources().getColor(
					R.color.blueMain));
			Drawable drawable = getResources().getDrawable(
					R.drawable.indexthisfooter);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			my_zhiku.setCompoundDrawables(null, null, null, drawable);
			world_zhiku.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.zhiku));
			world_zhiku.setCompoundDrawables(null, null, null, null);
			date_horizontal();
			}
			break;
		case R.id.world_library:// 创意世界智库
			zhiku.setVisibility(View.VISIBLE);
			my_zhiku_content.setVisibility(View.GONE);
			world_zhiku.setTextColor(getActivity().getResources().getColor(
					R.color.blueMain));
			Drawable drawable1 = getResources().getDrawable(
					R.drawable.indexthisfooter);
			drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
					drawable1.getMinimumHeight());
			world_zhiku.setCompoundDrawables(null, null, null, drawable1);
			my_zhiku.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.zhiku));
			my_zhiku.setCompoundDrawables(null, null, null, null);
			break;
		case R.id.ideas_more:// 更多
			intent = new Intent(getActivity(), HotZhiKu.class);
			startActivity(intent);
			break;
		case R.id.ideas_result:// 创意世界优秀结果
			if (IsTrue.userId == 0) {
				
			}else {
			intent = new Intent(getActivity(), BestResultsActivity.class);
			intent.putExtra("tid", id);
			startActivity(intent);
			}
			break;
		case R.id.ideas_heritage:// 创意世界优秀遗产
				intent = new Intent(getActivity(), WorldHeritageActivity.class);
				startActivity(intent);
			break;
		case R.id.linearLayout1_img1:// 人家的详情
			uid = listimg1.get(0).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout1_img2:// 人家的详情
			uid = listimg1.get(1).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout1_img3:// 人家的详情
			uid = listimg1.get(2).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout2_img1:// 人家的详情
			uid = listimg2.get(0).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout2_img2:// 人家的详情
			uid = listimg2.get(1).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout2_img3:// 人家的详情
			uid = listimg2.get(2).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout3_img1:// 人家的详情
			uid = listimg3.get(0).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout3_img2:// 人家的详情
			uid = listimg3.get(1).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		case R.id.linearLayout3_img3:// 人家的详情
			uid = listimg3.get(2).get("uid").toString();
			intent = new Intent(getActivity(), OtherDetailsActivity.class);
			intent.putExtra("id", uid);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
