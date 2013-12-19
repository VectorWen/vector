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
 * �����װ������ˢ�£��͵ײ����ظ��࣬��VectorListView �Ѻ�һЩ
 * <p>
 * ����ˢ�£���ʼ�����֣��������������ûص�����,��Ҫ�Ǵ���ͷ����view
 * </p>
 * <p>
 * �ײ����ظ���: һ��
 * </p>
 * 
 * @author vector
 * 
 */
public class VectorListView2 extends ListView implements OnScrollListener,
		OnClickListener {
	/**
	 * LV_NORMAL ��ͨ״̬��������ͷ��
	 * <p>
	 * LV_PULL_REFRESH ����״̬��ͷ����������״̬
	 * <p>
	 * LV_RELEASE_REFRESH �ɿ���ˢ��״̬��������ȫ��ͷ�����Ǹ�view
	 * <p>
	 * LV_LOADING ����״̬����LV_RELEASE_REFRESH ״̬���ɿ��־Ϳ��Խ������״̬
	 * 
	 * @author vector
	 * 
	 */
	private enum DListViewState {
		LV_NORMAL, LV_PULL_REFRESH, LV_RELEASE_REFRESH, LV_LOADING;
	}

	/**
	 * LV_NORMAL ��ͨ״̬ ����ʾ�鿴����
	 * <p>
	 * LV_LOADING ����״̬����ʾ���ڼ���
	 * <p>
	 * LV_OVER ����״̬����ʾ������ɣ���������Ǻ̵ܶ�ʱ��
	 * <p>
	 * 
	 * @author vector
	 * 
	 */
	private enum DListViewLoadingMore {
		LV_NORMAL, LV_LOADING, LV_OVER;
	}

	/**
	 * ͷ����Ϣ
	 */
	private View mHeadView;
	private TextView mTextViewRefresh;
	private TextView mTextViewLastUpdateDateTime;
	private ImageView mImageViewArrow;
	private ProgressBar mHeadProgressBar;
	private int mHeadViewWidth;
	private int mHeadViewHeight;
	private Animation animation, reverseAnimation;// ��ת��������ת����֮����ת����.

	/**
	 * �ײ���Ϣ
	 */
	private View mFootView;
	private View mFootLoadedMoreView; // ��������ʾ��
	private View mFootLoadingMoreView; // ��������ʾ��
	private TextView mLoadMoreTextView; // �������������Ϣ
	private ProgressBar mLoadingMorePB;

	private int mFirstItemIndex = -1;// ��ǰ��ͼ�ܿ����ĵ�һ���������

	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	private boolean mIsRecordStartY = false;

	private int mStartY, mMoveY;// �����ǵ�y����,moveʱ��y����

	private DListViewState mlistViewState = DListViewState.LV_NORMAL;// ����״̬.(�Զ���ö��)

	private DListViewLoadingMore loadingMoreState = DListViewLoadingMore.LV_NORMAL;// ���ظ���Ĭ��״̬.

	private final static int RATIO = 2;// �������������.

	private boolean mBack = false;// headView�Ƿ񷵻�.

	private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// ����ˢ�½ӿڣ��Զ��壩

	private boolean isScroller = true;// �Ƿ�����ListView������

	public VectorListView2(Context context) {
		super(context, null);
		initDragListView(context);
	}

	public VectorListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDragListView(context);
	}

	/**
	 * ���ü�������ˢ�·���
	 * 
	 * @param onRefreshLoadingMoreListener
	 *            �ص��ӿ�
	 */
	public void setOnRefreshListener(
			OnRefreshLoadingMoreListener onRefreshLoadingMoreListener) {
		this.onRefreshLoadingMoreListener = onRefreshLoadingMoreListener;
	}

	/***
	 * ��ʼ��ListView
	 */
	private void initDragListView(Context context) {

		String time = "1994.12.05";// ����ʱ��

		initHeadView(context, time);// ��ʼ����head.

		initLoadMoreView(context);// ��ʼ��footer

		setOnScrollListener(this);// ListView��������
	}

	/***
	 * ��ʼ��ͷ��HeadView
	 * 
	 * @param context
	 *            ������
	 * @param time
	 *            �ϴθ���ʱ��
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
		// ��ʾ�����¼�
		mTextViewLastUpdateDateTime.setText("�������:" + time);

		measureView(mHeadView);
		// ��ȡ��͸�
		mHeadViewWidth = mHeadView.getMeasuredWidth();
		mHeadViewHeight = mHeadView.getMeasuredHeight();

		addHeaderView(mHeadView, null, false);// ����ʼ�õ�ListView add����קListView
		// ����������Ҫ����headView���õ���������ʾλ��.
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

		initAnimation();// ��ʼ������
	}

	/***
	 * ��ʼ���ײ����ظ���ؼ�, ������style ��ָ��
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
	 * ��ʼ������
	 */
	private void initAnimation() {
		// ��ת����
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());// ����
		animation.setDuration(250);
		animation.setFillAfter(true);// ͣ�������״̬.
		// ������ת����
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true);
	}

	/***
	 * ���ã����� headView�Ŀ�͸�.
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
	 * touch �¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		// ����
		case MotionEvent.ACTION_DOWN:
			doActionDown(ev);
			break;
		// �ƶ�
		case MotionEvent.ACTION_MOVE:
			doActionMove(ev);
			break;
		// ̧��
		case MotionEvent.ACTION_UP:
			doActionUp(ev);
			break;
		default:
			break;
		}
		/***
		 * �����ListView�������������ô����true������ListView�������϶�.
		 * �������ListView����������ô���ø��෽���������Ϳ�������ִ��.
		 */
		if (isScroller) {
			return super.onTouchEvent(ev);
		} else {
			return true;
		}

	}

	/***
	 * ���²���
	 * 
	 * ���ã���ȡ�����ǵ�y����
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
	 * ��ק�ƶ�����
	 * 
	 * @param event
	 */
	void doActionMove(MotionEvent event) {
		mMoveY = (int) event.getY();// ��ȡʵʱ����y����
		// ����Ƿ���һ��touch�¼�.
		if (mIsRecordStartY == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecordStartY = true;
		}
		/***
		 * ���touch�رջ���������Loading״̬�Ļ� return.
		 */
		if (mIsRecordStartY == false
				|| mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// ������headview�ƶ�����Ϊy�ƶ���һ��.���Ƚ��Ѻã�
		int offset = (mMoveY - mStartY) / RATIO;

		switch (mlistViewState) {
		// ��ͨ״̬
		case LV_NORMAL: {
			// ���<0������ζ���ϻ���.
			if (offset > 0) {
				// ����headView��padding����.
				mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
				switchViewState(DListViewState.LV_PULL_REFRESH);// ����״̬
			}

		}
			break;
		// ����״̬
		case LV_PULL_REFRESH: {
			setSelection(0);// ʱʱ�����ڶ���.
			// ����headView��padding����.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			if (offset < 0) {
				/***
				 * Ҫ����ΪʲôisScroller = false;
				 */
				isScroller = false;
				switchViewState(DListViewState.LV_NORMAL);// ��ͨ״̬
				Log.e("jj", "isScroller=" + isScroller);
			} else if (offset > mHeadViewHeight) {// ���������offset����headView�ĸ߶���Ҫִ��ˢ��.
				switchViewState(DListViewState.LV_RELEASE_REFRESH);// ����Ϊ��ˢ�µ�����״̬.
			}
		}
			break;
		// ��ˢ��״̬
		case LV_RELEASE_REFRESH: {
			setSelection(0);// ʱʱ�����ڶ���
			// ����headView��padding����.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			// ����offset>0������û�г���headView�ĸ߶�.��ôҪgoback ԭװ.
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
	 * ����̧�����
	 * 
	 * @param event
	 */
	public void doActionUp(MotionEvent event) {
		mIsRecordStartY = false;// ��ʱ��touch�¼���ϣ�Ҫ�رա�
		isScroller = true;// ListView����Scrooler����.
		mBack = false;
		// �������״̬����loading״̬.
		if (mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// ������Ӧ״̬.
		switch (mlistViewState) {
		// ��ͨ״̬
		case LV_NORMAL:

			break;
		// ����״̬
		case LV_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			switchViewState(mlistViewState.LV_NORMAL);
			break;
		// ˢ��״̬
		case LV_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchViewState(mlistViewState.LV_LOADING);
			onRefresh();// ����ˢ��
			break;
		}

	}

	// �л�headview��ͼ
	private void switchViewState(DListViewState state) {

		switch (state) {
		// ��ͨ״̬
		case LV_NORMAL: {
			mImageViewArrow.clearAnimation();// �������
			mImageViewArrow.setImageResource(R.drawable.arrow);
		}
			break;
		// ����״̬
		case LV_PULL_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// ���ؽ�����
			mImageViewArrow.setVisibility(View.VISIBLE);// ����ͼ��
			mTextViewRefresh.setText("��������ˢ��");
			mImageViewArrow.clearAnimation();// �������

			// ���п�ˢ��״̬��LV_RELEASE_REFRESH��תΪ���״̬��ִ�У���ʵ��������������������ִ��.
			if (mBack) {
				mBack = false;
				mImageViewArrow.clearAnimation();// �������
				mImageViewArrow.startAnimation(reverseAnimation);// ������ת����
			}
		}
			break;
		// �ɿ�ˢ��״̬
		case LV_RELEASE_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// ���ؽ�����
			mImageViewArrow.setVisibility(View.VISIBLE);// ��ʾ����ͼ��
			mTextViewRefresh.setText("�ɿ���ȡ����");
			mImageViewArrow.clearAnimation();// �������
			mImageViewArrow.startAnimation(animation);// ��������
		}
			break;
		// ����״̬
		case LV_LOADING: {
			Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
			mHeadProgressBar.setVisibility(View.VISIBLE);
			mImageViewArrow.clearAnimation();
			mImageViewArrow.setVisibility(View.GONE);
			mTextViewRefresh.setText("������...");
		}
			break;
		default:
			return;
		}
		// �мǲ�Ҫ����ʱʱ����״̬��
		mlistViewState = state;

	}

	/***
	 * ����ˢ��
	 */
	private void onRefresh() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onRefresh();
		}
	}

	/***
	 * ����ˢ�����
	 */
	public void onRefreshComplete() {
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);// �ع�.
		switchViewState(mlistViewState.LV_NORMAL);//
	}

	/***
	 * ������ظ���
	 * 
	 * @param flag
	 *            �����Ƿ���ȫ���������
	 */
	public void onLoadMoreComplete(boolean flag) {
		if (flag) {
			updateLoadMoreViewState(DListViewLoadingMore.LV_OVER);
		} else {
			updateLoadMoreViewState(DListViewLoadingMore.LV_NORMAL);
		}

	}

	// ����Footview��ͼ
	private void updateLoadMoreViewState(DListViewLoadingMore state) {
		switch (state) {
		// ��ͨ״̬
		case LV_NORMAL:
			mFootLoadingMoreView.setVisibility(View.GONE);
			mFootLoadedMoreView.setVisibility(View.VISIBLE);
			break;
		// ������״̬
		case LV_LOADING:
			mFootLoadingMoreView.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setText("������...");
			mFootLoadedMoreView.setVisibility(View.GONE);
			break;
		// �������״̬
		case LV_OVER:
			mLoadingMorePB.setVisibility(View.GONE);
			mLoadMoreTextView.setText("�������");
			break;
		default:
			break;
		}
		loadingMoreState = state;
	}

	/***
	 * ListView ��������
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
	 * �ײ�����¼�
	 */
	@Override
	public void onClick(View v) {
		// ��ֹ�ظ����
		if (onRefreshLoadingMoreListener != null
				&& loadingMoreState == DListViewLoadingMore.LV_NORMAL) {
			updateLoadMoreViewState(DListViewLoadingMore.LV_LOADING);
			onRefreshLoadingMoreListener.onLoadMore();// �����ṩ�������ظ���.
		}

	}

	/***
	 * �Զ���ӿ�
	 */
	public interface OnRefreshLoadingMoreListener {
		/***
		 * // ����ˢ��ִ��
		 */
		void onRefresh();

		/***
		 * ������ظ���
		 */
		void onLoadMore();
	}
}
