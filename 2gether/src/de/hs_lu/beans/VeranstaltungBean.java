package de.hs_lu.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hs_lu.jdbc.PostgreSQLAccess;

public class VeranstaltungBean {

	int benutzer_id;
	int veranstaltung_id;
	String veranstaltung_name;
	String beschreibung;
	String ort;
	String datum;
	String beginn;
	String ende;
	String bild;
	int ersteller_id;
	boolean ersteller, mitglied;
	ArrayList<Veranstaltung> Veranstaltungen;
	ArrayList<Mitglied> Mitglieder;
	String view;
	// Variablen für Interessen:
	ArrayList<Interesse> alleInteressen; // Hier werden alle Interessen aus Tabelle "Interesse" aus DB gespeichert zur
											// Anzeige in ProfilBearbeitenView
	String[] interessenFuerDB; // Dieses String Array ist für die Zwischenspeicherung der ausgewählten
								// Interessen beim Profil bearbeiten zur Speicherung in die DB
	ArrayList<Interesse> interessenAusDB; // Hier werden die Interessen des Benutzers aus der DB gespeichert zur
											// Darstellung in ProfilView in HTML

	public VeranstaltungBean() throws SQLException {
		this.benutzer_id = WillkommenBean.getBenutzer_id();
		this.veranstaltung_id = 0;
		this.veranstaltung_name = "";
		this.beschreibung = "";
		this.ort = "";
		this.beginn = "";
		this.ende = "";
		this.bild = "";
		this.ersteller_id = 0;
		this.Veranstaltungen = new ArrayList<>();
		this.Mitglieder = new ArrayList<>();
		// Variablen für Interessen:
		this.alleInteressen = new ArrayList<>();
		this.interessenFuerDB = new String[alleInteressen.size()];
		this.interessenAusDB = new ArrayList<>();
		this.ersteller = false;
		this.mitglied = false;
		this.view = "";
	}

//ErstellView Buttons
	public String getButtonsErstellen() throws SQLException {
		String html = "";
		if (this.view.equals("bearbeiten")) {
			html += "<table>";
			html += "<tr><td><input class='w3-button  w3-theme-l4' type='submit' name='loeschen' value='Veranstaltung löschen' /></td></tr>";
			html += "<tr><td><input class='w3-button  w3-theme-l4' type='submit' name='speichern' value='Veranstaltung speichern' onclick='setVeranstaltungSpeichernGeklickt()'/></td></tr>";
			html += "</table>";
		} else if (this.view.equals("erstellen")) {
			html += "<input class='w3-button  w3-theme-l4' type='submit' name='erstellen' value='Veranstaltung erstellen' onclick='setVeranstaltungErstellenGeklickt()'/>";
		}
		return html;
	}

//3Menu als Link
	public String getGruppe3() throws SQLException {
		this.getVeranstaltungdaten();
		this.ueberpruefenStatusInVeranstaltung();
		String html = "<table class ='regCSS'>";
		html += " <tr><td><a href='../veranstaltung/VeranstaltungAppl.jsp?profil=Profil&veranstaltung_id="
				+ this.veranstaltung_id + "'class=\"w3-button w3-theme-l4 buttonwidth\">Profil</a></td></tr>";
		html += " <tr><td><a href='../veranstaltung/VeranstaltungAppl.jsp?news=News&veranstaltung_id_nachricht="
				+ this.veranstaltung_id + "'class=\"w3-button w3-theme-l4 buttonwidth\">News</a></td></tr>";
		html += " <tr><td><a href='../veranstaltung/VeranstaltungAppl.jsp?mitglieder=Mitglieder&veranstaltung_id="
				+ this.veranstaltung_id + "'class=\"w3-button w3-theme-l4 buttonwidth\">Mitglieder</a></td></tr>";
// Button für Ersteller / Mitglied / kein Mitglied
		if (this.ersteller) { // ersteller?
			html += "<tr><td><input type='submit' class=\"w3-button  w3-theme-l4\" name='bearbeiten' value='Veranstaltung bearbeiten' /></td></tr>";
//als Link	html += "<a href=\'=\'./VeranstaltungAppl.jsp?geheZu=VeranstaltungErstellView.jsp&veranstaltung_id='" + this.veranstaltung_id + "'> Veranstaltung bearbeiten</a> <br><br><br>";
		} else if (this.mitglied) { // Mitglied?
			html += "<tr><td><input type='submit' class=\"w3-button  w3-theme-l4\" name='verlassen' value='Veranstaltung verlassen' /></td></tr>";
		} else { // kein Mitglied
			html += "<tr><td><input type='submit' class=\"w3-button  w3-theme-l4\" name='beitreten' value='Veranstaltung beitreten' /></td></tr>";
		}
		html += "</table>";
		return html;
	}

