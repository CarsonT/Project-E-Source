package server.model.players;

import server.Config;
import server.Server;
import server.model.objects.Object;
import server.util.Misc;
import server.util.ScriptManager;
import server.model.minigames.*;
import server.model.minigames.CastleWarObjects;
import server.model.players.combat.CombatAssistant;
import server.model.minigames.FreeForAll;
import server.model.minigames.BountyHunter;
import server.model.content.DwarfMultiCannon;
import server.model.skills.runecrafting.Runecrafting;

public class ActionHandler {

    private Client c;

    public ActionHandler(Client Client) {
        this.c = Client;
    }

    public void resetPlayerAttack() {
        c.inCombat = false;
        c.usingMagic = false;
        c.npcIndex = 0;
        c.faceUpdate(0);
        c.playerIndex = 0;
        c.getPA().resetFollow();
    }

    public void firstClickObject(int objectType, int obX, int obY) {
        c.clickObjectType = 0;
        switch (objectType) {
		case 28120:
		case 28119:
		case 28121:
			BountyHunter.enterBH(c, c.objectId);
		break;
		case 28122:	
			BountyHunter.leaveBH(c);
		break;
				
			case 23271:
				c.wildyditch();
			break;		
            case 6:
                DwarfMultiCannon.shootCannon();
                break;
            case 162:
                if (c.playerRights == 2) {
                    c.sendMessage("Your rank is too high to participate in the lottery!");
                } else {
                    c.getDH().sendDialogues(185, -1);
                }
                break;
            case 575:
                c.getHighscores().openHighscores();
                break;
            case 574:
                c.getDH().sendDialogues(9292, 0);
                break;

                /** Runecrafting **/
            case 2478:
                Runecrafting.craftRunesOnAltar(c, 1, 5, 556, 30, 45, 60);
                break;
            case 2480:
                Runecrafting.craftRunesOnAltar(c, 5, 6, 555, 30, 45, 60);
                break;
            case 2481:
                Runecrafting.craftRunesOnAltar(c, 9, 7, 557, 45, 55, 65);
                break;
            case 2482:
                Runecrafting.craftRunesOnAltar(c, 14, 7, 554, 50, 60, 70);
                break;
            case 2483:
                Runecrafting.craftRunesOnAltar(c, 20, 8, 559, 55, 65, 75);
                break;
            case 2487:
                Runecrafting.craftRunesOnAltar(c, 35, 9, 562, 60, 70, 80);
                break;
            case 2486:
                Runecrafting.craftRunesOnAltar(c, 44, 9, 561, 60, 74, 91);
                break;
            case 2485:
                Runecrafting.craftRunesOnAltar(c, 54, 10, 563, 65, 79, 93);
                break;
            case 2488:
                Runecrafting.craftRunesOnAltar(c, 65, 10, 560, 72, 84, 96);
                break;
            case 2490:
                Runecrafting.craftRunesOnAltar(c, 77, 11, 565, 89, 94, 99);
                break;

            case 6282:
                if (c.memberStatus >= 1) {
                    c.getPA().movePlayer(2729, 3350, 0);
                     c.getPA().sendStatement("Welcome To The Memberzone @blu@" + c.playerName + "@bla@!");
                } else {
                    c.getPA().sendStatement("This is a Member only Feature.");
					c.getPA().showInterface(25900);
                }
                break;
            case 2391:
                if (c.memberStatus == 1) {
                    c.getPA().movePlayer(2728, 3350, 0);
                } else if (c.memberStatus == 2) {
                    c.getPA().movePlayer(2728, 3350, 0);
                } else if (c.memberStatus == 3) {
                    c.getPA().movePlayer(2728, 3350, 0);
                } else {
					c.getPA().sendStatement("This is a Member only Feature.");
					c.getPA().showInterface(25900);
                }
                break;
            case 2392:
                if (c.memberStatus == 1) {
                    c.getPA().movePlayer(2729, 3350, 0);
                } else if (c.memberStatus == 2) {
                    c.getPA().movePlayer(2729, 3350, 0);
                } else if (c.memberStatus == 3) {
                    c.getPA().movePlayer(2729, 3350, 0);
                } else {
					c.getPA().sendStatement("This is a Member only Feature.");
					c.getPA().showInterface(25900);
                }
                break;
            case 1725:
                if (c.memberStatus == 1) {
                    if (c.absX == 2732 && c.absY == 3376) {
                        c.getPA().movePlayer(2732, 3380, 1);
                    } else if (c.absX == 2733 && c.absY == 3376) {
                        c.getPA().movePlayer(2732, 3380, 1);
                    }
                } else if (c.memberStatus == 2) {
                    if (c.absX == 2732 && c.absY == 3376) {
                        c.getPA().movePlayer(2732, 3380, 1);
                    } else if (c.absX == 2733 && c.absY == 3376) {
                        c.getPA().movePlayer(2732, 3380, 1);
                    }
                } else if (c.memberStatus == 3) {
                    if (c.absX == 2732 && c.absY == 3376) {
                        c.getPA().movePlayer(2732, 3380, 1);
                    } else if (c.absX == 2733 && c.absY == 3376) {
                        c.getPA().movePlayer(2732, 3380, 1);
                    }
                } else {
					c.getPA().sendStatement("This is a Member only Feature.");
					c.getPA().showInterface(25900);

                }
                break;
            case 1726:
                if (c.memberStatus == 1) {
                    if (c.absX == 2732 && c.absY == 3380) {
                        c.objectDistance = 2;
                        c.getPA().movePlayer(2732, 3376, 0);
                    } else if (c.absX == 2733 && c.absY == 3380) {
                        c.objectDistance = 2;
                        c.getPA().movePlayer(2733, 3376, 0);
                    }
                } else if (c.memberStatus == 2) {
                    if (c.absX == 2732 && c.absY == 3380) {
                        c.objectDistance = 2;
                        c.getPA().movePlayer(2732, 3376, 0);
                    } else if (c.absX == 2733 && c.absY == 3380) {
                        c.objectDistance = 2;
                        c.getPA().movePlayer(2733, 3376, 0);
                    }
                } else if (c.memberStatus == 3) {
                    if (c.absX == 2732 && c.absY == 3380) {
                        //c.objectDistance = 2;
                        c.getPA().movePlayer(2732, 3376, 0);
                    } else if (c.absX == 2733 && c.absY == 3380) {
                        //c.objectDistance = 2;

                        c.getPA().movePlayer(2733, 3376, 0);
                    }
                } else {
					c.getPA().sendStatement("This is a Member only Feature.");
					c.getPA().showInterface(25900);
                }
                break;
            case 3515:
                if (c.playerLevel[0] >= 98 && c.playerLevel[1] >= 98 && c.playerLevel[2] >= 98 && c.playerLevel[3] >= 98 && c.playerLevel[4] >= 98 && c.playerLevel[5] >= 98 && c.playerLevel[6] >= 98 && c.playerLevel[7] >= 98 && c.playerLevel[8] >= 98 && c.playerLevel[9] >= 98 && c.playerLevel[10] >= 98 && c.playerLevel[11] >= 98 && c.playerLevel[12] >= 98 && c.playerLevel[13] >= 98 && c.playerLevel[14] >= 98 && c.playerLevel[15] >= 98 && c.playerLevel[16] >= 98 && c.playerLevel[17] >= 98 && c.playerLevel[18] >= 98 && c.playerLevel[19] >= 98 && c.playerLevel[20] >= 99 && c.playerLevel[21] >= 98 && c.playerLevel[22] >= 98) {
                    c.getDH().sendDialogues(3515, 1);
                } else {
                    c.sendMessage("Come back when you have achieved maxed stats!");
                }
                break;
            case 412:
                if (c.PRAYFUCK == false) {
                    c.startAnimation(645);
                    c.getCombat().resetPrayers();
                    c.PRAYFUCK = true;
                    c.setSidebarInterface(5, 22500);
                    c.sendMessage("You switch to Curse Prayers.");
                } else {
                    c.startAnimation(645);
                    c.getCombat().resetPrayers();
                    c.PRAYFUCK = false;
                    c.setSidebarInterface(5, 5608);
                    c.sendMessage("You switch to Normal Prayers.");
                }
                break;



                /** Portal Buttonns **/

            case 13617:
                //Skilling
                c.getDH().sendDialogues(10000, -1);
                break;

            case 13618:
                //Trainning
                c.getDH().sendDialogues(10005, -1);
                break;

            case 13619:
                //Bosses
                c.getDH().sendDialogues(10012, -1);
                break;

            case 13620:
                //Minigame
                c.getDH().sendDialogues(10015, -1);
                break;

            case 13623:
                //Pk
                c.getDH().sendDialogues(10020, -1);
                break;

                /** End Portal Buttonns **/

            case 9398:
                //deposit
                c.getPA().sendFrame126("The Bank of Project-Equinox - Deposit Box", 7421);
                c.getPA().sendFrame248(4465, 197);
                c.getItems().resetItems(7423);
                c.isBanking = true;
                break;

            case 172:
                if (!c.getItems().playerHasItem(989, 1)) {
                    c.sendMessage("You need a crystal key to open this chest--To get keys kill monsters");
                } else if (c.getItems().playerHasItem(989, 1)) {
                    c.getItems().addItem(c.getPA().randomCrap(), 1);
                    c.getItems().deleteItem(989, 1);
                    if (Misc.random(20) == 1) c.getItems().addItem(c.getPA().randomClue1(), 1);
                } else if (c.getItems().playerHasItem(989, 1)) {
                    c.sendMessage("You need at least 2 inventory slot opened.");
                }
                break;

            case 2471:
                c.getDH().sendDialogues(43, -1);
                //c.getPA().movePlayer(2722, 5105, 0);
                break;
            case 2470:
                c.getPA().movePlayer(3073, 3504, 0);
                break;
            case 26303:
                c.getPA().movePlayer(3056, 9562, 0);
                break;

            case 13179:
                if (c.playerMagicBook == 0) {
                    if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 15050 || c.playerEquipment[c.playerWeapon] == 15040) {
                        c.setSidebarInterface(0, 328);
                    }
                    c.startAnimation(645);
                    c.playerMagicBook = 2;
                    c.setSidebarInterface(6, 29999);
                    c.sendMessage("Your mind becomes stirred with thoughs of dreams.");
                    c.getPA().resetAutocast();
                } else {
                    if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 15050 || c.playerEquipment[c.playerWeapon] == 15040) {
                        c.setSidebarInterface(0, 328);
                    }
                    c.setSidebarInterface(6, 1151);
                    c.playerMagicBook = 0;
                    c.startAnimation(645);
                    c.sendMessage("You feel a drain on your memory.");
                    c.autocastId = -1;
                    c.getPA().resetAutocast();
                }
                break;
            case 10595:

