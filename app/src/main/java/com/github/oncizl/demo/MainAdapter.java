package com.github.oncizl.demo;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.oncizl.recycleradapter.RecyclerAdapter;

import java.util.List;

public class MainAdapter extends RecyclerAdapter<MainAdapter.Holder> {

	private List<String> mList;

	public MainAdapter(List<String> list) {
		mList = list;
	}

	@Override
	public Holder onCreateViewHolderCompat(ViewGroup parent, int viewType) {
		if (viewType == 0) {
			return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_list_item, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolderCompat(Holder holder, int position) {
		holder.textView.setText(mList.get(position));
	}

	@Override
	public int getItemCountCompat() {
		return mList == null ? 0 : mList.size();
	}

	class Holder extends RecyclerView.ViewHolder {

		TextView textView;

		public Holder(View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.text_view);
		}
	}
}
