package server.model.items;

import server.model.players.Client;
import server.model.content.*;
import server.util.Misc;
import server.Config;

public class UseItem {

	public static void ItemonObject(Client c, int objectID, int objectX, int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		switch(objectID) {
			case 2782:
				c.getSmithingInt().showSmithInterface(itemId);
			break;
			case 8151:
			case 8389:
				c.getFarming().checkItemOnObject(itemId);
			break;
			case 2728:
			case 12269:
				c.getCooking().itemOnObject(itemId);
			break;
			case 409:
				if (c.getPrayer().readBone(itemId))
					c.getPrayer().boneOnAlter(itemId);
			break;
		default:
			if(c.playerRights == 3)
				Misc.println("Player At Object id: "+objectID+" with Item id: "+itemId);
			break;
		}
		
	}

	public static void ItemonItem(Client c, int itemUsed, int useWith) {		
		if (c.getItems().getItemName(itemUsed).contains("(") && c.getItems().getItemName(useWith).contains("(")) {
			c.getPotMixing().potionCombination(itemUsed, useWith);
		}			
		if (itemUsed == 1733 || useWith == 1733)
			c.getCrafting().handleLeather(itemUsed, useWith);
		if (itemUsed == 1755 || useWith == 1755)
			c.getCrafting().handleChisel(itemUsed,useWith);
		if ((itemUsed == 1540 && useWith == 11286) || (itemUsed == 11286 && useWith == 1540)) {
			if (c.playerLevel[c.playerSmithing] >= 95) {
				c.getItems().deleteItem(1540, c.getItems().getItemSlot(1540), 1);
				c.getItems().deleteItem(11286, c.getItems().getItemSlot(11286), 1);
				c.getItems().addItem(11284,1);
				c.sendMessage("You combine the two materials to create a dragonfire shield.");
				c.getPA().addSkillXP(500 * Config.SMITHING_EXPERIENCE, c.playerSmithing);
			} else {
				c.sendMessage("You need a smithing level of 95 to create a dragonfire shield.");
			}
		}
		if (itemUsed == 9142 && useWith == 9190 || itemUsed == 9190 && useWith == 9142) {
			if (c.playerLevel[c.playerFletching] >= 58) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith) ? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9241, boltsMade);
				c.getPA().addSkillXP(boltsMade * 6 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 58 to fletch this item.");
			}
		}
		if (itemUsed == 9143 && useWith == 9191 || itemUsed == 9191 && useWith == 9143) {
			if (c.playerLevel[c.playerFletching] >= 63) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith) ? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9242, boltsMade);
				c.getPA().addSkillXP(boltsMade * 7 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 63 to fletch this item.");
			}		
		}
		/****** EXTREMES AND OVERLOADS ********/

if (itemUsed == 261 && useWith == 145 || itemUsed == 145 && useWith == 261 || itemUsed == 147 && useWith == 261 || itemUsed == 149 && useWith == 261 || itemUsed == 2436 && useWith == 261) {
           if (c.playerLevel[c.playerHerblore] >= 88) {
            c.getItems().deleteItem(261, c.getItems().getItemSlot(261),1);
            c.getItems().deleteItem(145, c.getItems().getItemSlot(145),1);
            
            c.getItems().addItem(15309,1);
                c.sendMessage("You make a Overload Potion (4).");
                c.getPA().addSkillXP(230 * Config.HERBLORE_EXPERIENCE, c.playerHerblore);
            } else {
                c.sendMessage("You need a herblore level of 88 to make that potion.");
             
           }
        }
           
        

if (itemUsed == 267 && useWith == 157 || itemUsed == 157 && useWith == 267 || itemUsed == 2440 && useWith == 267 || itemUsed == 159 && useWith == 267 || itemUsed == 161 && useWith == 267) {
           if (c.playerLevel[c.playerHerblore] >= 89) {
            c.getItems().deleteItem(267, c.getItems().getItemSlot(267),1);
            c.getItems().deleteItem(157, c.getItems().getItemSlot(157),1);
            
            c.getItems().addItem(15313,1);
                c.sendMessage("You make a Extreme Strength potion (4).");
                c.getPA().addSkillXP(230 * Config.HERBLORE_EXPERIENCE, c.playerHerblore);
            } else {
                c.sendMessage("You need a herblore level of 89 to make that potion.");
             
           }
        }
            
        

if (itemUsed == 163 && useWith == 2481 || itemUsed == 2481 && useWith == 163 || itemUsed == 2481 && useWith == 165 || itemUsed == 2481 && useWith == 167 || itemUsed == 2481 && useWith == 2442) {
           if (c.playerLevel[c.playerHerblore] >= 90) {
            c.getItems().deleteItem(163, c.getItems().getItemSlot(163),1);
            c.getItems().deleteItem(145, c.getItems().getItemSlot(145),1);
            
            c.getItems().addItem(15317,1);
                c.sendMessage("You make an extreme defencee Potion (4).");
                c.getPA().addSkillXP(230 * Config.HERBLORE_EXPERIENCE, c.playerHerblore);
            } else {
                c.sendMessage("You need a herblore level of 90 to make that potion.");
            
             
            }
        }
        
