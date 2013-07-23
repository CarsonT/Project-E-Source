package server.model.players.packets.item;

import server.model.minigames.*;
import server.Config;
import server.Server;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.PlayerSave;

public class DropItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		c.alchDelay = System.currentTimeMillis();
		int slot = c.getInStream().readUnsignedWordA();
		if (Server.playerHandler.players[c.playerId].underAttackBy != 0) {
			if ((c.getShops().getItemShopValue(itemId)*.75) > 10000) {
			c.sendMessage("You can't drop items worth over 10k in combat.");
			return;
			}
		}
		
		if (c.inCombat)
			if ((c.getShops().getItemShopValue(itemId)*.75) > 10000) {
			c.sendMessage("You can't drop items worth over 10k in combat.");	
		
			return;
		}
		
		if(itemId == 4045) {
			int explosiveHit = 15;
			c.startAnimation(827);
			c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
			c.handleHitMask(explosiveHit);
			c.dealDamage(explosiveHit);
			c.getPA().refreshSkill(3);
			c.forcedChat("Ow! That really hurt!");
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
		}		
		
		if(c.inWild() && !c.inCombat) {
			Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.playerItemsN[slot], c.getId());
			c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);			
			if(BountyHunter.inBH(c)) {
					c.cantLeavePenalty = 300;
					c.headIconPk = BountyHunter.getPlayerSkull(c);
					c.getPA().requestUpdates();
			}			
			PlayerSave.saveGame(c);
		}
		
		if (c.isBanking)
			return;
			
		if (System.currentTimeMillis() - c.lastAlch < 1800)
			return;
			
		if(c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			c.getTradeAndDuel().declineTrade();
			return;
	
		}
		
		if(!c.getItems().playerHasItem(itemId,1,slot)) {
			return;
		}
		
		if(c.tradeTimer > 0) {
			c.sendMessage("You must wait until your starter timer is up to drop items.");
			return;
		}
		
		if(c.playerRights == 2) {
			c.sendMessage("You cannot drop as an admin");
			return;
		}		
			
	
		if(c.inDuelArena()) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}
		
		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		if(c.playerItemsN[slot] != 0 && itemId != -1 && c.playerItems[slot] == itemId + 1) {
			if(droppable) {
				if (c.underAttackBy > 0) {
					if (c.getShops().getItemShopValue(itemId) > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.Asswhipe.");
						return;
					}
				}
				Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.playerItemsN[slot], c.getId());
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);					
				PlayerSave.saveGame(c);
			} else {
				c.sendMessage("This items cannot be dropped.");
			}
		}

	}
}
