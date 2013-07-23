package server.world;

import server.model.players.Client;
import server.Config;
import server.Connection;
import server.Server;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.model.players.packets.ClanChat;
import server.model.players.packets.PJClans;
import server.util.Misc;

import java.util.ArrayList;


public class ClanChatHandler {

    public ClanChatHandler() {

    }
    public Clan[] clans = new Clan[100]; // 100

    public void handleClanChat(Client c, String name) {
        if (c.clanId != -1) {
            c.sendMessage("You are already in a clan!");
            return;
        }

        if (c.hasAClan && !name.equalsIgnoreCase(c.clanName)) {
            c.sendMessage("Please type ::clandel");
            return;
        }
        for (int j = 0; j < clans.length; j++) {
            if (clans[j] != null) {
                if (clans[j].name.equalsIgnoreCase(name)) {
                    if (checkPass(c, j)) {
                        addToClan(c.playerId, j);
                        return;
                    } else {
                        c.sendMessage("Incorrect password!");
                        return;
                    }
                }
            }
        }
        makeClan(c, name);
    }


    public void makeClan(Client c, String name) {
        String pass = null;
        boolean loot = false;

        if (openClan() >= 0) {
            if (validName(name)) {
                c.clanId = openClan();
                createClan(c.playerName, name, "None", pass, loot, false);
                addToClan(c.playerId, c.clanId);
                Server.pJClans.saveClan(c.playerName, name, "None", pass, loot, false);
                c.hasAClan = true;
                c.clanId2 = c.clanId;
                c.lastClanJoined = c.clanId;
                //System.out.println(c.lastClanJoined);
			if (name != null) c.clanName = "" + name + "";
            if (c.hasAClan) c.doNotSendClanMsg = true;
                c.sendMessage("Attempting to join channel...");
                c.sendMessage("Now talking in channel: <col=124214>" + (c.clanName) + "</col>");
                c.sendMessage("To talk, start each line of chat with the / symbol.");
            } else {
                c.sendMessage("A clan with this name already exists.");
            }
        } else {
            c.sendMessage("Your clan chat request could not be completed.");
        }
    }
    public void banPlayer(Client c, Client c2) {
        if (clans[c.clanId] == null) {
            return;
        }
        if (!isOwner(c) && !isGeneral(c) && !isProjectEquinoxStaff(c)) {
            return;
        }
        if (c.playerId == c2.playerId) {
            c.sendMessage("Client two is nulled. Try again later.");
            return;
        }
        if (c.clanId != c2.clanId) {
            c.sendMessage("Couldn't find " + c2.playerName + " in your clan chat.");
            return;
        }
        if (isProjectEquinoxStaff(c2) || isOwner(c2) || isGeneral(c2)) {
            return;
        }
        if (clans[c.clanId] != null && clans[c2.clanId] != null && c2 != null) {
            c2.clanId = -1;
            c2.clanId2 = -1;
            c2.lastClanJoined = 0;
            c2.hasAClan = false;
            c2.clanName = "None";
            c2.getPA().clearClanChat();
            messageToClan("" + (c.playerName) + " just banned " + (c2.playerName) + " from the channel!", c.clanId);
            for (int j = 0; j < clans[c.clanId].members.length; j++) {
                if (clans[c.clanId].bannedMembers[j] == 0 || clans[c.clanId].bannedMembers[j] < 0) {
                    clans[c.clanId].bannedMembers[j] = c2.playerId;

                    System.out.println(clans[c.clanId].bannedMembers[j]);
                }
            }
            for (int j = 0; j < clans[c.clanId].members.length; j++) {

                if (clans[c.clanId].members[j] == c2.playerId) {
                    clans[c.clanId].members[j] = -1;
                }
            }
        }



        c.sendMessage("You've banned " + (c2.playerName) + " from " + (clans[c.clanId].name) + ".");
        c2.sendMessage("" + (c.playerName) + " has banned you from the clan chat " + (clans[c.clanId].name) + ".");



    }





