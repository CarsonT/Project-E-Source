package server.model.skills.fishing;

import server.Config;
import server.util.Misc;
import server.model.players.Client;
import server.Server;
import server.model.players.PlayerAssistant;
import server.model.objects.Objects;
import server.model.players.Player;

public class Fishing {

    private Client c;
	
    public Fishing(Client c) {
        this.c = c;
    }

    public void FishingProcess() {
        boolean resetAnim = false;
        if (c.fishtimer > 0) {
            c.fishtimer--;
        }
        if (c.fishing && c.getItems().freeSlots() <= 0) {
            c.fishing = false;
            c.sendMessage("Your inventory is full");
			c.getPA().sendStatement("Your inventory is full");
            c.getPA().frame1();
            resetAnim = true;
        }
        if (c.fishing && c.fishtimer <= 0 && c.getItems().freeSlots() > 0) {
            if (c.getItems().playerHasItem(c.fishitem)) {
                if (c.playerLevel[10] >= c.fishreqt) {
                    if (c.fishitem == 307 && !c.getItems().playerHasItem(313)) {
                        c.sendMessage("You need bait to fish here!");
						c.getPA().sendStatement("You need bait to fish here!");
                        c.fishing = false;
                    } else if (c.fishitem == 309 && !c.getItems().playerHasItem(314)) {
                        c.sendMessage("You need feathers to fish here!");
						c.getPA().sendStatement("You need feathers to fish here!");
                        c.fishing = false;
                    } else {
                        if (c.fishreq2 != 0 && c.playerLevel[10] >= c.fishreq2 && Misc.random(1) == 1) {
                            c.getItems().addItem(c.fishies2, 1);
                            c.getPA().addSkillXP(c.fishXP, 10);
                        } else {
                            c.getItems().addItem(c.fishies, 1);
                            c.getPA().addSkillXP(c.fishXP, 10);
                        }
                        if (c.fishitem == 307) c.getPA().addSkillXP(c.fishXP, 10);
                        c.fishtimer = Misc.random(fishtime(c.fishies, c.fishreqt));
                    }
                } else {
                    c.fishing = false;
                    c.sendMessage("You need a fishing level of " + c.fishreqt + " to fish for " + c.getItems().getItemName(c.fishies));
					c.getPA().sendStatement("You need a fishing level of " + c.fishreqt + " to fish for " + c.getItems().getItemName(c.fishies));
                    resetFishing();
                }
            } else {
                c.fishing = false;
                c.sendMessage("You need a " + c.getItems().getItemName(c.fishitem) + " to fish " + c.getItems().getItemName(c.fishies));
				c.getPA().sendStatement("You need a " + c.getItems().getItemName(c.fishitem) + " to fish " + c.getItems().getItemName(c.fishies));
                resetFishing();
            }
        }
        if (c.fishing) {
            c.startAnimation(c.fishemote);
            //c.stopMovement();
        }
        if (c.attemptingfish) if (c.clickObjectType > 0 && c.goodDistance(c.objectX + c.objectXOffset, c.objectY + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
            c.attemptingfish = false;
            c.fishing = true;
        }
    }
	
    public void Lol() {
        c.fishing = true;
        c.startAnimation(c.fishemote);
        return;
    }

    public int fishtime(int fish, int req) {
        int time = 10;
        if (fish == 317) { //Shrimp 1
            time = 10;
            c.sendMessage("You Catch a Raw Shrimp!");
        }

        if (fish == 321) { //Anchovies 5
            time = 11;
            c.sendMessage("You Catch a Raw Anchovie!");
        }
        if (fish == 327) { //Sardine 5
            time = 15;
            c.sendMessage("You Catch a Raw Sardine!");
        }
        if (fish == 355) { //Trout 20
            time = 20;
            c.sendMessage("You Catch a Raw Trout!");
        }
        if (fish == 341) { //Cods 23
            time = 25;
            c.sendMessage("You Catch a Raw Cod!");
        }
        if (fish == 349) { //Pike 25
            time = 28;
            c.sendMessage("You Catch a Raw Pike!");
        }
        if (fish == 359) { //Tuna 35
            time = 30;
            c.sendMessage("You Catch a Raw Tuna!");
        } else {
            if (c.fishies == 371) {
                time = 33;
                c.sendMessage("You Catch a Raw Swordfish!");
            }
        }
        if (fish == 377) { //Lobsters 40
            time = 35;
            c.sendMessage("You Catch a Raw Lobster!");
        }
        if (fish == 383) { //Sharks 79
            time = 40;
            c.sendMessage("You Catch a Raw Shark!");
        }
        if (fish == 389) { //Manta ray 85
            time = 45;
            c.sendMessage("You Catch a Raw Manta-ray!");
        }
        if (fish == 15271) { //Rocktail 95
            time = 46;
            c.sendMessage("You Catch a Raw Rocktail!");
        }
        int LevelXP = c.playerLevel[10] - req;
        if (LevelXP > req / 3) LevelXP = req / 3;
        time -= LevelXP;
        return time;
    }
	
    public void resetFishing() {
        this.c.fishies = -1;
        this.c.fishitem = -1;
        this.c.fishreqt = 0;
        c.fishing = false;
    }
	
	
}