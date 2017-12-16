package gameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetLoader {
	
	public static BitmapFont font;
	
	public static void load() {
		font = new BitmapFont(Gdx.files.internal("text.fnt"));
	}
	
	public static void dispose() {
		font.dispose();
	}

}
