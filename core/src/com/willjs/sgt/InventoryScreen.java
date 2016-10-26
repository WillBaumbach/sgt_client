package com.willjs.sgt;

import java.util.Objects;

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
	private Object[] listEntries = {"Ship", "Planet", "Units", "Settings"};
	private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	private Button closeButton = new TextButton("x", skin);
	private List list1 = new List(skin);
	private ScrollPane scrollPane = new ScrollPane(list1,skin);
	private Table table = new Table(skin);
	
	public Button getCloseButton()
	{
		return closeButton;
	}
	
	public InventoryScreen(String title, Skin skin) 
	{
		super(title, skin);
		this.setWidth(400);
		this.setHeight(400);
		this.defaults();
		list1.setItems(listEntries);
		list1.getSelection().setMultiple(true);
		list1.getSelection().setRequired(false);
		this.add(closeButton);
		getTitleTable().add(closeButton).size(20, 15).padTop(-1);
		this.add(table).left();
		table.add(scrollPane).fill().expand();
		
		
		
		
	}	
	
}
