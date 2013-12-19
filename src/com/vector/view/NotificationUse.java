package com.vector.view;

import com.vector.app.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Use ��β��������ʹ������࣬����ʹ�÷�ʽ���Ժ���
 * <p>
 * 1. Ĭ��ֱ��ʹ�ã�new һ���������run() �����Ϳ���
 * </p>
 * <p>
 * 2. �������õģ���new ֮����Ե���setXXX() ��������һЩ����֮����run()������������ã�һ�㲻��ܶԣ���������Ϳ�����
 * </p>
 * <p>
 * ������ǽ���Notification ��ô��ʹ�õ�
 * </p>
 * 
 * @author vector
 * 
 */
public class NotificationUse {
	private Context ctx = null;
	Intent intent = null;
	//notification ʵ��
	private Notification notification = null;
	private Notification.Builder builder = null;
	//notification ������
	private NotificationManager manager = null;
	
	public NotificationUse(Context ctx, Intent intent) {
		this.ctx = ctx;
		this.intent = intent;
		PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
		manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new Notification.Builder(ctx);
		builder.setTicker("���µ�֪ͨ��");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentText("֪ͨ�ı���");
		builder.setContentText("Test Content");
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setContentIntent(pIntent);
		notification = builder.getNotification();
	}
	
	public void run() {
		manager.notify(1000,notification);
	}
	
}
