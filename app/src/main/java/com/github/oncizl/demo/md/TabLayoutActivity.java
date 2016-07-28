package com.github.oncizl.demo.md;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.oncizl.demo.R;

import java.util.ArrayList;

public class TabLayoutActivity extends AppCompatActivity implements View.OnClickListener {

	private TabLayout mTabLayout;
	private ViewPager mViewPager;
	private TabsPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.design_tabs_viewpager);

		int position = getIntent().getIntExtra(ZhuXian.EXTRA_POS, -1);
		String title = "No Title";
		if (position >= 0 && position < ZhuXian.sTitles.length) {
			title = ZhuXian.sTitles[position];
		}
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			toolbar.setTitle(title);
			setSupportActionBar(toolbar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
				actionBar.setDefaultDisplayHomeAsUpEnabled(true);
			}
		}

		mTabLayout = (TabLayout) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);

		mPagerAdapter = new TabsPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);

		mTabLayout.setupWithViewPager(mViewPager);

		RadioGroup radioGroupMode = (RadioGroup) findViewById(R.id.radio_group_mode);
		if (radioGroupMode != null) {
			switch (mTabLayout.getTabMode()) {
				case TabLayout.MODE_SCROLLABLE:
					radioGroupMode.check(R.id.rb_scrollable);
					break;
				case TabLayout.MODE_FIXED:
					radioGroupMode.check(R.id.rb_fixed);
					break;
			}
			radioGroupMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup radioGroup, int id) {
					switch (id) {
						case R.id.rb_fixed:
							mTabLayout.setTabMode(TabLayout.MODE_FIXED);
							break;
						case R.id.rb_scrollable:
							mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
							break;
					}
				}
			});
		}
		RadioGroup radioGroupGravity = (RadioGroup) findViewById(R.id.radio_group_gravity);
		if (radioGroupGravity!=null) {
			switch (mTabLayout.getTabGravity()) {
				case TabLayout.GRAVITY_CENTER:
					radioGroupGravity.check(R.id.rb_center);
					break;
				case TabLayout.GRAVITY_FILL:
					radioGroupGravity.check(R.id.rb_fill);
					break;
			}
			radioGroupGravity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup radioGroup, int id) {
					switch (id) {
						case R.id.rb_center:
							mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
							break;
						case R.id.rb_fill:
							mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
							break;
					}
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_add_tab:
				mPagerAdapter.addTab("Tab" + mPagerAdapter.getCount());
				break;
			case R.id.btn_remove_tab:
				mPagerAdapter.removeTab();
				break;
			case R.id.btn_select_first_tab:
				if (mTabLayout.getTabCount() > 0) {
					mViewPager.setCurrentItem(0);
				}
				break;
		}
	}

	private static class TabsPagerAdapter extends PagerAdapter {
		private final ArrayList<CharSequence> mArrayList = new ArrayList<>();

		public void addTab(String title) {
			mArrayList.add(title);
			notifyDataSetChanged();
		}

		public void removeTab() {
			if (!mArrayList.isEmpty()) {
				mArrayList.remove(mArrayList.size() - 1);
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			return mArrayList.size();
		}

		@Override
		public int getItemPosition(Object object) {
			final Item item = (Item) object;
			final int index = mArrayList.indexOf(item.cs);
			return index >= 0 ? index : POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			final TextView tv = new TextView(container.getContext());
			tv.setText(getPageTitle(position));
			tv.setGravity(Gravity.CENTER);
			container.addView(tv, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);

			Item item = new Item();
			item.cs = mArrayList.get(position);
			item.view = tv;
			return item;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			final Item item = (Item) object;
			return item.view == view;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mArrayList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			final Item item = (Item) object;
			container.removeView(item.view);
		}

		private static class Item {
			TextView view;
			CharSequence cs;
		}
	}
}
