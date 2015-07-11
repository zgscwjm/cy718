package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.GameDetailsActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

public class GameEndFragment extends Fragment implements IXListViewListener{
	private View rootView;
	private XListView game_end;
	BaseAdapter adapter;
	Intent intent;
	AsyncHttpClient client;
	RequestParams params;
	List<Map<String, Object>> listmap;
	Dialog jaizaidialog;
	int count = 0;
	static String countriesgame,provincegame,citygame,qugame;
	private static String[] citys = new String[] { "成都智库", "北京智库", "上海智库",
			"绵阳智库", "广州智库", "深圳智库", "内蒙古智库" };
	private TextView text;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.game_end, container, false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();
		initdata();
		return rootView;

	}

	private void initdata() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("state", 2);
		if (IsTrue.Stringidstrarea0 != null) {
			params.put("countries", IsTrue.Stringidstrarea0);
		}
		if (IsTrue.Stringidstrarea1 != null) {
			params.put("province", IsTrue.Stringidstrarea1);
		}
		if (IsTrue.Stringidstrarea2 != null) {
			params.put("city", IsTrue.Stringidstrarea2);
		}
		if (IsTrue.Stringidstrarea3 != null) {
			params.put("qu", IsTrue.Stringidstrarea3);
		}
		System.out.println(IsTrue.Stringidstrarea0+"III"+IsTrue.Stringidstrarea1+"YYY"+IsTrue.Stringidstrarea2+"NNNNN"+IsTrue.Stringidstrarea3);
		client.post(MyUrl.lines, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i==1) {
						text.setVisibility(View.VISIBLE);
					}else if (i==2) {
						text.setVisibility(View.GONE);
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", object.getString("id").toString());
							map.put("title", object.getString("title").toString());
							map.put("image", object.getString("image").toString());
							map.put("time", object.getString("time").toString());
							map.put("address", object.getString("address").toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jaizaidialog.dismiss();
				xianshiinitdata();
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getActivity(), errorResponse+"", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	protected void xianshiinitdata() {
		adapter = new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				ViewHodler hodler = null;
				if (convertView == null) {

					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.game_end_item, null);
					hodler = new ViewHodler();
					hodler.game_end_item_img = (ImageView) convertView
							.findViewById(R.id.game_end_item_img);
					hodler.game_end_item_text1 = (TextView) convertView
							.findViewById(R.id.game_end_item_text1);
					hodler.game_end_item_text2 = (TextView) convertView
							.findViewById(R.id.game_end_item_text2);
					hodler.game_end_item_text3 = (TextView) convertView
							.findViewById(R.id.game_end_item_text3);
					hodler.game_end_item_text4 = (TextView) convertView
							.findViewById(R.id.game_end_item_text4);
					convertView.setTag(hodler);
				} else {
					hodler = (ViewHodler) convertView.getTag();
				}
				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
				bitmapUtils.display(hodler.game_end_item_img, ImageAddress.Stringgame+listmap.get(position).get("image").toString());
//				hodler.game_end_item_img.setBackground(getActivity()
//						.getResources().getDrawable(R.drawable.headimage));
//				hodler.game_end_item_text1.setText(citys[position]);
				hodler.game_end_item_text1.setText(listmap.get(position).get("title").toString());
				hodler.game_end_item_text2.setText("比赛地址:"+listmap.get(position).get("address").toString());
				hodler.game_end_item_text3.setText("比赛时间:"+listmap.get(position).get("time").toString());
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent = new Intent(getActivity(),
								GameDetailsActivity.class);
						intent.putExtra("sid", listmap.get(position).get("id").toString());
						intent.putExtra("title", listmap.get(position).get("title").toString());
						startActivity(intent);
					}
				});
				return convertView;
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
				return listmap.size();
			}
		};
		game_end.setAdapter(adapter);
	}

	public void getaddress(String countries,String province,String city,String qu) {
		countriesgame = countries;
		provincegame = province;
		citygame = city;
		qugame = qu;
		listmap = new ArrayList<Map<String,Object>>();
		initdata();
	}

	private void init() {
		count = 0;
		IsTrue.Stringidstrarea0="";
		IsTrue.Stringidstrarea1="";
		IsTrue.Stringidstrarea2="";
		IsTrue.Stringidstrarea3="";
		listmap = new ArrayList<Map<String,Object>>();
		game_end = (XListView) rootView.findViewById(R.id.game_end);
		text = (TextView) rootView.findViewById(R.id.game_end_text);
		game_end.setPullLoadEnable(true);
		game_end.setXListViewListener(this);
		showdialog();
	}

	private void showdialog() {
		jaizaidialog = new ResDialog(getActivity(), R.style.MyDialog,
				"努力加载中", R.drawable.loads);
		jaizaidialog.show();
		jaizaidialog.setCanceledOnTouchOutside(false);
	}

	static class ViewHodler {
		ImageView game_end_item_img;
		TextView game_end_item_text1;
		TextView game_end_item_text2;
		TextView game_end_item_text3;
		TextView game_end_item_text4;
	}
	private void onLoad() {
		game_end.stopRefresh();
		game_end.stopLoadMore();
		game_end.setRefreshTime("刚刚");
	}
	@Override
	public void onRefresh() {
		count = 0;
		listmap = new ArrayList<Map<String,Object>>();
		initdata();
		adapter.notifyDataSetChanged();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		LoadMoremsg();
		adapter.notifyDataSetChanged();
		onLoad();
	}

	private void LoadMoremsg() {
		client = new AsyncHttpClient();
		params.put("page", ++count + "");
		params.put("state", 2);
		if (IsTrue.Stringidstrarea0 != null) {
			params.put("countries", IsTrue.Stringidstrarea0);
		}
		if (IsTrue.Stringidstrarea1 != null) {
			params.put("province", IsTrue.Stringidstrarea1);
		}
		if (IsTrue.Stringidstrarea2 != null) {
			params.put("city", IsTrue.Stringidstrarea2);
		}
		if (IsTrue.Stringidstrarea3 != null) {
			params.put("qu", IsTrue.Stringidstrarea3);
		}
		System.out.println(IsTrue.Stringidstrarea0+"III"+IsTrue.Stringidstrarea1+"YYY"+IsTrue.Stringidstrarea2+"NNNNN"+IsTrue.Stringidstrarea3);
		client.post(MyUrl.linespage, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response+"TTTTTTTTTTTTTTTTTTTTTTT");
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i==1) {
						Toast.makeText(getActivity(),"亲,已经没有啦", Toast.LENGTH_SHORT).show();
					}else if (i==2) {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", object.getString("id").toString());
							map.put("title", object.getString("title").toString());
							map.put("image", object.getString("image").toString());
							map.put("time", object.getString("time").toString());
							map.put("address", object.getString("address").toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getActivity(), errorResponse+"", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
}
