package com.willjs.sgt;

public class World implements MessageListener {
	
	private long _posx = 0;
	private long _posy = 0;
	private long _sectorx = 0;
	private long _sectory = 0;
	private Server _server;
	
	
	public World(Server server){
		_server = server;
		_server.addMessageListener("YOURPOS", this);
		
		
		_server.send("WHEREAMI?", "");
	}


	@Override
	public void onMessage(Request r) {
		if(r.getRequest().equals("YOURPOS")){
			System.out.println("MSG");
		}
	}
	
	
	
}
