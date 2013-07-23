package server.model.minigames;
 
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.util.Misc;
import server.Server;
 
public class MonsterKiller {
 
	/*Iquit this shit*/
 
 
    public MonsterKiller() {
    }
 
    public final int GAME_TIMER = 70; //5 minutes
    public final int WAIT_TIMER = 15;
 
    public int gameTimer = -1;
    public int waitTimer = 15;
    public int properTimer = 0;
    public void process() {
        if (properTimer > 0) {
            properTimer--;
            return;
        } else {
            properTimer = 4;
        }
        if (waitTimer > 0) 
			waitTimer--;
        else if (waitTimer == 0) 
			startGame();
        if (gameTimer > 0) {
            gameTimer--;
            if (allDragonsDead()) {
                endGame(true);
            } else if (gameTimer == 0) 
				endGame(false);
            if (gameTimer == 70) 
				gameTimer--;
            else if (gameTimer == 60) {
                minionSpawn();
            } else if (gameTimer == 10) {
                almostOverMessage();
            }
        }
    }
 
    public void startGame() {
        if (playersInArea() > 0) {
            gameTimer = GAME_TIMER;
            waitTimer = -1;
            //spawn npcs
            SpawnMain();
            //move players into game
            for (int j = 0; j < PlayerHandler.players.length; j++) {
                if (PlayerHandler.players[j] != null) {
                    if (PlayerHandler.players[j].inDkArea()) {
                        Client c = (Client) PlayerHandler.players[j];
                        c.sendMessage("[ <col=2784FF>Minigame </col>] Minigame has started! Good luck!");
                        //movePlayer(j);
                    }
                }
            }
        } else {
            waitTimer = WAIT_TIMER;
            for (int j = 0; j < PlayerHandler.players.length; j++) {
                if (PlayerHandler.players[j] != null) {
                    if (PlayerHandler.players[j].inDkArea()) {
                        Client c = (Client) PlayerHandler.players[j];
                        c.sendMessage("[ <col=2784FF>Minigame </col>] There need to be at least 3 players to start the minigame.");
                    }
                }
            }
        }
    }
	
    public boolean spawned;
	
