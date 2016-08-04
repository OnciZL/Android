package com.github.oncizl.bluetooth.scan;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.github.oncizl.bluetooth.schedule.ScanSchedule;
import com.github.oncizl.bluetooth.schedule.Schedulable;

public final class Scanner {

	private Scannable mScannable;
	private ScanSchedule mScanSchedule;
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
					if (isScanSchedulable()) scanSchedule().purge();
					if (isScannable()) scannable().status(Scannable.STATUS_STOP);
				}
			}
		}
	};
	private Schedulable mSchedulable = new Schedulable() {
		@Override
		public void schedule() {
			stop();
			if (isScannable() && scannable().isListenable()) scannable().scanListener().onScanComplete();
		}
	};

	public Scanner(Context context, OnScanListener onScanListener) {
		scanSchedule(new ScanSchedule());
		initializeScannable(context, onScanListener);
	}

	private void initializeScannable(Context context, OnScanListener onScanListener) {
		if (Build.VERSION.SDK_INT >= 5 && Build.VERSION.SDK_INT < 18) {
			scannable(new ScannableExtends5(context, onScanListener));
		} else if (Build.VERSION.SDK_INT >= 18 && Build.VERSION.SDK_INT < 21) {
			scannable(new ScannableExtends18(context, onScanListener));
		} else if (Build.VERSION.SDK_INT >= 21) {
			scannable(new ScannableExtends21(context, onScanListener));
		}
	}

	private boolean isScannable() {
		return scannable() != null;
	}

	private Scannable scannable() {
		return mScannable;
	}

	private void scannable(Scannable scannable) {
		mScannable = scannable;
	}

	private boolean isScanSchedulable() {
		return scanSchedule() != null;
	}

	private ScanSchedule scanSchedule() {
		return mScanSchedule;
	}

	public void scanSchedule(ScanSchedule scanSchedule) {
		mScanSchedule = scanSchedule;
	}

	private void broadcastReceiver(BroadcastReceiver broadcastReceiver) {
		mBroadcastReceiver = broadcastReceiver;
	}

	private Schedulable schedulable() {
		return mSchedulable;
	}

	private void schedulable(Schedulable schedulable) {
		mSchedulable = schedulable;
	}

	public void start() {
		if (isScannable() && scannable().context() != null && isScanSchedulable()) {
			scannable().initialize();
			if (scannable().isBluetoothEnable()) {
				if (Build.VERSION.SDK_INT >= 11) scannable().context().registerReceiver(mBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
				if (scannable().isListenable() && scannable().isStop()) scannable().scanListener().onScanStart();
				scanSchedule().execute(schedulable());
				scannable().start();
			} else {
				if (scannable().isListenable()) scannable().scanListener().onBluetoothDisable();
			}
		}
	}

	public void stop() {
		if (isScannable() && scannable().isBluetoothEnable() && scannable().context() != null && isScanSchedulable()) {
			try {
				if (Build.VERSION.SDK_INT >= 11) scannable().context().unregisterReceiver(mBroadcastReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (scannable().isListenable() && scannable().isStart()) scannable().scanListener().onScanStop();
			scannable().stop();
			scanSchedule().purge();
		}
	}

	public void release() {
		if (isScannable()) scannable().release();
		if (isScanSchedulable()) scanSchedule().cancel();
		scannable(null);
		scanSchedule(null);
		schedulable(null);
		broadcastReceiver(null);
	}
}
