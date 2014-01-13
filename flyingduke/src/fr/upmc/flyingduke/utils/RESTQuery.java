package fr.upmc.flyingduke.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class RESTQuery {

	private final String ApiKey = "csv2n895fs3tyq5w5wsrkmhs";

	/**
	 * Builds a REST query to get the games schedule for a day.
	 * @param calendar months start at 0: January is 0.
	 * @return
	 */
	public String getGamesByDate(String day, String month, String year){

		// build request
		String requestURL = "http://api.sportsdatallc.org/nba-t3/games/" 
				+ year + "/" + month + "/" + day
				+ "/schedule.xml?api_key=" + ApiKey + "";
		URL url = null;
		BufferedReader reader = null;
		String xml = "";
		try {
			url = new URL(requestURL);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		
		// read request 
		String line = null;;
		while ((line = reader.readLine()) != null){
			xml += line;
		}
		reader.close();
		} catch (IOException e) {
			System.out.println("at line public String getGamesByDate(String day, String month, String year) {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getGamesByDate(String day, String month, String year) {");
		}

		return xml;
	}

	
	public String getGameByUUID(String gameUUID){
		String requestURL =  "http://api.sportsdatallc.org/nba-t3/games/" + gameUUID +"/summary.xml?api_key=" + ApiKey;
		URL url = null;
		BufferedReader reader = null;
		String xml = "";
		try {
			url = new URL(requestURL);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		// read request 
		String line = null;;
		while ((line = reader.readLine()) != null){
			xml += line;
		}
		reader.close();
		} catch (IOException e) {
			System.out.println("at line public String getGameByUUID(String gameUUID) {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getGameByUUID(String gameUUID) {");
		}
		return xml;
	}
	public String getAllGames(){
		String requestURL =  "http://api.sportsdatallc.org/nba-t3/games/2013/reg/schedule.xml?api_key=" + ApiKey;
		URL url = null;
		BufferedReader reader = null;
		String xml = "";
		try {
			url = new URL(requestURL);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		// read request 
		String line = null;
		while ((line = reader.readLine()) != null){
			xml += line +"\n";
		}
		reader.close();
		} catch (IOException e) {
			System.out.println("at line public String getAllGames() {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getAllGames() {");
		}
		return xml;
	}
	public String getAllTeams(){
		String requestURL =  "http://api.sportsdatallc.org/nba-t3/league/hierarchy.xml?api_key=" + ApiKey;
		URL url = null;
		BufferedReader reader = null;
		String xml = "";
		try {
			url = new URL(requestURL);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		// read request 
		String line = null;;
		while ((line = reader.readLine()) != null){
			xml += line;
		}
		reader.close();
		} catch (IOException e) {
			System.out.println("at line public String getAllTeams() {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getAllTeams() {");
		}
		return xml;
	}
		
	public String getTeamByUUID(String teamUUID){
		String requestURL = "http://api.sportsdatallc.org/nba-t3/teams/" + teamUUID + "/profile.xml?api_key=" + ApiKey;
		URL url = null;
		BufferedReader reader = null;
		String xml = "";
		try {
			url = new URL(requestURL);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		

		// read request 
		String line = null;;
		while ((line = reader.readLine()) != null){
			xml += line;
		}
		reader.close();
		} catch (IOException e) {
			System.out.println("at line public String getTeamByUUID(String teamUUID) {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getTeamByUUID(String teamUUID) {");
		}
		return xml;
	}
	public List<String> getGamesForDay() throws InterruptedException {
		List<String> xmlResults = new ArrayList<String>();
		// get strings for the date
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone((TimeZone.getTimeZone("America/New_York")));
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		// Calendar months start at 0, API months start at 1
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1); 
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		Boolean tomorrow = false;
		String xml = "";
		
		for (int i=0; i<2;i++){
		System.out.println("DAY AUJOURDUI " + day);
		if (tomorrow){
			Calendar tomorrowCal = Calendar.getInstance();
			tomorrowCal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			tomorrowCal.add(Calendar.DATE, +1);
			System.out.println("JOURNEE ! : " + tomorrowCal.get(Calendar.DATE));
			day = String.valueOf(tomorrowCal.get(Calendar.DAY_OF_MONTH));
			System.out.println("JOURNEE !!!!! " + day);
			month = String.valueOf(tomorrowCal.get(Calendar.MONTH) + 1);
			year = String.valueOf(tomorrowCal.get(Calendar.YEAR));
			System.out.println("LA DATE EST " + day + " " + month + " " + year);
		}
		tomorrow = true;
		// build request
		String requestURL = "http://api.sportsdatallc.org/nba-t3/games/" 
				+ year + "/" + month + "/" + day
				+ "/schedule.xml?api_key=" + ApiKey + "";
		URL url = null;
		BufferedReader reader = null;
		
		
		try {
			url = new URL(requestURL);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		

		// read request 
		String line = null;;
		while ((line = reader.readLine()) != null){
			xml += line + "\n";
		}
		Thread.sleep(1000);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("ENTRE DEUX APPELS");
		System.out.println();
		System.out.println();
		System.out.println(xml);
		xmlResults.add(xml);
		reader.close();
		xml="";
		} catch (IOException e) {
			System.out.println("at line public String getGamesForDay(Calendar calendar) {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getGamesForDay(Calendar calendar) {");
		}
		}

		return xmlResults;
	}
public String getTeamsStatistics() {
		
	
		// build request
		String requestURL = "http://api.sportsdatallc.org/nba-t3/seasontd/2013/reg/standings.xml?api_key=" + ApiKey;
		URL url = null;
		BufferedReader reader = null;
		String xml = "";
		try {
			url = new URL(requestURL);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		

		// read request 
		String line = null;;
		while ((line = reader.readLine()) != null){
			xml += line + "\n";
		}
		reader.close();
		} catch (IOException e) {
			System.out.println("at line public String getTeamsStatistics(Calendar calendar) {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getTeamsStatistics(Calendar calendar) {");
		}

		return xml;
	}
}