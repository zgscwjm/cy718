package com.lsfb.cysj.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsbf.cysj.R;
import com.lsfb.cysj.adapter.SortFriendAdapter;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.app.MyUrl;
import com.lsfb.cysj.model.SortModel;
import com.lsfb.cysj.sortlistview.CharacterParser;
import com.lsfb.cysj.sortlistview.PinyinComparator;
import com.lsfb.cysj.sortlistview.SideBar;
import com.lsfb.cysj.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.lsfb.cysj.view.ClearEditText;

/**
 * 我的好友(点击推荐加入好友跳转)
 * @author Administrator
 *
 */
public class FriendsFriends extends Fragment {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortFriendAdapter adapter;
	private ClearEditText mClearEditText;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private View rootView;
	AsyncHttpClient client;
	RequestParams params;
	Map<String, Object> map;//单个内容
	List<Map<String, Object>> list;//装下所有内容
	
	List<Map<String, Object>> newlist;
	int i = 0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// view = inflater.inflate(R.layout.mian_friends,container,false);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.friends_friends, container,
					false);
		} else {
			// 缓存的rootView需要判断是否已经被加过parent
			// 如果有parent需要从parent删除
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		init();
		data();
		// TODO Auto-generated method stub
		// View view = LayoutInflater.from(getActivity()).inflate(
		// R.layout.friendfriend, null);

		return rootView;

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
					Log.d("num", num);
					Integer i = Integer.parseInt(num);
					if (i == 1) {
					} else {
						String lists = response.getString("list");
						Log.d("friendslist", lists);
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
							list.add(map);
						}
					
						SourceDateList = filledData(list);
						adapter = new SortFriendAdapter(getActivity(),
								SourceDateList);
						
						Log.d("SourceDateList", ""+SourceDateList.size());
						for(int j=0;j<SourceDateList.size();j++){
							SourceDateList.get(j).getName();
						}
						sortListView.setAdapter(adapter);
						// 根据a-z进行排序源数据
						Collections.sort(SourceDateList, pinyinComparator);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_SHORT)
						.show();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(getActivity(),
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();

			}
		});

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


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
			String pinyin = characterParser.getSelling(data.get(i).get("name").toString());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		if (filterDateList==null) {
			return;
		}

		if (filterDateList.size() == 0) {
			if (i == 0) {
				newlist = list;
				i++;
			}
			list = new ArrayList<Map<String, Object>>();
		} else {
			if (i == 0) {
				newlist = list;
				i++;
			}
			list = newlist;
		}
		// 根据a-z进行排序
		System.out.println(filterDateList.toString()+"SSSSSSSSSSSSSSSSSS"+list.toString());
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	private void init() {
		
		newlist = new ArrayList<Map<String, Object>>();
		map = new HashMap<String, Object>();
		list = new ArrayList<Map<String, Object>>();
		
		newlist = new ArrayList<Map<String,Object>>();
		mClearEditText = (ClearEditText) rootView
				.findViewById(R.id.friends_frineds_edittext);
		sortListView = (ListView) rootView
				.findViewById(R.id.friends_frineds_list);
		dialog = (TextView) rootView.findViewById(R.id.friends_frineds_text);
		sideBar = (SideBar) rootView.findViewById(R.id.friends_frineds_sidrbar);
	}
}
