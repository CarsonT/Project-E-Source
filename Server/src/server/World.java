package server;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import server.event.impl.CleanupEvent;
import server.event.Event;
import server.event.EventManager;
import server.event.impl.GameProcess;
import server.event.impl.SavingEvent;
import server.event.impl.CastleWars2;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.model.players.PlayerSave;
import server.model.npcs.NPCDrops;
import server.task.Task;
import server.util.BlockingExecutorService;
import server.event.impl.Wilderness;
/**
 * Holds data global to the game world.
 * @author Graham Edgecombe
 *
 */
public class World {
	
	/**
	 * Logging class.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());
	
	/**
	 * World instance.
	 */
	private static final World world = new World();
	
	/**
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return world;
	}
	
	/**
	 * An executor service which handles background loading tasks.
	 */
	private BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newSingleThreadExecutor());

	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	/**
	 * The event manager.
	 */
	private EventManager eventManager;


	/**
	 * Creates the world and begins background loading tasks.
	 */
	
	public World() {
		backgroundLoader.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				NPCDrops.initialize();
				return null;
			}
		});
		backgroundLoader.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Connection.initialize();
				return null;
			}
		});
		
	}
	
	/**
	 * Gets the background loader.
	 * @return The background loader.
	 */
	public BlockingExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}

	/**
	 * Initialises the world: loading configuration and registering global
	 * events.
	 * @param engine The engine processing this world's tasks.
	 * @throws IOException if an I/O error occurs loading configuration.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 * @throws IllegalStateException if the world is already initialised.
	 */
	public void init(GameEngine engine) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(this.engine != null) {
			throw new IllegalStateException("The world has already been initialised.");
		} else {
			this.engine = engine;
			this.eventManager = new EventManager(engine);
			this.registerGlobalEvents();
		}
	}
	
	/**
	 * Registers global events such as updating.
	 */
	private void registerGlobalEvents() {
		submit(new GameProcess());
		submit(new CleanupEvent());
		submit(new SavingEvent());
		submit(new Wilderness());
		submit(new CastleWars2());
	}
	
	/**
	 * Submits a new event.
	 * @param event The event to submit.
	 */
	public void submit(Event event) {
		this.eventManager.submit(event);
	}

	/**
	 * Submits a new task.
	 * @param task The task to submit.
	 */
	public void submit(Task task) {
		this.engine.pushTask(task);
	}
	
	/**
	 * Gets the game engine.
	 * @return The game engine.
	 */
	public GameEngine getEngine() {
		return engine;
	}
	
	/**
	 * Handles an exception in any of the pools.
	 * @param t The exception.
	 */
	public void handleError(Throwable t) {
		logger.severe("An error occurred in an executor service! The server will be halted immediately.");
		t.printStackTrace();
	}
	
}
