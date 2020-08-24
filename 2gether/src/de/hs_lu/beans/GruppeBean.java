package de.hs_lu.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sound.midi.SysexMessage;

import de.hs_lu.jdbc.NoConnectionException;
import de.hs_lu.jdbc.PostgreSQLAccess;

public class GruppeBean {
	int benutzer_id;
	String gruppe_name;
	String beschreibung;
	String bild;
	int ersteller_id;
	ArrayList<Gruppe> Gruppen;
	ArrayList<Mitglied> Mitglieder;
	int gruppe_id;
	boolean ersteller, mitglied;
	String view;

// Variablen für Interessen:
	ArrayList<Interesse> alleInteressen; // Hier werden alle Interessen aus Tabelle "Interesse" aus DB gespeichert zur
											// Anzeige in ProfilBearbeitenView
	String[] interessenFuerDB; // Dieses String Array ist für die Zwischenspeicherung der ausgewählten
								// Interessen beim Profil bearbeiten zur Speicherung in die DB
	ArrayList<Interesse> interessenAusDB; // Hier werden die Interessen des Benutzers aus der DB gespeichert zur
											// Darstellung in ProfilView in HTML

	public GruppeBean() {
		this.benutzer_id = WillkommenBean.getBenutzer_id();
		this.gruppe_name = "";
		this.beschreibung = "";
		this.bild = "";
		this.ersteller_id = 0;
		this.Gruppen = new ArrayList<>();
		this.Mitglieder = new ArrayList<>();
		this.gruppe_id = 0;
		this.ersteller = false;
		this.mitglied = false;
		this.view = "";
		// Variablen für Interessen:
		this.alleInteressen = new ArrayList<>();
		this.interessenFuerDB = new String[alleInteressen.size()];
		this.interessenAusDB = new ArrayList<>();
	}

	// 3Menu als Link
	public String getGruppe3() throws SQLException {
		this.getGruppedaten();
		this.ueberpruefenStatusInGruppe();
		String html = "<table class='regCSS'>";
		html += " <tr><td><a href='../gruppe/GruppeAppl.jsp?profil=Profil&gruppe_id=" + this.gruppe_id
				+ "' class=\"w3-button w3-theme-l4 buttonwidth\">Profil</a></td></tr>";
		html += " <tr><td><a href='../gruppe/GruppeAppl.jsp?news=News&gruppe_id_nachricht=" + this.gruppe_id
				+ "'class=\"w3-button w3-theme-l4 buttonwidth\">News</a></td></tr>";
		html += " <tr><td><a href='../gruppe/GruppeAppl.jsp?mitglieder=Mitglieder&gruppe_id=" + this.gruppe_id
				+ "'class=\"w3-button w3-theme-l4 buttonwidth\">Mitglieder</a></td></tr>";

		// Button für Ersteller / Mitglied / kein Mitglied
		if (this.ersteller) { // ersteller?
			html += "<tr><td><input type='submit' class=\"w3-button  w3-theme-l4\" name='bearbeiten' value='Gruppe bearbeiten' /></td></tr>";
			// als Link html += "<a
			// href=\'=\'./VeranstaltungAppl.jsp?geheZu=VeranstaltungErstellView.jsp&veranstaltung_id='"
			// + this.veranstaltung_id + "'> Veranstaltung bearbeiten</a> <br><br><br>";
		} else if (this.mitglied) { // Mitglied?
			html += "<tr><td><input type='submit' class=\"w3-button  w3-theme-l4\" name='verlassen' value='Gruppe verlassen' /></td></tr>";
		} else { // kein Mitglied
			html += "<tr><td><input type='submit' class=\"w3-button  w3-theme-l4\" name='beitreten' value='Gruppe beitreten' /></td></tr>";
		}
		html += "</table>";
		return html;
	}

//Interessen
	public String getHTMLInteressenVonGruppe() throws SQLException {
		liesInteressenAusDB();
		String html = "";
		if (!this.interessenAusDB.isEmpty()) {
			html += "<table><tr>";
			for (int j = 0; j < interessenAusDB.size(); j++) {
				Interesse interesse = interessenAusDB.get(j);
				html += "<td class='w3-theme-l4 w3-center'>" + interesse.getArt() + "</td>";
				if ((j + 1) % 3 == 0) {
					html += "</tr><tr>";
				}
			}
			html += "</tr></table>";
		}
		return html;
	}

//	Alle generellen Interessen aus DB für HTML für GruppeErstellView auslesen
//	 Diese Methode ist nur für die eigene Gruppe zur Profilbearbeitung
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

//	Interessen der Gruppe aus DB holen zum Anzeigen in HTML
	public void liesInteressenAusDB() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.interessenAusDB.clear();

