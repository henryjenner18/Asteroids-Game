package gameObjects;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import gameManagers.World;
import main.Main;

public class SpaceObject {
	
	protected World world;
	protected Vector2 position, velocity;
	protected float[][] vertices;
	protected float[] gradients, yIntercepts;
	protected int[] fillColour, lineColour, missileColour;
	protected double heading;
	protected int edges, missileV, score;
	protected float r;
	protected Random rand;
	
	public SpaceObject(World world) {
		this.world = world;
		missileV = 900;
		rand = new Random();
	}
	
	public void linearEquation() {
		gradients = new float[edges]; // Set to the number of edges in the polygon
		yIntercepts = new float[edges];
		
		for(int i = 0; i < edges; i++) {
			float x1, y1, x2, y2, m, c;
			
			x1 = vertices[i][0]; // Find 1st x-coordinate
			y1 = vertices[i][1]; // Find 1st y-coordinate
			
			if(i == edges - 1) { // Check to see if looking at the last vertex of the polygon
				// Need to compare against 1st vertex if we are currently checking the last vertex
				x2 = vertices[0][0]; // Set x2 to the x value of the first vertex
				y2 = vertices[0][1]; // Set x2 to the x value of the first vertex
				
			} else { // There are still vertices ahead of it to compare against in the array
				x2 = vertices[i+1][0];
				y2 = vertices[i+1][1];
			}
			
			// Find m of each edge	
			m = (y1 - y2) / (x1 - x2); // m = d y / d x = (y1 - y2) / (x1 - x2)
				
			gradients[i] = m; // Place it in the array
			
			// Find y-intercept c		
			c = (- m * x1) + y1; // y - y1 = m(x - x1) so y = m(x - x1) + y1; therefore c = (- m * x1) + y1
			yIntercepts[i] = c;
		}
	}
	
	protected void wrap() { // Screen wrap
		float w = Main.getWidth();
		float h = Main.getHeight();
		
		if(position.x < -r)	position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
	}
	
	protected float randFloatInRange(double min, double max) {
		float x = (float) (min + (rand.nextFloat() * (max - min)));
		
		return x;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getR() {
		return r;
	}
		
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public double getHeading() {
		return heading;
	}
	
	public float[][] getVertices() {
		return vertices;
	}
	
	public float[] getGradients() {
		return gradients;
	}
	
	public float[] getYintercepts() {
		return yIntercepts;
	}
	
	public int getEdges() {
		return edges;
	}
	
	public int[] getFillColour() {
		return fillColour;
	}
	
	public int[] getLineColour() {
		return lineColour;
	}
	
	public int[] getMissileColour() {
		return missileColour;
	}
	
	public int getMissileV() {
		return missileV;
	}
	
	public int getScore() {
		return score;
	}
}
