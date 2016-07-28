package com.github.oncizl.demo.md;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.oncizl.demo.R;

import java.util.ArrayList;
import java.util.Collections;

public class BottomSheetActivity extends DesignBaseActivity {

	private BottomSheetBehavior<LinearLayout> mBehavior;
	private SwipeRefreshLayout mSwipeRefresh;
	private RecyclerView mRecyclerView;
	private ArrayList<String> mArrayList = new ArrayList<>();
	private Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.design_bottom_sheet);
		super.onCreate(savedInstanceState);

		mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
		if (mSwipeRefresh != null) {
			mSwipeRefresh.setColorSchemeResources(R.color.material_red);
			mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mSwipeRefresh.setRefreshing(false);
						}
					}, 2000);
				}
			});
		}

		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		if (mRecyclerView != null) {
			Collections.addAll(mArrayList, ZhuXian.sTitles);
			mRecyclerView.setClipToPadding(false);
			mRecyclerView.setAdapter(new DesignAdapter(mArrayList, null));
		}

		TextView textView = (TextView) findViewById(R.id.textView);
		if (textView != null) {
			textView.setText(TextUtils.concat(ZhuXian.sTexts));
		}
		LinearLayout bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
		if (bottomSheet != null) {
			mBehavior = BottomSheetBehavior.from(bottomSheet);
		}

		TextView bottom = (TextView) findViewById(R.id.bottom);
		if (bottom != null) {
			bottom.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
						mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
					} else if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
						mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
					}
				}
			});
		}

		Button dialog = (Button) findViewById(R.id.dialog);
		if (dialog != null) {
			dialog.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BottomSheetDialog sheetDialog = new BottomSheetDialog(BottomSheetActivity.this);
					sheetDialog.setTitle("Bottom Sheet Dialog");
					sheetDialog.setCancelable(false);
					View view = getLayoutInflater().inflate(R.layout.design_include_appbar_scrollview, null);
					TextView text = (TextView) view.findViewById(R.id.textView);
					text.setText(TextUtils.concat(ZhuXian.sTexts));
					sheetDialog.setContentView(view);
					sheetDialog.show();
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		if (mBehavior != null && mBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
			mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		} else {
			super.onBackPressed();
		}
	}
}
