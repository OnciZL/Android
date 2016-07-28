package com.github.oncizl.recycleritemclick;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

public abstract class OnRecyclerItemClickListener extends OnRecyclerItemTouchListener {

	public OnRecyclerItemClickListener(RecyclerView recyclerView) {
		super(recyclerView);
	}

	@Override
	public abstract void onItemClick(RecyclerView.ViewHolder viewHolder, MotionEvent event);

	@Override
	public void onItemLongClick(RecyclerView.ViewHolder viewHolder, MotionEvent event) {
	}
}
