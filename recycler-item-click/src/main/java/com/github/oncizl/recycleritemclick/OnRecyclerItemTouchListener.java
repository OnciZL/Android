package com.github.oncizl.recycleritemclick;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnRecyclerItemTouchListener implements RecyclerView.OnItemTouchListener {

	private GestureDetector mGestureDetector;

	public OnRecyclerItemTouchListener(final RecyclerView recyclerView) {
		mGestureDetector = new GestureDetector(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent event) {
				View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
				if (child != null) {
					RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
					onItemClick(viewHolder, event);
					return true;
				}
				return super.onSingleTapUp(event);
			}

			@Override
			public void onLongPress(MotionEvent event) {
				View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
				if (child != null) {
					RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
					onItemLongClick(viewHolder, event);
				}
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		mGestureDetector.onTouchEvent(e);
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent e) {
		mGestureDetector.onTouchEvent(e);
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

	}

	public abstract void onItemClick(RecyclerView.ViewHolder viewHolder, MotionEvent event);

	public abstract void onItemLongClick(RecyclerView.ViewHolder viewHolder, MotionEvent event);
}
