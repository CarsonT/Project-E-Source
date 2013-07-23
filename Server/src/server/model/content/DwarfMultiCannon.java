package server.model.content;

import server.Server;
import server.model.players.Client;
import server.model.npcs.NPC;
import server.model.npcs.NPCHandler;
import server.model.objects.Objects;
import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.event.Event;
import server.util.Misc;
import server.World;

//import server.model.minigames.ClanWars;

public class DwarfMultiCannon {

    /**
     * To-Do: Exception when trying to set up a cannon within 3 coords of another one
     *            NPC distance checking
     *	       Projectiles(Not sure if it works)
     *
     * @author relex lawl / relex
     */

    private static Client player;
    public DwarfMultiCannon(Client client) {
        DwarfMultiCannon.player = client;
    }
    private static final int CANNON_BASE = 7, CANNON_STAND = 8, CANNON_BARRELS = 9, CANNON = 6;
    private static final int CANNONBALL = 2, CANNON_BASE_ID = 6, CANNON_STAND_ID = 8, CANNON_BARRELS_ID = 10, CANNON_FURNACE_ID = 12;
	
	public static void um() {
		setUpCannon();
	}

    public static void setUpCannon() {
        //if (!canSetUpCannon())
            //return;
       World.getWorld().submit(new Event(150) {
            int time = 4;
           @Override
				public void execute() {
                //if (!canSetUpCannon())
                    //setup.stop();
                switch (time) {
                    case 4:
                        if (!player.getItems().playerHasItem(CANNON_BASE_ID))
                            stop();
                        player.startAnimation(827);
                        player.hasCannon = true;
                        player.settingUpCannon = true;
                        player.setUpBase = true;
                        Objects base = new Objects(CANNON_BASE, player.absX, player.absY, 0, 0, 10, 0);
                        Server.objectHandler.addObject(base);
                        Server.objectHandler.placeObject(base);
                        player.oldCannon = base;
                        player.getItems().deleteItem(CANNON_BASE_ID, 1);
                        base.belongsTo = player.playerName;
                        break;

                    case 3:
                        if (!player.getItems().playerHasItem(CANNON_STAND_ID)) {
                            player.settingUpCannon = false;
                            stop();
                        }
                        player.startAnimation(827);
                        player.setUpStand = true;
                        Objects stand = new Objects(CANNON_STAND, player.absX, player.absY, 0, 0, 10, 0);
                        Server.objectHandler.removeObject(player.oldCannon);
                        Server.objectHandler.addObject(stand);
                        Server.objectHandler.placeObject(stand);
                        player.oldCannon = stand;
                        player.getItems().deleteItem(CANNON_STAND_ID, 1);
                        stand.belongsTo = player.playerName;
                        break;

                    case 2:
                        if (!player.getItems().playerHasItem(CANNON_BARRELS_ID)) {
                            player.settingUpCannon = false;
                            stop();
                        }
                        player.startAnimation(827);
                        player.setUpBarrels = true;
                        Objects barrel = new Objects(CANNON_BARRELS, player.absX, player.absY, 0, 0, 10, 0);
                        Server.objectHandler.removeObject(player.oldCannon);
                        Server.objectHandler.addObject(barrel);
                        Server.objectHandler.placeObject(barrel);
                        player.oldCannon = barrel;
                        player.getItems().deleteItem(CANNON_BARRELS_ID, 1);
                        barrel.belongsTo = player.playerName;
                        break;

                    case 1:
                        if (!player.getItems().playerHasItem(CANNON_FURNACE_ID)) {
                            player.settingUpCannon = false;
                            stop();
                        }
                        player.startAnimation(827);
                        player.setUpFurnace = true;
                        Objects cannon = new Objects(CANNON, player.absX, player.absY, 0, 0, 10, 0);
                        player.cannonBaseX = player.absX;
                        player.cannonBaseY = player.absY;
                        player.cannonBaseH = player.heightLevel;
                        Server.objectHandler.removeObject(player.oldCannon);
                        Server.objectHandler.addObject(cannon);
                        Server.objectHandler.placeObject(cannon);
                        player.oldCannon = cannon;
                        player.getItems().deleteItem(CANNON_FURNACE_ID, 1);
                        cannon.belongsTo = player.playerName;
                        break;

                    case 0:
                        player.settingUpCannon = false;
                        stop();
                        break;
                }
                if (time > 0)
                    time--;
            }
        });
    }

