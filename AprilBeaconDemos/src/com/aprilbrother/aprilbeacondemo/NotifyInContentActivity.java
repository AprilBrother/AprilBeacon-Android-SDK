package com.aprilbrother.aprilbeacondemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NotifyInContentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_notify_in_content);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setClickFinish();
		setAnimtionStart();
	}

	private void setAnimtionStart() {
		ImageView iv = (ImageView) findViewById(R.id.iv_notify_content_in);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(3000);
		iv.startAnimation(alphaAnimation);
	}

	private void setClickFinish() {
		LinearLayout ll_notify_content = (LinearLayout) findViewById(R.id.ll_notify_content);
		ll_notify_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotifyInContentActivity.this.finish();
			}
		});
	}
}
