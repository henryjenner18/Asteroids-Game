package gameObjects;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Fragment extends SpaceObject {
	
	private int v;
	private float timeLeft;
	private float[] angles;
	private float[] radii;
	private double avgRadius;
	Random rand;
	
	public Fragment(float rockX, float rockY, int[] fillColour, int[] lineColour) {
		rand = new Random();
		position = new Vector2(rockX, rockY);
		velocity = new Vector2();	
		heading = rand.nextInt(361);
		v = rand.nextInt(50) + 100;	
		edges = rand.nextInt(4)+3;
		angles = new float[edges];
		radii = new float[edges];
		vertices = new float[edges][2];
		avgRadius = 18;
		
		setTimeLeft((rand.nextFloat() * 1));
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
	
	private void wrap() { // Screen wrap
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();;
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
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
	
	public void render(ShapeRenderer sr) {
		// Filled Polygon
		for(int i = 0; i < edges; i++) {
			sr.begin(ShapeType.Filled);
			sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
			
			if(i == edges - 1) { // Final vertex - need to make triangle with the first vertex
				sr.triangle(vertices[i][0], vertices[i][1],
						vertices[0][0], vertices[0][1],
						position.x, position.y);
				sr.end();
			} else {
				sr.triangle(vertices[i][0], vertices[i][1],
						vertices[i+1][0], vertices[i+1][1],
						position.x, position.y);
				sr.end();
			}
		}
		
		// Polygon outline
		float[] polygon = new float[edges * 2]; // Shape renderer polygon function only takes in 1D array
		for(int i = 0; i < edges; i ++) {
			polygon[i*2] = vertices[i][0];
			polygon[(i*2)+1] = vertices[i][1];
		}
		Gdx.gl.glLineWidth(4);
		sr.begin(ShapeType.Line);
		sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
		sr.polygon(polygon);
		sr.end();
	}
	
	public void setTimeLeft(float f) {
		timeLeft = f;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
}
