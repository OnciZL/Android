package com.github.oncizl.recycleritemclick;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerItemTouchCompat {

	public static boolean isTouchChild(ViewGroup viewGroup, View child, MotionEvent event) {

		Point point = new Point();
		viewGroup.getGlobalVisibleRect(new Rect(), point);
		Rect rect = new Rect();
		child.getGlobalVisibleRect(rect);
		rect.offset(-point.x, -point.y);
		float x = event.getX();
		float y = event.getY();
		return x > rect.left && x < rect.right && y > rect.top && y < rect.bottom;
	}
}
