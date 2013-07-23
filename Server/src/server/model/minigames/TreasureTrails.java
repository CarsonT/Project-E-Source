package server.model.minigames;

import server.util.Misc;
import server.model.players.Client;
import server.model.players.Player;

/**
 * Treasure Trails
 * 
 * @author Ian / Core
 *
 */

public class TreasureTrails {
	
	public static int lowLevelReward[] = {
		1077, 1125, 1165, 1195,
		1297, 1367, 853, 7390,
		7392, 7394, 7396, 7386, 
		7388, 1099, 1135, 1065,
		851, 1078, 10750, 10752,
		10754, 10756, 10402, 10406,
		10410, 10414, 10416, 10420, 
		10420, 10422, 10426, 10428, 
		10430, 10432, 10434, 10436,
		10438, 2583, 2585, 2587, 
		2589, 2591, 2593, 2595, 
		2597, 2633, 2635, 2637,
		15405, 15406, 15407, 15408,
		
		
	};
	public static int mediemLevelReward[] = {
		1073, 1123, 1161, 1199,
		1301, 1371, 857, 2577,
		2579, 2487, 2493, 2499, 
		2631, 855, 2599, 2601, 
		2603, 2605, 7372, 7380, 
		2607, 2609, 2611, 2613,
		7319, 7321, 7323, 7325, 
		7327, 2639, 2641, 2643,
		
	};
	public static int highLevelReward[] = {
		1079, 1093, 1113, 1127,
		1147, 1163, 1185, 1201,
		1275, 1303, 1319, 1333,
		1359, 1373, 2491, 2497,
		2503, 861, 859, 2581,
		2651, 10368, 10370, 10372,
		10374, 10450, 10386, 10386,
		10388, 10390, 10378, 10380,
		10382, 10380, 13101, 19453,
		19455, 19457, 19459, 19461, 
		19463, 19465, 19443, 
		19445, 19447, 19449, 19440,
		19437, 19398, 19401, 19404,		
		19407, 19413, 19416, 19419,
		19422, 19425, 19428, 19432,
		
	};
	
	public static int lowLevelStacks[] = {
		995, 380, 561, 886, 4661,
	};
	public static int mediumLevelStacks[] = {
		995, 374, 561, 563, 890,
	};
	public static int highLevelStacks[] = {
		995, 386, 561, 563, 560, 892
	};
	public static int allStacks[] = {
		995, 380, 561, 886,
		374, 561, 563, 890,
		386, 561, 563, 560, 
		892
	};
	
	public static void addClueReward(Client c, int clueLevel) {
		int chanceReward = Misc.random(2);
		if(clueLevel == 0) {
			switch (chanceReward) {
				case 0: 
				displayReward(c, lowLevelReward[Misc.random(lowLevelReward.length - 1)], 1, lowLevelReward[Misc.random(lowLevelReward.length - 1)], 1, lowLevelStacks[Misc.random(lowLevelStacks.length - 1)], 1 + Misc.random(150)); 
				break;
				case 1: 
				displayReward(c, lowLevelReward[Misc.random(lowLevelReward.length - 1)], 1, lowLevelStacks[Misc.random(lowLevelStacks.length - 1)], 1 + Misc.random(150), -1, 1); 
				break;
				case 2: 
				displayReward(c, lowLevelReward[Misc.random(lowLevelReward.length - 1)], 1, -1, 1, -1, 1); 
				break;
			}
		} else if(clueLevel == 1) {
			switch (chanceReward) {
				case 0: 
				displayReward(c, mediemLevelReward[Misc.random(mediemLevelReward.length - 1)], 1, mediemLevelReward[Misc.random(mediemLevelReward.length - 1)], 1, mediumLevelStacks[Misc.random(mediumLevelStacks.length - 1)], 1 + Misc.random(200));
				break;
				case 1: 
				displayReward(c, mediemLevelReward[Misc.random(mediemLevelReward.length - 1)], 1, mediumLevelStacks[Misc.random(mediumLevelStacks.length - 1)], 1 + Misc.random(200), -1, 1); 
				break;
				case 2: 
				displayReward(c, mediemLevelReward[Misc.random(mediemLevelReward.length - 1)], 1, -1, 1, -1, 1);
				break;
			}
		} else if(clueLevel == 2) {
			switch (chanceReward) {
				case 0: 
				displayReward(c, highLevelReward[Misc.random(highLevelReward.length - 1)], 1, highLevelReward[Misc.random(highLevelReward.length - 1)], 1, highLevelStacks[Misc.random(highLevelStacks.length - 1)], 1 + Misc.random(350)); 
				break;
				case 1: 
				displayReward(c, highLevelReward[Misc.random(highLevelReward.length - 1)], 1, highLevelStacks[Misc.random(highLevelStacks.length - 1)], 1 + Misc.random(350), -1, 1); 
				break;
				case 2: 
				displayReward(c, highLevelReward[Misc.random(highLevelReward.length - 1)], 1, -1, 1, -1, 1); 
				break;
			}
		}
	}

	public static void displayReward(Client c, int item, int amount, int item2, int amount2, int item3, int amount3) {
		int[] items = {
			item, item2, item3
		};
		int[] amounts = {
			amount, amount2, amount3
		};
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(6963);
		c.outStream.writeWord(items.length);
		for(int i = 0; i < items.length; i++) {
			if(c.playerItemsN[i] > 254) {
				c.outStream.writeByte(255);
				c.outStream.writeDWord_v2(amounts[i]);
			} else {
				c.outStream.writeByte(amounts[i]);
			}
			if(items[i] > 0) {
				c.outStream.writeWordBigEndianA(items[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
		c.getItems().addItem(item, amount);
		c.getItems().addItem(item2, amount2);
		c.getItems().addItem(item3, amount3);
		c.getPA().showInterface(6960);
	}
	
}
