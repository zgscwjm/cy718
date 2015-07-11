package com.lsfb.cysj;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.base.NoViewPage;
import com.lsfb.cysj.fragment.GameApplyFragment;
import com.lsfb.cysj.fragment.GameEndFragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IdeasWorldGameActivity extends FragmentActivity implements
		OnClickListener {
	/**
	 * ideas_world_game_text1 报名中
	 */
	@ViewInject(R.id.ideas_world_game_text1)
	private TextView ideas_world_game_text1;
	/**
	 * ideas_world_game_text2 已结束
	 */
	@ViewInject(R.id.ideas_world_game_text2)
	private TextView ideas_world_game_text2;
	/**
	 * page 创意世界大赛内容
	 */
	@ViewInject(R.id.ideas_world_game_listitem)
	private NoViewPage page;
	@ViewInject(R.id.ideas_world_game_back)
	private LinearLayout back;
	@ViewInject(R.id.ideas_world_game_city)
	private LinearLayout city;
	/**
	 * ideas_world_game_text1_img 报名中下线
	 */
	@ViewInject(R.id.ideas_world_game_text1_img)
	private ImageView ideas_world_game_text1_img;
	/**
	 * ideas_world_game_text2_img 报名中下线
	 */
	@ViewInject(R.id.ideas_world_game_text2_img)
	private ImageView ideas_world_game_text2_img;
	Drawable drawable;
	Intent intent;
	FragmentAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideas_world_game);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		IsTrue.city = false;
		ideas_world_game_text1.setOnClickListener(this);
		ideas_world_game_text2.setOnClickListener(this);
		page.setOnClickListener(this);
		adapter = new FragmentAdapter(
				getSupportFragmentManager(), 2, 6);
		page.setAdapter(adapter);
		back.setOnClickListener(this);
		city.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			String result0 = data.getExtras().getString("result0");
			String result1 = data.getExtras().getString("result1");
			String result2 = data.getExtras().getString("result2");
			String result3 = data.getExtras().getString("result3");
			if (IsTrue.city == false) {
				adapter.city(result0, result1, result2, result3);
			}else if (IsTrue.city == true) {
				adapter.city(result0, result1, result2, result3);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ideas_world_game_back:
			IsTrue.city = false;
			finish();
			break;
		case R.id.ideas_world_game_city:// 城市
			intent = new Intent(IdeasWorldGameActivity.this, HotZhiKuCity.class);
			// startActivity(intent);
			startActivityForResult(intent, 1);
			break;
		case R.id.ideas_world_game_text1:// 报名中
			IsTrue.city = false;
			page.setCurrentItem(0);
			ideas_world_game_text1.setTextColor(getApplicationContext()
					.getResources().getColor(R.color.blueMain));
			ideas_world_game_text1_img.setVisibility(View.VISIBLE);
			ideas_world_game_text2.setTextColor(getApplicationContext()
					.getResources().getColor(R.color.zhiku));
			ideas_world_game_text2_img.setVisibility(View.GONE);
			break;
		case R.id.ideas_world_game_text2:// 已结束
			IsTrue.city = true;
			page.setCurrentItem(1);
			ideas_world_game_text2.setTextColor(getApplicationContext()
					.getResources().getColor(R.color.blueMain));
			ideas_world_game_text1_img.setVisibility(View.GONE);
			ideas_world_game_text1.setTextColor(getApplicationContext()
					.getResources().getColor(R.color.zhiku));
			ideas_world_game_text2_img.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
}
