package com.license;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.bean.RasPiDetails;
import com.pundlik.PropertiesLoader;
import com.utils.SystemInfoUtils;

public class LicenseValidator {
	private static Logger logger = Logger.getLogger(LicenseValidator.class);
	
	private static byte[] getFileBytes(String filePath) throws IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream objectInputStream = new ObjectInputStream(fis);
		byte[] bytes = (byte[]) objectInputStream.readObject();
		fis.close();
		return bytes;
	}
	
	private static String getDeviceId(String filePath) throws ClassNotFoundException, IOException {
		String key = "Bar12345Bar12345"; // 128 bit key
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] dataBytes = getFileBytes(filePath);
			return new String(cipher.doFinal(dataBytes));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getStackTrace());
		} catch (NoSuchPaddingException e) {
			logger.error(e.getStackTrace());
		} catch (InvalidKeyException e) {
			logger.error(e.getStackTrace());
		} catch (IllegalBlockSizeException e) {
			logger.error(e.getStackTrace());
		} catch (BadPaddingException e) {
			logger.error(e.getStackTrace());
		}
		return null;
	}
	
	public static boolean isLicenseValid(){
		boolean isValid = false;
		RasPiDetails rasPiDetails = new RasPiDetails();
		String systemSerialNumber = null;
		try {
			SystemInfoUtils.getSystemInfo(rasPiDetails);
		} catch (NumberFormatException e1) {
			logger.error(e1.getStackTrace());
		} catch (UnsupportedOperationException e1) {
			logger.error(e1.getStackTrace());
		} catch (IOException e1) {
			logger.error(e1.getStackTrace());
		} catch (InterruptedException e1) {
			logger.error(e1.getStackTrace());
		}
		
		try {
			systemSerialNumber = rasPiDetails.getName();
			String deviceIdFromLicense = getDeviceId("./servercom.lic");
			isValid = systemSerialNumber.toLowerCase().equals(deviceIdFromLicense.toLowerCase());
		} catch (ClassNotFoundException e) {
			logger.error(e.getStackTrace());
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}catch (NullPointerException  e) {
			logger.error(e.getStackTrace());
		}
		return isValid;
	}
}
