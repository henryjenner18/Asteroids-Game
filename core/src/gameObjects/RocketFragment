package gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class RocketFragment extends SpaceObject {
	private int v;
	private int height;
	private float timeLeft;
	
	public RocketFragment(float rockX, float rockY) {
		Random rand = new Random();
		setTimeLeft((rand.nextFloat() * 1));
		//System.out.println("in constructor");
		vertices = new float[3][2]; // 3 vertices with a pair of x and y coordinates each
		position = new Vector2(rockX, rockY);
		velocity = new Vector2();
		//System.out.println("before heading");
		heading = rand.nextInt(361);
		//System.out.println("after heading");
		v = rand.nextInt(50) + 100;
		edges = vertices.length;
		setHeight(40);
		//System.out.println("end of constructor");
	}
	
	public void update(float delta) {
		move(delta);
		position.add(velocity);
		wrap();
		setVertices(); // Alter coordinates
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

	private void setVertices() {
		float radians = (float) Math.toRadians(heading);
		
		vertices[0][0] = position.x + MathUtils.cos(radians) * height / 2;
		vertices[0][1] = position.y + MathUtils.sin(radians) * height / 2;
		
		vertices[1][0] = position.x + MathUtils.cos(radians + 3 * MathUtils.PI / 4) * height / 3;
		vertices[1][1] = position.y + MathUtils.sin(radians + 3 * MathUtils.PI / 4) * height / 3;
				
		vertices[2][0] = position.x + MathUtils.cos(radians - 3 * MathUtils.PI / 4) * height / 3;
		vertices[2][1] = position.y + MathUtils.sin(radians - 3 * MathUtils.PI / 4) * height / 3;
	}
	
	public void render(ShapeRenderer sr) {
		// Filled triangle
		sr.begin(ShapeType.Filled);
		sr.setColor(60/255f, 200/255f, 255/255f, 0.5f);
		sr.triangle(vertices[0][0], vertices[0][1],
				vertices[1][0], vertices[1][1],
				vertices[2][0], vertices[2][1]);
		sr.end();
		
		// Triangle outline
		float[] polygon = new float[edges * 2]; // Shape renderer polygon function only takes in 1D array
		for(int i = 0; i < edges; i ++) {
			polygon[i*2] = vertices[i][0];
			polygon[(i*2)+1] = vertices[i][1];
		}
		Gdx.gl.glLineWidth(4);
		sr.begin(ShapeType.Line);
		sr.setColor(Color.MAROON);
		sr.polygon(polygon);
		sr.end();
	}
	
	private void setHeight(int h) {
		height = h;
	}
	
	public void setTimeLeft(float f) {
		timeLeft = f;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}

}
