package de.hs_lu.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import de.hs_lu.jdbc.PostgreSQLAccess;

public class NachrichtBean {

	int letzteNachrichtid;

	// Methoden aus anderen Beans verwenden
	ProfilBean profilbean;
	StartBean startbean;
	GruppeBean gruppebean;
	VeranstaltungBean veranstaltungbean;

	// Für Tabelle Nachricht
	int sender_id;
	int nachrichtid;
	String nachrichtentext;

	// Für Tabelle Nachricht_an_Benutzer
	int empfaenger_id;
	String vorname;
	String nachname;
	ArrayList<Nachricht> nachrichtenFuerBenutzer;
	ArrayList<Nachricht> neueNachrichten;
	ArrayList<Benutzer> benutzerMitDenenIchSchreibe;

	// Variablen für Einlesen Vor- und Nachname, wenn eine neue Nachricht über
	// NachrichtView verschickt wird
	String vornamenachricht;
	String nachnamenachricht;

	// Für Tabelle Nachricht_in_Gruppe
	int gruppe_id_nachricht;
	ArrayList<Nachricht> nachrichtenFuerGruppe;

	// Für Tabelle Nachricht_in_Veranstaltung
	int veranstaltung_id_nachricht;
	ArrayList<Nachricht> nachrichtenFuerVeranstaltung;

	public NachrichtBean() throws SQLException {

		this.letzteNachrichtid = 0;
		this.profilbean = new ProfilBean();
		this.startbean = new StartBean();
		this.gruppebean = new GruppeBean();
		this.veranstaltungbean = new VeranstaltungBean();
		this.sender_id = 0;
		this.nachrichtid = 0;
		this.nachrichtentext = "";
		this.empfaenger_id = 0;
		this.vorname = "";
		this.nachname = "";
		this.vornamenachricht = "";
		this.nachnamenachricht = "";
		this.nachrichtenFuerBenutzer = new ArrayList<>();
		this.neueNachrichten = new ArrayList<>();
		this.benutzerMitDenenIchSchreibe = new ArrayList<>();
		this.gruppe_id_nachricht = 0;
		this.nachrichtenFuerGruppe = new ArrayList<>();
		this.veranstaltung_id_nachricht = 0;
		this.nachrichtenFuerVeranstaltung = new ArrayList<>();

	}

