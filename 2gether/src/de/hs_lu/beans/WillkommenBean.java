package de.hs_lu.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hs_lu.jdbc.NoConnectionException;
import de.hs_lu.jdbc.PostgreSQLAccess;

public class WillkommenBean {
	static int benutzer_id;
	String vorname;
	String nachname;
	String geschlecht;
	String geburtstag;
	String geburtsTag;
	String geburtsMonat;
	String geburtsJahr;
	String wohnort;
	String email;
	String passwort;
	String altesPasswort;
	String neuesPasswort;
	String bestaetigtesPasswort;
	boolean istEingeloggt;
	String logout;

	public WillkommenBean() {
		WillkommenBean.benutzer_id = 0; // 0 richtig?
		this.vorname = "";
		this.nachname = "";
		this.geschlecht = "";
		this.geburtstag = "";
		this.geburtsTag = "";
		this.geburtsMonat = "";
		this.geburtsJahr = "";
		this.wohnort = "";
		this.email = "";
		this.passwort = "";
		this.altesPasswort = "";
		this.neuesPasswort = "";
		this.bestaetigtesPasswort = "";
		this.istEingeloggt = false;
		this.logout = "";
	}

//Logout
	public void logout() {
		this.istEingeloggt = false;
		WillkommenBean.benutzer_id = 0;
	}

//Account löschen
	public void accountEntfernen() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "DELETE FROM benutzer WHERE benutzer_id= '" + WillkommenBean.benutzer_id + "'";
		dbConn.prepareStatement(sql).executeUpdate();
		dbConn.close();
		this.logout();
	}

//Registrierung
	public String getGeburtstagAuswahl() {
		String html = "";
		int[] geburtsTag = new int[31];
		String[] geburtsTagString = new String[31];
		int[] geburtsMonat = new int[12];
		String[] geburtsMonatString = new String[12];
		int[] geburtsJahr = new int[120];

		// Arrays befüllen
		for (int i = 0; i < geburtsTag.length; i++) {
			geburtsTag[i] = i + 1;
			if (i<9) {
			geburtsTagString[i] = "0"+ Integer.toString(geburtsTag[i]);
			}
			else {
				geburtsTagString[i] = Integer.toString(geburtsTag[i]);
			}
		}
		for (int i = 0; i < geburtsMonat.length; i++) {
			geburtsMonat[i] = i + 1;
			if (i<9) {
				geburtsMonatString[i] = "0"+ Integer.toString(geburtsMonat[i]);
				}
			else {
				geburtsMonatString[i] = Integer.toString(geburtsMonat[i]);
			}
		}
		for (int i = 0; i < geburtsJahr.length; i++) {
			geburtsJahr[i] = 1900 + i;
		}

		// Dropdown Start
		html += "Geburtstag: <br>";
		// Tag
		html += "<select name='geburtsTag'>";
		html += "<option selected>Tag</option>";
		for (int i = 0; i < geburtsTagString.length; i++) {
			html += "<option>" + geburtsTagString[i] + "</option>";
		}
		html += "</select> ";
		// Monat
		html += "<select name='geburtsMonat'>";
		html += "<option selected>Monat</option>";
		for (int i = 0; i < geburtsMonatString.length; i++) {
			html += "<option>" + geburtsMonatString[i] + "</option>";
		}
		html += "</select> ";
		// Jahr
		html += "<select name='geburtsJahr'>";
		html += "<option selected>Jahr</option>";
		for (int i = geburtsJahr.length - 1; i >= 0; i--) {
			html += "<option>" + geburtsJahr[i] + "</option>";
		}
		html += "</select>";
		// Dropdown Ende

		
		 //Textfeld Start
	//	html += "<input type='text' class='w3-center' placeholder='tt.mm.jjjj' name='geburtstag' value=''>"; //Textfeld Ende
		

		return html;
	}

