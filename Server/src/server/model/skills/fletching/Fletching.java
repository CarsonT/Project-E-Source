package server.model.skills.fletching;

import server.Server;
import server.Config;
import server.World;
import server.model.players.Client;
import server.model.skills.SkillHandler;
import server.event.Event;

public class Fletching extends SkillHandler {

	private static int[][] arrows = {
		{52,314,53,1,1}, {53,39,882,3,1}, {53,40,884,4,15}, {53,41,886,6,30},
		{53,42,888,8,45}, {53,43,890,11,60}, {53,44,892,14,75}, {53,11237,11212,16,90},
		{314,819,806,2, 1}, {314,820,807,4,22}, {314,821,808,8,37}, {314,822,809,11,52},
		{314,823,810,15,67}, {314,824,811,19,81}, {314,11232,11230,25,95},
	};

	public static boolean arrows(Client c, int item1, int item2) {
		for(int i = 0; i < arrows.length; i++) {
			if((item1 == arrows[i][0] || item1 == arrows[i][1]) &&
					(item2 == arrows[i][1] || item2 == arrows[i][0])) {
				return true;

			}
		}
		return false;
	}

	public static void makeArrows(Client c, int item1, int item2) {
		for (int j = 0; j < arrows.length; j++) {
			if ((item1 == arrows[j][0] && item2 == arrows[j][1]) || (item2 == arrows[j][0] && item1 == arrows[j][1])) {
				if(!hasRequiredLevel(c, c.playerFletching, arrows[j][4], "fletching", "make "+c.getItems().getItemName(arrows[j][2])+"")) {
					return;
				}
				if(!noInventorySpace(c, "fletching")) {
					return;
				}
				int amount1 = c.getItems().getItemAmount(item1);
				int amount2 = c.getItems().getItemAmount(item2);
				int otherAmount = 0;
				if(amount1 >= 15) {
					amount1 = 15;
				}
				if(amount2 >= 15) {
					amount2 = 15;
				}
				if(amount1 > amount2) {
					otherAmount = amount1 - amount2;
					amount1 = amount1 - otherAmount;
				} else if(amount2 > amount1) {
					otherAmount = amount2 - amount1;
					amount2 = amount2 - otherAmount;
				}
				int xp = 0;
				if(amount1 >= amount2) {
					xp = amount1;
				} else {
					xp = amount2;
				}
				if (c.getItems().playerHasItem(item1, amount1) && c.getItems().playerHasItem(item2,amount2)) {
					c.getItems().deleteItem(item1, c.getItems().getItemSlot(item1), amount1);
					c.getItems().deleteItem(item2, c.getItems().getItemSlot(item2), amount2);
					c.getItems().addItem(arrows[j][2], amount1);
					c.getPA().addSkillXP((arrows[j][3] * xp) * FLETCHING_XP, c.playerFletching);
				}
			}
		}
	}

	public static void normal(Client c, int itemUsed, int useWith) {
		if((itemUsed == 946 && useWith == 1511) || (itemUsed == 1151 && useWith == 946)) {
			showInterfaceFletching(c, new int[] { 841, 839, 52 }, 0);
		}
	}

	public static void others(Client c, int itemUsed, int useWith) {
		for(int i = 0; i < data.length; i++) {
			if((itemUsed == 946 && useWith == data[i][0]) || (itemUsed == data[i][0] && useWith == 946)) {
				showInterfaceOthers(c, new int[] { data[i][1], data[i][4], }, data[i][7]);
			}
		}
	}

	private static int[][] data = {
		{1521, 843, 20, 17, 845, 25, 25, 0},
		{1519, 849, 35, 33, 847, 40, 41, 1},
		{1517, 853, 50, 50, 851, 55, 58, 2},
		{1515, 857, 65, 68, 855, 70, 75, 3},
		{1513, 861, 80, 83, 859, 85, 91, 4},
	};

	private static void showInterfaceOthers(Client c, int[] items, int type) {
		for(int i = 0; i < data.length; i++) {
			if(type == data[i][7]) {
				c.playerSkillProp[9][0] = data[i][0];	//LOG
				c.playerSkillProp[9][1] = data[i][1];	//SHORTBOW
				c.playerSkillProp[9][2] = data[i][2];	//LVL
				c.playerSkillProp[9][3] = data[i][3];	//XP
				c.playerSkillProp[9][4] = data[i][4];	//SHORTBOW
				c.playerSkillProp[9][5] = data[i][5];	//LVL
				c.playerSkillProp[9][6] = data[i][6];	//XP
				c.playerSkillProp[9][7] = data[i][7];	//TYPE
			}
		}
		c.getPA().sendFrame164(8866);
		c.getPA().sendFrame246(8869, view190 ? 190 : 225, items[0]);
		c.getPA().sendFrame246(8870, view190 ? 190 : 200, items[1]);
		c.getPA().sendFrame126(""+c.getItems().getItemName(items[0])+"", 8874);
		c.getPA().sendFrame126(""+c.getItems().getItemName(items[1])+"", 8878);
	}

