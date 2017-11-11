package com.pundlik;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.license.LicenseRequestGenerator;
import com.license.LicenseValidator;
import com.network.PostManNew;
import com.network.SocketWatcher;
import com.utils.UARTEvent;

public class NotifierThread {
	Logger logger = Logger.getLogger(NotifierThread.class);
	ExecutorService executor;
	private  void init() {
		executor = Executors.newFixedThreadPool(5);
		
		logger.info("Starting socket watcher........");
		SocketWatcher socketWatcher = new SocketWatcher();
		Thread socketWatcherThread = new Thread(socketWatcher);
		socketWatcherThread.start();
		
		UARTEvent uartUtils = new UARTEvent();
		synchronized (uartUtils) {
			while(true){
				try {
					logger.info("Waiting.......");
					uartUtils.wait();
					logger.info("Out of waiting........");
					printMessage(uartUtils.message,uartUtils.uartEventTime,uartUtils.gpioState,uartUtils.gpioEventTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		}
	}
	
	private void printMessage(String message,Date uartEventTime,HashMap<String, Boolean> gpioState,Date gpioEventTIme){
		PostManNew postMan = new PostManNew(message,uartEventTime,gpioState,gpioEventTIme);
		executor.execute(postMan);
	}
	
	public static void main(String[] args) {
		NotifierThread notifierThread = new NotifierThread();
		notifierThread.logger.info("Validating license........");
		boolean isLicenseValid = LicenseValidator.isLicenseValid();
		if(!isLicenseValid){
			notifierThread.logger.error("Invalid license file exiting application.....");
			try {
				notifierThread.logger.error("Generating key file");
				File file = LicenseRequestGenerator.generateLicRequestFile();
				notifierThread.logger.error("Generate license file with this key file at location "+file !=null?file.getAbsoluteFile():"");
				notifierThread.logger.error("Exiting application.....");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			notifierThread.logger.info("License matched moving to next step....");
		}
		notifierThread.logger.info("Starting server........");
		notifierThread.init();
	}
}
