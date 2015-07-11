package com.lsfb.cysj.bannerview;

/**
 * 广告实体类
 * @author wly
 *
 */
public class Advertising {

	private String picURL; //图片地址
	private String linkURL; //单击跳转地址
	private String title; //标题
	
	public Advertising(String picURL, String linkURL, String title) {
		super();
		this.picURL = picURL;
		this.linkURL = linkURL;
		this.title = title;
	}
	
	public String getPicURL() {
		return picURL;
	}
	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}
	public String getLinkURL() {
		return linkURL;
	}
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
