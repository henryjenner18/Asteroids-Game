package gameHelpers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import gameObjects.Rocket;

public class InputHandler implements InputProcessor {
	
	private Rocket myRocket;
	
	public InputHandler(Rocket rocket) {
		myRocket = rocket;
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
			// Shoot bullet
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