    public boolean minionSpawn() {
        if (spawned) return true;
        gameTimer = GAME_TIMER;
        spawnExtra();
        spawned = true;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client c = (Client) PlayerHandler.players[j];
                c.sendMessage("[ <col=2784FF>Minigame </col>] Minions Spawned!");
            }
        }
        return false;
    }
	
    public int playersInArea() {
        int count = 0;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (PlayerHandler.players[j].inDkArea()) {
                    count++;
                }
            }
        }
        return count;
    }
 
    public void endGame(boolean won) {
        gameTimer = -1;
        waitTimer = WAIT_TIMER;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (PlayerHandler.players[j].inDkArea()) {
                    Client c = (Client) PlayerHandler.players[j];
                    c.getPA().movePlayer(3073, 3504, 0);
                    c.InMinigame = false;
                    if (won && c.dkDamage > 6) {
                        c.sendMessage("[ <col=2784FF>Minigame </col>] You have won the minigame. You recieved 5 points.");
                        c.Frost += 5;
                        c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
                        c.playerLevel[5] = c.getLevelForXP(c.playerXP[5]);
                        c.specAmount = 10;
                        c.getItems().addItem(995, c.combatLevel * 50);
                        c.getPA().refreshSkill(3);
                        c.getPA().refreshSkill(5);
                    } else {
                        c.sendMessage("[ <col=2784FF>Minigame </col>] You have failed to kill all monsters in 5 minutes.");
                    }
                    c.dkDamage = 0;
                    c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
                    c.getCombat().resetPrayers();
                }
            }
        }
        for (int j = 0; j < NPCHandler.npcs.length; j++) {
            if (NPCHandler.npcs[j] != null) {
                if (NPCHandler.npcs[j].npcType >= 53 && NPCHandler.npcs[j].npcType <= 55) NPCHandler.npcs[j] = null;
            }
        }
    }
	
    public void endGame2(boolean won) {
        gameTimer = GAME_TIMER;
        waitTimer = WAIT_TIMER;
        waitTimer = WAIT_TIMER;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (PlayerHandler.players[j].inDkArea()) {
                    Client c = (Client) PlayerHandler.players[j];
                    c.getPA().movePlayer(3073, 3504, 0);
                    c.InMinigame = false;
                    if (won && c.dkDamage > 3 && gameTimer > 61) {
                        c.sendMessage("[ <col=2784FF>Minigame </col>] You killed the dragons before the minions spawned. You gain 10 points.");
                        c.Frost += 10;
                        c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
                        c.playerLevel[5] = c.getLevelForXP(c.playerXP[5]);
                        c.specAmount = 10;
                        c.getItems().addItem(995, c.combatLevel * 50);
                        c.getPA().refreshSkill(3);
                        c.getPA().refreshSkill(5);
                    }
                }
            }
        }
        for (int j = 0; j < NPCHandler.npcs.length; j++) {
            if (NPCHandler.npcs[j] != null) {
                if (NPCHandler.npcs[j].npcType >= 53 && NPCHandler.npcs[j].npcType <= 55) NPCHandler.npcs[j] = null;
            }
        }
    }
	
    public boolean allDragonsDead() {
        int count = 0;
        for (int j = 0; j < NPCHandler.npcs.length; j++) {
            if (NPCHandler.npcs[j] != null) {
                if (NPCHandler.npcs[j].npcType >= 52 && NPCHandler.npcs[j].npcType <= 55) if (NPCHandler.npcs[j].needRespawn) count++;
            }
        }
        return count >= 6;
    }
	
    public boolean allDragonsDead2() {
        int count = 0;
        for (int j = 0; j < NPCHandler.npcs.length; j++) {
            if (NPCHandler.npcs[j] != null) {
                if (NPCHandler.npcs[j].npcType >= 53 && NPCHandler.npcs[j].npcType <= 55) if (NPCHandler.npcs[j].needRespawn) count++;
            }
        }
        return count >= 3;
    }
	
    public void deadRespawn() {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (PlayerHandler.players[j].inDkArea()) {
                    Client c = (Client) PlayerHandler.players[j];
                    c.getPA().movePlayer(3073, 3504, 0);
                    c.InMinigame = false;
                }
            }
        }
    }
 
    public void movePlayer(int index) {
        Client c = (Client) PlayerHandler.players[index];
        if (c.combatLevel < 50) {
            c.sendMessage("[ <col=2784FF>Minigame </col>] You must be at least 50 to enter this game.");
            return;
        }
    }
	
    public void almostOverMessage() {
        gameTimer = GAME_TIMER;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (PlayerHandler.players[j].inDkArea()) {
                    Client c = (Client) PlayerHandler.players[j];
                    c.sendMessage("[ <col=2784FF>Minigame </col>] Hurry up the minigame is almost over!");
                }
            }
        }
    }
	
    public void spawnExtra() {
		Server.npcHandler.spawnNpc2(6212, 2912, 3617, 0, 1, 10, 5, 200, 70);
		Server.npcHandler.spawnNpc2(914, 2916, 3609, 0, 1, 10, 5, 200, 70);
		Server.npcHandler.spawnNpc2(1926, 2911, 3613, 0, 1, 10, 5, 200, 70);
    }
	
    public void SpawnMain() {
		Server.npcHandler.spawnNpc2(117, 2904, 3615, 0, 1, 10, 5, 200, 70);
        Server.npcHandler.spawnNpc2(134, 2910, 3617, 0, 1, 10, 20, 250, 100);
        Server.npcHandler.spawnNpc2(1977, 2915, 3615, 0, 1, 10, 20, 300, 100);
        Server.npcHandler.spawnNpc2(158, 2910, 3610, 0, 1, 10, 20, 350, 100);
    }
 
	
}