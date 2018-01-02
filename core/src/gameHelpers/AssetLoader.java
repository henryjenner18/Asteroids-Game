package gameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import main.Main;

public class AssetLoader {
	
	public static OrthographicCamera cam;
	public static SpriteBatch batch;
	public static BitmapFont font;
	public static ShapeRenderer sr;
	private static Preferences prefs;
	
	private static int w = Main.getWidth();
	private static int h = Main.getHeight();
	
	public static void load() {
		cam = new OrthographicCamera(w, h);
		cam.translate(w / 2, h / 2);
		cam.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		
		sr = new ShapeRenderer();
		sr.setProjectionMatrix(cam.combined);
		
		font = new BitmapFont(Gdx.files.internal("text.fnt"));
		
		prefs = Gdx.app.getPreferences("HighScore");

		if(!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
	}
	
	public static void setHighScore(int val) {
		prefs.putInteger("highScore", val);
		prefs.flush();
	}
	
	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}
	
	public static void dispose() {
		font.dispose();
	}

}
