package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.lsfb.cysj.SearchActivity.ViewHolder;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreSearchActivity extends FragmentActivity implements
		OnClickListener, IXListViewListener {
	@ViewInject(R.id.more_search_list)
	private XListView list;
	@ViewInject(R.id.more_search_title)
	private TextView title;
	@ViewInject(R.id.more_search_back)
	private LinearLayout back;
	Intent intent;
	BaseAdapter adapter;
	Dialog jiazaidialog;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	String listmsg = null;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	int count = 0;
	String URL = null;
	String name = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_search);
		ViewUtils.inject(this);
		init();
		listadapter();
		data();
	}

	private void listadapter() {
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.zhikuzhuanjia_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.zhikuzhuanjia_img);
					holder.name = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_name);
					holder.time = (TextView) view
							.findViewById(R.id.zhikuzhuanjia_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				String img = listmap.get(position).get("image").toString();
				if (img.equals("0")) {

				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, img);
				}
				holder.name.setText(listmap.get(position).get("title")
						.toString());
				holder.time.setText(listmap.get(position).get("time")
						.toString());
				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		list.setAdapter(adapter);
	}

	private void data() {
		try {
			JSONArray array = new JSONArray(listmsg);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("id", object.getString("id").toString());
				map.put("title", object.getString("title").toString());
				map.put("time", object.getString("time").toString());
				map.put("image", object.getString("image").toString());
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		back.setOnClickListener(this);
		intent = getIntent();
		int i = intent.getExtras().getInt("i");
		name = intent.getExtras().getString("name");
		switch (i) {
		case 1:
			title.setText("新闻");
			listmsg = intent.getExtras().getString("msg").toString();
			URL = MyUrl.newspage;
			break;
		case 2:
			title.setText("智库专家");
			listmsg = intent.getExtras().getString("msg").toString();
			URL = MyUrl.zhipage;
			break;
		case 3:
			title.setText("创意世界");
			listmsg = intent.getExtras().getString("msg").toString();
			URL = MyUrl.chuangpage;
			break;
		case 4:
			title.setText("创友圈");
			listmsg = intent.getExtras().getString("msg").toString();
			URL = MyUrl.youpage;
			break;
		case 5:
			title.setText("热门大赛");
			listmsg = intent.getExtras().getString("msg").toString();
			URL = MyUrl.repage;
			break;
		case 6:
			title.setText("线下大赛");
			listmsg = intent.getExtras().getString("msg").toString();
			URL = MyUrl.xiapage;
			break;
		default:
			break;
		}
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
		case R.id.more_search_back:
			finish();
			break;

		default:
			break;
		}
	}
	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime("刚刚");
	}
	@Override
	public void onRefresh() {
		count = 0;
		listmap = new ArrayList<HashMap<String, Object>>();
		data();
		adapter.notifyDataSetChanged();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		LoadMore();
		onLoad();
	}

	private void LoadMore() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("name", name);
		params.addBodyParameter("page", ++ count +"");
		httpUtils.send(HttpMethod.POST, URL, params, new RequestCallBack<String>() {

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
						JSONObject object2 = new JSONObject(list);
						String num = object2.getString("num");
						if (num.equals("1")) {
							
						}else {
							String numlist = object2.getString("numlist").toString();
								JSONArray array = new JSONArray(numlist);
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = (JSONObject) array.get(i);
									map = new HashMap<String, Object>();
									map.put("id", object.getString("id").toString());
									map.put("title", object.getString("title").toString());
									map.put("time", object.getString("time").toString());
									map.put("image", object.getString("image").toString());
									listmap.add(map);
								}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				
			}
		});
}
}
