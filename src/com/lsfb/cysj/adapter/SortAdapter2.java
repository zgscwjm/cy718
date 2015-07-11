package com.lsfb.cysj.adapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.OtherDetailsActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.model.SortModel;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class SortAdapter2 extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	HttpClient httpClient;
	Handler handlernoguanzhu = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				String str = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(str);
					switch (Integer.parseInt(jsonObject.getString("num")
							.toString())) {
					case 1:
						Toast.makeText(mContext, "取消关注失败)", Toast.LENGTH_SHORT)
								.show();
						break;
					case 2:
						Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT)
								.show();
						notifyDataSetChanged();
						break;

					default:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public SortAdapter2(Context mContext, List<SortModel> list) {
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
					R.layout.friends_attention_item, null);
			viewHolder.tvTitle = (TextView) view
					.findViewById(R.id.friends_attention_item_title);
			viewHolder.tvLetter = (TextView) view
					.findViewById(R.id.friends_attention_item_catalog);
			viewHolder.img = (ImageView) view
					.findViewById(R.id.friends_attention_item_img);
			viewHolder.guanzhu = (ImageView) view
					.findViewById(R.id.friends_attention_item_guanzhu);
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
		viewHolder.guanzhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.err.println();
				noguanzhu(list, position);
			}
		});
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
		return view;

	}

	protected void noguanzhu(final List<SortModel> list, final int positions) {
		// TODO Auto-generated method stub
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
	
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.stringNoLook);

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("mid", IsTrue.userId + ""));
					
					params.add(new BasicNameValuePair("uid", list
							.get(positions).getMid().toString()));
					list.remove(positions);
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handlernoguanzhu.sendMessage(msg);

					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView img, guanzhu;
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