	public String getHTMLInteressenVonVeranstaltung() throws SQLException {
		this.liesInteressenAusDB();
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

//	Alle generellen Interessen aus DB für HTML für VeranstaltungErstellView auslesen
//	 Diese Methode ist nur für die eigene Veranstaltung zur Profilbearbeitung
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


	
//	Interessen der Veranstaltung aus DB holen zum Anzeigen in HTML
	public void liesInteressenAusDB() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		this.interessenAusDB.clear();

		String sql = "SELECT INTERESSE FROM VERANSTALTUNG_INTERESSE WHERE VERANSTALTUNG_ID = '" + this.veranstaltung_id
				+ "'";
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

	public void schreibeAusgewaehlteInteressenVonVeranstaltungInDB() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sqlDELETE = "DELETE FROM VERANSTALTUNG_INTERESSE WHERE VERANSTALTUNG_ID = '" + this.veranstaltung_id
				+ "'";
		Statement stmt = dbConn.createStatement();
		stmt.executeUpdate(sqlDELETE);
		for (int i = 0; i < interessenFuerDB.length; i++) {
			String sql = "INSERT INTO VERANSTALTUNG_INTERESSE (VERANSTALTUNG_ID, INTERESSE)" + "values (?,?)";
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setInt(1, this.veranstaltung_id);
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
	
	//Darstellung aller Interessen in HTML ohne Vorauswahl für ErstellView
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
				Interesse meinInteresse = interessenAusDB.get(j);
				for (int i = 0; i < this.alleInteressen.size(); i++) {
					Interesse interesseAlle = this.alleInteressen.get(i);
					if (interesseAlle.getOberbegriff().equals(oberbegriffe[x])) { // Abgleich damit das Interesse beim
																					// richtigen Oberbegriff auftaucht

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
		liesMitgliederAusVeranstaltung();
		html += "<h4>Mitglieder der Veranstaltung</h4>";

		for (Mitglied mitglied : Mitglieder) {

			this.richtigesBild(mitglied.getMitglied_id());

			if (mitglied.getMitglied_id() == this.benutzer_id) {
				html += "<a href='../profil/ProfilAppl.jsp?geheZu=ProfilView.jsp' class=\"w3-button\"> <img src ='../img/"
						+ this.bild + "' alt='" + mitglied.getNachname() + "'" + "title='" + mitglied.getNachname()
						+ "' width='50' height='50' /> " + mitglied.getVorname() + " " + mitglied.getNachname()
						+ " </a><br>";
			} else {
				html += "<a href='../profil/ProfilAppl.jsp?geheZu=FreundeProfilView.jsp&ausgewaehlteFreund_id="
						+ mitglied.getMitglied_id() + "'class=\"w3-button\"> <img src ='../img/" + this.bild + "' alt='"
						+ mitglied.getNachname() + "'" + "title='" + mitglied.getNachname()
						+ "' width='50' height='50' /> " + mitglied.getVorname() + " " + mitglied.getNachname()
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
	public void liesMitgliederAusVeranstaltung() throws SQLException {
//		System.err.println("Mitglieder auslesen Methode start");
		int[] benutzer_id = new int[10];
		this.Mitglieder.clear();

		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT benutzer_id from benutzer_besucht_veranstaltung where veranstaltung_id = '"
				+ this.veranstaltung_id + "'";
//		System.err.println("benutzer_id: " + this.benutzer_id + " veranstaltung_id: " + this.veranstaltung_id);
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
						dbRes2.getString("nachname"), this.veranstaltung_id);
//				System.err.println("benutzer_id dbRes2.getInt" + dbRes2.getInt("benutzer_id"));
				this.Mitglieder.add(neuesMitglied);
//				System.err.println("Veranstaltungenmitglieder wurden für Veranstaltungen_id " + this.veranstaltung_id + " ausgelesen");
			}
		}
		dbConn.close();
	}

//	Veranstaltung entfernen
	public void veranstaltungEntfernen() throws SQLException {
		if (this.ersteller) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "DELETE FROM veranstaltung WHERE veranstaltung_id = '" + this.veranstaltung_id + "'";
			dbConn.prepareStatement(sql).executeUpdate();
			System.err.println("Veranstaltung_id " + this.veranstaltung_id + " wurde entfernt");
			dbConn.close();
		}
	}

//  VeranstaltungErstellView
	public String getVeranstaltungErstellView() throws SQLException {
		liesAlleInteressenAusDB();
		String html = "";
		if (this.view.equals("bearbeiten")) {
			html += "<table>";
			html += "<tr><td>Name: </td>" + "<td> <input type='text' name='veranstaltung_name' value='"
					+ this.veranstaltung_name + "'></td>" + "<td class='regRight' id='nameErrFeld'></td></tr>";
			html += "<tr><td>Beschreibung: </td><td><input type='text' name='beschreibung' value='" + this.beschreibung
					+ "'></td></tr>";
			html += "<tr><td>Ort: </td><td><select name='ort'><option>" + this.ort + "</option>"
					+ "<option>Heidelberg</option>" + "<option>Hockenheim</option>" + "<option>Leimen</option>"
					+ "<option>Ludwigshafen</option>" + "<option>Mannheim</option>" + "<option>Schriesheim</option>"
					+ "<option>Schwetzingen</option>" + "<option>Sinsheim</option>" + "<option>Weinheim</option>"
					+ "<option>Wiesloch</option>" + "</select></td><td class='regRight' id='ortErrFeld'></td></tr>";
			html += "<tr><td>Datum: </td><td><input type='text' name='datum' value='" + this.datum + "'></td></tr>";
			html += "<tr><td>Beginn: </td><td><input type='text' name='beginn' value='" + this.beginn + "'></td></tr>";
			html += "<tr><td>Ende: </td><td><input type='text' name='ende' value='" + this.ende + "'></td></tr>";
			html += "<tr><td>Interessen:  </td><td colspan='2'>" + this.getHTMLAllerInteressen() + "</td></tr></table>";
		} else if (this.view.equals("erstellen")) {
			html += "<table>";
			html += "<tr><td>Name: </td><td><input type='text' name='veranstaltung_name' value=''> </td>"
					+ "<td class='regRight' id='nameErrFeld'></td></tr>";
			html += "<tr><td>Beschreibung: </td><td><input type='text' name='beschreibung' value=''> </td></tr>";
			html += "<tr><td>Ort: </td><td><select name='ort'><option>--Bitte auswählen--</option>"
					+ "<option>Heidelberg</option>" + "<option>Hockenheim</option>" + "<option>Leimen</option>"
					+ "<option>Ludwigshafen</option>" + "<option>Mannheim</option>" + "<option>Schriesheim</option>"
					+ "<option>Schwetzingen</option>" + "<option>Sinsheim</option>" + "<option>Weinheim</option>"
					+ "<option>Wiesloch</option>" + "</select></td>"
					+ "<td class='regRight' id='ortErrFeld'></td></tr>";
			html += "<tr><td>Datum: </td><td><input type='text' name='datum' value=''> </td></tr>";
			html += "<tr><td>Beginn: </td><td><input type='text' name='beginn' value=''> </td></tr>";
			html += "<tr><td>Ende: </td><td><input type='text' name='ende' value=''> </td></tr>";
			html += "<tr><td>Interessen:  </td><td colspan='2'>" + this.getHTMLErstellInteressen() + "</td></tr></table>";
		}
		return html;
	}

//	VeranstaltungProfilView
	public String getVeranstaltungProfilView() throws SQLException {
		getVeranstaltungdaten();
		ueberpruefenStatusInVeranstaltung();
//		System.err.println("VERANSTALTUNGNAME: " + this.veranstaltung_name);
		String html = "";
		html += "<table class='regCSS'><tr><td width='20%'>Name</td><td>" + this.veranstaltung_name + "</td>"
				+ "<td rowspan='6' class=\"bild\"><img src=\"../img/veranstaltung.jpg\"	alt=\"veranstaltung.jpg\""
				+ "width=\"200px\" height=\"150px\"></td></tr>";
		html += "<tr><td width='20%'>Beschreibung</td><td>" + this.beschreibung + "</td><td></td></tr>";
		html += "<tr><td width='20%'>Ort</td><td>" + this.ort + "</td><td></td></tr>";
		html += "<tr><td width='20%'>Datum</td><td>" + this.datum + "</td><td></td></tr>";
		html += "<tr><td width='20%'>Beginn</td><td>" + this.beginn + "</td><td></td></tr>";
		html += "<tr><td width='20%'>Ende</td><td>" + this.ende + "</td><td></td></tr>";
		html += "<tr><td width='20%'>Interessen</td><td colspan='2'>" + this.getHTMLInteressenVonVeranstaltung()
				+ "</td></tr><tr></tr></table>";
		return html;
	}

//ueberprüfen Status in Veranstaltung (Ersteller/Mitglied/kein Mitglied)
	public void ueberpruefenStatusInVeranstaltung() throws SQLException {
		this.ersteller = false;
		this.mitglied = false;
		if (this.benutzer_id == this.ersteller_id) { // Ersteller?
			this.ersteller = true;
//			System.err.println("Benutzer ist Ersteller");
		} else { // Mitglied?
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "SELECT veranstaltung_id, benutzer_id from benutzer_besucht_veranstaltung where benutzer_id = '"
					+ this.benutzer_id + "' AND veranstaltung_id ='" + this.veranstaltung_id + "'";
			ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
			while (dbRes.next()) {
//				System.err.println("Benutzer ist Mitglied");
				this.mitglied = true;
			}
			dbConn.close();
		}
	}

//Veranstaltung bearbeiten
	public void veranstaltungBearbeiten() throws SQLException {
//		 Daten aus der View in die DB schreiben
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "UPDATE Veranstaltung SET veranstaltung_name = ?, beschreibung = ?, ort= ?, datum = ?, beginn= ?, ende= ? WHERE veranstaltung_id = '"
				+ this.veranstaltung_id + "'";
//		System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setString(1, this.veranstaltung_name);
		prep.setString(2, this.beschreibung);
		prep.setString(3, this.ort);
		prep.setString(4, this.datum);
		prep.setString(5, this.beginn);
		prep.setString(6, this.ende);
		prep.executeUpdate();
		this.schreibeAusgewaehlteInteressenVonVeranstaltungInDB();
//		System.out.println("Veranstaltung " + this.veranstaltung_name + " wurde bearbeitet.");

		dbConn.close();
	}

//Veranstaltung erstellen
	public void veranstaltungErstellen() throws SQLException {
//		 Daten aus der View in die DB schreiben
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "INSERT INTO Veranstaltung "
				+ "(veranstaltung_name, beschreibung, ersteller_id, ort, datum, beginn, ende) "
				+ "values (?,?,?,?,?,?,?)";
		// System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setString(1, this.veranstaltung_name);
		prep.setString(2, this.beschreibung);
		prep.setInt(3, this.benutzer_id);
		prep.setString(4, this.ort);
		prep.setString(5, this.datum);
		prep.setString(6, this.beginn);
		prep.setString(7, this.ende);
		prep.executeUpdate();
		this.liesVeranstaltungen_idAusVeranstaltungen_name();
		schreibeAusgewaehlteInteressenVonVeranstaltungInDB();
		this.veranstaltungBeitreten();
//		System.out.println("Veranstaltung " + this.veranstaltung_name + " wurde angelegt. Ersteller: " + this.benutzer_id);

		dbConn.close();
	}

//Veranstaltungen_id aus Veranstaltungen_name lesen
	public void liesVeranstaltungen_idAusVeranstaltungen_name() throws SQLException {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT veranstaltung_id FROM Veranstaltung WHERE veranstaltung_name = '" + this.veranstaltung_name
				+ "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		while (dbRes.next()) {
			this.veranstaltung_id = dbRes.getInt("veranstaltung_id");
//			System.err.println("Veranstaltungen_id der angelegten Veranstaltung lautet: " + this.veranstaltung_id);
		}

		dbConn.close();
	}

//Veranstaltungenname überprüfen
	public boolean ueberpruefeVeranstaltungExistiert() throws SQLException {
//		 true existiert -> anderer Name wählen, false existiert nicht -> ok
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT veranstaltung_name FROM veranstaltung WHERE veranstaltung_name = '"
				+ this.veranstaltung_name + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

//		System.err.println("dbRes: " + dbRes.next());
		while (dbRes.next()) {
			dbConn.close();
			return true;
		}
		dbConn.close();
		return false;

	}

//VeranstaltungVerlassen
	public void veranstaltungVerlassen() throws SQLException {
		if (this.mitglied) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
			String sql = "DELETE FROM benutzer_besucht_veranstaltung WHERE benutzer_id ='" + this.benutzer_id
					+ "' AND veranstaltung_id ='" + this.veranstaltung_id + "'";
			dbConn.prepareStatement(sql).executeUpdate();

			System.err.println("Benutzer_id " + this.benutzer_id + " hat Veranstaltung_id " + this.veranstaltung_id
					+ " verlassen.");
			dbConn.close();
		}
	}

//VeranstaltungBeitreten
	public void veranstaltungBeitreten() throws SQLException {
//		benutzer_id und Veranstaltungen_id in Tabelle eintragen
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "INSERT INTO benutzer_besucht_veranstaltung (veranstaltung_id, benutzer_id) " + "values (?,?)";
//		System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);

		prep.setInt(1, this.veranstaltung_id);
		prep.setInt(2, this.benutzer_id);
		prep.executeUpdate();
//		System.err.println("Benutzer " + this.benutzer_id + " ist der Veranstaltung_id " + this.veranstaltung_id + " beigetreten");

		dbConn.close();
	}

//VeranstlatungenView
	public String getAlleVeranstaltungenView() throws SQLException {
		liesAlleVeranstaltungenAusDB();
		String html = "";
		html += "<h5>Alle Veranstaltungen</h5>";
		for (Veranstaltung veranstaltung : Veranstaltungen) {
			html += "<a href='../veranstaltung/VeranstaltungAppl.jsp?geheZu=VeranstaltungProfilView.jsp&veranstaltung_id="
					+ veranstaltung.getVeranstaltung_id()
					+ "\' class=\"w3-button\"> <img src ='../img/veranstaltung.jpg' alt='"
					+ veranstaltung.getVeranstaltung_name() + "'" + "title='" + veranstaltung.getVeranstaltung_name()
					+ "' width='50' height='50' /> " + veranstaltung.getVeranstaltung_name() + "</a><br>";
		}
		return html;
	}

//VeranstaltungenView
	public String getEigeneVeranstaltungenViewRechts() throws SQLException {
		String html = "";
		html += "<h4>Deine Veranstaltungen</h4>";
		liesVeranstaltungenAusDBVonBenutzer();
		html += "<table width='100%'><tr>";
		for (Veranstaltung veranstaltung : Veranstaltungen) {
			html += "<tr><td><a href='../veranstaltung/VeranstaltungAppl.jsp?geheZu=VeranstaltungProfilView.jsp&veranstaltung_id="
					+ veranstaltung.getVeranstaltung_id()
					+ "\' class=\"w3-button\"> <img src ='../img/veranstaltung.jpg' alt='"
					+ veranstaltung.getVeranstaltung_name() + "'" + "title='" + veranstaltung.getVeranstaltung_name()
					+ "' width='50' height='50' /> " + veranstaltung.getVeranstaltung_name() + "</a></td><tr>";
		}
		html += "</table>";
		return html;
	}

//VeranstaltungenView
	public String getEigeneVeranstaltungenView() throws SQLException {
		String html = "";
		html += "<h4>Deine Veranstaltungen</h4>";
		liesVeranstaltungenAusDBVonBenutzer();
		int i = 0;
		html += "<table width='100%'><tr>";
		for (Veranstaltung veranstaltung : Veranstaltungen) {
			if (i % 2 == 0) {
				html += "</tr><tr>";
			}
			i++;
			html += "<td><a href='../veranstaltung/VeranstaltungAppl.jsp?geheZu=VeranstaltungProfilView.jsp&veranstaltung_id="
					+ veranstaltung.getVeranstaltung_id()
					+ "\' class=\"w3-button\"> <img src ='../img/veranstaltung.jpg' alt='"
					+ veranstaltung.getVeranstaltung_name() + "'" + "title='" + veranstaltung.getVeranstaltung_name()
					+ "' width='50' height='50' /> " + veranstaltung.getVeranstaltung_name() + "</a></td>";
		}
		html += "</tr></table>";
		return html;
	}

