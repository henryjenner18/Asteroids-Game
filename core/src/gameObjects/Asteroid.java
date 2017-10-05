package gameObjects;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Asteroid extends SpaceObject {
	
	private int size;
	
	private float[] angles;
	private float[] radii;
	
	private int maxRadius;
	private int minRadius;
	private int dv; // Alteration of velocity constant
	
	Random rand = new Random();
	
	public Asteroid(int s, float x, float y) {
		size = s;
		setProperties(size);
		
		position = new Vector2(x, y);
		
		velocity = new Vector2();
		heading = rand.nextInt(361);
		dv = rand.nextInt(31) + 40; // dv between 40 - 70
		
		angles = new float[edges];
		radii = new float[edges];
		vertices = new float[edges][2];
			
		generateAngles();
		generateRadii();
	}
	
	public void update(float delta) {
		move(delta);
		position.add(velocity);
		
		wrap();
		setVertices();
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);
		
		velocity.x = MathUtils.cos(radians) * delta * dv;
		velocity.y = MathUtils.sin(radians) * delta * dv;
	}
	
	private void wrap() { // Screen wrap
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();;
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
	}
	
	private void setProperties(int size) {
		int avgRadius = 0;
		if(size == 4) {
			avgRadius = 100;
			edges = 20;
		} else if(size == 3) {
			avgRadius = 50;
			edges = 15;
		} else if(size == 2) {
			avgRadius = 30;
			edges = 12;
		} else if(size == 1) {
			avgRadius = 15;
			edges = 10;
		}
		int diff = edges / 2;
		maxRadius = avgRadius + diff;
		minRadius = avgRadius - diff;
		r = maxRadius;
	}

	private void setVertices() {
		float radians;
		
		for(int i = 0; i < edges; i ++) {
			radians = (float) Math.toRadians(angles[i]);
			
			// x-coordinate
			vertices[i][0] = position.x + MathUtils.cos(radians) * radii[i];
			
			// y-coordinate
			vertices[i][1] = position.y + MathUtils.sin(radians) * radii[i];
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

	public void render(ShapeRenderer sr) {
		float[] polygon = new float[edges * 2]; // Shape renderer polygon function only takes in 1D array
		for(int i = 0; i < edges; i ++) {
			polygon[i*2] = vertices[i][0];
			polygon[(i*2)+1] = vertices[i][1];
		}
		sr.begin(ShapeType.Line);
		sr.setColor(Color.WHITE);	
		sr.polygon(polygon);
		sr.end();
	}
	
	public int getSize() {
		return size;
	}
}
