package de.hs_lu.beans;

public class MenuBean {

	// Menüleiste
		public MenuBean() {

		}

		public String getMenu() {
			String html = "Menüleiste: \r\n" + "<a href=\'../start/StartAppl.jsp?geheZu=StartView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\"  style=\"width:16%\" title=\"Start\" ><i class=\"fa fa-home\"></i>2gether</a> \r\n"
//					+ "<a href=\'../suche/SucheAppl.jsp?geheZu=SucheVeranstaltungView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" style=\"width:11% \" title=\"Suche\"><i class=\"fa fa-search\"></i></a> \r\n"
					+ "<a href=\'../profil/ProfilAppl.jsp?geheZu=ProfilView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" style=\"width:12%\" title=\"Profil\"><i class=\"fa fa-user\"></i></a> \r\n"
					+ "<a href=\'../nachricht/NachrichtAppl.jsp?geheZu=NachrichtView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\"  style=\"width:12%\" title=\"Nachrichten\"><i class=\"fa fa-envelope\"></i></a> \r\n"
					+ "<a href=\'../veranstaltung/VeranstaltungAppl.jsp?geheZu=VeranstaltungView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" style=\"width:12%\" title=\"Veranstaltungen\"><i class=\"fa fa-star\"></i></a> \r\n"
					+ "<a href=\'../profil/ProfilAppl.jsp?geheZu=FreundeView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" style=\"width:12%\" title=\"Freunde\"><i class=\"fa fa-handshake-o\"></i></a> \r\n"
					+ "<a href=\'../gruppe/GruppeAppl.jsp?geheZu=GruppeView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" style=\"width:12%\" title=\"Gruppen\"><i class=\"fa fa-group\"></i></a> \r\n"
					+ "<a href=\'../willkommen/WillkommenAppl.jsp?geheZu=EinstellungenView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" style=\"width:12%\" title=\"Einstellungen\"><i class=\"fa fa-cog\"></i></a> \r\n"
					+ "<a href=\'../willkommen/WillkommenAppl.jsp?logout=Logout\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" style=\"width:12%\" title=\"Logout\"><i class=\"fa fa-power-off\"></i></a> \r\n <br><br><br>";
			return html;
		}

		public String getSuche3() {
			String html = "<table>\r\n" + "		<tr>\r\n"
					+ "			<td><input type=\"submit\" class=\"w3-button w3-block w3-theme-l4\" name=\"veranstaltung\" value=\"Veranstaltung suchen\"></td>\r\n"
					+ "			<td><input type=\"submit\" class=\"w3-button w3-block w3-theme-l4\" name=\"person\" value=\"Person suchen\"></td>\r\n"
					+ "			<td><input type=\"submit\" class=\"w3-button w3-block w3-theme-l4\" name=\"gruppe\" value=\"Gruppe suchen\"></td>\r\n"
					+ "		</tr></table><br><br>";
			return html;
		}




	}

