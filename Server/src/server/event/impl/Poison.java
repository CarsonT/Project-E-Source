package server.event.impl;

import server.event.Event;
import server.model.players.Client;
import server.model.players.Player;
import server.model.players.PlayerHandler;

public class Poison extends Event {
	
	public Poison() {
		super(10000);
	}

	@Override
	public void execute() {
		if (PlayerHandler.playerCount <= 0) {
			return;
		}
		synchronized (PlayerHandler.players) {
			for(Player p : PlayerHandler.players) {
				if (p != null) {
					if (System.currentTimeMillis() - p.lastPoison > 20000 && p.poisonDamage > 0) {
						int damage = p.poisonDamage/2;			
						Client c = (Client)p;			
						if (damage > 0) {
							c.sendMessage("You are affected by the poison...");
							if (!p.getHitUpdateRequired()) {
								p.setHitUpdateRequired(true);
								p.setHitDiff(damage);
								p.updateRequired = true;
								p.poisonMask = 1;
							} else if (!p.getHitUpdateRequired2()) {
								p.setHitUpdateRequired2(true);
								p.setHitDiff2(damage);
								p.updateRequired = true;
								p.poisonMask = 2;
							}
							p.lastPoison = System.currentTimeMillis();
							p.poisonDamage--;
							p.dealDamage(damage);
						} else {
							p.poisonDamage = -1;
							c.sendMessage("You are no longer poisoned.");
						}	
					}
				}
			}
		}
	}

}
