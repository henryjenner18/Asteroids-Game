package gameManagers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameHelpers.AssetLoader;
import gameObjects.PowerUp;
import gameObjects.Shield;
import main.Main;

public class Renderer {
	
	private World world;
	private ShapeRenderer sr;
	private SpriteBatch batch;
	private int w = Main.getWidth();
	private int h = Main.getHeight();
	
	public Renderer(World world) {
		this.world = world;
		batch = AssetLoader.batch;
		sr = AssetLoader.sr;
	}

	public void render(float delta) {
		drawBackground();
		drawPowerUps();
		drawSparks();
		drawAsteroids();
		drawFragments();
		drawMissiles();
		drawUFOs();
		drawShields();
		
		if(world.isGameOver() == false) {
			drawRockets();
		}
		
		drawGameStats();
		drawText(delta);
	}
	
	private void drawText(float delta) {
		batch.begin();
		GlyphLayout layout = new GlyphLayout();
		String str;
		float strWidth, strHeight;
		
		if(world.isGameOver()) {
			Gdx.input.setCursorCatched(false);
			
			int x = Gdx.input.getX();
			int y = h - Gdx.input.getY();
			
			// Game over
			str = "GAME OVER";	
			layout.setText(AssetLoader.font, str);
			strWidth = layout.width;
			strHeight = layout.height;
			AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 + 2*strHeight);
			
			if(world.gameOverTimer > 0) {
				world.gameOverTimer -= delta;
				
			} else {			
				// Play again
				str = "Play again";	
				layout.setText(AssetLoader.font, str);
				strWidth = layout.width;
				strHeight = layout.height;
						
				if(x >= w/2 - strWidth/2 && x <= w/2 + strWidth/2 &&
					y >= h/2 - 2*strHeight && y <= h/2 - strHeight) {
							
					str = "[Play again]";
					layout.setText(AssetLoader.font, str);
					strWidth = layout.width;
					strHeight = layout.height;
							
					if(Gdx.input.isTouched()) {
						world.restart();
					}
				}
						
				AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 - strHeight);
				
				// Exit
				str = "Exit";
				layout.setText(AssetLoader.font, str);
				strWidth = layout.width;
				strHeight = layout.height;
						
				if(x >= w/2 - strWidth/2 && x <= w/2 + strWidth/2 &&
					y >= h/2 - 5*strHeight && y <= h/2 - 4*strHeight) {
	
					str = "[Exit]";
					layout.setText(AssetLoader.font, str);
					strWidth = layout.width;
					strHeight = layout.height;
							
					if(Gdx.input.isTouched()) {
						Gdx.app.exit();
					}
				}
						
				AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 - 4*strHeight);
				
				// Game time
				float gameTimer = world.getGameTimer();
				int mins = MathUtils.floor(gameTimer / 60);
				int secs = Math.round(gameTimer % 60);
				String strTimer = mins + " mins " + secs + " secs";
				
				// High score
				world.compareHighScore();
				
				String strHS;
				if(world.getScore() == AssetLoader.getHighScore()) {
					strHS = "New high score!";
				
				} else {
					strHS = "High score: " + AssetLoader.getHighScore();
				}
				
