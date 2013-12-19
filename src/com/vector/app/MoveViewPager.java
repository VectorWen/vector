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
 * ֻ��һ����ʾViewPager�Ľ��棬��Ҫ1����ʾ���棬2��Ҫ��ʾ�Ľ��棨���ݣ�3��������
 * 
 * @author vector
 * 
 */
public class MoveViewPager extends BaseActivity implements OnClickListener {

	/**
	 * Ҫ��ʾ��ҳ��
	 */
	private ArrayList<View> viewList;
	/**
	 * ҳ����������
	 */
	private MyViewPagerAdapter viewAdapter;
	/**
	 * �ؼ�
	 */
	private ViewPager viewPager;
	/**
	 * ������ͼƬ
	 */
	private ImageView image;
	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	private View view1, view2, view3;// ����ҳ��

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
	 * ��ʼ������
	 */
	protected void initLayout() {
		/**
		 * ��Ҫ���ǳ�ʼ�����ݣ�������
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
			Toast.makeText(MoveViewPager.this, "������Button",Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	/**
	 * ��ʼ���������������ҳ������ʱ������ĺ���Ҳ������Ч������������Ҫ����һЩ����
	 */
	private void initImageView() {
		image = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 3 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		image.setImageMatrix(matrix);// ���ö�����ʼλ��
	}

	/**
	 * ��ʼ��ͷ��, Ͷ��Ҳ�е���¼�
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
	 * ��ʼ��ViewPager
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
		// ��Ҫ����ҳ���ı��¼�
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			/*
			 * ���ַ����������һ�֣����滹��һ�֣���Ȼ����Ƚ��鷳 Animation animation = null; switch
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
					* arg0, 0, 0);// ��Ȼ����Ƚϼ�ֻ࣬��һ�д��롣
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			image.startAnimation(animation);
			Toast.makeText(MoveViewPager.this,
					"��ѡ����" + viewPager.getCurrentItem() + "ҳ��",
					Toast.LENGTH_SHORT).show();
		}

	}
	

	class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// ���췽�������������ǵ�ҳ���������ȽϷ��㡣
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// ɾ��ҳ��
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // �����������ʵ����ҳ��
			container.addView(mListViews.get(position), 0);// ���ҳ��
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// ����ҳ��������
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// �ٷ���ʾ����д
		}
	}

}
