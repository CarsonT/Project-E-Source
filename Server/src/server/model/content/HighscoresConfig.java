package server.model.content;

import java.io.*;

import server.util.Misc;
import server.model.players.Client;

public class HighscoresConfig {

	public static Rank rank[] = new Rank[51];
	
	public static void updateHighscores(Client c) {
		for(int i = 0; i < 50; i ++) {
			if(rank[i] == null) {
				addRank(i, c);
				break;
			}
			if(rank[i].totalLevel <= c.totalLevel && rank[i].xpTotal < c.xpTotal) {
				addRank(i, c);
				break;				
			}
			if(rank[i].playerName.equals(c.playerName))
				break;
		}
	}
	
	public static void addRank(int ranknum, Client c) {
		for(int i = 49; i >= ranknum; i --) {
			if(rank[i] != null) {
				if(rank[i].playerName.equals(c.playerName))
					rank[i] = null;
			}
			rank[i + 1] = rank[i];
		}
		Rank newRank = new Rank(ranknum, c.playerName, c.totalLevel, c.xpTotal);
		rank[ranknum] = newRank;
		saveHighscores();
	}
	
	public static void addRank(int ranknum, String playerName, int totalLevel, int xpTotal) {
		for(int i = 49; i >= ranknum; i --) {
			if(rank[i] != null) {
				if(rank[i].playerName.equals(playerName))
					rank[i] = null;
			}
			rank[i + 1] = rank[i];
		}
		Rank newRank = new Rank(ranknum, playerName, totalLevel, xpTotal);
		rank[ranknum] = newRank;
		saveHighscores();
	}
	
	public static void saveHighscores() {	
		BufferedWriter highscoresfile = null;
		try {
			highscoresfile = new BufferedWriter(new FileWriter("./data/Highscores/highscores.txt"));
			highscoresfile.write("//Rank# PlayerName TotalLevel XPTotal");
			highscoresfile.newLine();
			for(int i = 0; i < 50; i ++) {
				if(rank[i] != null) {
					highscoresfile.write(i+"	"+rank[i].playerName+"	"+rank[i].totalLevel+"	"+rank[i].xpTotal);
					highscoresfile.newLine();
				}
			}
			highscoresfile.write(".");
			highscoresfile.close();
		} catch(IOException ioexception) {}
	}
	
	public static void loadHighscores() {
		Misc.println("Highscores Successfully Loaded!");
		String line = "";
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("./data/Highscores/highscores.txt"));
		} catch(FileNotFoundException fileex) {
			return;
		}
		try {
			line = file.readLine();
		} catch(IOException ioexception) {
			return;
		}
		while(EndOfFile == false && line != null) {
			line = line.trim();
			try {
				line = file.readLine();
				if(line.equals(".")) {
					file.close();
					return;
				}
				String[] split = line.split("	");
				if(!(line.startsWith("//")) || !line.startsWith(".")) {
					loadHighscoreRank(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), Integer.parseInt(split[3]));
				}
			} catch(IOException ioexception1) { EndOfFile = true; }
		}
		try { 
		file.close(); 
		} catch(IOException ioexception) { 
		}
	}
	
	public static void loadHighscoreRank(int ranknum, String playerName, int totalLevel, int xpTotal) {
			for(int i = 0; i < 50; i ++) {
			if(rank[i] == null) {
				addRank(i, playerName, totalLevel, xpTotal);
				break;
			}
			if(rank[i].totalLevel <= totalLevel && rank[i].xpTotal < xpTotal) {
				addRank(i, playerName, totalLevel, xpTotal);
				break;				
			}
			if(rank[i].playerName.equals(playerName))
				break;
		}
	}
	
}

class Rank {
	public int rank, totalLevel, xpTotal;
	public String playerName;
	public Rank(int rank, String playerName, int totalLevel, int xpTotal) {
		this.rank = rank;
		this.playerName = playerName;
		this.totalLevel = totalLevel;
		this.xpTotal = xpTotal;
	}
}