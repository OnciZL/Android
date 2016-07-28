package com.github.oncizl.recycleritemclick;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

public abstract class OnRecyclerItemLongClickListener extends OnRecyclerItemTouchListener {

	public OnRecyclerItemLongClickListener(RecyclerView recyclerView) {
		super(recyclerView);
	}

	@Override
	public void onItemClick(RecyclerView.ViewHolder viewHolder, MotionEvent event) {

	}

	@Override
	public abstract void onItemLongClick(RecyclerView.ViewHolder viewHolder, MotionEvent event);
}
