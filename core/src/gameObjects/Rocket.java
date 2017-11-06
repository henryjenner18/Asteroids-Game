package gameObjects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameWorld.GameWorld;
import main.AsteroidsMain;

public class Rocket extends SpaceObject {
	
	GameWorld myWorld;
	
	private float[][] flame;
	private static int height;
	private int terminalVel;
	private int numFragments;
	
	public boolean thrusting;
	public boolean left;
	public boolean right;
	private boolean fl;
	Random rand;
	
	public Rocket(GameWorld world) {
		rand = new Random();
		myWorld = world;
		position = new Vector2();
		velocity = new Vector2();
		height = 90;
		r = height / 2;
		heading = 90;
		vertices = new float[4][2]; // 4 vertices with a pair of x and y coordinates each
		flame = new float[3][2];
		fl = false;
		edges = vertices.length;
		terminalVel = 10;
		reset();
	}
	
	public void reset() {
		position.x = AsteroidsMain.getWidth() / 2;
		position.y = AsteroidsMain.getHeight() / 2;
		velocity.x = 0;
		velocity.y = 0;
		heading = 90;
		numFragments = rand.nextInt(2) + 3;
	}
	
	public void update(float delta) {
		if(thrusting) { // If up key is being pressed
			thrust(delta); // Run thrust method to apply force
			fl = !fl;
		} else {
			fl = false;
		}
		
		asteroidsG(delta);
		terminalVelCheck(); // Adjust velocity if resultant is exceeding terminal velocity
		
		position.add(velocity); // Add velocity to rocket's position
		
		int dh = 4; // Change of heading when key pressed
		if(left) heading += dh;
		if(right) heading -= dh;
		
		wrap();	// Check if rocket has hit edges
		setVertices(); // Alter coordinates
	}
	
	private void asteroidsG(float delta) {
		int numAsteroids = myWorld.getNumAsteroids();
		ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>(numAsteroids);
		
		for(int a = 0; a < numAsteroids; a++) {
			asteroids.add(myWorld.getAsteroid(a));
			Vector2 gForce = new Vector2();
			Vector2 asteroid = new Vector2(asteroids.get(a).getX(), asteroids.get(a).getY());
			
			/*float dotProduct = (position.x * asteroid.x) + (position.y * asteroid.y);
			double magR = Math.sqrt((Math.pow(position.x, 2) + (Math.pow(position.y, 2))));
			double magA = Math.sqrt((Math.pow(asteroid.x, 2) + (Math.pow(asteroid.y, 2))));
			
			double x = dotProduct / (magR * magA);		
			double a = Math.acos(x) * (180 / Math.PI);
			
			System.out.println(a);*/
			
			float i = asteroid.x - position.x;
			float j = asteroid.y - position.y;
			
			double theta = Math.atan(j / i) * (180 / Math.PI);
			if(position.x > asteroid.x) {
				theta += 180;
			}
			System.out.println(theta);
			
			float radians = (float) Math.toRadians(theta);
			double f = 0.3;
			gForce.x = (float) (MathUtils.cos(radians) * delta * f);
			gForce.y = (float) (MathUtils.sin(radians) * delta * f);
			velocity.add(gForce);
		}
	}

	private void thrust(float delta) {
		Vector2 force = new Vector2();
		float radians = (float) Math.toRadians(heading);
		
		force.x = MathUtils.cos(radians) * delta * 10;
		force.y = MathUtils.sin(radians) * delta * 10;
		
		velocity.add(force);	
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
		
		vertices[2][0] = position.x - MathUtils.cos(radians) * (height) / 6;
		vertices[2][1] = position.y - MathUtils.sin(radians) * (height) / 6;
				
		vertices[3][0] = position.x + MathUtils.cos(radians - 3 * MathUtils.PI / 4) * height / 3;
		vertices[3][1] = position.y + MathUtils.sin(radians - 3 * MathUtils.PI / 4) * height / 3;
		
		setFire();
	}
	
	private void setFire() {
		float radians;
		radians = (float) Math.toRadians(heading - 90);
		flame[0][0] = position.x + MathUtils.cos(radians) * height / 6;
		flame[0][1] = position.y + MathUtils.sin(radians) * height / 6;
		
		radians = (float) Math.toRadians(heading + 90);
		flame[1][0] = position.x + MathUtils.cos(radians) * height / 6;
		flame[1][1] = position.y + MathUtils.sin(radians) * height / 6;
		
		radians = (float) Math.toRadians(heading + 180);
		flame[2][0] = position.x + MathUtils.cos(radians) * height / 2;
		flame[2][1] = position.y + MathUtils.sin(radians) * height / 2;
	}
	
	public void render(ShapeRenderer sr) {
		//Gdx.gl.glEnable(GL20.GL_BLEND); // Allows transparency	
		// Draw flame
		if(fl == true) {
			// Filled flame
			sr.begin(ShapeType.Filled);
			sr.setColor(1, 128/255f, 0, 1);
			sr.triangle(flame[0][0], flame[0][1],
					flame[1][0], flame[1][1],
					flame[2][0], flame[2][1]);
			sr.end();
			
			// Flame outline
			float[] polygon = new float[flame.length * 2]; // Shape renderer polygon function only takes in 1D array
			for(int i = 0; i < flame.length; i ++) {
				polygon[i*2] = flame[i][0];
				polygon[(i*2)+1] = flame[i][1];
			}
			Gdx.gl.glLineWidth(4);
			sr.begin(ShapeType.Line);
			sr.setColor(1, 1, 0, 1);
			sr.polygon(polygon);
			sr.end();
		}
				
		// Filled polygon
		sr.begin(ShapeType.Filled);
		sr.setColor(60/255f, 200/255f, 255/255f, 0.5f);
		sr.triangle(vertices[0][0], vertices[0][1],
				vertices[1][0], vertices[1][1],
				vertices[2][0], vertices[2][1]);
		sr.triangle(vertices[0][0], vertices[0][1],
				vertices[3][0], vertices[3][1],
				vertices[2][0], vertices[2][1]);
		sr.end();
		
		// Polygon outline
		float[] polygon = new float[edges * 2]; // Shape renderer polygon function only takes in 1D array
		for(int i = 0; i < edges; i ++) {
			polygon[i*2] = vertices[i][0];
			polygon[(i*2)+1] = vertices[i][1];
		}
		Gdx.gl.glLineWidth(4);
		sr.begin(ShapeType.Line);
		sr.setColor(Color.MAROON);
		sr.polygon(polygon);
		sr.end();
	}
	
	public static int getHeight() {
		return height;
	}
	
	public int getNumFragments() {
		return numFragments;
	}
}
