package server.model.content;

import server.*;
import server.util.*;
import server.model.items.*;
import server.model.players.*;

public class PublicEvent {

	public static String randomChar[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "x", "1", "2", "3", "$"};
	public static boolean firstEventInProgress = false;
	public static String firstString = "";
	public static int rewardId;
	public static int rewardAmount;
	public static int lastEventTimer = 0;
	public static boolean forceFirst = false;
	
	public static void forceFirst() {
		if(firstEventInProgress)
			return;
		forceFirst = true;
	}
	
	public static void processEntry(Client player, String command) {
		if(!firstEventInProgress)
			return;
		if(command.equals(firstString)) {
			firstEventInProgress = false;
			lastEventTimer = 0;
			player.getItems().addItem(rewardId, rewardAmount);
			if(rewardAmount == 1) {
				serverMessage("[ <col=2784FF>Event </col>] <col=2784FF>" + player.playerName.substring(0,1).toUpperCase() + player.playerName.substring(1) + " </col>has won a <col=2784FF>"+ItemAssistant.getItemName2(rewardId)+"</col>!");
			} else {
				String extra = "s";
				if(ItemAssistant.getItemName2(rewardId).endsWith("s"))
					extra = "";
				serverMessage("[ <col=2784FF>Event </col>] <col=2784FF>" + player.playerName.substring(0,1).toUpperCase() + player.playerName.substring(1) + "  </col>has won <col=2784FF>"+rewardAmount+" "+ItemAssistant.getItemName2(rewardId)+""+extra+"</col>!");
			}
			rewardId = 0;
			rewardAmount = 0;
		}
	}
	
	public static String newFirstString() {
		String returns = "";
		while(returns.length() < 10)
			returns += randomChar[Misc.random(randomChar.length - 1)];
		return returns;
	}
	
	public static String constructFirstYell() {
			Misc.println("[ Project E ] Public Event has been launched!");
		if(rewardAmount == 1)
			return "[ <col=2784FF>Event </col>] The first person to type ::<col=2784FF>"+firstString+" </col>will receive a <col=2784FF>"+ItemAssistant.getItemName2(rewardId)+"</col>!";
		if(ItemAssistant.getItemName2(rewardId).endsWith("s"))
			return "[ <col=2784FF>Event </col>] The first person to type ::<col=2784FF>"+firstString+" </col>will receive <col=2784FF>"+rewardAmount+" "+ItemAssistant.getItemName2(rewardId)+"</col>!";
			return "[ <col=2784FF>Event </col>] The first person to type ::<col=2784FF>"+firstString+" </col>will receive <col=2784FF>"+rewardAmount+" "+ItemAssistant.getItemName2(rewardId)+"s</col>!";
	}
	
	public static void generateReward() {
		int rewardLevel = Misc.random(50);
		if(rewardLevel < 40) {
			rewardLevel = 1;
		} else if(rewardLevel < 49) {
			rewardLevel = 2;
		} else if(rewardLevel == 50) {
			rewardLevel = 3;
		} else {
			rewardLevel = 1;
		}
		switch(rewardLevel) {
			case 1:
				switch(Misc.random(3)) {
					case 0:
						rewardId = 995;
						rewardAmount = (Misc.random(5) + 1) * 75000;
					break;
					case 1:
						rewardId = 1334;
						rewardAmount = (Misc.random(14) + 1) * 1;
					break;
					case 2:
						rewardId = 15272;
						rewardAmount = (Misc.random(3) + 1) * 2;
					break;
					case 3:
						rewardId = 4588;
						rewardAmount = (Misc.random(2) + 1) * 1;
					break;
					
				}
			break;
			case 2:
				switch(Misc.random(3)) {
					case 0:
						rewardId = 995;
						rewardAmount = (Misc.random(15) + 5) * 80000;
					break;
					case 1:
						rewardId = 1088;
						rewardAmount = (Misc.random(3) + 1) * 3;
					break;
					case 2:
						rewardId = 1622;
						rewardAmount = (Misc.random(10) + 10) * 10;
					break;
					case 3:
						rewardId = 386;
						rewardAmount = (Misc.random(80) + 20) * 10;
					break;
					
				}
			break;
			case 3:
				switch(Misc.random(5)) {
					case 0:
						rewardId = 15273;
						rewardAmount = (Misc.random(50) + 10) * 15;
					break;
					case 1:
						rewardId = 391;
						rewardAmount = (Misc.random(100) + 30) * 10;
					break;
					case 2:
						rewardId = 4151;
						rewardAmount = 1;
					break;
					case 3:
						rewardId = 1725;
						rewardAmount = 15;
					break;
					case 4:
						rewardId = 10828;
						rewardAmount = 1;
					break;
					case 5:
						rewardId = 1216;
						rewardAmount = 10;
					break;
				}
			break;
		}
	}
	
	public static void executeFirstEvent() {
		forceFirst = false;
		firstString = newFirstString();
		generateReward();
		firstEventInProgress = true;
		lastEventTimer = 600;
		serverMessage(constructFirstYell());
	}
	
	public static void process() {
		if((Misc.random(1500) == 0 || forceFirst) && !firstEventInProgress)
			executeFirstEvent();
		if(lastEventTimer > 0)
			lastEventTimer --;
		if(lastEventTimer == 0)
			firstEventInProgress = false;
	}
	
	public static void serverMessage(String s) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if(Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				c.sendMessage(s);
			}
		}
	}
	
}