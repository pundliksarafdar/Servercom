package com.pundlik;

import com.network.PostMan;
import com.utils.GPIOInfoUtils;
import com.utils.SerialPortUtil;

public class Notifier {
	public static void main(String[] args) {
		System.out.println("ServerCom started");
		//This is registration request on start
		//PostMan.postData(null);
		GPIOInfoUtils gpioInfoUtils = GPIOInfoUtils.getInstance();
		gpioInfoUtils.register(Thread.currentThread());
		System.out.println("Thread alive check......");
		if(!gpioInfoUtils.isAlive()){
			System.out.println("Starting port listner");
			gpioInfoUtils.start();
		}
		
		SerialPortUtil serialPortUtil = SerialPortUtil.getInstance();
		if(!serialPortUtil.isAlive()){
			System.out.println("Starting Com port listner");
			serialPortUtil.start();
		}
		
		System.out.println("Starting loop");
		while(true){
			try {
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				System.out.println("INturrupted");
				System.out.println("Posting data");
				PostMan.postData(null);				
			}
		}
	}
}
