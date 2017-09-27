package gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class Rocket {
	
	private Vector2 position;
	private Vector2 velocity;
	
	private float height;
	private float[] vertices;
	public float heading;
	
	public boolean thrusting;
	public boolean left;
	public boolean right;
	
	public Rocket(float ht, float hg, float x, float y) {
		height = ht;
		heading = hg + 90; //set 0 degrees to north
		position = new Vector2(x, y);
		vertices = new float[6];
		velocity = new Vector2(0, 0);
	}
	
	public void update(float delta) {
		if(thrusting) thrust(delta); //apply force if pressing up
		position.add(velocity); //add velocity to rocket's position
		//velocity.scl((float) 0.9999999); //friction
		
		if(left) heading += 4;
		if(right) heading -= 4;
		
		wrap();	
		setVertices();
	}
	
	private void thrust(float delta) {
		Vector2 force = new Vector2();
		float radians = (float) Math.toRadians(heading);
		
		force.x = MathUtils.cos(radians) * delta * 11;
		force.y = MathUtils.sin(radians) * delta * 11;
		
		velocity.add(force);
	}
	
	private void wrap() { //screen wrap
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();;
		float r = height / 2;
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
	}
	
	private void setVertices() {
		float radians = (float) Math.toRadians(heading);
		
		vertices[0] = position.x + MathUtils.cos(radians) * height / 2;
		vertices[1] = position.y + MathUtils.sin(radians) * height / 2;
		
		vertices[2] = position.x + MathUtils.cos(radians - 3 * MathUtils.PI / 4) * height / 3;
		vertices[3] = position.y + MathUtils.sin(radians - 3 * MathUtils.PI / 4) * height / 3;
		
		//vertices[4] = position.x + MathUtils.cos(radians + MathUtils.PI) * height / 4;
		//vertices[5] = position.y + MathUtils.sin(radians + MathUtils.PI) * height / 4;
		
		vertices[4] = position.x + MathUtils.cos(radians + 3 * MathUtils.PI / 4) * height / 3;
		vertices[5] = position.y + MathUtils.sin(radians + 3 * MathUtils.PI / 4) * height / 3;
	}

	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getHeight() {
		return height;
	}

	/*public float[] getVertices() {
		return vertices;
	}*/
	
	public float getVertex(int i) {
		return vertices[i];
	}

	public float getHeading() {
		return heading;
	}
}