		String sql = "SELECT INTERESSE FROM GRUPPE_INTERESSE WHERE GRUPPE_ID = '" + this.gruppe_id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		while (dbRes.next()) {
			Interesse neuesInteresse = new Interesse();
			neuesInteresse.setArt(dbRes.getString("interesse"));
			this.interessenAusDB.add(neuesInteresse);
		}
		// Hier werden noch die Oberbegriffe aus der Tabelle Interesse zur Art des
		// Interesse zugeordnet
		this.oberbegriffeZuInteressen();
		dbConn.close();
	}

	public void schreibeAusgewaehlteInteressenVonGruppeInDB() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sqlDELETE = "DELETE FROM GRUPPE_INTERESSE WHERE GRUPPE_ID = '" + this.gruppe_id + "'";
		Statement stmt = dbConn.createStatement();
		stmt.executeUpdate(sqlDELETE);
		for (int i = 0; i < interessenFuerDB.length; i++) {
			String sql = "INSERT INTO GRUPPE_INTERESSE (GRUPPE_ID, INTERESSE)" + "values (?,?)";
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setInt(1, this.gruppe_id);
			prep.setString(2, interessenFuerDB[i]);
			prep.executeUpdate();
		}
		dbConn.close();
	}

	// Oberbegriffe zu interessen holen
	public void oberbegriffeZuInteressen() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		for (int i = 0; i < interessenAusDB.size(); i++) {
			Interesse interesse = interessenAusDB.get(i);
			String sql = "SELECT OBERBEGRIFF FROM INTERESSE WHERE ART = '" + interesse.getArt() + "'";
			ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

			while (dbRes.next()) {
				interesse.setOberbegriff(dbRes.getString("oberbegriff"));
			}
		}
		dbConn.close();
	}

	// Abruf aller Oberbegriffe von Tabelle Interesse
	// Wird benötigt um die ausgewählten Interessen in HTML unter den richtigen
	// Oberbegriff zu schreiben
	public void alleOberbegriffeVonInteresse(String[] oberbegriffe) throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT DISTINCT OBERBEGRIFF FROM INTERESSE ORDER BY OBERBEGRIFF";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		int z = 0;
		while (dbRes.next()) {
			oberbegriffe[z] = dbRes.getString("oberbegriff");
			z++;
		}
		dbConn.close();
	}
	
	
	//Darstellung aller Interessen ohne Vorauswahl für ErstellView
	public String getHTMLErstellInteressen() throws SQLException {
		String[] oberbegriffe = new String[20];
		this.alleOberbegriffeVonInteresse(oberbegriffe);

		String html = "<table><tr>";

		for (int x = 0; x < oberbegriffe.length && oberbegriffe[x] != null; x++) {

			html += "<td valign=top><b>" + oberbegriffe[x] + ":<br/></b>";

			
				for (int i = 0; i < this.alleInteressen.size(); i++) {
					Interesse interesseAlle = this.alleInteressen.get(i);
					if (interesseAlle.getOberbegriff().equals(oberbegriffe[x])) { // Abgleich damit das Interesse beim
																					// richtigen Oberbegriff auftaucht

						html += "<label><input type='checkbox' name='interesse' value='" + interesseAlle.getArt() + "'/> "
								+ interesseAlle.getArt() + "</label><br/>";
					
						}
					}
				
	

			html += "</td>";

		}
		html += "</tr></table>";

		return html;
	}

	
	

