package server.model.players;

import server.Server;
import server.Config;
import server.model.content.Achievements;

public class DialogueHandler {

    private Client c;

    public DialogueHandler(Client client) {
        this.c = client;
    }

    public void sendDialogues(int dialogue, int npcId) {
        c.talkingNpc = npcId;
        switch (dialogue) {
            /* Tutorial - Omar / Play Boy */

            case 20000:
                sendOption2("Tutorial", "Skip Tutorial");
                c.dialogueAction = 20000;
				c.Unclosable = 1;
                break;

            case 20001:
                sendStatement("Have fun playing Project-Equinox!");
				c.Unclosable = 0;
                break;

            case 20005:
                c.DoingTut = true;
                c.TutorialStart = true;
                sendStatement("Welcome to Project-Equinox " + c.playerName + "!");
                c.nextChat = 90006;
                c.getPA().Tutorial(10);
				c.Unclosable = 1;
                break;

            case 90006:
                sendStatement("There are many wonderful features about Project-Equinox");
                c.nextChat = 90007;
                c.getPA().Tutorial(20);
				c.Unclosable = 1;
                break;

            case 90007:
                c.getPA().movePlayer(3086, 3498, 0);
                sendStatement("This is the home area, Project-E points can be spent here - they acquired from Slayer tasks, Pc, and Pk'ing.");
                c.nextChat = 90008;
                c.getPA().Tutorial(30);
				c.Unclosable = 1;
                break;

            case 90008:
                c.getPA().movePlayer(3102, 3494, 0);
                sendStatement("You can earn fast money by stealing items from these stalls");
                c.nextChat = 90009;
                c.getPA().Tutorial(40);
				c.Unclosable = 1;
                break;

            case 90009:
                c.getPA().movePlayer(3094, 3478, 0);
                sendStatement("This is the portal area");
                c.nextChat = 90010;
                c.getPA().Tutorial(50);
				c.Unclosable = 1;
                break;

            case 90010:
                sendStatement("Here you can teleport to various mingames, bosses, training areas, etc...");
                c.nextChat = 90011;
                c.getPA().Tutorial(60);
				c.Unclosable = 1;
                break;

            case 90011:
                c.getPA().movePlayer(2759, 3513, 0);
                sendStatement("This is the market area");
                c.nextChat = 90012;
                c.getPA().Tutorial(70);
				c.Unclosable = 1;
                break;

            case 90012:
                sendStatement("Every item that you will need can be bought here");
                c.nextChat = 90013;
                c.getPA().Tutorial(80);
				c.Unclosable = 1;
                break;

            case 90013:
                c.getPA().movePlayer(3170, 3681, 0);
                sendStatement("This is Bounty Hunter");
                c.nextChat = 90014;
                c.getPA().Tutorial(90);
				c.Unclosable = 1;
                break;

            case 90014:
                sendStatement("Bounty Hunter is fully functioning - Targets, penalties, etc...");
                c.nextChat = 90015;
                c.getPA().Tutorial(93);
				c.Unclosable = 1;
                break;

            case 90015:
                c.getPA().movePlayer(2327, 3681, 0);
                sendStatement("This is the Skilling Zone");
                c.nextChat = 90016;
                c.getPA().Tutorial(96);
				c.Unclosable = 1;
                break;

            case 90016:
                sendStatement("You can train almost every skill here");
                c.nextChat = 90017;
                c.getPA().Tutorial(97);
				c.Unclosable = 1;
                break;

            case 90017:
                c.getPA().movePlayer(Config.START_LOCATION_X, Config.START_LOCATION_Y, 3);
                sendStatement("The rest is up to you to find out.");
                c.nextChat = 90018;
                c.getPA().Tutorial(98);
				c.Unclosable = 1;
                break;

            case 90018:
                sendStatement("We hope you enjoy your stay at Project-Equinox!");
                c.nextChat = 90019;
                c.getPA().Tutorial(99);
				c.Unclosable = 1;
                break;

            case 90019:
                sendStatement("Have fun!");
                c.getPA().addStarter();
                c.getPA().Tutorial(100);
                Achievements.Tutorial();
                break;

                /* Shit to Porn for Porn :/ - Play Boy */

            case 25000:
                sendOption3("My Player Killing Statistics", "Player Killing Shop One", "Player Killing Shop Two");
                c.dialogueAction = 25000;
                break;
            case 25001:
                sendStatement("@red@" + c.pePoints + " @bla@Project-E Points || @red@" + c.KC + " @bla@Kills || @red@" + c.DC + " @bla@Deaths");
                c.nextChat = 25000;
                break;

            case 25050:
                sendOption3("My Membership Statistics", "Membership Shop One", "Membership Shop Two");
                c.dialogueAction = 25050;
                break;

            case 25075:
                sendOption3("Get Task", "Change Task", "My Slayer Statistics");
                c.dialogueAction = 25075;
                break;

            case 25078:
                sendStatement("Please get a task first!");
                c.nextChat = 25075;
                break;

            case 25074:
                sendStatement("You already have a task!");
                c.nextChat = 25075;
                break;

            case 25079:
                sendStatement("@red@" + c.pePoints + " @bla@Project-E(Slay) Points || Current Task: @red@" + c.taskAmount + " @bla@of @red@" + Server.npcHandler.getNpcListName(c.slayerTask));
                c.dialogueAction = 25075;
                break;

                /* Shops Dialogue */

            case 20010:
                sendOption2("Magic Accessories", "Magic Armours");
                c.dialogueAction = 20010;
                break;
            case 20011:
                sendOption3("Melee Weapons", "Melee Armours 1", "Melee Armours 2");
                c.dialogueAction = 20011;
                break;
            case 20012:
                sendOption2("Range Weapons", "Range Armours");
                c.dialogueAction = 20012;
                break;
            case 20013:
                sendOption2("Skillcapes", "Hoods");
                c.dialogueAction = 20013;
                break;

                /* End of Shops Dialogue */
            case 187:
                sendStatement("Current fund is at: @blu@" + Server.lottery.lotteryFund + "@bla@m!");
                break;
            case 185:
                sendOption4("How does this work?", "Current Lottery Fund", "Enter Lottery", "Nevermind");
                c.dialogueAction = 185;
                break;
            case 186:
                sendOption2("Yes I want to enter!", "No, I'd rather not.");
                c.dialogueAction = 186;
                break;
            case 3515:
                sendOption2("Obtain Completionist cape", "Don't Obtain Completionist cape");
                c.dialogueAction = 3516;
                break;
            case 9292:
                c.getDH().sendOption3("What is this?", "Random reward ( costs 50 PE points )", "Nevermind");
                c.dialogueAction = 9292;
                break;

            case 501:
                sendOption3("How many Maniac points do I have?", "I would like to play the minigame.", "I would like to spend my points please.");
                c.dialogueAction = 100;
                c.nextChat = 0;
                break;

            case 502:
                sendStatement("You Have @blu@" + c.Frost + " @bla@Maniac Points!");
                break;

            case 19998:
                sendStatement("Please talk to the guide first!");
                c.dialogueAction = 19998;
                break;

                /*Start Of Portals*/
            case 10000:
                //Skilling Portal 1
                sendOption4("Skillzone", "Agility", "Fishing/Cooking", "Next");
                c.dialogueAction = 10000;
                break;
            case 10001:
                //Skilling Portal 2
                sendOption4("Runecrafting", "Farming", "", "");
                c.dialogueAction = 10001;
                break;
            case 10005:
                //Trainning Portal 1
                sendOption5("Bandits", "Experiments", "Rock Crabs", "Chaos Druids", "Next");
                c.dialogueAction = 10005;
                break;
            case 10006:
                //Trainning Portal 2
                sendOption5("Varrock Sewer", "Dragons", "Coming Soon!", "Coming Soon!", "Coming Soon!");
                c.dialogueAction = 10006;
                break;
            case 10010:
                //Boss Portal 1
                sendOption5("King Black Dragon - @gre@Safe", "Corporeal Beast - @gre@Safe", "Chaos Element - @red@Danger", "Dagannoth Kings - @gre@Safe", "Next");
                c.dialogueAction = 10010;
                break;
            case 10011:
                //Boss Portal 2
                sendOption5("Godwars Dungeon - @gre@Safe", "Frost Dragons - @gre@Safe", "Revenants - @red@Danger", "Wildywrym - @red@Danger", "Next");
                c.dialogueAction = 10011;
                break;
            case 10013:
                //Boss Portal 3
                sendOption5("Kalphite Queen - @gre@Safe", "Coming Soon!", "Coming Soon!", "Coming Soon!", "Coming Soon!");
                c.dialogueAction = 10013;
                break;
            case 10012:
                //Boss Portal 0
                sendStatement("Type ::bossdrops for information regarding drops");
                c.nextChat = 10010;
                break;
            case 10015:
                //Minigame Portal 1
                sendOption5("Pest Control", "Barrows", "Castle Wars", "Coming Soon!", "Coming Soon!");
                c.dialogueAction = 10015;
                break;
            case 10020:
                //Pk Portal 1
                sendOption5("Bounty Hunter", "Fun Pk", "Green Dragon Easts - @gre@Single", "Hybrid Den - @gre@Single", "Next");
                c.dialogueAction = 10020;
                break;
            case 10021:
                //Pk Portal 2
                sendOption5("Island Madness - @gre@Single", "Mage Bank - @gre@Single", "Coming Soon!", "Coming Soon!", "Coming Soon!");
                c.dialogueAction = 10021;
                break;
                /*End Of Portals*/


            case 36899:
                sendOption4("What is this?", "Exchange Coins For Ticket", "Exchange Ticket For Coins", "Nevermind.");
                c.dialogueAction = 36899;
                break;
            case 0:
                c.talkingNpc = -1;
                c.getPA().removeAllWindows();
                c.nextChat = 0;
                break;
            case 1:
                sendStatement("You found a hidden tunnel! Do you want to enter it?");
                c.dialogueAction = 1;
                c.nextChat = 2;
                break;
            case 2:
                sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
                c.dialogueAction = 1;
                c.nextChat = 0;
                break;
            case 989:
                if (c.memberStatus >= 0) {
                    sendNpcChat3("Welcome " + c.playerName + "!,", "You're allowed to enter the.", "Member Zone.", c.talkingNpc, "Legends_Guard");
                    c.nextChat = 0;
                } else {
                    sendNpcChat3("Welcome traveller,", "This is a Member Only Area.", "Visit forums on how to become a Member.", c.talkingNpc, "Legends_Guard");
                    c.nextChat = 0;
                }
                break;
            case 1859:
                sendNpcChat3("Welcome " + c.playerName + "!,", "My Name is Brian, and I am in charge of the Member shop", "Would you like to take a gander?", c.talkingNpc, "Brian");
                c.nextChat = 1861;
                break;
            case 1861:
                c.getShops().openShop(90);
                c.nextChat = 0;
                break;
            case 979:
                if (c.getItems().playerHasItem(7774, 1)) {
                    if (c.memberStatus == 1) {
                        sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 990;
                    } else {
                        sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 990;
                    }
                } else if (c.getItems().playerHasItem(7775, 1)) {
                    if (c.memberStatus == 2) {
                        sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 990;
                    } else {
                        sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 990;
                    }
                } else if (c.getItems().playerHasItem(7776, 1)) {
                    if (c.memberStatus == 3) {
                        sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 990;
                    } else {
                        sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 990;
                    }
                } else if (c.memberStatus == 0) {
                    sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                    c.nextChat = 995;
                } else if (c.memberStatus >= 0) {
                    sendNpcChat3("Hello " + c.playerName + "!", "My name is King Lathas and I am in charge of the", "Members around here.", c.talkingNpc, "King_Lathas");
                    c.nextChat = 990;
                }
                break;
            case 995:
                sendNpcChat3("You're not a Member yet.", "If you want i can open the forums for you, With more", "information on how to become a member and their Benefits.", c.talkingNpc, "King_Lathas");
                c.nextChat = 996;
                break;
            case 996:
                sendOption2("Yes open the Forums for me", "No Thanks");
                c.dialogueAction = 1231;
                break;
            case 990:
                if (c.getItems().playerHasItem(7774, 1)) {
                    if (c.memberStatus == 3) {
                        sendNpcChat2("You're already a Extreme Member.", "Therefore you cannot use this Ticket", c.talkingNpc, "King_Lathas");
                        c.nextChat = 0;
                    } else if (c.memberStatus == 2) {
                        sendNpcChat2("You're already a Super Member.", "Therefore you cannot use this Ticket", c.talkingNpc, "King_Lathas");
                        c.nextChat = 0;
                    } else if (c.memberStatus == 1) {
                        sendNpcChat1("You're already a Extreme Member.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 0;
                    } else {
                        sendNpcChat3("Ah... I see you got a Member Ticket.", "Want me to Exchange it for:", "Normal Member Status + 5 Member Points?.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 1030;
                    }
                } else if (c.getItems().playerHasItem(7775, 1)) {
                    if (c.memberStatus == 3) {
                        sendNpcChat2("You're already a Extreme Member.", "Therefore you cannot use this Ticket", c.talkingNpc, "King_Lathas");
                        c.nextChat = 0;
                    } else if (c.memberStatus == 2) {
                        sendNpcChat1("You're already a Super Member.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 0;
                    } else {
                        sendNpcChat3("Ah... I see you got a Member Ticket.", "Want me to Exchange it for:", "Super Member Status + 10 Member Points?.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 1031;
                    }
                } else if (c.getItems().playerHasItem(7776, 1)) {
                    if (c.memberStatus == 3) {
                        sendNpcChat1("You're already a Extreme Member.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 0;
                    } else {
                        sendNpcChat3("Ah... I see you got a Member Ticket.", "Want me to Exchange it for:", "Extreme Member Status + 20 Member Points?.", c.talkingNpc, "King_Lathas");
                        c.nextChat = 1032;
                    }
                } else {
                    c.sendMessage("You need a Membership Ticket to use this feature.");
                    c.getPA().removeAllWindows();
                }
                break;
            case 1030:
                sendOption2("Yes i would like to become a Normal Member", "No Thanks, I might use my ticket later");
                c.dialogueAction = 1030; //This must be the same as the case number
                break;
            case 1031:
                sendOption2("Yes i would like to become a Super Member", "No Thanks, I might use my ticket later");
                c.dialogueAction = 1031; //This must be the same as the case number
                break;
            case 1032:
                sendOption2("Yes i would like to become a Extreme Member", "No Thanks, I might use my ticket later");
                c.dialogueAction = 1032; //This must be the same as the case number
                break;
            case 3:
                sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I can assign you a slayer task suitable to your combat level.",
                    "Would you like a slayer task?", c.talkingNpc, "Duradel");
                c.nextChat = 4;
                break;
            case 5:
                sendNpcChat4("Hello adventurer...", "My name is Kolodion, the master of this mage bank.", "Would you like to play a minigame in order ",
                    "to earn points towards recieving magic related prizes?", c.talkingNpc, "Kolodion");
                c.nextChat = 6;
                break;
            case 106:
                sendOption5("One 6-sided die", "Two 6-sided dice", "One 4-sided die", "One 8-sided die", "More...");
                c.dialogueAction = 106;
                c.teleAction = 0;
                c.nextChat = 0;
                break;

            case 107:
                sendOption5("One 10-sided die", "One 12-sided die", "One 20-sided die", "Two 10-sided dice for 1-100", "Back...");
                c.dialogueAction = 107;
                c.teleAction = 0;
                c.nextChat = 0;
                break;
            case 6:
                sendNpcChat4("The way the game works is as follows...", "You will be teleported to the wilderness,",
                    "You must kill mages to recieve points,", "redeem points with the chamber guardian.", c.talkingNpc, "Kolodion");
                c.nextChat = 15;
                break;
            case 11:
                sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I can assign you a slayer task suitable to your combat level.",
                    "Would you like a slayer task?", c.talkingNpc, "Duradel");
                c.nextChat = 12;
                break;
            case 12:
                sendOption2("Yes I would like a slayer task.", "No I would not like a slayer task.");
                c.dialogueAction = 5;
                break;
            case 13:
                sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I see I have already assigned you a task to complete.",
                    "Would you like me to give you an easier task?", c.talkingNpc, "Duradel");
                c.nextChat = 14;
                break;
            case 14:
                sendOption2("Yes I would like an easier task.", "No I would like to keep my task.");
                c.dialogueAction = 6;
                break;
            case 15:
                sendOption2("Yes I would like to play", "No, sounds too dangerous for me.");
                c.dialogueAction = 7;
                break;
            case 16:
                sendOption2("Fix all broken items (chaotics and barrows)", "Cancel");
                c.dialogueAction = 8;
                break;
            case 17:
                sendOption5("Air", "Mind", "Water", "Earth", "More");
                c.dialogueAction = 10;
                c.dialogueId = 17;
                c.teleAction = -1;
                break;
            case 18:
                sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
                c.dialogueAction = 11;
                c.dialogueId = 18;
                c.teleAction = -1;
                break;
            case 19:
                sendOption5("Nature", "Law", "Death", "Blood", "More");
                c.dialogueAction = 12;
                c.dialogueId = 19;
                c.teleAction = -1;
                break;

            case 20:
                sendNpcChat1("You currently have " + c.pePoints + " Project-E points.", c.talkingNpc, "Mazchna");
                c.dialogueId = 31;
                c.dialogueAction = 31;
                break;
            case 21:
                sendNpcChat4("Congratulations!", "You have killed 10 monkeys hope you learned something..", "would you like to escape?", "Do not break anymore rules!", c.talkingNpc, "Mosol Rei");
                c.dialogueAction = 26;
                c.nextChat = 22;
                break;
            case 22:
                sendOption2("Yes get me out of this fucking hell hole!", "Hell no! I love it here, I'm nuts for these monkeys!");
                c.dialogueAction = 27;
                c.nextChat = 0;
                break;
            case 23:
                sendNpcChat4("You cannot Escape yet!", "You've killed " + c.monkeyk0ed + " out of 10 monkeys!", "Come back when you have killed 10", "Kthxbai", c.talkingNpc, "Mosol Rei");
                c.dialogueAction = 30;
                c.nextChat = 0;
                break;
            case 24:
                sendNpcChat1("Are you sure you want to reset your skills?",
                c.talkingNpc, "Duradel");
                c.nextChat = 25;
                c.dialogueId = 25;
                break;
            case 25:
                sendOption2("Yes I'm positive", "Nevermind");
                c.dialogueAction = 35;
                c.nextChat = 26;
                break;
            case 26:

                sendOption2("Attack", "Defence");
                c.dialogueAction = 36;
                c.dialogueAction = 37;

                break;
            case 27:

                sendOption3("Green Dragons East", "Mage Bank", "Edgville");
                c.dialogueAction = 38;
                //c.dialogueAction = 39;

                break;


            case 28:
                sendStatement("Warning your are about to teleport out of here! Are you sure?");
                c.dialogueAction = 77;
                c.nextChat = 29;
                break;

            case 29:
                sendOption2("Yes", "No");
                c.dialogueAction = 77;
                c.nextChat = 0;
                break;
            case 30:
                sendOption5("Pure Starter + Stats", "Main Starter + Stats", "Zerker Starter + Stats", "Skiller Starter", "Range Starter + Stats");
                c.dialogueAction = 98;


                break;
            case 37:
                sendStatement("Do you want to enter the frost dragon's lair?");
                c.nextChat = 38;
                c.talkingNpc = -1;
                break;
            case 38:
                sendOption2("Yes", "No");
                c.dialogueAction = 108;
                c.nextChat = 0;

                break;

            case 39:
                sendStatement("Thank you for donating! Would you like a stat hammer or a vesta's longsword?");
                c.nextChat = 40;
                c.talkingNpc = -1;
                break;
            case 40:
                sendOption2("Statius's Hammer", "Vesta's Longsword");
                c.dialogueAction = 115;
                c.nextChat = 0;
                break;

            case 42:
                sendNpcChat4("Hello!", "I am in charge of the Member's shop!", "You can only buy items with Member points!",
                    "Which shop would you like to see?", c.talkingNpc, "Membership Bo$$");
                c.nextChat = 42;
                break;
            case 41:
                sendOption2("Shop 1", "Shop 2");
                c.dialogueAction = 40;
                break;
            case 43:
                sendStatement("[@red@WARNING@bla@] You are about to enter the rev's cave! Are you sure?");
                c.nextChat = 44;
                break;

            case 44:
                sendOption2("Yes", "No");
                c.dialogueAction = 41;
                c.nextChat = 0;

                break;
            case 45:
                sendNpcChat4("Hello!", "New update set your homes with house teletabs!", "And increased the amount of cash recieved from stalls",
                    "Remember, please keep voting and clicking on the advertisements!", c.talkingNpc, "Lumbridge Sage");


                break;
        }







    }

    /*
     * Information Box
     */

    public void sendStartInfo(String text, String text1, String text2, String text3, String title) {
        c.getPA().sendFrame126(title, 6180);
        c.getPA().sendFrame126(text, 6181);
        c.getPA().sendFrame126(text1, 6182);
        c.getPA().sendFrame126(text2, 6183);
        c.getPA().sendFrame126(text3, 6184);
        c.getPA().sendFrame164(6179);
    }

    /*
     * Options
     */

    private void sendOption(String s, String s1) {
        c.getPA().sendFrame126("Select an Option", 2470);
        c.getPA().sendFrame126(s, 2471);
        c.getPA().sendFrame126(s1, 2472);
        c.getPA().sendFrame126("Click here to continue", 2473);
        c.getPA().sendFrame164(13758);
    }

    public void sendOption2(String s, String s1) {
        c.getPA().sendFrame126("Select an Option", 2460);
        c.getPA().sendFrame126(s, 2461);
        c.getPA().sendFrame126(s1, 2462);
        c.getPA().sendFrame164(2459);
    }

    private void sendOption3(String s, String s1, String s2) {
        c.getPA().sendFrame126("Select an Option", 2470);
        c.getPA().sendFrame126(s, 2471);
        c.getPA().sendFrame126(s1, 2472);
        c.getPA().sendFrame126(s2, 2473);
        c.getPA().sendFrame164(2469);
    }

    public void sendOption4(String s, String s1, String s2, String s3) {
        c.getPA().sendFrame126("Select an Option", 2481);
        c.getPA().sendFrame126(s, 2482);
        c.getPA().sendFrame126(s1, 2483);
        c.getPA().sendFrame126(s2, 2484);
        c.getPA().sendFrame126(s3, 2485);
        c.getPA().sendFrame164(2480);
    }

    public void sendOption5(String s, String s1, String s2, String s3, String s4) {
        c.getPA().sendFrame126("Select an Option", 2493);
        c.getPA().sendFrame126(s, 2494);
        c.getPA().sendFrame126(s1, 2495);
        c.getPA().sendFrame126(s2, 2496);
        c.getPA().sendFrame126(s3, 2497);
        c.getPA().sendFrame126(s4, 2498);
        c.getPA().sendFrame164(2492);
    }

    /*
     * Statements
     */

    public void sendStatement(String s) { // 1 line click here to continue chat box interface
        c.getPA().sendFrame126(s, 357);
        c.getPA().sendFrame126("Click here to continue", 358);
        c.getPA().sendFrame164(356);
    }

    /*
     * Npc Chatting
     */

    private void sendNpcChat1(String s, int ChatNpc, String name) {
        c.getPA().sendFrame200(4883, 591);
        c.getPA().sendFrame126(name, 4884);
        c.getPA().sendFrame126(s, 4885);
        c.getPA().sendFrame75(ChatNpc, 4883);
        c.getPA().sendFrame164(4882);
    }

    public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
        c.getPA().sendFrame200(4888, 9847);
        c.getPA().sendFrame126(name, 4889);
        c.getPA().sendFrame126(s, 4890);
        c.getPA().sendFrame126(s1, 4891);
        c.getPA().sendFrame75(ChatNpc, 4888);
        c.getPA().sendFrame164(4887);
    }

    public void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
        c.getPA().sendFrame200(4894, 9847); //Was 591
        c.getPA().sendFrame126(name, 4895);
        c.getPA().sendFrame126(s, 4896);
        c.getPA().sendFrame126(s1, 4897);
        c.getPA().sendFrame126(s2, 4898);
        c.getPA().sendFrame75(ChatNpc, 4894);
        c.getPA().sendFrame164(4893);
    }


