package com.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.pundlik.PropertiesLoader;

public class SocketWatcher implements Runnable{
	Logger logger = Logger.getLogger(SocketWatcher.class);
	
	private void watchSocket(){
		while (true) {
			 try {
				 String socketServerIp = PropertiesLoader.getPropertyAsString("servercom.socketServer").trim();
				int socketServerPort = PropertiesLoader.getPropertyAsInteger("servercom.socketPort");
				int socketServerTimeout = PropertiesLoader.getPropertyAsInteger("servercom.socketTimeout");
				
				 Socket clientSocket = new Socket();
				 InetAddress inetAddress = InetAddress.getByName(socketServerIp);
				clientSocket.connect(new InetSocketAddress(inetAddress,socketServerPort),socketServerTimeout);
				clientSocket.close();
				PostFile postFile = new PostFile();
				postFile.runPost();
				Thread.sleep(60*1000);
			} catch (UnknownHostException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			 } catch (InterruptedException e) {
				 logger.error(e.getMessage());
			} catch(Exception e){
				logger.error(e.getMessage());
			}
			 }
	}

	@Override
	public void run() {
		watchSocket();
	}
}