//Darstellung aller Interessen in HTML mit Vorauswahl, welche gerade in DB sind
	public String getHTMLAllerInteressen() throws SQLException {
		String[] oberbegriffe = new String[20];
		this.alleOberbegriffeVonInteresse(oberbegriffe);

		String html = "<table><tr>";

		for (int x = 0; x < oberbegriffe.length && oberbegriffe[x] != null; x++) {

			html += "<td valign=top><b>" + oberbegriffe[x] + ":<br/></b>";

			// Schleife über interessen und AlleInteressen.
			// Wenn Treffer, dann soll es mit checked dargestellt werden und das Interesse
			// aus AlleInteressen gelöscht werden

			for (int j = 0; j < interessenAusDB.size(); j++) {
				Interesse interesse = interessenAusDB.get(j);
				for (int i = 0; i < this.alleInteressen.size(); i++) {
					Interesse interesseAlle = this.alleInteressen.get(i);
					if (interesseAlle.getOberbegriff().equals(oberbegriffe[x])) { // Abgleich damit das Interesse beim
																					// richtigen Oberbegriff auftaucht

						if (interesseAlle.getArt().equals(interesse.getArt())) { // Abgleich ob gleiches Interesse
																					// in Alle und Meine Interessen

							html += "<label><input type='checkbox' name='interesse' value='" + interesse.getArt()
									+ "' checked/> " + interesse.getArt() + "</label><br/>";
							this.alleInteressen.remove(interesseAlle);
						}
					}
				}
			}

			// Schleife über alleInteressen und Ausgabe von Checkboxen ohne checked
			for (int i = 0; i < this.alleInteressen.size(); i++) {
				Interesse interesseAlle = this.alleInteressen.get(i);

				if (interesseAlle.getOberbegriff().equals(oberbegriffe[x])) { // Abgleich damit das Interesse beim
																				// richtigen Oberbegriff auftaucht

					html += "<label><input type='checkbox' name='interesse' value='" + interesseAlle.getArt() + "'/> "
							+ interesseAlle.getArt() + "</label><br/>";
				}
			}

			html += "</td>";

		}
		html += "</tr></table>";

		return html;
	}

// MitgliederView
	public String getMitgliederView() throws SQLException {
		String html = "";
		ueberpruefenStatusInGruppe();
		liesMitgliederAusGruppe();
		html += "<h4>Mitglieder der Gruppe</h4>";

		for (Mitglied mitglied : Mitglieder) {

			this.richtigesBild(mitglied.getMitglied_id());

			if (mitglied.getMitglied_id() == this.benutzer_id) {
				html += "<a href='../profil/ProfilAppl.jsp?geheZu=ProfilView.jsp' class=\"w3-button\"> <img src ='../img/"
						+ this.bild + "' alt='" + mitglied.getNachname() + "'" + "title='" + mitglied.getNachname()
						+ "' width='50' height='50' />" + mitglied.getVorname() + " " + mitglied.getNachname()
						+ " </a><br>";
			} else {
				html += "<a href='../profil/ProfilAppl.jsp?geheZu=FreundeProfilView.jsp&ausgewaehlteFreund_id="
						+ mitglied.getMitglied_id() + "'class=\"w3-button\"> <img src ='../img/" + this.bild + "' alt='"
						+ mitglied.getNachname() + "'" + "title='" + mitglied.getNachname()
						+ "' width='50' height='50' />" + mitglied.getVorname() + " " + mitglied.getNachname()
						+ " </a><br>";

			}
		}
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

// Mitglieder auslesen aus DB
	public void liesMitgliederAusGruppe() throws SQLException {
//		System.err.println("Mitglieder auslesen Methode start");
		int[] benutzer_id = new int[10];
		this.Mitglieder.clear();

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT benutzer_id from Benutzer_in_Gruppe where gruppe_id = '" + this.gruppe_id + "'";
//		System.err.println("benutzer_id: " + this.benutzer_id + " gruppe_id: " + this.gruppe_id);
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		int i = 0;
		while (dbRes.next()) {
			benutzer_id[i] = dbRes.getInt("benutzer_id");
//			System.err.println("benutzer_id[i]: " + benutzer_id[i]);
			i++;
		}

		for (int j = 0; j < benutzer_id.length; j++) {
			String sql1 = "SELECT benutzer_id, vorname, nachname FROM benutzer WHERE benutzer_id = " + benutzer_id[j];
			ResultSet dbRes2 = dbConn.createStatement().executeQuery(sql1);
//			System.err.println("benutzer_id[j] dbRes2: " + benutzer_id[j]);
			while (dbRes2.next()) {
				Mitglied neuesMitglied = new Mitglied(dbRes2.getInt("benutzer_id"), dbRes2.getString("vorname"),
						dbRes2.getString("nachname"), this.gruppe_id);
//				System.err.println("benutzer_id dbRes2.getInt" + dbRes2.getInt("benutzer_id"));
				this.Mitglieder.add(neuesMitglied);
//				System.err.println("Gruppenmitglieder wurden für Gruppen_id " + this.gruppe_id + " ausgelesen");
			}
		}
		dbConn.close();
	}

//	Gruppe entfernen
	public void gruppeEntfernen() throws SQLException {
		if (this.ersteller) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "DELETE FROM gruppe WHERE gruppe_id = '" + this.gruppe_id + "'";
			dbConn.prepareStatement(sql).executeUpdate();
			System.err.println("Gruppe_id " + this.gruppe_id + " wurde entfernt");
			dbConn.close();
		}
	}

