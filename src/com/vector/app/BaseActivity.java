package com.vector.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * �?��Activity的基�?
 * @author Jayin Ton
 *
 */
public abstract class BaseActivity extends Activity{
	
	
	protected abstract void initData();

	protected abstract void initLayout();

	/**
	 * 
	 * 以一个Class<?> cls 启动�?��Activity
	 * @param cls
	 */
	public void openActivity(Class<?> cls) {
		this.startActivity(new Intent(this, cls));
	}

	/**
	 * 以一个intent来启动一个Activity
	 * 
	 * @param intent
	 */
	public void openActivity(Intent intent) {
		this.startActivity(intent);
	}

	/**
	 * 封装了Toast，直接toast（String content�?
	 * 
	 * @param content
	 *            content of your want to Toast
	 */
	public void toast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 轻量封装findViewById(int id)
	 * 
	 * @param id
	 * @return 
	 */
	public View _getView(int id) {
		return this.findViewById(id);
	}

	/**
	 * 用class 包装�?��Intent;
	 * 
	 * @param cls
	 * @return 
	 */
	public Intent wrapIntent(Class<?> cls) {
		return new Intent(this, cls);
	}

	/**
	 * 关闭Activity
	 */
	public void closeActivity() {
		this.finish();
	}

	/**
	 * 获得这个Activity的Context
	 * 
	 * @return
	 */
	public Context getContext() {
		return this;
	}
	/**
	 * 调试
	 * @param content
	 */
	public void debug(String content){
		 Log.i("debug",this.getClass().getName()+":"+content);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
