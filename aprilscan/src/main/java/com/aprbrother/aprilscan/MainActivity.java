package com.aprbrother.aprilscan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aprbrother.aprilbeaconscansdk.Beacon;
import com.aprbrother.aprilbeaconscansdk.ScanManager;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BeaconAdapter adapter;
    private ArrayList<Beacon> myBeacons;
    private ScanManager scanManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        startScan();
    }

    private void startScan() {
        scanManager = new ScanManager(this);
        scanManager.setScanListener(new ScanManager.MyScanListener() {
            @Override
            public void onScanListenre(ArrayList<Beacon> beacons) {
                Log.i(TAG, "------------------------------beacons.size = " + beacons.size());
                myBeacons.clear();
                myBeacons.addAll(beacons);
                ComparatorBeaconByRssi com = new ComparatorBeaconByRssi();
                Collections.sort(myBeacons, com);
                adapter.replaceWith(myBeacons);
            }
        });
    }

    private void initView() {
        myBeacons = new ArrayList<Beacon>();
        ListView lv = (ListView) findViewById(R.id.lv);
        adapter = new BeaconAdapter(this);
        lv.setAdapter(adapter);
        TextView tv = (TextView) findViewById(R.id.tv_scan_eddystone);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        EddyStoneScanActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanManager.startScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanManager.stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
