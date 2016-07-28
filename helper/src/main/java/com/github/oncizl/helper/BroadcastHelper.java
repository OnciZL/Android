package com.github.oncizl.helper;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;

public class BroadcastHelper {

	private static LocalBroadcastManager sLocalBroadcastManager;
	// Actions
	private static Map<android.support.v4.app.Fragment, String[]> sV4FragmentActionsMap = new HashMap<>();
	private static Map<Fragment, String[]> sFragmentActionsMap = new HashMap<>();
	private static Map<Activity, String[]> sActivityActionsMap = new HashMap<>();
	// BroadcastReceiver
	private static Map<android.support.v4.app.Fragment, BroadcastReceiver> sV4FragmentBroadcastReceiverMap = new HashMap<>();
	private static Map<Fragment, BroadcastReceiver> sFragmentBroadcastReceiverMap = new HashMap<>();
	private static Map<Activity, BroadcastReceiver> sActivityBroadcastReceiverMap = new HashMap<>();

	private BroadcastHelper() {
	}

	public static void init(Context context) {
		if (sLocalBroadcastManager == null) sLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
	}

	public static void puts(android.support.v4.app.Fragment fragment, String... actions) {
		sV4FragmentActionsMap.put(fragment, actions);
	}

	public static void puts(Fragment fragment, String... actions) {
		sFragmentActionsMap.put(fragment, actions);
	}

	public static void puts(Activity activity, String... actions) {
		sActivityActionsMap.put(activity, actions);
	}

	public static void sends(String... actions) {
		for (String action : actions) {
			sLocalBroadcastManager.sendBroadcastSync(new Intent(action));
		}
	}

	public static void sends(Intent... intents) {
		for (Intent intent : intents) {
			sLocalBroadcastManager.sendBroadcastSync(intent);
		}
	}

	public static void register(android.support.v4.app.Fragment fragment, OnBroadcastReceiveListener onBroadcastReceiveListener) {
		BroadcastReceiver broadcastReceiver = generateBroadcastReceiver(onBroadcastReceiveListener);
		sV4FragmentBroadcastReceiverMap.put(fragment, broadcastReceiver);
		String[] actions = sV4FragmentActionsMap.get(fragment);
		sV4FragmentActionsMap.remove(fragment);
		register(broadcastReceiver, actions);
	}

	public static void register(Fragment fragment, OnBroadcastReceiveListener onBroadcastReceiveListener) {
		BroadcastReceiver broadcastReceiver = generateBroadcastReceiver(onBroadcastReceiveListener);
		sFragmentBroadcastReceiverMap.put(fragment, broadcastReceiver);
		String[] actions = sFragmentActionsMap.get(fragment);
		sFragmentActionsMap.remove(fragment);
		register(broadcastReceiver, actions);
	}

	public static void register(Activity activity, OnBroadcastReceiveListener onBroadcastReceiveListener) {
		BroadcastReceiver broadcastReceiver = generateBroadcastReceiver(onBroadcastReceiveListener);
		sActivityBroadcastReceiverMap.put(activity, broadcastReceiver);
		String[] actions = sActivityActionsMap.get(activity);
		sActivityActionsMap.remove(activity);
		register(broadcastReceiver, actions);
	}

	private static void register(BroadcastReceiver broadcastReceiver, String... actions) {
		if (sLocalBroadcastManager != null && actions != null && actions.length > 0) {
			IntentFilter intentFilter = new IntentFilter();
			for (String action : actions) {
				intentFilter.addAction(action);
			}
			sLocalBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
		}
	}

	private static BroadcastReceiver generateBroadcastReceiver(final OnBroadcastReceiveListener onBroadcastReceiveListener) {
		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (onBroadcastReceiveListener != null) {
					onBroadcastReceiveListener.onReceive(context, intent, intent.getAction());
				}
			}
		};
	}

	public static void unregister(android.support.v4.app.Fragment fragment) {
		BroadcastReceiver broadcastReceiver = sV4FragmentBroadcastReceiverMap.get(fragment);
		sLocalBroadcastManager.unregisterReceiver(broadcastReceiver);
		sV4FragmentBroadcastReceiverMap.remove(fragment);
	}

	public static void unregister(Fragment fragment) {
		BroadcastReceiver broadcastReceiver = sFragmentBroadcastReceiverMap.get(fragment);
		sLocalBroadcastManager.unregisterReceiver(broadcastReceiver);
		sFragmentBroadcastReceiverMap.remove(fragment);
	}

	public static void unregister(Activity activity) {
		BroadcastReceiver broadcastReceiver = sActivityBroadcastReceiverMap.get(activity);
		sLocalBroadcastManager.unregisterReceiver(broadcastReceiver);
		sActivityBroadcastReceiverMap.remove(activity);
	}

	public static void registerGlobal(Activity activity, android.support.v4.app.Fragment fragment, String... actions) {
		BroadcastReceiver broadcastReceiver = get(fragment);
		IntentFilter filter = new IntentFilter();
		for (String action : actions) {
			filter.addAction(action);
		}
		activity.registerReceiver(broadcastReceiver, filter);
	}

	public static void registerGlobal(Activity activity, Fragment fragment, String... actions) {
		BroadcastReceiver broadcastReceiver = get(fragment);
		IntentFilter filter = new IntentFilter();
		for (String action : actions) {
			filter.addAction(action);
		}
		activity.registerReceiver(broadcastReceiver, filter);
	}

	public static void registerGlobal(Activity activity, String... actions) {
		BroadcastReceiver broadcastReceiver = get(activity);
		IntentFilter filter = new IntentFilter();
		for (String action : actions) {
			filter.addAction(action);
		}
		activity.registerReceiver(broadcastReceiver, filter);
	}

	public static void unregisterGlobal(Activity activity, android.support.v4.app.Fragment fragment) {
		activity.unregisterReceiver(get(fragment));
	}

	public static void unregisterGlobal(Activity activity, Fragment fragment) {
		activity.unregisterReceiver(get(fragment));
	}

	public static void unregisterGlobal(Activity activity) {
		activity.unregisterReceiver(get(activity));
	}

	public static BroadcastReceiver get(android.support.v4.app.Fragment fragment) {
		return sV4FragmentBroadcastReceiverMap.get(fragment);
	}

	public static BroadcastReceiver get(Fragment fragment) {
		return sFragmentBroadcastReceiverMap.get(fragment);
	}

	public static BroadcastReceiver get(Activity activity) {
		return sActivityBroadcastReceiverMap.get(activity);
	}

	public interface OnBroadcastReceiveListener {
		void onReceive(Context context, Intent intent, String action);
	}
}
