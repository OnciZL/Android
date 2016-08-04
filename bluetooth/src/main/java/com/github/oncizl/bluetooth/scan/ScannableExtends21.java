package com.github.oncizl.bluetooth.scan;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;

import java.util.List;

@TargetApi(21)
final class ScannableExtends21 extends Scannable {

	private BluetoothLeScanner mBluetoothLeScanner;
	private ScanCallback mScanCallback = new ScanCallback() {
		@Override
		public void onScanResult(int callbackType, final ScanResult result) {
			super.onScanResult(callbackType, result);
			execute(new Runnable() {
				@Override
				public void run() {
					if (isListenable()) {
						scanListener().onScanDevice(result.getDevice(), result.getRssi());
					}
				}
			});
		}

		@Override
		public void onBatchScanResults(List<ScanResult> results) {
			super.onBatchScanResults(results);
			for (final ScanResult result : results) {
				execute(new Runnable() {
					@Override
					public void run() {
						if (isListenable()) {
							scanListener().onScanDevice(result.getDevice(), result.getRssi());
						}
					}
				});
			}
		}
	};

	ScannableExtends21(Context context, OnScanListener onScanListener) {
		super(context, onScanListener);
	}

	@Override
	protected void initialize() {
		super.initialize();
		if (isBluetoothEnable() && !isBluetoothLeScannable()) bluetoothLeScanner(bluetoothAdapter().getBluetoothLeScanner());
	}

	private BluetoothLeScanner bluetoothLeScanner() {
		return mBluetoothLeScanner;
	}

	private void bluetoothLeScanner(BluetoothLeScanner bluetoothLeScanner) {
		mBluetoothLeScanner = bluetoothLeScanner;
	}

	private boolean isBluetoothLeScannable() {
		return mBluetoothLeScanner != null;
	}

	private ScanCallback scanCallback() {
		return mScanCallback;
	}

	private void scanCallback(ScanCallback scanCallback) {
		mScanCallback = scanCallback;
	}

	private boolean isScanCallbackable() {
		return scanCallback() != null;
	}

	@Override
	boolean isCallbackable() {
		return super.isCallbackable() && isBluetoothLeScannable() && isScanCallbackable();
	}

	@Override
	void start() {
		if (isStop() && isCallbackable()) {
			bluetoothLeScanner().startScan(scanCallback());
			status(STATUS_START);
		}
	}

	@Override
	void stop() {
		if (isStart() && isCallbackable()) {
			bluetoothLeScanner().stopScan(scanCallback());
			status(STATUS_STOP);
		}
	}

	@Override
	void release() {
		super.release();
		bluetoothLeScanner(null);
		scanCallback(null);
	}
}
