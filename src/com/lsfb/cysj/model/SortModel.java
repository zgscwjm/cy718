package com.lsfb.cysj.model;

public class SortModel {

	public String getMidds() {
		return midds;
	}
	public void setMidds(String midds) {
		this.midds = midds;
	}
	public String getMemdj() {
		return memdj;
	}
	public void setMemdj(String memdj) {
		this.memdj = memdj;
	}
	public String getZqzj() {
		return zqzj;
	}
	public void setZqzj(String zqzj) {
		this.zqzj = zqzj;
	}
	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	private String mid;//好友id
	private String image;//头像
	
	private String midds;// 是否关注
	
	private String memdj; //会员等级
	
	private String zqzj; //企业or个人
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
