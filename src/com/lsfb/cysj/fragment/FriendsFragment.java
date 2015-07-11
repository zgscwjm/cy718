package com.lsfb.cysj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.AddFriendsActivity;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.base.NoViewPage;

public class FriendsFragment extends Fragment implements OnClickListener {
	int h;
	private View rootView;
	Intent intent;
	/**
	 * friends_friends 好友 friends_attention 关注的人 friends_fans 我的粉丝 friends_games
	 * 关注的比赛
	 */
	private TextView friends_friends, friends_attention, friends_fans,
			friends_games;
	private ViewPager friends_page;
	private LinearLayout addfriends;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater
					.inflate(R.layout.mian_friends, container, false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();
		// height();//显示ScrollView内容
		return rootView;

	}

	private void height() {
		final int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		 h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		ViewTreeObserver vto = friends_page.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			public void onGlobalLayout() {
				friends_page.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				View view = friends_page.getChildAt(friends_page
						.getCurrentItem());
				if (view!=null) {
					
					view.measure(w, h);
					LayoutParams params = new LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.height = view.getMeasuredHeight();
					friends_page.setLayoutParams(params);
				}
			}
		});
		
	}
	private void init() {
		friends_friends = (TextView) rootView
				.findViewById(R.id.friends_friends);
		friends_attention = (TextView) rootView
				.findViewById(R.id.friends_attention);
		friends_fans = (TextView) rootView.findViewById(R.id.friends_fans);
		friends_games = (TextView) rootView.findViewById(R.id.friends_games);
		friends_friends.setOnClickListener(this);
		friends_attention.setOnClickListener(this);
		friends_fans.setOnClickListener(this);
		friends_games.setOnClickListener(this);
		addfriends = (LinearLayout) rootView.findViewById(R.id.mian_friends_add);
		addfriends.setOnClickListener(this);
		friends_page = (ViewPager) rootView.findViewById(R.id.friends_page);
		FragmentAdapter adapter = new FragmentAdapter(
				getChildFragmentManager(), 4, 5);
		friends_page.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friends_friends:
			friends_page.setCurrentItem(0);
			break;
		case R.id.friends_attention:
			friends_page.setCurrentItem(1);
			break;
		case R.id.friends_fans:
			friends_page.setCurrentItem(2);
			break;
		case R.id.friends_games:
			friends_page.setCurrentItem(3);
			break;
		case R.id.mian_friends_add://添加朋友
			intent = new Intent(getActivity(),AddFriendsActivity.class);
			startActivity(intent);
			break;
		}
	}
}
