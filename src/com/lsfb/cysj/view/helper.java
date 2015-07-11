package com.lsfb.cysj.view;

public class helper {
	/**
	 * 在ScrollView中嵌套了GridView或者ListView，每次启动或者再次唤醒activity的时候，就默认显示GridView了，
	 * 并不是ScrollView的顶部位置开始显示，这是由于系统原因造成的。 　　解决方案：
	 * 　　在Activity的OnResume方法中添加一段代码即可
	 * 
	 * @Override　 protected void onResume() {　 　　// TODO Auto-generated method
	 *           stub　 　　scrollView.smoothScrollTo(0, 0);　 　　super.onResume();　
	 *           } 　　网上也有解决办法，比如这个scrollView默认显示最下方内容 　　ScrollView
	 *           有一个方法scollTo(int, int)用来指定滚动条的位置。如果你尝试过，你会发现它是无效的。
	 *           　　因为scollTo()在Scrolliew内的内容加载完成后才能执行。所以我们这样设置滚动条的位置：
	 *           　　ScrollView mScrollView = findViewById(R.id.svid);
	 *           　　mScrollView.post(new Runnable() {　 　 public void run() {　 　　　
	 *           mScrollView.scrollTo(0, 1000);　 　 }　 });　 　　scrollTo(int,
	 *           int);方法生效啦。 　　他的办法可行，但是如果仔细看的话，会发现界面有个跳动，一闪一下。影响用户体验。
	 *           http://www.educity.cn/wenda/86901.html
	 */
}
