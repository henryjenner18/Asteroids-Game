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
			Asteroid asteroid = new Asteroid(4, rand.nextInt(AsteroidsMain.getWidth() + 1),
					rand.nextInt(AsteroidsMain.getHeight() + 1)); //rand.nextInt(4) + 1
			asteroids.add(asteroid);
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
	
	private void newAsteroid(int s, float x, float y) {
		if(s >= 1 & s <= 4) {
			Asteroid asteroid = new Asteroid(s, x, y);
			asteroids.add(asteroid);
		}
	}
	
	public void splitAsteroid(int a, int m) {
		int s = asteroids.get(a).getSize() - 1;
		float x = asteroids.get(a).xcoord();
		float y = asteroids.get(a).ycoord();
		
		if(s > 0) {
			for(int i = 0; i < 2; i++) {
				x += rand.nextInt(20) - 10;
				y += rand.nextInt(20) - 10; // Refine
				newAsteroid(s, x, y);
			}
		}
		//System.out.println("Asteroid hit - Length of asteroid list: " + asteroids.size() + ", removing " + a);
		asteroids.remove(a);
		
		//System.out.println("Asteroid hit - Length of missiles list: " + missiles.size() + ", removing " + m);
		missiles.remove(m); //ASKING IT TO REMOVEINVALID INDEX???!
	}
	
	public static void addMissile() {
		Missile missile = new Missile();
		missiles.add(missile);
	}
	
	public static void removeMissile() {
		//System.out.println("Time out - Length of missiles list: " + missiles.size() + ", removing " + 0);
		missiles.remove(0); // Need to remove first element of missile array as this will have been the earliest missile to have been fired
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
