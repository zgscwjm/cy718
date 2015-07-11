package com.lsfb.cysj.fragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lsbf.cysj.R;
import com.lsfb.cysj.MeditationActivity;
import com.lsfb.cysj.MyCollectionActivity;
import com.lsfb.cysj.MyIndexOfHistoryActivity;
import com.lsfb.cysj.MyChuangChuangCurrencyActivity;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.MyticketActivity;
import com.lsfb.cysj.SearchSchoolOrEnterpriseActivity;
import com.lsfb.cysj.SetUpTheActivity;
import com.lsfb.cysj.ThinkTankCertificationActivity;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.CircleImageView;

public class MyFragment extends Fragment {
	RelativeLayout my_details;// 进入我的详情内页
	RelativeLayout rlMyCreateindex;// 进入历史指数
	RelativeLayout rlMycurrency;// 进入我的创创币
	RelativeLayout rlCollection;// 进入我的收藏
	RelativeLayout rlMyticket;// 进入我的入场卷
	RelativeLayout rlThinkTankCertification;// 进入我的智库认证
	RelativeLayout rlMySetUpThe;// 进入设置
	RelativeLayout rlmeditation;// 进入禅修
	CircleImageView civhead;// 圆形头像
	TextView tv_My_name;// 名字
	TextView tv_My_chuangchuangNum;// 创创号
	TextView tv_My_chuangchuangzhikuNUm;// 创意世界智库号
	TextView tv_My_chuangyizhishu;// 创意指数
	TextView tv_My_chuangchuangbi;// 创创币
	private View rootView;
	HttpClient httpClient;
	String ccb = null;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				String str = msg.obj.toString();

				Log.d("2222222222222222222222222", str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					tv_My_chuangyizhishu.setText(jsonObject.getString("zhishu")
							.toString());// 创意指数
					tv_My_chuangchuangbi.setText(jsonObject.getString("ccb")
							.toString());// 创创币
					ccb = jsonObject.getString("ccb").toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onHiddenChanged(boolean)
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		setchu();
		pleaseIndex();
		super.onResume();
	}

	private void pleaseIndex() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {

					HttpPost post = new HttpPost(MyUrl.StringIndexRulechange);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("uid", IsTrue.userId + ""));
					if (IsTrue.userId == 0) {
						return;
					}
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {

						String str = EntityUtils.toString(response.getEntity());
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = str;
						handler.sendMessage(msg);

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

	public void setchu() {
		BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
		if (!IsTrue.Stringimage.equals("")) {
			bitmapUtils.display(civhead, ImageAddress.Stringhead
					+ IsTrue.Stringimage);
		}
		tv_My_name.setText(IsTrue.Stringnickname);// 名字
		tv_My_chuangchuangNum.setText(IsTrue.Stringnumber);// 创创号
		tv_My_chuangchuangzhikuNUm.setText(IsTrue.Stringznumber);// 创意世界智库号
		tv_My_chuangyizhishu.setText(IsTrue.Stringzhishu);// 创意指数
		tv_My_chuangchuangbi.setText(IsTrue.Stringmoney);// 创创币
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_my, container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.mian_my, container, false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();
		my_details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MyDetailsActivity.class);
				startActivity(intent);
			}
		});
		rlMyCreateindex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MyIndexOfHistoryActivity.class);
				startActivity(intent);
			}
		});
		rlMycurrency.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MyChuangChuangCurrencyActivity.class);
				intent.putExtra("ccb", ccb);
				startActivity(intent);
			}
		});
		rlCollection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MyCollectionActivity.class);
				startActivity(intent);
			}
		});
		rlMyticket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MyticketActivity.class);
				startActivity(intent);
			}
		});
		rlThinkTankCertification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ThinkTankCertificationActivity.class);
				startActivity(intent);
			}
		});
		rlMySetUpThe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						SetUpTheActivity.class);
				startActivity(intent);
			}
		});

		rlmeditation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MeditationActivity.class);
				startActivity(intent);
			}
		});

		return rootView;
	}

	private void init() {
		// TODO Auto-generated method stub
		httpClient = IsTrue.HttpConnectionManager.getHttpClient();
		my_details = (RelativeLayout) rootView.findViewById(R.id.my_details);
		rlMyCreateindex = (RelativeLayout) rootView
				.findViewById(R.id.rlMyCreateindex);
		rlMycurrency = (RelativeLayout) rootView
				.findViewById(R.id.rlMycurrency);
		rlCollection = (RelativeLayout) rootView
				.findViewById(R.id.rlCollection);
		rlMyticket = (RelativeLayout) rootView.findViewById(R.id.rlMyticket);
		rlThinkTankCertification = (RelativeLayout) rootView
				.findViewById(R.id.rlThinkTankCertification);
		rlMySetUpThe = (RelativeLayout) rootView
				.findViewById(R.id.rlMySetUpThe);
		rlmeditation = (RelativeLayout) rootView
				.findViewById(R.id.rlmeditation);
		civhead = (CircleImageView) rootView.findViewById(R.id.civhead);
		tv_My_name = (TextView) rootView.findViewById(R.id.tv_My_name);
		tv_My_chuangchuangNum = (TextView) rootView
				.findViewById(R.id.tv_My_chuangchuangNum);
		tv_My_chuangchuangzhikuNUm = (TextView) rootView
				.findViewById(R.id.tv_My_chuangchuangzhikuNUm);
		tv_My_chuangyizhishu = (TextView) rootView
				.findViewById(R.id.tv_My_chuangyizhishu);
		tv_My_chuangchuangbi = (TextView) rootView
				.findViewById(R.id.tv_My_chuangchuangbi);
	}

}
