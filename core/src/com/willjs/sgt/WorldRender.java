package com.willjs.sgt;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRender 
{
	private ArrayList<CelestialBody> _cbArray = new ArrayList<CelestialBody>();
	private OrthographicCamera _cam;
	private float _windowHeight, _windowWidth;
	private long _zoomWidth;
	
	public WorldRender(){
		setZoomWidth((long)1e12);
	}
	
	
	public void processWorldData(Request r)
	{
		if(r.getRequest().equals("DISTANT")){
			_cbArray.clear();
			JSONArray coords = r.getJSONMessage().getJSONArray("cbs");
			for(Object obj : coords)
			{
				JSONObject cb = (JSONObject)obj;
				boolean star = cb.getString("type").equals("star");
				CelestialBody body = new CelestialBody(cb.getLong("x"), cb.getLong("y"), star);
				
				_cbArray.add(body);
			}
		}
	}	
	
	public ArrayList<CelestialBody> getCBArray()
	{
		return _cbArray;
	}
	
	
	
	// moves camera focused at position x
	public void setCenterPosistion(long x, long y){
		_cam.position.set(x, y, 0);
	}
	
	// sets zoom level (width units?)
	public void setZoomWidth(long w){
		_zoomWidth = w;
		resize();
		
		for(CelestialBody cb : _cbArray){
			cb.setScale(_zoomWidth);
		}
	}
	
	public long getZoomWidth(){
		return _zoomWidth;
	}
	
	public void resize(){
		_windowHeight = Gdx.graphics.getHeight();
		_windowWidth = Gdx.graphics.getWidth();
		
		float x=0,y=0;
		if(_cam != null){
			x = _cam.position.x;
			y = _cam.position.y;
		}
		
		_cam = new OrthographicCamera(_zoomWidth,_zoomWidth * (_windowHeight/_windowWidth));
		_cam.position.set(x, y, 0);
	}
	
	public void preRender(SpriteBatch batch){
		_cam.update();
		batch.setProjectionMatrix(_cam.combined);
	}
	
	public void postRender(){
		
	}
	
	public void render(SpriteBatch batch){
		for(CelestialBody cb : _cbArray){
			Sprite s = cb.getSprite();
			if(s == null){
				cb.createSprite();
				s = cb.getSprite();
			}
			s.draw(batch);
		}
	}
}
