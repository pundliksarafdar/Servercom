package com.pundlik;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.network.SocketClient;

public class PropertiesLoader {
	private static Properties prop = null;
	private static Logger logger = Logger.getLogger(PropertiesLoader.class);
	
	public static void loadPropFile(){
		FileInputStream serverComProp;
		if(prop!=null)return;
		try {
			prop = new Properties();
			logger.info("Loading properties file....");
			serverComProp = new FileInputStream("./servercom.properties");
			prop.load(serverComProp);
			logger.info(prop);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	public static String getPropertyAsString(String key){
		loadPropFile();
		return prop.getProperty(key);
	}
	
	public static Integer getPropertyAsInteger(String key){
		loadPropFile();
		return Integer.parseInt(prop.getProperty(key));
	}
	
	public static Long getPropertyAsLong(String key){
		loadPropFile();
		return Long.parseLong(prop.getProperty(key));
	}
	public static Properties getProp() {
		loadPropFile();
		return prop;
	}
	public static void setProp(Properties prop) {
		PropertiesLoader.prop = prop;
	}
	
	
}