    public boolean doNOT = false;
    public void mutePlayer(Client c, Client c2) {
        if (!isOwner(c) && !isGeneral(c)) {
            return;
        }
        if (c.playerId == c2.playerId) {
            c.sendMessage("Client two is nulled. Try again later.");
            return;
        }
        if (c.clanId != c2.clanId) {
            c.sendMessage("Couldn't find " + c2.playerName + " in your clan chat.");
            return;
        }
        if (clans[c.clanId] != null && clans[c2.clanId] != null) {
            if (c2.playerRights == 1 || c2.playerRights == 2 || c2.playerRights == 3) {
                c2.sendMessage("" + c.playerName + " tried to mute you in his/her clan chat!");
                c.sendMessage("You can't mute a Project-E Staff member");
                return;
            }
            if (!isInClan(c2)) {
                c.sendMessage(c2.playerName + " is not in your clan!");
                return;
            }
            for (int j = 0; j < clans[c.clanId].mutedMembers.length; j++) {
                if (clans[c.clanId].mutedMembers[j] == 0 || clans[c.clanId].mutedMembers[j] < 0) {
                    clans[c.clanId].mutedMembers[j] = c2.playerId;
                }
            }


            messageToClan("" + (c2.playerName) + " was just muted by " + (c.playerName) + "!", c.clanId);
            c.sendMessage("To unmute " + c2.playerName + ", type ::clanunmute " + c2.playerName + "");
            c2.sendMessage("You have been muted in: " + clans[c.clanId].name);




        }
    }

    public void UnMutePlayer(Client c, Client c2) {
        try {
            if (!isOwner(c) && !isGeneral(c) && !isProjectEquinoxStaff(c)) {
                return;
            }
            if (c.playerId == c2.playerId) {
                c.sendMessage("Client two is nulled. Try again later.");
                return;
            }
            if (c.clanId != c2.clanId) {
                c.sendMessage("Couldn't find " + c2.playerName + " in your clan chat.");
                return;
            }
            if (clans[c.clanId] != null) {
                for (int j = 0; j < clans[c.clanId].mutedMembers.length; j++) {
                    if (!isInClan(c2)) {
                        c.sendMessage(c2.playerName + " is not in your clan!");
                        return;
                    }
                    if (clans[c.clanId].mutedMembers[j] == c2.playerId) {
                        clans[c.clanId].mutedMembers[j] = -1;
                        if (msg == 0) messageToClan("" + (c2.playerName) + " was just unmuted by " + (c.playerName) + "!", c.clanId);

                        if (msg == 0) c2.sendMessage("" + (c.playerName) + " has unmuted you in the clan chat channel: " + clans[c.clanId].name);

                        msg += 1;
                    }

                }

            }
        } catch (Exception e) {
            c.sendMessage("Player must be offline!");
        }
        msg = 0;
    }

    public boolean isOwner(Client c) {
        if (clans[c.clanId].owner.equalsIgnoreCase(c.playerName)) {
            return true;
        }
        return false;
    }

