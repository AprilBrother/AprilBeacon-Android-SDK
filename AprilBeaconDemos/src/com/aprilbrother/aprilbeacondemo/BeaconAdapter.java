package com.aprilbrother.aprilbeacondemo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aprilbrother.aprilbrothersdk.Beacon;

public class BeaconAdapter extends BaseAdapter {

	private ArrayList<Beacon> beacons;
	private LayoutInflater inflater;

	public BeaconAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
		this.beacons = new ArrayList<Beacon>();
	}

	public void replaceWith(Collection<Beacon> newBeacons) {
		this.beacons.clear();
		this.beacons.addAll(newBeacons);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return beacons.size();
	}

	@Override
	public Beacon getItem(int position) {
		return beacons.get(position);
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

	private void bind(Beacon beacon, View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		String mac = holder.macTextView.getText().toString().trim();
		if(!TextUtils.isEmpty(mac)){
			String lastDistanceStr = mac.substring(mac.indexOf("(")+1, mac.lastIndexOf("m"));
			double  lastDistance= Double.parseDouble(lastDistanceStr);
			if(lastDistance>beacon.getDistance()){
				if(lastDistance-beacon.getDistance()>0.2){
					double tempDistance = lastDistance-0.2;
					BigDecimal bg = new BigDecimal(tempDistance);
					double showDistance = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					holder.macTextView.setText("MAC : "+beacon.getMacAddress()+"("+showDistance+"m)");
				}else{
					holder.macTextView.setText("MAC : "+beacon.getMacAddress()+"("+beacon.getDistance()+"m)");
				}
			}else if(lastDistance<beacon.getDistance()){
				if(beacon.getDistance()-lastDistance>0.2){
					double tempDistance = lastDistance+0.2;
					BigDecimal bg = new BigDecimal(tempDistance);
					double showDistance = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					holder.macTextView.setText("MAC : "+beacon.getMacAddress()+"("+showDistance+"m)");
				}else{
					holder.macTextView.setText("MAC : "+beacon.getMacAddress()+"("+beacon.getDistance()+"m)");
				}
			}
		}else{
			holder.macTextView.setText("MAC : "+beacon.getMacAddress()+"("+beacon.getDistance()+"m)");
		}
//        holder.macTextView.setText("MAC : "+beacon.getMacAddress()+"("+beacon.getDistance()+"m)");
		holder.uuidTextView.setText("UUID: " + beacon.getProximityUUID());
		holder.majorTextView.setText("Major: " + beacon.getMajor());
		holder.minorTextView.setText("Minor: " + beacon.getMinor());
		holder.measuredPowerTextView.setText("MPower: "
				+ beacon.getMeasuredPower());
		holder.rssiTextView.setText("RSSI: " + beacon.getRssi());

		switch (beacon.getProximity()) {
		case 0:
			holder.proximityView.setText("proximity: " + "unknow");
			break;
		case 1:
			holder.proximityView.setText("proximity: " + "immediate");
			break;
		case 2:
			holder.proximityView.setText("proximity: " + "near");
			break;
		case 3:
			holder.proximityView.setText("proximity: " + "far");
			break;
		default:
			break;
		}
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.device_item, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	static class ViewHolder {
		final TextView macTextView;
		final TextView uuidTextView;
		final TextView majorTextView;
		final TextView minorTextView;
		final TextView measuredPowerTextView;
		final TextView rssiTextView;
		final TextView proximityView;

		ViewHolder(View view) {
			macTextView = (TextView) view.findViewWithTag("mac");
			uuidTextView = (TextView) view.findViewWithTag("uuid");
			majorTextView = (TextView) view.findViewWithTag("major");
			minorTextView = (TextView) view.findViewWithTag("minor");
			measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
			rssiTextView = (TextView) view.findViewWithTag("rssi");
			proximityView = (TextView) view.findViewWithTag("proximity");
		}
	}
}
