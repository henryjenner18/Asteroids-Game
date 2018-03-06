package gameManagers;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import gameObjects.Asteroid;
import gameObjects.Fragment;
import gameObjects.Missile;
import gameObjects.PowerUp;
import gameObjects.Rocket;
import gameObjects.Shield;
import gameObjects.Spark;
import gameObjects.UFO;
import main.Main;

public class ObjectSpawner {
	
	private World world;
	private Random rand;
	
	public ObjectSpawner(World world) {
		this.world = world;
		rand = new Random();
	}
	
	void rocket(float delta) {
		if(world.rocketSpawnTimer > 0) {
			world.rocketSpawnTimer -= delta;
			
		} else {
			Rocket rocket = new Rocket(world);
			world.rockets.add(rocket);
			world.resetRocketSpawnTimer();
			world.respawn = false;
				
			if(world.getLevel() != 0) {
				world.rockets.get(0).setInvincible(true);
			}
		}
	}
	
	public void newAsteroid() {
		float r = rand.nextInt(21) + 90;
		int hg = rand.nextInt(361);
		int v = rand.nextInt(81) + 130;
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
		
		asteroid(world, x, y, r, v, hg);
	}
	
	public void asteroid(World world, float x, float y, float r, int v, int hg) {
		if(r >= 20) {
			Asteroid asteroid = new Asteroid(world , x, y, r, v, hg);
			world.asteroids.add(asteroid);
		}	
	}
	
	public void newUFO() {
		if(world.getNumUFOs() < 4) {
			float x, y;
			int num = rand.nextInt(2);
			
			if(num == 0) {
				x = 0;
			} else {
				x = Main.getWidth();
			}
			y = rand.nextInt(1251);
			
			UFO ufo = new UFO(world, x, y, false);
			world.ufos.add(ufo);
		}
	}
	
	public void ufoDaughter(float x, float y) {
		if(world.getNumUFOs() < 4) {	
			UFO ufo = new UFO(world, x, y, true);
			world.ufos.add(ufo);
		}
	}
	
	public void missile(char creator, int num, float x, float y, double heading, int height, Vector2 velocity, int vMult, int[] colour) {
		int inc = 3;
		int dh = 0;
		double initHeading, newHeading;
		initHeading = newHeading = heading;
		
		for(int i = 0; i < num; i++) {
			Missile missile = new Missile(world, creator, x, y, newHeading, height, velocity, vMult, colour);
			world.missiles.add(missile);
			
			dh *= -1;
			
			if(i % 2 == 0) {
				dh += inc;
			}
			
			newHeading = initHeading + dh;
		}	
	}
	
	public void fragments(float x, float y, float r, Vector2 objVelocity, int[] fillColour, int[] lineColour) {
		for(int i = 0; i < 4; i++) {
			Fragment fragment = new Fragment(world, x, y, r, objVelocity, fillColour, lineColour);
			world.fragments.add(fragment);
		}
	}
	
	public void sparks(float x, float y, boolean missilesOnly) {
		for(int i = 0; i < rand.nextInt(11) + 25; i++) {
			Spark spark = new Spark(world, x, y, missilesOnly);
			world.sparks.add(spark);
		}
	}
	
	public void powerUp(float x, float y) {
		PowerUp powerUp = new PowerUp(world, x, y);
		world.powerUps.add(powerUp);
	}
	
	public void shield() {
		if(world.getNumShields() > 0) {
			world.getShield(0).resetTimeLeft();
			
		} else {
			Shield shield = new Shield(world);
			world.shields.add(shield);
			world.getRocket(0).setShield(true);
		}
	}
}
