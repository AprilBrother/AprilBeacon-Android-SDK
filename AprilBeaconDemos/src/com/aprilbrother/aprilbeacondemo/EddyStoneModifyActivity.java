package com.aprilbrother.aprilbeacondemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.EddyStone;
import com.aprilbrother.aprilbrothersdk.connection.EddyStoneConnection;
import com.aprilbrother.aprilbrothersdk.connection.EddyStoneConnection.EddyStoneWriteCallBack;

public class EddyStoneModifyActivity extends Activity implements
		OnClickListener {

	EddyStone eddyStone;

	EddyStoneConnection conn;

	private EditText et_url;

	private EditText et_uid;
	
	private String password = "195660";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eddystone_modify);
		eddyStone = (EddyStone) getIntent().getSerializableExtra("eddystone");
		initView();
	}

	private void initView() {
		TextView model = (TextView) findViewById(R.id.tv_model);
		TextView changeModelToIBeacon = (TextView) findViewById(R.id.tv_change_model_to_ibeacon);
		TextView changeModelToUrl = (TextView) findViewById(R.id.tv_change_model_to_url);
		TextView changeModelToUid = (TextView) findViewById(R.id.tv_change_model_to_uid);
		TextView changeUrl = (TextView) findViewById(R.id.tv_change_url);
		TextView changeUid = (TextView) findViewById(R.id.tv_change_uid);
		model.setText("当前模式：" + eddyStone.getModel());
		changeModelToIBeacon.setOnClickListener(this);
		changeModelToUrl.setOnClickListener(this);
		changeModelToUid.setOnClickListener(this);
		changeUrl.setOnClickListener(this);
		changeUid.setOnClickListener(this);
		et_url = (EditText)findViewById(R.id.et_url);
		et_uid = (EditText)findViewById(R.id.et_uid);
		et_url.setText("http://abc.com");
		et_uid.setText("12345678-1234-1234-1234-12345678901234");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_change_model_to_ibeacon:
			changeModelToIBeacon();
			break;
		case R.id.tv_change_model_to_url:
			changeModelToUrl();
			break;
		case R.id.tv_change_model_to_uid:
			changeModelToUid();
			break;
		case R.id.tv_change_url:
			changeUrl();
			break;
		case R.id.tv_change_uid:
			changeUid();
			break;
		default:
			break;
		}
	}

	private void changeUid() {
		conn = new EddyStoneConnection(eddyStone.getName(),
				eddyStone.getMacAddress(), eddyStone.getModel(), this);
		String uid = et_uid.getText().toString().trim();
		conn.changeUid(uid);
		conn.connectGattToWrite(password, new EddyStoneWriteCallBack() {

			@Override
			public void onWriteSuccess() {
				Toast.makeText(EddyStoneModifyActivity.this, "UID更改成功",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void changeUrl() {
		conn = new EddyStoneConnection(eddyStone.getName(),
				eddyStone.getMacAddress(), eddyStone.getModel(), this);
		String url = et_url.getText().toString().trim();
		conn.changeUrl(url);
		conn.connectGattToWrite(password, new EddyStoneWriteCallBack() {

			@Override
			public void onWriteSuccess() {
				Toast.makeText(EddyStoneModifyActivity.this, "URL更改成功",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void changeModelToUid() {
		// TODO 更改为uid模式
		changeModel("uid");
	}

	private void changeModelToUrl() {
		// TODO 更改为url模式
		changeModel("url");
	}

	private void changeModelToIBeacon() {
		// TODO 更改为iBeacon模式
		changeModel("iBeacon");
	}

	private void changeModel(String model) {
		conn = new EddyStoneConnection(eddyStone.getName(),
				eddyStone.getMacAddress(), eddyStone.getModel(), this);
		conn.changerModel(model);
		conn.connectGattToWrite(password, new EddyStoneWriteCallBack() {

			@Override
			public void onWriteSuccess() {
				Toast.makeText(EddyStoneModifyActivity.this, "模式更改成功",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onStop() {
		if(conn!=null){
			conn.close();
			conn = null;
		}
		super.onStop();
	}
	
}
