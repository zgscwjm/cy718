package com.lsfb.cysj.adapter;

import java.util.List;

import com.lsbf.cysj.R;
import com.lsfb.cysj.model.ZhiKuModel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ZhiKuListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private PopupWindow pw;
	View view;
	public TextView txtPlay, txtResetName, txtSetZhuji, txtRemove, txtjilu;
	int currentPosition;
	public ProgressDialog progressDialog;
	// private TAlarmSetInfor alarmInfo;
	public String imsi;
	Context con;
	List<ZhiKuModel> deviceList;
	ZhiKuModel devInfo;
	String uuid;

	public ZhiKuListAdapter(Context con) {
		this.con = con;
		inflater = LayoutInflater.from(con);
	}

	public ZhiKuListAdapter(Context con, List<ZhiKuModel> deviceList) {

		this.deviceList = deviceList;
		this.con = con;
		inflater = LayoutInflater.from(con);

	}

	public List<ZhiKuModel> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<ZhiKuModel> deviceList) {
		this.deviceList = deviceList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// if (deviceList == null)
		// return 1;
		// else
		return deviceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		// if (convertView == null) {
		holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.zhiku_manage_item, null);
		holder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

		Log.d("name" + position, deviceList.get(position).name);
		holder.tvCaption.setText(deviceList.get(position).name);
		// }
		// } else {
		// // holder.tvCaption.setText("体验安加");
		// }

		convertView.setTag(holder);

		return convertView;
	}

	public class ViewHolder {
		ImageView imgCamera;
		TextView tvCaption;
		ImageView imgArrow;
	}

}
