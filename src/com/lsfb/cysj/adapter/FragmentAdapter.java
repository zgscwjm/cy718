package com.lsfb.cysj.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lsfb.cysj.HomeActivity;
import com.lsfb.cysj.app.IsTrue;
import com.lsfb.cysj.fragment.CreaticeIndexRankingAreaCreaticeFragment;
import com.lsfb.cysj.fragment.CreaticeIndexRankingAreaThinkTankExpertsFragment;
import com.lsfb.cysj.fragment.CreaticeIndexRankingAreaUserFragment;
import com.lsfb.cysj.fragment.FriendsAttention;
import com.lsfb.cysj.fragment.FriendsFans;
import com.lsfb.cysj.fragment.FriendsFragment;
import com.lsfb.cysj.fragment.FriendsFriends;
import com.lsfb.cysj.fragment.FriendsGames;
import com.lsfb.cysj.fragment.GameApplyFragment;
import com.lsfb.cysj.fragment.GameEndFragment;
import com.lsfb.cysj.fragment.HotIdeasGameFragment;
import com.lsfb.cysj.fragment.IdeasFragment;
import com.lsfb.cysj.fragment.IdeasFriendsFragment;
import com.lsfb.cysj.fragment.IdeasRankingRegionCity;
import com.lsfb.cysj.fragment.IdeasRankingRegionCountry;
import com.lsfb.cysj.fragment.IdeasRankingRegionCounty;
import com.lsfb.cysj.fragment.IdeasRankingRegionProvince;
import com.lsfb.cysj.fragment.IdeasWorldSonFragment;
import com.lsfb.cysj.fragment.LoginFragment;
import com.lsfb.cysj.fragment.TicketAlreadyUseFragment;
import com.lsfb.cysj.fragment.TicketAlreadyoverdueFragment;
import com.lsfb.cysj.fragment.TicketNoUseFragment;
import com.lsfb.cysj.fragment.TrendsGameFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
	int TAB_COUNT;
	int i;
	String ccid,qqid;
	IdeasWorldSonFragment ideasWorldSonFragment;
	IdeasFriendsFragment ideasFriendsFragment;
	GameApplyFragment gameApplyFragment;
	GameEndFragment gameEndFragment;
	CreaticeIndexRankingAreaCreaticeFragment creaticeIndexRankingAreaCreaticeFragment;//创意
	CreaticeIndexRankingAreaUserFragment creaticeIndexRankingAreaUserFragment;//用户
	CreaticeIndexRankingAreaThinkTankExpertsFragment creaticeIndexRankingAreaThinkTankExpertsFragment;//智库专家

	public FragmentAdapter(FragmentManager fm, int num, int i) {
		super(fm);
		this.TAB_COUNT = num;
		this.i = i;
	}

	public void refresh() {
		if (ideasWorldSonFragment != null) {
			ideasWorldSonFragment.refresh();
		}

	}

	public void setDataareaRanking(String cid, String qid, int i) {
		ccid = cid;
		qqid = qid;
		switch (i) {
		case 1:
			creaticeIndexRankingAreaUserFragment.setData(cid, qid);
			break;
		case 2:
			creaticeIndexRankingAreaCreaticeFragment.setData(cid, qid);
			break;
		case 3:
			creaticeIndexRankingAreaThinkTankExpertsFragment.setData(cid, qid);
			break;
		default:
			break;
		}
		
	}

	public void setDatechuangFriend() {
		if (ideasFriendsFragment != null) {
			ideasFriendsFragment.setData();
		}
	}

	public void city(String countries, String province, String city, String qu) {
		if (gameApplyFragment != null && IsTrue.city == false) {
			gameApplyFragment.getaddress(countries, province, city, qu);
		} else if (gameEndFragment != null && IsTrue.city == true) {
			gameEndFragment.getaddress(countries, province, city, qu);
		}
	}

	@Override
	public Fragment getItem(int id) {
		switch (i) {
		case 0:// 下面的tab切换
			switch (id) {
			case HomeActivity.TAB_IDEAS:
				IdeasFragment ideasFragment = new IdeasFragment();
				return ideasFragment;
			case HomeActivity.TAB_FRIENDS:
				FriendsFragment friendsFragment = new FriendsFragment();
				return friendsFragment;
			case HomeActivity.TAB_TRENDSGAME:
				TrendsGameFragment trendsGameFragment = new TrendsGameFragment();
				return trendsGameFragment;
			case HomeActivity.TAB_MY:
				LoginFragment myFragment = new LoginFragment();
				return myFragment;
			}
			break;
		case 1:// 我的资料
				// switch (id) {
				// case 0:
				// HomePageFragment homePageFragment = new HomePageFragment();
				// return homePageFragment;
				// case 1:
				// DynamicFragment dynamicFragment = new DynamicFragment();
				// return dynamicFragment;
				// case 2:
				// CreativeFragment creativeFragment = new CreativeFragment();
				// return creativeFragment;
				// }
			break;
		// case 2:// 创意世界优秀结果
		// switch (id) {
		// case 0:
		// InnovationAwardFragment innovationAwardFragment = new
		// InnovationAwardFragment();
		// return innovationAwardFragment;
		// case 1:
		// ScienceAwardFragment scienceAwardFragment = new
		// ScienceAwardFragment();
		// return scienceAwardFragment;
		// case 2:
		// ArtPrizeFragment artPrizeFragment = new ArtPrizeFragment();
		// return artPrizeFragment;
		// case 3:
		// LiteraryAwardFragment literaryAwardFragment = new
		// LiteraryAwardFragment();
		// return literaryAwardFragment;
		// case 4:
		// GoodnessAwardFragment goodnessAwardFragment = new
		// GoodnessAwardFragment();
		// return goodnessAwardFragment;
		// }
		// break;
		// case 3:// 我的创意指数排行
		// switch (id) {
		// case 0:
		// IdeasRankingFriendsFragment ideasRankingFriendsFragment = new
		// IdeasRankingFriendsFragment();
		// return ideasRankingFriendsFragment;
		// case 1:
		// IdeasRankingRegionFragment ideasRankingRegionFragment = new
		// IdeasRankingRegionFragment();
		// return ideasRankingRegionFragment;
		// case 2:
		// IdeasRankingSchoolFragment ideasRankingSchoolFragment = new
		// IdeasRankingSchoolFragment();
		// return ideasRankingSchoolFragment;
		// case 3:
		// IdeasRankingGovFragment ideasRankingGovFragment = new
		// IdeasRankingGovFragment();
		// return ideasRankingGovFragment;
		// case 4:
		// IdeasRankingFirmFragment ideasRankingFirmFragment = new
		// IdeasRankingFirmFragment();
		// return ideasRankingFirmFragment;
		// }
		// break;
		case 4:// 动态比赛。创意世界
			switch (id) {
			case 0:
				HotIdeasGameFragment hotIdeasGameFragment = new HotIdeasGameFragment();
				return hotIdeasGameFragment;
			case 1:
				ideasWorldSonFragment = new IdeasWorldSonFragment();
				return ideasWorldSonFragment;
			case 2:
				ideasFriendsFragment = new IdeasFriendsFragment();
				return ideasFriendsFragment;
			}
			break;
		case 5:// 创友录
			switch (id) {
			case 0:
				FriendsFriends friendsFriends = new FriendsFriends();
				return friendsFriends;
			case 1:
				FriendsAttention friendsAttention = new FriendsAttention();
				return friendsAttention;
			case 2:
				FriendsFans friendsFans = new FriendsFans();
				return friendsFans;
			case 3:
				FriendsGames friendsGames = new FriendsGames();
				return friendsGames;
			}
			break;
		case 6:// 创意世界大赛
			switch (id) {
			case 0:
				gameApplyFragment = new GameApplyFragment();
				return gameApplyFragment;
			case 1:
				gameEndFragment = new GameEndFragment();
				return gameEndFragment;
			}
			break;
		case 7:// 入场卷
			switch (id) {
			case 0:
				TicketNoUseFragment ticketNoUseFragment = new TicketNoUseFragment();
				return ticketNoUseFragment;
			case 1:
				TicketAlreadyUseFragment ticketAlreadyUseFragment = new TicketAlreadyUseFragment();
				return ticketAlreadyUseFragment;
			case 2:
				TicketAlreadyoverdueFragment ticketAlreadyoverdueFragmentmt = new TicketAlreadyoverdueFragment();
				return ticketAlreadyoverdueFragmentmt;
			}
			break;
		case 8:// 创意世界指数排行地区排行
			switch (id) {
			case 0:
				IdeasRankingRegionCountry ideasRankingRegionCountry = new IdeasRankingRegionCountry();
				return ideasRankingRegionCountry;
			case 1:
				IdeasRankingRegionProvince ideasRankingRegionProvince = new IdeasRankingRegionProvince();
				return ideasRankingRegionProvince;
			case 2:
				IdeasRankingRegionCity ideasRankingRegionCity = new IdeasRankingRegionCity();
				return ideasRankingRegionCity;
			case 3:
				IdeasRankingRegionCounty ideasRankingRegionCounty = new IdeasRankingRegionCounty();
				return ideasRankingRegionCounty;
			}
			break;
		case 9:// 地区
			switch (id) {
			case 0:
				creaticeIndexRankingAreaUserFragment = new CreaticeIndexRankingAreaUserFragment();
				Bundle bundle = new Bundle();
				bundle.putString("cid", ccid);
				bundle.putString("qid", qqid);
				creaticeIndexRankingAreaUserFragment.setArguments(bundle);
				return creaticeIndexRankingAreaUserFragment;
			case 1:
				creaticeIndexRankingAreaCreaticeFragment = new CreaticeIndexRankingAreaCreaticeFragment();
				return creaticeIndexRankingAreaCreaticeFragment;
			case 2:
				creaticeIndexRankingAreaThinkTankExpertsFragment = new CreaticeIndexRankingAreaThinkTankExpertsFragment();
				return creaticeIndexRankingAreaThinkTankExpertsFragment;

			}
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}
}
