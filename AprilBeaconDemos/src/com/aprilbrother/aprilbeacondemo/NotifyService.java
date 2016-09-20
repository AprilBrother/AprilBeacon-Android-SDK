package com.aprilbrother.aprilbeacondemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.Region;

public class NotifyService extends Service {
	private final static String sample_url = "http://codeversed.com/androidifysteve.png";
	private BeaconManager beaconManager;
	private static final Region ALL_BEACONS_REGION = new Region("apr", null,
			null, null);

	// private static final Region ALL_BEACONS_REGION = new Region("apr",
	// "b9407f30-f5f8-466e-aff9-25556b57fe6d",54910,27038);
	private static NotificationManager mNotificationManager;

	// private static final Region ALL_BEACONS_REGION = new Region("apr",
	// "999557e7-23e4-4bed-988a-a02fe47f9888",
	// 1, 14);
	// private static final Region ALL_BEACONS_REGION_TEST = new Region("apr",
	// "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0",
	// 5, 5);
//	 private static final Region ALL_BEACONS_REGION = new Region("apr",
//	 "FDA50693-A4E2-4FB1-AFCF-C6EB07647825",
//	 555, 566);

	public static final int READ_BATTERY = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		beaconManager = new BeaconManager(this);
		beaconManager.setMonitoringExpirationMill(20L);
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startMonitoring(ALL_BEACONS_REGION);
				} catch (RemoteException e) {

				}
			}
		});

		beaconManager.setMonitoringListener(new MonitoringListener() {

			@Override
			public void onExitedRegion(Region region) {
				Log.i("Test", "onExitedRegion");
				generateNotification(getApplicationContext(),
						" bye bye see you");
				// new CreateNotification("bye bye see you").execute();
			}

			@Override
			public void onEnteredRegion(Region region, List<Beacon> beacons) {
				Log.i("Test", "onEnteredRegion");
				generateNotification(getApplicationContext(),
						"I am come in ! haha !");
				// new CreateNotification("I am come in ! haha !").execute();
//				new NetAsyncTask().execute();
			}
		});
		super.onCreate();
	}

	/**
	 * Notification AsyncTask to create and return the requested notification.
	 * 
	 * @see CreateNotification#CreateNotification(int)
	 */
	public class CreateNotification extends AsyncTask<Void, Void, Void> {

		String content;

		/**
		 * Main constructor for AsyncTask that accepts the parameters below.
		 * 
		 * @param style
		 *            {@link #NORMAL}, {@link #BIG_TEXT_STYLE},
		 *            {@link #BIG_PICTURE_STYLE}, {@link #INBOX_STYLE}
		 * @see #doInBackground
		 */
		public CreateNotification(String content) {
			this.content = content;
		}

		/**
		 * Creates the notification object.
		 * 
		 * @see #setNormalNotification
		 * @see #setBigTextStyleNotification
		 * @see #setBigPictureStyleNotification
		 * @see #setInboxStyleNotification
		 */
		@Override
		protected Void doInBackground(Void... params) {
			Notification noti = new Notification();
			noti = setNormalNotification(content);
			// noti = setBigTextStyleNotification(content);
			// noti = setBigPictureStyleNotification(content);
			// noti = setInboxStyleNotification(content);
			// noti = setCustomViewNotification(content);
			noti.defaults |= Notification.DEFAULT_LIGHTS;
			noti.defaults |= Notification.DEFAULT_VIBRATE;
			noti.defaults |= Notification.DEFAULT_SOUND;
			noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

			mNotificationManager.notify(0, noti);

			return null;

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private static void generateNotification(Context context, String message) {
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
						.setContentTitle(context.getString(R.string.app_name))
						.setContentText(message)
						.setContentIntent(
								PendingIntent.getActivity(context, 0,
										launchIntent, 0)).setAutoCancel(true)
						.build());

	}

	/**
	 * Normal Notification
	 * 
	 * @return Notification
	 * @see CreateNotification
	 */
	private Notification setNormalNotification(String content) {
		Bitmap remote_picture = null;

		try {
			remote_picture = BitmapFactory.decodeStream((InputStream) new URL(
					sample_url).getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Setup an explicit intent for an ResultActivity to receive.
		Intent resultIntent = new Intent(this, ResultActivity.class);

		// TaskStackBuilder ensures that the back button follows the recommended
		// convention for the back key.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent (but not the Intent itself).
		stackBuilder.addParentStack(ResultActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack.
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.setLargeIcon(remote_picture)
				.setContentIntent(resultPendingIntent)
				// .addAction(R.drawable.ic_launcher, "One",
				// resultPendingIntent)
				// .addAction(R.drawable.ic_launcher, "Two",
				// resultPendingIntent)
				// .addAction(R.drawable.ic_launcher, "Three",
				// resultPendingIntent)
				.setContentTitle("Normal Notification").setContentText(content)
				.build();
	}

	/**
	 * Big Text Style Notification
	 * 
	 * @return Notification
	 * @see CreateNotification
	 */
	private Notification setBigTextStyleNotification(String content) {
		Bitmap remote_picture = null;

		// Create the style object with BigTextStyle subclass.
		NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
		notiStyle.setBigContentTitle("Big Text Expanded");
		notiStyle.setSummaryText("Nice big text.");

		try {
			remote_picture = BitmapFactory.decodeStream((InputStream) new URL(
					sample_url).getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add the big text to the style.
		CharSequence bigText = "This is an example of a large string to demo how much "
				+ "text you can show in a 'Big Text Style' notification.";
		notiStyle.bigText(bigText);

		// Creates an explicit intent for an ResultActivity to receive.
		Intent resultIntent = new Intent(this, ResultActivity.class);

		// This ensures that the back button follows the recommended convention
		// for the back key.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent (but not the Intent itself).
		stackBuilder.addParentStack(ResultActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack.
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.setLargeIcon(remote_picture)
				.setContentIntent(resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "One", resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "Two", resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "Three", resultPendingIntent)
				.setContentTitle("Big Text Normal").setContentText(content)
				.setStyle(notiStyle).build();
	}

	/**
	 * Big Picture Style Notification
	 * 
	 * @return Notification
	 * @see CreateNotification
	 */
	private Notification setBigPictureStyleNotification(String content) {
		Bitmap remote_picture = null;

		// Create the style object with BigPictureStyle subclass.
		NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
		notiStyle.setBigContentTitle("Big Picture Expanded");
		notiStyle.setSummaryText("Nice big picture.");

		try {
			remote_picture = BitmapFactory.decodeStream((InputStream) new URL(
					sample_url).getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add the big picture to the style.
		notiStyle.bigPicture(remote_picture);

		// Creates an explicit intent for an ResultActivity to receive.
		Intent resultIntent = new Intent(this, ResultActivity.class);

		// This ensures that the back button follows the recommended convention
		// for the back key.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent (but not the Intent itself).
		stackBuilder.addParentStack(ResultActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack.
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.setLargeIcon(remote_picture)
				.setContentIntent(resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "One", resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "Two", resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "Three", resultPendingIntent)
				.setContentTitle("Big Picture Normal").setContentText(content)
				.setStyle(notiStyle).build();
	}

	/**
	 * Inbox Style Notification
	 * 
	 * @return Notification
	 * @see CreateNotification
	 */
	private Notification setInboxStyleNotification(String content) {
		Bitmap remote_picture = null;

		// Create the style object with InboxStyle subclass.
		NotificationCompat.InboxStyle notiStyle = new NotificationCompat.InboxStyle();
		notiStyle.setBigContentTitle("Inbox Style Expanded");

		// Add the multiple lines to the style.
		// This is strictly for providing an example of multiple lines.
		for (int i = 0; i < 5; i++) {
			notiStyle.addLine("(" + i + " of 6) Line one here.");
		}
		notiStyle.setSummaryText("+2 more Line Samples");

		try {
			remote_picture = BitmapFactory.decodeStream((InputStream) new URL(
					sample_url).getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creates an explicit intent for an ResultActivity to receive.
		Intent resultIntent = new Intent(this, ResultActivity.class);

		// This ensures that the back button follows the recommended convention
		// for the back key.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent (but not the Intent itself).
		stackBuilder.addParentStack(ResultActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack.
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.setLargeIcon(remote_picture)
				.setContentIntent(resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "One", resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "Two", resultPendingIntent)
				.addAction(R.drawable.ic_launcher, "Three", resultPendingIntent)
				.setContentTitle("Inbox Style Normal").setContentText(content)
				.setStyle(notiStyle).build();
	}

	/**
	 * Custom View Notification
	 * 
	 * @return Notification
	 * @see CreateNotification
	 */
	private Notification setCustomViewNotification(String content) {

		// Creates an explicit intent for an ResultActivity to receive.
		Intent resultIntent = new Intent(this, ResultActivity.class);

		// This ensures that the back button follows the recommended convention
		// for the back key.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(ResultActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack.
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Create remote view and set bigContentView.
		RemoteViews expandedView = new RemoteViews(this.getPackageName(),
				R.layout.notification_custom_remote);
		expandedView.setTextViewText(R.id.text_view, "Neat logo!");

		Notification notification = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
				.setContentIntent(resultPendingIntent)
				.setContentTitle("Custom View").build();

		// notification.bigContentView = expandedView;
		// notification.contentView = expandedView;

		return notification;
	}
}
