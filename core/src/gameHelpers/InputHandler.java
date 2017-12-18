package gameHelpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import gameManagers.World;
import gameObjects.Rocket;

public class InputHandler implements InputProcessor {
	
	private World world;
	private Rocket rocket;
	
	public InputHandler(World world, Rocket rocket) {
		this.world = world;
		this.rocket = rocket;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(world.isReady()) {
			if(keycode == Keys.ENTER) {
				world.start();
			}
			
		} else if(world.isGameOver()) {
			if(keycode == Keys.ENTER) {
				world.restart();
			}
			
		} else if(world.isRunning()) {
			if(keycode == Keys.UP) {
				rocket.setThrusting(true);
					
			} else if(keycode == Keys.LEFT) {
				rocket.setLeft(true);
					
			} else if(keycode == Keys.RIGHT) {
				rocket.setRight(true);
					
			} else if(keycode == Keys.SPACE) {
				world.spawnMissile('r', rocket.getX(), rocket.getY(), rocket.getHeading(), rocket.getHeight(), rocket.getVelocity(), rocket.getMissileV(), rocket.getMissileColour());	
					
			} else if(keycode == Keys.DOWN) {
				world.pause();
			}
			
		} else if(world.isPause()) {
			if(keycode == Keys.DOWN) {
				world.start();
			}
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(world.isRunning()) {
			if(keycode == Keys.UP) {
				rocket.setThrusting(false);
				
			} else if(keycode == Keys.LEFT) {
				rocket.setLeft(false);
				
			} else if(keycode == Keys.RIGHT) {
				rocket.setRight(false);
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