//ErstellView Buttons
	public String getButtonsErstellen() throws SQLException {
		String html = "";
		if (this.view.equals("bearbeiten")) {
			html += "<table>";
			html += "<tr><td><input class='w3-button  w3-theme-l4' type='submit' name='loeschen' value='Gruppe löschen' /></td></tr>";
			html += "<tr><td><input class='w3-button  w3-theme-l4' type='submit' name='speichern' value='Gruppe speichern'/></td></tr>";
			html += "</table>";
		} else if (this.view.equals("erstellen")) {
			html += "<input class='w3-button  w3-theme-l4' type='submit' name='erstellen' value='Gruppe erstellen'/>";
		}
		return html;
	}

//  GruppeErstellView
	public String getGruppeErstellView() throws SQLException {
		this.alleInteressen.clear();
		String html = "";
		if (this.view.equals("bearbeiten")) {
			liesAlleInteressenAusDB();
			html += "<table>";
			html += "<tr><td>Name: </td><td> <input type='text' name='gruppe_name' value='" + this.gruppe_name
					+ "'></td></tr>";
			html += "<tr><td>Beschreibung: </td><td><input type='text' name='beschreibung' value='" + this.beschreibung
					+ "'></td></tr>";
			html += "<tr><td>Interessen:  </td><td colspan='2'>" + this.getHTMLAllerInteressen() + "</td></tr>";
			html += "</table>";
		} else if (this.view.equals("erstellen")) {
			liesAlleInteressenAusDB();
			html += "<table>";
			html += "<tr><td>Name: </td><td><input type='text' name='gruppe_name' value=''> </td></tr>";
			html += "<tr><td>Beschreibung: </td><td><input type='text' name='beschreibung' value=''> </td></tr>";
			html += "<tr><td>Interessen:  </td><td>" + this.getHTMLErstellInteressen() + "</td></tr>";
			html += "</table>";
		}
		return html;
	}

//	GruppeProfilView
	public String getGruppeProfilView() throws SQLException {
		getGruppedaten();
//		System.err.println("Vor überprüfe Status");
		ueberpruefenStatusInGruppe();
//		System.err.println("Nach überprüfe Status");
//		System.err.println("GRUPPENNAME: " + this.gruppe_name);
		String html = "";
		html += "<table class='regCSS'><tr><td width='20%'>Name</td><td>" + this.gruppe_name + "</td>"
				+ "<td rowspan='2' class=\"bild\"><img src=\"../img/gruppe.jpg\"	alt=\"gruppe.jpg\""
				+ "width=\"120px\" height=\"120px\"></td></tr>";
		html += "<tr width='20%'><td>Beschreibung</td><td>" + this.beschreibung + "</td></tr>";
		html += "<tr width='20%'><td>Interessen</td><td colspan='2'>" + this.getHTMLInteressenVonGruppe()
				+ "</td></tr><tr></tr></table>";
		return html;
	}

