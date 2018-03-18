package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import gameHelpers.AssetLoader;
import gameManagers.World;
import main.Main;

public class MainMenuScreen implements Screen {
	
	private SpriteBatch batch;
	private ShapeRenderer sr;
	
	private int w = Main.getWidth();
	private int h = Main.getHeight();
	
	final Main main;
	private World world;
	
	public MainMenuScreen(final Main main) {
		batch = AssetLoader.batch;
		this.main = main;
		sr = AssetLoader.sr;
		world = new World();
		AssetLoader.closeZoom();
		
		for(int i = 0; i < 5; i++) {
			world.objSpawner.newAsteroid();
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		AssetLoader.zoomOut();
		world.update(delta);
		drawBackground();
		drawAsteroids();
		drawText();	
	}
	
	private void drawText() {
		int x = Gdx.input.getX();
		int y = h - Gdx.input.getY();
		
		batch.begin();
		GlyphLayout layout = new GlyphLayout();
		
		// Title - Asteroids
		String str = "ASTEROIDS";	
		layout.setText(AssetLoader.font, str);
		float strWidth = layout.width;
		float strHeight = layout.height;
		AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 + 2*strHeight);
				
		// Copyright
		BitmapFont f = new BitmapFont();
		str = "C 2017-18 Henry Jenner";
		layout.setText(f, str);
		strWidth = layout.width;
		strHeight = layout.height;
		f.draw(batch, str, w - strWidth - 10, strHeight + 10);
		
		// Play
		str = "Play";	
		layout.setText(AssetLoader.font, str);
		strWidth = layout.width;
		strHeight = layout.height;
		
		if(AssetLoader.cam.zoom == 1) {
		
		if(x >= w/2 - strWidth/2 && x <= w/2 + strWidth/2 &&
				y >= h/2 - 2*strHeight && y <= h/2 - strHeight) {
			
			str = "[Play]";
			layout.setText(AssetLoader.font, str);
			strWidth = layout.width;
			strHeight = layout.height;
			
			if(Gdx.input.isTouched()) {
				main.setGameScreen();
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
		}
		batch.end();
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

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
