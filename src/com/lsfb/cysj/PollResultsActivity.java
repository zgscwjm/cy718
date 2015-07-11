package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.view.ListViewForScrollView;
import com.lsfb.cysj.view.ResDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PollResultsActivity extends FragmentActivity implements
		OnClickListener {
	/**
	 * pollresults_more1 创新奖
	 */
	@ViewInject(R.id.pollresults_more1)
	private TextView pollresults_more1;
	/**
	 * pollresults_more2 科学奖
	 */
	@ViewInject(R.id.pollresults_more2)
	private TextView pollresults_more2;
	/**
	 * pollresults_more3艺术奖
	 */
	@ViewInject(R.id.pollresults_more3)
	private TextView pollresults_more3;
	/**
	 * pollresults_more4 文学奖
	 */
	@ViewInject(R.id.pollresults_more4)
	private TextView pollresults_more4;
	/**
	 * pollresults_more5 慈善奖
	 */
	@ViewInject(R.id.pollresults_more5)
	private TextView pollresults_more5;
	/**
	 * pollresults_more6 科学奖
	 */
	@ViewInject(R.id.pollresults_more6)
	private TextView pollresults_more6;
	@ViewInject(R.id.pollresults_back)
	private LinearLayout back;
	@ViewInject(R.id.pollresults_list1)
	private ListViewForScrollView list1;
	@ViewInject(R.id.pollresults_list2)
	private ListViewForScrollView list2;
	@ViewInject(R.id.pollresults_list3)
	private ListViewForScrollView list3;
	@ViewInject(R.id.pollresults_list4)
	private ListViewForScrollView list4;
	@ViewInject(R.id.pollresults_list5)
	private ListViewForScrollView list5;
	@ViewInject(R.id.pollresults_list6)
	private ListViewForScrollView list6;
	BaseAdapter adapter1;
	BaseAdapter adapter2;
	BaseAdapter adapter3;
	BaseAdapter adapter4;
	BaseAdapter adapter5;
	BaseAdapter adapter6;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	Dialog jiazaidialog;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pollresults);
		ViewUtils.inject(this);
		init();
		shuju1();
		shuju2();
		shuju3();
		shuju4();
		shuju5();
		shuju6();
		data();
	}

	

	private void data() {
		showdialogup();
		httpUtils = new HttpUtils();
		params = new RequestParams();
		
	}
	protected void showdialogup() {
		jiazaidialog = new ResDialog(PollResultsActivity.this,
				R.style.MyDialog, "正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}
	private void shuju1() {
		listmap = new ArrayList<HashMap<String, Object>>();
		adapter1 = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.goodness_award_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.goodness_award_list_item_img);
					holder.title = (TextView) view
							.findViewById(R.id.goodness_award_list_item_head);
					holder.text = (TextView) view
							.findViewById(R.id.goodness_award_list_num);
					holder.btn = (Button) view
							.findViewById(R.id.goodness_award_list_item_button);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
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
		list1.setAdapter(adapter1);
	}
	private void shuju2() {
		listmap = new ArrayList<HashMap<String, Object>>();
		adapter2 = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.goodness_award_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.goodness_award_list_item_img);
					holder.title = (TextView) view
							.findViewById(R.id.goodness_award_list_item_head);
					holder.text = (TextView) view
							.findViewById(R.id.goodness_award_list_num);
					holder.btn = (Button) view
							.findViewById(R.id.goodness_award_list_item_button);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
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
		list2.setAdapter(adapter2);
	}
	private void shuju3() {
		listmap = new ArrayList<HashMap<String, Object>>();
		adapter3 = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.goodness_award_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.goodness_award_list_item_img);
					holder.title = (TextView) view
							.findViewById(R.id.goodness_award_list_item_head);
					holder.text = (TextView) view
							.findViewById(R.id.goodness_award_list_num);
					holder.btn = (Button) view
							.findViewById(R.id.goodness_award_list_item_button);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
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
		list3.setAdapter(adapter3);
	}
	private void shuju4() {
		listmap = new ArrayList<HashMap<String, Object>>();
		adapter4 = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.goodness_award_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.goodness_award_list_item_img);
					holder.title = (TextView) view
							.findViewById(R.id.goodness_award_list_item_head);
					holder.text = (TextView) view
							.findViewById(R.id.goodness_award_list_num);
					holder.btn = (Button) view
							.findViewById(R.id.goodness_award_list_item_button);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
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
		list4.setAdapter(adapter4);
	}
	private void shuju5() {
		listmap = new ArrayList<HashMap<String, Object>>();
		adapter5 = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.goodness_award_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.goodness_award_list_item_img);
					holder.title = (TextView) view
							.findViewById(R.id.goodness_award_list_item_head);
					holder.text = (TextView) view
							.findViewById(R.id.goodness_award_list_num);
					holder.btn = (Button) view
							.findViewById(R.id.goodness_award_list_item_button);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
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
		list5.setAdapter(adapter5);
	}
	private void shuju6() {
		listmap = new ArrayList<HashMap<String, Object>>();
		adapter6 = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.goodness_award_list_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.goodness_award_list_item_img);
					holder.title = (TextView) view
							.findViewById(R.id.goodness_award_list_item_head);
					holder.text = (TextView) view
							.findViewById(R.id.goodness_award_list_num);
					holder.btn = (Button) view
							.findViewById(R.id.goodness_award_list_item_button);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
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
		list6.setAdapter(adapter6);
	}
	static class ViewHolder {
		ImageView img;
		TextView title;
		TextView name;
		TextView text;
		Button btn;
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		back.setOnClickListener(this);
		pollresults_more1.setOnClickListener(this);
		pollresults_more2.setOnClickListener(this);
		pollresults_more3.setOnClickListener(this);
		pollresults_more4.setOnClickListener(this);
		pollresults_more5.setOnClickListener(this);
		pollresults_more6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pollresults_back:
			finish();
			break;
		case R.id.pollresults_more1:

			break;
		case R.id.pollresults_more2:

			break;
		case R.id.pollresults_more3:

			break;
		case R.id.pollresults_more4:

			break;
		case R.id.pollresults_more5:

			break;
		case R.id.pollresults_more6:

			break;
		default:
			break;
		}
	}
}
