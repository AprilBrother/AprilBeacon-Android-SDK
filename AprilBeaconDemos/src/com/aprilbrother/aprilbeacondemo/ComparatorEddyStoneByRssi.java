package com.aprilbrother.aprilbeacondemo;

import java.util.Comparator;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.EddyStone;


public class ComparatorEddyStoneByRssi implements Comparator<EddyStone> {

	@Override
	public int compare(EddyStone lhs, EddyStone rhs) {
		return rhs.getRssi() - lhs.getRssi();
	}
}
