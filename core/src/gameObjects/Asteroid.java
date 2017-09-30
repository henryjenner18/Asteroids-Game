package gameObjects;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Asteroid extends SpaceObject {
	
	private float[] angles;
	private float[] radii;
	
	private int maxRadius;
	private int minRadius;
	
	Random rand = new Random();
	
	public Asteroid(int n, int avgRadius) {
		
		int x = rand.nextInt(AsteroidsMain.getWidth() + 1);
		int y = rand.nextInt(AsteroidsMain.getHeight() + 1);	
		position = new Vector2(x, y);
		
		int velX = rand.nextInt(9) - 4;
		int velY = rand.nextInt(9) - 4;
		velocity = new Vector2(velX, velY); // Need to use delta
		velocity.scl((float) 0.3);
		
		edges = 20; //n
		int diff = 10;
		maxRadius = avgRadius + diff;
		minRadius = avgRadius - diff;
		
		angles = new float[edges];
		radii = new float[edges];
		vertices = new float[edges * 2]; // 2 elements needed for each vertex
		
		generateAngles();
		generateRadii();
	}
	
	public void update(float delta) {
		position.add(velocity);
		
		wrap();
		setVertices();
	}
	
	private void wrap() { //screen wrap
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();
		float r = maxRadius;
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;		
	}

	private void setVertices() {
		float radians;
		
		for(int i = 0; i < edges * 2; i += 2) { // Search for every even element/x-coordinate
			// x-coordinate
			radians = (float) Math.toRadians(angles[i / 2]);
			vertices[i] = position.x + MathUtils.cos(radians) * radii[i / 2];
			
			// y-coordinate
			vertices[i+1] = position.y + MathUtils.sin(radians) * radii[i / 2];
		}
	}

	private void generateRadii() {
		for(int i = 0; i < edges; i++) {
			int r = rand.nextInt((maxRadius - minRadius) + 1) + minRadius;
			
			radii[i] = r;
		}
	}
	
	private void generateAngles() {
		float a = 360 / edges; // Regular difference between each angle
		
		for(int i = 0; i < edges; i++) {
			int da = rand.nextInt(11) - 5; // Generate number between -5, 5
			angles[i] = (a * i) + da; // Add change to offset angles a little
		}
		
		Arrays.sort(angles); // Order the angles ascending
	}
}
