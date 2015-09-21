package com.lsfb.cysj;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.base.NoViewPage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 动态比赛
 * @author Administrator
 *
 */
public class IdeasWorldActivity extends FragmentActivity implements OnClickListener{
	/**
	 * ideas_world_button1 热门创意比赛
	 */
	@ViewInject(R.id.ideas_world_button1)
	private TextView ideas_world_button1;
	/**
	 * ideas_world_button2 创意世界
	 */
	@ViewInject(R.id.ideas_world_button2)
	private TextView ideas_world_button2;
	/**
	 * ideas_world_button3 创友圈
	 */
	@ViewInject(R.id.ideas_world_button3)
	private TextView ideas_world_button3;
	/**
	 * ideas_world_page 切换页面
	 */
	@ViewInject(R.id.ideas_world_page)
	private NoViewPage ideas_world_page;
	/**
	 * ideas_world_head 
	 * head 头部显示标题
	 */
	@ViewInject(R.id.ideas_world_head)
	private TextView head;
//	@ViewInject(R.id.ideas_world_back)
//	private LinearLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideas_world);
		ViewUtils.inject(this);
		init();
	}
	private void init() {
		ideas_world_button1.setOnClickListener(this);		
		ideas_world_button2.setOnClickListener(this);
		ideas_world_button3.setOnClickListener(this);
//		back.setOnClickListener(this);
		FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), 3, 4);
		ideas_world_page.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.ideas_world_back://返回
//			finish();
//			break;
		case R.id.ideas_world_button1:
			ideas_world_page.setCurrentItem(0);
			ideas_world_button1.setBackgroundResource(R.drawable.shape);
			ideas_world_button1.setTextColor(IdeasWorldActivity.this
					.getResources().getColorStateList(R.color.white));
			ideas_world_button2.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button2.setTextColor(IdeasWorldActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			ideas_world_button3.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button3.setTextColor(IdeasWorldActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button1_text);
			break;
		case R.id.ideas_world_button2:
			ideas_world_page.setCurrentItem(1);
			ideas_world_button2.setBackgroundResource(R.drawable.shape);
			ideas_world_button2.setTextColor(IdeasWorldActivity.this
					.getResources().getColorStateList(R.color.white));
			ideas_world_button1.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button1.setTextColor(IdeasWorldActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			ideas_world_button3.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button3.setTextColor(IdeasWorldActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button2_text);
			break;
		case R.id.ideas_world_button3:
			ideas_world_page.setCurrentItem(2);
			ideas_world_button3.setBackgroundResource(R.drawable.shape);
			ideas_world_button3.setTextColor(IdeasWorldActivity.this
					.getResources().getColorStateList(R.color.white));
			ideas_world_button2.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button2.setTextColor(IdeasWorldActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			ideas_world_button1.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button1.setTextColor(IdeasWorldActivity.this.getResources()
					.getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button3_text);
			break;
		default:
			break;
		}
	}
}
