package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.lsfb.cysj.fragment.TicketAlreadyUseFragment.ViewHolder;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.ScrollLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class CreaticeIndexRankingAreaUserFragment extends Fragment {
	private View rootView;
	GridView gv;// 网格视图
	BaseAdapter baseAdapter;
	String cid;
	String qid;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	Dialog jiazaidialog;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	private static final float APP_PAGE_SIZE = 16.0f;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.creaticeindexrankingarea_user,
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
		adapterlist();
		data();
		return rootView;

	}
	private void adapterlist() {
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(getActivity()).inflate(
							R.layout.listview_creaticeindexrankingarea_user, null);
					holder.imageview = (ImageView) v.findViewById(R.id.iv_createIndexRankingArea);
					holder.btnLook = (Button) v.findViewById(R.id.iv_createIndexRankingArea_btn);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				bitmapUtils = new BitmapUtils(getActivity());
				bitmapUtils.display(holder.imageview, ImageAddress.Stringhead+listmap.get(position).get("image").toString());
				boolean contains = listmap.get(position).containsKey("ubs");
				if (contains) {
					String ubs = listmap.get(position).get("ubs").toString();
						if (ubs.equals("2")) {
							holder.btnLook.setText("    已关注");
						}else {
							holder.btnLook.setText("    关注");
						}
				}
				return v;

			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		gv.setAdapter(baseAdapter);
	}
	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("cid", cid);
		params.addBodyParameter("qid", qid);
		System.out.println(cid + "TTTTTTTTTTTTTTTT" + qid);
		httpUtils.send(HttpMethod.POST, MyUrl.memlist, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String lists= responseInfo.result;
				System.out.println(lists+"oooo");
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						
					}else if (num.equals("2")) {
						String list = object.getString("list").toString();
						jiazai(list);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	protected void jiazai(String list) {
		listmap = new ArrayList<HashMap<String,Object>>();
		try {
			JSONArray array = new JSONArray(list);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("uid", object.getString("uid").toString());
				map.put("image", object.getString("image").toString());
				boolean containsKey = map.containsKey("ubs");
				if (containsKey) {
					map.put("ubs", object.getString("ubs").toString());
				}
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		baseAdapter.notifyDataSetChanged();
	}
	private void showdialogup() {
		jiazaidialog = new ResDialog(getActivity(), R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	public void setData(String cid, String qid) {
		this.cid = cid;
		this.qid = qid;
		System.out.println(cid + "DDDDDDDDDDDDDDDDDDDDDDDDD" + qid);
		init();
//		count = 0;
//		chushihua();
		data();
	}
	public class ViewHolder {
		private ImageView imageview;
		private Button btnLook;

	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
		gv = (GridView) rootView.findViewById(R.id.gv_creaticeindexrankingarea_user);
//		cid = getArguments().getString("cid");
//		qid = getArguments().getString("qid");
	}
}
