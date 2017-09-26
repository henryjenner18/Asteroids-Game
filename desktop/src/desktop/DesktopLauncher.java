package desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import main.AsteroidsMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Asteroids by Henry";
		config.width = AsteroidsMain.getWidth();
		config.height = AsteroidsMain.getHeight();
		config.resizable = false;
		new LwjglApplication(new AsteroidsMain(), config);
	}
}
