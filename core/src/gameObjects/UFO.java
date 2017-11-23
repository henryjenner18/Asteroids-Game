package gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class UFO extends SpaceObject {
	
	private float width, height, dv;
	
	Random rand = new Random();
	
	public UFO(float x, float y) {
		position = new Vector2(x, y);
		velocity = new Vector2();
		dv = rand.nextInt(31) - 15;
		edges = 8;
		vertices = new float[edges][2];
		width = 100;
		height = (float) (width * 0.5);
		numFragments = rand.nextInt(2) + 2;
		setColours();
	}
	
	public void update(float delta) {
		move(delta);
		position.add(velocity);
		wrap();
		setVertices();
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		velocity.x = delta * 20 * dv;
	}
	
	private void setColours() {
		fillColour = new int[3];
		fillColour[0] = 147;
		fillColour[1] = 112;
		fillColour[2] = 219;
		
		lineColour = new int[3];
		lineColour[0] = 0;
		lineColour[1] = 250;
		lineColour[2] = 150;
	}
	
	private void wrap() { // Screen wrap
		float w = AsteroidsMain.getWidth();
		float h = AsteroidsMain.getHeight();
		r = (int) (width / 2);
		
		if(position.x < -r) position.x = w + r;
		if(position.x > w + r) position.x = -r;
		if(position.y < -r) position.y = h + r;
		if(position.y > h + r) position.y = -r;	
	}
	
	private void setVertices() {
		vertices[0][0] = position.x - width/2;
		vertices[0][1] = position.y;
		
		vertices[5][0] = position.x + width/2;
		vertices[5][1] = position.y;
		
		vertices[1][0] = position.x - width/5;
		vertices[1][1] = position.y + height/3;
		
		vertices[4][0] = position.x + width/5;
		vertices[4][1] = position.y + height/3;
		
		vertices[7][0] = position.x - width/5;
		vertices[7][1] = position.y - height/3;
		
		vertices[6][0] = position.x + width/5;
		vertices[6][1] = position.y - height/3;
		
		vertices[2][0] = position.x - width/8;
		vertices[2][1] = (float) (position.y + 2.1*height/3);
		
		vertices[3][0] = position.x + width/8;
		vertices[3][1] = (float) (position.y + 2.1*height/3);
	}
	
	public void render(ShapeRenderer sr) {
		// Filled Polygon
		for(int i = 0; i < edges; i++) {
			sr.begin(ShapeType.Filled);
			sr.setColor(fillColour[0]/255f, fillColour[1]/255f, fillColour[2]/255f, 1);
					
			if(i == edges - 1) { // Final vertex - need to make triangle with the first vertex
				sr.triangle(vertices[i][0], vertices[i][1],
						vertices[0][0], vertices[0][1],
						position.x, position.y);
				sr.end();
			} else {
				sr.triangle(vertices[i][0], vertices[i][1],
						vertices[i+1][0], vertices[i+1][1],
						position.x, position.y);
				sr.end();
			}
		}
				
		// Polygon outline
		float[] polygon = new float[edges * 2]; // Shape renderer polygon function only takes in 1D array
		for(int i = 0; i < edges; i ++) {
			polygon[i*2] = vertices[i][0];
			polygon[(i*2)+1] = vertices[i][1];
		}
		Gdx.gl.glLineWidth(2);
		sr.begin(ShapeType.Line);
		sr.setColor(lineColour[0]/255f, lineColour[1]/255f, lineColour[2]/255f, 1);
		sr.polygon(polygon);
		sr.end();
		
		// Horizontal lines
		Gdx.gl.glLineWidth(4);
		sr.begin(ShapeType.Line);
		sr.line(vertices[0][0], vertices[0][1], vertices[5][0], vertices[5][1]);
		sr.line(vertices[1][0], vertices[1][1], vertices[4][0], vertices[4][1]);
		sr.end();
	}
	
	public int getNumFragments() {
		return numFragments;
	}
}
