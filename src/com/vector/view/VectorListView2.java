package com.vector.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vector.app.R;

/**
 * 这个封装了下拉刷新，和底部加载更多，比VectorListView 友好一些
 * <p>
 * 下拉刷新：初始化布局，监听滑动，调用回调函数,主要是处理头部的view
 * </p>
 * <p>
 * 底部加载更多: 一样
 * </p>
 * 
 * @author vector
 * 
 */
public class VectorListView2 extends ListView implements OnScrollListener,
		OnClickListener {
	/**
	 * LV_NORMAL 普通状态：看不见头部
	 * <p>
	 * LV_PULL_REFRESH 下拉状态：头部被下拉的状态
	 * <p>
	 * LV_RELEASE_REFRESH 松开可刷新状态：下拉了全部头部的那个view
	 * <p>
	 * LV_LOADING 加载状态：在LV_RELEASE_REFRESH 状态下松开手就可以进入这个状态
	 * 
	 * @author vector
	 * 
	 */
	private enum DListViewState {
		LV_NORMAL, LV_PULL_REFRESH, LV_RELEASE_REFRESH, LV_LOADING;
	}

	/**
	 * LV_NORMAL 普通状态 ：显示查看更多
	 * <p>
	 * LV_LOADING 加载状态：显示正在加载
	 * <p>
	 * LV_OVER 结束状态：显示加载完成，这个过程是很短的时间
	 * <p>
	 * 
	 * @author vector
	 * 
	 */
	private enum DListViewLoadingMore {
		LV_NORMAL, LV_LOADING, LV_OVER;
	}

	/**
	 * 头部信息
	 */
	private View mHeadView;
	private TextView mTextViewRefresh;
	private TextView mTextViewLastUpdateDateTime;
	private ImageView mImageViewArrow;
	private ProgressBar mHeadProgressBar;
	private int mHeadViewWidth;
	private int mHeadViewHeight;
	private Animation animation, reverseAnimation;// 旋转动画，旋转动画之后旋转动画.

	/**
	 * 底部信息
	 */
	private View mFootView;
	private View mFootLoadedMoreView; // 加载完显示的
	private View mFootLoadingMoreView; // 加载中显示的
	private TextView mLoadMoreTextView; // 加载中里面的信息
	private ProgressBar mLoadingMorePB;

	private int mFirstItemIndex = -1;// 当前视图能看到的第一个项的索引

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean mIsRecordStartY = false;

	private int mStartY, mMoveY;// 按下是的y坐标,move时的y坐标

	private DListViewState mlistViewState = DListViewState.LV_NORMAL;// 拖拉状态.(自定义枚举)

	private DListViewLoadingMore loadingMoreState = DListViewLoadingMore.LV_NORMAL;// 加载更多默认状态.

	private final static int RATIO = 2;// 手势下拉距离比.

	private boolean mBack = false;// headView是否返回.

	private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// 下拉刷新接口（自定义）

	private boolean isScroller = true;// 是否屏蔽ListView滑动。

	public VectorListView2(Context context) {
		super(context, null);
		initDragListView(context);
	}

	public VectorListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDragListView(context);
	}

	/**
	 * 设置监听下拉刷新方法
	 * 
	 * @param onRefreshLoadingMoreListener
	 *            回调接口
	 */
	public void setOnRefreshListener(
			OnRefreshLoadingMoreListener onRefreshLoadingMoreListener) {
		this.onRefreshLoadingMoreListener = onRefreshLoadingMoreListener;
	}

	/***
	 * 初始化ListView
	 */
	private void initDragListView(Context context) {

		String time = "1994.12.05";// 更新时间

		initHeadView(context, time);// 初始化该head.

		initLoadMoreView(context);// 初始化footer

		setOnScrollListener(this);// ListView滚动监听
	}

	/***
	 * 初始话头部HeadView
	 * 
	 * @param context
	 *            上下文
	 * @param time
	 *            上次更新时间
	 */
	private void initHeadView(Context context, String time) {
		mHeadView = LayoutInflater.from(context).inflate(R.layout.head, null);
		mImageViewArrow = (ImageView) mHeadView
				.findViewById(R.id.head_arrowImageView);
		mImageViewArrow.setMinimumWidth(60);

		mHeadProgressBar = (ProgressBar) mHeadView
				.findViewById(R.id.head_progressBar);

		mTextViewRefresh = (TextView) mHeadView
				.findViewById(R.id.head_tipsTextView);

		mTextViewLastUpdateDateTime = (TextView) mHeadView
				.findViewById(R.id.head_lastUpdatedTextView);
		// 显示更新事件
		mTextViewLastUpdateDateTime.setText("最近更新:" + time);

		measureView(mHeadView);
		// 获取宽和高
		mHeadViewWidth = mHeadView.getMeasuredWidth();
		mHeadViewHeight = mHeadView.getMeasuredHeight();

		addHeaderView(mHeadView, null, false);// 将初始好的ListView add进拖拽ListView
		// 在这里我们要将此headView设置到顶部不显示位置.
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

		initAnimation();// 初始化动画
	}

	/***
	 * 初始化底部加载更多控件, 动画在style 上指定
	 */
	private void initLoadMoreView(Context context) {
		mFootView = LayoutInflater.from(context).inflate(
				R.layout.item_footer_lv, null);

		mFootLoadedMoreView =  mFootView
				.findViewById(R.id.item_footer_ll_more);
		mFootLoadedMoreView.setOnClickListener(this);

		mFootLoadingMoreView = mFootView.findViewById(R.id.item_footer_ll_refresh);

		mLoadingMorePB = (ProgressBar) mFootView.findViewById(R.id.item_footer_pb_refresh);

		mLoadMoreTextView = (TextView) mFootView.findViewById(R.id.item_footer_tv_refresh);
System.out.println(mLoadMoreTextView);
		addFooterView(mFootView);
	}

	/***
	 * 初始化动画
	 */
	private void initAnimation() {
		// 旋转动画
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());// 匀速
		animation.setDuration(250);
		animation.setFillAfter(true);// 停留在最后状态.
		// 反向旋转动画
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true);
	}

	/***
	 * 作用：测量 headView的宽和高.
	 * 
	 * @param child
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/***
	 * touch 事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		// 按下
		case MotionEvent.ACTION_DOWN:
			doActionDown(ev);
			break;
		// 移动
		case MotionEvent.ACTION_MOVE:
			doActionMove(ev);
			break;
		// 抬起
		case MotionEvent.ACTION_UP:
			doActionUp(ev);
			break;
		default:
			break;
		}
		/***
		 * 如果是ListView本身的拉动，那么返回true，这样ListView不可以拖动.
		 * 如果不是ListView的拉动，那么调用父类方法，这样就可以上拉执行.
		 */
		if (isScroller) {
			return super.onTouchEvent(ev);
		} else {
			return true;
		}

	}

	/***
	 * 摁下操作
	 * 
	 * 作用：获取摁下是的y坐标
	 * 
	 * @param event
	 */
	void doActionDown(MotionEvent event) {
		if (mIsRecordStartY == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecordStartY = true;
		}
	}

	/***
	 * 拖拽移动操作
	 * 
	 * @param event
	 */
	void doActionMove(MotionEvent event) {
		mMoveY = (int) event.getY();// 获取实时滑动y坐标
		// 检测是否是一次touch事件.
		if (mIsRecordStartY == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecordStartY = true;
		}
		/***
		 * 如果touch关闭或者正处于Loading状态的话 return.
		 */
		if (mIsRecordStartY == false
				|| mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// 向下啦headview移动距离为y移动的一半.（比较友好）
		int offset = (mMoveY - mStartY) / RATIO;

		switch (mlistViewState) {
		// 普通状态
		case LV_NORMAL: {
			// 如果<0，则意味着上滑动.
			if (offset > 0) {
				// 设置headView的padding属性.
				mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
				switchViewState(DListViewState.LV_PULL_REFRESH);// 下拉状态
			}

		}
			break;
		// 下拉状态
		case LV_PULL_REFRESH: {
			setSelection(0);// 时时保持在顶部.
			// 设置headView的padding属性.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			if (offset < 0) {
				/***
				 * 要明白为什么isScroller = false;
				 */
				isScroller = false;
				switchViewState(DListViewState.LV_NORMAL);// 普通状态
				Log.e("jj", "isScroller=" + isScroller);
			} else if (offset > mHeadViewHeight) {// 如果下拉的offset超过headView的高度则要执行刷新.
				switchViewState(DListViewState.LV_RELEASE_REFRESH);// 更新为可刷新的下拉状态.
			}
		}
			break;
		// 可刷新状态
		case LV_RELEASE_REFRESH: {
			setSelection(0);// 时时保持在顶部
			// 设置headView的padding属性.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			// 下拉offset>0，但是没有超过headView的高度.那么要goback 原装.
			if (offset >= 0 && offset <= mHeadViewHeight) {
				mBack = true;
				switchViewState(DListViewState.LV_PULL_REFRESH);
			} else if (offset < 0) {
				switchViewState(DListViewState.LV_NORMAL);
			} else {

			}
		}
			break;
		default:
			return;
		}
		;
	}

	/***
	 * 手势抬起操作
	 * 
	 * @param event
	 */
	public void doActionUp(MotionEvent event) {
		mIsRecordStartY = false;// 此时的touch事件完毕，要关闭。
		isScroller = true;// ListView可以Scrooler滑动.
		mBack = false;
		// 如果下拉状态处于loading状态.
		if (mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// 处理相应状态.
		switch (mlistViewState) {
		// 普通状态
		case LV_NORMAL:

			break;
		// 下拉状态
		case LV_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			switchViewState(mlistViewState.LV_NORMAL);
			break;
		// 刷新状态
		case LV_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchViewState(mlistViewState.LV_LOADING);
			onRefresh();// 下拉刷新
			break;
		}

	}

	// 切换headview视图
	private void switchViewState(DListViewState state) {

		switch (state) {
		// 普通状态
		case LV_NORMAL: {
			mImageViewArrow.clearAnimation();// 清除动画
			mImageViewArrow.setImageResource(R.drawable.arrow);
		}
			break;
		// 下拉状态
		case LV_PULL_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mImageViewArrow.setVisibility(View.VISIBLE);// 下拉图标
			mTextViewRefresh.setText("下拉可以刷新");
			mImageViewArrow.clearAnimation();// 清除动画

			// 是有可刷新状态（LV_RELEASE_REFRESH）转为这个状态才执行，其实就是你下拉后在上拉会执行.
			if (mBack) {
				mBack = false;
				mImageViewArrow.clearAnimation();// 清除动画
				mImageViewArrow.startAnimation(reverseAnimation);// 启动反转动画
			}
		}
			break;
		// 松开刷新状态
		case LV_RELEASE_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mImageViewArrow.setVisibility(View.VISIBLE);// 显示下拉图标
			mTextViewRefresh.setText("松开获取更多");
			mImageViewArrow.clearAnimation();// 清除动画
			mImageViewArrow.startAnimation(animation);// 启动动画
		}
			break;
		// 加载状态
		case LV_LOADING: {
			Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
			mHeadProgressBar.setVisibility(View.VISIBLE);
			mImageViewArrow.clearAnimation();
			mImageViewArrow.setVisibility(View.GONE);
			mTextViewRefresh.setText("载入中...");
		}
			break;
		default:
			return;
		}
		// 切记不要忘记时时更新状态。
		mlistViewState = state;

	}

	/***
	 * 下拉刷新
	 */
	private void onRefresh() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onRefresh();
		}
	}

	/***
	 * 下拉刷新完毕
	 */
	public void onRefreshComplete() {
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);// 回归.
		switchViewState(mlistViewState.LV_NORMAL);//
	}

	/***
	 * 点击加载更多
	 * 
	 * @param flag
	 *            数据是否已全部加载完毕
	 */
	public void onLoadMoreComplete(boolean flag) {
		if (flag) {
			updateLoadMoreViewState(DListViewLoadingMore.LV_OVER);
		} else {
			updateLoadMoreViewState(DListViewLoadingMore.LV_NORMAL);
		}

	}

	// 更新Footview视图
	private void updateLoadMoreViewState(DListViewLoadingMore state) {
		switch (state) {
		// 普通状态
		case LV_NORMAL:
			mFootLoadingMoreView.setVisibility(View.GONE);
			mFootLoadedMoreView.setVisibility(View.VISIBLE);
			break;
		// 加载中状态
		case LV_LOADING:
			mFootLoadingMoreView.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setText("加载中...");
			mFootLoadedMoreView.setVisibility(View.GONE);
			break;
		// 加载完毕状态
		case LV_OVER:
			mLoadingMorePB.setVisibility(View.GONE);
			mLoadMoreTextView.setText("加载完毕");
			break;
		default:
			break;
		}
		loadingMoreState = state;
	}

	/***
	 * ListView 滑动监听
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisibleItem;
	}

	/***
	 * 底部点击事件
	 */
	@Override
	public void onClick(View v) {
		// 防止重复点击
		if (onRefreshLoadingMoreListener != null
				&& loadingMoreState == DListViewLoadingMore.LV_NORMAL) {
			updateLoadMoreViewState(DListViewLoadingMore.LV_LOADING);
			onRefreshLoadingMoreListener.onLoadMore();// 对外提供方法加载更多.
		}

	}

	/***
	 * 自定义接口
	 */
	public interface OnRefreshLoadingMoreListener {
		/***
		 * // 下拉刷新执行
		 */
		void onRefresh();

		/***
		 * 点击加载更多
		 */
		void onLoadMore();
	}
}
