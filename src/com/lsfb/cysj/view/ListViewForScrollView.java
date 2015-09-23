package com.lsfb.cysj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ListViewForScrollView extends ListView {

	
	private static final String TAG = "SilderListView";

	private SliderView mFocusedItemView;

	float mX = 0;
	float mY = 0;
	private int mPosition = -1;
	boolean isSlider = false;
	
	
	public ListViewForScrollView(Context context) {
		super(context);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
//	}


	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isSlider = false;
			mX = x;
			mY = y;
			int position = pointToPosition((int) x, (int) y);
			if (mPosition != position) {
				mPosition = position;
				if (mFocusedItemView != null) {
					mFocusedItemView.reset();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mPosition != -1) {
				if (Math.abs(mY - y) < 30 && Math.abs(mX - x) > 20) {
					int first = this.getFirstVisiblePosition();
					int index = mPosition - first;
					Log.d("zgscwjm", index+"");
					
					View view=getChildAt(index);
					//mFocusedItemView=(SliderView) view;
//					mFocusedItemView = (SliderView) getChildAt(index);
//					mFocusedItemView.onTouchEvent(event);
					isSlider = true;
					return true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (isSlider) {
				isSlider = false;
				if (mFocusedItemView != null) {
					mFocusedItemView.adjust(mX - x > 0);
					return true;
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}
}
