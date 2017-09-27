package main;

import com.badlogic.gdx.Game;

import screens.GameScreen;

public class AsteroidsMain extends Game { //Main class
	//a test comment
	private static int width = 1600;
	private static int height = 1000;

	@Override
	public void create() {
		setScreen(new GameScreen());		
	}
	
	public static int getWidth() {
		return width;		
	}
	
	public static int getHeight() {
		return height;		
	}
}
