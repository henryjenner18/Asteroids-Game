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
		heading = hg;
		vertices = new float[6];
		edges = vertices.length / 2;
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
		
		force.x = MathUtils.cos(radians) * delta * 9;
		force.y = MathUtils.sin(radians) * delta * 9;
		
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
		float r = height / 2;
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
	}
	
	private void setVertices() {
		float radians = (float) Math.toRadians(heading);
		
		vertices[0] = position.x + MathUtils.cos(radians) * height / 2;
		vertices[1] = position.y + MathUtils.sin(radians) * height / 2;
		
		vertices[2] = position.x + MathUtils.cos(radians - 3 * MathUtils.PI / 4) * height / 3;
		vertices[3] = position.y + MathUtils.sin(radians - 3 * MathUtils.PI / 4) * height / 3;
		
		//vertices[4] = position.x + MathUtils.cos(radians + MathUtils.PI) * height / 4;
		//vertices[5] = position.y + MathUtils.sin(radians + MathUtils.PI) * height / 4;
		
		vertices[4] = position.x + MathUtils.cos(radians + 3 * MathUtils.PI / 4) * height / 3;
		vertices[5] = position.y + MathUtils.sin(radians + 3 * MathUtils.PI / 4) * height / 3;
	}
	
	public void render(ShapeRenderer sr) {
		Gdx.gl.glLineWidth(3);
		Gdx.gl.glEnable(GL20.GL_BLEND); // Allows transparency
		
		// Triangle
		sr.begin(ShapeType.Filled);
		sr.setColor(176/255.0f, 196/255.0f, 222/255.0f, 0.5f);
		//shapeRenderer.polygon(rocket.getVertices());
		sr.triangle(vertices[0], vertices[1], vertices[2],
				vertices[3], vertices[4], vertices[5]);
		sr.end();
		
		// Outline
		sr.begin(ShapeType.Line);
		sr.setColor(0, 1, 1, 1);	
		sr.triangle(vertices[0], vertices[1], vertices[2],
				vertices[3], vertices[4], vertices[5]);
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
