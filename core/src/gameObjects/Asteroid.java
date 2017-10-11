package gameObjects;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Asteroid extends SpaceObject {
	
	private float[] angles;
	private float[] radii;
	
	private double avgRadius;
	private double area;
	
	private int v; // Alteration of velocity constant
	
	Random rand = new Random();
	
	public Asteroid(float x, float y, double newR, int v) {
		avgRadius = newR;		
		setProperties();
		
		position = new Vector2(x, y);	
		velocity = new Vector2();
		heading = rand.nextInt(361);
		
		this.v = v;
		
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
	
	private void setProperties() {
		area = Math.PI * Math.pow(avgRadius, 2);
		
		if(avgRadius < 30) { edges = 12; }
		if(avgRadius >= 30) { edges = 14; }
		if(avgRadius >= 40) { edges = 15; }
		if(avgRadius >= 50) { edges = 17; }
		if(avgRadius >= 60) { edges = 19; }
		if(avgRadius >= 70) { edges = 20; }
		if(avgRadius >= 90) { edges = 22; }
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
		int diff = edges / 2;
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
		/*for(int i = 0; i < edges; i++) {
			sr.begin(ShapeType.Filled);
			sr.setColor(180/255f, 180/255f, 180/255f, 0);
			
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
		}*/
		
		// Polygon outline
		float[] polygon = new float[edges * 2]; // Shape renderer polygon function only takes in 1D array
		for(int i = 0; i < edges; i ++) {
			polygon[i*2] = vertices[i][0];
			polygon[(i*2)+1] = vertices[i][1];
		}
		Gdx.gl.glLineWidth(5);
		sr.begin(ShapeType.Line);
		//sr.setColor(100/255f, 100/255f, 100/255f, 0);	
		sr.polygon(polygon);
		sr.end();
	}
	
	public double getArea() {
		return area;
	}
	
	public int getV() {
		return v;
	}
}
