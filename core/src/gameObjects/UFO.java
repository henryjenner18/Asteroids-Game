package gameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameManagers.World;
import screens.GameScreen;

public class UFO extends SpaceObject {
	
	private float height, dv, countdown;
	private int terminalVel;
	private int[] flashColour;
	
	public UFO(World world, float x, float y, boolean daughter) {
		super(world);
		position = new Vector2(x, y);
		velocity = new Vector2();
		edges = 8;
		vertices = new float[edges][2];
		
		float ran;
		if(daughter == true) { 
			ran = randFloatInRange(0.7, 0.8);
		} else {
			ran = randFloatInRange(0.8, 1);
		}
		
		height = r = 45 * ran; //41
		dv = 0;
		
		while(dv == 0) {
			dv = rand.nextInt(5) - 2;
		}
		
		terminalVel = 6;
		score = (int) (1000 / ran);
		resetCountdown();
		setColours(daughter);
	}
	
	private void setFlashColour() {
		flashColour = new int[3];
		
		for(int i = 0; i < 3; i++) {
			flashColour[i] = rand.nextInt(256);
		}	
	}
	
	private void setColours(boolean daughter) {
		if(daughter == false) {
			fillColour = new int[3];
			fillColour[0] = 102;
			fillColour[1] = 255;
			fillColour[2] = 102;
			
			missileColour = new int[3];
			missileColour[0] = 250;
			missileColour[1] = 0;
			missileColour[2] = 0;
		} else {
			fillColour = new int[3];
			fillColour[0] = 255;
			fillColour[1] = 153;
			fillColour[2] = 200;
			
			missileColour = new int[3];
			missileColour[0] = 204;
			missileColour[1] = 0;
			missileColour[2] = 102;
		}
		
		
		lineColour = new int[3];
		lineColour[0] = 125;
		lineColour[1] = 50;
		lineColour[2] = 255;
		
		
		
		setFlashColour();
	}

	public void update(float delta) {
		countdown -= delta;
		moveInRelationToRocket(delta);
		terminalVelCheck();
		position.add(velocity);
		wrap();
		setVertices();
		checkCountdown(delta);
		setFlashColour();
		spawnCloneCheck();
	}
	
	private void spawnCloneCheck() {
		int n = rand.nextInt(600);
		if(n == 0) {
			world.objSpawner.ufoDaughter(getX(), getY());
		}	
	}
	
	private void findMissileAngle() {
		int numRockets = world.getNumRockets();
		
		for(int i = 0; i < numRockets; i++) {
			Rocket target = world.getRocket(i);	
			Vector2 targPos = new Vector2(target.getX(), target.getY());
			Vector2 targVel = target.getVelocity();	
			
			Vector2 origPos = position;
			Vector2 origVel = velocity;		
			
			double speed = GameScreen.getAvgDelta() * missileV;
			
			Vector2 relPos = new Vector2(targPos.x - origPos.x, targPos.y - origPos.y);
			Vector2 relVel = new Vector2(targVel.x - origVel.x, targVel.y - origVel.y);
			
			double a = dotProduct(relVel, relVel) - Math.pow(speed, 2);
			double b = dotProduct(relPos, relVel) * 2;
			double c = dotProduct(relPos, relPos);
			
			double D = Math.pow(b, 2) - (4 * a * c);
			
			if(D >= 0) { // There are real root(s)
				double t = 0;
				boolean foundPosT = false;
				
				if(D == 0) { // One repeated real root
					t = -b / (2 * a);
					
					if(t > 0) {
						foundPosT = true;
					}
					
				} else { // Two real roots
					double q = Math.sqrt(D); // Let q equal the square root of the discriminant
					double r0 = (-b - q) / (2 * a);
					double r1 = (-b + q) / (2 * a);
					
					if(r0 > 0) { // We know r1 > r0, but need to find the smallest positive root
						t = r0;
						foundPosT = true;
					} else if (r1 > 0) {
						t = r1;
						foundPosT = true;
					}
				}
				
				if(foundPosT == true) {
					double vx = (relPos.x / t) + relVel.x;
					double vy = (relPos.y / t) + relVel.y;
					
					double alpha = Math.toDegrees(Math.atan(vy / vx));
					
					if(vx < 0) {
						alpha += 180;
					}
					
					alpha += rand.nextInt(2 * world.getUFOAccuracy() + 1) - world.getUFOAccuracy();
		
					shootMissile(alpha);
				}				
			}
		}
	}
	
	private double dotProduct(Vector2 a, Vector2 b) {
		double dotP = (a.x * b.x) + (a.y * b.y);
		return dotP;
	}
	
	
	
	private void shootMissile(double heading) {
		world.objSpawner.missile('u', 1, position.x, position.y, heading, 0, velocity, missileV, missileColour);
	}
	
	private void checkCountdown(float delta) {
		if(countdown <= 0) {
			findMissileAngle();
			resetCountdown();
		}
	}
	
	private void resetCountdown() {
		countdown = 3;
	}
	
	private void moveInRelationToRocket(float delta) {
		int numRockets = world.getNumRockets();
		
		for(int a = 0; a < numRockets; a++) {
			Rocket rk = world.getRocket(a);
			Vector2 force = new Vector2();
			Vector2 rocket = new Vector2(rk.getX(), rk.getY());
			
			float i = rocket.x - position.x;
			float j = rocket.y - position.y;
			double r = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
			
			double theta = Math.atan(j / i) * (180 / Math.PI);
			if(position.x > rocket.x) {
				theta += 180;
			}
			
			double mag = Math.pow(r, 2) * Math.pow(delta, 4);
				
			float radians = (float) Math.toRadians(theta);		
			force.x = (float) (MathUtils.cos(radians) * mag);
			force.y = (float) (MathUtils.sin(radians) * mag);
			
			if(r <= 700) {
				force.x = force.x * -2;
				force.y = force.y * -2;
			}
			velocity.add(force);
		}
	}
	
	private void terminalVelCheck() {
		double resultantVel = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2)); // Pythagoras' theorem
		
		if(resultantVel > terminalVel) {
			velocity.x = (float) ((velocity.x / resultantVel) * terminalVel); // Reduce x and y components
			velocity.y = (float) ((velocity.y / resultantVel) * terminalVel);
		}
	}
	
	private void setVertices() {
		vertices[0][0] = position.x - r;
		vertices[0][1] = position.y;
		
		vertices[5][0] = position.x + r;
		vertices[5][1] = position.y;
		
		vertices[1][0] = position.x - 2*r/5;
		vertices[1][1] = position.y + height/3;
		
		vertices[4][0] = position.x + 2*r/5;
		vertices[4][1] = position.y + height/3;
		
		vertices[7][0] = position.x - 2*r/5;
		vertices[7][1] = position.y - height/3;
		
		vertices[6][0] = position.x + 2*r/5;
		vertices[6][1] = position.y - height/3;
		
		vertices[2][0] = position.x - r/4;
		vertices[2][1] = (float) (position.y + 2.1*height/3);
		
		vertices[3][0] = position.x + r/4;
		vertices[3][1] = (float) (position.y + 2.1*height/3);		
	}
	
	public int[] getFlashColour() {
		return flashColour;
	}
}