	public void benutzeridEinspielen() {
		this.sender_id = WillkommenBean.getBenutzer_id();

	}

// letzte NachrichtID abrufen. Diese wird genutzt, um die Nachricht zu identifizieren und richtiger Tabelle zuzuordnen	
	public void abrufLetzteNachrichtid() throws SQLException {

		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "SELECT MAX(NACHRICHT_ID) FROM NACHRICHT";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		if (dbRes.next()) {
			this.letzteNachrichtid = dbRes.getInt("max");
		} else {
		}
		dbConn.close();
	}

// Nachricht in Tabelle Nachricht speichern	
	public void nachrichtSpeichern() throws SQLException {

		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "INSERT INTO NACHRICHT (NACHRICHTENTEXT, SENDER_ID, GELESEN)" + " VALUES (?,?,?)";
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setString(1, this.nachrichtentext);
		prep.setInt(2, this.sender_id);
		prep.setInt(3, 1); // diese Nachricht wird immer gleich als gelesen gespeichert
		prep.executeUpdate();

		dbConn.close();
	}

// Nachricht  richtiger Tabelle Gruppe oder Veranstaltung zuordnen und darin speichern
	public void nachrichtRichtigerTabelleZuordnen(String tabelle, String tabellenid, int id) throws SQLException {

		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "INSERT INTO " + tabelle + " (" + tabellenid + ", NACHRICHT_ID)" + " VALUES (?,?)";
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setInt(1, id);
		prep.setInt(2, this.letzteNachrichtid);
		prep.executeUpdate();
		dbConn.close();
	}

// Nachricht an Benutzer mit Hinterlegung ungelesen in Datenbank (ungelesen = int 0)
	public void nachrichtAnEinenBenutzer() throws SQLException {

		String tabelle = "NACHRICHT_AN_BENUTZER";
		String tabellenid = "EMPFAENGER_ID";
		int id = this.empfaenger_id;

		this.benutzeridEinspielen();
		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "INSERT INTO NACHRICHT (NACHRICHTENTEXT, SENDER_ID, GELESEN)" + " VALUES (?,?,?)";
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setString(1, this.nachrichtentext);
		prep.setInt(2, this.sender_id);
		prep.setInt(3, 0);
		prep.executeUpdate();

		this.abrufLetzteNachrichtid();
		this.nachrichtRichtigerTabelleZuordnen(tabelle, tabellenid, id);
		dbConn.close();
	}

// Sind für den Benutzer ungelesen Nachrichten vorhanden
	public boolean neueNachrichten() throws SQLException {
		this.benutzeridEinspielen();

		this.neueNachrichten.clear();

		Connection dbConn = new PostgreSQLAccess().getConnection();
		boolean ergebnis = false;
		String sql = "SELECT NACH.NACHRICHT_ID, NACH.SENDER_ID, BE.VORNAME, BE.NACHNAME FROM NACHRICHT NACH"
				+ " INNER JOIN NACHRICHT_AN_BENUTZER NB" + " ON NACH.NACHRICHT_ID = NB.NACHRICHT_ID"
				+ " INNER JOIN BENUTZER BE" + " ON NACH.SENDER_ID = BE.BENUTZER_ID" + " WHERE NB.EMPFAENGER_ID = "
				+ this.sender_id + " AND GELESEN = 0";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		while (dbRes.next()) {

			Nachricht nachricht = new Nachricht(dbRes.getInt("nachricht_id"), dbRes.getInt("sender_id"),
					dbRes.getString("vorname"), dbRes.getString("nachname"));
			this.neueNachrichten.add(nachricht);
			ergebnis = true;
		}
		dbConn.close();
		return ergebnis;
	}

	public String getInfoNeueNachrichten() throws SQLException {
		String html = "";

		if (!this.neueNachrichten.isEmpty()) {
			html = "<h5>Du hast neue Nachrichten von:</h5><br/>";
			for (int y = 0; y < this.neueNachrichten.size(); y++) {
				Nachricht neu1 = this.neueNachrichten.get(y);

				for (int x = y + 1; x < this.neueNachrichten.size(); x++) {
					Nachricht neu2 = this.neueNachrichten.get(x);
					if (neu1.getSender_id() == neu2.getSender_id()) {
						this.neueNachrichten.remove(neu2);
					}
				}
			}

			for (Nachricht nachricht : this.neueNachrichten) {
				profilbean.richtigesBild(nachricht.getSender_id());
				html += "<a href='../nachricht/NachrichtAppl.jsp?geheZu=NachrichtView.jsp&empfaenger_id="
						+ nachricht.getSender_id() + "' class=\"w3-button\"> <img src ='../img/" + profilbean.getBild() + "'" + "title='"
						+ nachricht.getSender_nachname() + "' width='50' height='50' />" + nachricht.getSender_vorname()
						+ " " + nachricht.getSender_nachname() + "</a><br/>";

			}

		}
		return html;
	}

// Nachricht wird in DB als gelesen markiert, wenn man in NachrichtView geht
	public void nachrichtGelesen() throws SQLException {
		this.benutzeridEinspielen();

		if (!neueNachrichten.isEmpty()) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			for (Nachricht nachricht : neueNachrichten) {

				String sql = "UPDATE NACHRICHT SET GELESEN = ? WHERE NACHRICHT_ID = " + nachricht.getNachricht_id() + ""
						+ " AND SENDER_ID = " + nachricht.getSender_id();

				PreparedStatement prep = dbConn.prepareStatement(sql);

				prep.setInt(1, 1);
				prep.executeUpdate();

			}
			dbConn.close();
		}

	}

