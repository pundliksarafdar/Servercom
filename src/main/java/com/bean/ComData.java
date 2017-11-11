package com.bean;

import java.io.Serializable;

public class ComData  implements Serializable{
	private long date;
	private String text;
	
	public long getDate() {
		return date;
	}
	public String getText() {
		return text;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "ComData [date=" + date + ", text=" + text + "]";
	}
	
	
}
