package com.vector.app;

import com.vector.view.VectorDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ��ʾdialog ���÷���show() ֮����ǿ����ˣ�����а�ť�����°�ť�Ϳ������ˣ�֮��ص�һ��������
 * dialog.cancel(); �������ǿ�������
 * 
 * @author vector
 * 
 */
public class DialogActy extends BaseActivity implements OnClickListener {

	private Button btn_dialog_one,btn_dialog_twe;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogtest);
		initData();
		initLayout();
	}

	protected void initData() {

	}

	protected void initLayout() {
		btn_dialog_one = (Button) _getView(R.id.btn_dialog_one);
		btn_dialog_one.setOnClickListener(this);
		btn_dialog_twe = (Button) _getView(R.id.btn_dialog_twe);
		btn_dialog_twe.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_dialog_one:
			one();
			break;

		case R.id.btn_dialog_twe:
			twe();
			break;
			
		default:
			break;
		}
	}

	private void twe() {
		VectorDialog dialog = new VectorDialog(this);
	}
	/**
	 * ��һ���÷�
	 */
	private void one() {
		/**
		 * ��������
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(DialogActy.this);
		builder.setMessage("ȷ���˳���");
		builder.setTitle("��ʾ");
		/**
		 * ���Ӱ�ť��ֱ���ɻص�������
		 */
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				toast("ok");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		//dialog.cancel();
	}
}
