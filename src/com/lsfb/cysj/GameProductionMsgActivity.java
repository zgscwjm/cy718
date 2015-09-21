package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.lsfb.cysj.Dialog.JuBaoDialog;
import com.lsfb.cysj.Dialog.PingLunDialog;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.utils.Utils;
import com.lsfb.cysj.view.HorizontalListView;
import com.lsfb.cysj.view.ListViewForScrollView;
import com.lsfb.cysj.view.MyGridView;
import com.lsfb.cysj.view.ResDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.instagram.controller.UMInstagramHandler;
import com.umeng.socialize.instagram.media.InstagramShareContent;
import com.umeng.socialize.laiwang.controller.UMLWHandler;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.yixin.controller.UMYXHandler;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;

/**
 * 参赛作品详情,举报
 * @author Administrator
 *
 */
public class GameProductionMsgActivity extends FragmentActivity implements
		OnClickListener {
	@ViewInject(R.id.game_production_msg_backing)
	private ImageButton back;
	/**
	 * game_production_msg_imghead imgehead 发布会员头像
	 */
	@ViewInject(R.id.game_production_msg_imghead)
	private ImageView imgehead;
	/**
	 * game_production_msg_name fabuname 发布会员名称
	 */
	@ViewInject(R.id.game_production_msg_name)
	private TextView fabuname;
	/**
	 * game_production_msg_zhishu fabuzhishu 发布指数
	 */
	@ViewInject(R.id.game_production_msg_zhishu)
	private TextView fabuzhishu;
	/**
	 * game_production_msg_type fabutitle 创意题目
	 */
	@ViewInject(R.id.game_production_msg_type)
	private TextView fabutitle;
	/**
	 * game_production_msg_content fabucontent 发布创意内容
	 */
	@ViewInject(R.id.game_production_msg_content)
	private TextView fabucontent;
	/**
	 * game_production_msg_mg mg 多个图片
	 */
	// @ViewInject(R.id.game_production_msg_mg)
	// private MyGridView mg;
	/**
	 * game_production_msg_video fabuvideo 发布的视频
	 */
	@ViewInject(R.id.game_production_msg_video)
	private VideoView fabuvideo;
	/**
	 * game_production_msg_delete delete 删除
	 */
	// @ViewInject(R.id.game_production_msg_delete)
	// private TextView delete;
	/**
	 * game_production_msg_pinglun pinglun 评论
	 */
	@ViewInject(R.id.game_production_msg_pinglun)
	private LinearLayout pinglun;
	/**
	 * game_production_msg_pinglun_img pinglunimg 评论图像
	 */
	@ViewInject(R.id.game_production_msg_pinglun_img)
	private ImageView pinglunimg;
	/**
	 * game_production_msg_pingluntext pinglunnum 评论数量
	 */
	@ViewInject(R.id.game_production_msg_pingluntext)
	private TextView pinglunnum;
	/**
	 * game_production_msg_likeall like 喜欢
	 */
	@ViewInject(R.id.game_production_msg_likeall)
	private LinearLayout like;
	/**
	 * game_production_msg_like likeimg 喜欢图标
	 */
	@ViewInject(R.id.game_production_msg_like)
	private ImageView likeimg;
	/**
	 * game_production_msg_liketext liketext 喜欢内容
	 */
	@ViewInject(R.id.game_production_msg_liketext)
	private TextView liketext;
	/**
	 * game_production_msg_touxiang touxiang 多个头像
	 */
	@ViewInject(R.id.game_production_msg_touxiang)
	private LinearLayout touxiang;
	/**
	 * game_production_msg_share share 分享
	 */
	@ViewInject(R.id.game_production_msg_share)
	private LinearLayout share;
	@ViewInject(R.id.game_production_msg_shareimg)
	private ImageView shareimg;
	/**
	 * game_production_msg_down_xing down_xing 收藏
	 */
	@ViewInject(R.id.game_production_msg_down_xing)
	private LinearLayout down_xing;
	
	
	@ViewInject(R.id.game_production_msg_jubao)
	private LinearLayout jubao;
	
	/**
	 * game_production_msg_down_xingimg down_xingxing 五星
	 */
	@ViewInject(R.id.game_production_msg_down_xingimg)
	private ImageView down_xingxing;
	/**
	 * game_production_msg_down_pinglun downpinglun 底部评论
	 */
	@ViewInject(R.id.game_production_msg_down_pinglun)
	private LinearLayout downpinglun;
	/**
	 * game_production_msg_down_zan down_zan 底部赞
	 */
	@ViewInject(R.id.game_production_msg_down_zan)
	private LinearLayout down_zan;
	/**
	 * game_production_msg_down_zan_img zan_img 底部赞图片
	 */
	@ViewInject(R.id.game_production_msg_down_zan_img)
	private ImageView zan_img;
	/**
	 * game_production_msg_duogeimg duogeimg 横着的头像
	 */
	// @ViewInject(R.id.game_production_msg_duogeimg)
	// private HorizontalScrollView duogeimg;
	@ViewInject(R.id.game_production_msg_xian)
	private View xian;
	@ViewInject(R.id.game_production_msg_xian1)
	private View xian1;
	@ViewInject(R.id.game_production_msg_xian2)
	private View xian2;
	@ViewInject(R.id.game_production_msg_xian3)
	private View xian3;
	@ViewInject(R.id.game_production_msg_xian4)
	private View xian4;
	/**
	 * game_production_msg_btn1 btn1 冠軍
	 */
	@ViewInject(R.id.game_production_msg_btn1)
	private Button btn1;
	/**
	 * game_production_msg_btn2 btn2 冠軍
	 */
	@ViewInject(R.id.game_production_msg_btn2)
	private Button btn2;
	Intent intent;
	String zuopinid;// 创意作品id
	String zid; // 作品id
	
	String sid;//大赛id
	/**
	 * title:创意题目 content:创意内容(0无内容) images:多图(0无图片) video:视频(0无视频) price:创意价值
	 * sc:会员收藏数量 index 发布会员指数 count:评论数量 nickid:发布会员 iddelid:2可删|1不可删
	 * nickname:发布会员昵称 uimage:发布会员头像(0会员未设置头像) zhishu:发布会员总指数 id：创意id
	 * zan:1已赞|0未赞 scpd:1已收藏|0未收藏
	 */
	String title, content, images, video, price, sc, index, count, nickid,
			delid, nickname, uimage, zhishu, id, zan, scpd;
	/**
	 * numsc:2(有值存在) listsc:二维数组 nimage:收藏会员头像 zhishu2:收藏会员指数 memid:收藏会员id
	 * numsc:1(无值存在)评论列表有值存在 num:2(有值存在) list:二维数组 rtime:时间 wid:评论id
	 * content2:评论内容 mid:会员id image:会员头像 nickname2:会员昵称 zhishu3:会员总指数
	 * mdel:1(当前登录会员评论的信息)|0(其他会员评论的信息) 注:mdel主要用于删除自己的评论
	 */
	String nimage, zhishu2, memid, numsc, num, rtime, wid, content2, mid,
			image, nickname2, zhishu3, mdel;
	HttpUtils httpUtils;
	RequestParams params;
	Dialog jiazaidialog;
	BitmapUtils bitmapUtils;
	BaseAdapter adapter;
	BaseAdapter adapterlist;// 横着的头像
	BaseAdapter baseadapter;// 评论用的adapter
	BaseAdapter image_GridView;// 内容区域展示图片的adapter
	HashMap<String, Object> map;
	ArrayList<Map<String, Object>> listmap;
	Map<String, Object> map2;// 下面评论
	List<Map<String, Object>> listmap2;// 下面评论
	List<Map<String, Object>> pinglunlist;
	private HorizontalListView duogeimg;// 横着的头像
	private ListViewForScrollView listview_comments;// 评论列表
	String jiazhilist;// 进入页面显示是否有评论
	int numcount = 0;
	private MyGridView mg;
	int zani = 0;
	/**
	 * 2(比赛已结束不能支持创币)|1(比赛未结束能支持创币) bitmember 比赛发起人id(根据bitbs判断能点击不)
	 */
	String bitbs, bitmember;
	int guanjun = 0;
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_production_msg);
		ViewUtils.inject(this);
		init();
		showdialogup();
		data();
	}

	private void data() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("sid", zuopinid);
		params.addBodyParameter("uid", IsTrue.userId + "");
		httpUtils.send(HttpMethod.POST, MyUrl.worksinger, params,
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
						String list = responseInfo.result;
						System.out.println(list);
						try {
							JSONObject object = new JSONObject(list);
							title = object.getString("title").toString();
							content = object.getString("content").toString();
							images = object.getString("images").toString();
							video = object.getString("video").toString();
							sc = object.getString("sc").toString();
							index = object.getString("index").toString();
							count = object.getString("count").toString();
							nickid = object.getString("nickid").toString();
							nickname = object.getString("nickname").toString();
							uimage = object.getString("uimage").toString();
							id = object.getString("id").toString();
							scpd = object.getString("scpd").toString();
							bitbs = object.getString("bitbs").toString();
							
							sid = object.getString("sid").toString();
							
							
							if (bitbs.equals("1")) {
								bitmember = object.getString("bitmember")
										.toString();
								if (Integer.parseInt(bitmember) == IsTrue.userId) {
									btn2.setVisibility(View.VISIBLE);
									btn1.setVisibility(View.GONE);
								} else {
									btn1.setVisibility(View.VISIBLE);
									btn2.setVisibility(View.GONE);
								}
							} else {
								btn1.setVisibility(View.VISIBLE);
								btn2.setVisibility(View.GONE);
							}
							zan = object.getString("zan").toString();
							zani = Integer.parseInt(zan);
							if (zani == 1) {
								zan_img.setBackgroundResource(R.drawable.zan_ed);
							} else if (zani == 0) {
								zan_img.setBackgroundResource(R.drawable.zan);
							}
							numsc = object.getString("numsc").toString();
							if (uimage.equals("0")) {
								imgehead.setBackgroundResource(R.drawable.logo);
							} else {
								bitmapUtils = new BitmapUtils(
										getApplicationContext());
								bitmapUtils.display(imgehead,
										ImageAddress.Stringhead + uimage);
							}
							numcount = Integer.parseInt(count);
							if (numcount == 0) {
								pinglunimg
										.setBackgroundResource(R.drawable.pinglun);
								pinglunnum.setText(count);
							} else {
								pinglunimg
										.setBackgroundResource(R.drawable.pinglun_ed);
								pinglunnum.setText(count);
							}
							fabuname.setText(nickname);
							fabuzhishu.setText(index);
							fabutitle.setText(title);
							fabucontent.setText(content);
							if (Integer.parseInt(sc) >= 1) {
								likeimg.setBackgroundResource(R.drawable.like_ed_shixin);
							}
							liketext.setText(sc);
							pinglunnum.setText(count);
							if (scpd.equals("1")) {
								down_xingxing
										.setBackgroundResource(R.drawable.shoucang_ed);
							}
							if (numsc.equals("2")) {
								xian.setVisibility(View.VISIBLE);
								xian1.setVisibility(View.VISIBLE);
								touxiang.setVisibility(View.VISIBLE);
								String listsc = object.getString("listsc");
								duogeimgs(listsc);
							} else if (numsc.equals("1")) {

							}
							num = object.getString("num").toString();
							if (num.equals("2")) {
								xian2.setVisibility(View.VISIBLE);
								xian3.setVisibility(View.VISIBLE);
								xian4.setVisibility(View.VISIBLE);
								listview_comments.setVisibility(View.VISIBLE);
								jiazhilist = object.getString("list");
								xianshipinglun();
							} else if (num.equals("1")) {

							}
							duotuimg(images);
							shipin(video);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	protected void shipin(String videos) {
		if (videos.equals("0")) {

		} else {
			fabuvideo.setVisibility(View.VISIBLE);
			fabuvideo.setMediaController(new MediaController(
					GameProductionMsgActivity.this));
			Uri uri = Uri.parse(ImageAddress.cbit + video);
			fabuvideo.setVideoURI(uri);
			fabuvideo.requestFocus();
		}
	}

	protected void duotuimg(String string) {
		if (string.equals("0")) {

		} else {
			mg.setVisibility(View.VISIBLE);
			final String imgString = string;
			final String[] splitimages = imgString.split(",");
			mg.setSelector(new ColorDrawable(Color.TRANSPARENT));
			image_GridView = new BaseAdapter() {

				@Override
				public View getView(final int position, View v, ViewGroup arg2) {
					ViewHolder holder = null;
					if (v == null) {
						holder = new ViewHolder();
						// 可以理解为从vlist获取view 之后把view返回给ListView
						v = LayoutInflater.from(GameProductionMsgActivity.this)
								.inflate(R.layout.iamge_nine, null);
						holder.img = (ImageView) v
								.findViewById(R.id.moreimages);
						v.setTag(holder);
					} else {
						holder = (ViewHolder) v.getTag();
					}
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.img, ImageAddress.cbit
							+ splitimages[position]);
					holder.img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(),
									GalleryImageActivity.class);
							intent.putExtra("imagekan", imgString);
							IsTrue.kantu = 1;
							startActivity(intent);
						}
					});
					return v;

				}

				@Override
				public long getItemId(int i) {
					return 0;
				}

				@Override
				public Object getItem(int i) {
					return null;
				}

				@Override
				public int getCount() {
					return splitimages.length;
				}
			};
			mg.setAdapter(image_GridView);
		}
	}

	// 显示评论
	protected void xianshipinglun() {
		try {
			JSONArray array = new JSONArray(jiazhilist);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map2 = new HashMap<String, Object>();
				map2.put("rtime", object.getString("rtime").toString());
				map2.put("wid", object.getString("wid").toString());
				map2.put("content", object.getString("content").toString());
				map2.put("mid", object.getString("mid").toString());
				map2.put("image", object.getString("image").toString());
				map2.put("nickname", object.getString("nickname").toString());
				map2.put("zhishu", object.getString("zhishu").toString());
				map2.put("mdel", object.getString("mdel").toString());
				listmap2.add(map2);
			}
			baseadapter = new BaseAdapter() {
				@Override
				public View getView(final int i, View v, ViewGroup arg2) {
					ViewHolder holder = null;
					if (v == null) {
						// ImageView pinglunimg;//评论头像
						// TextView pinglunzhishu;//评论指数
						// TextView pinglunname;//评论姓名
						// TextView pingluntext;//评论内容
						// TextView pingluntime;//评论时间
						// TextView pinglundel;//评论删除
						holder = new ViewHolder();
						// 可以理解为从vlist获取view 之后把view返回给ListView
						v = LayoutInflater.from(GameProductionMsgActivity.this)
								.inflate(R.layout.creativedetails_comments,
										null);
						holder.pinglunimg = (ImageView) v
								.findViewById(R.id.ll_CreateDetails_image_img);
						holder.pinglunzhishu = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_text);
						holder.pinglunname = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_name);
						holder.pingluntext = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_img_text);
						holder.pingluntime = (TextView) v
								.findViewById(R.id.ll_CreateDetails_time);
						holder.pinglundel = (TextView) v
								.findViewById(R.id.ll_CreateDetails_image_img_del);
						v.setTag(holder);
					} else {
						holder = (ViewHolder) v.getTag();
					}
					BitmapUtils bitmapUtils = new BitmapUtils(
							getApplicationContext());
					bitmapUtils.display(
							holder.pinglunimg,
							ImageAddress.Stringhead
									+ listmap2.get(i).get("image").toString());
					holder.pinglunzhishu.setText(listmap2.get(i).get("zhishu")
							.toString());
					holder.pinglunname.setText(listmap2.get(i).get("nickname")
							.toString());
					holder.pingluntext.setText(listmap2.get(i).get("content")
							.toString());
					holder.pingluntime.setText(listmap2.get(i).get("rtime")
							.toString());
					if (listmap2.get(i).get("mdel").toString().equals("1")) {
						holder.pinglundel.setVisibility(View.VISIBLE);
						holder.pinglundel
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										showdialogup();
										jiazhilist = null;
										wid = listmap2.get(i).get("wid")
												.toString();
										delpinglun();
									}
								});
					} else if (listmap2.get(i).get("mdel").toString()
							.equals("0")) {
						holder.pinglundel.setVisibility(View.GONE);
					}
					return v;
				}

				@Override
				public long getItemId(int arg0) {
					return 0;
				}

				@Override
				public Object getItem(int arg0) {
					return null;
				}

				@Override
				public int getCount() {
					return listmap2.size();
				}
			};
			listview_comments.setAdapter(baseadapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 收藏人的列表
	protected void duogeimgs(String listsc) {
		try {
			JSONArray array = new JSONArray(listsc);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				map = new HashMap<String, Object>();
				map.put("nimage", object.get("nimage").toString());
				map.put("zhishu", object.get("zhishu").toString());
				map.put("memid", object.get("memid").toString());
				listmap.add(map);
			}
			adapterlist = new BaseAdapter() {

				@Override
				public View getView(final int position, View view,
						ViewGroup parent) {
					ViewHolder holder = null;
					if (view == null) {
						holder = new ViewHolder();
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.jointhinktank, null);
						holder.headimg = (ImageView) view
								.findViewById(R.id.ivjointhink);
						holder.texthead = (TextView) view
								.findViewById(R.id.tvjointhink);
						view.setTag(holder);
					} else {
						holder = (ViewHolder) view.getTag();
						System.out.println(listmap);
					}
					if (listmap.get(position).get("nimage").toString()
							.equals("")) {
						holder.headimg.setBackgroundResource(R.drawable.logo);
					}
					bitmapUtils = new BitmapUtils(getApplicationContext());
					bitmapUtils.display(holder.headimg, ImageAddress.Stringhead
							+ listmap.get(position).get("nimage").toString());
					holder.texthead.setText(listmap.get(position).get("zhishu")
							.toString());
					holder.headimg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int intentid = Integer.parseInt(listmap
									.get(position).get("memid").toString());
							if (intentid == IsTrue.userId) {
								intent = new Intent(
										GameProductionMsgActivity.this,
										MyDetailsActivity.class);
								startActivity(intent);
							} else {
								// 跳转其他人信息
								intent = new Intent(
										GameProductionMsgActivity.this,
										OtherDetailsActivity.class);
								intent.putExtra("id", intentid + "");
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
					return listmap.size();
				}
			};
			duogeimg.setAdapter(adapterlist);
		} catch (JSONException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public class ViewHolder {
		ImageView img;
		ImageView headimg;// 收藏头像
		TextView texthead;// 收藏指数
		ImageView pinglunimg;// 评论头像
		TextView pinglunzhishu;// 评论指数
		TextView pinglunname;// 评论姓名
		TextView pingluntext;// 评论内容
		TextView pingluntime;// 评论时间
		TextView pinglundel;// 评论删除
	}

	private void init() {
		intent = getIntent();
		zuopinid = intent.getExtras().getString("id").toString();
		zid = intent.getExtras().getString("zid").toString();
		back.setOnClickListener(this);
		like.setOnClickListener(this);
		share.setOnClickListener(this);
		down_xing.setOnClickListener(this);
		downpinglun.setOnClickListener(this);
		pinglun.setOnClickListener(this);
		down_zan.setOnClickListener(this);
		btn2.setOnClickListener(this);
		
		jubao.setOnClickListener(this);
		
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String, Object>>();
		map2 = new HashMap<String, Object>();
		listmap2 = new ArrayList<Map<String, Object>>();
		IsTrue.kantu = 0;
		duogeimg = (HorizontalListView) findViewById(R.id.game_production_msg_duogeimg);
		listview_comments = (ListViewForScrollView) findViewById(R.id.game_production_msg_list);
		mg = (MyGridView) findViewById(R.id.game_production_msg_mg);
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			shareimg.setBackgroundResource(R.drawable.fenxiang_ed);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		
		case R.id.game_production_msg_jubao: //举报
			jubaoDialog();
			 
			break;
		case R.id.game_production_msg_backing:
			finish();
			break;
		case R.id.game_production_msg_share:// 分享
			// intent = new Intent();
			// intent.setAction(Intent.ACTION_SEND);
			// intent.putExtra(Intent.EXTRA_TEXT, urls);
			// intent.setType("text/plain");
			// // startActivity(intent);
			// startActivityForResult(intent, 2);
			share();
			break;
		case R.id.game_production_msg_down_xing:// 底部收藏
			if (Utils.isFastDoubleClick()) {
				return;
			}
			downshoucang();
			break;
		case R.id.game_production_msg_likeall:// 收藏
			if (Utils.isFastDoubleClick()) {
				return;
			}
			downshoucang();
			break;
		case R.id.game_production_msg_down_pinglun:// 底部评论
			pinglun();
			break;
		case R.id.game_production_msg_pinglun:// 评论
			pinglun();
			break;
		case R.id.game_production_msg_down_zan:// 底部赞
			showdialogup();
			downzan();
			break;
		case R.id.game_production_msg_btn1:// 获胜冠军1
			break;
		case R.id.game_production_msg_btn2:// 获胜冠军2
			showdialogup();
			guanjun();
			break;
		default:
			break;
		}
	}
	
	private void jubaoDialog() {
		Dialog dialog = new JuBaoDialog(this, R.style.MyDialog, sid,4+"");
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);		
	}

	private void share() {
		// 首先在您的Activity中添加如下成员变量
		// final UMSocialService mController =
		// UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		// mController.setShareContent(urls);
		// mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
		// SHARE_MEDIA.WEIXIN_CIRCLE,
		// SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA,
		// SHARE_MEDIA.TENCENT,
		// SHARE_MEDIA.DOUBAN,
		// SHARE_MEDIA.RENREN);
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);

		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();

		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();

		// 添加有道云笔记平台
		UMYNoteHandler yNoteHandler = new UMYNoteHandler(
				GameProductionMsgActivity.this);
		yNoteHandler.addToSocialSDK();

		// 添加易信平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
		UMYXHandler yixinHandler = new UMYXHandler(
				GameProductionMsgActivity.this,
				"yxc0614e80c9304c11b0391514d09f13bf");
		// 关闭分享时的等待Dialog
		yixinHandler.enableLoadingDialog(false);
		// 把易信添加到SDK中
		yixinHandler.addToSocialSDK();

		// 易信朋友圈平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
		UMYXHandler yxCircleHandler = new UMYXHandler(
				GameProductionMsgActivity.this,
				"yxc0614e80c9304c11b0391514d09f13bf");
		yxCircleHandler.setToCircle(true);
		yxCircleHandler.addToSocialSDK();

		// 添加来往
		UMLWHandler umlwHandler = new UMLWHandler(
				GameProductionMsgActivity.this, "laiwangd497e70d4",
				"d497e70d4c3e4efeab1381476bac4c5e");
		umlwHandler.addToSocialSDK();
		umlwHandler.setMessageFrom("友盟分享组件");

		// 添加来往动态
		UMLWHandler umlwDynamicHandler = new UMLWHandler(
				GameProductionMsgActivity.this, "laiwangd497e70d4",
				"d497e70d4c3e4efeab1381476bac4c5e");
		umlwDynamicHandler.addToSocialSDK();
		umlwDynamicHandler.setMessageFrom("友盟分享组件");
		mController.openShare(GameProductionMsgActivity.this, false);

		// 添加Facebook分享
		// UMFacebookHandler mFacebookHandler =
		// UMFacebookHandler(GameProductionMsgActivity.this);
		// mFacebookHandler.addToSocialSDK();

		// 添加Twitter分享
		// mController.getConfig().supportAppPlatform(GameProductionMsgActivity.this,
		// SHARE_MEDIA.TWITTER,
		// "这里你构造mController填写的字符串参数", true) ;

		// 构建Instagram的Handler
		UMInstagramHandler instagramHandler = new UMInstagramHandler(
				GameProductionMsgActivity.this);
		// 将instagram添加到sdk中
		instagramHandler.addToSocialSDK();
		// 本地图片
		UMImage localImage = new UMImage(GameProductionMsgActivity.this,
				R.drawable.logo);
		// 设置分享到Instagram的内容，
		// 注意由于instagram客户端的限制，目前该平台只支持纯图片分享，文字、音乐、url图片等都无法分享。
		InstagramShareContent instagramShareContent = new InstagramShareContent(
				localImage);
		// 设置Instagram的分享内容
		mController.setShareMedia(instagramShareContent);

		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
		// 设置分享title
		qqShareContent.setTitle("hello, title");
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(
				GameProductionMsgActivity.this, R.drawable.logo));
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl("www.baidu.com");
		mController.setShareMedia(qqShareContent);

		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
		// 设置点击消息的跳转URL
		qzone.setTargetUrl("www.baidu.com");
		// 设置分享内容的标题
		// qzone.setTitle("QZone title");
		// 设置分享图片
		// qzone.setShareImage(urlImage);
		mController.setShareMedia(qzone);

		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
				GameProductionMsgActivity.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();

		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				GameProductionMsgActivity.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();

		SnsPostListener snsPostListener = new SnsPostListener() {

			@Override
			public void onStart() {
				Toast.makeText(GameProductionMsgActivity.this, "分享开始",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					Toast.makeText(GameProductionMsgActivity.this, "分享成功",
							Toast.LENGTH_SHORT).show();
					shareimg.setBackgroundResource(R.drawable.fenxiang_ed);
				} else {
					Toast.makeText(GameProductionMsgActivity.this, "分享失败",
							Toast.LENGTH_SHORT).show();
				}

			}
		};
		// mController.openShare(GameProductionMsgActivity.this, false);
		mController.registerListener(snsPostListener);
	}

	private void guanjun() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("zid", zid);
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter("sid", zuopinid);
		httpUtils.send(HttpMethod.POST, MyUrl.winccb, params,
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
						String list = responseInfo.result;
						try {
							JSONObject object = new JSONObject(list);
							String num = object.getString("num").toString();
							if (num.equals("1")) {
								Toast.makeText(getApplicationContext(), "失败",
										Toast.LENGTH_SHORT).show();
							} else if (num.equals("2")) {
								Toast.makeText(getApplicationContext(), "分配成功",
										Toast.LENGTH_SHORT).show();
							} else if (num.equals("3")) {
								Toast.makeText(getApplicationContext(),
										"发布人不存在", Toast.LENGTH_SHORT).show();
							} else if (num.equals("4")) {
								Toast.makeText(getApplicationContext(),
										"作品不存在", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

	private void downzan() {
		if (zani == 1) {
			zanimg(MyUrl.praisedel);
		} else if (zani == 0) {
			zanimg(MyUrl.Stringzan);
		}
	}

	// 底部赞
	private void zanimg(String URL) {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("sid", zuopinid);
		params.addBodyParameter("source", 2 + "");
		params.addBodyParameter("uid", IsTrue.userId + "");
		httpUtils.send(HttpMethod.POST, URL, params,
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
						String list = responseInfo.result;
						if (zani == 1) {
							try {
								JSONObject object = new JSONObject(list);
								String num = object.getString("num").toString();
								if (num.equals("1")) {
									Toast.makeText(getApplicationContext(),
											"删除点赞失败", Toast.LENGTH_SHORT)
											.show();
								} else if (num.equals("2")) {
									Toast.makeText(getApplicationContext(),
											"删除点赞成功", Toast.LENGTH_SHORT)
											.show();
									zan_img.setBackgroundResource(R.drawable.zan);
									// zan = 0 +"";
									zani = 0;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else if (zani == 0) {
							try {
								JSONObject object = new JSONObject(list);
								String num = object.getString("num").toString();
								if (num.equals("1")) {
									Toast.makeText(getApplicationContext(),
											"点赞失败", Toast.LENGTH_SHORT).show();
								} else if (num.equals("2")) {
									Toast.makeText(getApplicationContext(),
											"点赞成功", Toast.LENGTH_SHORT).show();
									zan_img.setBackgroundResource(R.drawable.zan_ed);
									// zan = 1+"";
									zani = 1;
								} else if (num.equals("3")) {
									Toast.makeText(getApplicationContext(),
											"已经点过赞了", Toast.LENGTH_SHORT)
											.show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	// 评论弹窗
	private void pinglun() {
		Dialog dialog = new PingLunDialog(this, R.style.MyDialog, zuopinid,
				2 + "", new PingLunDialog.PriorityListener() {

					@Override
					public void refreshPriorityUI(
							List<Map<String, Object>> listmap) {
						pinglunlist = listmap;
						listview_comments.setVisibility(View.VISIBLE);
						numcount = numcount + 1;
						pinglunnum.setText(numcount + "");
						if (numcount == 0) {
							pinglunimg
									.setBackgroundResource(R.drawable.pinglun);
						} else {
							pinglunimg
									.setBackgroundResource(R.drawable.pinglun_ed);
						}
						xian2.setVisibility(View.VISIBLE);
						xian3.setVisibility(View.VISIBLE);
						xian4.setVisibility(View.VISIBLE);
						pinglunxianshi();
					}
				});
		// 底部弹出充满屏幕
		Window window = dialog.getWindow();
		LayoutParams params = new LayoutParams();
		params.gravity = Gravity.BOTTOM;
		// params.width = WindowManager.LayoutParams.FILL_PARENT;
		// params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(params);
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}

	// 添加评论后显示评论
	protected void pinglunxianshi() {
		baseadapter = new BaseAdapter() {
			@Override
			public View getView(final int i, View v, ViewGroup arg2) {
				ViewHolder holder = null;
				if (v == null) {
					// ImageView pinglunimg;//评论头像
					// TextView pinglunzhishu;//评论指数
					// TextView pinglunname;//评论姓名
					// TextView pingluntext;//评论内容
					// TextView pingluntime;//评论时间
					// TextView pinglundel;//评论删除
					holder = new ViewHolder();
					// 可以理解为从vlist获取view 之后把view返回给ListView
					v = LayoutInflater.from(GameProductionMsgActivity.this)
							.inflate(R.layout.creativedetails_comments, null);
					holder.pinglunimg = (ImageView) v
							.findViewById(R.id.ll_CreateDetails_image_img);
					holder.pinglunzhishu = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_text);
					holder.pinglunname = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_name);
					holder.pingluntext = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_img_text);
					holder.pingluntime = (TextView) v
							.findViewById(R.id.ll_CreateDetails_time);
					holder.pinglundel = (TextView) v
							.findViewById(R.id.ll_CreateDetails_image_img_del);
					v.setTag(holder);
				} else {
					holder = (ViewHolder) v.getTag();
				}
				BitmapUtils bitmapUtils = new BitmapUtils(
						getApplicationContext());
				bitmapUtils.display(holder.pinglunimg, ImageAddress.Stringhead
						+ pinglunlist.get(i).get("image").toString());
				holder.pinglunzhishu.setText(pinglunlist.get(i).get("zhishu")
						.toString());
				holder.pinglunname.setText(pinglunlist.get(i).get("nickname")
						.toString());
				holder.pingluntext.setText(pinglunlist.get(i).get("content")
						.toString());
				holder.pingluntime.setText(pinglunlist.get(i).get("rtime")
						.toString());
				if (pinglunlist.get(i).get("mdel").toString().equals("1")) {
					holder.pinglundel.setVisibility(View.VISIBLE);
					holder.pinglundel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showdialogup();
							jiazhilist = null;
							wid = pinglunlist.get(i).get("wid").toString();
							delpinglun();
						}
					});
				} else if (pinglunlist.get(i).get("mdel").toString()
						.equals("0")) {
					holder.pinglundel.setVisibility(View.GONE);
				}
				return v;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return pinglunlist.size();
			}
		};
		listview_comments.setAdapter(baseadapter);
	}

	// 删除评论
	private void delpinglun() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("sid", zuopinid);
		params.addBodyParameter("source", 2 + "");
		params.addBodyParameter("uid", IsTrue.userId + "");
		params.addBodyParameter("cid", wid);
		httpUtils.send(HttpMethod.POST, MyUrl.origacommentdel, params,
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
						String list = responseInfo.result;
						try {
							JSONObject object = new JSONObject(list);
							String num = object.getString("num").toString();
							if (num.equals("1")) {

							} else if (num.equals("2")) {
								String lists = object.getString("list")
										.toString();
								if (lists == null || lists.equals("null")) {
									xian2.setVisibility(View.GONE);
									xian3.setVisibility(View.GONE);
									xian4.setVisibility(View.GONE);
									listview_comments.setVisibility(View.GONE);
									numcount = 0;
									if (numcount == 0) {
										pinglunimg
												.setBackgroundResource(R.drawable.pinglun);
									}
									pinglunnum.setText(numcount + "");
									return;
								} else {
									listmap2 = new ArrayList<Map<String, Object>>();
									jiazhilist = lists;
									numcount = numcount - 1;
									pinglunnum.setText(numcount + "");
									xianshipinglun();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	// 下面收藏
	private void downshoucang() {
		httpUtils = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("source", 2 + "");
		params.addBodyParameter("sid", zuopinid);
		params.addBodyParameter("uid", IsTrue.userId + "");
		httpUtils.send(HttpMethod.POST, MyUrl.Stringshoucang, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(getApplicationContext(),
								error.getExceptionCode() + ":" + msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String list = responseInfo.result;
						System.out.println(list);
						try {
							JSONObject object = new JSONObject(list);
							String num = object.getString("num");
							if (num.equals("1")) {
								Toast.makeText(getApplicationContext(), "收藏失败",
										Toast.LENGTH_SHORT).show();
							} else if (num.equals("2")) {
								Toast.makeText(getApplicationContext(), "收藏成功",
										Toast.LENGTH_SHORT).show();
								down_xingxing
										.setBackgroundResource(R.drawable.shoucang_ed);
								likeimg.setBackgroundResource(R.drawable.like_ed_shixin);
								liketext.setText(Integer.parseInt(sc) + 1 + "");
							} else if (num.equals("3")) {
								Toast.makeText(getApplicationContext(), "已经收藏",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
