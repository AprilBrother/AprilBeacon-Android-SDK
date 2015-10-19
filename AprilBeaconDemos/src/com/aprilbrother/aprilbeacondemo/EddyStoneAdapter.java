package com.aprilbrother.aprilbeacondemo;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aprilbrother.aprilbrothersdk.EddyStone;

public class EddyStoneAdapter extends BaseAdapter {

	private ArrayList<EddyStone> eddyStones;
	private LayoutInflater inflater;

	public EddyStoneAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
		this.eddyStones = new ArrayList<EddyStone>();
	}

	public void replaceWith(Collection<EddyStone> newBeacons) {
		this.eddyStones.clear();
		this.eddyStones.addAll(newBeacons);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return eddyStones.size();
	}

	@Override
	public EddyStone getItem(int position) {
		return eddyStones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = inflateIfRequired(view, position, parent);
		bind(getItem(position), view);
		return view;
	}

	private void bind(EddyStone eddyStone, View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(eddyStone.getName());
		holder.macTextView.setText(eddyStone.getMacAddress());
		if(eddyStone.getModel().equals("url")){
			holder.uuidTextView.setText("URL : "+eddyStone.getUrl());
			holder.modelTextView.setText("MODEL ： "+"url");
		}else if(eddyStone.getModel().equals("uid")){
			holder.uuidTextView.setText("UID : "+eddyStone.getUid());
			holder.modelTextView.setText("MODEL ： "+"uid");
		}
		holder.rssiTextView.setText("RSSI : "+eddyStone.getRssi());
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.devices_list_item, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	static class ViewHolder {
		final TextView name;
		final TextView macTextView;
		final TextView uuidTextView;
		final TextView modelTextView;
		final TextView rssiTextView;

		ViewHolder(View view) {
			name = (TextView) view.findViewWithTag("name");
			macTextView = (TextView) view.findViewWithTag("mac");
			uuidTextView = (TextView) view.findViewWithTag("uuid");
			modelTextView = (TextView) view.findViewWithTag("model");
			rssiTextView = (TextView) view.findViewWithTag("rssi");
		}
	}
}
