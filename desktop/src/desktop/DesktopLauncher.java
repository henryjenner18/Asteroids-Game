package desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import main.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Asteroids by Henry";
		config.width = Main.getWidth();
		config.height = Main.getHeight();
		config.fullscreen = true;
		config.resizable = false;
		new LwjglApplication(new Main(), config);
	}
}