// Nachrichten für Gruppen speichern
	public void nachrichtInGruppe() throws SQLException {

		String tabelle = "NACHRICHT_IN_GRUPPE";
		String tabellenid = "GRUPPE_ID";
		int id = this.gruppe_id_nachricht;

		this.nachrichtSpeichern();
		this.abrufLetzteNachrichtid();
		this.nachrichtRichtigerTabelleZuordnen(tabelle, tabellenid, id);

	}

// Nachrichten für Veranstaltungen speichern
	public void nachrichtInVeranstaltung() throws SQLException {

		String tabelle = "NACHRICHT_IN_VERANSTALTUNG";
		String tabellenid = "VERANSTALTUNG_ID";
		int id = this.veranstaltung_id_nachricht;

		this.nachrichtSpeichern();
		this.abrufLetzteNachrichtid();
		this.nachrichtRichtigerTabelleZuordnen(tabelle, tabellenid, id);

	}

// Nachrichten von Benutzer für Benutzer auslesen
	public void nachrichtenAuslesenFuerBenutzer() throws SQLException {

		this.nachrichtenFuerBenutzer.clear();

		if (empfaenger_id != 0) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "SELECT NACH.NACHRICHT_ID, NACH.NACHRICHTENTEXT, NACH.SENDER_ID, NB.EMPFAENGER_ID FROM NACHRICHT NACH INNER JOIN NACHRICHT_AN_BENUTZER NB"
					+ " ON NACH.NACHRICHT_ID = NB.NACHRICHT_ID " + " WHERE (SENDER_ID = " + this.sender_id
					+ " AND EMPFAENGER_ID = " + this.empfaenger_id + ") OR (SENDER_ID = " + this.empfaenger_id
					+ " AND EMPFAENGER_ID = " + this.sender_id + ")";
			ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

			while (dbRes.next()) {
				Nachricht nachricht = new Nachricht(dbRes.getInt("nachricht_id"), dbRes.getString("nachrichtentext"),
						dbRes.getInt("sender_id"), dbRes.getInt("empfaenger_id"));
				this.nachrichtenFuerBenutzer.add(nachricht);
			}
			dbConn.close();
		}
	}

	public void getHTMLNameVonEmpfaengerID() throws SQLException {

		if (this.empfaenger_id != 0) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "SELECT VORNAME, NACHNAME FROM BENUTZER WHERE BENUTZER_ID = " + this.empfaenger_id;
			ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
			if (dbRes.next()) {
				this.vorname = dbRes.getString("vorname");
				this.nachname = dbRes.getString("nachname");
			}
			dbConn.close();
		} else {
			this.vorname = "";
			this.nachname = "";
		}

	}
