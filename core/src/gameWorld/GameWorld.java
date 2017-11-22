package gameWorld;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import gameObjects.Asteroid;
import gameObjects.Missile;
import gameObjects.Particle;
import gameObjects.Rocket;
import gameObjects.RocketFragment;
import gameObjects.UFO;
import main.AsteroidsMain;

public class GameWorld { // Updates game objects
	
	private Rocket rocket;	
	private static ArrayList<Asteroid> asteroids;
	private static ArrayList<Missile> missiles;
	private static ArrayList<Particle> particles;
	private static ArrayList<RocketFragment> rocketFragments;
	private static ArrayList<UFO> ufos;
	private Random rand;
	private int n;
	
	private UFO ufo;
	
	public GameWorld() {
		n = 3;
		rand = new Random(); // Random function
		rocket = new Rocket(this);
		asteroids = new ArrayList<Asteroid>();	
		missiles = new ArrayList<Missile>();
		particles = new ArrayList<Particle>();
		rocketFragments = new ArrayList<RocketFragment>();
		ufos = new ArrayList<UFO>();
	}

	public void update(float delta) {		
		rocket.update(delta);
		
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(delta);
		}
		
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).update(delta);
		}
		
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).update(delta);
		}
		
		for(int i = 0; i < rocketFragments.size(); i++) {
			rocketFragments.get(i).update(delta);
		}
		
		for(int i = 0; i < ufos.size(); i++) {
			ufos.get(i).update(delta);
		}
		
		if(asteroids.size() == 0) {
			for(int i = 0; i < n; i++) {
				spawnAsteroid();
			}	
			n++;
		}
	}
	
	private void spawnAsteroid() {
		int f = 5; // Frame size, max hori/vert distance they can be away from the edges
		float x = 0, y = 0; // Initialise x and y
		float w = AsteroidsMain.getWidth(), h = AsteroidsMain.getHeight();
		boolean acceptable = false;
		
		while(acceptable == false) { // While acceptable coordinates haven't been found
			x = rand.nextInt((int) (w + 1));
			y = rand.nextInt((int) (h + 1));
			
			if(x >= f && x <= w - f && y >= f && y <= h - f) { // Check to see if not in frame
				acceptable = false;
			} else {
				acceptable = true;
			}
		}
		int avgR = rand.nextInt(21) + 90;
		int v = rand.nextInt(101) + 150;
		int hg = rand.nextInt(361);
		createAsteroid(x, y, avgR, v, hg);
	}
	
	public void createAsteroid(float x, float y, double avgR, int v, int hg) {
		if(avgR >= 15) {
			Asteroid asteroid = new Asteroid(x, y, avgR, v, hg);
			asteroids.add(asteroid);
		}	
	}
	
	public void createMissile(float x, float y, Vector2 vel, int hg) {
		Missile missile = new Missile(x, y, vel, hg);
		missiles.add(missile);
	}
	
	public void createParticle(float x, float y) {
		Particle particle = new Particle(x, y);
		particles.add(particle);
	}
	
	public void createRocketFragment(float x, float y) {
		if(rocketFragments.size() < rocket.getNumFragments()) {
			RocketFragment rocketFragment = new RocketFragment(x, y);
			rocketFragments.add(rocketFragment);
		}
	}
	
	public void createUFO(float x, float y) {
		UFO ufo = new UFO(x, y);
		ufos.add(ufo);
	}
	
	public void removeMissile(int i) {
		missiles.remove(i);
	}
	
	public void removeAsteroid(int i) {
		asteroids.remove(i);
	}
	
	public void removeParticle(int i) {
		particles.remove(i);
	}
	
	public void removeRocketFragment(int i) {
		rocketFragments.remove(i);
	}
	
	public void removeUFO(int i) {
		ufos.remove(i);
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
	
	public Particle getParticle(int i) {
		return particles.get(i);
	}
	
	public int getNumParticles() {
		return particles.size();
	}
	
	public RocketFragment getRocketFragment(int i) {
		return rocketFragments.get(i);
	}
	
	public int getNumRocketFragments() {
		return rocketFragments.size();
	}
	
	public UFO getUFO(int i) {
		return ufos.get(i);
	}
	
	public int getNumUFOs() {
		return ufos.size();
	}
}