//Registrierung
	public boolean benutzerAnlegenWennNichtExistiert() throws NoConnectionException, SQLException {
		// true - insert hat geklappt
		// false - diesen User gibt's schon in der DB
		// wennWerteZuLangDannAbschneiden
		this.attributeVorbereitenFuerRegDB();
		if (this.ueberpruefenEmailExistiert()) {
			return false;
		}

		else {
			this.benutzerAnlegenOhneUberpruefung();
			return true;
		}
	}

	public void attributeVorbereitenFuerRegDB() throws SQLException {
		if (this.vorname.length() > 100)
			this.vorname = this.vorname.substring(0, 100);
		if (this.nachname.length() > 100)
			this.nachname = this.nachname.substring(0, 100);
		if (this.geschlecht.length() > 20)
			this.geschlecht = this.geschlecht.substring(0, 20);
		if (this.email.length() > 100)
			this.email = this.email.substring(0, 100);
		if (this.passwort.length() > 100)
			this.passwort = this.passwort.substring(0, 100);
		if (this.bestaetigtesPasswort.length() > 100)
			this.bestaetigtesPasswort = this.bestaetigtesPasswort.substring(0, 100);
		
	
		this.geburtstag = this.geburtsTag + "." + this.geburtsMonat + "." + this.geburtsJahr; // Geburtstag Zusammenfassung
	}

//Registrierung + Änderungen
	public void attributeVorbereitenFuerProfilbearbeitungDB() throws SQLException {
		this.attributeVorbereitenFuerRegDB();
		if (this.wohnort.length() > 50)
			this.wohnort = this.wohnort.substring(0, 50);
		if (this.altesPasswort.length() > 100)
			this.altesPasswort = this.altesPasswort.substring(0, 100);
		if (this.neuesPasswort.length() > 100)
			this.neuesPasswort = this.neuesPasswort.substring(0, 100);
	}

	// Registrierung
	public void benutzerAnlegenOhneUberpruefung() throws NoConnectionException, SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "INSERT INTO BENUTZER " + "(vorname, nachname, geschlecht, geburtstag, bildurl, wohnort, email, passwort) "
				+ "values (?,?,?,?,?,?,?,?)";

		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.setString(1, this.vorname);
		prep.setString(2, this.nachname);
		prep.setString(3, this.geschlecht);
		prep.setString(4, this.geburtstag);
		if (this.geschlecht.equals("weiblich")) {
		prep.setString(5, "frau.jpg");
		}
		else {
		prep.setString(5, "mann.jpg");
		}
		prep.setString(6, this.wohnort);
		prep.setString(7, this.email);
		prep.setString(8, this.passwort);
		prep.executeUpdate();
		this.IDAbrufen();
		dbConn.close();
	}

	// Registrierung
	public boolean ueberpruefenEmailExistiert() throws SQLException { // email prüfen
		// gibt es diesen User schon in der DB?
		// true - gibt schon
		// false - gibt's noch nicht
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT EMAIL FROM BENUTZER WHERE EMAIL = ?";
		
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setString(1, email);
		ResultSet dbRes = prep.executeQuery();

		// boolean gefunden = dbRes.next();
		if (dbRes.next() == true) {
			dbConn.close();
			return true; // Das ResultSet findet den User ( Eine Zeile weiter nach unten war möglich)
		} else {
			dbConn.close();
			return false; // User existiert nicht, da das ResultSet nicht weiter gehen konnte.
		}
	}

// Login
	public boolean ueberpruefenEMailPasswort() throws SQLException { // Anmeldung mit Email
		// ja: username/password-Kombination ist gültig
		// d.h. in der Tabelle account gibt es einen Datensatz
		// mit der username UND dem password
		// nein: username/password ist keine gültige Kombination
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT email FROM BENUTZER " + "where email = '" + this.email + "' and " + "passwort = '"
				+ this.passwort + "'";
		
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		if (dbRes.next() == true) {
			this.IDAbrufen();
			dbConn.close();
			return true; // Das ResultSet findet den User (Eine Zeile weiter nach unten war möglich)
		} else {
			dbConn.close();
			return false; // User existiert nicht, da das ResultSet nicht weiter gehen konnte.
		}
	}

