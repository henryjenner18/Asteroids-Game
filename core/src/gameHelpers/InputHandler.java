package gameHelpers;

import com.badlogic.gdx.InputProcessor;

import com.badlogic.gdx.Input.Keys;

import gameManagers.World;
import gameObjects.Rocket;
import main.Main;

public class InputHandler implements InputProcessor {
	
	private World world;
	private int numRockets;
	
	public InputHandler(World world) {
		this.world = world;
		numRockets = world.getNumRockets();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(world.isRunning()) {
			if(numRockets > 0) {	
				Rocket r = world.getRocket(0); // Input for player one
				
				if(r.getRespawn() == false) {
					switch(keycode) {
					case Keys.UP:
						r.setThrusting(true);
						break;
						
					case Keys.LEFT:
						r.setLeft(true);
						break;
						
					case Keys.RIGHT:
						r.setRight(true);
						break;
						
					case Keys.SPACE:
						if(Main.isTwoPlayer() == false) {
							int num;
							if(r.getTripleMissile() == true) {
								num = 3;
							} else {
								num = 1;
							}
							world.objSpawner.missile('r', num, r.getX(), r.getY(), r.getHeading(),
									r.getHeight(), r.getVelocity(), r.getMissileV(), r.getMissileColour());
						}	
						break;
						
					case Keys.CONTROL_RIGHT:
						if(Main.isTwoPlayer() == true) {
							int num;
							if(r.getTripleMissile() == true) {
								num = 3;
							} else {
								num = 1;
							}
							world.objSpawner.missile('r', num, r.getX(), r.getY(), r.getHeading(),
									r.getHeight(), r.getVelocity(), r.getMissileV(), r.getMissileColour());
						}
						break;
						
					default:
						break;
					}
				}						
				
				if(numRockets > 1) { // Input for player two
					r = world.getRocket(1);
					
					if(r.getRespawn() == false) {
						switch(keycode) {
						case Keys.W:
							r.setThrusting(true);
							break;
							
						case Keys.A:
							r.setLeft(true);
							break;
							
						case Keys.D:
							r.setRight(true);
							break;
							
						case Keys.CONTROL_LEFT:
							int num;
							if(r.getTripleMissile() == true) {
								num = 3;
							} else {
								num = 1;
							}
							world.objSpawner.missile('r', num, r.getX(), r.getY(), r.getHeading(),
									r.getHeight(), r.getVelocity(), r.getMissileV(), r.getMissileColour());
							break;
							
						default:
							break;
						}
					}			
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
		
		if(keycode == Keys.O) {
			Main.toggleSound();
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(world.isRunning() || world.isNextLevel() || world.isPause()) {
			if(numRockets > 0) {
				Rocket r = world.getRocket(0);
				
				switch(keycode) {
				case Keys.UP:
					r.setThrusting(false);
					break;
					
				case Keys.LEFT:
					r.setLeft(false);
					break;
					
				case Keys.RIGHT:
					r.setRight(false);
					break;
					
				default:
					break;
				}
				
				if(numRockets > 1) {
					r = world.getRocket(1);
					
					switch(keycode) {
					case Keys.W:
						r.setThrusting(false);
						break;
						
					case Keys.A:
						r.setLeft(false);
						break;
						
					case Keys.D:
						r.setRight(false);
						break;
						
					default:
						break;
					}
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
