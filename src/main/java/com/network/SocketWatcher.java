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
				logger.info("Is connected...."+clientSocket.isConnected());
				clientSocket.close();
				PostFile postFile = new PostFile();
				postFile.runPost();
				Thread.sleep(5*1000);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			 } catch (InterruptedException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
			 }
	}

	@Override
	public void run() {
		watchSocket();
	}
}
