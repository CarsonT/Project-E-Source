package server.event.impl;

import server.World;
import server.event.Event;
import server.task.impl.ProcessServer;

public class GameProcess extends Event {
	
	public static final int CYCLE_TIME = 600;
	
	public GameProcess() {
		super(CYCLE_TIME);
	}

	@Override
	public void execute() {
		this.setDelay(600-ProcessServer.finish);
		World.getWorld().submit(new ProcessServer());
	}

}
