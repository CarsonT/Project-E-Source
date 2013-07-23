package server.model.players.packets.item;

import server.Server;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.minigames.*;

public class PickupItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
	c.pItemY = c.getInStream().readSignedWordBigEndian();
	c.pItemId = c.getInStream().readUnsignedWord();
	c.pItemX = c.getInStream().readSignedWordBigEndian();
	if (Math.abs(c.getX() - c.pItemX) > 25 || Math.abs(c.getY() - c.pItemY) > 25) {
		c.resetWalkingQueue();
		return;
	}
		c.getCombat().resetPlayerAttack();
	if(c.getX() == c.pItemX && c.getY() == c.pItemY) {
		Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, true);	
		if(BountyHunter.inBH(c)) {
		if (c.pickupPenalty > 0) {
			c.sendMessage("You should not be picking up items. Now you must wait before you can leave.");
			BountyHunter.handleCantLeave(c);
		}
	}
	} else {
		c.walkingToItem = true;
		}	
	}

}
