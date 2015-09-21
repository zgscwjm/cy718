package com.lsfb.cysj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CheckboxAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<HashMap<String, Object>> listmap;	
	//记录checkbox的状态
	public HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();		

	//构造函数
	public CheckboxAdapter(Context context,	ArrayList<HashMap<String, Object>> listmap) {
		this.context = context;
		this.listmap = listmap;	
	}
	@Override
	public int getCount() {
		return listmap.size();
	}
	public HashMap<Integer, Boolean> getstate(){
		return state;
	}
	@Override
	public Object getItem(int position) {
		return listmap.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 重写View
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.inviteman_item, null);
			holder = new ViewHolder();
			
			holder.rlCheck= (RelativeLayout) convertView
					.findViewById(R.id.rlCheck);
			
			holder.headimg = (ImageView) convertView
					.findViewById(R.id.inviteman_item_img);
			holder.name = (TextView) convertView
					.findViewById(R.id.inviteman_item_name);
			holder.num = (TextView) convertView
					.findViewById(R.id.inviteman_item_text);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.inviteman_item_cb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(holder.headimg, ImageAddress.Stringhead
				+ listmap.get(position).get("image").toString());
		holder.name.setText(listmap.get(position).get("nickname")
				.toString());
		holder.num.setText(listmap.get(position).get("index")
				.toString());
//		holder.num.setText(nums[position]);
				
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					state.put(position, isChecked);					
				} else {
					state.remove(position);		
				}
			}
		});
		holder.checkBox.setChecked((state.get(position) == null ? false : true));
		return convertView;
	}
	static class ViewHolder {
		ImageView headimg;
		TextView name;
		TextView num;
		CheckBox checkBox;
		RelativeLayout rlCheck;
	}
}