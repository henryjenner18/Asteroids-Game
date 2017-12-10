package gameManagers;

import java.util.ArrayList;

import gameObjects.SpaceObject;

public class CollisionDetector {
	
	private World world;
	private ArrayList<Integer> rocketsToRemove;
	private ArrayList<Integer> asteroidsToRemove;
	private ArrayList<Integer> missilesToRemove;
	private ArrayList<Integer> UFOsToRemove;
	
	public CollisionDetector(World world) {
		this.world = world;
	}

	public void manage() {
		rocketsToRemove = new ArrayList<Integer>();
		asteroidsToRemove = new ArrayList<Integer>();
		missilesToRemove = new ArrayList<Integer>();
		UFOsToRemove = new ArrayList<Integer>();
		
		checkForCollisions(world.getRockets(), 'r', world.getAsteroids(), 'a');
		checkForCollisions(world.getMissiles(), 'm', world.getAsteroids(), 'a');
		checkForCollisions(world.getRockets(), 'r', world.getMissiles(), 'm');
		checkForCollisions(world.getRockets(), 'r', world.getUFOs(), 'u');
		checkForCollisions(world.getMissiles(), 'm', world.getUFOs(), 'u');
		checkForCollisions(world.getMissiles(), 'm', world.getMissiles(), 'm');
		//checkForCollisions(world.getUFOs(), 'u', world.getUFOs(), 'u');
	}
	
	public void checkForCollisions(ArrayList<?> obj1, char obj1Type, ArrayList<?> obj2, char obj2Type) {
		
		for(int i = 0; i < obj1.size(); i++) {
			if(obj1.get(i) instanceof SpaceObject) {

				((SpaceObject) obj1.get(i)).linearEquation();
				float[][] obj1Vertices = ((SpaceObject) obj1.get(i)).getVertices();
				float[] obj1Gradients = ((SpaceObject) obj1.get(i)).getGradients();
				float[] obj1Yintercepts = ((SpaceObject) obj1.get(i)).getYintercepts();
				int obj1Edges = ((SpaceObject) obj1.get(i)).getEdges();
				
				for(int e = 0; e < obj1Edges; e++) {
					
					for(int a = 0; a < obj2.size(); a++) {
						if(obj2.get(a) instanceof SpaceObject) {
							
							((SpaceObject) obj2.get(a)).linearEquation();
							float[][] obj2Vertices = ((SpaceObject) obj2.get(a)).getVertices();
							float[] obj2Gradients = ((SpaceObject) obj2.get(a)).getGradients();
							float[] obj2Yintercepts = ((SpaceObject) obj2.get(a)).getYintercepts();
							int obj2Edges = ((SpaceObject) obj2.get(a)).getEdges();
							
							for(int q = 0; q < obj2Edges; q++) {
								
								boolean obj1InfG = infinityGradient(obj1Gradients[e]); // Need variables to tell which obj has the infinity gradient
								boolean obj2InfG = infinityGradient(obj2Gradients[q]);
								float x = 0, y = 0;
								
								float obj1XCoords[] = getXCoords(obj1Vertices, e, obj1Edges);
								float obj2XCoords[] = getXCoords(obj2Vertices, q, obj2Edges);
								
								if(obj1InfG == false && obj2InfG == false) { // No infinity gradient
									
									x = (obj2Yintercepts[q] - obj1Yintercepts[e]) / (obj1Gradients[e] - obj2Gradients[q]);
									
									if(coordRangeCheck(x, obj1XCoords, obj2XCoords) == true) {	
										
										y = (obj1Gradients[e] * x) + obj1Yintercepts[e];
										//System.out.println(f + ": collision occurred: no inf; " + obj1Type + "(" + i +"):[" + e +"] with "+ obj2Type + "(" + a +"):[" + q +"]");
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
										//System.out.println(f + ": collision occurred: inf; " + obj1Type + "(" + i +"):[" + e +"] with "+ obj2Type + "(" + a +"):[" + q +"]");
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
			world.spawnSparks(x, y);
		}
	}
	
	private boolean checkValidCollision(char obj1Type, int obj1Index, char obj2Type, int obj2Index) {
		
		if(obj1Type == 'm' && obj2Type == 'u') {
			
			if(world.getMissile(obj1Index).getCreator() == 'u') {
				return false;
			} else {
				return true;
			}
			
		} else if(obj1Type == 'u' && obj2Type == 'm') {
			if(world.getMissile(obj2Index).getCreator() == 'u') {
				return false;
			} else {
				return true;
			}
		
		} else if(obj1Type == 'm' && obj2Type == 'r') {
			
			if(world.getMissile(obj1Index).getCreator() == 'r') {
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
				
		case 'u': UFOsToRemove.add(i);
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
		return UFOsToRemove;
	}
}
