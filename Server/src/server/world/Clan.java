package server.world;


/**
 * @author Sanity
 */

public class Clan {

	public Clan(String owner, String name, String captain, String password, boolean lootshare, boolean hasPassword) {
		this.owner = owner;
		this.name = name;
		this.captain = captain;
		this.password = password;
		this.lootshare = lootshare;
		this.hasPassword = hasPassword;
	}
		
	public int[] members = new int [50];
	public int[] mutedMembers = new int [10];
	public int[] bannedMembers = new int [10];
	public String name;
	public String owner;
	public String captain;
	public String password;
	public boolean lootshare;
	public boolean hasPassword;
}