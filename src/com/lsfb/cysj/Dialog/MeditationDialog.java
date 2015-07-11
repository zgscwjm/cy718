package com.lsfb.cysj.Dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsbf.cysj.R;
import com.lsbf.cysj.R.integer;
import com.lsfb.cysj.GameDetailsActivity;
import com.lsfb.cysj.app.MyUrl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MeditationDialog extends Dialog {
	String name;
	Context context;
	private ListView list;
	AsyncHttpClient client;
	HashMap<String, Object> map;
	List<Map<String, Object>> listmap;
	Dialog dialog;
	int length;
	private static String[] nums = new String[] { "222", "223", "224", "225",
			"226", "227", "228", "222", "223", "224", "225", "226", "227",
			"228", "222", "223", "224", "225", "226", "227", "228" };
	BaseAdapter adapter;

	public MeditationDialog(Context context) {
		super(context);
		this.context = context;
	}

	public MeditationDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.gongdebu);
		list = (ListView) findViewById(R.id.gongde_list);
		listmap = new ArrayList<Map<String,Object>>();
//		showdialog();
		data();
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(getContext()).inflate(
							R.layout.gongde_list_item, null);
					holder = new ViewHolder();
					holder.num = (TextView) convertView
							.findViewById(R.id.gongde_list_item_text1);
					holder.date = (TextView) convertView
							.findViewById(R.id.gongde_list_item_text2);
					holder.text = (TextView) convertView
							.findViewById(R.id.gongde_list_item_text3);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.text.setText(listmap.get(position).get("content").toString());
				holder.date.setText(listmap.get(position).get("date").toString());
				holder.num.setText(listmap.get(position).get("zhanghao").toString());
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return length;
			}
		};
		list.setAdapter(adapter);

	}

	private void data() {
		client = new AsyncHttpClient();
		client.post(MyUrl.meritsslide, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					int i = Integer.parseInt(num);
					if (i == 2) {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						length = array.length();
						//[{"date":"01月01日","content":"上香一次·富贵吉祥","zhanghao":"651***"}]
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							map = new HashMap<String, Object>();
							map.put("content", object.getString("content"));
							map.put("date", object.getString("date"));
							map.put("zhanghao", object.getString("zhanghao"));
							listmap.add(map);
						}
						
					} else if(i==1){
						Toast.makeText(getContext(), "还未有人", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
//				dialog.dismiss();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void showdialog() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(R.layout.dialogview, null);

		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);

		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(),
				R.anim.animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);

		dialog = new Dialog(getContext(),
				R.style.FullHeightDialog);
		dialog.setCancelable(true);
		dialog.show();
		dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));		
	}

	static class ViewHolder {
		TextView num, date, text;
	}
}
