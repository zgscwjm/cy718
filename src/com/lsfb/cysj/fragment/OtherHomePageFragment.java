package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;

public class OtherHomePageFragment extends Fragment {

	private View rootView;
	private LinearLayout llHomepage;
	TextView tv_chuangchuangNumOther;// 创创号
	TextView tv_chuangyizhikuNumOther;// 创意世界智库号
	TextView tvchinese_other;// 国际
	TextView tvprovinces_other;// 省份
	TextView tvcity_other;// 市
	TextView tvarea_other;// 县区
	TextView tv_schoolOther;// 学校
	TextView tv_erpriseOther;// 政企
	TextView tv_SignatureOther;// 签名
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
			rootView = inflater.inflate(R.layout.fragment_other_homepage,
					container, false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();
		return rootView;

	}

	public void setDate(JSONObject jsonObject) {
		try {
			tv_chuangchuangNumOther.setText(jsonObject.getString("chuang")
					.toString());
			tv_chuangyizhikuNumOther.setText(jsonObject.getString("znumber")
					.toString());
			String strhome = jsonObject.getString("home").toString();
			System.err.println(strhome);
			String[] strhomes = strhome.split("\\|");
			
			switch (strhomes.length) {
			case 1:
				 tvchinese_other.setText(strhomes[0]);// 国际
				 tvprovinces_other.setText("");// 省份
				 tvcity_other.setText("");// 市
				 tvarea_other.setText("");// 县区
				break;
			case 2:
				 tvchinese_other.setText(strhomes[0]);// 国际
				 tvprovinces_other.setText(strhomes[1]);// 省份
				 tvcity_other.setText("");// 市
				 tvarea_other.setText("");// 县区
				break;
			case 3:
				 tvchinese_other.setText(strhomes[0]);// 国际
				 tvprovinces_other.setText(strhomes[1]);// 省份
				 tvcity_other.setText(strhomes[2]);// 市
				 tvarea_other.setText("");// 县区
				break;
			case 4:
				 tvchinese_other.setText(strhomes[0]);// 国际
				 tvprovinces_other.setText(strhomes[1]);// 省份
				 tvcity_other.setText(strhomes[2]);// 市
				 tvarea_other.setText(strhomes[3]);// 县区
				break;
			default:
				break;
			}
			tv_schoolOther.setText(jsonObject.getString("school").toString());
			tv_erpriseOther.setText(jsonObject.getString("government").toString());
			tv_SignatureOther.setText(jsonObject.getString("qianm").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void zhiku(){
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
	private void jointhinktank() {
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
		llHomepage = (LinearLayout) rootView
				.findViewById(R.id.llHomepage_other);
		tv_chuangchuangNumOther = (TextView) rootView
				.findViewById(R.id.tv_chuangchuangNumOther);
		tv_chuangyizhikuNumOther = (TextView) rootView
				.findViewById(R.id.tv_chuangyizhikuNumOther);
		tvchinese_other = (TextView) rootView
				.findViewById(R.id.tvchinese_other);// 国际
		tvprovinces_other = (TextView) rootView
				.findViewById(R.id.tvprovinces_other);// 省份
		tvcity_other = (TextView) rootView.findViewById(R.id.tvcity_other);// 市
		tvarea_other = (TextView) rootView.findViewById(R.id.tvarea_other);// 县区
		tv_schoolOther = (TextView) rootView.findViewById(R.id.tv_schoolOther);// 学校
		tv_erpriseOther = (TextView) rootView
				.findViewById(R.id.tv_erpriseOther);// 政企
		tv_SignatureOther = (TextView) rootView
				.findViewById(R.id.tv_SignatureOther);// 签名
	}
}
