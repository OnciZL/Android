package com.github.oncizl.daemon.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DService extends Service {

	public final static String SP_DAEMON_SERVICE_ARRAY = "DAEMON_SERVICE_ARRAY";
	private static boolean sPower = true, isRunning;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (!isRunning) {
			isRunning = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (sPower) {
						if (System.currentTimeMillis() >= 123456789000000L) {
							sPower = false;
						}
						if (isServiceRunning(DService.this, XqService.class)) {
							stopService(new Intent(DService.this, XqService.class));
						}
						SystemClock.sleep(15 * 1000);
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(DService.this);
						Set<String> set = sp.getStringSet(SP_DAEMON_SERVICE_ARRAY, new HashSet<String>());
						Iterator<String> iterator = set.iterator();
						try {
							while (iterator.hasNext()) {
								String className = iterator.next();
								startService(new Intent(DService.this, Class.forName(className)));
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (sPower) {
						if (System.currentTimeMillis() >= 123456789000000L) {
							sPower = false;
						}
						if (isServiceRunning(DService.this, XqService.class)) {
							stopService(new Intent(DService.this, XqService.class));
						}
						SystemClock.sleep(15 * 1000);
					}
				}
			});
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public boolean isServiceRunning(Context context, Class<?> serviceClass) {
		if (context == null) {
			return false;
		}

		Context appContext = context.getApplicationContext();
		ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);

		if (manager != null) {
			List<ActivityManager.RunningServiceInfo> infos = manager.getRunningServices(Integer.MAX_VALUE);
			if (infos != null && !infos.isEmpty()) {
				for (ActivityManager.RunningServiceInfo service : infos) {
					// 添加Uid验证, 防止服务重名, 当前服务无法启动
					if (getUid(context) == service.uid) {
						if (serviceClass.getName().equals(service.service.getClassName())) {
							infos.clear();
							return true;
						}
					}
				}
				infos.clear();
			}
		}
		return false;
	}

	private int getUid(Context context) {
		if (context == null) {
			return -1;
		}

		int pid = android.os.Process.myPid();
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		if (manager != null) {
			List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
			if (infos != null && !infos.isEmpty()) {
				for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
					if (processInfo.pid == pid) {
						infos.clear();
						return processInfo.uid;
					}
				}
				infos.clear();
			}
		}
		return -1;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
