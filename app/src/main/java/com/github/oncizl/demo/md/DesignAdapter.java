package com.github.oncizl.demo.md;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.oncizl.demo.R;

import java.util.ArrayList;
import java.util.List;

public class DesignAdapter extends RecyclerView.Adapter<DesignAdapter.ViewHolder> {

	private static final String TAG = "DesignAdapter";

	private ArrayList<String> mArrayList;
	private OnItemClickListener mOnItemClickListener;
	private int mBackgroundResId;

	public DesignAdapter(ArrayList<String> arrayList, OnItemClickListener onItemClickListener) {
		mArrayList = arrayList;
		mOnItemClickListener = onItemClickListener;
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		Log.d(TAG, "onAttachedToRecyclerView: ");
		TypedValue val = new TypedValue();
		if (recyclerView != null) {
			recyclerView.getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, val, true);
			mBackgroundResId = val.resourceId;
		}
	}

	@Override
	public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		Log.d(TAG, "onDetachedFromRecyclerView: ");
	}

	@Override
	public void onViewAttachedToWindow(ViewHolder holder) {
		super.onViewAttachedToWindow(holder);
		Log.d(TAG, "onViewAttachedToWindow: ");
	}

	@Override
	public void onViewDetachedFromWindow(ViewHolder holder) {
		super.onViewDetachedFromWindow(holder);
		Log.d(TAG, "onViewDetachedFromWindow: ");
	}

	@Override
	public boolean onFailedToRecycleView(ViewHolder holder) {
		Log.d(TAG, "onFailedToRecycleView: ");
		return super.onFailedToRecycleView(holder);
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);
		Log.d(TAG, "onViewRecycled: ");
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Log.d(TAG, "onCreateViewHolder: ");
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.design_list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.name.setText(mArrayList.get(position));
		holder.name.setBackgroundResource(mBackgroundResId);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnItemClickListener!=null) {
					mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
				}
			}
		});
		Log.d(TAG, "onBindViewHolder: 1");
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
		super.onBindViewHolder(holder, position, payloads);
		Log.d(TAG, "onBindViewHolder: 2");
	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount: ");
		return mArrayList == null ? 0 : mArrayList.size();
	}

	@Override
	public int getItemViewType(int position) {
		Log.d(TAG, "getItemViewType: ");
		return super.getItemViewType(position);
	}

	final static class ViewHolder extends RecyclerView.ViewHolder {

		TextView name;

		public ViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(android.R.id.text1);
		}
	}
}
