package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.CreativeDetailsActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.fragment.InnovationAwardFragment.ViewHolder;
import com.lsfb.cysj.view.ResDialog;
/**
 * 科学奖
 * @author Administrator
 *
 */
public class ScienceAwardFragment extends Fragment implements OnClickListener {
	private static String[] citys = new String[] { "成都智库", "北京智库", "上海智库",
			"绵阳智库", "广州智库", "深圳智库", "内蒙古智库", "北京智库", "上海智库", "绵阳智库", "广州智库",
			"深圳智库", "内蒙古智库" };
	private View rootView;
	private ListView list;
	BaseAdapter adapter;
	Intent intent;
	AsyncHttpClient client;
	RequestParams params;
	String tid;//智库id
	Dialog jiazaidialog;
	private TextView text;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	String tp;
	
	int piao;	
	ViewHolder viewHolder = null;
	public Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 112:				
				viewHolder.num.setText("当前票数:"+piao++);
				
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.science_award, container,
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
		showdialogup();
		initdata();
		date();
		return rootView;

	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			// fragment可见时加载数据
			
		} else {
			// 不可见时不执行操作
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	protected void showdialogup() {
		jiazaidialog = new ResDialog(getActivity(),
				R.style.MyDialog, "正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void initdata() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.add("tid", tid);
		params.add("uid", IsTrue.userId + "");
		params.add("class", 2+"");
		client.post(MyUrl.yxcguo, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				jiazaidialog.dismiss();
				System.out.println(response+"HHHHHHH");
				try {
					String num = response.getString("num").toString();
					if (num.equals("1")) {
						text.setVisibility(View.VISIBLE);
					}else if(num.equals("2")){
						tp = response.getString("tp").toString();
						text.setVisibility(View.GONE);
						String lists = response.getString("list").toString();
						JSONArray array = new JSONArray(lists);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("image", object.get("image").toString());
							map.put("title", object.get("title").toString());
							map.put("piao", object.get("piao").toString());
							map.put("name", object.get("name").toString());
							map.put("id", object.get("id").toString());
							map.put("chuangbs", object.get("chuangbs").toString());
							map.put("chuangid", object.get("chuangid").toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				jiazaidialog.dismiss();
				System.out.println(errorResponse);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
	private void date() {
		adapter = new BaseAdapter() {
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView==null) {
					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.innovation_award_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) convertView.findViewById(R.id.innovation_award_item_img);
					holder.city = (TextView) convertView.findViewById(R.id.innovation_award_item_head);
					holder.text = (TextView)convertView.findViewById(R.id.innovation_award_item_text);
					holder.num = (TextView) convertView.findViewById(R.id.innovation_award_item_num);
					holder.button = (Button) convertView.findViewById(R.id.innovation_award_item_button);
					convertView.setTag(holder);
				}else {
					holder = (ViewHolder) convertView.getTag();
				}
				String chuangbs = listmap.get(position).get("chuangbs").toString();
				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
				if (chuangbs.equals("1")) {
					bitmapUtils.display(holder.img, ImageAddress.Stringchuangyi+listmap.get(position).get("image").toString());
				}else if (chuangbs.equals("2")) {
					bitmapUtils.display(holder.img, ImageAddress.cbit+listmap.get(position).get("image").toString());
				}
				holder.city.setText(listmap.get(position).get("title").toString());
				holder.text.setText(listmap.get(position).get("name").toString());
				holder.num.setText("当前票数:"+listmap.get(position).get("piao").toString());
				 piao=Integer.parseInt(listmap.get(position).get("piao").toString());
				if (tp.equals("1")) {
					holder.button.setText("免费投票");
					holder.button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							toupiao(listmap.get(position).get("id").toString());
							showdialogup();
						}
					});
				} else if (tp.equals("2")) {
					holder.button.setText("创币投票");
					holder.button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							toupiao(listmap.get(position).get("id").toString());
							showdialogup();
						}
					});
				} else if (tp.equals("3")) {
					holder.button.setText("不能投票");
				}else if(tp.equals("0")){
					holder.button.setText("不能投票");
				}
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent = new Intent(getActivity(),
								CreativeDetailsActivity.class);
						intent.putExtra("id", listmap.get(position).get("chuangid")
								.toString());
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
		list.setAdapter(adapter);
	}
	private void toupiao(String sid) {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.add("uid", IsTrue.userId + "");
		params.add("liid", tid);
		params.add("cla", 1 + "");
		params.add("sid", sid);
		client.post(MyUrl.ltoup, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				jiazaidialog.dismiss();
				System.out.println(response);
				try {
					String num = response.getString("num").toString();
					if (num.equals("1")) {
						Toast.makeText(getActivity(), "投票失败",
								Toast.LENGTH_SHORT).show();
					}else if (num.equals("2")) {
						Toast.makeText(getActivity(), "投票成功",
								Toast.LENGTH_SHORT).show();
						handle.sendEmptyMessage(112);
					}else if (num.equals("3")) {
						Toast.makeText(getActivity(), "明天再来投吧",
								Toast.LENGTH_SHORT).show();
					}else if (num.equals("4")) {
						Toast.makeText(getActivity(), "创币不足",
								Toast.LENGTH_SHORT).show();
					}
					tp = response.getString("tp").toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				jiazaidialog.dismiss();
				System.out.println(errorResponse);
				Toast.makeText(getActivity(), errorResponse + "",
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
	private void init() {
		list = (ListView) rootView.findViewById(R.id.science_award_list);
		text = (TextView) rootView.findViewById(R.id.science_award_list_text);
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String,Object>>();
		tid = getArguments().getString("tid");
	}

	@Override
	public void onClick(View v) {

	}
	static class ViewHolder{
		ImageView img;
		TextView city;
		TextView text;
		TextView num;
		Button button;
	}
}
