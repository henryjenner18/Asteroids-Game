package main;

import com.badlogic.gdx.Game;
import screens.GameScreen;

public class AsteroidsMain extends Game { // Main game class
	private static int width = 2200; // Dimensions used for whole program
	private static int height = 1250;

	@Override
	public void create() {
		setScreen(new GameScreen()); // Set the current screen to the game screen	
	}
	
	public static int getWidth() {
		return width;		
	}
	
	public static int getHeight() {
		return height;		
	}
}
