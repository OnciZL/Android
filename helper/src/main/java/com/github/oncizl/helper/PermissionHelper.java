package com.github.oncizl.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionHelper {

	private static final int REQUEST_CODE = 777;

	private static Map<String, Integer> sPermissionNameMap = new HashMap<>();

	static {
		sPermissionNameMap.put("android.permission.ACCESS_COARSE_LOCATION", R.string.permission_group_location);
		sPermissionNameMap.put("android.permission.ACCESS_FINE_LOCATION", R.string.permission_group_location);
		sPermissionNameMap.put("android.permission.BODY_SENSORS", R.string.permission_group_sensors);
		sPermissionNameMap.put("android.permission.CALL_PHONE", R.string.permission_group_phone);
		sPermissionNameMap.put("android.permission.CAMERA", R.string.permission_group_camera);
		sPermissionNameMap.put("android.permission.PROCESS_OUTGOING_CALLS", R.string.permission_group_phone);
		sPermissionNameMap.put("android.permission.READ_CONTACTS", R.string.permission_group_contacts);
		sPermissionNameMap.put("android.permission.READ_EXTERNAL_STORAGE", R.string.permission_group_storage);
		sPermissionNameMap.put("android.permission.READ_PHONE_STATE", R.string.permission_group_phone);
		sPermissionNameMap.put("android.permission.READ_SMS", R.string.permission_group_sms);
		sPermissionNameMap.put("android.permission.RECEIVE_MMS", R.string.permission_group_sms);
		sPermissionNameMap.put("android.permission.RECEIVE_SMS", R.string.permission_group_sms);
		sPermissionNameMap.put("android.permission.RECEIVE_WAP_PUSH", R.string.permission_group_sms);
		sPermissionNameMap.put("android.permission.RECORD_AUDIO", R.string.permission_group_microphone);
		sPermissionNameMap.put("android.permission.SEND_SMS", R.string.permission_group_sms);
		sPermissionNameMap.put("android.permission.USE_SIP", R.string.permission_group_phone);
		sPermissionNameMap.put("android.permission.WRITE_CALENDAR", R.string.permission_group_calendar);
		sPermissionNameMap.put("android.permission.WRITE_CALL_LOG", R.string.permission_group_phone);
		sPermissionNameMap.put("android.permission.WRITE_CONTACTS", R.string.permission_group_contacts);
		sPermissionNameMap.put("android.permission.WRITE_EXTERNAL_STORAGE", R.string.permission_group_storage);
	}

	private PermissionHelper() {
	}

	public static boolean shouldRequest(Activity activity, List<String> permissionList) {
		if (activity == null) return false;
		for (String permission : permissionList) {
			if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
				return true;
			}
		}
		return false;
	}

	public static void request(final Activity activity, List<String> permissionList) {
		if (activity == null) return;
		final List<String> permissions = new ArrayList<>();
		List<String> permissionNames = new ArrayList<>();
		for (String permission : permissionList) {
			if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
				permissions.add(permission);
				if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
					Integer resId = sPermissionNameMap.get(permission);
					String permissionName = "";
					if (resId != null) permissionName = activity.getResources().getString(resId);
					if (!TextUtils.isEmpty(permissionName) && !permissionNames.contains(permissionName)) {
						permissionNames.add(permissionName);
					}
				}
			}
		}
		if (permissions.size() > 0) {
			if (permissionNames.size() > 0) {
				String header = activity.getString(R.string.dialog_permission_header) + "\n\n";
				String center = "";
				for (int i = 0; i < permissionNames.size(); i++) {
					center += "\t\t" + permissionNames.get(i) + " \n";
				}
				String footer = "\n" + activity.getString(R.string.dialog_permission_footer);
				String message = header + center + footer;
				SpannableString spannableString = new SpannableString(message);
				ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
				StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
				spannableString.setSpan(foregroundColorSpan, header.length(), header.length() + center.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(styleSpan, header.length(), header.length() + center.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				new AlertDialog.Builder(activity)
						.setTitle(R.string.dialog_permission_title)
						.setMessage(spannableString)
						.setPositiveButton(R.string.dilaog_permission_confirm, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setData(Uri.parse("package:" + activity.getPackageName()));
								activity.startActivity(intent);
							}
						})
						.setNegativeButton(R.string.dilaog_permission_cancel, null)
						.setCancelable(false)
						.create()
						.show();
				return;
			}
			ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), REQUEST_CODE);
		}
	}
}
