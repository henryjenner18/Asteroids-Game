package gameWorld;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import gameObjects.Asteroid;
import gameObjects.Rocket;
import main.AsteroidsMain;

public class GameRenderer { // Renders game objects
	
	private GameWorld myWorld;
	private OrthographicCamera cam; // Single plane camera view
	private ShapeRenderer shapeRenderer;
	
	private Texture spaceImage;
	private SpriteBatch batch;
	
	private static Rocket rocket; // Change back to un-static
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	
	
	// Test collision line
	public static float[] line = {540, 700, 450, 400};
	public static float mLine;
	public static float cLine;
	
	public static float[] rocketLine = new float[4];
	public static float mRocket;
	public static float cRocket;
	
	public GameRenderer(GameWorld world) {
		myWorld = world; // Initialise variable with GameWorld object received from GameScreen
		
		// Setting size of projection to resolution in Main and translating to move origin to bottom left
		cam = new OrthographicCamera(AsteroidsMain.getWidth(), AsteroidsMain.getHeight());
		cam.translate(AsteroidsMain.getWidth() / 2, AsteroidsMain.getHeight() / 2);
		cam.update();
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		rocket = myWorld.getRocket();
		
		numAsteroids = myWorld.getNumAsteroids();
		asteroids = new ArrayList<Asteroid>(numAsteroids);
		
		for(int i = 0; i < numAsteroids; i++) {
			asteroids.add(myWorld.getAsteroid(i));
		}
		
		spaceImage = new Texture("Infinite-space.jpg");
		batch = new SpriteBatch();
	}
	
	public void render() {
		drawBackground();		
		drawRocket();
		
		for(int i = 0; i < numAsteroids; i++) {
			//drawAsteroid(i);
		}
		
		drawLine();
		
		// Test collision line
		rocketLine[0] = rocket.getVertex(0);
		rocketLine[1] = rocket.getVertex(1);
		rocketLine[2] = rocket.getVertex(2);
		rocketLine[3] = rocket.getVertex(3);
		mLine = findGradient(line);
		cLine = findYintercept(line, mLine);
		mRocket = findGradient(rocketLine);
		cRocket = findYintercept(rocketLine, mRocket);
		System.out.println("Test line: y = " + mLine + "x + " + cLine);
		System.out.println("Rocket right-hand edge: y = " + mRocket + "x + " + cRocket);
		
		// Solve for x
		float x = (cRocket - cLine) / (mLine - mRocket);
		System.out.println(x);
		
		// Calculate y
		float y = mLine * x + cLine;
		
		System.out.println("Intersect at (" + x + ", " + y + ")");
		
		if(acceptableXcheck(x, line, rocketLine) == true) {
			// Collision
			System.out.println("Collision!!!");
			
			// Draw a pointer to show collision
			drawCollision(x, y);
		}
	}
	
	private void drawCollision(float x, float y) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 1, 1, 1);
		shapeRenderer.circle(x, y, 8);
		shapeRenderer.end();
	}
	
	private boolean acceptableXcheck(float x, float[] line, float[] rocketLine) {
		boolean testLineCheck = false;
		boolean rocketLineCheck = false;
		
		// Check if x and y are within range
		if(max2(line[0], line[2]) == line[0]) { //if test line's first x value is greater than its second
			if(x <= line[0] && x >= line[2]) {
				// Acceptable x for test line
				System.out.println("Acceptable x value for test line");
				testLineCheck = true;
			}
		} else { //test line's second x is greater than first
			if(x <= line[2] && x >= line[0]) {
				// Acceptable x for test line
				System.out.println("Acceptable x value for test line");
				testLineCheck = true;
			}
		}
				
		if(max2(rocketLine[0], rocketLine[2]) == rocketLine[0]) { //if rocket line's first x value is greater than its second
			if(x <= rocketLine[0] && x >= rocketLine[2]) {
				// Acceptable x for rocket line
				System.out.println("Acceptable x value for rocket line");
				rocketLineCheck = true;
			}
		} else { //rocket line's second x is greater than first
			if(x <= rocketLine[2] && x >= rocketLine[0]) {
				// Acceptable x for rocket line
				System.out.println("Acceptable x value for rocket line");
				rocketLineCheck = true;
			}
		}
		
		if(testLineCheck == true && rocketLineCheck == true) {
			// Acceptable x for both
			return true;
		} else {
			return false;
		}
	}
	
	private float max2(float a, float b) {
		if(a > b) return a;
		else return b;
	}
	
	private float findGradient(float[] l) {
		// y = mx + c
		// Find gradient m
		// m = d y / d x = (y1 - y2) / (x1 - x2)
				
		float m = (l[1] - l[3]) / (l[0] - l[2]); // Gradient of straight line
		
		return m;
	}
	
	private float findYintercept(float[] l, float m) {
		// Find y-intercept c
		// y - y1 = m(x - x1) so y = m(x - x1) + y1
		float c = (- m * l[0]) + l[1]; // Therefore c = (- m * x1) + y1
		
		return c;
	}
	
	private void drawLine() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.line(line[0], line[1], line[2], line[3]);
		shapeRenderer.end();
	}

	public void drawRocket() {
		Gdx.gl.glLineWidth(3);
		Gdx.gl.glEnable(GL20.GL_BLEND); // Allows transparency
		
		// Triangle
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(176/255.0f, 196/255.0f, 222/255.0f, 0.5f);
		//shapeRenderer.polygon(rocket.getVertices());
		shapeRenderer.triangle(rocket.getVertex(0), rocket.getVertex(1),
				rocket.getVertex(2), rocket.getVertex(3),
				rocket.getVertex(4), rocket.getVertex(5));
		shapeRenderer.end();
		
		// Outline
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);	
		shapeRenderer.triangle(rocket.getVertex(0), rocket.getVertex(1),
				rocket.getVertex(2), rocket.getVertex(3),
				rocket.getVertex(4), rocket.getVertex(5));
		shapeRenderer.end();
	}
	
	public void drawAsteroid(int i) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 1, 1);	
		shapeRenderer.polygon(asteroids.get(i).getVertices());
		shapeRenderer.end();
	}
	
	public void drawBackground() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(spaceImage, 0, 0, AsteroidsMain.getWidth(), AsteroidsMain.getHeight());
		batch.end();
	}
}