	private static void showInterfaceFletching(Client c, int[] items, int type) {
		c.getPA().sendFrame164(8880);
		c.getPA().sendFrame126("What would you like to make?", 8879);
		c.getPA().sendFrame246(8884, view190 ? 190 : 225, items[0]);
		c.getPA().sendFrame246(8883, view190 ? 190 : 210, items[1]);
		c.getPA().sendFrame246(8885, view190 ? 190 : 240, items[2]);
		c.getPA().sendFrame126(""+c.getItems().getItemName(items[1])+"", 8889);
		c.getPA().sendFrame126(""+c.getItems().getItemName(items[0])+"", 8893);
		c.getPA().sendFrame126(""+c.getItems().getItemName(items[2])+"", 8897);
		c.playerFletch = true;
	}

	public static int[][] normal = {
		{1511, 841, 839, 50, 1, 0, 6684, 52},
	};

	public static void attemptData(Client c, int amount, int type) {
		c.playerSkillProp[9][0] = normal[0][0];	//LOG
		switch (type) {
		case 0: //SHORTBOW
			c.playerSkillProp[9][1] = 841;
			c.playerSkillProp[9][2] = 5;
			c.playerSkillProp[9][3] = 5;
			break;
		case 1: //LONGBOW
			c.playerSkillProp[9][1] = 839;
			c.playerSkillProp[9][2] = 10;
			c.playerSkillProp[9][3] = 10;
			break;
		case 2: //LONGBOW
			c.playerSkillProp[9][1] = 52;
			c.playerSkillProp[9][2] = 1;
			c.playerSkillProp[9][3] = 1;
			break;
		}
		attemptData(c, amount, false);
	}

	public static void attemptData(final Client c, int amount, boolean second) {
		if(!hasRequiredLevel(c, 9, c.playerSkillProp[9][2], "fletching", "fletch this")){
			return;
		}
		int item = c.getItems().getItemAmount(c.playerSkillProp[9][0]);
		if(amount > item) {
			amount = item;
		}
		c.doAmount = amount;
		if(c.playerSkilling[9]) {
			return;
		}
		c.playerSkilling[9] = true;
		c.stopPlayerSkill = true;
		c.getPA().removeAllWindows();
		final int itemToDelete = c.playerSkillProp[9][0];
		int itemToAdd = c.playerSkillProp[9][1];
		int xpToAdd = c.playerSkillProp[9][3];
		if(second) {
			itemToAdd = c.playerSkillProp[9][4];
			xpToAdd = c.playerSkillProp[9][6];
		}
		final int addItem = itemToAdd;
		final int addXP = (xpToAdd * FLETCHING_XP);
		c.startAnimation(6702);
		World.getWorld().submit(new Event(2) {
			@Override
			public void execute() {
				if(c.playerSkillProp[9][0] > 0) {
					c.getItems().deleteItem(itemToDelete, c.getItems().getItemSlot(itemToDelete), 1);
					c.getItems().addItem(addItem, c.playerSkillProp[9][1] == 52 ? 15 : 1);
					c.sendMessage("You make a "+ c.getItems().getItemName(addItem).toLowerCase() +".");
					c.getPA().addSkillXP(addXP, 9);
				}
				c.startAnimation(6702);
				deleteTime(c);
				if(!c.getItems().playerHasItem(c.playerSkillProp[9][0], 1) || c.doAmount <= 0) {
					resetFletching(c);
					this.stop();
				}
				if(!c.stopPlayerSkill) {
					resetFletching(c);
					this.stop();
				}
			}
			@Override
			public void stop() {

			}
		});
	}

	public static void resetFletching(Client c) {
		c.playerSkilling[9] = false;
		c.stopPlayerSkill = false;
		c.playerFletch = false;
		for(int i = 0; i < 9; i++) {
			c.playerSkillProp[9][i] = -1;
		}
		c.startAnimation(65535);
	}
}