if (itemUsed == 3042 && useWith == 9594 || itemUsed == 9594 && useWith == 3042 || itemUsed == 9594 && useWith == 3041 || itemUsed == 9594 && useWith == 3044 || itemUsed == 9594 && useWith == 3041) {
           if (c.playerLevel[c.playerHerblore] >= 91) {
            c.getItems().deleteItem(3042, c.getItems().getItemSlot(3042),1);
            c.getItems().deleteItem(9594, c.getItems().getItemSlot(9594),1);
            
            c.getItems().addItem(15321,1);
                c.sendMessage("You make an extreme magic Potion (4).");
                c.getPA().addSkillXP(230 * Config.HERBLORE_EXPERIENCE, c.playerHerblore);
            } else {
                c.sendMessage("You need a herblore level of 91 to make that potion.");
             
           }
        }
           
if (itemUsed == 169 && useWith == 12539 || itemUsed == 12539 && useWith == 169 || itemUsed == 12539 && useWith == 171 || itemUsed == 12539 && useWith == 173 || itemUsed == 12539 && useWith == 2444)  {
           if (c.playerLevel[c.playerHerblore] >= 92) {
            c.getItems().deleteItem(169, c.getItems().getItemSlot(169),1);
            c.getItems().deleteItem(12359, c.getItems().getItemSlot(12359),1);
            
            c.getItems().addItem(15325,1);
                c.sendMessage("You make an extreme magic Potion (4).");
                c.getPA().addSkillXP(230 * Config.HERBLORE_EXPERIENCE, c.playerHerblore);
            } else {
                c.sendMessage("You need a herblore level of 92 to make that potion.");
              
            }}
	


		
