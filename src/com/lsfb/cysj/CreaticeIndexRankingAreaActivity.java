package com.lsfb.cysj;

import com.lsbf.cysj.R;
import com.lsbf.cysj.R.layout;
import com.lsbf.cysj.R.menu;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.base.NoViewPage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreaticeIndexRankingAreaActivity extends FragmentActivity {
	ImageButton ibCreatice_index_ranking_area_backing;
	TextView tvhead;
	String strid;//传过来的ID
	String strcid;//传过来的cid
	TextView tvCreatice_index_ranking_area_User;// 用户
	View tvCreatice_index_ranking_area_Userxian;//

	TextView tvCreatice_index_ranking_area_Idea;// 创意
	View tvCreatice_index_ranking_area_Ideaxian;//

	TextView tvCreatice_index_ranking_area_experts;// 智库专家
	View tvCreatice_index_ranking_area_expertsxian;//

	NoViewPage viewPagetvCreatice_index_ranking_area;
	FragmentAdapter adapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creatice_index_ranking_area);
		init();
		ibCreatice_index_ranking_area_backing
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		changecolor();
	}

	private void changecolor() {
		// TODO Auto-generated method stub
		tvCreatice_index_ranking_area_User
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						tvCreatice_index_ranking_area_User
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.blueMain));
						tvCreatice_index_ranking_area_Userxian
								.setBackgroundResource(R.color.blueMain);
						tvCreatice_index_ranking_area_Idea
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.greymy));
						tvCreatice_index_ranking_area_Ideaxian
								.setBackgroundResource(R.color.white);
						tvCreatice_index_ranking_area_experts
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.greymy));
						tvCreatice_index_ranking_area_expertsxian
								.setBackgroundResource(R.color.white);

						viewPagetvCreatice_index_ranking_area.setCurrentItem(0);
						adapter.setDataareaRanking(strcid, strid,1);
					}
				});
		tvCreatice_index_ranking_area_Idea
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						tvCreatice_index_ranking_area_User
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.greymy));
						tvCreatice_index_ranking_area_Userxian
								.setBackgroundResource(R.color.white);
						tvCreatice_index_ranking_area_Idea
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.blueMain));
						tvCreatice_index_ranking_area_Ideaxian
								.setBackgroundResource(R.color.blueMain);
						tvCreatice_index_ranking_area_experts
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.greymy));
						tvCreatice_index_ranking_area_expertsxian
								.setBackgroundResource(R.color.white);

						viewPagetvCreatice_index_ranking_area.setCurrentItem(1);
						adapter.setDataareaRanking(strcid, strid,2);
					}
				});
		tvCreatice_index_ranking_area_experts
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						tvCreatice_index_ranking_area_User
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.greymy));
						tvCreatice_index_ranking_area_Userxian
								.setBackgroundResource(R.color.white);
						tvCreatice_index_ranking_area_Idea
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.greymy));
						tvCreatice_index_ranking_area_Ideaxian
								.setBackgroundResource(R.color.white);
						tvCreatice_index_ranking_area_experts
								.setTextColor(CreaticeIndexRankingAreaActivity.this
										.getResources().getColorStateList(
												R.color.blueMain));
						tvCreatice_index_ranking_area_expertsxian
								.setBackgroundResource(R.color.blueMain);

						viewPagetvCreatice_index_ranking_area.setCurrentItem(2);
						adapter.setDataareaRanking(strcid, strid,3);
					}
				});
	}

	private void init() {
		// TODO Auto-generated method stub
		ibCreatice_index_ranking_area_backing = (ImageButton) findViewById(R.id.ibCreatice_index_ranking_area_backing);
		tvhead = (TextView) findViewById(R.id.tv_creaticeIndexHead);
		Intent intent = getIntent();
		strid=intent.getStringExtra("id").toString();
		strcid=intent.getStringExtra("cid").toString();
		tvhead.setText(intent.getStringExtra("area").toString());
		tvCreatice_index_ranking_area_User = (TextView) findViewById(R.id.tvCreatice_index_ranking_area_User);
		tvCreatice_index_ranking_area_Userxian = (View) findViewById(R.id.tvCreatice_index_ranking_area_Userxian);

		tvCreatice_index_ranking_area_Idea = (TextView) findViewById(R.id.tvCreatice_index_ranking_area_Idea);
		tvCreatice_index_ranking_area_Ideaxian = (View) findViewById(R.id.tvCreatice_index_ranking_area_Ideaxian);

		tvCreatice_index_ranking_area_experts = (TextView) findViewById(R.id.tvCreatice_index_ranking_area_experts);
		tvCreatice_index_ranking_area_expertsxian = (View) findViewById(R.id.tvCreatice_index_ranking_area_expertsxian);

		viewPagetvCreatice_index_ranking_area = (NoViewPage) findViewById(R.id.viewPagetvCreatice_index_ranking_area);
		 adapter = new FragmentAdapter(
				getSupportFragmentManager(), 3, 9);
		viewPagetvCreatice_index_ranking_area.setAdapter(adapter);
	}
}
