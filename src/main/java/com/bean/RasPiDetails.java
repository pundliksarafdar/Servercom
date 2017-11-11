package com.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class RasPiDetails implements Serializable{
	private int id = 1;
	private String name;
	private String status = "online";
	private int temp;
	private int freq;
	private HashMap<String, Boolean> gpioStatus = new HashMap<String, Boolean>();
	private ComData[] comData;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getStatus() {
		return status;
	}
	public int getTemp() {
		return temp;
	}
	public int getFreq() {
		return freq;
	}
	public ComData[] getComData() {
		return comData;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setTemp(int temp) {
		this.temp = temp;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	public void setComData(ComData[] comData) {
		this.comData = comData;
	}
	public HashMap<String, Boolean> getGpioStatus() {
		return gpioStatus;
	}
	public void setGpioStatus(HashMap<String, Boolean> gpioStatus) {
		this.gpioStatus = gpioStatus;
	}
	@Override
	public String toString() {
		return "RasPiDetails [id=" + id + ", name=" + name + ", status=" + status + ", temp=" + temp + ", freq=" + freq
				+ ", gpioStatus=" + gpioStatus + ", comData=" + Arrays.toString(comData) + "]";
	}
	
	
}
