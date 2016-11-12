package com.willjs.sgt;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class CelestialBody 
{
	private long _xcoord;
	private long _ycoord;
	private long _type;
	private Sprite _sprite;
	private boolean _star;
	
	public CelestialBody(long x, long y, boolean star)
	{
		_star = star;
		_xcoord = x;
		_ycoord = y;
	}
	
	public long getXCoord()
	{
		return _xcoord;
	}
	
	public long getYCoord()
	{
		return _ycoord;
	}
	
	public long getType()
	{
		return _type;
	}

	void setScale(long zoomwidth){
		if(_sprite == null){
			return;
		}
		
		if(_star){
			float zoomSize = 0.005f * zoomwidth;
			float actualSize = 0.5e11f;
			
			float size = Math.max(zoomSize, actualSize);
			_sprite.setSize(size, size);
		}else{
			if(zoomwidth < (long)1e13){
				float zoomSize = 0.005f * zoomwidth;
				float actualSize = 0.5e10f;
				
				float size = Math.max(zoomSize, actualSize);
				_sprite.setSize(size, size);
			}
		}
	}
	
	// generates the celestial body sprite
	public void createSprite(){
		Texture texture = null;
		
		if(_star){
			texture = new Texture(Gdx.files.internal("star.png"));
		}else{
			texture = new Texture(Gdx.files.internal("Planet Test.png"));
		}
		
		_sprite = new Sprite(texture);
		_sprite.setSize(0.5e10f,0.5e10f);
		//Random rand = new Random();
		_sprite.setPosition(getXCoord(),getYCoord());
	}



	public Sprite getSprite(){
		return _sprite;
	}
	
}
