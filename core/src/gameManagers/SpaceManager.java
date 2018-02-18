package gameManagers;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;

import gameObjects.Asteroid;
import gameObjects.Rocket;
import gameObjects.UFO;

public class SpaceManager {
	
	private World world;
	private CollisionDetector colDet;
	
	public SpaceManager(World world, CollisionDetector colDet) {
		this.world = world;
		this.colDet = colDet;
	}

	public void manage() {
		resolveRockets();
		resolveAsteroids();
		resolveMissiles();
		resolveUFOs();
		resolveFragments();
		resolveSparks();
		resolvePowerUps();
		resolveShields();
	}
	
	private ArrayList<Integer> sortArrayList(ArrayList<Integer> orig) {
		int origLength = orig.size(); // Length of original list
		
		if(origLength > 1) {
			// Remove duplicates
			ArrayList<Integer> noDuplicates = new ArrayList<Integer>(); // New list to fill
				
			for(int i = 0; i < origLength; i++) {
				int currentNum = orig.get(i); // Current number in original list to check
				int noDupsLength = noDuplicates.size(); // Update size of no dups list
				boolean found = false;
					
				for(int e = 0; e < noDupsLength; e++) { // Check against every value in dups list
					int noDupsNum = noDuplicates.get(e); // Current number in no dups list to check
						
					if(currentNum == noDupsNum) { // Already in no dups list
						found = true;
					}
				}
				
				if(found == false) { // Not already in no dups list
					noDuplicates.add(currentNum);
				}
			}
				
			int noDupsLength = noDuplicates.size();		
			
			// Bubble sort the no duplicates list in descending order
			for(int i = 0; i < noDupsLength-1; i++) {
				
				for(int j = 0; j < noDupsLength-i-1; j++) {
					
					if(noDuplicates.get(j) < noDuplicates.get(j+1)) { // Compare adjacent elements
						 // Switch values
						int temp = noDuplicates.get(j+1);
						noDuplicates.set(j+1, noDuplicates.get(j));
						noDuplicates.set(j, temp);
					}
				}
			}
			
			return noDuplicates;
		}
		
		return orig;
	}
	
	private void resolveRockets() {
		ArrayList<Integer> objs = sortArrayList(colDet.getRocketsToRemove());

		for(int i = 0; i < objs.size(); i++) {
			Rocket rocket = world.getRocket(i);
			float x = rocket.getX();
			float y = rocket.getY();
			float r = (float) (rocket.getR() / 2.4);
			Vector2 objVelocity = rocket.getVelocity();
			int[] fillColour = rocket.getFillColour();
			int[] lineColour = rocket.getLineColour();
			world.objSpawner.fragments(x, y, r, objVelocity, fillColour, lineColour);
			world.removeRocket(i);
			world.loseLife();
		}
	}
	
	private void resolveAsteroids() {
		ArrayList<Integer> asteroids = new ArrayList<Integer>();
		
		if(world.isClearScreen()) {
			int numAsteroids = world.getNumAsteroids();
			
			for(int i = 0; i < numAsteroids; i++) {
				asteroids.add(i);
				
				float x = world.getAsteroid(i).getX();
				float y = world.getAsteroid(i).getY();
				world.objSpawner.sparks(x, y, false);
				world.objSpawner.sparks(x, y, false);
			}
			
			world.setClearScreen(false);
			
		} else {
			asteroids = colDet.getAsteroidsToRemove();
		}

		ArrayList<Integer> objs = sortArrayList(asteroids);
				
		for(int i = 0; i < objs.size(); i++) {
			Asteroid asteroid = world.getAsteroid(objs.get(i));
			int score = asteroid.getScore();
			world.addScore(score);
			asteroid.split();
			world.removeAsteroid(objs.get(i));
		}
	}
	
	private void resolveMissiles() {
		ArrayList<Integer> objs = new ArrayList<Integer>();
		
		int numMissiles = world.getNumMissiles();
		
		for(int i = 0; i < numMissiles; i++) {
			if(world.getMissile(i).getTimeLeft() == 0) {
				objs.add(i);
			}
		}
		
		objs = sortArrayList(objs);
		
		for(int i = 0; i < objs.size(); i++) {		
			world.removeMissile(objs.get(i));
		}
	}
	
	private void resolveUFOs() {
		ArrayList<Integer> objs = sortArrayList(colDet.getUFOsToRemove());
				
		for(int i = 0; i < objs.size(); i++) {
			UFO ufo = world.getUFO(objs.get(i));
			int score = ufo.getScore();
			world.addScore(score);
			
			float x = ufo.getX();
			float y = ufo.getY();
			float r = (float) (ufo.getR() / 2.5);
			Vector2 objVelocity = ufo.getVelocity();
			int[] fillColour = ufo.getFillColour();
			int[] lineColour = ufo.getLineColour();
			
			world.checkForPowerUp(x, y);
			world.objSpawner.fragments(x, y, r, objVelocity, fillColour, lineColour);
			world.removeUFO(objs.get(i));
		}
	}
	
	private void resolveFragments() {
		ArrayList<Integer> objs = new ArrayList<Integer>();
		
		int numFragments = world.getNumFragments();
		
		for(int i = 0; i < numFragments; i++) {
			if(world.getFragment(i).getTimeLeft() == 0) {
				objs.add(i);
			}
		}
		
		objs = sortArrayList(objs);
		
		for(int i = 0; i < objs.size(); i++) {
			world.removeFragment(objs.get(i));
		}
	}
	
	private void resolveSparks() {
		ArrayList<Integer> objs = new ArrayList<Integer>();
		
		int numSparks = world.getNumSparks();
		
		for(int i = 0; i < numSparks; i++) {
			if(world.getSpark(i).getTimeLeft() == 0) {
				objs.add(i);
			}
		}
		
		objs = sortArrayList(objs);
		
		for(int i = 0; i < objs.size(); i++) {
			world.removeSpark(objs.get(i));
		}
	}
	
	private void resolvePowerUps() {
		ArrayList<Integer> objs = new ArrayList<Integer>();
		
		int numPowerUps = world.getNumPowerUps();
		
		for(int i = 0; i < numPowerUps; i++) {
			if(world.getPowerUp(i).getTimeLeft() == 0) {
				objs.add(i);
			}
		}
		
		objs = sortArrayList(objs);
		
		for(int i = 0; i < objs.size(); i++) {
			world.removePowerUp(objs.get(i));
		}
	}
	
	private void resolveShields() {
		ArrayList<Integer> objs = new ArrayList<Integer>();
		
		int numShields = world.getNumShields();
		
		for(int i = 0; i < numShields; i++) {
			if(world.getShield(i).getTimeLeft() == 0) {
				objs.add(i);
			}
		}
		
		objs = sortArrayList(objs);
		
		for(int i = 0; i < objs.size(); i++) {
			world.removeShield(objs.get(i));
			world.getRocket(0).setShield(false);
		}
	}
}
