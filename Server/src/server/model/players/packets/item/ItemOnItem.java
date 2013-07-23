package server.model.players.packets.item;

import server.model.items.UseItem;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.skills.herblore.Herblore;
import server.model.skills.firemaking.Firemaking;
import server.model.skills.fletching.Fletching;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int usedWithSlot = c.getInStream().readUnsignedWord();
		int itemUsedSlot = c.getInStream().readUnsignedWordA();
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		int slot;
		int logType;
		int otherItem;
		UseItem.ItemonItem(c, itemUsed, useWith);
if (Herblore.mixPotion(c, itemUsed, useWith)) {
			Herblore.setUpUnfinished(c, itemUsed, useWith);
		} else if (Herblore.mixPotionNew(c, itemUsed, useWith)) {
			Herblore.setUpPotion(c, itemUsed, useWith);	
		} else if((useWith == 1511 || itemUsed == 1511) && (useWith == 946 || itemUsed == 946)) {
			Fletching.normal(c, itemUsed, useWith);
		} else if(useWith == 946 || itemUsed == 946) {
			Fletching.others(c, itemUsed, useWith);
		} else if(Fletching.arrows(c, itemUsed, useWith)) {
			Fletching.makeArrows(c, itemUsed, useWith);			
		}
	}

}