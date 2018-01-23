package gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameManagers.World;

public class Shield extends SpaceObject {
	
	private float lifespan, timeLeft, dAngle, targetR, inc, dh;

	public Shield(World world) {
		super(world);
		position = new Vector2();
		edges = 5;
		vertices = new float[edges][2];
		r = 0;
		setColours();
		lifespan = 10;
		resetTimeLeft();
		dh = 0;
		targetR = 65;
		dAngle = 3 * (360 / edges);
		inc = dAngle / targetR;
	}
	
	public void update(float delta, float x, float y, double heading) {
		timeLeft -= delta;
		if(timeLeft <= 0) {
			setTimeLeft(0);
		}
		
		position.x = x;
		position.y = y;
		this.heading = heading;
		setVertices();
		
		if(r < targetR) {
			r += 0.5;
			dh += inc;
		}
		
		setColours();
	}

	private void setVertices() {
		float a = 360 / edges; // Regular difference between vertices
		float radians;
		
		for(int i = 0; i < edges; i++) {
			radians = (float) Math.toRadians(a * i + heading + dh);
			
			vertices[i][0] = position.x + MathUtils.cos(radians) * r;
			vertices[i][1] = position.y + MathUtils.sin(radians) * r;
		}
	}
	
	private void setColours() {
		fillColour = new int[3];
		lineColour = new int[3];
		
		fillColour[0] = 40;
		fillColour[1] = 100;
		fillColour[2] = 150;
				
		lineColour[0] = rand.nextInt(255);
		lineColour[1] = rand.nextInt(255);
		lineColour[2] = rand.nextInt(255);
	}
	
	public void setTimeLeft(float t) {
		timeLeft = t;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
	
	public void resetTimeLeft() {
		timeLeft = lifespan;
	}
}
