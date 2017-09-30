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
	private Texture explosionImage;
	private SpriteBatch batch;
	
	private Rocket rocket;
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	
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
		explosionImage = new Texture("explosion.png");
		batch = new SpriteBatch();
	}
	
	public void render() {
		drawBackground();		

		for(int i = 0; i < numAsteroids; i++) {
			drawAsteroid(i);
		}
		
		drawRocket();
		
		drawCollisions();
	}

	private void drawCollisions() {
		ArrayList<float[]> collisions = GameManager.getIntersections();
		
		/*if(collisions.size() > 0) { // An explosion
			int wh = 190; // Width and height
			batch.begin();
			batch.draw(explosionImage, collisions.get(0)[0] - (wh / 2), collisions.get(0)[1] - (wh / 2), wh, wh);
			batch.end();
		}*/
	
		for(int i = 0; i < collisions.size(); i++) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(1, 0, 0, 1);
			shapeRenderer.circle(collisions.get(i)[0], collisions.get(i)[1], 12);
			shapeRenderer.end();			
		}
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
		shapeRenderer.setColor(0, 1, 1, 1);	
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
