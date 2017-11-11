package com.license;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.bean.RasPiDetails;
import com.utils.SystemInfoUtils;

public class LicenseRequestGenerator {
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
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		oos.writeChars(rasPiDetails.getName().trim());
		oos.close();
		fos.close();
		return file;
	}
	
}
