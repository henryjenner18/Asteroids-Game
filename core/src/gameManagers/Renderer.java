package gameManagers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import main.Main;

public class Renderer {
	
	private World world;
	private OrthographicCamera cam;
	private ShapeRenderer sr;
	private BitmapFont font;
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
		generateStars(500);
		font = new BitmapFont(Gdx.files.internal("text.fnt"));
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
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
	}
	
	private void drawGameStats() {
		batch.begin();
		
		// Score
		int sc = world.getScore();
		String score = sc + "";
		font.draw(batch, score, 10, h - 10);
		
		// Level
		int lvl = world.getLevel();
		String level = "Lvl " + lvl;
		
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, level);
		float width = layout.width;

		font.draw(batch, level, w - width - 10, h - 10);
		
		// Lives
		int lvs = world.getLives();
		String lives = "Lives: " + lvs;
		
		layout.setText(font, lives);
		float height = layout.height;
		font.draw(batch, lives, 10, height + 10);
		batch.end();
		
		
		// Health bar
		/*int rectW = 250;
		int rectH = 40;
		
		float health = world.getHealth();
		float healthW = (health / 100) * rectW;
		
		int G = (int) ((255 * health) / 100);
		int R = (int) ((255 * (100 - health)) / 100);
		sr.begin(ShapeType.Filled);
		sr.setColor(R/255f, G/255f, 0/255f, 1);
		sr.rect(10, (rectH / 2)+10, healthW, rectH);
		sr.end();
		
		sr.begin(ShapeType.Line);
		sr.setColor(1, 1, 1, 1);
		sr.rect(10, (rectH / 2)+10, rectW, rectH);
		sr.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		sr.begin(ShapeType.Filled);
		sr.setColor(1, 1, 1, 0.5f);
		sr.triangle(200, 500, 215, 540, 230, 500);
		sr.end();*/
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
			Gdx.gl.glLineWidth(3);
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
