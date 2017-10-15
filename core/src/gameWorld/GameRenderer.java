package gameWorld;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import gameObjects.Asteroid;
import gameObjects.Missile;
import gameObjects.Particle;
import gameObjects.Rocket;
import main.AsteroidsMain;

public class GameRenderer { // Renders game objects
	
	private GameWorld myWorld;
	private CollisionDetector myManager;
	private OrthographicCamera cam; // Single plane camera view
	private ShapeRenderer shapeRenderer;
	
	private int numStars;
	private int[][] stars;
	
	private Rocket rocket;
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	private static int numMissiles;
	private static ArrayList<Missile> missiles;
	private static int numParticles;
	private static ArrayList<Particle> particles;
	
	public GameRenderer(GameWorld world, CollisionDetector manager) {
		myWorld = world; // Initialise variable with GameWorld object received from GameScreen
		myManager = manager;
		
		// Setting size of projection to resolution in Main and translating to move origin to bottom left
		cam = new OrthographicCamera(AsteroidsMain.getWidth(), AsteroidsMain.getHeight());
		cam.translate(AsteroidsMain.getWidth() / 2, AsteroidsMain.getHeight() / 2);
		cam.update();
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		rocket = myWorld.getRocket();
		
		numStars = 500;
		stars = new int[numStars][2];
		generateStars();
	}
	
	private void generateStars() {
		Random rand = new Random();
		for(int i = 0; i < numStars; i++) {
			int x = rand.nextInt(AsteroidsMain.getWidth());
			int y = rand.nextInt(AsteroidsMain.getHeight());
			stars[i][0] = x;
			stars[i][1] = y;
		}
	}

	public void render() {
		drawBackground();
		setParticles();
		setAsteroids();
		rocket.render(shapeRenderer);
		setMissiles();
		myManager.render(shapeRenderer);
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
	
	private void setParticles() {
		numParticles = myWorld.getNumParticles();
		particles = new ArrayList<Particle>(numParticles);
		
		for(int i = 0; i < numParticles; i++) {
			particles.add(myWorld.getParticle(i));
			particles.get(i).render(shapeRenderer);
		}
	}
	
	public void drawBackground() {
		Gdx.gl.glClearColor(10/255f, 10/255f, 10/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.setColor(Color.WHITE);
		for(int i = 0; i < numStars; i++) {
			shapeRenderer.point(stars[i][0], stars[i][1], 0);
		}
		shapeRenderer.end();
	}
}
