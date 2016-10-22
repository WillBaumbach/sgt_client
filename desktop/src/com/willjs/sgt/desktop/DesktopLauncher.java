package com.willjs.sgt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.willjs.sgt.Server;
import com.willjs.sgt.SpaceGameThing;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		Server server = new Server("ws://localhost:1111");
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpaceGameThing(), config);
	}
}
