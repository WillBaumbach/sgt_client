package com.willjs.sgt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Ship {

	private Texture texture = new Texture(Gdx.files.internal("spaceship.png"));
	private Sprite _spaceship = new Sprite(texture);
	
	public void Ship()
	{
		_spaceship.setOriginCenter();
	}
	
	public Sprite getSprite()
	{
		return _spaceship;
	}
	
}
