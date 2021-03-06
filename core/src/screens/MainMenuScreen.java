package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import gameHelpers.AssetLoader;
import main.Main;

public class MainMenuScreen implements Screen {
	
	private SpriteBatch batch;
	private ShapeRenderer sr;
	
	private int w = Main.getWidth();
	private int h = Main.getHeight();
	
	final Main main;
	
	public MainMenuScreen(final Main main) {
		Gdx.input.setCursorCatched(true);
		batch = AssetLoader.batch;
		this.main = main;
		sr = AssetLoader.sr;
		AssetLoader.closeZoom();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {	
		checkSound();
		AssetLoader.zoomOut();
		AssetLoader.fadeInMusic();
		drawBackground();
		drawText();
	}
	
	private void checkSound() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
			Main.toggleSound();
		}
	}
	
	private void drawText() {
		int x = Gdx.input.getX();
		int y = h - Gdx.input.getY();
		
		batch.begin();
		GlyphLayout layout = new GlyphLayout();
		
		// Title - Asteroids
		String str = "ASTEROIDS";
		AssetLoader.font.setColor(102/255f, 255/255f, 102/255f, 1f);
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
		
		// Sound
		if(Main.isSound()) {
			str = "Press O to turn sound OFF";
		} else {
			str = "Press O to turn sound ON";
		}
		layout.setText(f, str);
		strWidth = layout.width;
		strHeight = layout.height;
		f.draw(batch, str, 10, strHeight + 10);
				
		if(AssetLoader.cam.zoom == 1) {
			Gdx.input.setCursorCatched(false);
			AssetLoader.resetFont();
			// Play solo
			str = "Solo";	
			layout.setText(AssetLoader.font, str);
			strWidth = layout.width;
			strHeight = layout.height;
			
			if(x >= w/2 - strWidth/2 && x <= w/2 + strWidth/2 &&
					y >= h/2 - 2*strHeight && y <= h/2 - strHeight) {
				
				AssetLoader.hoverFont();
				if(Gdx.input.isTouched()) {
					main.setGameScreen(false);
				}
			}
			
			AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 - strHeight);
			
			// Play co-op
			AssetLoader.resetFont();
			str = "Co-op";	
			layout.setText(AssetLoader.font, str);
			strWidth = layout.width;
			strHeight = layout.height;
						
			if(x >= w/2 - strWidth/2 && x <= w/2 + strWidth/2 &&
					y >= h/2 - 4*strHeight && y <= h/2 - 3*strHeight) {
							
				AssetLoader.hoverFont();						
				if(Gdx.input.isTouched()) {
					main.setGameScreen(true);
				}
			}
						
			AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 - 3*strHeight);			
			
			// Exit
			AssetLoader.resetFont();
			str = "Exit";
			layout.setText(AssetLoader.font, str);
			strWidth = layout.width;
			strHeight = layout.height;
			
			if(x >= w/2 - strWidth/2 && x <= w/2 + strWidth/2 &&
					y >= h/2 - 6*strHeight && y <= h/2 - 5*strHeight) {
	
				AssetLoader.hoverFont();
				if(Gdx.input.isTouched()) {
					Gdx.app.exit();
				}
			}
			
			AssetLoader.font.draw(batch, str, w/2 - strWidth/2, h/2 - 5*strHeight);
		}
		batch.end();
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
