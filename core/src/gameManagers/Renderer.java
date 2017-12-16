package gameManagers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameHelpers.AssetLoader;
import main.Main;

public class Renderer {
	
	private World world;
	private OrthographicCamera cam;
	private ShapeRenderer sr;
	private SpriteBatch batch;
	private int[][] stars;
	private int w = Main.getWidth();
	private int h = Main.getHeight();
	
	public Renderer(World world) {
		this.world = world;
		cam = new OrthographicCamera(w, h);
		cam.translate(w / 2, h / 2);
		cam.update();
		sr = new ShapeRenderer();
		sr.setProjectionMatrix(cam.combined);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		generateStars(500);
	}

	public void render() {
		drawBackground();
		drawSparks();
		drawFragments();
		drawAsteroids();
		drawMissiles();
		drawUFOs();
		drawRockets();
		drawGameStats();
		drawGameStateInfo();
	}
	
	private void drawGameStateInfo() {
		batch.begin();
		GlyphLayout layout = new GlyphLayout();
		
		if(world.isReady()) {
			String str = "Press any key to play";
			
			layout.setText(AssetLoader.font, str);
			float width = layout.width;
			float height = layout.height;
			
			float x = (w / 2) - (width / 2);
			float y = (h / 2) + (height / 2);
			
			AssetLoader.font.draw(batch, str, x, y);			
		}
		
		if(world.isGameOver()) {
			String str = "Game Over!";
			
			layout.setText(AssetLoader.font, str);
			float width = layout.width;
			float height = layout.height;
			
			float x = (w / 2) - (width / 2);
			float y = (h / 2) + (height / 2);
			
			AssetLoader.font.draw(batch, str, x, y);			
		}
		
		batch.end();
	}

	private void drawLives(float scoreWidth) {
		int lives = world.getLives();
		int height = 80;
		int r = height / 2;
		Vector2 position = new Vector2(scoreWidth + r, h - height / 2 - 10);
		
		for(int i = 0; i < lives; i++) {
			float vertices[][] = new float[4][2];
			
			vertices[0][0] = position.x;
			vertices[0][1] = position.y + (height / 2);
			
			vertices[1][0] = position.x + MathUtils.cos(5 * MathUtils.PI / 4) * height / 3;
			vertices[1][1] = position.y + MathUtils.sin(5 * MathUtils.PI / 4) * height / 3;
			
			vertices[2][0] = position.x;
			vertices[2][1] = position.y - (height / 6);
					
			vertices[3][0] = position.x + MathUtils.cos(- MathUtils.PI / 4) * height / 3;
			vertices[3][1] = position.y + MathUtils.sin(- MathUtils.PI / 4) * height / 3;
			
			sr.begin(ShapeType.Filled);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			sr.setColor(255/255f, 255/255f, 255/255f, 0.5f);
			sr.triangle(vertices[0][0], vertices[0][1],
					vertices[1][0], vertices[1][1],
					vertices[2][0], vertices[2][1]);
			sr.triangle(vertices[0][0], vertices[0][1],
					vertices[3][0], vertices[3][1],
					vertices[2][0], vertices[2][1]);
			sr.end();
			
			float polygon[] = polygonArray(vertices, 4);
			Gdx.gl.glLineWidth(3);
			sr.begin(ShapeType.Line);
			sr.setColor(1, 1, 1, 1);
			sr.polygon(polygon);
			sr.end();
			
			position.x += r + 10;
		}
	}
	
	private void drawGameStats() {
		if(world.isRunning() || world.isGameOver()) {
			batch.begin();
			GlyphLayout layout = new GlyphLayout();
			
			// Score
			int sc = world.getScore();
			String score = sc + "";
			
			layout.setText(AssetLoader.font, score);
			float scoreWidth = layout.width;
			
			AssetLoader.font.draw(batch, score, 10, h - 10);
			
			// Level
			int lvl = world.getLevel();
			String level = "Lvl " + lvl;
			
			layout.setText(AssetLoader.font, level);
			float levelWidth = layout.width;
	
			AssetLoader.font.draw(batch, level, w - levelWidth - 10, h - 10);
	
			batch.end();
			
			drawLives(scoreWidth);
		}
	}
	
