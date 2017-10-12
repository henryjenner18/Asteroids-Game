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
		rocket = new Rocket(this);
		
		asteroids = new ArrayList<Asteroid>();
		for(int i = 0; i < 3; i++) {
			spawnAsteroid();
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
	
	private void spawnAsteroid() {
		int f = 5; // Frame size, max hori/vert distance they can be away from the edges
		float x = 0, y = 0; // Initialise x and y
		float w = AsteroidsMain.getWidth(), h = AsteroidsMain.getHeight();
		boolean acceptable = false;
		
		while(acceptable == false) { // While acceptable coordinates haven't been found
			x = rand.nextInt((int) (w + 1));
			y = rand.nextInt((int) (h + 1));
			
			if(x >= f && x <= w - f && y >= f && y <= h - f) { // Check to see if not in frame
				acceptable = false;
			} else {
				acceptable = true;
			}
		}
		int avgR = rand.nextInt(21) + 90;
		int v = rand.nextInt(101) + 150;
		createAsteroid(x, y, avgR, v);
	}
	
	public void createAsteroid(float x, float y, double avgR, int v) {
		if(avgR >= 15) {
			Asteroid asteroid = new Asteroid(x, y, avgR, v);
			asteroids.add(asteroid);
		}	
	}
	
	public void removeMissile(int i) {
		missiles.remove(i);
	}
	
	public void removeAsteroid(int i) {
		asteroids.remove(i);
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
