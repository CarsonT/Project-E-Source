package server.world;

import java.util.ArrayList;

import server.model.objects.Object;
import server.util.Misc;
import server.model.players.Client;
import server.Server;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();
	public void process() {
		for (Object o : objects) {
			if (o.tick > 0)
				o.tick--;
			else {
				updateObject(o);
				toRemove.add(o);
			}		
		}
		for (Object o : toRemove) {
			if (isObelisk(o.newId)) {
				int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);	
		}
		toRemove.clear();
	}
	
	public void removeObject(int x, int y) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				c.getPA().object(-1, x, y, 0, 10);			
			}	
		}	
	}
	
	public void updateObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);			
			}	
		}	
	}
	
	public void placeObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}	
		}
	}
	
	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}	
		return null;
	}
	
	public void loadObjects(Client c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o,c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
		}
		loadCustomSpawns(c);
		if (c.distanceToPoint(2813, 3463) <= 60) {
			c.getFarming().updateHerbPatch();
		}
	}
	
	private int[][] customObjects = {{}};
	public void loadCustomSpawns(Client c) {
		/** Donator Zone **/
		c.getPA().checkObjectSpawn(-1, 3219, 9623, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3216, 9618, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3216, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3217, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3218, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3219, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3219, 9616, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3219, 9618, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3219, 9617, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3219, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3218, 9615, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3217, 9615, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3211, 9615, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3210, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3211, 9616, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3211, 9617, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3211, 9618, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3213, 9617, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3213, 9618, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3213, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3213, 9621, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3214, 9621, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3213, 9622, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3214, 9623, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3219, 9622, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3216, 9621, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3217, 9622, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3215, 9625, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3212, 9625, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3211, 9625, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3209, 9623, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3208, 9623, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3209, 9619, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3209, 9616, 0, 10);
		c.getPA().checkObjectSpawn(14367, 3216, 9625, 0, 10); //Bank Booth
		c.getPA().checkObjectSpawn(14367, 3215, 9625, 0, 10); //Bank Booth
		c.getPA().checkObjectSpawn(14367, 3214, 9625, 0, 10); //Bank Booth
		c.getPA().checkObjectSpawn(14367, 3213, 9625, 0, 10); //Bank Booth
		c.getPA().checkObjectSpawn(14367, 3212, 9625, 0, 10); //Bank Booth
		c.getPA().checkObjectSpawn(14367, 3211, 9625, 0, 10); //Bank Booth
		c.getPA().checkObjectSpawn(2465, 3219, 9617, 0, 10); //Portal
		/** End of Donator Zone **/
		/** SKilling Zone **/
	    c.getPA().checkObjectSpawn(2090, 2321, 3689, 0, 10); //Copper Ore           
        c.getPA().checkObjectSpawn(2090, 2321, 3688, 0, 10); //Copper Ore       
        c.getPA().checkObjectSpawn(2090, 2321, 3687, 0, 10); //Copper Ore   
        c.getPA().checkObjectSpawn(2090, 2321, 3686, 0, 10); //Copper Ore   
        c.getPA().checkObjectSpawn(2094, 2321, 3685, 0, 10); //Tin Ore  
        c.getPA().checkObjectSpawn(2094, 2321, 3684, 0, 10); //Tin Ore  
        c.getPA().checkObjectSpawn(2094, 2321, 3683, 0, 10); //Tin Ore      
        c.getPA().checkObjectSpawn(2094, 2321, 3682, 0, 10); //Tin Ore      
        c.getPA().checkObjectSpawn(2093, 2321, 3681, 0, 10); //Iron Ore 
        c.getPA().checkObjectSpawn(2093, 2321, 3680, 0, 10); //Iron Ore 
        c.getPA().checkObjectSpawn(2093, 2321, 3679, 0, 10); //Iron Ore     
        c.getPA().checkObjectSpawn(2093, 2321, 3678, 0, 10); //Iron Ore                             
        c.getPA().checkObjectSpawn(2097, 2316, 3689, 0, 10); //Coal Ore 
        c.getPA().checkObjectSpawn(2097, 2316, 3688, 0, 10); //Coal Ore 
        c.getPA().checkObjectSpawn(2097, 2316, 3687, 0, 10); //Coal Ore     
        c.getPA().checkObjectSpawn(2097, 2316, 3686, 0, 10); //Coal Ore         
        c.getPA().checkObjectSpawn(2099, 2316, 3685, 0, 10); //Gold Ore
        c.getPA().checkObjectSpawn(2099, 2316, 3684, 0, 10); //Gold Ore
        c.getPA().checkObjectSpawn(2102, 2316, 3683, 0, 10); //Mith Ore     
        c.getPA().checkObjectSpawn(2102, 2316, 3682, 0, 10); //Mith Ore 
        c.getPA().checkObjectSpawn(2105, 2316, 3681, 0, 10); //Addy Ore
        c.getPA().checkObjectSpawn(2105, 2316, 3680, 0, 10); //Addy Ore
        c.getPA().checkObjectSpawn(14859, 2316, 3679, 0, 10); //Rune Ore        
        c.getPA().checkObjectSpawn(14859, 2316, 3678, 0, 10); //Rune Ore    
        c.getPA().checkObjectSpawn(2782, 2318, 3690, 0, 10); //Anvil    
        c.getPA().checkObjectSpawn(2782, 2320, 3690, 0, 10); //Anvil
        c.getPA().checkObjectSpawn(3044, 2315, 3694, 0, 10); //Furnace  
        c.getPA().checkObjectSpawn(1306, 2343, 3664, 0, 10); //Magic tree
        c.getPA().checkObjectSpawn(1306, 2341, 3664, 0, 10); //Magic tree
        c.getPA().checkObjectSpawn(1306, 2339, 3664, 0, 10); //Magic tree
        c.getPA().checkObjectSpawn(1306, 2337, 3664, 0, 10); //Magic tree
        c.getPA().checkObjectSpawn(1306, 2335, 3664, 0, 10); //Magic tree
        c.getPA().checkObjectSpawn(1309, 2343, 3669, 0, 10); //Yew tree
        c.getPA().checkObjectSpawn(1309, 2341, 3669, 0, 10); //Yew tree
        c.getPA().checkObjectSpawn(1309, 2339, 3669, 0, 10); //Yew tree
        c.getPA().checkObjectSpawn(1309, 2337, 3669, 0, 10); //Yew tree
        c.getPA().checkObjectSpawn(1309, 2335, 3669, 0, 10); //Yew tree     
        c.getPA().checkObjectSpawn(1307, 2334, 3674, 0, 10); //Maple tree
        c.getPA().checkObjectSpawn(1307, 2332, 3674, 0, 10); //Maple tree
        c.getPA().checkObjectSpawn(1307, 2330, 3674, 0, 10); //Maple tree       
        c.getPA().checkObjectSpawn(1308, 2328, 3674, 0, 10); //Willow tree
        c.getPA().checkObjectSpawn(1308, 2326, 3674, 0, 10); //Willow tree
        c.getPA().checkObjectSpawn(1308, 2324, 3674, 0, 10); //Willow tree      
        c.getPA().checkObjectSpawn(1281, 2322, 3674, 0, 10); //Oak tree
        c.getPA().checkObjectSpawn(1281, 2320, 3674, 0, 10); //Oak tree 
        c.getPA().checkObjectSpawn(1281, 2318, 3674, 0, 10); //Oak tree 
        c.getPA().checkObjectSpawn(1276, 2316, 3674, 0, 10); //Tree
        c.getPA().checkObjectSpawn(1276, 2314, 3674, 0, 10); //Tree
        c.getPA().checkObjectSpawn(1276, 2312, 3674, 0, 10); //Tree     
        c.getPA().checkObjectSpawn(14367, 2327, 3686, -1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 2327, 3687, -1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 2327, 3688, -1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 2327, 3689, -1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 2327, 3690, -1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 2327, 3691, -1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 2327, 3692, -1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 2327, 3693, -1, 10); //Bank Booth  
        c.getPA().checkObjectSpawn(14367, 3064, 4973, 1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 3064, 4972, 1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 3064, 4971, 1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 3064, 4970, 1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 3064, 4969, 1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(14367, 3064, 4968, 1, 10); //Bank Booth
        c.getPA().checkObjectSpawn(-1, 2320, 3688, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2320, 3686, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2320, 3684, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2320, 3682, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2320, 3680, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2316, 3688, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2316, 3686, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2316, 3684, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2316, 3682, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2316, 3680, 0, 10); //BEDS
        c.getPA().checkObjectSpawn(-1, 2321, 3689, 0, 10); //Minning Space
        c.getPA().checkObjectSpawn(-1, 2316, 3689, 0, 10); //Minning Space
        c.getPA().checkObjectSpawn(-1, 2316, 3678, 0, 10); //Minning Space
        c.getPA().checkObjectSpawn(-1, 2321, 3678, 0, 10); //Minning Space      
        c.getPA().checkObjectSpawn(-1, 2329, 3686, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2332, 3686, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2332, 3687, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2328, 3693, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2329, 3693, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2330, 3693, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2331, 3693, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2332, 3693, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2332, 3692, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2331, 3692, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2330, 3692, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2332, 3691, 0, 10); //Bank Space
        c.getPA().checkObjectSpawn(-1, 2319, 3677, 0, 0); //door
        c.getPA().checkObjectSpawn(-1, 2319, 3690, 0, 0); //door
        /**End of Skilling zone **/
				/** Shit deleted - Omar / Play Boy **/	
		c.getPA().checkObjectSpawn(-1, 3084, 3502, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2732, 3369, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2611, 4776, 0, 10);		
		c.getPA().checkObjectSpawn(-1, 3077, 3496, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3077, 3495, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2732, 3365, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2735, 3367, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2729, 3373, 0, 0);		
		c.getPA().checkObjectSpawn(-1, 2728, 3373, 0, 0);	
		c.getPA().checkObjectSpawn(-1, 2729, 3378, 0, 0);	
		c.getPA().checkObjectSpawn(-1, 2725, 3378, 0, 0);	
		c.getPA().checkObjectSpawn(-1, 2726, 3368, 0, 0);						
		c.getPA().checkObjectSpawn(-1, 3088, 3509, 0, 10);	
		c.getPA().checkObjectSpawn(-1, 3080, 3507, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3083, 3507, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3079, 3507, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3077, 3507, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3084, 3509, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3084, 3510, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3084, 3512, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3083, 3513, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3080, 3513, 0, 10);	
		c.getPA().checkObjectSpawn(-1, 3079, 3513, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3078, 3513, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3077, 3513, 0, 10);		
		c.getPA().checkObjectSpawn(-1, 3077, 3512, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3076, 3509, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3076, 3510, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3076, 3511, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3076, 3512, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3078, 3510, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3080, 3510, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3081, 3510, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3095, 3480, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3096, 3479, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3097, 3481, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3095, 3477, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3096, 3479, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3097, 3474, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3096, 3476, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3092, 3477, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3476, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3474, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3091, 3478, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3092, 3480, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3479, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3096, 3469, 0, 10);	
		c.getPA().checkObjectSpawn(-1, 3048, 3494, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3054, 3494, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3103, 9909, 0, 0);
		c.getPA().checkObjectSpawn(-1, 3101, 9910, 0, 0);
		c.getPA().checkObjectSpawn(-1, 2543, 10143, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2541, 10141, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2545, 10145, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3503, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3494, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3091, 3495, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3496, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3092, 3496, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2728, 3373, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2729, 3373, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2726, 3368, 0, 10);		
		/** Camelot Market Place Delete - Omar / Play Boy */
		c.getPA().checkObjectSpawn(-1, 2750, 3509, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2761, 3511, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2761, 3509, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2759, 3513, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2757, 3513, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2755, 3511, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2755, 3509, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2757, 3507, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2759, 3507, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2756, 3508, 0, 10); 		
		c.getPA().checkObjectSpawn(-1, 2765, 3516, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2766, 3515, 0, 10); 
		c.getPA().checkObjectSpawn(-1, 2762, 3517, 0, 10); 	
		c.getPA().checkObjectSpawn(-1, 2761, 3517, 0, 10); 	
		c.getPA().checkObjectSpawn(-1, 2764, 3517, 0, 10); 	
		c.getPA().checkObjectSpawn(-1, 2760, 3517, 0, 10); 	
		c.getPA().checkObjectSpawn(-1, 2752, 3517, 0, 10); 			
		c.getPA().checkObjectSpawn(-1, 2756, 3517, 0, 10); 		
		c.getPA().checkObjectSpawn(-1, 2765, 3510, 0, 10); 		
		c.getPA().checkObjectSpawn(-1, 2765, 3509, 0, 10); 		
		c.getPA().checkObjectSpawn(-1, 2766, 3509, 0, 10); 		
			
        c.getPA().checkObjectSpawn(14367, 2751, 3511, 0, 10); //bank	
        c.getPA().checkObjectSpawn(14367, 2750, 3511, 0, 10); //bank	
        c.getPA().checkObjectSpawn(14367, 2749, 3511, 0, 10); //bank			
		

		
		/** Member Zone - Omar / Stefano / Play Boy / Reader **/
		c.getPA().checkObjectSpawn(2213, 2733, 3374, 0, 10);//Bank Booth
		c.getPA().checkObjectSpawn(2213, 2732, 3374, 0, 10);//Bank Booth
		c.getPA().checkObjectSpawn(2213, 2731, 3374, 0, 10);//Bank Booth
		c.getPA().checkObjectSpawn(2213, 2730, 3374, 0, 10);//Bank Booth
		c.getPA().checkObjectSpawn(10660, 2732, 3369, 0, 10);//Christmass Tree
		/** Portals - Omar / Play Boy **/	
		c.getPA().checkObjectSpawn(6282, 3092, 3506, 0, 10);//Member Portal
		c.getPA().checkObjectSpawn(13617,3096, 3479, 1, 10);//SkillingZone Portal
		c.getPA().checkObjectSpawn(13618, 3096, 3476, 1, 10);//Trainning Portal
		c.getPA().checkObjectSpawn(13619, 3091, 3476, 3, 10);//Boss Portal
		c.getPA().checkObjectSpawn(13620, 3091, 3479, 3, 10);//Minigame Portal
		c.getPA().checkObjectSpawn(13623, 3094, 3487, 0, 10);//Pking Portal
		/** Home Stuff - Omar / Play Boy **/
		//c.getPA().checkObjectSpawn(4000, 3084, 3493, -4, 10);//Computer
		c.getPA().checkObjectSpawn(575, 3092, 3487, 0, 10);//Highscores
		c.getPA().checkObjectSpawn(574, 3090, 3492, -3, 10);//PKP Statue
		c.getPA().checkObjectSpawn(3515, 3090, 3488, -3, 10);//Comp Cape
		c.getPA().checkObjectSpawn(2182, 3057, 3505, 3, 10);//Donator safe
		c.getPA().checkObjectSpawn(4874, 3046, 3501, 0, 10);//Crafting Stall
		c.getPA().checkObjectSpawn(4875, 3046, 3503, 0, 10);//Food Stall
		c.getPA().checkObjectSpawn(4876, 3046, 3505, 0, 10);//General Stall
		c.getPA().checkObjectSpawn(6552, 3096, 3500, 0, 10);//Ancient Altar
		c.getPA().checkObjectSpawn(13179, 3097, 3506, -2, 10);//Lunar Prayer			
		c.getPA().checkObjectSpawn(409, 3090, 3495, 1, 10);//Prayer
		c.getPA().checkObjectSpawn(412, 3085, 3510, -3, 10);//Curse Prayer
        c.getPA().checkObjectSpawn(162, 3090, 3499, -1, 10); //Lottery Machine
        c.getPA().checkObjectSpawn(172, 3090, 3510, -1, 10); //Crystal Chest
		/** Market **/
		 c.getPA().checkObjectSpawn(162, 2741, 3468, -3, 10); //Lottery Machine
		 c.getPA().checkObjectSpawn(162, 2734, 3468, -1, 10); //Lottery Machine
		 c.getPA().checkObjectSpawn(576, 2734, 3467, -3, 10); //Statue
		 c.getPA().checkObjectSpawn(576, 2734, 3469, -3, 10); //Statue		 	 
		 c.getPA().checkObjectSpawn(576, 2741, 3467, -1, 10); //Statue
		 c.getPA().checkObjectSpawn(576, 2741, 3469, -1, 10); //Statue
		/** Skilling Stuff - Omar / Play Boy **/	
		c.getPA().checkObjectSpawn(2728, 2851, 3433, -1, 10);//Range		
		c.getPA().checkObjectSpawn(2090, 3277, 2767, 1, 10);//Copper
		c.getPA().checkObjectSpawn(2090, 3277, 2768, 1, 10);//Copper	
		c.getPA().checkObjectSpawn(2094, 3277, 2770, 1, 10);//Tin
		c.getPA().checkObjectSpawn(2094, 3277, 2771, 1, 10);//Tin	
		c.getPA().checkObjectSpawn(2092, 3277, 2773, 1, 10);//Copper
		c.getPA().checkObjectSpawn(2092, 3277, 2774, 1, 10);//Copper	
		c.getPA().checkObjectSpawn(2096, 3277, 2776, 1, 10);//Coal		
		c.getPA().checkObjectSpawn(2096, 3277, 2777, 1, 10);//Coal		
		c.getPA().checkObjectSpawn(2098, 3285, 2767, 1, 10);//Gold	
		c.getPA().checkObjectSpawn(2098, 3285, 2768, 1, 10);//Gold	
		c.getPA().checkObjectSpawn(2102, 3285, 2770, 1, 10);//Mith	
		c.getPA().checkObjectSpawn(2102, 3285, 2771, 1, 10);//Mith					
		c.getPA().checkObjectSpawn(2105, 3285, 2773, 1, 10);//Adamant	
		c.getPA().checkObjectSpawn(2105, 3285, 2774, 1, 10);//Adamant				
		c.getPA().checkObjectSpawn(14860, 3285, 2776, 1, 10);//Rune			
		c.getPA().checkObjectSpawn(14860, 3285, 2777, 1, 10);//Rune		
		c.getPA().checkObjectSpawn(2213, 3281, 2764, 0, 10);//Bank Booth
		c.getPA().checkObjectSpawn(3044, 3280, 2775, 5, 10);//Furnace
		c.getPA().checkObjectSpawn(2783, 3283, 2764, 0, 10);//Anvil
		c.getPA().checkObjectSpawn(2783, 3279, 2764, 0, 10);//Anvil
		/**End**/
        c.getPA().checkObjectSpawn(2472, 3056, 9561, 1, 10);
        c.getPA().checkObjectSpawn(2470, 2722, 5106, 1, 10);
        c.getPA().checkObjectSpawn(361, 2727, 5072, 1, 10);
        /****** Hybriders Den *********/
        c.getPA().checkObjectSpawn(1032, 3065, 3809, 1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3065, 3808, 1, 10); //danger   
        c.getPA().checkObjectSpawn(1032, 3065, 3807, 1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3065, 3806, 1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3065, 3805, 1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3067, 3804, 1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3804, 3068, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3804, 3069, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3804, 3070, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3804, 3071, 1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3804, 3072, -1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3074, 3805, -1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3074, 3806, -1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3074, 3807, -1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3074, 3808, -1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3074, 3809, -1, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3072, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3073, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3066, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3065, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3074, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3071, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3070, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3071, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3072, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3066, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3065, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3073, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3074, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3067, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3068, 3810, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3071, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3070, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3068, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(1032, 3067, 3804, 0, 10); //danger
        c.getPA().checkObjectSpawn(2466, 3070, 3807, 0, 10); //danger
        /** End Of Den **/
        c.getPA().checkObjectSpawn(-1, 3091, 3495, 0, 10);
        c.getPA().checkObjectSpawn(-1, 3092, 3496, 0, 10);
        c.getPA().checkObjectSpawn(-1, 3090, 3494, 0, 10);
        c.getPA().checkObjectSpawn(-1, 3090, 3496, 0, 10);
        c.getPA().checkObjectSpawn(-1, 3079, 3501, 0, 10);
        c.getPA().checkObjectSpawn(-1, 3080, 3501, 0, 10);
        c.getPA().checkObjectSpawn(-1, 2034, 4636, 0, 10);
        c.getPA().checkObjectSpawn(5276, 2030, 4632, 0, 10); //bank N
        c.getPA().checkObjectSpawn(5276, 2031, 4632, 0, 10); //bank N   
        c.getPA().checkObjectSpawn(14367, 2735, 3469, 0, 10); //bank N
        c.getPA().checkObjectSpawn(14367, 2736, 3469, 0, 10); //bank N
        c.getPA().checkObjectSpawn(14367, 2737, 3469, 0, 10); //bank N
        c.getPA().checkObjectSpawn(14367, 2738, 3469, 0, 10); //bank N
        c.getPA().checkObjectSpawn(14367, 2739, 3469, 0, 10); //bank N
        c.getPA().checkObjectSpawn(14367, 2740, 3469, 0, 10); //bank N
        c.getPA().checkObjectSpawn(14367, 2740, 3467, 0, 10); //bank S
        c.getPA().checkObjectSpawn(14367, 2739, 3467, 0, 10); //bank S
        c.getPA().checkObjectSpawn(14367, 2738, 3467, 0, 10); //bank S
        c.getPA().checkObjectSpawn(14367, 2737, 3467, 0, 10); //bank S
        c.getPA().checkObjectSpawn(14367, 2736, 3467, 0, 10); //bank S
        c.getPA().checkObjectSpawn(14367, 2735, 3467, 0, 10); //bank S
        c.getPA().checkObjectSpawn(357, 3312, 3235, 1, 10);
        c.getPA().checkObjectSpawn(2213, 2788, 2789, 1, 10); //bank monkey      
        c.getPA().checkObjectSpawn(1755, 3055, 9774, 0, 0);
        c.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
        c.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
        c.getPA().checkObjectSpawn(1596, 3040, 10307, -1, 0);
        c.getPA().checkObjectSpawn(1596, 3040, 10308, 1, 0);
        c.getPA().checkObjectSpawn(1596, 3022, 10311, -1, 0);
        c.getPA().checkObjectSpawn(1596, 3022, 10312, 1, 0);
        c.getPA().checkObjectSpawn(1596, 3044, 10341, -1, 0);
        c.getPA().checkObjectSpawn(1596, 3044, 10342, 1, 0);
        c.getPA().checkObjectSpawn(2213, 3047, 9779, 1, 10);
        c.getPA().checkObjectSpawn(2213, 3080, 9502, 1, 10);
        c.getPA().checkObjectSpawn(2475, 3233, 9312, 1, 10);
        c.getPA().checkObjectSpawn(4551, 2522, 3595, 1, 10);
			/** Market Delete **/
		c.getPA().checkObjectSpawn(-1, 2746, 3460, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2745, 3465, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2745, 3464, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2745, 3474, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2745, 3473, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2728, 3460, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2744, 3476, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2743, 3476, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2742, 3476, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2741, 3476, 0, 10);
		
		/**Stalls**/		
		c.getPA().checkObjectSpawn(2561, 3099, 3498, 0, 10);//bakery
		c.getPA().checkObjectSpawn(2560, 3099, 3495, 0, 10);//silk
		c.getPA().checkObjectSpawn(2564, 3099, 3492, 0, 10);//spice
		c.getPA().checkObjectSpawn(2562, 3099, 3489, 0, 10);//gem
		
		
		//Membership Training
		c.getPA().checkObjectSpawn(11356, 2734, 3374, 0, 10);//Mysterious Portal
		//End
		
		
		if (c.heightLevel == 0)
			c.getPA().checkObjectSpawn(2492, 2911, 3614, 1, 10);
		else
			c.getPA().checkObjectSpawn(-1, 2911, 3614, 1, 10);
	}
	
	public final int IN_USE_ID = 14825;
	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}
	public int[] obeliskIds = {14829,14830,14827,14828,14826,14831};
	public int[][] obeliskCoords = {{3154,3618},{3225,3665},{3033,3730},{3104,3792},{2978,3864},{3305,3914}};
	public boolean[] activated = {false,false,false,false,false,false};
	
	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,16));
			}
		}	
	}
	
	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}
	
	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				int xOffset = c.absX - obeliskCoords[port][0];
				int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1)) {
					c.getPA().startTeleport2(obeliskCoords[random][0] + xOffset, obeliskCoords[random][1] + yOffset, 0);
				}
			}		
		}
	}
	
	public boolean loadForPlayer(Object o, Client c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
	}
	
	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}	
	}




}