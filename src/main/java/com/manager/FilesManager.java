package com.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;

import com.bean.RasPiDetails;
import com.network.HttpClient;

public class FilesManager {
	Logger logger = Logger.getLogger(FilesManager.class);
	public void fileSave(RasPiDetails rasPiDetails){
		File file = new File (new Date().getTime()+".dat");
		logger.info("Saving data to file.........."+file.getAbsolutePath()+"#####"+rasPiDetails);
		FileOutputStream fStream = null;
		ObjectOutputStream oStream = null;
		try {
			if(!file.exists()){
				file.createNewFile();
			}	
				fStream = new FileOutputStream(file);
				oStream = new ObjectOutputStream(fStream);
				oStream.writeObject(rasPiDetails);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(fStream!=null)fStream.close();
				if(oStream!=null)oStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}				
		}
	}
	
	public RasPiDetails getFileData(String fileName){
		RasPiDetails rasPiDetails = null;
		String fileData = null;
		FileInputStream fStream = null;
		ObjectInputStream iStream = null;
		try {
			File file = new File(fileName);
			if(file.exists()){
				fStream = new FileInputStream(file);
				iStream = new ObjectInputStream(fStream);
				rasPiDetails = (RasPiDetails) iStream.readObject();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fStream!=null)fStream.close();
				if(iStream!=null)iStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}				
		}
		return rasPiDetails;
	}
}
