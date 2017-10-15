package gameObjects;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Particle extends SpaceObject {
	
	Random rand;
	private int v;
	
	public Particle(float astX, float astY) {
		rand = new Random();
		position = new Vector2(astX, astY);
		velocity = new Vector2();
		heading = rand.nextInt(361);
		v = rand.nextInt(101) + 150;
	}
	
	public void update(float delta) {
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
		sr.setColor(Color.RED);
		sr.circle(position.x, position.y, 4);
		sr.end();
	}

}
