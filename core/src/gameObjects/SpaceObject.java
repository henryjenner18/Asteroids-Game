package gameObjects;

import com.badlogic.gdx.math.Vector2;

public class SpaceObject {
	
	protected Vector2 position;
	protected Vector2 velocity;
	protected int heading;
	protected int r;
	
	protected float[][] vertices;
	protected int edges; // Also identical to number of vertices; vertices[0].length
	
	private float[] gradients; // Array for gradients of the edges
	private float[] yIntercepts; // Array for y-intercepts of the edges
	
	public void linearEquation() {
		gradients = new float[edges]; // Set to the number of edges in the polygon
		yIntercepts = new float[edges];
		
		for(int i = 0; i < edges; i++) {
			float x1, y1, x2, y2, m, c;
			
			x1 = vertices[i][0]; // Set x1 to i
			y1 = vertices[i][1]; // y1 is the element after x1
			
			if(i == edges - 1) { // Check to see if looking at the last vertex of the polygon
				// Need to compare against the first vertex if we are currently checking the last vertex
				x2 = vertices[0][0]; // Set x2 to the x value of the first vertex
				y2 = vertices[0][1]; // Set x2 to the x value of the first vertex
				
			} else { // There are still vertices ahead of it to compare against in the array
				x2 = vertices[i+1][0];
				y2 = vertices[i+1][1];
			}
			
			// Find m of each edge
			// m = d y / d x = (y1 - y2) / (x1 - x2)
			m = (y1 - y2) / (x1 - x2);
			
			// Place it in the array
			gradients[i] = m;
			
			// Find y-intercept c
			// y - y1 = m(x - x1) so y = m(x - x1) + y1; therefore c = (- m * x1) + y1
			c = (- m * x1) + y1;
			yIntercepts[i] = c;
		}
	}
	
	public float[][] getVertices() {
		return vertices;
	}
	
	public float getVertex(int i) {
		return vertices[i];
	}
	
	public int getEdges() {
		return edges;
	}
	
	public float[] getGradients() {
		return gradients;
	}
	
	public float[] getYintercepts() {
		return yIntercepts;
	}
}
