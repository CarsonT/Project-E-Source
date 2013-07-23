package server.model.content;

import java.util.Calendar;
import java.util.GregorianCalendar;

import server.model.players.Client;


public class JoinDate {
	
	public JoinDate(Client player) {
		this.player = player;
		if(player.joinDate.length() == 0)
			setJoinDate();
		else
			getJoinDate();
	}
	
	public void setJoinDate() {
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		monthOfYear = 1 + calendar.get(Calendar.MONTH);
		if(monthOfYear == 13)
			monthOfYear = 12;
		year = calendar.get(Calendar.YEAR);
		player.joinDate = Integer.toString(monthOfYear) + " " + Integer.toString(dayOfMonth) + " " + Integer.toString(year);
	}
	
	public void getJoinDate() {
		String month = player.joinDate.substring(0, 2).replaceAll(" ", "");
		String day = player.joinDate.substring(2, 4).replaceAll(" ", "");
		String year = player.joinDate.substring(4).replaceAll(" ", "");
		dayOfMonth = Integer.parseInt(day);
		monthOfYear = Integer.parseInt(month);
		this.year = Integer.parseInt(year);
	}
	
	public int getDay() {
		return dayOfMonth;
	}
	
	public int getMonth() {
		return monthOfYear;
	}
	
	public int getYear() {
		return year;
	}
	
	static Calendar calendar = GregorianCalendar.getInstance();
	private int dayOfMonth, monthOfYear, year;
	Client player;
	
}