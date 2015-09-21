package com.lsfb.cysj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.base.NoViewPage;
import com.lsfb.cysj.fragment.ArtPrizeFragment;
import com.lsfb.cysj.fragment.GoodnessAwardFragment;
import com.lsfb.cysj.fragment.InnovationAwardFragment;
import com.lsfb.cysj.fragment.LiteraryAwardFragment;
import com.lsfb.cysj.fragment.MingXingAwardFragment;
import com.lsfb.cysj.fragment.ScienceAwardFragment;
import com.viewpagerindicator.TabPageIndicator;


/**
 * 创意世界优秀成果
 * @author Administrator
 *
 */
public class BestResultsActivity extends FragmentActivity implements OnClickListener {
	/**
	 * Tab标题
	 */
	private static final String[] TITLE = new String[] { "创新奖", "科学奖", "艺术奖", "文学奖",
														"慈善奖", "明星奖"};
//	/**
//	 * innovation_award 创新奖
//	 */
//	@ViewInject(R.id.innovation_award)
//	private TextView innovation_award;
//	/**
//	 * science_award 科学奖
//	 */
//	@ViewInject(R.id.science_award)
//	private TextView science_award;
//	/**
//	 * artprize 艺术奖
//	 */
//	@ViewInject(R.id.artprize)
//	private TextView artprize;
//	/**
//	 * literary_award 文学奖
//	 */
//	@ViewInject(R.id.literary_award)
//	private TextView literary_award;
//	/**
//	 * goodness_award 慈善奖
//	 */
//	@ViewInject(R.id.goodness_award)
//	private TextView goodness_award;
	/**
	 * ideas_ranking_indicator 
	 * indicator title内容
	 */
	@ViewInject(R.id.ideas_ranking_indicator)
	private TabPageIndicator indicator;
	/**
	 * best_results_page 
	 * page 切换的页面
	 */
	@ViewInject(R.id.best_results_page)
	private NoViewPage page;
	/**
	 * best_results_back 返回主界面
	 */
	@ViewInject(R.id.best_results_back)
	private LinearLayout best_results_back;
	String tid;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.best_results);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		intent = getIntent();
		tid = intent.getExtras().getString("tid").toString();
		best_results_back.setOnClickListener(this);
		indicator.setOnClickListener(this);
		FragmentPagerAdapter pagerAdapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		page.setAdapter(pagerAdapter);
		//实例化TabPageIndicator然后设置ViewPager与之关联
        indicator.setViewPager(page);
        
        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
