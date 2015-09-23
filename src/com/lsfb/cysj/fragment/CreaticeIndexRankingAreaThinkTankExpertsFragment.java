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
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.OtherDetailsActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.TicketNoUseFragment.ViewHolder;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;
import com.umeng.socialize.utils.Log;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreaticeIndexRankingAreaThinkTankExpertsFragment extends Fragment implements IXListViewListener{
	private View rootView;
	XListView lv_CreativeIndexRankingArea_experts;
	BaseAdapter baseAdapter;
	String cid;
	String qid;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	Dialog jiazaidialog;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	int count = 0;
	private TextView text;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(
					R.layout.creaticeindexrankingarea_thinktankexperts,
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
//		data();
		return rootView;
	}
	private void adapterlist() {
		baseAdapter = new BaseAdapter() {

			@Override
			public View getView(final int position, View v, ViewGroup arg2) {
				ViewHolder holder = null;
				if (v == null) {
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater
							.from(getActivity()).inflate(
									R.layout.listview_creativeindexrangkingarea_expests, null);
					holder.imageview = (ImageView) v.findViewById(R.id.iv_listview_expests);
					holder.tvHead = (TextView) v.findViewById(R.id.iv_listview_expests_name);
					holder.tvContext = (TextView) v.findViewById(R.id.iv_listview_expests_text);
					holder.imgbtn = (ImageButton) v.findViewById(R.id.iv_listview_expests_guanzhu);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				bitmapUtils = new BitmapUtils(getActivity());
				bitmapUtils.display(holder.imageview, ImageAddress.Stringhead+listmap.get(position).get("image").toString());
				holder.tvHead.setText(listmap.get(position).get("name").toString());
				holder.tvContext.setText(listmap.get(position).get("bewrite").toString());
				boolean contains = listmap.get(position).containsKey("ubs");
				System.out.println(contains+"KKKKKKKKKKKKKKKKKK");
				if (contains) {
					String ubs = listmap.get(position).get("ubs").toString();
					Log.d("zgscwjm", ubs);	
					if (ubs.equals("2")) {
							//holder.imgbtn.setVisibility(View.VISIBLE);
						holder.imgbtn.setBackgroundResource(R.drawable.yiguanzhu);
						}else {
							holder.imgbtn.setBackgroundResource(R.drawable.guanzhu001);
							//holder.imgbtn.setVisibility(View.GONE);
						}
				}
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Toast.makeText(CreaticeIndexRankingAreaThinkTankExpertsFragment.this.getActivity().getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
						
							
					
							// TODO Auto-generated method stub
							if(listmap.get(position).get("uid").toString().equals(IsTrue.userId+"")){
								Intent intent=new Intent(getActivity(),MyDetailsActivity.class);
								startActivity(intent);
							}else{
								Intent intent=new Intent(getActivity(),OtherDetailsActivity.class);
								intent.putExtra("id", listmap.get(position).get("uid").toString());
								startActivity(intent);
							}
						
					}
				});
				
				return v;
			}

			@Override
			public long getItemId(int i) {
				return 0;
			}

			@Override
			public Object getItem(int i) {
				return null;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
		lv_CreativeIndexRankingArea_experts.setAdapter(baseAdapter);	
		
		
		
	}
	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("cid", cid);
		params.addBodyParameter("qid", qid);
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.zhuanjia, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getActivity(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String lists = responseInfo.result;
				Log.d("zgscwjm",lists);
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						text.setVisibility(View.VISIBLE);
					}else {
						text.setVisibility(View.GONE);
						String list = object.getString("list").toString();
						xianshi(list);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	protected void xianshi(String list) {
		listmap = new ArrayList<HashMap<String,Object>>();
		try {
			JSONArray array = new JSONArray(list);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
//				map.put("memid", object.getString("memid").toString());
				map.put("uid", object.getString("uid").toString());
				map.put("image", object.getString("image").toString());
				map.put("name", object.getString("name").toString());
				map.put("bewrite", object.getString("bewrite").toString());
				boolean has = object.has("ubs");
//				boolean containsKey = map.containsKey("ubs");
				if (has) {
					map.put("ubs", object.getString("ubs").toString());
				}else {
					map.put("ubs", "1");
				}
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(listmap+"SSSSSSS");
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
		init();
//		count = 0;
		data();
//		chushihua();
	}
	public class ViewHolder {
		private ImageView imageview;
		private TextView tvHead;
		private TextView tvContext;
		private ImageButton imgbtn;
	}
	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
		lv_CreativeIndexRankingArea_experts = (XListView) rootView
				.findViewById(R.id.lv_CreativeIndexRankingArea_experts);
		text = (TextView) rootView.findViewById(R.id.lv_CreativeIndexRankingArea_experts_text);
	}
	private void onLoad() {
		lv_CreativeIndexRankingArea_experts.stopRefresh();
		lv_CreativeIndexRankingArea_experts.stopLoadMore();
		lv_CreativeIndexRankingArea_experts.setRefreshTime("刚刚");
	}
	@Override
	public void onRefresh() {
		count = 0;
		data();
		baseAdapter.notifyDataSetChanged();
		onLoad();
	}
	@Override
	public void onLoadMore() {
		loadRemnantListItem();
		baseAdapter.notifyDataSetChanged();
		onLoad();
	}
	private void loadRemnantListItem() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("cid", cid);
		params.addBodyParameter("qid", qid);
		params.addBodyParameter("uid", IsTrue.userId+"");
		params.addBodyParameter("page", ++count+"");
		httpUtils.send(HttpMethod.POST, MyUrl.zhuanjiapage, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getActivity(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String lists = responseInfo.result;
				try {
					JSONObject object = new JSONObject(lists);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
					}else {
						text.setVisibility(View.GONE);
						String list = object.getString("list").toString();
						xianshi2(list);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	protected void xianshi2(String list) {
		try {
			JSONArray array = new JSONArray(list);
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
//				map.put("memid", object.getString("memid").toString());
				map.put("uid", object.getString("uid").toString());
				map.put("image", object.getString("image").toString());
				map.put("name", object.getString("name").toString());
				map.put("bewrite", object.getString("bewrite").toString());
				boolean has = object.has("ubs");
				if (has) {
					map.put("ubs", object.getString("ubs").toString());
				}else {
					map.put("ubs", "1");
				}
				listmap.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		baseAdapter.notifyDataSetChanged();
	}
}
