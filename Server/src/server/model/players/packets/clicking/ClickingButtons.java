package server.model.players.packets.clicking;

import server.Config;
import server.Server;
import server.model.items.GameItem;
import server.model.players.Client;
import server.model.players.Curse;
import server.model.players.SkillMenu;
import server.model.players.PacketType;
import server.model.players.PlayerSave;
import server.util.Misc;
import server.model.players.PlayerHandler;
import server.model.content.*;
import server.model.skills.herblore.Herblore;
import server.model.skills.fletching.Fletching;


public class ClickingButtons implements PacketType {

    int[] PvpItems = {
        14876, 14877, 14878, 14879, 14880, 14881, 14882, 14883, 14884, 14885, 14886, 14888, 14889, 14890, 14891, 14892
    };
    int[] PvpPrices = {
        50000000, 10000000, 500000, 350000, 8000000, 50000, 80000, 840000, 50000, 25000, 800000, 50000000, 24000, 87000, 2000000, 284000
    };

    @Override
    public void processPacket(final Client c, int packetType, int packetSize) {
        int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);
        //int actionButtonId = c.getInStream().readShort();
        if (c.isDead) return;

        if (c.playerRights == 3) c.sendMessage("<col=255>Actionbutton: " + actionButtonId + " Fight mode: " + c.fightMode + " Dialogue action: " + c.dialogueAction);
        int[] spellIds = {
            4128, 4130, 4132, 4134, 4136, 4139, 4142, 4145, 4148, 4151, 4153, 4157, 4159, 4161, 4164, 4165, 4129, 4133, 4137, 6006, 6007, 6026, 6036, 6046, 6056,
            4147, 6003, 47005, 4166, 4167, 4168, 48157, 50193, 50187, 50101, 50061, 50163, 50211, 50119, 50081, 50151, 50199, 50111, 50071, 50175, 50223, 50129, 50091
        };
        for (int i = 0; i < spellIds.length; i++) {
            if (actionButtonId == spellIds[i]) {
                c.autocasting = (c.autocastId != i) ? true : false;
                if (!c.autocasting) {
                    c.getPA().resetAutocast();
                } else {
                    c.autocastId = i;
                }
            }
        }
        if (actionButtonId >= 67050 && actionButtonId <= 67075) {
            if (c.playerPrayerBook == 0) QuickPrayers.clickPray(c, actionButtonId);
            else QuickCurses.clickCurse(c, actionButtonId);
        }
        switch (actionButtonId) {
        case 62165:
        	c.Achievements();
        	break;
            case 156068:
                c.getPA().showInterface(40030);
                break;

            case 156069:
                c.setSidebarInterface(11, 6299);
                break;

            case 144156:
                c.setSidebarInterface(11, 904);
                break;

                /* SoF Buttons By Omar / Play Boy */

            case 189118:
                c.getSoF().ShowMainInterface();
                break;

            case 189121:
                c.getSoF().BuySpins();
                break;

            case 64223:
            case 64123:
                c.getSoF().CheckForSpins();
                break;

            case 64229:
                c.getSoF().CloseInterface();
                break;

                /* Emotes Fixed By Omar / Play Boy */
            case 73000:
                c.startAnimation(3543);
                c.stopMovement();
                break;
            case 43092:
                c.startAnimation(0x558);
                c.stopMovement();
                c.gfx0(574);
                break;
            case 73003:
                c.startAnimation(2836);
                c.stopMovement();
                break;
            case 73002:
                c.startAnimation(3544);
                c.stopMovement();
                break;
            case 73004:
                c.startAnimation(7272);
                c.stopMovement();
                c.gfx0(1244);
                break;
            case 72255:
                c.startAnimation(6111);
                c.stopMovement();
                break;
            case 88058:
                c.startAnimation(7531);
                c.stopMovement();
                break;
            case 88059:
                if (System.currentTimeMillis() - c.logoutDelay < 8000) {
                    c.sendMessage("You cannot do this emote in combat!");
                    return;
                }
                c.startAnimation(2414);
                c.stopMovement();
                c.gfx0(1537);
                break;
            case 88060:
                c.startAnimation(8770);
                c.stopMovement();
                c.gfx0(1553);
                break;
            case 88061:
                c.startAnimation(9990);
                c.stopMovement();
                c.gfx0(1734);
                break;
            case 88062:
                c.startAnimation(10530);
                c.stopMovement();
                c.gfx0(1864);
                break;
            case 88063:
                c.startAnimation(11044);
                c.stopMovement();
                c.gfx0(1973);
                break;
            case 88064:
            case 88065:
                c.sendMessage("Sorry, this feature is unavailable at the moment.");
                break;
            case 88066:
                c.startAnimation(12658);
                c.stopMovement();
                c.gfx0(780);
                break;

                /* End of Emotes */

            case 113228:
                c.setSidebarInterface(2, 638);
                break;
            case 76134:
                c.setSidebarInterface(2, 19500);
                break;
            case 10239:
                if (!c.secondHerb) {
                    Herblore.finishUnfinished(c, 1);
                } else {
                    Herblore.finishPotion(c, 1);
                }
                break;
            case 10238:
                if (c.secondHerb) {
                    Herblore.finishPotion(c, 5);
                } else {
                    Herblore.finishUnfinished(c, 5);
                }
                break;
            case 6212:
                if (c.secondHerb) {
                    Herblore.finishPotion(c, c.getItems().getItemAmount(c.newItem));
                } else {
                    Herblore.finishUnfinished(c,
                    c.getItems().getItemAmount(c.doingHerb));
                }
                break;
            case 6211:
                if (c.secondHerb) {
                    Herblore.finishPotion(c, c.getItems().getItemAmount(c.newItem));
                } else {
                    Herblore.finishUnfinished(c,
                    c.getItems().getItemAmount(c.doingHerb));
                }
                break;
                /*Grand Exchange*/
            case 61037:
                //buy
                c.getPA().updateGE();
                c.getPA().showInterface(15751);
                break;
            case 61038:
                //sell
                c.getPA().updateGE();
                c.getPA().showInterface(15951);
                break;
            case 61138:
            case 62082:
                c.tempamt += 1;
                c.getPA().updateGE();
                break;
            case 61139:
            case 62083:
                c.tempamt += 5;
                c.getPA().updateGE();
                break;
            case 61140:
            case 62084:

                c.tempamt += 25;
                c.getPA().updateGE();
                break;
            case 61145:
            case 62089:
                c.tempamt += 1;
                c.getPA().updateGE();
                break;
            case 61143:
            case 62087:
                c.tempamt -= 1;
                c.getPA().updateGE();
                break;
            case 61146:
            case 62090:
                c.tempprice += 1;
                c.getPA().updateGE();
                break;
            case 61144:
            case 62088:
                c.tempprice -= 1;
                c.getPA().updateGE();
                break;
            case 62100:
            case 61156:
                c.tempamt = 0;
                c.tempid = 0;
                c.tempprice = 0;
                c.getPA().showInterface(15651);
                c.getPA().updateGE();
                break;
            case 61153:
            case 62097:
                c.tempamt = 0;
                c.tempid = 0;
                c.tempprice = 0;
                c.getPA().closeAllWindows();
                c.getPA().updateGE();
                break;
            case 61137:
                c.getPA().updateGE();
                GrandExchange.createConnection();
                GrandExchange.process(c, c.tempid, c.tempamt, c.tempprice, "buy");
                c.tempamt = 0;
                c.tempid = 0;
                c.tempprice = 0;
                c.getPA().showInterface(15651);
                c.getPA().updateGE();
                break;
            case 62081:
                c.getPA().updateGE();
                GrandExchange.createConnection();
                GrandExchange.process(c, c.tempid, c.tempamt, c.tempprice, "sell");
                c.tempamt = 0;
                c.tempid = 0;
                c.tempprice = 0;
                c.getPA().showInterface(15651);
                c.getPA().updateGE();
                break;
            case 61047:
                GrandExchange.createConnection();
                GrandExchange.writeInterfaceSell(c, c.playerName);
                break;
            case 61051:
                c.getPA().showInterface(16351);
                break;
            case 61055:
                GrandExchange.createConnection();
                GrandExchange.getCollect(c, c.playerName);
                break;
            case 61029:
                GrandExchange.createConnection();
                GrandExchange.writeInterfaceBuy(c, c.playerName);
                break;
            case 61043:
                c.getPA().showInterface(16151);
                break;
            case 63234:
            case 63034:
                c.getPA().closeAllWindows();
                c.tempid = 0;
                break;
            case 63028:
                GrandExchange.createConnection();
                GrandExchange.cancelBuy(c, c.tempid);
                c.getPA().showInterface(15651);
                break;
            case 63228:
                GrandExchange.createConnection();
                GrandExchange.cancelSell(c, c.tempid);
                c.getPA().showInterface(15651);
                break;
            case 19136:
                //Toggle quick prayers
                if (c.quickPray || c.quickCurse) {
                    QuickCurses.turnOffQuicks(c);
                    return;
                }
                QuickCurses.turnOnQuicks(c);
                break;

            case 19137:
                //Select quick prayers
                QuickCurses.selectQuickInterface(c);
                break;

            case 67089:
                //quick curse confirm
                QuickCurses.clickConfirm(c);
                break;

            case 70082:
                //select your quick prayers/curses
                QuickCurses.selectQuickInterface(c);
                c.getPA().sendFrame106(5);
                break;
                //crafting + fletching interface:
            case 150:
                if (c.autoRet == 0) c.autoRet = 1;
                else c.autoRet = 0;
                break;

                /* case 19212:
                c.setSidebarInterface(8, 5065);
                break;
            case 19215:
                c.setSidebarInterface(8, 5715);
                break;*/


                /**Prayers**/
            case 87231:
                // thick skin
                c.getCurse().activateCurse(0);
                return;
            case 87233:
                // burst of str
                c.getCurse().activateCurse(1);
                break;
            case 87235:
                // charity of thought
                c.getCurse().activateCurse(2);
                break;
            case 87237:
                // range
                c.getCurse().activateCurse(3);
                break;
            case 87239:
                // mage
                c.getCurse().activateCurse(4);
                break;
            case 87241:
                // rockskin
                c.getCurse().activateCurse(5);
                break;
            case 87243:
                // super human
                c.getCurse().activateCurse(6);
                break;
            case 87245:
                //defmage
                if (c.curseActive[7]) {
                    c.curseActive[7] = false;
                    c.getPA().sendFrame36(88, 0);
                    c.headIcon = -1;
                    c.getPA().requestUpdates();
                } else {
                    c.getCurse().activateCurse(7);
                    c.getPA().sendFrame36(90, 0); //defmellee
                    c.getPA().sendFrame36(89, 0); //defrang
                    c.getPA().sendFrame36(97, 0); //soulsplit
                    c.getPA().sendFrame36(96, 0); //warth
                    c.getPA().sendFrame36(88, 1); //deflmag
                }
                break;
            case 87247:
                //defrng
                if (c.curseActive[8]) {
                    c.getPA().sendFrame36(89, 0);
                    c.curseActive[8] = false;
                    c.headIcon = -1;
                    c.getPA().requestUpdates();
                } else {
                    c.getCurse().activateCurse(8);
                    c.getPA().sendFrame36(90, 0); //defmellee
                    c.getPA().sendFrame36(89, 1); //defrang
                    c.getPA().sendFrame36(88, 0); //deflmag
                    c.getPA().sendFrame36(97, 0); //soulsplit
                    c.getPA().sendFrame36(96, 0); //warth
                }
                break;
            case 87249:
                //defmel
                if (c.curseActive[9]) {
                    c.getPA().sendFrame36(90, 0);
                    c.curseActive[9] = false;
                    c.headIcon = -1;
                    c.getPA().requestUpdates();
                } else {
                    c.getCurse().activateCurse(9);
                    c.getPA().sendFrame36(90, 1); //defmellee
                    c.getPA().sendFrame36(89, 0); //defrang
                    c.getPA().sendFrame36(88, 0); //deflmag
                    c.getPA().sendFrame36(97, 0); //soulsplit
                    c.getPA().sendFrame36(96, 0); //warth
                }
                break;

            case 87251:
                // leeech attack
                if (c.curseActive[10]) {
                    c.getPA().sendFrame36(91, 0); //str
                    c.curseActive[10] = false;
                } else {
                    c.getCurse().activateCurse(10);
                    c.curseActive[19] = false;
                    c.getPA().sendFrame36(91, 1); //attack leech
                    c.getPA().sendFrame36(105, 0); // turmoil
                }
                break;
            case 87253:
                // leech range
                if (c.curseActive[11]) {
                    c.getPA().sendFrame36(103, 0); //str
                    c.curseActive[11] = false;
                } else {
                    c.getCurse().activateCurse(11);
                    c.curseActive[19] = false;
                    c.getPA().sendFrame36(105, 0); // turmoil
                    c.getPA().sendFrame36(103, 1); //range
                }
                break;
            case 87255:
                // leech magic
                if (c.curseActive[12]) {
                    c.getPA().sendFrame36(104, 0); //str
                    c.curseActive[12] = false;
                } else {
                    c.getCurse().activateCurse(12);
                    c.curseActive[19] = false;
                    c.getPA().sendFrame36(105, 0); // turmoil
                    c.getPA().sendFrame36(104, 1); //mage
                }
                break;
            case 88001:
                // leech def
                if (c.curseActive[13]) {
                    c.getPA().sendFrame36(92, 0); //str
                    c.curseActive[13] = false;
                } else {
                    c.getCurse().activateCurse(13);
                    c.curseActive[19] = false;
                    c.getPA().sendFrame36(105, 0); // turmoil
                    c.getPA().sendFrame36(92, 1); //def
                }
                break;
            case 88003:
                // leech str
                if (c.curseActive[14]) {
                    c.getPA().sendFrame36(93, 0); //str
                    c.curseActive[14] = false;
                } else {
                    c.getCurse().activateCurse(14);
                    c.curseActive[19] = false;
                    c.getPA().sendFrame36(105, 0); // turmoil
                    c.getPA().sendFrame36(93, 1); //str
                }
                break;
                /*          .getCurse().activateCurse(15);
            c.sendMessage("Doesn't work yet");
            return; */
            case 88007:
                // protect from magic
                if (c.curseActive[16]) {
                    c.getPA().sendFrame36(95, 0); //str
                    c.curseActive[16] = false;
                } else {
                    c.getCurse().activateCurse(16);
                    c.curseActive[19] = false;
                    c.getPA().sendFrame36(105, 0); // turmoil
                    c.getPA().sendFrame36(95, 1); //def
                }
                return;
            case 88009:
                // protect from range
                if (c.curseActive[17]) {
                    c.getPA().sendFrame36(96, 0);
                    c.curseActive[17] = false;
                    c.headIcon = -1;
                    c.getPA().requestUpdates();
                } else {
                    c.getCurse().activateCurse(17);
                    c.getPA().sendFrame36(90, 0); //defmellee
                    c.getPA().sendFrame36(89, 0); //defrang
                    c.getPA().sendFrame36(88, 0); //deflmag
                    c.getPA().sendFrame36(97, 0); //soulsplit
                    c.getPA().sendFrame36(96, 1); //warth
                }
                break;
            case 88011:
                // protect from melee
                if (c.curseActive[18]) {
                    c.getPA().sendFrame36(97, 0);
                    c.curseActive[18] = false;
                    c.headIcon = -1;
                    c.getPA().requestUpdates();
                } else {
                    c.getCurse().activateCurse(18);
                    c.getPA().sendFrame36(90, 0); //defmellee
                    c.getPA().sendFrame36(89, 0); //defrang
                    c.getPA().sendFrame36(88, 0); //deflmag
                    c.getPA().sendFrame36(97, 1); //soulsplit
                    c.getPA().sendFrame36(96, 0); //warth
                }
                break;
            case 88013:
                // 44 range
                if (c.curseActive[19]) {
                    c.getPA().sendFrame36(105, 0); //str
                    c.curseActive[19] = false;
                } else {
                    c.getCurse().activateCurse(19);
                    c.curseActive[10] = false;
                    c.curseActive[11] = false;
                    c.curseActive[12] = false;
                    c.curseActive[13] = false;
                    c.curseActive[14] = false;
                    c.getPA().sendFrame36(91, 0); //attack leech
                    c.getPA().sendFrame36(105, 1); // turmoil
                    c.getPA().sendFrame36(93, 0); //str
                    c.getPA().sendFrame36(92, 0); //def
                    c.getPA().sendFrame36(104, 0); //mage
                    c.getPA().sendFrame36(103, 0); //range
                    c.getPA().sendFrame36(95, 0); //spec
                    c.getPA().sendFrame36(96, 0); //run
                }
                break;
                /**End of curse prayers**/


            case 74113:
                if (c.memberStatus == 0 || c.inWild() || c.inDuelArena()) {
                    c.sendMessage("You must be outside wilderness and be a donator to use this!");
                    return;
                }
                if (c.playerMagicBook == 0 && c.memberStatus == 1 && !c.inWild()) {
                    c.playerMagicBook = 1;
                    c.setSidebarInterface(6, 12855);
                    c.setSidebarInterface(0, 328);
                    c.sendMessage("An ancient wisdomin fills your mind.");
                    c.getPA().resetAutocast();
                    return;
                }
                if (c.playerMagicBook == 1 && c.memberStatus == 1 && !c.inWild()) {
                    c.playerMagicBook = 2;
                    c.setSidebarInterface(0, 328);
                    c.setSidebarInterface(6, 29999);
                    c.sendMessage("Your mind becomes stirred with thoughs of dreams.");
                    c.getPA().resetAutocast();
                    return;
                }
                if (c.playerMagicBook == 2 && c.memberStatus == 1 && !c.inWild()) {
                    c.setSidebarInterface(6, 1151); //modern
                    c.playerMagicBook = 0;
                    c.setSidebarInterface(0, 328);
                    c.sendMessage("You feel a drain on your memory.");
                    c.autocastId = -1;
                    c.getPA().resetAutocast();
                    return;
                }
                break;



            case 74108:
                if (c.memberStatus == 0 || c.inWild()) {
                    c.sendMessage("You must be outside wilderness and be a donator to use this!");
                    return;
                }
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

            case 74109:
                if (c.memberStatus == 1) {
                    c.getPA().spellTeleport(2525, 4776, 0);
                    c.sendMessage("You teleported to donator-zone a place to chill/relax, theres also alot of benefits.");
                } else {
                    c.sendMessage("You must be an donator to teleport to the donator-zone!");
                    return;
                }
                break;




            case 74112:
                c.sendMessage("coming soon");
                break;



                /* case 108007:
               // c.getPA().showInterface(19050);
			   c.sendMessage("Coming Soon To A Theatre Near You!");
                break;*/
            case 74115:
                if (c.memberStatus == 0 || c.inWild()) {
                    c.sendMessage("You must be a donator to use this command");

                    return;
                }
                c.getPA().showInterface(20505);
                break;

            case 10252:
                c.antiqueSelect = 0;
                c.sendMessage("You select Attack");
                break;
            case 10253:
                c.antiqueSelect = 2;
                c.sendMessage("You select Strength");
                break;
            case 10254:
                c.antiqueSelect = 4;
                c.sendMessage("You select Ranged");
                break;
            case 10255:
                c.antiqueSelect = 6;
                c.sendMessage("You select Magic");
                break;
            case 11000:
                c.antiqueSelect = 1;
                c.sendMessage("You select Defence");
                break;
            case 11001:
                c.antiqueSelect = 3;
                c.sendMessage("You select Hitpoints");
                break;
            case 11002:
                c.antiqueSelect = 5;
                c.sendMessage("You select Prayer");
                break;
            case 11003:
                c.antiqueSelect = 16;
                c.sendMessage("You select Agility");
                break;
            case 11004:
                c.antiqueSelect = 15;
                c.sendMessage("You select Herblore");
                break;
            case 11005:
                c.antiqueSelect = 17;
                c.sendMessage("You select Thieving");
                break;
            case 11006:
                c.antiqueSelect = 12;
                c.sendMessage("You select Crafting");
                break;
            case 11007:
                c.antiqueSelect = 20;
                c.sendMessage("You select Runecrafting");
                break;
            case 47002:
                c.antiqueSelect = 18;
                c.sendMessage("You select Slayer");
                break;
            case 54090:
                c.antiqueSelect = 19;
                c.sendMessage("You select Farming");
                break;
            case 11008:
                c.antiqueSelect = 14;
                c.sendMessage("You select Mining");
                break;
            case 11009:
                c.antiqueSelect = 13;
                c.sendMessage("You select Smithing");
                break;
            case 11010:
                c.antiqueSelect = 10;
                c.sendMessage("You select Fishing");
                break;
            case 11011:
                c.antiqueSelect = 7;
                c.sendMessage("You select Cooking");
                break;
            case 11012:
                c.antiqueSelect = 11;
                c.sendMessage("You select Firemaking");
                break;
            case 11013:
                c.antiqueSelect = 8;
                c.sendMessage("You select Woodcutting");
                break;
            case 11014:
                c.antiqueSelect = 9;
                c.sendMessage("You select Fletching");
                break;
            case 11015:
                c.getPA().addSkillXP(13034436, c.antiqueSelect);
                c.getItems().deleteItem(2528, 1);
                c.sendMessage("The lamp mysteriously vanishes");
                c.getPA().closeAllWindows();
                break;

            case 74133:
                if (c.memberStatus > 0) {
                    //c.disconnected = true;
                    c.playerRights = 5;
                    c.sendMessage("You select purple");
                }
                break;

            case 74132:
                if (c.memberStatus > 0) {

                    c.playerRights = 6;
                    //c.disconnected = true;
                    c.sendMessage("You select green");
                }
                break;

            case 74130:
                if (c.memberStatus > 0) {
                    //c.disconnected = true;
                    c.playerRights = 4;
                    c.sendMessage("You select red");
                }
                break;

            case 74131:
                if (c.donorIcon == 1) {
                    c.playerRights = 5;
                }
                if (c.donorIcon == 3) {
                    c.playerRights = 4;
                }
                if (c.donorIcon == 2) {
                    c.playerRights = 6;
                }
                c.disconnected = true;
                c.getPA().closeAllWindows();
                break;



            case 108006:
                //items kept on death?
                c.StartBestItemScan();
                c.EquipStatus = 0;
                for (int k = 0; k < 4; k++)
                c.getPA().sendFrame34a(10494, -1, k, 1);
                for (int k = 0; k < 39; k++)
                c.getPA().sendFrame34a(10600, -1, k, 1);
                if (c.WillKeepItem1 > 0) c.getPA().sendFrame34a(10494, c.WillKeepItem1, 0, c.WillKeepAmt1);
                if (c.WillKeepItem2 > 0) c.getPA().sendFrame34a(10494, c.WillKeepItem2, 1, c.WillKeepAmt2);
                if (c.WillKeepItem3 > 0) c.getPA().sendFrame34a(10494, c.WillKeepItem3, 2, c.WillKeepAmt3);
                if (c.WillKeepItem4 > 0) c.getPA().sendFrame34a(10494, c.WillKeepItem4, 3, 1);
                for (int ITEM = 0; ITEM < 28; ITEM++) {
                    if (c.playerItems[ITEM] - 1 > 0 && !(c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot) && !(c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot) && !(c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot) && !(c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)) {
                        c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM]);
                        c.EquipStatus += 1;
                    } else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt1) {
                        c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - c.WillKeepAmt1);
                        c.EquipStatus += 1;
                    } else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt2) {
                        c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - c.WillKeepAmt2);
                        c.EquipStatus += 1;
                    } else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt3) {
                        c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - c.WillKeepAmt3);
                        c.EquipStatus += 1;
                    } else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot) && c.playerItemsN[ITEM] > 1) {
                        c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - 1);
                        c.EquipStatus += 1;
                    }
                }
                for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
                    if (c.playerEquipment[EQUIP] > 0 && !(c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot) && !(c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot) && !(c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot) && !(c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)) {
                        c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]);
                        c.EquipStatus += 1;
                    } else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt1 > 0) {
                        c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - c.WillKeepAmt1);
                        c.EquipStatus += 1;
                    } else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt2 > 0) {
                        c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - c.WillKeepAmt2);
                        c.EquipStatus += 1;
                    } else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt3 > 0) {
                        c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - c.WillKeepAmt3);
                        c.EquipStatus += 1;
                    } else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - 1 > 0) {
                        c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - 1);
                        c.EquipStatus += 1;
                    }
                }

                c.ResetKeepItems();
                c.getPA().showInterface(17100);


                break;












            case 154:
                if (System.currentTimeMillis() - c.logoutDelay < 8000) {
                    c.sendMessage("You cannot do skillcape emotes in combat!");
                    return;
                }
                if (System.currentTimeMillis() - c.lastEmote >= 7000) {
                    if (c.getPA().wearingCape(c.playerEquipment[c.playerCape])) {
                        c.stopMovement();
                        c.gfx0(c.getPA().skillcapeGfx(c.playerEquipment[c.playerCape]));
                        c.startAnimation(c.getPA().skillcapeEmote(c.playerEquipment[c.playerCape]));
                    } else {
                        c.sendMessage("You must be wearing a Skillcape to do this emote.");
                    }
                    c.lastEmote = System.currentTimeMillis();
                }
                break;
                /*case 113241:
                if (c.inWild()) return;
                c.getPA().spellTeleport(2736, 3475, 0);
                break;*/
                /*****START WITH BUTTONS ON CLIENT*********/
            case 82020:
                if (c.inWild()) {
                    c.sendMessage("<shad=6081134>Sorry but you can't deposit in the wilderness.");
                    return;
                } else {
                    c.getItems().depositInventory();
                }
                break;
            case 82024:
                c.sendMessage("<shad=6081134>You deposite your worn items!");
                for (int i = 0; i < c.playerEquipment.length; i++) {
                    int itemId = c.playerEquipment[i];
                    int itemAmount = c.playerEquipmentN[i];
                    c.getItems().removeItem(itemId, i);
                    c.getItems().bankItem(itemId, c.getItems().getItemSlot(itemId), itemAmount);
                }
                break;



            case 108003:
                if (c.xpLock == false) {
                    c.xpLock = true;
                    c.sendMessage("Your XP is now LOCKED!");
                } else {
                    c.xpLock = false;
                    c.sendMessage("Your XP is now UNLOCKED!");
                }
                break;




                /*case 108006: //items kept on death?
		c.StartBestItemScan();
		c.EquipStatus = 0;
		for (int k = 0; k < 4; k++)
			c.getPA().sendFrame34a(10494, -1, k, 1);
		for (int k = 0; k < 39; k++)
			c.getPA().sendFrame34a(10600, -1, k, 1);
		if(c.WillKeepItem1 > 0)
			c.getPA().sendFrame34a(10494, c.WillKeepItem1, 0, c.WillKeepAmt1);
		if(c.WillKeepItem2 > 0)
			c.getPA().sendFrame34a(10494, c.WillKeepItem2, 1, c.WillKeepAmt2);
		if(c.WillKeepItem3 > 0)
			c.getPA().sendFrame34a(10494, c.WillKeepItem3, 2, c.WillKeepAmt3);
		if(c.WillKeepItem4 > 0)
			c.getPA().sendFrame34a(10494, c.WillKeepItem4, 3, 1);
		for(int ITEM = 0; ITEM < 28; ITEM++){
			if(c.playerItems[ITEM]-1 > 0 && !(c.playerItems[ITEM]-1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
 			&& !(c.playerItems[ITEM]-1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
 			&& !(c.playerItems[ITEM]-1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
 			&& !(c.playerItems[ITEM]-1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)){
				c.getPA().sendFrame34a(10600, c.playerItems[ITEM]-1, c.EquipStatus, c.playerItemsN[ITEM]);
				c.EquipStatus += 1;
			} else if(c.playerItems[ITEM]-1 > 0 && (c.playerItems[ITEM]-1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt1){
				c.getPA().sendFrame34a(10600, c.playerItems[ITEM]-1, c.EquipStatus, c.playerItemsN[ITEM]-c.WillKeepAmt1);
				c.EquipStatus += 1;
			} else if(c.playerItems[ITEM]-1 > 0 && (c.playerItems[ITEM]-1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt2){
				c.getPA().sendFrame34a(10600, c.playerItems[ITEM]-1, c.EquipStatus, c.playerItemsN[ITEM]-c.WillKeepAmt2);
				c.EquipStatus += 1;
			} else if(c.playerItems[ITEM]-1 > 0 && (c.playerItems[ITEM]-1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt3){
				c.getPA().sendFrame34a(10600, c.playerItems[ITEM]-1, c.EquipStatus, c.playerItemsN[ITEM]-c.WillKeepAmt3);
				c.EquipStatus += 1;
			} else if(c.playerItems[ITEM]-1 > 0 && (c.playerItems[ITEM]-1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot) && c.playerItemsN[ITEM] > 1){
				c.getPA().sendFrame34a(10600, c.playerItems[ITEM]-1, c.EquipStatus, c.playerItemsN[ITEM]-1);
				c.EquipStatus += 1;
			}
		}
		for(int EQUIP = 0; EQUIP < 14; EQUIP++){
			if(c.playerEquipment[EQUIP] > 0 && !(c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP+28 == c.WillKeepItem1Slot)
			&& !(c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP+28 == c.WillKeepItem2Slot)
			&& !(c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP+28 == c.WillKeepItem3Slot)
			&& !(c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP+28 == c.WillKeepItem4Slot)){
				c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]);
				c.EquipStatus += 1;
			} else if(c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP+28 == c.WillKeepItem1Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP]-c.WillKeepAmt1 > 0){
				c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]-c.WillKeepAmt1);
				c.EquipStatus += 1;
			} else if(c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP+28 == c.WillKeepItem2Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP]-c.WillKeepAmt2 > 0){
				c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]-c.WillKeepAmt2);
				c.EquipStatus += 1;
			} else if(c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP+28 == c.WillKeepItem3Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP]-c.WillKeepAmt3 > 0){
				c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]-c.WillKeepAmt3);
				c.EquipStatus += 1;
			} else if(c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP+28 == c.WillKeepItem4Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP]-1 > 0){
				c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]-1);
				c.EquipStatus += 1;
			}
		}

	          	c.ResetKeepItems();
				c.getPA().showInterface(17100);
				

			break;*/

            case 108005:
                c.getPA().showInterface(19148);
                break;





                /** EQUIPMENT TAB **/
            case 59103:
                if (c.xpLock == false) {
                    c.xpLock = true;
                    c.getPA().sendFrame126("@red@Xp Locked", 15226);
                } else if (c.xpLock == true) {
                    c.xpLock = false;
                    c.getPA().sendFrame126("@gre@Xp Unlocked", 15226);
                }
                break;
                /*case 154:
//c.freezeTimer = 8;
c.stopMovement();

if (!c.skillCapeEquipped()) {

c.sendMessage("You need a skill cape equiped to perform this animation.");

return;

}

if (c.playerEquipment[c.playerCape] == 9747 || c.playerEquipment[c.playerCape] == 10639 || c.playerEquipment[c.playerCape] == 9748) { //attack

c.gfx0(823);

c.startAnimation(4959);

}

if (c.playerEquipment[c.playerCape] == 9753 || c.playerEquipment[c.playerCape] == 10641 || c.playerEquipment[c.playerCape] == 9754) { //Defence

c.gfx0(824);

c.startAnimation(4961);

}

if (c.playerEquipment[c.playerCape] == 9750 || c.playerEquipment[c.playerCape] == 10640 || c.playerEquipment[c.playerCape] == 9751) { //Strength

c.gfx0(828);

c.startAnimation(4981);

}

if (c.playerEquipment[c.playerCape] == 9768 || c.playerEquipment[c.playerCape] == 10647 || c.playerEquipment[c.playerCape] == 9769) { //Hitpoints

c.gfx0(833);

c.startAnimation(4971);

}

if (c.playerEquipment[c.playerCape] == 9756 || c.playerEquipment[c.playerCape] == 10642 || c.playerEquipment[c.playerCape] == 9757) { //Range

c.gfx0(832);

c.startAnimation(4973);

}

if (c.playerEquipment[c.playerCape] == 9759 || c.playerEquipment[c.playerCape] == 10643 || c.playerEquipment[c.playerCape] == 9760) { //Prayer

c.gfx0(829);

c.startAnimation(4979);

}

if (c.playerEquipment[c.playerCape] == 9762 || c.playerEquipment[c.playerCape] == 10644 || c.playerEquipment[c.playerCape] == 9763) { //Magic

c.gfx0(813);

c.startAnimation(4939);

}

if (c.playerEquipment[c.playerCape] == 9801 || c.playerEquipment[c.playerCape] == 10658 || c.playerEquipment[c.playerCape] == 9802) { //Cooking

c.gfx0(821);

c.startAnimation(4955);

}

if (c.playerEquipment[c.playerCape] == 9807 || c.playerEquipment[c.playerCape] == 10660 || c.playerEquipment[c.playerCape] == 9808) { //Woodcutting

c.gfx0(822);

c.startAnimation(4957);

}

if (c.playerEquipment[c.playerCape] == 9783 || c.playerEquipment[c.playerCape] == 10652 || c.playerEquipment[c.playerCape] == 9784) { //Fletching

c.gfx0(812);

c.startAnimation(4937);

}

if (c.playerEquipment[c.playerCape] == 9798 || c.playerEquipment[c.playerCape] == 10657 || c.playerEquipment[c.playerCape] == 9799) { //Fishing

c.gfx0(819);

c.startAnimation(4951);

}

if (c.playerEquipment[c.playerCape] == 9804 || c.playerEquipment[c.playerCape] == 10659 || c.playerEquipment[c.playerCape] == 9805) { //Firemaking

c.gfx0(831);

c.startAnimation(4975);

}

if (c.playerEquipment[c.playerCape] == 9780 || c.playerEquipment[c.playerCape] == 10651 || c.playerEquipment[c.playerCape] == 9781) { //Crafting

c.gfx0(818);

c.startAnimation(4949);

}

if (c.playerEquipment[c.playerCape] == 9795 || c.playerEquipment[c.playerCape] == 10656 || c.playerEquipment[c.playerCape] == 9796) { //Smithing

c.gfx0(815);

c.startAnimation(4943);

}

if (c.playerEquipment[c.playerCape] == 9792 || c.playerEquipment[c.playerCape] == 10655 || c.playerEquipment[c.playerCape] == 9793) { //Mining

c.gfx0(814);

c.startAnimation(4941);

}

if (c.playerEquipment[c.playerCape] == 9774 || c.playerEquipment[c.playerCape] == 10649 || c.playerEquipment[c.playerCape] == 9775) { //Herblore

c.gfx0(835);

c.startAnimation(4969);

}

if (c.playerEquipment[c.playerCape] == 9771 || c.playerEquipment[c.playerCape] == 10648 || c.playerEquipment[c.playerCape] == 9772) { //Agility

c.gfx0(830);

c.startAnimation(4977);

}

if (c.playerEquipment[c.playerCape] == 9777 || c.playerEquipment[c.playerCape] == 10650 || c.playerEquipment[c.playerCape] == 9778) { //Theiving

c.gfx0(826);

c.startAnimation(4965);

}

if (c.playerEquipment[c.playerCape] == 9786 || c.playerEquipment[c.playerCape] == 10653 || c.playerEquipment[c.playerCape] == 9787) { //Slayer

c.gfx0(1656);

c.startAnimation(4967);

}

if (c.playerEquipment[c.playerCape] == 9810 || c.playerEquipment[c.playerCape] == 10661 || c.playerEquipment[c.playerCape] == 9811) { //Farming

c.gfx0(825);

c.startAnimation(4963);

}

if (c.playerEquipment[c.playerCape] == 9765 || c.playerEquipment[c.playerCape] == 10645 || c.playerEquipment[c.playerCape] == 9766) { //Runecrafting

c.gfx0(817);

c.startAnimation(4947);

}

if (c.playerEquipment[c.playerCape] == 9789 || c.playerEquipment[c.playerCape] == 10654 || c.playerEquipment[c.playerCape] == 9790) { //Construction

c.gfx0(820);

c.startAnimation(4953);

}

if (c.playerEquipment[c.playerCape] == 9948 || c.playerEquipment[c.playerCape] == 10646 || c.playerEquipment[c.playerCape] == 9949) { // hunter

c.gfx0(907);

c.startAnimation(5158);

}

if (c.playerEquipment[c.playerCape] == 9813 || c.playerEquipment[c.playerCape] == 10662) { //Quest

c.gfx0(816);

c.startAnimation(4945);



}
break;*/
            case 59100:
                if (!c.isSkulled) {
                    c.getItems().resetKeepItems();
                    c.getItems().keepItem(0, false);
                    c.getItems().keepItem(1, false);
                    c.getItems().keepItem(2, false);
                    c.getItems().keepItem(3, false);
                    c.sendMessage("You can keep three items and a fourth if you use the protect item prayer.");
                } else {
                    c.getItems().resetKeepItems();
                    c.getItems().keepItem(0, false);
                    c.sendMessage("You are skulled and will only keep one item if you use the protect item prayer.");
                }
                c.getItems().sendItemsKept();
                c.getPA().showInterface(6960);
                c.getItems().resetKeepItems();
                break;

            case 59097:
                c.getPA().showInterface(15106);
                break;

            case 28164:
                if (c.inWild()) return;
                c.getPA().spellTeleport(2736, 3473, 0);

                break;

            case 51023:
            case 6005:
                if (c.inWild()) return;
                //c.getPA().startTeleport(Config.LUMBY_X, Config.LUMBY_Y, 0, "modern");
                c.getDH().sendOption5("Bandits", "Taverly Dungeon", "Slayer Tower", "Brimhaven Dungeon", "Island");
                c.teleAction = 1;
                break;

            case 50235:
            case 4140:
            case 117112:
                if (c.inWild()) return;
                c.getDH().sendDialogues(10005, -1);
                break;

            case 28171:
                c.getPA().sendFrame126("", 12000);
                break;
            case 28170:
                c.getPA().sendFrame126("", 12000);
                break;







            case 33206:
                c.forcedText = "[Quick Chat] My Attack level is " + c.playerLevel[0] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33209:
                c.forcedText = "[Quick Chat] My Strength level is " + c.playerLevel[2] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33212:
                c.forcedText = "[Quick Chat] My Defence level is " + c.playerLevel[1] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33215:
                c.forcedText = "[Quick Chat] My Range level is " + c.playerLevel[4] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33218:
                c.forcedText = "[Quick Chat] My Prayer level is " + c.playerLevel[5] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33221:
                c.forcedText = "[Quick Chat] My Magic level is " + c.playerLevel[6] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33224:
                c.forcedText = "[Quick Chat] My Runecrafting level is " + c.playerLevel[20] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33207:
                c.forcedText = "[Quick Chat] My Hitpoints level is " + c.playerLevel[3] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33210:
                c.forcedText = "[Quick Chat] My Agility level is " + c.playerLevel[16] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33213:
                c.forcedText = "[Quick Chat] My Herblore level is " + c.playerLevel[15] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33216:
                c.forcedText = "[Quick Chat] My Thieving level is " + c.playerLevel[17] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33219:
                c.forcedText = "[Quick Chat] My Crafting level is " + c.playerLevel[12] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33222:
                c.forcedText = "[Quick Chat] My Fletching level is " + c.playerLevel[9] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33208:
                c.forcedText = "[Quick Chat] My Mining level is " + c.playerLevel[14] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33211:
                c.forcedText = "[Quick Chat] My Smithing level is " + c.playerLevel[13] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33214:
                c.forcedText = "[Quick Chat] My Fishing level is " + c.playerLevel[10] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33217:
                c.forcedText = "[Quick Chat] My Cooking level is " + c.playerLevel[7] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 54104:
                c.forcedText = "[Quick Chat] My Farming level is " + c.playerLevel[19] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33220:
                c.forcedText = "[Quick Chat] My Firemaking level is " + c.playerLevel[11] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;

            case 33223:
                c.forcedText = "[Quick Chat] My Woodcutting level is " + c.playerLevel[8] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;
            case 33226:
                c.forcedText = "[Quick Chat] My Farming level is " + c.playerLevel[19] + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;



                /*case 87241:
			c.gfx0(2266);
			c.startAnimation(12552);
			break;*/


            case 89216:
                //other turmoil
                //c.gfx0(2226);
                //c.startAnimation(12565);
                c.getCombat().activatePrayer(26);
                break;

            case 70100:
                // Turmoil

                //c.startAnimation(12565);
                //c.gfx0(2226);
                c.getCombat().activatePrayer(26);


                break;

                /*case 87231: // protext items
			c.getCombat().activatePrayer(10);
				c.startAnimation(12567);
				c.gfx0(2213);*/

                //1st tele option
            case 9190:
                if (c.dialogueAction == 10005) {
                    c.getPA().startTeleport(3171, 2989, 0, "modern");
                }
                if (c.dialogueAction == 10013) {
                    c.getPA().startTeleport(3507, 9494, 0, "modern");
                }
                if (c.dialogueAction == 10006) {
                    c.getPA().startTeleport(3237, 9858, 0, "modern");
                }
                if (c.dialogueAction == 10015) {
                    c.getPA().startTeleport(2659, 2658, 0, "modern");
                }
                if (c.dialogueAction == 10020) {
                    c.getPA().startTeleport(3191, 3685, 0, "modern");
                }
                String type = c.playerMagicBook == 0 ? "modern" : "ancient";
                if (c.dialogueAction == 106) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15086, 1);
                        c.sendMessage("You get a six-sided die out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                } else if (c.dialogueAction == 107) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15092, 1);
                        c.sendMessage("You get a ten-sided die out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                }
                if (c.teleAction == 2) {
                    //barrows
                    c.getPA().spellTeleport(3565, 3314, 0);
                } else if (c.dialogueAction == 10011) {
                    //godwars
                    c.getPA().spellTeleport(2880, 5311, 2);
                    c.getPA().walkableInterface(16210);
                    c.sendMessage("If you get stuck do ::stuck to get unstuck :)");
                    c.sendMessage("Armadly has MOVED talk to the npc infront of the grapple place!");
                    c.Arma = 15;
                    c.Band = 15;
                    c.Sara = 15;
                    c.Zammy = 15;
                } else if (c.teleAction == 44) {
                    c.setSidebarInterface(6, 12855);
                    c.playerMagicBook = 1;
                    c.autocastId = -1;
                    c.getPA().resetAutocast();
                } else if (c.teleAction == 1) {
                    c.getPA().spellTeleport(3176, 2987, 0);

                    break;
                } else if (c.dialogueAction == 40) {
                    c.getPA().startTeleport(3366, 3266, 0, "modern");
                } else if (c.dialogueAction == 46) {
                    c.getPA().startTeleport(2271, 3476, 0, "modern");
                } else if (c.dialogueAction == 800) {
                    c.getPA().startTeleport(2522, 4777, 0, "modern");
                } else if (c.dialogueAction == 47) {
                    c.getPA().startTeleport(3053, 3850, 0, "modern");
                } else if (c.teleAction == 4) {
                    //varrock wildy
                    c.getPA().spellTeleport(2539, 4716, 0);
                } else if (c.teleAction == 5) {
                    c.getPA().spellTeleport(3046, 9779, 0);
                } else if (c.teleAction == 9) {
                    c.getPA().spellTeleport(3087, 3499, 0);
                }

                if (c.dialogueAction == 10) {
                    c.getPA().spellTeleport(2845, 4832, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 11) {
                    c.getPA().spellTeleport(2786, 4839, 0);
                    c.dialogueAction = -1;

                } else if (c.dialogueAction == 98) { // Pure starter
                    c.gotStarter = 1;
                    c.playerXP[0] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
                    c.getPA().refreshSkill(0);
                    c.playerXP[4] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
                    c.getPA().refreshSkill(4);
                    c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                    c.getPA().refreshSkill(3);
                    c.playerXP[5] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.getPA().refreshSkill(5);
                    c.playerXP[6] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
                    c.getPA().refreshSkill(6);
                    c.playerXP[2] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
                    c.getPA().refreshSkill(2);
                    c.getPA().addStarter2();
                    c.getPA().closeAllWindows();

                } else if (c.dialogueAction == 12) {
                    c.getPA().spellTeleport(2398, 4841, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 25) {
                    for (int j = 0; j < c.playerEquipment.length; j++) {
                        if (c.playerEquipment[j] > 0) {
                            c.sendMessage("Please remove all your equipment before using this command.");
                            return;
                        }
                    }
                    c.playerXP[1] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[1] = c.getPA().getLevelForXP(c.playerXP[1]);
                    c.getPA().refreshSkill(1);
                }
                break;
                //mining - 3046,9779,0
                //smithing - 3079,9502,0

                //2nd tele option
            case 9191:
                if (c.dialogueAction == 10010) {
                    c.getPA().startTeleport(3302, 9371, 0, "modern");
                }
                if (c.dialogueAction == 10011) {
                    c.getPA().startTeleport(3052, 9567, 0, "modern");
                }
                if (c.dialogueAction == 10005) {
                    c.getPA().startTeleport(3555, 9945, 0, "modern");
                }
                if (c.dialogueAction == 10006) {
                    c.getPA().startTeleport(2710, 9466, 0, "modern");
                }
                if (c.dialogueAction == 10020) {
                    c.getPA().startTeleport(3162, 3490, 2, "modern");
                }
                if (c.dialogueAction == 10021) {
                    c.getPA().startTeleport(2539, 4716, 0, "modern");
                }

                if (c.dialogueAction == 106) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15088, 1);
                        c.sendMessage("You get two six-sided dice out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                } else if (c.dialogueAction == 107) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15094, 1);
                        c.sendMessage("You get a twelve-sided die out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                }

                if (c.teleAction == 1) {
                    //tav dungeon
                    c.getPA().spellTeleport(2884, 9798, 0);
                } else if (c.teleAction == 2) {
                    //pest control
                    c.getPA().spellTeleport(2662, 2650, 0);
                } else if (c.teleAction == 44) {
                    c.setSidebarInterface(6, 1151);
                    c.playerMagicBook = 0;
                    c.autocastId = -1;
                    c.getPA().resetAutocast();
                } else if (c.teleAction == 3) {
                    //kbd
                    c.getPA().spellTeleport(3007, 3849, 0);
                } else if (c.teleAction == 4) {
                    //graveyard
                    c.getPA().spellTeleport(3350, 3712, 0);
                } else if (c.teleAction == 5) {
                    c.getPA().spellTeleport(3079, 9502, 0);
                } else if (c.teleAction == 9) {
                    c.getPA().spellTeleport(3223, 3218, 0); //Lumbridge
                } else if (c.dialogueAction == 25) {
                    for (int j = 0; j < c.playerEquipment.length; j++) {
                        if (c.playerEquipment[j] > 0) {
                            c.sendMessage("Please remove all your equipment before using this command.");
                            return;
                        }
                    }
                    c.playerXP[0] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
                    c.getPA().refreshSkill(0);
                }
                if (c.dialogueAction == 10) {
                    c.getPA().spellTeleport(2792, 4828, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 98) { // Main starter
                    c.playerXP[0] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
                    c.getPA().refreshSkill(0);
                    c.playerXP[1] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[1] = c.getPA().getLevelForXP(c.playerXP[1]);
                    c.getPA().refreshSkill(1);
                    c.playerXP[4] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
                    c.getPA().refreshSkill(4);
                    c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                    c.getPA().refreshSkill(3);
                    c.playerXP[5] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.getPA().refreshSkill(5);
                    c.playerXP[6] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
                    c.getPA().refreshSkill(6);
                    c.playerXP[2] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
                    c.getPA().refreshSkill(2);
                    c.gotStarter = 1;
                    c.getPA().addStarter();
                    c.getPA().closeAllWindows();
                } else if (c.dialogueAction == 11) {
                    c.getPA().spellTeleport(2527, 4833, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 12) {
                    c.getPA().startTeleport(2464, 4834, 0, "modern");
                    c.dialogueAction = -1;
                }
                if (c.dialogueAction == 40) {
                    c.getPA().startTeleport(2401, 5180, 0, "modern");
                }
                if (c.dialogueAction == 800) {
                    c.getPA().startTeleport(3019, 3631, 0, "modern");
                }
                if (c.dialogueAction == 47) {
                    c.getPA().startTeleport(2906, 3613, 0, "modern");
                }
                break;
                //3rd tele option	

            case 9192:
                if (c.dialogueAction == 10020) {
                    c.getPA().startTeleport(3341, 3669, 0, "modern");
                }
                if (c.dialogueAction == 10011) {
                    c.getPA().startTeleport(2722, 5105, 0, "modern");
                }
                if (c.dialogueAction == 10010) {
                    c.getPA().spellTeleport(1910, 4367, 0);
                }
                if (c.dialogueAction == 10010) {
                    c.getPA().startTeleport(3270, 3914, 0, "modern");
                }
                if (c.dialogueAction == 10005) {
                    c.getPA().startTeleport(2666, 3717, 0, "modern");
                }
                if (c.dialogueAction == 106) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15100, 1);
                        c.sendMessage("You get a four-sided die out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                } else if (c.dialogueAction == 107) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15096, 1);
                        c.sendMessage("You get a twenty-sided die out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                }
                if (c.teleAction == 1) {
                    //slayer tower
                    c.getPA().spellTeleport(3428, 3537, 0);
                } else if (c.teleAction == 2) {
                    //tzhaar
                    c.getPA().spellTeleport(2399, 5178, 0);
                    c.sendMessage("Enter the barrier to play, for firecape go east enter cave!");
                } else if (c.teleAction == 44) {
                    c.setSidebarInterface(6, 29999);
                    c.playerMagicBook = 2;
                    c.autocastId = -1;
                    c.getPA().resetAutocast();
                } else if (c.teleAction == 3) {
                    //dag kings
                    c.getPA().spellTeleport(1910, 4367, 0);
                    c.sendMessage("Climb down the ladder to get into the lair.");
                } else if (c.teleAction == 4) {
                    //44 portals
                    c.getPA().spellTeleport(2975, 3873, 0);
                } else if (c.teleAction == 5) {
                    c.getPA().spellTeleport(2472, 3437, 0); //agility
                } else if (c.teleAction == 9) {
                    c.getPA().spellTeleport(3214, 3424, 0); //Varrock
                } else if (c.dialogueAction == 25) {
                    for (int j = 0; j < c.playerEquipment.length; j++) {
                        if (c.playerEquipment[j] > 0) {
                            c.sendMessage("Please remove all your equipment before using this command.");
                            return;
                        }
                    }
                    c.playerXP[5] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.getPA().refreshSkill(5);
                }
                if (c.dialogueAction == 10) {
                    c.getPA().spellTeleport(2713, 4836, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 98) { // Zerker starter
                    c.playerXP[0] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
                    c.getPA().refreshSkill(0);
                    c.playerXP[1] = c.getPA().getXPForLevel(45) + 5;
                    c.playerLevel[1] = c.getPA().getLevelForXP(c.playerXP[1]);
                    c.getPA().refreshSkill(1);
                    c.playerXP[4] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
                    c.getPA().refreshSkill(4);
                    c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                    c.getPA().refreshSkill(3);
                    c.playerXP[5] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.getPA().refreshSkill(5);
                    c.playerXP[6] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
                    c.getPA().refreshSkill(6);
                    c.playerXP[2] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
                    c.getPA().refreshSkill(2);
                    c.gotStarter = 1;
                    c.getPA().addStarter3();
                    c.getPA().closeAllWindows();
                } else if (c.dialogueAction == 11) {
                    c.getPA().spellTeleport(2162, 4833, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 12) {
                    c.getPA().spellTeleport(2207, 4836, 0);
                    c.dialogueAction = -1;
                }
                if (c.dialogueAction == 40) {
                    c.getPA().startTeleport(3565, 3311, 0, "modern");
                }
                if (c.dialogueAction == 800) {
                    c.getPA().startTeleport(3296, 3650, 0, "modern");
                }
                if (c.dialogueAction == 47) {
                    c.getPA().startTeleport(2858, 3755, 0, "modern");
                }
                break;
                //4th tele option
            case 9193:
                if (c.dialogueAction == 10010) {
                    c.getPA().startTeleport(2910, 4460, 0, "modern");
                }
                if (c.dialogueAction == 10020) {
                    c.getPA().startTeleport(3072, 3808, 0, "modern");
                }
                if (c.dialogueAction == 10010) {
                    c.getPA().startTeleport(1888, 4373, 0, "modern");
                }
                if (c.dialogueAction == 10005) {
                    c.getPA().startTeleport(3015, 3472, 0, "modern");
                }
                if (c.dialogueAction == 106) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15090, 1);
                        c.sendMessage("You get an eight-sided die out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                } else if (c.dialogueAction == 107) {
                    if (c.getItems().playerHasItem(c.diceID, 1)) {
                        c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);
                        c.getItems().addItem(15098, 1);
                        c.sendMessage("You get the percentile dice out of the dice bag.");
                    }
                    c.getPA().closeAllWindows();
                }
                if (c.teleAction == 1) {
                    //brimhaven dungeon
                    c.getPA().spellTeleport(2710, 9466, 0);
                } else if (c.dialogueAction == 10011) {
                    c.sendMessage("Walk north a bit to find the wildywrm.");
                    c.getPA().startTeleport(3038, 3655, 0, "modern");
                } else if (c.teleAction == 2) {
                    //duel arena
                    c.getPA().spellTeleport(3366, 3266, 0);
                } else if (c.teleAction == 3) {
                    //chaos elemental
                    c.getPA().spellTeleport(3295, 3921, 0);
                } else if (c.teleAction == 4) {
                    //gdz
                    c.getCombat().resetPrayers();
                    c.getPA().spellTeleport(2662, 3306, 0);
                } else if (c.teleAction == 5) {
                    c.getPA().spellTeleport(2724, 3484, 0);
                    c.sendMessage("For magic logs, try north of the duel arena.");
                } else if (c.teleAction == 9) {
                    c.getPA().spellTeleport(2964, 3378, 0); //Falador
                } else if (c.dialogueAction == 25) {
                    for (int j = 0; j < c.playerEquipment.length; j++) {
                        if (c.playerEquipment[j] > 0) {
                            c.sendMessage("Please remove all your equipment before using this command.");
                            return;
                        }
                    }
                    c.playerXP[2] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
                    c.getPA().refreshSkill(2);
                }
                if (c.dialogueAction == 10) {
                    c.getPA().spellTeleport(2660, 4839, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 11) {
                    c.getPA().spellTeleport(2527, 4833, 0);
                    //c.getRunecrafting().craftRunes(9075);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 98) { // Skill starter
                    c.playerXP[0] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
                    c.getPA().refreshSkill(0);
                    c.playerXP[1] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[1] = c.getPA().getLevelForXP(c.playerXP[1]);
                    c.getPA().refreshSkill(1);
                    c.playerXP[4] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
                    c.getPA().refreshSkill(4);
                    c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                    c.getPA().refreshSkill(3);
                    c.playerXP[5] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.getPA().refreshSkill(5);
                    c.playerXP[6] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
                    c.getPA().refreshSkill(6);
                    c.playerXP[2] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
                    c.getPA().refreshSkill(2);
                    c.gotStarter = 1;
                    c.getPA().addStarter5();
                    c.getPA().closeAllWindows();
                }
                //} else if (c.dialogueAction == 12) {
                //c.getPA().spellTeleport(2464, 4834, 0); bloods here
                //  c.getRunecrafting().craftRunes(2489);
                //  c.dialogueAction = -1;
                //  }
                if (c.dialogueAction == 800) {
                    c.getDH().sendOption4("East Dragons - @gre@Single", "Mage Bank - @gre@Single", "Coming Soon!", "Coming Soon!");
                    c.dialogueAction = 801;
                }
                if (c.dialogueAction == 46) {
                    c.getDH().sendOption5("Dagannoths  - @gre@Safe", "Coming Soon!", "Coming Soon!", "Coming Soon!", "Coming Soon!");
                    c.dialogueAction = 47;
                }
                break;
                //5th tele option
            case 9194:
                if (c.dialogueAction == 10020) {
                    c.getDH().sendDialogues(10021, -1);
                }
                if (c.dialogueAction == 10005) {
                    c.getDH().sendDialogues(10006, -1);
                }
                if (c.dialogueAction == 106) {
                    c.getDH().sendDialogues(107, 0);
                    break;
                } else if (c.dialogueAction == 107) {
                    c.getDH().sendDialogues(106, 0);
                    break;
                }
                if (c.teleAction == 1) {
                    //island
                    c.getPA().spellTeleport(2895, 2727, 0);
                } else if (c.teleAction == 2) {
                    //Castle Wars
                    c.getPA().spellTeleport(2442, 3089, 0);
                    c.getPA().closeAllWindows();
                } else if (c.teleAction == 3) {
                    //last monster spot
                    c.getPA().spellTeleport(3303, 9375, 0);
                    c.getPA().closeAllWindows();
                } else if (c.teleAction == 4) {
                    //ardy lever
                    c.getPA().spellTeleport(3093, 3503, 0);
                } else if (c.dialogueAction == 10010) {
                    c.getDH().sendDialogues(10011, -1);
                } else if (c.dialogueAction == 10011) {
                    c.getDH().sendDialogues(10013, -1);
                } else if (c.teleAction == 5) {
                    c.getPA().spellTeleport(2812, 3463, 0);
                } else if (c.teleAction == 9) {
                    c.getPA().spellTeleport(3093, 3244, 0); //Draynor
                } else if (c.dialogueAction == 25) {
                    for (int j = 0; j < c.playerEquipment.length; j++) {
                        if (c.playerEquipment[j] > 0) {
                            c.sendMessage("Please remove all your equipment before using this command.");
                            return;
                        }
                    }
                    c.playerXP[4] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
                    c.getPA().refreshSkill(4);
                }
                if (c.dialogueAction == 10 || c.dialogueAction == 11) {
                    c.dialogueId++;
                    c.getDH().sendDialogues(c.dialogueId, 0);
                } else if (c.dialogueAction == 98) { // Range starter
                    c.playerXP[0] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
                    c.getPA().refreshSkill(0);
                    c.playerXP[1] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[1] = c.getPA().getLevelForXP(c.playerXP[1]);
                    c.getPA().refreshSkill(1);
                    c.playerXP[4] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
                    c.getPA().refreshSkill(4);
                    c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                    c.getPA().refreshSkill(3);
                    c.playerXP[5] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.getPA().refreshSkill(5);
                    c.playerXP[6] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
                    c.getPA().refreshSkill(6);
                    c.playerXP[2] = c.getPA().getXPForLevel(99) + 5;
                    c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
                    c.getPA().refreshSkill(2);
                    c.gotStarter = 1;
                    c.getPA().addStarter4();
                    c.getPA().closeAllWindows();
                } else if (c.dialogueAction == 12) {
                    c.dialogueId = 17;
                    c.getDH().sendDialogues(c.dialogueId, 0);
                } else if (c.dialogueAction == 46) {
                    c.getDH().sendOption5("Fareed - @red@Dangerous", "Dessous - @gre@Safe", "Skeletal Wyvern - @gre@Sa", "Coming Soon!", "Coming Soon!");
                    c.dialogueAction = 47;
                } else if (c.dialogueAction == 800) {
                    c.getDH().sendOption4("East Dragons - @gre@Single", "Mage Bank - @gre@Single", "Coming Soon!", "Coming Soon!");
                    c.dialogueAction = 801;
                } else if (c.dialogueAction == 46) {
                    c.getDH().sendOption5("Fareed - @red@Dangerous", "Dessous - @gre@Safe", "Skeletal Wyvern - @gre@Safe", "Coming Soon!", "Coming Soon!");
                    c.dialogueAction = 47;
                }
                break;

            case 71074:
                if (c.clanId >= 0) Server.clanChat.changeLootshareStatus(c.playerId, c.clanId);
                else c.sendMessage("You must be in a clan to do that!");

                break;



                /* case 34185:
            case 34184:
            case 34183:
            case 34182:
            case 34189:
            case 34188:
            case 34187:
            case 34186:
            case 34193:
            case 34192:
            case 34191:
            case 34190:
                if (c.craftingLeather) c.getCrafting().handleCraftingClick(actionButtonId);
                break;*/

            case 15147:
                if (c.smeltInterface) {
                    c.smeltType = 2349;
                    c.smeltAmount = 1;
                    c.getSmithing().startSmelting(c.smeltType);
                }
                break;

            case 15151:
                if (c.smeltInterface) {
                    c.smeltType = 2351;
                    c.smeltAmount = 1;
                    c.getSmithing().startSmelting(c.smeltType);
                }
                break;


            case 15159:
                if (c.smeltInterface) {
                    c.smeltType = 2353;
                    c.smeltAmount = 1;
                    c.getSmithing().startSmelting(c.smeltType);
                }
                break;


            case 29017:
                if (c.smeltInterface) {
                    c.smeltType = 2359;
                    c.smeltAmount = 1;
                    c.getSmithing().startSmelting(c.smeltType);
                }
                break;

            case 29022:
                if (c.smeltInterface) {
                    c.smeltType = 2361;
                    c.smeltAmount = 1;
                    c.getSmithing().startSmelting(c.smeltType);
                }
                break;

            case 29026:
                if (c.smeltInterface) {
                    c.smeltType = 2363;
                    c.smeltAmount = 1;
                    c.getSmithing().startSmelting(c.smeltType);
                }
                break;
            case 58253:
                //c.getPA().showInterface(15106);
                c.getItems().writeBonus();
                break;

            case 59004:
                c.getPA().removeAllWindows();
                break;

            case 70212:
                if (c.clanId > -1) Server.clanChat.leaveClan(c.playerId, c.clanId);
                else c.sendMessage("You are not in a clan.");
                break;
            case 62137:
                if (c.clanId >= 0) {
                    c.sendMessage("You are already in a clan.");
                    break;
                }
                if (c.getOutStream() != null) {
                    c.getOutStream().createFrame(187);
                    c.flushOutStream();
                }
                break;

            case 9167:
                if (c.dialogueAction == 14) { //44s
                    c.getPA().startTeleport(2980, 3866, 0, "modern");
                } else if (c.dialogueAction == 100) {
                    c.getDH().sendDialogues(502, -1);
                } else if (c.dialogueAction == 20011) {
                    c.getShops().openShop(7);
                } else if (c.dialogueAction == 25050) {
                    c.MemberStatus();
                } else if (c.dialogueAction == 25000) {
                    c.getDH().sendDialogues(25001, -1);
                } else if (c.dialogueAction == 38) {
                    c.getPA().spellTeleport(2992, 3604, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 9292) {
                    c.sendMessage("Pay 20 P-E Points and get a random item.");
                    c.getPA().removeAllWindows();
                }
                if (c.dialogueAction == 25075) {
                    c.getDH().sendDialogues(25079, -1);
                }
                break;

            case 9169:
            	if (c.dialogueAction == 25075) {
                    if (c.slayerTask <= 0) {
                        c.getDH().sendDialogues(25078, -1);
                    } else {
                        c.getDH().sendDialogues(14, c.npcType);
                    }
                }
                if (c.dialogueAction == 38) {
                    c.getPA().spellTeleport(3087, 3513, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 100) {
                    c.getShops().openShop(77);
                } else if (c.dialogueAction == 25050) {
                    c.getShops().openShop(66);
                } else if (c.dialogueAction == 25000) {
                    c.getShops().openShop(30);
                } else if (c.dialogueAction == 20011) {
                    c.getShops().openShop(6);
                } else if (c.dialogueAction == 9292) {
                    c.getPA().removeAllWindows();
                }
                break;

            case 9168:
                if (c.dialogueAction == 25075) {
                    if (c.slayerTask <= 0) {
                        c.getDH().sendDialogues(12, -1);
                    } else {
                        c.getDH().sendDialogues(25074, -1);
                    }
                }
                if (c.dialogueAction == 14) { //EASTS
                    c.getPA().startTeleport(3357, 3721, 0, "modern");
                } else if (c.dialogueAction == 100) {
                    c.getPA().movePlayer(2912, 3611, c.heightLevel);
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 25050) {
                    c.getShops().openShop(65);
                } else if (c.dialogueAction == 20011) {
                    c.getShops().openShop(5);
                } else if (c.dialogueAction == 25000) {
                    c.getShops().openShop(29);
                } else if (c.dialogueAction == 38) {
                    c.getPA().spellTeleport(2539, 4714, 0);
                    c.dialogueAction = -1;
                } else if (c.dialogueAction == 9292 && c.pePoints >= 50) {
                    c.BlowShitUp();
                } else if (c.dialogueAction == 9292 && c.pePoints < 50) {
                    c.getPA().sendStatement("You need @red@50@bla@ Project-E points to use this chest.");
                }
                break;

            case 9177:
                if (c.dialogueAction == 15) { //MB
                    c.getPA().startTeleport(2541, 4714, 0, "modern");
                }
                break;

            case 9178:
                if (c.dialogueAction == 10001) {
                    c.getDH().sendDialogues(17, -1);
                }
                if (c.dialogueAction == 10000) {
                    c.getPA().startTeleport(2329, 3690, 0, "modern");
                    c.sendMessage("You teleport to Skilling area.");
                }
                if (c.dialogueAction == 185) {
                    c.getPA().ExplainLottery();
                }
                if (c.dialogueAction == 15) { //fight pits
                    //c.sendMessage("Disabled ");
                    c.getPA().startTeleport(2444, 5171, 0, "modern");
                }
                if (c.teleAction == 56) {
                    //rock crabs
                    c.getPA().spellTeleport(2676, 3715, 0);
                }
                if (c.dialogueAction == 14) { //Castle
                    c.getPA().startTeleport(3013, 3631, 0, "modern");
                }
                if (c.usingbook && c.sarabook) {
                    c.forcedText = "In the name of Saradomin, Protector of us all, I now join you in the eyes of saradomin";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.zambook) {
                    c.forcedText = "Two great warriors, joined by hand, to spread destruction across the land, In Zamorak's name, now two are one.";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.guthbook) {
                    c.forcedText = "Light and dark, day and night, balance arises from contrast, I unify thee in the name of guthix";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.dialogueAction == 36899) {
                    c.sendMessage("You can exchange 1 billion GP (<col=255>1,000,000,000</col>) for a <col=255>Billion Ticket</col> or the opposite.");
                    c.sendMessage("These are tradeable!");
                    c.sendMessage("[<col=642>WARNING</col>] Make Sure You Have Atleast 2 Free Inventory Spaces!");
                    c.getPA().removeAllWindows();
                }
                if (c.usingGlory) c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern");
                if (c.dialogueAction == 2) c.getPA().startTeleport(3428, 3538, 0, "modern");
                if (c.dialogueAction == 3) c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern");
                if (c.dialogueAction == 4) c.getPA().startTeleport(3565, 3314, 0, "modern");
                if (c.dialogueAction == 20) {
                    c.getPA().startTeleport(2897, 3618, 4, "modern");
                    c.killCount = 0;
                }
                break;
            case 9179:
                if (c.dialogueAction == 10000) {
                    c.sendMessage("You teleported to the agility area.");
                    c.getPA().startTeleport(2472, 3437, 0, "modern");
                }
                if (c.dialogueAction == 185) {
                    c.getDH().sendDialogues(187, -1);
                }
                if (c.dialogueAction == 10001) {
                    c.sendMessage("You teleported to the farming area.");
                    c.sendMessage("Only one patch working for the moment...");
                    c.getPA().startTeleport(2814, 3462, 0, "modern");
                }
                if (c.dialogueAction == 15) { //duel arena
                    c.getPA().startTeleport(3366, 3266, 0, "modern");
                }
                if (c.usingbook && c.sarabook) {
                    c.forcedText = "Thy cause was false, thy skills did lack. see you in lumbridge when you get back";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.zambook) {
                    c.forcedText = "The weak deserve to die, so that the strong may florish, This is the creed of zamorak";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.guthbook) {
                    c.forcedText = "Thy death was not in vain, for it brought some balance to the world, may guthix bring you rest";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.teleAction == 56) {
                    //experiments
                    c.getPA().spellTeleport(3555, 9945, 0);
                }
                if (c.dialogueAction == 36899) {
                    c.getPA().Change();
                }
                if (c.dialogueAction == 35) {
                    c.getPA().startTeleport(2865, 9954, 0, "modern");
                    c.sendMessage("You teleport to ice warriors.");
                }
                if (c.usingGlory) c.getPA().startTeleport(Config.AL_KHARID_X, Config.AL_KHARID_Y, 0, "modern");
                if (c.dialogueAction == 2) c.getPA().startTeleport(2884, 3395, 0, "modern");
                if (c.dialogueAction == 3) c.getPA().startTeleport(3243, 3513, 0, "modern");
                if (c.dialogueAction == 4) c.getPA().startTeleport(2444, 5170, 0, "modern");
                if (c.dialogueAction == 20) {
                    c.getPA().startTeleport(2897, 3618, 12, "modern");
                    c.killCount = 0;
                }
                break;

            case 9180:
                if (c.dialogueAction == 15) { //pest control
                    c.getPA().startTeleport(2658, 2649, 0, "modern");
                }
                if (c.dialogueAction == 185) {
                    c.getDH().sendDialogues(186, -1);
                }
                if (c.dialogueAction == 36899) {
                    c.getPA().UnChange();
                }
                if (c.dialogueAction == 10000) {
                    c.getPA().startTeleport(2856, 3434, 0, "modern");
                    c.sendMessage("You teleport to the fishing/cooking area.");
                }
                if (c.usingbook && c.sarabook) {
                    c.forcedText = "go in peace in the name of saradomin, may his glory shine upon you like the sun.";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.zambook) {
                    c.forcedText = "May your bloodthirst never be sated, and may all your battles be glorious, Zamorak bring you strength";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.guthbook) {
                    c.forcedText = "May you walk the path and never fall, for guthix walks beside thee on thy journey, may guthix bring you peace";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.dialogueAction == 35) {
                    c.getPA().startTeleport(2905, 3611, 4, "modern");
                    c.sendMessage("You teleport to shadow warriors.");
                }
                if (c.usingGlory) c.getPA().startTeleport(Config.KARAMJA_X, Config.KARAMJA_Y, 0, "modern");
                if (c.dialogueAction == 2) c.getPA().startTeleport(2471, 10137, 0, "modern");
                if (c.dialogueAction == 3) c.getPA().startTeleport(3363, 3676, 0, "modern");
                if (c.dialogueAction == 4) c.getPA().startTeleport(2659, 2676, 0, "modern");
                if (c.dialogueAction == 20) {
                    c.getPA().startTeleport(2897, 3618, 8, "modern");
                    c.killCount = 0;
                }
                break;

            case 9181:
                if (c.dialogueAction == 15) { //barrows
                    c.getPA().startTeleport(3564, 3288, 0, "modern");
                }
                if (c.dialogueAction == 185) {
                    c.getDH().sendDialogues(187, -1);
                }
                if (c.usingbook && c.sarabook) {
                    c.forcedText = "Walk proud, and show mercy, for you cary my name in your heart, this is saradomin's wisdom";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.zambook) {
                    c.forcedText = "There is no opinion that cannot be proven true. By crushing those who choose to disagree with it, Zamorak Bring me Strength!";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.usingbook && c.guthbook) {
                    c.forcedText = "A Journey of a single step, may take thee over a thousand miles, may guthix bring you balance";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;
                    c.startAnimation(1670);
                }
                if (c.teleAction == 56) {
                    //ape atol
                    c.getPA().spellTeleport(2794, 2791, 0);
                }
                if (c.dialogueAction == 30) {
                    c.getPA().startTeleport(3040, 4842, 0, "modern");
                    c.sendMessage("You teleport to the runecrafting area.");
                }
                if (c.dialogueAction == 30) {
                    c.getPA().startTeleport(3040, 4842, 0, "modern");
                    c.sendMessage("You teleport to the runecrafting area.");
                }
                if (c.usingGlory) c.getPA().startTeleport(Config.MAGEBANK_X, Config.MAGEBANK_Y, 0, "modern");
                if (c.dialogueAction == 2) c.getPA().startTeleport(2669, 3714, 0, "modern");
                if (c.dialogueAction == 3) c.getPA().startTeleport(2540, 4716, 0, "modern");
                if (c.dialogueAction == 10000) c.getDH().sendDialogues(10001, -1);
                if (c.dialogueAction == 4) {
                    c.getPA().startTeleport(3366, 3266, 0, "modern");
                    c.sendMessage("Dueling is at your own risk. Refunds will not be given for items lost due to glitches.");
                }
                if (c.dialogueAction == 20) {
                    c.getPA().startTeleport(3366, 3266, 0, "modern");
                    c.killCount = 0;

                }

                break;
            case 9182:
                if (c.dialogueAction == 20) {
                    c.getPA().startTeleport(2898, 3618, 12, "modern");
                    c.killCount = 0;

                }
            case 9183:
                if (c.dialogueAction == 20) {
                    c.getPA().startTeleport(2898, 3618, 8, "modern");
                    c.killCount = 0;

                }




            case 1093:
            case 1094:
            case 1097:
                if (c.autocastId > 0) {
                    c.getPA().resetAutocast();
                } else {
                    if (c.playerMagicBook == 1) {
                        if (c.playerEquipment[c.playerWeapon] == 4675) c.setSidebarInterface(0, 1689);
                        else c.sendMessage("You can't autocast ancients without an ancient staff.");
                    } else if (c.playerMagicBook == 0) {
                        if (c.playerEquipment[c.playerWeapon] == 4170) {
                            c.setSidebarInterface(0, 12050);
                        } else {
                            c.setSidebarInterface(0, 1829);
                        }
                    }

                }
                break;

            case 9157:
                //barrows tele to tunnels
                if (c.dialogueAction == 3516) {
                    c.getItems().addItem(3515, 1);
                    c.sendMessage("You've obtained the Completionist Cape!");
                    c.getPA().removeAllWindows();
                }
                if (c.dialogueAction == 1) {
                    int r = 4;
                    //int r = Misc.random(3);
                    switch (r) {
                        case 0:
                            c.getPA().movePlayer(3534, 9677, 0);
                            break;

                        case 1:
                            c.getPA().movePlayer(3534, 9712, 0);
                            break;

                        case 2:
                            c.getPA().movePlayer(3568, 9712, 0);
                            break;

                        case 3:
                            c.getPA().movePlayer(3568, 9677, 0);
                            break;
                        case 4:
                            c.getPA().movePlayer(3551, 9694, 0);
                            break;
                    }
                } else if (c.dialogueAction == 2) {
                    c.getPA().movePlayer(2507, 4717, 0);
                } else if (c.dialogueAction == 1032) {
                    if (c.getItems().playerHasItem(7776)) {
                        c.getItems().deleteItem(7776, 1);
                        c.memberStatus = 3;
                        c.playerRights = 6;
                        //c.MemberPoints += 20;
                        c.logout();
                    }
                } else if (c.dialogueAction == 850) {
                    c.getPA().removeAllItems();
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 1031) {
                    if (c.getItems().playerHasItem(7775)) {
                        c.getItems().deleteItem(7775, 1);
                        c.memberStatus = 2;
                        c.playerRights = 5;
                        //c.MemberPoints += 10;
                        c.logout();
                    }
                } else if (c.dialogueAction == 1030) {
                    if (c.getItems().playerHasItem(7774)) {
                        c.getItems().deleteItem(7774, 1);
                        c.memberStatus = 1;
                        c.playerRights = 4;
                        ///c.MemberPoints += 5;
                        c.logout();
                    }
                } else if (c.dialogueAction == 186) {
                    Server.lottery.enterLottery(c);
                    c.getPA().removeAllWindows();
                    c.LotteryEnter = true;
                    Achievements.CheckMiscAchievement();
                    return;
                } else if (c.dialogueAction == 20013) {
                    c.getShops().openShop(31);
                } else if (c.dialogueAction == 20012) {
                    c.getShops().openShop(9);
                } else if (c.dialogueAction == 20010) {
                    c.getShops().openShop(3);
                } else if (c.dialogueAction == 5) {
                    c.getSlayer().giveTask();
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 20000) {
                    c.getDH().sendDialogues(20005, -1);
                } else if (c.dialogueAction == 40) {
                    c.getShops().openShop(65);
                } else if (c.dialogueAction == 41) {
                    c.getPA().closeAllWindows();
                    c.getPA().movePlayer(2722, 5105, 0);
                } else if (c.dialogueAction == 115) {
                    c.getItems().addItem(13902, 1);
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 6) {
                    c.getSlayer().giveTask2();
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 77) {
                    c.getPA().movePlayer(3073, 3504, 0);
                } else if (c.dialogueAction == 25) {
                    for (int j = 0; j < c.playerEquipment.length; j++) {
                        if (c.playerEquipment[j] > 0) {
                            c.sendMessage("Please remove all your equipment before using this command.");
                            return;
                        }
                    }
                    c.playerXP[1] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[1]);
                    c.getPA().refreshSkill(1);

                } else if (c.dialogueAction == 108) {

                    c.getPA().startTeleport(3056, 9570, 0, "modern");



                } else if (c.dialogueAction == 7) {
                    c.getPA().startTeleport(3088, 3933, 0, "modern");
                    c.sendMessage("NOTE: You are now in the wilderness...");
                } else if (c.dialogueAction == 37) {
                    c.getPA().removeAllItems();
                } else if (c.dialogueAction == 8) {
                    c.getPA().fixAllBarrows();
                    c.getPA().fixAllChaotics();

                } else if (c.dialogueAction == 27) {
                    c.getPA().movePlayer(2616, 3088, 0);
                    c.monkeyk0ed = 0;
                    c.forcedText = "Wow finally got out of jail I'm glad I got a second Chance!";
                    c.forcedChatUpdateRequired = true;
                    c.updateRequired = true;

                } else if (c.usingGamesNeck) {
                    c.getPA().startTeleport2(2899, 3565, 0);
                } else if (c.usingROD) {
                    c.getPA().startTeleport(3314, 3234, 0, "modern");
                } else if (c.dialogueAction == 21) {
                    for (int j = 0; j < PvpItems.length; j++) {
                        if (c.getItems().playerHasItem(PvpItems[j])) {
                            int amount = c.getItems().getItemAmount(PvpItems[j]);
                            c.getItems().deleteItem(PvpItems[j], amount);
                            c.getItems().addItem(995, PvpPrices[j]);
                        }
                    }
                }
                c.dialogueAction = 0;
                //c.getPA().removeAllWindows();
                break;
            case 9158:
                if (c.dialogueAction == 8) {} else if (c.usingGamesNeck) {
                    c.getPA().startTeleport2(2524, 3587, 0);
                } else if (c.dialogueAction == 20013) {
                    c.getShops().openShop(32);
                } else if (c.dialogueAction == 20010) {
                    c.getShops().openShop(4);
                } else if (c.dialogueAction == 20012) {
                    c.getShops().openShop(8);
                } else if (c.dialogueAction == 20000) {
                    c.getPA().addStarter();
                    c.getDH().sendDialogues(20001, -1);
                } else if (c.dialogueAction == 6) {
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 40) {
                    c.getShops().openShop(66);
                } else if (c.dialogueAction == 186) {
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 115) {
                    c.getItems().addItem(13899, 1);
                    c.getPA().removeAllWindows();
                } else if (c.dialogueAction == 850) {
                    c.getPA().removeAllWindows();
                    return;
                } else if (c.usingROD) {
                    c.getPA().startTeleport(2441, 3089, 0, "modern");

                }

                c.dialogueAction = 0;
                //c.getPA().removeAllWindows();
                break;

            case 23132:
                c.setSidebarInterface(1, 3917);
                c.setSidebarInterface(2, 638);
                c.setSidebarInterface(3, 3213);
                c.setSidebarInterface(4, 1644);
                c.setSidebarInterface(5, 5608);
                if (c.playerMagicBook == 0) {
                    c.setSidebarInterface(6, 1151);
                } else if (c.playerMagicBook == 1) {
                    c.setSidebarInterface(6, 12855);
                } else if (c.playerMagicBook == 2) {
                    c.setSidebarInterface(6, 29999);
                }
                c.setSidebarInterface(7, 18128);
                c.setSidebarInterface(8, 5065);
                c.setSidebarInterface(9, 5715);
                c.setSidebarInterface(10, 2449);
                c.setSidebarInterface(11, 904);
                c.setSidebarInterface(12, 147);
                c.setSidebarInterface(13, 962);
                c.setSidebarInterface(0, 2423);
                if (c.playerEquipment[c.playerRing] == 7927) {
                    c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
                    c.getItems().addItem(7927, 1);
                }
                c.isNpc = false;
                c.updateRequired = true;
                c.appearanceUpdateRequired = true;
                break;

                /**Specials**/


            case 29188:
                c.specBarId = 7636; // the special attack text - sendframe126(S P E C I A L  A T T A C K, c.specBarId);
                c.usingSpecial = !c.usingSpecial;
                c.getItems().updateSpecialBar();
                break;

            case 29163:
                c.specBarId = 7611;
                c.usingSpecial = !c.usingSpecial;
                c.getItems().updateSpecialBar();
                break;

            case 33033:
                c.specBarId = 8505;
                c.usingSpecial = !c.usingSpecial;
                c.getItems().updateSpecialBar();
                break;

            case 29038:
                c.specBarId = 7486;
                if (c.playerEquipment[c.playerWeapon] == 4153) {
                    c.getCombat().handleGmaulPlayer();
                } else {
                    c.usingSpecial = !c.usingSpecial;
                }
                c.getItems().updateSpecialBar();
                break;

            case 29063:
                if (c.getCombat().checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
                    c.gfx0(246);
                    c.forcedChat("Raarrrrrgggggghhhhhhh!");
                    c.startAnimation(1056);
                    c.playerLevel[2] = c.getLevelForXP(c.playerXP[2]) + (c.getLevelForXP(c.playerXP[2]) * 15 / 100);
                    c.getPA().refreshSkill(2);
                    c.getItems().updateSpecialBar();
                } else {
                    c.sendMessage("You don't have the required special energy to use this attack.");
                }
                break;

            case 48023:
                c.specBarId = 12335;
                c.usingSpecial = !c.usingSpecial;
                c.getItems().updateSpecialBar();
                break;

                /*case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;*/
            case 29138:

                if (c.playerEquipment[c.playerWeapon] == 15001) {
                    if (c.underAttackBy > 0) {
                        if (c.getCombat().checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
                            c.gfx0(2319);
                            c.SolProtect = 120;
                            c.startAnimation(10518);
                            c.getItems().updateSpecialBar();
                            c.usingSpecial = !c.usingSpecial;
                            c.sendMessage("All damage will be split into half for 1 minute.");
                            c.getPA().sendFrame126("@bla@S P E C I A L  A T T A C K", 7562);
                        } else {
                            c.sendMessage("You don't have the required special energy to use this attack.");
                        }
                    }
                }
                c.specBarId = 7586;
                c.usingSpecial = !c.usingSpecial;
                c.getItems().updateSpecialBar();
                break;
            case 29113:
                c.specBarId = 7561;
                c.usingSpecial = !c.usingSpecial;
                c.getItems().updateSpecialBar();
                break;

            case 29238:
                c.specBarId = 7686;
                c.usingSpecial = !c.usingSpecial;
                c.getItems().updateSpecialBar();
                break;

                /**Dueling**/
            case 26065:
                // no forfeit
            case 26040:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(0);
                break;

            case 26066:
                // no movement
            case 26048:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(1);
                break;

            case 26069:
                // no range
            case 26042:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(2);
                break;

            case 26070:
                // no melee
            case 26043:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(3);
                break;

            case 26071:
                // no mage
            case 26041:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(4);
                break;

            case 26072:
                // no drinks
            case 26045:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(5);
                break;

            case 26073:
                // no food
            case 26046:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(6);
                break;

            case 26074:
                // no prayer
            case 26047:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(7);
                break;

            case 26076:
                // obsticals
            case 26075:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(8);
                break;

            case 2158:
                // fun weapons
            case 2157:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(9);
                break;

            case 30136:
                // sp attack
            case 30137:
                c.duelSlot = -1;
                c.getTradeAndDuel().selectRule(10);
                break;

            case 53245:
                //no helm
                c.duelSlot = 0;
                c.getTradeAndDuel().selectRule(11);
                break;

            case 53246:
                // no cape
                c.duelSlot = 1;
                c.getTradeAndDuel().selectRule(12);
                break;

            case 53247:
                // no ammy
                c.duelSlot = 2;
                c.getTradeAndDuel().selectRule(13);
                break;

            case 53249:
                // no weapon.
                c.duelSlot = 3;
                c.getTradeAndDuel().selectRule(14);
                break;

            case 53250:
                // no body
                c.duelSlot = 4;
                c.getTradeAndDuel().selectRule(15);
                break;

            case 53251:
                // no shield
                c.duelSlot = 5;
                c.getTradeAndDuel().selectRule(16);
                break;

            case 53252:
                // no legs
                c.duelSlot = 7;
                c.getTradeAndDuel().selectRule(17);
                break;

            case 53255:
                // no gloves
                c.duelSlot = 9;
                c.getTradeAndDuel().selectRule(18);
                break;

            case 53254:
                // no boots
                c.duelSlot = 10;
                c.getTradeAndDuel().selectRule(19);
                break;

            case 53253:
                // no rings
                c.duelSlot = 12;
                c.getTradeAndDuel().selectRule(20);
                break;

            case 53248:
                // no arrows
                c.duelSlot = 13;
                c.getTradeAndDuel().selectRule(21);
                break;


            case 26018:
                Client o = (Client) Server.playerHandler.players[c.duelingWith];
                if (o == null) {
                    c.getTradeAndDuel().declineDuel();
                    return;
                }

                if (c.duelRule[2] && c.duelRule[3] && c.duelRule[4]) {
                    c.sendMessage("You won't be able to attack the player with the rules you have set.");
                    break;
                }
                c.duelStatus = 2;
                if (c.duelStatus == 2) {
                    c.getPA().sendFrame126("Waiting for other player...", 6684);
                    o.getPA().sendFrame126("Other player has accepted.", 6684);
                }
                if (o.duelStatus == 2) {
                    o.getPA().sendFrame126("Waiting for other player...", 6684);
                    c.getPA().sendFrame126("Other player has accepted.", 6684);
                }

                if (c.duelStatus == 2 && o.duelStatus == 2) {
                    c.canOffer = false;
                    o.canOffer = false;
                    c.duelStatus = 3;
                    o.duelStatus = 3;
                    c.getTradeAndDuel().confirmDuel();
                    o.getTradeAndDuel().confirmDuel();
                }
                break;

            case 25120:
                if (c.duelStatus == 5) {
                    break;
                }
                Client o1 = (Client) Server.playerHandler.players[c.duelingWith];
                if (o1 == null) {
                    c.getTradeAndDuel().declineDuel();
                    return;
                }

                c.duelStatus = 4;
                if (o1.duelStatus == 4 && c.duelStatus == 4) {
                    c.getTradeAndDuel().startDuel();
                    o1.getTradeAndDuel().startDuel();
                    o1.duelCount = 4;
                    c.duelCount = 4;
                    c.duelDelay = System.currentTimeMillis();
                    o1.duelDelay = System.currentTimeMillis();
                } else {
                    c.getPA().sendFrame126("Waiting for other player...", 6571);
                    o1.getPA().sendFrame126("Other player has accepted", 6571);
                }
                break;

            case 34170:
                Fletching.attemptData(c, 1, false);
                break;
            case 34169:
                Fletching.attemptData(c, 5, false);
                break;
            case 34168:
                Fletching.attemptData(c, 10, false);
                break;
            case 34167:
                Fletching.attemptData(c, 28, false);
                break;
            case 34174:
                Fletching.attemptData(c, 1, true);
                break;
            case 34173:
                Fletching.attemptData(c, 5, true);
                break;
            case 34172:
                Fletching.attemptData(c, 10, true);
                break;
            case 34171:
                Fletching.attemptData(c, 28, true);
                break;
            case 34185:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 1, 0);
                } else {

                }
                break;
            case 34184:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 5, 0);
                } else {

                }
                break;
            case 34183:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 10, 0);
                } else {

                }
                break;
            case 34182:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 28, 0);
                } else {

                }
                break;
            case 34189:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 1, 1);
                } else {

                }
                break;
            case 34188:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 5, 1);
                } else {

                }
                break;
            case 34187:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 10, 1);
                } else {

                }
                break;
            case 34186:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 28, 1);
                } else {

                }
                break;
            case 34193:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 1, 2);
                } else {

                }
                break;
            case 34192:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 5, 2);
                } else {

                }
                break;
            case 34191:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 10, 2);
                } else {

                }
                break;
            case 34190:
                if (c.playerFletch) {
                    Fletching.attemptData(c, 28, 2);
                } else {

                }
                break;


            case 4169:
                // god spell charge
                c.usingMagic = true;
                if (!c.getCombat().checkMagicReqs(48)) {
                    break;
                }

                if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
                    c.sendMessage("You still feel the charge in your body!");
                    break;
                }
                c.godSpellDelay = System.currentTimeMillis();
                c.sendMessage("You feel charged with a magical power!");
                c.gfx100(c.MAGIC_SPELLS[48][3]);
                c.startAnimation(c.MAGIC_SPELLS[48][2]);
                c.usingMagic = false;
                break;




            case 152:
                c.isRunning2 = !c.isRunning2;
                int frame = c.isRunning2 == true ? 1 : 0;
                c.getPA().sendFrame36(173, frame);
                break;

            case 9154:

                c.logout();
                break;

            case 28165:
                // MAGIC CHANING
                if (c.inWild()) return;
                for (int j = 0; j < c.playerEquipment.length; j++) {
                    if (c.playerEquipment[j] > 0) {
                        c.sendMessage("Please remove all your equipment before using this command.");
                        return;
                    }
                }
                c.getDH().sendOption5("Ancient Magic", "Normal Magic", "Lunar Magic", "PLEASE REMOVE ALL ARMOR", "FOR THIS TO TAKE EFFECT");
                c.teleAction = 44;
                break;



                /*case 28165:
				if (c.inWild())
					return;
				
				
			c.setSidebarInterface(6, 1151);
					c.playerMagicBook = 0;
					c.autocastId = -1;
					c.getPA().resetAutocast();
				
			break;

			
			case 28168:
			if (c.inWild())
					return;
				
			c.setSidebarInterface(6, 29999);
					c.playerMagicBook = 2;
					c.autocastId = -1;
					c.getPA().resetAutocast();
			
				break;*/
            case 82016:
                c.takeAsNote = !c.takeAsNote;
                break;
            case 21010:
                c.takeAsNote = true;
                break;

            case 21011:
                c.takeAsNote = false;
                break;

            case 28215:
                c.sendMessage("Max hit = " + Integer.toString(c.getCombat().calculateMeleeMaxHit()));
                c.sendMessage("This does not apply to spcials");
                break;





                /*case 113240:
                if (c.inWild()) return;
                c.getDH().sendOption5("Reset Defence (take all armour off)", "Reset Attack (take all armour off)", "Reset Prayer(take armour off)", "Reset Strength(take armour off)", "Reset Range(take armour off)");
                c.dialogueAction = 25;

                break;*/


            case 117048:
            case 30000:
            case 4171:
            case 50056:
                c.getPA().startTeleport(Config.HOME_X, Config.HOME_Y, 0, "modern");
                break;
            case 72038:
                c.getDH().sendOption5("Home", "Lumbridge", "Varrock", "Falador", "Draynor");
                c.teleAction = 9;

                break;





            case 28169:
                if (c.inWild()) return;
                c.getDH().sendOption2("Empty Inventory", "Nevermind");
                c.dialogueAction = 37;
                /*if (c.inWild())
					return;
c.getPA().removeAllItems();*/
                break;


            case 4143:
            case 50245:
            case 117123:
                if (c.inWild()) return;
                c.getDH().sendDialogues(10015, -1);
                break;

            case 50253:
            case 4146:
            case 117131:
                if (c.inWild()) return;
                c.getDH().sendDialogues(10010, -1);
                break;


            case 51005:
            case 4150:
            case 117154:
                if (c.inWild()) return;
                c.getDH().sendDialogues(10020, -1);
                break;

            case 51013:
            case 6004:
            case 117126:
                if (c.inWild()) return;
                c.getDH().sendDialogues(10000, -1);
                break;



                //case 6005:
                //c.getDH().sendOption5("Option 16", "Option 2", "Option 3", "Option 4", "Option 5");
                //c.teleAction = 6;
                //break; 


            case 51031:
            case 29031:
                //c.getDH().sendOption5("Option 17", "Option 2", "Option 3", "Option 4", "Option 5");
                //c.teleAction = 7;
                break;

                //case 72038:
            case 51039:
                //c.getDH().sendOption5("Option 18", "Option 2", "Option 3", "Option 4", "Option 5");
                //c.teleAction = 8;
                break;


                //Attack
            case 9125:
                //Accurate
            case 22230:
                //punch
            case 48010:
                //flick (whip)
            case 14218:
                //pound (mace)
            case 33020:
                //jab (halberd)
            case 21200:
                //spike (pickaxe)
            case 6168:
                //chop (axe)
            case 8234:
                //stab (dagger)
            case 17102:
                //accurate (darts)
            case 6236:
                //accurate (long bow)
            case 1080:
                //bash (staff)
            case 6221:
                // range accurate
            case 30088:
                //claws (chop)
            case 1177:
                //hammer (pound)
                c.fightMode = 0;
                if (c.autocasting) c.getPA().resetAutocast();
                break;

                //Defence
            case 9126:
                //Defensive
            case 22228:
                //block (unarmed)
            case 48008:
                //deflect (whip)
            case 1175:
                //block (hammer)
            case 21201:
                //block (pickaxe)
            case 14219:
                //block (mace)
            case 1078:
                //focus - block (staff)
            case 33018:
                //fend (hally)
            case 6169:
                //block (axe)
            case 8235:
                //block (dagger)
            case 18078:
                //block (spear)
            case 30089:
                //block (claws)
                c.fightMode = 1;
                if (c.autocasting) c.getPA().resetAutocast();
                break;
                //All
            case 9127:
                // Controlled
            case 14220:
                //Spike (mace)
            case 6234:
                //longrange (long bow)
            case 6219:
                //longrange
            case 18077:
                //lunge (spear)
            case 18080:
                //swipe (spear)
            case 18079:
                //pound (spear)
            case 17100:
                //longrange (darts)
                c.fightMode = 3;
                if (c.autocasting) c.getPA().resetAutocast();
                break;
                //Strength
            case 9128:
                //Aggressive
            case 14221:
                //Pummel(mace)
            case 33019:
                //Swipe(Halberd)
            case 21203:
                //impale (pickaxe)
            case 21202:
                //smash (pickaxe)
            case 6171:
                //hack (axe)
            case 6170:
                //smash (axe)
            case 6220:
                // range rapid
            case 8236:
                //slash (dagger)
            case 8237:
                //lunge (dagger)
            case 30090:
                //claws (lunge)
            case 30091:
                //claws (Slash)
            case 1176:
                //stat hammer
            case 22229:
                //block (unarmed)
            case 1079:
                //pound (staff)
            case 6235:
                //rapid (long bow)
            case 17101:
                //repid (darts)
                c.fightMode = 2;
                if (c.autocasting) c.getPA().resetAutocast();
                break;



                /**Prayers**/
            case 97168:
                // thick skin
                c.getCombat().activatePrayer(0);
                break;
            case 97170:
                // burst of str
                c.getCombat().activatePrayer(1);
                break;
            case 97172:
                // charity of thought
                c.getCombat().activatePrayer(2);
                break;
            case 97174:
                // range
                c.getCombat().activatePrayer(3);
                break;
            case 97176:
                // mage
                c.getCombat().activatePrayer(4);
                break;
            case 97178:
                // rockskin
                c.getCombat().activatePrayer(5);
                break;
            case 97180:
                // super human
                c.getCombat().activatePrayer(6);
                break;
            case 97182:
                // improved reflexes
                c.getCombat().activatePrayer(7);
                break;
            case 97184:
                // hawk eye
                c.getCombat().activatePrayer(8);
                break;
            case 97186:
                c.getCombat().activatePrayer(9);
                break;
            case 97188:
                // protect Item
                c.getCombat().activatePrayer(10);
                break;
            case 97190:
                // 26 range
                c.getCombat().activatePrayer(11);
                break;
            case 97192:
                // 27 mage
                c.getCombat().activatePrayer(12);
                break;
            case 97194:
                // steel skin
                c.getCombat().activatePrayer(13);
                break;
            case 97196:
                // ultimate str
                c.getCombat().activatePrayer(14);
                break;
            case 97198:
                // incredible reflex
                c.getCombat().activatePrayer(15);
                break;
            case 97200:
                // protect from magic
                c.getCombat().activatePrayer(16);
                break;
            case 97202:
                // protect from range
                c.getCombat().activatePrayer(17);
                break;
            case 97204:
                // protect from melee
                c.getCombat().activatePrayer(18);
                break;
            case 97206:
                // 44 range
                c.getCombat().activatePrayer(19);
                break;
            case 97208:
                // 45 mystic
                c.getCombat().activatePrayer(20);
                break;
            case 97210:
                // retrui
                c.getCombat().activatePrayer(21);
                break;
            case 97212:
                // redem
                c.getCombat().activatePrayer(22);
                break;
            case 97214:
                // smite
                c.getCombat().activatePrayer(23);
                break;
            case 97216:
                // chiv
                c.getCombat().activatePrayer(24);
                break;
            case 97218:
                // piety
                c.getCombat().activatePrayer(25);
                break;

            case 13092:
                if (System.currentTimeMillis() - c.lastButton < 400) {

                    c.lastButton = System.currentTimeMillis();

                    break;

                } else {

                    c.lastButton = System.currentTimeMillis();

                }
                Client ot = (Client) Server.playerHandler.players[c.tradeWith];
                if (ot == null) {
                    c.getTradeAndDuel().declineTrade();
                    c.sendMessage("Trade declined as the other player has disconnected.");
                    break;
                }
                c.getPA().sendFrame126("Waiting for other player...", 3431);
                ot.getPA().sendFrame126("Other player has accepted", 3431);
                c.goodTrade = true;
                ot.goodTrade = true;

                for (GameItem item: c.getTradeAndDuel().offeredItems) {
                    if (item.id > 0) {
                        if (ot.getItems().freeSlots() < c.getTradeAndDuel().offeredItems.size()) {
                            c.sendMessage(ot.playerName + " only has " + ot.getItems().freeSlots() + " free slots, please remove " + (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
                            ot.sendMessage(c.playerName + " has to remove " + (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items or you could offer them " + (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
                            c.goodTrade = false;
                            ot.goodTrade = false;
                            c.getPA().sendFrame126("Not enough inventory space...", 3431);
                            ot.getPA().sendFrame126("Not enough inventory space...", 3431);
                            break;
                        } else {
                            c.getPA().sendFrame126("Waiting for other player...", 3431);
                            ot.getPA().sendFrame126("Other player has accepted", 3431);
                            c.goodTrade = true;
                            ot.goodTrade = true;
                        }
                    }
                }
                if (c.inTrade && !c.tradeConfirmed && ot.goodTrade && c.goodTrade) {
                    c.tradeConfirmed = true;
                    if (ot.tradeConfirmed) {
                        c.getTradeAndDuel().confirmScreen();
                        ot.getTradeAndDuel().confirmScreen();
                        break;
                    }

                }


                break;

            case 13218:
                if (System.currentTimeMillis() - c.lastButton < 400) {
                    c.lastButton = System.currentTimeMillis();
                    break;
                } else {
                    c.lastButton = System.currentTimeMillis();
                }
                c.tradeAccepted = true;
                Client ot1 = (Client) Server.playerHandler.players[c.tradeWith];
                if (ot1 == null) {
                    c.getTradeAndDuel().declineTrade();
                    c.sendMessage("Trade declined as the other player has disconnected.");
                    break;
                }

                if (c.inTrade && c.tradeConfirmed && ot1.tradeConfirmed && !c.tradeConfirmed2) {
                    c.tradeConfirmed2 = true;
                    if (ot1.tradeConfirmed2) {
                        c.acceptedTrade = true;
                        ot1.acceptedTrade = true;
                        c.getTradeAndDuel().giveItems();
                        ot1.getTradeAndDuel().giveItems();
                        break;
                    }
                    ot1.getPA().sendFrame126("Other player has accepted.", 3535);
                    c.getPA().sendFrame126("Waiting for other player...", 3535);
                }
                c.sendMessage("Trade accepted.");
                ot1.sendMessage("Trade accepted.");
                break;


                /* Rules Interface Buttons */
            case 125011:
                //Click agree
                if (!c.ruleAgreeButton) {
                    c.ruleAgreeButton = true;
                    c.getPA().sendFrame36(701, 1);
                } else {
                    c.ruleAgreeButton = false;
                    c.getPA().sendFrame36(701, 0);
                }
                break;
            case 125003:
                //Accept
                if (c.ruleAgreeButton) {
                    c.getPA().showInterface(3559);
                    c.newPlayer = false;
                } else if (!c.ruleAgreeButton) {
                    c.sendMessage("You need to click on you agree before you can continue on.");
                }
                break;
            case 125006:
                //Decline
                c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
                break;
                /* End Rules Interface Buttons */
                /* Player Options */
            case 74176:
                if (!c.mouseButton) {
                    c.mouseButton = true;
                    c.getPA().sendFrame36(500, 1);
                    c.getPA().sendFrame36(170, 1);
                } else if (c.mouseButton) {
                    c.mouseButton = false;
                    c.getPA().sendFrame36(500, 0);
                    c.getPA().sendFrame36(170, 0);
                }
                break;
            case 74184:
                if (!c.splitChat) {
                    c.splitChat = true;
                    c.getPA().sendFrame36(502, 1);
                    c.getPA().sendFrame36(287, 1);
                } else {
                    c.splitChat = false;
                    c.getPA().sendFrame36(502, 0);
                    c.getPA().sendFrame36(287, 0);
                }
                break;
            case 74180:
                if (!c.chatEffects) {
                    c.chatEffects = true;
                    c.getPA().sendFrame36(501, 1);
                    c.getPA().sendFrame36(171, 0);
                } else {
                    c.chatEffects = false;
                    c.getPA().sendFrame36(501, 0);
                    c.getPA().sendFrame36(171, 1);
                }
                break;
            case 74188:
                if (!c.acceptAid) {
                    c.acceptAid = true;
                    c.getPA().sendFrame36(503, 1);
                    c.getPA().sendFrame36(427, 1);
                } else {
                    c.acceptAid = false;
                    c.getPA().sendFrame36(503, 0);
                    c.getPA().sendFrame36(427, 0);
                }
                break;
            case 74192:
                if (!c.isRunning2) {
                    c.isRunning2 = true;
                    c.getPA().sendFrame36(504, 1);
                    c.getPA().sendFrame36(173, 1);
                } else {
                    c.isRunning2 = false;
                    c.getPA().sendFrame36(504, 0);
                    c.getPA().sendFrame36(173, 0);
                }
                break;
            case 74201:
                //brightness1
                c.getPA().sendFrame36(505, 1);
                c.getPA().sendFrame36(506, 0);
                c.getPA().sendFrame36(507, 0);
                c.getPA().sendFrame36(508, 0);
                c.getPA().sendFrame36(166, 1);
                break;
            case 74203:
                //brightness2
                c.getPA().sendFrame36(505, 0);
                c.getPA().sendFrame36(506, 1);
                c.getPA().sendFrame36(507, 0);
                c.getPA().sendFrame36(508, 0);
                c.getPA().sendFrame36(166, 2);
                break;

            case 74204:
                //brightness3
                c.getPA().sendFrame36(505, 0);
                c.getPA().sendFrame36(506, 0);
                c.getPA().sendFrame36(507, 1);
                c.getPA().sendFrame36(508, 0);
                c.getPA().sendFrame36(166, 3);
                break;

            case 74205:
                //brightness4
                c.getPA().sendFrame36(505, 0);
                c.getPA().sendFrame36(506, 0);
                c.getPA().sendFrame36(507, 0);
                c.getPA().sendFrame36(508, 1);
                c.getPA().sendFrame36(166, 4);
                break;
            case 74206:
                //area1
                c.getPA().sendFrame36(509, 1);
                c.getPA().sendFrame36(510, 0);
                c.getPA().sendFrame36(511, 0);
                c.getPA().sendFrame36(512, 0);
                break;
            case 74207:
                //area2
                c.getPA().sendFrame36(509, 0);
                c.getPA().sendFrame36(510, 1);
                c.getPA().sendFrame36(511, 0);
                c.getPA().sendFrame36(512, 0);
                break;
            case 74208:
                //area3
                c.getPA().sendFrame36(509, 0);
                c.getPA().sendFrame36(510, 0);
                c.getPA().sendFrame36(511, 1);
                c.getPA().sendFrame36(512, 0);
                break;
            case 74209:
                //area4
                c.getPA().sendFrame36(509, 0);
                c.getPA().sendFrame36(510, 0);
                c.getPA().sendFrame36(511, 0);
                c.getPA().sendFrame36(512, 1);
                break;
            case 168:
                c.startAnimation(855);
                break;
            case 169:
                c.startAnimation(856);
                break;
            case 162:
                c.startAnimation(857);
                break;
            case 164:
                c.startAnimation(858);
                break;
            case 165:
                c.startAnimation(859);
                break;
            case 161:
                c.startAnimation(860);
                break;
            case 170:
                c.startAnimation(861);
                break;
            case 171:
                c.startAnimation(862);
                break;
            case 163:
                c.startAnimation(863);
                break;
            case 167:
                c.startAnimation(864);
                break;
            case 172:
                c.startAnimation(865);
                break;
            case 166:
                c.startAnimation(866);
                break;
            case 52050:
                c.startAnimation(2105);
                break;
            case 52051:
                c.startAnimation(2106);
                break;
            case 52052:
                c.startAnimation(2107);
                break;
            case 52053:
                c.startAnimation(2108);
                break;
            case 52054:
                c.startAnimation(2109);
                break;
            case 52055:
                c.startAnimation(2110);
                break;
            case 52056:
                c.startAnimation(2111);
                break;
            case 52057:
                c.startAnimation(2112);
                break;
            case 52058:
                c.startAnimation(2113);
                break;
            case 2155:
                c.startAnimation(0x46B);
                break;
            case 25103:
                c.startAnimation(0x46A);
                break;
            case 25106:
                c.startAnimation(0x469);
                break;
            case 2154:
                c.startAnimation(0x468);
                break;
            case 52071:
                c.startAnimation(0x84F);
                break;
            case 52072:
                c.startAnimation(0x850);
                break;
            case 28178:
                c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
                break;

                /* END OF EMOTES */

            case 118098:
                if (c.playerLevel[1] < 40) {
                    c.sendMessage("You need a defence level of 40 or more to cast this.");
                    return;
                }
                if (c.playerLevel[6] < 94) {
                    c.sendMessage("You need a magic level of 94 or more to cast this.");
                    return;
                } else {
                    c.getPA().vengMe();
                }
                //SkillMenu.openInterface(c, -1)
                //SkillMenu.openInterface(c,0);
                break;


            case 47130:
                c.forcedText = "[Quick Chat] My Slayer level is " + c.playerLevel[18] + ", and I must slay another " + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask) + "";
                c.forcedChatUpdateRequired = true;
                c.updateRequired = true;
                break;

            case 24017:
                c.getPA().resetAutocast();
                //c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
                c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
                //c.setSidebarInterface(0, 328);
                //c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 : c.playerMagicBook == 1 ? 12855 : 1151);
                break;
        }
        if (c.isAutoButton(actionButtonId)) c.assignAutocast(actionButtonId);
    }

}