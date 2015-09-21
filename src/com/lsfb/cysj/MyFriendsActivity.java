package com.lsfb.cysj;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.SortFriendAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.model.SortModel;

/**
 * 我的好友
 * @author Administrator
 *
 */
public class MyFriendsActivity extends Activity implements OnClickListener {
	Context mContext;

	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	/** 联系人名称 **/
	private ArrayList<String> mContactsName = new ArrayList<String>();

	/** 联系人头像 **/
	private ArrayList<String> mContactsNumber = new ArrayList<String>();

	/** 联系人头像 **/
	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	BaseAdapter adapter;
	@ViewInject(R.id.addfriends_list)
	private ListView list;
	@ViewInject(R.id.addfriends_back)
	private LinearLayout back;
	
	AsyncHttpClient client;
	RequestParams params;
	Map<String, Object> map;//单个内容
	List<Map<String, Object>> listData;//装下所有内容
	
	List<Map<String, Object>> newlist;
	
	private List<SortModel> SourceDateList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfriends);
		//启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		ViewUtils.inject(this);
//		getPhoneContacts();
//		getSIMContacts();
		init();
		data();
	}

	/** 得到手机通讯录联系人信息 **/
	private void getPhoneContacts() {
		ContentResolver resolver = this.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

				// 得到联系人头像ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

				// 得到联系人头像Bitamp
				Bitmap contactPhoto = null;

				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
				if (photoid > 0) {
					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(resolver, uri);
					contactPhoto = BitmapFactory.decodeStream(input);
				} else {
					contactPhoto = BitmapFactory.decodeResource(getResources(),
							R.drawable.zkman1);
				}

				mContactsName.add(contactName);
				mContactsNumber.add(phoneNumber);
				mContactsPhonto.add(contactPhoto);
			}

			phoneCursor.close();
		}
	}

	/** 得到手机SIM卡联系人人信息 **/
	private void getSIMContacts() {
		ContentResolver resolver = this.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
				null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// Sim卡中没有联系人头像

				mContactsName.add(contactName);
				mContactsNumber.add(phoneNumber);
			}

			phoneCursor.close();
		}
	}
	
	private void data() {
		client = new AsyncHttpClient();
		params = new RequestParams();
		params.put("uid", IsTrue.userId);
		client.post(MyUrl.memfriends, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String num = response.getString("num");
					Integer i = Integer.parseInt(num);
					if (i == 1) {
					} else {
						String lists = response.getString("list");
						JSONArray array = new JSONArray(lists);
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = (JSONObject) array.get(j);
							map = new HashMap<String, Object>();
							// mid:好友id
							// name:好友昵称
							// image:好友头像
							map.put("mid", object.getString("mid"));
							map.put("name", object.getString("name"));
							map.put("image", object.getString("image"));
							if(null!=listData)
							listData.add(map);
						}
					
						SourceDateList = filledData(listData);
						adapter = new SortFriendAdapter(MyFriendsActivity.this,
								SourceDateList);
						list.setAdapter(adapter);
						// 根据a-z进行排序源数据
//						Collections.sort(SourceDateList, pinyinComparator);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
//				Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_SHORT)
//						.show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		// 实例化汉字转拼音类
//		characterParser = CharacterParser.getInstance();
//
//		pinyinComparator = new PinyinComparator();
//		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
//		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
//
//			@Override
//			public void onTouchingLetterChanged(String s) {
//				// 该字母首次出现的位置
//				int position = adapter.getPositionForSection(s.charAt(0));
//				if (position != -1) {
//					list.setSelection(position);
//				}
//
//			}
//		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
//				Toast.makeText(getActivity(),
//						((SortModel) adapter.getItem(position)).getName(),
//						Toast.LENGTH_SHORT).show();

			}
		});

		// 根据输入框输入值的改变来过滤搜索
//		mClearEditText.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//				filterData(s.toString());
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});
	}

//	private void data() {
//		adapter = new BaseAdapter() {
//
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.addfriends_item, null);
//				ImageView img = (ImageView) view.findViewById(R.id.addfriends_item_img);
//				TextView name = (TextView) view.findViewById(R.id.addfriends_item_name);
//				TextView num = (TextView) view.findViewById(R.id.addfriends_item_text);
//				ImageView guanzhu = (ImageView) view.findViewById(R.id.addfriends_item_kan);
//				name.setText(mContactsName.get(position));
//				num.setText(mContactsNumber.get(position));
//				img.setImageBitmap(mContactsPhonto.get(position));
//				return view;
//			}
//
//			@Override
//			public long getItemId(int position) {
//				return position;
//			}
//
//			@Override
//			public boolean areAllItemsEnabled() {
//				return false;
//			}
//
//			@Override
//			public Object getItem(int position) {
//				return position;
//			}
//
//			@Override
//			public int getCount() {
//				// 设置绘制数量
//				return mContactsName.size();
//			}
//		};
//		list.setAdapter(adapter);
//	}
	
	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(List<Map<String,Object>> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < data.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(data.get(i).get("name").toString());
			sortModel.setMid(data.get(i).get("mid").toString());
			sortModel.setImage(data.get(i).get("image").toString());
			// 汉字转换成拼音
//			String pinyin = characterParser.getSelling(data.get(i).get("name").toString());
//			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
//			if (sortString.matches("[A-Z]")) {
//				sortModel.setSortLetters(sortString.toUpperCase());
//			} else {
//				sortModel.setSortLetters("#");
//			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	private void init() {
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addfriends_back:
			finish();
			break;

		default:
			break;
		}
	}

}