if (itemUsed == 269 && useWith == 15308 || itemUsed == 269 && useWith == 15312 || itemUsed == 269 && useWith == 15316 || itemUsed == 269 && useWith == 15320 || itemUsed == 269 && useWith == 15324) {
        if (c.getItems().playerHasItem(15308, 1) && c.getItems().playerHasItem(15312, 1) && c.getItems().playerHasItem(15316, 1) && c.getItems().playerHasItem(15320, 1) && c.getItems().playerHasItem(15324, 1)){
            if (c.playerLevel[c.playerHerblore] >= 96) {
            c.getItems().deleteItem(269, c.getItems().getItemSlot(269),1);
            c.getItems().deleteItem(15308, c.getItems().getItemSlot(15308),1);
            c.getItems().deleteItem(15312, c.getItems().getItemSlot(15312),1);
            c.getItems().deleteItem(15316, c.getItems().getItemSlot(15316),1);
            c.getItems().deleteItem(15320, c.getItems().getItemSlot(15320),1);
            c.getItems().deleteItem(15324, c.getItems().getItemSlot(15324),1);
            c.getItems().addItem(15332,1);
                c.sendMessage("You make a Overload Potion (4).");
                c.getPA().addSkillXP(500 * Config.HERBLORE_EXPERIENCE, c.playerHerblore);
            } else {
                c.sendMessage("You need a herblore level of 96 to make that potion.");
            }
            } else {
                //c.sendMessage("You need all extreme potions to make a Overload.");
            }
        }

	if (itemUsed >= 11710 && itemUsed <= 11714 && useWith >= 11710 && useWith <= 11714) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			}		
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368),1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366),1);
			c.getItems().addItem(1187,1);
		}
		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11690) {
				c.getItems().makeGodsword(hilt);
			}
		}

		if (itemUsed == 15025 && useWith == 15000 || itemUsed == 15000 && useWith == 15025) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15021,1);	
		}
		if (itemUsed == 15025 && useWith == 15001 || itemUsed == 15001 && useWith == 15025) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15023,1);	
		}
		if (itemUsed == 15025 && useWith == 15002 || itemUsed == 15002 && useWith == 15025) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15026,1);	
		}
		if (itemUsed == 15025 && useWith == 15003 || itemUsed == 15003 && useWith == 15025) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15022,1);	
		}
		if (itemUsed == 1767 && useWith == 4151 || itemUsed == 1767 && useWith == 4151) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15442,1);	
		}
		if (itemUsed == 1765 && useWith == 4151 || itemUsed == 1766 && useWith == 4151) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15441,1);	
		}
		if (itemUsed == 1771 && useWith == 4151 || itemUsed == 1771 && useWith == 4151) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15444,1);	
		}
		if (itemUsed == 5559 && useWith == 4151 || itemUsed == 5559 && useWith == 4151) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15443,1);	
		}
		if (itemUsed == 5559 && useWith == 11235 || itemUsed == 5559 && useWith == 11235) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15703,1);	
		}
		if (itemUsed == 1765 && useWith == 11235 || itemUsed == 1765 && useWith == 11235) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15701,1);	
		}
		if (itemUsed == 1771 && useWith == 11235 || itemUsed == 1771 && useWith == 11235) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15704,1);	
		}
		if (itemUsed == 1767 && useWith == 11235 || itemUsed == 1767 && useWith == 11235) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(15702,1);	
		}
		if (itemUsed == 13736 && useWith == 13746 || itemUsed == 13746 && useWith == 13736) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(13738,1);	
		}
		if (itemUsed == 13736 && useWith == 13748 || itemUsed == 13748 && useWith == 13736) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(13740,1);	
		}
		if (itemUsed == 13736 && useWith == 13750 || itemUsed == 13750 && useWith == 13736) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(13742,1);	
		}
		if (itemUsed == 13736 && useWith == 13752 || itemUsed == 13752 && useWith == 13736) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(13744,1);	
		}
		if (itemUsed == 12434 && useWith == 3751 || itemUsed == 3751 && useWith == 12434) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(12675,1);	
		}
		if (itemUsed == 12434 && useWith == 3753 || itemUsed == 3753 && useWith == 12434) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(12677,1);	
		}
		if (itemUsed == 12434 && useWith == 3755 || itemUsed == 3755 && useWith == 12434) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(12679,1);	
		}
		if (itemUsed == 12434 && useWith == 3749 || itemUsed == 3749 && useWith == 12434) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(12673,1);	
		}
		if (itemUsed == 12434 && useWith == 10828 || itemUsed == 10828 && useWith == 12434) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(12681,1);	
		}
		if (itemUsed == 12434 && useWith == 11718 || itemUsed == 11718 && useWith == 12434) {
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith),1);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed),1);
				c.getItems().addItem(12671,1);	
		}
		if (itemUsed == 9143 && useWith == 9192 || itemUsed == 9192 && useWith == 9143) {
			if (c.playerLevel[c.playerFletching] >= 65) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith) ? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9243, boltsMade);
				c.getPA().addSkillXP(boltsMade * 7 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 65 to fletch this item.");
			}		
		}
		if (itemUsed == 9144 && useWith == 9193 || itemUsed == 9193 && useWith == 9144) {
			if (c.playerLevel[c.playerFletching] >= 71) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith) ? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9244, boltsMade);
				c.getPA().addSkillXP(boltsMade * 10 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 71 to fletch this item.");
			}		
		}
		if (itemUsed == 9144 && useWith == 9194 || itemUsed == 9194 && useWith == 9144) {
			if (c.playerLevel[c.playerFletching] >= 58) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith) ? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9245, boltsMade);
				c.getPA().addSkillXP(boltsMade * 13 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 58 to fletch this item.");
			}		
		}
		if (itemUsed == 1601 && useWith == 1755 || itemUsed == 1755 && useWith == 1601) {
			if (c.playerLevel[c.playerFletching] >= 63) {
				c.getItems().deleteItem(1601, c.getItems().getItemSlot(1601), 1);
				c.getItems().addItem(9192, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 63 to fletch this item.");
			}
		}
		if (itemUsed == 1607 && useWith == 1755 || itemUsed == 1755 && useWith == 1607) {
			if (c.playerLevel[c.playerFletching] >= 65) {
				c.getItems().deleteItem(1607, c.getItems().getItemSlot(1607), 1);
				c.getItems().addItem(9189, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 65 to fletch this item.");
			}
		}
		if (itemUsed == 1605 && useWith == 1755 || itemUsed == 1755 && useWith == 1605) {
			if (c.playerLevel[c.playerFletching] >= 71) {
				c.getItems().deleteItem(1605, c.getItems().getItemSlot(1605), 1);
				c.getItems().addItem(9190, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 71 to fletch this item.");
			}
		}
		if (itemUsed == 1603 && useWith == 1755 || itemUsed == 1755 && useWith == 1603) {
			if (c.playerLevel[c.playerFletching] >= 73) {
				c.getItems().deleteItem(1603, c.getItems().getItemSlot(1603), 1);
				c.getItems().addItem(9191, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		if (itemUsed == 1615 && useWith == 1755 || itemUsed == 1755 && useWith == 1615) {
			if (c.playerLevel[c.playerFletching] >= 73) {
				c.getItems().deleteItem(1615, c.getItems().getItemSlot(1615), 1);
				c.getItems().addItem(9193, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		if (itemUsed >= 11710 && itemUsed <= 11714 && useWith >= 11710 && useWith <= 11714) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			}		
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368),1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366),1);
			c.getItems().addItem(1187,1);
		}
		
		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11690) {
				c.getItems().makeGodsword(hilt);
			}
		}
		
		switch(itemUsed) {
		case 1511:
		case 1521:
		case 1519:
		case 1517:
		case 1515:
		case 1513:
		case 590:
			c.getFiremaking().checkLogType(itemUsed, useWith);
			//c.sendMessage("Firemaking is disabled.");
		break;
			
		default:
			if(c.playerRights == 3)
				Misc.println("Player used Item id: "+itemUsed+" with Item id: "+useWith);
			break;
		}	
	}
	public static void ItemonNpc(Client c, int itemId, int npcId, int slot) {
		switch(itemId) {
		
		default:
			if(c.playerRights == 3)
				Misc.println("Player used Item id: "+itemId+" with Npc id: "+npcId+" With Slot : "+slot);
			break;
		}
		
	}


}