    public boolean isInClan(Client c) {
        for (int i = 0; i < clans.length; i++) {
            if (clans[i] != null) {
                for (int j = 0; i < clans[i].members.length; j++) {
                    if (clans[i].members[j] == c.playerId) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean isProjectEquinoxStaff(Client c) {
        if (c.playerRights == 1) {
            return true;
        }
        if (c.playerRights == 2) {
            return true;
        }
        if (c.playerRights == 3) {
            return true;
        }
        return false;
    }
    public boolean isClanMuted(Client c) {
        if (isProjectEquinoxStaff(c)) {
            return false;
        }
        for (int i = 0; i < clans[c.clanId].mutedMembers[i]; i++) {
            if (clans[c.clanId].members[i] == c.playerId) {
                return true;
            }
        }
        return false;
    }
    public boolean isBanned(Client c, int clanId) {
        if (isProjectEquinoxStaff(c)) {
            return false;
        }
        for (int i = 0; i < clans[clanId].bannedMembers[i]; i++) {
            if (clans[clanId].members[i] == c.playerId) {
                return true;
            }
        }
        return false;
    }

    public void createClan(String owner, String name, String captain, String pass, boolean lootshare, boolean hasPass) {
        if (openClan() >= 0) {
            if (validName(name)) {
                clans[openClan()] = new Clan(owner, name, captain, pass, lootshare, hasPass);
            }
        }
    }

    public void setClanPassword(Client c, String pass, boolean hasPass) {
        if (isOwner(c)) {
            if (pass.equalsIgnoreCase("none")) {
                c.sendMessage("You remove the clan chats password.");
                clans[c.clanId].password = null;
                clans[c.clanId].hasPassword = false;
                return;
            } else {
                c.sendMessage("You change the clan chats password to: " + pass + ".");
                clans[c.clanId].password = pass;
                clans[c.clanId].hasPassword = true;
            }
        } else {
            c.sendMessage("You do not have the rights to change this clan's password.");
        }
    }



    public void changeOwner(Client c, String name) {
        if (c.clanId == -1) {
            c.sendMessage("You are not in a clan!");
            return;
        }
        if (!isOwner(c)) {
            c.sendMessage("You do not have the power to change the owner of the clan chat!");
            return;
        }
        clans[c.clanId].owner = name;
        updateClanChat(c.clanId);
        messageToClan("The new owner of this clan chat is now " + (name) + ".", c.clanId);
    }

    public void promoteToGeneral(Client c, Client c2, String name) {
        if (c.clanId == -1) {
            c.sendMessage("You are not in a clan!");
            return;
        }
        if (!isOwner(c)) {
            return;
        }
        if (c.clanId != c2.clanId) {
            c.sendMessage("That player is not in the same clan chat as you.");
            return;
        }
        if (c.playerName.equalsIgnoreCase(c2.playerName)) {
            c.sendMessage("You can't promote yourself to general!");
            return;
        }
        clans[c.clanId].captain = name;
        updateClanChat(c.clanId);
        messageToClan("The new general of this clan chat is now " + (c2.playerName) + ".", c.clanId);
    }
    public void deleteGeneral(Client c) {
        clans[c.clanId].captain = "None";
        updateClanChat(c.clanId);
    }
    public void kickPlayerFromClan(Client c, String name) {
        if (!isOwner(c) && !isGeneral(c) && !isProjectEquinoxStaff(c)) {
            return;
        }
        //So they can't kick the owner / themselves
        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
            if (Server.playerHandler.players[i] != null) {
                if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
                    Client c2 = (Client) Server.playerHandler.players[i];
                    if (c.playerName.equalsIgnoreCase(name) || isOwner(c2) || isGeneral(c2)) {
                        c.sendMessage("You may not kick the chat's staff..!");
                        return;
                    }
                }
            }
        }
        if (c.clanId < 0) {
            c.sendMessage("You are not in a clan.");
            return;
        }

        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
            if (PlayerHandler.players[i] != null) {
                if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
                    Client c2 = (Client) PlayerHandler.players[i];
                    if (c2.playerRights == 2 || c2.playerRights == 3) {
                        c.sendMessage("You may NOT kick an admin from you clan!");
                        c2.sendMessage(c.playerName + " has tried to kick you from his/her clan.");
                        return;
                    }
                    c2.clanId = -1;
                    c2.clanId2 = -1;
                    c2.lastClanJoined = 0;
                    c2.hasAClan = false;
                    c2.clanName = "None";
                    c2.getPA().clearClanChat();
                    messageToClan("" + (c.playerName) + " just kicked " + (c2.playerName) + " from the channel!", c.clanId);
                    for (int j = 0; j < clans[c.clanId].members.length; j++) {
                        if (clans[c.clanId].members[j] == i) {
                            clans[c.clanId].members[j] = -1;
                        }
                    }
                }
            }
        }
        updateClanChat(c.clanId);
    }

    public void updateClanChat(int clanId) {
        if (clanId != -1 && clanId < 100) {
            if (clans[clanId] != null) {
                for (int j = 0; j < clans[clanId].members.length; j++) {
                    if (clans[clanId].members[j] <= 0) continue;
                    if (PlayerHandler.players[clans[clanId].members[j]] != null) {
                        Client c = (Client) PlayerHandler.players[clans[clanId].members[j]];
                        c.hasAClan = true;
                        c.clanName = "" + clans[clanId].name + "";
                        c.lastClanJoined = clanId;
                        //c.clanId = clanId;
                        c.clanId2 = clanId;
                        c.getPA().sendFrame126("Talking in: @whi@" + Misc.optimizeText(clans[clanId].name) + "", 18139);
                        c.getPA().sendFrame126("Owner: @whi@" + (clans[clanId].owner) + "", 18140);
                        int slotToFill = 18144;
                        for (int i = 0; i < clans[clanId].members.length; i++) {
                            if (clans[clanId].members[i] > 0) {
                                if (PlayerHandler.players[clans[clanId].members[i]] != null) {
                                    //if(PlayerHandler.players[clans[clanId].members[i]].playerName == clans[clanId].owner && PlayerHandler.players[clans[clanId].members[i]].playerName != null) {
                                    if (PlayerHandler.players[clans[clanId].members[i]].playerName.equalsIgnoreCase(clans[clanId].owner)) {
                                        c.getPA().sendFrame126("" + (PlayerHandler.players[clans[clanId].members[i]].playerName) + " - @whi@[ @cya@Clan Owner @whi@] ", slotToFill);
                                    } else {
                                        if (PlayerHandler.players[clans[clanId].members[i]].playerName.equalsIgnoreCase(clans[clanId].captain)) {
                                            c.getPA().sendFrame126("" + (PlayerHandler.players[clans[clanId].members[i]].playerName) + " - @whi@[ @gre@Clan General @whi@]", slotToFill);

                                        } else {
											c.getPA().sendFrame126((PlayerHandler.players[clans[clanId].members[i]].playerName) + getRank(c), slotToFill);
                                            //c.getPA().sendFrame126("@whi@[ @yel@Member @whi@] " + (PlayerHandler.players[clans[clanId].members[i]].playerName) + "", slotToFill);

                                        }
                                    }
                                }
                                if (PlayerHandler.players[clans[clanId].members[i]] == null) {

                                    c.getPA().clearClanChat();
                                    updateClanChat(clanId);
                                    c.hasAClan = true;
                                    c.clanId = clanId;
                                    c.clanId2 = clanId;
                                    c.lastClanJoined = clanId;
                                    c.clanName = "" + clans[clanId].name + "";
                                }
                                slotToFill++;

                            }
                        }
                        for (int k = slotToFill; k < 18244; k++)
                        c.getPA().sendFrame126("", k);
                    }
                }
            }
        }
    }
	
