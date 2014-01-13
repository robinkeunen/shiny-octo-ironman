/**
 * 
 */

function getRecentBets() {
	json = '{ "lastbets : \
		      [ \
     			{ "punder": "Robin Keunen", "amount": 36, "choice": "HOME" },\
				{ "punder": "Antoine Flinois", "amount": 22, "choice": "AWAY"} \
	     	  ] \
	        }';
	
	var bets = JSON.parse(json);
	
	var x = document.getElementById("bet1");
	x.innerHtml = bets[0];
	
	/*
	for (var i = 1; i <= bets.length ; i++) {
		var punterid = "punter" + i;
		x = document.getElementById(punterid);
		x.innerHtml = bets[i].punder;
	}*/
	
}

getRecentBets();