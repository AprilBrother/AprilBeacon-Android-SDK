package com.aprilbrother.aprilbeacondemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.RangingListener;
import com.aprilbrother.aprilbrothersdk.Region;

/**
 * 搜索展示beacon列表 scan beacon show beacon list
 */
public class BeaconList extends Activity {
	private static final int REQUEST_ENABLE_BT = 1234;
	private static final String TAG = "BeaconList";
	// private static final Region ALL_BEACONS_REGION = new Region("",
	// "e2c56db5-dffb-48d2-b060-d0f5a71096e3",
	// null, null);

	private static final Region ALL_BEACONS_REGION = new Region("apr", null,
			null, null);
	// private static final Region ALL_BEACONS_REGION = new Region("apr",
	// "e2c56db5-dffb-48d2-b060-d0f5a71096e0",
	// 985,211);
	// 扫描所有uuid为"aa000000-0000-0000-0000-000000000000"的beacon
	// private static final Region ALL_BEACONS_REGION = new Region("apr",
	// "aa000000-0000-0000-0000-000000000000",
	// null, null);
	private BeaconAdapter adapter;
	private BeaconManager beaconManager;
	private ArrayList<Beacon> myBeacons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		myBeacons = new ArrayList<Beacon>();
		ListView lv = (ListView) findViewById(R.id.lv);
		adapter = new BeaconAdapter(this);
		lv.setAdapter(adapter);

		beaconManager = new BeaconManager(this);
		// beaconManager.setMonitoringExpirationMill(10L);
		// beaconManager.setRangingExpirationMill(10L);
		beaconManager.setForegroundScanPeriod(2000, 0);

		beaconManager.setRangingListener(new RangingListener() {

			@Override
			public void onBeaconsDiscovered(Region region,
					final List<Beacon> beacons) {
				myBeacons.clear();
				myBeacons.addAll(beacons);
				if (beacons != null && beacons.size() > 0) {
					for (Beacon beacon : beacons) {
						Log.i(TAG, "mac = " + beacon.getMacAddress()
								+ "+++major = " + beacon.getMajor()
								+ "+++minor = " + beacon.getMinor());
						Log.i(TAG, "power = " + beacon.getPower());
					}
					Log.i(TAG, "-------------------------------");
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActionBar().setSubtitle(
								"Found beacons: " + beacons.size());
						ComparatorBeaconByRssi com = new ComparatorBeaconByRssi();
						Collections.sort(myBeacons, com);
						adapter.replaceWith(myBeacons);
					}
				});
			}
		});

		beaconManager.setMonitoringListener(new MonitoringListener() {

			@Override
			public void onExitedRegion(Region arg0) {
				Toast.makeText(BeaconList.this, "Notify in", 0).show();

			}

			@Override
			public void onEnteredRegion(Region arg0, List<Beacon> arg1) {
				Toast.makeText(BeaconList.this, "Notify out", 0).show();
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Beacon beacon = myBeacons.get(arg2);
				Intent intent;
				if (beacon.getName().contains("ABSensor")) {
					intent = new Intent(BeaconList.this, SensorActivity.class);
				} else {
					intent = new Intent(BeaconList.this, ModifyActivity.class);
					
				}
				Bundle bundle = new Bundle();
				bundle.putParcelable("beacon", beacon);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	/**
	 * 连接服务 开始搜索beacon connect service start scan beacons
	 */
	private void connectToService() {
		getActionBar().setSubtitle("Scanning...");
		adapter.replaceWith(Collections.<Beacon> emptyList());
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_BEACONS_REGION);
					// beaconManager.startMonitoring(ALL_BEACONS_REGION);
				} catch (RemoteException e) {

				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG)
						.show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		try {
			myBeacons.clear();
			beaconManager.stopRanging(ALL_BEACONS_REGION);
		} catch (RemoteException e) {
			Log.d(TAG, "Error while stopping ranging", e);
		}
		super.onStop();
	}
}
