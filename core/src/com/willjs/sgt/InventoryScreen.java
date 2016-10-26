package com.willjs.sgt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class InventoryScreen extends Window
{
	private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	private Button closeButton = new TextButton("x", skin);
	private String[] main = {"Ship", "Planet", "Units", "Settings"};
	private Menu mainMenu = new Menu(skin);
	private ScrollPane scrollPane = new ScrollPane(mainMenu);
	
	public Button getCloseButton()
	{
		return closeButton;
	}
	
	public InventoryScreen(String title, Skin skin, Menu start) 
	{
		super(title, skin);
		this.setWidth(400);
		this.setHeight(50);
		this.defaults();
		this.add(closeButton);
		getTitleTable().add(closeButton).size(20, 15).padTop(-1);
	}	
}
