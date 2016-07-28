package com.github.oncizl.helper;

import android.content.Context;
import android.util.TypedValue;

public class PixelsHelper {

	private PixelsHelper() {}

	public static float dp2px(Context context, float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
	}

	public static float sp2px(Context context, float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
	}

	public static float pt2px(Context context, float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, context.getResources().getDisplayMetrics());
	}

	public static float in2px(Context context, float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, value, context.getResources().getDisplayMetrics());
	}

	public static float mm2px(Context context, float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, context.getResources().getDisplayMetrics());
	}
}
