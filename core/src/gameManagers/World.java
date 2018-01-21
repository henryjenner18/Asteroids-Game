package gameManagers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import gameHelpers.AssetLoader;
import gameObjects.Asteroid;
import gameObjects.Fragment;
import gameObjects.Missile;
import gameObjects.PowerUp;
import gameObjects.Rocket;
import gameObjects.Shield;
import gameObjects.Spark;
import gameObjects.UFO;

public class World {
	
	ArrayList<Rocket> rockets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Missile> missiles;
	ArrayList<UFO> ufos;
	ArrayList<Fragment> fragments;
	ArrayList<Spark> sparks;
	ArrayList<PowerUp> powerUps;
	ArrayList<Shield> shields;
	
	float rocketSpawnTimer;
	private float ufoSpawnTimer, asteroidSpawnTimer, gameTimer;
	float gameOverTimer;
	boolean respawn;
	private boolean nextLevel, clearScreen;
	private int score, level, lives, UFOAccuracy, extraLifeCount;
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
		shields = new ArrayList<Shield>();
		
		resetUFOSpawnTimer();
		resetAsteroidSpawnTimer();
		resetGameOverTimer();
		gameTimer = 0;
		score = level = extraLifeCount = (int) (rocketSpawnTimer = 0);
		lives = 3;
		UFOAccuracy = 10;
		objSpawner.rocket(0);
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
		
		if(isRunning() && !isRespawn()) {
			for(int i = 0; i < rockets.size(); i++) {
				rockets.get(i).update(delta);
			}
			
			ufoSpawnTimer -= delta;
			
			if(ufoSpawnTimer <= 0) {
				objSpawner.ufo();
				resetUFOSpawnTimer();
			}
		}
		
		if(isRespawn()) {
			objSpawner.rocket(delta);
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
		
		//for(int i = 0; i < shields.size(); i++) {
			//shields.get(i).update(delta);
		//}
	}
	
	// Gameplay
	private void levelUp() {
		level++;
		if(UFOAccuracy > 0) {
			UFOAccuracy--;
		}
	}
	
	public void compareHighScore() {
		if(score > AssetLoader.getHighScore()) {
			AssetLoader.setHighScore(score);
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
	}
	
	public void restart() {
		init();
		Gdx.input.setCursorCatched(true);
	}
	
	public void pause() {
		currentState = GameState.PAUSE;
	}
	
	// Setters
	public void resetRocketSpawnTimer() {
		rocketSpawnTimer = 2;
	}
	
	public void resetAsteroidSpawnTimer() {
		asteroidSpawnTimer = 2;
	}
	
	private void resetUFOSpawnTimer() {
		ufoSpawnTimer = 20;
	}
	
	private void resetGameOverTimer() {
		gameOverTimer = 2;
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
	
	public void removeShield(int i) {
		shields.remove(i);
	}
	
	// Getters	
	public ArrayList<Rocket> getRockets() {
		return rockets;
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
		return shields;
	}
	
	public Shield getShield(int i) {
		return shields.get(i);
	}
	
	public int getNumShields() {
		return shields.size();
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
		return UFOAccuracy;
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
	
	public boolean isRespawn() {
		return respawn;
	}
	
	public boolean isNextLevel() {
		return nextLevel;
	}
	
	public boolean isClearScreen() {
		return clearScreen;
	}
}