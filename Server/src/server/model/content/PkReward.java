package server.model.content;

import server.model.shops.ShopAssistant;
import server.model.players.Client;
import server.model.items.*;
import server.event.Event;
import server.util.Misc;
import server.Server;
import server.World;

/**
 * Handles The Random Pk Points Reward
 * @author - Aintaro
 * Heavily Modified - Play Boy
 */

public class PkReward {

	private Client c;
	
	public PkReward(Client c) {
		this.c = c;
	}
	
	int winningItem;
	
	int[] lotteryItems = {4587, 3749, 6809, 11235, 6733, 9185, 4751, 4749, 4708, 4712, 4712, 4730, 4718, 4716, 4720, 4722, 1127, 1434, 6920, 1079, 11724, 10828, 4153, 1073, 1725, 11732, 10551, 7462, 1215, 1333, 1319, 4151, 6585, 6570, 4151, 11700, 6731, 5575, 6585, 6737, 3755, 11698, 1215, 4585, 3751, 5574, 11696, 1079, 11694, 6922, 18351, 1319, 10551, 6920, 10828, 18349, 6916, 6918, 18353, 4087, 18355, 5576, 18357, 6922, 1333, 18359, 18361, 18363};
	
	public int handleLotteryItems() {
		return lotteryItems[(int) (Math.random() * lotteryItems.length)];
	}
	
	public void RunThis() {
		 World.getWorld().submit(new Event(150) {
				@Override
				public void execute() {
					if (c.disconnected) {
						stop();
						c.KillingPoints += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(20) == 1) {
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();	
					this.stop();
					}
				}
			});
		c.getPA().sendFrame126("", 16003);
		c.getPA().showInterface(16000);
	}		
	
	private void SendMessages() {
		if(c.getItems().getItemName(winningItem).endsWith("s")) {
			c.getPA().sendFrame126("You've won " + c.getItems().getItemName(winningItem) + "!", 15003);
			c.sendMessage("[ <col=2784FF>Pk Points Statue </col>] Congratulations! You have won <col=2784FF>" + c.getItems().getItemName(winningItem) + "</col>!");
		} else {
			c.getPA().sendFrame126("You've won a " + c.getItems().getItemName(winningItem) + "!", 15003);
			c.sendMessage("[ <col=2784FF>Pk Points Statue </col>] Congratulations! You have won a <col=2784FF>" + c.getItems().getItemName(winningItem) + "</col>!");
		}
		PhatStack();
	}
	
	private void PhatStack() {
		if (c.getShops().getItemShopValue(winningItem) > 99999999) {
			if(c.getItems().getItemName(winningItem).endsWith("s")) {
				c.getPA().yell("[ <col=2784FF>Pk Points Statue </col>] <col=2784FF>" + c.playerName + " </col>has just won <col=2784FF>" + c.getItems().getItemName(winningItem) + "</col>!");
			} else {
				c.getPA().yell("[ <col=2784FF>Pk Points Statue </col>] <col=2784FF>" + c.playerName + " </col>has just won a <col=2784FF>" + c.getItems().getItemName(winningItem) + "</col>!");
			}
			MyBallsAreSoHappy();
			Achievements.BigRewardPK();
		}
	}
	
	private void MyBallsAreSoHappy() {
		c.getPA().handleBigfireWork(c);
		c.startAnimation(2109);	
	}
	
	private void CloseInterface() {
		World.getWorld().submit(new Event(2000) {
			@Override
			public void execute() {
				c.getPA().removeAllWindows();
			this.stop();
			}
		});	
	}		
	
}