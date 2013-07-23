package server.model.players.combat;

import server.model.players.*;
import server.model.npcs.NPC;
import server.model.npcs.NPCHandler;
import server.Config;
import server.Server;
import server.util.Misc;
import server.event.Event;
import server.event.EventManager;
import server.World;
import server.model.minigames.*;
import server.model.minigames.CastleWars;

public class CombatAssistant {

    private Client c;
	
    public CombatAssistant(Client Client) {
        this.c = Client;
    }

    public int[][] slayerReqs = {
        {
            1648, 5
        }, {
            1612, 15
        }, {
            1643, 45
        }, {
            1618, 50
        }, {
            1624, 65
        }, {
            1610, 75
        }, {
            1613, 80
        }, {
            1615, 85
        }, {
            2783, 90
        }
    };

    public boolean goodSlayer(int i) {
        for (int j = 0; j < slayerReqs.length; j++) {
            if (slayerReqs[j][0] == Server.npcHandler.npcs[i].npcType) {
                if (slayerReqs[j][1] > c.playerLevel[c.playerSlayer]) {
                    c.sendMessage("You need a slayer level of " + slayerReqs[j][1] + " to harm this NPC.");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean kalphite1(int i) {
        switch (Server.npcHandler.npcs[i].npcType) {
            case 1158:
                return true;
        }
        return false;
    }

    public boolean kalphite2(int i) {
        switch (Server.npcHandler.npcs[i].npcType) {
            case 1160:
                return true;
        }
        return false;
    }

    /**
     * Attack Npcs
     */
    public void attackNpc(int i) {
        if (Server.npcHandler.npcs[i] != null) {
            if (Server.npcHandler.npcs[i].isDead || Server.npcHandler.npcs[i].MaxHP <= 0) {
                c.usingMagic = false;
                c.faceUpdate(0);
                c.npcIndex = 0;
                return;
            }
            if (c.respawnTimer > 0) {
                c.npcIndex = 0;
                return;
            }
            /* DEGRADING */

            if (c.playerEquipment[c.playerHands] == 11095) {
                c.bracelet1 -= 1;
                c.degradebrace();
            }
            if (c.playerEquipment[c.playerHands] == 11097) {
                c.bracelet2 -= 1;
                c.degradebrace2();
            }
            if (c.playerEquipment[c.playerHands] == 11101) {
                c.bracelet3 -= 1;
                c.degradebrace3();
            }
            if (c.playerEquipment[c.playerHands] == 11103) {
                c.bracelet4 -= 1;
                c.degradebrace4();
            }
            if (c.playerEquipment[c.playerHands] == 11099) {
                c.bracelet5 -= 1;
                c.degradebrace5();
            }

            if (c.playerEquipment[c.playerWeapon] == 18349) {
                c.chaoticRapier -= 1;
                c.degraderapier();
            }

            if (c.playerEquipment[c.playerWeapon] == 18351) {
                c.chaoticLong -= 1;
                c.degradelong();
            }
            if (c.playerEquipment[c.playerWeapon] == 18355) {
                c.chaoticStaff -= 1;
                c.degradestaff();
            }

            if (c.playerEquipment[c.playerWeapon] == 18353) {
                c.chaoticMaul -= 1;
                c.degrademaul();
            }
            if (c.playerEquipment[c.playerWeapon] == 18537) {
                c.chaoticBow -= 1;
                c.degradebow();
            }
            if (c.playerEquipment[c.playerWeapon] == 18359) {
                c.chaoticShield -= 1;
                c.degradeshield();
            }
            if (c.playerEquipment[c.playerWeapon] == 13899) {
                c.vlsLeft -= 1;
                c.degradeVls();
            }








            if (c.playerEquipment[c.playerWeapon] == 13905) {
                c.vSpearLeft -= 1;
                c.degradeVSpear();
            }
            if (c.playerEquipment[c.playerChest] == 13858) {
                c.zTopLeft -= 1;
                c.degradeZTop();
            }
            if (c.playerEquipment[c.playerLegs] == 13861) {
                c.zBottomLeft -= 1;
                c.degradeZBottom();
            }
            if (c.playerEquipment[c.playerWeapon] == 13902) {
                c.statLeft -= 1;
                c.degradeStat();
            }
            if (c.playerEquipment[c.playerChest] == 13887) {
                c.vTopLeft -= 1;
                c.degradeVTop();
            }
            if (c.playerEquipment[c.playerLegs] == 13893) {
                c.vLegsLeft -= 1;
                c.degradeVLegs();
            }
            if (c.playerEquipment[c.playerWeapon] == 13867) {
                c.zStaffLeft -= 1;
                c.degradeZStaff();
            }
            if (c.playerEquipment[c.playerHat] == 13864) {
                c.zHoodLeft -= 1;
                c.degradeZHood();
            }
            if (c.playerEquipment[c.playerChest] == 13870) {
                c.mBodyLeft -= 1;
                c.degradeMBody();
            }
            if (c.playerEquipment[c.playerLegs] == 13873) {
                c.mChapsLeft -= 1;
                c.degradeMChaps();
            }
            if (c.playerEquipment[c.playerChest] == 13884) {
                c.sTopLeft -= 1;
                c.degradeSTop();
            }
            if (c.playerEquipment[c.playerLegs] == 13890) {
                c.sLegsLeft -= 1;
                c.degradeSLegs();
            }
            if (c.playerEquipment[c.playerHat] == 13896) {
                c.sHelmLeft -= 1;
                c.degradeSHelm();
            }
            if (Server.npcHandler.npcs[i].underAttackBy > 0 && Server.npcHandler.npcs[i].underAttackBy != c.playerId && !Server.npcHandler.npcs[i].inMulti()) {
                c.npcIndex = 0;
                c.sendMessage("This monster is already in combat.");
                return;
            }
            if ((c.underAttackBy > 0 || c.underAttackBy2 > 0) && c.underAttackBy2 != i && !c.inMulti()) {
                resetPlayerAttack();
                c.sendMessage("I am already under attack.");
                return;
            }
            if (!goodSlayer(i)) {
                resetPlayerAttack();
                return;
            }
            if (Server.npcHandler.npcs[i].spawnedBy != c.playerId && Server.npcHandler.npcs[i].spawnedBy > 0) {
                resetPlayerAttack();
                c.sendMessage("This monster was not spawned for you.");
                return;
            }
            c.followId2 = i;
            c.followId = 0;
            if (c.attackTimer <= 0) {
                boolean usingBow = false;
                boolean usingArrows = false;
                boolean usingOtherRangeWeapons = false;
                boolean usingCross = c.playerEquipment[c.playerWeapon] == 9185 && c.playerEquipment[c.playerWeapon] == 18357;

                boolean usingCannon = c.playerEquipment[c.playerWeapon] == 13022;
                c.bonusAttack = 0;
                c.rangeItemUsed = 0;
                c.projectileStage = 0;
                /*if (kalphite1(i) && !c.fullVerac() || c.usingMagic) {
                    resetPlayerAttack();
                    c.sendMessage("Your attacks have no effect on the Queen.");
                    return;
                }*/
                if (c.autocasting) {
                    c.spellId = c.autocastId;
                    c.usingMagic = true;
                }
                if (c.spellId > 0) {
                    c.usingMagic = true;
                }
                c.attackTimer = getAttackDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.specAccuracy = 1.0;
                c.specDamage = 1.0;
                if (!c.usingMagic) {
                    for (int bowId: c.BOWS) {
                        if (c.playerEquipment[c.playerWeapon] == bowId) {
                            usingBow = true;
                            for (int arrowId: c.ARROWS) {
                                if (c.playerEquipment[c.playerArrows] == arrowId) {
                                    usingArrows = true;
                                }
                            }
                        }
                    }

                    for (int otherRangeId: c.OTHER_RANGE_WEAPONS) {
                        if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
                            usingOtherRangeWeapons = true;
                        }
                    }
                }
                if (armaNpc(i) && !usingCannon && !usingCross && !usingBow && !c.usingMagic && !usingCrystalBow() && !usingOtherRangeWeapons) {
                    resetPlayerAttack();
                    return;
                }
                if ((!c.goodDistance(c.getX(), c.getY(), Server.npcHandler.npcs[i].getX(), Server.npcHandler.npcs[i].getY(), 2) && (usingHally() && !usingOtherRangeWeapons && !usingBow && !usingCross && !c.usingMagic)) || (!c.goodDistance(c.getX(), c.getY(), Server.npcHandler.npcs[i].getX(), Server.npcHandler.npcs[i].getY(), 4) && (usingOtherRangeWeapons && !usingCross && !usingBow && !c.usingMagic)) || (!c.goodDistance(c.getX(), c.getY(), Server.npcHandler.npcs[i].getX(), Server.npcHandler.npcs[i].getY(), 1) && (!usingOtherRangeWeapons && !usingHally() && !usingCross && !usingBow && !c.usingMagic)) || ((!c.goodDistance(c.getX(), c.getY(), Server.npcHandler.npcs[i].getX(), Server.npcHandler.npcs[i].getY(), 8) && (usingBow || usingCross || c.usingMagic)))) {
                    c.attackTimer = 2;
                    return;
                }

                if (!usingCannon && !usingCross && !usingArrows && usingBow && (c.playerEquipment[c.playerWeapon] == 7119 || c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223)) {
                    c.sendMessage("You have run out of arrows!");
                    c.stopMovement();
                    c.npcIndex = 0;
                    return;
                }
                if (correctBowAndArrows() < c.playerEquipment[c.playerArrows] && Config.CORRECT_ARROWS && usingBow && !usingCrystalBow() && c.playerEquipment[c.playerWeapon] != 9185 && c.playerEquipment[c.playerWeapon] != 13022 && c.playerEquipment[c.playerWeapon] != 18357) {
                    c.sendMessage("You can't use " + c.getItems().getItemName(c.playerEquipment[c.playerArrows]).toLowerCase() + "s with a " + c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + ".");
                    c.stopMovement();
                    c.npcIndex = 0;
                    return;
                }

                if (usingCross && !properBolts()) {
                    c.sendMessage("You must use bolts with a crossbow.");
                    c.stopMovement();
                    resetPlayerAttack();
                    return;
                }

                if (usingBow || c.usingMagic || usingOtherRangeWeapons || (c.goodDistance(c.getX(), c.getY(), Server.npcHandler.npcs[i].getX(), Server.npcHandler.npcs[i].getY(), 2) && usingHally())) {
                    c.stopMovement();
                }

                if (!checkMagicReqs(c.spellId)) {
                    c.stopMovement();
                    c.npcIndex = 0;
                    return;
                }

                c.faceUpdate(i);
                c.slayerHelmetEffect = c.playerEquipment[c.playerHat] == 15492 && c.slayerTask == i;
                //c.specAccuracy = 1.0;
                //c.specDamage = 1.0;
                Server.npcHandler.npcs[i].underAttackBy = c.playerId;
                Server.npcHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
                if (c.usingSpecial && !c.usingMagic) {
                    if (checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
                        c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
                        c.lastArrowUsed = c.playerEquipment[c.playerArrows];
                        activateSpecial(c.playerEquipment[c.playerWeapon], i);
                        return;
                    } else {
                        c.sendMessage("You don't have the required special energy to use this attack.");
                        c.usingSpecial = false;
                        c.getItems().updateSpecialBar();
                        c.npcIndex = 0;
                        return;
                    }
                }
                if (usingBow || c.usingMagic || usingOtherRangeWeapons) {
                    c.mageFollow = true;
                } else {
                    c.mageFollow = false;
                }
                c.specMaxHitIncrease = 0;
                if (!c.usingMagic) {
                    c.startAnimation(getWepAnim(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()));
                } else {
                    c.startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
                }
                c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
                c.lastArrowUsed = c.playerEquipment[c.playerArrows];
                if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons) { // melee hit delay
                    c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                    c.projectileStage = 0;
                    c.oldNpcIndex = i;
                }

                if (usingBow && !usingOtherRangeWeapons && !c.usingMagic || usingCross) { // range hit delay					
                    if (usingCannon) c.usingBow = true;

                    if (usingCross) c.usingBow = true;
                    if (c.fightMode == 2) c.attackTimer--;
                    c.lastArrowUsed = c.playerEquipment[c.playerArrows];
                    c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
                    c.gfx100(getRangeStartGFX());
                    c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                    c.projectileStage = 1;
                    c.oldNpcIndex = i;
                    if (c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223) {
                        c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
                        c.crystalBowArrowCount++;
                        c.lastArrowUsed = 0;
                    } else {
                        c.rangeItemUsed = c.playerEquipment[c.playerArrows];
                        c.getItems().deleteArrow();
                    }
                    fireProjectileNpc();
                }


                if (usingOtherRangeWeapons && !c.usingMagic && !usingBow) { // knives, darts, etc hit delay		
                    c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
                    c.getItems().deleteEquipment();
                    c.gfx100(getRangeStartGFX());
                    c.lastArrowUsed = 0;
                    c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                    c.projectileStage = 1;
                    c.oldNpcIndex = i;
                    if (c.fightMode == 2) c.attackTimer--;
                    fireProjectileNpc();
                }

                if (usingBow && usingCross && c.usingMagic && usingOtherRangeWeapons) {
                    c.getPA().followPlayer();
                    c.stopMovement();
                } else {
                    c.followId = 0;
                    c.followId2 = i;
                }

                if (c.usingMagic) { // magic hit delay
                    int pX = c.getX();
                    int pY = c.getY();
                    int nX = Server.npcHandler.npcs[i].getX();
                    int nY = Server.npcHandler.npcs[i].getY();
                    int offX = (pY - nY) * -1;
                    int offY = (pX - nX) * -1;
                    c.castingMagic = true;
                    c.projectileStage = 2;
                    if (c.MAGIC_SPELLS[c.spellId][3] > 0) {
                        if (getStartGfxHeight() == 100) {
                            c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
                        } else {
                            c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
                        }
                    }
                    if (c.MAGIC_SPELLS[c.spellId][4] > 0) {
                        c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, c.MAGIC_SPELLS[c.spellId][4], getStartHeight(), getEndHeight(), i + 1, 50);
                    }
                    c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                    c.oldNpcIndex = i;
                    c.oldSpellId = c.spellId;
                    c.spellId = 0;
                    if (!c.autocasting) c.npcIndex = 0;
                }
                if (Misc.random(15) == 0) {
                    npcleechAttack(0);
                }
                if (Misc.random(15) == 0) {
                    npcleechDefence(0);
                }
                if (Misc.random(15) == 0) {
                    npcleechStrength(0);
                }
                if (Misc.random(20) == 0) {
                    npcleechSpecial(0);
                }
                if (Misc.random(15) == 0) {
                    npcleechRanged(0);
                }
                if (Misc.random(15) == 0) {
                    npcleechMagic(0);
                }
                if (c.soulSplitDelay <= 0) {
                    applynpcSoulSplit(i, 1);
                }
                if (usingBow && Config.CRYSTAL_BOW_DEGRADES) { // crystal bow degrading
                    if (c.playerEquipment[c.playerWeapon] == 4212) { // new crystal bow becomes full bow on the first shot
                        c.getItems().wearItem(4214, 1, 3);
                    }

                    if (c.crystalBowArrowCount >= 250) {
                        switch (c.playerEquipment[c.playerWeapon]) {

                            case 4223:
                                // 1/10 bow
                                c.getItems().wearItem(-1, 1, 3);
                                c.sendMessage("Your crystal bow has fully degraded.");
                                if (!c.getItems().addItem(4207, 1)) {
                                    Server.itemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), 1, c.getId());
                                }
                                c.crystalBowArrowCount = 0;
                                break;

                            default:
                                c.getItems().wearItem(++c.playerEquipment[c.playerWeapon], 1, 3);
                                c.sendMessage("Your crystal bow degrades.");
                                c.crystalBowArrowCount = 0;
                                break;


                        }
                    }
                }
            }
        }
    }
    public static int finalMagicDamage(Client c) {
        double damage = c.MAGIC_SPELLS[c.oldSpellId][6];
        double damageMultiplier = 1;
        if (c.playerLevel[c.playerMagic] > c.getLevelForXP(c.playerXP[6]) && c.getLevelForXP(c.playerXP[6]) >= 95) damageMultiplier += .004 * (c.playerLevel[c.playerMagic] - 99);
        else damageMultiplier = 1;
        switch (c.playerEquipment[c.playerWeapon]) {
            case 18371:
                // Gravite Staff
                damageMultiplier += .05;
                break;
            case 4675:
                // Ancient Staff
            case 4710:
                // Ahrim's Staff
            case 4862:
                // Ahrim's Staff
            case 4864:
                // Ahrim's Staff
            case 4865:
                // Ahrim's Staff
            case 6914:
                // Master Wand
            case 8841:
                // Void Knight Mace
            case 13867:
                // Zuriel's Staff
            case 13869:
                // Zuriel's Staff (Deg)
                damageMultiplier += .10;
                break;
            case 15486:
                // Staff of Light
                damageMultiplier += .15;
                break;
            case 18355:
                // Chaotic Staff
                damageMultiplier += .23;
                break;
        }
        switch (c.playerEquipment[c.playerAmulet]) {
            case 18333:
                // Arcane Pulse
                damageMultiplier += .05;
                break;
            case 18334:
                // Arcane Blast
                damageMultiplier += .10;
                break;
            case 18335:
                // Arcane Stream
                damageMultiplier += .15;
                break;
        }
        damage *= damageMultiplier;
        return (int) damage;
    }
    public void delayedHit(int i) { // npc hit delay
        if (Server.npcHandler.npcs[i] != null) {
            if (Server.npcHandler.npcs[i].isDead) {
                c.npcIndex = 0;
                return;
            }
            Server.npcHandler.npcs[i].facePlayer(c.playerId);

            if (Server.npcHandler.npcs[i].underAttackBy > 0 && Server.npcHandler.getsPulled(i)) {
                Server.npcHandler.npcs[i].killerId = c.playerId;
            } else if (Server.npcHandler.npcs[i].underAttackBy < 0 && !Server.npcHandler.getsPulled(i)) {
                Server.npcHandler.npcs[i].killerId = c.playerId;
            }
            c.lastNpcAttacked = i;
            if (c.projectileStage == 0 && !c.usingMagic && !c.castingMagic) { // melee hit damage
                applyNpcMeleeDamage(i, 1, Misc.random(calculateMeleeMaxHit()));
                if (c.doubleHit && !c.usingClaws) {
                    applyNpcMeleeDamage(i, 2, Misc.random(calculateMeleeMaxHit()));
                }
                if (c.doubleHit && c.usingClaws) {
                    applyNpcMeleeDamage(i, 2, c.previousDamage / 2);
                }
            }

            if (c.playerEquipment[c.playerWeapon] == 4718 && c.playerEquipment[c.playerHat] == 4716 && c.playerEquipment[c.playerChest] == 4720 && c.playerEquipment[c.playerLegs] == 4722) {
                double dharokMultiplier = ((1 - ((float) c.playerLevel[3] / (float) c.getPA().getLevelForXP(c.playerXP[3]))) * 0.95) + 1;
                //base *= dharokMultiplier;
            }

            if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
                int damage = Misc.random(rangeMaxHit());
                int damage2 = -1;
                if (c.lastWeaponUsed == 15701 || c.lastWeaponUsed == 15702 || c.lastWeaponUsed == 15703 || c.lastWeaponUsed == 15704 || c.lastWeaponUsed == 14481 || c.lastWeaponUsed == 15704 || c.lastWeaponUsed == 11235 || c.lastWeaponUsed == 15702 || c.lastWeaponUsed == 15701 || c.lastWeaponUsed == 15703 || c.lastWeaponUsed == 14482 || c.bowSpecShot == 1) damage2 = Misc.random(rangeMaxHit());
                boolean ignoreDef = false;
                if (Misc.random(5) == 1 && c.lastArrowUsed == 9243 && c.playerEquipment[c.playerWeapon] == 9185) {
                    ignoreDef = true;
                    Server.npcHandler.npcs[i].gfx0(758);
                }
                if (Misc.random(4) == 1 && c.lastArrowUsed == 9245 && c.playerEquipment[c.playerWeapon] == 18357) {
                    ignoreDef = true;
                    Server.npcHandler.npcs[i].gfx0(753);
                }


                if (Misc.random(Server.npcHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()) && !ignoreDef) {
                    damage = 0;
                } else if (Server.npcHandler.npcs[i].npcType == 2881 || Server.npcHandler.npcs[i].npcType == 2883 || Server.npcHandler.npcs[i].npcType == 3340 && !ignoreDef) {
                    damage = 0;
                }
                if (Server.npcHandler.npcs[i].npcType == 8525) { //nomad
                    if (Misc.random(2) == 1) damage = (int) damage * 60 / 100;


                }

                if (Misc.random(4) == 1 && c.lastArrowUsed == 9242 && damage > 0 && c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 18357) {
                    //damage = Server.npcHandler.npcs[i].HP/15;
                    damage = Misc.random(30);
                    //if (Misc.random(Server.npcHandler.npcs[i].defence) > Misc.random(10+calculateRangeAttack()))		
                }



                if (c.lastWeaponUsed == 15701 || c.lastWeaponUsed == 15702 || c.lastWeaponUsed == 11235 || c.lastWeaponUsed == 15703 || c.lastWeaponUsed == 15704 || c.bowSpecShot == 1) {
                    if (Misc.random(Server.npcHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack())) damage2 = 0;
                }
                if (c.dbowSpec) {
                    Server.npcHandler.npcs[i].gfx100(1100);
                    if (damage < 8) damage = 8;
                    if (damage2 < 8) damage2 = 8;
                    c.dbowSpec = false;
                }
                if (damage > 0) {
                    if (NPCHandler.npcs[i].npcType >= 52 && NPCHandler.npcs[i].npcType <= 55) {
                        c.dkDamage += damage;
                    }
                }
                if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244 && c.playerEquipment[c.playerWeapon] == 18357) {
                    damage *= 1.55;
                    Server.npcHandler.npcs[i].gfx0(756);
                }
                if (damage > 0 && Misc.random(10) == 1 && c.lastArrowUsed == 9245 && c.playerEquipment[c.playerWeapon] == 18357) {
                    damage *= 1.95;
                    Server.npcHandler.npcs[i].gfx0(753);
                }
                if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244 && c.playerEquipment[c.playerWeapon] == 9185) {
                    damage *= 1.45;
                    Server.npcHandler.npcs[i].gfx0(756);
                }
                if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9245 && c.playerEquipment[c.playerWeapon] == 9185) {
                    damage *= 1.55;
                    Server.npcHandler.npcs[i].gfx0(753);
                }
                if (Server.npcHandler.npcs[i].HP - damage < 0) {
                    damage = Server.npcHandler.npcs[i].HP;
                }
                if (Server.npcHandler.npcs[i].HP - damage <= 0 && damage2 > 0) {
                    damage2 = 0;
                }
                if (c.fightMode == 3) {
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 4);
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 1);
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 3);
                    c.getPA().refreshSkill(1);
                    c.getPA().refreshSkill(3);
                    c.getPA().refreshSkill(4);
                } else {
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE), 4);
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 3);
                    c.getPA().refreshSkill(3);
                    c.getPA().refreshSkill(4);
                }
                if (damage > 0) {
                    if (Server.npcHandler.npcs[i].npcType >= 6142 && Server.npcHandler.npcs[i].npcType <= 6145) {
                        c.pcDamage += damage;
                    }
                }
                boolean dropArrows = true;

                for (int noArrowId: c.NO_ARROW_DROP) {
                    if (c.lastWeaponUsed == noArrowId) {
                        dropArrows = false;
                        break;
                    }
                }
                if (dropArrows) {
                    c.getItems().dropArrowNpc();
                }
                Server.npcHandler.npcs[i].underAttack = true;
                Server.npcHandler.npcs[i].hitDiff = damage;
                Server.npcHandler.npcs[i].HP -= damage;
                if (damage2 > -1) {
                    Server.npcHandler.npcs[i].hitDiff2 = damage2;
                    Server.npcHandler.npcs[i].HP -= damage2;
                    c.totalDamageDealt += damage2;
                }
                if (c.killingNpcIndex != c.oldNpcIndex) {
                    c.totalDamageDealt = 0;
                }
                c.killingNpcIndex = c.oldNpcIndex;
                c.totalDamageDealt += damage;
                Server.npcHandler.npcs[i].hitUpdateRequired = true;
                if (damage2 > -1) Server.npcHandler.npcs[i].hitUpdateRequired2 = true;
                Server.npcHandler.npcs[i].updateRequired = true;

            } else if (c.projectileStage > 0) { // magic hit damage
                int damage = Misc.random((finalMagicDamage(c)) + c.totalMagicBonus());
                if (godSpells()) {
                    if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
                        damage += Misc.random(10);
                    }
                }
                boolean magicFailed = false;
                //c.npcIndex = 0;
                int bonusAttack = getBonusAttack(i);
                if (Misc.random(Server.npcHandler.npcs[i].defence) > 10 + Misc.random(mageAtk()) + bonusAttack) {
                    damage = 0;
                    magicFailed = true;
                } else if (Server.npcHandler.npcs[i].npcType == 2881 || Server.npcHandler.npcs[i].npcType == 2882) {
                    damage = 0;
                    magicFailed = true;
                }
                for (int j = 0; j < Server.npcHandler.getNPCs().length; j++) {
                    if (Server.npcHandler.getNPCs()[j] != null && Server.npcHandler.getNPCs()[j].MaxHP > 0) {
                        int nX = Server.npcHandler.getNPCs()[j].getX();
                        int nY = Server.npcHandler.getNPCs()[j].getY();
                        int pX = Server.npcHandler.getNPCs()[i].getX();
                        int pY = Server.npcHandler.getNPCs()[i].getY();
                        if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1) && (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
                            if (c.getCombat().multis() && Server.npcHandler.getNPCs()[i].inMulti()) {
                                Client p = (Client) PlayerHandler.players[c.playerId];

                                Server.npcHandler.attackPlayer(p, j);
                            }
                        }
                    }
                }

                if (Server.npcHandler.npcs[i].HP - damage < 0) {
                    damage = Server.npcHandler.npcs[i].HP;
                }

                c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE), 6);
                c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE / 3), 3);
                c.getPA().refreshSkill(3);
                c.getPA().refreshSkill(6);
                if (damage > 0) {
                    if (Server.npcHandler.npcs[i].npcType >= 6142 && Server.npcHandler.npcs[i].npcType <= 6145) {
                        c.pcDamage += damage;
                    }
                }
                if (getEndGfxHeight() == 100 && !magicFailed) { // end GFX
                    NPCHandler.npcs[i].gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
                } else if (!magicFailed) {
                    NPCHandler.npcs[i].gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
                }
                multiSpellOnNPC(i);

                if (magicFailed) {
                    Server.npcHandler.npcs[i].gfx100(85);
                }
                if (!magicFailed) {
                    int freezeDelay = getFreezeTime(); //freeze 
                    if (freezeDelay > 0 && Server.npcHandler.npcs[i].freezeTimer == 0) {
                        Server.npcHandler.npcs[i].freezeTimer = freezeDelay;
                    }
                    switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
                        case 12901:
                        case 12919:
                            // blood spells
                        case 12911:
                        case 12929:
                            int heal = Misc.random(damage / 2);
                            if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(c.playerXP[3])) {
                                c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                            } else {
                                c.playerLevel[3] += heal;
                            }
                            c.getPA().refreshSkill(3);
                            break;
                    }

                }
                Server.npcHandler.npcs[i].underAttack = true;
                if (finalMagicDamage(c) != 0) {
                    Server.npcHandler.npcs[i].hitDiff = damage;
                    Server.npcHandler.npcs[i].HP -= damage;
                    Server.npcHandler.npcs[i].hitUpdateRequired = true;
                    c.totalDamageDealt += damage;
                }
                c.killingNpcIndex = c.oldNpcIndex;
                Server.npcHandler.npcs[i].updateRequired = true;
                c.usingMagic = false;
                c.castingMagic = false;
                c.oldSpellId = 0;
            }
        }

        if (c.bowSpecShot <= 0) {
            c.oldNpcIndex = 0;
            c.projectileStage = 0;
            c.doubleHit = false;
            c.lastWeaponUsed = 0;
            c.bowSpecShot = 0;
        }
        if (c.bowSpecShot >= 2) {
            c.bowSpecShot = 0;
            //c.attackTimer = getAttackDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
        }
        if (c.bowSpecShot == 1) {
            fireProjectileNpc();
            c.hitDelay = 2;
            c.bowSpecShot = 0;
        }
    }


    public void applyNpcMeleeDamage(int i, int damageMask, int damage) {
        c.previousDamage = damage;
        boolean fullVeracsEffect = c.getPA().fullVeracs() && Misc.random(3) == 1;
        if (Server.npcHandler.npcs[i].HP - damage < 0) {
            damage = Server.npcHandler.npcs[i].HP;
        }

        if (!fullVeracsEffect) {
            if (Misc.random(Server.npcHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack())) {
                damage = 0;
            } else if (Server.npcHandler.npcs[i].npcType == 2882 || Server.npcHandler.npcs[i].npcType == 2883) {
                damage = 0;
            }
        }
        boolean guthansEffect = false;
        if (c.getPA().fullGuthans()) {
            if (Misc.random(3) == 1) {
                guthansEffect = true;
            }
        }
        if (c.fightMode == 3) {
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 0);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 1);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 2);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 3);
            c.getPA().refreshSkill(0);
            c.getPA().refreshSkill(1);
            c.getPA().refreshSkill(2);
            c.getPA().refreshSkill(3);
        } else {
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE), c.fightMode);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 3);
            c.getPA().refreshSkill(c.fightMode);
            c.getPA().refreshSkill(3);
        }
        if (damage > 0) {
            if (Server.npcHandler.npcs[i].npcType >= 6142 && Server.npcHandler.npcs[i].npcType <= 6145) {
                c.pcDamage += damage;
            }
        }
        if (damage > 0 && guthansEffect) {
            c.playerLevel[3] += damage;
            if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3])) c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
            c.getPA().refreshSkill(3);
            Server.npcHandler.npcs[i].gfx0(398);
        }
        Server.npcHandler.npcs[i].underAttack = true;
        //Server.npcHandler.npcs[i].killerId = c.playerId;
        c.killingNpcIndex = c.npcIndex;
        c.lastNpcAttacked = i;
        switch (c.specEffect) {
            case 4:
                if (damage > 0) {
                    if (c.playerLevel[3] + damage > c.getLevelForXP(c.playerXP[3])) if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]));
                    else c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
                    else c.playerLevel[3] += damage;
                    c.getPA().refreshSkill(3);
                }
                break;
            case 5:
                c.clawDelay = 2;
                break;

        }
        switch (damageMask) {
            case 1:
                Server.npcHandler.npcs[i].hitDiff = damage;
                Server.npcHandler.npcs[i].HP -= damage;
                c.totalDamageDealt += damage;
                Server.npcHandler.npcs[i].hitUpdateRequired = true;
                Server.npcHandler.npcs[i].updateRequired = true;
                break;

            case 2:
                Server.npcHandler.npcs[i].hitDiff2 = damage;
                Server.npcHandler.npcs[i].HP -= damage;
                c.totalDamageDealt += damage;
                Server.npcHandler.npcs[i].hitUpdateRequired2 = true;
                Server.npcHandler.npcs[i].updateRequired = true;
                c.doubleHit = false;
                break;

        }
    }

    public void fireProjectileNpc() {
        if (c.oldNpcIndex > 0) {
            if (Server.npcHandler.npcs[c.oldNpcIndex] != null) {
                c.projectileStage = 2;
                int pX = c.getX();
                int pY = c.getY();
                int nX = Server.npcHandler.npcs[c.oldNpcIndex].getX();
                int nY = Server.npcHandler.npcs[c.oldNpcIndex].getY();
                int offX = (pY - nY) * -1;
                int offY = (pX - nX) * -1;
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 43, 31, c.oldNpcIndex + 1, getStartDelay());
                if (usingDbow()) c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 60, 31, c.oldNpcIndex + 1, getStartDelay(), 35);
            }
        }
    }



    /**
     * Attack Players, same as npc tbh xD
     **/

