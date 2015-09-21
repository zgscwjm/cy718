package com.lsfb.cysj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.MeditationDialog;
import com.lsfb.cysj.Dialog.MyDialog;
import com.lsfb.cysj.Dialog.QiFuialog;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.service.MusicService;
import com.lsfb.cysj.view.ResDialog;

import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * 禅修
 * 
 * @author Administrator
 * 
 */
public class MeditationActivity extends FragmentActivity implements
		OnClickListener {
	ImageView meditation_back;
	LinearLayout ll_meditation_xiang;// 上香背景图
	Button btn_meditation_xiang;// 上香按钮
	/**
	 * meditation_gongde gongde 功德簿
	 */
	@ViewInject(R.id.meditation_gongde)
	private LinearLayout gongde;
	/**
	 * btn_meditation_qifu qifu 供奉祈福
	 */
	@ViewInject(R.id.btn_meditation_qifu)
	private Button qifu;
	/**
	 * btn_meditation_songjing songjing 诵经
	 */
	@ViewInject(R.id.btn_meditation_songjing)
	private Button songjing;

	/**
	 * 烟飘
	 */
	@ViewInject(R.id.imgYan)
	private ImageView imgYan;
	
	boolean workFlag;
	int currIndex=0;

	// /**
	// * 姓名 name1 meditation_name1
	// */
	// @ViewInject(R.id.meditation_name1)
	// private Button name1;
	// /**
	// * 姓名 name2 meditation_name2
	// */
	// @ViewInject(R.id.meditation_name2)
	// private Button name2;
	// /**
	// * 姓名 name3 meditation_name3
	// */
	// @ViewInject(R.id.meditation_name3)
	// private Button name3;
	// /**
	// * 姓名 name4 meditation_name4
	// */
	// @ViewInject(R.id.meditation_name4)
	// private Button name4;
	// /**
	// * 姓名 name5 meditation_name5
	// */
	// @ViewInject(R.id.meditation_name5)
	// private Button name5;
	// /**
	// * 姓名 name6 meditation_name6
	// */
	// @ViewInject(R.id.meditation_name6)
	// private Button name6;
	// /**
	// * 姓名 name7 meditation_name7
	// */
	// @ViewInject(R.id.meditation_name7)
	// private Button name7;
	// /**
	// * 姓名 name8 meditation_name8
	// */
	// @ViewInject(R.id.meditation_name8)
	// private Button name8;
	// /**
	// * 姓名 name9 meditation_name9
	// */
	// @ViewInject(R.id.meditation_name9)
	// private Button name9;
	// /**
	// * 姓名 name10 meditation_name10
	// */
	// @ViewInject(R.id.meditation_name10)
	// private Button name10;
	/**
	 * meditation_pai1 pai1 牌位
	 */
	@ViewInject(R.id.meditation_pai1)
	private LinearLayout pai1;
	/**
	 * meditation_pai2 pai2 牌位
	 */
	@ViewInject(R.id.meditation_pai2)
	private LinearLayout pai2;
	
	AsyncHttpClient client;
	RequestParams params;
	Intent intent;
	int length;// 返回的数值长度
	Dialog dialog;
	Dialog jiazaidialog;
	LinearLayout layout;// 动态添加布局
	LinearLayout layout2;// 动态添加布局
	Button btnpai;// 牌位
	int pwidth;// 窗体宽度
	
	public static  final int YANPIAO_START=1009;

	// 烟飘
	int[] bitmapId = new int[] { R.drawable.yan1, R.drawable.yan2,
			R.drawable.yan3, R.drawable.yan4, R.drawable.yan5, R.drawable.yan6,
			R.drawable.yan7, R.drawable.yan8, R.drawable.yan9,
			R.drawable.yan11, R.drawable.yan12, R.drawable.yan13,
			R.drawable.yan14, R.drawable.yan15 };
	
	
	public  Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case YANPIAO_START:
				imgYan.setBackgroundResource(bitmapId[currIndex]);
				
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meditation);
		ViewUtils.inject(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		pwidth = dm.widthPixels;
		init();
		data();
		meditation_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_meditation_xiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IsTrue.userId == 0) {
					intent = new Intent(MeditationActivity.this,
							HomeActivity.class);
					IsTrue.tabnum = 4;
					IsTrue.exit = true;
					startActivity(intent);
					return;
				}
				IsTrue.shangxiang = true;
				shangxiang();
			}
		});
	}

	protected void shangxiang() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId + "");
		if (IsTrue.shangxiang == true) {
			params.put("bid", 1 + "");
		} else {
			params.put("bid", 2 + "");
		}
		client.post(MyUrl.chanting, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num").toString();
					int i = Integer.parseInt(num);
					switch (i) {
					case 1:
						if (IsTrue.shangxiang == true) {
							Toast.makeText(getApplicationContext(), "上香失败",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "诵经失败",
									Toast.LENGTH_SHORT).show();
						}
						break;
					case 2:
						if (IsTrue.shangxiang == true) {
							Toast.makeText(getApplicationContext(), "上香成功",
									Toast.LENGTH_SHORT).show();
							ll_meditation_xiang
									.setBackgroundResource(R.drawable.a7);
							
							yanPiao();

						} else {
							Toast.makeText(getApplicationContext(), "诵经成功",
									Toast.LENGTH_SHORT).show();
						}
						break;
					case 3:
						Toast.makeText(getApplicationContext(), "创币不足",
								Toast.LENGTH_SHORT).show();
						break;
					case 4:
						Toast.makeText(getApplicationContext(), "没有创币",
								Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(), "请求错误",
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
	
	/**
	 * 烟飘
	 */
	void yanPiao(){
		  workFlag=true;
		
		 new Thread(){   
			  
             //重写run方法   
             public void run(){ 
                     // TODO Auto-generated method stub   
                     while(workFlag)//一直执行这个循环(死循环)   
                     {   
//                             currIndex = (currIndex+1)%bitmapId.length;//更改图片的ID   
                             
                             if(currIndex<13){//                            	 
                            	handler.sendEmptyMessage(YANPIAO_START);
                            	 currIndex++; 
                             }else{
                            	 currIndex=0;
                             }                             
 
                             try 
                             {   
                                   Thread.sleep(500);//到此处暂停3秒钟,然后继续执行run函数,即实现每隔3秒钟刷新屏幕一次   
                             }  
                             catch (InterruptedException e)    
                             {   
                                     // TODO Auto-generated catch block   
                                     e.printStackTrace();   
                             }  
                     }  
             }  
     }.start();  
} 
  
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		workFlag=false;
	}

	private void showdialog() {
		// LayoutInflater inflater = LayoutInflater.from(this);
		// View v = inflater.inflate(R.layout.dialogview, null);
		//
		// LinearLayout layout = (LinearLayout)
		// v.findViewById(R.id.dialog_view);
		//
		// // main.xml中的ImageView
		// ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		// // 加载动画
		// Animation hyperspaceJumpAnimation =
		// AnimationUtils.loadAnimation(this,
		// R.anim.animation);
		// // 使用ImageView显示动画
		// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		//
		// jiazaidialog = new Dialog(this,
		// R.style.FullHeightDialog);
		// jiazaidialog.setCancelable(true);
		// jiazaidialog.show();
		// jiazaidialog.setCanceledOnTouchOutside(false);
		// jiazaidialog.setContentView(layout, new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT));
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void data() {
		showdialog();
		requestjson();

	}

	private void requestjson() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId + "");
		client.post(MyUrl.StringMeditation, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							String num = response.getString("num");
							int i = Integer.parseInt(num);
							if (i == 2) {
								String lists = response.getString("list");
								JSONArray array = new JSONArray(lists);
								length = array.length();
								int one = length - 5;
								if (one <= 0) {
									for (int j = 0; j < length; j++) {
										JSONObject object = (JSONObject) array
												.get(j);
										pai1.setOrientation(LinearLayout.HORIZONTAL);
										layout = new LinearLayout(
												getApplicationContext());
										layout.setLayoutParams(new LinearLayout.LayoutParams(
												pwidth / 5,
												LayoutParams.FILL_PARENT));
										btnpai = new Button(
												getApplicationContext());
										btnpai.setBackgroundResource(R.drawable.bitmpbg);
										String name = object.getString("fname")
												.toString();
										int namelength = name.length();
										if (namelength == 1) {
											btnpai.setTextSize(12);
											btnpai.setText(name);
										} else if (namelength == 2) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b);
										} else if (namelength == 3) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											String c = name.substring(2, 3);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b + "\n"
													+ c);
										} else if (namelength == 4) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											String c = name.substring(2, 3);
											String d = name.substring(3, 4);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b + "\n"
													+ c + "\n" + d);
										}
										layout.addView(btnpai);
										pai1.addView(layout);
									}
								} else {
									for (int j = 0; j < 5; j++) {
										JSONObject object = (JSONObject) array
												.get(j);
										pai1.setOrientation(LinearLayout.HORIZONTAL);
										layout = new LinearLayout(
												getApplicationContext());
										layout.setLayoutParams(new LinearLayout.LayoutParams(
												pwidth / 5,
												LayoutParams.FILL_PARENT));
										btnpai = new Button(
												getApplicationContext());
										btnpai.setBackgroundResource(R.drawable.bitmpbg);
										String name = object.getString("fname")
												.toString();
										int namelength = name.length();
										if (namelength == 1) {
											btnpai.setTextSize(12);
											btnpai.setText(name);
										} else if (namelength == 2) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b);
										} else if (namelength == 3) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											String c = name.substring(2, 3);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b + "\n"
													+ c);
										} else if (namelength == 4) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											String c = name.substring(2, 3);
											String d = name.substring(3, 4);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b + "\n"
													+ c + "\n" + d);
										}
										layout.addView(btnpai);
										pai1.addView(layout);
									}
									for (int j = 5; j < length; j++) {
										JSONObject object = (JSONObject) array
												.get(j);
										pai2.setOrientation(LinearLayout.HORIZONTAL);
										layout = new LinearLayout(
												getApplicationContext());
										layout.setLayoutParams(new LinearLayout.LayoutParams(
												pwidth / 5,
												LayoutParams.FILL_PARENT));
										btnpai = new Button(
												getApplicationContext());
										btnpai.setBackgroundResource(R.drawable.bitmpbg);
										String name = object.getString("fname")
												.toString();
										int namelength = name.length();
										if (namelength == 1) {
											btnpai.setTextSize(12);
											btnpai.setText(name);
										} else if (namelength == 2) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b);
										} else if (namelength == 3) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											String c = name.substring(2, 3);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b + "\n"
													+ c);
										} else if (namelength == 4) {
											String a = name.substring(0, 1);
											String b = name.substring(1, 2);
											String c = name.substring(2, 3);
											String d = name.substring(3, 4);
											btnpai.setTextSize(12);
											btnpai.setText(a + "\n" + b + "\n"
													+ c + "\n" + d);
										}
										layout.addView(btnpai);
										pai2.addView(layout);
									}
								}
							} else if (i == 1) {
								Toast.makeText(getApplicationContext(), "还未有人",
										Toast.LENGTH_SHORT).show();
								length = 1;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
						jiazaidialog.dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						Toast.makeText(getApplicationContext(), "请求错误",
								Toast.LENGTH_SHORT).show();
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
				});
	}

	private void init() {
		meditation_back = (ImageView) findViewById(R.id.meditation_back);
		ll_meditation_xiang = (LinearLayout) findViewById(R.id.ll_meditation_xiang);
		btn_meditation_xiang = (Button) findViewById(R.id.btn_meditation_xiang);
		// imgYan= (ImageView) findViewById(R.id.imgYan);

		gongde.setOnClickListener(this);
		qifu.setOnClickListener(this);
		songjing.setOnClickListener(this);
		pai1.setOnClickListener(this);
		pai2.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		intent = new Intent(MeditationActivity.this, MusicService.class);
		stopService(intent);
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if (IsTrue.exit == false && IsTrue.search == true) {
		// finish();
		// }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.meditation_gongde:// 功德簿
			showDialog();
			break;
		case R.id.btn_meditation_qifu:// 祈福
			// if (IsTrue.userId == 0) {
			// intent = new Intent(MeditationActivity.this,HomeActivity.class);
			// IsTrue.tabnum = 4;
			// IsTrue.exit = true;
			// startActivity(intent);
			// break;
			// }
			showDialog2();
			break;
		case R.id.btn_meditation_songjing:// 诵经
			// if (IsTrue.userId == 0) {
			// intent = new Intent(MeditationActivity.this,HomeActivity.class);
			// IsTrue.tabnum = 4;
			// IsTrue.exit = true;
			// startActivity(intent);
			// break;
			// }
			intent = new Intent(MeditationActivity.this, MusicService.class);
			startService(intent);
			IsTrue.shangxiang = false;
			shangxiang();
			break;
		default:
			break;
		}
	}

	private void showDialog2() {
		dialog = new QiFuialog(this, R.style.selectorDialog,
				new QiFuialog.PriorityListener() {

					@Override
					public void refreshPriorityUI(String string) {
						if (IsTrue.qifuchange == true) {
							int two = length - 5;
							Log.d("dialog", "" + two);
							if (two <= 0) {
								pai1.setOrientation(LinearLayout.HORIZONTAL);
								layout = new LinearLayout(
										getApplicationContext());
								layout.setLayoutParams(new LinearLayout.LayoutParams(
										pwidth / 5, LayoutParams.FILL_PARENT));
								btnpai = new Button(getApplicationContext());
								btnpai.setBackgroundResource(R.drawable.bitmpbg);
								String name = string.toString();
								int namelength = name.length();
								if (namelength == 1) {
									btnpai.setTextSize(12);
									btnpai.setText(name);
								} else if (namelength == 2) {
									String a = name.substring(0, 1);
									String b = name.substring(1, 2);
									btnpai.setTextSize(12);
									btnpai.setText(a + "\n" + b);
								} else if (namelength == 3) {
									String a = name.substring(0, 1);
									String b = name.substring(1, 2);
									String c = name.substring(2, 3);
									btnpai.setTextSize(12);
									btnpai.setText(a + "\n" + b + "\n" + c);
								} else if (namelength == 4) {
									String a = name.substring(0, 1);
									String b = name.substring(1, 2);
									String c = name.substring(2, 3);
									String d = name.substring(3, 4);
									btnpai.setTextSize(12);
									Log.d("abcd", "" + a + "\n" + b + "\n" + c
											+ "\n" + d);
									btnpai.setText(a + "\n" + b + "\n" + c
											+ "\n" + d);
								}
								layout.addView(btnpai);
								pai1.addView(layout);
								length++;
							} else if (two > 0) {
								pai2.setOrientation(LinearLayout.HORIZONTAL);
								layout = new LinearLayout(
										getApplicationContext());
								layout.setLayoutParams(new LinearLayout.LayoutParams(
										pwidth / 5, LayoutParams.FILL_PARENT));
								btnpai = new Button(getApplicationContext());
								btnpai.setBackgroundResource(R.drawable.bitmpbg);
								String name = string.toString();
								int namelength = name.length();
								if (namelength == 1) {
									btnpai.setTextSize(12);
									btnpai.setText(name);
								} else if (namelength == 2) {
									String a = name.substring(0, 1);
									String b = name.substring(1, 2);
									btnpai.setTextSize(12);
									btnpai.setText(a + "\n" + b);
								} else if (namelength == 3) {
									String a = name.substring(0, 1);
									String b = name.substring(1, 2);
									String c = name.substring(2, 3);
									btnpai.setTextSize(12);
									btnpai.setText(a + "\n" + b + "\n" + c);
								}
								layout.addView(btnpai);
								pai2.addView(layout);
								length++;
							}
						} else {
							pai1.removeAllViews();
							pai2.removeAllViews();
							showdialog();
							requestjson();
						}
					}
				});
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}

	private void showDialog() {
		dialog = new MeditationDialog(this, R.style.selectorDialog);
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
	}

}
