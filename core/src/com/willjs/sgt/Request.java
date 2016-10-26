package com.willjs.sgt;

import org.json.JSONObject;

// Container for requests from server
public class Request {

	private String _reqtext;
	private int _respid;
	private String _msg;
	private Server _server;
	
	public Request(Server s, String reqtext, int respid, String msg){
		_reqtext = reqtext;
		_respid = respid;
		_msg = msg;
		_server = s;
	}
	
	public String getMessage(){
		return _msg;
	}
	
	public JSONObject getJSONMessage(){
		return new JSONObject(getMessage());
	}
	
	public String getRequest(){
		return _reqtext;
	}
	
	// responds to server with new request
	public void reply(String req, String msg){
		_server.sendReply(req, _respid, msg);
	}
	
}