	/*public String getRank(final Client c) {
switch (c.playerRights) {
case 3:
return "lolz";
case 2:
return "lelz";
case 1:
return "lulz";
default:
return "lawlz";
}
}*/

   public String getRank(Client c) {
        String rank = "Player";
        if(c.playerRights == 1) {
			rank = " - @whi@[ @blu@LL Moderator @whi@]";
		} else if(c.playerRights == 2) {
			rank = " - @whi@[ @yel@LL Administrator @whi@]";
		} else if(c.playerRights == 3) {
			rank = " - @whi@[ @red@LL Owner @whi@]";
		} else if(c.playerRights == 0) {
			rank = " - @whi@[ @or1@Member @whi@]";
		}
		return " - @whi@[ @or1@Member @whi@]";
	}
    
    public int openClan() {
        for (int j = 0; j < clans.length; j++) {
            if (clans[j] == null || clans[j].owner == "") return j;
        }
        return -1;
    }

    public boolean validName(String name) {
        for (int j = 0; j < clans.length; j++) {
            if (clans[j] != null) {
                if (clans[j].name.equalsIgnoreCase(name)) return false;
            }
        }
        return true;
    }

    public boolean checkPass(Client c, int clanId) {
        if (!clans[clanId].hasPassword) {
            return true;
        }
        if (c.playerRights == 1 || c.playerRights == 2 || c.playerRights == 3) {
            return true;
        }
        if (clans[clanId].owner.equalsIgnoreCase(c.playerName)) {
            return true;
        }
        if (clans[clanId].password.equalsIgnoreCase(c.clanPass)) {
            return true;
        }
        return false;
    }

