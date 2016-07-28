package com.github.oncizl.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LogHelper {

	public static boolean sDebug = true;

	private LogHelper() {
	}

	public static void debug(boolean debug) {
		LogHelper.sDebug = debug;
	}

	public static void v(String tag, String log) {
		if (sDebug) Log.v(tag, log);
	}

	public static void d(String tag, String log) {
		if (sDebug) Log.d(tag, log);
	}

	public static void i(String tag, String log) {
		if (sDebug) Log.i(tag, log);
	}

	public static void w(String tag, String log) {
		if (sDebug) Log.w(tag, log);
	}

	public static void e(String tag, String log) {
		if (sDebug) Log.e(tag, log);
	}

	public static void wtf(String tag, String log) {
		if (sDebug) Log.wtf(tag, log);
	}

	public static void tS(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void tS(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	public static void tL(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void tL(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}
}
