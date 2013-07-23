package server.util;

import java.sql.*;
import server.Config;
import java.security.MessageDigest;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.Server;

public class Vote {
	public static Connection con = null;
	public static Statement stmt;
	private static long lastUsed = System.currentTimeMillis();

	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://techpopper.com/majora_relenvoting", "majora_relenvote", "jly549");
				stmt = con.createStatement();
		} catch (Exception e) {
			Misc.println("Connection Problem, please check vote.java");
		}
	}

	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stmt.executeQuery(s);
				return rs;
			} else {
				stmt.executeUpdate( s);
			}
			return null;
		} catch (Exception e) {
			destroyConnection();
			createConnection();
		}
		return null;
	}

	public static void destroyConnection() {
		try {
			stmt.close();
			con.close();
		} catch (Exception e) {
		}
	}
	
	public static Connection getConnection() throws Exception {
		if (con == null) {
			throw new Exception("connection is null");
		}
		if (System.currentTimeMillis() - lastUsed > 300000) {
			try {
				lastUsed = System.currentTimeMillis();
				con.close();
				createConnection();
			} catch (Exception e) {
				throw new Exception("error refreshing database connection");
			}
		}
		return con;

	}

	public static void checkVote(Client c) {
			try {
		createConnection();
			ResultSet results = stmt.executeQuery("SELECT COUNT(playerName) AS total FROM `votes` WHERE `playerName`= \"" + c.playerName + "\" AND `recieved`= 0");
			results.first();
			int total = results.getInt("total");
			if (total == 1) {
			Statement st = getConnection().createStatement();
				st.executeUpdate("UPDATE `votes` SET `recieved` = 1 WHERE `playerName` = '" + c.playerName + "'");
				c.getItems().addItem(995, 5000000);
				c.votePoints += 1;
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (Server.playerHandler.players[i] != null) {
					Client c2 = (Client) Server.playerHandler.players[i];
					c2.sendMessage("<shad=5081134><img=2>Thank " + c.playerName + " for Voting! The more votes we get the better the rewards!<img=2></col>");
				}}
				c.sendMessage("<shad=5081134><img=2>Thanks for voting! Remember to vote again in 1 hour.<img=2></col>");
			st.close();
		}
		destroyConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}