    public void addToClan(int playerId, int clanId) {
        Client lol = (Client) PlayerHandler.players[playerId];
        if (isBanned(lol, clanId)) { // Doesn't work :(
            lol.sendMessage("You're banned from this clan chat!");
            return;
        }
        if (clanId >= 100 || lol.clanId >= 100) {
            return;
        }
        if (clans[clanId] != null) {
            for (int j = 0; j < clans[clanId].members.length; j++) {
                if (clans[clanId].members[j] <= 0) {
                    if (PlayerHandler.players[playerId].playerName != null) clans[clanId].members[j] = playerId;

                    Client c = (Client) PlayerHandler.players[playerId];
                    c.chatId = clanId;
                    PlayerHandler.players[playerId].clanId = clanId;
                    //System.out.println(c.lastClanJoined);
                    if (isGeneral(c)) {
                        messageToClan("The clan chat general " + (PlayerHandler.players[playerId].playerName) + " has just logged in.", clanId);
                        updateClanChat(clanId);
                    }
                    if (isOwner(c)) {
                        messageToClan("The clan chat owner " + (PlayerHandler.players[playerId].playerName) + " has just logged in.", clanId);
                        updateClanChat(clanId);
                    }
                    if (!isOwner(c) && !isGeneral(c)) {
                        messageToClan("" + (PlayerHandler.players[playerId].playerName) + " has joined the clan chat channel " + (clans[clanId].name) + ".", clanId);
                        updateClanChat(clanId);
                    }


                    //@SuppressWarnings("unused")
                    //c.sendMessage("You have joined the clan chat: " + clans[clanId].name);


                    c.lastClanJoined = clanId;
                    c.clanId2 = clanId;
                    c.clanId = clanId;
                    c.hasAClan = true;
                    c.clanName = clans[clanId].name;
                    if (isOwner(c)) {
                        c.sendMessage("You join your clan chat.");
                        if (clans[clanId].captain.equalsIgnoreCase("None")) {
                            c.sendMessage("You do not have a clan general. Use ::setgen #Player to promote a clan member!");
                            c.sendMessage("Clan generals have the power to kick/ban and mute clan chat members.");
                            c.lastClanJoined = clanId;
                            c.clanId2 = clanId;
                            c.clanId = clanId;
                            c.hasAClan = true;
                            c.clanName = clans[clanId].name;
                        } else {
                            c.lastClanJoined = clanId;
                            c.clanId2 = clanId;
                            c.clanId = clanId;
                            c.hasAClan = true;
                            c.clanName = clans[clanId].name;
                            c.sendMessage("Clan chat general: " + (clans[clanId].captain) + ". To demote, type ::delgen");

                        }

                    } else {
                        c.sendMessage("Attempting to join channel...");
                        c.sendMessage("Now talking in channel: <col=124214>" + (c.clanName) + "</col>");
                        c.sendMessage("To talk, start each line of chat with the / symbol.");

                    }
                    updateClanChat(clanId);
                    c.SaveGame();
                    c.notNow = false;
                    c.lastClanJoined = clanId;
                    c.clanId2 = clanId;
                    c.clanId = clanId;
                    c.hasAClan = true;
                    c.clanName = clans[clanId].name;
                    return;
                }
            }
        }
    }
    public String getLootshareStatus(int playerId, int clanId) {
        if (clans[clanId].lootshare) {
            return "enabled";
        }
        return "disabled";
    }
    public void handleClanOnLogout(int playerId, int clanId) {
        Client c = (Client) PlayerHandler.players[playerId];
        if (c == null) {
            return;
        }
        if (c.clanId == -1) {
            return;
        }
        if (clanId >= 100 || c.clanId >= 100) {
            return;
        }
        if (clans[clanId] == null) {
            return;
        }
        c.getPA().clearClanChatv2();
        updateClanChat(clanId);
        c.hasAClan = true;
        c.clanName = "" + clans[clanId].name + "";
        c.lastClanJoined = clanId;
        c.clanId = clanId;
        c.clanId2 = clanId;
        if (isOwner(c)) {
            messageToClan("The clan chat owner " + (c.playerName) + " has logged out.", clanId);

            c.doNotCCLog = true;
            c.hasAClan = true;
            c.clanName = "" + clans[clanId].name + "";
            c.lastClanJoined = clanId;
            c.clanId = clanId;
            c.clanId2 = clanId;
        }
        if (isGeneral(c)) {
            //System.out.println("LOL2");
            messageToClan("The clan chat general " + (c.playerName) + " has logged out.", clanId);
            c.doNotCCLog = true;
            c.hasAClan = true;
            c.clanName = "" + clans[clanId].name + "";
            c.lastClanJoined = clanId;
            c.clanId = clanId;
            c.clanId2 = clanId;
        }
        /*if(!isOwner(c) && !isGeneral(c)) {
				messageToClan("The clan chat member "+(c.playerName)+" has logged out.", clanId);
			}*/
        for (int j = 0; j < clans[clanId].members.length; j++) {
            if (clans[clanId].members[j] == playerId) {
                clans[clanId].members[j] = -1;
            }
        }
        updateClanChat(clanId);

        c.doNotCCLog = true;
        c.hasAClan = true;
        c.clanName = "" + clans[clanId].name + "";
        c.lastClanJoined = clanId;
        c.clanId = clanId;
        c.clanId2 = clanId;

        c.doNotCCLog = true;
        c.hasAClan = true;
        c.clanName = "" + clans[clanId].name + "";
        c.lastClanJoined = clanId;
        c.clanId = clanId;
        c.clanId2 = clanId;
    }
    public void leaveClan(int playerId, int clanId) {
        if (clanId < 0) {
            Client c = (Client) PlayerHandler.players[playerId];
            if (c != null) c.sendMessage("You are not in a clan.");
            return;
        }
        if (clans[clanId] != null) {
            Client c = (Client) PlayerHandler.players[playerId];
            if (PlayerHandler.players[playerId] != null) {

                c.clanId = clanId;
                if (c.doNotCCLog == false && c.hasAClan && isOwner(c) && !c.clanName.equalsIgnoreCase("None")) {
                    sendMsgOnce = 0;
                    deleteClan(clanId, true);
                    return;
                }

                if (isGeneral(c) && !c.doNotCCLog) {
                    messageToClan("The clan chat general " + (c.playerName) + " has left the clan chat channel.", clanId);
                    deleteGeneral(c);
                    leaveClan(c.playerId, clanId);
                    c.clanId = -1;
                    c.clanName = "None";
                    c.lastClanJoined = 0;
                    c.clanId2 = -1;

                    c.getPA().clearClanChat();
                    c.hasAClan = false;
                    for (int j = 0; j < clans[clanId].members.length; j++) {
                        if (clans[clanId].members[j] == playerId) {
                            clans[clanId].members[j] = -1;
                        }
                    }
                    updateClanChat(clanId);
                    return;
                }
                if (!c.doNotCCLog) {
                    if (sendMsgOnce == 0 && !isGeneral(c) && !isOwner(c)) {
                        sendMsgOnce += 1;
                        c.getPA().clearClanChat();
                        sendMsgOnce = 0;
                        c.sendMessage("You have left the clan " + (clans[clanId].name) + ".");

                        c.clanId = -1;
                        c.clanName = "None";
                        c.lastClanJoined = 0;
                        c.clanId2 = -1;
                        c.hasAClan = false;
                        for (int j = 0; j < clans[clanId].members.length; j++) {
                            if (clans[clanId].members[j] == playerId) {
                                clans[clanId].members[j] = -1;
                            }
                        }
                        updateClanChat(clanId);
                        messageToClan("The clan chat member " + (c.playerName) + " has left your clan chat channel " + (clans[clanId].name) + ".", clanId);
                        return;
                    }

                    c.getPA().clearClanChat();
                    sendMsgOnce = 0;
                    c.sendMessage("You have left the clan " + (clans[clanId].name) + ".");

                    messageToClan("The clan chat member " + (c.playerName) + " has left your clan chat channel " + (clans[clanId].name) + ".", clanId);

                    c.clanId = -1;
                    c.clanName = "None";
                    c.lastClanJoined = 0;
                    c.clanId2 = -1;
                    c.hasAClan = false;
                    //System.out.println("hey2 "+c.clanId+"");
                    for (int j = 0; j < clans[clanId].members.length; j++) {
                        if (clans[clanId].members[j] == playerId) {
                            clans[clanId].members[j] = -1;
                        }
                    }
                }



            }
            updateClanChat(clanId);
        } else {
            Client c = (Client) PlayerHandler.players[playerId];
            //System.out.println("FUCKED UP");
            c.clanId = -1;
            c.hasAClan = false;
            c.lastClanJoined = 0;
            c.clanId2 = -1;
            c.sendMessage("You are not in a clan.");
            //System.out.println("hey2 "+c.clanId+"");
        }
    }
    public void deleteClan(int clanId, Client c) {
        if (isOwner(c)) {
            c.clanId = -1;
            String clanName = clans[clanId].name;
            Connection.deleteFromFile("Data/Clans/" + clanName.toLowerCase() + ".ini", c.playerName);
        }

    }

