package gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Missile extends SpaceObject {
	
	public Missile() {
		position = new Vector2(Rocket.getX(), Rocket.getY());
		heading = Rocket.getHeading();
		velocity = new Vector2();
	}
	
	public void update(float delta) {
		move(delta);
		position.add(velocity);
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);
		
		velocity.x = MathUtils.cos(radians) * delta * 400;
		velocity.y = MathUtils.sin(radians) * delta * 400;
	}
	
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.GOLD);
		sr.circle(position.x, position.y, 4);
		sr.end();
	}
}
