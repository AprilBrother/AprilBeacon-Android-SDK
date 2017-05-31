package com.aprbrother.aprilbeacondemos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aprbrother.aprilbeacondemo.R;
import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.RangingListener;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.utils.AprilL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 搜索展示beacon列表 scan beacon show beacon list
 */
public class BeaconList extends Activity {
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final String TAG = "BeaconList";
    private static final Region ALL_BEACONS_REGION = new Region(
            "customRegionName", null, null, null);
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
        AprilL.enableDebugLogging(true);
        beaconManager = new BeaconManager(getApplicationContext());

        // 此处上下文需要是Activity或者Serivce
        // 若在FragmentActivit或者其他Activity子类中使用 上下文请使用getApplicationContext
        // beaconManager.setForegroundScanPeriod(1000, 1000);
        beaconManager.setRangingListener(new RangingListener() {

            @Override
            public void onBeaconsDiscovered(Region region,
                                            final List<Beacon> beacons) {
                Log.i(TAG, "onBeaconsDiscovered: ");
                for (Beacon beacon : beacons) {
                    if (beacon.getRssi() > 0) {
                        Log.i(TAG, "rssi = " + beacon.getRssi());
                        Log.i(TAG, "mac = " + beacon.getMacAddress());
                    }
                }

                Log.i(TAG, "------------------------------beacons.size = " + beacons.size());

                myBeacons.clear();
                myBeacons.addAll(beacons);
                getActionBar().setSubtitle("Found beacons: " + beacons.size());
                ComparatorBeaconByRssi com = new ComparatorBeaconByRssi();
                Collections.sort(myBeacons, com);
                adapter.replaceWith(myBeacons);
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

        final TextView tv = (TextView) findViewById(R.id.tv_swith);
        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tv.getText().equals("开启扫描")) {
                    try {
                        tv.setText("停止扫描");
                        beaconManager.startRanging(ALL_BEACONS_REGION);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        tv.setText("开启扫描");
                        beaconManager.stopRanging(ALL_BEACONS_REGION);
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        final TextView tv_scan_eddystone = (TextView) findViewById(R.id.tv_scan_eddystone);
        tv_scan_eddystone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BeaconList.this,
                        EddyStoneScanActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 连接服务 开始搜索beacon connect service start scan beacons
     */
    private void connectToService() {
        Log.i(TAG, "connectToService");
        getActionBar().setSubtitle("Scanning...");
        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    Log.i(TAG, "connectToService");
                    beaconManager.startRanging(ALL_BEACONS_REGION);
                    // beaconManager.startMonitoring(ALL_BEACONS_REGION);
                } catch (RemoteException e) {
                    e.printStackTrace();
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
        Log.i(TAG, "onStart");
        // if (!beaconManager.hasBluetooth()) {
        // Toast.makeText(this, "Device does not have Bluetooth Low Energy",
        // Toast.LENGTH_LONG).show();
        // Log.i(TAG, "!hasBluetooth");
        // return;
        // }
        // if (!beaconManager.isBluetoothEnabled()) {
        // Log.i(TAG, "!isBluetoothEnabled");
        // Intent enableBtIntent = new Intent(
        // BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        // } else {
        // connectToService();
        // }
        connectToService();
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
            beaconManager.disconnect();
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
        super.onStop();
    }
}
