package server.model.players;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.Future;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import org.apache.mina.common.IoSession;

import server.Connection;
import server.Config;
import server.Server;
import server.model.content.*;
import server.World;
import server.net.HostList;
import server.net.Packet;
import server.net.StaticPacketBuilder;
import server.event.EventManager;
import server.event.Event;
import server.util.Misc;
import server.util.Stream;
import server.util.MadTurnipConnection;
import server.util.DonationMysql;
import server.util.SQL;
import server.util.Vote;
import server.model.players.ConnectedFrom;
import server.model.items.ItemAssistant;
import server.model.shops.ShopAssistant;
import server.model.minigames.CastleWars;
import server.model.minigames.FreeForAll;
import server.model.players.ClueScroll;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.model.npcs.NPC;
import server.model.content.PotionMixing;
import server.model.minigames.PestControl;
import server.model.minigames.*;
import server.model.players.combat.CombatAssistant;

import server.model.skills.*;
import server.model.skills.agility.Agility;
import server.model.skills.crafting.Crafting;
import server.model.skills.cooking.Cooking;
import server.model.skills.farming.Farming;
import server.model.skills.firemaking.Firemaking;
import server.model.skills.fishing.Fishing;
import server.model.skills.fletching.Fletching;
import server.model.skills.herblore.Herblore;
import server.model.skills.prayer.Prayer;
import server.model.skills.mining.Mining;
import server.model.skills.runecrafting.Runecrafting;
import server.model.skills.slayer.Slayer;
import server.model.skills.smithing.Smithing;
import server.model.skills.smithing.SmithingInterface;
import server.model.skills.thieving.Thieving;
import server.model.skills.woodcutting.Woodcutting;


public class Client extends Player {

    public byte buffer[] = null;
    public Stream inStream = null, outStream = null;

    private ItemAssistant itemAssistant = new ItemAssistant(this);
    private ShopAssistant shopAssistant = new ShopAssistant(this);
    private TradeAndDuel tradeAndDuel = new TradeAndDuel(this);
    private PlayerAssistant playerAssistant = new PlayerAssistant(this);
    private CombatAssistant combatAssistant = new CombatAssistant(this);
    private ActionHandler actionHandler = new ActionHandler(this);
    private PlayerKilling playerKilling = new PlayerKilling(this);
    private Curse curse = new Curse(this);
    private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private KillingStreak killingStreak = new KillingStreak(this);
    private Potions potions = new Potions(this);
    private PotionMixing potionMixing = new PotionMixing(this);
    private TradeLog tradeLog = new TradeLog(this);
    private Food food = new Food(this);
    private ClueScroll clueScroll = new ClueScroll(this);
    public boolean stopPlayerSkill = false;
    public boolean isnotfighting;
	public TicketCenter ticketCenter = new TicketCenter(this);
	private Highscores highscores = new Highscores(this);
	private JoinDate join = new JoinDate(this);
	private PkReward pkreward = new PkReward(this);
	private SoF sof = new SoF(this);
	private Achievements achievements = new Achievements(this);
    /**
     * Skill instances
     */
    private Slayer slayer = new Slayer(this);
    //private Runecrafting runecrafting = new Runecrafting(this);
    private Woodcutting woodcutting = new Woodcutting(this);
    private Mining mine = new Mining(this);
    private Agility agility = new Agility(this);
    private Cooking cooking = new Cooking(this);
    //private Fishing fish = new Fishing(this);
    private Crafting crafting = new Crafting(this);
    private Smithing smith = new Smithing(this);
    private Prayer prayer = new Prayer(this);
    //private Fletching fletching = new Fletching(this);
    private SmithingInterface smithInt = new SmithingInterface(this);
    private Farming farming = new Farming(this);
    private Thieving thieving = new Thieving(this);
    private Firemaking firemaking = new Firemaking(this);
    //private Herblore herblore = new Herblore(this);
    //private Fishing fishing = new Fishing(this);
    private Fishing fish = new Fishing(this);

    private int somejunk;
    public int lowMemoryVersion = 0;
    public int timeOutCounter = 0;
    public int returnCode = 2;
    private Future <? > currentTask;

    public String lastKilled = "";
    public long lastAlch;
    public boolean usingCarpet = false;
    public int itemBeforeCarpet;
    public boolean slayerHelmetEffect;
    public int clawDamage;
    public int clawIndex;
    public int clawType = 0;
    public int clawDelay = 0;
	
	public String savedClan;
	public boolean hasAClan = false;
	public int clanId2;
	public int chatId = -1;
	public int lastClanJoined = 0;
	public boolean notNow = false;
	public boolean doNotSendClanMsg = false;
	public String clanPass;
	public boolean doNotCCLog;

    private DwarfMultiCannon cannon = new DwarfMultiCannon(this);
	
    public DwarfMultiCannon getCannon() {
        return cannon;
    }
	
	public void stillCamera(int x, int y, int height, int speed, int angle) {
		outStream.createFrame(177);
		outStream.writeByte(x / 64);
		outStream.writeByte(y / 64);
		outStream.writeWord(height);
		outStream.writeByte(speed);
		outStream.writeByte(angle);
	}

	public void spinCamera(int x, int y, int height, int speed, int angle) {
		outStream.createFrame(166);
		outStream.writeByte(x);
		outStream.writeByte(y);
		outStream.writeWord(height);
		outStream.writeByte(speed);
		outStream.writeByte(angle);
	}

	public void resetCamera() {
		outStream.createFrame(107);
		updateRequired = true;
		appearanceUpdateRequired = true;
	}		


    public int previousDamage;
    public boolean usingClaws = false;

    public Client getClient(int index) {
        return ((Client) PlayerHandler.players[index]);
    }
    public boolean validClient(int index) {
        Client p = (Client) PlayerHandler.players[index];
        if ((p != null) && !p.disconnected) {
            return true;
        }
        return false;
    }
	
public void declinePlayerTrades() {
	for (int j = 0; j < PlayerHandler.players.length; j++) {
		if (PlayerHandler.players[j] != null) {
			Client c2 = (Client)PlayerHandler.players[j];
			if (c2.inTrade) {
				c2.getTradeAndDuel().declineTrade();
			}
		}
	}
}	
	
    public void HighAndLow() {

        if (combatLevel < 15) {
            int Low = 3;
            int High = combatLevel + 12;
            getPA().sendFrame126("@gre@" + Low + "@yel@ - @dre@" + High + "", 199);
        }
        if (combatLevel > 15 && combatLevel < 114) {
            int Low = combatLevel - 12;
            int High = combatLevel + 12;
            getPA().sendFrame126("@gre@" + Low + "@yel@ - @dre@" + High + "", 199);
        }
        if (combatLevel > 114) {
            int Low = combatLevel - 12;
            int High = 126;
            getPA().sendFrame126("@gre@" + Low + "@yel@ - @dre@" + High + "", 199);
        }

    }
    public Client(IoSession s, int _playerId) {
        super(_playerId);
        this.session = s;
        synchronized(this) {
            outStream = new Stream(new byte[Config.BUFFER_SIZE]);
            outStream.currentOffset = 0;
        }
        inStream = new Stream(new byte[Config.BUFFER_SIZE]);
        inStream.currentOffset = 0;
        buffer = new byte[Config.BUFFER_SIZE];
    }

    public void FetchDice() {
        World.getWorld().submit(new Event(100) {
            public void execute() {
                int rnd;
                String Message = "";
                if (cDice == 0 || (System.currentTimeMillis() - diceDelay <= 1000)) {
                    return;
                }
                switch (cDice) {
                    //Dice
                    case 15096:
                        rnd = Misc.random(19) + 1;
                        Message = ("rolled <col=16711680>" + rnd + "</col> on a twenty-sided die.");
                        break;
                    case 15094:
                        rnd = Misc.random(11) + 1;
                        Message = ("rolled <col=16711680>" + rnd + "</col> on a twelve-sided die.");
                        break;
                    case 15092:
                        rnd = Misc.random(9) + 1;
                        Message = ("rolled <col=16711680>" + rnd + "</col> on a ten-sided die.");
                        break;
                    case 15090:
                        rnd = Misc.random(7) + 1;
                        Message = ("rolled <col=16711680>" + rnd + "</col> on an eight-sided die.");
                        break;
                    case 15100:
                        rnd = Misc.random(3) + 1;
                        Message = ("rolled <col=16711680>" + rnd + "</col> on a four-sided die.");
                        break;
                    case 15086:
                        rnd = Misc.random(5) + 1;
                        Message = ("rolled <col=16711680>" + rnd + "</col> on a six-sided die.");
                        break;
                    case 15088:
                        rnd = Misc.random(11) + 1;
                        Message = ("rolled <col=16711680>" + rnd + "</col> on two six-sided dice.");
                        break;
                    case 15098:
                        rnd = Misc.random(9) * 10 + Misc.random(10);
                        Message = ("rolled <col=16711680>" + rnd + "</col> on the percentile dice.");
                        break;
                        //case 15098: rnd = Misc.random(99)+1; Message = ("rolled <col=16711680>"+ rnd +"</col> on the percentile dice."); break;
                }
                sendMessage("You " + Message);
                if (clanDice) {
                    if (clanId >= 0) {
                        Server.clanChat.messageToClan("Clan Chat channel-mate <col=16711680>" + playerName + "</col> " + Message, clanId);
                    }
                }
                cDice = 0;
                //this.stop();
            }
        });
    }

    public void useDice(int itemId, boolean clan) {
        if (playerRights == 3) {
            if (System.currentTimeMillis() - diceDelay >= 3000) {
                sendMessage("Rolling...");
                startAnimation(11900);
                diceDelay = System.currentTimeMillis();
                cDice = itemId;
                clanDice = clan;
                switch (itemId) {
                    //Gfx's
                    case 15086:
                        gfx0(2072);
                        break;
                    case 15088:
                        gfx0(2074);
                        break;
                    case 15090:
                        gfx0(2071);
                        break;
                    case 15092:
                        gfx0(2070);
                        break;
                    case 15094:
                        gfx0(2073);
                        break;
                    case 15096:
                        gfx0(2068);
                        break;
                    case 15098:
                        gfx0(2075);
                        break;
                    case 15100:
                        gfx0(2069);
                        break;
                }


            }
        }
    }
	
	public void applyFollowing()
	{
		if (follow2 > 0)
		{
			//Client p = Server.playerHandler.client[followId];
			Client p = (Client) Server.playerHandler.players[follow2];     
			if (p != null)
			{
				if (isDead)
				{
					follow(0, 3, 1);
					return;
				}
				if (!goodDistance(p.absX, p.absY, absX, absY, 25))
				{
					follow(0, 3, 1);
					return;
				}
			}
			else if (p == null)
			{
				follow(0, 3, 1);
			}
		}
		else if (follow2 > 0)
		{
			//Server.npcHandler.npcs.NPC npc = Server.npcHandler.npcs[followId2];
			if (Server.npcHandler.npcs[followId2] != null)
			{
				if (Server.npcHandler.npcs[followId2].isDead)
				{
					follow(0, 3, 1);
					return;
				}
				if (!goodDistance(Server.npcHandler.npcs[followId2].absX, Server.npcHandler.npcs[followId2].absY, absX, absY, 25))
				{
					follow(0, 3, 1);
					return;
				}
			}
			else if (Server.npcHandler.npcs[followId2] == null)
			{
				follow(0, 3, 1);
			}
		}
	}

