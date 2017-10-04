package gameWorld;

import java.util.ArrayList;
import java.util.Random;

import gameObjects.Asteroid;
import gameObjects.Missile;
import gameObjects.Rocket;
import main.AsteroidsMain;

public class GameWorld { // Updates game objects
	
	private Rocket rocket;	
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	public static int numMissiles;
	private static ArrayList<Missile> missiles;
	
	public GameWorld() {
		Random rand = new Random(); // Random function
		int randHeading = rand.nextInt(361); // Random heading between 0-360 degrees
		rocket = new Rocket(80, randHeading, AsteroidsMain.getWidth() / 2, AsteroidsMain.getHeight() / 2);
		
		numAsteroids = 8;
		asteroids = new ArrayList<Asteroid>(numAsteroids);
		
		for(int i = 0; i < numAsteroids; i++) {
			Asteroid asteroid = new Asteroid(rand.nextInt(4) + 1);
			asteroids.add(asteroid);
		}
		
		missiles = new ArrayList<Missile>();
	}

	public void update(float delta) {
		rocket.update(delta);
		
		for(int i = 0; i < numAsteroids; i++) {
			asteroids.get(i).update(delta);
		}
		
		numMissiles = missiles.size();
		
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).update(delta);
		}
	}
	
	public static void addMissile() {
		Missile missile = new Missile();
		missiles.add(missile);
		numMissiles = missiles.size();
	}
	
	public static void removeMissile() {
		missiles.remove(0); // Need to remove first element of missile array as this will have been the earliest missile to have been fired
		numMissiles = missiles.size(); // Update number of missiles remaining
	}
	
	public Rocket getRocket() {
		return rocket;
	}
	
	public Asteroid getAsteroid(int i) {
		return asteroids.get(i);
	}
	
	public int getNumAsteroids() {
		return numAsteroids;
	}
	
	public Missile getMissile(int i) {
		return missiles.get(i);
	}
	
	public int getNumMissiles() {
		return numMissiles;
	}
}
