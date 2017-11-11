package com.utils;

import java.io.IOException;
import java.util.Date;

import com.manager.ConfigurationManager;
import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import com.pi4j.util.Console;

public class SerialPortUtil extends Thread{
	static SerialPortUtil serialPortUtil;
	static Thread notifier;
	SerialConfig config = new SerialConfig();
	final Console console = new Console();
	final Serial serial = SerialFactory.createInstance();
	long startTime = new Date().getTime(); 	
	private SerialPortUtil() {
		 /*config.device("/dev/ttyAMA0")
         .baud(Baud._9600)
         .dataBits(DataBits._8)
         .parity(Parity.NONE)
         .stopBits(StopBits._1)
         .flowControl(FlowControl.NONE);*/
		
		try {
			config = ConfigurationManager.getSerialPortConfiguration();
		} catch (UnsupportedBoardType e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		 
		 console.box("Connecting to "+config.toString());

		 serial.addListener(new SerialDataEventListener() {
	            public void dataReceived(SerialDataEvent event) {

	                // NOTE! - It is extremely important to read the data received from the
	                // serial port.  If it does not get read from the receive buffer, the
	                // buffer will continue to grow and consume memory.

	                // print out the data received to the console
	                try {
	                    console.println("[HEX DATA]   " + event.getHexByteString());
	                    console.println("[ASCII DATA] " + event.getAsciiString());
	                    //serialPortUtil.notify();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
		 
		 try {
			serial.open(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void register(Thread notifierHere){
		notifier = notifierHere;
	}
	
	public static SerialPortUtil getInstance(){
		if(null==serialPortUtil){
			serialPortUtil = new SerialPortUtil();
		}
		return serialPortUtil;
	}
	
	@Override
	public void run() {
		while(true){
			long currentTime = new Date().getTime();
			long elapsedTime = currentTime-startTime;
        	console.box("Running since "+(elapsedTime/(1000*60))+ " min.");
            
		 try {
			serial.write("CURRENT TIME: " + new Date().toString());
			Thread.sleep(5*1000);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
}
