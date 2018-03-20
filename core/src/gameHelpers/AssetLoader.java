package gameHelpers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	private static boolean left, up;
	private static Random r;
	
	private static int w = Main.getWidth();
	private static int h = Main.getHeight();
	
	public static Sound asteroidExplosion, rocketExplosion, ufoExplosion, rocketMissile, ufoMissile, powerUp, levelUp, ufoSpawn, gameOver, ricochet;
	public static Music inPlayMusic;
	
	public static void load() {
		r = new Random();
		
		cam = new OrthographicCamera(w, h);	
		batch = new SpriteBatch();	
		sr = new ShapeRenderer();
		resetCam();
		
		font = new BitmapFont(Gdx.files.internal("text.fnt"));
		
		prefs = Gdx.app.getPreferences("HighScore");
		
		if(!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
		
		// Music
		inPlayMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/pixelatedCosmos.mp3"));
		inPlayMusic.setLooping(true); // Music is on a constant loop
		inPlayMusic.setVolume(0.2f);
		
		// Sound FX
		asteroidExplosion = Gdx.audio.newSound(Gdx.files.internal("audio/asteroidExplosion.wav"));
		rocketExplosion = Gdx.audio.newSound(Gdx.files.internal("audio/rocketExplosion.wav"));
		ufoExplosion = Gdx.audio.newSound(Gdx.files.internal("audio/ufoExplosion.wav"));
		rocketMissile = Gdx.audio.newSound(Gdx.files.internal("audio/rocketMissile.wav"));
		ufoMissile = Gdx.audio.newSound(Gdx.files.internal("audio/ufoMissile.wav"));
		powerUp = Gdx.audio.newSound(Gdx.files.internal("audio/powerUp.mp3"));
		levelUp = Gdx.audio.newSound(Gdx.files.internal("audio/levelUp.mp3"));
		ufoSpawn = Gdx.audio.newSound(Gdx.files.internal("audio/ufoSpawn.wav"));
		gameOver = Gdx.audio.newSound(Gdx.files.internal("audio/gameOver.wav"));
		ricochet = Gdx.audio.newSound(Gdx.files.internal("audio/ricochet.wav"));
	}
	
	public static void setHighScore(int val) {
		prefs.putInteger("highScore", val);
		prefs.flush();
	}
	
	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}
	
	public static void dispose() {
		font.dispose(); // Tidying up
		batch.dispose();
		asteroidExplosion.dispose();
		rocketExplosion.dispose();
		ufoExplosion.dispose();
		rocketMissile.dispose();
		ufoMissile.dispose();
		powerUp.dispose();
		levelUp.dispose();
		ufoSpawn.dispose();
		gameOver.dispose();
		ricochet.dispose();
	}
	
	public static void zoomOut() {
		if(cam.zoom < 1) {
			cam.zoom += 0.01;
		} else {
			cam.zoom = 1;
			
			if(Main.isSound()) {
				inPlayMusic.play();
			}		
		}
		
		setCam();
	}
	
	public static void closeZoom() {
		cam.zoom = 0;
		setCam();
	}
	
	public static void translate() {		
		float x = 0, y = 0;
		int t = 7;
		
		int n = r.nextInt(3);	
		if(n == 0) {	
			if(left) {
				x = -t;
			} else {
				x = t;
			}
			left = !left;
		}
		
		n = r.nextInt(3);	
		if(n == 0) {	
			if(up) {
				y = t;
			} else {
				y = -t;
			}
			up = !up;
		}
		
		cam.translate(x, y);
		setCam();
	}
	
	public static void resetCam() {
		cam.setToOrtho(false);
		cam.zoom = 1;
		setCam();
	}
	
	public static void setCam() {
		cam.update();
		sr.setProjectionMatrix(cam.combined);
		batch.setProjectionMatrix(cam.combined);
	}
}
