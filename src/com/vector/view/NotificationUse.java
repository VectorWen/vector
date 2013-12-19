package com.vector.view;

import com.vector.app.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Use 结尾的是我们使用这个类，二种使用方式，以后都是
 * <p>
 * 1. 默认直接使用，new 一个对象调用run() 方法就可以
 * </p>
 * <p>
 * 2. 含有设置的，在new 之后可以调用setXXX() 方法设置一些东西之后再run()，看需求而设置，一般不会很对，满足需求就可以了
 * </p>
 * <p>
 * 这个类是讲述Notification 怎么样使用的
 * </p>
 * 
 * @author vector
 * 
 */
public class NotificationUse {
	private Context ctx = null;
	Intent intent = null;
	//notification 实例
	private Notification notification = null;
	private Notification.Builder builder = null;
	//notification 管理者
	private NotificationManager manager = null;
	
	public NotificationUse(Context ctx, Intent intent) {
		this.ctx = ctx;
		this.intent = intent;
		PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
		manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new Notification.Builder(ctx);
		builder.setTicker("有新的通知了");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentText("通知的标题");
		builder.setContentText("Test Content");
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setContentIntent(pIntent);
		notification = builder.getNotification();
	}
	
	public void run() {
		manager.notify(1000,notification);
	}
	
}
