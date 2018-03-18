package desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import main.Main;

public class DesktopLauncher {
	
	public static void main (String[] arg) {		
		DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.setFromDisplayMode(displayMode);
		
		//config.fullscreen = false;
		//config.width *= 0.8;
		//config.height *= 0.8;
		//width = displayMode.width;
		//height = displayMode.height;
		
		new LwjglApplication(new Main(config.width, config.height), config);
	}
}