//		innovation_award.setOnClickListener(this);
//		science_award.setOnClickListener(this);
//		artprize.setOnClickListener(this);
//		literary_award.setOnClickListener(this);
//		goodness_award.setOnClickListener(this);
//		FragmentAdapter adapter = new FragmentAdapter(
//				getSupportFragmentManager(), 5, 2);
//		page.setAdapter(adapter);
	}
	/**
	 * ViewPager适配器
	 */
	class TabPageIndicatorAdapter extends FragmentPagerAdapter{
		Fragment fragment;
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int positon) {
//			if (positon==0) {
//				fragment = new InnovationAwardFragment();
//			}else if (positon==1) {
//				fragment = new ScienceAwardFragment();
//			}else if (positon==2) {
//				fragment = new ArtPrizeFragment();
//			}else if (positon==3) {
//				fragment = new LiteraryAwardFragment();
//			}else if (positon==4) {
//				fragment = new GoodnessAwardFragment();
//			}else if (positon==5) {
//				fragment = new GoodnessAwardFragment();
//			}
			switch (positon) {
			case 0:
				fragment = new InnovationAwardFragment();
				Bundle bundle = new Bundle();
				bundle.putString("tid", tid);
				fragment.setArguments(bundle);
				break;
			case 1:
				fragment = new ScienceAwardFragment();
				Bundle bundle2 = new Bundle();
				bundle2.putString("tid", tid);
				fragment.setArguments(bundle2);
				break;
			case 2:
				fragment = new ArtPrizeFragment();
				Bundle bundle3 = new Bundle();
				bundle3.putString("tid", tid);
				fragment.setArguments(bundle3);
				break;
			case 3:
				fragment = new LiteraryAwardFragment();
				Bundle bundle4 = new Bundle();
				bundle4.putString("tid", tid);
				fragment.setArguments(bundle4);
				break;
			case 4:
				fragment = new GoodnessAwardFragment();
				Bundle bundle5 = new Bundle();
				bundle5.putString("tid", tid);
				fragment.setArguments(bundle5);
				break;
			case 5:
				fragment = new MingXingAwardFragment();
				Bundle bundle6 = new Bundle();
				bundle6.putString("tid", tid);
				fragment.setArguments(bundle6);
				break;
			default:
				break;
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
		case R.id.best_results_back:
			finish();
			break;
		/*case R.id.innovation_award:// 创新奖
			best_results_page.setCurrentItem(0);
			innovation_award.setBackgroundResource(R.drawable.shape);
			innovation_award.setTextColor(BestResultsActivity.this
					.getResources().getColorStateList(R.color.white));
			science_award.setBackgroundResource(R.drawable.shapedefault);
			science_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			artprize.setBackgroundResource(R.drawable.shapedefault);
			artprize.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			literary_award.setBackgroundResource(R.drawable.shapedefault);
			literary_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			goodness_award.setBackgroundResource(R.drawable.shapedefault);
			goodness_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			break;
		case R.id.science_award:// 科学奖
			best_results_page.setCurrentItem(1);
			science_award.setBackgroundResource(R.drawable.shape);
			science_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.white));
			innovation_award.setBackgroundResource(R.drawable.shapedefault);
			innovation_award.setTextColor(BestResultsActivity.this
					.getResources().getColorStateList(R.color.greymy));
			artprize.setBackgroundResource(R.drawable.shapedefault);
			artprize.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			literary_award.setBackgroundResource(R.drawable.shapedefault);
			literary_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			goodness_award.setBackgroundResource(R.drawable.shapedefault);
			goodness_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			break;
		case R.id.artprize:// 艺术奖
			best_results_page.setCurrentItem(2);
			artprize.setBackgroundResource(R.drawable.shape);
			artprize.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.white));
			science_award.setBackgroundResource(R.drawable.shapedefault);
			science_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			innovation_award.setBackgroundResource(R.drawable.shapedefault);
			innovation_award.setTextColor(BestResultsActivity.this
					.getResources().getColorStateList(R.color.greymy));
			literary_award.setBackgroundResource(R.drawable.shapedefault);
			literary_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			goodness_award.setBackgroundResource(R.drawable.shapedefault);
			goodness_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			break;
		case R.id.literary_award:// 文学奖
			best_results_page.setCurrentItem(3);
			literary_award.setBackgroundResource(R.drawable.shape);
			literary_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.white));
			science_award.setBackgroundResource(R.drawable.shapedefault);
			science_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			artprize.setBackgroundResource(R.drawable.shapedefault);
			artprize.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			innovation_award.setBackgroundResource(R.drawable.shapedefault);
			innovation_award.setTextColor(BestResultsActivity.this
					.getResources().getColorStateList(R.color.greymy));
			goodness_award.setBackgroundResource(R.drawable.shapedefault);
			goodness_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			break;
		case R.id.goodness_award:// 慈善奖
			best_results_page.setCurrentItem(4);
			goodness_award.setBackgroundResource(R.drawable.shape);
			goodness_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.white));
			science_award.setBackgroundResource(R.drawable.shapedefault);
			science_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			artprize.setBackgroundResource(R.drawable.shapedefault);
			artprize.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			literary_award.setBackgroundResource(R.drawable.shapedefault);
			literary_award.setTextColor(BestResultsActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			innovation_award.setBackgroundResource(R.drawable.shapedefault);
			innovation_award.setTextColor(BestResultsActivity.this
					.getResources().getColorStateList(R.color.greymy));
			break;*/
		default:
			break;
		}
	}
}
