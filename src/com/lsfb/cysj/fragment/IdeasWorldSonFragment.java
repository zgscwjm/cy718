package com.lsfb.cysj.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsbf.cysj.R;
import com.lsfb.cysj.CreativeDetailsActivity;
import com.lsfb.cysj.HotZhiKuCity;
import com.lsfb.cysj.Dialog.ApplyDialog;
import com.lsfb.cysj.app.IsTrue;

/**
 * 动态比赛---创意世界---创意世界fragment,创意作品CreativeFragment
 * @author Administrator
 *
 */
public class IdeasWorldSonFragment extends Fragment implements OnClickListener {

	private View rootView;
	private TextView city, type;
	private ListView list;
	Intent intent;
	String result0 = "";
	String result1 = "";
	String result2 = "";
	String classs = "";
	CreativeFragment fragmebt_ideas_world;// 创意世界的Fragment
	public void refresh() {
		
		String result0 = "";
		String result1 = "";
		String result2 = "";
		String classs = "";
		fragmebt_ideas_world.changeDate(classs, result0, result1, result2);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.ideas_world_son, container,
					false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();

		return rootView;

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		System.err.println("开始1");
//		switch (requestCode) {
//		case 1:
//			System.err.println("开始2");
//			result0 = data.getExtras().getString("idresult0");
//			result1 = data.getExtras().getString("idresult1");
//			result2 = data.getExtras().getString("idresult2");
//			System.err.println("这里是1");
//			// resultnum = data.getExtras().getString("results");
//			fragmebt_ideas_world.changeDate(classs, result0, result1, result2);
//			System.err.println("这里是2");
//			break;
//
//		}

	}

	private void init() {
		city = (TextView) rootView.findViewById(R.id.ideas_world_son_button1);
		city.setOnClickListener(this);
		type = (TextView) rootView.findViewById(R.id.ideas_world_son_button2);
		type.setOnClickListener(this);
		fragmebt_ideas_world = (CreativeFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.fragmebt_ideas_world);

	}

	@Override
	public void onResume() {
		// ideas_vertical.smoothScrollTo(0, 0);
		result0=IsTrue.Stringidstrarea0;
		result1=IsTrue.Stringidstrarea1;
		result2=IsTrue.Stringidstrarea2;
		System.err.println("重复没有··········");
		fragmebt_ideas_world.changeDate(classs, result0, result1, result2);
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		IsTrue.Stringidstrarea0="";
		IsTrue.Stringidstrarea1="";
		IsTrue.Stringidstrarea2="";
		super.onDestroy();
	}

	@Override
	public void registerForContextMenu(View view) {
		// TODO Auto-generated method stub
		super.registerForContextMenu(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ideas_world_son_button1:// 选择城市
			intent = new Intent(getActivity(), HotZhiKuCity.class);
			startActivity(intent);
			break;
		case R.id.ideas_world_son_button2:// 创意类型
			showDialog();
			break;
		default:
			break;
		}
	}

	private void showDialog() {
		Dialog dialog = new ApplyDialog(getActivity(), R.style.MyDialog,new ApplyDialog.PriorityListener() {
			
			@Override
			public void refreshPriorityUI(String string, String string2) {
				// TODO Auto-generated method stub
				classs=string2;
				fragmebt_ideas_world.changeDate(classs, result0, result1, result2);
			}
		});
		dialog.show();
	}
}
