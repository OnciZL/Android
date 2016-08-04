package com.github.oncizl.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.github.oncizl.daemon.service.DService;
import com.github.oncizl.demo.bluetooth.BluetoothActivity;
import com.github.oncizl.demo.md.DesignActivity;
import com.github.oncizl.demo.rx.RxJavaActivity;
import com.github.oncizl.header.HeaderCallback;
import com.github.oncizl.header.HeaderManager;
import com.github.oncizl.recycleradapter.MoreHelper;
import com.github.oncizl.recycleradapter.OnLoadMoreListener;
import com.github.oncizl.recycleradapter.RecyclerAdapter;
import com.github.oncizl.recycleritemclick.OnRecyclerItemClickListener;
import com.github.oncizl.widget.MS903;
import com.github.oncizl.widget.OnActionListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

	private MS903 mMS903;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Set<String> set = new HashSet<>();
		set.add(AndroidService.class.getCanonicalName());
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putStringSet(DService.SP_DAEMON_SERVICE_ARRAY, set).apply();
		startService(new Intent(this, DService.class));

		HeaderManager.newInstance(this)
				.title(getTitle())
				.invisible(HeaderManager.V_LEFT_0)
				.right0(0, "MS903", new HeaderCallback() {
					@Override
					public void callback(@HeaderManager.V_CLICKABLE int viewType, HeaderManager headerManager) {
						PopupMenu menu = new PopupMenu(MainActivity.this, headerManager.switchView(HeaderManager.V_DOT_RIGHT_0), Gravity.END);
						menu.inflate(R.menu.menu_main);
						menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {
								switch (item.getItemId()) {
									case R.id.action_none:
										mMS903.none();
										return true;
									case R.id.action_loading:
										mMS903.loading();
										return true;
									case R.id.action_empty:
										mMS903.empty();
										return true;
									case R.id.action_reload:
										mMS903.reload();
										return true;
									case R.id.action_custom_loading:
										mMS903.custom(R.layout.activity_main_custom_loading);
										return true;
									case R.id.action_custom_empty:
										mMS903.custom(getResources().getDrawable(R.mipmap.ic_launcher), "empty", "empty");
										return true;
									case R.id.action_custom_reload:
										mMS903.custom(getResources().getDrawable(R.mipmap.ic_launcher), "reload", "reload");
										return true;
									case R.id.action_custom:
										mMS903.custom(R.layout.activity_main_custom);
										return true;
									case R.id.action_dismiss:
										mMS903.dismiss();
										return true;
								}
								return false;
							}
						});
						menu.show();
					}
				})
				.textColor(HeaderManager.V_RIGHT_0, Color.WHITE)
				.dot(HeaderManager.V_DOT_RIGHT_0);

		mMS903 = (MS903) findViewById(R.id.ms903);
		mMS903.setOnActionListener(new OnActionListener() {
			@Override
			public void onAction(View view) {

			}
		});
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setBackgroundResource(android.R.color.white);
		final List<String> list = new ArrayList<>();
		final MainAdapter adapter = new MainAdapter(list);
		recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
			@Override
			public void onItemClick(final RecyclerView.ViewHolder viewHolder, MotionEvent event) {
				int type = adapter.getViewHolderType(viewHolder);
				if (type == RecyclerAdapter.TYPE_VH_CONTENT) {
					int position = adapter.getRelativePosition(type, viewHolder);
					String name = list.get(position);
					if (name.equals("RxJava")) {
						startActivity(new Intent(MainActivity.this, RxJavaActivity.class));
					} else if (name.equals("support material design")) {
						startActivity(new Intent(MainActivity.this, DesignActivity.class));
					} else if (name.equals("Bluetooth")) {
						BluetoothActivity.start(MainActivity.this);
					}
				}
			}
		});
		list.add("RxJava");
		list.add("support material design");
		list.add("Bluetooth");
//		adapter.addHeader(getLayoutInflater().inflate(R.layout.activity_main_list_header, recyclerView, false));
		MoreHelper helper = new MoreHelper(recyclerView);
		helper.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {

			}
		});
		adapter.addFooter(helper.getView());
		recyclerView.setAdapter(adapter);
		helper.setHasMore(false);
	}
}
