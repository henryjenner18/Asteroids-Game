package gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameManagers.World;

public class Missile extends SpaceObject {
	
	private int height, vMult;
	final Vector2 objVelocity;
	private float timeLeft;
	private char creator;
	
	public Missile(World world, char creator, float objX, float objY, double heading, int objHeight, Vector2 objVelocity, int vMult, int[] colour) {
		super(world);
		setTimeLeft((float) 1.5);
		this.creator = creator;
		this.heading = heading;
		float radians = (float) Math.toRadians(heading);
		float x = objX + MathUtils.cos(radians) * objHeight / 2;
		float y = objY + MathUtils.sin(radians) * objHeight / 2;	
		position = new Vector2(x, y);
		velocity = new Vector2();
		this.objVelocity = new Vector2(objVelocity.x, objVelocity.y);
		vertices = new float[2][2];
		this.vMult = vMult;
		edges = vertices.length;
		height = 30;
		lineColour = colour;
	}
	
	public void update(float delta) {
		timeLeft -= delta;
		if(timeLeft <= 0) {
			setTimeLeft(0);
		}
		move(delta);
		position.add(velocity);
		wrap();
		setVertices();
	}
	
	private void setVertices() {
		float radians = (float) Math.toRadians(heading);
		
		vertices[0][0] = position.x;
		vertices[0][1] = position.y;
		vertices[1][0] = position.x + MathUtils.cos(radians) * height;
		vertices[1][1] = position.y + MathUtils.sin(radians) * height;
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);
		
		velocity.x = MathUtils.cos(radians) * delta * vMult;
		velocity.y = MathUtils.sin(radians) * delta * vMult;
		velocity.add(objVelocity);
	}
	
	public void setTimeLeft(float t) {
		timeLeft = t;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
	
	public char getCreator() {
		return creator;
	}
}
