package com.network;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.bean.RasPiDetails;
import com.google.gson.Gson;
import com.manager.FilesManager;

public class HttpClient {
	RasPiDetails rasPiDetails;
	Logger logger = Logger.getLogger(HttpClient.class);
	
		public void sendPost(RasPiDetails rasPiDetails) throws Exception {
			String url = "http://192.168.1.3:8080/rest/raspi/device";
			this.rasPiDetails = rasPiDetails;
			String jsonToSave = new Gson().toJson(this.rasPiDetails);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10 * 1000).setCircularRedirectsAllowed(false).build();
			org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
					.setRetryHandler(httpRequestRetryHandler).build();  
			
			try {
			    HttpPost request = new HttpPost(url);
			    StringEntity params =new StringEntity(jsonToSave);
			    request.addHeader("content-type", "application/json");
			    request.setEntity(params);
			    HttpResponse response = httpClient.execute(request);
			    int resultCode = response.getStatusLine().getStatusCode();
			    if(resultCode == HttpStatus.SC_OK){
			    	PostFile postFile = new PostFile();
			    	postFile.runPost();
			    }
			}catch (Exception ex) {
				logger.error("Exception occured while posting data..."+ex.getMessage());
			} finally {
			  
			}

		}
		
		private HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int exceptionCount, HttpContext context) {
				logger.error("Host unreachable saving file ");
				FilesManager filesManager = new FilesManager();
				filesManager.fileSave(rasPiDetails);
				return false;
			}
		}; 

}