	private void liesAlleVeranstaltungenAusDB() throws SQLException {
		this.Veranstaltungen.clear();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT veranstaltung_id, veranstaltung_name, beschreibung, ersteller_id, ort, datum, beginn, ende FROM veranstaltung";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		String beschreibungDB, datumDB, beginnDB, endeDB, ortDB;
		while (dbRes.next()) {
			beschreibungDB = "";
			datumDB = "";
			beginnDB = "";
			endeDB = "";
			ortDB = "";
			if (dbRes.getString("beschreibung") != null) {
				beschreibungDB = dbRes.getString("beschreibung");
			}
			if (dbRes.getString("datum") != null) {
				datumDB = dbRes.getString("datum");
			}
			if (dbRes.getString("beginn") != null) {
				beginnDB = dbRes.getString("beginn");
			}
			if (dbRes.getString("ende") != null) {
				endeDB = dbRes.getString("ende");
			}
			if (dbRes.getString("ort") != null) {
				ortDB = dbRes.getString("ort");
			}

			Veranstaltung neueVeranstaltung = new Veranstaltung(dbRes.getInt("veranstaltung_id"),
					dbRes.getString("veranstaltung_name"), beschreibungDB, dbRes.getInt("ersteller_id"), ortDB, datumDB,
					beginnDB, endeDB);
			this.Veranstaltungen.add(neueVeranstaltung);
//				System.out.println("Alle vorhandenen Veranstaltungen wurden ausgelesen");
		}
		dbConn.close();
	}

