package server.model.minigames;

import java.util.Random;
import server.model.players.Client;
import server.model.players.Player;
import server.model.players.PlayerHandler;

public class FreeForAll {

   private static final int[][] SPAWN_LOCATIONS = new int[][]{{3177, 9757}, {3172, 9761}, {3161, 9758}, {3167, 9754}, {3172, 9756}, {3178, 9761}, {3162, 9762}};
   private static final int[] LEAVE_LOCATION = new int[]{3200, 3200};
   private static final int GAME_COST = 1000000;
   public static int playerCount = 0;
   private static int processCount = 6;


   public static void process() {
      if((processCount -= 1) == 0) {
         processCount = 6;
         playerCount = 0;
         Player[] var3 = PlayerHandler.players;
         int var2 = PlayerHandler.players.length;

         Player p;
         int var1;
         Client c;
         for(var1 = 0; var1 < var2; ++var1) {
            p = var3[var1];
            if(p != null) {
               c = (Client)p;
               if(inGame(c)) {
                  ++playerCount;
               }
            }
         }

         var3 = PlayerHandler.players;
         var2 = PlayerHandler.players.length;

         for(var1 = 0; var1 < var2; ++var1) {
            p = var3[var1];
            if(p != null) {
               c = (Client)p;
               if(inGame(c)) {
                  showInterface(c);
               }
            }
         }

      }
   }

   public static void submitPlayer(Client c) {
      if(c.getItems().playerHasItem(995, 1000000)) {
         c.getItems().deleteItem2(995, 1000000);
         int random = (new Random()).nextInt(SPAWN_LOCATIONS.length - 1);
         c.getPA().movePlayer(SPAWN_LOCATIONS[random][0], SPAWN_LOCATIONS[random][1], 0);
         c.sendMessage("For every player you kill in this game, you will receive 1000000 coins.");
         c.sendMessage("When you die, you will leave the game and can instantly rejoin for 1000000 coins.");
         c.sendMessage("You can safely leave teleporting from the bank");
      } else {
         c.sendMessage("You need: 1000000 coins to play this game.");
      }

   }
   
	public static void iKilledPlayer(Client c) {
		c.getItems().addItem(995, 1000000);
		c.sendMessage("You have been rewarded 1000000 coins for kill");
	}
   
	public static void iDied(Client c) {
		c.getPA().movePlayer(LEAVE_LOCATION[0], LEAVE_LOCATION[1], 0);
	}

   public static void killedPlayer(Client killer, Client killed) {
      killed.getPA().movePlayer(LEAVE_LOCATION[0], LEAVE_LOCATION[1], 0);
      killer.getItems().addItem(995, 1000000);
      killer.pcPoints += 1;
      killer.sendMessage("You have been rewarded 1000000 coins for killing " + killed.playerName);
      killer.sendMessage("You have been rewarded 1 Pc points for killing " + killed.playerName);
   }

   public static void showInterface(Client c) {
      c.getPA().sendFrame126("", 6570);
      c.getPA().sendFrame126("Competitors. : " + playerCount, 6572);
      c.getPA().sendFrame126("", 6664);
      c.getPA().walkableInterface(6673);
   }

   public static void removePlayer(Client c) {
      c.getItems().addItem(995, 1000000);
      c.getPA().movePlayer(LEAVE_LOCATION[0], LEAVE_LOCATION[1], 0);
   }

   public static boolean inGame(Client c) {
      //return c.absX >= 2480 && c.absX <= 2630 && c.absY >= 3838 && c.absY <= 3907 && c.heightLevel == 0;
	  return c.absX >= 3158 && c.absX <= 3181 && c.absY >= 9752 && c.absY <= 9764 && c.heightLevel == 0;
   }
}
