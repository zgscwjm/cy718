package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import com.lsfb.cysj.Dialog.AddDialog;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ZhiKuManageActivity extends Activity implements OnClickListener,IXListViewListener{
	private static String[] nums = new String[] { "222", "223", "224", "225",
		"226", "227", "228" };
	@ViewInject(R.id.zhiku_manage_back)
	private LinearLayout back;
	@ViewInject(R.id.zhiku_manage_add)
	private LinearLayout add;
	@ViewInject(R.id.zhiku_manage_list)
	private XListView list;
	@ViewInject(R.id.zhiku_manage_text)
	private TextView zhiku_manage_text;
	String tid;//智库id
	Intent intent;
	BaseAdapter adapter;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	HttpUtils httpUtils;
	RequestParams params;
	Dialog jiazaidialog;
	int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhiku_manage);
		ViewUtils.inject(this);
		init();
		data();
		initdata();
	}

	private void initdata() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("tid", tid);
		httpUtils.send(HttpMethod.POST, MyUrl.thzulist, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String lists = responseInfo.result;
				System.out.println(lists);
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						zhiku_manage_text.setVisibility(View.VISIBLE);
					}else if (num.equals("2")) {
						zhiku_manage_text.setVisibility(View.GONE);
						String 	list = object.getString("list").toString();
						JSONArray array = new JSONArray(list);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object2 = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("id", object2.getString("id").toString());
							map.put("name", object2.getString("name").toString());
							map.put("memberid", object2.getString("memberid").toString());
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
	protected void showdialogup() {
		jiazaidialog = new ResDialog(ZhiKuManageActivity.this,
				R.style.MyDialog, "正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void data() {
		adapter = new BaseAdapter() {
			
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.zhiku_manage_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view.findViewById(R.id.zhiku_manage_list_item_img);
					holder.num = (TextView) view.findViewById(R.id.zhiku_manage_list_item_text);
					holder.del = (TextView) view.findViewById(R.id.zhiku_manage_list_item_del);
					view.setTag(holder);
				}else {
					holder = (ViewHolder) view.getTag();
				}
//				BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
//				bitmapUtils.display(holder.img, listmap.get(position).get("").toString());
				holder.num.setText(listmap.get(position).get("name").toString());
				String del = listmap.get(position).get("memberid").toString();
				if (del.equals("1")) {
					holder.del.setVisibility(View.GONE);
				}else if(del.equals("2")){
					holder.del.setVisibility(View.VISIBLE);
					holder.del.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							del(listmap.get(position).get("name").toString());
						}
					});
				}
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
	private void del(String name) {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("sid", tid);
		params.addBodyParameter("name", name);
		httpUtils.send(HttpMethod.POST, MyUrl.thzudel, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String lists = responseInfo.result;
				System.out.println(lists);
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
					}else if (num.equals("2")) {
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	static class ViewHolder{
		ImageView img;
		TextView num;
		TextView del;
	}
	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		intent = getIntent();
		tid = intent.getExtras().getString("tid");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zhiku_manage_back:
			finish();
			break;
		case R.id.zhiku_manage_add:
			showdialog();
			break;
		default:
			break;
		}
	}
	private void showdialog() {
		AddDialog addDialog = new AddDialog(getApplicationContext(), R.style.MyDialog, new AddDialog.PriorityListener() {
			
			@Override
			public void refreshPriorityUI(String string) {
				initdata();
			}
		}, tid);
	}

	private void onLoad() {
//		list.stopRefresh();
//		list.stopLoadMore();
//		list.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
//		count = 0;
//		listmap = new ArrayList<HashMap<String, Object>>();
//		initdata();
//		onLoad();
	}

	@Override
	public void onLoadMore() {
//		LoadMoremsg();
//		onLoad();
	}

	private void LoadMoremsg() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("tid", tid);
		params.addBodyParameter("page", ++count +"");
		httpUtils.send(HttpMethod.POST, MyUrl.thzulist, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getApplicationContext(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String lists = responseInfo.result;
				System.out.println(lists);
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
					}else if (num.equals("2")) {
						String 	list = object.getString("list").toString();
						JSONArray array = new JSONArray(list);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object2 = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("id", object2.getString("id").toString());
							map.put("name", object2.getString("name").toString());
							map.put("memberid", object2.getString("memberid").toString());
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

}
