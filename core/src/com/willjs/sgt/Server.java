package com.willjs.sgt;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

// Manages communication with the server
@WebSocket(maxTextMessageSize = 64 * 1024)
public class Server {
	
	// Instance Variables
	
	private WebSocketClient _client;
	private ClientUpgradeRequest _request;
	private Session _session;
	
	// Methods
	
	// Instantiates a connection to game server
	public Server(String address){
		_client = new WebSocketClient();
		_request = new ClientUpgradeRequest();
		
		try {
			URI url = new URI(address);
			
			_client.start();
			_client.connect(this, url, _request);
		}catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	
	// server closed connection
	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.printf("Connection closed: %d - %s%n",statusCode,reason);
		_session = null;
		
		try {
			_client.stop();
		}catch (Throwable t){
			t.printStackTrace();
		}
	}

	// server connected to
	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n",session);
		_session = session;
	}

	// message received
	@OnWebSocketMessage
	public void onMessage(String msg){
	    System.out.printf("Got msg: %s%n",msg);
	    
	    if(msg.equals("IDENT")){
	    	send("AUTH 1");
	    }
	}
	
	// Sends a message to client
	public void send(String msg){
		try {
			_session.getRemote().sendString(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