    public void destructClan(int clanId) {
        if (clanId < 0) return;
        //System.out.println("hey");
        for (int j = 0; j < clans[clanId].members.length; j++) {
            if (clanId < 0) continue;
            if (clans[clanId].members[j] <= 0) continue;
            if (PlayerHandler.players[clans[clanId].members[j]] != null) {
                Client c = (Client) PlayerHandler.players[clans[clanId].members[j]];
                if (!c.doNotCCLog) c.clanId = -1;
                c.clanId2 = -1;
                c.lastClanJoined = 0;
                c.hasAClan = false;
                c.getPA().clearClanChat();
            }
        }
        clans[clanId].members = new int[50];
        clans[clanId].owner = "";
        clans[clanId].name = "";
        clans[clanId].password = null;
        clans[clanId].hasPassword = false;
    }
    public int sendMsgOnce = 0;
    public void deleteClan(int clanId, boolean message) {
        if (clanId < 0) return;
        for (int j = 0; j < clans[clanId].members.length; j++) {
            if (clanId < 0) continue;
            if (clans[clanId].members[j] <= 0) continue;
            if (PlayerHandler.players[clans[clanId].members[j]] == null) {
                //System.out.println("null");
            }
            if (PlayerHandler.players[clans[clanId].members[j]] != null) {
                Client c = (Client) PlayerHandler.players[clans[clanId].members[j]];
                if (message && sendMsgOnce == 0) messageToClan("The clan chat owner deleted the clan chat.", clanId);
                sendMsgOnce = 1;
                c.clanId = -1;
                c.clanId2 = -1;
                c.lastClanJoined = 0;
                c.hasAClan = false;
                c.clanName = "None";
                c.getPA().clearClanChat();
            }
        }
        clans[clanId].members = new int[50];
        clans[clanId].owner = "";
        clans[clanId].name = "";
        clans[clanId].password = null;
        clans[clanId].hasPassword = false;

    }
    public void messageToClan(String message, int clanId) {
        if (clanId < 0) return;
        for (int j = 0; j < clans[clanId].members.length; j++) {
            if (clans[clanId].members[j] < 0) continue;
            if (PlayerHandler.players[clans[clanId].members[j]] != null) {
                Client c = (Client) PlayerHandler.players[clans[clanId].members[j]];
                c.sendMessage("" + message + "");
            }
        }
    }
    public void messageToClan24(String message, int clanId) {
        if (clanId < 0) return;
        for (int j = 0; j < clans[clanId].members.length; j++) {
            if (clans[clanId].members[j] < 0) continue;
            if (PlayerHandler.players[clans[clanId].members[j]] != null) {
                Client c = (Client) PlayerHandler.players[clans[clanId].members[j]];
                c.sendMessage("" + message + "");
            }
        }
    }


