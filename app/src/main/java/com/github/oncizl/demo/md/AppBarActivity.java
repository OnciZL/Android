package com.github.oncizl.demo.md;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.oncizl.demo.R;

import java.util.ArrayList;
import java.util.Collections;

public class AppBarActivity extends AppCompatActivity {

	private static final String TAG = "AppBarActivity";

	private Toolbar mToolbar;
	private CollapsingToolbarLayout mCollapsing;
	private TabLayout mTabs;
	private TextView mTextView;
	private SwipeRefreshLayout mSwipeRefresh;
	private RecyclerView mRecyclerView;
	private ArrayList<String> mArrayList = new ArrayList<>();
	private Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int position = getIntent().getIntExtra(ZhuXian.EXTRA_POS, -1);
		String title = "No Title";
		if (position >= 0 && position < ZhuXian.sTitles.length) {
			title = ZhuXian.sTitles[position];
		}
		switch (title) {
			case "AppBar/Collapsing Toolbar (pinned with FAB)":
				setContentView(R.layout.design_appbar_toolbar_collapse_pin_with_fab);
				break;
			case "AppBar/Collapsing Toolbar (pinned)":
				setContentView(R.layout.design_appbar_toolbar_collapse_pin);
				break;
			case "AppBar/Collapsing Toolbar (scroll off)":
				setContentView(R.layout.design_appbar_toolbar_collapse_scroll);
				break;
			case "AppBar/Collapsing Toolbar (scroll off) + Swipe Refresh":
				setContentView(R.layout.design_appbar_toolbar_collapse_scroll_with_swiperefresh);
				break;
			case "AppBar/Collapsing Toolbar + Parallax Image":
				setContentView(R.layout.design_appbar_toolbar_collapse_with_image);
				break;
			case "AppBar/Collapsing Toolbar + Parallax Image + Insets":
				getTheme().applyStyle(R.style.AppTheme_Navigation, true);
				setContentView(R.layout.design_appbar_toolbar_collapse_with_image_insets);
				break;
			case "AppBar/Parallax Overlapping content":
				setContentView(R.layout.design_appbar_toolbar_parallax_overlap);
				break;
			case "AppBar/Toolbar Scroll + Tabs Pin":
				setContentView(R.layout.design_appbar_toolbar_scroll_tabs_pinned);
				break;
			case "AppBar/Toolbar Scroll + Tabs Pin + Swipe Refresh":
				setContentView(R.layout.design_appbar_toolbar_scroll_tabs_pinned_with_swiperefres);
				break;
			case "AppBar/Toolbar Scroll + Tabs Scroll":
				setContentView(R.layout.design_appbar_toolbar_scroll_tabs_scroll);
				break;
			case "AppBar/Toolbar Scroll + Tabs Scroll + Snap":
				setContentView(R.layout.design_appbar_toolbar_scroll_tabs_scroll_snap);
				break;
		}

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			mToolbar.setTitle(title);
			setSupportActionBar(mToolbar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
				actionBar.setDefaultDisplayHomeAsUpEnabled(true);
			}
		}

		mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
		if (mCollapsing != null) {
			mCollapsing.setTitle(title);
		}

		mTabs = (TabLayout) findViewById(R.id.tabs);
		if (mTabs != null) {
			for (int i = 0; i < 10; i++) {
				mTabs.addTab(mTabs.newTab().setText("Tab " + i));
			}
		}

		mTextView = (TextView) findViewById(R.id.textView);
		if (mTextView != null) {
			mTextView.setText(TextUtils.concat(ZhuXian.sTexts));
		}

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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
