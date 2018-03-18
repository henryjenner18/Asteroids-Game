package gameManagers;

import java.util.ArrayList;

import gameHelpers.AssetLoader;
import gameObjects.SpaceObject;

public class CollisionDetector {
	
	private World world;
	private ArrayList<Integer> rocketsToRemove;
	private ArrayList<Integer> asteroidsToRemove;
	private ArrayList<Integer> ufosToRemove;
	
	public CollisionDetector(World world) {
		this.world = world;
	}

	public void manage() {
		rocketsToRemove = new ArrayList<Integer>();
		asteroidsToRemove = new ArrayList<Integer>();
		ufosToRemove = new ArrayList<Integer>();
		
		checkForCollisions(world.getShields(), world.getAsteroids());
		checkForCollisions(world.getShields(), world.getUFOs());
		checkForCollisions(world.getMissiles(), world.getShields());
		checkForCollisions(world.getMissiles(), world.getAsteroids());
		checkForCollisions(world.getMissiles(), world.getUFOs());
		checkForCollisions(world.getMissiles(), world.getMissiles());
		
		if(world.getNumRockets() > 0) {
			if(!world.getRocket(0).getShield()) {
				checkForCollisions(world.getRockets(), world.getAsteroids());
				checkForCollisions(world.getRockets(), world.getMissiles());
				checkForCollisions(world.getRockets(), world.getUFOs());	
			}
				
			checkForCollisions(world.getRockets(), world.getPowerUps());
		}
	}
	
	private void checkForCollisions(ArrayList<?> obj1, ArrayList<?> obj2) {
		
		for(int i = 0; i < obj1.size(); i++) {
			if(obj1.get(i) instanceof SpaceObject) {
				
				SpaceObject space1 = (SpaceObject) obj1.get(i);
				space1.linearEquation();
				float[][] obj1Vertices = space1.getVertices();
				float[] obj1Gradients = space1.getGradients();
				float[] obj1Yintercepts = space1.getYintercepts();
				int obj1Edges = space1.getEdges();
				String obj1Class = space1.getClass().getName();
				
				for(int e = 0; e < obj1Edges; e++) {
					
					for(int a = 0; a < obj2.size(); a++) {
						if(obj2.get(a) instanceof SpaceObject) {
							
							SpaceObject space2 = (SpaceObject) obj2.get(a);
							
							((SpaceObject) obj2.get(a)).linearEquation();
							float[][] obj2Vertices = space2.getVertices();
							float[] obj2Gradients = space2.getGradients();
							float[] obj2Yintercepts = space2.getYintercepts();
							int obj2Edges = space2.getEdges();
							String obj2Class = space2.getClass().getName();
							
							for(int q = 0; q < obj2Edges; q++) {
								
								boolean obj1InfG = infinityGradient(obj1Gradients[e]);
								boolean obj2InfG = infinityGradient(obj2Gradients[q]);
								float x = 0, y = 0;
								
								float obj1XCoords[] = getXCoords(obj1Vertices, e, obj1Edges);
								float obj2XCoords[] = getXCoords(obj2Vertices, q, obj2Edges);
								
								if(obj1InfG == false && obj2InfG == false) { // No infinity gradient
									
									x = (obj2Yintercepts[q] - obj1Yintercepts[e]) / (obj1Gradients[e] - obj2Gradients[q]);
									
									if(coordRangeCheck(x, obj1XCoords, obj2XCoords) == true) {	
										
										y = (obj1Gradients[e] * x) + obj1Yintercepts[e];
										collisionOccurred(x, y, obj1Class, i, obj2Class, a);
									}

								} else { // Infinity gradient
									
									float obj1YCoords[] = getYCoords(obj1Vertices, e, obj1Edges);
									float obj2YCoords[] = getYCoords(obj2Vertices, q, obj2Edges);
									
									if(obj1InfG == true && obj2InfG == false) { // Obj1 has the infinity gradient
										x = obj1Vertices[0][0];
										y = (obj2Gradients[q] * x) + obj2Yintercepts[q];
									
									} else if (obj1InfG == false && obj2InfG == true) { // Obj2 has the infinity gradient
										x = obj2Vertices[0][0];
										y = (obj1Gradients[e] * x) + obj1Yintercepts[e];
									}
									
									if(coordRangeCheck(y, obj1YCoords, obj2YCoords) == true &&
											coordRangeCheck(x, obj1XCoords, obj2XCoords) == true) {
										
										collisionOccurred(x, y, obj1Class, i, obj2Class, a);
									}
								}
							}
						}
					}
				}	
			}			
		}
	}
	
	private void collisionOccurred(float x, float y, String obj1Class, int obj1Index, String obj2Class, int obj2Index) {
		if(checkValidCollision(x, y, obj1Class, obj1Index, obj2Class, obj2Index) == true) {
			addObjectToList(obj1Class, obj1Index);
			addObjectToList(obj2Class, obj2Index);
			
			if((obj1Class == "gameObjects.Missile" && obj2Class == "gameObjects.Missile") ||
					(obj1Class == "gameObjects.Missile" && obj2Class == "gameObjects.Shield")) { // Missile-missile collision, therefore grey boulders not required in sparks
				world.objSpawner.sparks(x, y, true);
			} else {
				world.objSpawner.sparks(x, y, false);
			}
		}
	}
	
