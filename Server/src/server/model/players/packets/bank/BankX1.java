package server.model.players.packets.bank;

import server.model.players.Client;
import server.model.players.PacketType;

public class BankX1 implements PacketType {

    public static final int PART1 = 135;
    public static final int PART2 = 208;
    public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	@Override
    public void processPacket(Client c, int packetType, int packetSize) {
        if (packetType == 135) {
            c.xRemoveSlot = c.getInStream().readSignedWordBigEndian();
            c.xInterfaceId = c.getInStream().readUnsignedWordA();
            c.xRemoveId = c.getInStream().readSignedWordBigEndian();
        }
        if (c.xInterfaceId == 3900) {
            if (c.myShopId == 30 && c.memberStatus < 1) {
                c.sendMessage("You must be a donator to buy from this shop.");
                return;
            }
            c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, 50); //buy 20
            c.xRemoveSlot = 0;
            c.xInterfaceId = 0;
            c.xRemoveId = 0;
            return;
        }

        if (packetType == PART1) {
            synchronized(c) {
                c.getOutStream().createFrame(27);
            }
        }

    }
}