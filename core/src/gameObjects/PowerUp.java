package gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameManagers.World;

public class PowerUp extends SpaceObject {
	
	private float lifespan, timeLeft;
	private double flashTimer;
	private boolean flash;
	
	public PowerUp(World world, float x, float y) {
		super(world);
		position = new Vector2(x, y);
		r = 19;
		edges = 6;
		vertices = new float[edges][2];
		lifespan = 7;
		setTimeLeft(lifespan);
		setVertices();
		setColours();
		resetFlashTimer();
		flash = true;
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
	}
	
	private void setVertices() {
		float a = 360 / edges; // Regular difference between vertices
		float radians;
		
		for(int i = 0; i < edges; i++) {
			radians = (float) Math.toRadians(a * i);
			
			vertices[i][0] = position.x + MathUtils.cos(radians) * r;
			vertices[i][1] = position.y + MathUtils.sin(radians) * r;
		}
	}

	private void setColours() {
		fillColour = new int[3];
		fillColour[0] = 255;
		fillColour[1] = 40;
		fillColour[2] = 40;
		
		lineColour = new int[3];
		if(flash == true) {
			lineColour[0] = 204;
			lineColour[1] = 255;
			lineColour[2] = 51;
		} else {
			lineColour = fillColour;
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
}