public void attackPlayer(int i) {

		/*if (Server.playerHandler.players[i].newPlayer()) {
			resetPlayerAttack();
			c.sendMessage("That is a new player.");
			return;
		}
		if (c.newPlayer()) {
			resetPlayerAttack();
			c.teleportToX = 3090;
			c.teleportToY = 3500;
			c.sendMessage(c.getTimeLeftForNP());
			return;
		}*/

		if (Server.playerHandler.players[i] != null) {

			if (Server.playerHandler.players[i].isDead) {
				resetPlayerAttack();
				return;
			}

			if (c.respawnTimer > 0
					|| Server.playerHandler.players[i].respawnTimer > 0) {
				resetPlayerAttack();
				return;
			}
			
			if (c.usingSpecial && !c.usingMagic) {
				if (checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					activateSpecial(c.playerEquipment[c.playerWeapon], i);
					return;
				} else {
					c.sendMessage("You don't have the required special energy to use this attack.");
					c.usingSpecial = false;
					c.getItems().updateSpecialBar();
					c.npcIndex = 0;
					return;
				}
			}

			/*
			 * if (c.teleTimer > 0 || Server.playerHandler.players[i].teleTimer
			 * > 0) { resetPlayerAttack(); return; }
			 */

			if (!c.getCombat().checkReqs()) {
				return;
			}

			if (c.getPA().getWearingAmount() < 4 && c.duelStatus < 1) {
				c.sendMessage("You must be wearing at least 4 items to attack someone.");
				resetPlayerAttack();
				return;
			}
			boolean sameSpot = c.absX == Server.playerHandler.players[i].getX()
					&& c.absY == Server.playerHandler.players[i].getY();
			if (!c.goodDistance(Server.playerHandler.players[i].getX(),
					Server.playerHandler.players[i].getY(), c.getX(), c.getY(),
					25)
					&& !sameSpot) {
				resetPlayerAttack();
				return;
			}

			if (Server.playerHandler.players[i].respawnTimer > 0) {
				Server.playerHandler.players[i].playerIndex = 0;
				resetPlayerAttack();
				return;
			}

			if (Server.playerHandler.players[i].heightLevel != c.heightLevel) {
				resetPlayerAttack();
				return;
			}
			// c.sendMessage("Made it here0.");
			c.followId = i;
			c.followId2 = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.specEffect = 0;
				c.usingRangeWeapon = false;
				c.rangeItemUsed = 0;
				boolean usingBow = false;
				boolean usingArrows = false;
				boolean usingOtherRangeWeapons = false;
				boolean usingCross = c.playerEquipment[c.playerWeapon] == 9185;
				c.projectileStage = 0;

				if (c.absX == Server.playerHandler.players[i].absX
						&& c.absY == Server.playerHandler.players[i].absY) {
					if (c.freezeTimer > 0) {
						resetPlayerAttack();
						return;
					}
					c.followId = i;
					c.attackTimer = 0;
					return;
				}

				/*
				 * if ((c.inPirateHouse() &&
				 * !Server.playerHandler.players[i].inPirateHouse()) ||
				 * (Server.playerHandler.players[i].inPirateHouse() &&
				 * !c.inPirateHouse())) { resetPlayerAttack(); return; }
				 */
				// c.sendMessage("Made it here1.");
				if (!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if (c.playerEquipment[c.playerWeapon] == bowId) {
							usingBow = true;
							for (int arrowId : c.ARROWS) {
								if (c.playerEquipment[c.playerArrows] == arrowId) {
									usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
							usingOtherRangeWeapons = true;
						}
					}
				}
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				// c.sendMessage("Made it here2.");
				if (c.spellId > 0) {
					c.usingMagic = true;
				}
				c.attackTimer = getAttackDelay(c.getItems()
						.getItemName(c.playerEquipment[c.playerWeapon])
						.toLowerCase());

				if (c.duelRule[9]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Config.FUN_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						resetPlayerAttack();
						return;
					}
				}
				// c.sendMessage("Made it here3.");
				if (c.duelRule[2] && (usingBow || usingOtherRangeWeapons)) {
					c.sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (c.duelRule[3]
						&& (!usingBow && !usingOtherRangeWeapons && !c.usingMagic)) {
					c.sendMessage("Melee has been disabled in this duel!");
					return;
				}

				if (c.duelRule[4] && c.usingMagic) {
					c.sendMessage("Magic has been disabled in this duel!");
					resetPlayerAttack();
					return;
				}

				if ((!c.goodDistance(c.getX(), c.getY(),
						Server.playerHandler.players[i].getX(),
						Server.playerHandler.players[i].getY(), 4) && (usingOtherRangeWeapons
						&& !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								Server.playerHandler.players[i].getX(),
								Server.playerHandler.players[i].getY(), 2) && (!usingOtherRangeWeapons
								&& usingHally() && !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								Server.playerHandler.players[i].getX(),
								Server.playerHandler.players[i].getY(),
								getRequiredDistance()) && (!usingOtherRangeWeapons
								&& !usingHally() && !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								Server.playerHandler.players[i].getX(),
								Server.playerHandler.players[i].getY(), 10) && (usingBow || c.usingMagic))) {
					// c.sendMessage("Setting attack timer to 1");
					c.attackTimer = 1;
					if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons
							&& c.freezeTimer > 0)
						resetPlayerAttack();
					return;
				}

				if (!usingCross
						&& !usingArrows
						&& usingBow
						&& (c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223)
						&& !c.usingMagic) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (correctBowAndArrows() < c.playerEquipment[c.playerArrows]
						&& Config.CORRECT_ARROWS && usingBow
						&& !usingCrystalBow()
						&& c.playerEquipment[c.playerWeapon] != 9185
						&& !c.usingMagic) {
					c.sendMessage("You can't use "
							+ c.getItems()
									.getItemName(
											c.playerEquipment[c.playerArrows])
									.toLowerCase()
							+ "s with a "
							+ c.getItems()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase() + ".");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (c.playerEquipment[c.playerWeapon] == 9185 && !properBolts()
						&& !c.usingMagic) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (usingBow || c.usingMagic || usingOtherRangeWeapons
						|| usingHally()) {
					c.stopMovement();
				}

				if (!checkMagicReqs(c.spellId)) {
					c.stopMovement();
					resetPlayerAttack();
					return;
				}

				c.faceUpdate(i + 32768);

				if (c.duelStatus != 5) {
					if (!c.attackedPlayers.contains(c.playerIndex)
							&& !Server.playerHandler.players[c.playerIndex].attackedPlayers
									.contains(c.playerId)) {
						c.attackedPlayers.add(c.playerIndex);
						c.isSkulled = true;
						c.skullTimer = Config.SKULL_TIMER;
						c.headIconPk = 0;
						c.getPA().requestUpdates();
					}
				}
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				c.delayedDamage = c.delayedDamage2 = 0;
				if (c.usingSpecial && !c.usingMagic) {
					if (c.duelRule[10] && c.duelStatus == 5) {
						c.sendMessage("Special attacks have been disabled during this duel!");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						resetPlayerAttack();
						return;
					}
					if (checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						activateSpecial(c.playerEquipment[c.playerWeapon], i);
						c.followId = c.playerIndex;
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.playerIndex = 0;
						return;
					}
				}

				if (!c.usingMagic) {
					c.startAnimation(getWepAnim(c.getItems()
							.getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase()));
					c.mageFollow = false;
				} else {
					c.startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
					c.mageFollow = true;
					c.followId = c.playerIndex;
				}
				Server.playerHandler.players[i].underAttackBy = c.playerId;
				Server.playerHandler.players[i].logoutDelay = System
						.currentTimeMillis();
				Server.playerHandler.players[i].singleCombatDelay = System
						.currentTimeMillis();
				Server.playerHandler.players[i].killerId = c.playerId;
				c.lastArrowUsed = 0;
				c.rangeItemUsed = 0;
				if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons) { // melee
																				// hit
																				// delay
					c.followId = Server.playerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.hitDelay = getHitDelay(c.getItems()
							.getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
					c.delayedDamage = Misc.random(calculateMeleeMaxHit());
					c.projectileStage = 0;
					c.oldPlayerIndex = i;
				}

				if (usingBow && !usingOtherRangeWeapons && !c.usingMagic
						|| usingCross) { // range hit delay
					if (c.playerEquipment[c.playerWeapon] >= 4212
							&& c.playerEquipment[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.crystalBowArrowCount++;
					} else {
						c.rangeItemUsed = c.playerEquipment[c.playerArrows];
						c.getItems().deleteArrow();
					}
					if (c.fightMode == 2)
						c.attackTimer--;
					if (usingCross)
						c.usingBow = true;
					c.usingBow = true;
					c.followId = Server.playerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					c.gfx100(getRangeStartGFX());
					c.hitDelay = getHitDelay(c.getItems()
							.getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (usingOtherRangeWeapons) { // knives, darts, etc hit delay
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getItems().deleteEquipment();
					c.usingRangeWeapon = true;
					c.followId = Server.playerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.gfx100(getRangeStartGFX());
					if (c.fightMode == 2)
						c.attackTimer--;
					c.hitDelay = getHitDelay(c.getItems()
							.getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = Server.playerHandler.players[i].getX();
					int nY = Server.playerHandler.players[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					if (c.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (c.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY,
								50, 78, c.MAGIC_SPELLS[c.spellId][4],
								getStartHeight(), getEndHeight(), -i - 1,
								getStartDelay());
					}
					if (c.autocastId > 0) {
						c.followId = c.playerIndex;
						c.followDistance = 5;
					}
					c.hitDelay = getHitDelay(c.getItems()
							.getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
					c.oldPlayerIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					Client o = (Client) Server.playerHandler.players[i];
					if (c.MAGIC_SPELLS[c.oldSpellId][0] == 12891 && o.isMoving) {
						// c.sendMessage("Barrage projectile..");
						c.getPA().createPlayersProjectile(pX, pY, offX, offY,
								50, 85, 368, 25, 25, -i - 1, getStartDelay());
					}
					if (Misc.random(o.getCombat().mageDef()) > Misc
							.random(mageAtk())) {
						c.magicFailed = true;
					} else {
						c.magicFailed = false;
					}
					int freezeDelay = getFreezeTime();// freeze time
					if (freezeDelay > 0
							&& Server.playerHandler.players[i].freezeTimer <= -3
							&& !c.magicFailed) {
						Server.playerHandler.players[i].freezeTimer = freezeDelay;
						o.resetWalkingQueue();
						o.sendMessage("You have been frozen.");
						o.frozenBy = c.playerId;
					}
					if (!c.autocasting && c.spellId <= 0)
						c.playerIndex = 0;
				}

				if (usingBow && Config.CRYSTAL_BOW_DEGRADES) { // crystal bow
																// degrading
					if (c.playerEquipment[c.playerWeapon] == 4212) { // new
																		// crystal
																		// bow
																		// becomes
																		// full
																		// bow
																		// on
																		// the
																		// first
																		// shot
						c.getItems().wearItem(4214, 1, 3);
					}

					if (c.crystalBowArrowCount >= 250) {
						switch (c.playerEquipment[c.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getItems().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(c, 4207,
										c.getX(), c.getY(), 1, c.getId());
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(
									++c.playerEquipment[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;
						}
					}
				}
			}
		}
	}

    public boolean usingCrystalBow() {
        return c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223;
    }

    public void appendVengeance(int otherPlayer, int damage) {
        if (damage <= 0) return;
        Player o = Server.playerHandler.players[otherPlayer];
        o.forcedText = "Taste vengeance!";
        o.forcedChatUpdateRequired = true;
        o.updateRequired = true;
        o.vengOn = false;
        if ((o.playerLevel[3] - damage) > 0) {
            damage = (int)(damage * 0.75);
            if (damage > c.playerLevel[3]) {
                damage = c.playerLevel[3];
            }
            c.setHitDiff2(damage);
            c.setHitUpdateRequired2(true);
            c.playerLevel[3] -= damage;
            c.getPA().refreshSkill(3);
        }
        c.updateRequired = true;
    }

    public void playerDelayedHit(int i) {
        if (Server.playerHandler.players[i] != null) {
            if (Server.playerHandler.players[i].isDead || c.isDead || Server.playerHandler.players[i].playerLevel[3] <= 0 || c.playerLevel[3] <= 0) {
                c.playerIndex = 0;
                return;
            }
            if (Server.playerHandler.players[i].respawnTimer > 0) {
                c.faceUpdate(0);
                c.playerIndex = 0;
                return;
            }
            Client o = (Client) Server.playerHandler.players[i];
            o.getPA().removeAllWindows();
            o.inCombat = true;
            if (o.playerIndex <= 0 && o.npcIndex <= 0) {
                if (o.autoRet == 1) {
                    o.playerIndex = c.playerId;
                }
            }
            if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0 && !c.castingMagic) { // block animation
                o.startAnimation(o.getCombat().getBlockEmote());
            }
            if (o.inTrade) {
                o.getTradeAndDuel().declineTrade();
            }


            //if(c.goodDistance(c.getX(), c.getY(), Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY().toLowerCase())) {
            if (c.projectileStage == 0 && !c.usingMagic && !c.castingMagic) { // melee hit damage
                if (!c.usingClaws) applyPlayerMeleeDamage(i, 1, Misc.random(calculateMeleeMaxHit()));
                if (c.doubleHit && !c.usingClaws) {
                    applyPlayerMeleeDamage(i, 2, Misc.random(calculateMeleeMaxHit()));
                }
                if (c.doubleHit && c.usingClaws) {
                    //System.out.println(c.clawDamage + " " + c.clawDamage/2 + " " + c.clawDamage/4 + " " + c.clawDamage/4 + 1);
                    c.delayedDamage = c.clawDamage;
                    c.delayedDamage2 = c.clawDamage / 2;
                    applyPlayerMeleeDamage(i, 1, c.clawDamage);
                    applyPlayerMeleeDamage(i, 2, c.clawDamage / 2);
                }
            }



            /*if(c.projectileStage == 0 && !c.usingMagic && !c.castingMagic) { // melee hit damage								
					applyPlayerMeleeDamage(i, 1, Misc.random(calculateMeleeMaxHit()));
					if(c.doubleHit && !c.usingClaws) {
						applyPlayerMeleeDamage(i, 2, Misc.random(calculateMeleeMaxHit()));
					}	
					if(c.doubleHit && c.usingClaws) {
						applyPlayerMeleeDamage(i, 2, c.previousDamage / 2);
					}
			}*/

            if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
                int damage = Misc.random(rangeMaxHit());
                int damage2 = -1;
                int PrayerDrain = damage / 4;
                if (c.lastWeaponUsed == 15701 || c.lastWeaponUsed == 15702 || c.lastWeaponUsed == 11235 || c.lastWeaponUsed == 15703 || c.lastWeaponUsed == 15704 || c.bowSpecShot == 1) damage2 = Misc.random(rangeMaxHit());
                boolean ignoreDef = false;
                if (Misc.random(4) == 1 && c.lastArrowUsed == 9243 && c.playerEquipment[c.playerWeapon] == 9185) {
                    ignoreDef = true;
                    o.gfx0(753);
                }
                if (Misc.random(4) == 1 && c.lastArrowUsed == 9243 && c.playerEquipment[c.playerWeapon] == 9185) {
                    ignoreDef = true;
                    o.gfx0(753);
                }
                if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc.random(10 + calculateRangeAttack()) && !ignoreDef) {
                    damage = 0;
                }

                if (c.lastWeaponUsed == 11235 || c.lastWeaponUsed == 15701 || c.lastWeaponUsed == 15702 || c.lastWeaponUsed == 15703 || c.lastWeaponUsed == 15704 || c.bowSpecShot == 1) {
                    if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc.random(10 + calculateRangeAttack())) damage2 = 0;
                }

                if (c.dbowSpec) {
                    o.gfx100(1100);
                    if (damage < 8) damage = 8;
                    if (damage2 < 8) damage2 = 8;
                    c.dbowSpec = false;
                }
                if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244 && c.playerEquipment[c.playerWeapon] == 9185) {
                    damage *= 1.45;
                    o.gfx0(756);
                }
                if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244 && c.playerEquipment[c.playerWeapon] == 18357) {
                    damage *= 1.55;
                    o.gfx0(756);
                }
                if (damage > 0 && Misc.random(10) == 1 && c.lastArrowUsed == 9245 && c.playerEquipment[c.playerWeapon] == 18357) {
                    damage *= 1.95;
                    o.gfx0(753);
                }
                if (damage > 0 && Misc.random(10) == 1 && c.lastArrowUsed == 9245 && c.playerEquipment[c.playerWeapon] == 9185) {
                    damage *= 1.65;
                    o.gfx0(753);
                }
                if (o.playerEquipment[o.playerWeapon] == 15001 && damage >= 1 && o.SolProtect >= 1) {
                    damage = (int) damage / 2;
                    damage2 = (int) damage2 / 2;
                }
                if (o.curseActive[8]) {
                    o.gfx0(2229);
                    o.startAnimation(12573);
                }
                if (o.prayerActive[17] && System.currentTimeMillis() - o.protRangeDelay > 1500) { // if prayer active reduce damage by half 
                    damage = (int) damage * 60 / 100;
                    if (c.lastWeaponUsed == 11235 || c.lastWeaponUsed == 15701 || c.lastWeaponUsed == 15702 || c.lastWeaponUsed == 15703 || c.lastWeaponUsed == 15704 || c.bowSpecShot == 1) damage2 = (int) damage2 * 60 / 100;
                }
                if (Server.playerHandler.players[i].playerLevel[3] - damage < 0) {
                    damage = Server.playerHandler.players[i].playerLevel[3];
                }
                if (Server.playerHandler.players[i].playerLevel[3] - damage - damage2 < 0) {
                    damage2 = Server.playerHandler.players[i].playerLevel[3] - damage;
                }
                if (damage < 0) damage = 0;
                if (damage2 < 0 && damage2 != -1) damage2 = 0;
                if (o.vengOn) {
                    appendVengeance(i, damage);
                    appendVengeance(i, damage2);
                }
                if (damage > 0) applyRecoil(damage, i);
                if (damage2 > 0) applyRecoil(damage2, i);
                if (c.fightMode == 3) {
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 4);
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 1);
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 3);
                    c.getPA().refreshSkill(1);
                    c.getPA().refreshSkill(3);
                    c.getPA().refreshSkill(4);
                } else {
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE), 4);
                    c.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3), 3);
                    c.getPA().refreshSkill(3);
                    c.getPA().refreshSkill(4);
                }
                boolean dropArrows = true;

                for (int noArrowId: c.NO_ARROW_DROP) {
                    if (c.lastWeaponUsed == noArrowId) {
                        dropArrows = false;
                        break;
                    }
                }
                if (dropArrows) {
                    c.getItems().dropArrowPlayer();
                }
                Server.playerHandler.players[i].underAttackBy = c.playerId;
                Server.playerHandler.players[i].logoutDelay = System.currentTimeMillis();
                Server.playerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
                Server.playerHandler.players[i].killerId = c.playerId;
                //Server.playerHandler.players[i].setHitDiff(damage);
                //Server.playerHandler.players[i].playerLevel[3] -= damage;
                Server.playerHandler.players[i].dealDamage(damage);
                Server.playerHandler.players[i].damageTaken[c.playerId] += damage;
                c.killedBy = Server.playerHandler.players[i].playerId;
                Server.playerHandler.players[i].handleHitMask(damage);
                if (damage2 != -1) {
                    //Server.playerHandler.players[i].playerLevel[3] -= damage2;
                    Server.playerHandler.players[i].dealDamage(damage2);
                    Server.playerHandler.players[i].damageTaken[c.playerId] += damage2;
                    Server.playerHandler.players[i].handleHitMask(damage2);

                }
                o.getPA().refreshSkill(3);

                //Server.playerHandler.players[i].setHitUpdateRequired(true);	
                Server.playerHandler.players[i].updateRequired = true;
                applySmite(i, damage);
                if (c.soulSplitDelay <= 1) {
                    applySoulSplit(i, damage); //makes it like smite
                }
                if (damage2 != -1) applySmite(i, damage2);

            } else if (c.projectileStage > 0) { // magic hit damage
                int damage = Misc.random(finalMagicDamage(c) + c.totalMagicBonus());
                if (godSpells()) {
                    if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
                        damage += 10;
                    }
                }
                //c.playerIndex = 0;
                if (c.magicFailed) damage = 0;
                if (o.curseActive[7]) {
                    o.gfx0(2228);
                    o.startAnimation(12573);
                }
                if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) { // if prayer active reduce damage by half 
                    damage = (int) damage * 60 / 100;
                }
                if (o.playerEquipment[o.playerWeapon] == 15001 && damage >= 1 && o.SolProtect >= 1) {
                    damage = (int) damage / 2;
                }
                if (Server.playerHandler.players[i].playerLevel[3] - damage < 0) {
                    damage = Server.playerHandler.players[i].playerLevel[3];
                }
                if (o.vengOn) appendVengeance(i, damage);
                if (damage > 0) applyRecoil(damage, i);
                c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE), 6);
                c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE / 3), 3);
                c.getPA().refreshSkill(3);
                c.getPA().refreshSkill(6);

                if (getEndGfxHeight() == 100 && !c.magicFailed) { // end GFX
                    Server.playerHandler.players[i].gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
                } else if (!c.magicFailed) {
                    Server.playerHandler.players[i].gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
                } else if (c.magicFailed) {
                    Server.playerHandler.players[i].gfx100(85);
                }

                if (!c.magicFailed) {
                    if (System.currentTimeMillis() - Server.playerHandler.players[i].reduceStat > 35000) {
                        Server.playerHandler.players[i].reduceStat = System.currentTimeMillis();
                        switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
                            case 12987:
                            case 13011:
                            case 12999:
                            case 13023:
                                Server.playerHandler.players[i].playerLevel[0] -= ((o.getPA().getLevelForXP(Server.playerHandler.players[i].playerXP[0]) * 10) / 100);
                                break;
                        }
                    }

                    switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
                        case 12445:
                            //teleblock
                            if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {
                                o.teleBlockDelay = System.currentTimeMillis();
                                o.sendMessage("You have been teleblocked.");
                                if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) o.teleBlockLength = 150000;
                                else o.teleBlockLength = 300000;
                            }
                            break;

                        case 12901:
                        case 12919:
                            // blood spells
                        case 12911:
                        case 12929:
                            int heal = (int)(damage / 4);
                            if (c.playerLevel[3] + heal > c.getPA().getLevelForXP(c.playerXP[3])) {
                                c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                            } else {
                                c.playerLevel[3] += heal;
                            }
                            c.getPA().refreshSkill(3);
                            break;

                        case 1153:
                            Server.playerHandler.players[i].playerLevel[0] -= ((o.getPA().getLevelForXP(Server.playerHandler.players[i].playerXP[0]) * 5) / 100);
                            o.sendMessage("Your attack level has been reduced!");
                            Server.playerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
                            o.getPA().refreshSkill(0);
                            break;

                        case 1157:
                            Server.playerHandler.players[i].playerLevel[2] -= ((o.getPA().getLevelForXP(Server.playerHandler.players[i].playerXP[2]) * 5) / 100);
                            o.sendMessage("Your strength level has been reduced!");
                            Server.playerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
                            o.getPA().refreshSkill(2);
                            break;

                        case 1161:
                            Server.playerHandler.players[i].playerLevel[1] -= ((o.getPA().getLevelForXP(Server.playerHandler.players[i].playerXP[1]) * 5) / 100);
                            o.sendMessage("Your defence level has been reduced!");
                            Server.playerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
                            o.getPA().refreshSkill(1);
                            break;

                        case 1542:
                            Server.playerHandler.players[i].playerLevel[1] -= ((o.getPA().getLevelForXP(Server.playerHandler.players[i].playerXP[1]) * 10) / 100);
                            o.sendMessage("Your defence level has been reduced!");
                            Server.playerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
                            o.getPA().refreshSkill(1);
                            break;

                        case 1543:
                            Server.playerHandler.players[i].playerLevel[2] -= ((o.getPA().getLevelForXP(Server.playerHandler.players[i].playerXP[2]) * 10) / 100);
                            o.sendMessage("Your strength level has been reduced!");
                            Server.playerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
                            o.getPA().refreshSkill(2);
                            break;

                        case 1562:
                            Server.playerHandler.players[i].playerLevel[0] -= ((o.getPA().getLevelForXP(Server.playerHandler.players[i].playerXP[0]) * 10) / 100);
                            o.sendMessage("Your attack level has been reduced!");
                            Server.playerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
                            o.getPA().refreshSkill(0);
                            break;
                    }
                }

                Server.playerHandler.players[i].logoutDelay = System.currentTimeMillis();
                Server.playerHandler.players[i].underAttackBy = c.playerId;
                Server.playerHandler.players[i].killerId = c.playerId;
                Server.playerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
                if (finalMagicDamage(c) != 0) {
                    //Server.playerHandler.players[i].playerLevel[3] -= damage;
                    Server.playerHandler.players[i].dealDamage(damage);
                    Server.playerHandler.players[i].damageTaken[c.playerId] += damage;
                    c.totalPlayerDamageDealt += damage;
                    if (!c.magicFailed) {
                        //Server.playerHandler.players[i].setHitDiff(damage);
                        //Server.playerHandler.players[i].setHitUpdateRequired(true);
                        Server.playerHandler.players[i].handleHitMask(damage);
                    }
                }
                applySmite(i, damage);
                if (c.soulSplitDelay <= 1) {
                    applySoulSplit(i, damage); //Thank zant
                }
                c.killedBy = Server.playerHandler.players[i].playerId;
                o.getPA().refreshSkill(3);
                Server.playerHandler.players[i].updateRequired = true;
                c.usingMagic = false;
                c.castingMagic = false;
                if (o.inMulti() && multis()) {
                    c.barrageCount = 0;
                    for (int j = 0; j < Server.playerHandler.players.length; j++) {
                        if (Server.playerHandler.players[j] != null) {
                            if (j == o.playerId) continue;
                            if (c.barrageCount >= 9) break;
                            if (o.goodDistance(o.getX(), o.getY(), Server.playerHandler.players[j].getX(), Server.playerHandler.players[j].getY(), 1)) appendMultiBarrage(j, c.magicFailed);
                        }
                    }
                }
                c.getPA().refreshSkill(3);
                c.getPA().refreshSkill(6);
                c.oldSpellId = 0;
            }
        }
        c.getPA().requestUpdates();
        int oldindex = c.oldPlayerIndex;
        if (c.bowSpecShot <= 0) {
            c.oldPlayerIndex = 0;
            c.projectileStage = 0;
            c.lastWeaponUsed = 0;
            c.doubleHit = false;
            c.bowSpecShot = 0;
        }
        if (c.bowSpecShot != 0) {
            c.bowSpecShot = 0;
        }
    }
    public void applyPlayerClawDamage(int i, int damageMask, int damage) {
        int PrayerDrain = damage / 4;
        Client o = (Client) Server.playerHandler.players[i];
        if (o == null) {
            return;
        }

        c.previousDamage = damage;
        boolean veracsEffect = false;
        boolean guthansEffect = false;
        if (c.getPA().fullVeracs()) {
            if (Misc.random(4) == 1) {
                veracsEffect = true;
            }
        }
        if (o.playerEquipment[o.playerWeapon] == 15001 && damage >= 1 && o.SolProtect >= 1) {
            damage = (int) damage / 2;
        }

        if (c.getPA().fullGuthans()) {
            if (Misc.random(4) == 1) {
                guthansEffect = true;
            }
        }
        if (damageMask == 1) {
            damage = c.delayedDamage;
            c.delayedDamage = 0;
        } else {
            damage = c.delayedDamage2;
            c.delayedDamage2 = 0;
        }
        if (Misc.random(o.getCombat().calculateMeleeDefence()) > Misc.random(calculateMeleeAttack()) && !veracsEffect) {
            damage = 0;
            c.bonusAttack = 0;
        } else if (c.playerEquipment[c.playerWeapon] == 5698 && o.poisonDamage <= 0 && Misc.random(3) == 1) {
            o.getPA().appendPoison(13);
            c.bonusAttack += damage / 3;
        } else {
            c.bonusAttack += damage / 3;
        }





        if (c.maxNextHit) {
            damage = calculateMeleeMaxHit();
        }
        if (damage > 0 && guthansEffect) {
            c.playerLevel[3] += damage;
            if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3])) c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
            c.getPA().refreshSkill(3);
            o.gfx0(398);
        }
        if (c.ssSpec && damageMask == 2) {
            damage = 5 + Misc.random(11);
            c.ssSpec = false;
        }
        if (Server.playerHandler.players[i].playerLevel[3] - damage < 0) {
            damage = Server.playerHandler.players[i].playerLevel[3];
        }
        if (o.vengOn && damage > 0) appendVengeance(i, damage);
        if (damage > 0) applyRecoil(damage, i);

        switch (c.specEffect) {
            case 1:
                // dragon scimmy special
                if (damage > 0) {
                    if (o.prayerActive[16] || o.prayerActive[17] || o.prayerActive[18]) {
                        o.headIcon = -1;
                        o.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
                        o.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
                        o.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);

                    }
                    o.sendMessage("You have been injured!");
                    o.stopPrayerDelay = System.currentTimeMillis();
                    o.prayerActive[16] = false;
                    o.prayerActive[17] = false;
                    o.prayerActive[18] = false;

                    o.getPA().requestUpdates();
                }
                break;
            case 2:
                if (damage > 0) {
                    if (o.freezeTimer <= 0) o.freezeTimer = 30;
                    o.gfx0(369);
                    o.sendMessage("You have been frozen.");
                    o.frozenBy = c.playerId;
                    o.stopMovement();
                    c.sendMessage("You freeze your enemy.");
                }
                break;
            case 3:
                if (damage > 0) {
                    o.playerLevel[1] -= damage;
                    o.sendMessage("You feel weak.");
                    if (o.playerLevel[1] < 1) o.playerLevel[1] = 1;
                    o.getPA().refreshSkill(1);
                }
                break;
            case 4:
                if (damage > 0) {
                    if (c.playerLevel[3] + damage > c.getLevelForXP(c.playerXP[3])) if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]));
                    else c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
                    else c.playerLevel[3] += damage;
                    c.getPA().refreshSkill(3);
                }
                break;
            case 5:
                c.clawDelay = 2;
                break;
            case 6:
                o.morriganJavspec = 4;
                break;


        }
        c.specEffect = 0;
        if (c.fightMode == 3) {
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 0);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 1);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 2);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 3);
            c.getPA().refreshSkill(0);
            c.getPA().refreshSkill(1);
            c.getPA().refreshSkill(2);
            c.getPA().refreshSkill(3);
        } else {
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE), c.fightMode);
            c.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3), 3);
            c.getPA().refreshSkill(c.fightMode);
            c.getPA().refreshSkill(3);
        }
        Server.playerHandler.players[i].logoutDelay = System.currentTimeMillis();
        Server.playerHandler.players[i].underAttackBy = c.playerId;
        Server.playerHandler.players[i].killerId = c.playerId;
        Server.playerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
        if (c.killedBy != Server.playerHandler.players[i].playerId) c.totalPlayerDamageDealt = 0;
        c.killedBy = Server.playerHandler.players[i].playerId;
        applySmite(i, damage);
        switch (damageMask) {
            case 1:
                /*if (!Server.playerHandler.players[i].getHitUpdateRequired()){
				Server.playerHandler.players[i].setHitDiff(damage);
				Server.playerHandler.players[i].setHitUpdateRequired(true);
			} else {
				Server.playerHandler.players[i].setHitDiff2(damage);
				Server.playerHandler.players[i].setHitUpdateRequired2(true);			
			}*/
                //Server.playerHandler.players[i].playerLevel[3] -= damage;
                Server.playerHandler.players[i].dealDamage(damage);
                Server.playerHandler.players[i].damageTaken[c.playerId] += damage;
                c.totalPlayerDamageDealt += damage;
                Server.playerHandler.players[i].updateRequired = true;
                o.getPA().refreshSkill(3);
                break;

            case 2:
                /*if (!Server.playerHandler.players[i].getHitUpdateRequired2()){
				Server.playerHandler.players[i].setHitDiff2(damage);
				Server.playerHandler.players[i].setHitUpdateRequired2(true);
			} else {
				Server.playerHandler.players[i].setHitDiff(damage);
				Server.playerHandler.players[i].setHitUpdateRequired(true);			
			}*/
                //Server.playerHandler.players[i].playerLevel[3] -= damage;
                Server.playerHandler.players[i].dealDamage(damage);
                Server.playerHandler.players[i].damageTaken[c.playerId] += damage;
                c.totalPlayerDamageDealt += damage;
                Server.playerHandler.players[i].updateRequired = true;
                c.doubleHit = false;
                o.getPA().refreshSkill(3);
                break;
        }
        Server.playerHandler.players[i].handleHitMask(damage);
    }

    public boolean multis() {
        switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
            case 12891:
            case 12881:
            case 13011:
            case 13023:
            case 12919:
                // blood spells
            case 12929:
            case 12963:
            case 12975:
                return true;
        }
        return false;

    }
    public void multiSpellOnNPC(int originalNpc) {

        if (!multis() || !c.inMulti()) return;
        NPC o2 = (NPC) NPCHandler.npcs[originalNpc];
        int origX = o2.absX;
        int origY = o2.absY;
        for (int indx = 0; indx < NPCHandler.maxNPCs; indx++) {
            if (NPCHandler.npcs[indx] != null) {
                NPC o = (NPC) NPCHandler.npcs[indx];
                if (!(o.absX >= origX - 2 && o.absX <= origX + 2 && o.absY >= origY - 2 && o.absY <= origY + 2) || !o.inMulti()) continue;
                if (!checkNpcReq(indx) || indx == originalNpc) continue;
                int bonusAttack = getBonusAttack(indx);
                if (Misc.random(NPCHandler.npcs[indx].defence) > 10 + Misc.random(mageAtk()) + bonusAttack) {
                    if (getEndGfxHeight() == 100) { // end GFX
                        o.gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
                    } else {
                        o.gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
                    }
                    int damage = Misc.random(c.MAGIC_SPELLS[c.oldSpellId][6]);
                    c.getPA().addSkillXP(
                    (c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE), 6);
                    c.getPA().addSkillXP(
                    (c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE / 3), 3);
                    spellEffects(damage, indx);
                    if (damage > -1) {
                        NPCHandler.npcs[indx].hitDiff = damage;
                        NPCHandler.npcs[indx].HP -= damage;
                        NPCHandler.npcs[indx].hitUpdateRequired = true;
                        c.totalDamageDealt += damage;
                    }
                } else {
                    int damage = 0;
                    o.gfx100(85);
                    NPCHandler.npcs[indx].hitDiff = damage;
                    NPCHandler.npcs[indx].hitUpdateRequired = true;
                    c.totalDamageDealt += damage;
                }
            }
        }
    }



    public void spellEffects(int damage, int indx) {
        switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
            case 12901:
            case 12919:
            case 12911:
            case 12929:
                int heal = Misc.random(damage / 2);
                if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(
                c.playerXP[3])) {
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                } else {
                    c.playerLevel[3] += heal;
                }
                c.getPA().refreshSkill(3);
                break;
        }
        int freezeDelay = getFreezeTime();
        if (c.playerIndex > 0) {
            if (freezeDelay > 0 && PlayerHandler.players[indx].freezeTimer == 0) PlayerHandler.players[indx].freezeTimer = freezeDelay;
        } else if (c.npcIndex > 0) {
            if (freezeDelay > 0 && NPCHandler.npcs[indx].freezeTimer == 0) NPCHandler.npcs[indx].freezeTimer = freezeDelay;
        }
    }



    public boolean checkNpcReq(int i) {
        if (NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].MaxHP <= 0) {
            c.usingMagic = false;
            c.faceUpdate(0);
            c.npcIndex = 0;
            return false;
        }
        if (c.respawnTimer > 0) {
            c.npcIndex = 0;
            return false;
        }
        if (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.npcs[i].underAttackBy != c.playerId && !NPCHandler.npcs[i].inMulti() && c.inGWD() == false) {
            c.npcIndex = 0;
            c.sendMessage("This monster is already in combat.");
            return false;
        }
        if ((c.underAttackBy > 0 || c.underAttackBy2 > 0) && c.underAttackBy2 != i && !c.inMulti() && c.inGWD() == false) {
            resetPlayerAttack();
            c.sendMessage("I am already under attack.");
            return false;
        }
        if (!goodSlayer(i)) {
            resetPlayerAttack();
            return false;
        }

        if (NPCHandler.npcs[i].npcType == 6258) {
            if (92 > c.getLevelForXP(c.playerXP[c.playerPrayer])) {
                c.sendMessage("You need a prayer level of 92 to harm this NPC.");
                resetPlayerAttack();
                return false;
            }
        }
        if (NPCHandler.npcs[i].spawnedBy != c.playerId && NPCHandler.npcs[i].spawnedBy > 0) {
            resetPlayerAttack();
            c.sendMessage("This monster was not spawned for you.");
            return false;
        }
        return true;
    }

    public boolean checkPlayerReq(int i) {
        if (PlayerHandler.players[i].isDead) {
            resetPlayerAttack();
            return false;
        }

        if (c.respawnTimer > 0 || PlayerHandler.players[i].respawnTimer > 0) {
            resetPlayerAttack();
            return false;
        }

        /*if (c.teleTimer > 0 || Server.playerHandler.players[i].teleTimer > 0) {
			resetPlayerAttack();
			return;
		}*/

        if (!c.getCombat().checkReqs()) {
            return false;
        }

        if (c.playerRights == 1 && c.playerRights == 2) {
            c.sendMessage("Staff are not allowed to pk");
            c.sendMessage("Your account has been blacklisted for an owner to see");
            return false;
        }


        boolean sameSpot = c.absX == PlayerHandler.players[i].getX() && c.absY == PlayerHandler.players[i].getY();
        if (!c.goodDistance(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), c.getX(), c.getY(), 25) && !sameSpot) {
            resetPlayerAttack();
            return false;
        }
        if (PlayerHandler.players[i].respawnTimer > 0) {
            PlayerHandler.players[i].playerIndex = 0;
            resetPlayerAttack();
            return false;
        }

        if (PlayerHandler.players[i].heightLevel != c.heightLevel) {
            resetPlayerAttack();
            return false;
        }
        return true;
    }
    public void appendMultiBarrage(int playerId, boolean splashed) {
        if (Server.playerHandler.players[playerId] != null) {
            Client c2 = (Client) Server.playerHandler.players[playerId];
            if (c2.isDead || c2.respawnTimer > 0) return;
            if (checkMultiBarrageReqs(playerId)) {
                c.barrageCount++;
                if (Misc.random(mageAtk()) > Misc.random(mageDef()) && !c.magicFailed) {
                    if (getEndGfxHeight() == 100) { // end GFX
                        c2.gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
                    } else {
                        c2.gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
                    }
                    int damage = Misc.random(finalMagicDamage(c));
                    if (c2.prayerActive[12]) {
                        damage *= (int)(.60);
                    }
                    if (c2.playerLevel[3] - damage < 0) {
                        damage = c2.playerLevel[3];
                    }
                    c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE), 6);
                    c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Config.MAGIC_EXP_RATE / 3), 3);
                    //Server.playerHandler.players[playerId].setHitDiff(damage);
                    //Server.playerHandler.players[playerId].setHitUpdateRequired(true);
                    Server.playerHandler.players[playerId].handleHitMask(damage);
                    //Server.playerHandler.players[playerId].playerLevel[3] -= damage;
                    Server.playerHandler.players[playerId].dealDamage(damage);
                    Server.playerHandler.players[playerId].damageTaken[c.playerId] += damage;
                    c2.getPA().refreshSkill(3);
                    c.totalPlayerDamageDealt += damage;
                    multiSpellEffect(playerId, damage);
                } else {
                    c2.gfx100(85);
                }
            }
        }
    }

    public void multiSpellEffect(int playerId, int damage) {
        switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
            case 13011:
            case 13023:
                if (System.currentTimeMillis() - Server.playerHandler.players[playerId].reduceStat > 35000) {
                    Server.playerHandler.players[playerId].reduceStat = System.currentTimeMillis();
                    Server.playerHandler.players[playerId].playerLevel[0] -= ((Server.playerHandler.players[playerId].getLevelForXP(Server.playerHandler.players[playerId].playerXP[0]) * 10) / 100);
                }
                break;
            case 12919:
                // blood spells
            case 12929:
                int heal = (int)(damage / 4);
                if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(c.playerXP[3])) {
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                } else {
                    c.playerLevel[3] += heal;
                }
                c.getPA().refreshSkill(3);
                break;
            case 12891:
            case 12881:
                if (Server.playerHandler.players[playerId].freezeTimer < -4) {
                    Server.playerHandler.players[playerId].freezeTimer = getFreezeTime();
                    Server.playerHandler.players[playerId].stopMovement();
                }
                break;
        }
    }
    public static void applyPlayerHit(final Client c, final int i) {
        c.stopPlayerSkill = false;
        if (!c.usingClaws) {
            c.getCombat().applyPlayerMeleeDamage(i, 1, Misc.random(c.getCombat().calculateMeleeMaxHit()));
            if (c.doubleHit) {
                if (!c.oldSpec) {
                    c.getCombat().applyPlayerMeleeDamage(i, 2, Misc.random(c.getCombat().calculateMeleeMaxHit()));
                } else {
                    World.getWorld().submit(new Event(1) {@Override
                        public void execute() {
                            c.getCombat().applyPlayerMeleeDamage(i, 2, Misc.random(c.getCombat().calculateMeleeMaxHit()));
                            this.stop();
                        }
                    });
                }
            }
        } else {
            c.getPA().hitDragonClaws(c, Misc.random(c.getCombat().calculateMeleeMaxHit()));
        }
    }
    public void applyPlayerMeleeDamage(int i, int damageMask, int damage) {
        //c.previousDamage = damage;
        Client o = (Client) Server.playerHandler.players[i];
        if (o == null) {
            return;
        }


        int damage1 = 0;
        boolean veracsEffect = false;
        boolean guthansEffect = false;
        if (c.getPA().fullVeracs()) {
            if (Misc.random(4) == 1) {
                veracsEffect = true;
            }
        }
        if (c.getPA().fullGuthans()) {
            if (Misc.random(4) == 1) {
                guthansEffect = true;
            }
        }
        if (damageMask == 1) {
            damage1 = c.delayedDamage;
            c.delayedDamage = 0;
        } else {
            damage1 = c.delayedDamage2;
            c.delayedDamage2 = 0;
        }
        if (Misc.random(o.getCombat().calculateMeleeDefence()) > Misc.random(calculateMeleeAttack()) && !veracsEffect) {
            damage1 = 0;
            c.bonusAttack = 0;
        } else if (c.playerEquipment[c.playerWeapon] == 5698 && o.poisonDamage <= 0 && Misc.random(3) == 1) {
            o.getPA().appendPoison(13);
            c.bonusAttack += damage1 / 3;
        } else {
            c.bonusAttack += damage1 / 3;
        }
        if (o.curseActive[9]) {
            o.gfx0(2230);
            o.startAnimation(12573);
        }
        if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500 && !veracsEffect) { // if prayer active reduce damage by 40%
            damage1 = (int) damage1 * 60 / 100;
        }
        if (o.playerEquipment[o.playerWeapon] == 15001 && damage >= 1 && o.SolProtect >= 1) {
            damage = (int) damage / 2;
        }
        if (c.maxNextHit) {
            damage1 = calculateMeleeMaxHit();
        }
        if (damage1 > 0 && guthansEffect) {
            c.playerLevel[3] += damage1;
            if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3])) c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
            c.getPA().refreshSkill(3);
            o.gfx0(398);
        }
        if (c.ssSpec && damageMask == 2) {
            damage1 = 5 + Misc.random(11);
            c.ssSpec = false;
        }

        Client cl2 = (Client) Server.playerHandler.players[i];
        if (cl2.playerEquipment[5] == 13742) { //Elysian Effect
            if (Misc.random(100) < 75) {
                double damages = damage1;
                double damageDeduction = ((double) damages) / ((double) 4);
                damage1 = damage1 - ((int) Math.round(damageDeduction));
            }
        }
        if (cl2.playerEquipment[5] == 13740) { //Divine Effect
            double damages2 = damage1;
            double prayer = cl2.playerLevel[5];
            double possibleDamageDeduction = ((double) damages2) / ((double) 3.33); //30% of Damage Inflicted
            double actualDamageDeduction;
            if ((prayer * 2) < possibleDamageDeduction) {
                actualDamageDeduction = (prayer * 2); //Partial Effect(Not enough prayer points)
            } else {
                actualDamageDeduction = possibleDamageDeduction; //Full effect
            }
            double prayerDeduction = ((double) actualDamageDeduction) / ((double) 2); //Half of the damage deducted
            damage1 = damage1 - ((int) Math.round(actualDamageDeduction));
            cl2.playerLevel[5] = cl2.playerLevel[5] - ((int) Math.round(prayerDeduction));
            cl2.getPA().refreshSkill(5);
        }
        if (Server.playerHandler.players[i].playerLevel[3] - damage1 < 0) {
            damage1 = Server.playerHandler.players[i].playerLevel[3];
        }
        if (o.vengOn && damage1 > 0) appendVengeance(i, damage1);
        if (damage1 > 0) applyRecoil(damage1, i);
        switch (c.specEffect) {
            case 1:
                // dragon scimmy special
                if (damage1 > 0) {
                    if (o.prayerActive[16] || o.prayerActive[17] || o.prayerActive[18]) {
                        o.headIcon = -1;
                        o.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
                        o.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
                        o.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
                    }
                    o.sendMessage("You have been injured!");
                    o.stopPrayerDelay = System.currentTimeMillis();
                    o.prayerActive[16] = false;
                    o.prayerActive[17] = false;
                    o.prayerActive[18] = false;
                    o.getPA().requestUpdates();
                }
                break;
            case 2:
                if (damage1 > 0) {
                    if (o.freezeTimer <= 0) o.freezeTimer = 30;
                    o.gfx0(2111);
                    o.gfx0(2112);
                    o.sendMessage("You have been frozen.");
                    o.frozenBy = c.playerId;
                    o.stopMovement();
                    c.sendMessage("You freeze your enemy.");
                }
                break;
            case 3:
                if (damage1 > 0) {
                    o.playerLevel[1] -= damage1;
                    o.sendMessage("You feel weak.");
                    if (o.playerLevel[1] < 1) o.playerLevel[1] = 1;
                    o.getPA().refreshSkill(1);
                }
                break;
            case 4:
                if (damage1 > 0) {
                    if (c.playerLevel[3] + damage1 > c.getLevelForXP(c.playerXP[3])) if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]));
                    else c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
                    else c.playerLevel[3] += damage1;
                    c.getPA().refreshSkill(3);
                }
                break;
            case 5:
                c.clawDelay = 2;
                break;
            case 6:
                o.gfx100(2108);
                break;
            case 9:
                o.morriganJavspec = 5;
                break;

        }
        c.specEffect = 0;
        if (c.fightMode == 3) {
            c.getPA().addSkillXP((damage1 * Config.MELEE_EXP_RATE / 3), 0);
            c.getPA().addSkillXP((damage1 * Config.MELEE_EXP_RATE / 3), 1);
            c.getPA().addSkillXP((damage1 * Config.MELEE_EXP_RATE / 3), 2);
            c.getPA().addSkillXP((damage1 * Config.MELEE_EXP_RATE / 3), 3);
            c.getPA().refreshSkill(0);
            c.getPA().refreshSkill(1);
            c.getPA().refreshSkill(2);
            c.getPA().refreshSkill(3);
        } else {
            c.getPA().addSkillXP((damage1 * Config.MELEE_EXP_RATE), c.fightMode);
            c.getPA().addSkillXP((damage1 * Config.MELEE_EXP_RATE / 3), 3);
            c.getPA().refreshSkill(c.fightMode);
            c.getPA().refreshSkill(3);
        }
        Server.playerHandler.players[i].logoutDelay = System.currentTimeMillis();
        Server.playerHandler.players[i].underAttackBy = c.playerId;
        Server.playerHandler.players[i].killerId = c.playerId;
        Server.playerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
        if (c.killedBy != Server.playerHandler.players[i].playerId) c.totalPlayerDamageDealt = 0;
        c.killedBy = Server.playerHandler.players[i].playerId;
        applySmite(i, damage1);
        if (c.soulSplitDelay <= 1) {
            applySoulSplit(i, damage1); //Maded by zant
        }
        switch (damageMask) {
            case 1:
                /*if (!Server.playerHandler.players[i].getHitUpdateRequired()){
				Server.playerHandler.players[i].setHitDiff(damage);
				Server.playerHandler.players[i].setHitUpdateRequired(true);
			} else {
				Server.playerHandler.players[i].setHitDiff2(damage);
				Server.playerHandler.players[i].setHitUpdateRequired2(true);			
			}*/
                //Server.playerHandler.players[i].playerLevel[3] -= damage1;
                Server.playerHandler.players[i].dealDamage(damage1);
                Server.playerHandler.players[i].damageTaken[c.playerId] += damage1;
                c.totalPlayerDamageDealt += damage1;
                Server.playerHandler.players[i].updateRequired = true;
                o.getPA().refreshSkill(3);
                break;

            case 2:
                /*if (!Server.playerHandler.players[i].getHitUpdateRequired2()){
				Server.playerHandler.players[i].setHitDiff2(damage);
				Server.playerHandler.players[i].setHitUpdateRequired2(true);
			} else {
				Server.playerHandler.players[i].setHitDiff(damage);
				Server.playerHandler.players[i].setHitUpdateRequired(true);			
			}*/
                //Server.playerHandler.players[i].playerLevel[3] -= damage1;
                Server.playerHandler.players[i].dealDamage(damage1);
                Server.playerHandler.players[i].damageTaken[c.playerId] += damage1;
                c.totalPlayerDamageDealt += damage1;
                Server.playerHandler.players[i].updateRequired = true;
                c.doubleHit = false;
                o.getPA().refreshSkill(3);
                break;
        }
        Server.playerHandler.players[i].handleHitMask(damage1);
    }

    public void applySmite(int index, int damage) {
        if (!c.prayerActive[23]) return;
        if (damage <= 0) return;
        if (Server.playerHandler.players[index] != null) {
            Client c2 = (Client) Server.playerHandler.players[index];
            c2.playerLevel[5] -= (int)(damage / 4);
            if (c2.playerLevel[5] <= 0) {
                c2.playerLevel[5] = 0;
                c2.getCombat().resetPrayers();
            }
            c2.getPA().refreshSkill(5);
        }

    }
    /*Zant Curses*/
    public void applynpcSoulSplit(int index, int damage) {
        if (!c.curseActive[18]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                if (damage == 0) {
                    c.sendMessage("test");
                    return;
                }
                if (c.curseActive[18] && !c.prayerActive[23] && (c.playerLevel[3] <= 99)) {
                    int heal = 2;
                    if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(c.playerXP[3])) {
                        c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                    } else {
                        c.playerLevel[3] += heal;
                    }
                    c.getPA().refreshSkill(3);
                }
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, 50, 2263, 9, 9, c.oldNpcIndex + 1, 24, 0);
                c.soulSplitDelay = 4;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.soulSplitDelay > 0) {
                            c.soulSplitDelay--;
                        }
                        if (c.soulSplitDelay == 3) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2264); // 1738
                        }
                        if (c.soulSplitDelay == 2) {
                            int offX2 = (nY - pY) * -1;
                            int offY2 = (nX - pX) * -1;
                            c.getPA().createPlayersProjectile(nX, nY, offX2, offY2, 50, 45, 2263, 31, 31, -c.playerId - 1, 0);
                        }
                        if (c.soulSplitDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }
    public void applySoulSplit(int index, int damage) {
        if (!c.curseActive[18]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            if (c.curseActive[18] && !c.prayerActive[23] && (c.playerLevel[3] <= 99)) {
                int heal = 2;
                if (c2.playerLevel[5] <= 0) {
                    c2.playerLevel[5] = 0;
                    c2.getCombat().resetPrayers();
                }
                if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(c.playerXP[3])) {
                    c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
                } else {
                    c.playerLevel[3] += heal;
                    c2.playerLevel[5] -= 1;
                }
                c.getPA().refreshSkill(3);
                c2.getPA().refreshSkill(5);
            }
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2263, 31, 31, -c.oldPlayerIndex - 1, 0);
            c.soulSplitDelay = 4;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.soulSplitDelay > 0) {
                        c.soulSplitDelay--;
                    }
                    if (c.soulSplitDelay == 3) {
                        c2.gfx0(2264);
                    }
                    if (c.soulSplitDelay == 2) {
                        int offX2 = (oY - pY) * -1;
                        int offY2 = (oX - pX) * -1;
                        c.getPA().createPlayersProjectile(oX, oY, offX2, offY2, 50, 45, 2263, 31, 31, -c.playerId - 1, 0);
                    }
                    if (c.soulSplitDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }
    public void deflectDamage(int damage) {
        int damage2 = 0;
        if (damage < 10) damage2 = 0;
        else damage2 = damage / 10;
        c.dealDamage(damage2);
    }



    public void applyLeeches(int index) {
        if (Misc.random(20) == 0) {
            leechAttack(index);
        }
        if (Misc.random(20) == 0) {
            leechDefence(index);
        }
        if (Misc.random(20) == 0) {
            leechStrength(index);
        }
        if (Misc.random(20) == 0) {
            leechSpecial(index);
        }
        if (Misc.random(20) == 0) {
            leechRanged(index);
        }
        if (Misc.random(20) == 0) {
            leechMagic(index);
        }
        if (Misc.random(20) == 0) {
            leechEnergy(index);
        }
    }

    public void applynpcLeeches(int index) {
        if (Misc.random(20) == 0) {
            npcleechAttack(index);
        }
        if (Misc.random(20) == 0) {
            npcleechDefence(index);
        }
        if (Misc.random(20) == 0) {
            npcleechStrength(index);
        }
        if (Misc.random(20) == 0) {
            npcleechSpecial(index);
        }
        if (Misc.random(20) == 0) {
            npcleechRanged(index);
        }
        if (Misc.random(20) == 0) {
            npcleechMagic(index);
        }
        if (Misc.random(20) == 0) {
            npcleechEnergy(index);
        }
    }

    public void leechAttack(int index) {
        if (!c.curseActive[10]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            c.sendMessage("You leech your opponent's attack.");
            c.startAnimation(12575);
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2231, 43, 31, -c.oldPlayerIndex - 1, 1);
            c.leechAttackDelay = 2;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.leechAttackDelay > 0) {
                        c.leechAttackDelay--;
                    }
                    if (c.leechAttackDelay == 1) {
                        c2.gfx0(2232);
                        if (c.attackMultiplier < 1.10) {
                            c.attackMultiplier += 0.01;
                        }
                        if (c2.attackMultiplier > 0.80) {
                            c2.attackMultiplier -= 0.01;
                        }
                    }
                    if (c.leechAttackDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }

    public void leechRanged(int index) {
        if (!c.curseActive[11]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            c.sendMessage("You leech your opponent's range.");
            c.startAnimation(12575);
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2236, 43, 31, -c.oldPlayerIndex - 1, 0);
            c.leechRangedDelay = 2;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.leechRangedDelay > 0) {
                        c.leechRangedDelay--;
                    }
                    if (c.leechRangedDelay == 1) {
                        c2.gfx0(2238);
                        if (c.rangedMultiplier < 1.10) {
                            c.rangedMultiplier += 0.01;
                        }
                        if (c2.rangedMultiplier > 0.80) {
                            c2.rangedMultiplier -= 0.01;
                        }
                    }
                    if (c.leechRangedDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }

    public void leechMagic(int index) {
        if (!c.curseActive[12]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            c.sendMessage("You leech your opponent's magic.");
            c.startAnimation(12575);
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2240, 43, 31, -c.oldPlayerIndex - 1, 2);
            c.leechMagicDelay = 2;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.leechMagicDelay > 0) {
                        c.leechMagicDelay--;
                    }
                    if (c.leechMagicDelay == 1) {
                        c2.gfx0(2242);
                        if (c.magicMultiplier < 1.10) {
                            c.magicMultiplier += 0.01;
                        }
                        if (c2.magicMultiplier > 0.80) {
                            c2.magicMultiplier -= 0.01;
                        }
                    }
                    if (c.leechMagicDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }

    public void leechDefence(int index) {
        if (!c.curseActive[13]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            c.sendMessage("You leech your opponent's defence.");
            c.startAnimation(12575);
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2244, 43, 31, -c.oldPlayerIndex - 1, 3);
            c.leechDefenceDelay = 2;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.leechDefenceDelay > 0) {
                        c.leechDefenceDelay--;
                    }
                    if (c.leechDefenceDelay == 1) {
                        c2.gfx0(2246);
                        if (c.defenceMultiplier < 1.10) {
                            c.defenceMultiplier += 0.01;
                        }
                        if (c2.defenceMultiplier > 0.80) {
                            c2.defenceMultiplier -= 0.01;
                        }
                    }
                    if (c.leechDefenceDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }

    public void leechStrength(int index) {
        if (!c.curseActive[14]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            c.sendMessage("You leech your opponent's strength.");
            c.startAnimation(12575);
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2248, 43, 31, -c.oldPlayerIndex - 1, 4);
            c.leechStrengthDelay = 2;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.leechStrengthDelay > 0) {
                        c.leechStrengthDelay--;
                    }
                    if (c.leechStrengthDelay == 1) {
                        c2.gfx0(2250);
                        if (c.strengthMultiplier < 1.10) {
                            c.strengthMultiplier += 0.01;
                        }
                        if (c2.strengthMultiplier > 0.80) {
                            c2.strengthMultiplier -= 0.01;
                        }
                    }
                    if (c.leechStrengthDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }

    public void leechEnergy(int index) {
        if (!c.curseActive[15]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            c.sendMessage("You leech your opponent's run energy.");
            c.startAnimation(12575);
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2252, 43, 31, -c.oldPlayerIndex - 1, 5);
            c.leechEnergyDelay = 2;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.leechEnergyDelay > 0) {
                        c.leechEnergyDelay--;
                    }
                    if (c.leechEnergyDelay == 1) {
                        c2.gfx0(2254);
                    }
                    if (c.leechEnergyDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }

    public void leechSpecial(int index) {
        if (!c.curseActive[16]) return;
        if (Server.playerHandler.players[index] != null) {
            final Client c2 = (Client) Server.playerHandler.players[index];
            final int pX = c.getX();
            final int pY = c.getY();
            final int oX = c2.getX();
            final int oY = c2.getY();
            int offX = (pY - oY) * -1;
            int offY = (pX - oX) * -1;
            c.sendMessage("You leech your opponent's special attack.");
            c.startAnimation(12575);
            c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2256, 43, 31, -c.oldPlayerIndex - 1, 6);
            c.leechSpecialDelay = 2;
            World.getWorld().submit(new Event(500) {
                public void execute() {
                    if (c.leechSpecialDelay > 0) {
                        c.leechSpecialDelay--;
                    }
                    if (c.leechSpecialDelay == 1) {
                        c2.gfx0(2258);
                        if (c.specAmount >= 10) return;
                        if (c2.specAmount <= 0) return;
                        c.specAmount += 1;
                        c2.specAmount -= 1;
                        c2.sendMessage("Your special attack has been drained.");
                    }
                    if (c.leechSpecialDelay == 0) {
                        this.stop();
                    }
                }
            });
        }
    }

    public void npcleechAttack(int index) {
        if (!c.curseActive[10]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.sendMessage("You leech your opponent's attack.");
                c.startAnimation(12575);
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2231, 43, 31, -c.oldNpcIndex - 1, 1);
                c.leechAttackDelay = 2;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.leechAttackDelay > 0) {
                            c.leechAttackDelay--;
                        }
                        if (c.leechAttackDelay == 1) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2232);
                            if (c.attackMultiplier < 1.10) {
                                c.attackMultiplier += 0.01;
                            }
                        }
                        if (c.leechAttackDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }

    public void npcleechRanged(int index) {
        if (!c.curseActive[11]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.sendMessage("You leech your opponent's range.");
                c.startAnimation(12575);
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2236, 43, 31, -c.oldNpcIndex - 1, 0);
                c.leechRangedDelay = 2;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.leechRangedDelay > 0) {
                            c.leechRangedDelay--;
                        }
                        if (c.leechRangedDelay == 1) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2238);
                            if (c.rangedMultiplier < 1.10) {
                                c.rangedMultiplier += 0.01;
                            }
                        }
                        if (c.leechRangedDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }

    public void npcleechMagic(int index) {
        if (!c.curseActive[12]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.sendMessage("You leech your opponent's magic.");
                c.startAnimation(12575);
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2240, 43, 31, -c.oldNpcIndex - 1, 2);
                c.leechMagicDelay = 2;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.leechMagicDelay > 0) {
                            c.leechMagicDelay--;
                        }
                        if (c.leechMagicDelay == 1) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2242);
                            if (c.magicMultiplier < 1.10) {
                                c.magicMultiplier += 0.01;
                            }
                        }
                        if (c.leechMagicDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }

    public void npcleechDefence(int index) {
        if (!c.curseActive[13]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.sendMessage("You leech your opponent's defence.");
                c.startAnimation(12575);
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2244, 43, 31, -c.oldNpcIndex - 1, 3);
                c.leechDefenceDelay = 2;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.leechDefenceDelay > 0) {
                            c.leechDefenceDelay--;
                        }
                        if (c.leechDefenceDelay == 1) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2246);
                            if (c.defenceMultiplier < 1.10) {
                                c.defenceMultiplier += 0.01;
                            }
                        }
                        if (c.leechDefenceDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }

    public void npcleechStrength(int index) {
        if (!c.curseActive[14]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.sendMessage("You leech your opponent's strength.");
                c.startAnimation(12575);
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2248, 43, 31, -c.oldNpcIndex - 1, 4);
                c.leechStrengthDelay = 2;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.leechStrengthDelay > 0) {
                            c.leechStrengthDelay--;
                        }
                        if (c.leechStrengthDelay == 1) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2250);
                            if (c.strengthMultiplier < 1.10) {
                                c.strengthMultiplier += 0.01;
                            }
                        }
                        if (c.leechStrengthDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }

    public void npcleechEnergy(int index) {
        if (!c.curseActive[15]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.sendMessage("You leech your opponent's run energy.");
                c.startAnimation(12575);
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2252, 43, 31, -c.oldNpcIndex - 1, 5);
                c.leechEnergyDelay = 2;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.leechEnergyDelay > 0) {
                            c.leechEnergyDelay--;
                        }
                        if (c.leechEnergyDelay == 1) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2254);
                        }
                        if (c.leechEnergyDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }

    public void npcleechSpecial(int index) {
        if (!c.curseActive[16]) return;
        if (c.oldNpcIndex > 0) {
            if (NPCHandler.npcs[c.oldNpcIndex] != null) {
                final int pX = c.getX();
                final int pY = c.getY();
                final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
                final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
                final int offX = (pY - nY) * -1;
                final int offY = (pX - nX) * -1;
                c.sendMessage("You leech your opponent's special attack.");
                c.startAnimation(12575);
                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2256, 43, 31, -c.oldNpcIndex - 1, 6);
                c.leechSpecialDelay = 2;
                World.getWorld().submit(new Event(500) {
                    public void execute() {
                        if (c.leechSpecialDelay > 0) {
                            c.leechSpecialDelay--;
                        }
                        if (c.leechSpecialDelay == 1) {
                            NPCHandler.npcs[c.oldNpcIndex].gfx0(2258);
                            if (c.specAmount >= 10) return;
                            c.specAmount += 1;
                        }
                        if (c.leechSpecialDelay == 0) {
                            this.stop();
                        }
                    }
                });
            }
        }
    }
    public void fireProjectilePlayer() {
        if (c.oldPlayerIndex > 0) {
            if (Server.playerHandler.players[c.oldPlayerIndex] != null) {
                c.projectileStage = 2;
                int pX = c.getX();
                int pY = c.getY();
                int oX = Server.playerHandler.players[c.oldPlayerIndex].getX();
                int oY = Server.playerHandler.players[c.oldPlayerIndex].getY();
                int offX = (pY - oY) * -1;
                int offY = (pX - oX) * -1;
                if (!c.msbSpec) c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, getStartDelay());
                else if (c.msbSpec) {
                    c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, getStartDelay(), 10);
                    c.msbSpec = false;
                }
                if (usingDbow()) c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 60, 31, -c.oldPlayerIndex - 1, getStartDelay(), 35);
            }
        }
    }

    public boolean usingDbow() {
        return c.playerEquipment[c.playerWeapon] == 11235 || c.playerEquipment[c.playerWeapon] == 15704 || c.playerEquipment[c.playerWeapon] == 15703 || c.playerEquipment[c.playerWeapon] == 15702 || c.playerEquipment[c.playerWeapon] == 15701;
    }





    /**Prayer**/

    public void activatePrayer(int i) {
        if (c.duelRule[7]) {
            for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows 
                c.prayerActive[p] = false;
                c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
            }
            c.sendMessage("Prayer has been disabled in this duel!");
            return;
        }
        if (i == 10 && c.isInArd()) {
            c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
            c.sendMessage("You can't Protect item in Ardounge PVP, You automatically keep your most valuable item!");
            return;
        }
        if (c.playerPrayerBook == 1) {
            for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows 
                c.prayerActive[p] = false;
                c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
            }
            c.sendMessage("You are not on the right prayerbook!");
            return;
        }
        if (i == 24 && c.playerLevel[1] < 60) {
            c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
            c.sendMessage("You need 60 defence to use this");
            return;
        }
        if (i == 26 && c.playerLevel[1] < 30) {
            c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
            c.sendMessage("You need 30 defence to use this");
            return;
        }
        if (i == 25 && c.playerLevel[1] < 70) {
            c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
            c.sendMessage("You need 70 defence to use piety");
            return;
        }
        int[] defPray = {
            0, 5, 13, 24, 25, 26
        };
        int[] strPray = {
            1, 6, 14, 24, 25, 26
        };
        int[] atkPray = {
            2, 7, 15, 24, 25, 26
        };
        int[] rangePray = {
            3, 11, 19
        };
        int[] magePray = {
            4, 12, 20
        };
        int[] sapPray = {
            27, 28, 29, 30
        };
        int[] deflectPray = {
            32, 33, 34, 35
        };
        int[] leechPray = {
            36, 37, 38, 39, 40, 41, 42
        };
        int[] others = {
            43, 44, 45
        };
        if (c.playerLevel[5] > 0 || !Config.PRAYER_POINTS_REQUIRED) {
            if (c.getPA().getLevelForXP(c.playerXP[5]) >= c.PRAYER_LEVEL_REQUIRED[i] || !Config.PRAYER_LEVEL_REQUIRED) {
                boolean headIcon = false;
                switch (i) {
                    //case 26:
                    case 27:
                    case 28:
                    case 29:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < sapPray.length; j++) {
                                if (sapPray[j] != i) {
                                    c.prayerActive[sapPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[sapPray[j]], 0);
                                }
                            }
                        }
                        break;

                    case 32:
                    case 33:
                    case 34:
                    case 35:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < deflectPray.length; j++) {
                                if (deflectPray[j] != i) {
                                    c.prayerActive[deflectPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[deflectPray[j]], 0);
                                }
                            }
                        }
                        break;

                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < leechPray.length; j++) {
                                if (leechPray[j] != i) {
                                    c.prayerActive[leechPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[leechPray[j]], 0);
                                }
                            }
                        }
                        break;

                    case 43:
                    case 44:
                    case 45:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < others.length; j++) {
                                if (others[j] != i) {
                                    c.prayerActive[others[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[others[j]], 0);
                                }
                            }
                        }
                        break;
                    case 0:
                    case 5:
                    case 13:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < defPray.length; j++) {
                                if (defPray[j] != i) {
                                    c.prayerActive[defPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
                                }
                            }
                        }
                        break;

                    case 1:
                    case 6:

                    case 14:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < strPray.length; j++) {
                                if (strPray[j] != i) {
                                    c.prayerActive[strPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < rangePray.length; j++) {
                                if (rangePray[j] != i) {
                                    c.prayerActive[rangePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < magePray.length; j++) {
                                if (magePray[j] != i) {
                                    c.prayerActive[magePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
                                }
                            }
                        }
                        break;

                    case 2:
                    case 7:
                    case 15:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < atkPray.length; j++) {
                                if (atkPray[j] != i) {
                                    c.prayerActive[atkPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < rangePray.length; j++) {
                                if (rangePray[j] != i) {
                                    c.prayerActive[rangePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < magePray.length; j++) {
                                if (magePray[j] != i) {
                                    c.prayerActive[magePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
                                }
                            }
                        }
                        break;

                    case 3:
                        //range prays
                    case 11:
                    case 19:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < atkPray.length; j++) {
                                if (atkPray[j] != i) {
                                    c.prayerActive[atkPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < strPray.length; j++) {
                                if (strPray[j] != i) {
                                    c.prayerActive[strPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < rangePray.length; j++) {
                                if (rangePray[j] != i) {
                                    c.prayerActive[rangePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < magePray.length; j++) {
                                if (magePray[j] != i) {
                                    c.prayerActive[magePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
                                }
                            }
                        }
                        break;
                    case 4:
                    case 12:
                    case 20:
                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < atkPray.length; j++) {
                                if (atkPray[j] != i) {
                                    c.prayerActive[atkPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < strPray.length; j++) {
                                if (strPray[j] != i) {
                                    c.prayerActive[strPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < rangePray.length; j++) {
                                if (rangePray[j] != i) {
                                    c.prayerActive[rangePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < magePray.length; j++) {
                                if (magePray[j] != i) {
                                    c.prayerActive[magePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
                                }
                            }
                        }
                        break;
                    case 10:
                        c.lastProtItem = System.currentTimeMillis();
                        break;


                    case 16:
                    case 17:
                    case 18:
                        if (System.currentTimeMillis() - c.stopPrayerDelay < 5000) {
                            c.sendMessage("You have been injured and can't use this prayer!");
                            c.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
                            c.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
                            c.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
                            return;
                        }
                        if (i == 16) c.protMageDelay = System.currentTimeMillis();
                        else if (i == 17) c.protRangeDelay = System.currentTimeMillis();
                        else if (i == 18) c.protMeleeDelay = System.currentTimeMillis();
                    case 21:
                    case 22:
                    case 23:
                        headIcon = true;
                        for (int p = 16; p < 24; p++) {
                            if (i != p && p != 19 && p != 20) {
                                c.prayerActive[p] = false;
                                c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
                            }
                        }
                        break;
                    case 26:
                        if (c.prayerActive[i] == false) {
                            c.gfx0(2226);
                            c.startAnimation(12565);
                            for (int j = 0; j < atkPray.length; j++) {
                                if (atkPray[j] != i) {
                                    c.prayerActive[atkPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < strPray.length; j++) {
                                if (strPray[j] != i) {
                                    c.prayerActive[strPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < rangePray.length; j++) {
                                if (rangePray[j] != i) {
                                    c.prayerActive[rangePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < magePray.length; j++) {
                                if (magePray[j] != i) {
                                    c.prayerActive[magePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < defPray.length; j++) {
                                if (defPray[j] != i) {
                                    c.prayerActive[defPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
                                }
                            }
                        }
                        break;
                    case 24:
                    case 25:

                        if (c.prayerActive[i] == false) {
                            for (int j = 0; j < atkPray.length; j++) {
                                if (atkPray[j] != i) {
                                    c.prayerActive[atkPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < strPray.length; j++) {
                                if (strPray[j] != i) {
                                    c.prayerActive[strPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
                                }
                            }
                            for (int j = 0; j < rangePray.length; j++) {
                                if (rangePray[j] != i) {
                                    c.prayerActive[rangePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < magePray.length; j++) {
                                if (magePray[j] != i) {
                                    c.prayerActive[magePray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
                                }
                            }
                            for (int j = 0; j < defPray.length; j++) {
                                if (defPray[j] != i) {
                                    c.prayerActive[defPray[j]] = false;
                                    c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
                                }
                            }
                        }
                        break;
                }

                if (!headIcon) {
                    if (c.prayerActive[i] == false) {
                        c.prayerActive[i] = true;
                        c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
                    } else {
                        c.prayerActive[i] = false;
                        c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
                    }
                } else {
                    if (c.prayerActive[i] == false) {
                        c.prayerActive[i] = true;
                        c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
                        c.headIcon = c.PRAYER_HEAD_ICONS[i];
                        c.getPA().requestUpdates();
                    } else {
                        c.prayerActive[i] = false;
                        c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
                        c.headIcon = -1;
                        c.getPA().requestUpdates();
                    }
                }
            } else {
                c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
                c.getPA().sendFrame126("You need a @blu@Prayer level of " + c.PRAYER_LEVEL_REQUIRED[i] + " to use " + c.PRAYER_NAME[i] + ".", 357);
                c.getPA().sendFrame126("Click here to continue", 358);
                c.getPA().sendFrame164(356);
            }
        } else {
            c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
            c.sendMessage("You have run out of prayer points!");
        }

    }

    /**
     *Specials
     **/

    public void activateSpecial(int weapon, int i) {
        c.doubleHit = false;
        c.specEffect = 0;
        c.projectileStage = 0;
        c.specMaxHitIncrease = 2;
        if (c.npcIndex > 0) {
            c.oldNpcIndex = i;
        } else if (c.playerIndex > 0) {
            c.oldPlayerIndex = i;
            Server.playerHandler.players[i].underAttackBy = c.playerId;
            Server.playerHandler.players[i].logoutDelay = System.currentTimeMillis();
            Server.playerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
            Server.playerHandler.players[i].killerId = c.playerId;
        }
        switch (weapon) {
            case 10887:
                c.startAnimation(5870);
                c.specDamage = 1.15;
                c.specAccuracy = 1.50;
                c.gfx0(425);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                break;
            case 1305:
                // dragon long
                c.gfx100(248);
                c.startAnimation(12033);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.specAccuracy = 1.10;
                c.specDamage = 1.20;
                break;

            case 1215:
                // dragon daggers
            case 1231:
            case 5680:
            case 5698:
                c.gfx100(252);
                c.startAnimation(1062);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.doubleHit = true;
                c.specAccuracy = 1.05;
                c.specDamage = 1.05;
                break;

            case 11730:
                //saradomin sword

                //c.gfx100(1194);
                c.startAnimation(7072);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.doubleHit = true;
                c.ssSpec = true;
                c.specAccuracy = 2.35;
                c.specDamage = .92;
                break;


            case 4151:
                // whip
            case 15441:
            case 15442:
            case 15443:
            case 15444:
                if (Server.npcHandler.npcs[i] != null) {
                    Server.npcHandler.npcs[i].gfx100(2108);
                }
                c.specEffect = 6;
                c.specAccuracy = 1.10;
                c.startAnimation(11956);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                break;



            case 14484:
                c.gfx0(1950);
                c.startAnimation(10961);
                //c.specAccuracy = 3.5;
                c.specAccuracy = 8;
                c.specDamage = .30;
                c.clawDamage = 0;

                if (c.playerIndex > 0) {
                    Client o = (Client) Server.playerHandler.players[c.playerIndex];
                    if (Misc.random(calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence())) {
                        c.clawDamage = Misc.random(calculateMeleeMaxHit() + Misc.random(2));
                    }
                    c.clawIndex = c.playerIndex;
                    c.clawType = 1;
                } else if (c.npcIndex > 0) {
                    //NPC n = Server.npcHandler.npcs[c.npcIndex];
                    //if (Misc.random(calculateMeleeAttack()) > Misc.random(n.defence)) {
                    c.clawDamage = Misc.random(calculateMeleeMaxHit() + Misc.random(2));
                    //}
                    c.clawIndex = c.npcIndex;
                    c.clawType = 2;
                }

                c.doubleHit = true;
                c.usingClaws = true;
                c.specEffect = 5;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                break;
            case 11700:
                // zgs
                c.startAnimation(7070);
                c.gfx0(1221);
                c.specAccuracy = 1.55;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.specEffect = 2;
                break;
            case 7158:
                // dragon 2h
                c.startAnimation(3157);
                c.gfx0(1225);
                c.specDamage = 1.75;
                c.specAccuracy = 2.6;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                //c.specEffect = 2;
                break;
            case 19780:
                c.startAnimation(4000);
                c.gfx0(1247);
                //c.korasiSpec = true;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
                c.specEffect = 9;
                c.specAccuracy = 0.01;
                break;
            case 11694:
                // ags
                c.startAnimation(11989);
                c.specDamage = 1.25;
                c.specAccuracy = 1.95; //was 1.95
                c.gfx0(2113);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                break;
            case 11696:
                // bgs
                c.startAnimation(11991);
                c.gfx0(1223);
                c.specDamage = 1.10;
                c.specAccuracy = 1.5;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.specEffect = 3;
                break;

            case 11698:
                c.startAnimation(7071);
                c.gfx0(1220);
                c.specAccuracy = 1.25;
                c.specEffect = 4;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                break;

            case 1249:
                c.startAnimation(405);
                c.gfx100(253);
                resetPlayerAttack();

                break;

            case 3204:
                // d hally
                c.gfx100(282);
                c.startAnimation(1203);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                if (Server.npcHandler.npcs[i] != null && c.npcIndex > 0) {
                    if (!c.goodDistance(c.getX(), c.getY(), Server.npcHandler.npcs[i].getX(), Server.npcHandler.npcs[i].getY(), 1)) {
                        c.doubleHit = true;
                    }
                }
                if (Server.playerHandler.players[i] != null && c.playerIndex > 0) {
                    if (!c.goodDistance(c.getX(), c.getY(), Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), 1)) {
                        c.doubleHit = true;
                        c.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
                    }
                }
                break;

            case 4153:
                // maul
                c.startAnimation(1667);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                /*if (c.playerIndex > 0)
				gmaulPlayer(i);
			else
				gmaulNpc(i);*/
                c.gfx100(337);
                break;

            case 4587:
                // dscimmy
                c.gfx100(347);
                c.specEffect = 1;
                c.startAnimation(12031);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                break;

            case 1434:
                // mace
                c.startAnimation(1060);
                c.gfx100(251);
                c.specMaxHitIncrease = 3;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()) + 1;
                c.specDamage = 1.35;
                c.specAccuracy = 1.15;
                break;

            case 13899:
                // VLS
                c.startAnimation(10502);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
                c.specDamage = 1.35;
                c.specAccuracy = 1.45;
                break;
            case 13905:
                c.startAnimation(10499);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
                c.specDamage = 1.35;
                c.gfx100(1835);
                c.specAccuracy = 1.65;
                break;
            case 13902:
                c.startAnimation(10505);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
                c.specDamage = 1.35;
                c.gfx100(1840);
                c.specAccuracy = 1.65;
                break;
            case 13879:
                // Morrigan Throwing Axe
                c.usingRangeWeapon = true;
                c.rangeItemUsed = c.playerEquipment[c.playerArrows];
                c.getItems().deleteArrow();
                c.lastWeaponUsed = weapon;
                c.startAnimation(10501);
                c.gfx0(1836);
                c.specEffect = 6;
                c.hitDelay = 3;
                c.specAccuracy = 1.90;
                c.specDamage = 1.09;
                c.projectileStage = 1;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                if (c.fightMode == 2) c.attackTimer--;
                if (c.playerIndex > 0) fireProjectilePlayer();
                else if (c.npcIndex > 0) fireProjectileNpc();
                break;

            case 13883:
                // Morrigan throwingaxe
                c.usingRangeWeapon = true;
                c.rangeItemUsed = c.playerEquipment[c.playerArrows];
                c.getItems().deleteArrow();
                c.lastWeaponUsed = weapon;
                c.startAnimation(10504);
                c.gfx0(1838);
                c.specAccuracy = 2.00;
                c.specDamage = 1.30;
                c.hitDelay = 3;
                c.projectileStage = 1;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                if (c.fightMode == 2) c.attackTimer--;
                if (c.playerIndex > 0) fireProjectilePlayer();
                else if (c.npcIndex > 0) fireProjectileNpc();
            case 859:
                // magic long
                c.usingBow = true;
                c.bowSpecShot = 3;
                c.rangeItemUsed = c.playerEquipment[c.playerArrows];
                c.getItems().deleteArrow();
                c.lastWeaponUsed = weapon;
                c.startAnimation(426);
                c.gfx100(250);
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.projectileStage = 1;
                if (c.fightMode == 2) c.attackTimer--;
                break;
            case 13022:
                //Cannon
                c.usingBow = true;
                c.rangeItemUsed = c.playerEquipment[c.playerArrows];
                c.lastWeaponUsed = weapon;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                c.specAccuracy = 1.75;
                c.specDamage = 1.50;
                //cannonSpec(i);
                break;
            case 861:
                // magic short	
                c.usingBow = true;
                c.bowSpecShot = 1;
                c.rangeItemUsed = c.playerEquipment[c.playerArrows];
                c.getItems().deleteArrow();
                c.lastWeaponUsed = weapon;
                c.startAnimation(1074);
                c.hitDelay = 3;
                c.projectileStage = 1;
                c.specAccuracy = 1.50;
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                if (c.fightMode == 2) c.attackTimer--;
                if (c.playerIndex > 0) fireProjectilePlayer();
                else if (c.npcIndex > 0) fireProjectileNpc();
                break;

            case 11235:
                // dark bow	
            case 15701:
            case 15702:
            case 15703:
            case 15704:
                c.usingBow = true;
                c.dbowSpec = true;
                c.rangeItemUsed = c.playerEquipment[c.playerArrows];
                c.getItems().deleteArrow();
                c.getItems().deleteArrow();
                c.lastWeaponUsed = weapon;
                c.hitDelay = 3;
                c.startAnimation(426);
                c.projectileStage = 1;
                c.gfx100(getRangeStartGFX());
                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                if (c.fightMode == 2) c.attackTimer--;
                if (c.playerIndex > 0) fireProjectilePlayer();
                else if (c.npcIndex > 0) fireProjectileNpc();
                c.specAccuracy = 1.75;
                c.specDamage = 1.50;
                break;
        }
        c.delayedDamage = Misc.random(calculateMeleeMaxHit());
        c.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
        c.usingSpecial = false;
        c.getItems().updateSpecialBar();
    }



    public boolean checkSpecAmount(int weapon) {
        if (c.playerEquipment[c.playerRing] == 19669) {
            c.specAmount += c.specAmount * 0.1;
        }
        switch (weapon) {

            case 19780:
                if (c.specAmount >= 6) {
                    c.specAmount -= 6;
                    c.getItems().addSpecialBar(weapon);
                    return true;
                }
                return false;

            case 1249:
            case 1215:
            case 1231:
            case 5680:
            case 5698:
            case 1305:
            case 1434:
            case 13899:
                if (c.specAmount >= 2.5) {
                    c.specAmount -= 2.5;
                    c.getItems().addSpecialBar(weapon);
                    return true;
                }
                return false;
            case 14484:
            case 13905:
            case 13022:
            case 10887:
            case 15241:
            case 4151:
            case 15441:
            case 15442:
            case 15443:
            case 15444:
            case 13879:
            case 13883:
                //case 13879: 
            case 11694:
            case 11698:
            case 4153:
            case 13902:
                if (c.specAmount >= 5) {
                    c.specAmount -= 5;
                    c.getItems().addSpecialBar(weapon);
                    return true;
                }
                return false;

            case 3204:
                if (c.specAmount >= 3) {
                    c.specAmount -= 3;
                    c.getItems().addSpecialBar(weapon);
                    return true;
                }
                return false;

            case 1377:
            case 11696:
            case 11730:
                if (c.specAmount >= 10) {
                    c.specAmount -= 10;
                    c.getItems().addSpecialBar(weapon);
                    return true;
                }
                return false;
            case 7158:
                if (c.specAmount >= 6) {
                    c.specAmount -= 6;
                    c.getItems().addSpecialBar(weapon);
                    return true;
                }
                return false;
            case 4587:
            case 859:
            case 861:
            case 11235:
            case 15701:
            case 15702:
            case 15703:
            case 15704:
            case 11700:

                if (c.specAmount >= 5.5) {
                    c.specAmount -= 5.5;
                    c.getItems().addSpecialBar(weapon);
                    return true;
                }
                return false;


            default:
                return true; // incase u want to test a weapon
        }
    }
    /*public void cannonSpec(int index) {
                if (Server.playerHandler.players[index] != null) {
                        Client c2 = (Client)Server.playerHandler.players[index];
                        int pX = c.getX();
                        int pY = c.getY();
                        int oX = c2.getX();
                        int oY = c2.getY();
                        int offX = (pY - oY)* -1;
                        int offY = (pX - oX)* -1;
                        final int test = index;
                        c.startAnimation(12152);
                        c.gfx0(2138);
                        c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 70, 2143, 43, 31, - c.oldPlayerIndex - 1, 30);
                        c.usingBow = true;
                        c.cannonSpecDelay = 7;
                        if (!c2.isDead || c2 != null) {
                        EventManager.getSingleton().addEvent(new Event() {
                                public void execute(EventContainer s) {
                                        if (c.cannonSpecDelay > 0) {
                                                c.cannonSpecDelay--;
                                        }
                                        if (c.cannonSpecDelay == 4) {
                                                Client c2 = (Client)Server.playerHandler.players[test];
                                                int pX = c.getX();
                                                int pY = c.getY();
                                                int oX = c2.getX();
                                                int oY = c2.getY();
                                                int offX = (pY - oY)* -1;
                                                int offY = (pX - oX)* -1;
                                                c.startAnimation(12153);
                                                c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2143, 43, 31, - c.oldPlayerIndex - 1, 30);
                                                c.gfx0(2138);
                                                c.attackTimer = 9;
                                        }
                                        if (c.cannonSpecDelay == 0) {
                                                Client c2 = (Client)Server.playerHandler.players[test];
                                                c.specAccuracy = 1.75;
                                                c.specDamage = 1.50;
                                                int damage = Misc.random(rangeMaxHit());
                                                if(Misc.random(10+c2.getCombat().calculateRangeDefence()) > Misc.random(10+calculateRangeAttack())) {
                                                        damage = 0;
                                                }
                                                if (c2.prayerActive[17] && System.currentTimeMillis() - c2.protRangeDelay > 1100)
                                                c2.dealDamage((damage * 6) / 10);
                                                
                                                else
                                                c2.dealDamage(damage);
                                                s.stop();
                                        }
                                }
                        }, 500);
                        }
                }
        }*/
    public void resetPlayerAttack() {
        c.inCombat = false;
        c.usingMagic = false;
        c.npcIndex = 0;
        c.faceUpdate(0);
        c.playerIndex = 0;
        c.getPA().resetFollow();
        //c.sendMessage("Reset attack.");
    }

    public int getCombatDifference(int combat1, int combat2) {
        if (combat1 > combat2) {
            return (combat1 - combat2);
        }
        if (combat2 > combat1) {
            return (combat2 - combat1);
        }
        return 0;
    }

    /**
     *Get killer id 
     **/

    public int getKillerId(int playerId) {
        int oldDamage = 0;
        int count = 0;
        int killerId = 0;
        for (int i = 1; i < Config.MAX_PLAYERS; i++) {
            if (Server.playerHandler.players[i] != null) {
                if (Server.playerHandler.players[i].killedBy == playerId) {
                    if (Server.playerHandler.players[i].withinDistance(Server.playerHandler.players[playerId])) {
                        if (Server.playerHandler.players[i].totalPlayerDamageDealt > oldDamage) {
                            oldDamage = Server.playerHandler.players[i].totalPlayerDamageDealt;
                            killerId = i;
                        }
                    }
                    Server.playerHandler.players[i].totalPlayerDamageDealt = 0;
                    Server.playerHandler.players[i].killedBy = 0;
                }
            }
        }
        return killerId;
    }



    double[] prayerData = {
        1, // Thick Skin.
        1, // Burst of Strength.
        1, // Clarity of Thought.
        1, // Sharp Eye.
        1, // Mystic Will.
        2, // Rock Skin.
        2, // SuperHuman Strength.
        2, // Improved Reflexes.
        0.4, // Rapid restore.
        0.6, // Rapid Heal.
        0.6, // Protect Items.
        1.5, // Hawk eye.
        2, // Mystic Lore.
        4, // Steel Skin.
        4, // Ultimate Strength.
        4, // Incredible Reflexes.
        4, // Protect from Magic.
        4, // Protect from Missiles.
        4, // Protect from Melee.
        4, // Eagle Eye.
        4, // Mystic Might.
        1, // Retribution.
        2, // Redemption.
        6, // Smite.
        7, // Chivalry.
        8, // Piety.
        9, // Turmoil

    };

    double[] curseData = {
        0.6, // Protect Item
        1, // Sap Warrior
        1, // Sap Range
        1, // Sap Mage
        1, // Sap Spirit
        1, // Berserker
        2, // Deflect Summoning
        2, // Deflect Mage 7
        2, // Deflect Range 8 
        2, // Deflect Melee 9
        2, // Leech Attack
        2, // Leech Range
        2, // Leech Mage
        2, // Leech Defence
        2, // Leech Strength
        2, // Leech Energy
        2, // Leech Special
        2, // Wrath
        4, // Soul Split
        5, // Turmoil
    };

    public void handlePrayerDrain() {
        c.usingPrayer = false;
        double toRemove = 0.0;
        for (int j = 0; j < prayerData.length; j++) {
            if (c.prayerActive[j]) {
                toRemove += prayerData[j] / 20;
                c.usingPrayer = true;
            }
        }
        for (int j = 0; j < curseData.length; j++) {
            if (c.curseActive[j]) {
                toRemove += curseData[j] / 20;
                c.usingPrayer = true;
            }
        }
        if (toRemove > 0) {
            toRemove /= (1 + (0.035 * c.playerBonus[11]));
        }
        c.prayerPoint -= toRemove;
        if (c.prayerPoint <= 0) {
            c.prayerPoint = 1.0 + c.prayerPoint;
            reducePrayerLevel();
        }
    }

    public void reducePrayerLevel() {
        if (c.playerLevel[5] - 1 > 0) {
            c.playerLevel[5] -= 1;
        } else {
            c.sendMessage("You have run out of prayer points!");
            c.playerLevel[5] = 0;
            c.getCombat().resetPrayers();
            c.prayerId = -1;
        }
        c.getPA().refreshSkill(5);
    }

    public void resetPrayers() {
        for (int i = 0; i < c.prayerActive.length; i++) {
            c.prayerActive[i] = false;
            c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
        }
        for (int i = 0; i < c.curseActive.length; i++) {
            c.curseActive[i] = false;
            c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
        }
        c.headIcon = -1;
        c.getPA().requestUpdates();
    }

    /**
     * Wildy and duel info
     **/

    public boolean checkReqs() {
        if (CastleWars.isInCw(c)) {
            return true;
        }
        if (Server.playerHandler.players[c.playerIndex] == null) {
            return false;
        }
        if (c.playerIndex == c.playerId) return false;
        if (c.inPits && Server.playerHandler.players[c.playerIndex].inPits) return true;
        if (Server.playerHandler.players[c.playerIndex].inDuelArena() && c.duelStatus != 5 && !c.usingMagic) {
            if (c.arenas() || c.duelStatus == 5) {
                c.sendMessage("You can't challenge inside the arena!");
                return false;
            }
            c.getTradeAndDuel().requestDuel(c.playerIndex);
            return false;
        }
        if (c.duelStatus == 5 && Server.playerHandler.players[c.playerIndex].duelStatus == 5) {
            if (Server.playerHandler.players[c.playerIndex].duelingWith == c.getId()) {
                return true;
            } else {
                c.sendMessage("This isn't your opponent!");
                return false;
            }
        }
        if (!Server.playerHandler.players[c.playerIndex].inFunPk() && !Server.playerHandler.players[c.playerIndex].inWild() && !Server.playerHandler.players[c.playerIndex].FreeForAllArea() && !Server.playerHandler.players[c.playerIndex].inIsland() && !Server.playerHandler.players[c.playerIndex].isInArd()) {
            c.sendMessage("That player is not in the wilderness.");
            c.stopMovement();
            c.getCombat().resetPlayerAttack();
            return false;
        }
        if (!Server.playerHandler.players[c.playerIndex].inFunPk() && !c.inWild() && !Server.playerHandler.players[c.playerIndex].FreeForAllArea() && !Server.playerHandler.players[c.playerIndex].inIsland() && !Server.playerHandler.players[c.playerIndex].isInArd()) {
            c.sendMessage("You are not in the wilderness.");
            c.stopMovement();
            c.getCombat().resetPlayerAttack();
            return false;
        }
        if (Server.playerHandler.players[c.playerIndex].safeZone()) {
            c.sendMessage("This player is currently in a safe zone.");
            c.stopMovement();
            c.getCombat().resetPlayerAttack();
            return false;
        }
        if (c.safeZone()) {
            c.sendMessage("You are standing in a safe zone.");
            c.stopMovement();
            c.getCombat().resetPlayerAttack();
            return false;
        }
        if (Config.COMBAT_LEVEL_DIFFERENCE && !Server.playerHandler.players[c.playerIndex].FreeForAllArea() && !Server.playerHandler.players[c.playerIndex].inIsland()) {
            int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, Server.playerHandler.players[c.playerIndex].combatLevel);
            if (combatDif1 > c.wildLevel || combatDif1 > Server.playerHandler.players[c.playerIndex].wildLevel) {
                c.sendMessage("Your combat level difference is too great to attack that player here.");
                c.stopMovement();
                c.getCombat().resetPlayerAttack();
                return false;
            }
        }

        if (Config.SINGLE_AND_MULTI_ZONES && !Server.playerHandler.players[c.playerIndex].inIsland()) {
            if (!Server.playerHandler.players[c.playerIndex].inMulti()) { // single combat zones
                if (Server.playerHandler.players[c.playerIndex].underAttackBy != c.playerId && Server.playerHandler.players[c.playerIndex].underAttackBy != 0) {
                    c.sendMessage("That player is already in combat.");
                    c.stopMovement();
                    c.getCombat().resetPlayerAttack();
                    return false;
                }
                if (Server.playerHandler.players[c.playerIndex].playerId != c.underAttackBy && c.underAttackBy != 0 || c.underAttackBy2 > 0) {
                    c.sendMessage("You are already in combat.");
                    c.stopMovement();
                    c.getCombat().resetPlayerAttack();
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkMultiBarrageReqs(int i) {
        if (Server.playerHandler.players[i] == null) {
            return false;
        }
        if (i == c.playerId) return false;
        if (c.inPits && Server.playerHandler.players[i].inPits) return true;
        if (!Server.playerHandler.players[i].inWild()) {
            return false;
        }
        if (Config.COMBAT_LEVEL_DIFFERENCE && !Server.playerHandler.players[c.playerIndex].inIsland()) {
            int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, Server.playerHandler.players[i].combatLevel);
            if (combatDif1 > c.wildLevel || combatDif1 > Server.playerHandler.players[i].wildLevel) {
                c.sendMessage("Your combat level difference is too great to attack that player here.");
                return false;
            }
        }

        if (Config.SINGLE_AND_MULTI_ZONES && !Server.playerHandler.players[c.playerIndex].inIsland()) {
            if (!Server.playerHandler.players[i].inMulti()) { // single combat zones
                if (Server.playerHandler.players[i].underAttackBy != c.playerId && Server.playerHandler.players[i].underAttackBy != 0) {
                    return false;
                }
                if (Server.playerHandler.players[i].playerId != c.underAttackBy && c.underAttackBy != 0) {
                    c.sendMessage("You are already in combat.");
                    return false;
                }
            }
        }
        return true;
    }
    /**
     *Weapon stand, walk, run, etc emotes
     **/

    public void getPlayerAnimIndex(String weaponName) {
        c.playerStandIndex = 0x328;
        c.playerTurnIndex = 0x337;
        c.playerWalkIndex = 0x333;
        c.playerTurn180Index = 0x334;
        c.playerTurn90CWIndex = 0x335;
        c.playerTurn90CCWIndex = 0x336;
        c.playerRunIndex = 0x338;

        if (weaponName.contains("halberd") || weaponName.contains("guthan")) {
            c.playerStandIndex = 809;
            c.playerWalkIndex = 1146;
            c.playerRunIndex = 1210;
            return;
        }
        if (weaponName.contains("dharok")) {
            c.playerStandIndex = 0x811;
            c.playerWalkIndex = 0x67F;
            c.playerRunIndex = 0x680;
            return;
        }
        if (weaponName.contains("chaotic maul")) {
            c.playerStandIndex = 1662;
            c.playerWalkIndex = 1663;
            c.playerRunIndex = 1664;
            return;
        }
        if (weaponName.contains("ahrim")) {
            c.playerStandIndex = 809;
            c.playerWalkIndex = 1146;
            c.playerRunIndex = 1210;
            return;
        }
        if (weaponName.contains("verac")) {
            c.playerStandIndex = 0x328;
            c.playerWalkIndex = 0x333;
            c.playerRunIndex = 824;
            return;
        }
        if (weaponName.contains("chaotic staff")) {
            c.playerStandIndex = 808;
            c.playerRunIndex = 1210;
            c.playerWalkIndex = 1146;
            return;
        }
        if (weaponName.contains("wand") || weaponName.contains("staff") || weaponName.contains("staff") || weaponName.contains("spear")) {
            c.playerStandIndex = 8980;
            c.playerRunIndex = 1210;
            c.playerWalkIndex = 1146;
            return;
        }
        if (weaponName.contains("karil")) {
            c.playerStandIndex = 2074;
            c.playerWalkIndex = 2076;
            c.playerRunIndex = 2077;
            return;
        }
        if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")) {
            c.playerStandIndex = 7047;
            c.playerWalkIndex = 7046;
            c.playerRunIndex = 7039;
            return;
        }
        if (weaponName.contains("bow")) {
            c.playerStandIndex = 808;
            c.playerWalkIndex = 819;
            c.playerRunIndex = 824;
            return;
        }

        switch (c.playerEquipment[c.playerWeapon]) {
            case 4151:
                c.playerStandIndex = 11973;
                c.playerWalkIndex = 11975;
                c.playerRunIndex = 1661;
                break;
            case 15403:

                c.playerStandIndex = 0x811;
                c.playerWalkIndex = 0x67F;
                c.playerRunIndex = 0x680;
                break;

            case 15441:
                c.playerStandIndex = 11973;
                c.playerWalkIndex = 11975;
                c.playerRunIndex = 1661;
                break;
            case 15241:
                c.playerStandIndex = 12155;
                c.playerWalkIndex = 12154;
                c.playerRunIndex = 12154;
                break;
            case 15442:
                c.playerStandIndex = 11973;
                c.playerWalkIndex = 11975;
                c.playerRunIndex = 1661;
                break;
            case 15443:
                c.playerStandIndex = 11973;
                c.playerWalkIndex = 11975;
                c.playerRunIndex = 1661;
                break;
            case 15444:
                c.playerStandIndex = 11973;
                c.playerWalkIndex = 11975;
                c.playerRunIndex = 1661;
                break;
            case 10887:
                c.playerStandIndex = 5869;
                c.playerWalkIndex = 5867;
                c.playerRunIndex = 5868;
                break;
            case 6528:
                c.playerStandIndex = 0x811;
                c.playerWalkIndex = 2064;
                c.playerRunIndex = 1664;
                break;
            case 4153:
                c.playerStandIndex = 1662;
                c.playerWalkIndex = 1663;
                c.playerRunIndex = 1664;
                break;
            case 13022:
                c.playerStandIndex = 12155;
                c.playerWalkIndex = 12155;
                c.playerRunIndex = 12154;
                break;
            case 11694:
            case 11696:
            case 11730:
            case 11698:
            case 11700:
                c.playerStandIndex = 4300;
                c.playerWalkIndex = 4306;
                c.playerRunIndex = 4305;
                break;
            case 1305:
                c.playerStandIndex = 809;
                break;
        }
    }

    /**
     * Weapon emotes
     **/

    public int getWepAnim(String weaponName) {
        if (c.playerEquipment[c.playerWeapon] <= 0) {
            switch (c.fightMode) {
                case 0:
                    return 422;
                case 2:
                    return 423;
                case 1:
                    return 451;
            }
        }
        if (weaponName.contains("morrigan's throwing axe") || weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
            return 806;
        }
        if (weaponName.contains("halberd")) {
            return 440;
        }
        if (weaponName.startsWith("dragon dagger")) {
            return 402;
        }
        if (weaponName.endsWith("dagger")) {
            return 412;
        }
        if (weaponName.contains("crossbow")) {
            return 4230;
        }
        if (weaponName.contains("chaotic rapier")) {
            return 386;
        }
        if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sword") || weaponName.contains("Dragon 2h sword")) {
            switch (c.fightMode) {
                case 0:
                    return 7041;
                case 2:
                    return 7041;
                case 1:
                    return 7048;
            }
        }
        if (weaponName.contains("scimitar") || weaponName.contains("longsword")) {
            switch (c.fightMode) {
                case 0:
                    return 12029;
                case 1:
                    // New Scimmi models
                    return 12029;
                case 2:
                    return 12029;
                case 3:
                    return 12028;
            }
        }
        if (weaponName.contains("rapier")) {
            switch (c.fightMode) {
                case 0:
                    return 390;
                case 1:
                    return 390;
                case 2:
                    return 390;
                case 3:
                    return 386;
            }
        }
        if (weaponName.contains("dharok") || weaponName.contains("balmung")) {
            switch (c.fightMode) {
                case 0:
                    return 2066;
                case 1:
                    return 2066;
                case 2:
                    return 2066;
                case 3:
                    return 2067;
            }
        }
        if (weaponName.contains("sword")) {
            return 451;
        }
        if (weaponName.contains("karil")) {
            return 2075;
        }
        //if(weaponName.contains("bow") && !weaponName.contains("'bow")) {
        //return 426;
        //}
        if (weaponName.contains("'bow")) return 4230;
        if (weaponName.contains("Hand cannon")) return 4230;
        switch (c.playerEquipment[c.playerWeapon]) { // if you don't want to use strings
            case 841:
            case 843:
            case 845:
            case 847:
            case 849:
            case 851:
            case 853:
            case 855:
            case 857:
            case 859:
            case 861:
            case 4212:
            case 4215:
            case 4216:
            case 4217:
            case 4218:
            case 4219:
            case 4220:
            case 4221:
            case 4222:
            case 4223:
            case 4214:

            case 11235:
            case 15701:
            case 15702:
            case 15703:
            case 15704:

                return 426;
            case 18357:
                return 4230;
            case 6522:
                return 2614;
            case 13905:
                return 2080;
            case 4153:
                // granite maul
                return 1665;
            case 18349:
                // Item ID 
                return 386; //Animation ID Chaotic rapier
            case 18351:
                //Item ID 
                return 451; //Animation Id Chaotic longsword
            case 4726:
                // guthan 
                return 2080;
            case 15015:
            case 15016:
                return 806;
            case 14484:
                //  Dclaw
                return 393;
            case 18353:
                //  Chaotic maul
                return 2661;
            case 13022:
                return 12153;
            case 4747:
                // torag
                return 0x814;
            case 4710:
                // ahrim
                return 406;
            case 4755:
                // verac
                return 2062;
            case 4734:
                // karil
                return 2075;
            case 15241:
                return 12153;
            case 10887:
                return 5865;
            case 4151:
            case 15441:
            case 15442:
            case 15443:
            case 15444:
                return 1658;
            case 6528:
                return 2661;
            default:
                return 451;
        }
    }

    /**
     * Block emotes
     */
    public int getBlockEmote() {
        if (c.playerEquipment[c.playerShield] >= 8844 && c.playerEquipment[c.playerShield] <= 8850 && c.playerEquipment[c.playerShield] == 13351) {
            return 4177;
        }
        switch (c.playerEquipment[c.playerWeapon]) {

            case 19780:
                return 12030;

            case 8844:
            case 8850:
            case 16714:
                return 4177;

            case 15241:
                return 12156;

            case 4755:
                return 2063;

            case 10887:
                return 5866;

            case 4718:
                return 12004;

            case 4153:
                return 1666;

            case 13022:
                return 12156;

            case 18353:
                return 12004;

            case 4151:
            case 15441:
            case 15442:
            case 15443:
            case 15444:
                return 11974;

            case 11694:
            case 11698:
            case 11700:
                // scimmy anim 12030
            case 11696:
            case 11730:
                //case 861:
                return -1;

            default:
                //return 404;
                return 424;
        }
    }

    /**
     * Weapon and magic attack speed!
     **/

    public int getAttackDelay(String s) {
        int get = 4; // default
        String[][] getDelay = {
            {
                "dart", "3"
            }, {
                "knife", "3"
            }, {
                "Blisterwood stake", "3"
            }, {
                "Shortbow", "4"
            }, {
                "Karils crossbow", "4"
            }, {
                " Toktz-xil-ul", "4"
            }, {
                " Dagger", "4"
            }, {
                "Bronze sword", "4"
            }, {
                "Iron sword", "4"
            }, {
                "Steel sword", "4"
            }, {
                "Black sword", "4"
            }, {
                "Mithril sword", "4"
            }, {
                "Adamant sword", "4"
            }, {
                "Rune sword", "4"
            }, {
                "Scimitar", "4"
            }, {
                "Abyssal whip", "4"
            }, {
                "claws", "4"
            }, {
                "Zamorakian spear", "4"
            }, {
                "Saradomin sword", "4"
            }, {
                "Toktz-xil-ak", "4"
            }, {
                "Toktz-xil-ek", "4"
            }, {
                "Saradomin staff", "4"
            }, {
                "Zamorak staff", "4"
            }, {
                "Guthix staff", "4"
            }, {
                "Slayer's staff", "4"
            }, {
                "Ancient staff", "4"
            }, {
                "Gravite rapier", "4"
            }, {
                "Chaotic rapier", "4"
            }, {
                "Armadyl battlestaff", "4"
            }, {
                "Longsword", "5"
            }, {
                "mace", "5"
            }, {
                "axe", "5"
            }, {
                "pickaxe", "5"
            }, {
                "Tzhaar-ket-em", "5"
            }, {
                "Torags hammers", "5"
            }, {
                "Guthans warspear", "5"
            }, {
                "Veracs flail", "5"
            }, {
                "Staff", "5"
            }, {
                "Staff of air", "5"
            }, {
                "Staff of water", "5"
            }, {
                "Staff of earth", "5"
            }, {
                "Staff of fire", "5"
            }, {
                "Magic staff", "5"
            }, {
                "Mystic fire staff", "5"
            }, {
                "Mystic fire staff", "5"
            }, {
                "Mystic water staff", "5"
            }, {
                "Mystic water staff", "5"
            }, {
                "Mystic air staff", "5"
            }, {
                "Mystic air staff", "5"
            }, {
                "Mystic earth staff", "5"
            }, {
                "Mystic earth staff", "5"
            }, {
                "Battlestaff", "5"
            }, {
                "Iban's staff", "5"
            }, {
                "Staff of light", "5"
            }, {
                "Salamander", "5"
            }, {
                "Maple longbow (sighted)", "5"
            }, {
                "Magic longbow (sighted)", "5"
            }, {
                "thrownaxe", "5"
            }, {
                "Comp ogre bow", "5"
            }, {
                "New crystal bow", "5"
            }, {
                "Crystal bow", "5"
            }, {
                "Seercull", "5"
            }, {
                "Chaotic longsword", "5"
            }, {
                "Gravite longsword", "5"
            }, {
                "Battleaxe", "6"
            }, {
                "warhammer", "6"
            }, {
                "godsword", "6"
            }, {
                "Barrelchest anchor", "6"
            }, {
                "Ahrims staff", "6"
            }, {
                "Toktz-mej-tal", "6"
            }, {
                "Gravite 2h sword", "6"
            }, {
                "Chaotic maul", "6"
            }, {
                "Longbow", "6"
            }, {
                "Zamorak bow", "6"
            }, {
                "Saradomin bow", "6"
            }, {
                "Guthix bow", "6"
            }, {
                "javelin", "6"
            }, {
                "Dorgeshuun c'bow", "6"
            }, {
                "Crossbow", "6"
            }, {
                "Zaryte bow", "6"
            }, {
                "Phoenix crossbow", "6"
            }, {
                "Sagaie", "6"
            }, {
                "Bolas", "6"
            }, {
                "Auspicious katana", "6"
            }, {
                "2h sword", "7"
            }, {
                "halberd", "7"
            }, {
                "Granite maul", "7"
            }, {
                "Balmung", "7"
            }, {
                "Tzhaar-ket-om", "7"
            }, {
                "Ivandis flail", "7"
            }, {
                "Hand cannon", "7"
            }, {
                "Dharoks greataxe", "7"
            }, {
                "Ogre bow", "8"
            }, {
                "Dark bow", "9"
            }, {
                "Dreadnip", "10"
            }, {
                "Swagger stick", "10"
            }
        };
        for (int i = 0; i < getDelay.length; i++) {
            if (s.contains(getDelay[i][0].toLowerCase().replaceAll("_", " "))) {
                get = Integer.parseInt(getDelay[i][1]);
            }
        }
        if (c.usingMagic) {
            switch (c.MAGIC_SPELLS[c.spellId][0]) {
                case 12871:
                    // ice blitz
                case 13023:
                    // shadow barrage
                case 12891:
                    // ice barrage
                    get = 5;

                default:
                    get = 6;
            }
        }
        return get;
    }

    /*public int getAttackDelay(String s) {
		if(c.usingMagic) {
			switch(c.MAGIC_SPELLS[c.spellId][0]) {
				case 12871: // ice blitz
				case 13023: // shadow barrage
				case 12891: // ice barrage
				return 5;
				
				default:
				return 5;
			}
		}
		if(c.playerEquipment[c.playerWeapon] == -1)
			return 4;//unarmed
			
		switch (c.playerEquipment[c.playerWeapon]) {
			case 15039:
			case 15585:
			return 7;
		case 9185:
			case 18357:
			if (c.fightMode == 2) {
			return 6; }
		case 13022:
			return 9;
			case 18349:
			case 15662:
			return 4;
			case 15574:
			
			case 15038:
			return 5;
			case 13883:
			case 13879:
			case 15403:
			case 18353:
			return 6;
			case 4151:
			case 15701:
			case 15702:
			case 15703:
			case 15704:			
			return 9;
			case 11730:
			return 4;
			case 6528:
			
			return 7;
		}
		
		if(s.endsWith("greataxe"))
			return 7;
		else if(s.equals("torags hammers"))
			return 5;
		else if(s.equals("guthans warspear"))
			return 5;
		else if(s.equals("veracs flail"))
			return 5;
			else if(s.contains("anchor"))
			return 7;
		else if(s.equals("ahrims staff"))
			return 6;
		else if(s.contains("staff")){
			if(s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer") || s.contains("ancient"))
				return 4;
			else
				return 5;
		} else if(s.contains("bow")){
			if(s.contains("composite") || s.equals("seercull"))
				return 5;
			else if (s.contains("aril"))
				return 4;
			else if(s.contains("Ogre"))
				return 8;
			else if(s.contains("short") || s.contains("hunt") || s.contains("sword"))
				return 4;
			else if(s.contains("long") || s.contains("crystal"))
				return 6;
			else if(s.contains("'bow"))
				return 7;
			
			return 5;
		}
		else if(s.contains("dagger"))
			return 4;
		else if(s.contains("godsword") || s.contains("2h"))
			return 6;
		else if(s.contains("longsword"))
			return 5;
		else if(s.contains("sword"))
			return 4;
		else if(s.contains("scimitar"))
			return 4;
		else if(s.contains("mace"))
			return 5;
		else if(s.contains("battleaxe"))
			return 6;
		else if(s.contains("pickaxe"))
			return 5;
		else if(s.contains("thrownaxe"))
			return 5;
		else if(s.contains("axe"))
			return 5;
		else if(s.contains("warhammer"))
			return 6;
		else if(s.contains("2h"))
			return 7;
		else if(s.contains("spear"))
			return 5;
		else if(s.contains("claw"))
			return 4;
		else if(s.contains("halberd"))
			return 7;
		
		//sara sword, 2400ms
		else if(s.equals("granite maul"))
			return 7;
		else if(s.equals("toktz-xil-ak"))//sword
			return 4;
		else if(s.equals("tzhaar-ket-em"))//mace
			return 5;
		else if(s.equals("tzhaar-ket-om"))//maul
			return 7;
		
		else if(s.equals("toktz-xil-ek"))//knife
			return 4;
		else if(s.equals("toktz-xil-ul"))//rings
			return 4;
		else if(s.equals("toktz-mej-tal"))//staff
			return 6;
		else if(s.contains("whip"))
			return 4;
		
		else if(s.contains("dart"))
			return 3;
		else if(s.contains("knife"))
			return 3;
		else if(s.contains("javelin"))
			return 6;
		return 5;
	}*/

    /**
     * How long it takes to hit your enemy
     **/
    public int getHitDelay(String weaponName) {
        if (c.usingMagic) {
            switch (c.MAGIC_SPELLS[c.spellId][0]) {
                case 12891:
                    return 4;
                case 12871:
                    return 6;
                default:
                    return 4;
            }
        } else {

            if (weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin") || weaponName.contains("thrownaxe") || c.playerEquipment[c.playerWeapon] == 13879 || c.playerEquipment[c.playerWeapon] == 13883) {
                return 3;
            }
            if (weaponName.contains("cross") || weaponName.contains("c'bow") || weaponName.contains("cannon")) {
                return 4;
            }
            if (weaponName.contains("bow") && !c.dbowSpec) {
                return 4;
            } else if (c.dbowSpec) {
                return 4;
            }

            switch (c.playerEquipment[c.playerWeapon]) {
                case 6522:
                    // Toktz-xil-ul
                    return 3;


                default:
                    return 2;
            }
        }
    }

    public int getRequiredDistance() {

        if (c.followId > 0 && c.freezeTimer <= 0 && !c.isMoving) return 2;
        else if (c.followId > 0 && c.freezeTimer <= 0 && c.isMoving) {
            return 3;
        } else {
            return 1;
        }
    }

    public boolean usingHally() {
        switch (c.playerEquipment[c.playerWeapon]) {
            case 3190:
            case 3192:
            case 3194:
            case 3196:
            case 3198:
            case 3200:
            case 3202:
            case 3204:
                return true;

            default:
                return false;
        }
    }

    /**
     * Melee
     **/

    public int calculateMeleeAttack() {
        int attackLevel = c.playerLevel[0];
        if (c.slayerHelmetEffect && c.slayerTask != 0) attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
        if (c.prayerActive[2]) {
            attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.05;
        } else if (c.prayerActive[7]) {
            attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.1;
        } else if (c.prayerActive[15]) {
            attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
        } else if (c.prayerActive[24]) {
            attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
        } else if (c.prayerActive[25]) {
            attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.2;
        } else if (c.prayerActive[26]) {
            attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.25;
        }
        if (c.fullVoidMelee()) {
            attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.56;
        }
        attackLevel *= c.specAccuracy;
        int i = c.playerBonus[bestMeleeAtk()];
        i += c.bonusAttack;
        if (c.playerEquipment[c.playerAmulet] == 11128 && c.playerEquipment[c.playerWeapon] == 6528) {
            i *= 1.30;
        }
        if (c.playerEquipment[c.playerWeapon] == 15403) {
            i *= 1.39;
        }
        if (c.playerEquipment[c.playerWeapon] == 15060) {
            i *= 1.30;
        }
        return (int)(attackLevel + (attackLevel * 0.60) + (i + i * 0.05));
    }
    public int bestMeleeAtk() {
        if (c.playerBonus[0] > c.playerBonus[1] && c.playerBonus[0] > c.playerBonus[2]) return 0;
        if (c.playerBonus[1] > c.playerBonus[0] && c.playerBonus[1] > c.playerBonus[2]) return 1;
        return c.playerBonus[2] <= c.playerBonus[1] || c.playerBonus[2] <= c.playerBonus[0] ? 0 : 2;
    }

    public int calculateMeleeMaxHit() {
        double maxHit = 0;
        int strBonus = c.playerBonus[10];
        int strength = c.playerLevel[2];
        int lvlForXP = c.getLevelForXP(c.playerXP[2]);
        if (c.slayerHelmetEffect && c.slayerTask != 0) maxHit = (int)(maxHit * 1.15);
        if (c.prayerActive[1]) {
            strength += (int)(lvlForXP * .05);
        } else if (c.prayerActive[6]) {
            strength += (int)(lvlForXP * .10);
        } else if (c.prayerActive[14]) {
            strength += (int)(lvlForXP * .15);
        } else if (c.prayerActive[24]) {
            strength += (int)(lvlForXP * .18);
        } else if (c.prayerActive[25]) {
            strength += (int)(lvlForXP * .23);
        } else if (c.prayerActive[26]) {
            strength += (int)(lvlForXP * .26);
        }
        if (c.playerEquipment[c.playerHat] == 2526 && c.playerEquipment[c.playerChest] == 2520 && c.playerEquipment[c.playerLegs] == 2522) {
            maxHit += (maxHit * 10 / 100);
        }
        maxHit += 1.05D + (double)(strBonus * strength) * 0.00175D;
        maxHit += (double) strength * 0.11D;
        if (c.playerEquipment[c.playerWeapon] == 4718 && c.playerEquipment[c.playerHat] == 4716 && c.playerEquipment[c.playerChest] == 4720 && c.playerEquipment[c.playerLegs] == 4722) {
            maxHit += (c.getPA().getLevelForXP(c.playerXP[3]) - c.playerLevel[3]) / 2;

        }
        if (c.specDamage > 1) maxHit = (int)(maxHit * c.specDamage);
        if (maxHit < 0) maxHit = 1;
        if (c.fullVoidMelee()) maxHit = (int)(maxHit * 1.10);
        if (c.playerEquipment[c.playerAmulet] == 11128 && c.playerEquipment[c.playerWeapon] == 6528) {
            maxHit *= 1.20;
        }
        if (c.playerEquipment[c.playerWeapon] == 15060) {
            maxHit *= 1.30;
        }
        if (c.playerEquipment[c.playerWeapon] == 15403) {
            maxHit *= 1.29;
        }
        return (int) Math.floor(maxHit);
    }


    public int calculateMeleeDefence() {
        int defenceLevel = c.playerLevel[1];
        int i = c.playerBonus[bestMeleeDef()];
        if (c.prayerActive[0]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
        } else if (c.prayerActive[5]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
        } else if (c.prayerActive[13]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
        } else if (c.prayerActive[24]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
        } else if (c.prayerActive[25]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
        } else if (c.prayerActive[26]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.28;
        }
        return (int)(defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
    }

    public int bestMeleeDef() {
        if (c.playerBonus[5] > c.playerBonus[6] && c.playerBonus[5] > c.playerBonus[7]) return 5;
        if (c.playerBonus[6] > c.playerBonus[5] && c.playerBonus[6] > c.playerBonus[7]) return 6;
        return c.playerBonus[7] <= c.playerBonus[5] || c.playerBonus[7] <= c.playerBonus[6] ? 5 : 7;
    }

    /**
     * Range
     **/

    public int calculateRangeAttack() {
        int attackLevel = c.playerLevel[4];
        attackLevel *= c.specAccuracy;
        if (c.fullVoidRange()) attackLevel += c.getLevelForXP(c.playerXP[c.playerRanged]) * 0.1;
        if (c.prayerActive[3]) attackLevel *= 1.05;
        else if (c.prayerActive[11]) attackLevel *= 1.10;
        else if (c.prayerActive[19]) attackLevel *= 1.15;
        //dbow spec
        if (c.playerEquipment[c.playerAmulet] == 15048) {
            attackLevel *= 1.01;
        }
        if (c.fullVoidRange() && c.specAccuracy > 1.35) {
            attackLevel *= 1.80;
        }
        return (int)(attackLevel + (c.playerBonus[4] * 1.95));
    }

    public int calculateRangeDefence() {
        int defenceLevel = c.playerLevel[1];
        if (c.prayerActive[0]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
        } else if (c.prayerActive[5]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
        } else if (c.prayerActive[13]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
        } else if (c.prayerActive[24]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
        } else if (c.prayerActive[25]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
        } else if (c.prayerActive[26]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.28;
        }
        return (int)(defenceLevel + c.playerBonus[9] + (c.playerBonus[9] / 2));
    }
    public boolean usingBolts() {
        return c.playerEquipment[c.playerArrows] >= 9130 && c.playerEquipment[c.playerArrows] <= 9145 || c.playerEquipment[c.playerArrows] >= 9230 && c.playerEquipment[c.playerArrows] <= 9245;
    }
    public boolean usingHandcannon() {
        return c.playerEquipment[c.playerArrows] == 7119;
    }
    public int rangeMaxHit() {
        int rangeLevel = c.playerLevel[4];
        double modifier = 1.0;
        double wtf = c.specDamage;
        int itemUsed = c.usingBow ? c.lastArrowUsed : c.lastWeaponUsed;
        if (c.prayerActive[3]) modifier += 0.05;
        else if (c.prayerActive[11]) modifier += 0.10;
        else if (c.prayerActive[19]) modifier += 0.15;
        if (c.playerEquipment[c.playerAmulet] == 15048) modifier += 0.11;
        if (c.playerEquipment[c.playerAmulet] == 18357) modifier += 0.11;

        if (c.fullVoidRange()) modifier += 0.22;
        double c = modifier * rangeLevel;
        int rangeStr = getRangeStr(itemUsed);
        double max = (c + 8) * (rangeStr + 64) / 640;

        if (wtf != 1) max *= wtf;
        if (max < 1) max = 1;
        return (int) max;
    }

    public int getRangeStr(int i) {
        if (i == 13022) return 150;
        if (i == 4214) return 90;
        if (i == 18357) return 150;
        switch (i) {
            //  bolts  \\
            case 877:
                //bronze bolts
                return 10;
            case 9140:
                //iron bolts
                return 46;
            case 9141:
                //steel bolts
                return 64;
            case 9142:
                //mith bolts
                return 82;
            case 9241:
                //emerald bolts e
                return 85;
            case 9240:
                //saphhire bolts e
                return 83;
            case 9143:
                //addy bolts
                return 100;
            case 9243:
                //diamond bolts e
                return 105;
            case 9242:
                //ruby bolts e
                return 103;
            case 11277:
                // dragon arrow (P)
                return 120;
            case 9144:
                //rune bolts
                return 115;
            case 9244:
                //dragon bolts e
                return 117;
            case 9245:
                //onxy bolts e
                return 120;
                //  arrows  \\
            case 882:
                //bronze
                return 7;
            case 13883:
                return 150;

            case 13879:
                return 130;
            case 7119:
                return 150;
            case 884:
                //iron
                return 4;
            case 886:
                //steel
                return 16;
            case 888:
                //mithril
                return 22;
            case 890:
                //adamant
                return 31;
            case 892:
                //rune
                return 49;
            case 4740:
                //bolt rack
                return 55;
            case 11212:
                //dragon arrow
                return 60;
                //  knifes  \\
            case 864:
                //bronze
                return 3;
            case 863:
                //iron
                return 4;
            case 865:
                //steel
                return 7;
            case 869:
                //black
                return 8;
            case 866:
                //mithril
                return 10;
            case 867:
                //adamant
                return 14;
            case 868:
                //rune
                return 24;
                //  darts \\
            case 806:
                //bronze
                return 1;
            case 807:
                //iron
                return 3;
            case 808:
                //steel
                return 4;
            case 3093:
                //black
                return 6;
            case 809:
                //mithril
                return 7;
            case 810:
                //adamant
                return 10;
            case 814:
                //rune
                return 14;
            case 11230:
                //dragon
                return 20;
                //  thrownaxes  \\
            case 800:
                //bronze
                return 5;
            case 801:
                //iron
                return 7;
            case 802:
                //steel
                return 11;
            case 803:
                //mithril
                return 16;
            case 804:
                //adamant
                return 23;
            case 805:
                //rune
                return 36;
                //		case ####://morrigan throw axe
                //		return 200;
                //  javelins  \\
            case 825:
                //bronze
                return 6;
            case 826:
                //iron
                return 10;
            case 827:
                //steel
                return 12;
            case 828:
                //mithril
                return 18;
            case 829:
                //adamant
                return 28;
            case 830:
                //rune
                return 42;
                //		case ####://morrigan javelin
                //		return 200;
        }
        return 0;
    }
    /*
		
	public int rangeMaxHit() {
        int rangehit = 0;
        rangehit += c.playerLevel[4] / 7.5;
        int weapon = c.lastWeaponUsed;
        int Arrows = c.lastArrowUsed;
        if (weapon == 4223) {//Cbow 1/10
            rangehit = 2;
            rangehit += c.playerLevel[4] / 7;
        } else if (weapon == 4222) {//Cbow 2/10
            rangehit = 3;
            rangehit += c.playerLevel[4] / 7;
        } else if (weapon == 4221) {//Cbow 3/10
            rangehit = 3;
            rangehit += c.playerLevel[4] / 6.5;
        } else if (weapon == 4220) {//Cbow 4/10
            rangehit = 4;
            rangehit += c.playerLevel[4] / 6.5;
        } else if (weapon == 4219) {//Cbow 5/10
            rangehit = 4;
            rangehit += c.playerLevel[4] / 4;
		} else if (weapon == 13022) {//Cbow 5/10
            rangehit = 500;
            rangehit += c.playerLevel[4] / 10;
        } else if (weapon == 4218) {//Cbow 6/10
            rangehit = 5;
            rangehit += c.playerLevel[4] / 6;
        } else if (weapon == 4217) {//Cbow 7/10
            rangehit = 5;
            rangehit += c.playerLevel[4] / 5.5;
        } else if (weapon == 4216) {//Cbow 8/10
            rangehit = 6;
            rangehit += c.playerLevel[4] / 5.5;
        } else if (weapon == 4215) {//Cbow 9/10
            rangehit = 6;
            rangehit += c.playerLevel[4] / 5;
        } else if (weapon == 4214) {//Cbow Full
            rangehit = 7;
            rangehit += c.playerLevel[4] / 5;
        } else if (weapon == 6522) {
            rangehit = 5;
            rangehit += c.playerLevel[4] / 6;
        } else if (weapon == 11230) {//dragon darts
            rangehit = 8;
            rangehit += c.playerLevel[4] / 8;
	 } else if (weapon == 13883) {//morrigan throwing axe
            rangehit = 15;
            rangehit += c.playerLevel[4] / 6;
	 } else if (weapon == 13879) {//morrigan javelin
            rangehit = 18;
            rangehit += c.playerLevel[4] / 5.5;
        } else if (weapon == 811 || weapon == 868) {//rune darts
            rangehit = 2;
            rangehit += c.playerLevel[4] / 8.5;
	
        } else if (weapon == 810 || weapon == 867) {//adamant darts
            rangehit = 2;
            rangehit += c.playerLevel[4] / 9;
        } else if (weapon == 809 || weapon == 866) {//mithril darts
            rangehit = 2;
            rangehit += c.playerLevel[4] / 9.5;
        } else if (weapon == 808 || weapon == 865) {//Steel darts
            rangehit = 2;
            rangehit += c.playerLevel[4] / 10;
        } else if (weapon == 807 || weapon == 863) {//Iron darts
            rangehit = 2;
            rangehit += c.playerLevel[4] / 10.5;
        } else if (weapon == 806 || weapon == 864) {//Bronze darts
            rangehit = 1;
            rangehit += c.playerLevel[4] / 11;
        } else if (Arrows == 884 && weapon == 4734) {//BoltRacks
			rangehit = 3;
            rangehit += c.playerLevel[4] / 6;
		} else if (Arrows == 7119 && weapon == 13022) {//BoltRacks
			rangehit = 3;
            rangehit += c.playerLevel[4] / 6;
        } else if (Arrows == 11212) {//dragon arrows
            rangehit = 4;
            rangehit += c.playerLevel[4] / 5.5;
        } else if (Arrows == 892) {//rune arrows
            rangehit = 3;
            rangehit += c.playerLevel[4] / 6;
        } else if (Arrows == 890) {//adamant arrows
            rangehit = 2;
            rangehit += c.playerLevel[4] / 7;
        } else if (Arrows == 888) {//mithril arrows
            rangehit = 2;
            rangehit += c.playerLevel[4] / 7.5;
        } else if (Arrows == 886) {//steel arrows
            rangehit = 2;
            rangehit += c.playerLevel[4] / 8;
       
        } else if (Arrows == 882) {//Bronze arrows
            rangehit = 1;
            rangehit += c.playerLevel[4] / 9.5;
        } else if (Arrows == 9244) {
			rangehit = 8;
			rangehit += c.playerLevel[4] / 3;
		} else if (Arrows == 9139) {
			rangehit = 12;
			rangehit += c.playerLevel[4] / 4;
		} else if (Arrows == 9140) {
			rangehit = 2;
            rangehit += c.playerLevel[4] / 7;
		} else if (Arrows == 9141) {
			rangehit = 3;
            rangehit += c.playerLevel[4] / 6;
		} else if (Arrows == 9142) {
			rangehit = 4;
            rangehit += c.playerLevel[4] / 6;
		} else if (Arrows == 9143) {
			rangehit = 7;
			rangehit += c.playerLevel[4] / 5;
		} else if (Arrows == 9144) {
			rangehit = 7;
			rangehit += c.playerLevel[4] / 4.5;
		}
        int bonus = 0;
        bonus -= rangehit / 10;
        rangehit += bonus;
        if (c.specDamage != 1)
			rangehit *= c.specDamage;
		if (rangehit == 0)
			rangehit++;
		if (c.fullVoidRange()) {
			rangehit *= 1.10;
		}
		if (c.prayerActive[3])
			rangehit *= 1.05;
		else if (c.prayerActive[11])
			rangehit *= 1.10;
		else if (c.prayerActive[19])
			rangehit *= 1.15;
		return rangehit;
    }*/

    public boolean properBolts() {
        return c.playerEquipment[c.playerArrows] >= 9140 && c.playerEquipment[c.playerArrows] <= 9144 || c.playerEquipment[c.playerArrows] >= 9240 && c.playerEquipment[c.playerArrows] <= 9244;
    }
    public boolean handCannon() {
        return c.playerEquipment[c.playerArrows] == 7119;
    }
    public int correctBowAndArrows() {

        if (usingBolts())

        return -1;

        if (usingHandcannon())

        return -1;
        switch (c.playerEquipment[c.playerWeapon]) {

            case 839:
            case 841:
                return 882;



            case 847:
            case 849:
                return 886;

            case 851:
            case 853:
                return 888;

            case 855:
            case 857:
                return 890;

            case 859:
            case 861:

                return 892;





            case 4734:
            case 4935:
            case 4936:
            case 4937:
                return 4740;

            case 11235:
            case 15701:
            case 15702:
            case 15703:
            case 15704:
                return 11212;
        }
        return -1;
    }

    public int getRangeStartGFX() {
        if (c.playerEquipment[c.playerWeapon] == 13022) return 2141;
        switch (c.rangeItemUsed) {

            case 863:
                return 220;
            case 864:
                return 219;
            case 865:
                return 221;
            case 866:
                // knives
                return 223;
            case 867:
                return 224;
            case 868:
                return 225;
            case 869:
                return 222;

            case 806:
                return 232;
            case 807:
                return 233;
            case 808:
                return 234;
            case 809:
                // darts
                return 235;
            case 810:
                return 236;
            case 811:
            case 11230:
                return 237;

            case 825:
                return 206;
            case 826:
                return 207;
            case 827:
                // javelin
                return 208;
            case 828:
                return 209;
            case 829:
                return 210;
            case 830:
                return 211;

            case 800:
                return 42;
            case 801:
                return 43;
            case 802:
                return 44; // axes
            case 803:
                return 45;
            case 804:
                return 46;
            case 805:
                return 48;

            case 882:
                return 19;

            case 884:
                return 18;

            case 886:
                return 20;

            case 888:
                return 21;

            case 890:
                return 22;

            case 892:
                return 24;

            case 11212:
                return 26;

            case 4212:
            case 4214:
            case 4215:
            case 4216:
            case 4217:
            case 4218:
            case 4219:
            case 4220:
            case 4221:
            case 4222:
            case 4223:
                return 250;

        }
        return -1;
    }

    public int getRangeProjectileGFX() {
        if (c.dbowSpec) {
            return 1099;
        }
        if (c.bowSpecShot > 0) {
            switch (c.rangeItemUsed) {
                default: return 249;
            }
        }
        if (c.playerEquipment[c.playerWeapon] == 9185) return 27;
        if (c.playerEquipment[c.playerWeapon] == 18357) return 27;
        if (c.playerEquipment[c.playerWeapon] == 15241) return 2143;
        if (c.playerEquipment[c.playerWeapon] == 13879) return 1837;
        if (c.playerEquipment[c.playerWeapon] == 13957) return 1839;
        switch (c.rangeItemUsed) {
            case 13883:
                return 1839;
            case 13879:
                return 1837;
            case 863:
                return 213;
            case 864:
                return 212;
            case 865:
                return 214;
            case 866:
                // knives
                return 216;
            case 867:
                return 217;
            case 868:
                return 218;
            case 869:
                return 215;

            case 806:
                return 226;
            case 807:
                return 227;
            case 808:
                return 228;
            case 809:
                // darts
                return 229;
            case 810:
                return 230;
            case 811:
            case 11230:
                return 231;

            case 825:
                return 200;
            case 826:
                return 201;
            case 827:
                // javelin
                return 202;
            case 828:
                return 203;
            case 829:
                return 204;
            case 830:
                return 205;

            case 6522:
                // Toktz-xil-ul
                return 442;

            case 800:
                return 36;
            case 801:
                return 35;
            case 802:
                return 37; // axes
            case 803:
                return 38;
            case 804:
                return 39;
            case 805:
                return 40;

            case 882:
                return 10;

            case 884:
                return 9;

            case 886:
                return 11;

            case 888:
                return 12;

            case 890:
                return 13;

            case 892:
                return 15;

            case 11212:
                return 17;

            case 4740:
                // bolt rack
                return 27;



            case 4212:
            case 4214:
            case 4215:
            case 4216:
            case 4217:
            case 4218:
            case 4219:
            case 4220:
            case 4221:
            case 4222:
            case 4223:
                return 249;


        }
        return -1;
    }

    public int getProjectileSpeed() {
        if (c.dbowSpec) return 100;
        return 70;
    }

    public int getProjectileShowDelay() {
        switch (c.playerEquipment[c.playerWeapon]) {
            case 863:
            case 864:
            case 865:
            case 866:
                // knives
            case 867:
            case 868:
            case 869:

            case 806:
            case 807:
            case 808:
            case 809:
                // darts
            case 810:
            case 811:
            case 11230:
            case 825:
            case 826:
            case 827:
                // javelin
            case 828:
            case 829:
            case 830:

            case 800:
            case 801:
            case 802:
            case 803:
                // axes
            case 804:
            case 805:

            case 4734:
            case 9185:
            case 18357:
            case 4935:
            case 4936:
            case 4937:
                return 15;


            default:
                return 5;
        }
    }

    /**
     *MAGIC
     **/

    public int mageAtk() {
        int attackLevel = c.playerLevel[6];
        int magicalAttack = 0;
        int magicalAttackBonus = (c.playerBonus[3] * 2);
        int magicalAttackLevel = (c.playerLevel[6] / 2);
        magicalAttack = magicalAttackLevel + magicalAttackBonus;
        if (c.fullVoidMage()) {
            magicalAttack += (magicalAttack * 60 / 100);

            return magicalAttack;
        }
        if (c.prayerActive[4]) attackLevel *= 1.05;
        else if (c.prayerActive[12]) attackLevel *= 1.10;
        else if (c.prayerActive[20]) attackLevel *= 1.15;
        return (int)(attackLevel + (c.playerBonus[3] * 2));
    }
    public int mageDef() {
        int defenceLevel = c.playerLevel[1] / 2 + c.playerLevel[6] / 2;
        if (c.prayerActive[0]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
        } else if (c.prayerActive[3]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
        } else if (c.prayerActive[9]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
        } else if (c.prayerActive[18]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
        } else if (c.prayerActive[19]) {
            defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
        }
        return (int)(defenceLevel + c.playerBonus[8] + (c.playerBonus[8] / 3));
    }

    public boolean wearingStaff(int runeId) {
        int wep = c.playerEquipment[c.playerWeapon];
        switch (runeId) {
            case 554:
                if (wep == 1387) return true;
                break;
            case 555:
                if (wep == 1383) return true;
                break;
            case 556:
                if (wep == 1381) return true;
                break;
            case 557:
                if (wep == 1385) return true;
                break;
        }
        return false;
    }

    public boolean checkMagicReqs(int spell) {
        if (c.usingMagic && Config.RUNES_REQUIRED) { // check for runes
            if ((!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][8], c.MAGIC_SPELLS[spell][9]) && !wearingStaff(c.MAGIC_SPELLS[spell][8])) || (!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][10], c.MAGIC_SPELLS[spell][11]) && !wearingStaff(c.MAGIC_SPELLS[spell][10])) || (!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][12], c.MAGIC_SPELLS[spell][13]) && !wearingStaff(c.MAGIC_SPELLS[spell][12])) || (!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][14], c.MAGIC_SPELLS[spell][15]) && !wearingStaff(c.MAGIC_SPELLS[spell][14]))) {
                c.sendMessage("You don't have the required runes to cast this spell.");
                return false;
            }
        }

        if (c.usingMagic && c.playerIndex > 0) {
            if (Server.playerHandler.players[c.playerIndex] != null) {
                for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing spells, confuse etc
                    if (Server.playerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == c.MAGIC_SPELLS[spell][0]) {
                        c.reduceSpellId = r;
                        if ((System.currentTimeMillis() - Server.playerHandler.players[c.playerIndex].reduceSpellDelay[c.reduceSpellId]) > Server.playerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[c.reduceSpellId]) {
                            Server.playerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = true;
                        } else {
                            Server.playerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = false;
                        }
                        break;
                    }
                }
                if (!Server.playerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId]) {
                    c.sendMessage("That player is currently immune to this spell.");
                    c.usingMagic = false;
                    c.stopMovement();
                    resetPlayerAttack();
                    return false;
                }
            }
        }

        int staffRequired = getStaffNeeded();
        if (c.usingMagic && staffRequired > 0 && Config.RUNES_REQUIRED) { // staff required
            if (c.playerEquipment[c.playerWeapon] != staffRequired) {
                c.sendMessage("You need a " + c.getItems().getItemName(staffRequired).toLowerCase() + " to cast this spell.");
                return false;
            }
        }

        if (c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) { // check magic level
            if (c.playerLevel[6] < c.MAGIC_SPELLS[spell][1]) {
                c.sendMessage("You need to have a magic level of " + c.MAGIC_SPELLS[spell][1] + " to cast this spell.");
                return false;
            }
        }
        if (c.usingMagic && Config.RUNES_REQUIRED) {
            if (c.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
                if (!wearingStaff(c.MAGIC_SPELLS[spell][8])) c.getItems().deleteItem(c.MAGIC_SPELLS[spell][8], c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][8]), c.MAGIC_SPELLS[spell][9]);
            }
            if (c.MAGIC_SPELLS[spell][10] > 0) {
                if (!wearingStaff(c.MAGIC_SPELLS[spell][10])) c.getItems().deleteItem(c.MAGIC_SPELLS[spell][10], c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][10]), c.MAGIC_SPELLS[spell][11]);
            }
            if (c.MAGIC_SPELLS[spell][12] > 0) {
                if (!wearingStaff(c.MAGIC_SPELLS[spell][12])) c.getItems().deleteItem(c.MAGIC_SPELLS[spell][12], c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][12]), c.MAGIC_SPELLS[spell][13]);
            }
            if (c.MAGIC_SPELLS[spell][14] > 0) {
                if (!wearingStaff(c.MAGIC_SPELLS[spell][14])) c.getItems().deleteItem(c.MAGIC_SPELLS[spell][14], c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][14]), c.MAGIC_SPELLS[spell][15]);
            }
        }
        return true;
    }


    public int getFreezeTime() {
        switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
            case 1572:
            case 12861:
                // ice rush
                return 10;

            case 1582:
            case 12881:
                // ice burst
                return 17;

            case 1592:
            case 12871:
                // ice blitz
                return 25;

            case 12891:
                // ice barrage
                return 33;

            default:
                return 0;
        }
    }

    public void freezePlayer(int i) {


    }

    public int getStartHeight() {
        switch (c.MAGIC_SPELLS[c.spellId][0]) {
            case 1562:
                // stun
                return 25;

            case 12939:
                // smoke rush
                return 35;

            case 12987:
                // shadow rush
                return 38;

            case 12861:
                // ice rush
                return 15;

            case 12951:
                // smoke blitz
                return 38;

            case 12999:
                // shadow blitz
                return 25;

            case 12911:
                // blood blitz
                return 25;

            default:
                return 43;
        }
    }



    public int getEndHeight() {
        switch (c.MAGIC_SPELLS[c.spellId][0]) {
            case 1562:
                // stun
                return 10;

            case 12939:
                // smoke rush
                return 20;

            case 12987:
                // shadow rush
                return 28;

            case 12861:
                // ice rush
                return 10;

            case 12951:
                // smoke blitz
                return 28;

            case 12999:
                // shadow blitz
                return 15;

            case 12911:
                // blood blitz
                return 10;

            default:
                return 31;
        }
    }

    public int getStartDelay() {
        switch (c.MAGIC_SPELLS[c.spellId][0]) {
            case 1539:
                return 60;

            default:
                return 53;
        }
    }

    public int getStaffNeeded() {
        switch (c.MAGIC_SPELLS[c.spellId][0]) {
            case 1539:
                return 1409;

            case 12037:
                return 4170;

            case 1190:
                return 2415;

            case 1191:
                return 2416;

            case 1192:
                return 2417;

            default:
                return 0;
        }
    }

    public boolean godSpells() {
        switch (c.MAGIC_SPELLS[c.spellId][0]) {
            case 1190:
                return true;

            case 1191:
                return true;

            case 1192:
                return true;

            default:
                return false;
        }
    }

    public int getEndGfxHeight() {
        switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
            case 12987:
            case 12901:
            case 12861:
            case 12445:
            case 1192:
            case 13011:
            case 12919:
            case 12881:
            case 12999:
            case 12911:
            case 12871:
            case 13023:
            case 12929:
            case 12891:
                return 0;

            default:
                return 100;
        }
    }

    public int getStartGfxHeight() {
        if (c.playerEquipment[c.playerWeapon] == 13022) return 0;
        switch (c.MAGIC_SPELLS[c.spellId][0]) {
            case 12871:
            case 12891:
                return 0;

            default:
                return 100;
        }
    }



    public void handleDfsNPC() {
        if (System.currentTimeMillis() - c.dfsDelay > 30000) {
            if (c.npcIndex > 0 && Server.npcHandler.npcs[c.npcIndex] != null) {
                int damage = Misc.random(15) + 5;
                c.startAnimation(2836);
                c.gfx0(600);
                Server.npcHandler.npcs[c.npcIndex].HP -= damage;
                Server.npcHandler.npcs[c.npcIndex].hitDiff2 = damage;
                Server.npcHandler.npcs[c.npcIndex].hitUpdateRequired2 = true;
                Server.npcHandler.npcs[c.npcIndex].updateRequired = true;
                c.dfsDelay = System.currentTimeMillis();
            } else {
                c.sendMessage("I should be in combat before using this.");
            }
        } else {
            c.sendMessage("My shield hasn't finished recharging yet.");
        }
    }
    public void handleDfs() {
        if (System.currentTimeMillis() - c.dfsDelay > 30000) {
            if (c.playerIndex > 0 && Server.playerHandler.players[c.playerIndex] != null) {
                int damage = Misc.random(15) + 5;
                c.startAnimation(6696);
                c.gfx0(1165);
                //c.gfx0(1166);
                Server.playerHandler.players[c.playerIndex].playerLevel[3] -= damage;
                Server.playerHandler.players[c.playerIndex].hitDiff2 = damage;
                Server.playerHandler.players[c.playerIndex].hitUpdateRequired2 = true;
                Server.playerHandler.players[c.playerIndex].updateRequired = true;
                c.dfsDelay = System.currentTimeMillis();
            } else {
                c.sendMessage("I should be in combat before using this.");
            }
        } else {
            c.sendMessage("My shield hasn't finished recharging yet.");
        }
    }

    public void applyRecoil(int damage, int i) {
        if (damage > 0 && Server.playerHandler.players[i].playerEquipment[c.playerRing] == 2550) {
            int recDamage = damage / 10 + 1;
            if (!c.getHitUpdateRequired()) {
                c.setHitDiff(recDamage);
                c.setHitUpdateRequired(true);
            } else if (!c.getHitUpdateRequired2()) {
                c.setHitDiff2(recDamage);
                c.setHitUpdateRequired2(true);
            }
            c.dealDamage(recDamage);
            c.updateRequired = true;
        }
    }

    public int getBonusAttack(int i) {
        switch (Server.npcHandler.npcs[i].npcType) {
            case 2883:
                return Misc.random(50) + 30;
            case 2026:
            case 2027:
            case 2029:
            case 2030:
                return Misc.random(50) + 30;
        }
        return 0;
    }



    public void handleGmaulPlayer() {
        if (c.playerIndex > 0) {
            Client o = (Client) Server.playerHandler.players[c.playerIndex];
            if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), getRequiredDistance())) {
                if (checkReqs()) {
                    if (checkSpecAmount(4153)) {
                        boolean hit = Misc.random(calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence());
                        int damage = 0;
                        if (hit) damage = Misc.random(calculateMeleeMaxHit());
                        if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500) damage *= .6;
                        o.handleHitMask(damage);
                        c.startAnimation(1667);
                        c.gfx100(340);
                        o.dealDamage(damage);
                    }
                }
            }
        }
    }

    public boolean armaNpc(int i) {
        switch (Server.npcHandler.npcs[i].npcType) {
            case 6232:
            case 6233:
            case 6234:
            case 6235:
            case 6236:
            case 6237:
            case 6238:
            case 6239:
            case 6240:
            case 6241:
            case 6242:
            case 6243:
            case 6244:
            case 6245:
            case 6246:
            case 6247:

            case 6222:
            case 6223:
            case 6225:
            case 6227:
                return true;
        }
        return false;
    }

}