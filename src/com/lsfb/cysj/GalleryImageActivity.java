package com.lsfb.cysj;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.CreativeDetailsActivity.ViewHolder;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;

public class GalleryImageActivity extends Activity {
	Intent intent;
	private ViewPager pager;
	String iamges;
	BaseAdapter adapter;
	private ArrayList<View> pageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.galleryimageactivity);
		init();
		data();
	}

	private void data() {
		final String[] splitimages = iamges.split(",");
		// 查找布局文件用LayoutInflater.inflate
		LayoutInflater inflater = getLayoutInflater();
		pageview = new ArrayList<View>();
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		System.out.println(splitimages.length+"OOOOOOOO");
		for (int i = 0; i < splitimages.length; i++) {
			View view = inflater.inflate(R.layout.kantu_img, null);
			ImageView iv = (ImageView) view.findViewById(R.id.img_kantu);
			System.out.println(ImageAddress.Stringchuangyi+ splitimages[i]);
			if (IsTrue.kantu == 1) {
				bitmapUtils.display(iv, ImageAddress.cbit+ splitimages[i]);
			}else if (IsTrue.kantu == 2) {
				bitmapUtils.display(iv, ImageAddress.Stringchuangyi+ splitimages[i]);
			}
			pageview.add(view);
		}
		// 数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			// 获取当前窗体界面数
			public int getCount() {
				return splitimages.length;
			}

			@Override
			// 断是否由对象生成界面
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			// 是从ViewGroup中移出当前View
			public void destroyItem(View arg0, int arg1, Object arg2) {
				((ViewPager) arg0).removeView(pageview.get(arg1));
			}

			// 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
			public Object instantiateItem(View arg0, int arg1) {
				((ViewPager) arg0).addView(pageview.get(arg1));
				return pageview.get(arg1);
			}
		};

		// 绑定适配器
		pager.setAdapter(mPagerAdapter);

		// adapter = new BaseAdapter() {
		// @Override
		// public View getView(int position, View view, ViewGroup parent) {
		// ViewHolder holder = null;
		// if (view == null) {
		// holder = new ViewHolder();
		// view =
		// LayoutInflater.from(getApplicationContext()).inflate(R.layout.kantu_img,
		// null);
		// holder.img = (ImageView) findViewById(R.id.img_kantu);
		// view.setTag(holder);
		// }else {
		// holder = (ViewHolder)view.getTag();
		// }
		// BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
		// bitmapUtils.display(holder.img, ImageAddress.Stringchuangyi
		// + splitimages[position]);
		// return view;
		// }
		//
		// @Override
		// public long getItemId(int position) {
		// return 0;
		// }
		//
		// @Override
		// public Object getItem(int arg0) {
		// return null;
		// }
		//
		// @Override
		// public int getCount() {
		// return iamges.length();
		// }
		// };
		// pager.setAdapter(adapter);
	}

	private void init() {
		pager = (ViewPager) findViewById(R.id.kantu);
		intent = getIntent();
		iamges = intent.getExtras().getString("imagekan");
		// String[] splitimages = iamges.split(",");
	}

	static class ViewHolder {
		ImageView img;
	}
}
