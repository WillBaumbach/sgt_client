package com.willjs.sgt;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class WorldRender 
{
	private ArrayList<CelestialBody> _cbArray;
	private ArrayList<Sprite> _spriteArray;
	private long centerx;
	private long centery;
	
	public void processWorldData(Request r)
	{
		if(r.getRequest().equals("DISTANT")){
			JSONArray coords = r.getJSONMessage().getJSONArray("cbs");
			for(Object obj : coords)
			{
				_cbArray = new ArrayList<CelestialBody>();
				_spriteArray = new ArrayList<Sprite>();
				JSONObject cb = (JSONObject)obj;
				CelestialBody body = new CelestialBody();
				Sprite sprite = new Sprite();
				body.setXCoord(cb.getLong("x"));
				_cbArray.add(body);
				
				sprite.setPosition(body.getXCoord(), 0);
				_spriteArray.add(sprite);
				
				
				
				System.out.println(body.getXCoord());
			}
		}
	}	
	
	public ArrayList<CelestialBody> getCBArray()
	{
		return _cbArray;
	}
	
	public void createSprites()
	{
		
	}
}
