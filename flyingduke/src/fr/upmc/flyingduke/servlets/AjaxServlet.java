package fr.upmc.flyingduke.servlets;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.upmc.flyingduke.domain.Bet;
import fr.upmc.flyingduke.domain.dao.BetDao;

@SuppressWarnings("serial")
public class AjaxServlet extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BetDao betDao = new BetDao();
		int i = 0;
		List<Bet> betResponse = new ArrayList<Bet>();
		//Query
		List<Bet> betList = betDao.get();
		Calendar date = new GregorianCalendar();
		Long dateTard = new Date(Calendar.getInstance().getTimeInMillis()).getTime();
		for (Bet bet : betList){
			if (i<3){
				betResponse.add(bet);
				if (bet.getDate().getTime()<dateTard){
					dateTard=bet.getDate().getTime();
				}
			}
			i++;
			if (bet.getDate().getTime()>dateTard){
				int index = 0;
				for(Bet betBis : betResponse){
					if (betBis.getDate().getTime() == dateTard){
						betResponse.remove(index);
					}
					index++;
				}
				betResponse.add(bet);
				dateTard = bet.getDate().getTime();
				for(Bet betbis : betResponse){
					if(betbis.getDate().getTime()<dateTard){
						dateTard=bet.getDate().getTime();
					}

				}

			}
		}
	}
}
