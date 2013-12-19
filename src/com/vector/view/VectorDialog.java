package com.vector.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vector.app.R;

/**
 * mDialog.setContentView(layout); 主要是这个方法，设置布局
 * 我封装的Dialog , 和需求相关的，以后想改就可以改
 * @author vector
 *
 */
public class VectorDialog implements OnClickListener {

	/**
	 * 要显示的还是dialog，创建show() 就看见了
	 */
	private Dialog mDialog;
	/**
	 * 要绑定事件的控件
	 */
	private RelativeLayout rl_dialog_item;
	private ImageView ivOut;

	private Context context;
	
	/**
	 * 创建的时候就初始化布局
	 * @param context
	 */
	public VectorDialog(Context context) {
		this.context = context;
		
		/**
		 * 创建对话框
		 */
		mDialog = new AlertDialog.Builder(context).create();
		//创建就看见了
		mDialog.show();
		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog, null);
		rl_dialog_item = (RelativeLayout) layout.findViewById(R.id.rl_dialog_item);
		ivOut = (ImageView) layout.findViewById(R.id.iv_dialog_tui);
		
		// 为按钮设置事件监听
		rl_dialog_item.setOnClickListener(this);
		ivOut.setOnClickListener(this);
		
		/**
		 * 设置自动义的布局
		 */
		mDialog.setContentView(layout);
	}

	/**
	 * 事件接口
	 * @author vector
	 *
	 */
	public interface MyDialogInt {
		public void onClick(View view);
	}

	MyDialogInt LeftcallBack = null;
	MyDialogInt RightcallBack = null;

	public void dismiss() {
		mDialog.dismiss();
	}

	public boolean isShowing() {
		return mDialog.isShowing();
	}

	// 设置左按钮
	public void setLeftButton(String value, MyDialogInt iBack) {
		if (value == null || value.equals("")) {
			return;
		}
		LeftcallBack = iBack;
	}

	// 设置右按钮
	public void setRightButton(String value, MyDialogInt iBack) {
		if (value == null || value.equals("")) {
			return;
		}
		RightcallBack = iBack;
	}
	/**
	 * 自己封装的就提供一些方法给人家设置，调用，全部是自己定的
	 * @param title
	 */
	public void setTitle(String title) {
	}

	public void setMessage(String message) {
	}

	public void onClick(View view) {
		mDialog.cancel();
		int id = view.getId();
		switch (id) {
		case R.id.iv_dialog_tui:
			Toast.makeText(context, "XXX", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.rl_dialog_item:
			Toast.makeText(context, "好像点击了！", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		
	}

}
