package fr.upmc.flyingduke.Utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.GameDao;

public class Parser {

	public ArrayList<Game> parseGamesForDate(String xmlToParse, Date date){
		ArrayList<Game> gamesList = new ArrayList<Game>();
		try{
			GameDao gameDao = new GameDao();
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
}
