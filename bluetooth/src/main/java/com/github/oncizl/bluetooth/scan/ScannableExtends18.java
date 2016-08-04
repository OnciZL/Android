package com.github.oncizl.bluetooth.scan;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

@TargetApi(18)
final class ScannableExtends18 extends Scannable {

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
			execute(new Runnable() {
				@Override
				public void run() {
					if (isListenable()) {
						scanListener().onScanDevice(device, rssi);
					}
				}
			});
		}
	};

	ScannableExtends18(Context context, OnScanListener onScanListener) {
		super(context, onScanListener);
	}

	@Override
	boolean isCallbackable() {
		return super.isCallbackable() && isLeScanCallbackable();
	}

	private BluetoothAdapter.LeScanCallback leScanCallback() {
		return mLeScanCallback;
	}

	private void leScanCallback(BluetoothAdapter.LeScanCallback leScanCallback) {
		mLeScanCallback = leScanCallback;
	}

	private boolean isLeScanCallbackable() {
		return leScanCallback() != null;
	}

	@Override
	void start() {
		if (isStop() && isCallbackable()) {
			bluetoothAdapter().startLeScan(leScanCallback());
			status(STATUS_START);
		}
	}

	@Override
	void stop() {
		if (isStart() && isCallbackable()) {
			bluetoothAdapter().stopLeScan(leScanCallback());
			status(STATUS_STOP);
		}
	}

	@Override
	void release() {
		super.release();
		leScanCallback(null);
	}
}
