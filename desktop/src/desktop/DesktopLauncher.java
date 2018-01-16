package desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import main.Main;

public class DesktopLauncher {
	
	private static int width, height;
	
	public static void main (String[] arg) {		
		/*DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.setFromDisplayMode(displayMode);
		
		width = displayMode.width;
		height = displayMode.height;*/
		
		width = 1920;
		height = 1080;
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Asteroids by Henry";
		config.width = width;
		config.height = height;
		config.fullscreen = false;
		config.resizable = false;

		
		new LwjglApplication(new Main(width, height), config);
	}
}
