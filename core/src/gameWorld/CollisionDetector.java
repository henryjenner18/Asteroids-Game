package gameWorld;

import java.util.ArrayList;
import gameObjects.Asteroid;
import gameObjects.Missile;
import gameObjects.Rocket;
import gameObjects.UFO;

public class CollisionDetector {
	
	private GameWorld myWorld;
	
	private Rocket rocket;
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	private static int numMissiles;
	private static ArrayList<Missile> missiles;
	private static int numUFOs;
	private static ArrayList<UFO> ufos;
	
	private ArrayList<Integer> removeAsteroids;
	private ArrayList<Integer> removeUFOs;
	
	public CollisionDetector(GameWorld world) {
		myWorld = world; // Initialise variable with GameWorld object received from GameScreen	
		rocket = myWorld.getRocket();
	}

	public void manage() {
		rocket.linearEquation();	
		setAsteroids();
		setMissiles();
		setUFOs();
		checkCollisions();
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
	
	private void setUFOs() {
		numUFOs = myWorld.getNumUFOs();
		ufos = new ArrayList<UFO>(numUFOs);
		
		for(int i = 0; i < numUFOs; i++) {
			ufos.add(myWorld.getUFO(i));
			ufos.get(i).linearEquation();
		}
	}

	private void checkCollisions() {
		//intersections = new ArrayList<float[]>();
		removeAsteroids = new ArrayList<Integer>();	
		removeUFOs = new ArrayList<Integer>();
		
		// Asteroid collisions
		float[][] rocketVertices = rocket.getVertices(); // Attributes of the rocket
		float[] rocketGradients = rocket.getGradients();
		float[] rocketYintercepts = rocket.getYintercepts();
		
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
					
					if(coordRangeCheck(x, rocketXs, asteroidXs) == true) { // Collision has occurred
						float y = (rocketGradients[r] * x) + rocketYintercepts[r]; // Calculate y with x

						for(int i = 0; i < rocket.getNumFragments(); i++){
							myWorld.createRocketFragment(x, y, rocket.getFillColour(), rocket.getLineColour());
						}
						removeAsteroids.add(a);
						rocket.reset();
					}
				}
				
				// Asteroids - missiles
				for(int m = 0; m < numMissiles; m++) { // For each missile
					float[][] missileVertices = missiles.get(m).getVertices(); // Attributes of current missile
					float[] missileGradients = missiles.get(m).getGradients();
					float[] missileYintercepts = missiles.get(m).getYintercepts();
					
					boolean infinityGradient;
					if(missileGradients[0] == Double.POSITIVE_INFINITY || missileGradients[0] == Double.NEGATIVE_INFINITY) {
						infinityGradient = true;
					} else {
						infinityGradient = false;
					}
					
					float x, y;
					if(infinityGradient == false) {	
						x = (missileYintercepts[0] - asteroidYintercepts[s]) / (asteroidGradients[s] - missileGradients[0]);
						
						float[] missileXs = new float[2];
						missileXs[0] = missileVertices[0][0];
						missileXs[1] = missileVertices[1][0];
					
						if(coordRangeCheck(x, missileXs, asteroidXs) == true) { // Collision has occurred
							y = (missileGradients[0] * x) + missileYintercepts[0];			
							//float[] intersection = {x, y};
							//intersections.add(intersection);
							missiles.get(m).setTimeLeft(0);
							removeAsteroids.add(a);
						}
						
					} else if(infinityGradient == true) {
						x = missileVertices[0][0];
						y = (asteroidGradients[s] * x) + asteroidYintercepts[s];
						
						float[] missileYs = new float[2];
						missileYs[0] = missileVertices[0][1];
						missileYs[1] = missileVertices[1][1];
						
						float[] asteroidYs = new float[2];
						asteroidYs[0] = asteroidVertices[s][1];
						
						if(s == asteroids.get(a).getEdges() - 1) {
							asteroidYs[1] = asteroidVertices[0][1];
						} else {
							asteroidYs[1] = asteroidVertices[s+1][1];
						}
						
						if(coordRangeCheck(y, missileYs, asteroidYs) == true) { // Collision has occurred		
							missiles.get(m).setTimeLeft(0);
							removeAsteroids.add(a);
						}
					}
				}				
			}
		}
		
		// UFO collisions
		for(int u = 0; u < numUFOs; u++) {
			
			float[][] ufoVertices = ufos.get(u).getVertices(); // Attributes of current ufo
			float[] ufoGradients = ufos.get(u).getGradients();
			float[] ufoYintercepts = ufos.get(u).getYintercepts();
			
			for(int s = 0; s < ufos.get(u).getEdges(); s++) { // For each edge of the ufo
				
				float[] ufoXs = new float[2];
				ufoXs[0] = ufoVertices[s][0];
				
				if(s == ufos.get(u).getEdges() - 1) {
					ufoXs[1] = ufoVertices[0][0];
				} else {
					ufoXs[1] = ufoVertices[s+1][0];
				}
				
				// UFOs - missiles
				for(int m = 0; m < numMissiles; m++) { // For each missile
					float[][] missileVertices = missiles.get(m).getVertices(); // Attributes of current missile
					float[] missileGradients = missiles.get(m).getGradients();
					float[] missileYintercepts = missiles.get(m).getYintercepts();
					
					boolean infinityGradient;
					if(missileGradients[0] == Double.POSITIVE_INFINITY || missileGradients[0] == Double.NEGATIVE_INFINITY) {
						infinityGradient = true;
					} else {
						infinityGradient = false;
					}
					
					float x, y;
					if(infinityGradient == false) {	
						x = (missileYintercepts[0] - ufoYintercepts[s]) / (ufoGradients[s] - missileGradients[0]);
						
						float[] missileXs = new float[2];
						missileXs[0] = missileVertices[0][0];
						missileXs[1] = missileVertices[1][0];
					
						if(coordRangeCheck(x, missileXs, ufoXs) == true) { // Collision has occurred
							y = (missileGradients[0] * x) + missileYintercepts[0];			
							//float[] intersection = {x, y};
							//intersections.add(intersection);
							missiles.get(m).setTimeLeft(0);
							removeUFOs.add(u);
							for(int i = 0; i < ufos.get(u).getNumFragments(); i++){
								myWorld.createFragment(x, y, ufos.get(u).getFillColour(), ufos.get(u).getLineColour());
							}
						}
						
					} else if(infinityGradient == true) {
						x = missileVertices[0][0];
						y = (ufoGradients[s] * x) + ufoYintercepts[s];
						
						float[] missileYs = new float[2];
						missileYs[0] = missileVertices[0][1];
						missileYs[1] = missileVertices[1][1];
						
						float[] ufoYs = new float[2];
						ufoYs[0] = ufoVertices[s][1];
						
						if(s == ufos.get(u).getEdges() - 1) {
							ufoYs[1] = ufoVertices[0][1];
						} else {
							ufoYs[1] = ufoVertices[s+1][1];
						}
						
						if(coordRangeCheck(y, missileYs, ufoYs) == true) { // Collision has occurred		
							missiles.get(m).setTimeLeft(0);
							removeUFOs.add(u);
							for(int i = 0; i < ufos.get(u).getNumFragments(); i++){
								myWorld.createFragment(x, y, ufos.get(u).getFillColour(), ufos.get(u).getLineColour());
							}
						}
					}
				}
			}
		}
	}

	private boolean coordRangeCheck(float c, float[] aCoords, float[] bCoords) {
		boolean aCheck = false;
		boolean bCheck = false;
		
		if(max2(aCoords[0], aCoords[1]) == aCoords[0]) {
			if(c <= aCoords[0] && c >= aCoords[1]) {
				// Acceptable coord for a edge
				aCheck = true;
			}
		} else {
			if(c <= aCoords[1] && c >= aCoords[0]) {
				// Acceptable coord for a edge
				aCheck = true;
			}
		}
		
		if(max2(bCoords[0], bCoords[1]) == bCoords[0]) {
			if(c <= bCoords[0] && c >= bCoords[1]) {
				// Acceptable coord for b edge
				bCheck = true;
			}
		} else {
			if(c <= bCoords[1] && c >= bCoords[0]) {
				// Acceptable coord for b edge
				bCheck = true;
			}
		}
		
		if(aCheck == true && bCheck == true) {
			// Acceptable coord for both
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
	
	/*public void render(ShapeRenderer sr) {
		for(int i = 0; i < intersections.size(); i++) {
			sr.begin(ShapeType.Line);
			sr.setColor(Color.RED);
			sr.circle(intersections.get(i)[0], intersections.get(i)[1], 14);
			sr.end();
		}
	}*/
	
	/*public static ArrayList<float[]> getIntersections() {
		return intersections;
	}*/
	
	public ArrayList<Integer> getRemoveAsteroids() {
		return removeAsteroids;
	}
	
	public ArrayList<Integer> getRemoveUFOs() {
		return removeUFOs;
	}
}
