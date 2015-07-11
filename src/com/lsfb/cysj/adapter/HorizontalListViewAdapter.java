package com.lsfb.cysj.adapter;

import com.lsbf.cysj.R;
import com.lsfb.cysj.fragment.ZhikuFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter{

	public HorizontalListViewAdapter(Context context){
		mInflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return 5;
	}
	private LayoutInflater mInflater;
	@Override
	public Object getItem(int position) {
		return position;
	}
	private ViewHolder vh 	 =new ViewHolder();
	private static class ViewHolder {
		private TextView time ;
		private TextView title ;
		private ImageView im;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.ideas_horizontal, null);
			vh.im=(ImageView)convertView.findViewById(R.id.my_library_img);
			vh.time=(TextView)convertView.findViewById(R.id.my_library_city);
			vh.title=(TextView)convertView.findViewById(R.id.my_library_num);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		vh.time.setText("00:00");
		vh.title.setText("XXXXXX");
		return convertView;
	}
}