    public static void shootCannon() {
        Objects cannon = null;
        for(server.model.objects.Objects o: Server.objectHandler.globalObjects) {
            if (o.objectX == player.cannonBaseX && o.objectY == player.cannonBaseY && o.objectHeight == player.cannonBaseH) {
                cannon = o;
            }
        }
        if (cannon == null) {
            player.sendMessage("This is not your cannon!");
            return;
        }
        if (player.cannonIsShooting) {
            if (player.getItems().playerHasItem(CANNONBALL)) {
                int amountOfCannonBalls = player.getItems().getItemAmount(CANNONBALL) > 30 ? 30 : player.getItems().getItemAmount(CANNONBALL);
                player.cannonBalls += amountOfCannonBalls;
            } else {
                player.sendMessage("Your cannon is already firing!");
                return;
            }
        }
        if (player.cannonBalls < 1) {
            int amountOfCannonBalls = player.getItems().getItemAmount(CANNONBALL) > 30 ? 30 : player.getItems().getItemAmount(CANNONBALL);
            if (amountOfCannonBalls < 1) {
                player.sendMessage("You need ammo to shoot this cannon!");
                return;
            }
            player.cannonBalls = amountOfCannonBalls;
            player.getItems().deleteItem(CANNONBALL, player.getItems().getItemSlot(CANNONBALL), amountOfCannonBalls);
        } else
            startFiringCannon(cannon);
    }

    private static void startFiringCannon(final Objects cannon) {
        player.cannonIsShooting = true;
        World.getWorld().submit(new Event(200) {
            @Override
				public void execute() {
                if (player.cannonBalls < 1) {
                    player.sendMessage("Your cannon has run out of ammo!");
                    player.cannonIsShooting = false;
                    stop();
                } else {
                    player.rotation++;
                    rotateCannon(cannon);
                }
            }
        });
    }

