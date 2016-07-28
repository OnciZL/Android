package com.github.oncizl.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ViewHelper {

	private static final long DEFAULT_CLICK_INTERVAL_TIME = 500;

	private static long sClickTime;

	private ViewHelper() {
	}

	public static void clicks(final View.OnClickListener onClickListener, View... views) {
		for (View v : views) {
			if (v != null) {
				if (onClickListener != null) {
					v.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							long cur = System.currentTimeMillis();
							if (cur - sClickTime >= DEFAULT_CLICK_INTERVAL_TIME) {
								sClickTime = cur;
								onClickListener.onClick(v);
							}
						}
					});
				}
			}
		}
	}

	public static void enables(View... views) {
		for (View v : views) {
			if (v != null) {
				v.setEnabled(true);
			}
		}
	}

	public static void disables(View... views) {
		for (View v : views) {
			if (v != null) {
				v.setEnabled(false);
			}
		}
	}

	public static void visibles(View... views) {
		for (View v : views) {
			if (v != null) {
				v.setVisibility(View.VISIBLE);
			}
		}
	}

	public static void invisibles(View... views) {
		for (View v : views) {
			if (v != null) {
				v.setVisibility(View.INVISIBLE);
			}
		}
	}

	public static void gones(View... views) {
		for (View v : views) {
			if (v != null) {
				v.setVisibility(View.GONE);
			}
		}
	}

	public static void touches(final View.OnTouchListener onTouchListener, View... views) {
		for (View v : views) {
			if (v != null) {
				if (onTouchListener != null) {
					v.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							return onTouchListener.onTouch(v, event);
						}
					});
				}
			}
		}
	}

	public static void textChanges(final OnTextChangedListener onTextChangedListener, final TextView... textViews) {
		final Map<View, CharSequence> map = new HashMap<>();
		for (final TextView v : textViews) {
			if (v != null) {
				if (onTextChangedListener != null) {
					map.put(v, "");
					v.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						}

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							map.put(v, s);
							CharSequence[] charSequences = new CharSequence[textViews.length];
							Set<View> set = map.keySet();
							Iterator<View> it = set.iterator();
							while (it.hasNext()) {
								View view = it.next();
								if (view != null) {
									for (int i = 0; i < textViews.length; i++) {
										if (view == textViews[i]) {
											charSequences[i] = map.get(view);
										}
									}
								}
							}
							onTextChangedListener.onTextChanged(charSequences);
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});
				}
			}
		}
	}

	public interface OnTextChangedListener {
		void onTextChanged(CharSequence... charSequences);
	}
}