	private boolean checkValidCollision(float x, float y, String obj1Class, int obj1Index, String obj2Class, int obj2Index) {
		
		if(obj1Class == "gameObjects.Missile") {
			
			if(obj2Class == "gameObjects.UFO") {
				if(world.getMissile(obj1Index).getCreator() == 'u') {
					return false;
				} else {
					return true;
				}
				
			} else if(obj2Class == "gameObjects.Shield") {
				if(world.getMissile(obj1Index).getCreator() == 'r') {
					return false;
				} else {
					return true;
				}
			}
			
		} else if(obj1Class == "gameObjects.Rocket") { // Rocket involved
			if(obj2Class == "gameObjects.Missile") { // Missile involved
				
				// If missile belongs to the rocket, or rocket is invincible do not collide
				if((world.getMissile(obj2Index).getCreator() == 'r')
						|| world.getRocket(0).getInvincible() == true) {
					
					world.getMissile(obj2Index).setTimeLeft(0); // But remove missile
					world.objSpawner.sparks(world.getMissile(obj2Index).getX(), world.getMissile(obj2Index).getY(), true); // Create sparks
					
					return false;
				} else {
					return true;
				}
				
			} else if(obj2Class == "gameObjects.PowerUp") { // Power up involved
				world.getPowerUp(obj2Index).setTimeLeft(0);
				
				if(world.getPowerUp(obj2Index).getType() == 0) {
					world.getRocket(obj1Index).setTripleMissile(true);
					world.getRocket(obj1Index).resetTripleMissileTimer();
				
				} else if(world.getPowerUp(obj2Index).getType() == 1) {
					world.setClearScreen(true);
				
				} else if(world.getPowerUp(obj2Index).getType() == 2) {
					world.objSpawner.shield();
				
				} else if(world.getPowerUp(obj2Index).getType() == 3) {
					world.getRocket(obj1Index).setContinuousFire(true);
				}
				
				AssetLoader.powerUp.play(0.4f);
				
				return false;
			
			} else if(world.getRocket(0).getInvincible() == true) {
				addObjectToList(obj2Class, obj2Index); // Still remove obj2
				world.objSpawner.sparks(x, y, false); // Create sparks
				return false; // Don't collide rocket as it's invincible
			}
			
		} else { // Collide in any other rocket situation
			return true;
		}
		
		return true; // Collide if not mentioned
	}

	private void addObjectToList(String z, int i) {

		if(z == "gameObjects.Rocket") {
			rocketsToRemove.add(i);
			
		} else if(z == "gameObjects.Asteroid") {
			asteroidsToRemove.add(i);
			
		} else if(z == "gameObjects.Missile") {
			world.getMissile(i).setTimeLeft(0);
			
		} else if(z == "gameObjects.UFO") {
			ufosToRemove.add(i);
			
		} else if(z == "gameObjects.PowerUp") {
			world.getPowerUp(i).setTimeLeft(0);
		}
	}
	
	private boolean coordRangeCheck(float z, float[] aCoords, float[] bCoords) {
		boolean aCheck = false;
		boolean bCheck = false;
		
		if(max2(aCoords[0], aCoords[1]) == aCoords[0]) {
			if(z <= aCoords[0] && z >= aCoords[1]) {
				// Acceptable coord for a edge
				aCheck = true;
			}
		} else {
			if(z <= aCoords[1] && z >= aCoords[0]) {
				// Acceptable coord for a edge
				aCheck = true;
			}
		}
		
		if(max2(bCoords[0], bCoords[1]) == bCoords[0]) {
			if(z <= bCoords[0] && z >= bCoords[1]) {
				// Acceptable coord for b edge
				bCheck = true;
			}
		} else {
			if(z <= bCoords[1] && z >= bCoords[0]) {
				// Acceptable coord for b edge
				bCheck = true;
			}
		}
		
		if(aCheck == true && bCheck == true) {
			// Acceptable coord for both
			return true;
		} else {
			return false;
		}
	}
	
	private float max2(float a, float b) {
		if(a > b) {
			return a;
		} else {
			return b;
		}
	}
	
	private float[] getXCoords(float[][] vertices, int edge, int totalEdges) {
		float[] xCoords = new float[2];
		
		xCoords[0] = vertices[edge][0];
		
		if(edge == totalEdges - 1) {
			xCoords[1] = vertices[0][0];
		} else {
			xCoords[1] = vertices[edge + 1][0];
		}
		
		return xCoords;
	}
	
	private float[] getYCoords(float[][] vertices, int edge, int totalEdges) {
		float[] yCoords = new float[2];
		
		yCoords[0] = vertices[edge][1];
		
		if(edge == totalEdges - 1) {
			yCoords[1] = vertices[0][1];
		} else {
			yCoords[1] = vertices[edge + 1][1];
		}
		
		return yCoords;
	}
	
	private boolean infinityGradient(float m) {
		if(m == Double.POSITIVE_INFINITY || m == Double.NEGATIVE_INFINITY) {
			return true;
		} else
			return false;
	}
	
	public ArrayList<Integer> getRocketsToRemove() {
		return rocketsToRemove;
	}
	
	public ArrayList<Integer> getAsteroidsToRemove() {
		return asteroidsToRemove;
	}
	
	public ArrayList<Integer> getUFOsToRemove() {
		return ufosToRemove;
	}
}
