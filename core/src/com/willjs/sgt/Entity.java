package com.willjs.sgt;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity {

	private String _type;
	private long _xcoord;
	private long _ycoord;
	
	private Sprite _sprite;
	
	public Entity(JSONObject e) {
		_type = e.getString("type");
		_xcoord = e.getLong("x");
		_ycoord = e.getLong("y");
		
	}
	
	// generates the celestial body sprite
	public void createSprite(){
		Texture texture = null;
		
		if(_type.equals("player")){
			texture = new Texture(Gdx.files.internal("spaceship.png"));
		}
		
		_sprite = new Sprite(texture);
		_sprite.setSize(0.5e13f,0.5e13f);
		_sprite.setPosition(_xcoord, _ycoord);
	}



	public Sprite getSprite(){
		return _sprite;
	}

	public void update(JSONObject e) {
		_type = e.getString("type");
		_xcoord = e.getLong("x");
		_ycoord = e.getLong("y");
		
		if(_sprite != null){
			_sprite.setPosition(_xcoord, _ycoord);
		}
	}

}