                c.getPA().movePlayer(3056, 9562, 0);
                c.sendMessage("You have " + c.Frost + " points!");


                c.sendMessage("You must get an abbysal book/frost book from the slayer shop to enter the frost cave.");

                break;
            case 2472:
                c.getPA().movePlayer(3056, 9555, 4);
                c.sendMessage("Slay Frost Dragons for points to buy chaotic items!");
                break;
            case 12266:
                if (!c.getItems().playerHasItem(1546, 1)) {
                    c.sendMessage("You need a blue key to open enter this trap door");
                } else if (c.getItems().playerHasItem(1546, 1)) {
                    c.getPA().movePlayer(2806, 9200, 0);
                }
                break;
            case 12617:
                if (!c.getItems().playerHasItem(1546, 1)) {
                    c.sendMessage("You need a purple key to open enter this cave");
                } else if (c.getItems().playerHasItem(1547, 1)) {
                    //c.getPA().movePlayer(2806, 9200, 0);
                }
                break;
            case 6452:
                if (c.absX == 3304 && c.absY == 9376) {
                    c.getPA().movePlayer(3305, 9376, 4);
                    c.sendMessage("Prepare for the strongest monster in the game!");
                    c.sendMessage("Note: It has 3 waves on it's hp bar!");
                } else {
                    c.autoRet = 0;
                    c.getCombat().resetPlayerAttack();
                    c.getPA().movePlayer(3304, 9376, 0);
                }
                break;
            case 26428:
                if (c.Zammy < 15 && c.absX == 2925 && c.absY == 5332) {
                    c.sendMessage("You need atleast 15 Zamorak KC to enter here!");
                    return;
                }
                if (c.absX == 2925 && c.absY == 5332) {
                    c.getPA().movePlayer(2925, 5331, 6);
                    c.Zammy -= 15;
                    c.sendMessage("A magical force reseted your Zamorak kill count!");
                }
                if (c.absX == 2925 && c.absY == 5331) {
                    c.getPA().movePlayer(2925, 5332, 2);
                    c.autoRet = 0;
                    c.getCombat().resetPlayerAttack();
                }
                break;
            case 26425:
                if (c.Band < 15 && c.absX == 2863 && c.absY == 5354) {
                    c.sendMessage("You need atleast 15 Bandos KC to enter here!");
                    return;
                }
                if (c.absX == 2863 && c.absY == 5354) {
                    c.getPA().movePlayer(2864, 5354, 6);
                    c.Band -= 15;
                    c.sendMessage("A magical force reseted your Bandos kill count!");
                }
                if (c.absX == 2864 && c.absY == 5354) {
                    c.getPA().movePlayer(2863, 5354, 2);
                    c.autoRet = 0;
                    c.getCombat().resetPlayerAttack();
                }
                break;

            case 556:
                if (c.Arma < 15) {
                    c.sendMessage("You need atleast 15 Armadyl KC to enter here!");
                    return;
                } else {
                    c.getPA().movePlayer(2897, 3619, 4);
                    c.Arma -= 15;
                    c.sendMessage("A magical force reseted your Armadyl kill count!");
                }
                break;
            case 26426:
                if (c.Arma < 15 && c.absX == 2839 && c.absY == 5295) {
                    c.sendMessage("You need atleast 15 Armadyl KC to enter here!");
                    return;
                }
                if (c.absX == 2839 && c.absY == 5295) {
                    c.getPA().movePlayer(2839, 5296, 6);
                    c.Arma -= 15;
                    c.sendMessage("A magical force reseted your Armadyl kill count!");
                }
                if (c.absX == 2839 && c.absY == 5296) {
                    c.getPA().movePlayer(2839, 5295, 2);
                    c.autoRet = 0;
                    c.getCombat().resetPlayerAttack();
                }
                break;
            case 26427:
                if (c.Sara < 15 && c.absX == 2908 && c.absY == 5265) {
                    c.sendMessage("You need atleast 15 Saradomin KC to enter here!");
                    return;
                }
                if (c.absX == 2908 && c.absY == 5265) {
                    c.Sara -= 15;
                    c.sendMessage("A magical force reseted your Saradomin kill count!");
                    c.getPA().movePlayer(2907, 5265, 4);
                }
                if (c.absX == 2907 && c.absY == 5265) {
                    c.getPA().movePlayer(2908, 5265, 0);
                    c.autoRet = 0;
                    c.getCombat().resetPlayerAttack();
                }
                break;
            case 6451:
                if (c.absX == 3304 && c.absY == 9375) {
                    c.getPA().movePlayer(3305, 9375, 4);
                    c.sendMessage("Prepare for the strongest monster in the game!");
                    c.sendMessage("Note: It has 3 waves on it's hp bar!");
                } else {
                    c.autoRet = 0;
                    c.getCombat().resetPlayerAttack();
                    c.getPA().movePlayer(3304, 9375, 0);
                }
                break;

            case 26288:
            case 26287:
            case 26286:
            case 26289:

