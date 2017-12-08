package gameManagers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import gameObjects.Asteroid;
import gameObjects.Fragment;
import gameObjects.Missile;
import gameObjects.Rocket;
import gameObjects.Spark;
import gameObjects.UFO;
import main.Main;

public class World {
	
	private ArrayList<Rocket> rockets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Missile> missiles;
	private ArrayList<UFO> ufos;
	private ArrayList<Fragment> fragments;
	private ArrayList<Spark> sparks;
	private float rocketRespawnTimer;
	private boolean countdownRocketRespawn;
	Random rand = new Random();
	
	public World() {
		rockets = new ArrayList<Rocket>();
		asteroids = new ArrayList<Asteroid>();
		missiles = new ArrayList<Missile>();
		ufos = new ArrayList<UFO>();
		fragments = new ArrayList<Fragment>();
		sparks = new ArrayList<Spark>();
		rocketRespawnTimer = 2;
		countdownRocketRespawn = false;
		spawnRocket();
	}
	
	private void checkTimer(float delta) {
		if(rocketRespawnTimer > 0 && countdownRocketRespawn == true) {
			rocketRespawnTimer -= delta;
			
		} else if(rocketRespawnTimer <= 0) {
			spawnRocket();
			rocketRespawnTimer = 2;
			countdownRocketRespawn = false;
		}
	}
	
	public void startRocketRespawnTimer() {
		countdownRocketRespawn = true;	
	}

	public void update(float delta) {
		if(asteroids.size() == 0) {
			for(int i = 0; i < 5; i++) {
				//newAsteroid();
			}
		}
		
		for(int i = 0; i < rockets.size(); i++) {
			rockets.get(i).update(delta);
		}
		
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(delta);
		}
		
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).update(delta);
		}
		
		for(int i = 0; i < ufos.size(); i++) {
			ufos.get(i).update(delta);
		}
		
		for(int i = 0; i < fragments.size(); i++) {
			fragments.get(i).update(delta);
		}
		
		for(int i = 0; i < sparks.size(); i++) {
			sparks.get(i).update(delta);
		}
		
		checkTimer(delta);
	}
	
	public void spawnFragments(float x, float y, int[] fillColour, int[] lineColour) {
		for(int i = 0; i < 3; i++) {
			Fragment fragment = new Fragment(x, y, fillColour, lineColour);
			fragments.add(fragment);
		}
	}
	
	private void newAsteroid() {
		float r = rand.nextInt(21) + 90;
		int hg = rand.nextInt(361);
		int v = rand.nextInt(101) + 150;		
		float w = Main.getWidth(), h = Main.getHeight();
		float x = 0, y = 0;
		
		switch(rand.nextInt(4)) {
		case 0: // Left
			x = -r;
			y = h * rand.nextFloat();
			break;
		case 1: // Right
			x = w + r;
			y = h * rand.nextFloat();
			break;
		case 2: // Top
			x = w * rand.nextFloat();
			y = h + r;
			break;
		case 3: // Bottom
			x = w * rand.nextFloat();
			y = -r;	
		}
		
		spawnAsteroid(this, x, y, r, v, hg);
	}
	
	public void spawnAsteroid(World world, float x, float y, float r, int v, int hg) {
		if(r >= 15) {
			Asteroid asteroid = new Asteroid(world , x, y, r, v, hg);
			asteroids.add(asteroid);
		}	
	}
	
	public void spawnMissile(char creator, float x, float y, double heading, int height, Vector2 velocity, int vMult, int[] colour) {
		Missile missile = new Missile(creator, x, y, heading, height, velocity, vMult, colour);
		missiles.add(missile);
	}
	
	public void spawnUFO() {
		int num = rand.nextInt(2);
		float x, y;
		if(num == 0) {
			x = 0;
		} else {
			x = Main.getWidth();
		}
		y = rand.nextInt(1251);
		UFO ufo = new UFO(this, x, y);
		ufos.add(ufo);
	}
	
	private void spawnRocket() {
		Rocket rocket = new Rocket(this);
		rockets.add(rocket);
	}
	
	public void spawnSparks(float x, float y) {
		for(int i = 0; i < rand.nextInt(11) + 25; i++) {
			Spark spark = new Spark(x, y);
			sparks.add(spark);
		}
	}
	
	public void removeRocket(int i) {
		rockets.remove(i);
	}
	
	public void removeAsteroid(int i) {
		asteroids.remove(i);
	}
	
	public void removeMissile(int i) {
		missiles.remove(i);
	}
	
	public void removeUFO(int i) {
		ufos.remove(i);
	}
	
	public void removeFragment(int i) {
		fragments.remove(i);
	}
	
	public void removeSpark(int i) {
		sparks.remove(i);
	}
	
	public ArrayList<Rocket> getRockets() {
		return rockets;
	}
	
	public Rocket getRocket(int i) {
		return rockets.get(i);
	}
	
	public int getNumRockets() {
		return rockets.size();
	}
	
	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}
	
	public Asteroid getAsteroid(int i) {
		return asteroids.get(i);
	}
	
	public int getNumAsteroids() {
		return asteroids.size();
	}
	
	public ArrayList<Missile> getMissiles() {
		return missiles;
	}
	
	public Missile getMissile(int i) {
		return missiles.get(i);
	}
	
	public int getNumMissiles() {
		return missiles.size();
	}
	
	public ArrayList<UFO> getUFOs() {
		return ufos;
	}
	
	public UFO getUFO(int i) {
		return ufos.get(i);
	}
	
	public int getNumUFOs() {
		return ufos.size();
	}
	
	public ArrayList<Fragment> getFragments() {
		return fragments;
	}
	
	public Fragment getFragment(int i) {
		return fragments.get(i);
	}
	
	public int getNumFragments() {
		return fragments.size();
	}
	
	public ArrayList<Spark> getSparks() {
		return sparks;
	}
	
	public Spark getSpark(int i) {
		return sparks.get(i);
	}
	
	public int getNumSparks() {
		return sparks.size();
	}
}