package fr.upmc.flyingduke.utils;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.EntityNotFoundException;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Player;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.TeamDao;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

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
				String teamName = teamXml.getAttribute("market") + " " + teamXml.getAttribute("name");
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

	public String parseGameBoxScore(String xmlToParse){
		try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlToParse));
			Document doc = db.parse(is);
			NodeList games = doc.getElementsByTagName("game");
			Element gameXml = (Element) games.item(0);
			String gameStatus = gameXml.getAttribute("status");
			String homeTeamid = gameXml.getAttribute("home_team");
			if (gameStatus.equalsIgnoreCase("closed")){
				NodeList teams = doc.getElementsByTagName("team");
				String winningTeam = "";
				int winningTeamScore = 0;
				for (int i = 0; i < games.getLength(); i++) {
					Element teamXml = (Element) teams.item(i);
					int teamScore = Integer.parseInt(teamXml.getAttribute("points"));
					if (teamScore > winningTeamScore){
						winningTeamScore = teamScore;
						if(teamXml.getAttribute("id").equalsIgnoreCase(homeTeamid)){
							winningTeam = "home";
						}else{
							winningTeam="away";
						}
					}
				}
				return winningTeam;
			}else{
				return "NotOver";
			}
		}catch(Exception e){
			System.out.println("Parsing Game Box Error");
		}
		return "NotOver";
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
				/*Team homeTeam = new Team(homeTeamElement.getAttribute("id"));
				homeTeam.setName(homeTeamElement.getAttribute("market") + " " + homeTeamElement.getAttribute("name"));
				homeTeam.setAlias(homeTeamElement.getAttribute("alias"));
				Team awayTeam = new Team(awayTeamElement.getAttribute("id"));
				awayTeam.setName(awayTeamElement.getAttribute("market") + " " + awayTeamElement.getAttribute("name"));
				awayTeam.setAlias(awayTeamElement.getAttribute("alias"));
				 */
				String homeTeamId = homeTeamElement.getAttribute("id");
				String awayTeamId = awayTeamElement.getAttribute("id");
				//Set Game's date
				String dateXml = gameXml.getAttribute("scheduled");
				Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(dateXml);
				System.out.println("DATE : " + date);
				game.setDate(date);
				//Set Game's AwayTeam, homeTeam
				game.setAwayTeamUUID(awayTeamId);
				game.setHomeTeamUUID(homeTeamId);
				//Add the game to the ArrayList previously created
				gamesList.add(game);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return gamesList;
	}


	public ArrayList<Game> parseGamesForDate(String xmlToParse){
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
				String dateXml = gameXml.getAttribute("scheduled");
				Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(dateXml);
				Game game = new Game(gameUUID);
				NodeList homeTeams = gameXml.getElementsByTagName("home");
				NodeList awayTeams = gameXml.getElementsByTagName("away");
				Element homeTeamElement = (Element) homeTeams.item(0);
				Element awayTeamElement = (Element) awayTeams.item(0);
				//Get Id, name and alias for Away and Home Teams
				//Team homeTeam = new Team(homeTeamElement.getAttribute("id"));
				//homeTeam.setName(homeTeamElement.getAttribute("market") + " " + homeTeamElement.getAttribute("name"));
				//homeTeam.setAlias(homeTeamElement.getAttribute("alias"));
				//Team awayTeam = new Team(awayTeamElement.getAttribute("id"));
				String homeTeamId = homeTeamElement.getAttribute("id");
				String awayTeamId = awayTeamElement.getAttribute("id");
				//awayTeam.setName(awayTeamElement.getAttribute("market") + " " + awayTeamElement.getAttribute("name"));
				//awayTeam.setAlias(awayTeamElement.getAttribute("alias"));
				//Set Game's AwayTeam, homeTeam and date
				game.setAwayTeamUUID(awayTeamId);
				game.setHomeTeamUUID(homeTeamId);
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
			double home_winpct, away_winpct, home_PointsFor, home_PointsAgainst, away_PointsFor, away_PointsAgainst;
			home_winpct = away_winpct = home_PointsAgainst = home_PointsFor =away_PointsAgainst = away_PointsFor = 0.0;

			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlToParse));
			Document doc = db.parse(is);
			Thread.sleep(1000);
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
			//Loop on the games List
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
				//Team homeTeam = new Team(homeTeamElement.getAttribute("id"));
				//homeTeam.setName(homeTeamElement.getAttribute("market") + " " + homeTeamElement.getAttribute("name"));
				//homeTeam.setAlias(homeTeamElement.getAttribute("alias"));
				String homeTeamId = homeTeamElement.getAttribute("id");
				String awayTeamId = awayTeamElement.getAttribute("id");
				//Team awayTeam = new Team(awayTeamElement.getAttribute("id"));
				//awayTeam.setName(awayTeamElement.getAttribute("market") + " " + awayTeamElement.getAttribute("name"));
				//awayTeam.setAlias(awayTeamElement.getAttribute("alias"));
				//Set date of the game
				String dateXml = gameXml.getAttribute("scheduled");
				System.out.println("DATE : " +dateXml);
				Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(dateXml);
				game.setDate(date);
				//Get win pourcentages for away and home Team
				for(int j=0;j<teamsStats.getLength();j++){
					Element teamStatXml = (Element) teamsStats.item(j);
					String teamStatId = teamStatXml.getAttribute("id");
					if(teamStatId.equalsIgnoreCase(homeTeamId)){
						home_winpct = Double.parseDouble(teamStatXml.getAttribute("win_pct"));
						home_PointsFor = Double.parseDouble(teamStatXml.getAttribute("points_for"));
						home_PointsAgainst = Double.parseDouble(teamStatXml.getAttribute("points_against"));
					}else if(teamStatId.equalsIgnoreCase(awayTeamId)){
						away_winpct = Double.parseDouble(teamStatXml.getAttribute("win_pct"));
						away_PointsFor = Double.parseDouble(teamStatXml.getAttribute("points_for"));
						away_PointsAgainst = Double.parseDouble(teamStatXml.getAttribute("points_against"));
					}
				}
				//Set Game's AwayTeam, homeTeam
				game.setAwayTeamUUID(awayTeamId);
				game.setHomeTeamUUID(homeTeamId);
				//Set game's odds
				double awayOdds = 1.25 + home_winpct/away_winpct;
				double homeOdds = 1.0 + away_winpct/home_winpct;
				game.setOdds(homeOdds,awayOdds);
				try {
					Team homeTeamDB = TeamDao.deepGet(homeTeamId);
					Team awayTeamDB = TeamDao.deepGet(awayTeamId);
					homeTeamDB.setWinRatio(home_winpct);
					homeTeamDB.setPointsFor(home_PointsFor);
					homeTeamDB.setPointsAgainst(home_PointsAgainst);
					awayTeamDB.setWinRatio(away_winpct);
					awayTeamDB.setPointsFor(away_PointsFor);
					awayTeamDB.setPointsAgainst(away_PointsAgainst);
					TeamDao.store(awayTeamDB);
					TeamDao.store(homeTeamDB);

					System.out.println(game.getUUID());
					System.out.println("NOM Home :");
					//Game gamesql = GameDao.get(game.getUUID());
					System.out.println("NOM EXTERIEUR :");
				} catch (MissingUUIDException e) {
					e.printStackTrace();
				} catch (EntityNotFoundException e) {

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

	public ArrayList<Player> parsePlayersTeam(String listPlayersXml) throws ParserConfigurationException, SAXException, IOException {
		ArrayList<Player> listPlayers = new ArrayList<Player>();
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(listPlayersXml));
		Document doc = db.parse(is);
		NodeList players = doc.getElementsByTagName("player");
		for (int i=0;i<players.getLength();i++){
			Element playerXml = (Element) players.item(i);
			Player player = new Player(playerXml.getAttribute("id"));
			player.setFirstName(playerXml.getAttribute("first_name"));
			player.setLastName(playerXml.getAttribute("last_name"));
			player.setPosition(playerXml.getAttribute("position"));
			listPlayers.add(player);
		}
		return listPlayers;
	}
}
