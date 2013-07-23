package server.model.players.packets.item;

import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

public class ItemClick3 implements PacketType {

    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        int itemId11 = c.getInStream().readSignedWordBigEndianA();
        int itemId1 = c.getInStream().readSignedWordA();
        int itemId = c.getInStream().readSignedWordA();
        if (c.usingCarpet) {
            return;
        }
        /*if (itemId > 15085 && itemId < 15102){
		{//dice bag
			c.diceID = itemId;
			c.getDH().sendDialogues(106, 0);
		}
		}*/
        final String name = c.getItems().getItemName(itemId);
        if (c.getPotions().isPotion(itemId) && System.currentTimeMillis() - c.lastEmpty >= 1500) {
            c.sendMessage("You empty your " + name + ".");
            c.getItems().deleteItem(itemId, 1);
            c.getItems().addItem(229, 1);
            c.lastEmpty = System.currentTimeMillis();
        }
        switch (itemId) {
			case 15349:
				c.getPA().startTeleport(2672, 3374, 0, "cloak");
			break;
            case 15086:
                c.getItems().deleteItem(15086, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;

            case 15088:
                c.getItems().deleteItem(15088, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;

            case 15090:
                c.getItems().deleteItem(15090, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;

            case 15092:
                c.getItems().deleteItem(15092, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;

            case 15094:
                c.getItems().deleteItem(15094, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;

            case 15096:
                c.getItems().deleteItem(15096, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;

            case 15098:
                c.getItems().deleteItem(15098, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;

            case 15100:
                c.getItems().deleteItem(15100, 1);
                c.getItems().addItem(15084, 1);
                c.sendMessage("You put away the dies from the dice bag.");
                break;
            case 2552:
                c.getPA().handleROD(itemId);
                break;
            case 1712:
                c.getPA().handleGlory(itemId);
                break;
            case 3853:
                c.getPA().handleGamesNeck(itemId);
                break;

            default:
                if (c.playerRights == 3) 
					//Misc.println(c.playerName + " - Item3rdOption: " + itemId + " : " + itemId11 + " : " + itemId1);
                break;
        }

    }

}