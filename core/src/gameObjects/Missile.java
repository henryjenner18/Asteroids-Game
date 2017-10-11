package gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Missile extends SpaceObject {
	
	private int l;
	private Vector2 rocketVel;
	private float timeLeft;
	private boolean b;
	
	public Missile(boolean b) {
		setTimeLeft(2);
		
		heading = Rocket.getHeading();
		float radians = (float) Math.toRadians(heading);
		float x = Rocket.getX() + MathUtils.cos(radians) * Rocket.getHeight() / 2;
		float y = Rocket.getY() + MathUtils.sin(radians) * Rocket.getHeight() / 2;
		
		position = new Vector2(x, y);
		velocity = new Vector2();
		rocketVel = new Vector2(Rocket.getVelocity());
		vertices = new float[2][2];
		edges = vertices.length;
		l = 30;
		this.b = b;
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
		vertices[1][0] = position.x + MathUtils.cos(radians) * l;
		vertices[1][1] = position.y + MathUtils.sin(radians) * l;
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		float radians = (float) Math.toRadians(heading);
		
		velocity.x = MathUtils.cos(radians) * delta * 800;
		velocity.y = MathUtils.sin(radians) * delta * 800;
		
		velocity.add(rocketVel);
	}
	
	private void wrap() {
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();
		
		if(position.x < 0) position.x = w;
		if(position.x > w) position.x = 0;
		if(position.y < 0) position.y = h;
		if(position.y > h) position.y = 0;	
	}
	
	public void render(ShapeRenderer sr) {
		Gdx.gl.glLineWidth(5);
		sr.begin(ShapeType.Line);
		if(b == true) {
			//sr.setColor(Color.YELLOW);
		} else {
			//sr.setColor(Color.MAGENTA);
		}

		sr.line(vertices[0][0], vertices[0][1],
				vertices[1][0], vertices[1][1]);
		sr.end();
	}
	
	public void setTimeLeft(int i) {
		timeLeft = i;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
}
