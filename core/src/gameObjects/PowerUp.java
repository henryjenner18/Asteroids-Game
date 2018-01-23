package gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameManagers.World;

public class PowerUp extends SpaceObject {
	
	private float lifespan, timeLeft, dh;
	private double flashTimer;
	private boolean flash;
	private int type;
	
	public PowerUp(World world, float x, float y) {
		super(world);
		type = rand.nextInt(3);
		position = new Vector2(x, y);
		r = 19;
		edges = 6;
		vertices = new float[edges][2];
		lifespan = 7;
		setTimeLeft(lifespan);
		setColours();
		setVertices();
		resetFlashTimer();
		flash = true;
		dh = 0;
	}
	
	public void update(float delta) {
		timeLeft -= delta;
		if(timeLeft <= 0) {
			setTimeLeft(0);
		}
		
		flashTimer -= delta;
		if(flashTimer <= 0) {
			setColours();
			flash = !flash;
			resetFlashTimer();
		}
		
		dh ++;
		setVertices();
	}
	
	private void setVertices() {
		float a = 360 / edges; // Regular difference between vertices
		float radians;
		
		for(int i = 0; i < edges; i++) {
			radians = (float) Math.toRadians(a * i + dh);
			
			vertices[i][0] = position.x + MathUtils.cos(radians) * r;
			vertices[i][1] = position.y + MathUtils.sin(radians) * r;
		}
	}

	private void setColours() {
		fillColour = new int[3];
		lineColour = new int[3];
		
		if(type == 0) {
			fillColour[0] = 255;
			fillColour[1] = 24;
			fillColour[2] = 24;
				
			if(flash == true) {
				lineColour[0] = 221;
				lineColour[1] = 218;
				lineColour[2] = 55;
			} else {
				lineColour = fillColour;
			}
			
		} else if(type == 1) {
			fillColour[0] = 255;
			fillColour[1] = 76;
			fillColour[2] = 31;
			
			if(flash == true) {
				lineColour[0] = 94;
				lineColour[1] = 62;
				lineColour[2] = 222;
			} else {
				lineColour = fillColour;
			}
		
		} else if(type == 2) {
			fillColour[0] = 0;
			fillColour[1] = 204;
			fillColour[2] = 102;
			
			if(flash == true) {
				lineColour[0] = 51;
				lineColour[1] = 102;
				lineColour[2] = 255;
			} else {
				lineColour = fillColour;
			}
		}
	}
	
	private void resetFlashTimer() {
		flashTimer = 0.7 * (timeLeft / lifespan);
	}
	
	public void setTimeLeft(float t) {
		timeLeft = t;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
	
	public int getType() {
		return type;
	}
}
