package com.aprilbrother.aprilbeacondemo;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.EddyStoneListener;
import com.aprilbrother.aprilbrothersdk.EddyStone;

public class EddyStoneScanActivity extends Activity {
	BeaconManager manager;
	EddyStoneAdapter adapter;
	private ArrayList<EddyStone> eddyStones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eddystonescan);
		eddyStones = new ArrayList<EddyStone>();
		initView();
		initScan();
	}

	private void initScan() {
		manager = new BeaconManager(this);
		manager.setEddyStoneListener(new EddyStoneListener() {

			@Override
			public void onEddyStoneDiscovered(EddyStone eddyStone) {
				refreshList(eddyStone);
			}

			private void refreshList(EddyStone eddyStone) {
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
		lv_eddystone.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(EddyStoneScanActivity.this,EddyStoneModifyActivity.class);
				intent.putExtra("eddystone", eddyStones.get(position));
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onStart() {
		manager.startEddyStoneScan();
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		manager.stopEddyStoneScan();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
