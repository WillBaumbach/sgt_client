package com.willjs.sgt;

import org.json.JSONObject;

public class World implements MessageListener {
	
	private long _posx = 0;
	private long _posy = 0;
	private long _sectorx = 0;
	private long _sectory = 0;
	private Server _server;
	private WorldRender render = new WorldRender();
	
	
	public World(Server server){
		_server = server;
		_server.addMessageListener("YOURPOS", this);
		_server.addMessageListener("NEARBY", this);
		_server.addMessageListener("DISTANT", this);
		
		
		_server.send("WHEREAMI?", "");
		_server.send("DISTANT?", "");
	}


	@Override
	public void onMessage(Request r) {
		if(r.getRequest().equals("YOURPOS")){ // we got the player's position
			JSONObject coords = r.getJSONMessage();
			_posx = coords.getLong("x");
			_posy = coords.getLong("y");
			_sectorx = coords.getLong("sectorx");
			_sectory = coords.getLong("sectory");
		}else if(r.getRequest().equals("NEARBY")){
			
		}else if(r.getRequest().equals("DISTANT")){
			render.processWorldData(r);
		}
	}
	
	
	
	
}
