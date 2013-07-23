package server.model.skills.firemaking;

import java.util.Timer;

import server.Config;
import server.model.players.Client;
import server.model.objects.Objects;
import server.Server;
/**
 * Firemaking.java
 *
 * @author Sanity
 *
 **/ 
public class Firemaking {
	
	public static Client c;
	
	public static int[] logs = {1511,1521,1519,1517,1515,1513};
	public static int[] exp = {1,3,4,5,7,8};
	public static int[] level = {1,15,30,45,60,75};
	public static long lastLight;
	public static int DELAY = 1250;
	public static boolean resetAnim = true;
	
	public Firemaking(Client c) {
		this.c = c;
	}
	public void resetAnimation() {
		c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		c.startAnimation(c.playerStandIndex);
	}
	public void checkLogType(int logType, int otherItem) {
		for (int j = 0; j < logs.length;j++) {
			if (logs[j] == logType || logs[j] == otherItem) {
				lightFire(j);
				return;
			}
		}	
	}
	public void lightFire(int slot) {
		if (c.playerLevel[c.playerFiremaking] >= level[slot]) {
			if (c.getItems().playerHasItem(590) && c.getItems().playerHasItem(logs[slot])) {
				if (System.currentTimeMillis() - lastLight > DELAY) {
					c.startAnimation(733,0);
					c.getItems().deleteItem(logs[slot], c.getItems().getItemSlot(logs[slot]), 1);
					c.getPA().addSkillXP(logs[slot] * Config.FIREMAKING_EXPERIENCE, c.playerFiremaking);
					c.getPA().object(2732,c.getX(),c.getY(), 0, 10);					
					c.sendMessage("You light the fire.");
					lastLight = System.currentTimeMillis();
					//c.getPA().frame1();
					resetAnim = true;
					new java.util.Timer().schedule( 
					        new java.util.TimerTask() {
					            @Override
					            public void run() {
					                resetAnimation();
c.getPA().movePlayer(c.getX(), c.getY() + 1, 0);
					            }
					        }, 
					        3000
					        
					);
				}
			}
		}
	}
	
}