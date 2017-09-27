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

public class GameRenderer { //Renders game objects
	
	private Texture spaceImage;
	private SpriteBatch batch;
	
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	
	private Rocket rocket;
	
	private static int numAsteroids;
	private static ArrayList<Asteroid> asteroids;
	
	float x = -200;
	
	public GameRenderer(GameWorld world) {
		myWorld = world;
		
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
	}
	
	public void drawRocket() {
		Gdx.gl.glLineWidth(3);
		
		//triangle
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(176/255.0f, 196/255.0f, 222/255.0f, 1);	
		//shapeRenderer.polygon(rocket.getVertices());
		shapeRenderer.triangle(rocket.getVertex(0), rocket.getVertex(1),
				rocket.getVertex(2), rocket.getVertex(3),
				rocket.getVertex(4), rocket.getVertex(5));
		shapeRenderer.end();
		
		//outline
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 1, 1, 1);	
		shapeRenderer.triangle(rocket.getVertex(0), rocket.getVertex(1),
				rocket.getVertex(2), rocket.getVertex(3),
				rocket.getVertex(4), rocket.getVertex(5));
		shapeRenderer.end();
		
		//window
		/*shapeRenderer.begin(ShapeType.Filled);	
		shapeRenderer.circle(rocket.getX(), rocket.getY(), 12);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(135/255.0f, 206/255.0f, 235/255.0f, 1);
		shapeRenderer.circle(rocket.getX(), rocket.getY(), 8);
		shapeRenderer.end();*/
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
		batch.draw(spaceImage, x, 0, 2000, 1600);
		batch.end();
		
		x += 0.05;
	}
}
