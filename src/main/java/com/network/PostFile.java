package com.network;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.bean.RasPiDetails;
import com.manager.FilesManager;

public class PostFile{
	Logger logger = Logger.getLogger(PostFile.class);
	public static Boolean inProgress = false;
	static ExecutorService executor = Executors.newFixedThreadPool(5);
	
	public void runPost() {
		synchronized (inProgress) {
			if(inProgress)return;
			
			final String dir = System.getProperty("user.dir");
			File folder = new File(dir);
			if(folder.exists()){
				inProgress = true;
				File[] dataFiles = folder.listFiles();
				int filesLength = dataFiles.length;
				for(int index=0;index<filesLength;index++){
					if(dataFiles[index].getName().endsWith(".dat") && dataFiles[index].exists()){
						FilesManager filesManager = new FilesManager();
						RasPiDetails rasPiDetails = filesManager.getFileData(dataFiles[index].getAbsolutePath());
						dataFiles[index].delete();
						printMessage(rasPiDetails.getComData()[0].getText(),new Date(rasPiDetails.getComData()[0].getDate()),rasPiDetails.getGpioStatus(),new Date());
					}
				}
				inProgress = false;
			}
		}
	}
	
	private void printMessage(String message,Date uartEventTime,HashMap<String, Boolean> gpioState,Date gpioEventTIme){
		PostManNew postMan = new PostManNew(message,uartEventTime,gpioState,gpioEventTIme);
		executor.execute(postMan);
	}

}
