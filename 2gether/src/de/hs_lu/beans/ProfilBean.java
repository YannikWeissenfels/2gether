package de.hs_lu.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hs_lu.jdbc.PostgreSQLAccess;

public class ProfilBean {


	// Variablen für Benutzer
	int benutzer_id;
	String vorname;
	String nachname;
	String geschlechtBenutzer;
	String geburtstag;
	String wohnort;
	String bild;
	String beziehungsstatus;
	String email;
	String kurzprofil;
	// Variablen für PersonenInteressen
	int personeninteressenid;
	String mindestalter;
	String maximalalter;
	String geschlechtPerson;
	// Variablen für Interessen:
	ArrayList<Interesse> alleInteressen; // Hier werden alle Interessen aus Tabelle "Interesse" aus DB gespeichert zur
											// Anzeige in ProfilBearbeitenView
	String[] meineInteressenFuerDB; // Dieses String Array ist für die Zwischenspeicherung der ausgewählten
									// Interessen beim Profil bearbeiten zur Speicherung in die DB
	ArrayList<Interesse> meineInteressenAusDB; // Hier werden die Interessen des Benutzers aus der DB gespeichert zur
												// Darstellung in ProfilView in HTML
	// Variablen für Interessen:
	ArrayList<String> alleStaedte;
	String[] meineStaedteFuerDB;
	ArrayList<String> meineStaedteAusDB;
	// Variablen für Freundschaft:
	ArrayList<Freund> meineFreunde; // Diese ArrayList wird für die Darstellung der Freunde in FreundeView verwendet
									// (ID, Vorname, Nachname)
	int ausgewaehlteFreund_id; // Diese freund_id übergibt die ID für den richtigen Abruf des Profils
	ArrayList<Benutzer> benutzer; // Diese ArrayList wird für die Darstellung aller Benutzer in StartView
									// verwendet (ID, Vorname, Nachname)
	ArrayList<Freund> meineFreundschaftsanfragen; // Diese ArrayList enthält die ID, Vorname, Nachname von Freunden, die
													// eine Freundschaftsanrage geschickt haben
	String antwortFreundschaftsanfrage;
	ArrayList<Freund> gesendeteOffeneFreundschaftsanfragen;

	// lokale Variable benutzerid von ProfilBean erhält die BenutzerID von
	// Willkommenbean
	public void benutzeridEinspielen() {
		this.benutzer_id = WillkommenBean.getBenutzer_id();
	}

	public ProfilBean() throws SQLException {

		// Variablen für Benutzer
		this.benutzeridEinspielen();
		this.nachname = "";
		this.geschlechtBenutzer = "";
		this.geburtstag = "";
		this.wohnort = "";
		this.bild = "";
		this.beziehungsstatus = "";
		this.email = "";
		this.kurzprofil = "";
		// Variablen für PersonenInteressen
		this.geschlechtPerson = "";
		this.mindestalter = "";
		this.maximalalter = "";
		// Variablen für Interessen:
		this.alleInteressen = new ArrayList<>();
		this.meineInteressenFuerDB = new String[alleInteressen.size()];
		this.meineInteressenAusDB = new ArrayList<>();
		// Variablen für Interessen:
		this.alleStaedte = new ArrayList<>();
		this.meineStaedteFuerDB = new String[alleStaedte.size()];
		this.meineStaedteAusDB = new ArrayList<>();
		// Variablen für Freundschaft:
		this.meineFreunde = new ArrayList<>();
		this.ausgewaehlteFreund_id = 0;
		this.benutzer = new ArrayList<>();
		this.meineFreundschaftsanfragen = new ArrayList<>();
		this.antwortFreundschaftsanfrage = "";
		this.gesendeteOffeneFreundschaftsanfragen = new ArrayList<>();

	}