	private void drawSparks() {
		int numSparks = world.getNumSparks();
		
		for(int i = 0; i < numSparks; i++) {
			float x = world.getSpark(i).getX();
			float y = world.getSpark(i).getY();
			
			int[] fillColour = world.getSpark(i).getFillColour();
			
			sr.begin(ShapeType.Filled);
			sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
			sr.circle(x, y, 1);
			sr.end();
		}
	}

	private void drawRockets() {
		int numRockets = world.getNumRockets();
		
		for(int i = 0; i < numRockets; i++) {		
			float vertices[][] = world.getRocket(i).getFlameVertices();
			int edges = vertices.length;
			float[] polygon = polygonArray(vertices, edges);
			
			int[] fillColour = world.getRocket(i).getFlameFillColour();
			int[] lineColour = world.getRocket(i).getFlameLineColour();
			
			if(world.getRocket(i).getFlameOn() == true) {
				// Filled flame
				sr.begin(ShapeType.Filled);
				sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
				sr.triangle(vertices[0][0], vertices[0][1],
						vertices[1][0], vertices[1][1],
						vertices[2][0], vertices[2][1]);
				sr.end();
				
				// Flame outline
				sr.begin(ShapeType.Line);
				sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
				sr.polygon(polygon);
				sr.end();
			}
			
			vertices = world.getRocket(i).getVertices();
			edges = world.getRocket(i).getEdges();
			polygon = polygonArray(vertices, edges);
			
			fillColour = world.getRocket(i).getFillColour();
			lineColour = world.getRocket(i).getLineColour();
			
			// Filled polygon
			sr.begin(ShapeType.Filled);
			sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
			sr.triangle(vertices[0][0], vertices[0][1],
					vertices[1][0], vertices[1][1],
					vertices[2][0], vertices[2][1]);
			sr.triangle(vertices[0][0], vertices[0][1],
					vertices[3][0], vertices[3][1],
					vertices[2][0], vertices[2][1]);
			sr.end();
						
			// Polygon outline
			Gdx.gl.glLineWidth(3);
			sr.begin(ShapeType.Line);
			sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
			sr.polygon(polygon);
			sr.end();
		}
	}
	
	private void drawAsteroids() {
		int numAsteroids = world.getNumAsteroids();
		
		for(int i = 0; i < numAsteroids; i++) {
			float x = world.getAsteroid(i).getX();
			float y = world.getAsteroid(i).getY();
			float vertices[][] = world.getAsteroid(i).getVertices();
			int edges = world.getAsteroid(i).getEdges();
			float[] polygon = polygonArray(vertices, edges);
			
			int[] fillColour = world.getAsteroid(i).getFillColour();
			int[] lineColour = world.getAsteroid(i).getLineColour();
			
			// Filled Polygon
			for(int e = 0; e < edges; e++) {
				sr.begin(ShapeType.Filled);
				sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
				
				if(e == edges - 1) { // Final vertex - need to make triangle with the first vertex
					sr.triangle(vertices[e][0], vertices[e][1],
							vertices[0][0], vertices[0][1],
							x, y);
					sr.end();
				} else {
					sr.triangle(vertices[e][0], vertices[e][1],
							vertices[e+1][0], vertices[e+1][1],
							x, y);
					sr.end();
				}
			}
			
			// Polygon outline
			Gdx.gl.glLineWidth(5);
			sr.begin(ShapeType.Line);
			sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
			sr.polygon(polygon);
			sr.end();
		}
	}
	