// Darstellung der Nachrichten an Benutzer mit dem Namen (Dein Chat mit: [name]) (Methode HTMLNameVonEmpfaengerID)
// wenn kein Chat ausgewählt wurde, ist die "Startseite" links "Deine Nachrichten"
// und rechts ist es möglich, eine Nachricht an einen beliebigen Benutzer zu schreiben

	public String getHTMLNachrichtenAnBenutzer() throws SQLException {

		this.getHTMLNameVonEmpfaengerID();

		String html = "";
		if (this.empfaenger_id != 0) {

			html += "<td valign=top><h5>Dein Chat mit " + this.vorname + " " + this.nachname + ":</h5><br/><br/>";

			if (!this.nachrichtenFuerBenutzer.isEmpty()) {
				Connection dbConn = new PostgreSQLAccess().getConnection();
				for (Nachricht nachricht : nachrichtenFuerBenutzer) {
					String sql2 = "SELECT VORNAME, NACHNAME FROM BENUTZER WHERE BENUTZER_ID = "
							+ nachricht.getSender_id();
					ResultSet dbRes2 = dbConn.createStatement().executeQuery(sql2);
					if (dbRes2.next()) {
						Nachricht neueNachricht = new Nachricht();
						neueNachricht.setSender_vorname(dbRes2.getString("vorname"));
						neueNachricht.setSender_nachname(dbRes2.getString("nachname"));

						html += "<div class=\"container\" >" + neueNachricht.getSender_vorname() + " "
								+ neueNachricht.getSender_nachname() + ": "
								+ nachricht.getNachrichtentext() + "</div>";
					}
				}
				dbConn.close();
			}
			html += "<textarea name='nachricht' cols='30' rows '200'></textarea><br/><input type='submit'class=\"w3-button w3-theme-l4\" name='senden' value='senden'></td>";

		} else {

			html = "<td valign=top><h5>Neue Nachricht an:</h5><br/>"
					+ "<table><tr><td>Vorname:</td><td><input type='text' name ='vornamenachricht' value=''></td></tr>"
					+ "<tr><td>Nachname:</td><td><input type='text' name ='nachnamenachricht' value=''></td></tr></table>";

			html += "<br/><br/><textarea name='nachricht' cols='50' rows '200'></textarea><br/>"
					+ "<input type='submit' class=\"w3-button w3-theme-l4\" name='senden' value='senden'></td>";
		}
		return html;
	}

// Prüfen, ob Vorname, Nachname bei "Neue Nachricht an" in NachrichtView existiert
	public boolean benutzeridAuslesenVonBenutzerNeueNachricht() throws SQLException {
		boolean ergebnis = false;

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT BENUTZER_ID FROM BENUTZER WHERE VORNAME = '" + this.vornamenachricht + "' AND NACHNAME = '"
				+ this.nachnamenachricht + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			this.empfaenger_id = dbRes.getInt("benutzer_id");
			ergebnis = true;
		}

		dbConn.close();
		return ergebnis;

	}

//Benutzer auslesen, mit denen schon ein Chat besteht
	public void benutzerAuslesenMitDenenGeschriebenWird() throws SQLException {

		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.benutzeridEinspielen();

		this.benutzerMitDenenIchSchreibe.clear();

		ArrayList<Benutzer> benutzerids = new ArrayList<>();

		// Nachrichten, die ich empfangen habe und evtl. noch nicht geantwortet habe
		String sql = "SELECT DISTINCT NACH.SENDER_ID FROM NACHRICHT_AN_BENUTZER NB INNER JOIN NACHRICHT NACH ON NB.NACHRICHT_ID = NACH.NACHRICHT_ID"
				+ " WHERE EMPFAENGER_ID = " + this.sender_id;
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			int benutzerid = dbRes.getInt("sender_id");
			Benutzer neuerBenutzer = new Benutzer(benutzerid);
			benutzerids.add(neuerBenutzer);

		}
		// Nachrichten, die ich gesendet habe und evtl. noch keine Antwort habe
		String sql1 = "SELECT DISTINCT NB.EMPFAENGER_ID FROM NACHRICHT_AN_BENUTZER NB INNER JOIN NACHRICHT NACH ON NB.NACHRICHT_ID = NACH.NACHRICHT_ID"
				+ " WHERE SENDER_ID = " + this.sender_id;
		ResultSet dbRes1 = dbConn.createStatement().executeQuery(sql1);
		while (dbRes1.next()) {
			int benutzerid = dbRes1.getInt("empfaenger_id");
			Benutzer neuerBenutzer = new Benutzer(benutzerid);
			benutzerids.add(neuerBenutzer);

		}

		// Doppelte Benutzerids aus ArrayList benutzerids löschen
		for (int y = 0; y < benutzerids.size(); y++) {
			Benutzer neu1 = benutzerids.get(y);

			for (int x = y + 1; x < benutzerids.size(); x++) {
				Benutzer neu2 = benutzerids.get(x);
				if (neu1.getBenutzeridVonArrayList() == neu2.getBenutzeridVonArrayList()) {
					benutzerids.remove(neu2);
				}
			}
		}

		for (int l = 0; l < benutzerids.size(); l++) {
			Benutzer neueID = benutzerids.get(l);
			String sql2 = "SELECT BENUTZER_ID, VORNAME, NACHNAME FROM BENUTZER WHERE BENUTZER_ID = "
					+ neueID.getBenutzeridVonArrayList();
			ResultSet dbRes2 = dbConn.createStatement().executeQuery(sql2);
			if (dbRes2.next()) {
				Benutzer neuerBenutzer = new Benutzer(dbRes2.getInt("benutzer_id"), dbRes2.getString("vorname"),
						dbRes2.getString("nachname"));
				this.benutzerMitDenenIchSchreibe.add(neuerBenutzer);
			}
		}
		dbConn.close();
	}

	// class für Button wurde hinzugefügt

	public String getHTMLAlleBenutzerMitDenenIchSchreibe() throws SQLException {

		String html = "<td valign=top><h5>Deine Nachrichten:</h5><br/>";
		if (!this.benutzerMitDenenIchSchreibe.isEmpty()) {
			for (Benutzer benutzer : benutzerMitDenenIchSchreibe) {
				profilbean.richtigesBild(benutzer.getBenutzeridVonArrayList());
				html += "<a href='./NachrichtAppl.jsp?geheZu=NachrichtView.jsp&empfaenger_id="
						+ benutzer.getBenutzeridVonArrayList() + "' class=\"w3-button\"> <img src ='../img/"
						+ profilbean.getBild() + "' alt='" + benutzer.getNachname() + "'" + "title='"
						+ benutzer.getNachname() + "' width='50' height='50' />" + benutzer.getVorname() + " "
						+ benutzer.getNachname() + "</a><br/>";

			}
		} else {
			html += "</td>";
		}
		return html;
	}

