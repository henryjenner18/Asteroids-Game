package gameWorld;

import java.util.ArrayList;
import java.util.Random;

import gameObjects.Asteroid;
import gameObjects.Rocket;
import main.AsteroidsMain;

public class GameWorld { // Updates game objects
	
	private Rocket rocket;	
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	
	public static float xInt, yInt;
	
	public GameWorld() {
		Random r = new Random(); // Random function
		int randHeading = r.nextInt(361); // Random heading between 0-360 degrees
		rocket = new Rocket(80, randHeading, AsteroidsMain.getWidth() / 2, AsteroidsMain.getHeight() / 2);
		
		numAsteroids = 5;
		asteroids = new ArrayList<Asteroid>(numAsteroids);
		
		for(int i = 0; i < numAsteroids; i++) {
			Asteroid asteroid = new Asteroid(10, 150); // Vertices, max radius
			asteroids.add(asteroid);
		}
	}

	public void update(float delta) {
		rocket.update(delta);
		
		for(int i = 0; i < numAsteroids; i++) {
			asteroids.get(i).update(delta);
		}
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
}
