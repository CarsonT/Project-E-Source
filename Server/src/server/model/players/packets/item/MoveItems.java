package server.model.players.packets.item;

import server.model.players.Client;
import server.model.players.PacketType;

public class MoveItems implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int somejunk = c.getInStream().readUnsignedWordA(); 
		int itemFrom =  c.getInStream().readUnsignedWordA();
		int itemTo = (c.getInStream().readUnsignedWordA() -128);
		if(c.inTrade) {
			c.getTradeAndDuel().declineTrade();
            return;
		}
		if(c.tradeStatus == 1) {
			c.getTradeAndDuel().declineTrade();
			return;
		}
		if(c.duelStatus == 1) {
			c.getTradeAndDuel().declineDuel();
			return;
		}
		c.getItems().moveItems(itemFrom, itemTo, somejunk);
	}
	
}
