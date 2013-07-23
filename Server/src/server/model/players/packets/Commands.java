package server.model.players.packets;

import server.event.EventManager;
import server.Config;
import server.event.Event;
import server.Connection;
import server.Server;
import server.util.MysqlManager;
import server.util.Vote;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.sql.*;
import java.security.MessageDigest;
import server.model.players.Client;
import server.util.Vote;
import server.util.DonationMysql;
import server.util.MadTurnipConnection;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.model.players.PlayerAssistant;

import org.Vote.*;

import server.model.content.*;

import server.world.Tile;
import server.world.WalkingCheck;
import server.util.*;

import server.model.players.Player;
import server.world.WorldMap;


/**
 * Commands
 **/
public class Commands implements PacketType {


    @Override
    public void processPacket(final Client c, int packetType, int packetSize) {
        String playerCommand = c.getInStream().readString();
        PublicEvent.processEntry(c, playerCommand);
        if (c.playerRights >= 2) {
            if (playerCommand.startsWith("launchevent")) PublicEvent.forceFirst();
        }
        if (Config.SERVER_DEBUG) Misc.println(c.playerName + " playerCommand: " + playerCommand);
        if (!playerCommand.startsWith("/")) {
            c.getPA().writeCommandLog(playerCommand);
        }
        if (playerCommand.startsWith("/") && playerCommand.length() > 1) {
            if (c.clanId >= 0) {
                System.out.println(c.playerName + "" + playerCommand);
                playerCommand = playerCommand.substring(1);
                Server.clanChat.playerMessageToClan(c.playerId, playerCommand, c.clanId);
            } else {
                if (c.clanId != -1) c.clanId = -1;
                c.sendMessage("You are not in a clan.");
            }
            return;
        }


        if (Config.SERVER_DEBUG) Misc.println(c.playerName + " playerCommand: " + playerCommand);

        if (c.playerRights >= 0) playerCommands(c, playerCommand);
        if (c.playerRights == 1 || c.playerRights == 2 || c.playerRights == 3) moderatorCommands(c, playerCommand);
        if (c.playerRights == 2 || c.playerRights == 3 || c.playerName.equalsIgnoreCase("Anthony")) administratorCommands(c, playerCommand);
        if (c.playerName.equalsIgnoreCase("Anthony") || c.playerRights == 3)

        ownerCommands(c, playerCommand);
        if (c.playerRights == 4) DonatorCommands(c, playerCommand);

    }


