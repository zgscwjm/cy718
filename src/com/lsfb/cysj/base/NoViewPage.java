package com.lsfb.cysj.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class NoViewPage extends ViewPager {

	private boolean scrollble = true;

	public NoViewPage(Context context) {
		super(context);
	}

	public NoViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (scrollble) {
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (scrollble) {
			return false;
		}
		return super.onTouchEvent(ev);
	}

	public boolean isScrollble() {
		return scrollble;
	}

	

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}

}