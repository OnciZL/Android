package com.github.oncizl.bluetooth.scan;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

@TargetApi(5)
final class ScannableExtends5 extends Scannable {

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				final int rssi = intent.getIntExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
				execute(new Runnable() {
					@Override
					public void run() {
						if (isListenable()) {
							scanListener().onScanDevice(device, rssi);
						}
					}
				});
			}
		}
	};

	ScannableExtends5(Context context, OnScanListener onScanListener) {
		super(context, onScanListener);
	}

	private BroadcastReceiver broadcastReceiver() {
		return mBroadcastReceiver;
	}

	private void broadcastReceiver(BroadcastReceiver broadcastReceiver) {
		mBroadcastReceiver = broadcastReceiver;
	}

	@Override
	void start() {
		if (isStop() && isCallbackable()) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(BluetoothDevice.ACTION_FOUND);
			context().registerReceiver(broadcastReceiver(), filter);
			bluetoothAdapter().startDiscovery();
			status(STATUS_START);
		}
	}

	@Override
	void stop() {
		if (isStart() && isCallbackable()) {
			try {
				context().unregisterReceiver(broadcastReceiver());
			} catch (Exception ignored) {
			}
			bluetoothAdapter().cancelDiscovery();
			status(STATUS_STOP);
		}
	}

	@Override
	void release() {
		super.release();
		broadcastReceiver(null);
	}
}
