package gameObjects;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameManagers.World;

public class Fragment extends SpaceObject {
	
	private int v, dr;
	final Vector2 objVelocity;
	private float timeLeft, rotation;
	private float[] angles;
	private float[] radii;
	Random rand;
	
	public Fragment(World world, float x, float y, float r, Vector2 objVelocity, int[] fillColour, int[] lineColour) {
		super(world);
		rand = new Random();
		position = new Vector2(x, y);
		velocity = new Vector2();
		this.objVelocity = new Vector2(objVelocity.x, objVelocity.y).scl((float) 0.3);
		heading = rand.nextInt(361);
		v = rand.nextInt(50) + 50;	
		edges = rand.nextInt(3)+3;
		angles = new float[edges];
		radii = new float[edges];
		vertices = new float[edges][2];
		this.r = r;
		rotation = 0;
		dr = rand.nextInt(31) - 10;
		setTimeLeft(randFloatInRange(1.5, 2));
		generateAngles();
		generateRadii();
		setColours(fillColour, lineColour);
	}
	
	public void update(float delta) {
		timeLeft -= delta;
		if(timeLeft <= 0) {
			setTimeLeft(0);
		}
		move(delta);
		position.add(velocity);
		rotate(delta);
		wrap();
		setVertices();
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);	
		velocity.x = MathUtils.cos(radians) * delta * v;
		velocity.y = MathUtils.sin(radians) * delta * v;		
		velocity.add(objVelocity);
	}
	
	private void rotate(float delta) {
		rotation += dr * delta;
	}
	
	private void setColours(int[] fillColour, int[] lineColour) {
		this.fillColour = fillColour;
		this.lineColour = lineColour;
	}

	private void setVertices() {
		float radians;
		
		for(int i = 0; i < edges; i ++) {
			radians = (float) Math.toRadians(angles[i] + rotation);
			
			// x-coordinate
			vertices[i][0] = position.x + MathUtils.cos(radians) * radii[i];
			
			// y-coordinate
			vertices[i][1] = position.y + MathUtils.sin(radians) * radii[i];
		}
	}

	private void generateRadii() {
		int diff = edges;
		double maxRadius = r + diff;
		double minRadius = r - diff;

		for(int i = 0; i < edges; i++) {
			double rd = (rand.nextInt((int) ((maxRadius - minRadius) + 1)) + minRadius);
			
			radii[i] = (float) rd;
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
	
	public void setTimeLeft(float t) {
		timeLeft = t;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
}