    private static void rotateCannon(Objects cannon) {
        switch (player.rotation) {
            case 1: //north
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 516, 10, -1);
                break;
            case 2: //north-east
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 517, 10, -1);
                break;
            case 3: //east
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 518, 10, -1);
                break;
            case 4: //south-east
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 519, 10, -1);
                break;
            case 5: //south
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 520, 10, -1);
                break;
            case 6: //south-west
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 521, 10, -1);
                break;
            case 7: //west
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 514, 10, -1);
                break;
            case 8: //north-west
                player.getPA().objectAnim(cannon.objectX, cannon.objectY, 515, 10, -1);
                player.rotation = 0;
                break;
        }
    }

    public static void pickUpCannon() {
        Objects cannon = null;
        for(server.model.objects.Objects o: Server.objectHandler.globalObjects) {
            if (o.objectX == player.cannonBaseX && o.objectY == player.cannonBaseY && o.objectHeight == player.cannonBaseH) {
                cannon = o;
            }
        }
        if (cannon == null) {
            player.sendMessage("This is not your cannon!");
            return;
        }
        player.startAnimation(827);
        server.model.objects.Objects empty = new server.model.objects.Objects(100, cannon.objectX, cannon.objectY, 0, 0, 10, 0);
        Server.objectHandler.addObject(empty);
        Server.objectHandler.placeObject(empty);
        Server.objectHandler.removeObject(empty);
        if (player.setUpBase) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_BASE_ID, 1);
            else {
                player.getItems().addItemToBank(CANNON_BASE_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpBase = false;
        }
        if (player.setUpStand) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_STAND_ID, 1);
            else {
                player.getItems().addItemToBank(CANNON_STAND_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpStand = false;
        }
        if (player.setUpBarrels) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_BARRELS_ID, 1);
            else {
                player.getItems().addItemToBank(CANNON_BARRELS_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpBarrels = false;
        }
        if (player.setUpFurnace) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_FURNACE_ID, 1);
            else {
                player.getItems().addItemToBank(CANNON_FURNACE_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpFurnace = false;
        }
        if (player.cannonBalls > 0) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNONBALL, player.cannonBalls);
            else {
                player.getItems().addItemToBank(CANNONBALL, player.cannonBalls);
                player.sendMessage("You did not have enough inventory space, so your cannonballs have been banked.");
            }
            player.cannonBalls = 0;
        }
    }

    public static void checkNPCDistance() {
        for (Player p : PlayerHandler.players) {
            if (p == null)
                return;
            Client players = (Client) p;
            NPC n = getNPCWithinDistance(players, players.cannonBaseX, players.cannonBaseY, players.cannonBaseH);
            int damage = Misc.random(30);
            if (players.inMulti() && n.inMulti()) {
                startCannonballProjectile(players, players.oldCannon, n);
                n.hitDiff = damage;
                n.HP -= damage;
                n.hitUpdateRequired = true;
                n.killerId = players.playerId;
                n.facePlayer(players.playerId);
                n.forceChat("im hit, multi");
            } else {
                if (n.underAttackBy > 0 && n.underAttackBy != players.playerId)
                    return;
                startCannonballProjectile(players, players.oldCannon, n);
                n.hitDiff = damage;
                n.HP -= damage;
                n.hitUpdateRequired = true;
                n.killerId = players.playerId;
                n.facePlayer(players.playerId);
                n.forceChat("im hit, single");
            }
            players.cannonBalls--;
        }
    }

    private static NPC getNPCWithinDistance(Client players, int x, int y, int height) {
        NPC npc = null;
        for (int i = 0; i < NPCHandler.npcs.length; i++) {
            if (NPCHandler.npcs[i] != null)
                npc = (NPC) NPCHandler.npcs[i];
        }
        int myX = players.cannonBaseX;
        int myY = players.cannonBaseY;
        int theirX = npc.absX;
        int theirY = npc.absY;
        if (!npc.isDead && npc.heightLevel == height && !npc.isDead && npc.HP != 0 && npc.npcType != 1266 && npc.npcType != 1268) {
            switch (players.rotation) {
                case 1: //north
                    if(theirY > myY && theirX >= myX - 1 && theirX <= myX + 1)
                        return npc;
                    break;
                case 2: //north-east
                    if(theirX >= myX + 1 && theirY >= myY + 1)
                        return npc;
                    break;
                case 3: //east
                    if(theirX > myX && theirY >= myY - 1 && theirY <= myY + 1)
                        return npc;
                    break;
                case 4: //south-east
                    if(theirY <= myY - 1 && theirX >= myX + 1)
                        return npc;
                    break;
                case 5: //south
                    if(theirY < myY && theirX >= myX - 1 && theirX <= myX + 1)
                        return npc;
                    break;
                case 6: //south-west
                    if(theirX <= myX - 1 && theirY <= myY - 1)
                        return npc;
                    break;
                case 7: //west
                    if(theirX < myX && theirY >= myY - 1 && theirY <= myY + 1)
                        return npc;
                    break;
                case 8: //north-west
                    if(theirX <= myX - 1 && theirY >= myY + 1)
                        return npc;
                    break;
            }
        }
        return null;
    }

    private static void startCannonballProjectile(Client player, Objects cannon, NPC n) {
        int oX = cannon.objectX;
        int oY = cannon.objectY;
        int offX = ((oX - n.getX()) * -1);
        int offY = ((oY - n.getY()) * -1);
        player.getPA().createPlayersProjectile(oX, oY, offY, offX, 50, 60, 53, 20, 20, - player.oldNpcIndex + 1, 30);
    }

    public static int distanceToSquare(int x, int y, int tx, int ty){
        return (int) Math.sqrt((Math.abs(x - tx) + Math.abs(y - ty)));
    }

    private boolean canSetUpCannon() {
        return  !player.hasCannon || !player.settingUpCannon;
    }

    private boolean inGoodArea() {
        return true;
    }
}