package gameWorld;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import gameObjects.Asteroid;
import gameObjects.Missile;
import gameObjects.Rocket;

public class CollisionDetector {
	
	private GameWorld myWorld;
	
	private Rocket rocket;
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	private static int numMissiles;
	private static ArrayList<Missile> missiles;
	
	private static ArrayList<float[]> intersections;
	private ArrayList<Integer> removeAsteroids;
	
	public CollisionDetector(GameWorld world) {
		myWorld = world; // Initialise variable with GameWorld object received from GameScreen	
		rocket = myWorld.getRocket();
	}

	public void manage() {
		rocket.linearEquation();	
		setAsteroids();
		setMissiles();
		checkCollisions(); // Checks for intersections between rocket and asteroids
	}
	
	private void setAsteroids() {
		numAsteroids = myWorld.getNumAsteroids();
		asteroids = new ArrayList<Asteroid>(numAsteroids);
		
		for(int i = 0; i < numAsteroids; i++) {
			asteroids.add(myWorld.getAsteroid(i));
			asteroids.get(i).linearEquation();
		}
	}
	
	private void setMissiles() {
		numMissiles = myWorld.getNumMissiles();
		missiles = new ArrayList<Missile>(numMissiles);
		
		for(int i = 0; i < numMissiles; i++) {
			missiles.add(myWorld.getMissile(i));
			missiles.get(i).linearEquation();
		}
	}

	private void checkCollisions() {
		intersections = new ArrayList<float[]>();
		removeAsteroids = new ArrayList<Integer>();	
		
		for(int a = 0; a < numAsteroids; a++) { // For each asteroid
			
			float[][] asteroidVertices = asteroids.get(a).getVertices(); // Attributes of current asteroid
			float[] asteroidGradients = asteroids.get(a).getGradients();
			float[] asteroidYintercepts = asteroids.get(a).getYintercepts();

			for(int s = 0; s < asteroids.get(a).getEdges(); s++) { // For each edge of the asteroid

				float[] asteroidXs = new float[2];
				asteroidXs[0] = asteroidVertices[s][0];
				
				if(s == asteroids.get(a).getEdges() - 1) {
					asteroidXs[1] = asteroidVertices[0][0];
				} else {
					asteroidXs[1] = asteroidVertices[s+1][0];
				}
				
				// Asteroids - rocket
				float[][] rocketVertices = rocket.getVertices(); // Attributes of the rocket
				float[] rocketGradients = rocket.getGradients();
				float[] rocketYintercepts = rocket.getYintercepts();
				
				for(int r = 0; r < rocket.getEdges(); r++) { // For each edge of the rocket

					float[] rocketXs = new float[2];
					rocketXs[0] = rocketVertices[r][0];
					
					if(r == rocket.getEdges() - 1) {
						rocketXs[1] = rocketVertices[0][0];
					} else {
						rocketXs[1] = rocketVertices[r+1][0];
					}
					
					// Solve for x
					float x = (rocketYintercepts[r] - asteroidYintercepts[s]) / (asteroidGradients[s] - rocketGradients[r]);
					
					if(xCheck(x, rocketXs, asteroidXs) == true) { // Collision has occurred
						float y = (rocketGradients[r] * x) + rocketYintercepts[r];// Calculate y with x
						float[] intersection = {x, y};
						intersections.add(intersection);		
						//Blow up asteroid
					}
				}
				
				// Asteroids - missiles
				for(int m = 0; m < numMissiles; m++) { // For each missile
					float[][] missileVertices = missiles.get(m).getVertices(); // Attributes of current missile
					float[] missileGradients = missiles.get(m).getGradients();
					float[] missileYintercepts = missiles.get(m).getYintercepts();
					
					float[] missileXs = new float[2];
					missileXs[0] = missileVertices[0][0];
					missileXs[1] = missileVertices[1][0];
					
					// Solve for x
					float x = (missileYintercepts[0] - asteroidYintercepts[s]) / (asteroidGradients[s] - missileGradients[0]);
					
					if(xCheck(x, missileXs, asteroidXs) == true) { // Collision has occurred
						float y = (missileGradients[0] * x) + missileYintercepts[0];
						float[] intersection = {x, y};
						intersections.add(intersection);
						missiles.get(m).setTimeLeft(0);
						removeAsteroids.add(a);
					}
				}
			}
		}
	}

	private boolean xCheck(float x, float[] aXs, float[] bXs) {
		boolean aCheck = false;
		boolean bCheck = false;
		
		if(max2(aXs[0], aXs[1]) == aXs[0]) {
			if(x <= aXs[0] && x >= aXs[1]) {
				// Acceptable x for a edge
				aCheck = true;
			}
		} else {
			if(x <= aXs[1] && x >= aXs[0]) {
				// Acceptable x for a edge
				aCheck = true;
			}
		}
		
		if(max2(bXs[0], bXs[1]) == bXs[0]) {
			if(x <= bXs[0] && x >= bXs[1]) {
				// Acceptable x for b edge
				bCheck = true;
			}
		} else {
			if(x <= bXs[1] && x >= bXs[0]) {
				// Acceptable x for b edge
				bCheck = true;
			}
		}
		
		if(aCheck == true && bCheck == true) {
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
	
	public void render(ShapeRenderer sr, SpriteBatch batch, Texture explosionImage) {
		for(int i = 0; i < intersections.size(); i++) {
			sr.begin(ShapeType.Line);
			sr.setColor(Color.RED);
			sr.circle(intersections.get(i)[0], intersections.get(i)[1], 14);
			sr.end();
		}
		
		/*if(intersections.size() > 0) { // An explosion
			int wh = 190; // Width and height
			batch.begin();
			batch.draw(explosionImage, intersections.get(0)[0] - (wh / 2), intersections.get(0)[1] - (wh / 2), wh, wh);
			batch.end();
		}*/
	}
	
	public static ArrayList<float[]> getIntersections() {
		return intersections;
	}
	
	public ArrayList<Integer> getRemoveAsteroids() {
		return removeAsteroids;
	}
}
