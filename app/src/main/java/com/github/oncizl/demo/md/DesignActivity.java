package com.github.oncizl.demo.md;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.oncizl.demo.R;

import java.util.ArrayList;
import java.util.Collections;

public class DesignActivity extends AppCompatActivity {

	private static final String TAG = "DesignActivity";
	private DrawerLayout mDrawerLayout;
	private Toolbar mToolbar;
	private FloatingActionButton mFab;
	private NavigationView mNavigation;
	private SwipeRefreshLayout mSwipeRefresh;
	private ArrayList<String> mArrayList = new ArrayList<>();
	private RecyclerView mRecyclerView;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_design);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar!=null) {
			setSupportActionBar(mToolbar);
			mToolbar.setNavigationIcon(R.mipmap.ic_menu_white);
			mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mDrawerLayout.openDrawer(GravityCompat.START);
				}
			});
		}

		mNavigation = (NavigationView) findViewById(R.id.navigation);
		if (mNavigation != null) {
			mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem item) {
					mDrawerLayout.closeDrawers();
					switch (item.getItemId()) {
						case R.id.navigation_item_1:
						case R.id.navigation_item_2:
						case R.id.navigation_item_3:
						case R.id.navigation_sub_item_1:
						case R.id.navigation_sub_item_2:
						case R.id.navigation_with_icon:
						case R.id.navigation_without_icon:
							mToolbar.setTitle(item.getTitle());
							return true;
						default:
							return false;
					}
				}
			});
		}

		mFab = (FloatingActionButton) findViewById(R.id.fab);
		if (mFab !=null) {
			mFab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Snackbar.make(view, "替换成你自己的动作", Snackbar.LENGTH_INDEFINITE)
							.setAction("动作", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(DesignActivity.this, "动作", Toast.LENGTH_SHORT).show();
								}
							}).show();
				}
			});
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
//			mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//				@Override
//				public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//					if (parent.getChildAdapterPosition(view) != 0) {
//						outRect.top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
//					}
//				}
//			});
			mRecyclerView.setAdapter(new DesignAdapter(mArrayList, new OnItemClickListener() {
				@Override
				public void onItemClick(View itemView, int position) {
					if (position>=0 && position< ZhuXian.sTitles.length) {
						String title = ZhuXian.sTitles[position];
						Intent intent = new Intent();
						if (title.startsWith("AppBar")) {
							intent.setClass(DesignActivity.this, AppBarActivity.class);
						} else if (title.startsWith("BottomSheet")) {
							intent.setClass(DesignActivity.this, BottomSheetActivity.class);
						} else if (title.startsWith("Snackbar")) {
							intent.setClass(DesignActivity.this, SnackbarActivity.class);
						} else if (title.startsWith("TabLayout")) {
							intent.setClass(DesignActivity.this, TabLayoutActivity.class);
						} else if (title.startsWith("TextInput")) {
							intent.setClass(DesignActivity.this, TextInputLayoutActivity.class);
						}
						intent.putExtra(ZhuXian.EXTRA_POS, position);
						startActivity(intent);
					}
				}
			}));
		}
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
}
