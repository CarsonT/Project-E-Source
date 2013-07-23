package server.task.impl;

import server.GameEngine;
import server.Server;
import server.World;
import server.task.Task;
import server.task.ConsecutiveTask;
import server.model.minigames.*;
import server.model.content.PublicEvent;
import server.event.CycleEventHandler;

public class ProcessServer implements Task {

	public static long finish = 0;

	@Override
	public void execute(GameEngine context) {
		context.submitWork(new Runnable() {
			public void run() {
				try {
					long start = System.currentTimeMillis();
					World.getWorld().submit(new ConsecutiveTask(new ProcessPlayerHandler(), new ProcessItems(), new ProcessObjects(), new ProcessShops(), new ProcessNPCs()));
					Server.pestControl.process();
					Server.publicEvent.process();
					//Server.monsterKiller.process();
					Server.lottery.process();
					CycleEventHandler.getSingleton().process();
					Server.totalCycleTime += Server.cycleTime;
					Server.cycles++;
					Server.debug();
					//Server.handleMemoryUsage();
					finish = System.currentTimeMillis() - start;				
				} catch(Exception e) {
					World.getWorld().handleError(e);
				}
			}
		});
	}

}