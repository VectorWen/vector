package com.vector.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestLayouActy extends Activity implements OnClickListener{

	RelativeLayout rl_back,rl_true;
	TextView tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_layout_header);
		initLayout();
	}

	private void initLayout() {
		rl_back = (RelativeLayout) findViewById(R.id.all_layout_head_rl_back);
		rl_back.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 初次触摸(ACTION_DOWN操作)，滑动(ACTION_MOVE操作)和抬起(ACTION_UP)
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					System.out.println("Down");
					RelativeLayout rl_back = (RelativeLayout) v;
					rl_back.setBackgroundColor(0xff32789b);//透明度
				}
				if (event.getActionMasked() == MotionEvent.ACTION_UP) {
					System.out.println("Up");
					RelativeLayout rl_back = (RelativeLayout) v;
					rl_back.setBackgroundColor(0x0066cc);
				}
				return false;
			}
		});
		rl_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(this, "点击了", 1).show();
	}

}
