package com.github.oncizl.daemon.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

public class XqService extends Service {

	private static boolean sPower = true, isRunning;

	@Override
	public void onCreate() {
		super.onCreate();

		if (Build.VERSION.SDK_INT >= 18) {
			startForeground(2333, new Notification());
			startService(new Intent(this, XService.class));
		} else {
			startForeground(2333, new Notification());
		}
	}

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
						startService(new Intent(XqService.this, DService.class));
						SystemClock.sleep(15 * 1000);
					}
				}
			}).start();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
