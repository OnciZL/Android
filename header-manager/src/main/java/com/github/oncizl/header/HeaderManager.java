package com.github.oncizl.header;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class HeaderManager {

	public final static int V_LAYOUT_HEADER = 1;
	public final static int V_STATUS_BAR = 2;

	public final static int V_LEFT_0 = 100;
	public final static int V_LEFT_1 = 101;

	public final static int V_RIGHT_0 = 200;
	public final static int V_RIGHT_1 = 201;

	public final static int V_TITLE = 300;
	public final static int V_SUBTITLE = 301;
	public final static int V_LAYOUT_TITLE = 302;

	public final static int V_DOT_LEFT_0 = 400;
	public final static int V_DOT_LEFT_1 = 401;
	public final static int V_DOT_RIGHT_0 = 402;
	public final static int V_DOT_RIGHT_1 = 403;

	public final static int V_DOT_NUM_LEFT_0 = 500;
	public final static int V_DOT_NUM_LEFT_1 = 501;
	public final static int V_DOT_NUM_RIGHT_0 = 502;
	public final static int V_DOT_NUM_RIGHT_1 = 503;
	private LinearLayout layoutHeader, layoutTitle;
	@Nullable
	private TextView tvLeft0, tvLeft1, tvRight0, tvRight1, tvTitle, tvSubtitle;
	@Nullable
	private View vPlaceHolder, vDotLeft0, vDotLeft1, vDotRight0, vDotRight1;
	@Nullable
	private TextView tvDotNumLeft0, tvDotNumLeft1, tvDotNumRight0, tvDotNumRight1;
	@Nullable
	private CharSequence title, subtitle;
	private int statusBarHeight;

	private HeaderManager() {
	}

	public static HeaderManager newInstance(@NonNull final Activity activity) {
		return new HeaderManager().initHeaderLayout((LinearLayout) activity.findViewById(R.id.layout_header))
				.initStatusBar(activity)
				.title(activity.getTitle())
				.left0(R.mipmap.default_back, "", new HeaderCallback() {

					@Override
					public void callback(@V_CLICKABLE int viewType, HeaderManager headerManager) {
						activity.finish();
					}
				})
				.invisible(V_RIGHT_0);
	}

	public static HeaderManager newInstance(@NonNull final Activity activity, @NonNull View rootView) {
		return new HeaderManager().initHeaderLayout((LinearLayout) rootView.findViewById(R.id.layout_header))
				.initStatusBar(activity, rootView)
				.left0(R.mipmap.default_back, "", new HeaderCallback() {

					@Override
					public void callback(@V_CLICKABLE int viewType, HeaderManager headerManager) {
						activity.finish();
					}
				})
				.invisible(V_RIGHT_0);
	}

	public static HeaderManager newInstanceOnlyStatusBar(@NonNull final Activity activity) {
		return new HeaderManager().initStatusBar(activity);
	}

	public static HeaderManager newInstanceOnlyStatusBar(@NonNull Activity activity, @NonNull View rootView) {
		return new HeaderManager().initStatusBar(activity, rootView);
	}

	private HeaderManager initStatusBar(@NonNull Activity activity) {
		if (Build.VERSION.SDK_INT >= 19) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			if (Build.VERSION.SDK_INT >= 21) {
				activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
			}
			Resources res = activity.getResources();
			statusBarHeight = res.getDimensionPixelSize(res.getIdentifier("status_bar_height", "dimen", "android"));
			vPlaceHolder = activity.findViewById(R.id.header_v_status_bar);
			if (vPlaceHolder != null) vPlaceHolder.getLayoutParams().height = statusBarHeight;
		}
		return this;
	}

	private HeaderManager initStatusBar(@NonNull Activity activity, @NonNull View rootView) {
		if (Build.VERSION.SDK_INT >= 19) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			if (Build.VERSION.SDK_INT >= 21) {
				activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
			}
			Resources res = activity.getResources();
			statusBarHeight = res.getDimensionPixelSize(res.getIdentifier("status_bar_height", "dimen", "android"));
			vPlaceHolder = rootView.findViewById(R.id.header_v_status_bar);
			if (vPlaceHolder != null) vPlaceHolder.getLayoutParams().height = statusBarHeight;
		}
		return this;
	}

	private HeaderManager initHeaderLayout(@Nullable LinearLayout layoutHeader) {
		this.layoutHeader = layoutHeader;
		if (layoutHeader != null) {
			tvLeft0 = (TextView) layoutHeader.findViewById(R.id.header_tv_left0);
			tvLeft1 = (TextView) layoutHeader.findViewById(R.id.header_tv_left1);
			tvRight0 = (TextView) layoutHeader.findViewById(R.id.header_tv_right0);
			tvRight1 = (TextView) layoutHeader.findViewById(R.id.header_tv_right1);
			tvTitle = (TextView) layoutHeader.findViewById(R.id.header_tv_title);
			tvSubtitle = (TextView) layoutHeader.findViewById(R.id.header_tv_subtitle);
			layoutTitle = (LinearLayout) layoutHeader.findViewById(R.id.header_layout_title);
			vDotLeft0 = layoutHeader.findViewById(R.id.header_v_dot_left0);
			vDotLeft1 = layoutHeader.findViewById(R.id.header_v_dot_left1);
			vDotRight0 = layoutHeader.findViewById(R.id.header_v_dot_right0);
			vDotRight1 = layoutHeader.findViewById(R.id.header_v_dot_right1);
			tvDotNumLeft0 = (TextView) layoutHeader.findViewById(R.id.header_tv_dot_num_left0);
			tvDotNumLeft1 = (TextView) layoutHeader.findViewById(R.id.header_tv_dot_num_left1);
			tvDotNumRight0 = (TextView) layoutHeader.findViewById(R.id.header_tv_dot_num_right0);
			tvDotNumRight1 = (TextView) layoutHeader.findViewById(R.id.header_tv_dot_num_right1);
		}
		return this;
	}

	public int statusBarHeight() {
		return statusBarHeight;
	}

	public CharSequence title() {
		return title;
	}

	public HeaderManager title(@NonNull CharSequence title) {
		if (!TextUtils.isEmpty(title)) {
			this.title = title;
			if (tvTitle != null) {
				tvTitle.setText(title);
				visible(V_TITLE);
			}
		}
		return this;
	}

	public HeaderManager title(@NonNull CharSequence title, final HeaderCallback headerCallback) {
		if (!TextUtils.isEmpty(title)) {
			this.title = title;
			if (tvTitle != null) {
				tvTitle.setText(title);
				visible(V_TITLE);
				callback(V_LAYOUT_TITLE, headerCallback);
			}
		}
		return this;
	}

	public CharSequence subtitle() {
		return subtitle;
	}

	public HeaderManager subtitle(@NonNull CharSequence subtitle) {
		if (!TextUtils.isEmpty(subtitle)) {
			this.subtitle = subtitle;
			if (tvSubtitle != null) {
				tvSubtitle.setText(subtitle);
				visible(V_SUBTITLE);
			}
		}
		return this;
	}

	public HeaderManager left0(int ivResId, @Nullable String text, final HeaderCallback headerCallback) {
		if (tvLeft0 != null) {
			mipmap(V_LEFT_0, ivResId, 0, 0, 0);
			text(V_LEFT_0, text);
			callback(V_LEFT_0, headerCallback);
		}
		return this;
	}

	public HeaderManager left1(int ivResId, @Nullable String text, final HeaderCallback headerCallback) {
		if (tvLeft1 != null) {
			mipmap(V_LEFT_1, ivResId, 0, 0, 0);
			text(V_LEFT_1, text);
			callback(V_LEFT_1, headerCallback);
		}
		return this;
	}

	public HeaderManager right0(int ivResId, @Nullable String text, final HeaderCallback headerCallback) {
		if (tvRight0 != null) {
			mipmap(V_RIGHT_0, ivResId, 0, 0, 0);
			text(V_RIGHT_0, text);
			callback(V_RIGHT_0, headerCallback);
		}
		return this;
	}

	public HeaderManager right1(int ivResId, @Nullable String text, final HeaderCallback headerCallback) {
		if (tvRight1 != null) {
			mipmap(V_RIGHT_1, ivResId, 0, 0, 0);
			text(V_RIGHT_1, text);
			callback(V_RIGHT_1, headerCallback);
		}
		return this;
	}

	public HeaderManager dotNum(@V_DOT_NUM int viewType, int num) {
		if (num > 0) {
			visible(viewType);
			text(viewType, String.valueOf(num));
		}
		return this;
	}

	public HeaderManager dot(@V_DOT int viewType) {
		visible(viewType);
		return this;
	}

	public HeaderManager mipmap(@V_DRAWABLE int viewType, @DrawableRes int leftResId, @DrawableRes int topResId,
	                            @DrawableRes int rightResId, @DrawableRes int bottomResId) {
		TextView textView = (TextView) switchView(viewType);
		Drawable[] drawables = new Drawable[4];
		if (textView != null) {
			Resources res = textView.getResources();
			visible(viewType);
			if (leftResId >>> 24 >= 2) {
				drawables[0] = res.getDrawable(leftResId);
				if (drawables[0] != null) {
					drawables[0].setBounds(0, 0, drawables[0].getIntrinsicWidth(), drawables[0].getIntrinsicWidth());
				}
			}
			if (topResId >>> 24 >= 2) {
				drawables[1] = res.getDrawable(topResId);
				if (drawables[1] != null) {
					drawables[1].setBounds(0, 0, drawables[1].getIntrinsicWidth(), drawables[1].getIntrinsicWidth());
				}
			}
			if (rightResId >>> 24 >= 2) {
				drawables[2] = res.getDrawable(rightResId);
				if (drawables[2] != null) {
					drawables[2].setBounds(0, 0, drawables[2].getIntrinsicWidth(), drawables[2].getIntrinsicWidth());
				}
			}
			if (bottomResId >>> 24 >= 2) {
				drawables[3] = res.getDrawable(bottomResId);
				if (drawables[3] != null) {
					drawables[3].setBounds(0, 0, drawables[3].getIntrinsicWidth(), drawables[3].getIntrinsicWidth());
				}
			}
			textView.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
		}
		return this;
	}

	public HeaderManager background(@V int viewType, @DrawableRes int drawableResId) {
		View view = switchView(viewType);
		if (view != null) view.setBackgroundResource(drawableResId);
		return this;
	}

	public HeaderManager textColor(@V_TEXT int viewType, @ColorInt int color) {
		TextView textView = (TextView) switchView(viewType);
		if (textView != null) {
			textView.setTextColor(color);
		}
		return this;
	}

	public HeaderManager text(@V_TEXT int viewType, @Nullable String text) {
		TextView textView = (TextView) switchView(viewType);
		if (textView != null) {
			visible(viewType);
			if (!TextUtils.isEmpty(text)) {
				textView.setText(text);
			} else {
				textView.setText("");
			}
		}
		return this;
	}

	public HeaderManager callback(@V_CLICKABLE final int viewType, final HeaderCallback headerCallback) {
		View clickableView = switchView(viewType);
		if (clickableView != null) {
			visible(viewType);
			if (headerCallback != null) {
				clickableView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						headerCallback.callback(viewType, HeaderManager.this);
					}
				});
			} else {
				clickableView.setOnClickListener(null);
			}
		}
		return this;
	}

	public HeaderManager visible(@V int viewType) {
		View view = switchView(viewType);
		if (view != null) view.setVisibility(View.VISIBLE);
		return this;
	}

	public HeaderManager gone(@V int viewType) {
		View view = switchView(viewType);
		if (view != null) view.setVisibility(View.GONE);
		return this;
	}

	public HeaderManager invisible(@V int viewType) {
		View view = switchView(viewType);
		if (view != null) view.setVisibility(View.INVISIBLE);
		return this;
	}

	public View switchView(@V int viewType) {
		View view = null;
		switch (viewType) {
			case V_LAYOUT_HEADER:
				view = layoutHeader;
				break;
			case V_STATUS_BAR:
				view = vPlaceHolder;
				break;
			case V_LEFT_0:
				view = tvLeft0;
				break;
			case V_LEFT_1:
				view = tvLeft1;
				break;
			case V_RIGHT_0:
				view = tvRight0;
				break;
			case V_RIGHT_1:
				view = tvRight1;
				break;
			case V_TITLE:
				view = tvTitle;
				break;
			case V_SUBTITLE:
				view = tvSubtitle;
				break;
			case V_LAYOUT_TITLE:
				view = layoutTitle;
				break;
			case V_DOT_LEFT_0:
				view = vDotLeft0;
				break;
			case V_DOT_LEFT_1:
				view = vDotLeft1;
				break;
			case V_DOT_RIGHT_0:
				view = vDotRight0;
				break;
			case V_DOT_RIGHT_1:
				view = vDotRight1;
				break;
			case V_DOT_NUM_LEFT_0:
				view = tvDotNumLeft0;
				break;
			case V_DOT_NUM_LEFT_1:
				view = tvDotNumLeft1;
				break;
			case V_DOT_NUM_RIGHT_0:
				view = tvDotNumRight0;
				break;
			case V_DOT_NUM_RIGHT_1:
				view = tvDotNumRight1;
				break;
		}
		return view;
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({V_LAYOUT_HEADER, V_STATUS_BAR,
			V_LEFT_0, V_LEFT_1, V_RIGHT_0, V_RIGHT_1,
			V_LAYOUT_TITLE, V_TITLE, V_SUBTITLE,
			V_DOT_LEFT_0, V_DOT_LEFT_1, V_DOT_RIGHT_0, V_DOT_RIGHT_1,
			V_DOT_NUM_LEFT_0, V_DOT_NUM_LEFT_1, V_DOT_NUM_RIGHT_0, V_DOT_NUM_RIGHT_1})
	public @interface V {
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({V_DOT_LEFT_0, V_DOT_LEFT_1, V_DOT_RIGHT_0, V_DOT_RIGHT_1})
	public @interface V_DOT {
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({V_DOT_NUM_LEFT_0, V_DOT_NUM_LEFT_1, V_DOT_NUM_RIGHT_0, V_DOT_NUM_RIGHT_1})
	public @interface V_DOT_NUM {
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({V_TITLE, V_SUBTITLE, V_LEFT_0, V_LEFT_1, V_RIGHT_0, V_RIGHT_1,
			V_DOT_NUM_LEFT_0, V_DOT_NUM_LEFT_1, V_DOT_NUM_RIGHT_0, V_DOT_NUM_RIGHT_1})
	public @interface V_TEXT {
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({V_LEFT_0, V_LEFT_1, V_RIGHT_0, V_RIGHT_1, V_TITLE, V_SUBTITLE})
	public @interface V_DRAWABLE {
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({V_LEFT_0, V_LEFT_1, V_RIGHT_0, V_RIGHT_1, V_LAYOUT_TITLE})
	public @interface V_CLICKABLE {
	}

}
