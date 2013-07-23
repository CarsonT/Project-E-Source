package server.event.impl;

import server.event.Event;
import server.model.players.Client;
import server.model.players.Player;
import server.model.players.PlayerHandler;

public class RestoreStats extends Event {

	public RestoreStats() {
		super(60000);
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
					p.restoreStatsDelay = System.currentTimeMillis();
					for (int level = 0; level < p.playerLevel.length; level++)  {
						if (p.playerLevel[level] < p.getLevelForXP(p.playerXP[level])) {
							if(level != 5) { // prayer doesn't restore
								p.playerLevel[level] += 1;
								c.getPA().setSkillLevel(level, p.playerLevel[level], p.playerXP[level]);
								c.getPA().refreshSkill(level);
							}
						} else if (p.playerLevel[level] > p.getLevelForXP(p.playerXP[level])) {
							p.playerLevel[level] -= 1;
							c.getPA().setSkillLevel(level, p.playerLevel[level], p.playerXP[level]);
							c.getPA().refreshSkill(level);
						}
					}
				}			
			}
		}
	}
}