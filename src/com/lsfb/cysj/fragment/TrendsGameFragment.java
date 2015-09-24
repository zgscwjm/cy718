package com.lsfb.cysj.fragment;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsbf.cysj.R;
import com.lsfb.cysj.FaBuFriendsActivity;
import com.lsfb.cysj.HomeActivity;
import com.lsfb.cysj.IdeasWorldActivity;
import com.lsfb.cysj.ReleaseGameActivity;
import com.lsfb.cysj.ReleaseIdeasActivity;
import com.lsfb.cysj.adapter.FragmentAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.Myapplication;
import com.lsfb.cysj.base.NoViewPage;
import com.lsfb.cysj.utils.SharedPrefsUtil;

/**
 * 动态比赛,1.熱門HotIdeasGameFragment。2.創意世界 IdeasWorldSonFragment3.
 * @author Administrator
 *
 */
public class TrendsGameFragment extends Fragment implements OnClickListener {

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	private View rootView;
//	private LinearLayout back;
	private TextView head;
	private TextView ideas_world_button1, ideas_world_button2,
			ideas_world_button3;
	private NoViewPage ideas_world_page;
	private RelativeLayout xie;
	Intent intent;
	FragmentAdapter adapter ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_trends_game, container,
		// false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.ideas_world, container, false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
				
				
			}
		}
//		IsTrue.fabulogin = false;//发布默认未登录
		init();
		data();
		return rootView;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			IsTrue.tabnum = 0;
		}
		return false;
		
	}
	
	private void data() {

	}

	private void init() {
//		back = (LinearLayout) rootView.findViewById(R.id.ideas_world_back);
//		back.setOnClickListener(this);
		head = (TextView) rootView.findViewById(R.id.ideas_world_head);
		head.setOnClickListener(this);
		ideas_world_button1 = (TextView) rootView
				.findViewById(R.id.ideas_world_button1);
		ideas_world_button2 = (TextView) rootView
				.findViewById(R.id.ideas_world_button2);
		ideas_world_button3 = (TextView) rootView
				.findViewById(R.id.ideas_world_button3);
		ideas_world_button1.setOnClickListener(this);
		ideas_world_button2.setOnClickListener(this);
		ideas_world_button3.setOnClickListener(this);
		
		//发布
		xie = (RelativeLayout) rootView.findViewById(R.id.ideas_world_xie);		
		xie.setOnClickListener(this);
		ideas_world_page = (NoViewPage) rootView
				.findViewById(R.id.ideas_world_page);
		 adapter = new FragmentAdapter(
				getChildFragmentManager(), 3, 4);
		ideas_world_page.setAdapter(adapter);
		if(3==SharedPrefsUtil.getIntValue(Myapplication.context, "pageto", 0)){
			ideas_world_page.setCurrentItem(2);
		    SharedPrefsUtil.putValue(Myapplication.context, "pageto", 0);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ideas_world_button1:
			IsTrue.fabuotheridea = 0;
			ideas_world_page.setCurrentItem(0);
			ideas_world_button1.setBackgroundResource(R.drawable.shape);
			ideas_world_button1.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.white));
			ideas_world_button2.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button2.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			ideas_world_button3.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button3.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button1_text);
			break;
		case R.id.ideas_world_button2:
			IsTrue.fabuotheridea = 1;
			ideas_world_page.setCurrentItem(1);
			ideas_world_button2.setBackgroundResource(R.drawable.shape);
			ideas_world_button2.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.white));
			ideas_world_button1.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button1.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			ideas_world_button3.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button3.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button2_text);
			adapter.refresh();
			break;
		case R.id.ideas_world_button3:
			IsTrue.fabuotheridea = 2;
			ideas_world_page.setCurrentItem(2);
			ideas_world_button3.setBackgroundResource(R.drawable.shape);
			ideas_world_button3.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.white));
			ideas_world_button2.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button2.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			ideas_world_button1.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button1.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button3_text);
			adapter.setDatechuangFriend();
			break;
			/*
			 * 发布图标按钮
			 */
		case R.id.ideas_world_xie:
			if (IsTrue.userId == 0) {
//				IsTrue.fabulogin = false;//发布默认未登录
//				intent = new Intent(getActivity(),HomeActivity.class);
//				startActivity(intent);
				Toast.makeText(getActivity(), "您还未登陆", Toast.LENGTH_SHORT).show();
				break;
			}else{
			if (IsTrue.fabuotheridea == 0) {
				intent = new Intent(getActivity(),ReleaseGameActivity.class);
				startActivity(intent);
			}else if (IsTrue.fabuotheridea == 1) {
				intent = new Intent(getActivity(),ReleaseIdeasActivity.class);
				startActivity(intent);
			}else if (IsTrue.fabuotheridea == 2) {
				intent = new Intent(getActivity(),FaBuFriendsActivity.class);
				startActivity(intent);
			}
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(ideas_world_page.getCurrentItem()==1){
			IsTrue.fabuotheridea = 1;
			ideas_world_button2.setBackgroundResource(R.drawable.shape);
			ideas_world_button2.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.white));
			ideas_world_button1.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button1.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			ideas_world_button3.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button3.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button2_text);
			adapter.refresh();
			
		}else if(ideas_world_page.getCurrentItem()==2){
			IsTrue.fabuotheridea = 2;
			ideas_world_button3.setBackgroundResource(R.drawable.shape);
			ideas_world_button3.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.white));
			ideas_world_button2.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button2.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			ideas_world_button1.setBackgroundResource(R.drawable.shapedefault);
			ideas_world_button1.setTextColor(getActivity()
					.getResources().getColorStateList(R.color.greymy));
			head.setText(R.string.ideas_world_button3_text);
			adapter.setDatechuangFriend();
			
		}
//		else if{
//			
//		}
	}

}
