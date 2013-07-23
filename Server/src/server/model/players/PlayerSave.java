package server.model.players;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import server.Server;
import server.util.Misc;

public class PlayerSave {



    /**
     *Loading
     **/
    public static int loadGame(Client p, String playerName, String playerPass) {
        String line = "";
        String token = "";
        String token2 = "";
        String[] token3 = new String[3];
        boolean EndOfFile = false;
        int ReadMode = 0;
        BufferedReader characterfile = null;
        boolean File1 = false;

        try {
            characterfile = new BufferedReader(new FileReader("./Data/characters/" + playerName + ".txt"));
            File1 = true;
        } catch (FileNotFoundException fileex1) {}

        if (File1) {
            //new File ("./characters/"+playerName+".txt");
        } else {
            Misc.println(playerName + ": character file not found.");
            p.newPlayer = false;
            return 0;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(playerName + ": Error loading file!");
            return 3;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token3 = token2.split("\t");
                switch (ReadMode) {
                    case 1:
                        if (token.equals("character-password")) {
                            if (playerPass.equalsIgnoreCase(token2)) {
                                playerPass = token2;
                            } else {
                                return 3;
                            }
                        }
                        break;
                    case 2:
                        if (token.equals("character-height")) {
                            p.heightLevel = Integer.parseInt(token2);
                        } else if (token.equals("character-posx")) {
                            p.teleportToX = (Integer.parseInt(token2) <= 0 ? 3210 : Integer.parseInt(token2));
                        } else if (token.equals("character-posy")) {
                            p.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424 : Integer.parseInt(token2));

                        } else if (token.equals("character-energy")) {
                            p.runEnergy = Integer.parseInt(token2);
                        } else if (token.equals("dragon-points")) {
                            p.dkPoints = Integer.parseInt(token2);
                        } else if (token.equals("character-rights")) {
                            p.playerRights = Integer.parseInt(token2);
                        } else if (token.equals("connected-from")) {
                            p.lastConnectedFrom.add(token2);
                        } else if (token.equals("NigaGotStarter")) {
                            p.NigaGotStarter = Boolean.parseBoolean(token2);
                        } else if (token.equals("join-date")) {
                            p.joinDate = token2;
                        } else if (token.equals("tempid")) {
                            p.tempid = Integer.parseInt(token2);
                        } else if (token.equals("tempamt")) {
                            p.tempamt = Integer.parseInt(token2);
						} else if (token.equals("loyalty-rank")) {
							p.loyaltyRank = Integer.parseInt(token2);	
                        } else if (token.equals("tempprice")) {
                            p.tempprice = Integer.parseInt(token2);

                        } else if (token.equals("xpLock")) {
                            p.xpLock = Boolean.parseBoolean(token2);
                        } else if (token.equals("clanId")) {
                            p.clanId = Integer.parseInt(token2);
                        } else if (token.equals("chatId")) {
                            p.chatId = Integer.parseInt(token2);
                        } else if (token.equals("clanId2")) {
                            p.lastClanJoined = Integer.parseInt(token2);
                        } else if (token.equals("HasAClan")) {
                            p.hasAClan = Boolean.parseBoolean(token2);
                        } else if (token.equals("prayer-book")) {
                            p.playerPrayerBook = Integer.parseInt(token2);
                        } else if (token.equals("killStreak")) {
                            p.killStreak = Integer.parseInt(token2);
                        } else if (line.startsWith("KC")) {
                            p.KC = Integer.parseInt(token2);
                        } else if (line.startsWith("DC")) {
                            p.DC = Integer.parseInt(token2);
                        } else if (token.equals("tutorial-progress")) {
                            p.tutorial = Integer.parseInt(token2);
                        } else if (token.equals("cw-games")) {
                            p.cwGames = Integer.parseInt(token2);
                        } else if (token.equals("Forinthry-brace")) {
                            p.bracelet1 = Integer.parseInt(token2);
                        } else if (token.equals("Forinthry-brace2")) {
                            p.bracelet2 = Integer.parseInt(token2);
                        } else if (token.equals("Forinthry-brace3")) {
                            p.bracelet3 = Integer.parseInt(token2);
                        } else if (token.equals("Forinthry-brace4")) {
                            p.bracelet4 = Integer.parseInt(token2);
                        } else if (token.equals("Forinthry-brace5")) {
                            p.bracelet5 = Integer.parseInt(token2);
                        } else if (token.equals("vls-hits")) {
                            p.degradeTime = Integer.parseInt(token2);
                        } else if (token.equals("vesta pl8body")) {
                            p.vestaplatedegrade = Integer.parseInt(token2);
                        } else if (token.equals("clickedHome")) {
                            p.clickedHome = Integer.parseInt(token2);

                        } else if (token.equals("Homex")) {
                            p.playerHomeX = (Integer.parseInt(token2) <= 0 ? 3210 : Integer.parseInt(token2));
                            //p.playerHomeX = Integer.parseInt(token2);
                        } else if (token.equals("HomeY")) {
                            p.playerHomeY = (Integer.parseInt(token2) <= 0 ? 3210 : Integer.parseInt(token2));

                        } else if (token.equals("crystal-bow-shots")) {
                            p.crystalBowArrowCount = Integer.parseInt(token2);

                        } else if (token.equals("gotStarter")) {
                            p.gotStarter = Integer.parseInt(token2);

                        } else if (token.equals("skull-timer")) {
                            p.skullTimer = Integer.parseInt(token2);
                        } else if (token.equals("magic-book")) {
                            p.playerMagicBook = Integer.parseInt(token2);
                        } else if (token.equals("brother-info")) {
                            p.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer.parseInt(token3[1]);
                        } else if (token.equals("special-amount")) {
                            p.specAmount = Double.parseDouble(token2);
                        } else if (token.equals("selected-coffin")) {
                            p.randomCoffin = Integer.parseInt(token2);
                        } else if (token.equals("barrows-killcount")) {
                            p.pkPoints = Integer.parseInt(token2);
                        } else if (token.equals("teleblock-length")) {
                            p.teleBlockDelay = System.currentTimeMillis();
                            p.teleBlockLength = Integer.parseInt(token2);
                        } else if (token.equals("pc-points")) {
                            p.pcPoints = Integer.parseInt(token2);
                        } else if (token.equals("votePoints")) {
                            p.votePoints = Integer.parseInt(token2);
                        } else if (token.equals("pePoints")) {
                            p.pePoints = Integer.parseInt(token2);
                        } else if (token.equals("slayerTask")) {
                            p.slayerTask = Integer.parseInt(token2);
                        } else if (token.equals("taskAmount")) {
                            p.taskAmount = Integer.parseInt(token2);
                        } else if (token.equals("magePoints")) {
                            p.magePoints = Integer.parseInt(token2);
                        } else if (token.equals("donatorPoints")) {
                            p.donatorPoints = Integer.parseInt(token2);
                        } else if (token.equals("autoRet")) {
                            p.autoRet = Integer.parseInt(token2);
                        } else if (token.equals("character-longsword")) {
                            p.vlsLeft = Integer.parseInt(token2);

                        } else if (token.equals("bhenter")) {
                            p.enterBHTime = Integer.parseInt(token2);
                        } else if (token.equals("bhleave")) {
                            p.enterBHTime = Integer.parseInt(token2);
                        } else if (token.equals("bhpickup")) {
                            p.enterBHTime = Integer.parseInt(token2);
                        } else if (token.equals("bhcash")) {
                            p.enterBHTime = Integer.parseInt(token2);
                        } else if (token.equals("bhcrater")) {
                            p.enterBHTime = Integer.parseInt(token2);
                        } else if (token.equals("bhrogue")) {
                            p.rogueKill = Integer.parseInt(token2);
                        } else if (token.equals("bhtarget")) {
                            p.targetKill = Integer.parseInt(token2);

                        } else if (token.equals("character-warhammer")) {
                            p.statLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-spear")) {
                            p.vSpearLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-chainbody")) {
                            p.vTopLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-chainskirt")) {
                            p.vLegsLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-full helm")) {
                            p.sHelmLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-platebody")) {
                            p.sTopLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-platelegs")) {
                            p.sLegsLeft = Integer.parseInt(token2);

                        } else if (token.equals("Frost-KC")) {
                            p.Frost = Integer.parseInt(token2);
                        } else if (token.equals("character-cRapier")) {
                            p.chaoticRapier = Integer.parseInt(token2);
                        } else if (token.equals("character-cLong")) {
                            p.chaoticLong = Integer.parseInt(token2);
                        } else if (token.equals("character-cStaff")) {
                            p.chaoticStaff = Integer.parseInt(token2);
                        } else if (token.equals("character-cMaul")) {
                            p.chaoticMaul = Integer.parseInt(token2);
                        } else if (token.equals("character-cBow")) {
                            p.chaoticBow = Integer.parseInt(token2);
                        } else if (token.equals("Chaotic-shield")) {
                            p.chaoticShield = Integer.parseInt(token2);
                        } else if (token.equals("Arma-KC")) {
                            p.Arma = Integer.parseInt(token2);
                        } else if (token.equals("Band-KC")) {
                            p.Band = Integer.parseInt(token2);
                        } else if (token.equals("Zammy-KC")) {
                            p.Zammy = Integer.parseInt(token2);
                        } else if (token.equals("Sara-KC")) {
                            p.Sara = Integer.parseInt(token2);
                        } else if (token.equals("character-hood")) {
                            p.zHoodLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-staff")) {
                            p.zStaffLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-robe top")) {
                            p.zTopLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-robe bottom")) {
                            p.zBottomLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-leather body")) {
                            p.mBodyLeft = Integer.parseInt(token2);
                        } else if (token.equals("character-chaps")) {
                            p.mChapsLeft = Integer.parseInt(token2);


                        } else if (token.equals("clue-1")) {
                            p.clueTask[0] = Integer.parseInt(token2);
                        } else if (token.equals("clue-2")) {
                            p.clueTask[1] = Integer.parseInt(token2);
                        } else if (token.equals("clue-3")) {
                            p.clueTask[2] = Integer.parseInt(token2);
                        } else if (token.equals("barrowskillcount")) {
                            p.barrowsKillCount = Integer.parseInt(token2);

                        } else if (token.equals("flagged")) {
                            p.accountFlagged = Boolean.parseBoolean(token2);
                        } else if (token.equals("PRAYFUCK")) {
                            p.PRAYFUCK = Boolean.parseBoolean(token2);
                        } else if (token.equals("NoMoreInterface")) {
                            p.NoMoreInterface = Boolean.parseBoolean(token2);	
                        } else if (token.equals("GettingReward")) {
                            p.GettingReward = Boolean.parseBoolean(token2);	
							
                        } else if (token.equals("LotteryEnter")) {
                            p.LotteryEnter = Boolean.parseBoolean(token2);
                        } else if (token.equals("LotteryDone")) {
                            p.LotteryDone = Boolean.parseBoolean(token2);					
                        } else if (token.equals("TutorialStart")) {
                            p.TutorialStart = Boolean.parseBoolean(token2);
                        } else if (token.equals("TutorialDone")) {
                            p.TutorialDone = Boolean.parseBoolean(token2);		
                        } else if (token.equals("EarnedRareItem")) {
                            p.EarnedRareItem = Boolean.parseBoolean(token2);	
							
                        } else if (token.equals("wave")) {
                            p.waveId = Integer.parseInt(token2);
                        } else if (token.equals("void")) {
                            for (int j = 0; j < token3.length; j++) {
                                p.voidStatus[j] = Integer.parseInt(token3[j]);
                            }
                        } else if (token.equals("gwkc")) {
                            p.killCount = Integer.parseInt(token2);
                        } else if (token.equals("fightMode")) {
                            p.fightMode = Integer.parseInt(token2);
                        } else if (token.equals("Monkey Kc")) {
                            p.monkeyk0ed = Integer.parseInt(token2);

                        } else if (token.equals("tradeTimer")) {
                            p.tradeTimer = Integer.parseInt(token2);
                        } else if (token.equals("pkPoints")) {
                            p.pkPoints = Integer.parseInt(token2);
						} else if (token.equals("SoFpoints")) {
                            p.SoFpoints = Integer.parseInt(token2);	
						} else if (token.equals("character-yellTag")) {
							p.customYellTag = token2;
                        } else if (token.equals("AchievementPoints")) {
                            p.achievementPoints = Integer.parseInt(token2);
                        } else if (token.equals("KillingPoints")) {
                            p.KillingPoints = Integer.parseInt(token2);
                        } else if (token.equals("memberStatus")) {
                            p.memberStatus = Integer.parseInt(token2);
                        } else if (token.equals("firstMember")) {
                            p.firstMember = Integer.parseInt(token2);
                        } else if (token.equals("isDonator")) {
                            p.isDonator = Integer.parseInt(token2);
                        }
                        break;
                    case 3:
                        if (token.equals("character-equip")) {
                            p.playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                            p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                        }
                        break;
                    case 4:
                        if (token.equals("character-look")) {
                            p.playerAppearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                        }
                        break;
                    case 5:
                        if (token.equals("character-skill")) {
                            p.playerLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                            p.playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                        }
                        break;
                    case 6:
                        if (token.equals("character-item")) {
                            p.playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                            p.playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                        }
                        break;
                    case 7:
                        if (token.equals("character-bank")) {
                            p.bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                            p.bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                        }
                        break;
                    case 8:
                        if (token.equals("character-friend")) {
                            p.friends[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
                        }
                        break;
                    case 9:
                        /* if (token.equals("character-ignore")) {
						ignores[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
					} */
                        break;


                }
            } else {
                if (line.equals("[ACCOUNT]")) {
                    ReadMode = 1;
                } else if (line.equals("[CHARACTER]")) {
                    ReadMode = 2;
                } else if (line.equals("[AGILITY]")) {
                    ReadMode = 2;
                } else if (line.equals("[EQUIPMENT]")) {
                    ReadMode = 3;
                } else if (line.equals("[LOOK]")) {
                    ReadMode = 4;
                } else if (line.equals("[SKILLS]")) {
                    ReadMode = 5;
                } else if (line.equals("[ITEMS]")) {
                    ReadMode = 6;
                } else if (line.equals("[BANK]")) {
                    ReadMode = 7;
                } else if (line.equals("[FRIENDS]")) {
                    ReadMode = 8;
                } else if (line.equals("[IGNORES]")) {
                    ReadMode = 9;
                } else if (line.equals("[STORED]")) {
                    ReadMode = 20;
                } else if (line.equals("[OCCUPY]")) {
                    ReadMode = 21;
                } else if (line.equals("[EOF]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {}
                    return 1;
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {}
        return 13;
    }


    /**
     *Saving
     **/
    public static boolean saveGame3(Client p) {
        if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
            //System.out.println("first");
            return false;
        }
        if (p.playerName == null || Server.playerHandler.players[p.playerId] == null) {
            //System.out.println("second");
            return false;
        }
        p.playerName = p.playerName2;
        int tbTime = (int)(p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
        if (tbTime > 500000 || tbTime < 0) {
            tbTime = 0;
        }

        BufferedWriter characterfile = null;
        try {
            characterfile = new BufferedWriter(new FileWriter("C:/Users/Administrator/Dropbox/Public/CharacterfilesSave/characters/" + p.playerName + ".txt"));

            /*ACCOUNT*/
            characterfile.write("[ACCOUNT]", 0, 9);
            characterfile.newLine();
            characterfile.write("character-username = ", 0, 21);
            characterfile.write(p.playerName, 0, p.playerName.length());
            characterfile.newLine();
            characterfile.write("character-password = ", 0, 21);
            characterfile.write(p.playerPass, 0, p.playerPass.length());
            characterfile.newLine();
            characterfile.newLine();

            /*CHARACTER*/
            characterfile.write("[CHARACTER]", 0, 11);
            characterfile.newLine();
            characterfile.write("character-height = ", 0, 19);
            characterfile.write(Integer.toString(p.heightLevel), 0, Integer.toString(p.heightLevel).length());
            characterfile.newLine();
            characterfile.write("VLS-hits = ");
            characterfile.write(Integer.toString(p.degradeTime), 0, Integer.toString(p.degradeTime).length());
            characterfile.newLine();
            characterfile.write("vesta pl8body = ");
            characterfile.write(Integer.toString(p.vestaplatedegrade), 0, Integer.toString(p.vestaplatedegrade).length());
            characterfile.newLine();
            characterfile.write("character-posx = ", 0, 17);
            characterfile.write(Integer.toString(p.absX), 0, Integer.toString(p.absX).length());
            characterfile.newLine();
            characterfile.write("character-posy = ", 0, 17);
            characterfile.write(Integer.toString(p.absY), 0, Integer.toString(p.absY).length());
            characterfile.newLine();
			characterfile.write("loyalty-rank = ", 0, 15);
			characterfile.write(Integer.toString(p.loyaltyRank), 0, Integer.toString(p.loyaltyRank).length());
			characterfile.newLine();
            characterfile.write("character-rights = ", 0, 19);
            characterfile.write(Integer.toString(p.playerRights), 0, Integer.toString(p.playerRights).length());
            characterfile.newLine();
            for (int i = 0; i < p.lastConnectedFrom.size(); i++) {
                characterfile.write("connected-from = ", 0, 17);
                characterfile.write(p.lastConnectedFrom.get(i), 0, p.lastConnectedFrom.get(i).length());
                characterfile.newLine();
            }
            characterfile.write("cw-games = ", 0, 11);
            characterfile.write(Integer.toString(p.cwGames), 0, Integer.toString(p.cwGames).length());
            characterfile.newLine();
            characterfile.write("crystal-bow-shots = ", 0, 20);
            characterfile.write(Integer.toString(p.crystalBowArrowCount), 0, Integer.toString(p.crystalBowArrowCount).length());
            characterfile.newLine();

            characterfile.write("gotStarter = ", 0, 13);
            characterfile.write(Integer.toString(p.gotStarter), 0, Integer.toString(p.gotStarter).length());
            characterfile.newLine();

            characterfile.write("skull-timer = ", 0, 14);
            characterfile.write(Integer.toString(p.skullTimer), 0, Integer.toString(p.skullTimer).length());
            characterfile.newLine();
            characterfile.write("magic-book = ", 0, 13);
            characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer.toString(p.playerMagicBook).length());
            characterfile.newLine();
            for (int b = 0; b < p.barrowsNpcs.length; b++) {
                characterfile.write("brother-info = ", 0, 15);
                characterfile.write(Integer.toString(b), 0, Integer.toString(b).length());
                characterfile.write("	", 0, 1);
                characterfile.write(p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0) : Integer.toString(p.barrowsNpcs[b][1]), 0, Integer.toString(p.barrowsNpcs[b][1]).length());
                characterfile.newLine();
            }
            characterfile.write("special-amount = ", 0, 17);
            characterfile.write(Double.toString(p.specAmount), 0, Double.toString(p.specAmount).length());
            characterfile.newLine();
            characterfile.write("selected-coffin = ", 0, 18);
            characterfile.write(Integer.toString(p.randomCoffin), 0, Integer.toString(p.randomCoffin).length());
            characterfile.newLine();
            characterfile.write("barrows-killcount = ", 0, 20);
            characterfile.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
            characterfile.newLine();
            characterfile.write("teleblock-length = ", 0, 19);
            characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
            characterfile.newLine();
            characterfile.write("pc-points = ", 0, 12);
            characterfile.write(Integer.toString(p.pcPoints), 0, Integer.toString(p.pcPoints).length());
            characterfile.newLine();
            characterfile.write("votePoints = ", 0, 13);
            characterfile.write(Integer.toString(p.votePoints), 0, Integer.toString(p.votePoints).length());
            characterfile.newLine();
            characterfile.write("pePoints = ", 0, 13);
            characterfile.write(Integer.toString(p.pePoints), 0, Integer.toString(p.pePoints).length());
            characterfile.newLine();
            characterfile.write("slayerTask = ", 0, 13);
            characterfile.write(Integer.toString(p.slayerTask), 0, Integer.toString(p.slayerTask).length());
            characterfile.newLine();
            characterfile.write("taskAmount = ", 0, 13);
            characterfile.write(Integer.toString(p.taskAmount), 0, Integer.toString(p.taskAmount).length());
            characterfile.newLine();
            characterfile.write("magePoints = ", 0, 13);
            characterfile.write(Integer.toString(p.magePoints), 0, Integer.toString(p.magePoints).length());
            characterfile.newLine();
            characterfile.write("donatorPoints = ", 0, 16);
            characterfile.write(Integer.toString(p.donatorPoints), 0, Integer.toString(p.donatorPoints).length());
            characterfile.newLine();
            characterfile.write("character-longsword = ", 0, 22);
            characterfile.write(Integer.toString(p.vlsLeft), 0, Integer.toString(p.vlsLeft).length());
            characterfile.newLine();

            characterfile.write("Forinthry-brace = ", 0, 18);
            characterfile.write(Integer.toString(p.bracelet1), 0, Integer.toString(p.bracelet1).length());
            characterfile.newLine();

            characterfile.write("Forinthry-brace2 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet2), 0, Integer.toString(p.bracelet2).length());
            characterfile.newLine();
            characterfile.write("Forinthry-brace3 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet3), 0, Integer.toString(p.bracelet3).length());
            characterfile.newLine();
            characterfile.write("Forinthry-brace4 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet4), 0, Integer.toString(p.bracelet4).length());
            characterfile.newLine();
            characterfile.write("Forinthry-brace5 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet5), 0, Integer.toString(p.bracelet5).length());
            characterfile.newLine();
            characterfile.write("character-warhammer = ", 0, 22);
            characterfile.write(Integer.toString(p.statLeft), 0, Integer.toString(p.statLeft).length());
            characterfile.newLine();
            characterfile.write("character-spear = ", 0, 18);
            characterfile.write(Integer.toString(p.vSpearLeft), 0, Integer.toString(p.vSpearLeft).length());
            characterfile.newLine();
            characterfile.write("character-chainbody = ", 0, 22);
            characterfile.write(Integer.toString(p.vTopLeft), 0, Integer.toString(p.vTopLeft).length());
            characterfile.newLine();
            characterfile.write("character-chainskirt = ", 0, 23);
            characterfile.write(Integer.toString(p.vLegsLeft), 0, Integer.toString(p.vLegsLeft).length());
            characterfile.newLine();
            characterfile.write("character-full helm = ", 0, 22);
            characterfile.write(Integer.toString(p.sHelmLeft), 0, Integer.toString(p.sHelmLeft).length());
            characterfile.newLine();
            characterfile.write("character-platebody = ", 0, 22);
            characterfile.write(Integer.toString(p.sTopLeft), 0, Integer.toString(p.sTopLeft).length());
            characterfile.newLine();
            characterfile.write("character-platelegs = ", 0, 22);
            characterfile.write(Integer.toString(p.sLegsLeft), 0, Integer.toString(p.sLegsLeft).length());
            characterfile.newLine();
            characterfile.write("character-hood = ", 0, 17);
            characterfile.write(Integer.toString(p.zHoodLeft), 0, Integer.toString(p.zHoodLeft).length());
            characterfile.newLine();
            characterfile.write("character-staff = ", 0, 18);
            characterfile.write(Integer.toString(p.zStaffLeft), 0, Integer.toString(p.zStaffLeft).length());
            characterfile.newLine();
            characterfile.write("character-robe top = ", 0, 21);
            characterfile.write(Integer.toString(p.zTopLeft), 0, Integer.toString(p.zTopLeft).length());
            characterfile.newLine();
            characterfile.write("character-robe bottom = ", 0, 24);
            characterfile.write(Integer.toString(p.zBottomLeft), 0, Integer.toString(p.zBottomLeft).length());
            characterfile.newLine();

            characterfile.write("character-cLong = ", 0, 18);
            characterfile.write(Integer.toString(p.chaoticLong), 0, Integer.toString(p.chaoticLong).length());
            characterfile.newLine();

            characterfile.write("character-cShield = ", 0, 20);
            characterfile.write(Integer.toString(p.chaoticShield), 0, Integer.toString(p.chaoticShield).length());
            characterfile.newLine();

            characterfile.write("character-cStaff = ", 0, 19);
            characterfile.write(Integer.toString(p.chaoticStaff), 0, Integer.toString(p.chaoticStaff).length());
            characterfile.newLine();
            characterfile.write("character-cBow = ", 0, 17);
            characterfile.write(Integer.toString(p.chaoticBow), 0, Integer.toString(p.chaoticBow).length());
            characterfile.newLine();
            characterfile.write("character-cMaul = ", 0, 18);
            characterfile.write(Integer.toString(p.chaoticMaul), 0, Integer.toString(p.chaoticMaul).length());
            characterfile.newLine();
            characterfile.write("character-cRapier = ", 0, 20);
            characterfile.write(Integer.toString(p.chaoticRapier), 0, Integer.toString(p.chaoticRapier).length());
            characterfile.newLine();
            characterfile.write("gwdelay = ", 0, 10);
            characterfile.write(Integer.toString(p.gwdelay), 0, Integer.toString(p.gwdelay).length());
            characterfile.newLine();
            characterfile.write("Arma-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Arma), 0, Integer.toString(p.Arma).length());
            characterfile.newLine();
            characterfile.write("Band-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Band), 0, Integer.toString(p.Band).length());
            characterfile.newLine();
            characterfile.write("Zammy-KC = ", 0, 11);
            characterfile.write(Integer.toString(p.Zammy), 0, Integer.toString(p.Zammy).length());
            characterfile.newLine();
            characterfile.write("Sara-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Sara), 0, Integer.toString(p.Sara).length());
            characterfile.newLine();
            characterfile.write("clickedHome = ", 0, 14);
            characterfile.write(Integer.toString(p.clickedHome), 0, Integer.toString(p.clickedHome).length());
            characterfile.newLine();
            characterfile.write("Homex = ", 0, 8);
            characterfile.write(Integer.toString(p.playerHomeX), 0, Integer.toString(p.playerHomeX).length());
            characterfile.newLine();
            characterfile.write("Homey = ", 0, 8);
            characterfile.write(Integer.toString(p.playerHomeY), 0, Integer.toString(p.playerHomeY).length());
            characterfile.newLine();
            characterfile.write("Frost-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Frost), 0, Integer.toString(p.Frost).length());
            characterfile.newLine();
            characterfile.write("character-leather body = ", 0, 25);
            characterfile.write(Integer.toString(p.mBodyLeft), 0, Integer.toString(p.mBodyLeft).length());
            characterfile.newLine();
            characterfile.write("character-chaps = ", 0, 18);
            characterfile.write(Integer.toString(p.mChapsLeft), 0, Integer.toString(p.mChapsLeft).length());
            characterfile.newLine();
            characterfile.write("autoRet = ", 0, 10);
            characterfile.write(Integer.toString(p.autoRet), 0, Integer.toString(p.autoRet).length());
            characterfile.newLine();
            characterfile.write("barrowskillcount = ", 0, 19);
            characterfile.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
            characterfile.newLine();
            characterfile.write("flagged = ", 0, 10);
            characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean.toString(p.accountFlagged).length());
            characterfile.newLine();
            characterfile.write("wave = ", 0, 7);
            characterfile.write(Integer.toString(p.waveId), 0, Integer.toString(p.waveId).length());
            characterfile.newLine();
            characterfile.write("gwkc = ", 0, 7);
            characterfile.write(Integer.toString(p.killCount), 0, Integer.toString(p.killCount).length());
            characterfile.newLine();
            characterfile.write("fightMode = ", 0, 12);
            characterfile.write(Integer.toString(p.fightMode), 0, Integer.toString(p.fightMode).length());
            characterfile.newLine();
            characterfile.write("void = ", 0, 7);
            String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t" + p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t" + p.voidStatus[4];
            characterfile.write(toWrite);
            characterfile.newLine();
            characterfile.write("tradeTimer = ", 0, 13);
            characterfile.write(Integer.toString(p.tradeTimer), 0, Integer.toString(p.tradeTimer).length());
            characterfile.newLine();
            characterfile.write("pkPoints = ", 0, 11);
            characterfile.write(Integer.toString(p.pkPoints), 0, Integer.toString(p.pkPoints).length());
            characterfile.newLine();
            characterfile.write("memberStatus = ", 0, 15);
            characterfile.write(Integer.toString(p.memberStatus), 0, Integer.toString(p.memberStatus).length());
            characterfile.newLine();
            characterfile.write("firstMember = ", 0, 14);
            characterfile.write(Integer.toString(p.firstMember), 0, Integer.toString(p.firstMember).length());
            characterfile.newLine();
            characterfile.write("isDonator = ", 0, 12);
            characterfile.write(Integer.toString(p.isDonator), 0, Integer.toString(p.isDonator).length());
            characterfile.newLine();
            characterfile.newLine();

            /*EQUIPMENT*/
            characterfile.write("[EQUIPMENT]", 0, 11);
            characterfile.newLine();
            for (int i = 0; i < p.playerEquipment.length; i++) {
                characterfile.write("character-equip = ", 0, 18);
                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerEquipment[i]), 0, Integer.toString(p.playerEquipment[i]).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0, Integer.toString(p.playerEquipmentN[i]).length());
                characterfile.write("	", 0, 1);
                characterfile.newLine();
            }
            characterfile.newLine();

            /*LOOK*/
            characterfile.write("[LOOK]", 0, 6);
            characterfile.newLine();
            for (int i = 0; i < p.playerAppearance.length; i++) {
                characterfile.write("character-look = ", 0, 17);
                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerAppearance[i]), 0, Integer.toString(p.playerAppearance[i]).length());
                characterfile.newLine();
            }
            characterfile.newLine();

            /*SKILLS*/
            characterfile.write("[SKILLS]", 0, 8);
            characterfile.newLine();
            for (int i = 0; i < p.playerLevel.length; i++) {
                characterfile.write("character-skill = ", 0, 18);
                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerLevel[i]), 0, Integer.toString(p.playerLevel[i]).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer.toString(p.playerXP[i]).length());
                characterfile.newLine();
            }
            characterfile.newLine();

            /*ITEMS*/
            characterfile.write("[ITEMS]", 0, 7);
            characterfile.newLine();
            for (int i = 0; i < p.playerItems.length; i++) {
                if (p.playerItems[i] > 0) {
                    characterfile.write("character-item = ", 0, 17);
                    characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.playerItems[i]), 0, Integer.toString(p.playerItems[i]).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.playerItemsN[i]), 0, Integer.toString(p.playerItemsN[i]).length());
                    characterfile.newLine();
                }
            }
            characterfile.newLine();

            /*BANK*/
            characterfile.write("[BANK]", 0, 6);
            characterfile.newLine();
            for (int i = 0; i < p.bankItems.length; i++) {
                if (p.bankItems[i] > 0) {
                    characterfile.write("character-bank = ", 0, 17);
                    characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.bankItems[i]), 0, Integer.toString(p.bankItems[i]).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.bankItemsN[i]), 0, Integer.toString(p.bankItemsN[i]).length());
                    characterfile.newLine();
                }
            }
            characterfile.newLine();

            /*FRIENDS*/
            characterfile.write("[FRIENDS]", 0, 9);
            characterfile.newLine();
            for (int i = 0; i < p.friends.length; i++) {
                if (p.friends[i] > 0) {
                    characterfile.write("character-friend = ", 0, 19);
                    characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write("" + p.friends[i]);
                    characterfile.newLine();
                }
            }
            characterfile.newLine();



            /*IGNORES*/
            /*characterfile.write("[IGNORES]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < ignores.length; i++) {
				if (ignores[i] > 0) {
					characterfile.write("character-ignore = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Long.toString(ignores[i]), 0, Long.toString(ignores[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();*/
            /*EOF*/
            characterfile.write("[EOF]", 0, 5);
            characterfile.newLine();
            characterfile.newLine();
            characterfile.close();
        } catch (IOException ioexception) {
            Misc.println(p.playerName + ": error writing file.");
            return false;
        }
        return true;
    }


    /**
     *Saving
     **/
    public static boolean saveGame(Client p) {
        if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
            //System.out.println("first");
            return false;
        }
        if (p.playerName == null || Server.playerHandler.players[p.playerId] == null) {
            //System.out.println("second");
            return false;
        }
        p.playerName = p.playerName2;
        int tbTime = (int)(p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
        if (tbTime > 500000 || tbTime < 0) {
            tbTime = 0;
        }

        BufferedWriter characterfile = null;
        try {
            characterfile = new BufferedWriter(new FileWriter("./Data/characters/" + p.playerName + ".txt"));

            /*ACCOUNT*/
            characterfile.write("[ACCOUNT]", 0, 9);
            characterfile.newLine();
            characterfile.write("character-username = ", 0, 21);
            characterfile.write(p.playerName, 0, p.playerName.length());
            characterfile.newLine();
            characterfile.write("character-password = ", 0, 21);
            characterfile.write(p.playerPass, 0, p.playerPass.length());
            characterfile.newLine();
            characterfile.newLine();

            /*CHARACTER*/
            characterfile.write("[CHARACTER]", 0, 11);
            characterfile.newLine();
            characterfile.write("character-height = ", 0, 19);
            characterfile.write(Integer.toString(p.heightLevel), 0, Integer.toString(p.heightLevel).length());
            characterfile.newLine();
            characterfile.write("Join-Date = ", 0, 12);
            characterfile.write(p.joinDate, 0, p.joinDate.length());
            characterfile.newLine();
            characterfile.write("character-posx = ", 0, 17);
            characterfile.write(Integer.toString(p.absX), 0, Integer.toString(p.absX).length());
            characterfile.newLine();
            characterfile.write("character-posy = ", 0, 17);
            characterfile.write(Integer.toString(p.absY), 0, Integer.toString(p.absY).length());
            characterfile.newLine();
            characterfile.write("character-rights = ", 0, 19);
            characterfile.write(Integer.toString(p.playerRights), 0, Integer.toString(p.playerRights).length());
            characterfile.newLine();
            for (int i = 0; i < p.lastConnectedFrom.size(); i++) {
                characterfile.write("connected-from = ", 0, 17);
                characterfile.write(p.lastConnectedFrom.get(i), 0, p.lastConnectedFrom.get(i).length());
                characterfile.newLine();
            }
            characterfile.write("AchievementPoints = ", 0, 20);
            characterfile.write(Integer.toString(p.achievementPoints), 0, Integer.toString(p.achievementPoints).length());
            characterfile.newLine();	
            characterfile.write("KillingPoints = ", 0, 16);
            characterfile.write(Integer.toString(p.KillingPoints), 0, Integer.toString(p.KillingPoints).length());
            characterfile.newLine();							
            characterfile.write("KC = ", 0, 5);
            characterfile.write(Integer.toString(p.KC), 0, Integer.toString(p.KC).length());
            characterfile.newLine();
            characterfile.write("DC = ", 0, 5);
            characterfile.write(Integer.toString(p.DC), 0, Integer.toString(p.DC).length());
            characterfile.newLine();
            characterfile.write("KillStreak = ", 0, 13);
            characterfile.write(Integer.toString(p.killStreak), 0, Integer.toString(p.killStreak).length());
            characterfile.newLine();
            for (int j = 0; j < p.lastKilledPlayers.size(); j++) {
                characterfile.write("Killed-Players = ", 0, 17);
                characterfile.write(p.lastKilledPlayers.get(j), 0, p.lastKilledPlayers.get(j).length());
                characterfile.newLine();
            }
			characterfile.write("character-yellTag = ", 0, 20);
			characterfile.write(p.customYellTag, 0, p.customYellTag.length());
			characterfile.newLine();			
            characterfile.write("ClanName = ");
            characterfile.write(p.clanName);
            characterfile.newLine();
            characterfile.write("ClanId = ", 0, 9);
            characterfile.write(Integer.toString(p.clanId), 0, Integer.toString(p.clanId).length());
            characterfile.newLine();
            characterfile.write("ChatId = ", 0, 9);
            characterfile.write(Integer.toString(p.chatId), 0, Integer.toString(p.chatId).length());
            characterfile.newLine();
            characterfile.write("HasAClan = ", 0, 11);
            characterfile.write(Boolean.toString(p.hasAClan), 0, Boolean.toString(p.hasAClan).length());
            characterfile.newLine();
            characterfile.write("tempid=", 0, 7);
            characterfile.write(Integer.toString(p.tempid), 0, Integer.toString(p.tempid).length());
            characterfile.newLine();
            characterfile.write("tempamt=", 0, 8);
            characterfile.write(Integer.toString(p.tempamt), 0, Integer.toString(p.tempamt).length());
            characterfile.newLine();
            characterfile.write("tempprice=", 0, 10);
            characterfile.write(Integer.toString(p.tempprice), 0, Integer.toString(p.tempprice).length());
            characterfile.newLine();
            characterfile.write("crystal-bow-shots = ", 0, 20);
            characterfile.write(Integer.toString(p.crystalBowArrowCount), 0, Integer.toString(p.crystalBowArrowCount).length());
            characterfile.newLine();
            characterfile.write("gotStarter = ", 0, 13);
            characterfile.write(Integer.toString(p.gotStarter), 0, Integer.toString(p.gotStarter).length());
            characterfile.newLine();
            characterfile.write("skull-timer = ", 0, 14);
            characterfile.write(Integer.toString(p.skullTimer), 0, Integer.toString(p.skullTimer).length());
            characterfile.newLine();
            characterfile.write("prayr-book = ", 0, 13);
            characterfile.write(Integer.toString(p.playerPrayerBook), 0, Integer.toString(p.playerPrayerBook).length());
            characterfile.newLine();
            characterfile.write("PRAYFUCK = ", 0, 11);
            characterfile.write(Boolean.toString(p.PRAYFUCK), 0, Boolean.toString(p.PRAYFUCK).length());
            characterfile.newLine();
            characterfile.write("magic-book = ", 0, 13);
            characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer.toString(p.playerMagicBook).length());
            characterfile.newLine();
            for (int b = 0; b < p.barrowsNpcs.length; b++) {
                characterfile.write("brother-info = ", 0, 15);
                characterfile.write(Integer.toString(b), 0, Integer.toString(b).length());
                characterfile.write("	", 0, 1);
                characterfile.write(p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0) : Integer.toString(p.barrowsNpcs[b][1]), 0, Integer.toString(p.barrowsNpcs[b][1]).length());
                characterfile.newLine();
            }
            characterfile.write("special-amount = ", 0, 17);
            characterfile.write(Double.toString(p.specAmount), 0, Double.toString(p.specAmount).length());
            characterfile.newLine();
            characterfile.write("selected-coffin = ", 0, 18);
            characterfile.write(Integer.toString(p.randomCoffin), 0, Integer.toString(p.randomCoffin).length());
            characterfile.newLine();
            characterfile.write("barrows-killcount = ", 0, 20);
            characterfile.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
            characterfile.newLine();
            characterfile.write("teleblock-length = ", 0, 19);
            characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
            characterfile.newLine();
            characterfile.write("pc-points = ", 0, 12);
            characterfile.write(Integer.toString(p.pcPoints), 0, Integer.toString(p.pcPoints).length());
            characterfile.newLine();	
            characterfile.write("SoFpoints = ", 0, 12);
            characterfile.write(Integer.toString(p.SoFpoints), 0, Integer.toString(p.SoFpoints).length());
            characterfile.newLine();						
            characterfile.write("votePoints = ", 0, 13);
            characterfile.write(Integer.toString(p.votePoints), 0, Integer.toString(p.votePoints).length());
            characterfile.newLine();
            characterfile.write("slayerTask = ", 0, 13);
            characterfile.write(Integer.toString(p.slayerTask), 0, Integer.toString(p.slayerTask).length());
            characterfile.newLine();
            characterfile.write("taskAmount = ", 0, 13);
            characterfile.write(Integer.toString(p.taskAmount), 0, Integer.toString(p.taskAmount).length());
            characterfile.newLine();
            characterfile.write("magePoints = ", 0, 13);
            characterfile.write(Integer.toString(p.magePoints), 0, Integer.toString(p.magePoints).length());
            characterfile.newLine();
            characterfile.write("donatorPoints = ", 0, 16);
            characterfile.write(Integer.toString(p.donatorPoints), 0, Integer.toString(p.donatorPoints).length());
            characterfile.newLine();
            characterfile.write("Frost-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Frost), 0, Integer.toString(p.Frost).length());
            characterfile.newLine();
            characterfile.write("bhenter = ", 0, 10);
            characterfile.write(Integer.toString(p.enterBHTime), 0, Integer.toString(p.enterBHTime).length());
            characterfile.newLine();
            characterfile.write("bhleave = ", 0, 10);
            characterfile.write(Integer.toString(p.leavePenalty), 0, Integer.toString(p.leavePenalty).length());
            characterfile.newLine();
            characterfile.write("bhpickup = ", 0, 11);
            characterfile.write(Integer.toString(p.pickupPenalty), 0, Integer.toString(p.pickupPenalty).length());
            characterfile.newLine();
            characterfile.write("bhcash = ", 0, 9);
            characterfile.write(Integer.toString(p.bountyCash), 0, Integer.toString(p.bountyCash).length());
            characterfile.newLine();
            characterfile.write("bhcrater = ", 0, 11);
            characterfile.write(Integer.toString(p.publicCrater), 0, Integer.toString(p.publicCrater).length());
            characterfile.newLine();
            characterfile.write("bhrogue = ", 0, 10);
            characterfile.write(Integer.toString(p.bountyCash), 0, Integer.toString(p.bountyCash).length());
            characterfile.newLine();
            characterfile.write("bhtarget = ", 0, 11);
            characterfile.write(Integer.toString(p.publicCrater), 0, Integer.toString(p.publicCrater).length());
            characterfile.newLine();
            characterfile.write("character-longsword = ", 0, 22);
            characterfile.write(Integer.toString(p.vlsLeft), 0, Integer.toString(p.vlsLeft).length());
            characterfile.newLine();

            characterfile.write("Forinthry-brace = ", 0, 18);
            characterfile.write(Integer.toString(p.bracelet1), 0, Integer.toString(p.bracelet1).length());
            characterfile.newLine();

            characterfile.write("Forinthry-brace2 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet2), 0, Integer.toString(p.bracelet2).length());
            characterfile.newLine();
            characterfile.write("Forinthry-brace3 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet3), 0, Integer.toString(p.bracelet3).length());
            characterfile.newLine();
            characterfile.write("Forinthry-brace4 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet4), 0, Integer.toString(p.bracelet4).length());
            characterfile.newLine();
            characterfile.write("clickedHome = ", 0, 14);
            characterfile.write(Integer.toString(p.clickedHome), 0, Integer.toString(p.clickedHome).length());
            characterfile.newLine();
            characterfile.write("Homex = ", 0, 8);
            characterfile.write(Integer.toString(p.playerHomeX), 0, Integer.toString(p.playerHomeX).length());
            characterfile.newLine();
            characterfile.write("Homey = ", 0, 8);
            characterfile.write(Integer.toString(p.playerHomeY), 0, Integer.toString(p.playerHomeY).length());
            characterfile.newLine();
            characterfile.write("Forinthry-brace5 = ", 0, 19);
            characterfile.write(Integer.toString(p.bracelet5), 0, Integer.toString(p.bracelet5).length());
            characterfile.newLine();
            characterfile.write("character-warhammer = ", 0, 22);
            characterfile.write(Integer.toString(p.statLeft), 0, Integer.toString(p.statLeft).length());
            characterfile.newLine();
            characterfile.write("character-spear = ", 0, 18);
            characterfile.write(Integer.toString(p.vSpearLeft), 0, Integer.toString(p.vSpearLeft).length());
            characterfile.newLine();
            characterfile.write("character-chainbody = ", 0, 22);
            characterfile.write(Integer.toString(p.vTopLeft), 0, Integer.toString(p.vTopLeft).length());
            characterfile.newLine();
            characterfile.write("character-chainskirt = ", 0, 23);
            characterfile.write(Integer.toString(p.vLegsLeft), 0, Integer.toString(p.vLegsLeft).length());
            characterfile.newLine();
            characterfile.write("character-full helm = ", 0, 22);
            characterfile.write(Integer.toString(p.sHelmLeft), 0, Integer.toString(p.sHelmLeft).length());
            characterfile.newLine();
            characterfile.write("character-platebody = ", 0, 22);
            characterfile.write(Integer.toString(p.sTopLeft), 0, Integer.toString(p.sTopLeft).length());
            characterfile.newLine();
            characterfile.write("character-platelegs = ", 0, 22);
            characterfile.write(Integer.toString(p.sLegsLeft), 0, Integer.toString(p.sLegsLeft).length());
            characterfile.newLine();
            characterfile.write("character-hood = ", 0, 17);
            characterfile.write(Integer.toString(p.zHoodLeft), 0, Integer.toString(p.zHoodLeft).length());
            characterfile.newLine();
            characterfile.write("character-staff = ", 0, 18);
            characterfile.write(Integer.toString(p.zStaffLeft), 0, Integer.toString(p.zStaffLeft).length());
            characterfile.newLine();
            characterfile.write("character-robe top = ", 0, 21);
            characterfile.write(Integer.toString(p.zTopLeft), 0, Integer.toString(p.zTopLeft).length());
            characterfile.newLine();
            characterfile.write("character-robe bottom = ", 0, 24);
            characterfile.write(Integer.toString(p.zBottomLeft), 0, Integer.toString(p.zBottomLeft).length());
            characterfile.newLine();

            characterfile.write("character-cLong = ", 0, 18);
            characterfile.write(Integer.toString(p.chaoticLong), 0, Integer.toString(p.chaoticLong).length());
            characterfile.newLine();

            characterfile.write("character-cShield = ", 0, 20);
            characterfile.write(Integer.toString(p.chaoticShield), 0, Integer.toString(p.chaoticShield).length());
            characterfile.newLine();

            characterfile.write("character-cStaff = ", 0, 19);
            characterfile.write(Integer.toString(p.chaoticStaff), 0, Integer.toString(p.chaoticStaff).length());
            characterfile.newLine();
            characterfile.write("character-cBow = ", 0, 17);
            characterfile.write(Integer.toString(p.chaoticBow), 0, Integer.toString(p.chaoticBow).length());
            characterfile.newLine();
            characterfile.write("character-cMaul = ", 0, 18);
            characterfile.write(Integer.toString(p.chaoticMaul), 0, Integer.toString(p.chaoticMaul).length());
            characterfile.newLine();
            characterfile.write("character-cRapier = ", 0, 20);
            characterfile.write(Integer.toString(p.chaoticRapier), 0, Integer.toString(p.chaoticRapier).length());
            characterfile.newLine();
            characterfile.write("gwdelay = ", 0, 10);
            characterfile.write(Integer.toString(p.gwdelay), 0, Integer.toString(p.gwdelay).length());
            characterfile.newLine();
            characterfile.write("Arma-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Arma), 0, Integer.toString(p.Arma).length());
            characterfile.newLine();
            characterfile.write("Band-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Band), 0, Integer.toString(p.Band).length());
            characterfile.newLine();
            characterfile.write("Zammy-KC = ", 0, 11);
            characterfile.write(Integer.toString(p.Zammy), 0, Integer.toString(p.Zammy).length());
            characterfile.newLine();
            characterfile.write("Sara-KC = ", 0, 10);
            characterfile.write(Integer.toString(p.Sara), 0, Integer.toString(p.Sara).length());
            characterfile.newLine();

            characterfile.write("Monster-Points = ", 0, 17);
            characterfile.write(Integer.toString(p.Frost), 0, Integer.toString(p.Frost).length());
            characterfile.newLine();
            characterfile.write("character-leather body = ", 0, 25);
            characterfile.write(Integer.toString(p.mBodyLeft), 0, Integer.toString(p.mBodyLeft).length());
            characterfile.newLine();
            characterfile.write("character-chaps = ", 0, 18);
            characterfile.write(Integer.toString(p.mChapsLeft), 0, Integer.toString(p.mChapsLeft).length());
            characterfile.newLine();
            characterfile.write("autoRet = ", 0, 10);
            characterfile.write(Integer.toString(p.autoRet), 0, Integer.toString(p.autoRet).length());
            characterfile.newLine();
            characterfile.write("barrowskillcount = ", 0, 19);
            characterfile.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
            characterfile.newLine();
            characterfile.write("flagged = ", 0, 10);
            characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean.toString(p.accountFlagged).length());
            characterfile.newLine();
            characterfile.write("wave = ", 0, 7);
            characterfile.write(Integer.toString(p.waveId), 0, Integer.toString(p.waveId).length());
            characterfile.newLine();
            characterfile.write("gwkc = ", 0, 7);
            characterfile.write(Integer.toString(p.killCount), 0, Integer.toString(p.killCount).length());
            characterfile.newLine();
            characterfile.write("fightMode = ", 0, 12);
            characterfile.write(Integer.toString(p.fightMode), 0, Integer.toString(p.fightMode).length());
            characterfile.newLine();
            characterfile.write("void = ", 0, 7);
            String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t" + p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t" + p.voidStatus[4];
            characterfile.write(toWrite);
            characterfile.newLine();
            characterfile.write("tradeTimer = ", 0, 13);
            characterfile.write(Integer.toString(p.tradeTimer), 0, Integer.toString(p.tradeTimer).length());
            characterfile.newLine();
            characterfile.write("memberStatus = ", 0, 15);
            characterfile.write(Integer.toString(p.memberStatus), 0, Integer.toString(p.memberStatus).length());
            characterfile.newLine();
            characterfile.write("firstMember = ", 0, 14);
            characterfile.write(Integer.toString(p.firstMember), 0, Integer.toString(p.firstMember).length());
            characterfile.newLine();
            characterfile.write("isDonator = ", 0, 12);
            characterfile.write(Integer.toString(p.isDonator), 0, Integer.toString(p.isDonator).length());
            characterfile.newLine();
            characterfile.write("NoMoreInterface = ", 0, 18);
            characterfile.write(Boolean.toString(p.NoMoreInterface), 0, Boolean.toString(p.NoMoreInterface).length());			
            characterfile.newLine();
            characterfile.write("[ACHIEVEMENTS]", 0, 14);
            characterfile.newLine();
            characterfile.write("GettingReward = ", 0, 16);
            characterfile.write(Boolean.toString(p.GettingReward), 0, Boolean.toString(p.GettingReward).length());
            characterfile.newLine();			
            characterfile.write("LotteryEnter = ", 0, 15);
            characterfile.write(Boolean.toString(p.LotteryEnter), 0, Boolean.toString(p.LotteryEnter).length());
            characterfile.newLine();
            characterfile.write("LotteryDone = ", 0, 14);
            characterfile.write(Boolean.toString(p.LotteryDone), 0, Boolean.toString(p.LotteryDone).length());
            characterfile.newLine();	
            characterfile.write("TutorialStart = ", 0, 16);
            characterfile.write(Boolean.toString(p.TutorialStart), 0, Boolean.toString(p.TutorialStart).length());
            characterfile.newLine();
            characterfile.write("TutorialDone = ", 0, 15);
            characterfile.write(Boolean.toString(p.TutorialDone), 0, Boolean.toString(p.TutorialDone).length());
            characterfile.newLine();	
            characterfile.write("EarnedRareItem = ", 0, 17);
            characterfile.write(Boolean.toString(p.EarnedRareItem), 0, Boolean.toString(p.EarnedRareItem).length());
            characterfile.newLine();				
            characterfile.newLine();

            /*EQUIPMENT*/
            characterfile.write("[EQUIPMENT]", 0, 11);
            characterfile.newLine();
            for (int i = 0; i < p.playerEquipment.length; i++) {
                characterfile.write("character-equip = ", 0, 18);
                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerEquipment[i]), 0, Integer.toString(p.playerEquipment[i]).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0, Integer.toString(p.playerEquipmentN[i]).length());
                characterfile.write("	", 0, 1);
                characterfile.newLine();
            }
            characterfile.newLine();

            /*LOOK*/
            characterfile.write("[LOOK]", 0, 6);
            characterfile.newLine();
            for (int i = 0; i < p.playerAppearance.length; i++) {
                characterfile.write("character-look = ", 0, 17);
                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerAppearance[i]), 0, Integer.toString(p.playerAppearance[i]).length());
                characterfile.newLine();
            }
            characterfile.newLine();

            /*SKILLS*/
            characterfile.write("[SKILLS]", 0, 8);
            characterfile.newLine();
            for (int i = 0; i < p.playerLevel.length; i++) {
                characterfile.write("character-skill = ", 0, 18);
                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerLevel[i]), 0, Integer.toString(p.playerLevel[i]).length());
                characterfile.write("	", 0, 1);
                characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer.toString(p.playerXP[i]).length());
                characterfile.newLine();
            }
            characterfile.newLine();

            /*ITEMS*/
            characterfile.write("[ITEMS]", 0, 7);
            characterfile.newLine();
            for (int i = 0; i < p.playerItems.length; i++) {
                if (p.playerItems[i] > 0) {
                    characterfile.write("character-item = ", 0, 17);
                    characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.playerItems[i]), 0, Integer.toString(p.playerItems[i]).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.playerItemsN[i]), 0, Integer.toString(p.playerItemsN[i]).length());
                    characterfile.newLine();
                }
            }
            characterfile.newLine();

            /*BANK*/
            characterfile.write("[BANK]", 0, 6);
            characterfile.newLine();
            for (int i = 0; i < p.bankItems.length; i++) {
                if (p.bankItems[i] > 0) {
                    characterfile.write("character-bank = ", 0, 17);
                    characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.bankItems[i]), 0, Integer.toString(p.bankItems[i]).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write(Integer.toString(p.bankItemsN[i]), 0, Integer.toString(p.bankItemsN[i]).length());
                    characterfile.newLine();
                }
            }
            characterfile.newLine();

            /*FRIENDS*/
            characterfile.write("[FRIENDS]", 0, 9);
            characterfile.newLine();
            for (int i = 0; i < p.friends.length; i++) {
                if (p.friends[i] > 0) {
                    characterfile.write("character-friend = ", 0, 19);
                    characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                    characterfile.write("	", 0, 1);
                    characterfile.write("" + p.friends[i]);
                    characterfile.newLine();
                }
            }
            characterfile.newLine();



            /*IGNORES*/
            /*characterfile.write("[IGNORES]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < ignores.length; i++) {
				if (ignores[i] > 0) {
					characterfile.write("character-ignore = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Long.toString(ignores[i]), 0, Long.toString(ignores[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();*/
            /*EOF*/
            characterfile.write("[EOF]", 0, 5);
            characterfile.newLine();
            characterfile.newLine();
            characterfile.close();
        } catch (IOException ioexception) {
            Misc.println(p.playerName + ": error writing file.");
            return false;
        }
        return true;
    }

}