package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import gameHelpers.InputHandler;
import gameWorld.GameRenderer;
import gameWorld.GameWorld;

public class GameScreen implements Screen { // Implementing methods of the screen interface
	// GameScreen class does not do any rendering/updating itself
	
	private GameWorld world;
	private GameRenderer renderer;
	
	public GameScreen() {
		world = new GameWorld(); // Initialise world
		renderer = new GameRenderer(world); // Initialise renderer; can retrieve objects from world
		
		Gdx.input.setInputProcessor(new InputHandler(world.getRocket()));
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) { // Renders the game each second; delta is the time since last called
		world.update(delta); // Updates all game objects
		renderer.render(); // Render all game objects
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}

}