//ueberprüfen Status in Gruppe (Ersteller/Mitglied/kein Mitglied)
	public void ueberpruefenStatusInGruppe() throws SQLException {
		this.ersteller = false;
		this.mitglied = false;

		if (this.benutzer_id == this.ersteller_id) { // Ersteller?
			this.ersteller = true;
//			System.err.println("Benutzer ist Ersteller");
		} else { // Mitglied?
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "SELECT gruppe_id, benutzer_id from Benutzer_in_Gruppe where benutzer_id = '"
					+ this.benutzer_id + "' AND gruppe_id ='" + this.gruppe_id + "'";
			ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

			while (dbRes.next()) {
//				System.err.println("Benutzer ist Mitglied");
				this.mitglied = true;
			}
			dbConn.close();
		}
	}

//Gruppe bearbeiten
	public void gruppeBearbeiten() throws SQLException {
//		 Daten aus der View in die DB schreiben
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "UPDATE Gruppe SET gruppe_name = ?, beschreibung = ? WHERE gruppe_id = '" + this.gruppe_id + "'";
//		System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setString(1, this.gruppe_name);
		prep.setString(2, this.beschreibung);
		prep.executeUpdate();
		this.schreibeAusgewaehlteInteressenVonGruppeInDB();
//		System.out.println("Gruppe " + this.gruppe_name + " wurde bearbeitet.");

		dbConn.close();
	}

//Gruppe erstellen
	public void gruppeErstellen() throws SQLException {
//		 Daten aus der View in die DB schreiben
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "INSERT INTO Gruppe " + "(gruppe_name, beschreibung, ersteller_id) " + "values (?,?,?)";
		// System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setString(1, this.gruppe_name);
		prep.setString(2, this.beschreibung);
		prep.setInt(3, this.benutzer_id);
		prep.executeUpdate();
		this.liesGruppen_idAusGruppen_name();
		schreibeAusgewaehlteInteressenVonGruppeInDB();
		this.gruppeBeitreten();
		System.out.println("Gruppe " + this.gruppe_name + " wurde angelegt. Ersteller: " + this.benutzer_id);

		dbConn.close();
	}

//Gruppen_id aus Gruppen_name lesen
	public void liesGruppen_idAusGruppen_name() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT gruppe_id FROM Gruppe WHERE gruppe_name = '" + this.gruppe_name + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		if (dbRes.next()) {
			this.gruppe_id = dbRes.getInt("gruppe_id");
//			System.err.println("Gruppen_id der angelegten Gruppe lautet: " + this.gruppe_id);
		}

		dbConn.close();
	}

//Gruppenname überprüfen
	public boolean ueberpruefeGruppeExistiert() throws SQLException {
//		 true existiert -> anderer Name wählen, false existiert nicht -> ok
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT gruppe_name FROM gruppe WHERE gruppe_name = '" + this.gruppe_name + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while (dbRes.next()) {
			System.err.println("DB: " + dbRes.getString("gruppe_name") + " / Eingabe: " + this.gruppe_name);
			System.err.println("dbRes: " + dbRes.next());
			dbConn.close();
			return true;
		}
		dbConn.close();
		return false;

	}

//GruppeVerlassen
	public void gruppeVerlassen() throws SQLException {
		if (this.mitglied) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "DELETE FROM benutzer_in_gruppe WHERE benutzer_id ='" + this.benutzer_id + "' AND gruppe_id ='"
					+ this.gruppe_id + "'";
			dbConn.prepareStatement(sql).executeUpdate();

			System.err.println("Benutzer_id " + this.benutzer_id + " hat Gruppe_id " + this.gruppe_id + " verlassen.");
			dbConn.close();
		}
	}

//GruppeBeitreten
	public void gruppeBeitreten() throws SQLException {
//		benutzer_id und Gruppen_id in Tabelle eintragen
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "INSERT INTO benutzer_in_gruppe (gruppe_id, benutzer_id) " + "values (?,?)";
//		System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setInt(1, this.gruppe_id);
		prep.setInt(2, this.benutzer_id);
		prep.executeUpdate();
//		System.err.println("Benutzer " + this.benutzer_id + " ist der Gruppe_id " + this.gruppe_id + " beigetreten");

		dbConn.close();
	}

