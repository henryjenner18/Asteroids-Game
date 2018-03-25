package screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import gameHelpers.InputHandler;
import gameManagers.CollisionDetector;
import gameManagers.Renderer;
import gameManagers.SpaceManager;
import gameManagers.World;

public class GameScreen implements Screen {
	
	private World world;
	private CollisionDetector collisionDetector;
	private SpaceManager spaceManager;
	private Renderer renderer;
	private static ArrayList<Float> deltas;
	
	public GameScreen() {
		world = new World();
		collisionDetector = new CollisionDetector(world);
		spaceManager = new SpaceManager(world, collisionDetector);
		renderer = new Renderer(world);
		deltas = new ArrayList<Float>();
		
		Gdx.input.setInputProcessor(new InputHandler(world));	
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void render(float delta) {
		addDelta(delta);	
		world.update(delta);
		collisionDetector.manage();
		spaceManager.manage();
		renderer.render(delta);
	}
	
	private void addDelta(float delta) {
		deltas.add(delta);
	}
	
	public static float getAvgDelta() {
		int numDeltas = deltas.size();
		float sum = 0;
		
		for(int i = 0; i < numDeltas; i++) {
			sum += deltas.get(i);
		}
		
		float avgDelta = sum / numDeltas;
		
		return avgDelta;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
