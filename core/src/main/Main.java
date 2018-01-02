package main;

import java.util.Random;

import com.badlogic.gdx.Game;

import gameHelpers.AssetLoader;
import screens.GameScreen;
import screens.MainMenuScreen;

public class Main extends Game { // Main game class
	private static int width; // Dimensions used for whole program
	private static int height;
	private static int[][] stars;
	
	public Main() {
		width = 1920;
		height = 1080;
		generateStars(500);
	}
	
	private void generateStars(int n) {
		stars = new int[n][2];
		Random rand = new Random();
		for(int i = 0; i < n; i++) {
			int x = rand.nextInt(Main.getWidth());
			int y = rand.nextInt(Main.getHeight());
			stars[i][0] = x;
			stars[i][1] = y;
		}
	}

	@Override
	public void create() {
		AssetLoader.load();
		setMainMenuScreen(this);
	}
	
	public void dispose() {
		AssetLoader.dispose();
	}
	
	public void setMainMenuScreen(Main main) {
		setScreen(new MainMenuScreen(this));
	}
	
	public void setGameScreen() {
		setScreen(new GameScreen());
	}
	
	public static int getWidth() {
		return width;		
	}
	
	public static int getHeight() {
		return height;		
	}
	
	public static int[][] getStars() {
		return stars;
	}
}