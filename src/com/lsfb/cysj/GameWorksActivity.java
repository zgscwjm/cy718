package com.lsfb.cysj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.Dialog.ApplyDialog;
import com.lsfb.cysj.Dialog.CaiDialog;
import com.lsfb.cysj.app.ImageAddress;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.view.ResDialog;
import com.lsfb.cysj.view.XListView;
import com.lsfb.cysj.view.XListView.IXListViewListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 参赛作品
 * @author admin
 *
 */
public class GameWorksActivity extends Activity implements OnClickListener,IXListViewListener{
	private static String[] nums = new String[] { "222", "223", "224", "225",
			"226", "227", "228" };
	/**
	 * game_works_back back 返回
	 */
	@ViewInject(R.id.game_works_back)
	private LinearLayout back;
	/**
	 * game_works_tijiao tijiao 提交
	 */
	@ViewInject(R.id.game_works_tijiao)
	private TextView tijiao;
	@ViewInject(R.id.game_works_list)
	private XListView list;
	@ViewInject(R.id.game_works_wu)
	private TextView wu;
	BaseAdapter adapter;
	AsyncHttpClient client;
	RequestParams params;
	Intent intent;
	String sid;// 大赛id
	int count = 0;
	/**
	 * id:作品id bsid sid:比赛id title:作品题目 image:作品封面图 nickname:会员昵称 memid:会员id
	 * memimage:会员头像 memindex:会员指数 heat:热度 content:作品内容(文字) praise:1(已点赞)|0(未点赞)
	 * money:1(已支持)|0(未支持)  clasid:分类数值   bitbs:2(比赛已结束不能支持创币)|1(比赛未结束能支持创币)
	 */
	String id, bsid, title, image, nickname, memid, memimage, memindex, heat,
			content, praise, money,clasid,bitbs;
	Map<String, Object> map;
	List<Map<String, Object>> listmap;
	BitmapUtils bitmapUtils;
	Dialog jiazaidialog;
	ViewHolder holder = null;
	ImageView imageViewholder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_works);
		ViewUtils.inject(this);
		init();
		showdialogup();
		xianshidata();
		data();
	}

	private void showdialogup() {
		jiazaidialog = new ResDialog(this, R.style.MyDialog, "正在加载...",
				R.drawable.loads);
		jiazaidialog.show();
		jiazaidialog.setCanceledOnTouchOutside(false);
	}

	private void data() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", sid);
		params.put("uid", IsTrue.userId);
		client.post(MyUrl.worklist, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response);
				try {
					String num = response.getString("num");
					clasid = response.getString("clasid");
					if (num.equals("1")) {
						wu.setVisibility(View.VISIBLE);
					} else if (num.equals("2")) {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("id", object.getString("id").toString());
							map.put("bsid", object.getString("sid").toString());
							map.put("title", object.getString("title")
									.toString());
							map.put("image", object.getString("image")
									.toString());
							map.put("nickname", object.getString("nickname")
									.toString());
							map.put("memid", object.getString("memid")
									.toString());
							map.put("memimage", object.getString("memimage")
									.toString());
							map.put("memindex", object.getString("memindex")
									.toString());
							map.put("heat", object.getString("heat").toString());
							map.put("content", object.getString("content")
									.toString());
							map.put("praise", object.getString("praise")
									.toString());
							map.put("money", object.getString("money")
									.toString());
							map.put("bitbs", object.getString("bitbs").toString());
							listmap.add(map);
						}
					}
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				jiazaidialog.dismiss();
				Toast.makeText(GameWorksActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
	protected void xianshidata() {
		adapter = new BaseAdapter() {
			@Override
			public View getView(final int position, View view, ViewGroup parent) {
				if (view == null) {
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.game_works_item, null);
					holder = new ViewHolder();
					holder.bigimg = (ImageView) view
							.findViewById(R.id.game_works_img);
					holder.title = (TextView) view
							.findViewById(R.id.game_works_title);
					holder.num = (Button) view
							.findViewById(R.id.game_works_btn);
					holder.text = (TextView) view
							.findViewById(R.id.game_works_text);
					holder.samllimg = (ImageView) view
							.findViewById(R.id.game_works_head);
					holder.name = (TextView) view
							.findViewById(R.id.game_works_name);
					holder.zhishu = (TextView) view
							.findViewById(R.id.game_works_num);
					holder.zhichi = (LinearLayout) view
							.findViewById(R.id.game_works_zhichi);
					holder.zhichiimg = (ImageView) view
							.findViewById(R.id.game_works_zhichiimg);
					holder.love = (LinearLayout) view
							.findViewById(R.id.game_works_love);
					holder.loveimg = (ImageView) view
							.findViewById(R.id.game_works_loveimg);
					holder.pinglun = (LinearLayout) view
							.findViewById(R.id.game_works_pinglun);
					holder.pinglunimg = (ImageView) view
							.findViewById(R.id.game_works_pinglunimg);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				bitmapUtils = new BitmapUtils(getApplicationContext());
				bitmapUtils.display(holder.bigimg, ImageAddress.cbit+listmap.get(position).get("image").toString());
				holder.title.setText(listmap.get(position).get("title").toString());
				holder.num.setText("热度:"+listmap.get(position).get("heat").toString());
				holder.text.setText(listmap.get(position).get("content").toString());
				bitmapUtils.display(holder.samllimg, ImageAddress.Stringhead+listmap.get(position).get("memimage").toString());
				holder.name.setText(listmap.get(position).get("nickname").toString());
				holder.zhishu.setText(listmap.get(position).get("memindex").toString());
				if (listmap.get(position).get("money").toString().equals("1")) {
					holder.zhichiimg.setBackgroundResource(R.drawable.zhici_ed);
					holder.zhichiimg.setClickable(false);
				}else{
					holder.zhichiimg.setBackgroundResource(R.drawable.zhici);
					holder.zhichiimg.setClickable(true);
				}
				if (listmap.get(position).get("praise").toString().equals("1")) {
					holder.loveimg.setBackgroundResource(R.drawable.like_ed_shixin);
				}else {
					holder.loveimg.setBackgroundResource(R.drawable.like);
				}
				if (listmap.get(position).get("bitbs").toString().equals("2")) {
					holder.zhichiimg.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(getApplicationContext(), "比赛以结束", Toast.LENGTH_SHORT).show();
						}
					});
				}else if (listmap.get(position).get("bitbs").toString().equals("1")){
				holder.zhichiimg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (listmap.get(position).get("money").toString().equals("1")) {
							holder.zhichiimg.setClickable(false);
						}else{
							holder.zhichiimg.setClickable(true);
							imageViewholder = (ImageView)v;
							id = listmap.get(position).get("id").toString();
							Dialog dialog = new CaiDialog(GameWorksActivity.this, R.style.MyDialog,
									new CaiDialog.PriorityListener() {
								
								@Override
								public void refreshPriorityUI(String string) {
									if (string.equals("1")) {
										map = listmap.get(position);
										map.put("money", 1+"");
										imageViewholder.setBackgroundResource(R.drawable.zhici_ed);
									}
								}
							}, id,listmap.get(position).get("bsid").toString());
							dialog.show();
							dialog.setCanceledOnTouchOutside(false);
						}
					}
				});
				}
				holder.loveimg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						imageViewholder = (ImageView)v;
						soucang(listmap.get(position).get("id").toString(),position);
					}
				});
				view.setOnClickListener(new OnClickListener() {//holder.bigimg
					
					@Override
					public void onClick(View v) {
						intent = new Intent(GameWorksActivity.this,GameProductionMsgActivity.class);
						intent.putExtra("id", listmap.get(position).get("id").toString());
						intent.putExtra("zid", listmap.get(position).get("bsid").toString());
						startActivity(intent);
					}
				});
				holder.pinglun.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						intent = new Intent(GameWorksActivity.this,GameProductionMsgActivity.class);
						intent.putExtra("id", listmap.get(position).get("id").toString());
						intent.putExtra("zid", listmap.get(position).get("bsid").toString());
						startActivity(intent);
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
		list.setAdapter(adapter);
	}
	private void soucang(String string,final int i) {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", string);
		params.put("source", 2);
		params.put("uid", IsTrue.userId);
		System.out.println(string+"IIII"+IsTrue.userId);
		client.post(MyUrl.Stringshoucang, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				System.out.println(response);
				try {
					String num = response.getString("num");
					if (num.equals("1")) {
						Toast.makeText(GameWorksActivity.this, "收藏失败",
								Toast.LENGTH_SHORT).show();
					}else if (num.equals("2")) {
						Toast.makeText(GameWorksActivity.this, "收藏成功",
								Toast.LENGTH_SHORT).show();
						map = listmap.get(i);
						map.put("praise", 1+"");
						imageViewholder.setBackgroundResource(R.drawable.like_ed_shixin);
					}else if (num.equals("3")) {
						Toast.makeText(GameWorksActivity.this, "已经收藏",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(GameWorksActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
//	@Override
//	protected void onResume() {
//		count = 0;
//		listmap = new ArrayList<Map<String,Object>>();
//		data();
//		adapter.notifyDataSetChanged();
//		super.onResume();
//	}
	private void init() {
		intent = getIntent();
		sid = ""+intent.getExtras().getString("sid").toString();
		map = new HashMap<String, Object>();
		listmap = new ArrayList<Map<String, Object>>();
		back.setOnClickListener(this);
		tijiao.setOnClickListener(this);
		list.setPullLoadEnable(true);
		list.setXListViewListener(this);
	}

	static class ViewHolder {
		ImageView bigimg;// 大头像
		TextView title;// 标题
		Button num;// 热度
		TextView text;// 内容
		ImageView samllimg;// 小头像
		TextView name;// 姓名
		TextView zhishu;// 指数
		LinearLayout zhichi;// 支持
		ImageView zhichiimg;// 支持图标
		LinearLayout love;// 喜欢
		ImageView loveimg;// 喜欢图标
		LinearLayout pinglun;// 评论
		ImageView pinglunimg;// 评论图标
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.game_works_back:// 返回
			finish();
			break;
		case R.id.game_works_tijiao://提交作品
			intent = new Intent(GameWorksActivity.this,GameWorksFaBuActivity.class);
			intent.putExtra("clasid", clasid);
			intent.putExtra("sid", sid);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	@Override
	public void onRefresh() {
		count = 0;
		listmap = new ArrayList<Map<String,Object>>();
		xianshidata();
		data();
		adapter.notifyDataSetChanged();
		onLoad();
	}
	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime("刚刚");
	}
	@Override
	public void onLoadMore() {
		LoadMoremsg();
		adapter.notifyDataSetChanged();
		onLoad();
	}

	private void LoadMoremsg() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("sid", sid);
		params.put("page", ++count + "");
		params.put("uid", IsTrue.userId);
		client.post(MyUrl.worklistpage, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					clasid = response.getString("clasid");
					if (num.equals("1")) {
						Toast.makeText(GameWorksActivity.this,"亲,已经没有啦", Toast.LENGTH_SHORT).show();
					} else if (num.equals("2")) {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array.get(i);
							map = new HashMap<String, Object>();
							map.put("id", object.getString("id").toString());
							map.put("bsid", object.getString("sid").toString());
							map.put("title", object.getString("title")
									.toString());
							map.put("image", object.getString("image")
									.toString());
							map.put("nickname", object.getString("nickname")
									.toString());
							map.put("memid", object.getString("memid")
									.toString());
							map.put("memimage", object.getString("memimage")
									.toString());
							map.put("memindex", object.getString("memindex")
									.toString());
							map.put("heat", object.getString("heat").toString());
							map.put("content", object.getString("content")
									.toString());
							map.put("praise", object.getString("praise")
									.toString());
							map.put("money", object.getString("money")
									.toString());
							listmap.add(map);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jiazaidialog.dismiss();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				jiazaidialog.dismiss();
				Toast.makeText(GameWorksActivity.this, errorResponse + "",
						Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		
	}

}
