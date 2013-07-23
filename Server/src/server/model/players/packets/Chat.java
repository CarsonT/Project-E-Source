package server.model.players.packets;
import server.util.Misc;

import server.model.players.Client;
import server.model.players.PacketType;
import server.Connection;
import server.model.content.ReportHandler;
/**
 * @author JaydenD12
 */
public class Chat implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		/**
		 * Returns if you're muted.
		 */
		if (Connection.isMuted(c)) {
			c.sendMessage("You are muted and cannot talk.");
			return;
		}		
		ReportHandler.addText(c.playerName, c.getChatText(), packetSize - 2);
		/**
		 * You NEED to get the data first so that the chat message can be compared with the array.
		 */
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
			if (c.playerRights == 0) {
		/**
		 * Chat filtering
		 */
		String chatText = Misc.textUnpack(c.getChatText(), c.getChatTextSize());
		for (int i = 0; i < c.blockedcurses.length; i++) {
			if (chatText.replaceAll(" ", "").trim().indexOf(c.blockedcurses[i]) >= 0) {
				c.sendMessage("Foul Language is not tolerated! Continue this you will be automuted");
				c.getPA().checkCount2(c.advertiseCount++, c.playerName, c.connectedFrom);
				return;
			}
		}
		//String chatText = Misc.textUnpack(c.getChatText(), c.getChatTextSize());
		for (int i = 0; i < c.blockedTerms.length; i++) {
			if (chatText.replaceAll(" ", "").trim().indexOf(c.blockedTerms[i]) >= 0) {
				c.sendMessage("@red@Avertising is not tolerated, Do it more and you will be automuted");
				c.getPA().checkCount(c.advertiseCount++, c.playerName, c.connectedFrom);
				return;
			
			}
		}
		}
		/**
		 * Update the chatbox.
		 */
		c.setChatTextUpdateRequired(true);
	}
}