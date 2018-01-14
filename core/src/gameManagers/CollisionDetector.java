package gameManagers;

import java.util.ArrayList;

import gameObjects.SpaceObject;

public class CollisionDetector {
	
	private World world;
	private ArrayList<Integer> rocketsToRemove;
	private ArrayList<Integer> asteroidsToRemove;
	private ArrayList<Integer> missilesToRemove;
	private ArrayList<Integer> ufosToRemove;
	
	public CollisionDetector(World world) {
		this.world = world;
	}

	public void manage() {
		rocketsToRemove = new ArrayList<Integer>();
		asteroidsToRemove = new ArrayList<Integer>();
		missilesToRemove = new ArrayList<Integer>();
		ufosToRemove = new ArrayList<Integer>();
		
		if(world.getNumRockets() > 0) {	
			if(world.getRocket(0).getInvincible() == false) {
				checkForCollisions(world.getRockets(), 'r', world.getAsteroids(), 'a');
				checkForCollisions(world.getRockets(), 'r', world.getMissiles(), 'm');
				checkForCollisions(world.getRockets(), 'r', world.getUFOs(), 'u');
			}
			
			checkForCollisions(world.getRockets(), 'r', world.getPowerUps(), 'p');
		}
		
		checkForCollisions(world.getMissiles(), 'm', world.getAsteroids(), 'a');
		checkForCollisions(world.getMissiles(), 'm', world.getUFOs(), 'u');
		checkForCollisions(world.getMissiles(), 'm', world.getMissiles(), 'm');
	}
	
	public void checkForCollisions(ArrayList<?> obj1, char obj1Type, ArrayList<?> obj2, char obj2Type) {
		
		for(int i = 0; i < obj1.size(); i++) {
			if(obj1.get(i) instanceof SpaceObject) {
				
				SpaceObject space1 = (SpaceObject) obj1.get(i);

				space1.linearEquation();
				float[][] obj1Vertices = space1.getVertices();
				float[] obj1Gradients = space1.getGradients();
				float[] obj1Yintercepts = space1.getYintercepts();
				int obj1Edges = space1.getEdges();
				
				for(int e = 0; e < obj1Edges; e++) {
					
					for(int a = 0; a < obj2.size(); a++) {
						if(obj2.get(a) instanceof SpaceObject) {
							
							SpaceObject space2 = (SpaceObject) obj2.get(a);
							
							((SpaceObject) obj2.get(a)).linearEquation();
							float[][] obj2Vertices = space2.getVertices();
							float[] obj2Gradients = space2.getGradients();
							float[] obj2Yintercepts = space2.getYintercepts();
							int obj2Edges = space2.getEdges();
							
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
										collisionOccurred(x, y, obj1Type, i, obj2Type, a);
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
										
										collisionOccurred(x, y, obj1Type, i, obj2Type, a);
									}
								}
							}
						}
					}
				}	
			}			
		}
	}
	
	private void collisionOccurred(float x, float y, char obj1Type, int obj1Index, char obj2Type, int obj2Index) {
		if(checkValidCollision(obj1Type, obj1Index, obj2Type, obj2Index) == true) {
			addObjectToList(obj1Type, obj1Index);
			addObjectToList(obj2Type, obj2Index);
			
			if(obj1Type == 'm' && obj2Type == 'm') { // Missile-missile collision, therefore grey boulders not required in sparks
				world.objSpawner.sparks(x, y, true);
			} else {
				world.objSpawner.sparks(x, y, false);
			}
		}
	}
	
	private boolean checkValidCollision(char obj1Type, int obj1Index, char obj2Type, int obj2Index) {
		
		if(obj1Type == 'm' && obj2Type == 'u') {
			
			if(world.getMissile(obj1Index).getCreator() == 'u') {
				return false;
			} else {
				return true;
			}
				
		} else if(obj1Type == 'r' && obj2Type == 'm') {
			if(world.getMissile(obj2Index).getCreator() == 'r') {
				return false;
			} else {
				return true;
			}
			
		} else if(obj1Type == 'r' && obj2Type == 'p') {
			world.getPowerUp(obj2Index).setTimeLeft(0);
			world.getRocket(obj1Index).setTripleMissile(true);
			world.getRocket(obj1Index).resetTripleMissileTimer();
			return false;
			
		} else {
			return true;
		}
	}

	private void addObjectToList(char z, int i) {
		switch(z) {
		case 'r': rocketsToRemove.add(i);
			break;
				
		case 'a': asteroidsToRemove.add(i);
			break;
				
		case 'm': world.getMissile(i).setTimeLeft(0);
			break;
				
		case 'u': ufosToRemove.add(i);
			break;
			
		case 'p': world.getPowerUp(i).setTimeLeft(0);
			break;
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
	
	public ArrayList<Integer> getMissilesToRemove() {
		return missilesToRemove;
	}
	
	public ArrayList<Integer> getUFOsToRemove() {
		return ufosToRemove;
	}
}