				str = strTimer + "    " + strHS;
				layout.setText(AssetLoader.font, str);
				strWidth = layout.width;
				strHeight = layout.height;
				AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 + 5*strHeight);
			}
		}
		
		if(world.isPause()) {
			// Pause
			str = "II";	
			layout.setText(AssetLoader.font, str);
			strWidth = layout.width;
			strHeight = layout.height;
			AssetLoader.font.draw(batch, str, w/2 - strWidth/2, strHeight + 10);
			
			// Exit and restart
			BitmapFont f = new BitmapFont();
			str = "Press E to exit, R to restart";
			layout.setText(f, str);
			strWidth = layout.width;
			strHeight = layout.height;
			f.draw(batch, str, w - strWidth - 10, strHeight + 10);
			
			if(Gdx.input.isKeyPressed(Input.Keys.E)) {
				Gdx.app.exit();
				
			} else if(Gdx.input.isKeyPressed(Input.Keys.R)) {
				world.restart();
			}
		}
		
		batch.end();
	}
	
	private void drawLives() {
		int lives = world.getLives();
		int height = 80;
		int r = height / 2;
		int width = (r * lives) + (lives - 1) * 10;
		Vector2 position = new Vector2((w / 2) - (width / 2) + (r / 2), h - height / 2 - 15);
		
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
			sr.setColor(51/255f, 153/255f, 255/255f, 0.5f);
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
			sr.setColor(204/255f, 0/255f, 102/255f, 0.5f);
			sr.polygon(polygon);
			sr.end();
			
			position.x += r + 10;
		}
	}
	
	private void drawGameStats() {
		batch.begin();
			
		// Score
		int sc = world.getScore();
		String score = sc + "";
			
		AssetLoader.font.draw(batch, score, 10, h - 10);
			
		// Level
		int lvl = world.getLevel();
		
		if(world.isNextLevel() == false) {
			String level = "Lvl " + lvl;
				
			GlyphLayout layout = new GlyphLayout();
			layout.setText(AssetLoader.font, level);
			float levelWidth = layout.width;
		
			AssetLoader.font.draw(batch, level, w - levelWidth - 10, h - 10);
		}
		batch.end();
			
		drawLives();
	}
	
	private void drawPowerUps() {
		int numPowerUps = world.getNumPowerUps();
		
		for(int i = 0; i < numPowerUps; i++) {
			PowerUp p = world.getPowerUps().get(i);
			
			float x = p.getX();
			float y = p.getY();
			float vertices[][] = p.getVertices();
			int edges = p.getEdges();
			float[] polygon = polygonArray(vertices, edges);
			
			int[] fillColour = p.getFillColour();
			int[] lineColour = p.getLineColour();
			
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
	
	private void drawSparks() {
		int numSparks = world.getNumSparks();
		
		for(int i = 0; i < numSparks; i++) {
			float x = world.getSpark(i).getX();
			float y = world.getSpark(i).getY();
			float r = world.getSpark(i).getR();
			
			int[] fillColour = world.getSpark(i).getFillColour();
			
			sr.begin(ShapeType.Filled);
			sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
			sr.circle(x, y, r);
			sr.end();
		}
	}

	private void drawRockets() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glLineWidth(3);
		
		int numRockets = world.getNumRockets();
		
		for(int i = 0; i < numRockets; i++) {		
			float vertices[][] = world.getRocket(i).getFlameVertices();
			int edges = vertices.length;
			float[] polygon = polygonArray(vertices, edges);
			
			int[] fillColour = world.getRocket(i).getFlameFillColour();
			int[] lineColour = world.getRocket(i).getFlameLineColour();
			
			boolean invincible;		
			if(world.getRocket(i).getInvincible() == true) {
				invincible = true;
			} else {
				invincible = false;
			}
			
			if(world.getRocket(i).getFlameOn() == true && invincible == false) {
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
			
			if(invincible == true) {
				sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 0.5f);

			} else {
				sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
			}
			
			sr.triangle(vertices[0][0], vertices[0][1],
					vertices[1][0], vertices[1][1],
					vertices[2][0], vertices[2][1]);
			sr.triangle(vertices[0][0], vertices[0][1],
					vertices[3][0], vertices[3][1],
					vertices[2][0], vertices[2][1]);
			sr.end();
						
			// Polygon outline
			sr.begin(ShapeType.Line);
			
			if(invincible == true) {
				sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 0.5f);

			} else {
				sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
			}
			
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
			Gdx.gl.glLineWidth(6);
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
	
	private void drawShields() {
		int numShields = world.getNumShields();
		
		for(int i = 0; i < numShields; i++) {
			Shield s = world.getShield(i);
			float x = s.getX();
			float y = s.getY();
			float vertices[][] = s.getVertices();
			int edges = s.getEdges();
			float[] polygon = polygonArray(vertices, edges);
			
			int[] fillColour = s.getFillColour();
			int[] lineColour = s.getLineColour();
			
			// Filled Polygon
			for(int e = 0; e < edges; e++) {
				Gdx.gl.glEnable(GL20.GL_BLEND);
				sr.begin(ShapeType.Filled);
				sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 0.3f);
							
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
						
			Gdx.gl.glLineWidth(3);
			sr.begin(ShapeType.Line);
			sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 0.3f);
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

	private void drawBackground() {
		Gdx.gl.glClearColor(2/255f, 2/255f, 2/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sr.begin(ShapeType.Point);
		sr.setColor(Color.WHITE);
	
		int[][] stars = Main.getStars();
		
		for(int i = 0; i < stars.length; i++) {
			sr.point(stars[i][0], stars[i][1], 0);
		}
		
		sr.end();
	}
}