//GruppenView
	public String getAlleGruppenView() throws SQLException {
		liesAlleGruppenAusDB();
		String html = "";
		html += "<h5>Alle Gruppen</h5>";
		for (Gruppe gruppe : Gruppen) {

			html += "<a href='../gruppe/GruppeAppl.jsp?geheZu=GruppeProfilView.jsp&gruppe_id=" + gruppe.getGruppe_id()
					+ "' class=\"w3-button\"> <img src ='../img/gruppe.jpg' alt='" + gruppe.getGruppe_name()
					+ "' title=' " + gruppe.getGruppe_name() + "' width='50' height='50' />" + gruppe.getGruppe_name()
					+ "</a>" + "<br>";

		}
		return html;
	}

//GruppenView eine Spalte
	public String getEigeneGruppenViewRechts() throws SQLException {
		String html = "";
		html += "<h4>Deine Gruppen</h4>";
		liesGruppenAusDBVonBenutzer();
		html += "<table width='100%'>";
		for (Gruppe gruppe : Gruppen) {
			html += "<tr><td><a href='../gruppe/GruppeAppl.jsp?geheZu=GruppeProfilView.jsp&gruppe_id="
					+ gruppe.getGruppe_id() + "' class=\"w3-button\"> <img src ='../img/gruppe.jpg' alt='"
					+ gruppe.getGruppe_name() + "' title=' " + gruppe.getGruppe_name() + "' width='50' height='50' />"
					+ gruppe.getGruppe_name() + "</a></td></tr>";
		}
		html += "</table>";
		return html;
	}

//GruppenView 2 Spalten
	public String getEigeneGruppenView() throws SQLException {
		String html = "";

		html += "<h4>Deine Gruppen</h4>";
		liesGruppenAusDBVonBenutzer();
		int i = 0;
		html += "<table width='100%'><tr>";
		for (Gruppe gruppe : Gruppen) {
			if (i % 2 == 0) {
				html += "</tr><tr>";
			}
			i++;
			html += "<td><a href='../gruppe/GruppeAppl.jsp?geheZu=GruppeProfilView.jsp&gruppe_id="
					+ gruppe.getGruppe_id() + "' class=\"w3-button\"> <img src ='../img/gruppe.jpg' alt='"
					+ gruppe.getGruppe_name() + "' title=' " + gruppe.getGruppe_name() + "' width='50' height='50' />"
					+ gruppe.getGruppe_name() + "</a></td>";
		}
		html += "</tr></table>";
		return html;
	}

	private void liesAlleGruppenAusDB() throws SQLException {
		this.Gruppen.clear();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT gruppe_id, gruppe_name, beschreibung, ersteller_id  FROM Gruppe";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		while (dbRes.next()) {
			Gruppe neueGruppe = new Gruppe(dbRes.getInt("gruppe_id"), dbRes.getString("gruppe_name"),
					dbRes.getString("beschreibung"), dbRes.getInt("ersteller_id"));
			this.Gruppen.add(neueGruppe);
//				System.out.println("Alle vorhandenen Gruppen wurden ausgelesen");
		}
		dbConn.close();

	}

	// Eigene Gruppen aus DB lesen
	public void liesGruppenAusDBVonBenutzer() throws SQLException { //
		this.benutzer_id = WillkommenBean.getBenutzer_id();
//		System.err.println(this.benutzer_id);
		int[] gruppe_id = new int[10];
		this.Gruppen.clear();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT gruppe_id from Benutzer_in_Gruppe where benutzer_id = '" + this.benutzer_id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		int i = 0;
		while (dbRes.next()) {
			int gruppeid = dbRes.getInt("gruppe_id");
			gruppe_id[i] = gruppeid;
			i++;
		}
		for (int j = 0; j < gruppe_id.length; j++) {
			String sql1 = "SELECT gruppe_id, gruppe_name, beschreibung, ersteller_id FROM gruppe WHERE gruppe_id = "
					+ gruppe_id[j];
			ResultSet dbRes2 = dbConn.createStatement().executeQuery(sql1);
			while (dbRes2.next()) {
				Gruppe neueGruppe = new Gruppe(dbRes2.getInt("gruppe_id"), dbRes2.getString("gruppe_name"),
						dbRes2.getString("beschreibung"), dbRes2.getInt("ersteller_id"));
				this.Gruppen.add(neueGruppe);
//				System.out.println("Eigene Gruppen wurden für Benutzer_id " + this.benutzer_id + " ausgelesen");

			}
		}
		dbConn.close();
	}

