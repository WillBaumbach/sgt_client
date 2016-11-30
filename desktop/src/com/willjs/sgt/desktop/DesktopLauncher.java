package com.willjs.sgt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.willjs.sgt.MessageListener;
import com.willjs.sgt.Request;
import com.willjs.sgt.Server;
import com.willjs.sgt.SpaceGameThing;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Space Game Thing";
		config.width = 1080;
		config.height = 720;
		new LwjglApplication(new SpaceGameThing(), config);
	}
} 