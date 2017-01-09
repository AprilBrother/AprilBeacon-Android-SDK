package com.aprbrother.aprilscan;


import com.aprbrother.aprilbeaconscansdk.EddyStone;

import java.util.Comparator;



public class ComparatorEddyStoneByRssi implements Comparator<EddyStone> {

	@Override
	public int compare(EddyStone lhs, EddyStone rhs) {
		return rhs.getRssi() - lhs.getRssi();
	}
}
