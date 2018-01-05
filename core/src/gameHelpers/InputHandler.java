package gameHelpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import gameManagers.World;
import gameObjects.Rocket;

public class InputHandler implements InputProcessor {
	
	private World world;
	
	public InputHandler(World world) {
		this.world = world;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(world.isRunning() && !world.isRespawn()) {
			if(world.getNumRockets() > 0) {
				Rocket rocket = world.getRocket(0);
				
				if(keycode == Keys.UP || keycode == Keys.W) {
					rocket.setThrusting(true);
						
				} else if(keycode == Keys.LEFT || keycode == Keys.A) {
					rocket.setLeft(true);
						
				} else if(keycode == Keys.RIGHT  || keycode == Keys.D) {
					rocket.setRight(true);
						
				} else if(keycode == Keys.SPACE) {
					int num;
					
					if(rocket.getTripleMissile() == true) {
						num = 3;
					} else {
						num = 1;
					}
					
					world.objSpawner.missile('r', num, rocket.getX(), rocket.getY(), rocket.getHeading(), rocket.getHeight(), rocket.getVelocity(), rocket.getMissileV(), rocket.getMissileColour());
				}
			}
		}
		
		if(keycode == Keys.P && !world.isGameOver()) {
			if(world.isPause()) {
				world.start();
			
			} else {
				world.pause();
			}
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(world.isRunning() || world.isNextLevel() || world.isPause()) {
			if(world.getNumRockets() > 0) {
				Rocket rocket = world.getRocket(0);
				
				if(keycode == Keys.UP || keycode == Keys.W) {
					rocket.setThrusting(false);
					
				} else if(keycode == Keys.LEFT || keycode == Keys.A) {
					rocket.setLeft(false);
					
				} else if(keycode == Keys.RIGHT || keycode == Keys.D) {
					rocket.setRight(false);
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
