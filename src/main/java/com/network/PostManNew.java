package com.network;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bean.ComData;
import com.bean.RasPiDetails;
import com.manager.FilesManager;
import com.utils.SystemInfoUtils;

public class PostManNew implements Runnable{
	Logger logger = Logger.getLogger(PostManNew.class);
	String message;
	Date uartEventTime = null;
	Date gpioEventTIme = null;
	
	HashMap<String, Boolean> gpioState;
	
	public PostManNew(String message,Date uartEventTime,HashMap<String, Boolean> gpioState,Date gpioEventTIme) {
		this.message = message;
		this.uartEventTime = uartEventTime;
		
		this.gpioState = gpioState;
		this.gpioEventTIme = gpioEventTIme;
	}
	@Override
	public void run() {
		postData(this.message,this.uartEventTime,this.gpioState,this.gpioEventTIme);
	}
	
	private void postData(String message,Date uartEventTime,HashMap<String, Boolean> gpioState,Date gpioEventTIme){
		RasPiDetails rasPiDetails = new RasPiDetails();
		ComData comData = new ComData();
		comData.setDate(uartEventTime.getTime());
		comData.setText(message);
		ComData[] comDatas = new ComData[]{comData};
		if(null!=gpioState){
			rasPiDetails.setGpioStatus(gpioState);
		}
		rasPiDetails.setComData(comDatas);
		try {
			SystemInfoUtils.getSystemInfo(rasPiDetails);
			logger.info("RasPiDetails "+rasPiDetails.toString());
		} catch (NumberFormatException e) {
			logger.error(e.getStackTrace());
		} catch (UnsupportedOperationException e) {
			logger.error(e.getStackTrace());
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		} catch (InterruptedException e) {
			logger.error(e.getStackTrace());
		}
		
		if(rasPiDetails.getId() != 0){
			//Removing httpClient for socket client
			/*
			try {
				HttpClient client = new HttpClient();
				client.sendPost(rasPiDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			SocketClient socketClient = new SocketClient();
			try {
				socketClient.socketSend(rasPiDetails);
				
				//Now moved to socketWatcher 
				/*PostFile postFile = new PostFile();
				postFile.runPost();*/
			} catch (IOException e) {
				logger.error(e.getStackTrace());
				logger.error("socket error "+e.getMessage());
				FilesManager filesManager = new FilesManager();
				filesManager.fileSave(rasPiDetails);
			}catch (Exception e) {
				logger.error("Sending message...");
			}
		}

	}
	
}
