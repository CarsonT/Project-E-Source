package server.event.impl;
import server.model.players.Client;
import server.World;
import server.event.Event;
import server.task.impl.SavePlayers;
import server.model.players.Player;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.model.players.PlayerAssistant;
import server.model.minigames.*;
public class CastleWars2 extends Event {
	private Client c;

	public static final int SAVE_CYCLE_TIME = 10;	


	public CastleWars2() {
		super(SAVE_CYCLE_TIME);
	}
	
	@Override
	public void execute() {
		CastleWars.process();
		
	}

}
