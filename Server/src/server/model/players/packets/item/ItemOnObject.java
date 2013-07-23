package server.model.players.packets.item;

import server.model.items.UseItem;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.skills.cooking.Cooking;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int a = c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readSignedWordBigEndian();
		int objectY = c.getInStream().readSignedWordBigEndianA();
		int b = c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readSignedWordBigEndianA();
		int itemId = c.getInStream().readUnsignedWord();
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		c.turnPlayerTo(objectX, objectY);
		c.cookingCoords[0] = objectX;
		c.cookingCoords[1] = objectY;
			switch (objectId) {

			
		}
	}

	
}
