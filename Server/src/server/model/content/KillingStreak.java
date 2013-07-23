package server.model.content;

import server.model.players.Client;
import server.Server;
import server.model.players.PlayerHandler;
import server.model.players.Player;
import server.Config;

/**
 * Killing Streak System
 * 
 * @author Dakota Chest
 * Modified for Rockstar Jax
 */

public class KillingStreak {

	Client c;
	private int maxKC = Config.MAX_KC;

	public KillingStreak(Client c) {
		this.c = c;
	}

	/**
	 * Sends the message throughout the server
	 */

	public void yell(String msg) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c2 = (Client)Server.playerHandler.players[j];
				c2.sendMessage(msg);
			}
		}	
	}

	/**
	 * Add rewards to the player who died
	 * with the kill streak
	 * 
	 */
	public int reward;
	public void Rewards() {
		Client o = (Client) PlayerHandler.players[c.killerId];
		int cash = 1000000; //1M
		reward = o.killStreak * cash;
		for(int i = 1; i < o.killStreak; i++) {
			if(o.killStreak == i) {
				c.getItems().addItem(4067, reward);
			}
		}
	}

	/**
	 * Checks the player if they have
	 * a killstreak of 2 or more
	 * can add on
	 * 
	 */

	public void checkKillStreak() {
		int rewardforbounty = 1;
		int bountyreward = c.killStreak * rewardforbounty;
		for(int i = 5; i < maxKC; i++) {
			if(c.killStreak == i) {
			yell("[ <col=2784FF>PvP System </col>] <col=2784FF>" +c.playerName+ "</col>, is on a killing streak of <col=2784FF>" +c.killStreak+ "</col>. His Bounty: <col=2784FF>" +bountyreward+ "M</col>.");
		}		
	}
	}

	/**
	 * Checks if the player with the killstreak
	 * died add items to the killer
	 * 
	 */	

	public void killedPlayer() {
		Client o = (Client) PlayerHandler.players[c.killerId];
		if (o.killStreak >= 2 || c.killerId != o.playerId) {
			yell("[ <col=FF0000>PvP System </col>] <col=FF0000>"+c.playerName+"</col> has ended <col=FF0000>"+o.playerName+" </col>killing streak of <col=FF0000>"+o.killStreak+"</col>, and was rewarded.");
			Rewards();
		}
	}	  
}