    private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
        c.getPA().sendFrame200(4901, 9847);
        c.getPA().sendFrame126(name, 4902);
        c.getPA().sendFrame126(s, 4903);
        c.getPA().sendFrame126(s1, 4904);
        c.getPA().sendFrame126(s2, 4905);
        c.getPA().sendFrame126(s3, 4906);
        c.getPA().sendFrame75(ChatNpc, 4901);
        c.getPA().sendFrame164(4900);
    }

    /*
     * Player Chating Back
     */

    private void sendPlayerChat1(String s) {
        c.getPA().sendFrame200(969, 591);
        c.getPA().sendFrame126(c.playerName, 970);
        c.getPA().sendFrame126(s, 971);
        c.getPA().sendFrame185(969);
        c.getPA().sendFrame164(968);
    }

    private void sendPlayerChat2(String s, String s1) {
        c.getPA().sendFrame200(974, 591);
        c.getPA().sendFrame126(c.playerName, 975);
        c.getPA().sendFrame126(s, 976);
        c.getPA().sendFrame126(s1, 977);
        c.getPA().sendFrame185(974);
        c.getPA().sendFrame164(973);
    }

    private void sendPlayerChat3(String s, String s1, String s2) {
        c.getPA().sendFrame200(980, 591);
        c.getPA().sendFrame126(c.playerName, 981);
        c.getPA().sendFrame126(s, 982);
        c.getPA().sendFrame126(s1, 983);
        c.getPA().sendFrame126(s2, 984);
        c.getPA().sendFrame185(980);
        c.getPA().sendFrame164(979);
    }

    private void sendPlayerChat4(String s, String s1, String s2, String s3) {
        c.getPA().sendFrame200(987, 591);
        c.getPA().sendFrame126(c.playerName, 988);
        c.getPA().sendFrame126(s, 989);
        c.getPA().sendFrame126(s1, 990);
        c.getPA().sendFrame126(s2, 991);
        c.getPA().sendFrame126(s3, 992);
        c.getPA().sendFrame185(987);
        c.getPA().sendFrame164(986);
    }
    public void itemMessage(String title, String message, int itemid, int size) {
        c.getPA().sendFrame200(4883, 591);
        c.getPA().sendFrame126(title, 4884);
        c.getPA().sendFrame126(message, 4885);
        c.getPA().sendFrame126("Click here to continue.", 4886);
        c.getPA().sendFrame246(4883, size, itemid);
        c.getPA().sendFrame164(4882);
    }
}