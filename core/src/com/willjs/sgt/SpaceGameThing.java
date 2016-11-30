package com.willjs.sgt;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	Texture texture;
	Sprite sprite;
	OrthographicCamera cam;
	
	
	// Screens
	InventoryScreen menuScreenMain, shipScreen, inventoryScreen, planetScreen, unitsScreen, settingsScreen;

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
	Menu inventoryMenu;
	Menu planetMenu;
	Menu unitsMenu;
	Menu settingsMenu;
	SpriteBatch batch;
	World _world;
	Server _server;
	
	@Override
	public void create ()
	{
		_server = new Server("ws://localhost:1111", "2");
		while(!_server.isAuthenticated()){ // wait for client to connect
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		_world = new World(_server);
		
		float h = Gdx.graphics.getHeight();
		float w = Gdx.graphics.getWidth();
		cam = new OrthographicCamera(30,30 * (h/w));
		
		texture = new Texture(Gdx.files.internal("Planet Test.png"));
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		//cam.viewportWidth/2-sprite.getWidth()/2,cam.viewportHeight/2-sprite.getHeight()/2
		
		sprite = new Sprite(texture);
		sprite.setSize(100,100);
		Random rand = new Random();
		float wid = rand.nextInt(1081 - (int)sprite.getWidth());
		float hig = rand.nextInt(721 - (int)sprite.getHeight());
		sprite.setPosition(wid,hig);
		batch = new SpriteBatch();
		
		
		//Buttons
		inventoryButton = new TextButton("Inventory", skin);
		shipButton = new TextButton("Ship", skin);
		planetButton = new TextButton("Planet", skin);
		settingsButton = new TextButton("Settings", skin);
		unitsButton = new TextButton("Units", skin);
		
		//Stage(s)
		mainStage = new Stage(new ScreenViewport());
		//cam.position.set(mainStage.getWidth(),mainStage.getHeight(), 0);
		//cam.update();
		
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
		
		//Ship Screen
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
		
		shipScreen.getCloseButton().addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				shipScreen.remove();
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
		
		cam.update();
		//batch.setProjectionMatrix(cam.combined);
		
		
		//Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		_world.getRenderer().preRender(batch);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sprite.draw(batch);
		_world.getRenderer().render(batch);
		batch.end();
		mainStage.draw();
		//mainStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		
		
		//Checks if 'i' is pressed and opens inventory
		if(Gdx.input.isKeyPressed(Input.Keys.I))
		{
			if(menuScreenMain.getStage() == null)
			{
				mainStage.addActor(menuScreenMain);
			}
		}
		// Checks if ESC is pressed and closes the inv if it is open
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		{
			if(menuScreenMain.getStage() != null)
			{
				menuScreenMain.remove();
			}
		}
		
		_world.processInput();
		_world.update(Gdx.graphics.getDeltaTime());
		
		
	}
	
	@Override
	public void resize (int width, int height) 
	{
		_world.getRenderer().resize();
	}

	@Override
	public void dispose () 
	{
		mainStage.dispose();
		skin.dispose();
	}
}