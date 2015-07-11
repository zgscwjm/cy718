package com.lsfb.cysj.adapter;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.model.ChairmanModel;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

public class ChairmanAdapter extends XUtilsBaseAdapterInject<ChairmanModel>{

	public ChairmanAdapter(Context context) {
		super(context);
	}

	@Override
	public int getConvertViewId(int position) {
		return R.layout.ideas_world_man;
	}

	@Override
	public XUtilsViewHolderInject<ChairmanModel> getNewHolder(int position) {
		return new ViewHolder();
	}
	class ViewHolder extends XUtilsViewHolderInject<ChairmanModel>{
		@ViewInject(R.id.ideas_world_man_img)
		private ImageView img;
		@ViewInject(R.id.ideas_world_man_item_text)
		private TextView name;
		@ViewInject(R.id.ideas_world_man_item_num)
		private TextView num;
		public ChairmanModel model;
		@Override
		public void loadData(ChairmanModel data, int position) {
			img.setBackgroundResource(R.drawable.headimage);
			name.setText("王小庄");
			num.setText("555");
		}
		
	}
}
