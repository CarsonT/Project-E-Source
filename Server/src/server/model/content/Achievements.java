package server.model.content;

import server.model.players.Client;
import server.model.items.*;
import server.event.Event;
import server.util.Misc;
import server.Server;
import server.World;


/**
 * Handles Achievements 
 * @author - Play Boy
 * 
 *	TODO:
 *
 *	Add Special Item Only Rewarded When All Achievements Completed
 *
 *
 *	COMPLETED:
 *
 *	Added Interface
 *	Interface Is Shown After Earned Achievement
 * 	Interface Goes Away After Roughly 10 Seconds
 *	Added Achievement Point System
 *	Every Achievement Completed Is Rewarded By One Achievement Point
 *	Added Fireworks When Achievement Is Completed
 *	Add Achievement Store
 *	Add Items To Achievement Store
 *	Add Checking For Everything
 *
 */
 

 
 public class Achievements {
 
 	private static Client c;
	
	public Achievements(Client c) {
		this.c = c;
	}
 
	public static void CheckKillingAchievement() {
		if (c.KC == 100) {
			c.GettingReward = true;
			EarnedAchievement();
		}
	}
	
	public static void CheckMiscAchievement() {
		if (c.LotteryEnter == true && c.LotteryDone == false) {
			c.GettingReward = true;
			EarnedAchievement();
			c.LotteryDone = true;
			c.getPA().sendFrame126(" Enter The Lottery Once", 23136);
		}
		
	}
	
	public static void Tutorial() {
		if (c.TutorialStart == true && c.TutorialDone == false) {
			c.GettingReward = true;
			EarnedAchievement();
			c.TutorialDone = true;
			c.getPA().sendFrame126(" Complete The Tutorial", 23136);
		}	
	}
	
	public static void BigRewardPK() {
		if (c.EarnedRareItem == false) {
			c.GettingReward = true;
			EarnedAchievement();
			c.EarnedRareItem = true;
			c.getPA().sendFrame126(" Win A Rare Item", 23136);	
		}
	}	
 
	public static void AchievementTexts() {
		if (c.KC >= 100) 
			c.getPA().sendFrame126("@gre@Kill 100 Player's [DONE]", 19507);
		if (c.KC >= 145)
			c.getPA().sendFrame126("@gre@Kill 145 Player's [DONE]", 19508);
		if (c.KC >= 200)
			c.getPA().sendFrame126("@gre@Kill 200 Player's [DONE]", 19509);
		if (c.KC >= 250)
			c.getPA().sendFrame126("@gre@Kill 250 Player's [DONE]", 19510);
		if (c.KC >= 325)
			c.getPA().sendFrame126("@gre@Kill 325 Player's [DONE]", 19511);
		if (c.KC >= 400)
			c.getPA().sendFrame126("@gre@Kill 400 Player's [DONE]", 19512);
		if (c.KC >= 500)
			c.getPA().sendFrame126("@gre@Kill 500 Player's [DONE]", 19513);
		if (c.KC >= 1500)
			c.getPA().sendFrame126("@gre@Kill 1500 Player's [DONE]", 19514);
		if (c.KC >= 2000)
			c.getPA().sendFrame126("@gre@Kill 2000 Player's [DONE]", 19515);
	}
 
	public static void EarnedAchievement() {
		World.getWorld().submit(new Event(5000) {
		@Override
			public void execute() {
				c.getPA().walkableInterface(-1);
				c.GettingReward = false;				
			this.stop();
			}
		});
		Rewards();
		c.getPA().sendFrame126("     ACHIEVEMENT", 23135);
		c.getPA().walkableInterface(23133);		
	}		
		
	public static void got99() {
		c.getPA().handleBigfireWork(c);
		c.achievementPoints += 1;
		c.sendMessage("[ <col=2784FF>Achievements </col>] Congratulations! You have earned one achievement point!");	
}
	
	private static void Rewards() {
		c.getPA().handleBigfireWork(c);
		c.achievementPoints += 1;
		c.sendMessage("[ <col=2784FF>Achievements </col>] Congratulations! You have earned one achievement point!");
	}

 }