package server.task.impl;

import server.GameEngine;
import server.Server;
import server.task.Task;

public class ProcessShops implements Task {

	@Override
	public void execute(GameEngine context) {
		context.submitWork(new Runnable() {
			public void run() {
				Server.shopHandler.process();
				//System.out.println("ShopHandler processed at "+System.currentTimeMillis());
			}
		});
	}

}