//Darstellung der eigenen Gruppen in HTML
	public String getHTMLAllerGruppenVonBenutzer() {

		String html = "";

		for (Gruppe gruppe : Gruppen) {

			html += "<a href=\'=\'../gruppe/GruppeAppl.jsp?geheZu=GruppeProfilView.jsp&gruppe_id="
					+ gruppe.getGruppe_id() + "\'>" + gruppe.getGruppe_name() + "</a>";
		}
		return html;
	}

//Gruppendaten aus DB auslesen
	public void getGruppedaten() throws SQLException { //

		Connection dbConn = new PostgreSQLAccess().getConnection();
		System.err.println("getGruppedaten(): gid: " + this.gruppe_id);
		String sql = "SELECT gruppe_id, gruppe_name, beschreibung, ersteller_id FROM Gruppe WHERE gruppe_id = '"
				+ this.gruppe_id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		if (dbRes.next()) {
			this.gruppe_id = dbRes.getInt("gruppe_id");
			System.err.println("getGruppedaten(): dbResgid: " + dbRes.getInt("gruppe_id"));
			this.gruppe_name = dbRes.getString("gruppe_name");
			System.err.println("getGruppedaten(): dbResgname: " + dbRes.getString("gruppe_name"));
			this.beschreibung = dbRes.getString("beschreibung");
			System.err.println("getGruppedaten(): dbResbeschr: " + dbRes.getString("beschreibung"));
			this.ersteller_id = dbRes.getInt("ersteller_id");
			System.err.println("getGruppedaten(): dbReserstid: " + dbRes.getInt("ersteller_id"));
			System.err.println("Daten von Gruppe mit ID " + this.gruppe_id + " ausgelesen");
		}
		dbConn.close();
	}

	public int getBenutzer_id() {
		return benutzer_id;
	}

	public void setBenutzer_id(int benutzer_id) {
		this.benutzer_id = benutzer_id;
	}

	public String getGruppe_name() {
		return gruppe_name;
	}

	public void setGruppe_name(String gruppe_name) {
		this.gruppe_name = gruppe_name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public int getErsteller_id() {
		return ersteller_id;
	}

	public void setErsteller_id(int ersteller_id) {
		this.ersteller_id = ersteller_id;
	}

	public ArrayList<Gruppe> getGruppen() {
		return Gruppen;
	}

	public void setGruppen(ArrayList<Gruppe> gruppen) {
		Gruppen = gruppen;
	}

	public int getGruppe_id() {
		return gruppe_id;
	}

	public void setGruppe_id(String gruppe_id) {
		this.gruppe_id = Integer.parseInt(gruppe_id);
	}

	public boolean isErsteller() {
		return ersteller;
	}

	public void setErsteller(boolean ersteller) {
		this.ersteller = ersteller;
	}

	public boolean isMitglied() {
		return mitglied;
	}

	public void setMitglied(boolean mitglied) {
		this.mitglied = mitglied;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public void setGruppe_id(int gruppe_id) {
		this.gruppe_id = gruppe_id;
	}

	public ArrayList<Mitglied> getMitglieder() {
		return Mitglieder;
	}

	public void setMitglieder(ArrayList<Mitglied> mitglieder) {
		Mitglieder = mitglieder;
	}

	public ArrayList<Interesse> getAlleInteressen() {
		return alleInteressen;
	}

	public void setAlleInteressen(ArrayList<Interesse> alleInteressen) {
		this.alleInteressen = alleInteressen;
	}

	public String[] getInteressenFuerDB() {
		return interessenFuerDB;
	}

	public void setInteressenFuerDB(String[] interessenFuerDB) {
		this.interessenFuerDB = interessenFuerDB;
	}

	public ArrayList<Interesse> getInteressenAusDB() {
		return interessenAusDB;
	}

	public void setInteressenAusDB(ArrayList<Interesse> interessenAusDB) {
		this.interessenAusDB = interessenAusDB;
	}

}