	public int followDistance = 0;

		public void follow(int slot, int type, int distance)
	{
		if (slot > 0 && slot == follow2 && type == 1 && follow2 > 0 && followDistance != distance && (/*usingOtherRangeWeapons || */usingBow || usingMagic))
                    return;
		else if (slot > 0 && slot == followId2 && type == 0 && followId2 > 0 && followDistance >= distance && distance != 1)
                    return;
		//else if (type == 3 && followId2 == 0 && follow2 == 0)
                    //return;
		outStream.createFrame(174);
		if (freezeTimer > 0) {
			outStream.writeWord(0);
		} else {
			outStream.writeWord(slot);
			if (type == 0) {
				follow2 = 0;
				followId2 = slot;
				faceUpdate(followId2);
			} else if (type == 1) {
				followId2 = 0;
				follow2 = slot;
				faceUpdate(32768 + follow2);
			} else if (type == 3) {
				followId2 = 0;
				follow2 = 0;
				followDistance = 0;
				faceUpdate(65535);
			}
			followDistance = distance;
		}
		outStream.writeByte(type);
		outStream.writeWord(distance);
	}
	
	/** Donations **/	
    public void didDonate2() {
        if (didDonated2 = true) {
            DonationMysql.needsItems(this);
            didDonated2 = false;
        }
        DonationMysql.destroyConnection();
        sendMessage("If you have not recieved your items, pelase wait 10-40 seconds before trying again");
        sendMessage("Otherwise contact and administrator! Justin, or AJ or post on forums");
    }

    public void didDonate() {
        if (didDonated = true) {
            MadTurnipConnection.addDonateItems(this, playerName);
            didDonated = false;
			sendMessage("If you have not recieved your items, pelase wait 10-40 seconds before trying again");
            sendMessage("Otherwise contact and administrator! Justin, or AJ or post on forums");
        }
    }
	
	
    public void jadSpawn() {
        getDH().sendDialogues(41, 2618);
        World.getWorld().submit(new Event(10000) {
            public void execute() {
                Server.fightCaves.spawnNextWave((Client) Server.playerHandler.players[playerId]);
                this.stop();
            }
        });

    }


    public void deleteFury() {
        if (getItems().playerHasItem(19335, 1)) {
            getItems().deleteItem(19335, 50);

        }
    }

	/** Degrades **/
    public void degradebrace() {
        if (playerEquipment[playerHands] == 11095 && bracelet1 < 1) {
            playerEquipment[playerHands] = -1;
            playerEquipmentN[playerHands] = 0;
            getItems().wearItem(11097, 1, 9);
            sendMessage("Your bracelet has lost a charge!");
            if (memberStatus == 1) {
                bracelet1 = 1000;
            } else {
                bracelet1 = 500;
            }
        }
    }

    public void degradebrace2() {
        if (playerEquipment[playerHands] == 11097 && bracelet2 < 1) {
            playerEquipment[playerHands] = -1;
            playerEquipmentN[playerHands] = 0;
            getItems().wearItem(11099, 1, 9);
            sendMessage("Your bracelet has lost a charge!");
            if (memberStatus == 1) {
                bracelet2 = 1000;
            } else {
                bracelet2 = 500;
            }
        }
    }
	
    public void degradebrace5() {
        if (playerEquipment[playerHands] == 11099 && bracelet5 < 1) {
            playerEquipment[playerHands] = -1;
            playerEquipmentN[playerHands] = 0;
            getItems().wearItem(-1, 1, 12);
            getItems().wearItem(11101, 1, 9);
            sendMessage("Your bracelet has lost a charge!!");
            if (memberStatus == 1) {
                bracelet5 = 1000;
            } else {
                bracelet5 = 500;
            }
        }
    }
	
    public void degradebrace3() {
        if (playerEquipment[playerHands] == 11101 && bracelet3 < 1) {
            playerEquipment[playerHands] = -1;
            playerEquipmentN[playerHands] = 0;
            getItems().wearItem(11103, 1, 9);
            sendMessage("Your bracelet has lost a charge!");
            if (memberStatus == 1) {
                bracelet3 = 1000;
            } else {
                bracelet3 = 500;
            }
        }
    }

    public void degradebrace4() {
        if (playerEquipment[playerHands] == 11103 && bracelet4 < 1) {
            playerEquipment[playerHands] = -1;
            playerEquipmentN[playerHands] = 0;
            getItems().wearItem(-1, 1, 9);
            sendMessage("Your bracelet has lost all its charges and degraded!! You can now be attacked!");
            if (memberStatus == 1) {
                bracelet4 = 1000;
            } else {
                bracelet4 = 500;
            }
        }
    }

    public void degraderapier() {
        if (getItems().freeSlots() <= 1) {

            return;
        }
        if (playerEquipment[playerWeapon] == 18349 && chaoticRapier < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            getItems().addItem(18350, 1);
            sendMessage("Your chaotic rapier has broken please speak to bla bla,, to fix.");
            if (memberStatus == 1) {
                chaoticRapier = 6000;

            } else {
                chaoticRapier = 3000;

            }
        }
    }
    public void degradebow() {
        if (getItems().freeSlots() <= 1) {

            return;
        }
        if (playerEquipment[playerWeapon] == 18537 && chaoticBow < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            getItems().addItem(18358, 1);
            sendMessage("Your chaotic cross-bow has broken please speak to bla bla,, to fix.");

            if (memberStatus == 1) {
                chaoticBow = 6000;

            } else {
                chaoticBow = 3000;

            }
        }
    }
    public void degradeshield() {
        if (getItems().freeSlots() <= 1) {

            return;
        }
        if (playerEquipment[playerShield] == 18349 && chaoticShield < 1) {
            playerEquipment[playerShield] = -1;
            playerEquipmentN[playerShield] = 0;
            getItems().wearItem(-1, 1, 3);
            getItems().addItem(18350, 1);
            sendMessage("Your chaotic shield has broken please speak to bla bla,, to fix.");

            if (memberStatus == 1) {
                chaoticShield = 6000;

            } else {
                chaoticShield = 3000;

            }
        }
    }
    public void degradestaff() {
        if (getItems().freeSlots() <= 1) {

            return;
        }
        if (playerEquipment[playerWeapon] == 18355 && chaoticStaff < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            getItems().addItem(18356, 1);
            sendMessage("Your chaotic staff has broken please speak to bla bla,, to fix.");

            if (memberStatus == 1) {
                chaoticStaff = 6000;

            } else {
                chaoticStaff = 3000;

            }
        }
    }

    public void degrademaul() {
        if (getItems().freeSlots() <= 1) {

            return;
        }
        if (playerEquipment[playerWeapon] == 18353 && chaoticMaul < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            getItems().addItem(18354, 1);
            sendMessage("Your chaotic maul has broken please speak to bla bla,, to fix.");

            if (memberStatus == 1) {
                chaoticMaul = 6000;

            } else {
                chaoticMaul = 3000;

            }

        }
    }


    public void degradelong() {
        if (getItems().freeSlots() <= 1) {

            return;
        }
        if (playerEquipment[playerWeapon] == 18351 && chaoticLong < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            getItems().addItem(18352, 1);
            sendMessage("Your chaotic long has broken please speak to bla bla,, to fix.");

            if (memberStatus == 1) {
                chaoticLong = 6000;

            } else {
                chaoticLong = 3000;

            }
        }
    }



    public void degradeVls() {
        if (playerEquipment[playerWeapon] == 13899 && vlsLeft < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            sendMessage("Your Vesta longsword crumbles to dust!");

            if (memberStatus == 1) {
                vlsLeft = 6000;

            } else {
                vlsLeft = 3000;

            }

        }
    }
    public void degradeVSpear() {
        if (playerEquipment[playerWeapon] == 13905 && vSpearLeft < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            sendMessage("Your Vesta spear crumbles to dust!");

            if (memberStatus == 1) {
                vSpearLeft = 6000;

            } else {
                vSpearLeft = 3000;

            }
        }
    }
    public void degradeStat() {
        if (playerEquipment[playerWeapon] == 13902 && statLeft < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            sendMessage("Your Statius warhammer crumbles to dust!");

            if (memberStatus == 1) {
                statLeft = 6000;

            } else {
                statLeft = 3000;

            }
        }
    }
    public void degradeVTop() { //vesta top
        if (playerEquipment[playerChest] == 13887 && vTopLeft < 1) {
            playerEquipment[playerChest] = -1;
            playerEquipmentN[playerChest] = 0;
            getItems().wearItem(-1, 1, playerChest);
            sendMessage("Your Vesta chainbody crumbles to dust!");

            if (memberStatus == 1) {
                vTopLeft = 6000;

            } else {
                vTopLeft = 3000;

            }
        }
    }
    public void degradeVLegs() { //vesta legs
        if (playerEquipment[playerLegs] == 13893 && vLegsLeft < 1) {
            playerEquipment[playerLegs] = -1;
            playerEquipmentN[playerLegs] = 0;
            getItems().wearItem(-1, 1, playerLegs);
            sendMessage("Your Vesta plateskirt crumbles to dust!");

            if (memberStatus == 1) {
                vLegsLeft = 6000;

            } else {
                vLegsLeft = 3000;

            }
        }
    }
    public void degradeSTop() { //statius top
        if (playerEquipment[playerChest] == 13884 && sTopLeft < 1) {
            playerEquipment[playerChest] = -1;
            playerEquipmentN[playerChest] = 0;
            getItems().wearItem(-1, 1, playerChest);
            sendMessage("Your Statius platebody crumbles to dust!");


            if (memberStatus == 1) {
                sTopLeft = 6000;

            } else {
                sTopLeft = 3000;

            }
        }
    }
    public void degradeSLegs() { //statius legs
        if (playerEquipment[playerLegs] == 13890 && sLegsLeft < 1) {
            playerEquipment[playerLegs] = -1;
            playerEquipmentN[playerLegs] = 0;
            getItems().wearItem(-1, 1, playerLegs);
            sendMessage("Your Statius platelegs crumbles to dust!");;
            if (memberStatus == 1) {
                sLegsLeft = 6000;

            } else {
                sLegsLeft = 3000;

            }
        }
    }
    public void degradeSHelm() { //statius helm
        if (playerEquipment[playerHat] == 13896 && sHelmLeft < 1) {
            playerEquipment[playerHat] = -1;
            playerEquipmentN[playerHat] = 0;
            getItems().wearItem(-1, 1, playerHat);
            sendMessage("Your Statius full helm crumbles to dust!");

            if (memberStatus == 1) {
                sHelmLeft = 6000;

            } else {
                sHelmLeft = 6000;

            }
        }
    }
    public void degradeZHood() { //zuriel hood
        if (playerEquipment[playerHat] == 13864 && zHoodLeft < 1) {
            playerEquipment[playerHat] = -1;
            playerEquipmentN[playerHat] = 0;
            getItems().wearItem(-1, 1, playerHat);
            sendMessage("Your Zuriel hood crumbles to dust!");

            if (memberStatus == 1) {
                zHoodLeft = 6000;

            } else {
                zHoodLeft = 3000;

            }
        }
    }
    public void degradeZTop() { //zuriel hood
        if (playerEquipment[playerChest] == 13858 && zTopLeft < 1) {
            playerEquipment[playerChest] = -1;
            playerEquipmentN[playerChest] = 0;
            getItems().wearItem(-1, 1, playerChest);
            sendMessage("Your Zuriel robe top crumbles to dust!");

            if (memberStatus == 1) {
                zTopLeft = 6000;

            } else {
                zTopLeft = 3000;

            }

        }
    }
    public void degradeZBottom() { //zuriel hood
        if (playerEquipment[playerLegs] == 13861 && zBottomLeft < 1) {
            playerEquipment[playerLegs] = -1;
            playerEquipmentN[playerLegs] = 0;
            getItems().wearItem(-1, 1, playerLegs);
            sendMessage("Your Zuriel robe bottom crumbles to dust!");

            if (memberStatus == 1) {
                zBottomLeft = 6000;

            } else {
                zBottomLeft = 3000;

            }
        }
    }
    public void degradeZStaff() { //zuriel staff
        if (playerEquipment[playerWeapon] == 13868 && zStaffLeft < 1) {
            playerEquipment[playerWeapon] = -1;
            playerEquipmentN[playerWeapon] = 0;
            getItems().wearItem(-1, 1, 3);
            sendMessage("Your Zuriel staff crumbles to dust!");

            if (memberStatus == 1) {
                zStaffLeft = 6000;

            } else {
                zStaffLeft = 3000;

            }
        }
    }
    public void degradeMBody() { //morrigans body
        if (playerEquipment[playerChest] == 13870 && mBodyLeft < 1) {
            playerEquipment[playerChest] = -1;
            playerEquipmentN[playerChest] = 0;
            getItems().wearItem(-1, 1, playerChest);
            sendMessage("Your Morrigans leather body crumbles to dust!");

            if (memberStatus == 1) {
                mBodyLeft = 6000;

            } else {
                mBodyLeft = 3000;

            }
        }
    }
    public void degradeMChaps() { //morrigans chaps
        if (playerEquipment[playerLegs] == 13873 && mChapsLeft < 1) {
            playerEquipment[playerLegs] = -1;
            playerEquipmentN[playerLegs] = 0;
            getItems().wearItem(-1, 1, playerLegs);
            sendMessage("Your Morrigans chaps crumbles to dust!");

            if (memberStatus == 1) {
                mChapsLeft = 6000;

            } else {
                mChapsLeft = 3000;

            }
        }
    }
    public void clearPlayersInterface() {
        for (int players: PlayersInterface) {
            getPA().sendFrame126("", players);
        }
    }
    public int[] PlayersInterface = {
        8147, 8148, 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194, 8195, 8196, 8197, 8198, 8199, 8200, 8201, 8202, 8203, 8204, 8205, 8206, 8207, 8208, 8209, 8210, 8211, 8212, 8213, 8214, 8215, 8216, 8217, 8218, 8219, 8220, 8221, 8222, 8223, 8224, 8225, 8226, 8227, 8228, 8229, 8230, 8231, 8232, 8233, 8234, 8235, 8236, 8237, 8238, 8239, 8240, 8241, 8242, 8243, 8244, 8245, 8246, 8247
    };