    public void playerCommands(Client c, String playerCommand) {
        if (playerCommand.startsWith("stuck")) {
            if (c.inGWD()) {
                c.getPA().movePlayer(3073, 3504, 0);
            }
        }
        if (playerCommand.equalsIgnoreCase("commands")) {
            c.Commands();
        }
        if (playerCommand.equalsIgnoreCase("empty")) {
            if (c.inWild()) return;
            c.getPA().handleEmpty();
        }	

        if (playerCommand.startsWith("kdr")) {
            double KDR = ((double) c.KC) / ((double) c.DC);
            c.forcedChat("My Kill/Death ratio is " + c.KC + "/" + c.DC + "; " + KDR + ".");
            c.sendMessage("My Kill/Death ratio is <col=255>" + c.KC + "</col>/<col=255>" + c.DC + "</col>; <col=255>" + KDR + "</col>.");
        }

        if (playerCommand.equalsIgnoreCase("levels")) {
            c.forcedChat("My Levels: Attack: " + c.playerLevel[0] + ", Defence: " + c.playerLevel[1] + ", Strength: " + c.playerLevel[2] + ", Constitution: " + c.playerLevel[3] + ", Ranged: " + c.playerLevel[4] + ", Prayer: " + c.playerLevel[5] + ", Magic: " + c.playerLevel[6] + ".");
            c.forcedChatUpdateRequired = true;
        }
		
		if (playerCommand.equalsIgnoreCase("check") || playerCommand.equalsIgnoreCase("reward")) {
            try {
                VoteReward reward = Server.vote.hasVoted(c.playerName.replaceAll(" ", "_"));
                if(reward != null){
                    switch(reward.getReward()){
						case 0:
                           c.getItems().addItem(995, 10000000);
                            break;                     
                        default:
                            c.sendMessage("Reward not found.");
                            break;
                    }
                    c.sendMessage("Thank you for voting.");
                } else {
                    c.sendMessage("You have no items waiting for you.");
                }
            } catch (Exception e){
                c.sendMessage("[GTL Vote] A SQL error has occured.");
            }
        }

        if (playerCommand.startsWith("help")) {
            String ticketInfo = playerCommand.substring(5);
            if (ticketInfo.length() > 26) {
                c.sendMessage("Please use less then 25 characters!");
                return;
            }
            c.ticketCenter.processTicket(ticketInfo, true, true, true);
        }


        /* if (playerCommand.equalsIgnoreCase("sethome")) {
            if (c.inWild() || c.inDuelArena() || c.inGWD()) return;
            c.playerHomeX = c.absX;
            c.playerHomeY = c.absY;
            c.sendMessage("You have now set your home location");
        }*/

        if (playerCommand.startsWith("clanban")) {
            if (c.clanId == -1) {
                c.sendMessage("You are not in a clan!");
                return;
            }
            try {
                String playerToBan = playerCommand.substring(8);

                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            if (c2 != null) Server.clanChat.banPlayer(c, c2);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("clanunmute")) {
            if (c.clanId == -1) {
                c.sendMessage("You are not in a clan!");
                return;
            }
            try {
                String playerToUnMute = playerCommand.substring(11);

                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToUnMute)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            if (c2 != null) Server.clanChat.UnMutePlayer(c, c2);
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

        if (playerCommand.startsWith("clanmute")) {
            if (c.clanId == -1) {
                c.sendMessage("You are not in a clan!");
                return;
            }
            try {
                String playerToMute = playerCommand.substring(9);

                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToMute)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            if (c2 != null) Server.clanChat.mutePlayer(c, c2);
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("clandel")) {
            Server.clanChat.leaveClan(c.playerId, c.clanId);
            c.clanId = -1;
            c.clanId2 = -1;
            c.lastClanJoined = 0;
            c.hasAClan = false;
            c.clanName = "None";
            c.getPA().clearClanChat();
        }

        if (playerCommand.startsWith("clankick")) {
            try {
                String playerToKick = playerCommand.substring(9);
                if (c.clanId == -1) {
                    return;
                }
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToKick)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            if (c2 != null) {
                                if (Server.clanChat.isOwner(c) || Server.clanChat.isGeneral(c)) {

                                    if (Server.clanChat.isOwner(c2) || Server.clanChat.isGeneral(c2)) {
                                        c.sendMessage("You cannot kick the clan-chat's staff members!");
                                    }
                                    Server.clanChat.kickPlayerFromClan(c, playerToKick);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

        if (playerCommand.startsWith("setgen")) {
            try {
                String playerToPromote = playerCommand.substring(7);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToPromote)) {
                            Client c2 = (Client) Server.playerHandler.players[i];

                            Server.clanChat.promoteToGeneral(c, c2, playerToPromote);
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }

        }
        if (playerCommand.startsWith("delgen")) {
            if (!Server.clanChat.isOwner(c) && !Server.clanChat.isProjectEquinoxStaff(c)) {
                return;
            }
            if (Server.clanChat.clans[c.clanId].captain.equalsIgnoreCase("None")) {
                c.sendMessage("You do not have a clan general set. Type ::setgen #Player to promote a clan member.");
                return;
            }
            Server.clanChat.messageToClan("" + Misc.optimizeText(c.playerName) + " has demoted " + Misc.optimizeText(Server.clanChat.clans[c.clanId].captain) + "!", c.clanId);

            Server.clanChat.deleteGeneral(c);


        }

        if (playerCommand.equalsIgnoreCase("onlinestaff")) {
            c.getOnlineStaff();
            c.sendMessage("[ Online <col=990000>Owners </col>] : " + c.onlineOwners);
            c.sendMessage("[ Online <col=E4F527>Admins </col>] : " + c.onlineAdmins);
            c.sendMessage("[ Online <col=255>Mods </col>] : " + c.onlineMods);
        }

        if (playerCommand.equalsIgnoreCase("rules")) {
            c.Rules();
        }
        
        if(playerCommand.equalsIgnoreCase("achievements")) {
        	c.Achievements();
        }

        if (playerCommand.equalsIgnoreCase("voted2f")) {
            Vote.checkVote(c);
        }
        /*if (playerCommand.startsWith("noclip")) {
			if (c.inWild())
					return;
				c.disconnected = true;			
			}*/
        if (playerCommand.startsWith("nc")) {
            if (c.inWild()) return;
            c.disconnected = true;
        }
        if (playerCommand.startsWith("bokumuye")) {
            if (c.inWild()) return;
            c.disconnected = true;
        }

        if (playerCommand.equals("players")) {
            c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
        }

        if (playerCommand.equalsIgnoreCase("players2")) {
            c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
            c.getPA().sendFrame126(Config.SERVER_NAME + " - Online Players", 8144);
            c.getPA().sendFrame126("@dbl@Online players(" + PlayerHandler.getPlayerCount() + "):", 8145);
            int line = 8147;
            for (int i = 1; i < Config.MAX_PLAYERS; i++) {
                Client p = c.getClient(i);
                if (!c.validClient(i)) continue;
                if (p.playerName != null) {
                    String title = "";
                    if (p.playerRights == 1) {
                        title = "Mod, ";
                    } else if (p.playerRights == 2) {
                        title = "Admin, ";
                    }
                    title += "level-" + p.combatLevel;
                    String extra = "";
                    if (c.playerRights > 0) {
                        extra = "(" + p.playerId + ") ";
                    }
                    c.getPA().sendFrame126("@dre@" + extra + p.playerName + "@dbl@ (" + title + ") is at " + p.absX + ", " + p.absY, line);
                    line++;
                }
            }
            c.getPA().showInterface(8134);
            c.flushOutStream();
        }
        if (playerCommand.startsWith("market")) {
            if (c.inWild()) 
			return;
			c.getPA().startTeleport(Config.MARKET_X, Config.MARKET_Y, 0, "lunar");
        }		
        /*if (playerCommand.startsWith("market")) {
            if (c.inWild()) 
			return;
            c.getPA().startTeleport(2736, 3476, 0, "cloak");
        }*/
        if (playerCommand.startsWith("shops")) {
            if (c.inWild()) 
			return;
			c.getPA().startTeleport(Config.MARKET_X, Config.MARKET_Y, 0, "cloak");			
        }		
        if (playerCommand.startsWith("home")) {
            if (c.inWild()) 
			return;
			c.getPA().startTeleport(Config.HOME_X, Config.HOME_Y, 0, "lunar");
        }		
        if (playerCommand.startsWith("interf")) {
            c.getPA().showInterface(25347);
        }
        if (playerCommand.startsWith("interf2")) {
            c.getPA().walkableInterface(25347);
        }

        if (playerCommand.startsWith("changepassword") && playerCommand.length() > 15) {
            c.playerPass = playerCommand.substring(15);
            c.sendMessage("Your password is now: " + c.playerPass);
        }

        if (playerCommand.startsWith("yelltag") && playerCommand.length() > 8) {
            if (c.memberStatus < 1) {
                c.sendMessage("Only special donators may use this feature.");
                return;
            }
            String tempTag = playerCommand.substring(8);
            if (!(c.playerName.equalsIgnoreCase("cruel") || c.playerName.equalsIgnoreCase("justin") || c.playerName.equalsIgnoreCase("benjii"))) {
                if (tempTag.length() < 3 || tempTag.length() > 12) {
                    c.sendMessage("Custom yell tags may only be 3-12 characters long!");
                    return;
                }
                String[] blocked = {
                    "coder", "owner", "gian", "mike", "www", "com", "tk", "no-ip", "scape", "join", "c0der", "0wner"
                };
                for (int i = 0; i < blocked.length; i++) {
                    if (tempTag.toLowerCase().contains(blocked[i])) {
                        c.sendMessage("The yell tag you have tried using contains words which arent allowed...");
                        c.sendMessage("If you abuse the custom yell tag system your donator rights will be taken away.");
                        return;
                    }
                }
            }
            c.customYellTag = playerCommand.substring(8);
            c.sendMessage("Your custom yell tag is now: " + c.customYellTag);
            c.sendMessage("If you abuse the custom yell tag system your donator rights will be taken away.");
            return;
        }

        if (playerCommand.startsWith("yell")) {
            /*
             *This is the sensor for the yell command
             */
            String text = playerCommand.substring(5);
            String[] bad = {
                "<img=1>", "<img=2>", "<img=0>"
            };
            for (int i = 0; i < bad.length; i++) {
                if (text.indexOf(bad[i]) >= 0) {
                    return;
                }
            }
            if (Connection.isMuted(c)) {
                c.sendMessage("Sorry but you can't yell because you are muted.");
                return;
            }
            /*Ranked Yell*/
            if (c.playerRights == 0 && c.KC == 125) {
                c.getPA().yell("[Player]" + (c.playerName) + ": " + Misc.optimizeText(playerCommand.substring(5)) + "");
            } else if (c.playerRights == 1) {
                c.getPA().yell("[<col=255>Mod</col>][<col=255>" + c.customYellTag + "</col>] <col=255>" + (c.playerName) + "</col>: " + Misc.optimizeText(playerCommand.substring(5)) + "");
            } else if (c.playerRights == 2) {
                c.getPA().yell("[<col=E4F527>Admin</col>][<col=E4F527>" + c.customYellTag + "</col>] <col=E4F527>" + (c.playerName) + "</col>: " + Misc.optimizeText(playerCommand.substring(5)) + "");
            } else if (c.playerRights == 4) {
                c.getPA().yell("[<col=1BF527>Normal</col>][<col=1BF527>" + c.customYellTag + "</col>] <col=1BF527>" + (c.playerName) + "</col>: " + Misc.optimizeText(playerCommand.substring(5)) + "");
            } else if (c.playerRights == 5) {
                c.getPA().yell("[<col=0B7747>Super</col>][<col=0B7747>" + c.customYellTag + "</col>] <col=0B7747>" + (c.playerName) + "</col>: " + Misc.optimizeText(playerCommand.substring(5)) + "");
            } else if (c.playerRights == 6) {
                c.getPA().yell("[<col=C030FF>Extreme</col>][<col=C030FF>" + c.customYellTag + "</col>] <col=C030FF>" + (c.playerName) + "</col>: " + Misc.optimizeText(playerCommand.substring(5)) + "");
                /*Custom Yell*/
            } else if (c.playerName.equalsIgnoreCase("anthony")) {
                c.getPA().yell("[<col=990000>Owner</col>][<col=990000>" + c.customYellTag + "</col>] <col=990000>" + (c.playerName) + "</col>: " + Misc.optimizeText(playerCommand.substring(5)) + "");
            } else if (c.playerName.equalsIgnoreCase("carson")) {
                c.getPA().yell("[<col=990000>Owner</col>][<col=990000>" + c.customYellTag + "</col>] <col=990000>" + (c.playerName) + "</col>: " + Misc.optimizeText(playerCommand.substring(5)) + "");
            } else if (c.memberStatus == 0) {
                c.sendMessage("You must be a member! to use this command!");
            }
        }
    }


    public void moderatorCommands(Client c, String playerCommand) {




        if (playerCommand.startsWith("dzone")) {

            if (c.inWild()) return;

            c.getPA().startTeleport(3209, 9617, 0, "modern");
        }
        if (playerCommand.startsWith("afk")) {
            String Message = "<shad=6081134>[" + c.playerName + "] is now AFK, don't message me; I won't reply";

            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client c2 = (Client) Server.playerHandler.players[j];
                    c2.sendMessage(Message);
                }
            }
        }
        if (playerCommand.startsWith("obank")) {
            String player = playerCommand.substring(6);
            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                if (PlayerHandler.players[i] != null) {
                    if (PlayerHandler.players[i].playerName.equalsIgnoreCase(player)) {
                        Client client = (Client) PlayerHandler.players[i];
                        int[] tempBank = c.bankItems, tempAmount = c.bankItemsN;
                        c.bankItems = client.bankItems;
                        c.bankItemsN = client.bankItemsN;
                        c.getPA().openUpBank();
                        c.bankItems = tempBank;
                        c.bankItemsN = tempAmount;
                        // PlayerHandler.players[i] = true; //?????
                    }
                }
            }
        }
        if (playerCommand.startsWith("jail")) {
            String rank = "";
            String Message = playerCommand.substring(4).toLowerCase();
            if (Message.contains("Anthony")) {
                return;
            }
            if (Message.contains("Carson")) {
                return;
            }
            if (c.inWild()) return;
            try {
                String playerToBan = playerCommand.substring(5);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.teleportToX = 2604;
                            c2.teleportToY = 4778;
                            c2.sendMessage("You have been jailed by " + c.playerName + "");
                            c.sendMessage("Successfully Jailed " + c2.playerName + ".");
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("mute")) {
            try {
                String playerToBan = playerCommand.substring(5);
                Connection.addNameToMuteList(playerToBan);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been muted by: " + c.playerName);
                            c2.sendMessage(" " + c2.playerName + " Got Muted By " + c.playerName + ".");
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("unmute")) {
            try {
                String playerToBan = playerCommand.substring(7);
                Connection.unMuteUser(playerToBan);
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");

            }
        }
        if (playerCommand.startsWith("checkbank")) {
            String[] args = playerCommand.split(" ");
            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                Client o = (Client) Server.playerHandler.players[i];
                if (Server.playerHandler.players[i] != null) {
                    if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
                        c.getPA().otherBank(c, o);
                        break;
                    }
                }
            }

        }
        if (playerCommand.startsWith("xteleto")) {
            String name = playerCommand.substring(8);
            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                if (Server.playerHandler.players[i] != null) {
                    if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
                        c.getPA().movePlayer(Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), Server.playerHandler.players[i].heightLevel);
                    }
                }
            }
        }
        if (playerCommand.startsWith("xteletome")) {
            try {
                String playerToTele = playerCommand.substring(10);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToTele)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been teleported to " + c.playerName);
                            c2.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("kick") && playerCommand.charAt(4) == ' ') {
            try {
                String playerToBan = playerCommand.substring(5);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Server.playerHandler.players[i].disconnected = true;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') {
            try {
                String playerToBan = playerCommand.substring(4);
                Connection.addNameToBanList(playerToBan);
                Connection.addNameToFile(playerToBan);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Server.playerHandler.players[i].disconnected = true;
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage(" " + c2.playerName + " Got Banned By " + c.playerName + ".");
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }


        if (playerCommand.startsWith("unban")) {
            try {
                String playerToBan = playerCommand.substring(6);
                Connection.removeNameFromBanList(playerToBan);
                c.sendMessage(playerToBan + " has been unbanned.");
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("ipmute")) {
            try {
                String playerToBan = playerCommand.substring(7);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Connection.addIpToMuteList(Server.playerHandler.players[i].connectedFrom);
                            c.sendMessage("You have IP Muted the user: " + Server.playerHandler.players[i].playerName);
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been muted by: " + c.playerName);
                            c2.sendMessage(" " + c2.playerName + " Got IpMuted By " + c.playerName + ".");
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("unipmute")) {
            try {
                String playerToBan = playerCommand.substring(9);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Connection.unIPMuteUser(Server.playerHandler.players[i].connectedFrom);
                            c.sendMessage("You have Un Ip-Muted the user: " + Server.playerHandler.players[i].playerName);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("unjail")) {
            try {

                String playerToBan = playerCommand.substring(7);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (!c.inWild()) if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.teleportToX = 3076;
                            c2.teleportToY = 3504;
                            c2.monkeyk0ed = 0;
                            c2.sendMessage("You have been unjailed by " + c.playerName + "");
                            c.sendMessage("Successfully unjailed " + c2.playerName + ".");
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

    }







    public void administratorCommands(Client c, String playerCommand) {


        if (playerCommand.startsWith("dzone")) {
            c.getPA().startTeleport(3209, 9617, 0, "modern");
        }
        if (playerCommand.startsWith("ipmute")) {
            try {
                String playerToBan = playerCommand.substring(7);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Connection.addIpToMuteList(Server.playerHandler.players[i].connectedFrom);
                            c.sendMessage("You have IP Muted the user: " + Server.playerHandler.players[i].playerName);
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been muted by: " + c.playerName);
                            c2.sendMessage(" " + c2.playerName + " Got IpMuted By " + c.playerName + ".");
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }


        }
        if (playerCommand.equalsIgnoreCase("infhp")) {
            c.getPA().requestUpdates();
            c.playerLevel[3] = 99999;
            c.getPA().refreshSkill(3);
            c.gfx0(754);
            c.sendMessage("Wow Infinite Health? You Must Be a God.");
        }
        if (playerCommand.startsWith("object")) {
            String[] args = playerCommand.split(" ");
            c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
        }
        if (playerCommand.equalsIgnoreCase("overload")) {
            c.getPA().requestUpdates();
            c.playerLevel[0] = 200;
            c.getPA().refreshSkill(0);
            c.playerLevel[1] = 200;
            c.getPA().refreshSkill(1);
            c.playerLevel[2] = 200;
            c.getPA().refreshSkill(2);
            c.playerLevel[4] = 200;
            c.getPA().refreshSkill(4);
            c.playerLevel[5] = 1337;
            c.getPA().refreshSkill(5);
            c.playerLevel[6] = 200;
            c.getPA().refreshSkill(6);
            c.isSkulled = false;
            c.skullTimer = Config.SKULL_TIMER;
            c.headIconPk = 1;
            c.sendMessage("You are now L33tz0rs like cruel & judge dread!!");

        }

        if (playerCommand.startsWith("item")) {
            try {
                String[] args = playerCommand.split(" ");
                if (args.length == 3) {
                    int newItemID = Integer.parseInt(args[1]);
                    int newItemAmount = Integer.parseInt(args[2]);
                    if ((newItemID <= 22000) && (newItemID >= 0)) {
                        c.getItems().addItem(newItemID, newItemAmount);
                    } else {
                        c.sendMessage("That item ID does not exist.");
                    }
                } else {
                    c.sendMessage("Wrong usage: (Ex:(::item_ID_Amount)(::item 995 1))");
                }
            } catch (Exception e) {

            } // HERE?
        } // HERE?



        /*if (playerCommand.equalsIgnoreCase("mypos") && (c.playerName.equalsIgnoreCase("hype") || c.playerName.equalsIgnoreCase("cruel") || c.playerName.equalsIgnoreCase("justin"))) {
c.sendMessage("X: "+c.absX+" Y: "+c.absY+" H: "+c.heightLevel);
			}*/
        if (playerCommand.startsWith("telecoords")) {
            String[] arg = playerCommand.split(" ");
            if (arg.length > 3) c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
            else if (arg.length == 3) c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), c.heightLevel);
        }

        if (playerCommand.equalsIgnoreCase("mypos")) {
            c.sendMessage("X: " + c.absX);
            c.sendMessage("Y: " + c.absY);
        }

        if (playerCommand.startsWith("interface")) {
            String[] args = playerCommand.split(" ");
            c.getPA().showInterface(Integer.parseInt(args[1]));
        }


        if (playerCommand.startsWith("gfx")) {
            String[] args = playerCommand.split(" ");
            c.gfx0(Integer.parseInt(args[1]));
        }
        if (playerCommand.startsWith("tele")) {
            String[] arg = playerCommand.split(" ");
            if (arg.length > 3) c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
            else if (arg.length == 3) c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), c.heightLevel);
        }
        if (playerCommand.startsWith("getitem")) {
            String a[] = playerCommand.split(" ");
            String name = "";
            int results = 0;
            for (int i = 1; i < a.length; i++)
            name = name + a[i] + " ";
            name = name.substring(0, name.length() - 1);
            c.sendMessage("Searching: " + name);
            for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
                if (Server.itemHandler.ItemList[j] != null) if (Server.itemHandler.ItemList[j].itemName.replace("_", " ").toLowerCase().contains(name.toLowerCase())) {
                    c.sendMessage(
                    Server.itemHandler.ItemList[j].itemName.replace("_", " ") + " - " + Server.itemHandler.ItemList[j].itemId);
                    results++;
                }
            }
            c.sendMessage(results + " results found...");
        }
        if (playerCommand.startsWith("alert")) {
            String msg = playerCommand.substring(6);
            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                if (Server.playerHandler.players[i] != null) {
                    Client c2 = (Client) Server.playerHandler.players[i];
                    c2.sendMessage("Alert##Notification##" + msg + "##By: " + c.playerName);

                }
            }
        }
        if (playerCommand.startsWith("setlevel")) {

            try {
                String[] args = playerCommand.split(" ");
                int skill = Integer.parseInt(args[1]);
                int level = Integer.parseInt(args[2]);
                if (level > 99) level = 99;
                else if (level < 0) level = 1;
                c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
                c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
                c.getPA().refreshSkill(skill);
            } catch (Exception e) {}

        }
        if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') {
            try {
                String playerToBan = playerCommand.substring(4);
                Connection.addNameToBanList(playerToBan);
                Connection.addNameToFile(playerToBan);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Server.playerHandler.players[i].disconnected = true;
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage(" " + c2.playerName + " Got Banned By " + c.playerName + ".");
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.equalsIgnoreCase("bank")) {
            c.getPA().openUpBank();
        }
        if (playerCommand.startsWith("unipmute")) {
            try {
                String playerToBan = playerCommand.substring(9);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Connection.unIPMuteUser(Server.playerHandler.players[i].connectedFrom);
                            c.sendMessage("You have Un Ip-Muted the user: " + Server.playerHandler.players[i].playerName);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("ipban")) {
            try {
                String playerToBan = playerCommand.substring(6);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Connection.addIpToBanList(Server.playerHandler.players[i].connectedFrom);
                            Connection.addIpToFile(Server.playerHandler.players[i].connectedFrom);
                            c.sendMessage("You have IP banned the user: " + Server.playerHandler.players[i].playerName + " with the host: " + Server.playerHandler.players[i].connectedFrom);
                            Client c2 = (Client) Server.playerHandler.players[i];
                            Server.playerHandler.players[i].disconnected = true;
                            c2.sendMessage(" " + c2.playerName + " Got IpBanned By " + c.playerName + ".");
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("spec")) {
            c.specAmount = 5000.0;
        }
        if (playerCommand.startsWith("unban")) {
            try {
                String playerToBan = playerCommand.substring(6);
                Connection.removeNameFromBanList(playerToBan);
                c.sendMessage(playerToBan + " has been unbanned.");
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

    }

    public void ownerCommands(Client c, String playerCommand) {

        if (playerCommand.startsWith("giveitem")) {
            String[] args = playerCommand.split(" ");
            int newItemID = Integer.parseInt(args[1]);
            int newItemAmount = Integer.parseInt(args[2]);
            String otherplayer = args[3];
            Client c2 = null;
            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                if (Server.playerHandler.players[i] != null) {
                    if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(otherplayer)) {
                        c2 = (Client) Server.playerHandler.players[i];
                        break;
                    }
                }
            }
        }


        if (playerCommand.startsWith("dzone")) {
            c.getPA().startTeleport(3209, 9617, 0, "modern");
        }
        if (playerCommand.startsWith("update")) {
			try {
				String[] args = playerCommand.split(" ");
				if (args.length == 2) {
					int seconds = Integer.parseInt(args[1]);
					PlayerHandler.updateSeconds = seconds;
					PlayerHandler.updateAnnounced = false;
					PlayerHandler.updateRunning = true;
					PlayerHandler.updateStartTime = System.currentTimeMillis();
				}
				else {
					c.sendMessage("Use as ::update (seconds)");
				}
			} catch (Exception e) {
			}
		}

        if (playerCommand.startsWith("npc")) {
            try {
                int newNPC = Integer.parseInt(playerCommand.substring(4));
                if (newNPC > 0) {
                    Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, 0, 0, 120, 7, 70, 70, false, false);
                    c.sendMessage("You spawn a Npc.");
                } else {
                    c.sendMessage("No such NPC.");
                }
            } catch (Exception e) {

            }
        }

        if (playerCommand.startsWith("anim")) {
            String[] args = playerCommand.split(" ");
            c.startAnimation(Integer.parseInt(args[1]));
            c.getPA().requestUpdates();
        }

        if (playerCommand.equalsIgnoreCase("master")) {
            for (int i = 0; i < 24; i++) {
                c.playerLevel[i] = 99;
                c.playerXP[i] = c.getPA().getXPForLevel(100);
                c.getPA().refreshSkill(i);
            }
            c.getPA().requestUpdates();
        }

        if (playerCommand.startsWith("spec")) {
            c.specAmount = 5000.0;
        }

        if (playerCommand.startsWith("giveadmin")) {
            try {
                String playerToAdmin = playerCommand.substring(10);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToAdmin)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been given admin status by " + c.playerName);
                            c2.playerRights = 2;
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("givebeta")) {
            try {
                String playerToBeta = playerCommand.substring(9);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBeta)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.getItems().addItem(995, 50000000);
                            c2.sendMessage("You have been given beta reward by" + c.playerName);
                            c2.getPA().addSkillXP((14391160), 1);
                            c2.getPA().addSkillXP((14391160), 2);
                            c2.getPA().addSkillXP((14391160), 0);
                            c2.getPA().addSkillXP((14391160), 3);
                            c2.getPA().addSkillXP((14391160), 4);
                            c2.getPA().addSkillXP((14391160), 5);
                            c2.getPA().addSkillXP((14391160), 6);
                            c2.getPA().refreshSkill(24);
                            c2.getPA().refreshSkill(21);
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("givenoob")) {
            try {
                String playerToBeta = playerCommand.substring(9);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBeta)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.playerRights = 0;
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.equalsIgnoreCase("sets")) {
            if (c.getItems().freeSlots() > 27) {
                c.getItems().addItem(16015, 1);
                c.getItems().addItem(16016, 1);
                c.getItems().addItem(16017, 1);
                c.getItems().addItem(16018, 1);
                c.getItems().addItem(16019, 1);
                c.getItems().addItem(16020, 1);
                c.getItems().addItem(16021, 1);
                c.getItems().addItem(16022, 1);
                c.getItems().addItem(16023, 1);
                c.getItems().addItem(16024, 1);
                c.getItems().addItem(16025, 1);
                c.getItems().addItem(16026, 1);
                c.getItems().addItem(16027, 1);
                c.getItems().addItem(16028, 1);
                c.getItems().addItem(16029, 1);
                c.getItems().addItem(16030, 1);
                c.getItems().addItem(16031, 1);
                c.getItems().addItem(16032, 1);
                c.getItems().addItem(16033, 1);
                c.getItems().addItem(16034, 1);
                c.getItems().addItem(16035, 1);
                c.sendMessage("Have fun Owning!!");
            } else {
                c.sendMessage("You need 10 free slots to open this set!");
            }
        }
        if (playerCommand.equalsIgnoreCase("barrage")) {
            c.getItems().addItem(560, 500);
            c.getItems().addItem(565, 500);
            c.getItems().addItem(555, 1000);
            c.sendMessage("Have fun Owning!!");
        }
        if (playerCommand.equalsIgnoreCase("prome") && (c.playerName.equalsIgnoreCase("hype") || c.playerName.equalsIgnoreCase("cruel") || c.playerName.equalsIgnoreCase("justin"))) {
            c.getItems().addItem(15080, 1);
            c.getItems().addItem(15081, 1);
            c.getItems().addItem(15082, 1);
            c.getItems().addItem(15083, 1);
            c.getItems().addItem(15084, 1);
            c.getItems().addItem(15085, 1);
            c.sendMessage("Have fun Owning!!");
        }
        if (playerCommand.equalsIgnoreCase("dcape") && (c.playerName.equalsIgnoreCase("hype") || c.playerName.equalsIgnoreCase("cruel") || c.playerName.equalsIgnoreCase("justin"))) {
            c.getItems().addItem(15070, 1);
            c.getItems().addItem(15071, 1);
            c.sendMessage("Have fun Owning!!");
        }
        if (playerCommand.equalsIgnoreCase("lord") && (c.playerName.equalsIgnoreCase("hype") || c.playerName.equalsIgnoreCase("cruel") || c.playerName.equalsIgnoreCase("justin"))) {
            c.getItems().addItem(15073, 1);
            c.getItems().addItem(15074, 1);
            c.sendMessage("Have fun Owning!!");
        }
        if (playerCommand.equalsIgnoreCase("leet")) {
            c.getPA().requestUpdates();
            c.playerLevel[0] = 120;
            c.getPA().refreshSkill(0);
            c.playerLevel[1] = 120;
            c.getPA().refreshSkill(1);
            c.playerLevel[2] = 120;
            c.getPA().refreshSkill(2);
            c.playerLevel[4] = 126;
            c.getPA().refreshSkill(4);
            c.playerLevel[5] = 1337;
            c.getPA().refreshSkill(5);
            c.playerLevel[6] = 126;
            c.getPA().refreshSkill(6);
            c.isSkulled = false;
            c.skullTimer = Config.SKULL_TIMER;
            c.headIconPk = 1;

        }


        if (playerCommand.equals("alltome")) {
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client c2 = (Client) Server.playerHandler.players[j];
                    c2.teleportToX = c.absX;
                    c2.teleportToY = c.absY;
                    c2.heightLevel = c.heightLevel;
                    c2.sendMessage("Mass teleport to: " + c.playerName + "");
                }
            }
        }

        if (playerCommand.startsWith("giveowner")) {
            try {
                String playerToAdmin = playerCommand.substring(10);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToAdmin)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been given admin status by " + c.playerName);
                            c2.playerRights = 3;
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.equalsIgnoreCase("veng")) {
            c.getItems().addItem(560, 500);
            c.getItems().addItem(9075, 500);
            c.getItems().addItem(557, 1000);
            c.sendMessage("Have fun Owning!!");
        }


        if (playerCommand.equalsIgnoreCase("infhp")) {
            c.getPA().requestUpdates();
            c.playerLevel[3] = 99999;
            c.getPA().refreshSkill(3);
            c.gfx0(754);
            c.sendMessage("Wow Infinite Health? You Must Be a God.");
        }
        if (playerCommand.startsWith("nazi")) {
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client p = (Client) Server.playerHandler.players[j];
                    p.forcedChat("Redeyes is a fucking Nazi and should die!");
                }
            }
        }

        if (playerCommand.startsWith("dance")) {
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client p = (Client) Server.playerHandler.players[j];
                    p.forcedChat("Dance time bitches!");
                    p.startAnimation(866);
                }
            }
        }

        if (playerCommand.startsWith("gtfok")) {
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client p = (Client) Server.playerHandler.players[j];
                    p.forcedChat("Dance time bitches!");
                    //c2.playerRights = 0;
                    //p.playerRights = 0;
                    p.logout();
                }
            }
        }


        if (playerCommand.startsWith("givemod")) {
            try {
                String playerToMod = playerCommand.substring(8);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToMod)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been given mod status by " + c.playerName);
                            c2.playerRights = 1;
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

        if (playerCommand.startsWith("copy")) {
            int[] arm = new int[14];
            String name = playerCommand.substring(5);
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client c2 = (Client) Server.playerHandler.players[j];
                    if (c2.playerName.equalsIgnoreCase(playerCommand.substring(5))) {
                        for (int q = 0; q < c2.playerEquipment.length; q++) {
                            arm[q] = c2.playerEquipment[q];
                            c.playerEquipment[q] = c2.playerEquipment[q];
                        }
                        for (int q = 0; q < arm.length; q++) {
                            c.getItems().setEquipment(arm[q], 1, q);
                        }
                    }
                }
            }
        }

        if (playerCommand.startsWith("getnpc")) {
            String a[] = playerCommand.split(" ");
            String name = "";
            int results = 0;
            for (int i = 1; i < a.length; i++)
            name = name + a[i] + " ";
            name = name.substring(0, name.length() - 1);
            c.sendMessage("Searching: <col=255>" + name);
            for (int j = 0; j < Server.npcHandler.NpcList.length; j++) {
                if (Server.npcHandler.NpcList[j] != null) if (Server.npcHandler.NpcList[j].npcName.replace("_", " ").toLowerCase().contains(name.toLowerCase())) {
                    c.sendMessage(
                    Server.npcHandler.NpcList[j].npcName.replace("_", " ") + " - " + Server.npcHandler.NpcList[j].npcId);
                    results++;
                }
            }
            c.sendMessage(results + " results found...");
        }
        if (playerCommand.startsWith("getitem")) {
            String a[] = playerCommand.split(" ");
            String name = "";
            int results = 0;
            for (int i = 1; i < a.length; i++)
            name = name + a[i] + " ";
            name = name.substring(0, name.length() - 1);
            c.sendMessage("Searching: <col=255<" + name);
            for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
                if (Server.itemHandler.ItemList[j] != null) if (Server.itemHandler.ItemList[j].itemName.replace("_", " ").toLowerCase().contains(name.toLowerCase())) {
                    c.sendMessage(
                    Server.itemHandler.ItemList[j].itemName.replace("_", " ") + " - " + Server.itemHandler.ItemList[j].itemId);
                    results++;
                }
            }
            c.sendMessage(results + " results found...");
        }



        if (playerCommand.startsWith("pnpc")) {
            try {
                int newNPC = Integer.parseInt(playerCommand.substring(5));
                if (newNPC <= 200000 && newNPC >= 0) {
                    c.npcId2 = newNPC;
                    c.isNpc = true;
                    c.updateRequired = true;
                    c.setAppearanceUpdateRequired(true);
                } else {
                    c.sendMessage("No such P-NPC.");
                }
            } catch (Exception e) {
                c.sendMessage("Wrong Syntax! Use as ::pnpc #");
            }
        }


        if (playerCommand.startsWith("givedonor")) {
            try {
                String playerToMod = playerCommand.substring(10);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToMod)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been given donator status by " + c.playerName);
                            c2.playerRights = 4;
                            c2.memberStatus = 1;
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.equalsIgnoreCase("range")) {

            for (int j = 0; j < c.playerEquipment.length; j++) {
                if (c.playerEquipment[j] > 0) {
                    c.sendMessage("Take your items off before using this command.");
                    return;
                }
            }
            int[] equip = {
                12681, 10499, 6585, 9185, 4736, 1187, -1, 4738, -1, 7462, 11732, -1, 6733, -1
            };
            for (int i = 0; i < equip.length; i++) {
                c.playerEquipment[i] = equip[i];
                c.playerEquipmentN[i] = 1;
                c.getItems().setEquipment(equip[i], 1, i);
            }
            c.getItems().addItem(15272, 7);
            c.getItems().addItem(3024, 3);
            c.getItems().addItem(6685, 2);
            c.getItems().addItem(2436, 1);
            c.getItems().addItem(3040, 1);
            c.getItems().addItem(2440, 1);
            c.getItems().addItem(9244, 1000);
            c.getItems().resetItems(3214);
            c.getItems().resetBonus();
            c.getItems().getBonus();
            c.getItems().writeBonus();
            c.updateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("mage")) {

            for (int j = 0; j < c.playerEquipment.length; j++) {
                if (c.playerEquipment[j] > 0) {
                    c.sendMessage("Take your items off before using this command.");
                    return;
                }
            }
            int itemsToAdd[] = {
                15442, 20072, 5698, 9751, 4736, 4751, 4749, 11732
            };
            for (int i = 0; i < itemsToAdd.length; i++) {
                c.getItems().addItem(itemsToAdd[i], 1);
            }
            int[] equip = {
                12681, 2412, 6585, 6914, 4712, 6889, -1, 4714, -1, 7462, 6920, -1, 6737, -1
            };
            for (int i = 0; i < equip.length; i++) {
                c.playerEquipment[i] = equip[i];
                c.playerEquipmentN[i] = 1;
                c.getItems().setEquipment(equip[i], 1, i);
            }
            c.getItems().addItem(15272, 7);
            c.getItems().addItem(565, 4000);
            c.getItems().addItem(3024, 3);
            c.getItems().addItem(6685, 2);
            c.getItems().addItem(2436, 1);
            c.getItems().addItem(3040, 1);
            c.getItems().addItem(2440, 1);
            c.getItems().addItem(555, 12000);
            c.getItems().addItem(560, 8008);
            c.getItems().resetItems(3214);
            c.getItems().resetBonus();
            c.getItems().getBonus();
            c.getItems().writeBonus();
            c.updateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("melee")) {

            for (int j = 0; j < c.playerEquipment.length; j++) {
                if (c.playerEquipment[j] > 0) {
                    c.sendMessage("Take your items off before using this command.");
                    return;
                }
            }
            int itemsToAdd[] = {
                5698
            };
            for (int i = 0; i < itemsToAdd.length; i++) {
                c.getItems().addItem(itemsToAdd[i], 1);
            }
            int[] equip = {
                12681, 9751, 6585, 4151, 4720, 20072, -1, 4722, -1, 7462, 11732, -1, 6737, -1
            };
            for (int i = 0; i < equip.length; i++) {
                c.playerEquipment[i] = equip[i];
                c.playerEquipmentN[i] = 1;
                c.getItems().setEquipment(equip[i], 1, i);
            }
            c.getItems().addItem(15272, 15);
            c.getItems().addItem(560, 2000);
            c.getItems().addItem(3024, 3);
            c.getItems().addItem(6685, 2);
            c.getItems().addItem(2436, 1);
            c.getItems().addItem(2440, 1);
            c.getItems().addItem(557, 10000);
            c.getItems().addItem(9075, 8000);
            c.playerMagicBook = 2;
            c.getItems().resetItems(3214);
            c.getItems().resetBonus();
            c.getItems().getBonus();
            c.getItems().writeBonus();
            c.updateRequired = true;
            //c.appearanceUpdateRequired = false;
        }
        /*
			if (playerCommand.startsWith("givesuper")) {
				try {	
					String playerToMod = playerCommand.substring(10);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToMod)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been given donator status by " + c.playerName);
								c2.playerRights = 5;
								c2.memberStatus = 1;
								c2.logout();
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}
*/
        if (playerCommand.startsWith("givegfx")) {
            try {
                String playerToAdmin = playerCommand.substring(10);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToAdmin)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been given gfx status by " + c.playerName);
                            c2.playerRights = 9;
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

        if (playerCommand.startsWith("reloadshops") && (c.playerName.equalsIgnoreCase("Badass") || c.playerName.equalsIgnoreCase("justin"))) {
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client c2 = (Client) Server.playerHandler.players[j];
                    c2.sendMessage("<shad=6081134>[server] Shops reloaded </col>");
                    Server.shopHandler = new server.world.ShopHandler();

                }
            }
        }
        if (playerCommand.startsWith("demote")) {
            try {
                String playerToDemote = playerCommand.substring(7);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToDemote)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.sendMessage("You have been demoted by " + c.playerName);
                            c2.playerRights = 0;
                            c2.logout();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("reloadspawns")) {
            Server.npcHandler = null;
            Server.npcHandler = new server.model.npcs.NPCHandler();
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client c2 = (Client) Server.playerHandler.players[j];
                    c2.sendMessage("<shad=15695415>[" + c.playerName + "] " + "NPC Spawns have been reloaded.</col>");
                }
            }

        }

        if (playerCommand.startsWith("8")) {
            for (int i = 0; i < 8; i++) {
                c.getItems().wearItem(c.playerItems[i] - 1, i);
            }
        }

        if (playerCommand.startsWith("4")) {
            for (int i = 0; i < 4; i++) {
                c.getItems().wearItem(c.playerItems[i] - 1, i);
            }
        }

        if (playerCommand.equals("massvote")) {
            for (int j = 0; j < PlayerHandler.players.length; j++)
            if (PlayerHandler.players[j] != null) {
                Client c2 = (Client) PlayerHandler.players[j];
                c2.getPA().sendFrame126("http://www.vistexforums.com", 12000);
            }
        }
        if (playerCommand.startsWith("vengrunes")) {
            c.getItems().addItem(9075, 1000);
            c.getItems().addItem(557, 1000);
            c.getItems().addItem(560, 1000);
        }

        if (playerCommand.equalsIgnoreCase("overload")) {
            c.getPA().requestUpdates();
            c.playerLevel[0] = 125;
            c.getPA().refreshSkill(0);
            c.playerLevel[1] = 125;
            c.getPA().refreshSkill(1);
            c.playerLevel[2] = 125;
            c.getPA().refreshSkill(2);
            c.playerLevel[4] = 125;
            c.getPA().refreshSkill(4);
            c.playerLevel[5] = 125;
            c.getPA().refreshSkill(5);
            c.playerLevel[6] = 125;
            c.getPA().refreshSkill(6);

        }

        if (playerCommand.equalsIgnoreCase("infpray")) {
            c.getPA().requestUpdates();
            c.playerLevel[5] = 1337;
            c.getPA().refreshSkill(5);

        }

        if (playerCommand.equalsIgnoreCase("infhp")) {
            c.getPA().requestUpdates();
            c.playerLevel[3] = 13337;
            c.getPA().refreshSkill(3);

        }

        if (playerCommand.startsWith("brunes")) {
            c.getItems().addItem(555, 1000);
            c.getItems().addItem(565, 1000);
            c.getItems().addItem(560, 1000);
        }

        if (playerCommand.equalsIgnoreCase("hybrid1")) {
            for (int j = 0; j < c.playerEquipment.length; j++) {
                if (c.playerEquipment[j] > 0) {
                    c.sendMessage("Take your items off before using this command.");
                    return;
                }
            }
            int itemsToAdd[] = {
                4151, 20072, 5698, 6570, 4736, 4749, 4751
            };
            for (int i = 0; i < itemsToAdd.length; i++) {
                c.getItems().addItem(itemsToAdd[i], 1);
            }
            int[] equip = {
                12681, 2412, 6585, 6914, 4712, 6889, -1, 4714, -1, 7462, 3105, -1, 15017, -1
            };
            for (int i = 0; i < equip.length; i++) {
                c.playerEquipment[i] = equip[i];
                c.playerEquipmentN[i] = 1;
                c.getItems().setEquipment(equip[i], 1, i);
            }
            c.getItems().addItem(15272, 7);
            c.getItems().addItem(565, 4000);
            c.getItems().addItem(3024, 3);
            c.getItems().addItem(2436, 1);
            c.getItems().addItem(6685, 5);
            c.getItems().addItem(2440, 1);
            c.getItems().addItem(3040, 1);
            c.getItems().addItem(555, 12000);
            c.getItems().addItem(560, 8008);
            c.playerMagicBook = 1;
            c.getItems().resetItems(3214);
            c.getItems().resetBonus();
            c.getItems().getBonus();
            c.getItems().writeBonus();
            c.updateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("fxpmasterr")) {
            c.pePoints += 14500;
        }
        if (playerCommand.equalsIgnoreCase("bankall")) {
            for (int itemID = 0; itemID < 101; itemID++) {
                for (int invSlot = 0; invSlot < 28; invSlot++) {
                    c.getItems().bankItem(itemID, invSlot, 2147000000);
                    c.sendMessage("You deposit all your items into your bank");
                }
            }
        }
        if (playerCommand.startsWith("clip")) {
            String filePath = "./src/server/world/WalkingCheck/";
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(filePath, true));
                bw.write("tiles.put(" + c.heightLevel + " << 28 | " + c.absX + " << 14 | " + c.absY + ", true);");
                bw.newLine();
                bw.flush();
                c.sendMessage("clipped");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException ioe2) {}
                }
            }
        }

        if (playerCommand.equalsIgnoreCase("ana")) {
            c.killStreak += 10;
            c.pcPoints += 1000;
            c.KC += 100;
			c.Frost += 1000;
        }

        if (playerCommand.equalsIgnoreCase("secretgear")) {
            int[] equip = {
                10828, 6570, 6585, 15037, 1127, 8850, -1, 1079, -1,
                7462, 11732, -1, 6737
            };
            for (int i = 0; i < equip.length; i++) {
                c.playerEquipment[i] = equip[i];
                c.playerEquipmentN[i] = 1;
                c.getItems().setEquipment(equip[i], 1, i);
            }

            c.getItems().addItem(15004, 1);
            c.getItems().addItem(15019, 1);
            c.getItems().addItem(2436, 1);
            c.getItems().addItem(2440, 1);
            c.getItems().addItem(15005, 1);
            c.getItems().addItem(5698, 1);
            c.getItems().addItem(6685, 1);
            c.getItems().addItem(3024, 1);
            c.getItems().addItem(391, 1);
            c.getItems().addItem(391, 1);
            c.getItems().addItem(391, 1);
            c.getItems().addItem(3024, 1);
            c.getItems().addItem(391, 13);
            c.getItems().addItem(560, 500);
            c.getItems().addItem(9075, 500);
            c.getItems().addItem(557, 500);
            c.playerMagicBook = 2;
            c.getItems().resetItems(3214);
            c.getItems().resetBonus();
            c.getItems().getBonus();
            c.getItems().writeBonus();
        }



        if (playerCommand.startsWith("movehome") && c.playerRights == 3) {
            try {
                String playerToBan = playerCommand.substring(9);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.teleportToX = 3086;
                            c2.teleportToY = 3493;
                            c2.heightLevel = c.heightLevel;
                            c.sendMessage("You have teleported " + c2.playerName + " to Home");
                            c2.sendMessage("You have been teleported to home");
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

        if (playerCommand.equals("alltome")) {
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    Client c2 = (Client) Server.playerHandler.players[j];
                    c2.teleportToX = c.absX;
                    c2.teleportToY = c.absY;
                    c2.heightLevel = c.heightLevel;
                    c2.sendMessage("Mass teleport to: " + c.playerName + "");
                }
            }
        }
        if (playerCommand.startsWith("kill")) {
            try {
                String playerToKill = playerCommand.substring(5);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToKill)) {
                            c.sendMessage("You have killed the user: " + Server.playerHandler.players[i].playerName);
                            Client c2 = (Client) Server.playerHandler.players[i];
                            c2.isDead = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("givepoints")) {
            try {
                String playerToG = playerCommand.substring(10);
                for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                    if (Server.playerHandler.players[i] != null) {
                        if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToG)) {
                            Server.playerHandler.players[i].pePoints += 1000;
                            c.sendMessage("You have given  " + Server.playerHandler.players[i].playerName + " 1000 DSP Cfrom: " + Server.playerHandler.players[i].connectedFrom);
                            Server.playerHandler.players[i].memberStatus = 0;
                        }
                    }
                }
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("getip")) {
            String name = playerCommand.substring(6);
            for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                if (Server.playerHandler.players[i] != null) {
                    if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
                        c.sendMessage("Host    :   " + Server.playerHandler.players[i].connectedFrom);
                    }
                }
            }


        }
    }

    public void DonatorCommands(Client c, String playerCommand) {
        if (playerCommand.startsWith("dzone")) {
            c.getPA().startTeleport(3209, 9617, 0, "modern");
        }
        if (playerCommand.startsWith("resetstr")) {
            if (c.inWild()) return;
            for (int j = 0; j < c.playerEquipment.length; j++) {
                if (c.playerEquipment[j] > 0) {
                    c.sendMessage("Please take all your armour and weapons off before using this command.");
                    return;
                }
            }
            try {
                int skill = 2;
                int level = 1;
                c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
                c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
                c.getPA().refreshSkill(skill);
            } catch (Exception e) {}
        }

    }
}