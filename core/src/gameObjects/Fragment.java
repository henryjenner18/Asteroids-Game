package gameObjects;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Fragment extends SpaceObject {
	
	private int v;
	private float timeLeft;
	private float[] angles;
	private float[] radii;
	private double avgRadius;
	Random rand;
	
	public Fragment(float x, float y, int[] fillColour, int[] lineColour) {
		rand = new Random();
		position = new Vector2(x, y);
		velocity = new Vector2();	
		heading = rand.nextInt(361);
		v = rand.nextInt(50) + 100;	
		edges = rand.nextInt(3)+3;
		angles = new float[edges];
		radii = new float[edges];
		vertices = new float[edges][2];
		avgRadius = 18;
		setTimeLeft(randFloatInRange(1.5, 2));
		generateAngles();
		generateRadii();
		setColours(fillColour, lineColour);
	}
	
	private void setColours(int[] fillColour, int[] lineColour) {
		this.fillColour = fillColour;
		this.lineColour = lineColour;
	}
	
	public void update(float delta) {
		timeLeft -= delta;
		if(timeLeft <= 0) {
			setTimeLeft(0);
		}
		move(delta);
		position.add(velocity);
		wrap();
		setVertices(); // Alter coordinates
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);		
		velocity.x = MathUtils.cos(radians) * delta * v;
		velocity.y = MathUtils.sin(radians) * delta * v;
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
		int diff = edges;
		double maxRadius = avgRadius + diff;
		double minRadius = avgRadius - diff;

		for(int i = 0; i < edges; i++) {
			double r = (rand.nextInt((int) ((maxRadius - minRadius) + 1)) + minRadius);
			
			radii[i] = (float) r;
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
	
	public void setTimeLeft(float f) {
		timeLeft = f;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
}
