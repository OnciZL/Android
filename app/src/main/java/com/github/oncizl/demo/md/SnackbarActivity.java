package com.github.oncizl.demo.md;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.oncizl.demo.R;

public class SnackbarActivity extends DesignBaseActivity implements View.OnClickListener {

	private ViewGroup mContentView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int pos = getIntent().getIntExtra(ZhuXian.EXTRA_POS, -1);
		if (pos >= 0) {
			String title = ZhuXian.sTitles[pos];
			switch (title) {
				case "Snackbar/Usage":
					setContentView(R.layout.design_snackbar);
					break;
				case "Snackbar/Usage without CoordinatorLayout":
					setContentView(R.layout.design_snackbar_without_col);
					break;
				case "Snackbar/Coordinated with FAB":
					setContentView(R.layout.design_snackbar_with_fab);
					break;
			}
		} else {
			finish();
		}

		mContentView = (ViewGroup) findViewById(R.id.content_view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_short:
				Snackbar.make(mContentView, "Short snackbar message", Snackbar.LENGTH_SHORT).show();
				break;
			case R.id.btn_short_action:
				Snackbar.make(mContentView, "Short snackbar message", Snackbar.LENGTH_SHORT)
						.setAction("Action", new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Toast.makeText(SnackbarActivity.this, "Snackbar Action pressed",
										Toast.LENGTH_SHORT).show();
							}
						}).show();
				break;
			case R.id.btn_long:
				Snackbar.make(mContentView, "Long snackbar message which wraps onto another line and"
						+ "makes the Snackbar taller", Snackbar.LENGTH_SHORT).show();
				break;
			case R.id.btn_long_action1:
				Snackbar.make(mContentView, "Long snackbar message which wraps onto another line and"
						+ "makes the Snackbar taller", Snackbar.LENGTH_SHORT)
						.setAction("Action", new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Toast.makeText(SnackbarActivity.this, "Snackbar Action pressed",
										Toast.LENGTH_SHORT).show();
							}
						}).show();
				break;
			case R.id.btn_long_action2:
				Snackbar.make(mContentView, "Long snackbar message which wraps onto another line and"
						+ "makes the Snackbar taller", Snackbar.LENGTH_SHORT)
						.setAction("Action which wraps", new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Toast.makeText(SnackbarActivity.this, "Snackbar Action pressed",
										Toast.LENGTH_SHORT).show();
							}
						}).show();
				break;
		}
	}
}
