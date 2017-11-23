package com.utils;

import java.util.HashMap;



import org.apache.log4j.Logger;

import com.license.LicenseRequestGenerator;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class GPIOInfoUtils extends Thread{
	static Logger logger = Logger.getLogger(LicenseRequestGenerator.class);
	static GPIOInfoUtils gpioInfoUtils;
	static Thread notifier;
	private GPIOInfoUtils() {
		
	}
	
	public void register(Thread notifierHere){
		notifier = notifierHere;
	}
	public static GPIOInfoUtils getInstance(){
		if(null == gpioInfoUtils){
			gpioInfoUtils = new GPIOInfoUtils();
		}
		return gpioInfoUtils;
	}
	@Override
	public void run() {
		final GpioController gpio = GpioFactory.getInstance();

		GpioPinListenerDigital gpioLisnter = new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                HashMap<String, Boolean> gpioState = new HashMap<String, Boolean>();
                gpioState.put(event.getPin().getName(), event.getState().equals(PinState.HIGH));
                
            }

        };

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        // set shutdown state for this input pin
        myButton2.setShutdownOptions(true);
        // create and register gpio pin listener
        myButton2.addListener(gpioLisnter);
        
     // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        // set shutdown state for this input pin
        myButton1.setShutdownOptions(true);
        // create and register gpio pin listener
        myButton1.addListener(gpioLisnter);
        
        System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");
        try {
			while(true)this.sleep(1000);
		} catch (InterruptedException e) {
			logger.error(e.getStackTrace());
		}
	}
}
