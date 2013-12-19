package com.vector.app;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 只是一个演示ViewPager的界面，需要1，显示界面，2，要显示的界面（数据）3，适配器
 * 
 * @author vector
 * 
 */
public class MoveViewPager extends BaseActivity implements OnClickListener {

	/**
	 * 要显示的页卡
	 */
	private ArrayList<View> viewList;
	/**
	 * 页卡的适配器
	 */
	private MyViewPagerAdapter viewAdapter;
	/**
	 * 控件
	 */
	private ViewPager viewPager;
	/**
	 * 动画的图片
	 */
	private ImageView image;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private View view1, view2, view3;// 各个页卡

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		initData();
		initLayout();

	}


	@Override
	protected void initData() {

	}

	/**
	 * 初始化布局
	 */
	protected void initLayout() {
		/**
		 * 主要就是初始化数据，三大步骤
		 */
		initImageView();
		initTextView();
		initViewPager();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.text1:
			viewPager.setCurrentItem(0);
			break;

		case R.id.text2:
			viewPager.setCurrentItem(1);
			break;

		case R.id.text3:
			viewPager.setCurrentItem(2);
			break;
			
		case R.id.button1:
			Toast.makeText(MoveViewPager.this, "按下了Button",Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */
	private void initImageView() {
		image = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		image.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 初始化头标, 投标也有点击事件
	 */

	private void initTextView() {
		TextView textView1 = (TextView) findViewById(R.id.text1);
		TextView textView2 = (TextView) findViewById(R.id.text2);
		TextView textView3 = (TextView) findViewById(R.id.text3);

		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);
		textView3.setOnClickListener(this);
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewList = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.viewpager1, null);
		view2 = inflater.inflate(R.layout.viewpager2, null);
		Button btn_v2 = (Button) view2.findViewById(R.id.button1);
		btn_v2.setOnClickListener(this);
		view3 = inflater.inflate(R.layout.viewpager3, null);
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewAdapter = new MyViewPagerAdapter(viewList);
		viewPager.setAdapter(viewAdapter);
		viewPager.setCurrentItem(0);
		// 需要监听页卡改变事件
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			/*
			 * 两种方法，这个是一种，下面还有一种，显然这个比较麻烦 Animation animation = null; switch
			 * (arg0) { case 0: if (currIndex == 1) { animation = new
			 * TranslateAnimation(one, 0, 0, 0); } else if (currIndex == 2) {
			 * animation = new TranslateAnimation(two, 0, 0, 0); } break; case
			 * 1: if (currIndex == 0) { animation = new
			 * TranslateAnimation(offset, one, 0, 0); } else if (currIndex == 2)
			 * { animation = new TranslateAnimation(two, one, 0, 0); } break;
			 * case 2: if (currIndex == 0) { animation = new
			 * TranslateAnimation(offset, two, 0, 0); } else if (currIndex == 1)
			 * { animation = new TranslateAnimation(one, two, 0, 0); } break;
			 * 
			 * }
			 */
			Animation animation = new TranslateAnimation(one * currIndex, one
					* arg0, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			image.startAnimation(animation);
			Toast.makeText(MoveViewPager.this,
					"您选择了" + viewPager.getCurrentItem() + "页卡",
					Toast.LENGTH_SHORT).show();
		}

	}
	

	class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

}
