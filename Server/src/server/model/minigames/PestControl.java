package server.model.minigames;
 
import server.model.players.PlayerHandler;
import server.model.players.Client;
import server.model.npcs.NPCHandler;
import server.model.npcs.NPC;
import server.Server;
 
public class PestControl {
 
    public PestControl() {
 
    }
 
    public final int GAME_TIMER = 70;
    public final int WAIT_TIMER = 7;
 
    public int gameTimer = -1;
    public int waitTimer = 15;
    public int properTimer = 0;
 
    public void process() {
        setInterface();
        if (properTimer > 0) {
            properTimer--;
            return;
        } else {
            properTimer = 4;
        }
        if (waitTimer > 0) waitTimer--;
        else if (waitTimer == 0) startGame();
        if (gameTimer > 0) {
            gameTimer--;
            if (allPortalsDead()) {
                endGame(true);
            }
        } else if (gameTimer == 0) endGame(false);
    }
 
    public static int[] portalHealth = {
        200,
        200,
        200,
        200
    };
 
    public void startGame() {
        if (playersInBoat() > 0) {
            gameTimer = GAME_TIMER;
            waitTimer = -1;
            spawnNpcs();
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    if (Server.playerHandler.players[j].inPcBoat()) {
                        movePlayer(j);
                    }
                }
            }
        } else {
            waitTimer = WAIT_TIMER;
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (Server.playerHandler.players[j] != null) {
                    if (Server.playerHandler.players[j].inPcBoat()) {
                        Client c = (Client) Server.playerHandler.players[j];
                        c.sendMessage("[ <col=2784FF>Pest Control</col> ] There need to be at least 2 players to start the game.");
 
                    }
                }
            }
        }
    }
 
 
    public void setInterface() {
        for (int j = 0; j < Server.playerHandler.players.length; j++) {
            if (Server.playerHandler.players[j] != null) {
                if (Server.playerHandler.players[j].inPcBoat()) {
                    Client c = (Client) Server.playerHandler.players[j];
                    c.getPA().sendFrame126("Next Departure: " + waitTimer + "", 21120);
                    c.getPA().sendFrame126("Players Ready: " + playersInBoat() + "", 21121);
                    c.getPA().sendFrame126("(Need 3 to 25 players)", 21122);
                    c.getPA().sendFrame126("Pc Points(Project-E points): " + c.pePoints + "", 21123);
                }
                if (Server.playerHandler.players[j].inPcGame()) {
                    Client c = (Client) Server.playerHandler.players[j];
                    updateGame();
                    if (c.pcDamage < 10) {
                        c.getPA().sendFrame126("@red@" + c.pcDamage + ".", 21116);
                    } else {
                        c.getPA().sendFrame126("@gre@" + c.pcDamage + ".", 21116);
                    }
                    c.getPA().sendFrame126("Time remaining: " + this.secondsToMinutesAndSeconds(this.gameTimer) + "!", 21117);
                }
            }
        }
    }
 
    public int getNPCHp(int type) {
        for (int n = 0; n < NPCHandler.maxNPCs; ++n) {
            NPC npc = NPCHandler.npcs[n];
            if (npc != null && npc.npcType == type) {
                return npc.HP;
            }
        }
 
        return 0;
    }
 
    private String secondsToMinutesAndSeconds(int seconds) {
        int minutes = seconds / 60;
        String time = minutes + " min " + (seconds - minutes * 60) + " seconds";
        return time;
    }
 
    private String secondsToMinutes(int seconds) {
        int minutes = seconds / 60;
        String time = minutes + " min";
        return time;
    }
 
    public void updateGame() {
        for (int j = 0; j < PlayerHandler.players.length; ++j) {
            if (PlayerHandler.players[j] != null && PlayerHandler.players[j].inPcGame()) {
                Client c = (Client) PlayerHandler.players[j];
                int voidHp = getNPCHp(3782);
                int bluePortal = getNPCHp(6143);
                int redPortal = getNPCHp(6145);
                int yellowPortal = getNPCHp(6144);
                int purplePortal = getNPCHp(6142);
                c.getPA().sendFrame126("W", 21107);
                c.getPA().sendFrame126("E", 21108);
                c.getPA().sendFrame126("SE", 21109);
                c.getPA().sendFrame126("SW", 21110);
                if (purplePortal > 50) {
                    c.getPA().sendFrame126("@gre@" + purplePortal, 21111);
                } else if (purplePortal < 50) {
                    c.getPA().sendFrame126("@red@" + purplePortal, 21111);
                } else if (purplePortal == 0) {
                    c.getPA().sendFrame126("@red@Dead", 21111);
                }
 
                if (bluePortal > 50) {
                    c.getPA().sendFrame126("@gre@" + bluePortal, 21112);
                } else if (bluePortal < 50) {
                    c.getPA().sendFrame126("@red@" + bluePortal, 21112);
                } else if (bluePortal == 0) {
                    c.getPA().sendFrame126("@red@Dead", 21112);
                }
 
                if (yellowPortal > 50) {
                    c.getPA().sendFrame126("@gre@" + yellowPortal, 21113);
                } else if (yellowPortal < 50) {
                    c.getPA().sendFrame126("@red@" + yellowPortal, 21113);
                } else if (yellowPortal == 0) {
                    c.getPA().sendFrame126("@red@Dead", 21113);
                }
 
 
                if (redPortal > 50) {
                    c.getPA().sendFrame126("@gre@" + redPortal, 21114);
                } else if (redPortal < 50) {
                    c.getPA().sendFrame126("@red@" + redPortal, 21114);
                } else if (redPortal == 0) {
                    c.getPA().sendFrame126("@red@Dead", 21114);
                }
 
                if (voidHp < 75) {
                    c.getPA().sendFrame126("@red@" + voidHp, 21115);
                } else {
                    c.getPA().sendFrame126("@gre@" + voidHp, 21115);
                }
 
                if (c.pcDamage < 50) {
                    c.getPA().sendFrame126("@red@" + c.pcDamage, 21116);
                } else {
                    c.getPA().sendFrame126("@gre@" + c.pcDamage, 21116);
                }
 
                c.getPA().sendFrame126("Time remaining: " + this.secondsToMinutesAndSeconds(this.gameTimer) + "!", 21117);
                c.getPA().sendFrame126("", 21118);
            }
        }
 
    }
 
    public int playersInBoat() {
        int count = 0;
        for (int j = 0; j < Server.playerHandler.players.length; j++) {
            if (Server.playerHandler.players[j] != null) {
                if (Server.playerHandler.players[j].inPcBoat()) {
                    count++;
                }
            }
        }
        return count;
    }
 
    public void endGame(boolean won) {
        gameTimer = -1;
        waitTimer = WAIT_TIMER;
        for (int j = 0; j < Server.playerHandler.players.length; j++) {
            if (Server.playerHandler.players[j] != null) {
                if (Server.playerHandler.players[j].inPcGame()) {
                    Client c = (Client) Server.playerHandler.players[j];
                    c.getPA().movePlayer(2657, 2639, 0);
                    if (won && c.pcDamage > 30) {
                        c.sendMessage("[ <col=2784FF>Pest Control</col> ] You have won the Minigame and won <col=2784FF>4</col> Project-Equinox Points!");
                        c.getPA().yell("[ <col=2784FF>Pest Control</col> ] The mingame has been won!");
                        c.pePoints += 4;
                        c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
                        c.playerLevel[5] = c.getLevelForXP(c.playerXP[5]);
                        c.specAmount = 10;
                        c.getItems().addItem(995, c.combatLevel * 50);
                        c.getPA().refreshSkill(3);
                        c.getPA().refreshSkill(5);
                    } else if (won) {
                        c.sendMessage("[ <col=2784FF>Pest Control</col> ] The void knights notice your lack of zeal.");
                    } else {
                        c.sendMessage("[ <col=2784FF>Pest Control</col> ] You failed to kill all the portals in 1 minute!");
                    }
                    c.pcDamage = 0;
                    c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
                    c.getCombat().resetPrayers();
                }
            }
        }
 
        for (int j = 0; j < Server.npcHandler.npcs.length; j++) {
            if (Server.npcHandler.npcs[j] != null) {
                if (Server.npcHandler.npcs[j].npcType >= 6142 && Server.npcHandler.npcs[j].npcType <= 6145) Server.npcHandler.npcs[j] = null;
            }
        }
    }
 
    public boolean allPortalsDead() {
        int count = 0;
        for (int j = 0; j < Server.npcHandler.npcs.length; j++) {
            if (Server.npcHandler.npcs[j] != null) {
                if (Server.npcHandler.npcs[j].npcType >= 6142 && Server.npcHandler.npcs[j].npcType <= 6145) if (Server.npcHandler.npcs[j].needRespawn) count++;
            }
        }
        return count >= 4;
    }
 
    public void movePlayer(int index) {
        Client c = (Client) Server.playerHandler.players[index];
        if (c.combatLevel < 40) {
            c.sendMessage("[ <col=2784FF>Pest Control</col> ] You must be at least combat level 40 to enter this boat.");
            return;
        }
        c.getPA().movePlayer(2658, 2611, 0);
        c.sendMessage("[ <col=2784FF>Pest Control</col> ] The game has started! Goodluck!");
    }
 
 
 
    public void spawnNpcs() {
        Server.npcHandler.spawnNpc2(6142, 2628, 2591, 0, 0, 200, 0, 0, 100);
        Server.npcHandler.spawnNpc2(6143, 2680, 2588, 0, 0, 200, 0, 0, 100);
        Server.npcHandler.spawnNpc2(6144, 2669, 2570, 0, 0, 200, 0, 0, 100);
        Server.npcHandler.spawnNpc2(6145, 2645, 2569, 0, 0, 200, 0, 0, 100);
        Server.npcHandler.spawnNpc2(3782, 2656, 2592, 0, 0, 200, 0, 0, 100);
        Server.npcHandler.spawnNpc2(3732, 2657, 2579, 0, 1, 400, 10, 200, 100);
        Server.npcHandler.spawnNpc2(3734, 2667, 2575, 0, 1, 400, 10, 100, 100);
        Server.npcHandler.spawnNpc2(3736, 2679, 2591, 0, 1, 400, 10, 200, 100);
        Server.npcHandler.spawnNpc2(3738, 2631, 2594, 0, 1, 400, 10, 200, 100);
        Server.npcHandler.spawnNpc2(3738, 2632, 2591, 0, 1, 400, 10, 200, 100);
        Server.npcHandler.spawnNpc2(3734, 2657, 2606, 0, 1, 400, 10, 200, 100);
        Server.npcHandler.spawnNpc2(3734, 2659, 2606, 0, 1, 200, 10, 200, 100);
    }
 
 
}