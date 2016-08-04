package com.github.oncizl.demo.bluetooth;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.oncizl.bluetooth.scan.OnScanListener;
import com.github.oncizl.bluetooth.scan.Scanner;
import com.github.oncizl.demo.R;
import com.github.oncizl.header.HeaderManager;

public class BluetoothActivity extends AppCompatActivity implements OnScanListener, View.OnClickListener {

	private static final String TAG = "BluetoothActivity";

	Scanner mScanner;

	public static void start(Context context) {
		Intent starter = new Intent(context, BluetoothActivity.class);
		context.startActivity(starter);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);

		HeaderManager.newInstance(this).title(getTitle());

		mScanner = new Scanner(this, this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button_start) {
			mScanner.start();
		} else if (id == R.id.button_stop) {
			mScanner.stop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mScanner.stop();
		mScanner.release();
	}

	@Override
	public void onScanDevice(BluetoothDevice device, int rssi) {
		Log.e(TAG, "onScanDevice: " + device.getName() + "，" + device.getAddress() + "，" + rssi);
	}

	@Override
	public void onBluetoothDisable() {
		Log.e(TAG, "onBluetoothDisable: ");
	}

	@Override
	public void onScanStart() {
		Log.e(TAG, "onScanStart: ");
	}

	@Override
	public void onScanStop() {
		Log.e(TAG, "onScanStop: ");
	}

	@Override
	public void onScanComplete() {
		Log.e(TAG, "onScanComplete: ");
	}
}
