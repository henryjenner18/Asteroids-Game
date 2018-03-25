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
		type = rand.nextInt(5);
		position = new Vector2(x, y);
		r = 19;
		edges = 6;
		vertices = new float[edges][2];
		lifespan = 7;
		setTimeLeft(lifespan);
		fillColour = new int[3];
		lineColour = new int[3];
		setColours();
		setVertices();
		resetFlashTimer();
		flash = true;
		dh = 0;
	}
	
	public void update(float delta) {
		timeLeft -= delta; // Reducing time to live
		if(timeLeft <= 0) {
			setTimeLeft(0);
		}
		
		flashTimer -= delta; // Rate of flashing increases
		if(flashTimer <= 0) {
			setColours();
			flash = !flash; // Reverse colour scheme
			resetFlashTimer();
		}
		
		dh += 2;
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
		if(type == 0) {
			fillColour[0] = 236;
			fillColour[1] = 0;
			fillColour[2] = 0;
			
			lineColour[0] = 150;
			lineColour[1] = 0;
			lineColour[2] = 0;

		} else if(type == 1) {
			fillColour[0] = 255;
			fillColour[1] = 145;
			fillColour[2] = 31;
			
			lineColour[0] = 251;
			lineColour[1] = 76;
			lineColour[2] = 31;

		} else if(type == 2) {
			fillColour[0] = 2;
			fillColour[1] = 179;
			fillColour[2] = 55;
			
			lineColour[0] = 2;
			lineColour[1] = 111;
			lineColour[2] = 55;
		
		} else if(type == 3) {
			fillColour[0] = 2;
			fillColour[1] = 111;
			fillColour[2] = 253;
			
			lineColour[0] = 2;
			lineColour[1] = 54;
			lineColour[2] = 144;
			
		} else if(type == 4) {
			fillColour[0] = 153;
			fillColour[1] = 0;
			fillColour[2] = 153;
			
			lineColour[0] = 102;
			lineColour[1] = 0;
			lineColour[2] = 102;		
		}
		
		if(flash == true) {
			int[] temp = fillColour;
			fillColour = lineColour;
			lineColour = temp;
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
