package com.aprilbrother.aprilbeacondemo;

import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.RangingListener;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics.MyReadCallBack;

public class NotifyService extends Service{

	private BeaconManager beaconManager;
	private static final Region ALL_BEACONS_REGION = new Region("apr", null,
			null, 65535);
	
//	private static final Region ALL_BEACONS_REGION_TEST = new Region("apr", "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0",
//			5, 5);
	
	public static final int READ_BATTERY = 0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		beaconManager = new BeaconManager(this);
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
			public void onExitedRegion(Region arg0) {
				generateNotification(getApplicationContext()," bye bye see you");
			}
			
			@Override
			public void onEnteredRegion(Region arg0, List<Beacon> beacons) {
				generateNotification(getApplicationContext(),"I am come in ! haha !");
			}
		});
		super.onCreate();
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
}
