package server.model.content;

import server.model.shops.ShopAssistant;
import server.model.players.Client;
import server.model.items.*;
import server.event.Event;
import server.util.Misc;
import server.Server;
import server.World;

 /**
   * Squeal of Fortune
   * @author Play Boy
   **/
  
public class SoF {

	private Client c;
	
	public SoF(Client c) {
		this.c = c;
	}

	int SpinItemWon;
	
	int[] SpinRewards = {4587, 6809, 11235, 9185, 4751, 4749, 4708, 4712, 4712, 4730, 4718, 4716, 4720, 4722, 1127, 1434, 6920, 1079, 11724, 10828, 4153, 1073, 1725, 11732, 10551, 7462, 1215, 1333, 1319, 4151, 6585, 6570, 4151, 11700, 6585, 11698, 1215, 11696, 1079, 11694, 18351, 1319, 10551, 10828, 18349, 6916, 6918, 18353, 18355, 18357, 6922, 1333, 18359, 18361, 18363};
	
	public int handleSpinRewards() {
		return SpinRewards[(int) (Math.random() * SpinRewards.length)];
	}	
	
	public void ShowMainInterface() {
		c.getPA().showInterface(16500);
		c.getPA().sendFrame126(""+c.SoFpoints+"", 16510);
	}
	
	public void BuySpins() {
		c.sendMessage("Will be coming son!");
	}
	
	public void CheckForSpins() {
		if (c.SoFpoints >= 0) {
				c.SoFpoints -= 1;
				StartSpinning();
		} else {
				c.sendMessage("[ <col=2784FF>Squeal of Fortune </col>] You need spins in order to do this!");
		
		}	
	}

	public void StartSpinning() {
		c.getPA().showInterface(16600);
			World.getWorld().submit(new Event(150) {
				@Override
				public void execute() {
					if (c.disconnected) {
						stop();
						c.SoFpoints += 1;
						return;
					}
					c.getPA().sendFrame34a(16602, handleSpinRewards(), 0, 1);
					if (Misc.random(20) == 1) {
						SpinItemWon = handleSpinRewards();
						c.getPA().sendFrame34a(16602, SpinItemWon, 0, 1);
						c.getItems().addItem(SpinItemWon, 1);
						//c.getPA().showInterface(16600);
						SendMessages();
					this.stop();
					}
				}
			});
	}		
	
	private void SendMessages() {
		if(c.getItems().getItemName(SpinItemWon).endsWith("s")) {
			c.getPA().sendFrame126("You've won " + c.getItems().getItemName(SpinItemWon) + "!", 15003);
			c.sendMessage("[ <col=2784FF>Squeal of Fortune </col>] Congratulations! You have won <col=2784FF>" + c.getItems().getItemName(SpinItemWon) + "</col>!");
		} else {
			c.getPA().sendFrame126("You've won a " + c.getItems().getItemName(SpinItemWon) + "!", 15003);
			c.sendMessage("[ <col=2784FF>Squeal of Fortune </col>] Congratulations! You have won a <col=2784FF>" + c.getItems().getItemName(SpinItemWon) + "</col>!");
		}
		PhatStack();
	}
	
	private void PhatStack() {
		if (c.getShops().getItemShopValue(SpinItemWon) > 99999999) {
			if(c.getItems().getItemName(SpinItemWon).endsWith("s")) {
				c.getPA().yell("[ <col=2784FF>Squeal of Fortune </col>] <col=2784FF>" + c.playerName + " </col>has just won <col=2784FF>" + c.getItems().getItemName(SpinItemWon) + "</col>!");
			} else {
				c.getPA().yell("[ <col=2784FF>Squeal of Fortune </col>] <col=2784FF>" + c.playerName + " </col>has just won a <col=2784FF>" + c.getItems().getItemName(SpinItemWon) + "</col>!");
			}
			MyBallsAreSoHappy();
		}
	}
	
	private void MyBallsAreSoHappy() {
		c.getPA().handleBigfireWork(c);
		c.startAnimation(2109);	
	}
	
	public void CloseInterface() {
		c.getPA().removeAllWindows();
	}
	
}