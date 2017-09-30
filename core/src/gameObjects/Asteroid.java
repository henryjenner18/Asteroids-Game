package gameObjects;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Asteroid extends SpaceObject {
	
	private float[] angles;
	private float[] radii;
	
	private float maxRadius;
	//private float minRadius;
	
	Random rand = new Random();
	
	public Asteroid(int n, int max) {
		edges = n;
		maxRadius = max;
		//minRadius = maxRadius - (maxRadius * 1 / 2);
		
		int x = rand.nextInt(AsteroidsMain.getWidth() + 1);
		int y = rand.nextInt(AsteroidsMain.getHeight() + 1);	
		position = new Vector2(x, y);
		
		int velX = rand.nextInt(9) - 4;
		int velY = rand.nextInt(9) - 4;
		velocity = new Vector2(velX, velY); // Need to use delta
		velocity.scl((float) 0.2);
		
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
		
		//linearEquation(vertices);
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
		
		for(int i = 0; i < edges * 2; i += 2) { //search for every even element/x-coordinate
			// x-coordinate
			radians = (float) Math.toRadians(angles[i / 2]);
			vertices[i] = position.x + MathUtils.cos(radians) * radii[i / 2];
			
			// y-coordinate
			vertices[i+1] = position.y + MathUtils.sin(radians) * radii[i / 2];
		}
	}

	private void generateRadii() {
		int r = 100; // starting radius
		
		for(int i = 0; i < edges; i++) {
			int dr = rand.nextInt(21) - 10;
			r += dr;
			
			radii[i] = r;
			
			
			
			/*float r = rand.nextInt((int) (maxRadius + 1 - minRadius)) + minRadius;
			radii[i] = r;*/
		}
	}
	
	private void generateAngles() {
		float a = 360 / edges; // regular difference between each angle
		
		for(int i = 0; i < edges; i++) {
			angles[i] = a * i;
		}
		
		/*int maxD = 10;
		int minD = -(maxD);
		
		for(int i = 0; i < edges; i++) {
			int d = rand.nextInt(maxD + 1 - minD) + minD;
			angles[i] = (regularDiff * i) + d;
		}*/
		
	}
}