// Nachrichten für Gruppe auslesen
	public void nachrichtenAuslesenFuerGruppe() throws SQLException {

		ArrayList<Nachricht> nachrichtids = new ArrayList<>();
		this.nachrichtenFuerGruppe.clear();

		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "SELECT NACHRICHT_ID FROM NACHRICHT_IN_GRUPPE WHERE GRUPPE_ID = " + this.gruppe_id_nachricht;
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		while (dbRes.next()) {
			Nachricht id = new Nachricht(dbRes.getInt("nachricht_id"));
			nachrichtids.add(id);
		}

		for (int i = 0; i < nachrichtids.size(); i++) {
			Nachricht neueID = nachrichtids.get(i);
			String sql1 = "SELECT NACHRICHTENTEXT, SENDER_ID FROM NACHRICHT WHERE NACHRICHT_ID = "
					+ neueID.getNachricht_id();
			ResultSet dbRes1 = dbConn.createStatement().executeQuery(sql1);
			if (dbRes1.next()) {
				Nachricht neueNachricht = new Nachricht(neueID.getNachricht_id(), dbRes1.getString("nachrichtentext"),
						dbRes1.getInt("sender_id"));
				this.nachrichtenFuerGruppe.add(neueNachricht);
			}
		}
		dbConn.close();
	}

	public String getHTMLNachrichtenInGruppe() throws SQLException {

		String html = "";

		if (!this.nachrichtenFuerGruppe.isEmpty()) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			for (Nachricht nachricht : nachrichtenFuerGruppe) {
				String sql2 = "SELECT VORNAME, NACHNAME FROM BENUTZER WHERE BENUTZER_ID = " + nachricht.getSender_id();
				ResultSet dbRes2 = dbConn.createStatement().executeQuery(sql2);
				if (dbRes2.next()) {
					Nachricht neueNachricht = new Nachricht();
					neueNachricht.setSender_vorname(dbRes2.getString("vorname"));
					neueNachricht.setSender_nachname(dbRes2.getString("nachname"));

					html += "<div class= \"container\">" + neueNachricht.getSender_vorname() + " "
							+ neueNachricht.getSender_nachname() + ": " + nachricht.getNachrichtentext()
							+ "</div>";
					
					
				}

				/*
				 * <div class="container"> </div>
				 * "<a href=\'../suche/SucheAppl.jsp?geheZu=SucheVeranstaltungView.jsp\'class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" "
				 * +
				 * "style=\"width:11% \" title=\"Suche\"><i class=\"fa fa-search\"></i></a> \r\n"
				 */
			}
			
			dbConn.close();
		}
		html += "<br/><br/><textarea name='nachricht' cols='50' rows '200'></textarea><br/>"
				+ "<input type='submit' class=\"w3-button w3-theme-l4\" name='senden' value='senden'></td>";
		return html;
	}

