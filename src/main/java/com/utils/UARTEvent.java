package com.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.manager.ConfigurationManager;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
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
import com.pundlik.NotifierThread;

public class UARTEvent{
	
	Logger logger = Logger.getLogger(UARTEvent.class);
	public String message;
	public Date uartEventTime = null;
	public Date gpioEventTime = null;
	public HashMap<String, Boolean> gpioState;
	
	UARTEvent uartEvent;
	
	Console console = new Console();
	
	public UARTEvent() {
		uartEvent = this;
		initializeEvents();
	}
	
	private void initializeEvents(){
		initialiseUartEvents();		
	}
	
	private void initialiseUartEvents(){
		SerialConfig config = null;
		try {
			config = ConfigurationManager.getSerialPortConfiguration();
		} catch (UnsupportedBoardType e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final Serial serial = SerialFactory.createInstance();
		console.box("Starting conf server at port "+config.toString());
		StringBuffer cncMessage = new StringBuffer();
		serial.addListener(new SerialDataEventListener() {
			@Override
			public void dataReceived(SerialDataEvent event) {
				try {
					String intermediateMessage = event.getAsciiString();
					logger.info("Port data received ###########"+intermediateMessage);
					
					cncMessage.append(intermediateMessage);
					String messageBuffer = cncMessage.toString().trim();
					logger.info("Formed message..."+messageBuffer);
					if(( messageBuffer.trim().startsWith("START")) && messageBuffer.trim().endsWith("END-")){
						cncMessage.setLength(0);
						message = messageBuffer;
						uartEventTime = new Date();
						synchronized (uartEvent) {
							uartEvent.notify();
						}
					}
					if(messageBuffer.trim().endsWith("END-")){
						cncMessage.setLength(0);
					}
					/*else if( intermediateMessage.trim().startsWith("START")){
						logger.info("Start received...");
						cncMessage.setLength(0);
						cncMessage.append(intermediateMessage);
					}else if(cncMessage.length()>0 && intermediateMessage.trim().endsWith("END-")){
						logger.info("End received...");
						cncMessage.append(intermediateMessage);
						message = cncMessage.toString();
						uartEventTime = new Date();
						cncMessage.setLength(0);
						synchronized (uartEvent) {
							uartEvent.notify();
						}
					}else if(cncMessage.length()>0){
						cncMessage.append(intermediateMessage);
					}else{
						logger.info("Packate droped..");
					}*/
										
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
}
