package com.lsfb.cysj;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.BestResultsActivity.TabPageIndicatorAdapter;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.base.NoViewPage;
import com.lsfb.cysj.fragment.ArtPrizeFragment;
import com.lsfb.cysj.fragment.GoodnessAwardFragment;
import com.lsfb.cysj.fragment.IdeasRankingFirmFragment;
import com.lsfb.cysj.fragment.IdeasRankingFriendsFragment;
import com.lsfb.cysj.fragment.IdeasRankingGovFragment;
import com.lsfb.cysj.fragment.IdeasRankingRegionFragment;
import com.lsfb.cysj.fragment.IdeasRankingSchoolFragment;
import com.lsfb.cysj.fragment.InnovationAwardFragment;
import com.lsfb.cysj.fragment.LiteraryAwardFragment;
import com.lsfb.cysj.fragment.ScienceAwardFragment;
import com.viewpagerindicator.TabPageIndicator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IdeasRankingActivity extends FragmentActivity implements
		OnClickListener {
	/**
	 * Tab标题
	 */
	private static final String[] TITLE = new String[] { "创友圈排行", "地区排行",
			"学校排行", "政府排行", "企业排行" };
	// /**
	// * ideas_ranking 创友圈排行
	// */
	// @ViewInject(R.id.ideas_ranking_friends)
	// private TextView ideas_ranking_friends;
	// /**
	// * ideas_ranking_region 地区排行
	// */
	// @ViewInject(R.id.ideas_ranking_region)
	// private TextView ideas_ranking_region;
	// /**
	// * ideas_ranking_school 学校排行
	// */
	// @ViewInject(R.id.ideas_ranking_school)
	// private TextView ideas_ranking_school;
	// /**
	// * ideas_ranking_gov 政府排行
	// */
	// @ViewInject(R.id.ideas_ranking_gov)
	// private TextView ideas_ranking_gov;
	// /**
	// * ideas_ranking_firm 企业排行
	// */
	// @ViewInject(R.id.ideas_ranking_firm)
	// private TextView ideas_ranking_firm;
	@ViewInject(R.id.ideas_ranking_indicator)
	private TabPageIndicator indicator;
	/**
	 * ideas_ranking_back 返回主页面
	 */
	@ViewInject(R.id.ideas_ranking_back)
	private LinearLayout ideas_ranking_back;
	/**
	 * ideas_ranking_page 切换页面
	 */
	@ViewInject(R.id.ideas_ranking_page)
	private NoViewPage ideas_ranking_page;
	/**
	 * drawable 下图标
	 */
	Drawable drawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ideas_ranking);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		// drawable = getResources().getDrawable(
		// R.drawable.indexthisfooter);
		// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
		// drawable.getMinimumHeight());
		// ideas_ranking_friends.setOnClickListener(this);
		// ideas_ranking_region.setOnClickListener(this);
		// ideas_ranking_school.setOnClickListener(this);
		// ideas_ranking_gov.setOnClickListener(this);
		// ideas_ranking_firm.setOnClickListener(this);
		ideas_ranking_back.setOnClickListener(this);
		ideas_ranking_page.setOnClickListener(this);
		final FragmentPagerAdapter pagerAdapter = new TabPageIndicatorAdapter(
				getSupportFragmentManager());
		ideas_ranking_page.setAdapter(pagerAdapter);
		// 实例化TabPageIndicator然后设置ViewPager与之关联
		indicator.setViewPager(ideas_ranking_page);

		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int i) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		// FragmentAdapter adapter = new FragmentAdapter(
		// getSupportFragmentManager(), 5, 3);
		// ideas_ranking_page.setAdapter(adapter);
	}

	/**
	 * ViewPager适配器
	 */
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		Fragment fragment;

		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int positon) {
			if (positon == 0) {
				fragment = new IdeasRankingFriendsFragment();				
			} else if (positon == 1) {
				fragment = new IdeasRankingRegionFragment();
			} else if (positon == 2) {
				fragment = new IdeasRankingSchoolFragment();
			} else if (positon == 3) {
				fragment = new IdeasRankingGovFragment();
			} else if (positon == 4) {
				fragment = new IdeasRankingFirmFragment();
			}
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public int getCount() {
			return TITLE.length;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ideas_ranking_back:
			finish();
			break;
		// case R.id.ideas_ranking_friends://创友圈排行
		// ideas_ranking_page.setCurrentItem(0);
		// ideas_ranking_friends.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.blueMain));
		// ideas_ranking_friends.setCompoundDrawables(null, null, null,
		// drawable);
		// ideas_ranking_region.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_region.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_school.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_school.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_gov.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_gov.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_firm.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_firm.setCompoundDrawables(null, null, null, null);
		// break;
		// case R.id.ideas_ranking_region://地区排名
		// ideas_ranking_page.setCurrentItem(1);
		// ideas_ranking_region.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.blueMain));
		// ideas_ranking_region.setCompoundDrawables(null, null, null,
		// drawable);
		// ideas_ranking_friends.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_friends.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_school.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_school.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_gov.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_gov.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_firm.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_firm.setCompoundDrawables(null, null, null, null);
		// break;
		// case R.id.ideas_ranking_school://学校排名
		// ideas_ranking_page.setCurrentItem(2);
		// ideas_ranking_school.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.blueMain));
		// ideas_ranking_school.setCompoundDrawables(null, null, null,
		// drawable);
		// ideas_ranking_region.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_region.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_friends.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_friends.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_gov.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_gov.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_firm.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_firm.setCompoundDrawables(null, null, null, null);
		// break;
		// case R.id.ideas_ranking_gov://政府排名
		// ideas_ranking_page.setCurrentItem(3);
		// ideas_ranking_gov.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.blueMain));
		// ideas_ranking_gov.setCompoundDrawables(null, null, null, drawable);
		// ideas_ranking_region.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_region.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_school.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_school.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_friends.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_friends.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_firm.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_firm.setCompoundDrawables(null, null, null, null);
		// break;
		// case R.id.ideas_ranking_firm://企业排名
		// ideas_ranking_page.setCurrentItem(4);
		// ideas_ranking_firm.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.blueMain));
		// ideas_ranking_firm.setCompoundDrawables(null, null, null, drawable);
		// ideas_ranking_region.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_region.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_school.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_school.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_gov.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_gov.setCompoundDrawables(null, null, null, null);
		// ideas_ranking_friends.setTextColor(IdeasRankingActivity.this.getResources().getColor(R.color.zhiku));
		// ideas_ranking_friends.setCompoundDrawables(null, null, null, null);
		// break;

		default:
			break;
		}
	}
}
