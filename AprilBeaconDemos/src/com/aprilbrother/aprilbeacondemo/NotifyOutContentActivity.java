package com.aprilbrother.aprilbeacondemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class NotifyOutContentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_notify_out_content);
		super.onCreate(savedInstanceState);
		LinearLayout ll_notify_content = (LinearLayout) findViewById(R.id.ll_notify_content);
		ll_notify_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotifyOutContentActivity.this.finish();
			}
		});
	}
}
