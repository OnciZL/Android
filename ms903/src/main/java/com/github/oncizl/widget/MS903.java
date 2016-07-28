package com.github.oncizl.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MS903 extends FrameLayout {

	private static final String TAG = "MS903";

	private View mChildView;
	private View mCustomView;
	private OnActionListener mOnActionListener;

	public MS903(Context context) {
		super(context, null);
	}

	public MS903(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public MS903(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public MS903(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onFinishInflate() {
		int childCount = getChildCount();
		if (childCount > 1) {
			throw new IllegalStateException("MS903 only can host 1 elements");
		} else if (childCount == 1) {
			mChildView = getChildAt(0);
		}
		super.onFinishInflate();
	}

	public void none() {
		custom(new View(getContext()));
	}

	public void loading() {
		custom(R.layout.ms903_loading);
	}

	public void empty() {
		Resources res = getResources();
		custom(res.getDrawable(R.mipmap.ms903_empty),
				res.getString(R.string.ms903_empty_tips),
				res.getString(R.string.ms903_empty_action));
	}

	public void reload() {
		Resources res = getResources();
		custom(res.getDrawable(R.mipmap.ms903_reload),
				res.getString(R.string.ms903_reload_tips),
				res.getString(R.string.ms903_reload_action));
	}

	public void custom(Drawable drawable, String tips, String action) {

		View view = LayoutInflater.from(getContext()).inflate(R.layout.ms903_action, this, false);

		ImageView imageView = (ImageView) view.findViewById(R.id.ms903_image);
		if (imageView !=null) {
			if (drawable == null) {
				imageView.setVisibility(GONE);
			} else {
				imageView.setVisibility(VISIBLE);
				imageView.setImageDrawable(drawable);
			}
		}

		TextView textView = (TextView) view.findViewById(R.id.ms903_tips);
		if (textView !=null) {
			if (TextUtils.isEmpty(tips)) {
				textView.setVisibility(GONE);
			} else {
				textView.setVisibility(VISIBLE);
				textView.setText(tips);
			}
		}

		Button button = (Button) view.findViewById(R.id.ms903_action);
		if (button !=null) {
			if (TextUtils.isEmpty(action)) {
				button.setVisibility(GONE);
			} else {
				button.setVisibility(VISIBLE);
				button.setText(action);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mOnActionListener != null) {
							mOnActionListener.onAction(v);
						}
					}
				});
			}
		}

		custom(view);
	}

	public void custom(int resId) {
		custom(LayoutInflater.from(getContext()).inflate(resId, this, false));
	}

	public void custom(View customView) {
		removeView(mCustomView);
		mCustomView = customView;
		if (mChildView != null) mChildView.setVisibility(GONE);
		if (mCustomView != null) mCustomView.setVisibility(VISIBLE);
		addView(mCustomView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void dismiss() {
		removeView(mCustomView);
		if (mChildView != null) mChildView.setVisibility(VISIBLE);
	}

	public void setOnActionListener(OnActionListener onActionListener) {
		mOnActionListener = onActionListener;
	}
}
