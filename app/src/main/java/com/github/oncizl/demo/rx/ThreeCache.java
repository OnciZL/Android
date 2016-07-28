package com.github.oncizl.demo.rx;

import android.graphics.Bitmap;

public class ThreeCache {

	String url;
	Bitmap bitmap;
	String from;

	public ThreeCache(String url) {
		this.url = url;
	}

	public String url() {
		return url;
	}

	public Bitmap bitmap() {
		return bitmap;
	}

	public ThreeCache bitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		return this;
	}

	public String from() {
		return from;
	}

	public ThreeCache from(String from) {
		this.from = from;
		return this;
	}
}
