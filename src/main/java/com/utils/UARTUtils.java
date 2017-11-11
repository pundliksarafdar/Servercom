package com.utils;

public class UARTUtils extends Thread{
	public String message;
	int messageCount = 0;
	@Override
	public void run() {
		while(messageCount<6){
			try {
				sleep(2*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (this) {
				message = "Hi this is message"+(messageCount++);
				System.out.println("Notifing.............");
				notify();
			}
		}
	}
}
