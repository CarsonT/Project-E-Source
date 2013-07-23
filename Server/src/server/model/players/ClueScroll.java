package server.model.players;

import server.util.Misc;
import server.model.players.Client;
import server.model.players.Player;

/**
 * Clue Scroll
 * 
 * @author Ian / Core
 *
 */

public class ClueScroll {
	
	public Client c;
	
	public ClueScroll(Client c) {
		this.c = c;
	}

	public void cluePath(int clueLevel) {
		int random = Misc.random(1);
		switch(clueLevel) {
			case 0:
			if(c.clueTask[0] == 1 || (random == 0 && c.clueTask[0] == 0)) {
				c.clueTask[0] = 1;
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Talk to the Magic Store Owner", 6970);
				c.getPA().sendFrame126("He will give you your next clue.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[0] == 2 || (random == 1 && c.clueTask[0] == 0)) {
				c.clueTask[0] = 2;
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Talk to Sim Louie near the market.", 6970);
				c.getPA().sendFrame126("She will give you your next clue.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[0] == 3) {
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("", 6970);
				c.getPA().sendFrame126("Dig Inside of the pig pen.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[0] == 4) {
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Dig infront of a gravestone", 6970);
				c.getPA().sendFrame126("In the Lumbridge cemetery.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			}
			break;
			
			case 1:
			if(c.clueTask[1] == 1 || (random == 0 && c.clueTask[1] == 0)) {
				c.clueTask[1] = 1;
				c.clueStep = random;
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Search the crates in", 6970);
				c.getPA().sendFrame126("Draynor Village", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[1] == 2 || (random == 1 && c.clueTask[1] == 0)) {
				c.clueTask[1] = 2;
				c.clueStep = random;
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Talk to Kulodian and he will", 6970);
				c.getPA().sendFrame126("point you in the right direction.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[1] == 3) {
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Search the boxes in the south-west", 6970);
				c.getPA().sendFrame126("corner of the Ardougne market.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[1] == 4) {
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Dig behind the mysterious statue", 6970);
				c.getPA().sendFrame126("in Seers village.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			}
			break;	
			
			case 2:
			if(c.clueTask[2] == 1 || (random == 0 && c.clueTask[2] == 0)) {
				c.clueTask[2] = 1;
				c.clueStep = random;
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Talk to TzHaar-Hur-Lek", 6970);
				c.getPA().sendFrame126("he will give you your next clue.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[2] == 2 || (random == 1 && c.clueTask[2] == 0)) {
				c.clueTask[2] = 2;
				c.clueStep = random;
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Open and search a drawer", 6970);
				c.getPA().sendFrame126("in the monastery.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[2] == 3) {
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Dig behind the altar slab", 6970);
				c.getPA().sendFrame126("near the Chaos druids.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			} else if(c.clueTask[2] == 4) {
				c.getPA().sendFrame126("", 6968);
				c.getPA().sendFrame126("", 6969);
				c.getPA().sendFrame126("Dig next to the lever that teleports", 6970);
				c.getPA().sendFrame126("you inside of the Magic arena.", 6971);
				c.getPA().sendFrame126("", 6972);
				c.getPA().sendFrame126("", 6973);
				c.getPA().sendFrame126("", 6974);
				c.getPA().sendFrame126("", 6975);
				c.getPA().showInterface(6965);
			}
			break;
		}
	}
	
	public void resetClueScroll() {
		for(int i = 0; i < 3; i++) {
			c.clueTask[i] = 0;
		}
		c.clueStep = 0;
	}

}
