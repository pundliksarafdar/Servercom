package com.utils;

import java.io.IOException;

import com.bean.RasPiDetails;
import com.pi4j.system.SystemInfo;

public class SystemInfoUtils {
	
	public static void getSystemInfo(RasPiDetails rasPiDetails) throws NumberFormatException, UnsupportedOperationException, IOException, InterruptedException{
		rasPiDetails.setTemp((int) SystemInfo.getCpuTemperature());
		
		Long clockFreq = SystemInfo.getClockFrequencyCore();
		rasPiDetails.setFreq(clockFreq.intValue());
		
		String sName = SystemInfo.getSerial();
		rasPiDetails.setName(sName);
	}
	
	
}
