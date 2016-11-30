package com.willjs.sgt;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRender 
{
	private BitmapFont font = new BitmapFont();
	private Ship _spaceship = new Ship();
	
	private ArrayList<CelestialBody> _cbArrayD = new ArrayList<CelestialBody>();
	private ArrayList<CelestialBody> _cbArrayN = new ArrayList<CelestialBody>();
	private ArrayList<CelestialBody> _cbAll = new ArrayList<CelestialBody>();
	
	private HashMap<String, Entity> _entities = new HashMap<String, Entity>();
	
	private OrthographicCamera _cam;
	private float _windowHeight, _windowWidth;
	private long _zoomWidth;
	
	
	public Ship getSpaceship()
	{
		return _spaceship;
	}
	
	public WorldRender(){
		setZoomWidth((long)1e12);
	}
	
	
	public void processWorldData(Request r)
	{
		if(r.getRequest().equals("DISTANT") || r.getRequest().equals("NEARBY")){
			ArrayList<CelestialBody> container = _cbArrayD;
			if(r.getRequest().equals("NEARBY")){ // we only want to clear nearby planets here
				container = _cbArrayN;
			}
			
			container.clear();
			JSONArray coords = r.getJSONMessage().getJSONArray("cbs");
			for(Object obj : coords)
			{
				JSONObject cb = (JSONObject)obj;
				boolean star = cb.getString("type").equals("star");
				CelestialBody body = new CelestialBody(cb.getLong("x"), cb.getLong("y"), star);
				
				container.add(body);
			}
			
			ArrayList<CelestialBody> cbAllNew = new ArrayList<CelestialBody>();
			for(CelestialBody x : _cbArrayD){
				cbAllNew.add(x);
			}for(CelestialBody x : _cbArrayN){
				cbAllNew.add(x);
			}
			_cbAll = cbAllNew;
		}
	}
	
	synchronized public void processEntityWorldData(Request r)
	{
		if(r.getRequest().equals("NEARBYENT")){
			Set<String> oldEnts = new HashSet<String>(_entities.keySet());
			
			JSONArray coords = r.getJSONMessage().getJSONArray("ent");
			for(Object obj : coords)
			{
				JSONObject e = (JSONObject)obj;
				String eid = e.getString("id");
				
				oldEnts.remove(eid);
				if(_entities.containsKey(eid)){
					_entities.get(eid).update(e);
				}else{
					Entity ent = new Entity(e);
					_entities.put(eid, ent);
					System.out.println("Added Entity");
				}
			}
			for(String oid : oldEnts){
				System.out.println("Removed Entity");
				_entities.remove(oid);
			}
		}
	}
	
	public ArrayList<CelestialBody> getCBArray()
	{
		return _cbAll;
	}
	
	
	
	// moves camera focused at position x
	public void setCenterPosistion(long x, long y){
		_cam.position.set(x, y, 0);
	}
	
	// sets zoom level (width units?)
	public void setZoomWidth(long w){
		_zoomWidth = w;
		resize();
		
		for(CelestialBody cb : _cbAll){
			cb.setScale(_zoomWidth);
		}
		float zoomSize = 0.015f * _zoomWidth;
		float actualSize = 0.5e10f;
		float size = Math.max(zoomSize, actualSize);
		_spaceship.getSprite().setSize(size,size);
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
	
	public void postRender(SpriteBatch batch){
		/*
		NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat("0.#####E0");
		System.out.println(formatter.format(_zoomWidth));
	    String zoom = formatter.format(_zoomWidth);
	    font.draw(batch, zoom, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    font.setColor(1, 1, 1, 1);
	    */
	}
	
	synchronized public void render(SpriteBatch batch){
		for(CelestialBody cb : _cbAll){
			Sprite s = cb.getSprite();
			if(s == null){
				cb.createSprite();
				s = cb.getSprite();
			}
			s.draw(batch);
		}
		
		for(Entity e : _entities.values()){
			Sprite s = e.getSprite();
			if(s == null){
				e.createSprite();
				s = e.getSprite();
			}
			s.draw(batch);
			System.out.println("Rendering Entity");
		}
		
		//_spaceship.setSize((float)(_zoomWidth * .015) , (float)(_zoomWidth * .015));
		_spaceship.getSprite().setPosition(_cam.position.x,_cam.position.y);
		//spaceship.setRotation((float) Math.atan(world.getVelocityy()/world.getVelocityx()));
		_spaceship.getSprite().draw(batch);
	}
}
