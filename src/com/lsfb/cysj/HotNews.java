package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lsbf.cysj.R;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HotNews extends FragmentActivity{
//	@ViewInject(R.id.hotnews)
//	private ListView hotnews;
	@ViewInject(R.id.hotnews_nes)
	private TextView hotnews_nes;
	@ViewInject(R.id.hotnews_back)
	private LinearLayout back;
	@ViewInject(R.id.hotnewsweb)
	private WebView web;
	BaseAdapter adapter;
	String classs = null;
	String url = null;
	Intent intent;
	HashMap<String, Object> map;
	ArrayList<HashMap<String, Object>> listmap;
	HttpUtils httpUtils;
	RequestParams params;
	BitmapUtils bitmapUtils;
	Dialog jiazaidialog;
	int count = 0;
//	private XListView hotnews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotnews);
		ViewUtils.inject(this);
		init();
//		showdialogup();
//		web.getSettings().setJavaScriptEnabled(true);
//		web.loadUrl(url);
		if(web != null)
        {
			web.setWebViewClient(new WebViewClient()
        	{
        		@Override
        		public void onPageFinished(WebView view,String url)
        		{
        			jiazaidialog.dismiss();
        		}
        	});
        	
        	loadUrl(url);
        }
//		adapter();
//		data();
//		hotnews.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//				}
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				if (firstVisibleItem == 0) {
//					LoadMoredata();
//				}
//				if (visibleItemCount + firstVisibleItem == totalItemCount) {
//				}
//			}
//		});
	}

	@SuppressLint("SetJavaScriptEnabled") private void loadUrl(String string) {
		if(web != null)
    	{
			web.getSettings().setJavaScriptEnabled(true);
			web.loadUrl(string);
//    		pd = ProgressDialog.show(this, "导购系统", "系统加载中...");
			showdialogup();
			web.reload();
    	}
	}

	protected void LoadMoredata() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("class", classs);
		params.addBodyParameter("page", ++count + "");
		httpUtils.send(HttpMethod.POST, MyUrl.newslist, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						jiazaidialog.dismiss();
						String lists = responseInfo.result;
						System.out.println(lists+"??????");
						try {
							JSONObject object2 = new JSONObject(lists);
							String num = object2.getString("num").toString();
							if (num.equals("1")) {
//								Toast.makeText(getApplicationContext(), "没有了亲",
//										Toast.LENGTH_SHORT).show();
							} else {
								String list = object2.getString("list")
										.toString();
								JSONArray array = new JSONArray(list);
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("title1", object
											.getString("title1").toString());
									map.put("image1", object
											.getString("image1").toString());
									map.put("url1", object.getString("url1")
											.toString());
									map.put("title2", object
											.getString("title2").toString());
									map.put("image2", object
											.getString("image2").toString());
									map.put("url2", object.getString("url2")
											.toString());
									map.put("title3", object
											.getString("title3").toString());
									map.put("image3", object
											.getString("image3").toString());
									map.put("url3", object.getString("url3")
											.toString());
									map.put("title4", object
											.getString("title4").toString());
									map.put("image4", object
											.getString("image4").toString());
									map.put("url4", object.getString("url4")
											.toString());
									map.put("time", object
											.getString("time").toString());
									listmap.add(0,map);
								}
//								adapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void adapter() {
		adapter = new BaseAdapter() {

			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				ViewHoldr holdr = null;
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.hotnews_itme, null);
					holdr = new ViewHoldr();
					holdr.img = (ImageView) view.findViewById(R.id.hotnews_img);
					holdr.newsimg1 = (ImageView) view
							.findViewById(R.id.hot_news_img1);
					holdr.newsimg2 = (ImageView) view
							.findViewById(R.id.hot_news_img2);
					holdr.newsimg3 = (ImageView) view
							.findViewById(R.id.hot_news_img3);
					holdr.text = (TextView) view
							.findViewById(R.id.hotnews_text);
					holdr.newstext1 = (TextView) view
							.findViewById(R.id.hot_news_text1);
					holdr.newstext2 = (TextView) view
							.findViewById(R.id.hot_news_text2);
					holdr.newstext3 = (TextView) view
							.findViewById(R.id.hot_news_text3);
					holdr.hot_news2 = (RelativeLayout) view.findViewById(R.id.hot_news2);
					holdr.hot_news3 = (RelativeLayout) view.findViewById(R.id.hot_news3);
					holdr.hot_news4 = (RelativeLayout) view.findViewById(R.id.hot_news4);
					holdr.hot_news_head = (LinearLayout) view.findViewById(R.id.hot_news_head);
					holdr.time = (TextView) view.findViewById(R.id.hot_news_time);
					view.setTag(holdr);
				} else {
					holdr = (ViewHoldr) view.getTag();
				}
				if (listmap.get(position).get("image1").toString().equals("0") && listmap.get(position).get("title1").toString().equals("null")) {
					holdr.hot_news_head.setVisibility(View.GONE);
				}
				if (listmap.get(position).get("image1").toString().equals("0")) {
					holdr.img.setVisibility(View.GONE);
				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					System.out.println(ImageAddress.news+ listmap.get(position).get("image1").toString());
					bitmapUtils.display(holdr.img, ImageAddress.news+ listmap.get(position).get("image1").toString());
					holdr.img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url1")
											.toString());
							startActivity(intent);
						}
					});
				}
				if (listmap.get(position).get("title1").toString().equals("null")) {
					holdr.text.setVisibility(View.GONE);
				} else {
					holdr.text.setText("    "
							+ listmap.get(position).get("title1").toString());
					holdr.text.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url1")
											.toString());
							startActivity(intent);
						}
					});
				}
				if (listmap.get(position).get("image2").toString().equals("0")) {
					holdr.img.setVisibility(View.GONE);
				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holdr.newsimg1, ImageAddress.news
							+ listmap.get(position).get("image2").toString());
					holdr.newsimg1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url2")
											.toString());
							startActivity(intent);
						}
					});
				}
				if (listmap.get(position).get("image3").toString().equals("0")) {
					holdr.img.setVisibility(View.GONE);
				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holdr.newsimg2, ImageAddress.news
							+ listmap.get(position).get("image3").toString());
					holdr.newsimg2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url3")
											.toString());
							startActivity(intent);
						}
					});
				}
				if (listmap.get(position).get("image4").toString().equals("0")) {
					holdr.img.setVisibility(View.GONE);
				} else {
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holdr.newsimg3, ImageAddress.news
							+ listmap.get(position).get("image4").toString());
					holdr.newsimg3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url4")
											.toString());
							startActivity(intent);
						}
					});
				}
				if (listmap.get(position).get("title2").toString()
						.equals("null")) {
					holdr.newstext1.setVisibility(View.GONE);
//					holdr.hot_news2.setVisibility(View.GONE);
				} else {
					holdr.newstext1.setText(listmap.get(position).get("title2")
							.toString());
					holdr.newstext1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url2")
											.toString());
							startActivity(intent);
						}
					});
				}
				if (listmap.get(position).get("title3").toString()
						.equals("null")) {
					holdr.newstext2.setVisibility(View.GONE);
//					holdr.hot_news3.setVisibility(View.GONE);
				} else {
					holdr.newstext2.setText(listmap.get(position).get("title3")
							.toString());
					holdr.newstext2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url3")
											.toString());
							startActivity(intent);
						}
					});
				}
				if (listmap.get(position).get("title4").toString()
						.equals("null")) {
					holdr.newstext3.setVisibility(View.GONE);
//					holdr.hot_news4.setVisibility(View.GONE);
				} else {
					holdr.newstext3.setText(listmap.get(position).get("title4")
							.toString());
					holdr.newstext3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(HotNews.this,
									XinWenActivity.class);
							intent.putExtra("url",
									listmap.get(position).get("url4")
											.toString());
							startActivity(intent);
						}
					});
				}
				holdr.time.setText(listmap.get(position).get("time")
							.toString());
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public int getCount() {
				return listmap.size();
			}
		};