	private void drawMissiles() {
		int numMissiles = world.getNumMissiles();
		
		for(int i = 0; i < numMissiles; i++) {
			float vertices[][] = world.getMissile(i).getVertices();
			int[] lineColour = world.getMissile(i).getLineColour();
			Gdx.gl.glLineWidth(5);
			sr.begin(ShapeType.Line);
			sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
			sr.line(vertices[0][0], vertices[0][1],
					vertices[1][0], vertices[1][1]);
			sr.end();
		}
	}
	
	private void drawUFOs() {
		int numUFOs = world.getNumUFOs();
		
		for(int i = 0; i < numUFOs; i++) {
			float x = world.getUFO(i).getX();
			float y = world.getUFO(i).getY();
			float vertices[][] = world.getUFO(i).getVertices();
			int edges = world.getUFO(i).getEdges();
			float[] polygon = polygonArray(vertices, edges);
			
			int[] fillColour = world.getUFO(i).getFillColour();
			int[] lineColour = world.getUFO(i).getLineColour();
			
			// Filled Polygon
			for(int e = 0; e < edges; e++) {
				sr.begin(ShapeType.Filled);
				sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
						
				if(e == edges - 1) { // Final vertex - need to make triangle with the first vertex
					sr.triangle(vertices[e][0], vertices[e][1],
							vertices[0][0], vertices[0][1],
							x, y);
					sr.end();
				} else {
					sr.triangle(vertices[e][0], vertices[e][1],
							vertices[e+1][0], vertices[e+1][1],
							x, y);
					sr.end();
				}
			}
			
			// Polygon outline
			Gdx.gl.glLineWidth(3);
			sr.begin(ShapeType.Line);
			sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
			sr.polygon(polygon);
			sr.end();
			
			//Horizontal lines
			sr.begin(ShapeType.Line);
			sr.line(vertices[0][0], vertices[0][1], vertices[5][0], vertices[5][1]);
			sr.line(vertices[1][0], vertices[1][1], vertices[4][0], vertices[4][1]);
			sr.end();
		}
	}
	
	private void drawFragments() {
		int numFragments = world.getNumFragments();
		
		for(int i = 0; i < numFragments; i++) {
			float x = world.getFragment(i).getX();
			float y = world.getFragment(i).getY();
			float vertices[][] = world.getFragment(i).getVertices();
			int edges = world.getFragment(i).getEdges();
			float[] polygon = polygonArray(vertices, edges);
			
			int[] fillColour = world.getFragment(i).getFillColour();
			int[] lineColour = world.getFragment(i).getLineColour();
			
			// Filled Polygon
			for(int e = 0; e < edges; e++) {
				sr.begin(ShapeType.Filled);
				sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
				
				if(e == edges - 1) { // Final vertex - need to make triangle with the first vertex
					sr.triangle(vertices[e][0], vertices[e][1],
							vertices[0][0], vertices[0][1],
							x, y);
					sr.end();
				} else {
					sr.triangle(vertices[e][0], vertices[e][1],
							vertices[e+1][0], vertices[e+1][1],
							x, y);
					sr.end();
				}
			}			
			
			// Polygon outline
			Gdx.gl.glLineWidth(3);
			sr.begin(ShapeType.Line);
			sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
			sr.polygon(polygon);
			sr.end();
		}
	}
	
	private float[] polygonArray(float[][] vertices, int edges) {
		float[] polygon = new float[edges * 2];
		
		for(int i = 0; i < edges; i ++) {
			polygon[i*2] = vertices[i][0];
			polygon[(i*2)+1] = vertices[i][1];
		}
		
		return polygon;	
	}
	
	private void generateStars(int n) {
		stars = new int[n][2];
		Random rand = new Random();
		for(int i = 0; i < n; i++) {
			int x = rand.nextInt(Main.getWidth());
			int y = rand.nextInt(Main.getHeight());
			stars[i][0] = x;
			stars[i][1] = y;
		}
	}

	private void drawBackground() {
		Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sr.begin(ShapeType.Point);
		sr.setColor(Color.WHITE);
		for(int i = 0; i < stars.length; i++) {
			sr.point(stars[i][0], stars[i][1], 0);
		}
		sr.end();
	}
}
