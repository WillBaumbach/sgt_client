package com.willjs.sgt;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

// Manages communication with the server
@WebSocket(maxTextMessageSize = 64 * 1024)
public class Server implements MessageListener {
	
	// Instance Variables
	
	private WebSocketClient _client;
	private ClientUpgradeRequest _request;
	private Session _session;
	private int _respid;
	private HashMap<String, MessageListener> _listeners;
	private HashMap<Integer, MessageListener> _resplisteners;
	private String _userid;
	private boolean _authenticated;
	
	// Methods
	
	// Instantiates a connection to game server
	public Server(String address, String userid){
		_client = new WebSocketClient();
		_request = new ClientUpgradeRequest();
		_respid = 0;
		_resplisteners = new HashMap<Integer, MessageListener>();
		_listeners = new HashMap<String, MessageListener>();
		_userid = userid;
		_authenticated = false;
		
		try {
			URI url = new URI(address);
			
			_client.start();
			_client.connect(this, url, _request);
		}catch (Throwable t){
			t.printStackTrace();
		}
		
		addMessageListener("IDENT", this);
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

	// message received of format <REQ> <RESPID> [MSG]
	@OnWebSocketMessage
	public void onMessage(String msg){
		if(msg.length() < 4 || msg.indexOf(" ") < 1){
			return;
		}
		String req = msg.substring(0, msg.indexOf(" "));
		msg = msg.substring(msg.indexOf(" ")+1);
		if(msg.length() < 1){
			return;
		}
		
		String srespid;
		if(msg.contains(" ")){
			srespid = msg.substring(0, msg.indexOf(" "));
			msg = msg.substring(msg.indexOf(" ")+1);
		}else{
			srespid = msg;
			msg = "";
		}
		
		int respid;
		try{
			respid = Integer.parseInt(srespid);
		}catch(NumberFormatException e){
			return;
		}

		processMessage(req, respid, msg);
	}
	
	private void processMessage(String req, int respid, String msg){
		System.out.printf("Got req: %s%n",req);
	    
		Request request = new Request(this, req, respid, msg);
		if(_resplisteners.containsKey(respid)){
			_resplisteners.get(respid).onMessage(request);
			_resplisteners.remove(respid);
		}else if(_listeners.containsKey(req)){
			_listeners.get(req).onMessage(request);
		}else{
			System.err.println("No Listener For Request");
		}
	}
	
	
	// registers a listener with a particular request
	public void addMessageListener(String req, MessageListener listener){
		_listeners.put(req, listener);
	}
	
	// registers a listener with a particular response id
	public void addMessageListener(int respid, MessageListener listener){
		_resplisteners.put(respid, listener);
	}

	
	// Sends a message to client
	public int send(String req, String msg){
		_respid ++;
		return sendReply(req, _respid, msg);
	}
	
	// Sends a message to client and registers response
	public int sendAwaitReply(String req, String msg, MessageListener listener){
		_respid ++;
		int respid = _respid;
		addMessageListener(respid, listener);
		sendReply(req, respid, msg);
		return respid;
	}
	
	// Sends a message to client with given response id
	public int sendReply(String req, int respid, String msg){
		while(_session == null || _session.getRemote() == null){ // really bad, but...
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			if(msg.isEmpty()){
				_session.getRemote().sendString(req + " " + respid);
			}else{
				_session.getRemote().sendString(req + " " + respid + " " + msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respid;
	}
	
	public boolean isAuthenticated(){
		return _authenticated;
	}


	// message handler for authentication
	@Override
	public void onMessage(Request r) {
		r.reply("AUTH", _userid);
		_authenticated = true;
	}


}
