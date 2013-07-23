package server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.model.players.Client;

public class DonationMysql {
	
	/**
	 * Donation System Sql handler
	 * 
	 * @author Akeid
	 * Some Credits to Sanity's Original Connector
	 * @since 1/6/11
	 */
	
	
	
	public static String MySQLDataBase = "relen712_donations";
	public static String MySQLURL = "www.relentless-scape.net:3306";
	public static String MySQLUser = "relen712_dnadmin";
	public static String MySQLPassword = "bSnWr]R7Z9pm";
	
    public static Connection con = null;
	public static Statement stmt;
	public static boolean connectionMade;
    
    
	/**
	 * Instructions:
	 * In Server.java add...
	 * DonationMysql.createConnection();
	 * DonationMysql.destroyConnection();
	 * 
	 * In Initialize Client Method add...
	 * server.model.players.DonationMysql.needsItems(this);
	 */
	
    
	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://"+MySQLURL+"/"+MySQLDataBase, ""+MySQLUser, ""+MySQLPassword);
			stmt = con.createStatement();
			connectionMade = true;
			Misc.println("Mysql Timed Ban and Mute and donations Handler Successfully Connected...");
		} catch (Exception e) {
			connectionMade = false;
			Misc.println("Failed to connect to Mysql database for Timed Ban and Mutes!");
			e.printStackTrace();
		}
	}
	
	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stmt.executeQuery(s);
				return rs;
			} else {
				stmt.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			destroyConnection();
			createConnection();
			//e.printStackTrace();
		}
		return null;
	}

	public static void destroyConnection() {
		try {
			stmt.close();
			con.close();
			connectionMade = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testReturn(Client c) {
		if(!connectionMade){
			createConnection();
			c.sendMessage("Was closed but now connected");
		}
		try {
			Statement s = con.createStatement();
			Statement d = con.createStatement(); //needed to delete
			ResultSet m = s.executeQuery("SELECT * FROM `items_to_give_users` WHERE `username`='"+ c.playerName + "'");
			while (m.next()) {

				String itemName = m.getString("item_name");
				
				c.sendMessage("This is The item Name "+itemName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		c.sendMessage("Connection Closed");
	}




public static void needsItems(Client c) {
		if(!connectionMade){
			createConnection();
			//c.sendMessage("Was closed but now connected");
		}
		try {
			Statement s = con.createStatement();
			Statement d = con.createStatement(); 
			ResultSet m = s.executeQuery("SELECT * FROM `items_to_give_users` WHERE `username`='"+ c.playerName + "'");
			while (m.next()) {
				int item = m.getInt("item_number_code");
				int itemAmount = m.getInt("item_amount");
				int itemSlotReq = m.getInt("item_inv_space_req");
				String itemName = m.getString("item_name");
				/**
				 * Hardcoded Things Go Here // For Skills and stats only
				 * Dont use for items , make a special item code id in the database for stats
				 * a dd a return under every custom part except the default
				 */

				if(item == 12081208) {
				
			c.getPA().addSkillXP(13075347, 18);
					d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
					//return;
				}


	
				
				if(item == 12271208) {
				c.getPA().addSkillXP(13075347, 19);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				//return;
				}
				if(item == 15038) {
				c.memberStatus = 1;
				c.getItems().addItem(15038, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 15039) {
				c.memberStatus = 1;
				c.getItems().addItem(15039, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 15037) {
				c.memberStatus = 1;
				c.getItems().addItem(150387, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 1042) {
				c.memberStatus = 1;
				c.getItems().addItem(1042, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 1038) {
				c.memberStatus = 1;
				c.getItems().addItem(1038, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 1040) {
				c.memberStatus = 1;
				c.getItems().addItem(1040, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 1044) {
				c.memberStatus = 1;
				c.getItems().addItem(1044, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 1048) {
				c.memberStatus = 1;
				c.getItems().addItem(1048, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
if(item == 1046) {
				c.memberStatus = 1;
				c.getItems().addItem(1046, 1);
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				return;
				}
				if(item == 5555555) {
				c.memberStatus = 1;
				d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
				//return;
				}
				if(item == 12081207){
					c.getPA().addSkillXP(13075347, 15);
					d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';"); //so it delets drom db okk
					//return;
				}
				
				if(item > 0) {
					if(c.getItems().freeSlots() >= itemSlotReq){
						c.getItems().addItem(item, itemAmount);
						d.executeUpdate("DELETE FROM `items_to_give_users` WHERE username = '"+c.playerName+"';");
						c.sendMessage("Thank You for your donation "+c.playerName+" ! Your "+itemName+" is waiting in your inventory.");
						c.sendMessage("Please check your email adress(paypal) for a payment receipt.");
						c.gfx0(481);
					} else {
						c.sendMessage("-You have a(n) @gre@"+itemName+" @bla@waiting for you!");
						c.sendMessage("-You need to have at least @red@"+itemSlotReq+" @bla@inventory slot(s) open to receive your item!");
						c.sendMessage("-Please free up some inventory space, and relog on to your account.");
						c.sendMessage("-You will then receive your "+itemName+" donator item!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}






	
	}
