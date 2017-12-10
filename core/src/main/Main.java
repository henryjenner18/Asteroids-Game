package main;

import com.badlogic.gdx.Game;

import gameHelpers.AssetLoader;
import screens.GameScreen;

public class Main extends Game { // Main game class
	private static int width; // Dimensions used for whole program
	private static int height;
	
	public Main() {
		width = 1920;
		height = 1080;
	}

	@Override
	public void create() {
		//AssetLoader.load();
		setScreen(new GameScreen()); // Set the current screen to the game screen	
	}
	
	public void dispose() {
		//AssetLoader.dispose();
	}
	
	public static int getWidth() {
		return width;		
	}
	
	public static int getHeight() {
		return height;		
	}
}