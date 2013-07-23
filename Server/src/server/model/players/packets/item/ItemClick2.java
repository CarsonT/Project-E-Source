package server.model.players.packets.item;
 
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;
 
 
public class ItemClick2 implements PacketType {
 
    @Override
    public void processPacket(Client c, int packetType, int packetSize) {
        int itemId = c.getInStream().readSignedWordA();
 
        if (!c.getItems().playerHasItem(itemId, 1)) return;
 
        switch (itemId) {
            case 11938:
                if (c.getItems().freeSlots() <= 6) {
                    c.sendMessage("You need at least 6 inventory slot opened.");
                }
                c.getItems().deleteItem(11938, 1);
                c.getItems().addItem(3481, 1);
                c.getItems().addItem(3483, 1);
                c.getItems().addItem(3486, 1);
                c.getItems().addItem(3488, 1);
 
                break;
            case 11940:
                if (c.getItems().freeSlots() <= 6) {
                    c.sendMessage("You need at least 6 inventory slot opened.");
                }
                c.getItems().addItem(3481, 1);
                c.getItems().addItem(3485, 1);
                c.getItems().addItem(3486, 1);
                c.getItems().addItem(3488, 1);
 
                c.getItems().deleteItem(11940, 1);
                break;
			case 3842:
			case 3840:
			case 3844:
				c.getPA().handleGodBooks(itemId);
			break;				
            case 18351:
                c.sendMessage("You have " + c.chaoticLong + " charges left.");
                break;
            case 18349:
                c.sendMessage("You have " + c.chaoticRapier + " charges left.");
                break;
            case 18353:
                c.sendMessage("You have " + c.chaoticMaul + " charges left.");
                break;
 
            case 18355:
                c.sendMessage("You have " + c.chaoticStaff + " charges left.");
                break;
            case 18357:
                c.sendMessage("You have " + c.chaoticBow + " charges left.");
                break;
            case 18359:
                c.sendMessage("You have " + c.chaoticShield + " charges left.");
                break;
 
                case 11694:/*AGS*/
		if(c.getItems().freeSlots() < 1) {
		c.sendMessage("You need atleast 2 free slot's to dismantle your godsword.");
		} else if (c.getItems().playerHasItem(11694, 1)) {
			c.getItems().deleteItem(11694,1);
			c.getItems().addItem(11702,1);
			c.getItems().addItem(11690,1);
			c.sendMessage("You dismantle the godsword blade from the hilt.");
		} else {
		}
		break;
			case 11696:/*BGS*/
		if(c.getItems().freeSlots() < 1) {
		c.sendMessage("You need atleast 2 free slot's to dismantle your godsword.");
		} else if (c.getItems().playerHasItem(11696, 1)) {
			c.getItems().deleteItem(11696,1);
			c.getItems().addItem(11704,1);
			c.getItems().addItem(11690,1);
			c.sendMessage("You dismantle the godsword blade from the hilt.");
		} else {
		}
		break;
			case 11698:/*SGS*/
		if(c.getItems().freeSlots() < 1) {
		c.sendMessage("You need atleast 2 free slot's to dismantle your godsword.");
		} else if (c.getItems().playerHasItem(11698, 1)) {
			c.getItems().deleteItem(11698,1);
			c.getItems().addItem(11706,1);
			c.getItems().addItem(11690,1);
			c.sendMessage("You dismantle the godsword blade from the hilt.");
		} else {
		}
		break;
			case 11700:/*ZGS*/
		if(c.getItems().freeSlots() < 1) {
			c.sendMessage("You need atleast 2 free slot's to dismantle your godsword.");
		} else if (c.getItems().playerHasItem(11700, 1)) {
			c.getItems().deleteItem(11700,1);
			c.getItems().addItem(11708,1);
			c.getItems().addItem(11690,1);
			c.sendMessage("You dismantle the godsword blade from the hilt.");
		} else {
		}
		break;
            default:
                if (c.playerRights == 3) Misc.println(c.playerName + " - Item3rdOption: " + itemId);
                break;
        }
 
    }
 
}