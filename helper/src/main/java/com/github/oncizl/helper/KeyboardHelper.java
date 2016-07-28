package com.github.oncizl.helper;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardHelper {

	public static void show(final View view) {
		if (view == null) return;
		view.requestFocus();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.showSoftInput(view, 0);
			}
		}, 150);
	}

	public static void dismiss(final View view) {
		if (view == null) return;
		view.clearFocus();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}, 150);
	}
}
