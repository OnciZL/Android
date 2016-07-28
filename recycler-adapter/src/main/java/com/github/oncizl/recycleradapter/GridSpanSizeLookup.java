package com.github.oncizl.recycleradapter;

import android.support.v7.widget.GridLayoutManager;

public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

	private RecyclerAdapter mRecyclerAdapter;
	private int mDefaultSpanSize;

	public GridSpanSizeLookup(RecyclerAdapter recyclerAdapter, int defaultSpanSize) {
		this.mRecyclerAdapter = recyclerAdapter;
		mDefaultSpanSize = defaultSpanSize;
	}

	@Override
	public int getSpanSize(int position) {
		if (mRecyclerAdapter==null) {
			return 1;
		} else {
			return mRecyclerAdapter.isHeaderOrFooter(position) ? mDefaultSpanSize : 1;
		}
	}
}
