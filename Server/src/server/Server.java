package server;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.sql.Statement;
import java.text.DecimalFormat;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.Vote.*;

import server.model.minigames.FightCaves;
import server.model.minigames.PestControl;

import server.model.npcs.NPCDrops;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.Player;
import server.world.WalkingCheck;
import server.model.players.PlayerHandler;
import server.model.players.PlayerSave;
import server.net.ConnectionHandler;
import server.net.ConnectionThrottleFilter;
import server.util.*;
import server.util.MadTurnipConnection;
import server.util.log.*;
import server.world.*;
import server.event.*;
import server.event.CycleEventHandler;
import server.model.minigames.*;
import server.net.HostBlacklist;
import server.model.players.packets.PJClans;
import server.model.content.*;
import server.model.controlpanel.ControlPanel;

public class Server {
	
	
	public static boolean sleeping;
	public static int cycleRate, cyclesDone = 0;
	public static int days, hours, minutes, seconds;
	private static long endTime = 0, startTime = 0;
	public static boolean UpdateServer = false;
	public static long lastMassSave = System.currentTimeMillis();
	public static IoAcceptor acceptor;
	public static long averageCycleTime;
	public static ConnectionHandler connectionHandler;
	public static ConnectionThrottleFilter throttleFilter;
	public static SimpleTimer engineTimer, debugTimer;
	public static long cycleTime, cycles, totalCycleTime, sleepTime;
	public static DecimalFormat debugPercentFormat;
	public static boolean shutdownServer = false;		
	public static boolean shutdownClientHandler;			
	public static int serverlistenerPort; 
	public static ItemHandler itemHandler = new ItemHandler();
	public static PlayerHandler playerHandler = new PlayerHandler();
    public static NPCHandler npcHandler = new NPCHandler();
	public static ShopHandler shopHandler = new ShopHandler();
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();
	public static PestControl pestControl = new PestControl();
	public static MainLoader vote = new MainLoader("192.232.251.96", "frank_GTLVote", "pcig1234", "frank_GTLVote");
	public static NPCDrops npcDrops = new NPCDrops();
	public static FightCaves fightCaves = new FightCaves();
	public static PublicEvent publicEvent = new PublicEvent();	
	public static ClanChatHandler clanChat = new ClanChatHandler();
	public static PJClans pJClans = new PJClans();
	public static lottery lottery = new lottery();
	public static ControlPanel panel = new ControlPanel(false); 

	static {
		serverlistenerPort = 43594;
		cycleRate = 600;
		shutdownServer = false;
		engineTimer = new SimpleTimer();
		debugTimer = new SimpleTimer();
		sleepTime = 0;
		debugPercentFormat = new DecimalFormat("0.0#%");
		
	}
	
	public static void main(String[] args) throws NullPointerException, IOException {
		startTime = System.currentTimeMillis();
		System.setOut(new Logger(System.out));
		System.setErr(new Logger(System.err));
		System.out.println("Launching Project-Equinox...");
		HostBlacklist.loadBlacklist();
		World.getWorld();
		WalkingCheck.check();
		WalkingCheck.check2();
		NPCDrops.initialize();
		System.out.println("All NPC Drops Loaded Successfully!");
		pJClans.initialize();
		HighscoresConfig.loadHighscores();
		//server.util.DonationMysql.createConnection();
		//MadTurnipConnection md = new MadTurnipConnection();
		//md.start();	
		//GrandExchange.createConnection();		
		try {
			new RS2Server().start();
		} catch(Exception ex) {
			System.out.println("Error starting Project-Equinox...");
			ex.printStackTrace();
			System.exit(1);
		}
		endTime = System.currentTimeMillis() - startTime ;		
		System.out.println("Server launching took " +endTime +" ms");
		System.out.println("Server now accepting connections on port: " + serverlistenerPort);
		Player.getUptime();
	}
	
	public static void setupLoginChannels() {
		acceptor = new SocketAcceptor();
		connectionHandler = new ConnectionHandler();	
		SocketAcceptorConfig sac = new SocketAcceptorConfig();
		sac.getSessionConfig().setTcpNoDelay(false);
		sac.setReuseAddress(true);
		sac.setBacklog(100);		
		throttleFilter = new ConnectionThrottleFilter(Config.CONNECTION_DELAY);
		sac.getFilterChain().addFirst("throttleFilter", throttleFilter);
		try {
			acceptor.bind(new InetSocketAddress(serverlistenerPort), connectionHandler, sac);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean playerExecuted = false;
	public static void debug() {
		if (debugTimer.elapsed() > 360*1000 || playerExecuted) {
			long averageCycleTime = totalCycleTime / cycles;
			System.out.println("[Project-E] Average Cycle Time: " + averageCycleTime + " milliseconds.");
			double engineLoad = ((double) averageCycleTime / (double) cycleRate);
			System.out.println("[Project-E] Players online: " + PlayerHandler.playerCount+ ", engine load: "+ debugPercentFormat.format(engineLoad));
			totalCycleTime = 0;
			cycles = 0;
			System.gc();
			System.runFinalization();
			debugTimer.reset();
			playerExecuted = false;
		}
	}	
	
	/*private static void handleMemoryUsage() {
		DecimalFormat decimalFormat = new DecimalFormat("0.0#%");
		NumberFormat memoryFormat = NumberFormat.getInstance();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		long usedMemory = totalMemory - freeMemory;
		System.out.println("Total Used Memory: "+ memoryFormat.format(usedMemory / 1024) + "/" + memoryFormat.format(totalMemory / 1024) + " MB, " + decimalFormat.format(((double) usedMemory / (double) totalMemory)));
		System.out.println("Free Memory: " + memoryFormat.format(freeMemory / 1024) + " MB, " + decimalFormat.format((double) freeMemory / (double) totalMemory));
	}*/
	
	
}
