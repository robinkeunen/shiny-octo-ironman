package fr.upmc.flyingduke.utils;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.servlets.HomeServlet;

public class Parser {

	public ArrayList<Team> parseAllTeams(String xmlToParse){
		ArrayList<Team> teamsList = new ArrayList<Team>();
		try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlToParse));
			Document doc = db.parse(is);
			//Get all the "game" field of the XML
			NodeList teams = doc.getElementsByTagName("team");
			for (int i = 0; i < teams.getLength(); i++) {
				Element teamXml = (Element) teams.item(i);
				String teamUUID = teamXml.getAttribute("id");
				String teamName = teamXml.getAttribute("name");
				String teamAlias = teamXml.getAttribute("alias");
				Team team = new Team(teamUUID);
				team.setAlias(teamAlias);
				team.setName(teamName);
				teamsList.add(team);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return teamsList;
	}

	public ArrayList<Game> parseAllGames(String xmlToParse){
		ArrayList<Game> gamesList = new ArrayList<Game>();
		try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlToParse));
			Document doc = db.parse(is);
			//Get all the "game" field of the XML
			NodeList games = doc.getElementsByTagName("game");
			for (int i = 0; i < games.getLength(); i++) {
				Element gameXml = (Element) games.item(i);
				String gameUUID = gameXml.getAttribute("id");
				Game game = new Game(gameUUID);
				NodeList homeTeams = gameXml.getElementsByTagName("home");
				NodeList awayTeams = gameXml.getElementsByTagName("away");
				Element homeTeamElement = (Element) homeTeams.item(0);
				Element awayTeamElement = (Element) awayTeams.item(0);
				//Get Id, name and alias for Away and Home Teams
				Team homeTeam = new Team(homeTeamElement.getAttribute("id"));
				homeTeam.setName(homeTeamElement.getAttribute("name"));
				homeTeam.setAlias(homeTeamElement.getAttribute("alias"));
				Team awayTeam = new Team(awayTeamElement.getAttribute("id"));
				awayTeam.setName(awayTeamElement.getAttribute("name"));
				awayTeam.setAlias(awayTeamElement.getAttribute("alias"));
				//Set Game's date
				String dateXml = gameXml.getAttribute("scheduled");
				Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(dateXml);
				System.out.println("DATE : " + date);
				game.setDate(date);
				//Set Game's AwayTeam, homeTeam
				game.setAwayTeam(awayTeam);
				game.setHomeTeam(homeTeam);
				//Add the game to the ArrayList previously created
				gamesList.add(game);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return gamesList;
	}


	public ArrayList<Game> parseGamesForDate(String xmlToParse, Date date){
		ArrayList<Game> gamesList = new ArrayList<Game>();
		try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlToParse));
			Document doc = db.parse(is);
			//Get all the "game" field of the XML
			NodeList games = doc.getElementsByTagName("game");
			for (int i = 0; i < games.getLength(); i++) {
				Element gameXml = (Element) games.item(i);
				String gameUUID = gameXml.getAttribute("id");
				Game game = new Game(gameUUID);
				NodeList homeTeams = gameXml.getElementsByTagName("home");
				NodeList awayTeams = gameXml.getElementsByTagName("away");
				Element homeTeamElement = (Element) homeTeams.item(0);
				Element awayTeamElement = (Element) awayTeams.item(0);
				//Get Id, name and alias for Away and Home Teams
				Team homeTeam = new Team(homeTeamElement.getAttribute("id"));
				homeTeam.setName(homeTeamElement.getAttribute("name"));
				homeTeam.setAlias(homeTeamElement.getAttribute("alias"));
				Team awayTeam = new Team(awayTeamElement.getAttribute("id"));
				awayTeam.setName(awayTeamElement.getAttribute("name"));
				awayTeam.setAlias(awayTeamElement.getAttribute("alias"));
				//Set Game's AwayTeam, homeTeam and date
				game.setAwayTeam(awayTeam);
				game.setHomeTeam(homeTeam);
				game.setDate(date);
				//Add the game to the ArrayList previously created
				gamesList.add(game);
			}
		}	catch(Exception e){
			e.printStackTrace();
		}
		return gamesList;
	}

	public ArrayList<Game> parseGamesForDay(String xmlToParse){
		ArrayList<Game> gamesList = new ArrayList<Game>();
		try{
			Calendar calendar = new GregorianCalendar();
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlToParse));
			Document doc = db.parse(is);
			//Fetch Team Statistics and XML parsing
			RESTQuery queryLauncher = new RESTQuery();
			String xmlStats = queryLauncher.getTeamsStatistics();
			DocumentBuilder docBuilderStats = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSourceStats = new InputSource();
			inputSourceStats.setCharacterStream(new StringReader(xmlStats));
			Document docStats = docBuilderStats.parse(inputSourceStats);
			System.out.println("debut parser");
			//Get all the "game" field of the XML
			NodeList games = doc.getElementsByTagName("game");
			NodeList teamsStats = docStats.getElementsByTagName("team");
			for (int i = 0; i < games.getLength(); i++) {
				System.out.println("CPT : " + i);
				Element gameXml = (Element) games.item(i);
				String gameUUID = gameXml.getAttribute("id");
				Game game = new Game(gameUUID);
				NodeList homeTeams = gameXml.getElementsByTagName("home");
				NodeList awayTeams = gameXml.getElementsByTagName("away");
				Element homeTeamElement = (Element) homeTeams.item(0);
				Element awayTeamElement = (Element) awayTeams.item(0);
				//Get Id, name and alias for Away and Home Teams
				Team homeTeam = new Team(homeTeamElement.getAttribute("id"));
				homeTeam.setName(homeTeamElement.getAttribute("name"));
				homeTeam.setAlias(homeTeamElement.getAttribute("alias"));
				Team awayTeam = new Team(awayTeamElement.getAttribute("id"));
				awayTeam.setName(awayTeamElement.getAttribute("name"));
				awayTeam.setAlias(awayTeamElement.getAttribute("alias"));
				//Set Game's AwayTeam, homeTeam and date
				game.setAwayTeam(awayTeam);
				game.setHomeTeam(homeTeam);
				game.setDate(calendar.getTime());
				for(int j=0;j<teamsStats.getLength();j++){
					Element teamStatXml = (Element) teamsStats.item(j);
					String teamStatId = teamStatXml.getAttribute("id");
					if(teamStatId.equalsIgnoreCase(homeTeam.getUUID())){
						double home_winpct = Double.parseDouble(teamStatXml.getAttribute("win_pct"));
						System.out.println("pourcentage home : " + home_winpct);
					}else if(teamStatId.equalsIgnoreCase(awayTeam.getUUID())){
						double away_winpct = Double.parseDouble(teamStatXml.getAttribute("win_pct"));
						System.out.println("pourcentage away : " + away_winpct);
					}
					//Set game's odds
					
				}
			//Add the game to the ArrayList previously created
			gamesList.add(game);
		}
	}	catch(Exception e){
		e.printStackTrace();
	}
	return gamesList;
}
//Fetch teams statistics

}
