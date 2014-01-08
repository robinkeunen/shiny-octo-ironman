package fr.upmc.flyingduke.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

public class RESTQuery {

	private final String ApiKey = "rguqxedqsff7aspmq9uxvqed";

	/**
	 * Builds a REST query to get the games schedule for a day.
	 * @param calendar months start at 0: January is 0.
	 * @return
	 */
	public String getGamesForDay(Calendar calendar) {
		
		// get strings for the date
		
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		// Calendar months start at 0, API months start at 1
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1); 
		String year = String.valueOf(calendar.get(Calendar.YEAR));

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
			System.out.println("at line public String getGamesForDay(Calendar calendar) {");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("at line public String getGamesForDay(Calendar calendar) {");
		}

		return xml;
	}
}