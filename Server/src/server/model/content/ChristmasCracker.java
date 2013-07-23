package server.model.content;

import server.model.players.Client;
import server.model.items.*;
import server.event.Event;
import server.util.Misc;
import server.Server;


public class ChristmasCracker {

	public void handleCrackers(Client c, int itemId, int playerId) {
		Client usedOn = (Client) Server.playerHandler.players[playerId];
		if (!c.getItems().playerHasItem(itemId))
			return;
		
		if (usedOn.getItems().freeSlots() < 1) {
			c.sendMessage("The other player doesn't have enough inventory space!");
			return;
		}
		
		c.sendMessage("You crack the cracker...");
		c.getItems().deleteItem(itemId, 1);
		
		if (Misc.random(1) == 0) {
			c.getItems().addItem(getRandomPhat(), 1);
			c.sendMessage("You got the prize!");
			usedOn.sendMessage("You didn't get the prize.");
		} else {
			usedOn.getItems().addItem(getRandomPhat(), 1);
			usedOn.sendMessage("You got the prize!");
			c.sendMessage("You didn't get the prize.");
		}
	}

	public int getRandomPhat() {
		int[] phats = { 1038, 1040, 1042, 1044, 1048 };
		return phats[(int) Math.floor(Math.random() * phats.length)];
	}
	
}