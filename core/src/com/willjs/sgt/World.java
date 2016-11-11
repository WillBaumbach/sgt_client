package com.willjs.sgt;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class World implements MessageListener {
	
	private long _posx = 0;
	private long _posy = 0;
	private long _sectorx = 0;
	private long _sectory = 0;
	private Server _server;
	private float _velocityx;
	private float _velocityy;
	private float _acelerationx;
	private float _acelerationy;
	private float _mass;
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
	
	WorldRender getRenderer(){
		return render;
	}
	
	void processInput(){
		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			render.setZoomWidth((long)((float)render.getZoomWidth()*0.97f));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			render.setZoomWidth((long)((float)render.getZoomWidth()*1.03f));
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			_acelerationy = 1e12f;
		}else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			_acelerationy = -1e12f;
		}else{
			_acelerationy = 0;
		}
		
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			_acelerationx = -1e12f;
		}else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			_acelerationx = 1e12f;
		}else{
			_acelerationx = 0.0f;
		}
	}


	// deltaTime passed since last call
	public void update(float deltaTime) {
		_velocityx = _velocityx + deltaTime * _acelerationx;
		_velocityy = _velocityy + deltaTime * _acelerationy;
		
		_posx = _posx + (long)(_velocityx*deltaTime);
		_posy = _posy + (long)(_velocityy*deltaTime);
		render.setCenterPosistion(_posx, _posy);
	}
	
	
	
	
}
