package com.license;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.bean.RasPiDetails;
import com.pundlik.NotifierThread;
import com.utils.SystemInfoUtils;

public class LicenseRequestGenerator {
	static Logger logger = Logger.getLogger(LicenseRequestGenerator.class);
	public static File generateLicRequestFile() throws IOException{
		File file = new File("./licenseKeyGen.key"); 
		if(file.exists()){
			file.mkdir();
		}
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		RasPiDetails rasPiDetails = new RasPiDetails();
		try {
			SystemInfoUtils.getSystemInfo(rasPiDetails);
		} catch (NumberFormatException e) {
			logger.error(e.getStackTrace());
		} catch (UnsupportedOperationException e) {
			logger.error(e.getStackTrace());
		} catch (InterruptedException e) {
			logger.error(e.getStackTrace());
		}
		oos.writeChars(rasPiDetails.getName().trim());
		oos.close();
		fos.close();
		return file;
	}
	
}
