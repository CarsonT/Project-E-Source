package server.model.players.packets.item;
 
import server.Server;
import server.model.items.Item;
import server.model.objects.Objects;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.minigames.TreasureTrails;
import server.model.content.DwarfMultiCannon;
import server.model.skills.herblore.Herblore;
import server.Config;
 
/**
 * Clicking an item, bury bone, eat food etc
 **/
 
public class ClickItem implements PacketType {
    public long lastFlower;
    private int DELAY = 1250;@Override
    public void processPacket(Client c, int packetType, int packetSize) {
        int junk = c.getInStream().readSignedWordBigEndianA();
        int itemSlot = c.getInStream().readUnsignedWordA();
        int itemId = c.getInStream().readUnsignedWordBigEndian();
        if (System.currentTimeMillis() - c.cluetimer < 1000) return;
        if (itemId > 15084 && itemId < 15103) {
            if (c.playerRights == 3) {
 
                c.useDice(itemId, false);
            }
            c.sendMessage("Disabled won't be back till we get a few more peopel!");
        }
 
        if (itemId == 15084) {
 
            if (c.playerRights == 3) {
                c.diceID = itemId;
                c.getDH().sendDialogues(106, 0);
            } else {
                c.sendMessage("Disabled won't be back till we get a few more peopel!");
            }
        }
 
        if (itemId >= 5070 && itemId <= 5074) {
            if (c.getItems().freeSlots() < 1) {
                c.getPA().sendStatement("You need atleast two free inventory spaces!");
                return;
            }
            c.sendMessage("You search the birds nest...");
            c.getItems().deleteItem(itemId, c.getItems().getItemSlot(itemId), 1);
            c.getItems().addItem(5075, 1);
            c.getItems().addItem(c.getWoodcutting().randomBirdNests(), 10);
        }
 
        if (itemId == 7774) {
            if (c.inWild()) {
                return;
            } else {
                c.getPA().movePlayer(2727, 3347, 0);
                c.sendMessage("You teleport to the Member Zone Area.");
                c.sendMessage("You're Only allowed to enter when u have exchanged the ticket.");
                c.sendMessage("Talk to the Membership King to get Membership.");
                //Server.npcHandler.newNPC(364, c.absX, c.absY-1, 0, 0, 120, 7, 70, 70);
                //c.usedTicket = 1;
                //c.getActions().firstClickNpc(364);
            }
        }
 
        if (itemId == 7775) {
            //if (c.usedTicket == 1) {
            //  c.sendMessage("You Already Clicked on the ticket, Click on the spawned npc to become member.");
            //return;   
            //} else {
            if (c.inWild()) {
                return;
            } else {
                c.getPA().movePlayer(2727, 3347, 0);
                c.sendMessage("You teleport to the Member Zone Area.");
                c.sendMessage("You're Only allowed to enter when u have exchanged the ticket.");
                c.sendMessage("Talk to the Membership King to get Membership.");
                //Server.npcHandler.newNPC(364, c.absX, c.absY-1, 0, 0, 120, 7, 70, 70);
                //c.usedTicket = 1;
                //c.getActions().firstClickNpc(364);
            }
        }
 
        if (itemId == 7776) {
            //if (c.usedTicket == 1) {
            //  c.sendMessage("You Already Clicked on the ticket, Click on the spawned npc to become member.");
            //return;   
            //} else {
            if (c.inWild()) {
                return;
            } else {
                c.getPA().movePlayer(2727, 3347, 0);
                c.sendMessage("You teleport to the Member Zone Area.");
                c.sendMessage("You're Only allowed to enter when u have exchanged the ticket.");
                c.sendMessage("Talk to the Membership King to get Membership.");
                //Server.npcHandler.newNPC(364, c.absX, c.absY-1, 0, 0, 120, 7, 70, 70);
                //c.usedTicket = 1;
                //c.getActions().firstClickNpc(364);
            }
        }
 
        /*if (itemId == 299) {
            
            Objects Flowers = new Objects(2980,c.getX(),c.getY(), 0, -1, 10, 3);
            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                if (Server.playerHandler.players[i] != null) {
                    Client c2 = (Client) Server.playerHandler.players[i];
                    c2.getPA().checkObjectSpawn(c.getPA().randomFlower(),c.getX(),c.getY(), 0, 10);
            c.getItems().deleteItem2(299, 1);
            Server.objectHandler.addObject(Flowers);
            c.getPA().addSkillXP(2500, 19);
            c.getPA().walkTo(-1,0);
            
            
            
            c.getPA().walkTo(-1,0);
            c.turnPlayerTo(c.getX() + 1, c.getY());
            
            c.sendMessage("You plant a flower!");
            return;
        }}}*/
 
 
        switch (itemId) {
 
            case 6:
            case 8:
            case 10:
            case 12:
                DwarfMultiCannon.um();
                break;
 
            case 74115:
                if (c.memberStatus > 0) {
 
                    //Change symbol
 
                }
                break;
            case 74110:
                c.sendMessage("Coming soon!");
                break;
            case 5520:
                c.getDH().sendDialogues(37, -1);
                break;
            case 2528:
                c.sendMessage("You rub the mysterious Lamp.");
                c.getPA().showInterface(2808);
 
 
                break;
            case 74108:
                if (c.memberStatus == 1 && c.inGWD()) {
                    c.Arma = 15;
                    c.Band = 15;
                    c.Sara = 15;
                    c.Zammy = 15;
                    c.sendMessage("Your magical donator rank forces your KC to raise to 15!");
                } else {
                    c.sendMessage("You must be a donator and be in godwars dungeon to use this!");
                }
                break;
            case 299:
                c.getItems().deleteItem2(299, 1);
                c.getPA().walkTo(-1, 0);
                c.turnPlayerTo(c.getX() + 1, c.getY());
 
                c.sendMessage("You plant a flower!");
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        Client c2 = (Client) Server.playerHandler.players[i];
                        c2.getPA().checkObjectSpawn(c.getPA().randomFlower(), c.getX(), c.getY(), 0, 10);
                    }
                }
 
