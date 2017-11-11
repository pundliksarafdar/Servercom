package com.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

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

public class UARTEvent{
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
		initialiseGPIOEvents();
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
		/*config.device("/dev/ttyAMA0")
        .baud(Baud._9600)
        .dataBits(DataBits._8)
        .parity(Parity.NONE)
        .stopBits(StopBits._1)
        .flowControl(FlowControl.NONE);
		*/ 
		console.box("Starting conf server at port "+config.toString());
		
		serial.addListener(new SerialDataEventListener() {
			@Override
			public void dataReceived(SerialDataEvent event) {
				try {
					message = event.getAsciiString();
					System.out.println("Port data received ###########"+message);
					uartEventTime = new Date();
					synchronized (uartEvent) {
						uartEvent.notify();
					}					
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
	
	private void initialiseGPIOEvents(){
		final GpioController gpio = GpioFactory.getInstance();
		gpioState = new HashMap<String, Boolean>();
		GpioPinListenerDigital gpioLisnter = new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                gpioState.put(event.getPin().getName(), event.getState().equals(PinState.HIGH));
                gpioEventTime = new Date();
                synchronized (uartEvent) {
					uartEvent.notify();
				}
            }
        };

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        // set shutdown state for this input pin
        myButton2.setShutdownOptions(true);
        // create and register gpio pin listener
        myButton2.addListener(gpioLisnter);
        
        final GpioPinDigitalInput myButton1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        myButton1.setShutdownOptions(true);
        myButton1.addListener(gpioLisnter);        
	}
}
