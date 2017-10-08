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
	private boolean b; // For alternating missile colour
	
	public GameWorld() {
		rand = new Random(); // Random function
		rocket = new Rocket();
		
		asteroids = new ArrayList<Asteroid>();
		for(int i = 0; i < 4; i++) {
			createAsteroid(4, rand.nextInt(AsteroidsMain.getWidth() + 1),
					rand.nextInt(AsteroidsMain.getHeight() + 1));
		}	
		
		missiles = new ArrayList<Missile>();
		b = true;
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
	
	public void createMissile() {
		Missile missile = new Missile(b);
		missiles.add(missile);
		b = !b; // Invert boolean variable
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
