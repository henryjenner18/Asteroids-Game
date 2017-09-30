package gameWorld;

import java.util.ArrayList;

import gameObjects.Asteroid;
import gameObjects.Rocket;

public class GameManager {
	
	private GameWorld myWorld;
	
	private Rocket rocket;
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	
	private static ArrayList<float[]> intersections;
	
	public GameManager(GameWorld world) {
		myWorld = world; // Initialise variable with GameWorld object received from GameScreen
		
		rocket = myWorld.getRocket();
		
		numAsteroids = myWorld.getNumAsteroids();
		asteroids = new ArrayList<Asteroid>(numAsteroids);
		
		for(int i = 0; i < numAsteroids; i++) {
			asteroids.add(myWorld.getAsteroid(i));
		}
	}

	public void manage() {
		System.out.println("");
		rocket.linearEquation();
		
		for(int i = 0; i < numAsteroids; i++) {
			asteroids.get(i).linearEquation();
		}
		
		checkCollisions();
	}

	private void checkCollisions() {
		intersections = new ArrayList<float[]>();
		
		float[] rocketVertices = rocket.getVertices(); // Attributes of the rocket
		float[] rocketGradients = rocket.getGradients();
		float[] rocketYintercepts = rocket.getYintercepts();
		
		for(int r = 0; r < rocket.getEdges(); r++) { // For each edge of the rocket
			int numRocketPoints = r * 2;
			float[] rocketXs = new float[2];
			rocketXs[0] = rocketVertices[numRocketPoints];
			
			if(numRocketPoints == rocketVertices.length - 2) {
				rocketXs[1] = rocketVertices[0];
			} else {
				rocketXs[1] = rocketVertices[numRocketPoints + 2];
			}
			
			for(int a = 0; a < numAsteroids; a++) { // For each asteroid
				
				float[] asteroidVertices = asteroids.get(a).getVertices(); // Attributes of current asteroid
				float[] asteroidGradients = asteroids.get(a).getGradients();
				float[] asteroidYintercepts = asteroids.get(a).getYintercepts();

				for(int s = 0; s < asteroids.get(a).getEdges(); s++) { // For each edge of the asteroid
					int numAsteroidPoints = s * 2;
					float[] asteroidXs = new float[2];
					asteroidXs[0] = asteroidVertices[numAsteroidPoints];
					
					if(numAsteroidPoints == asteroidVertices.length - 2) {
						asteroidXs[1] = asteroidVertices[0];
					} else {
						asteroidXs[1] = asteroidVertices[numAsteroidPoints + 2];
					}
					
					// Solve for x
					float x = (rocketYintercepts[r] - asteroidYintercepts[s]) / (asteroidGradients[s] - rocketGradients[r]);
					//System.out.println(x);
					//System.out.println("Rocket edge: " + r + ", Asteroid no: " + a + ", Edge no: " + s);
					if(xCheck(x, rocketXs, asteroidXs) == true) {
						// Collision has occurred
						float y = (rocketGradients[r] * x) + rocketYintercepts[r];// Calculate y with x
						//System.out.println(x + "," + y);
						float[] intersection = {x, y};
						intersections.add(intersection);
					}
				}
			}
		}
	}
	
	private boolean xCheck(float x, float[] rocketXs, float[] asteroidXs) {
		boolean rocketCheck = false;
		boolean asteroidCheck = false;
		
		if(max2(rocketXs[0], rocketXs[1]) == rocketXs[0]) {
			if(x <= rocketXs[0] && x >= rocketXs[1]) {
				// Acceptable x for rocket edge
				rocketCheck = true;
			}
		} else {
			if(x <= rocketXs[1] && x >= rocketXs[0]) {
				// Acceptable x for rocket edge
				rocketCheck = true;
			}
		}
		
		if(max2(asteroidXs[0], asteroidXs[1]) == asteroidXs[0]) {
			if(x <= asteroidXs[0] && x >= asteroidXs[1]) {
				// Acceptable x for asteroid edge
				asteroidCheck = true;
			}
		} else {
			if(x <= asteroidXs[1] && x >= asteroidXs[0]) {
				// Acceptable x for asteroid edge
				asteroidCheck = true;
			}
		}
		
		if(rocketCheck == true && asteroidCheck == true) {
			// Acceptable x for both
			return true;
		} else {
			return false;
		}
	}
	
	private float max2(float a, float b) {
		if(a > b) {
			return a;
		} else {
			return b;
		}
	}
	
	public static ArrayList<float[]> getIntersections() {
		return intersections;
	}

}
