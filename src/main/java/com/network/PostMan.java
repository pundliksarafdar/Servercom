package com.network;

import java.io.IOException;
import java.util.HashMap;

import com.bean.RasPiDetails;
import com.utils.SystemInfoUtils;

public class PostMan {
public static void postData(HashMap<String, Boolean> gpioState){
	
	RasPiDetails rasPiDetails = new RasPiDetails();
	if(null!=gpioState){
		rasPiDetails.setGpioStatus(gpioState);
	}
	try {
		SystemInfoUtils.getSystemInfo(rasPiDetails);
		System.out.println("RasPiDetails "+rasPiDetails.toString());
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnsupportedOperationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(rasPiDetails.getId() != 0){
		try {
			HttpClient client = new HttpClient();
			client.sendPost(rasPiDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}
}
}
