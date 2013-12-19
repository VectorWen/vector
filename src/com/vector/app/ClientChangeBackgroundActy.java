package com.vector.app;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * 测试一个点击事件，当点下的时候改变背景，当放开的时候恢复原样，点击事件响应
 * @author vector
 *
 */
public class ClientChangeBackgroundActy extends BaseActivity implements OnTouchListener , OnClickListener{

	private View rl_head_back,rl_head_true;
	private TextView tv_head_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_layout_header);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		rl_head_back = _getView(R.id.all_layout_head_rl_back);
		rl_head_back.setOnTouchListener(this);
		rl_head_back.setOnClickListener(this);
		
		rl_head_true = _getView(R.id.all_layout_head_rl_true);
		rl_head_true.setOnTouchListener(this);
		rl_head_true.setOnClickListener(this);
		
		tv_head_title = (TextView) _getView(R.id.all_layout_head_tv_title);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.all_layout_head_rl_back:
			tv_head_title.setText("Back is clicked!");
			break;
			
		case R.id.all_layout_head_rl_true:
			tv_head_title.setText("True is clicked!");
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		// 初次触摸(ACTION_DOWN操作)，滑动(ACTION_MOVE操作)和抬起(ACTION_UP)
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			v.setBackgroundColor(0xff32789b);//透明度+红+绿+蓝
		}
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			v.setBackgroundColor(0x0066cc);
		}
		
		return false;
	}

}
