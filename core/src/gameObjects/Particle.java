package gameObjects;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Particle extends SpaceObject {
	
	private int v;
	private float timeLeft;
	private int colour;
	
	public Particle(float astX, float astY) {
		Random rand = new Random();
		setTimeLeft((rand.nextFloat() * 1));
		position = new Vector2(astX, astY);
		velocity = new Vector2();
		heading = rand.nextInt(361);
		v = rand.nextInt(50) + 100;
		colour = rand.nextInt(4);
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
	
	private void wrap() { // Screen wrap
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();;
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
	}
	
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		if(colour == 0) {
			sr.setColor(Color.ORANGE);
		} else if(colour == 1) {
			sr.setColor(Color.YELLOW);
		} else if(colour == 2) {
			sr.setColor(Color.CORAL);
		} else {
			sr.setColor(Color.LIGHT_GRAY);
		}
		if(colour == 3) {
			sr.circle(position.x, position.y, 4);
		} else {
			sr.circle(position.x, position.y, 1);
		}
		sr.end();
	}
	
	public void setTimeLeft(float f) {
		timeLeft = f;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
}
