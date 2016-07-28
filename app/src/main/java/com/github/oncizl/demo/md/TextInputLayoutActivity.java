package com.github.oncizl.demo.md;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.oncizl.demo.R;

public class TextInputLayoutActivity extends AppCompatActivity implements View.OnClickListener {

	private TextInputLayout mUsernameInputLayout;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.design_text_input);

		mUsernameInputLayout = (TextInputLayout) findViewById(R.id.input_username);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_show_error:
				mUsernameInputLayout.setError("Some unknown error has occurred");
				break;
			case R.id.btn_clear_error:
				mUsernameInputLayout.setError(null);
				break;
		}
	}
}
