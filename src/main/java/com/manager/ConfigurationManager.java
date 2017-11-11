package com.manager;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.network.HttpClient;
import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialPort;
import com.pi4j.io.serial.StopBits;
import com.pi4j.util.Console;
import com.pundlik.PropertiesLoader;

public class ConfigurationManager {
	Logger logger = Logger.getLogger(ConfigurationManager.class);
	
	public static SerialConfig getSerialPortConfiguration() throws UnsupportedBoardType, IOException, InterruptedException{
		System.out.println("Geting server config.....");
		SerialConfig serialConfig = new SerialConfig();
		ConfigurationManager manager = new ConfigurationManager();
		manager.logger.info("initializing.....");
		//Load uart file 
		String serialPortFile = PropertiesLoader.getPropertyAsString("servercom.comportFile")!=null && PropertiesLoader.getPropertyAsString("servercom.comportFile").trim().length()!=0?
				PropertiesLoader.getPropertyAsString("servercom.comportFile"):SerialPort.getDefaultPort();
		//
		int  baudRate = PropertiesLoader.getPropertyAsInteger("servercom.baudrate");
		int dataBits = PropertiesLoader.getPropertyAsInteger("servercom.databits");
		String parity = PropertiesLoader.getPropertyAsString("servercom.parity");
		String flowControl = PropertiesLoader.getPropertyAsString("servercom.flowControl");
		int stopBit = PropertiesLoader.getPropertyAsInteger("servercom.stopBits");
		manager.logger.info("baudRate "+baudRate);
		/*config.device("/dev/ttyAMA0")
        .baud(Baud._9600)
        .dataBits(DataBits._8)
        .parity(Parity.NONE)
        .stopBits(StopBits._1)
        .flowControl(FlowControl.NONE);*/
		
		serialConfig.device(serialPortFile)
			.baud(getBaudRate(baudRate))
			.dataBits(getDataBits(dataBits))
			.parity(getParity(parity))
			.stopBits(getStopBits(stopBit))
			.flowControl(getFlowControl(flowControl));
		
		Console console = new Console();
		console.box(serialConfig.toString());
		return serialConfig;
	}
	
	private static StopBits getStopBits(int stopBits){
		StopBits stopBitsEnum;
		switch (stopBits) {
		case 1:
			stopBitsEnum = StopBits._1;
			break;
		case 2:
			stopBitsEnum = StopBits._2;
			break;
		default:
			stopBitsEnum = StopBits._1;
			break;
		}
		return stopBitsEnum;
	}
	
	private static FlowControl getFlowControl(String flowControl){
		FlowControl flowControlEnum;
		switch (flowControl.trim().toLowerCase()) {
		case "none":
			flowControlEnum = FlowControl.NONE;
			break;
		case "hardware":
			flowControlEnum = FlowControl.HARDWARE;
			break;
		case "software":
			flowControlEnum = FlowControl.SOFTWARE;
			break;
		default:
			flowControlEnum = FlowControl.NONE;
			break;
		}
		
		return flowControlEnum;
	}
	private static Parity getParity(String parity){
		Parity parityEnum;
		switch(parity.trim().toLowerCase()){
		case "even":
			parityEnum = Parity.EVEN;
			break;
		case "mark":
			parityEnum = Parity.MARK;
			break;
		case "none":
			parityEnum = Parity.NONE;
			break;
		case "odd":
			parityEnum = Parity.ODD;
			break;
		case "space":
			parityEnum = Parity.SPACE;
			break;
		default:
			parityEnum = Parity.NONE;
		}
		return parityEnum;
	}
	
	private static DataBits getDataBits(int dataBits){
		DataBits dataBitsEnum;
		switch (dataBits) {
		case 5:
			dataBitsEnum = DataBits._5;
			break;
		case 6:
			dataBitsEnum = DataBits._6;
			break;
		case 7:
			dataBitsEnum = DataBits._7;
			break;
		case 8:
			dataBitsEnum = DataBits._8;
			break;
		default:
			dataBitsEnum = DataBits._8;
			break;
		}
		return dataBitsEnum;
	}
	
	private static Baud getBaudRate(int baudRate){
		Baud baudRateEnum = Baud._9600;
		switch (baudRate) {
		case 50:
			baudRateEnum = Baud._50;
			break;
		case 75:
			baudRateEnum = Baud._75;
			break;
		case 110:
			baudRateEnum = Baud._110;
			break;
		case 134:
			baudRateEnum = Baud._134;
			break;
		case 150:
			baudRateEnum = Baud._150;
			break;
		case 200:
			baudRateEnum = Baud._200;
			break;
		case 300:
			baudRateEnum = Baud._300;
			break;
		case 600:
			baudRateEnum = Baud._600;
			break;
		case 1200:
			baudRateEnum = Baud._1200;
			break;
		case 1800:
			baudRateEnum = Baud._1800;
			break;
		case 2400:
			baudRateEnum = Baud._2400;
			break;
		case 4800:
			baudRateEnum = Baud._4800;
			break;
		case 9600:
			baudRateEnum = Baud._9600;
			break;
		case 19200:
			baudRateEnum = Baud._19200;
			break;
		case 38400:
			baudRateEnum = Baud._38400;
			break;
		case 57600:
			baudRateEnum = Baud._57600;
			break;
		case 115200:
			baudRateEnum = Baud._115200;
			break;
		case 230400:
			baudRateEnum = Baud._230400;
			break;
		default:
			baudRateEnum = Baud._9600;
			break;
		}
		return baudRateEnum;
	}
}