                break;
 
 
            case 11938:
                if (c.getItems().freeSlots() <= 6) {
                    c.sendMessage("You need at least 6 inventory slot opened.");
                }
                c.getItems().deleteItem(11940, 1);
                c.getItems().addItem(3481, 1);
                c.getItems().addItem(3482, 1);
                c.getItems().addItem(3483, 1);
                c.getItems().addItem(3484, 1);
 
                break;
            case 11940:
                if (c.getItems().freeSlots() <= 6) {
                    c.sendMessage("You need at least 6 inventory slot opened.");
                }
                c.getItems().deleteItem(11940, 1);
                c.getItems().addItem(3481, 1);
                c.getItems().addItem(3482, 1);
                c.getItems().addItem(3483, 1);
                c.getItems().addItem(3485, 1);
 
                break;
            case 7144:
                c.getItems().deleteItem(7144, 1);
                c.playerRights = 4;
                c.memberStatus = 1;
                c.getDH().sendDialogues(39, -1);
                break;
            case 952:
                c.startAnimation(830);
                if (c.inArea(3553, 3294, 3561, 3301)) {
                    c.teleTimer = 3;
                    c.newLocation = 1;
                } else if (c.inArea(3550, 3279, 3557, 3285)) {
                    c.teleTimer = 3;
                    c.newLocation = 2;
                } else if (c.inArea(3561, 3285, 3568, 3292)) {
                    c.teleTimer = 3;
                    c.newLocation = 3;
                } else if (c.inArea(3570, 3293, 3579, 3303)) {
                    c.teleTimer = 3;
                    c.newLocation = 4;
                } else if (c.inArea(3571, 3278, 3582, 3285)) {
                    c.teleTimer = 3;
                    c.newLocation = 5;
                } else if (c.inArea(3562, 3273, 3569, 3279)) {
                    c.teleTimer = 3;
                    c.newLocation = 6;
                } else if (c.absX == c.clueCoords[0][0] && c.absY == c.clueCoords[0][1]) {
                    if (c.clueTask[0] == 3 && c.getItems().playerHasItem(2677, 1)) {
                        c.clueTask[0] = 0;
                        c.getDH().itemMessage("", "You have found a casket!", 2714, 250);
                        c.getItems().deleteItem(2677, 1);
                        c.getItems().addItem(2714, 1);
                    }
                } else if (c.absX == c.clueCoords[1][0] && c.absY == c.clueCoords[1][1]) {
                    if (c.clueTask[0] == 4 && c.getItems().playerHasItem(2677, 1)) {
                        c.clueTask[0] = 0;
                        c.getDH().itemMessage("", "You have found a casket!", 2714, 250);
                        c.getItems().deleteItem(2677, 1);
                        c.getItems().addItem(2714, 1);
                    }
                } else if (c.absX == c.clueCoords[3][0] && c.absY == c.clueCoords[3][1]) {
                    if (c.clueTask[1] == 4 && c.getItems().playerHasItem(2678, 1)) {
                        c.clueTask[1] = 0;
                        c.getDH().itemMessage("", "You have found a casket!", 2714, 250);
                        c.getItems().deleteItem(2678, 1);
                        c.getItems().addItem(2717, 1);
                    }
                } else if (c.absX == c.clueCoords[4][0] && c.absY == c.clueCoords[4][1]) {
                    if (c.clueTask[2] == 3 && c.getItems().playerHasItem(2679, 1)) {
                        c.clueTask[2] = 0;
                        c.getDH().itemMessage("", "You have found a casket!", 2714, 250);
                        c.getItems().deleteItem(2679, 1);
                        c.getItems().addItem(2720, 1);
                    }
                } else if (c.absX == c.clueCoords[5][0] && c.absY == c.clueCoords[5][1]) {
                    if (c.clueTask[2] == 4 && c.getItems().playerHasItem(2679, 1)) {
                        c.clueTask[2] = 0;
                        c.getDH().itemMessage("", "You have found a casket!", 2714, 250);
                        c.getItems().deleteItem(2679, 1);
                        c.getItems().addItem(2720, 1);
                    }
                } else {
                    c.sendMessage("You don't find anything...");
                }
                break;
            case 2714:
 
