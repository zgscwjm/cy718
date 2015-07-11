package com.lsfb.cysj.adapter;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.model.SortModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> list = null;
	private Context mContext;
	private List<Map<String, Object>> listmap = null;
	
	public SortAdapter(Context mContext, List<SortModel> list,List<Map<String, Object>> listmap) {
		this.mContext = mContext;
		this.list = list;
		this.listmap = listmap;
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list,List<Map<String, Object>> listmap){
		this.list = list;
		this.listmap = listmap;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.listmap.size();
	}

	public Object getItem(int position) {
		return listmap.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		System.out.println(list.toString()+"SSSSSSSSSS"+listmap.toString());
		SortModel mContent = list.get(position);
//		SortModel mContent = (SortModel) listmap.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.friends_attention_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.friends_attention_item_title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.friends_attention_item_catalog);
			viewHolder.img = (ImageView) view.findViewById(R.id.friends_attention_item_img);
			viewHolder.guanzhu = (ImageView) view.findViewById(R.id.friends_attention_item_guanzhu);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.display(viewHolder.img, ImageAddress.Stringhead+this.listmap.get(position).get("image").toString());
		viewHolder.tvTitle.setText(this.listmap.get(position).get("name").toString());
//		viewHolder.tvTitle.setText(this.list.get(position).getName());
		
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView img,guanzhu;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}