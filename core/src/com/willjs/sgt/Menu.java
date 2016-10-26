package com.willjs.sgt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Menu extends Table
{
	private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	private Table table = new Table(skin);
	
	public Menu(Skin skin)
	{
		super(skin);
	}
	
	public Table getTable()
	{
		return table;
	}
	
	public void  addButton(TextButton button)
	{
		this.getTable().add(button);
	}
}
