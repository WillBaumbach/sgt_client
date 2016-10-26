package com.willjs.sgt.desktop;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.willjs.sgt.MessageListener;
import com.willjs.sgt.Request;
import com.willjs.sgt.Server;
import com.willjs.sgt.SpaceGameThing;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpaceGameThing(), config);
	}
}
