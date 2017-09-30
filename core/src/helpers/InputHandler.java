package helpers;

import com.badlogic.gdx.InputProcessor;

import objects.Rocket;

public class InputHandler implements InputProcessor {
	
	private Rocket myRocket;
	
	public InputHandler(Rocket rocket) {
		myRocket = rocket;
	}

	@Override
	public boolean keyDown(int keycode) { // Pressing a key
		if(keycode == 19) { // Up
			myRocket.thrusting = true;
			
		} else if(keycode == 21) { // Left
			myRocket.left = true;
			
		} else if(keycode == 22) { // Right
			myRocket.right = true;
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) { // Releasing a key
		if(keycode == 19) { // Up
			myRocket.thrusting = false;
		
		} else if(keycode == 21) { // Left
			myRocket.left = false;
			
		} else if(keycode == 22) { // Right
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
