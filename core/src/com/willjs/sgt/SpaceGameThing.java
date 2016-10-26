package com.willjs.sgt;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SpaceGameThing extends ApplicationAdapter 
{
	
	Skin skin;
	Stage mainStage;
	
	// Screens
	InventoryScreen menuScreenMain;
	InventoryScreen shipScreen;
	
	// Buttons
	TextButton x;					// Closes the window
	TextButton shipButton;
	TextButton inventoryButton;
	TextButton planetButton;
	TextButton unitsButton;
	TextButton settingsButton;
	
	// Menus
	Menu mainMenu;
	Menu shipMenu;
	
	World _world;
	Server _server;
	
	@Override
	public void create ()
	{
		_server = new Server("ws://localhost:1111", "1");
		while(!_server.isAuthenticated()){ // wait for client to connect
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		_world = new World(_server);
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		//Buttons
		inventoryButton = new TextButton("Inventory", skin);
		shipButton = new TextButton("Ship", skin);
		planetButton = new TextButton("Planet", skin);
		settingsButton = new TextButton("Settings", skin);
		unitsButton = new TextButton("Units", skin);
		
		//Stage(s)
		mainStage = new Stage(new ScreenViewport());
		
		//Menu(s)
		mainMenu = new Menu(skin);
		mainMenu.addButton(shipButton);
		mainMenu.addButton(unitsButton);
		mainMenu.addButton(inventoryButton);
		mainMenu.addButton(planetButton);
		mainMenu.addButton(settingsButton);
		shipMenu = new Menu(skin);
		
		
		// Screens 
		menuScreenMain = new InventoryScreen("Menu", skin, mainMenu);
		menuScreenMain.add(mainMenu.getTable());
		mainMenu.getTable().defaults().expand().fill();
		shipScreen = new InventoryScreen("Ship", skin, shipMenu);
		shipScreen.setWidth(400);
		shipScreen.setHeight(400);
		Gdx.input.setInputProcessor(mainStage);
		
		
		
		
		
		// Set the "x" in the corner of the inventory to close the window
		menuScreenMain.getCloseButton().addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				menuScreenMain.remove();
			}
		});
		shipButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				mainStage.addActor(shipScreen);
			}
		});
	}
	
	
	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mainStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		mainStage.draw();
		
		//Checks if 'i' is pressed and opens/closes inventory
		if(Gdx.input.isKeyPressed(Input.Keys.I))
		{
			if(menuScreenMain.getStage() == null)
			{
				mainStage.addActor(menuScreenMain);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		{
			if(menuScreenMain.getStage() != null)
			{
				menuScreenMain.remove();
			}
		}
		
		
	}
	
	@Override
	public void resize (int width, int height) 
	{
		mainStage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose () 
	{
		mainStage.dispose();
		skin.dispose();
	}
}