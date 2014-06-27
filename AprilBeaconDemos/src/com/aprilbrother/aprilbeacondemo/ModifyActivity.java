package com.aprilbrother.aprilbeacondemo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics.MyReadCallBack;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection.MyWriteCallback;

public class ModifyActivity extends Activity {

	protected static final String TAG = "ModifyActivity";
	private EditText uuid;
	private EditText major;
	private EditText minor;
	private EditText password;
	private Beacon beacon;

	private AprilBeaconConnection conn;
	private TextView battery;
	private TextView txpower;
	private TextView advinterval;

	private String oldPassword;

	private AprilBeaconCharacteristics read;

	private static final int READBATTERY = 0;

	private static final int READTXPOWER = 1;

	private static final int READADVINTERVAL = 2;

	private BeaconManager beaconManager;
	private static final Region ALL_BEACONS_REGION = new Region("apr", null,
			null, null);

	final String URL_Post = "http://bbs.aprbrother.com";
	final String URL = "http://bbs.aprbrother.com";
	
	public final static Uri URI = Uri.parse("http://aprbrother.com");
	
	String resultData = "";
	URL url = null;
	HttpURLConnection urlConn = null;
	boolean isPost = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_modify);
		super.onCreate(savedInstanceState);
		init();
	}

	public void getBattery(View v) {
		setBattery();
	}

	public void getTxPower(View v) {
		setTxPower();
	}

	public void getAdvinterval(View v) {
		setAdvinterval();
	}

	private void setAdvinterval() {
		read = new AprilBeaconCharacteristics(this, beacon);
		read.connectGattToRead(new MyReadCallBack() {
			@Override
			public void readyToGetAdvinterval() {
				final Integer mAdvinterval;
				try {
					mAdvinterval = read.getAdvinterval();
					ModifyActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							advinterval.setText(mAdvinterval + "00ms");
							read.close();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, READADVINTERVAL);
	}

	private void setTxPower() {
		read = new AprilBeaconCharacteristics(this, beacon);
		read.connectGattToRead(new MyReadCallBack() {
			@Override
			public void readyToGetTxPower() {
				final Integer txpowervalue;
				try {
					txpowervalue = read.getTxPower();
					ModifyActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							switch (txpowervalue) {
							case 0:
								txpower.setText("0dbm");
								read.close();
								break;
							case 1:
								txpower.setText("4dbm");
								read.close();
								break;
							case 2:
								txpower.setText("-6dbm");
								read.close();
								break;
							case 3:
								txpower.setText("-23dbm");
								read.close();
								break;
							default:
								break;
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, READTXPOWER);
	}

	private void setBattery() {
		read = new AprilBeaconCharacteristics(this, beacon);
		read.connectGattToRead(new MyReadCallBack() {
			@Override
			public void readyToGetBattery() {
				final Integer battery2;
				try {
					battery2 = read.getBattery();
					ModifyActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							battery.setText(battery2 + "%");
							read.close();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, READBATTERY);
	}

	private void init() {

		uuid = (EditText) findViewById(R.id.uuid);
		major = (EditText) findViewById(R.id.major);
		minor = (EditText) findViewById(R.id.minor);
		password = (EditText) findViewById(R.id.password);

		battery = (TextView) findViewById(R.id.battery);
		txpower = (TextView) findViewById(R.id.txpower);
		advinterval = (TextView) findViewById(R.id.advinterval);
		Bundle bundle = getIntent().getExtras();
		beacon = bundle.getParcelable("beacon");

		Log.i(TAG, beacon.getMacAddress());
		Log.i(TAG, beacon.getMajor() + "");
		Log.i(TAG, beacon.getMinor() + "");

		String proximityUUID = beacon.getProximityUUID();
		uuid.setHint(proximityUUID);
		major.setHint(beacon.getMajor() + "");
		minor.setHint(beacon.getMinor() + "");
	}

	public void sure(View v) {
		showEnterDialog();
	}

	private EditText et_pwd;
	private Button bt_ok;
	private Button bt_cancel;
	private AlertDialog dialog;

	/**
	 * 输入密码的对话框
	 */
	private void showEnterDialog() {

		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_enter_password, null);
		et_pwd = (EditText) view.findViewById(R.id.et_pwd);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oldPassword = et_pwd.getText().toString().trim();
				aprilWrite();
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private void aprilWrite() {
		conn = new AprilBeaconConnection(this, beacon);

		if (!TextUtils.isEmpty(major.getText().toString())) {
			int newMajor = Integer.parseInt(major.getText().toString());
			conn.writeMajor(newMajor);
		}
		if (!TextUtils.isEmpty(minor.getText().toString())) {
			int newMinor = Integer.parseInt(minor.getText().toString());
			conn.writeMinor(newMinor);
		}
		if (!TextUtils.isEmpty(uuid.getText().toString())) {
			String newUuid = uuid.getText().toString();
			conn.writeUUID(newUuid);
		}
		if (!TextUtils.isEmpty(password.getText().toString())) {
			String newPassword = password.getText().toString();
			conn.writePassword(newPassword);
		}

		conn.connectGattToWrite(new MyWriteCallback() {

			@Override
			public void onWriteNewPasswordSuccess(final String oldPassword,
					final String newPassword) {
				super.onWriteNewPasswordSuccess(oldPassword, newPassword);
				ModifyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								ModifyActivity.this,
								"oldPassword is " + oldPassword
										+ "newPassword is" + newPassword,
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onBeaconError() {
				ModifyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								ModifyActivity.this,
								"please make sure the beacon is AprilBeacon and the FW should above 2.0",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onPasswordWrong(final String password) {
				ModifyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								ModifyActivity.this,
								"password is wrong what you input is ："
										+ password, Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onWriteMajorSuccess(final int oldMajor,
					final int newMajor) {
				ModifyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								ModifyActivity.this,
								"oldMajor is " + oldMajor + "newMajor is "
										+ newMajor, Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onErrorOfPassword() {
				Toast.makeText(ModifyActivity.this, "onErrorOfPassword",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onErrorOfConnection() {
				Toast.makeText(ModifyActivity.this, "onErrorOfConnection",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onErrorOfDiscoveredServices() {
				Toast.makeText(ModifyActivity.this,
						"onErrorOfDiscoveredServices", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onWriteMinorSuccess(final int oldMionr,
					final int newMinor) {
				ModifyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								ModifyActivity.this,
								"oldMionr is " + oldMionr + "newMinor is "
										+ newMinor, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}, oldPassword);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (conn != null && !conn.isConnected()) {
			conn = new AprilBeaconConnection(this, beacon);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		beacon = null;
		if (conn != null && conn.isConnected()) {
			conn.close();
		}
	}

	public void notify(View v) {
		beaconManager = new BeaconManager(this);
		beaconManager.setMonitoringListener(new MonitoringListener() {

			@Override
			public void onExitedRegion(Region region) {
				Toast.makeText(getApplicationContext(), "你离开beacon范围", 0)
						.show();
			}

			@Override
			public void onEnteredRegion(Region region, List<Beacon> beacons) {
				
//				try {
//					beaconManager.stopMonitoring(ALL_BEACONS_REGION);
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
				
				Toast.makeText(getApplicationContext(),
						"你进入beacon范围 beacons.size =" + beacons.size(), 0)
						.show();
				//HttpURL(); 
				Intent it = new Intent(Intent.ACTION_VIEW, URI);
				startActivity(it);
			}
		});
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startMonitoring(ALL_BEACONS_REGION);
				} catch (RemoteException e) {
				}
			}
		});
	}

	
	private void HttpURLConnection_Get(){  
        try{  
            isPost = false;  
            //通过openConnection 连接  
            URL url = new java.net.URL(URL);  
            urlConn=(HttpURLConnection)url.openConnection();  
            //设置输入和输出流   
            urlConn.setDoOutput(true);  
            urlConn.setDoInput(true);  
            //关闭连接  
            urlConn.disconnect();  
        }catch(Exception e){  
            resultData = "连接超时";  
            Message mg = Message.obtain();    
            mg.obj = resultData;    
            handler.sendMessage(mg);   
            e.printStackTrace();  
        }  
    }  
	
	private void HttpURLConnection_Post(){  
        try{  
            //通过openConnection 连接  
            URL url = new java.net.URL(URL_Post);  
            urlConn=(HttpURLConnection)url.openConnection();  
            //设置输入和输出流   
            urlConn.setDoOutput(true);  
            urlConn.setDoInput(true);  
              
            urlConn.setRequestMethod("POST");  
            urlConn.setUseCaches(false);  
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的    
            urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，  
            // 要注意的是connection.getOutputStream会隐含的进行connect。    
            urlConn.connect();  
            //DataOutputStream流  
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());  
            //要上传的参数  
            String content = "par=" + URLEncoder.encode("ylx_Post+中正", "UTF_8");   
            //将要上传的内容写入流中  
            out.writeBytes(content);     
            //刷新、关闭  
            out.flush();  
            out.close();     
              
        }catch(Exception e){  
            resultData = "连接超时";  
            Message mg = Message.obtain();    
            mg.obj = resultData;    
            handler.sendMessage(mg);   
            e.printStackTrace();  
        }  
    }  
	
	 private void HttpURL() {  
	        new Thread(){  
	            public void run() {   
	  
	                try{  
	                    //Get方式  
	                    //HttpURLConnection_Get();  
	                    //Post方式  
	                    HttpURLConnection_Post();   
	                      
	                    InputStreamReader in = new InputStreamReader(urlConn.getInputStream());    
	                    BufferedReader buffer = new BufferedReader(in);    
	                    String inputLine = null;    
	                    while (((inputLine = buffer.readLine()) != null)){  
	                        resultData += inputLine + "\n";    
	                    }  
	                    System.out.println(resultData);  
	                    in.close();   
	  
	                }catch(Exception e){  
	                    resultData = "连接超时";  
	                    e.printStackTrace();  
	                }finally{  
	                    try{  
	                        //关闭连接  
	                        if(isPost)  
	                        urlConn.disconnect();  
	                          
	                        Message mg = Message.obtain();    
	                        mg.obj = resultData;    
	                        handler.sendMessage(mg);    
	                    }catch(Exception e){  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }  
	        }.start();  
	    }  
	 private Handler handler = new Handler() {    
	        @Override    
	        public void handleMessage(Message msg) {    
	            String m = (String) msg.obj;  
	            
	            Intent intent = new Intent(ModifyActivity.this,RequestActivity.class);
	            
	            intent.putExtra("string", m);
	            
	            startActivity(intent);
	            
	            Log.i(TAG, m);
	        }    
	    };    
	
	@Override
	protected void onStop() {
		try {
			if (beaconManager != null)
				beaconManager.disconnect();
			// beaconManager.stopRanging(ALL_BEACONS_REGION);
			// beaconManager.stopMonitoring(ALL_BEACONS_REGION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStop();
	}
}
