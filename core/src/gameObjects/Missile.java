package gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Missile extends SpaceObject {
	
	private int r;
	private Vector2 rocketVel;
	
	public Missile() {
		heading = Rocket.getHeading();
		float radians = (float) Math.toRadians(heading);
		float x = Rocket.getX() + MathUtils.cos(radians) * Rocket.getHeight() / 2;
		float y = Rocket.getY() + MathUtils.sin(radians) * Rocket.getHeight() / 2;
		
		position = new Vector2(x, y);
		velocity = new Vector2();
		rocketVel = new Vector2(Rocket.getVelocity());
		
		r = 4;
	}
	
	public void update(float delta) {
		move(delta);
		position.add(velocity);
		wrap();
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);
		
		velocity.x = MathUtils.cos(radians) * delta * 600;
		velocity.y = MathUtils.sin(radians) * delta * 600;
		
		velocity.add(rocketVel);
	}
	
	private void wrap() {
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();;
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
	}
	
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.YELLOW);
		sr.circle(position.x, position.y, r);
		sr.end();
	}
}
