package com.lsfb.cysj.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lsbf.cysj.R;
import com.lsfb.cysj.HomeActivity;
import com.lsfb.cysj.HotNews;
import com.lsfb.cysj.IdeasRankingActivity;
import com.lsfb.cysj.MyDetailsActivity;
import com.lsfb.cysj.NewsActivity;
import com.lsfb.cysj.SearchActivity;
import com.lsfb.cysj.XiTongMsg;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.bannerview.AdGalleryHelper;
import com.lsfb.cysj.bannerview.Advertising;

import com.lsfb.cysj.view.CircleImageView;
import com.lsfb.cysj.view.ListViewForScrollView;
import com.lsfb.cysj.view.ResDialog;
import com.readystatesoftware.viewbadger.BadgeView;

@SuppressLint("ResourceAsColor")
public class IdeasFragment extends Fragment implements OnClickListener {
	private static String[] nums = new String[] { "222", "223", "224", "225",
			"226", "227", "228" };
	private View rootView;
	Intent intent;

	/**
	 * head 自定义圆形头像
	 */
	private CircleImageView head;
	/**
	 * head_num 头像上面的数字
	 */
	private Button head_num;
	/**
	 * head_num2 头像下面的数字
	 */
	private TextView head_num2;
	/**
	 * head_search 搜索框里面的内容
	 */
	private TextView head_search;
	/**
	 * search 搜索
	 */
	private RelativeLayout search;
	/**
	 * search_img 搜索图片
	 */
	private ImageView search_img;
	/**
	 * head_page viewpager ����ҳ
	 */
	// private ViewPager head_page;

	/**
	 * ideas_Ranking 创意指数排行
	 */
	private LinearLayout ideas_Ranking;
	/**
	 * ideas_listview 创意下面的消息
	 */
	public ListViewForScrollView ideas_listview;

	/**
	 * data ���
	 */
	private List<Map<String, Object>> data;
	/**
	 * zhikufragment �ǿ����
	 */
	private ZhikuFragment zhikufragment;
	ScrollView ideas_vertical;
	BaseAdapter adapter;