                //c.sendMessage("Disabled for now please wait it will be fixed");
                c.getItems().deleteItem(2714, 1);
                TreasureTrails.addClueReward(c, 0);
                c.cluetimer = System.currentTimeMillis();
                break;
 
            case 2717:
 
                //c.sendMessage("Disabled for now please wait it will be fixed");
                c.getItems().deleteItem(2717, 1);
                TreasureTrails.addClueReward(c, 1);
                c.cluetimer = System.currentTimeMillis();
                break;
 
            case 2720:
 
                //c.sendMessage("Disabled for now please wait it will be fixed");
                c.getItems().deleteItem(2720, 1);
                TreasureTrails.addClueReward(c, 2);
                c.cluetimer = System.currentTimeMillis();
                break;
 
            case 2677:
                c.getClueScroll().cluePath(0);
                break;
 
            case 2678:
                c.getClueScroll().cluePath(1);
                break;
 
            case 2679:
                c.getClueScroll().cluePath(2);
                break;
        }
        if (c.usingCarpet) {
            return;
        }
        if (itemId != c.playerItems[itemSlot] - 1) {
            return;
        }
        if (itemId == 8007) {
            c.getPA().teleTab("varrock");
        }
        if (itemId == 8013) {
            if (c.clickedHome == 0) {
                c.getDH().sendDialogues(46, 0);
                c.clickedHome = 1;
            } else {
                c.getPA().teleTab("house");
            }
        }
        if (itemId == 8008) {
            c.getPA().teleTab("lumbridge");
        }
        if (itemId == 8009) {
            c.getPA().teleTab("falador");
        }
        if (itemId == 8010) {
            c.getPA().teleTab("camelot");
        }
        if (itemId == 8011) {
            c.getPA().teleTab("ardy");
        }
        if (itemId == 757) {
            c.Rules();
        }
        if (itemId == 6767) {
            c.Commands();
        }
        if (itemId == 4251) {
            c.getPA().movePlayer(3565, 3316, 0);
            c.sendMessage("You empty the ectophial.");
            c.getItems().deleteItem(4251, c.getItems().getItemSlot(4251), 1);
            c.getItems().addItem(4252, 1);
        }
        if (itemId >= 5509 && itemId <= 5514) {
            int pouch = -1;
            int a = itemId;
            if (a == 5509) pouch = 0;
            if (a == 5510) pouch = 1;
            if (a == 5512) pouch = 2;
            if (a == 5514) pouch = 3;
            c.getPA().fillPouch(pouch);
            return;
        }
        if (Herblore.isHerb(c, itemId)) Herblore.cleanTheHerb(c, itemId);
        if (c.getFood().isFood(itemId)) c.getFood().eat(itemId, itemSlot);
        if (c.getPotions().isPotion(itemId)) c.getPotions().handlePotion(itemId, itemSlot);
        if (c.getPrayer().readBone(itemId)) c.getPrayer().boneOnGround(itemId);
        if (itemId == 952) {
            if (c.inArea(3553, 3301, 3561, 3294)) {
                c.teleTimer = 3;
                c.newLocation = 1;
            } else if (c.inArea(3550, 3287, 3557, 3278)) {
                c.teleTimer = 3;
                c.newLocation = 2;
            } else if (c.inArea(3561, 3292, 3568, 3285)) {
                c.teleTimer = 3;
                c.newLocation = 3;
            } else if (c.inArea(3570, 3302, 3579, 3293)) {
                c.teleTimer = 3;
                c.newLocation = 4;
            } else if (c.inArea(3571, 3285, 3582, 3278)) {
                c.teleTimer = 3;
                c.newLocation = 5;
            } else if (c.inArea(3562, 3279, 3569, 3273)) {
                c.teleTimer = 3;
                c.newLocation = 6;
            }
        }
    }
 
}