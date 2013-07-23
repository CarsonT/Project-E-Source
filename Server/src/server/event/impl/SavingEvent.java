package server.event.impl;
import server.model.players.Client;
import server.World;
import server.event.Event;
import server.task.impl.SavePlayers;
import server.model.players.Player;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.model.players.PlayerAssistant;

public class SavingEvent extends Event {
	private Client c;

	public static final int SAVE_CYCLE_TIME = 120000;	


	public SavingEvent() {
		super(SAVE_CYCLE_TIME);
	}
	
	@Override
	public void execute() {
		World.getWorld().submit(new SavePlayers());
		
	}

}