    public void playerMessageToClan(int playerId, String message, int clanId) {
        if (clanId < 0) return;
        if (Connection.isMuted((Client) PlayerHandler.players[playerId]) && !isOwner((Client) PlayerHandler.players[playerId]) && !isProjectEquinoxStaff((Client) PlayerHandler.players[playerId]) && !isGeneral((Client) PlayerHandler.players[playerId])) {
            Client c = (Client) PlayerHandler.players[playerId];
            c.sendMessage("You are muted and are not permitted to speak!");
            return;
        }
        if (isClanMuted((Client) PlayerHandler.players[playerId])) {
            Client c = (Client) PlayerHandler.players[playerId];
            c.sendMessage("You are muted in this clan and are not permitted to speak!");
            return;
        }
        if (clans[clanId] == null) {
            return;
        }
        for (int j = 0; j < clans[clanId].members.length; j++) {
            if (clans[clanId].members[j] <= 0) continue;
            if (PlayerHandler.players[clans[clanId].members[j]] != null) {
                Client c = (Client) PlayerHandler.players[clans[clanId].members[j]];
            }
            //c.sendClan(PlayerHandler.players[playerId].playerName, message, clans[clanId].name, PlayerHandler.players[playerId].playerRights);
            //c.sendMessage("<shad=15494925>[Channel: "+(clans[c.clanId].name)+"] - "+(Server.playerHandler.players[playerId].playerName)+": "+message+"");
            Client PLAYERc = (Client) PlayerHandler.players[playerId];

            if (isOwner(PLAYERc)) {
                messageToClan24("[ <col=002B84>Owner</col> ][ <col=002B84>" + Misc.optimizeText(clans[clanId].name) + "</col> ] <col=002B84>" + (Server.playerHandler.players[playerId].playerName) + "</col>: " + (message) + "", clanId);
                return;
            }
            if (isGeneral(PLAYERc)) {
                messageToClan24("[<col=0B7747>General</col>][ <col=0B7747>" + Misc.optimizeText(clans[clanId].name) + "</col> ] <col=0B7747>" + (Server.playerHandler.players[playerId].playerName) + "</col>: " + (message) + "", clanId);
                return;
            }
            if (isProjectEquinoxStaff(PLAYERc)) {
                messageToClan24("[ <col=990000>Project Equinox Staff </col>][ <col=990000>" + Misc.optimizeText(clans[clanId].name) + " </col>] <col=990000>" + (Server.playerHandler.players[playerId].playerName) + "</col>: " + (message) + "", clanId);
                return;
            }
            if (!isProjectEquinoxStaff(PLAYERc) && !isOwner(PLAYERc) && !isGeneral(PLAYERc)) {
                messageToClan24("[ <col=255>Member </col>][ <col=255>" + Misc.optimizeText(clans[clanId].name) + " </col>] <col=255>" + (Server.playerHandler.players[playerId].playerName) + "</col>: " + (message) + "", clanId);
                return;
            }
        }
    }

    public boolean goodDistance(int x, int y, int x2, int y2, int distance) {
        int xdiff = (x > x2 ? x - x2 : x2 - x);
        int ydiff = (y > y2 ? y - y2 : y2 - y);
        if (xdiff <= distance && ydiff <= distance) return true;
        return false;
    }
    public void sendLootShareMessage(int clanId, String message) {
        if (clanId >= 0) {
            for (int j = 0; j < clans[clanId].members.length; j++) {
                if (clans[clanId].members[j] <= 0) continue;
                if (PlayerHandler.players[clans[clanId].members[j]] != null) {
                    Client c = (Client) PlayerHandler.players[clans[clanId].members[j]];
                    c.sendMessage(message);
                }
            }
        }
    }

    public void handleLootShare(Client c, int item, int amount) {
        if (c != null && Server.clanChat.clans[c.clanId] != null) {
            if (Server.clanChat.clans[c.clanId].lootshare) {
                //if (c.getShops().getItemShopValue(item) > 10000) { // only send msg if item is more then 50k
                //sendLootShareMessage(c.clanId, "<shad=15494925>[Channel: "+(clans[c.clanId].name)+"] - "+(c.playerName)+" has received " + amount + "x " + server.model.items.Item.getItemName(item) + ".");	
                //sendLootShareMessage(c.clanId, "<shad=15494925>[LC] "+(c.playerName)+" has received " + amount + "x " + server.model.items.Item.getItemName(item) + ".");	

                handleLootShare(c, item, amount, c.absX, c.absY);

            }
            //}
        }
    }

