package com.github.oncizl.recycleradapter;

import android.content.res.TypedArray;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MoreHelper implements View.OnClickListener {

	private ProgressBar mProgressBar;
	private TextView mTextView;
	private View mMoreView;
	private OnLoadMoreListener mOnLoadMoreListener;
	private boolean mHasMore = true, mAutoRefresh = true;
	private int mBackgroundResId;

	private MoreHelper(@NonNull LayoutInflater layoutInflater) {
		mMoreView = layoutInflater.inflate(R.layout.layout_more, null);
		int[] attrs = {R.attr.selectableItemBackground};
		TypedArray array = mMoreView.getContext().obtainStyledAttributes(attrs);
		mBackgroundResId = array.getResourceId(0,0);
		array.recycle();
		mMoreView.setOnClickListener(this);
		mProgressBar = (ProgressBar) mMoreView.findViewById(R.id.pb_more);
		mTextView = (TextView) mMoreView.findViewById(R.id.tv_more);
		mTextView.setOnClickListener(this);
	}

//	public MoreHelper(@NonNull ListView listView) {
//		this(LayoutInflater.from(listView.getContext()));
//		if (listView.getFooterViewsCount() <= 0) listView.addFooterView(mMoreView, null, false);
//
//		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				if (visibleItemCount + firstVisibleItem == totalItemCount) {
//					if (mHasMore && mAutoRefresh && mOnLoadMoreListener != null) {
//						mHasMore = false;
//						start();
//					}
//				}
//			}
//		});
//	}

	public MoreHelper(@NonNull RecyclerView recyclerView) {
		this(LayoutInflater.from(recyclerView.getContext()));
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
				int visibleItemCount = layoutManager.getChildCount();
				int totalItemCount = layoutManager.getItemCount();
				int pastItems = layoutManager.findFirstVisibleItemPosition();
				if ((pastItems + visibleItemCount) >= totalItemCount) {
					if (mHasMore && mAutoRefresh && mOnLoadMoreListener != null) {
						mHasMore = false;
						start();
					}
				}
			}
		});
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	@CheckResult
	public View getView() {
		return mMoreView;
	}

	@Override
	public void onClick(View v) {
		if (mHasMore && !mAutoRefresh && mOnLoadMoreListener != null && v == mTextView) {
			start();
		}
	}

	private void start() {
		mTextView.setEnabled(false);
		mProgressBar.setVisibility(View.VISIBLE);
		mTextView.setText(R.string.more_manager_loading);
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	public void setAutoRefresh(boolean autoRefresh) {
		this.mAutoRefresh = autoRefresh;
		if (!mAutoRefresh && mHasMore) {
			mMoreView.setBackgroundResource(android.R.color.transparent);
			mTextView.setVisibility(View.VISIBLE);
			mTextView.setEnabled(true);
			mTextView.setText(R.string.more_manager_click_load_more);
			mProgressBar.setVisibility(View.GONE);
		}
	}

	public void setHasMore(boolean hasMore) {
		mHasMore = hasMore;
		mMoreView.setBackgroundResource(android.R.color.transparent);
		mTextView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		if (hasMore) {
			if (!mAutoRefresh) {
				mTextView.setEnabled(true);
				mTextView.setText(R.string.more_manager_click_load_more);
				mTextView.setBackgroundResource(mBackgroundResId);
			}
		} else {
			mTextView.setEnabled(false);
			mTextView.setText(R.string.more_manager_load_more_complete);
			mTextView.setBackgroundResource(android.R.color.transparent);
		}
	}
}