                if (c.gwdelay > 1) {
                    c.sendMessage("You can only do this once every 5 minute!");
                    return;
                }
                if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
                    c.startAnimation(645);
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.sendMessage("You recharge your prayer points.");
                    c.getPA().refreshSkill(5);
                    c.gwdelay = 600;
                } else {
                    c.sendMessage("You already have full prayer points.");
                }

                break;
            case 1558:
                c.getPA().movePlayer(3092, 3485, 0);
                break;
                /*case 1568:
			if (!c.getItems().playerHasItem(1586,1)) {
			c.sendMessage("You need a miscalenous key to enter the cave");
			}
			 else if(c.getItems().playerHasItem(1586,1)) {
				c.getPA().movePlayer(2033, 4636, 0);
				c.getItems().deleteItem(1586,1);
				}
			c.getPA().movePlayer(3104, 9909, 0);
			break;*/
            case 2:
                /*if (c.teleBlockDelay > 0) {
		
		
		return;
		}*/
                if (c.playerLevel[13] < 82) {
                    c.sendMessage("You need 82 smithing to enter this cave OH NOES!");
                    return;
                }
                if (c.playerLevel[10] < 92) {
                    c.sendMessage("You need 92 fishing to enter this cave OH NOES!");
                    return;
                }
                c.getPA().movePlayer(2399, 4681, 0);
                c.sendMessage("Wow i can't believe you made it, now move east for the DEMONS!");

                break;

            case 4411:
            case 4415:
            case 4417:
            case 4418:
            case 4419:
            case 4420:
            case 4469:
            case 4470:
            case 4911:
            case 4912:
            case 1747:
            case 1757:
            case 4437:
            case 6281:
            case 6280:
            case 4472:
            case 4471:
            case 4406:
            case 4407:
            case 4458:
            case 4902:
            case 4903:
            case 4900:
            case 4901:
            case 4461:
            case 4463:
            case 4464:
            case 4377:
            case 4378:
                CastleWarObjects.handleObject(c, objectType, obX, obY);
            case 1568:
                if (obX == 3097 && obY == 3468) {
                    c.getPA().movePlayer(3097, 9868, 0);
                } else {
                    CastleWarObjects.handleObject(c, obY, obY, obY);
                }
                break;


            case 2466:
                if (c.teleBlockDelay > 0) {

                    if (c.underAttackBy > 0) {
                        return;
                    }
                }


                //c.getPA().spellTeleport(3073, 3504, 0);
                c.getPA().movePlayer(3073, 3504, 0);
                return;

            case 2561:
                c.getThieving().stealFromStall(995, 120000, 10, 1);
                break;
            case 2560:
                c.getThieving().stealFromStall(995, 120000, 40, 40);
                break;

            case 4800:
                if (c.objectX == 2777 && c.objectY == 9195) {
                    if (c.absX < 2777) {
                        c.getPA().walkTo(1, 0);
                    } else {
                        c.getPA().walkTo(-1, 0);
                    }
                }

                break;

            case 1516:
                if (c.objectX == 2737 && c.objectY == 3477) {
                    if (c.absY < 2737) {
                        c.getPA().walkTo(0, 1);
                    } else {
                        c.getPA().walkTo(0, -1);
                    }
                }

                break;
            case 4801:
                //dungeon to apes needs donor
                if (c.memberStatus < 1) {
                    c.sendMessage("You need to be a donator to use this");

                    return;
                }
                c.getPA().movePlayer(2806, 2785, 0);
                break;
            case 4879:
                if (c.memberStatus < 1) {
                    c.sendMessage("You need to be a donator to use this");

                    return;
                }
                c.getPA().movePlayer(2806, 9200, 0);
                break;
            case 7049:
            case 7050:
                c.getPA().movePlayer(3078, 3259, 0);
                break;
            case 4881:
                if (c.memberStatus < 1) {
                    c.sendMessage("You need to be a donator to use this");

                    return;
                }
                c.getPA().movePlayer(2806, 2785, 0);
                break;
            case 2564:
                c.getThieving().stealFromStall(995, 160000, 60, 60);
                break;
            case 2562:
                c.getThieving().stealFromStall(995, 300000, 80, 80);
                break;
            case 9299:
                if (c.absY <= 3190) {
                    c.getPA().walkTo(0, 1);
                } else {
                    c.getPA().walkTo(0, -1);
                }
                break;
            case 1528:
                if (c.objectX == 3182 && c.objectY == 2984) {
                    if (c.absX < 3183) {
                        c.getPA().walkTo(1, 0);
                    } else {
                        c.getPA().walkTo(-1, 0);
                    }
                }
                if (c.objectX == 3172 && c.objectY == 2977) {
                    if (c.absY >= 2977) {
                        c.getPA().walkTo(0, -1);
                    } else if (c.absY < 2977) {
                        c.getPA().walkTo(0, 1);
                    }
                }
                break;
            case 11844:
                if (c.absX == 2936) {
                    c.getPA().walkTo(-1, 0);
                } else if (c.absX == 2935) {
                    c.getPA().walkTo(1, 0);
                }
                break;
            case 4383:
                c.getPA().movePlayer(2442, 10147, 0);
                break;
            case 8966:
                c.getPA().movePlayer(2510, 3644, 0);
                break;
            case 4577:
                if (c.absY == 3635) {
                    c.getPA().walkTo(0, 1);
                } else {
                    c.getPA().walkTo(0, -1);
                }
                break;
            case 4558:
            case 4559:
                c.getPA().movePlayer(2522, 3595, 0);
                break;
            case 4551:
                c.getPA().movePlayer(2514, 3619, 0);
                break;
            case 9358:
                c.getPA().movePlayer(2480, 5175, 0);
                break;
            case 9359:
                c.getPA().movePlayer(2862, 9572, 0);
                break;
            case 492:
                c.getPA().movePlayer(2856, 9570, 0);
                break;
            case 1764:
                if (c.objectX == 2856 && c.objectY == 9569) {
                    c.getPA().movePlayer(2858, 3168, 0);
                }
                break;
            case 4031:
                if (c.absY == 3117) {
                    if (c.getItems().playerHasItem(995, 5)) {
                        c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 5);
                        c.getPA().walkTo(0, -2);
                    } else {
                        c.sendMessage("You need 5 coins to go through here.");
                    }
                }
                if (c.absY == 3115) {
                    c.getPA().walkTo(0, 2);
                }
                break;
            case 2406:
                if (c.absX > 3201) {
                    c.getPA().walkTo(-1, 0);
                } else {
                    c.getPA().walkTo(1, 0);
                }
                break;
            case 3725:
            case 3726:
                if (c.absX > 2824) {
                    c.getPA().walkTo(-1, 0);
                } else {
                    c.getPA().walkTo(1, 0);
                }
                break;
            case 3745:
                if (c.absX >= 2823) {
                    c.getPA().walkTo(-1, 0);
                } else {
                    c.getPA().walkTo(1, 0);
                }
                break;

            case 7257:
                if (c.objectX == 2905 && c.objectY == 3537) {
                    c.getPA().movePlayer(3061, 4983, 1);
                }
                break;
            case 11867:
                if (c.objectX == 3019 && c.objectY == 3450) {
                    c.getPA().movePlayer(3058, 9776, 0);
                }
                break;
            case 1755:
                if (c.objectX == 3019 && c.objectY == 9850) {
                    c.getPA().movePlayer(3018, 3450, 0);
                }
                break;
            case 4615:
                if (c.objectX == 2596 && c.objectY == 3608) {
                    c.getPA().movePlayer(2598, 3608, 0);
                }
                break;
            case 2475:
                if (c.objectX == 3233 && c.objectY == 9312) {
                    c.getPA().movePlayer(3233, 2887, 0);
                }
                break;
            case 6481:
                if (c.objectX == 3233 && c.objectY == 2888) {
                    c.getPA().movePlayer(3234, 9312, 0);
                }
                break;
            case 2492:

                if (c.killCount >= 10) {
                    c.getDH().sendOption4("Armadyl", "Bandos", "Saradomin", "Zamorak");
                    c.dialogueAction = 20;
                } else {
                    c.sendMessage("You need 10 kill count before teleporting to a boss chamber.");
                }
                break;

            case 1765:
                //kbd lader
                c.stopMovement();
                c.freezeTimer = 2;
                resetPlayerAttack();
                c.getPA().movePlayer(3067, 10256, 0);
                break;
            case 2882:
            case 2883:
                if (c.objectX == 3268) {
                    if (c.absX < c.objectX) {
                        c.getPA().walkTo(1, 0);
                    } else {
                        c.getPA().walkTo(-1, 0);
                    }
                }
                break;
            case 272:
                c.getPA().movePlayer(c.absX, c.absY, 1);
                break;

            case 273:
                c.getPA().movePlayer(c.absX, c.absY, 0);
                break;
            case 245:
                c.getPA().movePlayer(c.absX, c.absY + 2, 2);
                break;
            case 246:
                c.getPA().movePlayer(c.absX, c.absY - 2, 1);
                break;
            case 1766:
                c.getPA().movePlayer(3016, 3849, 0);
                break;
            case 6552:
                if (c.playerMagicBook == 0) {
                    if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 15050 || c.playerEquipment[c.playerWeapon] == 15040) {
                        c.setSidebarInterface(0, 328);
                    }
                    c.playerMagicBook = 1;
                    c.startAnimation(645);
                    c.setSidebarInterface(6, 12855);
                    c.sendMessage("An ancient wisdomin fills your mind.");
                    c.getPA().resetAutocast();
                } else {
                    if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 15050 || c.playerEquipment[c.playerWeapon] == 15040) {
                        c.setSidebarInterface(0, 328);
                    }
                    c.setSidebarInterface(6, 1151); //modern
                    c.playerMagicBook = 0;
                    c.startAnimation(645);
                    c.sendMessage("You feel a drain on your memory.");
                    c.autocastId = -1;
                    c.getPA().resetAutocast();
                }
                break;

            case 1816:
                resetPlayerAttack();
                c.getPA().startTeleport2(2271, 4680, 0);
                break;
            case 1817:
                c.getPA().startTeleport(3067, 10253, 0, "modern");
                break;
            case 1814:
                //ardy lever
                c.getPA().startTeleport(3153, 3923, 0, "modern");
                break;

            case 9356:
                c.getPA().enterCaves();
                //c.sendMessage("PLEASE LOG OUT AND BACK IN FOR YOUR WAVES TO START!");
                //c.sendMessage("If you don't get a new wave relog it will fix");
                break;
            case 1733:
                // c.getPA().movePlayer(c.absX, 10322, 0);
                c.sendMessage("The basement is scary. No people allowed sowwie :[");
                break;

            case 1734:
                if (c.absY != 10323) {
                    c.getPA().movePlayer(3018, 3450, 0);
                } else {
                    c.getPA().movePlayer(3044, 3927, 0);
                }
                break;

            case 9357:
                c.getPA().resetTzhaar();
                break;

            case 8959:
                if (c.getX() == 2490 && (c.getY() == 10146 || c.getY() == 10148)) {
                    if (c.getPA().checkForPlayer(2490, c.getY() == 10146 ? 10148 : 10146)) {
                        new Object(6951, c.objectX, c.objectY, c.heightLevel, 1, 10, 8959, 15);
                    }
                }
                break;
            case 5276:
                //bank wyv
                if (c.memberStatus < 1) {
                    c.sendMessage("You need to be a donator to use this bank booth");

                    return;
                }
                c.getPA().openUpBank();
                break;
            case 2213:
            case 14367:
            case 11758:
            case 3193:
			case 26972:
			case 11402:
            case 2693:
                c.getPA().openUpBank();
                break;

            case 10177:
                c.getPA().movePlayer(1890, 4407, 0);
                break;
            case 10230:
                c.getPA().movePlayer(2900, 4449, 0);
                break;
            case 10229:
                c.getPA().movePlayer(1912, 4367, 0);
                break;
            case 2623:
                if (c.absX >= c.objectX) c.getPA().walkTo(-1, 0);
                else c.getPA().walkTo(1, 0);
                break;
                //pc boat
            case 14315:
                c.getPA().movePlayer(2661, 2639, 0);
                c.sendMessage("[ <col=2784FF>Pest Control</col> ] You have joined the waiting boat.");
                break;
            case 14314:
                c.getPA().movePlayer(2657, 2639, 0);
                c.sendMessage("[ <col=2784FF>Pest Control</col> ] You have left the waiting boat.");
                break;

            case 1596:
            case 1597:
                if (c.absX == 2936) {
                    c.getPA().walkTo(-1, 0);
                } else {
                    c.getPA().walkTo(1, 0);
                }
                if (c.absY != 3451 && c.absY != 3450) {
                    if (c.getY() >= c.objectY) c.getPA().walkTo(0, -1);
                    else c.getPA().walkTo(0, 1);
                }
                break;

            case 14235:
            case 14233:
                if (c.objectX == 2670) if (c.absX <= 2670) c.absX = 2671;
                else c.absX = 2670;
                if (c.objectX == 2643) if (c.absX >= 2643) c.absX = 2642;
                else c.absX = 2643;
                if (c.absX <= 2585) c.absY += 1;
                else c.absY -= 1;
                c.getPA().movePlayer(c.absX, c.absY, 0);
                break;

            case 14829:
            case 14830:
            case 14827:
            case 14828:
            case 14826:
            case 14831:
                Server.objectHandler.startObelisk(objectType);
                Server.objectManager.startObelisk(objectType);
                break;


            case 9369:
                if (c.getY() > 5175) c.getPA().movePlayer(2399, 5175, 0);
                else c.getPA().movePlayer(2399, 5177, 0);
                break;






            case 2286:
            case 154:
            case 4058:
            case 2295:
            case 2285:
            case 2313:
            case 2312:
            case 2314:
                c.getAgility().handleGnomeCourse(objectType, obX, obY);
                break;

                //barrows
                //Chest
            case 10284:
                if (c.barrowsKillCount < 5) {
                    c.sendMessage("You haven't killed all the brothers");
                }
                if (c.barrowsKillCount == 5 && c.barrowsNpcs[c.randomCoffin][1] == 1) {
                    c.sendMessage("I have already summoned this npc.");
                }
                if (c.barrowsNpcs[c.randomCoffin][1] == 0 && c.barrowsKillCount >= 5) {
                    Server.npcHandler.spawnNpc(c, c.barrowsNpcs[c.randomCoffin][0], 3551, 9694 - 1, 0, 0, 120, 30, 200, 200, true, true);
                    c.barrowsNpcs[c.randomCoffin][1] = 1;
                }
                if ((c.barrowsKillCount > 5 || c.barrowsNpcs[c.randomCoffin][1] == 2) && c.getItems().freeSlots() >= 2) {
                    c.getPA().resetBarrows();
                    c.getItems().addItem(c.getPA().randomRunes(), Misc.random(150) + 100);
                    //if (Misc.random(2) == 1)
                    c.getItems().addItem(c.getPA().randomBarrows(), 1);
                    c.getPA().startTeleport(3564, 3288, 0, "modern");
                } else if (c.barrowsKillCount > 5 && c.getItems().freeSlots() <= 1) {
                    c.sendMessage("You need at least 2 inventory slot opened.");
                }
                break;
                //doors
            case 6749:
                if (obX == 3562 && obY == 9678) {
                    c.getPA().object(3562, 9678, 6749, -3, 0);
                    c.getPA().object(3562, 9677, 6730, -1, 0);
                } else if (obX == 3558 && obY == 9677) {
                    c.getPA().object(3558, 9677, 6749, -1, 0);
                    c.getPA().object(3558, 9678, 6730, -3, 0);
                }
                break;
            case 6730:
                if (obX == 3558 && obY == 9677) {
                    c.getPA().object(3562, 9678, 6749, -3, 0);
                    c.getPA().object(3562, 9677, 6730, -1, 0);
                } else if (obX == 3558 && obY == 9678) {
                    c.getPA().object(3558, 9677, 6749, -1, 0);
                    c.getPA().object(3558, 9678, 6730, -3, 0);
                }
                break;
            case 6727:
                if (obX == 3551 && obY == 9684) {
                    c.sendMessage("You cant open this door..");
                }
                break;
            case 6746:
                if (obX == 3552 && obY == 9684) {
                    c.sendMessage("You cant open this door..");
                }
                break;
            case 6748:
                if (obX == 3545 && obY == 9678) {
                    c.getPA().object(3545, 9678, 6748, -3, 0);
                    c.getPA().object(3545, 9677, 6729, -1, 0);
                } else if (obX == 3541 && obY == 9677) {
                    c.getPA().object(3541, 9677, 6748, -1, 0);
                    c.getPA().object(3541, 9678, 6729, -3, 0);
                }
                break;
            case 6729:
                if (obX == 3545 && obY == 9677) {
                    c.getPA().object(3545, 9678, 6748, -3, 0);
                    c.getPA().object(3545, 9677, 6729, -1, 0);
                } else if (obX == 3541 && obY == 9678) {
                    c.getPA().object(3541, 9677, 6748, -1, 0);
                    c.getPA().object(3541, 9678, 6729, -3, 0);
                }
                break;
            case 6726:
                if (obX == 3534 && obY == 9684) {
                    c.getPA().object(3534, 9684, 6726, -4, 0);
                    c.getPA().object(3535, 9684, 6745, -2, 0);
                } else if (obX == 3535 && obY == 9688) {
                    c.getPA().object(3535, 9688, 6726, -2, 0);
                    c.getPA().object(3534, 9688, 6745, -4, 0);
                }
                break;
            case 6745:
                if (obX == 3535 && obY == 9684) {
                    c.getPA().object(3534, 9684, 6726, -4, 0);
                    c.getPA().object(3535, 9684, 6745, -2, 0);
                } else if (obX == 3534 && obY == 9688) {
                    c.getPA().object(3535, 9688, 6726, -2, 0);
                    c.getPA().object(3534, 9688, 6745, -4, 0);
                }
                break;
            case 6743:
                if (obX == 3545 && obY == 9695) {
                    c.getPA().object(3545, 9694, 6724, -1, 0);
                    c.getPA().object(3545, 9695, 6743, -3, 0);
                } else if (obX == 3541 && obY == 9694) {
                    c.getPA().object(3541, 9694, 6724, -1, 0);
                    c.getPA().object(3541, 9695, 6743, -3, 0);
                }
                break;
            case 6724:
                if (obX == 3545 && obY == 9694) {
                    c.getPA().object(3545, 9694, 6724, -1, 0);
                    c.getPA().object(3545, 9695, 6743, -3, 0);
                } else if (obX == 3541 && obY == 9695) {
                    c.getPA().object(3541, 9694, 6724, -1, 0);
                    c.getPA().object(3541, 9695, 6743, -3, 0);
                }
                break;
                //end doors
                //coffins
            case 6707:
                // verac
                c.getPA().movePlayer(3556, 3298, 0);
                break;

            case 6823:
                if (server.model.minigames.Barrows.selectCoffin(c, objectType)) {
                    return;
                }
                if (c.barrowsNpcs[0][1] == 0) {
                    Server.npcHandler.spawnNpc(c, 2030, c.getX(), c.getY() - 1, -1, 0, 120, 25, 200, 200, true, true);
                    c.barrowsNpcs[0][1] = 1;
                } else {
                    c.sendMessage("You have already searched in this sarcophagus.");
                }
                break;

            case 6706:
                // torag 
                c.getPA().movePlayer(3553, 3283, 0);
                break;

            case 6772:
                if (server.model.minigames.Barrows.selectCoffin(c, objectType)) {
                    return;
                }
                if (c.barrowsNpcs[1][1] == 0) {
                    Server.npcHandler.spawnNpc(c, 2029, c.getX() + 1, c.getY(), -1, 0, 120, 20, 200, 200, true, true);
                    c.barrowsNpcs[1][1] = 1;
                } else {
                    c.sendMessage("You have already searched in this sarcophagus.");
                }
                break;


            case 6705:
                // karil stairs
                c.getPA().movePlayer(3565, 3276, 0);
                break;
            case 6822:
                if (server.model.minigames.Barrows.selectCoffin(c, objectType)) {
                    return;
                }
                if (c.barrowsNpcs[2][1] == 0) {
                    Server.npcHandler.spawnNpc(c, 2028, c.getX(), c.getY() - 1, -1, 0, 90, 17, 200, 200, true, true);
                    c.barrowsNpcs[2][1] = 1;
                } else {
                    c.sendMessage("You have already searched in this sarcophagus.");
                }
                break;

            case 6704:
                // guthan stairs
                c.getPA().movePlayer(3578, 3284, 0);
                break;
            case 6773:
                if (server.model.minigames.Barrows.selectCoffin(c, objectType)) {
                    return;
                }
                if (c.barrowsNpcs[3][1] == 0) {
                    Server.npcHandler.spawnNpc(c, 2027, c.getX(), c.getY() - 1, -1, 0, 120, 23, 200, 200, true, true);
                    c.barrowsNpcs[3][1] = 1;
                } else {
                    c.sendMessage("You have already searched in this sarcophagus.");
                }
                break;

            case 6703:
                // dharok stairs
                c.getPA().movePlayer(3574, 3298, 0);
                break;
            case 6771:
                if (server.model.minigames.Barrows.selectCoffin(c, objectType)) {
                    return;
                }
                if (c.barrowsNpcs[4][1] == 0) {
                    Server.npcHandler.spawnNpc(c, 2026, c.getX(), c.getY() - 1, -1, 0, 120, 45, 250, 250, true, true);
                    c.barrowsNpcs[4][1] = 1;
                } else {
                    c.sendMessage("You have already searched in this sarcophagus.");
                }
                break;

            case 6702:
                // ahrim stairs
                c.getPA().movePlayer(3565, 3290, 0);
                break;
            case 6821:
                if (server.model.minigames.Barrows.selectCoffin(c, objectType)) {
                    return;
                }
                if (c.barrowsNpcs[5][1] == 0) {
                    Server.npcHandler.spawnNpc(c, 2025, c.getX(), c.getY() - 1, -1, 0, 90, 19, 200, 200, true, true);
                    c.barrowsNpcs[5][1] = 1;
                } else {
                    c.sendMessage("You have already searched in this sarcophagus.");
                }
                break;

            case 1276:
            case 1278:
                //trees
                if (c.getItems().freeSlots() <= 0) {
                    c.getPA().sendStatement("Your inventory is full");
                } else {
                    c.woodcut[0] = 1511;
                    c.woodcut[1] = 1;
                    c.woodcut[2] = 25;
                    c.woodcut[3] = obX;
                    c.woodcut[4] = obY;
                    c.woodcut[5] = 8;
                    c.woodcut[6] = objectType;
                    c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2], c.woodcut[3], c.woodcut[4], c.woodcut[5], c.woodcut[6]);
                }
                break;
            case 1281:
                //oak
                if (c.getItems().freeSlots() <= 0) {
                    c.getPA().sendStatement("Your inventory is full");
                } else {
                    c.woodcut[0] = 1521;
                    c.woodcut[1] = 15;
                    c.woodcut[2] = 37;
                    c.woodcut[3] = obX;
                    c.woodcut[4] = obY;
                    c.woodcut[5] = 9;
                    c.woodcut[6] = objectType;
                    c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2], c.woodcut[3], c.woodcut[4], c.woodcut[5], c.woodcut[6]);
                }
                break;

            case 1308:
                //willow
                if (c.getItems().freeSlots() <= 0) {
                    c.getPA().sendStatement("Your inventory is full");
                } else {
                    c.woodcut[0] = 1519;
                    c.woodcut[1] = 30;
                    c.woodcut[2] = 68;
                    c.woodcut[3] = obX;
                    c.woodcut[4] = obY;
                    c.woodcut[5] = 10;
                    c.woodcut[6] = objectType;
                    c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2], c.woodcut[3], c.woodcut[4], c.woodcut[5], c.woodcut[6]);
                }
                break;

            case 1307:
                //maple
                if (c.getItems().freeSlots() <= 0) {
                    c.getPA().sendStatement("Your inventory is full");
                } else {
                    c.woodcut[0] = 1517;
                    c.woodcut[1] = 45;
                    c.woodcut[2] = 100;
                    c.woodcut[3] = obX;
                    c.woodcut[4] = obY;
                    c.woodcut[5] = 11;
                    c.woodcut[6] = objectType;
                    c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2], c.woodcut[3], c.woodcut[4], c.woodcut[5], c.woodcut[6]);
                }
                break;

            case 1309:
                //yew
                if (c.getItems().freeSlots() <= 0) {
                    c.getPA().sendStatement("Your inventory is full");
                } else {
                    c.woodcut[0] = 1515;
                    c.woodcut[1] = 60;
                    c.woodcut[2] = 175;
                    c.woodcut[3] = obX;
                    c.woodcut[4] = obY;
                    c.woodcut[5] = 12;
                    c.woodcut[6] = objectType;
                    c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2], c.woodcut[3], c.woodcut[4], c.woodcut[5], c.woodcut[6]);
                }
                break;

            case 1306:
                // magic
                if (c.getItems().freeSlots() <= 0) {
                    c.getPA().sendStatement("Your inventory is full");
                } else {
                    c.woodcut[0] = 1513;
                    c.woodcut[1] = 75;
                    c.woodcut[2] = 250;
                    c.woodcut[3] = obX;
                    c.woodcut[4] = obY;
                    c.woodcut[5] = 13;
                    c.woodcut[6] = objectType;
                    c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2], c.woodcut[3], c.woodcut[4], c.woodcut[5], c.woodcut[6]);
                }
                break;

            case 2090:
                //copper
            case 2091:
                c.mining[0] = 436;
                c.mining[1] = 1;
                c.mining[2] = 18;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;

            case 2094:
                //tin
                c.mining[0] = 438;
                c.mining[1] = 1;
                c.mining[2] = 18;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;

            case 145856:
            case 2092:
            case 2093:
                //iron
                c.mining[0] = 440;
                c.mining[1] = 15;
                c.mining[2] = 35;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;

            case 14850:
            case 14851:
            case 14852:
            case 2096:
            case 2097:
                //coal
                c.mining[0] = 453;
                c.mining[1] = 30;
                c.mining[2] = 50;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;

            case 2098:
            case 2099:
                c.mining[0] = 444;
                c.mining[1] = 40;
                c.mining[2] = 65;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;

            case 2102:
            case 2103:
            case 14853:
            case 14854:
            case 14855:
                //mith ore
                c.mining[0] = 447;
                c.mining[1] = 55;
                c.mining[2] = 80;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;

            case 2105:
            case 14862:
                //addy ore
                c.mining[0] = 449;
                c.mining[1] = 70;
                c.mining[2] = 95;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;
            case 2107:
            case 14859:
            case 14860:
                //rune ore
                c.mining[0] = 451;
                c.mining[1] = 85;
                c.mining[2] = 125;
                c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
                break;

            case 8143:
                if (c.farm[0] > 0 && c.farm[1] > 0) {
                    c.getFarming().pickHerb();
                }
                break;

                // DOORS
                /*case 1516:
		case 1519:
			if (c.objectY == 9698 || c.objectY == 3558) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0,-1);
				else
					c.getPA().walkTo(0,1);
				break;
			}*/
            case 1601:
                if (c.objectY == 9488 || c.objectY == 3088) {
                    if (c.absY >= c.objectY) c.getPA().walkTo(-1, 0);
                    else c.getPA().walkTo(1, 0);
                    break;
                }
            case 1600:
                if (c.objectY == 9487 || c.objectY == 3087) {
                    if (c.absY >= c.objectY) c.getPA().walkTo(-1, 0);
                    else c.getPA().walkTo(1, 0);
                    break;
                }
            case 1530:
                if (c.absX == 2922) {
                    c.getPA().walkTo(-1, 0);
                } else if (c.absX == 2921) {
                    c.getPA().walkTo(1, 0);
                }
                if (c.objectY == 2564 || c.objectY == 3310) {
                    if (c.absX >= c.objectX) c.getPA().walkTo(-1, 0);
                    else c.getPA().walkTo(1, 0);
                    break;
                }
            case 1531:
            case 1533:
            case 1534:
            case 11712:
            case 11711:
            case 11707:
            case 11708:
            case 6725:
            case 3198:
            case 3197:
                Server.objectHandler.doorHandling(objectType, c.objectX, c.objectY, 0);
                break;

            case 9319:
                if (c.heightLevel == 0) c.getPA().movePlayer(c.absX, c.absY, 1);
                else if (c.heightLevel == 1) c.getPA().movePlayer(c.absX, c.absY, 2);
                break;

            case 9320:
                if (c.heightLevel == 1) c.getPA().movePlayer(c.absX, c.absY, 0);
                else if (c.heightLevel == 2) c.getPA().movePlayer(c.absX, c.absY, 1);
                break;

            case 4496:
            case 4494:
                if (c.heightLevel == 2) {
                    c.getPA().movePlayer(c.absX - 5, c.absY, 1);
                } else if (c.heightLevel == 1) {
                    c.getPA().movePlayer(c.absX + 5, c.absY, 0);
                }
                break;

            case 4493:
                if (c.heightLevel == 0) {
                    c.getPA().movePlayer(c.absX - 5, c.absY, 1);
                } else if (c.heightLevel == 1) {
                    c.getPA().movePlayer(c.absX + 5, c.absY, 2);
                }
                break;

            case 4495:
                if (c.heightLevel == 1) {
                    c.getPA().movePlayer(c.absX + 5, c.absY, 2);
                }
                break;

            case 5126:
                if (c.absY == 3554) c.getPA().walkTo(0, 1);
                else c.getPA().walkTo(0, -1);
                break;

            case 1759:
                if (c.objectX == 2884 && c.objectY == 3397) c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
                break;
                /*case 3203: //dueling forfeit
			if (c.duelCount > 0) {
				c.sendMessage("You may not forfeit yet.");
				break;
			}
			Client o = (Client) Server.playerHandler.players[c.duelingWith];				
			if(o == null) {
				c.getTradeAndDuel().resetDuel();
				c.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
				break;
			}
			if(c.duelRule[0]) {
				c.sendMessage("Forfeiting the duel has been disabled!");
				break;
			}
			if(o != null) {
				o.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
				c.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
				o.duelStatus = 6;
				o.getTradeAndDuel().duelVictory();
				c.getTradeAndDuel().resetDuel();
				c.getTradeAndDuel().resetDuelItems();
				o.sendMessage("The other player has forfeited the duel!");
				c.sendMessage("You forfeit the duel!");
				break;
			}
			
			break;*/

            case 409:
            case 4859:
            case 2640:
                if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
                    c.startAnimation(645);
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.sendMessage("You recharge your prayer points.");
                    c.getPA().refreshSkill(5);
                } else {
                    c.sendMessage("You already have full prayer points.");
                }
                break;

            case 2873:
                if (!c.getItems().ownsCape()) {
                    c.startAnimation(645);
                    c.sendMessage("Saradomin blesses you with a cape.");
                    c.getItems().addItem(2412, 1);
                }
                break;
            case 2875:
                if (!c.getItems().ownsCape()) {
                    c.startAnimation(645);
                    c.sendMessage("Guthix blesses you with a cape.");
                    c.getItems().addItem(2413, 1);
                }
                break;
            case 2874:
                if (!c.getItems().ownsCape()) {
                    c.startAnimation(645);
                    c.sendMessage("Zamorak blesses you with a cape.");
                    c.getItems().addItem(2414, 1);
                }
                break;

            case 357:
                if (c.clueTask[1] == 1 && c.getItems().playerHasItem(2678, 1)) {
                    c.getDH().itemMessage("", "You have found a Clue scroll!", 2677, 250);
                    c.getItems().deleteItem(2678, 1);
                    c.getItems().addItem(2678, 1);
                    c.clueTask[1] = 3;
                }
                break;

            case 350:
                if (c.clueTask[2] == 2 && c.getItems().playerHasItem(2679, 1)) {
                    c.getDH().itemMessage("", "You have found a Clue scroll!", 2677, 250);
                    c.getItems().deleteItem(2679, 1);
                    c.getItems().addItem(2679, 1);
                    c.clueTask[2] = 4;
                }
                break;
            case 361:
                if (c.clueTask[1] == 3 && c.getItems().playerHasItem(2678, 1)) {
                    c.getDH().itemMessage("", "You have found a casket!", 2714, 250);
                    c.getItems().deleteItem(2678, 1);
                    c.getItems().addItem(2717, 1);
                    c.clueTask[1] = 0;
                }
                break;
            case 4616:
                c.getPA().movePlayer(2595, 3608, 0);
                break;
            case 2879:
                c.getPA().movePlayer(2538, 4716, 0);
                break;
            case 2878:
                c.getPA().movePlayer(2509, 4689, 0);
                break;
            case 5960:
                c.getPA().startTeleport2(3090, 3956, 0);
                break;

            case 1815:
                c.getPA().startTeleport2(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
                break;

            case 9706:
                c.getPA().startTeleport2(3105, 3951, 0);
                break;
            case 9707:
                c.getPA().startTeleport2(3105, 3956, 0);
                break;

            case 5959:
                c.getPA().startTeleport2(2539, 4712, 0);
                break;

            case 2558:
                c.sendMessage("This door is locked.");
                break;

            case 9294:
                if (c.absX < c.objectX) {
                    c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
                } else if (c.absX > c.objectX) {
                    c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
                }
                break;

            case 9293:
                if (c.absX < c.objectX) {
                    c.getPA().movePlayer(2892, 9799, 0);
                } else {
                    c.getPA().movePlayer(2886, 9799, 0);
                }
                break;
            case 10529:
            case 10527:
                if (c.absY <= c.objectY) c.getPA().walkTo(0, 1);
                else c.getPA().walkTo(0, -1);
                break;
            case 3044:
                //smithing
                c.getSmithing().sendSmelting();

                if (!c.getItems().playerHasItem(4, 1) && !c.getItems().playerHasItem(2353, 1)) {
                    c.sendMessage("A steel bar or ammo mould to make this");
                } else if (c.getItems().playerHasItem(4, 1) && c.getItems().playerHasItem(2353, 1)) {
                    c.getItems().addItem(7119, 3);
                    c.startAnimation(898);
                    c.getItems().deleteItem(2353, 1);
                    c.sendMessage("You make some cannon balls");

                }



                break;
            case 733:
                c.startAnimation(451);
                /*if (Misc.random(1) == 1) {
				c.getPA().removeObject(c.objectX, c.objectY);
				c.sendMessage("You slash the web.");
			} else {
				c.sendMessage("You fail to slash the webs.");
			}*/
                if (c.objectX == 3158 && c.objectY == 3951) {
                    new Object(734, c.objectX, c.objectY, c.heightLevel, 1, 10, 733, 50);
                } else {
                    new Object(734, c.objectX, c.objectY, c.heightLevel, 0, 10, 733, 50);
                }
                break;

            default:
                ScriptManager.callFunc("objectClick1_" + objectType, c, objectType, obX, obY);
                break;

        }
    }

    public void secondClickObject(int objectType, int obX, int obY) {
        c.clickObjectType = 0;
        if (c.playerRights == 3) c.sendMessage("Object type: " + objectType);
        switch (objectType) {
            case 6:
                DwarfMultiCannon.pickUpCannon();
                break;
            case 6731:
                c.getShops().openShop(77);
                break;
            case 1152:
                c.getDH().sendDialogues(16, -1);
                break;
            case 2561:
                c.getThieving().stealFromStall(995, 25000, 1, 1);
                break;
            case 707:
                c.getDH().sendDialogues(41, 7502);
                //c.getShops().openShop(65);
                break;
            case 2560:
                c.getThieving().stealFromStall(995, 45000, 40, 40);
                break;
            case 2565:

                if (c.memberStatus < 1) {
                    c.sendMessage("You need to be a donator to steal from this stall you get 450k each thiev");

                    return;
                }

                c.getThieving().stealFromStall(995, 450000, 50, 1);
                break;



            case 2564:
                c.getThieving().stealFromStall(995, 75000, 60, 60);
                break;
            case 2562:
                c.getThieving().stealFromStall(995, 120000, 80, 80);
                break;
            case 11666:
            case 3044:
                c.getSmithing().sendSmelting();
                break;
			case 2213:
            case 14367:
            case 11758:
            case 3193:
			case 26972:
			case 25808:
			case 11402:
            case 2693:
			case 4483:
                c.getPA().openUpBank();
                break;

            case 2557:
                if (System.currentTimeMillis() - c.lastLockPick < 3 || c.freezeTimer > 0) break;
                if (c.getItems().playerHasItem(1523, 1)) {
                    c.lastLockPick = System.currentTimeMillis();
                    if (Misc.random(10) <= 3) {
                        c.sendMessage("You fail to pick the lock.");
                        break;
                    }
                    if (c.objectX == 3190 && c.objectY == 3957) {
                        if (c.absY == 3958) {
                            c.getPA().walkTo2(0, -1);
                        } else if (c.absY == 3957) {
                            c.getPA().walkTo2(0, 1);
                        }

                    } else if (c.objectX == 3191 && c.objectY == 3963) {
                        if (c.absY == 3962) {
                            c.getPA().walkTo2(0, 1);
                        } else if (c.absY == 3963) {
                            c.getPA().walkTo2(0, -1);
                        }
                    }
                } else {
                    c.sendMessage("I need a lockpick to pick this lock.");
                }
                break;
            case 2558:
                if (System.currentTimeMillis() - c.lastLockPick < 3000 || c.freezeTimer > 0) break;
                if (c.getItems().playerHasItem(1523, 1)) {
                    c.lastLockPick = System.currentTimeMillis();
                    if (Misc.random(10) <= 3) {
                        c.sendMessage("You fail to pick the lock.");
                        break;
                    }
                    if (c.objectX == 3044 && c.objectY == 3956) {
                        if (c.absX == 3045) {
                            c.getPA().walkTo2(-1, 0);
                        } else if (c.absX == 3044) {
                            c.getPA().walkTo2(1, 0);
                        }

                    } else if (c.objectX == 3038 && c.objectY == 3956) {
                        if (c.absX == 3037) {
                            c.getPA().walkTo2(1, 0);
                        } else if (c.absX == 3038) {
                            c.getPA().walkTo2(-1, 0);
                        }
                    } else if (c.objectX == 3041 && c.objectY == 3959) {
                        if (c.absY == 3960) {
                            c.getPA().walkTo2(0, -1);
                        } else if (c.absY == 3959) {
                            c.getPA().walkTo2(0, 1);
                        }
                    }
                } else {
                    c.sendMessage("I need a lockpick to pick this lock.");
                }
                break;
            default:
                ScriptManager.callFunc("objectClick2_" + objectType, c, objectType, obX, obY);
                break;
        }
    }


    public void thirdClickObject(int objectType, int obX, int obY) {
        c.clickObjectType = 0;
        if (c.playerRights == 3) c.sendMessage("Object type: " + objectType);
        switch (objectType) {
            default: ScriptManager.callFunc("objectClick3_" + objectType, c, objectType, obX, obY);
            break;
        }
    }

    public void firstClickNpc(int npcType) {
        c.fishitem = -1;
        c.clickNpcType = 0;
        c.npcClickIndex = 0;
        if (c.fishitem != -1) {
            if (!c.getItems().playerHasItem(c.fishitem)) {
                c.sendMessage("You need a " + c.getItems().getItemName(c.fishitem) + " to fish for " + c.getItems().getItemName(c.fishies));
                c.fishing = false;
                return;
            }
            if (c.getItems().freeSlots() == 0) {
                c.sendMessage("Your inventory is full.");
                c.fishing = false;
                return;
            }
            if (c.playerFishing < c.fishreqt) {
                c.sendMessage("You need a fishing level of " + c.fishreqt + " to fish here.");
                c.fishing = false;
                return;
            }
            c.fishtimer = c.getFishing().fishtime(c.fishies, c.fishreqt);
        }
        switch (npcType) {
            /* Shops */
            case 1513:
                c.getDH().sendDialogues(20010, -1);
                break;
            case 1289:
                c.getDH().sendDialogues(20011, -1);
                break;
            case 649:
                c.getDH().sendDialogues(20012, -1);
                break;
            case 2566:
                c.getDH().sendDialogues(20013, -1);
                break;
            case 1294:
                c.getShops().openShop(23);
                break;
            case 519:
                c.getShops().openShop(11);
                break;
            case 8206:
                c.getShops().openShop(45);
                c.sendMessage("[ <col=2784FF>Vote Shop </col>] You have <col=2784FF>" + c.votePoints + " </col>vote points! Please remember to vote daily!");
                break;
            case 2270:
                c.getShops().openShop(10);
                break;
            case 638:
                c.getShops().openShop(2);
                break;
            case 884:
                //c.getShops().openShop(29);
                c.getDH().sendDialogues(25000, -1);
                break;
            case 707:
                c.getDH().sendDialogues(25050, -1);
                break;
                /* Shops End */

                /* Fishing */
            case 219:
                //fish shop
                break;
			case 1660:
				c.getShops().openShop(78);
			break;

            case 316:
                c.fishing = true;
                c.fishXP = 350;
                c.fishies = 317;
                c.fishreqt = 0;
                c.fishitem = 303;
                c.fishemote = 621;
                break;
            case 334:
                c.fishing = true;
                c.fishXP = 350; // Anchovie/Shrimp
                c.fishies = 317;
                c.fishreqt = 0;
                c.fishitem = 303;
                c.fishemote = 621;
                c.fishies2 = 321;
                c.fishreq2 = 5;
                break;
            case 324:
                c.fishing = true;
                c.fishXP = 700;
                c.fishies = 377;
                c.fishreqt = 40;
                c.fishitem = 301;
                c.fishemote = 619;
                break;
            case 325:
                c.fishing = true;
                c.fishXP = 2000;
                c.fishies = 15270;
                c.fishreqt = 95;
                c.fishitem = 303;
                c.fishemote = 621;
                break;
            case 320:
                c.fishing = true;
                c.fishXP = 2000;
                c.fishies = 15270;
                c.fishreqt = 95;
                c.fishitem = 303;
                c.fishemote = 621;
                break;
            case 326:
                c.fishing = true;
                c.fishXP = 600;
                c.fishies = 341;
                c.fishreqt = 23;
                c.fishitem = 303;
                c.fishemote = 621;
                c.fishies2 = 7944;
                c.fishreq2 = 62;
                break;
            case 313:
                c.fishing = true;
                c.fishXP = 600;
                c.fishies = 341;
                c.fishreqt = 23;
                c.fishitem = 303;
                c.fishemote = 621;
                break;

                /* End Fishing */


            case 945:
                c.getDH().sendDialogues(20000, -1);
                break;
            case 220:
                c.getDH().sendDialogues(36899, npcType);
                break;
            case 556:
                if (c.Arma < 15) {
                    c.sendMessage("You need atleast 15 Armadyl KC to enter here!");
                    return;
                } else {
                    c.getPA().movePlayer(2897, 3619, 4);
                    c.Arma -= 15;
                    c.sendMessage("A magical force reseted your Armadyl kill count!");
                }
                break;
            case 1860:
                if (c.memberStatus == 1) {
                    c.getDH().sendDialogues(1859, npcType);
                } else if (c.memberStatus == 2) {
                    c.getDH().sendDialogues(1859, npcType);
                } else if (c.memberStatus == 3) {
                    c.getDH().sendDialogues(1859, npcType);
                } else {
                    c.sendMessage("This is a Member only Feature.");
                }

                break;
            case 364:
                c.getDH().sendDialogues(979, npcType);
                break;
            case 6731:
                c.getShops().openShop(77);
                break;
            case 2261:
                c.getPA().walkableInterface(-1);
                c.getPA().movePlayer(2885, 5330, 2);
                break;
            case 2259:
                c.getPA().movePlayer(2885, 5345, 2);
                c.getPA().walkableInterface(12418);
                c.sendMessage("You have entered Zamorak, To leave talk to me on the other side.");
                break;
            case 398:
                c.getDH().sendDialogues(989, npcType);
                break;
            case 262:
                c.getPA().movePlayer(2855, 3542, 0);
                break;
            case 399:
                c.getPA().movePlayer(2911, 5299, 2);
                break;
            case 1064:
                c.getPA().movePlayer(2852, 5333, 2);
                break;



            case 888:
                if (c.Band < 15) {
                    c.sendMessage("You need atleast 15 Bandos KC to enter here!");
                    return;
                }

                c.getPA().movePlayer(2864, 5354, 6);
                c.Band -= 15;
                c.sendMessage("A magical force reseted your Bandos kill count!");

                if (c.absX == 2864 && c.absY == 5354) {
                    c.getPA().movePlayer(2863, 5354, 2);
                    c.autoRet = 0;
                    c.getCombat().resetPlayerAttack();
                }
                break;


            case 70:
                c.getPA().movePlayer(2872, 5269, 2);
                c.sendMessage("You have entered Armadyl, To leave click the Pillar.");
                c.sendMessage("Note: Ruby bolts (e) and Diamond bolts (e) are recommended!");
                break;
            case 251:
                c.getDH().sendDialogues(27, npcType);
                break;
            case 243:
                c.getDH().sendDialogues(20, npcType);
                break;
            case 2262:
                c.getDH().sendDialogues(23, npcType);
                break;
            case 3299:
                c.getDH().sendDialogues(24, npcType);
                break;

            case 461:
                if (c.clueTask[0] == 1 && c.getItems().playerHasItem(2677, 1)) {
                    c.getDH().itemMessage("", "You have found a Clue scroll!", 2677, 250);
                    c.getItems().deleteItem(2677, 1);
                    c.getItems().addItem(2677, 1);
                    c.clueTask[0] = 3;
                } else {
                    c.getShops().openShop(2);
                }
                break;
            case 3305:
                if (c.clueTask[0] == 2 && c.getItems().playerHasItem(2677, 1)) {
                    c.getDH().itemMessage("", "You have found a Clue scroll!", 2677, 250);
                    c.getItems().deleteItem(2677, 1);
                    c.getItems().addItem(2677, 1);
                    c.clueTask[0] = 4;
                }
                break;

            case 278:
                if (c.clueTask[1] == 2 && c.getItems().playerHasItem(2678, 1)) {
                    c.getDH().itemMessage("", "You have found a Clue scroll!", 2677, 250);
                    c.getItems().deleteItem(2678, 1);
                    c.getItems().addItem(2678, 1);
                    c.clueTask[1] = 4;
                }
                break;

            case 2622:
                if (c.clueTask[2] == 1 && c.getItems().playerHasItem(2679, 1)) {
                    c.getDH().itemMessage("", "You have found a Clue scroll!", 2677, 250);
                    c.getItems().deleteItem(2679, 1);
                    c.getItems().addItem(2679, 1);
                    c.clueTask[2] = 3;
                }
                break;
            case 905:
                if (c.clueTask[1] == 2 && c.getItems().playerHasItem(2678, 1)) {
                    c.clueTask[1] = 4;
                    c.getDH().itemMessage("", "You have found a Clue scroll!", 2677, 250);
                    c.getItems().deleteItem(2678, 1);
                    c.getItems().addItem(2678, 1);
                } else {
                    //c.getShops().openShop(12);
                }
                break;


            case 887:
                c.getShops().openShop(32);
                break;

            case 2402:
                c.getShops().openShop(31);
                break;
            case 651:
                c.getShops().openShop(29);
                break;
            case 1334:
                if (!c.getItems().ownsBook()) {
                    c.getItems().addItem(3842, 1);
                    c.sendMessage("You recieve a Unholy book from Jossik.");
                } else {
                    c.sendMessage("You already have a book.");
                }
                break;
            case 587:
                c.getShops().openShop(22);
                break;
            case 3792:
                c.getPA().movePlayer(2659, 2676, 0);
                break;
            case 1596:
                c.getDH().sendDialogues(20, npcType);
                break;
            case 3020:
                c.getDH().sendDialogues(20, npcType);

                break;
            case 1918:
                break;
            case 3791:
                c.getPA().movePlayer(3049, 3235, 0);
                break;
            case 2825:
                c.getPA().movePlayer(2710, 9466, 0);
                c.sendMessage("The ship boards and the pirate escorts you to the dungeon.");
                break;
            case 534:
                c.getShops().openShop(21);
                break;
            case 1071:
                c.getShops().openShop(19);
                break;
            case 706:
                c.getDH().sendDialogues(9, npcType);
                break;
            case 2258:
                c.getDH().sendDialogues(17, npcType);
                break;
            case 8275:
                /*if (c.slayerTask <= 0) {
                    c.getDH().sendDialogues(11, npcType);
                } else {
                    c.getDH().sendDialogues(13, npcType);
                }*/
				 c.getDH().sendDialogues(25075, -1);
                break;




            case 500:
                if (c.monkeyk0ed >= 10) {
                    c.getDH().sendDialogues(21, npcType);
                } else {
                    c.getDH().sendDialogues(23, npcType);
                }
                break;
                /*case 461:
				c.getShops().openShop(2);
			break;*/

            case 683:
                c.getShops().openShop(3);
                break;

            case 549:
                c.getShops().openShop(28);
                break;

            case 2538:
                c.getShops().openShop(6);
                break;


            case 1282:
                c.getShops().openShop(7);
                break;
            case 1152:
                c.getDH().sendDialogues(16, npcType);
                break;
            case 494:
                c.getPA().openUpBank();
                break;
            case 3789:
                c.sendMessage("You currently have " + c.pcPoints + " pest control points.");
                break;
            case 546:
                c.getShops().openShop(27);
                break;
            case 3788:
                c.getShops().openVoid();
                break;

            case 460:
                c.getDH().sendDialogues(3, npcType);
                break;
            case 462:
                c.getDH().sendDialogues(7, npcType);
                break;

            case 541:
                c.getShops().openShop(26);
                break;
            case 522:
            case 523:
                c.getShops().openShop(1);
                break;
            case 599:
                c.getPA().showInterface(3559);
                c.canChangeAppearance = true;
                break;
            case 668:
                c.getDH().sendDialogues(501, -1);
                break;
            case 904:
                c.sendMessage("You have " + c.magePoints + " points.");
                break;
            default:
                ScriptManager.callFunc("npcClick1_" + npcType, c, npcType);
                if (c.playerRights == 3) Misc.println("First Click Npc : " + npcType);
                break;
        }
    }

    public void secondClickNpc(int npcType) {
        c.fishitem = -1;
        c.clickNpcType = 0;
        c.npcClickIndex = 0;
        if (c.fishitem != -1) {
            if (!c.getItems().playerHasItem(c.fishitem)) {
                c.sendMessage("You need a " + c.getItems().getItemName(c.fishitem) + " to fish for " + c.getItems().getItemName(c.fishies));
                c.fishing = false;
                return;
            }
            if (c.getItems().freeSlots() == 0) {
                c.sendMessage("Your inventory is full.");
                c.fishing = false;
                return;
            }
            if (c.playerFishing < c.fishreqt) {
                c.sendMessage("You need a fishing level of " + c.fishreqt + " to fish here.");
                c.fishing = false;
                return;
            }
            c.fishtimer = c.getFishing().fishtime(c.fishies, c.fishreqt);
        }
        switch (npcType) {
            case 212:
                c.getShops().openShop(29);
                break;
            case 553:
                c.getShops().openShop(2);
                break;
            case 1039:
                c.getShops().openShop(30);
                break;
            case 546:
                c.getShops().openShop(27);
                break;
            case 541:
                c.getShops().openShop(26);
                break;
            case 2824:
                c.getShops().openShop(25);
                break;
            case 570:
                c.getShops().openShop(24);
                break;
            case 587:
                c.getShops().openShop(22);
                break;
            case 534:
                c.getShops().openShop(21);
                break;
            case 2270:
                c.getShops().openShop(20);
                break;
            case 1282:
                c.getShops().openShop(7);
                break;

            case 3788:
                c.getShops().openVoid();
                break;
            case 494:
                c.getPA().openUpBank();
                break;

            case 904:
                c.getShops().openShop(17);
                break;
            case 522:
            case 523:
                c.getShops().openShop(1);
                break;
            case 8275:
                c.getShops().openShop(17);
                break;
                /*case 461:
				c.getShops().openShop(2);
			break;*/

            case 683:
                c.getShops().openShop(3);
                break;

            case 549:
                c.getShops().openShop(28);
                break;

            case 2538:
                c.getShops().openShop(6);
                break;

            case 519:
                c.getShops().openShop(11);
                break;
            case 3789:
                c.getShops().openShop(18);
                break;
            case 1:
            case 9:
            case 18:
            case 20:
            case 26:
            case 21:
                c.getThieving().stealFromNPC(npcType);
                break;
            case 333:
                c.fishing = true;
                c.fishXP = 650;
                c.fishies = 359;
                c.fishreqt = 35;
                c.fishitem = 311;
                c.fishemote = 618;
                c.fishies2 = 371;
                c.fishreq2 = 50;
                break;

            case 312:
                c.fishing = true;
                c.fishXP = 650;
                c.fishies = 359;
                c.fishreqt = 35;
                c.fishitem = 311;
                c.fishemote = 618;
                c.fishies2 = 371;
                c.fishreq2 = 50;
                break;
            case 324:
                c.fishing = true;
                c.fishXP = 650;
                c.fishies = 359;
                c.fishreqt = 35;
                c.fishitem = 311;
                c.fishemote = 618;
                c.fishies2 = 371;
                c.fishreq2 = 50;
                break;
            case 334:
                c.fishing = true;
                c.fishXP = 650;
                c.fishies = 359;
                c.fishreqt = 35;
                c.fishitem = 311;
                c.fishemote = 618;
                c.fishies2 = 371;
                c.fishreq2 = 50;
                break;
            case 316:
                c.fishing = true;
                c.fishXP = 630;
                c.fishies = 327;
                c.fishreqt = 5;
                c.fishitem = 307;
                c.fishemote = 622;
                c.fishies2 = 345;
                c.fishreq2 = 10;
                break;
            case 326:
                c.fishing = true;
                c.fishXP = 530;
                c.fishies = 327;
                c.fishreqt = 5;
                c.fishitem = 307;
                c.fishemote = 622;
                c.fishies2 = 345;
                c.fishreq2 = 10;
                break;
            case 331:
                c.fishing = true;
                c.fishXP = 770;
                c.fishies = 349;
                c.fishreqt = 25;
                c.fishitem = 307;
                c.fishemote = 622;
                c.fishies2 = 0;
                c.fishreq2 = 0;
                break;
            case 313:
                c.fishing = true;
                c.fishXP = 1000;
                c.fishies = 383;
                c.fishreqt = 79;
                c.fishitem = 311;
                c.fishemote = 618;
                c.fishies2 = 0;
                c.fishreq2 = 0;
                break;
            default:
                ScriptManager.callFunc("npcClick2_" + npcType, c, npcType);
                if (c.playerRights == 3) Misc.println("Second Click Npc : " + npcType);
                break;

        }
    }

    public void thirdClickNpc(int npcType) {
        c.clickNpcType = 0;
        c.npcClickIndex = 0;
        switch (npcType) {

            case 8275:
                //slayer
                c.getShops().openShop(17);
                break;

            case 212:
                c.getShops().openShop(29);
                break;
            default:
                ScriptManager.callFunc("npcClick3_" + npcType, c, npcType);
                if (c.playerRights == 3) Misc.println("Third Click NPC : " + npcType);
                break;

        }
    }


}