package gameManagers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;

import gameObjects.Rocket;

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
		Set<Integer> noDuplicates = new HashSet<Integer>();
		noDuplicates.addAll(objs);
		objs.clear();
		objs.addAll(noDuplicates);
		Collections.sort(objs);
		Collections.reverse(objs);
		
		return objs;
	}
	
	private void resolveRockets() {
		ArrayList<Integer> objs = sortArrayList(colDet.getRocketsToRemove());
		
		for(int i = 0; i < objs.size(); i++) {
			Rocket rocket = world.getRocket(i);
			float x = rocket.getX();
			float y = rocket.getY();
			float r = (float) (rocket.getR() / 2.2);
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
			int score = world.getAsteroid(objs.get(i)).getScore();
			world.addScore(score);
			world.getAsteroid(objs.get(i)).split();
			world.removeAsteroid(objs.get(i));
		}
	}
	
	private void resolveMissiles() {
		ArrayList<Integer> objs = new ArrayList<Integer>();
		
		int numMissiles = world.getNumMissiles();
		
		for(int i = 0; i < numMissiles; i++) {
			if(world.getMissile(i).getTimeLeft() == 0) {
				objs.add(i);
				
				if(world.getMissile(i).getCreator() == 'r') {
					world.setHits(1, 1);
				}
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
			int score = world.getUFO(objs.get(i)).getScore();
			world.addScore(score);
			
			float x = world.getUFO(i).getX();
			float y = world.getUFO(i).getY();
			float r = (world.getUFO(i).getR() * 2) / 5;
			Vector2 objVelocity = world.getUFO(i).getVelocity();
			int[] fillColour = world.getUFO(i).getFillColour();
			int[] lineColour = world.getUFO(i).getLineColour();
			
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
