package com.github.oncizl.recycleradapter;


public abstract class RecyclerAdapterMulti extends RecyclerAdapter {

	@Override
	public abstract int getItemViewTypeCompat(int position);

	@Override
	public abstract int getItemViewTypeCountCompat();
}
