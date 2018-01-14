package gameManagers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
	}
	
	private ArrayList<Integer> sortArrayList(ArrayList<Integer> objs) {
		Set<Integer> noDuplicates = new HashSet<Integer>(); // Removes duplicates
		noDuplicates.addAll(objs);
		objs.clear(); // Clears original array list
		objs.addAll(noDuplicates); // Replaces with new list
		Collections.sort(objs); // Sorts list ascending
		Collections.reverse(objs); // Sorts list descending
		
		return objs;
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
		ArrayList<Integer> objs = sortArrayList(colDet.getAsteroidsToRemove());
				
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
}
