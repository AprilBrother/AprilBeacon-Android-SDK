package com.aprilbrother.aprilbeacondemo;

import java.util.Comparator;

import com.aprilbrother.aprilbrothersdk.Beacon;


public class ComparatorBeaconByRssi implements Comparator<Beacon> {

	@Override
	public int compare(Beacon lhs, Beacon rhs) {
		
		if(lhs.getRssi() == rhs.getRssi()){
			int flag=lhs.getProximityUUID().compareTo(rhs.getProximityUUID());
			  if(flag==0){
				  if(lhs.getMajor() == rhs.getMajor()){
					  if(lhs.getMinor() == rhs.getMinor()){
						  return 0;
					  }else{
						  return lhs.getMinor() - rhs.getMinor();
					  }
				  }else{
					  return lhs.getMajor() - rhs.getMajor();
				  }
			  }else{
			   return flag;
			  }  
		}else{
			return rhs.getRssi() - lhs.getRssi();
		}
	}
}
