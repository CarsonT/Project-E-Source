package server.model.content;

import java.util.Calendar;
import java.io.*;

import server.util.Misc;
import server.model.players.Client;
import server.model.players.PlayerHandler;

public class TicketCenter {

	private Client c;
	public TicketCenter(Client c) {
		this.c = c;
	}
	
	public long lastTicket;
	
	public void writeTicket(String ticketInfo) {
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter ticketFile = new BufferedWriter(new FileWriter("./Data/Help/" + Misc.optimizeText(c.playerName) + ".txt", true));
			try {		
				ticketFile.newLine();
				ticketFile.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : " + C.get(Calendar.DAY_OF_MONTH));
				ticketFile.newLine();
				ticketFile.write((c.playerName) +" needs help with '"+ ticketInfo+"'");
				ticketFile.newLine();
				ticketFile.write("--------------------------------------------------");
			} finally {
				ticketFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processTicket(String ticketInfo, boolean writeFile, boolean sendStaffMessage, boolean alertStaff) {
		try {
			if (System.currentTimeMillis() - lastTicket < 300000) {
				c.sendMessage("Please only submit a ticket once every 5 minutes!");
				return;
			}
			if (writeFile) {
				writeTicket(ticketInfo);
			}
			if (sendStaffMessage) {
				PlayerHandler.messageAllStaff((c.playerName) +" needs help with '"+ ticketInfo+"'", true);
			}
			if (alertStaff) {
				PlayerHandler.messageAllStaff((c.playerName) +" needs help with '"+ ticketInfo+"'", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}