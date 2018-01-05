package gameObjects;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameManagers.World;

public class Spark extends SpaceObject {
	
	private int v, colour;
	private float timeLeft;
	private boolean missilesOnly;
	Random rand = new Random();
	
	public Spark(World world, float x, float y, boolean missilesOnly) {
		super(world);
		position = new Vector2(x, y);
		velocity = new Vector2();
		r = 1;
		setTimeLeft(rand.nextFloat() * rand.nextFloat());
		heading = rand.nextInt(361);
		v = rand.nextInt(80) + 80;
		this.missilesOnly = missilesOnly;
		setColour();
	}
	
	private void setColour() {
		int n;
		if(missilesOnly == true) {
			n = 3;
		} else {
			n = 4;
		}
		colour = rand.nextInt(n);
		
		fillColour = new int[3];
		switch(colour) {
		case 0:
			fillColour[0] = 255;
			fillColour[1] = 128;
			fillColour[2] = 0;
			break;
		
		case 1:
			fillColour[0] = 255;
			fillColour[1] = 255;
			fillColour[2] = 0;
			break;
			
		case 2:
			fillColour[0] = 255;
			fillColour[1] = 102;
			fillColour[2] = 102;
			break;
			
		case 3:
			fillColour[0] = 150;
			fillColour[1] = 150;
			fillColour[2] = 150;
			r = 3;
			break;
		}
	}

	public void update(float delta) {
		timeLeft -= delta;
		if(timeLeft <= 0) {
			setTimeLeft(0);
		}
		move(delta);
		position.add(velocity);
		wrap();
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);
		
		velocity.x = MathUtils.cos(radians) * delta * v;
		velocity.y = MathUtils.sin(radians) * delta * v;
	}
	
	public void setTimeLeft(float f) {
		timeLeft = f;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
}
