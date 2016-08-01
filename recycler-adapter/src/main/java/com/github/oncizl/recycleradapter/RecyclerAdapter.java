package com.github.oncizl.recycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

	public static final int TYPE_VH_HEADER = 0;
	public static final int TYPE_VH_CONTENT = 1;
	public static final int TYPE_VH_FOOTER = 2;

	private List<View> mHeaderList = new ArrayList<>();
	private List<View> mFooterList = new ArrayList<>();

	public void addHeader(View header) {
		mHeaderList.add(header);
		notifyDataSetChanged();
	}

	public void removeHeader(View header) {
		mHeaderList.remove(header);
		notifyDataSetChanged();
	}

	public void removeHeader(int index) {
		mHeaderList.remove(index);
		notifyDataSetChanged();
	}

	public void removeAllHeaders() {
		mHeaderList.clear();
		notifyDataSetChanged();
	}

	public void addFooter(View footer) {
		mFooterList.add(footer);
		notifyDataSetChanged();
	}

	public void removeFooter(View footer) {
		mFooterList.remove(footer);
		notifyDataSetChanged();
	}

	public void removeFooter(int index) {
		mFooterList.remove(index);
		notifyDataSetChanged();
	}

	public void removeAllFooters() {
		mFooterList.clear();
		notifyDataSetChanged();
	}

	@Override
	public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		removeAllHeaders();
		removeAllFooters();
	}

	public boolean isHeaderOrFooter(int position) {
		int headerCount = mHeaderList.size();
		int contentCount = getItemCountCompat();
		return !(position >= headerCount && position < headerCount + contentCount);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {
		int headerCount = mHeaderList.size();
		if (viewType < headerCount) {
			return (VH) new RecyclerView.ViewHolder(mHeaderList.get(viewType)) {
			};
		} else if (viewType >= headerCount && viewType < headerCount + getItemViewTypeCountCompat()) {
			return onCreateViewHolderCompat(parent, viewType - headerCount);
		} else {
			return (VH) new RecyclerView.ViewHolder(mFooterList.get(viewType - headerCount - getItemViewTypeCountCompat())) {
			};
		}
	}

	@Deprecated
	@Override
	public void onBindViewHolder(VH holder, int position) {
		int headerCount = mHeaderList.size();
		int contentCount = getItemCountCompat();
		if (position >= headerCount && position < headerCount + contentCount) {
			onBindViewHolderCompat(holder, position - headerCount);
		}
	}

	@Deprecated
	@Override
	public int getItemCount() {
		int headerCount = mHeaderList.size();
		int contentCount = getItemCountCompat();
		int footerCount = mFooterList.size();
		return headerCount + contentCount + footerCount;
	}

	@Deprecated
	@Override
	public int getItemViewType(int position) {
		int headerCount = mHeaderList.size();
		int contentCount = getItemCountCompat();
		if (position < headerCount) {
			return position;
		} else if (position >= headerCount && position < headerCount + contentCount) {
			return getItemViewTypeCompat(position - headerCount) + headerCount;
		} else {
			return headerCount + getItemViewTypeCountCompat() + (position - headerCount - contentCount);
		}
	}

	public int getViewHolderType(RecyclerView.ViewHolder holder) {
		int position = holder.getAdapterPosition();
		int headerCount = mHeaderList.size();
		int contentCount = getItemCountCompat();
		int footerCount = mFooterList.size();
		if (headerCount > 0) {
			if (position >= 0 && position < headerCount) {
				return TYPE_VH_HEADER;
			} else if (position >= headerCount && position < headerCount + contentCount) {
				return TYPE_VH_CONTENT;
			} else if (position >= headerCount + contentCount && position < headerCount + contentCount + footerCount) {
				return TYPE_VH_FOOTER;
			}
		} else {
			if (position >= 0 && position < contentCount) {
				return TYPE_VH_CONTENT;
			} else if (position >= contentCount && position < contentCount + footerCount) {
				return TYPE_VH_FOOTER;
			}
		}
		return -1;
	}

	public int getRelativePosition(int type, RecyclerView.ViewHolder holder) {
		int position = holder.getAdapterPosition();
		int headerCount = mHeaderList.size();
		int contentCount = getItemCountCompat();
		if (type == TYPE_VH_HEADER) {
			return position;
		} else if (type == TYPE_VH_CONTENT) {
			return position - headerCount;
		} else if (type == TYPE_VH_FOOTER) {
			return position - headerCount - contentCount;
		}
		return -1;
	}

	int getItemViewTypeCompat(int position) {
		return 0;
	}

	int getItemViewTypeCountCompat() {
		return 1;
	}

	public abstract VH onCreateViewHolderCompat(ViewGroup parent, int viewType);

	public abstract void onBindViewHolderCompat(VH holder, int position);

	public abstract int getItemCountCompat();
}
