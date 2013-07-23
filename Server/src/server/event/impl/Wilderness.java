package server.event.impl;

import server.event.Event;
import server.model.players.Client;
import server.model.players.Player;
import server.model.players.PlayerHandler;

public class Wilderness extends Event {

	public Wilderness() {
		super(2400);
	}	

	@Override
	public void execute() {	
		if (PlayerHandler.playerCount <= 0) {
			return;
		}
		synchronized (PlayerHandler.players) {
			for(Player p : PlayerHandler.players) {
				if (p != null) {
					Client c = (Client)p;
					if(p.skullTimer > 0) {
						p.skullTimer--;
						if(p.skullTimer == 1) {
							p.isSkulled = false;
							p.attackedPlayers.clear();
							p.headIconPk = -1;
							p.skullTimer = -1;
							c.getPA().requestUpdates();
						}	
					}
				}			
			}
		}
	}
}