package gameManagers;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import gameObjects.Asteroid;
import gameObjects.Fragment;
import gameObjects.Missile;
import gameObjects.PowerUp;
import gameObjects.Rocket;
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
			boolean clear = true;
			
			int w = Main.getWidth(); // replace with global variable
			int h = Main.getHeight();
			
			float fw = w / 3;
			float fh = h / 3;
			
			for(int i = 0; i < world.asteroids.size(); i++) {
				float x = world.asteroids.get(i).getX();
				float y = world.asteroids.get(i).getY();
						
				if(x > fw && x < w - fw) {
					if(y > fh && y < h - fh) {
						clear = false;
					}
				}
			}
			
			for(int u = 0; u < world.ufos.size(); u++) {
				float x = world.ufos.get(u).getX();
				float y = world.ufos.get(u).getY();
						
				if(x > fw && x < w - fw) {
					if(y > fh && y < h - fh) {
						clear = false;
					}
				}
			}
			
			if(clear == true) {
				Rocket rocket = new Rocket(world);
				world.rockets.add(rocket);
				world.resetRocketSpawnTimer();
				world.respawn = false;
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
	
	public void ufo() {
		int num = rand.nextInt(2);
		float x, y;
		if(num == 0) {
			x = 0;
		} else {
			x = Main.getWidth();
		}
		y = rand.nextInt(1251);
		UFO ufo = new UFO(world, x, y);
		world.ufos.add(ufo);
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
	
	public void sparks(float x, float y) {
		for(int i = 0; i < rand.nextInt(11) + 25; i++) {
			Spark spark = new Spark(world, x, y);
			world.sparks.add(spark);
		}
	}
	
	public void powerUp(float x, float y) {
		PowerUp powerUp = new PowerUp(world, x, y);
		world.powerUps.add(powerUp);
	}
}
