package gameHelpers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
	private static float musicVol;
	
	public static Sound asteroidExplosion, rocketExplosion, ufoExplosion,
	rocketMissile, ufoMissile, powerUp, levelUp, ufoSpawn, gameOver;
	public static Music inPlayMusic;
	
	public static void load() {
		r = new Random();
		
		cam = new OrthographicCamera(w, h);	
		batch = new SpriteBatch();	
		sr = new ShapeRenderer();
		resetCam();
		
		font = new BitmapFont(Gdx.files.internal("text.fnt"));
		
		// Storing high scores
		prefs = Gdx.app.getPreferences("HighScore");

		if(!prefs.contains("highScore1P")) {
			prefs.putInteger("highScore1P", 0);
		}

		if(!prefs.contains("highScore2P")) {
			prefs.putInteger("highScore2P", 0);
		}

		// Music
		inPlayMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/pixelatedCosmos.mp3"));
		inPlayMusic.setLooping(true);
		musicVol = 0f;
		inPlayMusic.setVolume(musicVol);
		inPlayMusic.play();
		
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
	}
	
	public static void fadeInMusic() {
		if(musicVol < 0.199f) {
			musicVol += 0.001f;
		}
		
		inPlayMusic.setVolume(musicVol);
	}
	
	public static void resetFont() { // Default font colour
		AssetLoader.font.setColor(Color.WHITE);
	}
	
	public static void hoverFont() { // Colour for when a button is hovered over
		AssetLoader.font.setColor(Color.VIOLET);
	}
	
	public static void setHighScore1P(int val) {
		prefs.putInteger("highScore1P", val);
		prefs.flush();
	}
	
	public static void setHighScore2P(int val) {
		prefs.putInteger("highScore2P", val);
		prefs.flush();
	}
	
	public static int getHighScore1P() {
		return prefs.getInteger("highScore1P");
	}
	
	public static int getHighScore2P() {
		return prefs.getInteger("highScore2P");
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
	}
	
	public static void playMusic() {
		inPlayMusic.setLooping(true);
		inPlayMusic.setVolume(0.2f);
		inPlayMusic.play();
	}
	
	public static void pauseAudio() {
		inPlayMusic.pause();
		asteroidExplosion.pause();
		ufoSpawn.pause();
		rocketExplosion.pause();
		ufoMissile.pause();
		rocketMissile.pause();
		ufoExplosion.pause();
		levelUp.pause();
		powerUp.pause();
	}
	
	public static void resumeAudio() {
		inPlayMusic.play();
		asteroidExplosion.resume();
		ufoSpawn.resume();
		rocketExplosion.resume();
		ufoMissile.resume();
		rocketMissile.resume();
		ufoExplosion.resume();
		levelUp.resume();
		powerUp.resume();
	}
	
	public static void zoomOut() {
		if(cam.zoom < 1) {
			cam.zoom += 0.005;
		} else {
			cam.zoom = 1;	
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
