package com.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.bean.RasPiDetails;
import com.pi4j.util.Console;
import com.pundlik.PropertiesLoader;

public class SocketClient {
	Logger logger = Logger.getLogger(SocketClient.class);
	
	public synchronized void socketSend(RasPiDetails rasPiDetails) throws UnknownHostException, IOException{
		Socket clientSocket = new Socket();
		String socketServerIp = PropertiesLoader.getPropertyAsString("servercom.socketServer").trim();
		int socketServerPort = PropertiesLoader.getPropertyAsInteger("servercom.socketPort");
		int socketServerTimeout = PropertiesLoader.getPropertyAsInteger("servercom.socketTimeout");
		Console console = new Console();
		console.box(socketServerIp+":"+socketServerPort+"###"+socketServerTimeout);
		InetAddress inetAddress = InetAddress.getByName(socketServerIp);
		clientSocket.connect(new InetSocketAddress(inetAddress,socketServerPort),socketServerTimeout); 
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		out.println(rasPiDetails.getComData()[0].getDate()+"|"+rasPiDetails.getComData()[0].getText());
		out.close();
		clientSocket.close();
	}
}
