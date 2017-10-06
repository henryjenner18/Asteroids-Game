package gameWorld;

import java.util.ArrayList;
import java.util.Random;
import gameObjects.Asteroid;
import gameObjects.Missile;
import gameObjects.Rocket;
import main.AsteroidsMain;

public class GameWorld { // Updates game objects
	
	private Rocket rocket;	
	private static ArrayList<Asteroid> asteroids;
	public static int numMissiles;
	private static ArrayList<Missile> missiles;
	private Random rand;
	
	public GameWorld() {
		rand = new Random(); // Random function
		int randHeading = rand.nextInt(361); // Random heading between 0-360 degrees
		rocket = new Rocket(80, randHeading, AsteroidsMain.getWidth() / 2, AsteroidsMain.getHeight() / 2);
		
		asteroids = new ArrayList<Asteroid>();
		for(int i = 0; i < 5; i++) {
			createAsteroid(4, rand.nextInt(AsteroidsMain.getWidth() + 1),
					rand.nextInt(AsteroidsMain.getHeight() + 1));
		}	
		
		missiles = new ArrayList<Missile>();
	}

	public void update(float delta) {		
		rocket.update(delta);
		
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(delta);
		}
		
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).update(delta);
		}
	}
	
	public static void createMissile() {
		Missile missile = new Missile();
		missiles.add(missile);
	}
	
	public void createAsteroid(int s, float x, float y) {
		Asteroid asteroid = new Asteroid(s, x, y);
		asteroids.add(asteroid);
	}
	
	public void removeMissile(int i) {
		missiles.remove(i);
		System.out.println("Removed missile " + i);
	}
	
	public void removeAsteroid(int i) {
		asteroids.remove(i);
		System.out.println("Removed asteroid " + i);
	}
	
	public Rocket getRocket() {
		return rocket;
	}
	
	public Asteroid getAsteroid(int i) {
		return asteroids.get(i);
	}
	
	public int getNumAsteroids() {
		return asteroids.size();
	}
	
	public Missile getMissile(int i) {
		return missiles.get(i);
	}
	
	public int getNumMissiles() {
		return missiles.size();
	}
}
