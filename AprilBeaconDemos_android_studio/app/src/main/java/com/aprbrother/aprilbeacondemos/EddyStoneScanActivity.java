package com.aprbrother.aprilbeacondemos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.aprbrother.aprilbeacondemo.R;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.EddyStoneListener;
import com.aprilbrother.aprilbrothersdk.EddyStone;

import java.util.ArrayList;
import java.util.Collections;

public class EddyStoneScanActivity extends Activity {
	BeaconManager manager;
	EddyStoneAdapter adapter;
	private ArrayList<EddyStone> eddyStones;
	private Button button;
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
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(button.getText().toString().equals("stopScan")){
					button.setText("startScan");
					manager.stopEddyStoneScan();
				}else{
					button.setText("stopScan");
					manager.startEddyStoneScan();
				}
			}
		});
	}

	@Override
	protected void onStart() {
		if(button.getText().toString().equals("startScan")){
			manager.startEddyStoneScan();
			button.setText("stopScan");
		}

		super.onStart();
	}

	@Override
	protected void onStop() {
		if(button.getText().toString().equals("stopScan")){
			manager.stopEddyStoneScan();
			button.setText("startScan");
		}
		super.onStop();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
