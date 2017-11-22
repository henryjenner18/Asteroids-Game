package gameWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import screens.GameScreen;

public class DeletionManager {
	
	private GameWorld myWorld;
	private CollisionDetector myCollisionDetector;
	
	private ArrayList<Integer> removeMissiles;
	private ArrayList<Integer> removeAsteroids;
	private ArrayList<Integer> removeParticles;
	private ArrayList<Integer> removeRocketFragments;
	private ArrayList<Integer> removeUFOs;
	
	private int smallCount;
	
	public DeletionManager(GameWorld world, CollisionDetector collisionDetector) {
		myWorld = world;
		myCollisionDetector = collisionDetector;
		smallCount = 0;
	}

	public void delete() {
		removeMissiles = new ArrayList<Integer>();
		removeAsteroids = new ArrayList<Integer>(myCollisionDetector.getRemoveAsteroids());
		removeParticles = new ArrayList<Integer>();
		removeRocketFragments = new ArrayList<Integer>();
		removeUFOs = new ArrayList<Integer>(myCollisionDetector.getRemoveUFOs());
		removeMissiles();
		removeAsteroids();
		removeParticles();
		removeRocketFragments();
		removeUFOs();
	}
	
	private void removeUFOs() {
		Set<Integer> noDuplicates = new HashSet<Integer>();
		noDuplicates.addAll(removeUFOs);
		removeUFOs.clear();
		removeUFOs.addAll(noDuplicates);
		Collections.sort(removeUFOs);
		Collections.reverse(removeUFOs);
		
		for(int i = 0; i < removeUFOs.size(); i++) {
			myWorld.removeUFO(removeUFOs.get(i));
		}
	}
	
	private void removeRocketFragments() {
		int numRocketFragments = myWorld.getNumRocketFragments();
		
		for(int i = 0; i < numRocketFragments; i++) {
			if(myWorld.getRocketFragment(i).getTimeLeft() == 0) {
				removeRocketFragments.add(i);
			}
		}
		
		Collections.sort(removeRocketFragments);
		Collections.reverse(removeRocketFragments);
		
		for(int i = 0; i < removeRocketFragments.size(); i++) {
			myWorld.removeRocketFragment(removeRocketFragments.get(i));
		}
	}

	private void removeParticles() {
		int numParticles = myWorld.getNumParticles();
		
		for(int i = 0; i < numParticles; i++) {
			if(myWorld.getParticle(i).getTimeLeft() == 0) {
				removeParticles.add(i);
			}
		}
		
		Collections.sort(removeParticles);
		Collections.reverse(removeParticles);
		
		for(int i = 0; i < removeParticles.size(); i++) {
			myWorld.removeParticle(removeParticles.get(i));
		}
	}

	private void removeMissiles() {
		int numMissiles = myWorld.getNumMissiles();
		
		for(int i = 0; i < numMissiles; i++) {
			if(myWorld.getMissile(i).getTimeLeft() == 0) {
				removeMissiles.add(i);
			}
		}
		
		Set<Integer> noDuplicates = new HashSet<Integer>();
		noDuplicates.addAll(removeMissiles);
		removeMissiles.clear();
		removeMissiles.addAll(noDuplicates);
		Collections.sort(removeMissiles);
		Collections.reverse(removeMissiles);
		
		for(int i = 0; i < removeMissiles.size(); i++) {
			myWorld.removeMissile(removeMissiles.get(i));
		}
	}
	
	private void removeAsteroids() {
		Set<Integer> noDuplicates = new HashSet<Integer>();
		noDuplicates.addAll(removeAsteroids);
		removeAsteroids.clear();
		removeAsteroids.addAll(noDuplicates);
		Collections.sort(removeAsteroids);
		Collections.reverse(removeAsteroids);
		
		for(int i = 0; i < removeAsteroids.size(); i++) {
			int avgRadius = (int) myWorld.getAsteroid(removeAsteroids.get(i)).getAvgRadius();	
			int score = 0;
			if(avgRadius < 30) {score = 100; smallCount++; }
			if(avgRadius >= 30) { score = 80; }
			if(avgRadius >= 40) { score = 70; }
			if(avgRadius >= 50) { score = 60; }
			if(avgRadius >= 60) { score = 40; }
			if(avgRadius >= 70) { score = 30; }
			if(avgRadius >= 90) { score = 20; }
			
			//System.out.println("Score: " + score);
			GameScreen.changeScore(score);
			splitAsteroid(i);
			myWorld.removeAsteroid(removeAsteroids.get(i));
			//System.out.println(smallCount);
			if(smallCount == 8) {
				//myWorld.spawnAsteroid();
				//System.out.println("new due to small count");
				smallCount = 0;
			}
		}
	}
	
	private void splitAsteroid(int i) {
		float x = myWorld.getAsteroid(removeAsteroids.get(i)).getX();
		float y = myWorld.getAsteroid(removeAsteroids.get(i)).getY();
		
		double originalA = myWorld.getAsteroid(removeAsteroids.get(i)).getArea();
		double newR = Math.sqrt(originalA * 0.7 / (2 * Math.PI)); // % of asteroid to waste away
		int v = myWorld.getAsteroid(removeAsteroids.get(i)).getV();
		
		Random rand = new Random();
		int hg = rand.nextInt(361);
		for(int a = 0; a < 2; a++) {
			myWorld.createAsteroid(x, y, newR, v, hg);
			hg += rand.nextInt(161) + 100;
		}
		
		for(int p = 0; p < 10; p++) {
			myWorld.createParticle(x, y);
		}
	}
}