	// Eigene Veranstaltungen aus DB lesen
	public void liesVeranstaltungenAusDBVonBenutzer() throws SQLException { //
		this.benutzer_id = WillkommenBean.getBenutzer_id();
		int[] veranstaltung_id = new int[10];
		this.Veranstaltungen.clear();
		Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT veranstaltung_id from benutzer_besucht_veranstaltung WHERE benutzer_id = '"
				+ this.benutzer_id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		int i = 0;
		while (dbRes.next()) {
			int veranstaltungid = dbRes.getInt("veranstaltung_id");
			veranstaltung_id[i] = veranstaltungid;
			i++;
		}

		for (int j = 0; j < veranstaltung_id.length; j++) {
			String sql1 = "SELECT veranstaltung_id, veranstaltung_name, beschreibung, ersteller_id, ort, datum, beginn, ende FROM veranstaltung WHERE veranstaltung_id = "
					+ veranstaltung_id[j];
			ResultSet dbRes2 = dbConn.createStatement().executeQuery(sql1);
			while (dbRes2.next()) {
				Veranstaltung neueVeranstaltung = new Veranstaltung(dbRes2.getInt("veranstaltung_id"),
						dbRes2.getString("veranstaltung_name"), dbRes2.getString("beschreibung"),
						dbRes2.getInt("ersteller_id"), dbRes2.getString("ort"), dbRes2.getString("datum"),
						dbRes2.getString("beginn"), dbRes2.getString("ende"));
				this.Veranstaltungen.add(neueVeranstaltung);
//				System.out.println("Eigene Veranstaltungen wurden für Benutzer_id " + this.benutzer_id + " ausgelesen");
			}
		}
		dbConn.close();
	}

//Darstellung der eigenen Veranstaltungen in HTML
	public String getHTMLAllerVeranstaltungenVonBenutzer() {

		String html = "";

		for (Veranstaltung veranstaltung : Veranstaltungen) {

			html += "<a href=\'=\'../veranstaltung/VeranstaltungAppl.jsp?geheZu=VeranstaltungProfilView.jsp&veranstaltung_id="
					+ veranstaltung.getVeranstaltung_id()
					+ "\' class=\"w3-button\"> <img src ='../img/veranstaltung.jpg alt='"
					+ veranstaltung.getVeranstaltung_name() + "'" + "title='" + veranstaltung.getVeranstaltung_name()
					+ "' width='50' height='50' /> " + veranstaltung.getVeranstaltung_name() + "</a>";
		}
		return html;
	}

//Veranstaltungendaten aus DB auslesen
	public void getVeranstaltungdaten() throws SQLException { //
		Connection dbConn = new PostgreSQLAccess().getConnection();
//		System.err.println("getVeranstaltungdaten(): gid: " + this.veranstaltung_id);
		String sql = "SELECT veranstaltung_id, veranstaltung_name, beschreibung, ersteller_id, ort, datum, beginn, ende FROM Veranstaltung WHERE veranstaltung_id = '"
				+ this.veranstaltung_id + "'";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		while (dbRes.next()) {
			this.veranstaltung_id = dbRes.getInt("veranstaltung_id");
			this.veranstaltung_name = dbRes.getString("veranstaltung_name");
			this.beschreibung = dbRes.getString("beschreibung");
			this.ersteller_id = dbRes.getInt("ersteller_id");
			this.ort = dbRes.getString("ort");
			this.datum = dbRes.getString("datum");
			this.beginn = dbRes.getString("beginn");
			this.ende = dbRes.getString("ende");

			if (this.beschreibung == null)
				this.beschreibung = "";
			if (this.datum == null)
				this.datum = "";
			if (this.beginn == null)
				this.beginn = "";
			if (this.ende == null)
				this.ende = "";
			if (this.ort == null)
				this.ort = "";

			System.err.println("Daten von Veranstaltung mit ID " + this.veranstaltung_id + " ausgelesen");
		}
		dbConn.close();
	}

