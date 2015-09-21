package com.lsfb.cysj.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.OtherDetailsActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.app.Myapplication;
import com.lsfb.cysj.model.SortModel;
import com.lsfb.cysj.utils.Show;
import com.umeng.socialize.utils.Log;

/**
 * 我的粉丝列表 ，增加关注功能
 * 
 * @time 0805
 * @author Administrator
 * 
 */
public class SortFansAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	RequestParams params;

	public SortFansAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		SortModel mContent = list.get(position);
		// SortModel mContent = (SortModel) listmap.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.friends_fans_item, null);
			viewHolder.tvTitle = (TextView) view
					.findViewById(R.id.friends_fans_item_title);
			viewHolder.tvLetter = (TextView) view
					.findViewById(R.id.friends_fans_item_catalog);
			viewHolder.img = (ImageView) view
					.findViewById(R.id.friends_fans_item_img);
			viewHolder.guanzhu = (Button) view
					.findViewById(R.id.friends_fans_item_guanzhu);
			viewHolder.guanzhu.setOnClickListener(new OnClickListstener(
					viewHolder.guanzhu, position));
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.display(viewHolder.img, ImageAddress.Stringhead
				+ this.list.get(position).getImage().toString());
		viewHolder.tvTitle
				.setText(this.list.get(position).getName().toString());
		// viewHolder.tvTitle.setText(this.list.get(position).getName());
		viewHolder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (list.get(position).getMid().toString()
						.equals(IsTrue.userId + "")) {
					Intent intent = new Intent(mContext,
							MyDetailsActivity.class);
					mContext.startActivity(intent);
				} else {
					Intent intent = new Intent(mContext,
							OtherDetailsActivity.class);
					intent.putExtra("id", list.get(position).getMid()
							.toString());
					mContext.startActivity(intent);
				}

			}
		});
		if(this.list.get(position).getMidds().equals("1"))
		{
		viewHolder.guanzhu.setText("关注");
		}else
		{
		viewHolder.guanzhu.setText("已关注");
		}
		
		return view;

	}

	/**
	 * 增加关注功能
	 * 
	 * @author Administrator
	 * 
	 */
	public class OnClickListstener implements OnClickListener

	{
		int position;
		Button imgaArrow;

		public OnClickListstener(int position) {
			this.position = position;

		}

		public OnClickListstener(Button imgaArrow, int position2) {
			// TODO Auto-generated constructor stub
			this.position = position2;
			this.imgaArrow = imgaArrow;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(((Button)v).getText().equals("关注"))
			{
			params = new RequestParams();
			params.addBodyParameter("mid", IsTrue.userId + "");
			params.addBodyParameter("uid", Myapplication.fensiList.get(position).get("mid") + "");
			Myapplication.httpUtils.send(HttpMethod.POST,
					MyUrl.stringLook, params,
					new RequestCallBack<String>() {
						
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							Show.toast(mContext,"关注失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							Show.toast(mContext,"关注成功");
							imgaArrow.setText("已关注");
						//	imgaArrow.setTextColor(mContext.getResources().getColor(R.color.gray_normal));
						}
					});
			}
			// int location[] = new int[2];
			// imgaArrow.getLocationOnScreen(location);
			// Show.toast(con, "点击了第" + position +
			// "个"+location[0]+",,"+location[1]);
			// pw.showAsDropDown(imgaArrow);

		}

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView img;
		Button guanzhu;
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
		String sortStr = str.trim().substring(0, 1).toUpperCase();
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
