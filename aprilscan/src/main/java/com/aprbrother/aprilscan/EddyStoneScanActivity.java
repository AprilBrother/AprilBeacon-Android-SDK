package com.aprbrother.aprilscan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.aprbrother.aprilbeaconscansdk.EddyStone;
import com.aprbrother.aprilbeaconscansdk.ScanManager;

import java.util.ArrayList;
import java.util.Collections;


public class EddyStoneScanActivity extends AppCompatActivity {
    ScanManager scanManager;
    EddyStoneAdapter adapter;
    private ArrayList<EddyStone> eddyStones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eddystonescan);
        eddyStones = new ArrayList<>();
        initView();
        initScan();
    }

    private void initScan() {
        scanManager = new ScanManager(this);
        scanManager.setScanListener(new ScanManager.MyScanListener() {
            @Override
            public void onEddyStoneDiscovered(EddyStone eddyStone) {
                if (!eddyStones.contains(eddyStone)) {
                    eddyStones.add(eddyStone);
                } else {
                    eddyStones.remove(eddyStone);
                    eddyStones.add(eddyStone);
                }
                ComparatorEddyStoneByRssi com = new ComparatorEddyStoneByRssi();
                Collections.sort(eddyStones, com);
                adapter.replaceWith(eddyStones);
            }
        });
    }

    private void initView() {
        ListView lv_eddystone = (ListView) findViewById(R.id.lv_eddystone);
        adapter = new EddyStoneAdapter(this);
        lv_eddystone.setAdapter(adapter);
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
