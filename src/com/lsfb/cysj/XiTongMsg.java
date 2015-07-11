package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class XiTongMsg extends FragmentActivity implements IXListViewListener{
	private XListView list;
	BaseAdapter adapter;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	Dialog jiazaidialog;
	HttpUtils httpUtils;
	RequestParams params;
	private TextView xitongmsg2;
	int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xitongmsg);
		init();
		data();
		initdata();
//		list.setOnScrollListener(new OnScrollListener() {
//			
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				
//			}
//			
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				if (firstVisibleItem == 0) {
//					LoadMoredata();
//				}
//				if (visibleItemCount + firstVisibleItem == totalItemCount) {
//				}
//			}
//		});
	}

	protected void LoadMoredata() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("page", ++count +"");
		httpUtils.send(HttpMethod.POST, MyUrl.messagelistpage, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				System.out.println(list+"GG");
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
					}else if (num.equals("2")) {
						String sysxx = object.getString("sysxx").toString();
						JSONArray array = new JSONArray(sysxx);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object2 = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("id", object2.getString("id").toString());
							map.put("send", object2.getString("send").toString());
							map.put("newmsg", object2.getString("newmsg").toString());
							map.put("time", object2.getString("time").toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
		list = (XListView) findViewById(R.id.xitongmsg);
		list.setPullLoadEnable(true);
		list.setXListViewListener(this);
		xitongmsg2 = (TextView) findViewById(R.id.xitongmsg2);
	}

	private void initdata() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.messagelist, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				System.out.println(list+"DD");
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						xitongmsg2.setVisibility(View.VISIBLE);
					}else if (num.equals("2")) {
						xitongmsg2.setVisibility(View.GONE);
						String sysxx = object.getString("sysxx").toString();
						JSONArray array = new JSONArray(sysxx);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object2 = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("id", object2.getString("id").toString());
							map.put("send", object2.getString("send").toString());
							map.put("newmsg", object2.getString("newmsg").toString());
							map.put("time", object2.getString("time").toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog,
				"正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void data() {
		adapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.xitongmsg_item, null);
					holder = new ViewHolder();
					holder.time = (TextView) view.findViewById(R.id.xitongmsg_item_time);
					holder.text = (TextView) view.findViewById(R.id.xitongmsg_item_text);
					view.setTag(holder);
				}else {
					holder= (ViewHolder) view.getTag();
				}
				holder.time.setText(listmap.get(position).get("time").toString());
				holder.text.setText(listmap.get(position).get("newmsg").toString());
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
				return listmap.size();
			}
		};
		list.setAdapter(adapter);
	}
	static class ViewHolder{
		TextView time,text;
	}
	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime("刚刚");
	}
	@Override
	public void onRefresh() {
		LoadMoredata();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		count = 0;
		listmap = new ArrayList<HashMap<String,Object>>();
		initdata();
		onLoad();
	}
}