	public String getName() {
		return veranstaltung_name;
	}

	public void setName(String veranstaltung_name) {
		this.veranstaltung_name = veranstaltung_name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getBeginn() {
		return beginn;
	}

	public int getBenutzer_id() {
		return benutzer_id;
	}

	public void setBenutzer_id(int benutzer_id) {
		this.benutzer_id = benutzer_id;
	}

	public int getVeranstaltung_id() {
		return veranstaltung_id;
	}

	public void setVeranstaltung_id(String veranstaltung_id) {
		this.veranstaltung_id = Integer.parseInt(veranstaltung_id);
	}

	public String getVeranstaltung_name() {
		return veranstaltung_name;
	}

	public void setVeranstaltung_name(String veranstaltung_name) {
		this.veranstaltung_name = veranstaltung_name;
	}

	public String getEnde() {
		return ende;
	}

	public void setEnde(String ende) {
		this.ende = ende;
	}

	public int getErsteller_id() {
		return ersteller_id;
	}

	public void setErsteller_id(int ersteller_id) {
		this.ersteller_id = ersteller_id;
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

	public ArrayList<Veranstaltung> getVeranstaltungen() {
		return Veranstaltungen;
	}

	public void setVeranstaltungen(ArrayList<Veranstaltung> veranstaltungen) {
		Veranstaltungen = veranstaltungen;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public ArrayList<Mitglied> getMitglieder() {
		return Mitglieder;
	}

	public void setBeginn(String beginn) {
		this.beginn = beginn;
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

	public void setVeranstaltung_id(int veranstaltung_id) {
		this.veranstaltung_id = veranstaltung_id;
	}

	public String getBild() {
		return bild;
	}

	public void setBild(String bild) {
		this.bild = bild;
	}

}