	RelativeLayout galleryContainer; // 广告业滚动
	LayoutInflater inflater;
	AdGalleryHelper mGalleryHelper;
	HttpUtils httpUtils;
	RequestParams params;
	Dialog jiazaidialog;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	private ImageView img1,img2,img3;
	private TextView title1,title2,title3;
	private TextView text1,text2,text3;
	private TextView time1,time2,time3;
	private RelativeLayout news1;
	private RelativeLayout news2;
	private RelativeLayout news3;
	String sysxx;//系统消息
	private RelativeLayout mian_ideas_num;
	private ImageView mian_ideas_num2;
	private Button delmsg;
	String url = null;
	String url2 = null;//热门新闻
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_ideas, container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.mian_ideas, container, false);

		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		// 初始化
		init();
		showdialogup();
		date();
		adapter();
		return rootView;
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(getActivity(), R.style.MyDialog,
				"正在加载...", R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void adapter() {
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder holder = null;
				if (view == null) {
					view = LayoutInflater.from(getActivity()).inflate(
							R.layout.ideas_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view
							.findViewById(R.id.ideas_list_head);
					holder.title = (TextView) view
							.findViewById(R.id.ideas_list_msg);
					holder.num = (TextView) view
							.findViewById(R.id.ideas_list_content);
					holder.time = (TextView) view
							.findViewById(R.id.ideas_list_time);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				holder.num.setText(nums[position]);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (IsTrue.userId == 0) {

						} else {
							intent = new Intent(getActivity(),
									NewsActivity.class);
							startActivity(intent);
						}
					}
				});
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return nums.length;
			}
		};
		ideas_listview.setAdapter(adapter);
	}

	private void date() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId + "");
		httpUtils.send(HttpMethod.POST, MyUrl.index, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getActivity(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						jiazaidialog.dismiss();
						String list = responseInfo.result;
						System.out.println(list+"OOOOOOOOOOOOOOO");
						try {
							JSONObject object = new JSONObject(list);
							String banner = object.getString("banner");
							JSONArray array = new JSONArray(banner);
							for (int i = 0; i < array.length(); i++) {
								JSONObject object2 = (JSONObject) array.get(i);
								map = new HashMap<String, Object>();
								map.put("title", object2.getString("title")
										.toString());
								map.put("image", object2.getString("image")
										.toString());
								listmap.add(map);
							}
							String news2 = object.getString("news2").toString();
							news2(news2);//佛学新闻
							String news1 = object.getString("news1").toString();
							news1(news1);//热门新闻
							String sys = object.getString("sys").toString();
							if (sys.equals("0")) {
								news3.setVisibility(View.GONE);
							}else {
								news3.setVisibility(View.VISIBLE);
								sysxx = object.getString("sysxx").toString();
								systemmsg(sysxx,sys);
							}
							if (IsTrue.userId == 0) {
							}else {
								String userindex = object.getString("userindex").toString();
								int a = Integer.parseInt(userindex);
								if (a<0) {
									head_num.setVisibility(View.VISIBLE);
									head_num.setText(userindex);
								}else if (a==0) {
									head_num.setVisibility(View.GONE);
								}else if (a>0) {
									head_num.setVisibility(View.VISIBLE);
									head_num.setText("+"+userindex);
								}
//								if (userindex.equals("0")) {
//									head_num.setVisibility(View.GONE);
//								}else {
//									head_num.setVisibility(View.VISIBLE);
//									head_num.setText("+"+userindex);
//								}
								String userimage = object.getString("userimage").toString();
								BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
								bitmapUtils.display(head, ImageAddress.Stringhead+userimage);
								String usercountindex = object.getString("usercountindex").toString();
								if (usercountindex.equals("0")) {
									head_num2.setVisibility(View.GONE);
								}else {
									head_num2.setVisibility(View.VISIBLE);
									head_num2.setText(usercountindex);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Banner();
					}

				});
	}

	protected void systemmsg(String sysxx,String num) {
		System.out.println(sysxx+">>>>>>>>>>>>>>>>>>>>");
		ArrayList<HashMap<String, Object>> listmaps = new ArrayList<HashMap<String,Object>>();
		try {
			JSONArray array = new JSONArray(sysxx);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("time", object.getString("time").toString());
				listmaps.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		BadgeView badgeView = new BadgeView(getActivity(), img3);
		badgeView.setText(num);
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView.show();
		time3.setText(listmaps.get(0).get("time").toString());
	}

	protected void news1(String news1) {
		try {
			JSONObject object = new JSONObject(news1);
			String classnews = object.getString("class").toString();
			String title = object.getString("title").toString();
			String time = object.getString("time").toString();
			url2 = object.getString("url").toString();
			text1.setText(title);
			time1.setText(time);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void news2(String news2) {
		try {
			JSONObject object = new JSONObject(news2);
			String classnews = object.getString("class").toString();
			String title = object.getString("title").toString();
			String time = object.getString("time").toString();
			url = object.getString("url").toString();
			text2.setText(title);
			time2.setText(time);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void Banner() {
		// ============初始化缓存路径，以及AfinalBitmap的初始化，可以忽略
		String cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File f = getActivity().getExternalCacheDir();
			if (null == f) {

				cacheDir = Environment.getExternalStorageDirectory().getPath()
						+ File.separator + getActivity().getPackageName()
						+ File.separator + "cache";
			} else {
				cacheDir = f.getPath();
			}
		} else {
			File f = getActivity().getCacheDir();
			cacheDir = f.getPath();
		}
		FinalBitmap.create(getActivity(), cacheDir + File.separator + "images"
				+ File.separator, 0.3f, 1024 * 1024 * 100, 10);
//		// 构造测试数据
//		Advertising ad1 = new Advertising(
//				"http://img.my.csdn.net/uploads/201312/14/1386989803_3335.PNG",
//				"http://blog.csdn.net/u011638883/article/details/17302293",
//				"双向搜索");
//		Advertising ad2 = new Advertising(
//				"http://img.my.csdn.net/uploads/201312/14/1386989613_6900.jpg",
//				"http://blog.csdn.net/u011638883/article/details/17245371",
//				"创意设计");
//		Advertising ad3 = new Advertising(
//				"http://img.my.csdn.net/uploads/201312/14/1386989802_7236.PNG",
//				"http://blog.csdn.net/u011638883/article/details/17248135",
//				"Artificial Intelligence");
//		Advertising ad4 = new Advertising(
//				"http://img.my.csdn.net/uploads/201312/14/1386989802_7236.PNG",
//				"http://blog.csdn.net/u011638883/article/details/17248135",
//				"Artificial Intelligence");
//		Advertising[] ads = { ad1, ad2, ad3, ad4 };
		Advertising[] adsss = new Advertising[listmap.size()];
		for (int i = 0; i < listmap.size(); i++) {
			Advertising advertising = new Advertising(ImageAddress.Banner+listmap.get(i).get("image"), "", listmap.get(i).get("title").toString());
			adsss[i] = advertising;
		}
		// 将AdGalleryHelper添加到布局文件中
		galleryContainer = (RelativeLayout) rootView
				.findViewById(R.id.mian_ideas_head);
		mGalleryHelper = new AdGalleryHelper(getActivity(), adsss, 5000);
		galleryContainer.addView(mGalleryHelper.getLayout());

		// /将AdGalleryHelper添加到布局文件中
		mGalleryHelper.startAutoSwitch();
	}

	static class ViewHolder {
		ImageView img;
		TextView title;
		TextView num;
		TextView time;
	}

	/**
	 * init
	 */
	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
		head = (CircleImageView) rootView.findViewById(R.id.head);
		head.setOnClickListener(this);
		head_num = (Button) rootView.findViewById(R.id.head_num);
		head_num2 = (TextView) rootView.findViewById(R.id.head_num2);
		head_search = (TextView) rootView.findViewById(R.id.head_search);
		head_search.setOnClickListener(this);
		search = (RelativeLayout) rootView.findViewById(R.id.search);
		search.setOnClickListener(this);
		search_img = (ImageView) rootView.findViewById(R.id.search_img);
		ideas_listview = (ListViewForScrollView) rootView
				.findViewById(R.id.ideas_listview);
		img1 = (ImageView) rootView.findViewById(R.id.mian_ideas_img1);
		title1 = (TextView) rootView.findViewById(R.id.mian_ideas_title1);
		text1 = (TextView) rootView.findViewById(R.id.mian_ideas_text1);
		time1 = (TextView) rootView.findViewById(R.id.mian_ideas_time1);
		img2 = (ImageView) rootView.findViewById(R.id.mian_ideas_img2);
		title2 = (TextView) rootView.findViewById(R.id.mian_ideas_title2);
		text2 = (TextView) rootView.findViewById(R.id.mian_ideas_text2);
		time2 = (TextView) rootView.findViewById(R.id.mian_ideas_time2);
		img3 = (ImageView) rootView.findViewById(R.id.mian_ideas_img3);
		mian_ideas_num = (RelativeLayout) rootView.findViewById(R.id.mian_ideas_num);
		mian_ideas_num2 = (ImageView) rootView.findViewById(R.id.mian_ideas_num2);
		title3 = (TextView) rootView.findViewById(R.id.mian_ideas_title3);
		text3 = (TextView) rootView.findViewById(R.id.mian_ideas_text3);
		time3 = (TextView) rootView.findViewById(R.id.mian_ideas_time3);
		news1 = (RelativeLayout) rootView.findViewById(R.id.mian_ideas_news1);
		news2 = (RelativeLayout) rootView.findViewById(R.id.mian_ideas_news2);
		news3 = (RelativeLayout) rootView.findViewById(R.id.mian_ideas_news3);
		news1.setOnClickListener(this);
		news2.setOnClickListener(this);
		news3.setOnClickListener(this);
		// head_page = (ViewPager) rootView.findViewById(R.id.head_page);
		// head_page.setOnClickListener(this);

		// mian_ideas_head = (RelativeLayout) rootView
		// .findViewById(R.id.mian_ideas_head);
		// mian_ideas_head.setOnClickListener(this);

		// img = (ViewFlow) rootView.findViewById(R.id.mian_ideas_img);
		// img.setOnClickListener(this);
		// guanggaotext = (TextView)
		// rootView.findViewById(R.id.mian_ideas_text);
		// guanggaotext.setOnClickListener(this);
		// dian = (CircleFlowIndicator) rootView
		// .findViewById(R.id.mian_ideas_dian);
		// dian.setOnClickListener(this);

		ideas_Ranking = (LinearLayout) rootView
				.findViewById(R.id.ideas_Ranking);
		ideas_Ranking.setOnClickListener(this);
		zhikufragment = (ZhikuFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.zhikufragment);
		zhikufragment.data();
		if (IsTrue.userId == 0) {
		}else {
			zhikufragment.hengdata();
		}
		// 不让listview加载在最上边
		ideas_vertical = (ScrollView) rootView
				.findViewById(R.id.ideas_vertical);
		ideas_vertical.smoothScrollTo(0, 0);
		delmsg = (Button) rootView.findViewById(R.id.delmsg);
		delmsg.setOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mGalleryHelper.stopAutoSwitch();
	}

	@Override
	public void onResume() {
		ideas_vertical.smoothScrollTo(0, 0);
		super.onResume();
	}

	// private void initBanner(ArrayList<String> imageUrlList) {
	//
	// mViewFlow.setAdapter(new ImagePagerAdapter(this, imageUrlList,
	// linkUrlArray, titleList).setInfiniteLoop(true));
	// mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
	// // 我的ImageAdapter实际图片张数为3
	//
	// mViewFlow.setFlowIndicator(mFlowIndicator);
	// mViewFlow.setTimeSpan(4500);
	// mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
	// mViewFlow.startAutoFlowTimer(); // 启动自动播放
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_search:// 搜索
			intent = new Intent(getActivity(), SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.ideas_Ranking:// 创意指数排行
			if (IsTrue.userId == 0) {
				intent = new Intent(getActivity(), HomeActivity.class);
				IsTrue.zhishu = 1;
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), IdeasRankingActivity.class);
				startActivity(intent);
			}
			break;
		// case R.id.mian_ideas_head://创意世界大赛
		// intent = new Intent(getActivity(), IdeasWorldGameActivity.class);
		// startActivity(intent);
		// break;
		case R.id.head:// 我的
			if (IsTrue.userId == 0) {
				return;
			}
			intent = new Intent(getActivity(), MyDetailsActivity.class);
			startActivity(intent);
			break;
		case R.id.mian_ideas_news1://热门新闻
			intent = new Intent(getActivity(),HotNews.class);
			intent.putExtra("class", 1+"");
			intent.putExtra("url2", url2);
			startActivity(intent);
			break;
		case R.id.mian_ideas_news2://佛学新闻
			intent = new Intent(getActivity(),HotNews.class);
			intent.putExtra("class", 2+"");
			intent.putExtra("url", url);
			startActivity(intent);
			break;
		case R.id.mian_ideas_news3://众创创
			intent = new Intent(getActivity(),XiTongMsg.class);
//			intent.putExtra("sysxx", sysxx);
			startActivity(intent);
			break;
		case R.id.delmsg://删除系统消息
			showdialogup();
			deletemag();
			break;
		default:
			break;
		}

	}

	private void deletemag() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("uid", IsTrue.userId+"");
		httpUtils.send(HttpMethod.POST, MyUrl.messagedel, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				jiazaidialog.dismiss();
				Toast.makeText(getActivity(), error.getExceptionCode()+":"+msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				jiazaidialog.dismiss();
				String list = responseInfo.result;
				try {
					JSONObject object = new JSONObject(list);
					String num = object.getString("num").toString();
					if (num.equals("1")) {
						
					}else if (num.equals("2")) {
						news3.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
