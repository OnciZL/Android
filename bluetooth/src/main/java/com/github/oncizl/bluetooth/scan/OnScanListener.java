package com.github.oncizl.bluetooth.scan;

import android.bluetooth.BluetoothDevice;

public interface OnScanListener {

	void onScanDevice(BluetoothDevice device, int rssi);

	void onBluetoothDisable();

	void onScanStart();

	void onScanStop();

	void onScanComplete();
}
