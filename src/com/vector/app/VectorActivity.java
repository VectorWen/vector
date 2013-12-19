package com.vector.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 跳转的页面，如果增加了一个activity，你需要增加一个跳转Button，和在androidmanifest.xml 配置
 * 
 * @author vector
 * 
 */
public class VectorActivity extends BaseActivity implements OnClickListener {
	/**
	 * 跳转的Button
	 */
	private Button btn_ViewPager, btnListView,btnTestLayout,btnDialog,btnClickChangeBackground;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		btn_ViewPager = (Button) _getView(R.id.btn_ViewPager);
		btn_ViewPager.setOnClickListener(this);

		btnListView = (Button) _getView(R.id.btn_listview);
		btnListView.setOnClickListener(this);
		
		btnTestLayout = (Button) _getView(R.id.btn_testlayout);
		btnTestLayout.setOnClickListener(this);
		
		btnDialog = (Button) _getView(R.id.btn_testdialog);
		btnDialog.setOnClickListener(this);
		
		btnClickChangeBackground = (Button) _getView(R.id.main_acty_clickchangebackground);
		btnClickChangeBackground.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {
		case R.id.btn_ViewPager:
			intent.setClass(VectorActivity.this, MoveViewPager.class);
			startActivity(intent);
			break;
			
		case R.id.btn_listview:
			intent.setClass(VectorActivity.this, ListViewActy.class);
			startActivity(intent);
			break;
			
		case R.id.btn_testlayout:
			intent.setClass(VectorActivity.this, TestLayouActy.class);
			startActivity(intent);
			break;
			
		case R.id.btn_testdialog:
			intent.setClass(VectorActivity.this, DialogActy.class);
			startActivity(intent);
			break;
			
		case R.id.main_acty_clickchangebackground:
			intent.setClass(VectorActivity.this, ClientChangeBackgroundActy.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}