	// Benutzerdaten aus DB auslesen, die Methode wird einmal für Benutzer und für
	// Freunde/andere Benutzer verwendet
	// siehe nächsten beiden Methoden
	public boolean getDatenAusBenutzer(int id) throws SQLException { //

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT vorname, nachname, geschlecht, geburtstag, wohnort, beziehungsstatus, email, kurzprofil FROM BENUTZER "
				+ "where benutzer_id = '" + id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			this.vorname = dbRes.getString("vorname");
			this.nachname = dbRes.getString("nachname");
			this.geschlechtBenutzer = dbRes.getString("geschlecht");
			this.geburtstag = dbRes.getString("geburtstag");
			this.wohnort = dbRes.getString("wohnort");
			this.beziehungsstatus = dbRes.getString("beziehungsstatus");
			this.email = dbRes.getString("email");
			this.kurzprofil = dbRes.getString("kurzprofil");

			if (this.wohnort == null)
				this.wohnort = "";
			if (this.beziehungsstatus == null)
				this.beziehungsstatus = "";
			if (this.kurzprofil == null)
				this.kurzprofil = "";


			dbConn.close();
			return true;
		} else {
			return false;
		}

	}

	public void persoenlicheDatenFuerAndereBenutzer() throws SQLException {
		this.getDatenAusBenutzer(this.ausgewaehlteFreund_id);
		this.getDatenAusPersoneninteresse(this.ausgewaehlteFreund_id);
	}

	public void persoenlicheDatenFuerEigenesProfil() throws SQLException {
		this.benutzeridEinspielen();
		this.getDatenAusBenutzer(this.benutzer_id);
		this.getDatenAusPersoneninteresse(this.benutzer_id);
	}

	// Daten von PersonenInteresse aus DB auslesen für eigenen Benutzer und andere
	// Benutzer/Freunde
	public void getDatenAusPersoneninteresse(int id) throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "SELECT MINDESTALTER, MAXIMALALTER, GESCHLECHT FROM PERSONENINTERESSE WHERE BENUTZER_ID = " + id
				+ "";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			this.mindestalter = dbRes.getString("mindestalter");
			this.maximalalter = dbRes.getString("maximalalter");
			this.geschlechtPerson = dbRes.getString("geschlecht");
		}
	}

	public boolean personenInteresseVorhanden() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		boolean ergebnis = false;
		String sql = "SELECT MINDESTALTER, MAXIMALALTER, GESCHLECHT FROM PERSONENINTERESSE WHERE BENUTZER_ID = "
				+ this.benutzer_id + "";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			ergebnis = true;
		}

		dbConn.close();
		return ergebnis;
	}

	// Neues Personeninteresse hinterlegen mit Abruf der ID und Hinterlegung im
	// Benutzer
	// Diese Methode ist nur für den eigenen Benutzer
	public void neuesPersoneninteresse() throws SQLException {
		this.benutzeridEinspielen();

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "INSERT INTO PERSONENINTERESSE (MINDESTALTER, MAXIMALALTER, GESCHLECHT, BENUTZER_ID)"
				+ "VALUES (?,?,?,?)";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.setString(1, this.mindestalter);
		prep.setString(2, this.maximalalter);
		prep.setString(3, this.geschlechtPerson);
		prep.setInt(4, this.benutzer_id);
		prep.executeUpdate();
		

		dbConn.close();
	}

	// Bereits vorhandene Personeninteressen bearbeiten mit Verweis auf Methode
	// neuesPersonenInteresse anlegen
	// Diese Methode ist nur für den eigenen Benutzer
	public void personeninteresseBearbeiten() throws SQLException {

		this.benutzeridEinspielen();
		if (this.personenInteresseVorhanden() == false) {
			this.neuesPersoneninteresse();
		} else {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "UPDATE PERSONENINTERESSE SET MINDESTALTER = ?, MAXIMALALTER = ?, GESCHLECHT = ?"
					+ "WHERE BENUTZER_ID = '" + this.benutzer_id + "'";
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setString(1, this.mindestalter);
			prep.setString(2, this.maximalalter);
			prep.setString(3, this.geschlechtPerson);
			prep.executeUpdate();

			dbConn.close();
		}
	}

	// Profildaten bearbeiten mit Aufruf der Methode für PersonenInteressen
	// bearbeiten
	// Diese Methode ist nur für den eigenen Benutzer
	public void profilBearbeiten() throws SQLException {
		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "UPDATE BENUTZER SET VORNAME = ?, NACHNAME = ?, GESCHLECHT = ?, GEBURTSTAG = ?, WOHNORT = ?,"
				+ "BEZIEHUNGSSTATUS = ?, EMAIL = ?, KURZPROFIL = ?" + "WHERE BENUTZER_ID = '" + this.benutzer_id + "'";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.setString(1, this.vorname);
		prep.setString(2, this.nachname);
		prep.setString(3, this.geschlechtBenutzer);
		prep.setString(4, this.geburtstag);
		prep.setString(5, this.wohnort);
		prep.setString(6, this.beziehungsstatus);
		prep.setString(7, this.email);
		prep.setString(8, this.kurzprofil);
		prep.executeUpdate();

		this.personeninteresseBearbeiten();
		dbConn.close();
	}
	// Ab hier alles was Interessen betrifft:

	// Alle generellen Interessen aus DB für HTML für ProfilBearbeitenView auslesen
	// Diese Methode ist nur für den eigenen Benutzer zur Profilbearbeitung
	public void liesAlleInteressenAusDB() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.alleInteressen.clear();

		String sql = "SELECT ART, OBERBEGRIFF FROM INTERESSE";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			Interesse neuesInteresse = new Interesse(dbRes.getString("art"), dbRes.getString("oberbegriff"));
			this.alleInteressen.add(neuesInteresse);
		}
		dbConn.close();
	}

	// Abruf aller Oberbegriffe von Tabelle Interesse
	// Wird benötigt um die ausgewählten Interessen in HTML unter den richtigen
	// Oberbegriff zu schreiben
	public void alleOberbegriffeVonInteresse(ArrayList<Interesse> oberbegriffe) throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "SELECT DISTINCT OBERBEGRIFF FROM INTERESSE ORDER BY OBERBEGRIFF";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			Interesse neuerOberbegriff = new Interesse(dbRes.getString("oberbegriff"));
			oberbegriffe.add(neuerOberbegriff);
		}
		dbConn.close();
	}

	// Darstellung aller Interessen in HTML mit Vorauswahl, welche gerade in DB sind
	// für ProfilBearbeitenView
	public String getHTMLAllerInteressen() throws SQLException {
		ArrayList<Interesse> oberbegriffe = new ArrayList<>();

		this.alleOberbegriffeVonInteresse(oberbegriffe);

		String html = "<table ><tr >";

		for (int x = 0; x < oberbegriffe.size(); x++) {
			Interesse oberbegriff = oberbegriffe.get(x);
			if (x == oberbegriffe.size()-2)	{
				html += "</tr><tr><td valign=top><h4>" + oberbegriff.getOberbegriff() + ":<br/></h4>";;																		// auftaucht
				} else {
			html += "<td valign=top><h4>" + oberbegriff.getOberbegriff() + ":<br/></h4>";
			}
			// Schleife über meineInteressen und AlleInteressen.
			// Wenn Treffer, dann soll es mit checked dargestellt werden und das Interesse
			// aus AlleInteressen gelöscht werden

			for (int j = 0; j < meineInteressenAusDB.size(); j++) {
				Interesse meinInteresse = meineInteressenAusDB.get(j);
				for (int i = 0; i < this.alleInteressen.size(); i++) {
					Interesse interesseAlle = this.alleInteressen.get(i);
					if (interesseAlle.getOberbegriff().equals(oberbegriff.getOberbegriff())) { // Abgleich damit das
																								// Interesse beim
																								// richtigen Oberbegriff
	

						if (interesseAlle.getArt().equals(meinInteresse.getArt())) { // Abgleich ob gleiches Interesse
																						// in Alle und Meine Interessen

							html += "<label><input type='checkbox' name='interesse' value='" + meinInteresse.getArt()
									+ "' checked/> " + meinInteresse.getArt() + "</label><br/>";
							this.alleInteressen.remove(interesseAlle);
						}
					}
				}
			}

			// Schleife über alleInteressen und Ausgabe von Checkboxen ohne checked
			for (int i = 0; i < this.alleInteressen.size(); i++) {
				Interesse interesseAlle = this.alleInteressen.get(i);

				if (interesseAlle.getOberbegriff().equals(oberbegriff.getOberbegriff())) { // Abgleich damit das
																							// Interesse beim richtigen
																							// Oberbegriff auftaucht

					html += "<label><input type='checkbox' name='interesse' value='" + interesseAlle.getArt() + "'/> "
							+ interesseAlle.getArt() + "</label><br/>";
				}
			}

			html += "</td>";

		}
		html += "</tr></table>";

		return html;
	}

	// Vom Benutzer ausgewählte Interessen aus Array in DB schreiben
	// Aus Datenbank wird erst alles gelöscht und dann die Neuauswahl neu
	// reingeschrieben
	// Diese Methode ist nur für den eigenen Benutzer
	public void schreibeAusgewaehlteInteressenVonBenutzerInDB() throws SQLException {
		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sqlDELETE = "DELETE FROM BENUTZER_INTERESSE WHERE BENUTZER_ID = '" + this.benutzer_id + "'";
		Statement stmt = dbConn.createStatement();
		stmt.executeUpdate(sqlDELETE);

		for (int i = 0; i < meineInteressenFuerDB.length; i++) {
			String sql = "INSERT INTO BENUTZER_INTERESSE (BENUTZER_ID, INTERESSE)" + "values (?,?)";
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setInt(1, this.benutzer_id);
			prep.setString(2, meineInteressenFuerDB[i]);
			prep.executeUpdate();
		}
		dbConn.close();
	}

	// Interessen des Benutzers aus DB holen zum Anzeigen in HTML
	public void liesInteressenAusDB(int id) throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.meineInteressenAusDB.clear();

		String sql = "SELECT BI.INTERESSE, I.OBERBEGRIFF FROM BENUTZER_INTERESSE BI" + " INNER JOIN INTERESSE I"
				+ " ON I.ART = BI.INTERESSE WHERE BENUTZER_ID = '" + id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			Interesse neuesInteresse = new Interesse(dbRes.getString("interesse"), dbRes.getString("oberbegriff"));
			this.meineInteressenAusDB.add(neuesInteresse);

		}
		dbConn.close();
	}

	public void liesInteressenVonBenutzerAusDB() throws SQLException {
		this.benutzeridEinspielen();
		this.liesInteressenAusDB(this.benutzer_id);
		this.liesStaedteAusDB(this.benutzer_id);

	}

	public void liesInteressenVonFreundenAusDB() throws SQLException {
		this.liesInteressenAusDB(this.ausgewaehlteFreund_id);
		this.liesStaedteAusDB(this.ausgewaehlteFreund_id);
	}

	// Darstellung der Interessen des Benutzers aus DB in HTML
	// für ProfilView bzw. FreundeProfilView
	public String getHTMLInteressenVonBenutzer() throws SQLException {
		
		String html = "";
		
		if (!this.meineInteressenAusDB.isEmpty()) {
			html += "<table><tr>";
		for (int j = 0; j < meineInteressenAusDB.size(); j++) {
			Interesse meinInteresse = meineInteressenAusDB.get(j);
			html += "<td class=\"w3-theme-l4 w3-center\">"+meinInteresse.getArt() + "</td>";
			if (j==2 || j==5 || j==8 || j==11 || j==14 || j==17	||
				j==20 || j==23 || j==26 || j==29 || j==32 || j==35 ||
				j==38 || j==41 || j==44 || j==47) {
				html += "</tr><tr>";	
			}
		}
		html+= "</tr></table>";
	}
		return html;
	}
	
	// Ab hier alles was Staedteinteressen betrifft

	// Alle Staedte aus DB für HTML für ProfilBearbeitenView auslesen
	// Diese Methode ist nur für den eigenen Benutzer zur Profilbearbeitung
	public void liesAlleStaedteAusDB() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.alleStaedte.clear();

		String sql = "SELECT STADT FROM STAEDTEINTERESSE";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			String neueStadt = new String(dbRes.getString("stadt"));
			this.alleStaedte.add(neueStadt);
		}
		dbConn.close();
	}

	// Darstellung aller Staedte in HTML mit Vorauswahl, welche gerade in DB sind
	public String getHTMLAllerStaedte() throws SQLException {

		String html = "";

		// Schleife über meineStaedte und AlleStaedte.
		// Wenn Treffer, dann soll es mit checked dargestellt werden und das Interesse
		// aus AlleInteressen gelöscht werden

		for (int j = 0; j < meineStaedteAusDB.size(); j++) {
			String meineStadt = meineStaedteAusDB.get(j);
			for (int i = 0; i < this.alleStaedte.size(); i++) {
				String stadtAlle = this.alleStaedte.get(i);

				if (stadtAlle.equals(meineStadt)) { // Abgleich ob Stadt in Alle und Meine Staedte

					html += "<label><input type='checkbox' name='stadt' value='" + meineStadt + "' checked/> " + meineStadt
							+ "</label><br/>";
					this.alleStaedte.remove(stadtAlle);
				}
			}
		}

		// Schleife über alleStaedte und Ausgabe von Checkboxen ohne checked
		for (int i = 0; i < this.alleStaedte.size(); i++) {
			String stadtAlle = this.alleStaedte.get(i);

			html += "<label><input type='checkbox' name='stadt' value='" + stadtAlle + "'/> " + stadtAlle + "</label><br/>";
		}

		return html;
	}

	// Vom Benutzer ausgewaehlte Staedte aus Array in DB schreiben
	// Aus Datenbank wird erst alles gelöscht und dann die Neuauswahl neu
	// reingeschrieben
	// Diese Methode ist nur für den eigenen Benutzer
	public void schreibeAusgewaehlteStaedteVonBenutzerInDB() throws SQLException {
		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sqlDELETE = "DELETE FROM BENUTZER_STAEDTEINTERESSE WHERE BENUTZER_ID = '" + this.benutzer_id + "'";
		Statement stmt = dbConn.createStatement();
		stmt.executeUpdate(sqlDELETE);

		for (int i = 0; i < meineStaedteFuerDB.length; i++) {
			String sql = "INSERT INTO BENUTZER_STAEDTEINTERESSE (BENUTZER_ID, STADT)" + "values (?,?)";
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setInt(1, this.benutzer_id);
			prep.setString(2, meineStaedteFuerDB[i]);
			prep.executeUpdate();
		}
		dbConn.close();
	}

	// Staedte des Benutzers aus DB holen zum Anzeigen in HTML
	public void liesStaedteAusDB(int id) throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.meineStaedteAusDB.clear();

		String sql = "SELECT STADT FROM BENUTZER_STAEDTEINTERESSE WHERE BENUTZER_ID = '" + id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			String neueStadt = new String(dbRes.getString("stadt"));
			this.meineStaedteAusDB.add(neueStadt);

		}
		dbConn.close();
	}

	// Darstellung der Staedte des Benutzers aus DB in HTML für ProfilView bzw.
	// FreundeProfilView
	public String getHTMLStaedteVonBenutzer() throws SQLException {

		String html = "";
		
		if (!this.meineStaedteAusDB.isEmpty()) {
			html += "<table><tr>";
			for (int j = 0; j < meineStaedteAusDB.size(); j++) {
				String meineStadt = meineStaedteAusDB.get(j);
				html += "<td class=\"w3-theme-l4 w3-center\">"+meineStadt +"</td>";
				if (j==2 || j==5 || j==8) {
					html += "</tr><tr>";
			}
		}
			html+= "</tr></table>";
		}

		return html;
	}


	// Ab hier alles was Freundschaften betrifft:

	// Freundschaften aus Freundesliste Tabelle auslesen, die entsprechenden IDs
	// auslesen und mit diesen IDs über
	// Benutzertabelle gehen und ID, Vorname, Nachname des Freundes auslesen und in
	// ArrayList Freunde speichern
	public void liesFreundschaftenUndFreundenamenVonBenutzerAusDB() throws SQLException {
		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.meineFreunde.clear();

		String sql = "SELECT F.FREUND_ID, B.VORNAME, B.NACHNAME" + " FROM FREUNDSCHAFT F" + " INNER JOIN BENUTZER B"
				+ " ON F.FREUND_ID = B.BENUTZER_ID" + " WHERE F.BENUTZER_ID = '" + this.benutzer_id + "' AND ANFRAGE = 0";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			Freund neuerFreund = new Freund(dbRes.getInt("freund_id"), dbRes.getString("vorname"),
					dbRes.getString("nachname"));
			this.meineFreunde.add(neuerFreund);
		}
		dbConn.close();
	}

	// Darstellung der Freunde in HTML
	public String getHTMLAllerFreundeVonBenutzer() throws SQLException {

		String html = "<h5>Deine Freunde</h5><br/><br/>";

		if (this.meineFreunde.isEmpty()) {
			html += "Du hast derzeit leider noch keine Freunde.";
		} else {
			
			for (int i = 0; i < meineFreunde.size(); i++) {
				Freund neuerFreund = meineFreunde.get(i);
				this.richtigesBild(neuerFreund.getFreund_id());
				
				
				html += "<a href='./ProfilAppl.jsp?geheZu=FreundeProfilView.jsp&ausgewaehlteFreund_id="
						+ neuerFreund.getFreund_id() + "'class=\"w3-button\"> <img src ='../img/" + this.getBild() + "' alt='"
						+ neuerFreund.getNachname() + "'" + "title='" + neuerFreund.getNachname()
						+ "' width='50' height='50' />" + neuerFreund.getVorname() + " " + neuerFreund.getNachname()+" </a><br/>";

			}

		}

		return html;
	}

	// Buttons Freundschaftsanfrage senden / Freundschaftsanfrage zurückziehen oder
	// Freund entfernen Button
	public String getButtonsFuerFreundschaft() throws SQLException {
		this.benutzeridEinspielen();
		String html = "";

		if (freundBereitsInFreundschaft()) {
			html += "<input type='submit' class=\"w3-button  w3-theme-l4\" name='entfernen' value='Freund entfernen'>";

		} else if (freundBereitsInFreundschaft() == false && benutzerAnFreundBereitsAnfrageGesendet()) {
			html += "<input type='submit' class=\"w3-button  w3-theme-l4 buttonwidth_2\" name='zurueckziehen' value='Freundschaftsanfrage zurückziehen'>";
		} else if (freundBereitsInFreundschaft() == false && benutzerBereitsFreundschaftsanfrageVonFreundErhalten()) {
			html += "<input type='submit' class=\"w3-button  w3-theme-l4 buttonwidth_2\" name='annehmen' value='Annehmen'><br/><br/>"
					+ "<input type='submit' class=\"w3-button  w3-theme-l4 buttonwidth_2\" name='ablehnen' value='Ablehnen'>";
		}

		else {
			html += "<input type='submit' class=\"w3-button  w3-theme-l4\" name='freundschaftsanfrage' value='Freundschaftsanfrage senden'>";
		}
		return html;

	}

	// Speicherung in Tabelle Freundschaftsanfrage
	public void freundschaftsanfrageSenden() throws SQLException {

		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "INSERT INTO FREUNDSCHAFT (BENUTZER_ID, FREUND_ID, ANFRAGE)" + "VALUES (?,?,?)";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.setInt(1, this.benutzer_id);
		prep.setInt(2, this.ausgewaehlteFreund_id);
		prep.setInt(3, 1);
		prep.executeUpdate();
		dbConn.close();
	}

	public boolean freundschaftsanfragenAusDBAuslesen() throws SQLException {
		this.meineFreundschaftsanfragen.clear();
		this.benutzeridEinspielen();
		boolean ergebnis = false;
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT F.BENUTZER_ID, B.VORNAME, B.NACHNAME " + " FROM FREUNDSCHAFT F "
				+ " INNER JOIN BENUTZER B" + " ON F.BENUTZER_ID = B.BENUTZER_ID WHERE FREUND_ID = '" + this.benutzer_id
				+ "' AND ANFRAGE = 1";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			Freund neuerMoeglicherFreund = new Freund(dbRes.getInt("BENUTZER_ID"), dbRes.getString("VORNAME"),
					dbRes.getString("NACHNAME"));
			this.meineFreundschaftsanfragen.add(neuerMoeglicherFreund);
			ergebnis = true;

		}
		dbConn.close();
		return ergebnis;
	}

	// Anzahl der Freundschaftsanfragen
	public int getAnzahlFreundschaftsanfragen() {

		return this.meineFreundschaftsanfragen.size();
	}

	// Darstellung der Freundschaftsanfragen in HTML
		public String getHTMLAllerFreundschaftsanfragen() throws SQLException {
			// @ Sommer, diese Links sollen noch als Buttons dargestellt werden (Annehmen
			// Ablehnen)
			String html = "";

			if (meineFreundschaftsanfragen.isEmpty()) {
			} else {
				if (this.getAnzahlFreundschaftsanfragen() == 1) {
					html += "<b><h5>Du hast 1 neue Freundschaftsanfrage:</h5></b><br/>";
				} else {
					html += "<b><h5>Du hast " + this.getAnzahlFreundschaftsanfragen()
							+ " neue Freundschaftsanfragen:</h5></b><br/>";
				}
				html += "<table>";	
				for (int i = 0; i < meineFreundschaftsanfragen.size(); i++) {
					Freund neuerFreund = meineFreundschaftsanfragen.get(i);
					this.richtigesBild(neuerFreund.getFreund_id());

					html += "<tr><td><a href='../profil/ProfilAppl.jsp?geheZu=FreundeProfilView.jsp&ausgewaehlteFreund_id="
							+ neuerFreund.getFreund_id() + "' class=\"w3-button\"> <img src ='../img/" + this.getBild() + "' alt='"
							+ neuerFreund.getNachname() + "'" + "title='" + neuerFreund.getNachname()
							+ "' width='50' height='50' />" + neuerFreund.getVorname() + " " + neuerFreund.getNachname()
							+ "</a></td>";

					html += "<td><a href='../profil/ProfilAppl.jsp?annehmen=annehmen&ausgewaehlteFreund_id="
							+ neuerFreund.getFreund_id() + "&annehmen=" + this.antwortFreundschaftsanfrage + "' class=\"w3-button w3-theme-l4\">"
							+ "Annehmen </a> <br>";

					html += "<a href='../profil/ProfilAppl.jsp?ablehnen=ablehnen&ausgewaehlteFreund_id="
							+ neuerFreund.getFreund_id() + "&ablehnen=" + this.antwortFreundschaftsanfrage + "' class=\"w3-button w3-theme-l4\">"
							+ "Ablehnen </a></td></tr>";
				}

			}
			html += "</table>";
			return html;
		}

	// Freundschaftsanfrage von Tabelle Freundschaft löschen, weil Freund abgelehnt hat
	public void loescheFreundschaftsanfrage() throws SQLException {
		this.benutzeridEinspielen();
		// int freundid = Integer.parseInt(this.ausgewaehlteFreund_id);
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "DELETE FROM FREUNDSCHAFT WHERE FREUND_ID = '" + this.benutzer_id + "' AND BENUTZER_ID = '"
				+ this.ausgewaehlteFreund_id + "' AND ANFRAGE = 1";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.executeUpdate();

		for (int i = 0; i < meineFreundschaftsanfragen.size(); i++) {
			Freund neueAnfrage = meineFreundschaftsanfragen.get(i);
			if (neueAnfrage.getFreund_id() == this.ausgewaehlteFreund_id) {
				this.meineFreundschaftsanfragen.remove(neueAnfrage);
			}
		}
		dbConn.close();
	}

	// Benutzer kann seine eigene Freundschaftsanfrage zurückziehen
	public void eigeneFreundschaftsanfrageZurueckziehen() throws SQLException {
		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "DELETE FROM FREUNDSCHAFT WHERE BENUTZER_ID = '" + this.benutzer_id + "' AND FREUND_ID = '"
				+ this.ausgewaehlteFreund_id + "' AND ANFRAGE = 1";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.executeUpdate();

		for (int i = 0; i < meineFreundschaftsanfragen.size(); i++) {
			Freund neueAnfrage = meineFreundschaftsanfragen.get(i);
			if (neueAnfrage.getFreund_id() == this.benutzer_id) {
				this.meineFreundschaftsanfragen.remove(neueAnfrage);
			}
		}
		dbConn.close();
	}

	// Freundschaftsanfrage annehmen
	public void freundschaftsanfrageAnnehmenUndSpeichern() throws SQLException {

		this.benutzeridEinspielen();

		// int freundid = Integer.parseInt(this.ausgewaehlteFreund_id);
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "UPDATE FREUNDSCHAFT SET ANFRAGE = 0 WHERE FREUND_ID = "+this.benutzer_id+" AND BENUTZER_ID = "+this.ausgewaehlteFreund_id;
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.executeUpdate();

		String sql2 = "INSERT INTO FREUNDSCHAFT (BENUTZER_ID, FREUND_ID, ANFRAGE)" + "VALUES (?,?,?)";
		PreparedStatement prep2 = dbConn.prepareStatement(sql2);
		prep2.setInt(1, this.benutzer_id);
		prep2.setInt(2, this.ausgewaehlteFreund_id);
		prep2.setInt(3,0);
		prep2.executeUpdate();

		this.loescheFreundschaftsanfrage();
		dbConn.close();
	}

	// Freund aus der Freundesliste entfernen
	public void freundEntfernen() throws SQLException {
		this.benutzeridEinspielen();

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "DELETE FROM FREUNDSCHAFT WHERE BENUTZER_ID = '" + this.benutzer_id + "' AND FREUND_ID = '"
				+ this.ausgewaehlteFreund_id + "' AND ANFRAGE = 0";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.executeUpdate();

		String sql2 = "DELETE FROM FREUNDSCHAFT WHERE BENUTZER_ID = '" + this.ausgewaehlteFreund_id
				+ "' AND FREUND_ID = '" + this.benutzer_id + "' AND ANFRAGE = 0";
		PreparedStatement prep2 = dbConn.prepareStatement(sql2);
		prep2.executeUpdate();

		dbConn.close();
	}

	// Sind Benutzer und Freund bereits befreundet?
	// Abfrage, damit entsprechende Meldung / Button kommt, wenn auf
	// Freundschaftsanfrage geklickt wird
	public boolean freundBereitsInFreundschaft() throws SQLException {

		this.benutzeridEinspielen();
		boolean ergebnis = false;
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT FREUND_ID FROM FREUNDSCHAFT WHERE BENUTZER_ID = '" + this.benutzer_id
				+ "' AND FREUND_ID = '" + this.ausgewaehlteFreund_id + "' AND ANFRAGE = 0";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			ergebnis = true;
		}

		dbConn.close();
		return ergebnis;
	}

	// Wurde bereits eine Freundschaftsanfrage gesendet, Abgleich in Tabelle
	// Freundschaftsanfrage
	// Abfrage, damit der Button Anfrage zurückziehen erscheint
	public boolean benutzerAnFreundBereitsAnfrageGesendet() throws SQLException {

		this.benutzeridEinspielen();
		boolean ergebnis = false;
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT FREUND_ID FROM FREUNDSCHAFT WHERE BENUTZER_ID = '" + this.benutzer_id
				+ "' AND FREUND_ID = '" + this.ausgewaehlteFreund_id + "' AND ANFRAGE = 1";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			ergebnis = true;
		}

		dbConn.close();
		return ergebnis;
	}

	// Wenn der Freund einem bereits eine Anfrage geschickt hat, soll keine erneute
	// Anfrage möglich sein
	// Dann kommen Buttons Annehmen Ablehnen
	public boolean benutzerBereitsFreundschaftsanfrageVonFreundErhalten() throws SQLException {

		this.benutzeridEinspielen();
		boolean ergebnis = false;
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT BENUTZER_ID FROM FREUNDSCHAFT WHERE FREUND_ID = '" + this.benutzer_id
				+ "' AND BENUTZER_ID = '" + this.ausgewaehlteFreund_id + "' AND ANFRAGE = 1";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			ergebnis = true;
		}

		dbConn.close();
		return ergebnis;
	}

	// Die gesendeten Freundschaftsanfragen von Benutzer anzeigen zur Orientierung,
	// welche Anfragen von den Freunden noch nicht angenommen wurden
	public void offeneAnfragenVonBenutzerGesendet() throws SQLException {

		this.gesendeteOffeneFreundschaftsanfragen.clear();
		this.benutzeridEinspielen();

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT F.FREUND_ID, B.VORNAME, B.NACHNAME" + " FROM FREUNDSCHAFT F"
				+ " INNER JOIN BENUTZER B" + " ON F.FREUND_ID = B.BENUTZER_ID" + " WHERE F.BENUTZER_ID = '"
				+ this.benutzer_id + "' AND ANFRAGE = 1";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			Freund offenerFreund = new Freund(dbRes.getInt("freund_id"), dbRes.getString("vorname"),
					dbRes.getString("nachname"));
			this.gesendeteOffeneFreundschaftsanfragen.add(offenerFreund);
		}
		dbConn.close();
	}

	// Darstellung der offenen und eigenen gesendeten Freundschaftsanfragen in HTML
	public String getHTMLAllerOffenenAnfragenVonBenutzer() throws SQLException {

		String html = "<h5>Gesendete und unbeantwortete Freundschaftsanfragen</h5><br/><br/><tr><td>";

		if (!this.gesendeteOffeneFreundschaftsanfragen.isEmpty()) {
		
		for (int i = 0; i < this.gesendeteOffeneFreundschaftsanfragen.size(); i++) {
			Freund neuerFreund = this.gesendeteOffeneFreundschaftsanfragen.get(i);
			this.richtigesBild(neuerFreund.getFreund_id());
			html += "<a href='../profil/ProfilAppl.jsp?geheZu=FreundeProfilView.jsp&ausgewaehlteFreund_id="
					+ neuerFreund.getFreund_id() + "' class=\"w3-button\"> <img src ='../img/" + this.getBild() + "' alt='"
					+ neuerFreund.getNachname() + "'" + "title='" + neuerFreund.getNachname()
					+ "' width='50' height='50' />" + neuerFreund.getVorname() + " " + neuerFreund.getNachname()
					+ "</a><br/>";

		}
		}
		return html;
	}


	public String getHTMLNachrichtSchreiben() {
		// @ Sommer, dieser Link soll noch als Button dargestellt werden (Nachricht schreiben)
		String html = "";
		html += "<a href='../nachricht/NachrichtAppl.jsp?geheZu=NachrichtView.jsp" + "&empfaenger_id="
				+ this.ausgewaehlteFreund_id + "' class=\"w3-button w3-theme-l4\"> Nachricht schreiben </a><br/>";

		return html;
	}
	

	
	// Diese Methode wird für das eigene Profil sowie die Freunde/Benutzer zur
	// Darstellung des Alters in der View verwendet
	public int getAlterFuerBenutzer() {
		return this.getAlter(this.geburtstag);
	}

	// Diese Methode wird für das Personenmatching verwendet in StartBean, um das
	// Alter für die aus der DB ausgelesenen Benutzer zu berechnen
	public int getAlter(String geburtsdatum) {

		if (!geburtsdatum.equals("")) {

			GregorianCalendar gcal = new GregorianCalendar();
			int tagheute = gcal.get(Calendar.DAY_OF_MONTH);
			int monatheute = gcal.get(Calendar.MONTH) + 1;
			int jahrheute = gcal.get(Calendar.YEAR);

			String geburtsjahr = geburtsdatum.substring(6, 10);
			String geburtsmonat = geburtsdatum.substring(3, 5);
			String geburtstag = geburtsdatum.substring(0, 2);

			int jahr = Integer.parseInt(geburtsjahr);
			int monat = Integer.parseInt(geburtsmonat);
			int tag = Integer.parseInt(geburtstag);

			if ((tagheute >= tag && monat == monatheute) || (monatheute > monat)) {
				return jahrheute - jahr;
			} else {
				return jahrheute - jahr - 1;
			}
		} else {
			return 0;
		}
	}

	// Darstellung der Auswahl des Geschlechts für Freunde als Auswahlmenü. Das
	// derzeit ausgewählte erscheint oben
	public String getGeschlechtPersonOption() {

		String html = "<select name='geschlechtPers'>";
		String[] geschlechtPerson = new String[] { "beides", "weiblich", "männlich" };
		for (int i = 0; i < geschlechtPerson.length; i++) {
			if (this.geschlechtPerson.equals(geschlechtPerson[i])) {
				html += "<br/><option selected value='" + geschlechtPerson[i] + "'>" + geschlechtPerson[i]
						+ "</option>";
			} else {
				html += "<br/><option value='" + geschlechtPerson[i] + "'>" + geschlechtPerson[i] + "</option>";
			}
		}
		return html;
	}

	// Darstellung der Auswahl des Geschlechts im eigenen Profil als Auswahlmenü.
	// Das derzeit ausgewählte erscheint oben
	public String getGeschlechtBenutzerOption() {
		String html = "";
		if (this.geschlechtBenutzer.equals("weiblich")) {
			html += "<select name='geschlecht'>" + "<option selected value='weiblich'>weiblich</option>"
					+ "<option value='männlich'>männlich</option>" + "</select>";
		} else {
			html += "<select name='geschlecht'>" + "<option selected value='männlich'>männlich</option>"
					+ "<option value='weiblich'>weiblich</option>" + "</select>";
		}

		return html;

	}

	// Darstellung der Auswahl seines eigenes Beziehungsstatus im eigenen Profil als
	// Auswahlmenü. Das derzeit ausgewählte erscheint oben
	public String getBeziehungsstatusOption() {
		String html = "<select name='beziehungsstatus'>";
		String[] bezstatus = new String[] { "keine Angabe", "Single", "Vergeben", "Verheiratet", "Geschieden", "F+" };

		for (int i = 0; i < bezstatus.length; i++) {
			if (this.beziehungsstatus.equals(bezstatus[i])) {

				html += "<br/><option selected value='" + bezstatus[i] + "'>" + bezstatus[i] + "</option>";
			} else {
				html += "<br/><option value='" + bezstatus[i] + "'>" + bezstatus[i] + "</option>";
			}
		}
		html += "</select>";
		return html;
	}

	public String getKurzprofilAnzeigen() {

		String html = "<textarea name='kurzprofil'	cols='15' rows='4'";

		if (this.kurzprofil.equals("")) {
			html += "placeholder='Hier ist Platz für dein Kurzprofil'></textarea>";
		} else
			html += ">" + this.kurzprofil + "</textarea>";

		return html;
	}

	public String getMindestalterMaximalalterOption() throws SQLException {
		this.getDatenAusPersoneninteresse(this.benutzer_id);

		String html = "<select name='mindestalter'>";
		String[] alter = new String[] { "16", "18", "20", "22", "24", "26", "28", "30", "32", "34", "36",
				"38", "40" };

		for (int i = 0; i < alter.length; i++) {
			if (this.mindestalter.equals(alter[i])) {

				html += "<br/><option selected value='" + alter[i] + "'>" + alter[i] + "</option>";
			} else {
				html += "<br/><option value='" + alter[i] + "'>" + alter[i] + "</option>";
			}
		}
		html += "</select> bis <select name='maximalalter'>";

		for (int i = 0; i < alter.length; i++) {
			if (this.maximalalter.equals(alter[i])) {

				html += "<br/><option selected value='" + alter[i] + "'>" + alter[i] + "</option>";
			} else {
				html += "<br/><option value='" + alter[i] + "'>" + alter[i] + "</option>";
			}
		}

		html += "</select>Jahre</td>";

		return html;

	}

	public void richtigesBild(int id) throws SQLException {

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT BILDURL FROM BENUTZER WHERE BENUTZER_ID =" + id;
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			this.bild = dbRes.getString("bildurl");
		}

	}

	public String getHTMLBild() throws SQLException {
		this.richtigesBild(this.benutzer_id);
		String html = "";
		html = "<img src ='../img/" + this.getBild() + "' width='150' height='150'>";

		return html;
	}

	public String getHTMLBildFreunde() throws SQLException {
		this.richtigesBild(this.ausgewaehlteFreund_id);
		String html = "";
		html = "<img src ='../img/" + this.getBild() + "' width='150' height='150'>";

		return html;
	}

	// Getter + Setter

	public int getBenutzer_id() {
		return benutzer_id;
	}

	public ArrayList<Freund> getFreunde() {
		return meineFreunde;
	}

	public void setFreunde(ArrayList<Freund> freunde) {
		meineFreunde = freunde;
	}

	public int getAusgewaehlteFreund_id() {
		return ausgewaehlteFreund_id;
	}

	public void setAusgewaehlteFreund_id(String ausgewaehlteFreund_id) {
		this.ausgewaehlteFreund_id = Integer.parseInt(ausgewaehlteFreund_id);
	}

	public String[] getMeineInteressenFuerDB() {
		return meineInteressenFuerDB;
	}

	public void setMeineInteressenFuerDB(String[] meineInteressenFuerDB) {
		this.meineInteressenFuerDB = meineInteressenFuerDB;
	}

	public String getGeschlechtBenutzer() {
		return geschlechtBenutzer;
	}

	public void setGeschlechtBenutzer(String geschlechtBenutzer) {
		this.geschlechtBenutzer = geschlechtBenutzer;
	}

	public ArrayList<Interesse> getAlleInteressen() {
		return alleInteressen;
	}

	public void setAlleInteressen(ArrayList<Interesse> alleInteressen) {
		this.alleInteressen = alleInteressen;
	}

	public ArrayList<Freund> getMeineFreunde() {
		return meineFreunde;
	}

	public void setMeineFreunde(ArrayList<Freund> meineFreunde) {
		this.meineFreunde = meineFreunde;
	}

	public ArrayList<Benutzer> getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(ArrayList<Benutzer> benutzer) {
		this.benutzer = benutzer;
	}

	public ArrayList<Freund> getMeineFreundschaftsanfragen() {
		return meineFreundschaftsanfragen;
	}

	public void setMeineFreundschaftsanfragen(ArrayList<Freund> meineFreundschaftsanfragen) {
		this.meineFreundschaftsanfragen = meineFreundschaftsanfragen;
	}

	public ArrayList<Interesse> getMeineInteressenAusDB() {
		return meineInteressenAusDB;
	}

	public void setMeineInteressenAusDB(ArrayList<Interesse> meineInteressenAusDB) {
		this.meineInteressenAusDB = meineInteressenAusDB;
	}

	public String getMindestalter() {

		return mindestalter;
	}

	public int getMindestalterInt() {

		int mindestalterInt = Integer.parseInt(mindestalter);
		return mindestalterInt;
	}

	public void setMindestalter(String mindestalter) {
		this.mindestalter = mindestalter;
	}

	public String getMaximalalter() {

		return maximalalter;
	}

	public int getMaximalalterInt() {
		int maximalalterInt = Integer.parseInt(maximalalter);
		return maximalalterInt;
	}

	public void setMaximalalter(String maximalalter) {

		this.maximalalter = maximalalter;
	}

	public String getGeschlechtPerson() {
		return geschlechtPerson;
	}

	public void setGeschlechtPerson(String geschlechtPerson) {
		this.geschlechtPerson = geschlechtPerson;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getWohnort() {
		return wohnort;
	}

	public void setWohnort(String wohnort) {
		this.wohnort = wohnort;
	}

	public String getBeziehungsstatus() {
		return beziehungsstatus;
	}

	public void setBeziehungsstatus(String beziehungsstatus) {
		this.beziehungsstatus = beziehungsstatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getKurzprofil() {
		return kurzprofil;
	}

	public void setKurzprofil(String kurzprofil) {
		this.kurzprofil = kurzprofil;
	}

	public int getPersoneninteressenid() {
		return personeninteressenid;
	}

	public void setPersoneninteressenid(int personeninteressenid) {
		this.personeninteressenid = personeninteressenid;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getGeburtstag() {
		return geburtstag;
	}

	public void setGeburtstag(String geburtstag) {
		this.geburtstag = geburtstag;
	}

	public String getAntwortFreundschaftsanfrage() {
		return antwortFreundschaftsanfrage;
	}

	public void setAntwortFreundschaftsanfrage(String antwortFreundschaftsanfrage) {
		this.antwortFreundschaftsanfrage = antwortFreundschaftsanfrage;
	}

	public ArrayList<String> getAlleStaedte() {
		return alleStaedte;
	}

	public void setAlleStaedte(ArrayList<String> alleStaedte) {
		this.alleStaedte = alleStaedte;
	}

	public String[] getMeineStaedteFuerDB() {
		return meineStaedteFuerDB;
	}

	public void setMeineStaedteFuerDB(String[] meineStaedteFuerDB) {
		this.meineStaedteFuerDB = meineStaedteFuerDB;
	}

	public ArrayList<String> getMeineStaedteAusDB() {
		return meineStaedteAusDB;
	}

	public void setMeineStaedteAusDB(ArrayList<String> meineStaedteAusDB) {
		this.meineStaedteAusDB = meineStaedteAusDB;
	}

	public ArrayList<Freund> getGesendeteOffeneFreundschaftsanfragen() {
		return gesendeteOffeneFreundschaftsanfragen;
	}

	public void setGesendeteOffeneFreundschaftsanfragen(ArrayList<Freund> gesendeteOffeneFreundschaftsanfragen) {
		this.gesendeteOffeneFreundschaftsanfragen = gesendeteOffeneFreundschaftsanfragen;
	}

	public String getBild() {
		return bild;
	}

	public void setBild(String bild) {
		this.bild = bild;
	}

}
