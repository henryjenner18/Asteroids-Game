package gameManagers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import gameHelpers.AssetLoader;
import gameObjects.Asteroid;
import gameObjects.Fragment;
import gameObjects.Missile;
import gameObjects.PowerUp;
import gameObjects.Rocket;
import gameObjects.Shield;
import gameObjects.Spark;
import gameObjects.UFO;
import main.Main;

public class World {
	
	ArrayList<Rocket> rockets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Missile> missiles;
	ArrayList<UFO> ufos;
	ArrayList<Fragment> fragments;
	ArrayList<Spark> sparks;
	ArrayList<PowerUp> powerUps;
	
	private float ufoSpawnTimer, asteroidSpawnTimer, gameTimer;
	float gameOverTimer;
	boolean respawn;
	private boolean nextLevel, clearScreen;
	private int score, level, lives, ufoAccuracy, extraLifeCount, ufoDelta;
	private GameState currentState;	
	public ObjectSpawner objSpawner;
	
	public World() {
		objSpawner = new ObjectSpawner(this);
		init();
	}
	
	private void init() {
		rockets = new ArrayList<Rocket>();
		asteroids = new ArrayList<Asteroid>();
		missiles = new ArrayList<Missile>();
		ufos = new ArrayList<UFO>();
		fragments = new ArrayList<Fragment>();
		sparks = new ArrayList<Spark>();
		powerUps = new ArrayList<PowerUp>();
		
		ufoDelta = 18;
		gameTimer = 0;
		score = level = extraLifeCount = 0;
		lives = 3;
		ufoAccuracy = 10;
		
		resetUFOSpawnTimer();
		resetAsteroidSpawnTimer();
		resetGameOverTimer();
		
		objSpawner.rocket(1);
		if(Main.isTwoPlayer() == true) {
			objSpawner.rocket(2);
		}
		
		currentState = GameState.RUNNING;
	}
	
	public enum GameState {
		RUNNING, GAMEOVER, PAUSE
	}
	
	public void update(float delta) {
		switch (currentState) {
		case PAUSE:
			break;
			
		default:
			updatePlay(delta);
			break;
		}
	}

	private void updatePlay(float delta) {
		updateObjects(delta);
		
		if(extraLifeCount >= 25000) {
			extraLifeCount -= 25000;
			lives += 1;
		}
		
		if(isRunning()) {
			for(int i = 0; i < rockets.size(); i++) {
				rockets.get(i).update(delta);
			}
			
			ufoSpawnTimer -= delta;
			
			if(ufoSpawnTimer <= 0) {
				objSpawner.newUFO();
				resetUFOSpawnTimer();
			}
		}
		
		if(asteroids.size() == 0 && ufos.size() == 0 && !isGameOver()) {
			nextLevel = true;
		}
		
		if(isNextLevel()) {
			if(asteroidSpawnTimer > 0) {
				asteroidSpawnTimer -= delta;
				
			} else {
				levelUp();
				
				for(int a = 0; a < level + 1; a ++) {
					objSpawner.newAsteroid();
				}
				
				resetAsteroidSpawnTimer();
				nextLevel = false;
			}
		}
		
		if(!isGameOver()) {
			gameTimer += delta;
		}
	}
	