// Nachrichten auslesen für Veranstaltungen
	public void nachrichtenAuslesenFuerVeranstaltungen() throws SQLException {
		ArrayList<Nachricht> nachrichtids = new ArrayList<>();

		this.nachrichtenFuerVeranstaltung.clear();

		Connection dbConn = new PostgreSQLAccess().getConnection();

		String sql = "SELECT NACHRICHT_ID FROM NACHRICHT_IN_VERANSTALTUNG WHERE VERANSTALTUNG_ID = "
				+ this.veranstaltung_id_nachricht;
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			Nachricht id = new Nachricht(dbRes.getInt("nachricht_id"));
			nachrichtids.add(id);
		}

		for (int i = 0; i < nachrichtids.size(); i++) {
			Nachricht neueID = nachrichtids.get(i);
			String sql1 = "SELECT NACHRICHTENTEXT, SENDER_ID FROM NACHRICHT WHERE NACHRICHT_ID = "
					+ neueID.getNachricht_id();
			ResultSet dbRes1 = dbConn.createStatement().executeQuery(sql1);
			if (dbRes1.next()) {
				Nachricht neueNachricht = new Nachricht(neueID.getNachricht_id(), dbRes1.getString("nachrichtentext"),
						dbRes1.getInt("sender_id"));
				this.nachrichtenFuerVeranstaltung.add(neueNachricht);
			}
		}
		dbConn.close();
	}

	public String getHTMLNachrichtenInVeranstaltung() throws SQLException {

		String html = "";

		if (!this.nachrichtenFuerVeranstaltung.isEmpty()) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			for (Nachricht nachricht : nachrichtenFuerVeranstaltung) {

				String sql2 = "SELECT VORNAME, NACHNAME FROM BENUTZER WHERE BENUTZER_ID = " + nachricht.getSender_id();
				ResultSet dbRes2 = dbConn.createStatement().executeQuery(sql2);
				if (dbRes2.next()) {
					Nachricht neueNachricht = new Nachricht();
					neueNachricht.setSender_vorname(dbRes2.getString("vorname"));
					neueNachricht.setSender_nachname(dbRes2.getString("nachname"));

					html += "<div class=\"container\"> " + neueNachricht.getSender_vorname() + " "
							+ neueNachricht.getSender_nachname() + ": " + nachricht.getNachrichtentext()
							+ "</div>";
					
					
				}
			}
			
			dbConn.close();
		}
		html += "<br/><br/><textarea name='nachricht' cols='50' rows '200'></textarea><br/>"
				+ "<input type='submit' class=\"w3-button w3-theme-l4\" name='senden' value='senden'></td>";
		return html;
	}