    public void flushOutStream() {
        if (disconnected || outStream.currentOffset == 0) return;
        synchronized(this) {
            StaticPacketBuilder out = new StaticPacketBuilder().setBare(true);
            byte[] temp = new byte[outStream.currentOffset];
            System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
            out.addBytes(temp);
            session.write(out.toPacket());
            outStream.currentOffset = 0;
        }
    }

    public void sendClan(String name, String message, String clan, int rights) {
        outStream.createFrameVarSizeWord(217);
        outStream.writeString(name);
        outStream.writeString(message);
        outStream.writeString(clan);
        outStream.writeWord(rights);
        outStream.endFrameVarSize();
    }

    public void StartBestItemScan() {
        if (isSkulled && !prayerActive[10]) {
            ItemKeptInfo(0);
            return;
        }
        FindItemKeptInfo();
        ResetKeepItems();
        BestItem1();
    }
    public void BestItem1() {
        int BestValue = 0;
        int NextValue = 0;
        int ItemsContained = 0;
        WillKeepItem1 = 0;
        WillKeepItem1Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (playerItems[ITEM] > 0) {
                ItemsContained += 1;
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerItems[ITEM] - 1));
                if (NextValue > BestValue) {
                    BestValue = NextValue;
                    WillKeepItem1 = playerItems[ITEM] - 1;
                    WillKeepItem1Slot = ITEM;
                    if (playerItemsN[ITEM] > 2 && !prayerActive[10]) {
                        WillKeepAmt1 = 3;
                    } else if (playerItemsN[ITEM] > 3 && prayerActive[10]) {
                        WillKeepAmt1 = 4;
                    } else {
                        WillKeepAmt1 = playerItemsN[ITEM];
                    }
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (playerEquipment[EQUIP] > 0) {
                ItemsContained += 1;
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerEquipment[EQUIP]));
                if (NextValue > BestValue) {
                    BestValue = NextValue;
                    WillKeepItem1 = playerEquipment[EQUIP];
                    WillKeepItem1Slot = EQUIP + 28;
                    if (playerEquipmentN[EQUIP] > 2 && !prayerActive[10]) {
                        WillKeepAmt1 = 3;
                    } else if (playerEquipmentN[EQUIP] > 3 && prayerActive[10]) {
                        WillKeepAmt1 = 4;
                    } else {
                        WillKeepAmt1 = playerEquipmentN[EQUIP];
                    }
                }
            }
        }
        if (!isSkulled && ItemsContained > 1 && (WillKeepAmt1 < 3 || (prayerActive[10] && WillKeepAmt1 < 4))) {
            BestItem2(ItemsContained);
        }
    }
    public void BestItem2(int ItemsContained) {
        int BestValue = 0;
        int NextValue = 0;
        WillKeepItem2 = 0;
        WillKeepItem2Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (playerItems[ITEM] > 0) {
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerItems[ITEM] - 1));
                if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)) {
                    BestValue = NextValue;
                    WillKeepItem2 = playerItems[ITEM] - 1;
                    WillKeepItem2Slot = ITEM;
                    if (playerItemsN[ITEM] > 2 - WillKeepAmt1 && !prayerActive[10]) {
                        WillKeepAmt2 = 3 - WillKeepAmt1;
                    } else if (playerItemsN[ITEM] > 3 - WillKeepAmt1 && prayerActive[10]) {
                        WillKeepAmt2 = 4 - WillKeepAmt1;
                    } else {
                        WillKeepAmt2 = playerItemsN[ITEM];
                    }
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (playerEquipment[EQUIP] > 0) {
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerEquipment[EQUIP]));
                if (NextValue > BestValue && !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)) {
                    BestValue = NextValue;
                    WillKeepItem2 = playerEquipment[EQUIP];
                    WillKeepItem2Slot = EQUIP + 28;
                    if (playerEquipmentN[EQUIP] > 2 - WillKeepAmt1 && !prayerActive[10]) {
                        WillKeepAmt2 = 3 - WillKeepAmt1;
                    } else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && prayerActive[10]) {
                        WillKeepAmt2 = 4 - WillKeepAmt1;
                    } else {
                        WillKeepAmt2 = playerEquipmentN[EQUIP];
                    }
                }
            }
        }
        if (!isSkulled && ItemsContained > 2 && (WillKeepAmt1 + WillKeepAmt2 < 3 || (prayerActive[10] && WillKeepAmt1 + WillKeepAmt2 < 4))) {
            BestItem3(ItemsContained);
        }
    }
    public void BestItem3(int ItemsContained) {
        int BestValue = 0;
        int NextValue = 0;
        WillKeepItem3 = 0;
        WillKeepItem3Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (playerItems[ITEM] > 0) {
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerItems[ITEM] - 1));
                if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1) && !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2)) {
                    BestValue = NextValue;
                    WillKeepItem3 = playerItems[ITEM] - 1;
                    WillKeepItem3Slot = ITEM;
                    if (playerItemsN[ITEM] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !prayerActive[10]) {
                        WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
                    } else if (playerItemsN[ITEM] > 3 - (WillKeepAmt1 + WillKeepAmt2) && prayerActive[10]) {
                        WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
                    } else {
                        WillKeepAmt3 = playerItemsN[ITEM];
                    }
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (playerEquipment[EQUIP] > 0) {
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerEquipment[EQUIP]));
                if (NextValue > BestValue && !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1) && !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2)) {
                    BestValue = NextValue;
                    WillKeepItem3 = playerEquipment[EQUIP];
                    WillKeepItem3Slot = EQUIP + 28;
                    if (playerEquipmentN[EQUIP] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !prayerActive[10]) {
                        WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
                    } else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && prayerActive[10]) {
                        WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
                    } else {
                        WillKeepAmt3 = playerEquipmentN[EQUIP];
                    }
                }
            }
        }
        if (!isSkulled && ItemsContained > 3 && prayerActive[10] && ((WillKeepAmt1 + WillKeepAmt2 + WillKeepAmt3) < 4)) {
            BestItem4();
        }
    }
    public void BestItem4() {
        int BestValue = 0;
        int NextValue = 0;
        WillKeepItem4 = 0;
        WillKeepItem4Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (playerItems[ITEM] > 0) {
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerItems[ITEM] - 1));
                if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1) && !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2) && !(ITEM == WillKeepItem3Slot && playerItems[ITEM] - 1 == WillKeepItem3)) {
                    BestValue = NextValue;
                    WillKeepItem4 = playerItems[ITEM] - 1;
                    WillKeepItem4Slot = ITEM;
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (playerEquipment[EQUIP] > 0) {
                NextValue = (int) Math.floor(getShops().getItemShopValue(playerEquipment[EQUIP]));
                if (NextValue > BestValue && !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1) && !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2) && !(EQUIP + 28 == WillKeepItem3Slot && playerEquipment[EQUIP] == WillKeepItem3)) {
                    BestValue = NextValue;
                    WillKeepItem4 = playerEquipment[EQUIP];
                    WillKeepItem4Slot = EQUIP + 28;
                }
            }
        }
    }
    public void ItemKeptInfo(int Lose) {
        for (int i = 17109; i < 17131; i++) {
            getPA().sendFrame126("", i);
        }
        getPA().sendFrame126("Items you will keep on death:", 17104);
        getPA().sendFrame126("Items you will lose on death:", 17105);
        getPA().sendFrame126("Server", 17106);
        getPA().sendFrame126("Max items kept on death:", 17107);
        getPA().sendFrame126("~ " + Lose + " ~", 17108);
        getPA().sendFrame126("The normal amount of", 17111);
        getPA().sendFrame126("items kept is three.", 17112);
        switch (Lose) {
            case 0:
            default:
                getPA().sendFrame126("Items you will keep on death:", 17104);
                getPA().sendFrame126("Items you will lose on death:", 17105);
                getPA().sendFrame126("You're marked with a", 17111);
                getPA().sendFrame126("@red@skull. @lre@This reduces the", 17112);
                getPA().sendFrame126("items you keep from", 17113);
                getPA().sendFrame126("three to zero!", 17114);
                break;
            case 1:
                getPA().sendFrame126("Items you will keep on death:", 17104);
                getPA().sendFrame126("Items you will lose on death:", 17105);
                getPA().sendFrame126("You're marked with a", 17111);
                getPA().sendFrame126("@red@Skull. @lre@This reduces the", 17112);
                getPA().sendFrame126("items you keep from", 17113);
                getPA().sendFrame126("three to zero!", 17114);
                getPA().sendFrame126("However, you also have", 17115);
                getPA().sendFrame126("@red@Protect @lre@Item prayer", 17118);
                getPA().sendFrame126("active, which saves", 17119);
                getPA().sendFrame126("you one extra item!", 17120);
                break;
            case 3:
                getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
                getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
                getPA().sendFrame126("You have no factors", 17111);
                getPA().sendFrame126("affecting the items", 17112);
                getPA().sendFrame126("you keep.", 17113);
                break;
            case 4:
                getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
                getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
                getPA().sendFrame126("You have the @red@Protect", 17111);
                getPA().sendFrame126("@red@Item @lre@prayer active,", 17112);
                getPA().sendFrame126("which saves you one", 17113);
                getPA().sendFrame126("extra item!", 17114);
                break;
            case 5:
                getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
                getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
                getPA().sendFrame126("@red@You are in a @red@Dangerous", 17111);
                getPA().sendFrame126("@red@Zone, and will lose all", 17112);
                getPA().sendFrame126("@red@if you die.", 17113);
                getPA().sendFrame126("", 17114);
                break;
        }
    }
    public void ResetKeepItems() {
        WillKeepItem1 = 0;
        WillKeepItem1Slot = 0;
        WillKeepItem2 = 0;
        WillKeepItem2Slot = 0;
        WillKeepItem3 = 0;
        WillKeepItem3Slot = 0;
        WillKeepItem4 = 0;
        WillKeepItem4Slot = 0;
        WillKeepAmt1 = 0;
        WillKeepAmt2 = 0;
        WillKeepAmt3 = 0;
    }
    public void FindItemKeptInfo() {
        if (isSkulled && prayerActive[10]) ItemKeptInfo(1);
        else if (!isSkulled && !prayerActive[10]) ItemKeptInfo(3);
        else if (!isSkulled && prayerActive[10]) ItemKeptInfo(4);
        else if (inPits || inFightCaves()) {
            ItemKeptInfo(5);
            if (isInFala() || isInArd()) {
                ItemKeptInfo(6);
            }
        }
    }

    public static int Flower[] = {
        2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988
    };


    public static int randomFlower()

    {

        return Flower[(int)(Math.random() * Flower.length)];

    }
    public static final int PACKET_SIZES[] = {
        0, 0, 0, 1, -1, 0, 0, 0, 0, 0, //0
        0, 0, 0, 0, 8, 0, 6, 2, 2, 0, //10
        0, 2, 0, 6, 0, 12, 0, 0, 0, 0, //20
        0, 0, 0, 0, 0, 8, 4, 0, 0, 2, //30
        2, 6, 0, 6, 0, -1, 0, 0, 0, 0, //40
        0, 0, 0, 12, 0, 0, 0, 8, 8, 12, //50
        8, 8, 0, 0, 0, 0, 0, 0, 0, 0, //60
        6, 0, 2, 2, 8, 6, 0, -1, 0, 6, //70
        0, 0, 0, 0, 0, 1, 4, 6, 0, 0, //80
        0, 0, 0, 0, 0, 3, 0, 0, -1, 0, //90
        0, 13, 0, -1, 0, 0, 0, 0, 0, 0, //100
        0, 0, 0, 0, 0, 0, 0, 6, 0, 0, //110
        1, 0, 6, 0, 0, 0, -1, 0, 2, 6, //120
        0, 4, 6, 8, 0, 6, 0, 0, 0, 2, //130
        0, 0, 0, 0, 0, 6, 0, 0, 0, 0, //140
        0, 0, 1, 2, 0, 2, 6, 0, 0, 0, //150
        0, 0, 0, 0, -1, -1, 0, 0, 0, 0, //160
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //170
        0, 8, 0, 3, 0, 2, 0, 0, 8, 1, //180
        0, 0, 12, 0, 0, 0, 0, 0, 0, 0, //190
        2, 0, 0, 0, 0, 0, 0, 0, 4, 0, //200
        4, 0, 0, 0, 7, 8, 0, 0, 10, 0, //210
        0, 0, 0, 0, 0, 0, -1, 0, 6, 0, //220
        1, 0, 0, 0, 6, 0, 6, 8, 1, 0, //230
        0, 4, 0, 0, 0, 0, -1, 0, -1, 4, //240
        0, 0, 6, 6, 0, 0, 0 //250
    };
    public void clearQuestInterface() {
        for (int element: QuestInterface) {
            getPA().sendFrame126("", element);
        }
    }
    public int[] QuestInterface = {
        8145, 8147, 8148, 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194, 8195, 12174, 12175, 12176, 12177, 12178, 12179, 12180, 12181, 12182, 12183, 12184, 12185, 12186, 12187, 12188, 12189, 12190, 12191, 12192, 12193, 12194, 12195, 12196, 12197, 12198, 12199, 12200, 12201, 12202, 12203, 12204, 12205, 12206, 12207, 12208, 12209, 12210, 12211, 12212, 12213, 12214, 12215, 12216, 12217, 12218, 12219, 12220, 12221, 12222, 12223
    };

    public void wyverns() {
        getPA().sendFrame126("            @red@Wyvern Cavern", 8144);
        clearQuestInterface();
        getPA().sendFrame126("New update today the Wyvern Cavern", 8147);
        getPA().sendFrame126("The Wyverns Drop Chaotic Kite Shield! Hand Cannon", 8148);
        getPA().sendFrame126("and D- Claws. The Drop-Rate if fairly High, ", 8149);
        getPA().sendFrame126("meaning it drops a lot. To get into the cave you ", 8150);
        getPA().sendFrame126("first must have a Iron Key which you can get from ", 8151);
        getPA().sendFrame126("the vote shop for 15 points. Once you get a key head ", 8152);
        getPA().sendFrame126("South from edgville bank  and click on the trapdoor ", 8153);
        getPA().sendFrame126("with your key in your inventory. ", 8154);
        getPA().sendFrame126("Once in the cave Follow the path and you will see the Wyverns.  ", 8155);
        getPA().sendFrame126("The wyverns also drop the best bones ", 8156);
        getPA().sendFrame126("in the game which gives 3x", 8157);
        getPA().sendFrame126("the xp of dragon bones, and 8x on the altar.", 8158);
        getPA().sendFrame126(" I hope you guys enjoy This.", 8158);
        getPA().sendFrame126("Also when you enter you loose your key there is no bank", 8159);
        getPA().sendFrame126("Good luck have fun, Anso bring a Anti-Fire shield", 8160);
        getPA().sendFrame126("Also drop Imboune rings like berserker ring [i]", 8161);
        getPA().sendFrame126("@red@ Also there is a runite ore in the CAVE!", 8162);
        getPA().showInterface(8134);
        flushOutStream();
    }

    public void destruct() {
		if(underAttackBy > 0 || underAttackBy2 > 0)
			return;
        if (CastleWars.isInCwWait(this)) {
            CastleWars.leaveWaitingRoom(this);
        }
        if (CastleWars.isInCw(this)) {
            CastleWars.removePlayerFromCw(this);
        }
		if (clanId >= 0) {
			Server.clanChat.handleClanOnLogout(playerId, clanId);
		}
			PlayerSave.saveGame(this);
			PlayerSave.saveGame(this);
        if (disconnected == true) {
            saveCharacter = true;
        }
		if (InMinigame = true) {
			 getPA().movePlayer(3073, 3504, 0);	
			 InMinigame = false;
		}
        if (disconnected == true) {
            getTradeAndDuel().declineTrade();
        }
		/*if (PestControl.isInGame(this)) {
            PestControl.removePlayerGame(this);
			getPA().movePlayer(2440, 3089, 0);
        }*/
        if (session == null) 
			return;
			PlayerSave.saveGame(this);
        if (clanId >= 0) Server.clanChat.leaveClan(playerId, clanId);
			Misc.println("[DEREGISTERED]: " + playerName + "");
			PlayerSave.saveGame(this);
			saveCharacter = true;
			HostList.getHostList().remove(session);
			disconnected = true;
			session.close();
			session = null;
			inStream = null;
			Server.panel.removeEntity(playerName);
			outStream = null;
			isActive = false;
			buffer = null;
			super.destruct();
		}


    /** Custom Voids **/

    public void UserLogIn() {
        if (playerRights == 1) {
            getPA().yell("[ <col=255>Moderator </col>] <col=255>" + playerName + "</col> has just logged in!");
        }
        if (playerRights == 2) {
            getPA().yell("[ <col=E4F527>Administrator </col>] <col=E4F527>" + playerName + "</col> has just logged in!");
        }
        if (playerRights == 3) {
            getPA().yell("[ <col=990000>Owner & Developer</col>] <col=990000>" + playerName + "</col> has just logged in!");
        }
        if (memberStatus == 1) {
        	if (playerRights > 0 && playerRights < 4) {
        		
        	} else {
            getPA().yell("[ <col=1BF527>Normal Member </col>] <col=1BF527>" + playerName + "</col> has just logged in!");
        }
        }
        if (memberStatus == 2) {
           	if (playerRights > 0 && playerRights < 4) {
        		 
        	} else {
            getPA().yell("[ <col=0B7747>Super Member </col>] <col=0B7747>" + playerName + "</col> has just logged in!");
        }
        }
        if (memberStatus == 3) {
           	if (playerRights > 0 && playerRights < 4) {
        		
        	} else {
            getPA().yell("[ <col=C030FF>Extreme Member </col>] <col=C030FF>" + playerName + "</col> has just logged in!");
        }
        }
    }

	
    public void sendMessage(String s) {
        synchronized(this) {
            if (getOutStream() != null) {
                outStream.createFrameVarSize(253);
                outStream.writeString(s);
                outStream.endFrameVarSize();
            }
        }
    }

    public void setSidebarInterface(int menuId, int form) {
        synchronized(this) {
            if (getOutStream() != null) {
                outStream.createFrame(71);
                outStream.writeWord(form);
                outStream.writeByteA(menuId);
            }
        }
    }
    public void checkStarter() {
        if (gotStarter > 0) {
            return;
        } else {
            sendMessage("wtf");

        }
    }
	
	public void ClanLogIn() {
		if(chatId == -1 || chatId == 0) {
			clanName = "None";
			clanId = -1;
			chatId = -1;
		}
		if(chatId >= 100) {
			chatId = -1;
			clanName = "None";
			clanId = -1;
		}
		if(!clanName.equalsIgnoreCase("None") && !clanName.equalsIgnoreCase("") && chatId > 0 && Server.clanChat.clans[chatId] != null) {
				notNow = true; // makes sure the 'owner has logged in is sent'
				Server.clanChat.addToClan(playerId, chatId);
			}
		}
	public void GiveThesePlayerAStarterCauseHesOnCrack() {
		if (NigaGotStarter == false) {
			return;
		} else if (NigaGotStarter == true) {
			getPA().addStarter();
		}	
	}
	

    public void initialize() {
        synchronized(this) {
			gotStarter = 1;
			//GiveThesePlayerAStarterCauseHesOnCrack();
			getPA().sendFrame126(":prayer:" + (playerPrayerBook == 1 ? "curses" : "prayers"), -1);
            clearPlayersInterface();
			//safeTimer = 0;
SQL.createConnection();
SQL.saveHighScore(this);
SQL.destroyConnection();
            outStream.createFrame(249);
            outStream.writeByteA(1); // 1 for members, zero for free
            outStream.writeWordBigEndianA(playerId);
            for (int j = 0; j < Server.playerHandler.players.length; j++) {
                if (j == playerId) continue;
                if (Server.playerHandler.players[j] != null) {
                    if (Server.playerHandler.players[j].playerName.equalsIgnoreCase(playerName)) disconnected = true;
                }
            }
            for (int i = 0; i < 25; i++) {
                getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
                getPA().refreshSkill(i);
            }
            for (int p = 0; p < CURSE.length; p++) { // reset prayer glows
                curseActive[p] = false;
                getPA().sendFrame36(CURSE_GLOW[p], 0);
            }
            for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows 
                prayerActive[p] = false;
                getPA().sendFrame36(PRAYER_GLOW[p], 0);
            }
            //if (playerName.equalsIgnoreCase("Sanity")) {
            //getPA().sendCrashFrame();
            //}
			Server.lottery.checkUnclaimedWinners(this);
            getPA().clearClanChat();
            getPA().handleWeaponStyle();
            getPA().handleLoginText();
            accountFlagged = getPA().checkForFlags();
            //getPA().sendFrame36(43, fightMode-1);
            getPA().sendFrame36(108, 0); //resets autocast button
            getPA().sendFrame36(172, 1);
            getPA().sendFrame107(); // reset screen
            getPA().setChatOptions(0, 0, 0); // reset private messaging options
            setSidebarInterface(1, 3917);
            setSidebarInterface(2, 638);
            setSidebarInterface(3, 3213);
            setSidebarInterface(4, 1644);
			
            if (PRAYFUCK == true) {
                setSidebarInterface(5, 22500);
            } else if (PRAYFUCK == false) {
                setSidebarInterface(5, 5608);
			}

            if (playerMagicBook == 0) {
                setSidebarInterface(6, 1151); //modern
            } else if (playerMagicBook == 1) {
                setSidebarInterface(6, 12855); // ancient
            } else if (playerMagicBook == 2) {
                setSidebarInterface(6, 29999); // lunar
            }

            correctCoordinates();
            setSidebarInterface(7, -1);//Note Tab
            setSidebarInterface(8, 5065);
            setSidebarInterface(9, 5715);
            setSidebarInterface(10, 18128);//logout
            getPA().totallevelsupdate();
            //setSidebarInterface(11, 4445); // wrench tab
            setSidebarInterface(11, 904); // wrench tab
            setSidebarInterface(12, 147); // run tab
            setSidebarInterface(14, 29265); //acheivement
            setSidebarInterface(15, 2449); //blank
            setSidebarInterface(13, -1); //music tab 6299 for lowdetail. 962 for highdetail
            setSidebarInterface(0, 2423);
            //getDH().sendDialogues(45, npcType);
            ConnectedFrom.addConnectedFrom(this, connectedFrom);
            //Vote.checkVote(this);
            //MadTurnipConnection.addDonateItems(this,playerName);
			Server.panel.addEntity(playerName);
            mymessage();
			Achievements.AchievementTexts();
            FetchDice();
            startUpStuff();
            if (Config.doubleEXPWeekend == true) {
                sendMessage("<col=255<Enjoy Double EXP Weekend!");
            }
            if (memberStatus >= 0 && firstMember == 0) {
				sendMessage("Welcome " + playerName + ", to Project-Equinox");
            	getPA().yell("[ <col=2784FF>Project-Equinox's </col>] <col=2784FF>"+ playerName +"</col> has just joined.");
            	firstMember = 1;
            }
			getPA().checkDateAndTime();
			//sendMessage("Current date is: <col=255>" + date + "</col> || Current time is: <col=255>" + currentTime + " " + getPA().checkTimeOfDay() + "</col> ");
			getPA().showOption(4, 0,"Follow", 4);
			getPA().showOption(5, 0,"Trade With", 3);
            getItems().resetItems(3214);
            getItems().sendWeapon(playerEquipment[playerWeapon], getItems().getItemName(playerEquipment[playerWeapon]));
            getItems().resetBonus();
            getItems().getBonus();
            getItems().writeBonus();
            getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
            getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
            getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
            getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
            getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
            getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
            getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
            getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
            getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
            getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
            getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
            getCombat().getPlayerAnimIndex(getItems().getItemName(playerEquipment[playerWeapon]).toLowerCase());
            getPA().logIntoPM();
            getItems().addSpecialBar(playerEquipment[playerWeapon]);
            saveCharacter = true;
            Misc.println("[REGISTERED]: " + playerName + "");
            handler.updatePlayer(this, outStream);
            handler.updateNPC(this, outStream);
            flushOutStream();
            getPA().clearClanChat();
			totalLevel = getPA().totalLevel();
			xpTotal = getPA().xpTotal();
			HighscoresConfig.updateHighscores(this);
            if (autoRet == 1) getPA().sendFrame36(172, 1);
				else getPA().sendFrame36(172, 0);
			UserLogIn();
			getPA().loadAnnouncements();	
			ClanLogIn();	
			getPA().sendFrame126(""+SoFpoints+"", 48508);			
        }
    }
	
	public void SetNoteTabShit() {
		getPA().sendFrame126("@or1@Latest Project-E Updates:", 13800);
		getPA().sendFrame126("", 13803);
		getPA().sendFrame126("*Added Random Pk Reward System", 13806);
		getPA().sendFrame126("*Added Deposite Inventory & Equipment", 13809);
		getPA().sendFrame126("*Added Hybrid's Den Pk", 13812);
		getPA().sendFrame126("*Pimped Out Marketplace", 13815);
		getPA().sendFrame126("*Fixed Up Starter System", 13818);
		getPA().sendFrame126("*Added Lottery System", 13821);
		getPA().sendFrame126("*Added Completionist Cape", 13823);
		getPA().sendFrame126("", 13825);
		getPA().sendFrame126("", 13827);
		getPA().sendFrame126("", 13829);
	}



    public void update() {
        synchronized(this) {
            handler.updatePlayer(this, outStream);
            handler.updateNPC(this, outStream);
            flushOutStream();
        }
    }

    public void logout() {
        if (System.currentTimeMillis() - logoutDelay > 10000) {
            outStream.createFrame(109);
            properLogout = true;
            PlayerSave.saveGame(this);
            ConnectedFrom.addConnectedFrom(this, connectedFrom);
            saveCharacter = true;
        } else {
            sendMessage("You must wait a few seconds from being out of combat before you can do this.");
        }
    }
    public void SaveGame() {
        //synchronized (this) {
        PlayerSave.saveGame(this);
        //}
    }
    public int getMove(int Place1, int Place2) {
        if ((Place1 - Place2) == 0) return 0;
        else if ((Place1 - Place2) < 0) return 1;
        else if ((Place1 - Place2) > 0) return -1;
        return 0;
    }


 

  
    public int packetSize = 0, packetType = -1;
    public int degradeTime = 0;
    public int vestaplatedegrade = 0;
    public int tradeTimer;

	
	
	
    public void process() {
			getFishing().FishingProcess();
	/*if(inWild()) {
		safeTimer = 11;
	}
	if(safeTimer > 0 && !inWild()) {
		safeTimer--;
	}*/
	
		if (BountyHunter.inBH(this)) {
			BountyHunter.handleNewTarget(this);
		}
	
			if (BountyHunter.safeArea(this) && DoingTut == false) {
			headIconPk = -1;
			getPA().requestUpdates();
			getPA().walkableInterface(-1);	
		}
	
	applyFollowing();

if(followId > 0) {

getPA().followPlayer();

} else if (followId2 > 0) {

getPA().followNpc();

}
	

        if (gwdelay > 0) {
            gwdelay--;
        }
        /*if (System.currentTimeMillis() - lastoverload > 1000) {
			if (overloadcounter > 0) {
				startAnimation(2383);
				dealDamage(10);
				handleHitMask(10);
				overloadcounter -= 1;
				getPA().refreshSkill(3);
				lastoverload = System.currentTimeMillis();	
			}
		}*/

		                if ((runEnergy < 100 && ((!isRunning && !isRunning2) || !isMoving)) && System.currentTimeMillis() - runEnergyTime > Config.RUN_ENERGY_GAIN) {
                    runEnergyTime = System.currentTimeMillis();
                    runEnergy++;
                }
                    getPA().sendFrame126(runEnergy + "%", 149);
				
        if (followId > 0) {
            getPA().followPlayer();
        } else if (followId2 > 0) {
            getPA().followNpc();
        }

        if (wcTimer > 0 && woodcut[0] > 0) {
            wcTimer--;
        } else if (wcTimer == 0 && woodcut[0] > 0) {
            getWoodcutting().cutWood();
        } else if (miningTimer > 0 && mining[0] > 0) {
            miningTimer--;
        } else if (miningTimer == 0 && mining[0] > 0) {
            getMining().mineOre();
        } else if (smeltTimer > 0 && smeltType > 0) {
            smeltTimer--;
        } else if (smeltTimer == 0 && smeltType > 0) {
            getSmithing().smelt(smeltType);
        }
        if (tradeTimer > 0) {
            tradeTimer--;
        }
        if (System.currentTimeMillis() - duelDelay > 800 && duelCount > 0) {
            if (duelCount != 1) {
                forcedChat("" + (--duelCount));
                duelDelay = System.currentTimeMillis();
            } else {
                damageTaken = new int[Config.MAX_PLAYERS];
                forcedChat("FIGHT!");
                duelCount = 0;
            }
        }

        /*if (absX == 3292 && absY == 3091 || absX == 3292 && absY == 3090) {
			getPA().walkTo3(-130, -64);
		}
		if (absX == 3274 && absY == 3072 || absX == 3275 && absY == 3073) {
			getPA().walkTo3(-130, -64);
		}
		if (absX == 3256 && absY == 3054 || absX == 3257 && absY == 3055) {
			getPA().walkTo3(-130, -64);
		}*/

        if (overloadcounter > 0) {
            startAnimation(3170);
            dealDamage(10);
            handleHitMask(10);
            overloadcounter -= 1;
            getPA().refreshSkill(3);
        }
        if (morriganJavspec > 0) {

            dealDamage(5);
            handleHitMask(5);
            morriganJavspec -= 1;
            getPA().refreshSkill(3);
        }
        if (clawDelay > 0) {
            clawDelay--;
        }
        if (clawDelay == 1) {
            delayedDamage = clawDamage / 4;
            delayedDamage2 = (clawDamage / 4) + 1;
            if (clawType == 2) {
                getCombat().applyNpcMeleeDamage(clawIndex, 1, clawDamage / 4);
            }
            if (clawType == 1) {
                getCombat().applyPlayerMeleeDamage(clawIndex, 1, clawDamage / 4);
            }
            if (clawType == 2) {
                getCombat().applyNpcMeleeDamage(clawIndex, 2, (clawDamage / 4) + 1);
            }
            if (clawType == 1) {
                getCombat().applyPlayerMeleeDamage(clawIndex, 2, (clawDamage / 4) + 1);
            }
            clawDelay = 0;
            specEffect = 0;
            previousDamage = 0;
            usingClaws = false;
            clawType = 0;
        }
        if (System.currentTimeMillis() - lastPoison > 20000 && poisonDamage > 0) {
            int damage = poisonDamage / 2;
            if (damage > 0) {
                sendMessage("Applying poison damage.");
                if (!getHitUpdateRequired()) {
                    setHitUpdateRequired(true);
                    setHitDiff(damage);
                    updateRequired = true;
                    poisonMask = 1;
                } else if (!getHitUpdateRequired2()) {
                    setHitUpdateRequired2(true);
                    setHitDiff2(damage);
                    updateRequired = true;
                    poisonMask = 2;
                }
                lastPoison = System.currentTimeMillis();
                poisonDamage--;
                dealDamage(damage);
            } else {
                poisonDamage = -1;
                sendMessage("You are no longer poisoned.");
            }
        }



        if (System.currentTimeMillis() - specDelay > Config.INCREASE_SPECIAL_AMOUNT) {
            specDelay = System.currentTimeMillis();
            if (specAmount < 10) {
                specAmount += .5;
                if (specAmount > 10) specAmount = 10;
                getItems().addSpecialBar(playerEquipment[playerWeapon]);
            }
        }

        /*if(clickObjectType > 0 && goodDistance(objectX + objectXOffset, objectY + objectYOffset, getX(), getY(), objectDistance)) {
			if(clickObjectType == 1) {
				getActions().firstClickObject(objectId, objectX, objectY);
			}
			if(clickObjectType == 2) {
				getActions().secondClickObject(objectId, objectX, objectY);
			}
			if(clickObjectType == 3) {
				getActions().thirdClickObject(objectId, objectX, objectY);
			}
		}*/
        if (clickObjectType > 0 && (goodDistance(objectX + objectXOffset, objectY + objectYOffset, getX(), getY(), objectDistance) || goodDistance(objectX + objectXOffset, objectY, getX(), getY(), objectDistance) || goodDistance(objectX, objectY + objectYOffset, getX(), getY(), objectDistance) || goodDistance(objectX, objectY, getX(), getY(), objectDistance))) {
            if (clickObjectType == 1) {
                getActions().firstClickObject(objectId, objectX, objectY);
                getPA().resetAction();
            }
            if (clickObjectType == 2) {
                getActions().secondClickObject(objectId, objectX, objectY);
                getPA().resetAction();
            }
            if (clickObjectType == 3) {
                getActions().thirdClickObject(objectId, objectX, objectY);
                getPA().resetAction();
            }

        }

        if ((clickNpcType > 0) && Server.npcHandler.npcs[npcClickIndex] != null) {
            if (goodDistance(getX(), getY(), Server.npcHandler.npcs[npcClickIndex].getX(), Server.npcHandler.npcs[npcClickIndex].getY(), 1)) {
                if (clickNpcType == 1) {
                    turnPlayerTo(Server.npcHandler.npcs[npcClickIndex].getX(), Server.npcHandler.npcs[npcClickIndex].getY());
                    Server.npcHandler.npcs[npcClickIndex].facePlayer(playerId);
                    getActions().firstClickNpc(npcType);
                }
                if (clickNpcType == 2) {
                    turnPlayerTo(Server.npcHandler.npcs[npcClickIndex].getX(), Server.npcHandler.npcs[npcClickIndex].getY());
                    Server.npcHandler.npcs[npcClickIndex].facePlayer(playerId);
                    getActions().secondClickNpc(npcType);
                }
                if (clickNpcType == 3) {
                    turnPlayerTo(Server.npcHandler.npcs[npcClickIndex].getX(), Server.npcHandler.npcs[npcClickIndex].getY());
                    Server.npcHandler.npcs[npcClickIndex].facePlayer(playerId);
                    getActions().thirdClickNpc(npcType);
                }
            }
        }

        if (walkingToItem) {
            if (getX() == pItemX && getY() == pItemY || goodDistance(getX(), getY(), pItemX, pItemY, 1)) {
                walkingToItem = false;
                Server.itemHandler.removeGroundItem(this, pItemId, pItemX, pItemY, true);
            }
        }

        /*if(followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}*/

        getCombat().handlePrayerDrain();

        if (System.currentTimeMillis() - restoreStatsDelay > (curseActive[6] ? 69000 : 60000)) {
            restoreStatsDelay = System.currentTimeMillis();
            for (int level = 0; level < playerLevel.length; level++) {
                if (playerLevel[level] < getLevelForXP(playerXP[level])) {
                    if (level != 5) { // prayer doesn't restore
                        playerLevel[level] += 1;
                        getPA().setSkillLevel(level, playerLevel[level],
                        playerXP[level]);
                        getPA().refreshSkill(level);
                    }
                } else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
                    playerLevel[level] -= 1;
                    getPA().setSkillLevel(level, playerLevel[level],
                    playerXP[level]);
                    getPA().refreshSkill(level);
                }
            }
        }

        if (System.currentTimeMillis() - singleCombatDelay > 8500) {
            underAttackBy = 0;
        }
        if (System.currentTimeMillis() - singleCombatDelay2 > 8500) {
            underAttackBy2 = 0;
        }

        if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
            restoreStatsDelay = System.currentTimeMillis();
            for (int level = 0; level < playerLevel.length; level++) {
                if (playerLevel[level] < getLevelForXP(playerXP[level])) {
                    if (level != 5) { // prayer doesn't restore
                        playerLevel[level] += 1;
                        getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
                        getPA().refreshSkill(level);
                    }
                } else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
                    playerLevel[level] -= 1;
                    getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
                    getPA().refreshSkill(level);
                }
            }
        }

        /*if(System.currentTimeMillis() - teleGrabDelay >  1550 && usingMagic) {
			usingMagic = false;
			if(Server.itemHandler.itemExists(teleGrabItem, teleGrabX, teleGrabY)) {
				Server.itemHandler.removeGroundItem(this, teleGrabItem, teleGrabX, teleGrabY, true);
			}
		}*/




        if (inWild() && !isInArd() && !safeZone() && !BountyHunter.inBH(this) && !BountyHunter.safeArea(this)) {
            int modY = absY > 6400 ? absY - 6400 : absY;
            wildLevel = (((modY - 3520) / 8) + 1);
            getPA().walkableInterface(197);
            if (Config.SINGLE_AND_MULTI_ZONES) {
                if (inMulti()) {
                    getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			} else {
                    getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
                }
            } else {
                getPA().multiWay(-1);
                getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
            }
				getPA().showOption(3, 0, "Attack", 1);		
		}
		
		if (inIsland()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = 99;
			headIconPk = 2;
			getPA().walkableInterface(197);
			getPA().showOption(3, 0, "Attack", 1);
            getPA().sendFrame126("@red@Madness!", 199);
		}
		
		if (BountyHunter.inBH(this)) {
			wildLevel = 138;
			if (playerTarget > 0) {
				getPA().sendFrame126(targetName, 25350);
			} else {
				getPA().sendFrame126("None", 25350);
			}
			if (cantLeavePenalty > 0 || pickupPenalty > 0) {
				getPA().sendFrame126("" + BountyHunter.getTime(this) + " Sec", 25352);
			} else {
				getPA().sendFrame126("", 25352);
			}
			getPA().showOption(3, 0, "Attack", 1);
			getPA().walkableInterface(25347);
			getPA().sendFrame126(""+ (cantLeavePenalty > 0 ? ("Can't leave for:") : pickupPenalty > 0 ? ("Pickup penalty:") : ("")) + "", 25351);
			headIconPk = BountyHunter.getPlayerSkull(this);
			getPA().requestUpdates();		
		}
		
		if (inFunPk()) {
			wildLevel = 99;
			headIconPk = 2;
			getPA().walkableInterface(197);
			getPA().sendFrame126("@blu@FunPk!", 199);
			getPA().showOption(3, 0, "Attack", 1);	
		}
		
		/*if (FreeForAllArea()) {
			getPA().sendFrame126("Free For All", 6570);
			getPA().sendFrame126("Competitors. : " + FreeForAll.playerCount, 6572);
			getPA().sendFrame126("", 6664);
			getPA().walkableInterface(6673);
			getPA().showOption(3, 0, "Attack", 1);
		}*/
		
		if (inPcBoat()) {
				getPA().walkableInterface(21119);
		}
		
		if (inPcGame()) {
				getPA().walkableInterface(21100);
		}
		
		if(!inFunPk() && !inWild() && !inIsland() && !inPcBoat() && !inPcGame() && DoingTut == false && GettingReward == false){
				getPA().walkableInterface(-1);
				getPA().showOption(3, 0, "null", 1);	
		}

        if (!hasMultiSign && inMulti()) {
            hasMultiSign = true;
            getPA().multiWay(1);
        }

        if (hasMultiSign && !inMulti()) {
            hasMultiSign = false;
            getPA().multiWay(-1);
        }

        if (skullTimer > 0) {
            skullTimer--;
            if (skullTimer == 1) {
                isSkulled = false;
                attackedPlayers.clear();
                headIconPk = -1;
                skullTimer = -1;
                getPA().requestUpdates();
            }
        }

        if (isDead && respawnTimer == -6) {
            getPA().applyDead();
        }

        if (respawnTimer == 7) {
            respawnTimer = -6;
            getPA().giveLife();
        } else if (respawnTimer == 12) {
            respawnTimer--;
            startAnimation(0x900);
            poisonDamage = -1;
        }

        if (respawnTimer > -6) {
            respawnTimer--;
        }
        if (freezeTimer > -6) {
            freezeTimer--;
            if (frozenBy > 0) {
                if (Server.playerHandler.players[frozenBy] == null) {
                    freezeTimer = -1;
                    frozenBy = -1;
                } else if (!goodDistance(absX, absY, Server.playerHandler.players[frozenBy].absX, Server.playerHandler.players[frozenBy].absY, 20)) {
                    freezeTimer = -1;
                    frozenBy = -1;
                }
            }
        }
		
	/*if (!inWild() && safeTimer > 0){
		getPA().walkableInterface(197);
		wildLevel = (60);
		getPA().showOption(3, 0, "Attack", 1);
		getPA().sendFrame126("@or1@"+safeTimer, 199);
 }*/

        if (hitDelay > 0) {
            hitDelay--;
        }

        if (teleTimer > 0) {
            teleTimer--;
            if (!isDead) {
                if (teleTimer == 1 && newLocation > 0) {
                    teleTimer = 0;
                    getPA().changeLocation();
                }
                if (teleTimer == 5) {
                    teleTimer--;
                    getPA().processTeleport();
                }
                if (teleTimer == 9 && teleGfx > 0) {
                    teleTimer--;
                    gfx100(teleGfx);
                }
            } else {
                teleTimer = 0;
            }
        }

        if (hitDelay == 1) {
            if (oldNpcIndex > 0) {
                getCombat().delayedHit(oldNpcIndex);
            }
            if (oldPlayerIndex > 0) {
                getCombat().playerDelayedHit(oldPlayerIndex);
            }
        }

        if (attackTimer > 0) {
            attackTimer--;
        }

        if (attackTimer == 1) {
            if (npcIndex > 0 && clickNpcType == 0) {
                getCombat().attackNpc(npcIndex);
            }
            if (playerIndex > 0) {
                getCombat().attackPlayer(playerIndex);
            }
        } else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
            if (npcIndex > 0) {
                attackTimer = 0;
                getCombat().attackNpc(npcIndex);
            } else if (playerIndex > 0) {
                attackTimer = 0;
                getCombat().attackPlayer(playerIndex);
            }
        }

        if (timeOutCounter > Config.TIMEOUT) {
            disconnected = true;
        }

        timeOutCounter++;

        if (inTrade && tradeResetNeeded) {
            Client o = (Client) Server.playerHandler.players[tradeWith];
            if (o != null) {
                if (o.tradeResetNeeded) {
                    getTradeAndDuel().resetTrade();
                    o.getTradeAndDuel().resetTrade();
                }
            }
        }
    }
    private IoSession session;@SuppressWarnings("serial")
    private Queue < Packet > queuedPackets = new LinkedList < Packet > () {@Override
        public boolean add(Packet o) {
            for (int i = 0; i < Config.IMMEDIATE_PROCESS_PACKET_IDS.length; i++) {
                if (o.getId() == Config.IMMEDIATE_PROCESS_PACKET_IDS[i]) {
                    processPacket((Packet) o);
                    return false;
                }
            }
            super.add(o);
            return false;
        }
    };

    public void setCurrentTask(Future <? > task) {
        currentTask = task;
    }

    public Future <? > getCurrentTask() {
        return currentTask;
    }

    public synchronized Stream getInStream() {
        return inStream;
    }

    public synchronized int getPacketType() {
        return packetType;
    }

    public synchronized int getPacketSize() {
        return packetSize;
    }
    public synchronized boolean processPacket(Packet p) {
        //synchronized (this) {
        if (p == null) {
            return false;
        }
        inStream.currentOffset = 0;
        packetType = p.getId();
        packetSize = p.getLength();
        inStream.buffer = p.getData();
        if (packetType > 0) {
            PacketHandler.processPacket(this, packetType, packetSize);
        }
        timeOutCounter = 0;
        return true;
        //}
    }
    public synchronized Stream getOutStream() {
        return outStream;
    }

    public ItemAssistant getItems() {
        return itemAssistant;
    }

    public PlayerAssistant getPA() {
        return playerAssistant;
    }
    public TradeLog getTradeLog() {
        return tradeLog;
    }

    public DialogueHandler getDH() {
        return dialogueHandler;
    }

    public ShopAssistant getShops() {
        return shopAssistant;
    }
    public ClueScroll getClueScroll() {
        return clueScroll;
    }
    public TradeAndDuel getTradeAndDuel() {
        return tradeAndDuel;
    }

    public CombatAssistant getCombat() {
        return combatAssistant;
    }

    public ActionHandler getActions() {
        return actionHandler;
    }
	
	public PkReward getPkReward() {
		return pkreward;
	}

	public SoF getSoF() {
		return sof;
	}	
	
	public Achievements getAchievements() {
		return achievements;
	}	
	
	public JoinDate getJoinDate() {
		return join;
	}	
	
	public Highscores getHighscores() {
		return highscores;
	}

	public KillingStreak getStreak() {
		return killingStreak;
	}
  
	public PlayerKilling getKill() {
		return playerKilling;
	}

    public IoSession getSession() {
        return session;
    }

    public Potions getPotions() {
        return potions;
    }

    public PotionMixing getPotMixing() {
        return potionMixing;
    }

    public Food getFood() {
        return food;
    }

    /**
     * Skill Constructors
     */
    public Slayer getSlayer() {
        return slayer;
    }

    public Woodcutting getWoodcutting() {
        return woodcutting;
    }

    public Mining getMining() {
        return mine;
    }

    public Cooking getCooking() {
        return cooking;
    }

    public Agility getAgility() {
        return agility;
    }

    public Fishing getFishing() {
        return fish;
    }

    public Crafting getCrafting() {
        return crafting;
    }

    public Smithing getSmithing() {
        return smith;
    }

    public Farming getFarming() {
        return farming;
    }

    public Thieving getThieving() {
        return thieving;
    }

    public Firemaking getFiremaking() {
        return firemaking;
    }

    public SmithingInterface getSmithingInt() {
        return smithInt;
    }

    public Prayer getPrayer() {
        return prayer;
    }
    public Curse getCurse() {
        return curse;
    }


    /**
     * End of Skill Constructors
     */

    public void queueMessage(Packet arg1) {
        synchronized(queuedPackets) {
            queuedPackets.add(arg1);
        }
    }

    public synchronized boolean processQueuedPackets() {
        Packet p = null;
        synchronized(queuedPackets) {
            p = queuedPackets.poll();
        }
        if (p == null) {
            return false;
        }
        try {
            inStream.currentOffset = 0;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }
        packetType = p.getId();
        packetSize = p.getLength();
        inStream.buffer = p.getData();
        if (packetType > 0) {
            PacketHandler.processPacket(this, packetType, packetSize);
        }
        timeOutCounter = 0;
        return true;
    }
	
	
    public void startUpStuff() {
        World.getWorld().submit(new Event(500) {
            public void execute() {
                getPA().sendFrame126("@or1@Players Online : @gre@" + PlayerHandler.getPlayerCount() + " ", 16023);
                getPA().sendFrame126("@or1@Server Uptime:", 16026);
                getPA().sendFrame126("@gre@ " + Player.days + " @or1@days: @gre@" + Player.hours + " @or1@hours: @gre@" + Player.minutes + " @or1@min", 16027);
                getPA().sendFrame126("", 16028);			
                getPA().sendFrame126("@or1@Join Date: @gre@"+joinDate+"", 16029);						
                getPA().sendFrame126("@or1@Project-E Points: @gre@" + pePoints + "", 16030);
                getPA().sendFrame126("@or1@Kills: @gre@" + KC + "", 16031);
                getPA().sendFrame126("@or1@Deaths: @gre@" + DC + "", 16032);
                getPA().sendFrame126("@or1@Current KillStreak: @gre@" + killStreak + "", 16033);
                getPA().sendFrame126("@or1@Achievement Points: @gre@" + achievementPoints + "", 16035);
                getPA().sendFrame126("@or1@Vote Points: @gre@" + votePoints + "", 16034);
                getPA().sendFrame126("@or1@Member Points: @gre@" + donatorPoints + "", 16036);
                getPA().sendFrame126("@or1@View Achievements @gre@", 16037);
                getPA().sendFrame126("", 16038);
                getPA().sendFrame126("", 16039);
            }
        }); //Time it takes to send the message 
    }
	
	
		public void Commands() {
			getPA().showInterface(6308);
			getPA().sendFrame126("Close Window", 6401);
			getPA().sendFrame126("", 6399);
			getPA().sendFrame126("	Project-Equinox Commands", 6402);
			getPA().sendFrame126("	  All Commands Have :: Before  ", 6403);
			getPA().sendFrame126("---------------------------------------------------", 6404);
			getPA().sendFrame126("Players - Shows amount of players online", 6405);
			getPA().sendFrame126("Changepassword -  Changes password", 6406);
			getPA().sendFrame126("Clancommands - Clan related commands", 6407);
			getPA().sendFrame126("Rules - Shows rules interface", 6408);
			getPA().sendFrame126("Help (message) - Send staff help message", 6409);
			getPA().sendFrame126("Vote - Opens vote page", 6410);
			getPA().sendFrame126("Membership - All membership information", 6411);
			getPA().sendFrame126("Onlinestaff - Shows all online staff", 8578);
			getPA().sendFrame126("Market - Teleports you to market place", 8579);
			getPA().sendFrame126("Kdr - Shows your kill/death ratio", 8580);
			getPA().sendFrame126("Levels - Shouts your combat levels", 8581);
			getPA().sendFrame126(" ", 8582);
			getPA().sendFrame126(" ", 8583);
			getPA().sendFrame126(" ", 8584);
			getPA().sendFrame126(" ", 8585);
			getPA().sendFrame126(" ", 8586);
			getPA().sendFrame126(" ", 8587);
			getPA().sendFrame126(" ", 8588);
			getPA().sendFrame126(" ", 8589);
			getPA().sendFrame126(" ", 8590);
			getPA().sendFrame126(" ", 8591);
			getPA().sendFrame126(" ", 8592);
			getPA().sendFrame126(" ", 8593);
			getPA().sendFrame126(" ", 8594);
			getPA().sendFrame126(" ", 8595);
			getPA().sendFrame126(" ", 8596);
			getPA().sendFrame126(" ", 8597);
			getPA().sendFrame126(" ", 8598);
			getPA().sendFrame126(" ", 8599);
			getPA().sendFrame126(" ", 8600);
			getPA().sendFrame126(" ", 8601);
			getPA().sendFrame126(" ", 8602);
			getPA().sendFrame126(" ", 8603);
			getPA().sendFrame126(" ", 8604);
			getPA().sendFrame126(" ", 8605);
			getPA().sendFrame126(" ", 8606);
			getPA().sendFrame126(" ", 8607);
			getPA().sendFrame126(" ", 8608);
			getPA().sendFrame126(" ", 8609);
			getPA().sendFrame126(" ", 8610);
			getPA().sendFrame126(" ", 8611);
			getPA().sendFrame126(" ", 8612);
			getPA().sendFrame126(" ", 8613);
			getPA().sendFrame126(" ", 8614);
			getPA().sendFrame126(" ", 8615);
			getPA().sendFrame126(" ", 8616);
			getPA().sendFrame126(" ", 8617);
		}
		
		public void Rules() {
			getPA().showInterface(6308);
			getPA().sendFrame126("Close Window", 6401);
			getPA().sendFrame126("", 6399);
			getPA().sendFrame126("	Project-Equinox  Rules", 6402);
			getPA().sendFrame126("	  Be sure to rember the rules!  ", 6403);
			getPA().sendFrame126("---------------------------------------------------", 6404);
			getPA().sendFrame126("1. No flaming over yell.", 6405);
			getPA().sendFrame126("2. No Disrespecting Staff.", 6406);
			getPA().sendFrame126("3. No excessive Flaming/Trolling/Bullying.", 6407);
			getPA().sendFrame126("4. No spamming.", 6408);
			getPA().sendFrame126("5. No real world trading.", 6409);
			getPA().sendFrame126("6. No convincing other playes to rule break", 6410);
			getPA().sendFrame126("7. No pretending to hack", 6411);
			getPA().sendFrame126("8. No Scamming/Attempting to Scam.", 8578);
			getPA().sendFrame126("9. No Luring against Mods/Admins.", 8579);
			getPA().sendFrame126("10. No Bug Abusing.", 8580);
			getPA().sendFrame126("11. No Botting/Cheating/Macroing.", 8581);
			getPA().sendFrame126("12. No Asking For Staff.", 8582);
			getPA().sendFrame126("13. No Discrimination.", 8583);
			getPA().sendFrame126("14. No Staff Impersonation.", 8584);
			getPA().sendFrame126("15. No DDoSing. We'll take you down.", 8585);
			getPA().sendFrame126("16. No duping nor possession of duped item.", 8586);
			getPA().sendFrame126("17. No ban evading.", 8587);
			getPA().sendFrame126("18. No threatening the wellbeing of the server. You'll die.", 8588);
			getPA().sendFrame126("19. No advertising other servers/market links etc. Same as above.", 8589);
			getPA().sendFrame126(" ", 8590);
			getPA().sendFrame126(" ", 8591);
			getPA().sendFrame126(" ", 8592);
			getPA().sendFrame126(" ", 8593);
			getPA().sendFrame126(" ", 8594);
			getPA().sendFrame126(" ", 8595);
			getPA().sendFrame126(" ", 8596);
			getPA().sendFrame126(" ", 8597);
			getPA().sendFrame126(" ", 8598);
			getPA().sendFrame126(" ", 8599);
			getPA().sendFrame126(" ", 8600);
			getPA().sendFrame126(" ", 8601);
			getPA().sendFrame126(" ", 8602);
			getPA().sendFrame126(" ", 8603);
			getPA().sendFrame126(" ", 8604);
			getPA().sendFrame126(" ", 8605);
			getPA().sendFrame126(" ", 8606);
			getPA().sendFrame126(" ", 8607);
			getPA().sendFrame126(" ", 8608);
			getPA().sendFrame126(" ", 8609);
			getPA().sendFrame126(" ", 8610);
			getPA().sendFrame126(" ", 8611);
			getPA().sendFrame126(" ", 8612);
			getPA().sendFrame126(" ", 8613);
			getPA().sendFrame126(" ", 8614);
			getPA().sendFrame126(" ", 8615);
			getPA().sendFrame126(" ", 8616);
			getPA().sendFrame126(" ", 8617);
		}
		public void Achievements() {
			getPA().showInterface(6308);
			getPA().sendFrame126("Close Window", 6401);
			getPA().sendFrame126("", 6399);
			getPA().sendFrame126("	Project-Equinox Achievements", 6402);
			getPA().sendFrame126("	 Be sure to complete these!  ", 6403);
			getPA().sendFrame126("---------------------------------------------------", 6404);
			getPA().sendFrame126("---------------------------------------------------", 6405);
			getPA().sendFrame126("---------------------------------------------------", 6406);
			getPA().sendFrame126("---------------------------------------------------", 6407);
			getPA().sendFrame126("---------------------------------------------------", 6408);
			getPA().sendFrame126("---------------------------------------------------", 6409);
			getPA().sendFrame126("---------------------------------------------------", 6410);
			getPA().sendFrame126("---------------------------------------------------", 6411);
			getPA().sendFrame126("---------------------------------------------------", 8578);
			getPA().sendFrame126("---------------------------------------------------", 8579);
			getPA().sendFrame126("---------------------------------------------------", 8580);
			getPA().sendFrame126("---------------------------------------------------", 8581);
			getPA().sendFrame126("@red@Player Killing:@red@", 8582);
			getPA().sendFrame126("Kill 100 Player's", 8583);
			getPA().sendFrame126("Kill 145 Player's", 8584);
			getPA().sendFrame126("Kill 200 Player's", 8585);
			getPA().sendFrame126( "Kill 250 Player's", 8586);
			getPA().sendFrame126( "Kill 325 Player's", 8587);
			getPA().sendFrame126( "Kill 400 Player's", 8588);
			getPA().sendFrame126( "Kill 500 Player's", 8589);
			getPA().sendFrame126( "Kill 1500 Player's", 8590);
			getPA().sendFrame126( "Kill 2000 Player's", 8591);
			getPA().sendFrame126("@red@Skilling:@red@", 8592);
			getPA().sendFrame126( "99 Mining", 8593);
			getPA().sendFrame126( "99 Fishing", 8594);
			getPA().sendFrame126( "99 Herblore", 8595);
			getPA().sendFrame126( "99 Thieving", 8596);
			getPA().sendFrame126( "99 Crafting", 8597);
			getPA().sendFrame126( "99 Hunter", 8598);
			getPA().sendFrame126( "99 Summoning", 8599);
			getPA().sendFrame126( "99 Slayer", 8600);
			getPA().sendFrame126("All stat's 99", 8601);
			getPA().sendFrame126("@red@Kill Streaks:@red@", 8602);
			getPA().sendFrame126("Killstreak of 10", 8603);
			getPA().sendFrame126("Killstreak of 15", 8604);
			getPA().sendFrame126("Killstreak of 20", 8605);
			getPA().sendFrame126("Killstreak of 30", 8606);
			getPA().sendFrame126("Killstreak of 40", 8607);
			getPA().sendFrame126("Killstreak of 50", 8608);
			getPA().sendFrame126("Killstreak of 75", 8609);
			getPA().sendFrame126("Killstreak of 100", 8610);
			getPA().sendFrame126("@red@Miscellaneous:@red@", 8611);
			getPA().sendFrame126("2 Days Playing", 8612);
			getPA().sendFrame126( "7 Days Playing", 8613);
			getPA().sendFrame126( "10 Days Playing", 8614);
			getPA().sendFrame126( "100 Days Playing", 8615);
			getPA().sendFrame126( "Finish Kony Quest", 8616);
			getPA().sendFrame126( "Earn 1000 DT Points", 8617);
			getPA().sendFrame126("", 6405);
			getPA().sendFrame126(" ", 8618);
			getPA().sendFrame126(" ", 8619);
			getPA().sendFrame126(" ", 8620);
			getPA().sendFrame126(" ", 8621);
			getPA().sendFrame126(" ", 8622);
			getPA().sendFrame126(" ", 8623);
			getPA().sendFrame126(" ", 8624);
			getPA().sendFrame126(" ", 8625);
			getPA().sendFrame126(" ", 8626);
			getPA().sendFrame126(" ", 8627);
			getPA().sendFrame126(" ", 8628);
			getPA().sendFrame126(" ", 8629);
			getPA().sendFrame126(" ", 8630);
			getPA().sendFrame126(" ", 8631);
			getPA().sendFrame126(" ", 8632);
			getPA().sendFrame126(" ", 8633);
			getPA().sendFrame126(" ", 8634);
		}
	public final ArrayList<String> onlineAdmins = new ArrayList<String>();
	public final ArrayList<String> onlineMods = new ArrayList<String>();
	public final ArrayList<String> onlineOwners = new ArrayList<String>();

	public void getOnlineStaff() {
		onlineAdmins.clear();
		onlineMods.clear();
		onlineOwners.clear();
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Client staff = (Client) p;
				if (staff.playerRights == 1 && staff.privateChat == 0) {
					onlineMods.add(staff.playerName);
				}
				if (staff.playerRights == 2 && staff.privateChat == 0) {
					onlineAdmins.add(staff.playerName);
				}
				if (staff.playerRights == 3 && staff.privateChat == 0) {
					onlineOwners.add(staff.playerName);
				}
			}
		}
	}
	
	
    public void mymessage() {
        if (inWild()) return;


        World.getWorld().submit(new Event(350000) {
            public void execute() {
                getPA().loadRandomMessages();
            }
        }); //Time it takes to send the message 
    }

	public void BlowShitUp() {
		if (getItems().freeSlots() >= 1) {
				pePoints -= 50;
				getPkReward().RunThis();
				sendMessage("[ <col=2784FF>Project-E Points Statue </col>] Good luck!");
		} else {
				getPA().removeAllWindows();
				getPA().sendStatement("You need atleast 1 free spot in your inventory.");
		}
	}

	public Client getClient(String name) {
		name = name.toLowerCase();
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(validClient(i)) {
				Client client = getClient(i);
				if(client.playerName.toLowerCase().equalsIgnoreCase(name)) {
					return client;
				}
			}
		}
		return null;
	}
	public boolean validClient(String name) {
		return validClient(getClient(name));
	}
	public boolean validClient(Client client) {
		return (client != null && !client.disconnected);
	}
	public boolean validNpc(int index) {
		if (index < 0 || index >= Config.MAX_NPCS) {
			return false;
		}
		NPC n = getNpc(index);
		if (n != null) {
			return true;
		}
		return false;
	}
	public NPC getNpc(int index) {
		return ((NPC) Server.npcHandler.npcs[index]);
	}
	public void yell(String s) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				getClient(i).sendMessage(s);
			}
		}
	}

	public void Achievement() {
		Achievements.CheckKillingAchievement();
	}
	
	public void MemberStatus() {
		if (playerRights == 1) {
				getPA().sendStatement("Membership Rank: @red@Extreme Member @bla@|| Membership Points: @red@" + donatorPoints + "");
		}
		if (playerRights == 2) {
				getPA().sendStatement("Membership Rank: @red@Extreme Member @bla@|| Membership Points: @red@" + donatorPoints + "");
		}
		if (playerRights == 3) {
				getPA().sendStatement("Membership Rank: @red@Extreme Member @bla@|| Membership Points: @red@" + donatorPoints + "");
		}
		if (playerRights == 4) {
				getPA().sendStatement("Membership Rank: @red@Normal Member @bla@|| Membership Points: @red@" + donatorPoints + "");
		}
		if (playerRights == 5) {
				getPA().sendStatement("Membership Rank: @red@Super Member @bla@|| Membership Points: @red@" + donatorPoints + "");
		}
		if (playerRights == 6) {
				getPA().sendStatement("Membership Rank: @red@Extreme Member @bla@|| Membership Points: @red@" + donatorPoints + "");
		}
		if (playerRights == 0) {
				getPA().sendStatement("Membership Rank: @red@None! @bla@|| Membership Points: @red@None@bla@!");
				getPA().showInterface(25900);
				sendMessage("You have no membership rank!");
		}	
	
	}
		
	public void wildyditch() {
	if (absY <= objectY){	
		startAnimation(6132);
		getPA().walkTo(0, +3);
	} else if (objectY < absY) {
		startAnimation(6132);
		getPA().walkTo(0, -3);
		} 
	}	

    public void shopsload() {



        World.getWorld().submit(new Event(60000) {
            public void execute() {
                sendMessage("Alert##Notification##Shops Reloaded");
            }
        }); //Time it takes to send the message 
    }

    public void correctCoordinates() {
        if (inPcGame()) {
            getPA().movePlayer(2657, 2639, 0);
        }
        if (inFightCaves()) {
            getPA().movePlayer(absX, absY, playerId * 4);
            sendMessage("Your wave will start in 10 seconds.");
            World.getWorld().submit(new Event(10000) {
                public void execute() {
                    Server.fightCaves.spawnNextWave((Client) Server.playerHandler.players[playerId]);
                    this.stop();
                }
            });

        }

    }

}