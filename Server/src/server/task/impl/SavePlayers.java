package server.task.impl;

import server.GameEngine;
import server.World;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.model.players.PlayerSave;
import server.task.Task;

public class SavePlayers implements Task {

	@Override
	public void execute(GameEngine context) {
		if (PlayerHandler.players.length == 0)
			return;
		try {
			for(int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					PlayerSave.saveGame((Client)PlayerHandler.players[i]);
					//PlayerSave.saveGame2((Client)PlayerHandler.players[i]);
					System.out.println("Auto-Saving for "+PlayerHandler.players[i].playerName);
				}	
			}
		} catch (Exception e) {
			World.getWorld().handleError(e);
		}		
	}	

}
