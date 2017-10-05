package gameWorld;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gameObjects.Asteroid;
import gameObjects.Missile;
import gameObjects.Rocket;
import main.AsteroidsMain;

public class GameRenderer { // Renders game objects
	
	private GameWorld myWorld;
	private GameManager myManager;
	private OrthographicCamera cam; // Single plane camera view
	private ShapeRenderer shapeRenderer;
	
	private Texture spaceImage;
	private Texture explosionImage;
	private SpriteBatch batch;
	
	private Rocket rocket;
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	private static int numMissiles;
	private static ArrayList<Missile> missiles;
	
	public GameRenderer(GameWorld world, GameManager manager) {
		myWorld = world; // Initialise variable with GameWorld object received from GameScreen
		myManager = manager;
		
		// Setting size of projection to resolution in Main and translating to move origin to bottom left
		cam = new OrthographicCamera(AsteroidsMain.getWidth(), AsteroidsMain.getHeight());
		cam.translate(AsteroidsMain.getWidth() / 2, AsteroidsMain.getHeight() / 2);
		cam.update();
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		rocket = myWorld.getRocket();
		
		spaceImage = new Texture("Infinite-space.jpg");
		explosionImage = new Texture("explosion.png");
		batch = new SpriteBatch();
	}
	
	public void render() {
		drawBackground();
		
		setAsteroids();
		rocket.render(shapeRenderer);
		setMissiles();
		
		myManager.render(shapeRenderer, batch, explosionImage);
	}
	
	private void setAsteroids() {
		numAsteroids = myWorld.getNumAsteroids();
		asteroids = new ArrayList<Asteroid>(numAsteroids);
		
		for(int i = 0; i < numAsteroids; i++) {
			asteroids.add(myWorld.getAsteroid(i));
			asteroids.get(i).linearEquation();
			asteroids.get(i).render(shapeRenderer);
		}
	}
	
	private void setMissiles() {
		numMissiles = myWorld.getNumMissiles();
		missiles = new ArrayList<Missile>(numMissiles);
		
		for(int i = 0; i < numMissiles; i++) {
			missiles.add(myWorld.getMissile(i));
			missiles.get(i).linearEquation();
			missiles.get(i).render(shapeRenderer);
		}
	}
	
	public void drawBackground() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(spaceImage, 0, 0, AsteroidsMain.getWidth(), AsteroidsMain.getHeight());
		batch.end();
	}
}