//Passwort bei Änderung überprüfen
	public boolean ueberpruefePasswort() throws SQLException {
		this.attributeVorbereitenFuerProfilbearbeitungDB();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		// true - Passwortänderung hat geklappt
		// false - Passwortänderung hat nicht geklappt (falsches Passwort, Anforderungen
		// passen nicht etc.)
	
		String sql = "SELECT passwort FROM BENUTZER " + "where benutzer_id = " + WillkommenBean.benutzer_id;
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		if (dbRes.next()) {
			this.passwort = dbRes.getString("passwort");
		}

		if (this.passwort.equals(this.altesPasswort) && this.neuesPasswort.equals(this.bestaetigtesPasswort)) {
			dbConn.close();
			return true;
		} else {
			dbConn.close();
			return false;
		}
	}

//Änderungen
	public void bearbeitenPasswort() throws SQLException {
		String sql1 = "UPDATE BENUTZER SET PASSWORT = ? where benutzer_id =" + WillkommenBean.benutzer_id;
	
		Connection dbConn = new PostgreSQLAccess().getConnection();
		PreparedStatement prep = dbConn.prepareStatement(sql1);
		prep.setString(1, this.neuesPasswort);
		prep.executeUpdate();
		dbConn.close();
	}

//ID aus EMail abrufen
	public void IDAbrufen() throws SQLException { // Anmeldung mit Email
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT benutzer_id FROM BENUTZER " + "where email = '" + this.email + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		dbRes.next();
		WillkommenBean.benutzer_id = dbRes.getInt("benutzer_id");


		dbConn.close();
	}

//Getter Setter

	public static int getBenutzer_id() {
		return benutzer_id;
	}

	public static void setBenutzer_id(int benutzer_id) {
		WillkommenBean.benutzer_id = benutzer_id;
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

	public String getGeschlecht() {
		return geschlecht;
	}

	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getGeburtstag() {
		return geburtstag;
	}

	public void setGeburtstag(String geburtstag) {
		this.geburtstag = geburtstag;
	}

	public String getWohnort() {
		return wohnort;
	}

	public void setWohnort(String wohnort) {
		this.wohnort = wohnort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getAltesPasswort() {
		return altesPasswort;
	}

	public void setAltesPasswort(String altesPasswort) {
		this.altesPasswort = altesPasswort;
	}

	public String getNeuesPasswort() {
		return neuesPasswort;
	}

	public void setNeuesPasswort(String neuesPasswort) {
		this.neuesPasswort = neuesPasswort;
	}

	public String getBestaetigtesPasswort() {
		return bestaetigtesPasswort;
	}

	public void setBestaetigtesPasswort(String bestaetigtesPasswort) {
		this.bestaetigtesPasswort = bestaetigtesPasswort;
	}

	public boolean isIstEingeloggt() {
		return istEingeloggt;
	}

	public void setIstEingeloggt(boolean istEingeloggt) {
		this.istEingeloggt = istEingeloggt;
	}

	public String getGeburtsTag() {
		return geburtsTag;
	}

	public void setGeburtsTag(String geburtsTag) {
		this.geburtsTag = geburtsTag;
	}

	public String getGeburtsMonat() {
		return geburtsMonat;
	}

	public void setGeburtsMonat(String geburtsMonat) {
		this.geburtsMonat = geburtsMonat;
	}

	public String getGeburtsJahr() {
		return geburtsJahr;
	}

	public void setGeburtsJahr(String geburtsJahr) {
		this.geburtsJahr = geburtsJahr;
	}

	public String getLogout() {
		return logout;
	}

	public void setLogout(String logout) {
		this.logout = logout;
	}

}
