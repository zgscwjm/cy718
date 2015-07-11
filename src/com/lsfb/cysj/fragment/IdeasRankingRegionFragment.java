package com.lsfb.cysj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lsbf.cysj.R;
import com.lsfb.cysj.BestResultsActivity;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.base.NoViewPage;

public class IdeasRankingRegionFragment extends Fragment implements
		OnClickListener {

	private View rootView;
	/**
	 * ideas_ranking_region_Country country 国家级 ideas_ranking_region_province
	 * province 省级 ideas_ranking_region_city city 市级 ideas_ranking_region_county
	 * county 区级
	 */
	private TextView country, province, city, county;
	private NoViewPage page;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.ideas_ranking_region,
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
		data();
		return rootView;

	}

	private void data() {

	}

	private void init() {
		country = (TextView) rootView
				.findViewById(R.id.ideas_ranking_region_Country);
		country.setOnClickListener(this);
		province = (TextView) rootView
				.findViewById(R.id.ideas_ranking_region_province);
		province.setOnClickListener(this);
		city = (TextView) rootView.findViewById(R.id.ideas_ranking_region_city);
		city.setOnClickListener(this);
		county = (TextView) rootView
				.findViewById(R.id.ideas_ranking_region_county);
		county.setOnClickListener(this);
		page = (NoViewPage) rootView.findViewById(R.id.ideas_ranking_region_page);
		FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), 4, 8);
		page.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ideas_ranking_region_Country:// 国家级
			page.setCurrentItem(0);
			country.setBackgroundResource(R.drawable.shape);
			country.setTextColor(getActivity().getResources().getColorStateList(R.color.white));
			province.setBackgroundResource(R.drawable.city);
			province.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			city.setBackgroundResource(R.drawable.city);
			city.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			county.setBackgroundResource(R.drawable.city);
			county.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			break;
		case R.id.ideas_ranking_region_province:// 省级
			page.setCurrentItem(1);
			province.setBackgroundResource(R.drawable.shape);
			province.setTextColor(getActivity().getResources().getColorStateList(R.color.white));
			country.setBackgroundResource(R.drawable.city);
			country.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			city.setBackgroundResource(R.drawable.city);
			city.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			county.setBackgroundResource(R.drawable.city);
			county.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			break;
		case R.id.ideas_ranking_region_city:// 市级
			page.setCurrentItem(2);
			city.setBackgroundResource(R.drawable.shape);
			city.setTextColor(getActivity().getResources().getColorStateList(R.color.white));
			province.setBackgroundResource(R.drawable.city);
			province.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			country.setBackgroundResource(R.drawable.city);
			country.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			county.setBackgroundResource(R.drawable.city);
			county.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			break;
		case R.id.ideas_ranking_region_county:// 县级
			page.setCurrentItem(3);
			county.setBackgroundResource(R.drawable.shape);
			county.setTextColor(getActivity().getResources().getColorStateList(R.color.white));
			province.setBackgroundResource(R.drawable.city);
			province.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			city.setBackgroundResource(R.drawable.city);
			city.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			country.setBackgroundResource(R.drawable.city);
			country.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.greymy));
			break;
		default:
			break;
		}
	}
}
