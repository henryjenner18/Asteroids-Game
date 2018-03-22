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
	private static boolean sound, twoPlayer;

	public Main(int width, int height) {
		Main.width = width;
		Main.height = height;
		generateStars(500);
		sound = true;
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
	
	public void setGameScreen(boolean twoPlayer) {
		Main.twoPlayer = twoPlayer;
		setScreen(new GameScreen());
		
		if(isSound()) {
			AssetLoader.inPlayMusic.setVolume(0.13f);
		}
	}
	
	public static boolean isSound() {
		return sound;
	}

	public static void toggleSound() {
		sound = !sound;	
		if(sound == false && AssetLoader.inPlayMusic.isPlaying()) {
			AssetLoader.inPlayMusic.pause();
		} else if(sound == true && AssetLoader.inPlayMusic.isPlaying() == false){
			AssetLoader.inPlayMusic.play();
		}
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
	
	public static boolean isTwoPlayer() {
		return twoPlayer;
	}
}