    public void handleLootShare(Client c, int item, int amount, int x, int y) {
        try {
            if (c.clanId < 0) return;
            ArrayList < Client > killers = new ArrayList < Client > ();
            for (int i = 0; i < clans[c.clanId].members.length; i++) {
                if (clans[c.clanId].members[i] > 0) {
                    Client killer = (Client) Server.playerHandler.players[clans[c.clanId].members[i]];
                    if (goodDistance(killer.absX, killer.absY, x, y, 16)) {
                        killers.add(killer);
                    } else {
                        //The player is alone, still send LS Msg but only if wealth is more then 9k:)
                        if (c.getShops().getItemShopValue(item, 1, c.getItems().getItemSlot(item)) > 9000) 
						sendLootShareMessage(c.clanId, "<shad=60141.45>[LS] " + (c.playerName) + " has received " + amount + "x " + server.model.items.Item.getItemName(item) + ".");

                    }
                }
            }
            if (killers.size() > 1) {
                int random = Misc.random(killers.size());
                Client winner = killers.get(random);
                if (c.getShops().getItemShopValue(item, 1, c.getItems().getItemSlot(item)) > 500000) handleCoinShare(c, item, amount, x, y);
                else {
                    if (winner != null && c != null) if (msg > -1 && msg < 2) {
                        sendLootShareMessage(c.clanId, "<shad=60141.45>[LS] " + (winner.playerName) + " has received " + amount + "x " + server.model.items.Item.getItemName(item) + ".");
                        msg += 1;
                    }

                    Server.itemHandler.createGroundItem(winner, item, x, y, amount, winner.playerId);
                    msg = 0;
                }
                return;
            }
            Server.itemHandler.createGroundItem(c, item, x, y, amount, c.playerId);
        } catch (Exception e) {}
    }
    public int msg = -1;
    public void handleCoinShare(Client c, int item, int amount, int xpos, int ypos) {
        try {
            if (c.clanId >= 0) {
                for (int j = 0; j < clans[c.clanId].members.length; j++) {
                    if (clans[c.clanId].members[j] <= 0) continue;
                    if (Server.playerHandler.players[clans[c.clanId].members[j]] != null) {
                        Client x = (Client) Server.playerHandler.players[clans[c.clanId].members[j]];
                        int total = c.getShops().getItemShopValue(item) / j;
                        Server.itemHandler.createGroundItem(x, 995, xpos, ypos, total, x.playerId);
                        x.sendMessage("<col=1532693>You received " + total + " gold as your split of this drop: " + amount + " x " + server.model.items.Item.getItemName(item) + ".</col>");

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void changeLootshareStatus(int playerId, int clanId) {
        Client c = (Client) PlayerHandler.players[playerId];
        if (c != null && clans[clanId] != null) {
            if (!isOwner(c) && !isGeneral(c)) {
                c.sendMessage("You must be a clan owner or general to do that...");
                return;
            }
            if (isOwner(c) || isGeneral(c)) {
                if (!clans[clanId].lootshare) {
                    if (isGeneral(c)) {
                        messageToClan("[Clan: " + (c.clanName) + " ] Clan Owner " + (clans[clanId].owner) + " has enabled lootshare!", clanId);
                    } else {
                        if (isOwner(c)) {
                            messageToClan("[Clan: " + (c.clanName) + " ] Clan Owner " + (clans[clanId].owner) + " has enabled lootshare!", clanId);

                        }
                    }
                    clans[clanId].lootshare = true;
                    PJClans.saveClan(clans[clanId].owner, clans[clanId].name, clans[clanId].captain, null, true, false);
                } else {
                    if (isGeneral(c)) {
                        messageToClan("[Clan: " + (c.clanName) + " ] Clan Owner " + (clans[clanId].owner) + " has disabled lootshare!", clanId);

                    } else {
                        if (isOwner(c)) {
                            messageToClan("[Clan: " + (c.clanName) + " ] Clan Owner " + (clans[clanId].owner) + " has disabled lootshare!", clanId);

                        }
                    }
                    clans[clanId].lootshare = false;
                    PJClans.saveClan(clans[clanId].owner, clans[clanId].name, clans[clanId].captain, null, false, false);
                }

            }
        }
    }


    public boolean isGeneral(Client c) {
        if (clans[c.clanId].captain.equalsIgnoreCase(c.playerName)) {
            return true;
        }
        return false;
    }

}