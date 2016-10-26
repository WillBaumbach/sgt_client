package com.willjs.sgt.desktop;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
<<<<<<< HEAD
import com.willjs.sgt.MessageListener;
import com.willjs.sgt.Request;
import com.willjs.sgt.Server;
=======
//import com.willjs.sgt.Server;
>>>>>>> b5800758ad2210e6f2dd44d1c6462684c1dbafa9
import com.willjs.sgt.SpaceGameThing;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
<<<<<<< HEAD
=======
		
		//Server server = new Server("ws://localhost:1111");
		
>>>>>>> b5800758ad2210e6f2dd44d1c6462684c1dbafa9
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Space Game Thing";
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new SpaceGameThing(), config);
	}
}