// Getter + Setter

	public int getLetzteNachrichtid() {
		return letzteNachrichtid;
	}

	public void setLetzteNachrichtid(int letzteNachrichtid) {
		this.letzteNachrichtid = letzteNachrichtid;
	}

	public int getNachrichtid() {
		return nachrichtid;
	}

	public void setNachrichtid(int nachrichtid) {
		this.nachrichtid = nachrichtid;
	}

	public String getNachrichtentext() {
		return nachrichtentext;
	}

	public void setNachrichtentext(String nachrichtentext) {
		this.nachrichtentext = nachrichtentext;
	}

	public int getEmpfaenger_id() {
		return empfaenger_id;
	}

	public void setEmpfaenger_id(String empfaenger_id) {
		this.empfaenger_id = Integer.parseInt(empfaenger_id);
	}

	public int getSender_id() {
		return sender_id;
	}

	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}

	public int getGruppe_id_nachricht() {
		return gruppe_id_nachricht;
	}

	public void setGruppe_id_nachricht(String gruppe_id_nachricht) {
		this.gruppe_id_nachricht = Integer.parseInt(gruppe_id_nachricht);
	}

	public int getVeranstaltung_id_nachricht() {
		return veranstaltung_id_nachricht;
	}

	public void setVeranstaltung_id_nachricht(String veranstaltung_id_nachricht) {
		this.veranstaltung_id_nachricht = Integer.parseInt(veranstaltung_id_nachricht);
	}

	public ArrayList<Nachricht> getNachrichtenFuerGruppe() {
		return nachrichtenFuerGruppe;
	}

	public void setNachrichtenFuerGruppe(ArrayList<Nachricht> nachrichtenFuerGruppe) {
		this.nachrichtenFuerGruppe = nachrichtenFuerGruppe;
	}

	public ArrayList<Nachricht> getNachrichtenFuerVeranstaltung() {
		return nachrichtenFuerVeranstaltung;
	}

	public void setNachrichtenFuerVeranstaltung(ArrayList<Nachricht> nachrichtenFuerVeranstaltung) {
		this.nachrichtenFuerVeranstaltung = nachrichtenFuerVeranstaltung;
	}

	public ArrayList<Benutzer> getMeineNachrichtenBenutzer() {
		return benutzerMitDenenIchSchreibe;
	}

	public void setMeineNachrichtenBenutzer(ArrayList<Benutzer> meineNachrichtenBenutzer) {
		this.benutzerMitDenenIchSchreibe = meineNachrichtenBenutzer;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public ArrayList<Nachricht> getNachrichtenFuerBenutzer() {
		return nachrichtenFuerBenutzer;
	}

	public void setNachrichtenFuerBenutzer(ArrayList<Nachricht> nachrichtenFuerBenutzer) {
		this.nachrichtenFuerBenutzer = nachrichtenFuerBenutzer;
	}

	public ArrayList<Nachricht> getNeueNachrichten() {
		return neueNachrichten;
	}

	public void setNeueNachrichten(ArrayList<Nachricht> neueNachrichten) {
		this.neueNachrichten = neueNachrichten;
	}

	public ArrayList<Benutzer> getBenutzerMitDenenIchSchreibe() {
		return benutzerMitDenenIchSchreibe;
	}

	public void setBenutzerMitDenenIchSchreibe(ArrayList<Benutzer> benutzerMitDenenIchSchreibe) {
		this.benutzerMitDenenIchSchreibe = benutzerMitDenenIchSchreibe;
	}

	public ProfilBean getProfilbean() {
		return profilbean;
	}

	public void setProfilbean(ProfilBean profilbean) {
		this.profilbean = profilbean;
	}

	public String getVornamenachricht() {
		return vornamenachricht;
	}

	public void setVornamenachricht(String vornamenachricht) {
		this.vornamenachricht = vornamenachricht;
	}

	public String getNachnamenachricht() {
		return nachnamenachricht;
	}

	public void setNachnamenachricht(String nachnamenachricht) {
		this.nachnamenachricht = nachnamenachricht;
	}

	public void setGruppe_id_nachricht(int gruppe_id_nachricht) {
		this.gruppe_id_nachricht = gruppe_id_nachricht;
	}

	public void setVeranstaltung_id_nachricht(int veranstaltung_id_nachricht) {
		this.veranstaltung_id_nachricht = veranstaltung_id_nachricht;
	}

}
