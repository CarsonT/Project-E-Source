package server.model.content;

import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.*;

public class Report implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		try {
			ReportHandler.handleReport(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}