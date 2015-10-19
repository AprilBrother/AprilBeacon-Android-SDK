package com.aprilbrother.aprilbeacondemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection.MyWriteCallback;
import com.aprilbrother.aprilbrothersdk.internal.ABAcceleration;

public class SensorActivity extends Activity implements OnClickListener {

	protected static final String TAG = "SensorActivity";

	private Button btn_conn, btn_on, btn_off, btn_notify, btn_on_l, btn_off_l,
			btn_notify_l;

	private TextView tv_ac_x, tv_ac_y, tv_ac_z, tv_light,tv_conn_status;

	private AprilBeaconConnection conn;

	private Beacon beacon;

	private ProgressDialog proDialog;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		handler = new Handler();
		Bundle bundle = getIntent().getExtras();
		beacon = bundle.getParcelable("beacon");
		conn = new AprilBeaconConnection(this, beacon);
		initView();
	}

	private void initView() {
		btn_on = (Button) findViewById(R.id.bt_turn_on);
		btn_on_l = (Button) findViewById(R.id.bt_turn_on_l);
		btn_off = (Button) findViewById(R.id.bt_turn_off);
		btn_off_l = (Button) findViewById(R.id.bt_turn_off_l);
		btn_notify = (Button) findViewById(R.id.bt_notify);
		btn_notify_l = (Button) findViewById(R.id.bt_notify_l);
		tv_ac_x = (TextView) findViewById(R.id.tv_ac_x);
		tv_ac_y = (TextView) findViewById(R.id.tv_ac_y);
		tv_ac_z = (TextView) findViewById(R.id.tv_ac_z);
		tv_light = (TextView) findViewById(R.id.tv_light);
		tv_conn_status = (TextView) findViewById(R.id.tv_conn_status);
		

		btn_on.setOnClickListener(this);
		btn_on_l.setOnClickListener(this);
		btn_off.setOnClickListener(this);
		btn_off_l.setOnClickListener(this);
		btn_notify.setOnClickListener(this);
		btn_notify_l.setOnClickListener(this);

		btn_conn = (Button) findViewById(R.id.bt_conn);
		btn_conn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_turn_on:
			conn.turnOnCalu();
			break;
		case R.id.bt_turn_off:
			conn.turnOffCalu();
			break;
		case R.id.bt_notify:
			conn.enableACNotification();
			break;
		case R.id.bt_turn_on_l:
			conn.turnOnLight();
			break;
		case R.id.bt_turn_off_l:
			conn.turnOffLight();
			break;
		case R.id.bt_notify_l:
			conn.enableLightNotification();
			break;
		case R.id.bt_conn:
			proDialog = android.app.ProgressDialog.show(SensorActivity.this,
					"Connecting", "Please wait for connect device");

			conn.connectGattToWrite(new MyWriteCallback() {
				@Override
				public void notifyABAcceleration(ABAcceleration abAcceleration) {
					Log.i(TAG, "-----------------");
					Log.i(TAG, " x = " + abAcceleration.getX());
					Log.i(TAG, " y = " + abAcceleration.getY());
					Log.i(TAG, " z = " + abAcceleration.getZ());

					tv_ac_x.setText(abAcceleration.getX() + "");
					tv_ac_y.setText(abAcceleration.getY() + "");
					tv_ac_z.setText(abAcceleration.getZ() + "");
					super.notifyABAcceleration(abAcceleration);
				}

				@Override
				public void notifyABLight(double light) {
					Log.i(TAG, "light = " + light);
					tv_light.setText(light + "");
					super.notifyABLight(light);
				}

				@Override
				public void connected() {
					Toast.makeText(SensorActivity.this, "have connect", 0)
							.show();
					if (proDialog != null && proDialog.isShowing()) {
						proDialog.dismiss();
					}
					tv_conn_status.setText("Connected");
					super.connected();
				}

			}, null);

			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					
					if (proDialog != null && proDialog.isShowing()){
						Toast.makeText(SensorActivity.this, "have not connect,please click the device buonnt s1 and try again", 0).show();
						proDialog.dismiss();
						if (conn != null) {
							conn.close();
						}
						tv_conn_status.setText("disConnect");
					}
				}
			}, 10000);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		conn.close();
		super.onStop();
	}
}
