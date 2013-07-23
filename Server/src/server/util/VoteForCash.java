package server.util;

import java.sql.*;
import java.security.MessageDigest;
import server.model.players.Client;

public class VoteForCash {

	public static Connection con = null;
	public static Statement stm;

	public synchronized static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://relentless-scape.net/relen712_voting2", "relen712_voting2", "MMCc&;^dE6#a");
			stm = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public synchronized static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stm.executeQuery(s);
				return rs;
			} else {
				stm.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			//misc.println("MySQL Error:"+s);
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static void destroyCon() {
		try {
			stm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static boolean checkMoparVotes(String playerName) {
		try {
			if (con == null)
				return false;
			Statement statement = con.createStatement();
			String query = "SELECT * FROM MoparVotes WHERE username = '" + playerName + "'";
			ResultSet results = statement.executeQuery(query);
			while(results.next()) {
				int recieved = results.getInt("recieved");
				if(recieved == 0) {
				return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized static boolean moparvoteGiven(String playerName) {
		try {
			query("UPDATE MoparVotes SET recieved = 1 WHERE username = '" + playerName + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}