	private void updateObjects(float delta) {
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(delta);
		}
		
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).update(delta);
		}
		
		for(int i = 0; i < ufos.size(); i++) {
			ufos.get(i).update(delta);
		}
		
		for(int i = 0; i < fragments.size(); i++) {
			fragments.get(i).update(delta);
		}
		
		for(int i = 0; i < sparks.size(); i++) {
			sparks.get(i).update(delta);
		}
		
		for(int i = 0; i < powerUps.size(); i++) {
			powerUps.get(i).update(delta);
		}
	}
	
	// Gameplay
	private void levelUp() {
		level++;
		if(ufoAccuracy > 0) {
			ufoAccuracy--;
		}
		
		if(ufoDelta > 8) {
			ufoDelta--;
		}
		
		if(level > 1 && Main.isSound()) {
			AssetLoader.levelUp.play(1f);
		}	
	}
	
	public void compareHighScore(boolean twoPlayer) {		
		if(twoPlayer == false) {
			if(score > AssetLoader.getHighScore1P()) {
				AssetLoader.setHighScore1P(score);
			}
			
		} else {
			if(score > AssetLoader.getHighScore2P()) {
				AssetLoader.setHighScore2P(score);
			}
		}
		
	}
	
	public void addScore(int s) {
		score += s;
		extraLifeCount += s;
	}
	
	public void loseLife() {
		lives--;
		
		if(lives <= 0) {
			currentState = GameState.GAMEOVER;

			for(int i = getNumRockets()-1; i >= 0 ; i--) {
				Rocket rocket = getRocket(i);
				float x = rocket.getX();
				float y = rocket.getY();
				float r = (float) (rocket.getR() / 2.4);
				Vector2 objVelocity = rocket.getVelocity();
				int[] fillColour = rocket.getFillColour();
				int[] lineColour = rocket.getLineColour();
				objSpawner.fragments(x, y, r, objVelocity, fillColour, lineColour);
				removeRocket(i);
			}
			
			if(Main.isSound()) {
				AssetLoader.gameOver.play(0.7f);
			}
			
		} else {
			respawn = true;
		}
	}
	
	public void checkForPowerUp(float x, float y) {
		Random rand = new Random();
		
		int n = rand.nextInt(20) + 1;
		
		if(n == 1) {
			objSpawner.powerUp(x, y);
		}
	}
	
	// Game states
	public void start() {
		currentState = GameState.RUNNING;
		
		if(Main.isSound()) {
			AssetLoader.resumeAudio();
		}	
	}
	
	public void restart() {
		init();
		Gdx.input.setCursorCatched(true);
		
		if(Main.isSound()) {
			AssetLoader.playMusic();
		}		
	}
	
	public void pause() {
		currentState = GameState.PAUSE;
		
		if(Main.isSound()) {
			AssetLoader.pauseAudio();
		}
	}
	
	// Setters	
	public void resetAsteroidSpawnTimer() {
		asteroidSpawnTimer = 2;
	}
	
	private void resetUFOSpawnTimer() {
		ufoSpawnTimer = ufoDelta;
	}
	
	private void resetGameOverTimer() {
		gameOverTimer = 3;
	}
	
	public void setClearScreen(boolean b) {
		clearScreen = b;
	}
	
	// Remove objects
	public void removeRocket(int i) {
		rockets.remove(i);
	}
	
	public void removeAsteroid(int i) {
		asteroids.remove(i);
	}
	
	public void removeMissile(int i) {
		missiles.remove(i);
	}
	
	public void removeUFO(int i) {
		ufos.remove(i);
	}
	
	public void removeFragment(int i) {
		fragments.remove(i);
	}
	
	public void removeSpark(int i) {
		sparks.remove(i);
	}
	
	public void removePowerUp(int i) {
		powerUps.remove(i);
	}
	
	// Getters	
	public ArrayList<Rocket> getRockets() {
		return rockets;
	}
	
	public ArrayList<Rocket> getRocketsWithoutShields() {
		ArrayList<Rocket> noShields = new ArrayList<Rocket>();
		
		for(int i = 0; i < getNumRockets(); i++) {
			Rocket r = getRocket(i);
			if(r.getShieldOn() == false) {
				noShields.add(r);
			}
		}
		return noShields;
	}
	
	public Rocket getRocket(int i) {
		return rockets.get(i);
	}
	
	public int getNumRockets() {
		return rockets.size();
	}
	
	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}
	
	public Asteroid getAsteroid(int i) {
		return asteroids.get(i);
	}
	
	public int getNumAsteroids() {
		return asteroids.size();
	}
	
	public ArrayList<Missile> getMissiles() {
		return missiles;
	}
	
	public Missile getMissile(int i) {
		return missiles.get(i);
	}
	
	public int getNumMissiles() {
		return missiles.size();
	}
	
	public ArrayList<UFO> getUFOs() {
		return ufos;
	}
	
	public UFO getUFO(int i) {
		return ufos.get(i);
	}
	
	public int getNumUFOs() {
		return ufos.size();
	}
	
	public ArrayList<Fragment> getFragments() {
		return fragments;
	}
	
	public Fragment getFragment(int i) {
		return fragments.get(i);
	}
	
	public int getNumFragments() {
		return fragments.size();
	}
	
	public ArrayList<Spark> getSparks() {
		return sparks;
	}
	
	public Spark getSpark(int i) {
		return sparks.get(i);
	}
	
	public int getNumSparks() {
		return sparks.size();
	}
	
	public ArrayList<PowerUp> getPowerUps() {
		return powerUps;
	}
	
	public PowerUp getPowerUp(int i) {
		return powerUps.get(i);
	}
	
	public int getNumPowerUps() {
		return powerUps.size();
	}
	
	public ArrayList<Shield> getShields() {
		ArrayList<Shield> shields = new ArrayList<Shield>();
		
		for(int i = 0; i < getNumRockets(); i++) {
			Rocket r = getRocket(i);
			if(r.getShieldOn() == true) {
				shields.add(r.getShield());
			}
		}
		
		return shields;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int getUFOAccuracy() {
		return ufoAccuracy;
	}
	
	public float getGameTimer() {
		return gameTimer;
	}
	
	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}
	
	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}
	
	public boolean isPause() {
		return currentState == GameState.PAUSE;
	}
	
	public boolean isNextLevel() {
		return nextLevel;
	}
	
	public boolean isClearScreen() {
		return clearScreen;
	}
}
