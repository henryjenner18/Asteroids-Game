package gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Rocket extends SpaceObject {
	
	private static Vector2 position;
	private static Vector2 velocity;
	private static int heading;
	private static int height;
	private int terminalVel;
	
	public boolean thrusting;
	public boolean left;
	public boolean right;
	
	public Rocket(int ht, int hg, float x, float y) {
		
		position = new Vector2(x, y);
		velocity = new Vector2();
		height = ht;
		r = height / 2;
		heading = hg;
		vertices = new float[3][2]; // 3 vertices with a pair of x and y coordinates each
		edges = vertices.length;
		terminalVel = 8;
	}
	
	public void update(float delta) {
		if(thrusting) { // If up key is being pressed
			thrust(delta); // Run thrust method to apply force
		}
		position.add(velocity); // Add velocity to rocket's position
		
		int dh = 4; // Change of heading when key pressed
		if(left) heading += dh;
		if(right) heading -= dh;
		
		wrap();	// Check if rocket has hit edges
		setVertices(); // Alter coordinates
	}
	
	private void thrust(float delta) {
		Vector2 force = new Vector2();
		float radians = (float) Math.toRadians(heading);
		
		force.x = MathUtils.cos(radians) * delta * 10;
		force.y = MathUtils.sin(radians) * delta * 10;
		
		velocity.add(force);
		
		terminalVelCheck(); // Adjust velocity if resultant is exceeding terminal velocity
	}
	
	private void terminalVelCheck() {
		double resultantVel = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2)); // Pythagoras' theorem
		
		if(resultantVel > terminalVel) {
			velocity.x = (float) ((velocity.x / resultantVel) * terminalVel); // Reduce x and y components
			velocity.y = (float) ((velocity.y / resultantVel) * terminalVel);
		}
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
		float radians = (float) Math.toRadians(heading);
		
		vertices[0][0] = position.x + MathUtils.cos(radians) * height / 2;
		vertices[0][1] = position.y + MathUtils.sin(radians) * height / 2;
		
		vertices[1][0] = position.x + MathUtils.cos(radians + 3 * MathUtils.PI / 4) * height / 3;
		vertices[1][1] = position.y + MathUtils.sin(radians + 3 * MathUtils.PI / 4) * height / 3;
				
		vertices[2][0] = position.x + MathUtils.cos(radians - 3 * MathUtils.PI / 4) * height / 3;
		vertices[2][1] = position.y + MathUtils.sin(radians - 3 * MathUtils.PI / 4) * height / 3;
		
	}
	
	public void render(ShapeRenderer sr) {
		Gdx.gl.glLineWidth(3);
		Gdx.gl.glEnable(GL20.GL_BLEND); // Allows transparency
		
		// Triangle
		sr.begin(ShapeType.Filled);
		sr.setColor(176/255.0f, 196/255.0f, 222/255.0f, 0.5f);
		sr.triangle(vertices[0][0], vertices[0][1], // Vertex 0
				vertices[1][0], vertices[1][1], // Vertex 1
				vertices[2][0], vertices[2][1]); // Vertex 2
		sr.end();
		
		// Outline
		sr.begin(ShapeType.Line);
		sr.setColor(0, 1, 1, 1);	
		sr.triangle(vertices[0][0], vertices[0][1], // Vertex 0
				vertices[1][0], vertices[1][1], // Vertex 1
				vertices[2][0], vertices[2][1]); // Vertex 2
		sr.end();
	}
	
	public static float getX() {
		return position.x;
	}
		
	public static float getY() {
		return position.y;
	}
	
	public static Vector2 getVelocity() {
		return velocity;
	}
	
	public static int getHeading() {
		return heading;
	}
	
	public static int getHeight() {
		return height;
	}
}
