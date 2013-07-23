package server.model.players.packets.bank;

import server.model.players.Client;
import server.model.players.PacketType;

public class BankX2 implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        int Xamount = c.getInStream().readDWord();
		if (Xamount < 0) {
			Xamount = c.getItems().getItemAmount(c.xRemoveId);
		}
		if (Xamount == 0) {
			Xamount = 1;
		}
        switch (c.xInterfaceId) {
            case 5064:
                c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot, Xamount > c.getItems().getItemAmount(c.xRemoveId) ? c.getItems().getItemAmount(c.xRemoveId) : Xamount);
			break;

            case 5382:
                c.getItems().fromBank(c.bankItems[c.xRemoveSlot], c.xRemoveSlot, Xamount > c.getItems().getBankAmount(c.xRemoveId) ? c.getItems().getBankAmount(c.xRemoveId) : Xamount);
			break;

            case 3322:
                if (c.duelStatus <= 0) {
                    c.getTradeAndDuel().tradeItem(c.xRemoveId, c.xRemoveSlot, Xamount > c.getItems().getItemAmount(c.xRemoveId) ? c.getItems().getItemAmount(c.xRemoveId) : Xamount);
                } else {
                    c.getTradeAndDuel().stakeItem(c.xRemoveId, c.xRemoveSlot, Xamount > c.getItems().getItemAmount(c.xRemoveId) ? c.getItems().getItemAmount(c.xRemoveId) : Xamount);
                }
                if (!c.getItems().playerHasItem(c.xRemoveId, Xamount)) 
				return;
			break;
        }
    }
	
}