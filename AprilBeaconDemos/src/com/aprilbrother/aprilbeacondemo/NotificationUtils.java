package com.aprilbrother.aprilbeacondemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {
	
	public static void generateNotification(Context context, String message, String title) {
		Intent launchIntent = new Intent(context, NotifyInContentActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		((NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
				0,
				new NotificationCompat.Builder(context)
						.setWhen(System.currentTimeMillis())
						.setSmallIcon(R.drawable.ic_launcher)
						.setTicker(message)
						.setContentTitle(title)
						.setContentText(message)
						.setContentIntent(
								PendingIntent.getActivity(context, 0,
										launchIntent, 0)).setAutoCancel(true)
						.build());

	}
}
