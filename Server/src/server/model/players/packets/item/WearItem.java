package server.model.players.packets.item;
 
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;
 
public class WearItem implements PacketType {
 
    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        c.wearId = c.getInStream().readUnsignedWord();
        c.wearSlot = c.getInStream().readUnsignedWordA();
        c.interfaceId = c.getInStream().readUnsignedWordA();
 
        int oldCombatTimer = c.attackTimer;
        if (c.playerIndex > 0 || c.npcIndex > 0) c.getCombat().resetPlayerAttack();
        if (c.playerEquipment[c.playerHands] == 11095 || c.playerEquipment[c.playerHands] == 11097 || c.playerEquipment[c.playerHands] == 11101 || c.playerEquipment[c.playerHands] == 11103 && c.inRev()) {
            c.isnotfighting = true;
 
        } else {
            c.isnotfighting = false;
        }
        if (c.wearId >= 5509 && c.wearId <= 5515) {
            int pouch = -1;
            int a = c.wearId;
            if (a == 5509) pouch = 0;
            if (a == 5510) pouch = 1;
            if (a == 5512) pouch = 2;
            if (a == 5514) pouch = 3;
            c.getPA().emptyPouch(pouch);
            return;
        }
        if (c.wearId == 6583) {
            c.setSidebarInterface(1, 6014);
            c.setSidebarInterface(2, 6014);
            c.setSidebarInterface(3, 6014);
            c.setSidebarInterface(4, 6014);
            c.setSidebarInterface(5, 6014);
            c.setSidebarInterface(6, 6014);
            c.setSidebarInterface(7, 6014);
            c.setSidebarInterface(8, 6014);
            c.setSidebarInterface(9, 6014);
            c.setSidebarInterface(10, 6014);
            c.setSidebarInterface(11, 6014);
            c.setSidebarInterface(12, 6014);
            c.setSidebarInterface(13, 6014);
            c.setSidebarInterface(0, 6014);
            c.sendMessage("As you put on the ring you turn into a rock!");
            c.npcId2 = 2626;
            c.isNpc = true;
            c.updateRequired = true;
            c.setAppearanceUpdateRequired(true);
        }
 
        if (c.wearId == 7927) {
            c.setSidebarInterface(1, 6014);
            c.setSidebarInterface(2, 6014);
            c.setSidebarInterface(3, 6014);
            c.setSidebarInterface(4, 6014);
            c.setSidebarInterface(5, 6014);
            c.setSidebarInterface(6, 6014);
            c.setSidebarInterface(7, 6014);
            c.setSidebarInterface(8, 6014);
            c.setSidebarInterface(9, 6014);
            c.setSidebarInterface(10, 6014);
            c.setSidebarInterface(11, 6014);
            c.setSidebarInterface(12, 6014);
            c.setSidebarInterface(13, 6014);
            c.setSidebarInterface(0, 6014);
            c.sendMessage("As you put on the ring you turn into an egg!");
            c.npcId2 = 3689 + Misc.random(5);
            c.isNpc = true;
            c.updateRequired = true;
            c.setAppearanceUpdateRequired(true);
        }
        c.getItems().wearItem(c.wearId, c.wearSlot);
    }

}