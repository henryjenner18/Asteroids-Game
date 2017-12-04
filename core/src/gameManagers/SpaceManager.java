package gameManagers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
		ArrayList<Integer> objs = sortArrayList(colDet.getRocketsToReset());
		
		for(int i = 0; i < objs.size(); i++) {
			float x = world.getRocket(i).getX();
			float y = world.getRocket(i).getY();
			int[] fillColour = world.getRocket(i).getFillColour();
			int[] lineColour = world.getRocket(i).getLineColour();
			world.spawnFragments(x, y, fillColour, lineColour);
			//world.getRocket(i).reset();
			world.getRocket(i).setAlive();
			world.removeRocket(i);
			world.startRocketRespawnTimer();
		}
	}
	
	private void resolveAsteroids() {
		ArrayList<Integer> objs = sortArrayList(colDet.getAsteroidsToRemove());
				
		for(int i = 0; i < objs.size(); i++) {
			world.getAsteroid(objs.get(i)).split();
			world.removeAsteroid(objs.get(i));
			//System.out.println("removed a " + i);
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
			//System.out.println("removed m " + i);
		}
	}
	
	private void resolveUFOs() {
		ArrayList<Integer> objs = sortArrayList(colDet.getUFOsToRemove());
				
		for(int i = 0; i < objs.size(); i++) {
			float x = world.getUFO(i).getX();
			float y = world.getUFO(i).getY();
			int[] fillColour = world.getUFO(i).getFillColour();
			int[] lineColour = world.getUFO(i).getLineColour();
			world.spawnFragments(x, y, fillColour, lineColour);
			world.removeUFO(objs.get(i));
			//System.out.println("removed u " + i);
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
			//System.out.println("removed f " + i);
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
}
