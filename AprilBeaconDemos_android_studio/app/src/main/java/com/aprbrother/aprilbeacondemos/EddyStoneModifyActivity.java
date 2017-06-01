package com.aprbrother.aprilbeacondemos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aprbrother.aprilbeacondemo.R;
import com.aprilbrother.aprilbrothersdk.EddyStone;
import com.aprilbrother.aprilbrothersdk.connection.EddyStoneConnection;
import com.aprilbrother.aprilbrothersdk.connection.EddyStoneConnection.EddyStoneWriteCallBack;

public class EddyStoneModifyActivity extends Activity implements
		OnClickListener {

	EddyStone eddyStone;

	EddyStoneConnection conn;

	private EditText et_url;

	private EditText et_uid;
	private EditText et_uid_name_space;
	private EditText et_uid_custom;

//	private String password = "PROXIoTSetup";
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
		TextView changeModelToAll = (TextView) findViewById(R.id.tv_change_model_to_all);

		TextView changeUrl = (TextView) findViewById(R.id.tv_change_url);
		TextView changeUid = (TextView) findViewById(R.id.tv_change_uid);
		TextView changeUidNameSpace = (TextView) findViewById(R.id.tv_change_uid_name_space);
		TextView changeUidCustom = (TextView) findViewById(R.id.tv_change_uid_custom);
		model.setText(getResources().getString(R.string.current_model)
				+ eddyStone.getModel());
		changeModelToIBeacon.setOnClickListener(this);
		changeModelToUrl.setOnClickListener(this);
		changeModelToUid.setOnClickListener(this);
		changeModelToAll.setOnClickListener(this);
		changeUrl.setOnClickListener(this);
		changeUid.setOnClickListener(this);
		changeUidNameSpace.setOnClickListener(this);
		changeUidCustom.setOnClickListener(this);
		et_url = (EditText) findViewById(R.id.et_url);
		et_uid = (EditText) findViewById(R.id.et_uid);
		et_uid_name_space = (EditText) findViewById(R.id.et_uid_name_space);
		et_uid_custom = (EditText) findViewById(R.id.et_uid_custom);
		et_url.setText("http://abc.com");
		et_uid.setText("12345678-1234-1234-1234-12345678901234");
		et_uid_name_space.setText("11223344556677889900");
		et_uid_custom.setText("112233445566");
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
		case R.id.tv_change_model_to_all:
			changeModelToAll();
			break;
		case R.id.tv_change_url:
			changeUrl();
			break;
		case R.id.tv_change_uid:
			changeUid();
			break;
		case R.id.tv_change_uid_name_space:
			changeNameSpace();
			break;
		case R.id.tv_change_uid_custom:
			changeUidCustom();
			break;
		default:
			break;
		}
	}

	private void changeUidCustom() {
		conn = new EddyStoneConnection(eddyStone.getName(),
				eddyStone.getMacAddress(), eddyStone.getModel(), this);
		String uid_custom = et_uid_custom.getText().toString().trim();
		conn.changeUidCustom(uid_custom);
		conn.connectGattToWrite(password, new EddyStoneWriteCallBack() {

			@Override
			public void onWriteSuccess() {
				Toast.makeText(EddyStoneModifyActivity.this, "UID CUSTOM change success",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void changeNameSpace() {
		conn = new EddyStoneConnection(eddyStone.getName(),
				eddyStone.getMacAddress(), eddyStone.getModel(), this);
		String uid_name_space = et_uid_name_space.getText().toString().trim();
		conn.changeUidNameSpace(uid_name_space);
		conn.connectGattToWrite(password, new EddyStoneWriteCallBack() {

			@Override
			public void onWriteSuccess() {
				Toast.makeText(EddyStoneModifyActivity.this,
						"UID NAME SPACE change success", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void changeUid() {
		conn = new EddyStoneConnection(eddyStone.getName(),
				eddyStone.getMacAddress(), eddyStone.getModel(), this);
		String uid = et_uid.getText().toString().trim();
		conn.changeUid(uid);
		conn.connectGattToWrite(password, new EddyStoneWriteCallBack() {

			@Override
			public void onWriteSuccess() {
				Toast.makeText(EddyStoneModifyActivity.this, "UID change success",
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onErrorOfConnection() {
				// TODO Auto-generated method stub
				super.onErrorOfConnection();
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
				Toast.makeText(EddyStoneModifyActivity.this, "URL change success",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void changeModelToAll() {
		changeModel("iBeacon_uid_url");
	}

	private void changeModelToUid() {
		changeModel("uid");
	}

	private void changeModelToUrl() {
		changeModel("url");
	}

	private void changeModelToIBeacon() {
		changeModel("iBeacon");
	}

	private void changeModel(String model) {
		conn = new EddyStoneConnection(eddyStone.getName(),
				eddyStone.getMacAddress(), eddyStone.getModel(), this);
		conn.changerModel(model);
		conn.connectGattToWrite(password, new EddyStoneWriteCallBack() {

			@Override
			public void onWriteSuccess() {
				Toast.makeText(EddyStoneModifyActivity.this, "change model success",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onStop() {
		if (conn != null) {
			conn.close();
			conn = null;
		}
		super.onStop();
	}

}
