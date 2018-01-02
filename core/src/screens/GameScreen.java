package screens;

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
	//private TextInputHandler textInputHandler;
	
	public GameScreen() {
		world = new World();
		collisionDetector = new CollisionDetector(world);
		spaceManager = new SpaceManager(world, collisionDetector);
		renderer = new Renderer(world);
		
		Gdx.input.setInputProcessor(new InputHandler(world));
		
		Gdx.input.setCursorCatched(true);
		//TextInputHandler textInputHandler = new TextInputHandler();
		//Gdx.input.getTextInput(textInputHandler, "Enter your name", null, null);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void render(float delta) {
		world.update(delta);
		collisionDetector.manage();
		spaceManager.manage();
		renderer.render();
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
