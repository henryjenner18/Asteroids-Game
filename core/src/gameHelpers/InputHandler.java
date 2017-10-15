package gameHelpers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import gameObjects.Rocket;
import gameWorld.GameWorld;

public class InputHandler implements InputProcessor {
	
	private Rocket myRocket;
	private GameWorld myWorld;
	
	public InputHandler(Rocket rocket, GameWorld world) {
		myRocket = rocket;
		myWorld = world;
	}

	@Override
	public boolean keyDown(int keycode) { // Pressing a key
		if(keycode == Keys.UP) { // Up
			myRocket.thrusting = true;
			
		} else if(keycode == Keys.LEFT) {
			myRocket.left = true;
			
		} else if(keycode == Keys.RIGHT) {
			myRocket.right = true;
			
		} else if(keycode == Keys.SPACE) {
			float x = myRocket.getX(); // Get coordinates of rocket
			float y = myRocket.getY();
			Vector2 vel = myRocket.getVelocity();
			int hg = myRocket.getHeading();
			myWorld.createMissile(x, y, vel, hg);
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) { // Releasing a key
		if(keycode == Keys.UP) {
			myRocket.thrusting = false;
		
		} else if(keycode == Keys.LEFT) {
			myRocket.left = false;
			
		} else if(keycode == Keys.RIGHT) {
			myRocket.right = false;
		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