//		hotnews.setAdapter(adapter);
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void data() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("class", classs);
		httpUtils.send(HttpMethod.POST, MyUrl.news, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						jiazaidialog.dismiss();
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						jiazaidialog.dismiss();
						String lists = responseInfo.result;
						try {
							JSONObject object2 = new JSONObject(lists);
							String num = object2.getString("num").toString();
							if (num.equals("1")) {
							} else {
								String list = object2.getString("list")
										.toString();
								JSONArray array = new JSONArray(list);
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = (JSONObject) array
											.get(i);
									map = new HashMap<String, Object>();
									map.put("title1", object
											.getString("title1").toString());
									map.put("image1", object
											.getString("image1").toString());
									map.put("url1", object.getString("url1")
											.toString());
									map.put("title2", object
											.getString("title2").toString());
									map.put("image2", object
											.getString("image2").toString());
									map.put("url2", object.getString("url2")
											.toString());
									map.put("title3", object
											.getString("title3").toString());
									map.put("image3", object
											.getString("image3").toString());
									map.put("url3", object.getString("url3")
											.toString());
									map.put("title4", object
											.getString("title4").toString());
									map.put("image4", object
											.getString("image4").toString());
									map.put("url4", object.getString("url4")
											.toString());
									map.put("time", object
											.getString("time").toString());
									listmap.add(0,map);
								}
								adapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	static class ViewHoldr {
		ImageView img, newsimg1, newsimg2, newsimg3;
		TextView text, newstext1, newstext2, newstext3,time;
		RelativeLayout hot_news2, hot_news3, hot_news4;
		LinearLayout hot_news_head;
	}

	private void init() {
		map = new HashMap<String, Object>();
		listmap = new ArrayList<HashMap<String, Object>>();
//		hotnews = (XListView) findViewById(R.id.hotnews);
//		hotnews.setPullLoadEnable(true);
//		hotnews.setXListViewListener(this);
		intent = getIntent();
		classs = intent.getExtras().getString("class");
		if (classs.equals("1")) {
			url = intent.getExtras().getString("url2");
		}else if (classs.equals("2")) {
			url = intent.getExtras().getString("url");
		}
		if (classs.equals("1")) {
			hotnews_nes.setText("热门新闻");
		} else {
			hotnews_nes.setText("佛学新闻");
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
//	private void onLoad() {
//		hotnews.stopRefresh();
//		hotnews.stopLoadMore();
//		hotnews.setRefreshTime("刚刚");
//	}
//	@Override
//	public void onRefresh() {
////		adapter.notifyDataSetChanged();
//		LoadMoredata();
//		onLoad();
//		System.out.println(listmap+"SSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//		adapter.notifyDataSetChanged();
//	}
//
//	@Override
//	public void onLoadMore() {
//		count = 0;
//		listmap = new ArrayList<HashMap<String,Object>>();
//		data();
//		onLoad();
//		adapter.notifyDataSetChanged();
//	}
}
