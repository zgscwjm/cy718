package com.lsfb.cysj.adapter;

import java.util.List;

import com.lsbf.cysj.R;
import com.lsfb.cysj.base.Node;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T>
{

	public SimpleTreeAdapter(ListView list, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(list, context, datas, defaultExpandLevel);
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.city_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.hot_zhiku_city_item_img);
			viewHolder.label = (TextView) convertView
					.findViewById(R.id.hot_zhiku_city_item_city);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1)
		{
			viewHolder.icon.setVisibility(View.INVISIBLE);
		} else
		{
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}

		viewHolder.label.setText(node.getName());
		
		
		return convertView;
	}

	private final class ViewHolder
	{
		ImageView icon;
		TextView label;
	}

}
