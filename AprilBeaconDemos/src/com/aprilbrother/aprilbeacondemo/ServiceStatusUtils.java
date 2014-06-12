package com.aprilbrother.aprilbeacondemo;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {

	public static boolean isServiceRunning(Context context,String serviename){
		ActivityManager  am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for(RunningServiceInfo info :infos){
			String classname = info.service.getClassName();
			if(serviename.equals(classname)){
				return true;
			}
		}
		return false;
	}
}
