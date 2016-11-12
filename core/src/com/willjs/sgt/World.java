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
	
	private long _lastNearbyRequestx = 0;
	private long _lastNearbyRequesty = 0;
	
	private final long NEARBY_DISTANCE = (long)1e13;
	private final long NEARBY_ZOOM = (long)1e13;
	
	
	public World(Server server){
		_server = server;
		_server.addMessageListener("YOURPOS", this);
		_server.addMessageListener("NEARBY", this);
		_server.addMessageListener("DISTANT", this);
		
		
		_server.send("WHEREAMI?", "");
		
	}


	@Override
	public void onMessage(Request r) {
		if(r.getRequest().equals("YOURPOS")){ // we got the player's position
			JSONObject coords = r.getJSONMessage();
			_posx = coords.getLong("x");
			_posy = coords.getLong("y");
			_sectorx = coords.getLong("sectorx");
			_sectory = coords.getLong("sectory");
			
			_server.send("DISTANT?", "");
			_server.send("NEARBY?", Long.toString(NEARBY_ZOOM));
		}else if(r.getRequest().equals("NEARBY")){
			_lastNearbyRequestx = _posx;
			_lastNearbyRequesty = _posy;
			render.processWorldData(r);
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
		
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			_velocityx = (float) (_velocityx * 0.95);
			_velocityy = (float) (_velocityy * 0.95);
		}
	}


	// deltaTime passed since last call
	public void update(float deltaTime) {
		
		// Checking if your zoom is "close" or "far" to determine velocity
		if (render.getZoomWidth() < 1e12)
		{
			// Setting x veloicty at a close distance based on acceleration
			if (_acelerationx >= 0){
				_velocityx = (float) Math.min(_velocityx + deltaTime * _acelerationx, 5e10);
			} else {
				_velocityx = (float) Math.max(_velocityx + deltaTime * _acelerationx, -5e10);
			}
			// Setting y velocity at a close distance based on acceleration
			if(_acelerationy >= 0)
			{
				_velocityy = (float) Math.min(_velocityy + deltaTime * _acelerationy, 5e10);
			} else {
				_velocityy = (float) Math.max(_velocityy + deltaTime * _acelerationy, -5e10);
			}
		} else {
			// Setting x velocity at a far distance based on acceleration
			if (_acelerationx >= 0){
				_velocityx = (float) Math.min(_velocityx + deltaTime * _acelerationx, 1e14);
			} else {
				_velocityx = (float) Math.max(_velocityx + deltaTime * _acelerationx, -1e14);
			}
			// Setting y velocity at a far distance based on acceleration
			if(_acelerationy >= 0)
			{
				_velocityy = (float) Math.min(_velocityy + deltaTime * _acelerationy, 1e14);
			} else {
				_velocityy = (float) Math.max(_velocityy + deltaTime * _acelerationy, -1e14);
			}
		}
		_posx = _posx + (long)(_velocityx*deltaTime);
		_posy = _posy + (long)(_velocityy*deltaTime);
		render.setCenterPosistion(_posx, _posy);
		
		if(Math.abs(_posx - _lastNearbyRequestx) > NEARBY_DISTANCE || 
				Math.abs(_posy - _lastNearbyRequesty) > NEARBY_DISTANCE){
			if(render.getZoomWidth() < NEARBY_ZOOM){
				_server.send("NEARBY?", Long.toString(NEARBY_ZOOM));
				
				_lastNearbyRequestx = _posx;
				_lastNearbyRequesty = _posy;
			}
		}
	}
	
	
	
	
}
