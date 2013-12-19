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
 * mDialog.setContentView(layout); ��Ҫ��������������ò���
 * �ҷ�װ��Dialog , ��������صģ��Ժ���ľͿ��Ը�
 * @author vector
 *
 */
public class VectorDialog implements OnClickListener {

	/**
	 * Ҫ��ʾ�Ļ���dialog������show() �Ϳ�����
	 */
	private Dialog mDialog;
	/**
	 * Ҫ���¼��Ŀؼ�
	 */
	private RelativeLayout rl_dialog_item;
	private ImageView ivOut;

	private Context context;
	
	/**
	 * ������ʱ��ͳ�ʼ������
	 * @param context
	 */
	public VectorDialog(Context context) {
		this.context = context;
		
		/**
		 * �����Ի���
		 */
		mDialog = new AlertDialog.Builder(context).create();
		//�����Ϳ�����
		mDialog.show();
		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog, null);
		rl_dialog_item = (RelativeLayout) layout.findViewById(R.id.rl_dialog_item);
		ivOut = (ImageView) layout.findViewById(R.id.iv_dialog_tui);
		
		// Ϊ��ť�����¼�����
		rl_dialog_item.setOnClickListener(this);
		ivOut.setOnClickListener(this);
		
		/**
		 * �����Զ���Ĳ���
		 */
		mDialog.setContentView(layout);
	}

	/**
	 * �¼��ӿ�
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

	// ������ť
	public void setLeftButton(String value, MyDialogInt iBack) {
		if (value == null || value.equals("")) {
			return;
		}
		LeftcallBack = iBack;
	}

	// �����Ұ�ť
	public void setRightButton(String value, MyDialogInt iBack) {
		if (value == null || value.equals("")) {
			return;
		}
		RightcallBack = iBack;
	}
	/**
	 * �Լ���װ�ľ��ṩһЩ�������˼����ã����ã�ȫ�����Լ�����
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
			Toast.makeText(context, "�������ˣ�", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		
	}

}
