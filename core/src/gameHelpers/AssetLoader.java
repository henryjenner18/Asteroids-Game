package gameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetLoader {
	
	public static BitmapFont font;
	private static Preferences prefs;
	
	public static void load() {
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
