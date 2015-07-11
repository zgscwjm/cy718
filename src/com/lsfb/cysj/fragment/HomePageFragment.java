package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.NoValidationActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;

public class HomePageFragment extends Fragment {
	Button btn_homepage_enterprise_NoValidation;// 政企验证按钮
	Button btn_homepage_school_Validation;// 学校验证按钮
	private View rootView;
	private LinearLayout llHomepage;
	TextView tv_homepage_birthady;// 生日
	TextView tv_homepage_sex;// 性别
	TextView tv_country;// 中国
	TextView tv_provinces;// 四川
	TextView tv_city;// 成都
	TextView tv_area;// 青羊区
	TextView tv_phones;// 手机号码
	TextView tv_school;// 学校
	TextView tv_erprise;// 政企
	TextView tv_signature;// 个性签名
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_homepage, container,
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
		jointhinktank();// 加入的智库
		btn_homepage_enterprise_NoValidation
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 跳转验证界面
						if (btn_homepage_enterprise_NoValidation.getText()
								.toString().equals("未验证")) {
							Intent intent = new Intent(getActivity(),
									NoValidationActivity.class);
							intent.putExtra("check", "2");
							startActivity(intent);
						}

					}
				});
		btn_homepage_school_Validation
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// 跳转验证界面
						if (btn_homepage_school_Validation.getText().toString()
								.equals("未验证")) {
							Intent intent = new Intent(getActivity(),
									NoValidationActivity.class);
							intent.putExtra("check", "1");
							startActivity(intent);
						}
					}
				});
		setData();
		zhiku();
		return rootView;

	}

	private void zhiku() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.myidea, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(getActivity(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String lists = responseInfo.result;
				System.out.println(lists+"MMMMM");
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
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
							listmap.add(map);
						}
						System.out.println(listmap+"JJJJJJJJJJJJJJJJJJJ");
						jointhinktank();// 加入的智库
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		setData();
		super.onResume();
	}

	private void setData() {
		// TODO Auto-generated method stub
		if (IsTrue.Stringbirthday.equals("0000-00-00")) {
			tv_homepage_birthady.setText("未设置");// 生日
		} else {
			tv_homepage_birthady.setText(IsTrue.Stringbirthday);// 生日
		}
		tv_homepage_sex.setText(IsTrue.Stringsex);// 性别
		tv_country.setText(IsTrue.Stringhome1);// 中国

		if (IsTrue.Stringhome2.equals("null")) {
			tv_provinces.setText("");// 四川
		} else {
			tv_provinces.setText(IsTrue.Stringhome2);// 四川
		}
		if (IsTrue.Stringhome3.equals("null")) {
			tv_city.setText("");// 四川
		} else {
			tv_city.setText(IsTrue.Stringhome3);
		}

		if (IsTrue.Stringhome4.equals("null")) {
			tv_area.setText("");// 四川
		} else {
			tv_area.setText(IsTrue.Stringhome4);// 青羊区
		}
		tv_phones.setText(IsTrue.Stringphone);// 手机号码
		tv_school.setText(IsTrue.Stringschool);// 学校
		Log.d("学校的名称", IsTrue.Stringschool);
		tv_erprise.setText(IsTrue.Stringgovernment);// 政企
		tv_signature.setText(IsTrue.Stringsignatur);// 个性签名
		if (IsTrue.booleanisschool) {
			btn_homepage_school_Validation.setText("已验证");
			btn_homepage_school_Validation.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greyhomepage));
			btn_homepage_school_Validation
					.setBackgroundResource(R.drawable.shape_validation);
		} else {
			btn_homepage_school_Validation.setText("未验证");
			btn_homepage_school_Validation.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.yellowhomepage));
			btn_homepage_school_Validation
					.setBackgroundResource(R.drawable.shape_yellow);

		}
		if (IsTrue.booleanisgovernment) {
			btn_homepage_enterprise_NoValidation.setText("已验证");
			btn_homepage_enterprise_NoValidation.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greyhomepage));
			btn_homepage_enterprise_NoValidation
					.setBackgroundResource(R.drawable.shape_validation);
		} else {
			btn_homepage_enterprise_NoValidation.setText("未验证");
			btn_homepage_enterprise_NoValidation.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.yellowhomepage));
			btn_homepage_enterprise_NoValidation
					.setBackgroundResource(R.drawable.shape_yellow);

		}
	}

	private void jointhinktank() {
		// TODO Auto-generated method stub

		for (int i = 0; i < listmap.size(); i++) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.jointhinktank, null);
			ImageView iamgeview = (ImageView) view
					.findViewById(R.id.ivjointhink);
			TextView textview = (TextView) view.findViewById(R.id.tvjointhink);
			String image = null;
			switch (listmap.size()) {
			case 1:
				image = listmap.get(0).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(0).get("name").toString());
				break;
			case 2:
				image = listmap.get(0).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(0).get("name").toString());
				image = listmap.get(1).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(1).get("name").toString());
				break;
			case 3:
				image = listmap.get(0).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(0).get("name").toString());
				image = listmap.get(1).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(1).get("name").toString());
				image = listmap.get(2).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(2).get("name").toString());
				break;
			case 4:
				image = listmap.get(0).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(0).get("name").toString());
				image = listmap.get(1).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(1).get("name").toString());
				image = listmap.get(2).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(2).get("name").toString());
				image = listmap.get(3).get("image").toString();
				if (image.equals("0")) {
					iamgeview.setImageResource(R.drawable.logo);
				}else {
					bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(iamgeview, ImageAddress.think+image);
				}
				textview.setText(listmap.get(3).get("name").toString());
				break;
			default:
				break;
			}
//			if (i == 0) {
//				iamgeview.setImageResource(R.drawable.myzkimg);
//				textview.setText("成都智库");
//			}
//			if (i == 1) {
//				iamgeview.setImageResource(R.drawable.xxheader);
//				textview.setText("北京智库");
//			}
			view.setPadding(0, 0, 0, 0);
			llHomepage.addView(view);
		}
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
		// TODO Auto-generated method stub
		llHomepage = (LinearLayout) rootView.findViewById(R.id.llHomepage);
		btn_homepage_enterprise_NoValidation = (Button) rootView
				.findViewById(R.id.btn_homepage_enterprise_Validation);
		btn_homepage_school_Validation = (Button) rootView
				.findViewById(R.id.btn_homepage_school_Validation);
		tv_homepage_birthady = (TextView) rootView
				.findViewById(R.id.tv_homepage_birthady);
		tv_homepage_sex = (TextView) rootView
				.findViewById(R.id.tv_homepage_sex);
		tv_country = (TextView) rootView.findViewById(R.id.tv_country);// 中国
		tv_provinces = (TextView) rootView.findViewById(R.id.tv_provinces);// 四川
		tv_city = (TextView) rootView.findViewById(R.id.tv_city);// 成都
		tv_area = (TextView) rootView.findViewById(R.id.tv_area);// 青羊区
		tv_phones = (TextView) rootView.findViewById(R.id.tv_phones);
		tv_school = (TextView) rootView.findViewById(R.id.tv_school);
		tv_erprise = (TextView) rootView.findViewById(R.id.tv_erprise);
		tv_signature = (TextView) rootView.findViewById(R.id.tv_signature);
	}
}
