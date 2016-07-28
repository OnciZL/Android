package com.github.oncizl.daemon.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class XService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		startForeground(2333, new Notification());
		stopForeground(true);
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(2333);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
