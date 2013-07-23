package server.model.content;

import java.sql.*;
import server.model.players.Client;
import server.util.*;

public class GrandExchange {

    public GrandExchange (Client c) {
        
    }

        public static Connection con = null;
        public static Statement stm;
        private static boolean check = true;
        
        public static void createConnection() { // is the connection made? yes i can add to ge and sell but cant collect
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection("jdbc:mysql://localhost/grandexchange", "root", "Andrea191");
                stm = con.createStatement();
                Misc.println("Grand Exchange Connected");
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
            }
        }
        
        public static boolean checkBeforeBuy(final Client c, final int itemid, final int itemamt, final int itemprice){
            try {
                Statement statement = con.createStatement();
                String query = "SELECT * FROM sell";
                ResultSet rs = query(query);
                if(rs.next()){
                    int listedid = Integer.parseInt(rs.getString("itemid"));
                    int listedamt = Integer.parseInt(rs.getString("amount"));
                    int listedprice = Integer.parseInt(rs.getString("price"));
                    /**Buying 100 arrows at 10m....selling 50 arrows at 5m each**/
                    if(itemid == listedid && itemprice >= listedprice){
                        return true;//&& itemamt <= listedamt
                    } else{//means no buyer
                        return false;
                    }
                }else 
                    return false;
            } 
            catch (Exception sqlEx) {
                sqlEx.printStackTrace();
            }
                return false;
            }
        public static boolean checkBeforeSell(final Client c, final int itemid, final int itemamt, final int itemprice){
            try {
                Statement statement = con.createStatement();
                String query = "SELECT * FROM buy";
                ResultSet rs = query(query);
                if (rs.next()){
                    int listedid = Integer.parseInt(rs.getString("itemid"));
                    int listedamt = Integer.parseInt(rs.getString("amount"));
                    int listedprice = Integer.parseInt(rs.getString("price"));
                    /**Buying 100 arrows at 10m....selling 50 arrows at 5m each**/
                    if(itemid == listedid && itemprice <= listedprice){
                        return true;// && itemamt >= listedamt
                    } else{//means no buyer
                        return false;
                    }
                } else
                    return false;
            } 
            catch (Exception sqlEx) {
                sqlEx.printStackTrace();
            }
            return true;
            }

        public void run() {
            while(true) {        
                try {
                    if(con == null)
                        createConnection(); 
                    else
                        ping();
                    Thread.sleep(10000);//10 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        public static void ping(){
            try {
                String query = "SELECT * FROM buy WHERE itemid = 'null'";
                query(query);
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
            }
        }
        public static void process(Client c, int id, int amt, int price, String bos){
            
            if(bos.equals("sell")){
                if(c.getItems().playerHasItem(id, amt)){
                    boolean sell = checkBeforeSell(c,id, amt, price);;
                    //checkBeforeSell(c,id, amt, price);
                    if (sell == true){
                        sellWithBuyer(c, id, amt, price);
                        c.sendMessage("Sell and true/buyer");
                    } else{
                        sellNoBuyer(c, id, amt, price);
                        c.sendMessage("Sell and false/nobuyer");
                    }
                } else{
                    c.sendMessage("You don't have itemid: " + id + " Amt: " + amt);
                }
            } else if(bos.equals("buy")){
                int temp = amt* price;
                if(c.getItems().playerHasItem(995, temp)){
                    boolean checkbuy = checkBeforeBuy(c, id, amt, price);
                    //checkBeforeBuy(c, id, amt, price);
                    if (checkbuy == true){
                        buyWithBuyer(c, id, amt, price);
                        c.sendMessage("Buy and true/buyer");
                    } else{
                        buyNoBuyer(c, id, amt, price);
                        c.sendMessage("Buy and false/nobuyer");
                    }
                }else {
                    c.sendMessage("You don't have " + temp + " coins");
                }
            }
        }
        /**public static  void checkBeforeBuy(final Client c, final int itemid, final int itemamt, final int itemprice){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        String query = "SELECT * FROM sell";
                        ResultSet rs = query(query);
                            int listedid = Integer.parseInt(rs.getString("itemid"));
                            int listedamt = Integer.parseInt(rs.getString("amount"));
                            int listedprice = Integer.parseInt(rs.getString("price"));
                            /**Buying 100 arrows at 10m....selling 50 arrows at 5m each**/
                            /**if(itemid == listedid && itemamt <= listedamt && itemprice >= listedprice){
                                check = true;
                            } else{//means no buyer
                                check = false;
                            }
                        
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }**/
        public static void buyNoBuyer(final Client c, final int id, final int amt, final int price){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        if(c.getItems().playerHasItem(id, amt)){
                            query("INSERT INTO `buy` (`itemid`, `amount`, `price`, `bought`, `username`) VALUES ('"+id+"', '"+amt+"', '"+price+"', '', '"+c.playerName+"');");
                            int temp = price * amt;
                            c.getItems().deleteItem(995, temp);
                            c.sendMessage("Successfully added to the Grand Exchange Buy List");
                            //INSERT INTO `gedb`.`buy` (`itemid`, `amount`, `price`, `bought`, `username`) VALUES ('1', '1', '1', '', 'saad');
                        }else{
                            c.sendMessage("You dont have id: " + id + " Amt: " + amt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }
        public static void buyWithBuyer(final Client c, final int buyingid, final int buyingamt, final int buyingprice){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        String query = "SELECT * FROM sell";
                        ResultSet rs = query(query);
                        while (rs.next()){
                            int sellingid = Integer.parseInt(rs.getString("itemid"));
                            int sellingamt = Integer.parseInt(rs.getString("amount"));
                            int sellingprice = Integer.parseInt(rs.getString("price"));
                            String player2 = rs.getString("username");
                            /**Buying 100 arrows at 10m....selling 50 arrows at 5m each**/
                            int temp = buyingprice * buyingamt;
                            if(c.getItems().playerHasItem(995, temp)){
                                c.getItems().deleteItem(995, temp);
                            }else{
                                c.sendMessage("@dre@You dont have enough money!");
                                return;
                            }
                            if(sellingid != buyingid){
                                c.sendMessage("Error. Please Report to staff");
                                return;
                            }
                            if(sellingamt <= buyingamt){
                                int amtleft = (buyingamt - sellingamt);
                                
                                if(sellingprice == buyingprice){
                                    writeCollect(c.playerName, buyingid, sellingamt);
                                    writeCollect(player2, 995, sellingprice * sellingamt);
                                    removeFromGEBuy(c.playerName, sellingid, sellingamt, sellingprice);
                                    if(amtleft != 0)
                                        buyNoBuyer(c, sellingid, amtleft, buyingprice);
                                } else if(sellingprice <= buyingprice){//buying for 50m, selling for 25
                                    int tempprice = (buyingprice - sellingprice);
                                    
                                    writeCollect(c.playerName, buyingid, sellingamt);
                                    writeCollect(player2, 995, (sellingprice * sellingamt) + (tempprice * sellingamt));
                                    writeCollect(player2, 995, tempprice * sellingamt);
                                    removeFromGEBuy(c.playerName, sellingid, sellingamt, sellingprice);
                                    if(amtleft != 0)
                                        buyNoBuyer(c, sellingid, amtleft, buyingprice);
                                }
                                removeFromGESell(player2, sellingid, sellingamt, sellingprice);
                            } else if (sellingamt > buyingamt){
                                int amtleft = (sellingamt - buyingamt);
                                
                                if(sellingprice == buyingprice){
                                    writeCollect(c.playerName, buyingid, buyingamt);
                                    writeCollect(player2, 995, sellingprice * buyingamt);
                                    removeFromGEBuy(c.playerName, sellingid, sellingamt, sellingprice);
                                    writeCollect(player2, sellingid, amtleft);
                                } else if(sellingprice <= buyingprice){//buying for 50m, selling for 25
                                    int tempprice = (buyingprice - sellingprice);
                                    writeCollect(c.playerName, buyingid, buyingamt);
                                    writeCollect(player2, 995, sellingprice * buyingamt);
                                    removeFromGEBuy(c.playerName, sellingid, sellingamt, sellingprice);
                                    writeCollect(player2, sellingid, amtleft);
                                    writeCollect(player2, 995, tempprice * sellingamt);
                                }
                                removeFromGESell(player2, sellingid, sellingamt, sellingprice);
                            
                                
                            } /*else if(sellingprice <= buyingprice){
                            
                                int moneytobuyer = (buyingprice - sellingprice);
                                if(sellingamt == buyingamt){
                                    writeCollect(c.playerName, buyingid, buyingamt);
                                    writeCollect(player2, 995, moneytobuyer * buyingamt);
                                    writeCollect(c.playerName, 995, moneytobuyer * buyingamt);
                                    removeFromGEBuy(c.playerName, sellingid, sellingamt, sellingprice);
                                } else if (sellingamt < buyingamt){
                                    int amtleft = (buyingamt - sellingamt);
                                    writeCollect(c.playerName, buyingid, buyingamt);
                                    writeCollect(c.playerName, 995, moneytobuyer * sellingamt);
                                    writeCollect(player2, 995, buyingprice * sellingamt);
                                    removeFromGEBuy(c.playerName, sellingid, sellingamt, sellingprice);
                                    buyNoBuyer(c, sellingid, amtleft, buyingprice);
                                }
                                removeFromGESell(player2, sellingid, sellingamt, sellingprice);
                            }*/
                        
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }
        public static void removeFromGESell(final String username, final int id, final int amt, final int price){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        
                        query("DELETE FROM `sell` WHERE `username` = '"+ username + "' AND `itemid` = '"+id+"' AND `amount` = '"+amt+"' AND `price` = '"+price+"'");
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }
        /**public static  void checkBeforeSell(final Client c, final int itemid, final int itemamt, final int itemprice){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        String query = "SELECT * FROM buy";
                        ResultSet rs = query(query);
                            int listedid = Integer.parseInt(rs.getString("itemid"));
                            int listedamt = Integer.parseInt(rs.getString("amount"));
                            int listedprice = Integer.parseInt(rs.getString("price"));
                            /**Buying 100 arrows at 10m....selling 50 arrows at 5m each**/
                            /**if(itemid == listedid && itemamt >= listedamt && itemprice <= listedprice){
                                check = true;
                            } else{//means no buyer
                                check = false;
                            }
                        
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }**/
        public static void sellWithBuyer(final Client c, final int sellingid, final int sellingamt, final int sellingprice){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        String query = "SELECT * FROM buy";
                        ResultSet rs = query(query);
                        while(rs.next()){
                            int buyingid = Integer.parseInt(rs.getString("itemid"));
                            int buyingamt = Integer.parseInt(rs.getString("amount"));
                            int buyingprice = Integer.parseInt(rs.getString("price"));
                            String player2 = rs.getString("username");
                            /**Buying 100 arrows at 10m....selling 50 arrows at 5m each**/
                            if(c.getItems().playerHasItem(sellingid, sellingamt)){
                                c.getItems().deleteItem(sellingid, sellingamt);
                            }else{
                                c.sendMessage("You dont have id: " + sellingid + " Amt: " + sellingamt);
                                return;
                            }
                            if(sellingid != buyingid){
                                c.sendMessage("Error. Please Report to staff");
                                return;
                            }
                            if(sellingamt >= buyingamt){//selling 100 but buyer wants 50
                                int amtleft = (sellingamt - buyingamt);
                                
                                if(sellingprice == buyingprice){
                                    writeCollect(player2, buyingid, buyingamt);
                                    writeCollect(c.playerName, 995, buyingprice * buyingamt);
                                    removeFromGESell(c.playerName, sellingid, sellingamt, sellingprice);
                                    sellNoBuyer(c, sellingid, amtleft, sellingprice);
                                } else if(sellingprice <= buyingprice){//buying for 50m, selling for 25
                                    int tempprice = (buyingprice - sellingprice);
                                    
                                    writeCollect(player2, buyingid, buyingamt);
                                    writeCollect(c.playerName, 995, tempprice * buyingamt);
                                    writeCollect(c.playerName, 995, buyingprice * buyingamt);
                                    removeFromGESell(c.playerName, sellingid, sellingamt, sellingprice);
                                    sellNoBuyer(c, sellingid, amtleft, sellingprice);
                                }
                                removeFromGEBuy(player2, buyingid, buyingamt, buyingprice);
                            }else if (sellingamt < buyingamt){
                                int amtleft = (buyingamt - sellingamt);
                                if(sellingprice == buyingprice){
                                    writeCollect(player2, buyingid, sellingamt);
                                    writeCollect(c.playerName, 995, buyingprice * sellingamt);
                                    removeFromGESell(c.playerName, sellingid, sellingamt, sellingprice);
                                    removeFromGEBuy(player2, buyingid, buyingamt, buyingprice);
                                } else if(sellingprice <= buyingprice){//buying for 50m, selling for 25
                                    int tempprice = (buyingprice - sellingprice);
                                    writeCollect(player2, buyingid, sellingamt);
                                    writeCollect(c.playerName, 995, (tempprice * sellingamt) + (buyingprice * sellingamt));
                                    removeFromGESell(c.playerName, sellingid, sellingamt, sellingprice);
                                    removeFromGEBuy(player2, buyingid, buyingamt, buyingprice);
                                    writeCollect(player2, buyingid, amtleft);
                                }
                            }
                        
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }
        public static void sellNoBuyer(final Client c, final int id, final int amt, final int price){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        if(c.getItems().playerHasItem(id, amt)){
                            query("INSERT INTO `sell` (`itemid`, `amount`, `price`, `sold`, `username`) VALUES ('"+id+"', '"+amt+"', '"+price+"', '', '"+c.playerName+"');");
                            c.getItems().deleteItem(id, amt);
                            c.sendMessage("Successfully added to the Grand Exchange Sell list");
                            //INSERT INTO `gedb`.`buy` (`itemid`, `amount`, `price`, `bought`, `username`) VALUES ('1', '1', '1', '', 'saad');
                        }else{
                            c.sendMessage("You dont have id: " + id + " Amt: " + amt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }
        public static void removeFromGEBuy(final String username, final int id, final int amt, final int price){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        
                        query("DELETE FROM `buy` WHERE `username` = '"+ username + "' AND `itemid` = '"+id+"' AND `amount` = '"+amt+"' AND `price` = '"+price+"'");
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }
        public static void writeCollect(final String username, final int id, final int amt){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        
                        query("INSERT INTO `collections` (`itemid`, `amount`, `collected`, `username`) VALUES ('"+id+"', '"+amt+"', '', '"+username+"');");
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }
        /**public static void getCollect(final Client c, final String Username){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        String query = "SELECT * FROM collections WHERE username = '"+ Username + "'";
                        ResultSet rs = query(query);
                        while (rs.next()){
                            int id = Integer.parseInt(rs.getString("itemid"));
                            int amt = Integer.parseInt(rs.getString("amount"));
                            String name = rs.getString("username");
                            //int listedprice = Integer.parseInt(rs.getString("price"));
                            if(name.equalsIgnoreCase(Username)){
                                c.getItems().addItem(id, amt);
                                c.SaveGame();
                                c.sendMessage("Successfully collected GE Items.");
                                query("DELETE FROM `collections` WHERE username = '"+ Username + "' AND `itemid` = '"+id+"' AND `amount` = '"+amt+"'");
                            }else{
                                c.sendMessage("@dre@You have nothing to collect!");
                            }
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }**/
        public static void getCollect(final Client c, final String Username){
            try {
                String query = "SELECT * FROM collections";
                ResultSet rs = query(query);
                while(rs.next()){
                    int id = Integer.parseInt(rs.getString("itemid"));
                    int amt = Integer.parseInt(rs.getString("amount"));
                    String name = rs.getString("username");
                    if(name.equalsIgnoreCase(Username)){
                        c.getItems().addItem(id, amt);
                        c.sendMessage("<shad=65280>Successfully collected GE Items.");
                        query("DELETE FROM `collections` WHERE username = '"+ Username + "' AND `itemid` = '"+id+"' AND `amount` = '"+amt+"'");
                        }else{
                        c.sendMessage("<shad=16711680>You have nothing to collect!");
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
            }
        }
        public static void cancelBuy(final Client c, final int id){
            try {
                String query = "SELECT * FROM buy";
                ResultSet rs = query(query);
                while(rs.next()){
                    int itemid = Integer.parseInt(rs.getString("itemid"));
                    int itemamt = Integer.parseInt(rs.getString("amount"));
                    int itemprice = Integer.parseInt(rs.getString("price"));
                    String name = rs.getString("username");
                    if(name.equalsIgnoreCase(c.playerName) && id == itemid){
                        c.getItems().addItem(995, (itemamt * itemprice));
                        removeFromGEBuy(c.playerName, itemid, itemamt, itemprice);
                        c.sendMessage("<shad=65280>Successfully removed from GE.");
                        }else{
                        c.sendMessage("<shad=16711680>You are not buying that Item!");
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
            }
        }
        public static void cancelSell(final Client c, final int id){
            try {
                String query = "SELECT * FROM sell";
                ResultSet rs = query(query);
                while(rs.next()){
                    int itemid = Integer.parseInt(rs.getString("itemid"));
                    int itemamt = Integer.parseInt(rs.getString("amount"));
                    int itemprice = Integer.parseInt(rs.getString("price"));
                    String name = rs.getString("username");
                    if(name.equalsIgnoreCase(c.playerName) && id == itemid){
                        c.getItems().addItem(itemid, itemamt);
                        removeFromGESell(c.playerName, itemid, itemamt, itemprice);
                        c.sendMessage("<shad=65280>Successfully removed from GE.");
                        }else{
                        c.sendMessage("<shad=16711680>You are not selling that Item!");
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
            }
        }
        public static void writeInterfaceBuy(final Client c, final String Username){
            try {
                String query = "SELECT * FROM buy";
                ResultSet rs = query(query);
                int line = 13592;
                while(rs.next()){
                    int price = Integer.parseInt(rs.getString("price"));
                    int id = Integer.parseInt(rs.getString("itemid"));
                    int amt = Integer.parseInt(rs.getString("amount"));
                    String name = rs.getString("username");
                    if(name.equalsIgnoreCase(Username)){
                        c.getPA().sendFrame126("You are buying Item ID: " + id + " Amt: " + amt + " Total Price: " + price, line);
                        line++;
                    } else {
                        c.getPA().sendFrame126("You are buying nothing", line);
                    }
                }
                c.getPA().showInterface(13585);
                c.flushOutStream();
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
            }
        }
        
        
        public static void writeInterfaceSell(final Client c, final String Username){
            try {
                String query = "SELECT * FROM sell";
                ResultSet rs = query(query);
                int line = 8147;
                while(rs.next()){
                    int price = Integer.parseInt(rs.getString("price"));
                    int id = Integer.parseInt(rs.getString("itemid"));
                    int amt = Integer.parseInt(rs.getString("amount"));
                    String name = rs.getString("username");
                    if(name.equalsIgnoreCase(Username)){
                        c.getPA().sendFrame126("You are selling Item ID: " + id + " Amt: " + amt + " Total Price: " + price, line);
                    }
                line++;
                }
                c.getPA().showInterface(8134);
                c.flushOutStream();
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
            }
        }

        /*public static void getCollect1(final Client c, final String Username){
            if(con == null){
                if(stm != null){
                    try {
                        stm = con.createStatement();
                    } catch(Exception e){
                        con = null;
                        stm = null;
                        //put a sendmessage here telling them to relog in 30 seconds
                        return;
                    }
                } else {
                    //put a sendmessage here telling them to relog in 30 seconds
                    
                }
            }
            new Thread(){
                @Override
                public void run()
                {
                    try {
                        String query = "SELECT * FROM collections WHERE username = '"+c.playerName+"'";
                        ResultSet rs = query(query);
                        /**while(rs.next()){
                        if (rs != null && rs.next()) {
                            int id = Integer.parseInt(rs.getString("itemid"));
                            int amt = Integer.parseInt(rs.getString("amount"));
                            String name = rs.getString("username");
                            //int listedprice = Integer.parseInt(rs.getString("price"));
                            /**Buying 100 arrows at 10m....selling 50 arrows at 5m each**/
                            /*if(name.equalsIgnoreCase(Username)){
                                c.getItems().addItem(id, amt);
                                c.sendMessage("Successfully collected GE Items.");
                                query("DELETE FROM `collections` WHERE username = '"+ Username + "' AND `itemid` = '"+id+"' AND `amount` = '"+amt+"'");
                                }else{
                                c.sendMessage("@dre@You have nothing to collect!");
                            }
                        } else{
                            c.sendMessage("@dre@You have nothing to collect!");
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        con = null;
                        stm = null;
                    }
                }
            }.start();
        }*/
        public static ResultSet query(String s) throws SQLException {//heres error
            try {
                if (s.toLowerCase().startsWith("select")) {
                    if(stm == null) {
                        createConnection();
                    }
                    ResultSet rs = stm.executeQuery(s);
                    return rs;
                } else {
                    if(stm == null) {
                        createConnection();
                    }
                    stm.executeUpdate(s);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                con = null;
                stm = null;
                
            }
            return null;
        }
        
        public synchronized static void destroyConnection() {
            try {
                stm.close();
                con.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }        
}