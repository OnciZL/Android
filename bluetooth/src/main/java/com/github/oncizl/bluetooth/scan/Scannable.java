package com.github.oncizl.bluetooth.scan;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class Scannable {

	static final int STATUS_START = 0;
	static final int STATUS_STOP = 1;

	private Context mContext;
	private BluetoothAdapter mBluetoothAdapter;
	private ExecutorService mExecutorService;
	private OnScanListener mOnScanListener;
	private int mStatus = STATUS_STOP;

	Scannable(Context context, OnScanListener onScanListener) {
		context(context);
		scanListener(onScanListener);
		executor(Executors.newSingleThreadExecutor());
	}

	abstract void start();

	abstract void stop();

	Context context() {
		return mContext;
	}

	private void context(Context context) {
		mContext = context;
	}

	boolean isCallbackable() {
		return isBluetoothEnable() && context() != null;
	}

	OnScanListener scanListener() {
		return mOnScanListener;
	}

	private void scanListener(OnScanListener onScanListener) {
		mOnScanListener = onScanListener;
	}

	boolean isListenable() {
		return scanListener() != null;
	}

	private ExecutorService executor() {
		return mExecutorService;
	}

	private void executor(ExecutorService executorService) {
		mExecutorService = executorService;
	}

	private boolean isExecutable() {
		return executor() != null && !executor().isShutdown();
	}

	void execute(Runnable runnable) {
		if (isExecutable() && isListenable()) {
			executor().execute(runnable);
		}
	}

	protected void initialize() {
		if (context() != null && !isBluetoothEnable()) {
			if (Build.VERSION.SDK_INT >= 5 && Build.VERSION.SDK_INT < 18) {
				bluetoothAdapter(BluetoothAdapter.getDefaultAdapter());
			} else if (Build.VERSION.SDK_INT >= 18) {
				BluetoothManager manager = (BluetoothManager) context().getSystemService(Context.BLUETOOTH_SERVICE);
				bluetoothAdapter(manager.getAdapter());
			}
		}
	}

	BluetoothAdapter bluetoothAdapter() {
		return mBluetoothAdapter;
	}

	private void bluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
		mBluetoothAdapter = bluetoothAdapter;
	}

	boolean isBluetoothEnable() {
		return bluetoothAdapter() != null && bluetoothAdapter().isEnabled();
	}

	void status(int status) {
		mStatus = status;
	}

	private int status() {
		return mStatus;
	}

	boolean isStart() {
		return status() == STATUS_START;
	}

	boolean isStop() {
		return status() == STATUS_STOP;
	}

	void release() {
		context(null);
		scanListener(null);
		executor(null);
		bluetoothAdapter(null);
	}
}
