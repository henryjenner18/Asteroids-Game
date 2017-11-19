package gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import main.AsteroidsMain;

public class UFO extends SpaceObject {
	private float width, height;
	
	public UFO(float x, float y) {
		position = new Vector2(x, y);
		velocity = new Vector2();
		edges = 8;
		vertices = new float[edges][2];
		width = 100;
		height = (float) (width * 0.5);
	}
	
	public void update(float delta) {
		move(delta);
		position.add(velocity);
		wrap();
		setVertices();
	}
	
	private void move(float delta) {
		velocity.setZero(); // Wipes the current velocity vector
		velocity.x = delta * 100;
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
			sr.setColor(51/255f, 51/255f, 255/255f, 1);
					
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
		sr.setColor(Color.LIGHT_GRAY);
		sr.polygon(polygon);
		sr.end();
		
		// Horizontal lines
		Gdx.gl.glLineWidth(3);
		sr.begin(ShapeType.Line);
		sr.setColor(Color.LIME);
		sr.line(vertices[0][0], vertices[0][1], vertices[5][0], vertices[5][1]);
		sr.line(vertices[1][0], vertices[1][1], vertices[4][0], vertices[4][1]);
		sr.end();
	}
}
