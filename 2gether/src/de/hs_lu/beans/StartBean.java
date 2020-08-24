package de.hs_lu.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import de.hs_lu.jdbc.PostgreSQLAccess;

public class StartBean {

ArrayList<Benutzer> benutzerMatching;
ArrayList<Veranstaltung> veranstaltungsMatching;
ArrayList<Gruppe> gruppenMatching;

ProfilBean profilbean;

GruppeBean gruppebean;
VeranstaltungBean veranstaltungbean;



	public StartBean() throws SQLException {
		
	this.benutzerMatching = new ArrayList<>();
	this.veranstaltungsMatching = new ArrayList<>();
	this.gruppenMatching = new ArrayList<>();
	
	this.profilbean = new ProfilBean();
	this.gruppebean = new GruppeBean();
	this.veranstaltungbean = new VeranstaltungBean();

	}
	

	
// Benutzermatching
	
	public void allePersonenmatchings() throws SQLException {
		this.benutzerMatching.clear();

		this.benutzermatchingWegenPersoneninteressen();
		this.benutzermatchingWegenGleicherVeranstaltung();
		this.benutzermatchingWegenGleicherGruppe();
		this.benutzerMatchingWegenGleichenInteressen();
	}

// Benutzermatching durch Abgleich der Personeninteressen im Profil des Benutzers und Eigenschaften aller Benutzer	
	public boolean benutzermatchingWegenPersoneninteressen() throws SQLException {

		boolean ergebnis = false;
	
		profilbean.getDatenAusPersoneninteresse(profilbean.getBenutzer_id());
	
		Connection dbConn = new PostgreSQLAccess().getConnection();
		
		String sql = "SELECT benutzer_id, vorname, nachname, geschlecht, geburtstag FROM BENUTZER";
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);

		while (dbRes.next()) {
		int benutzerid = dbRes.getInt("benutzer_id");
		String vorname = dbRes.getString("vorname");
		String nachname = dbRes.getString("nachname");
		String geschlechtBenutzer = dbRes.getString("geschlecht");
		String geburtstag = dbRes.getString("geburtstag");	

		
		int alter = profilbean.getAlter(geburtstag);

		// Abgleich Geschlecht und Alter
		if ((geschlechtBenutzer.equals(profilbean.getGeschlechtPerson()) || 
			profilbean.getGeschlechtPerson().equals("beides"))
			&& alter >= profilbean.getMindestalterInt() && alter <= profilbean.getMaximalalterInt()
			&& profilbean.getBenutzer_id() != benutzerid) {
			Benutzer neuerBenutzer = new Benutzer(benutzerid, vorname, nachname, "Passt zu deinen Einstellungen");
			this.benutzerMatching.add(neuerBenutzer);
			
			ergebnis = true;
		}
		else 
			ergebnis = false;	
}
		this.istVorgeschlagenerBenutzerSchonFreund();
		this.AbgleichBenutzerMatchingMehrereGruende();
		dbConn.close();
		return ergebnis;
	}

// Benutzermatching wegen gleichen Veranstaltungen
	public void benutzermatchingWegenGleicherVeranstaltung() throws SQLException {
		profilbean.benutzeridEinspielen();
		
		String grund = "Besucht die gleiche Veranstaltung";

		// Veranstaltungsids von Benutzer auslesen		
		veranstaltungbean.liesVeranstaltungenAusDBVonBenutzer();
		
		// Fremde Benutzerids auslesen -> wer geht auf die gleiche Veranstaltung
		if (!veranstaltungbean.Veranstaltungen.isEmpty()) {
		Connection dbConn = new PostgreSQLAccess().getConnection();
		
		String sql = "SELECT DISTINCT BV.BENUTZER_ID, B.VORNAME, B.NACHNAME" + 
				" FROM BENUTZER_BESUCHT_VERANSTALTUNG BV" + 
				" INNER JOIN BENUTZER B" + 
				" ON BV.BENUTZER_ID = B.BENUTZER_ID" + 
				" WHERE (";
				
		for (int j = 0; j < veranstaltungbean.Veranstaltungen.size(); j++) {
			Veranstaltung id = veranstaltungbean.Veranstaltungen.get(j);
			if (j == veranstaltungbean.Veranstaltungen.size()-1) {
					sql += "BV.VERANSTALTUNG_ID ='"+ id.veranstaltung_id +"')";	
					}
			else {
			sql += "BV.VERANSTALTUNG_ID ='"+ id.veranstaltung_id +"' OR ";
				}
			}
			sql += " AND NOT BV.BENUTZER_ID = "+profilbean.getBenutzer_id();
			ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
	
		while (dbRes.next()) {
		Benutzer neuerBenutzer = new Benutzer(dbRes.getInt("benutzer_id"), dbRes.getString("vorname"), dbRes.getString("nachname"), grund);
		this.benutzerMatching.add(neuerBenutzer);		
		}
		
		this.istVorgeschlagenerBenutzerSchonFreund();
		this.AbgleichBenutzerMatchingMehrereGruende();
		dbConn.close();
	}
		
	}
	
// Methode Benutzermatching wegen gleichen Gruppen	
	public void benutzermatchingWegenGleicherGruppe() throws SQLException {
		profilbean.benutzeridEinspielen();
		String grund = "Ist in der gleichen Gruppe";
		gruppebean.liesGruppenAusDBVonBenutzer();
		
		// Fremde Benutzerids auslesen -> wer ist in der gleichen Gruppe
		if (!gruppebean.Gruppen.isEmpty()) {
			Connection dbConn = new PostgreSQLAccess().getConnection();
		String sql = "SELECT DISTINCT BG.BENUTZER_ID, B.VORNAME, B.NACHNAME" + 
				" FROM BENUTZER_IN_GRUPPE Bg" + 
				" INNER JOIN BENUTZER B" + 
				" ON BG.BENUTZER_ID = B.BENUTZER_ID" + 
				" WHERE (";
				
		for (int j = 0; j < gruppebean.Gruppen.size(); j++) {
			Gruppe id = gruppebean.Gruppen.get(j);
			if (j == gruppebean.Gruppen.size()-1) {
					sql += "BG.GRUPPE_ID ='"+ id.gruppe_id +"')";	
					}
			else {
			sql += "BG.GRUPPE_ID ='"+ id.gruppe_id +"' OR ";
				}
			}
			sql += " AND NOT BG.BENUTZER_ID = "+profilbean.getBenutzer_id();

		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		
		while (dbRes.next()) {
		Benutzer neuerBenutzer = new Benutzer(dbRes.getInt("benutzer_id"), dbRes.getString("vorname"), dbRes.getString("nachname"), grund);
		this.benutzerMatching.add(neuerBenutzer);		
		}
			 	
		this.istVorgeschlagenerBenutzerSchonFreund();
		this.AbgleichBenutzerMatchingMehrereGruende();
		dbConn.close();
	}
	}
	

// Methode BenutzermatchingWegenGleichenInteressen
	public void benutzerMatchingWegenGleichenInteressen() throws SQLException {

	String grund = "Hat gleiche Interessen";
	profilbean.liesInteressenVonBenutzerAusDB();

	// Fremde Benutzerids auslesen -> wer hat gleiche Interessen
	if (!profilbean.meineInteressenAusDB.isEmpty()) {
		
		Connection dbConn = new PostgreSQLAccess().getConnection();
		
		String sql = "SELECT DISTINCT BI.BENUTZER_ID, B.VORNAME, B.NACHNAME" + 
				" FROM BENUTZER_INTERESSE BI" + 
				" INNER JOIN BENUTZER B" + 
				" ON BI.BENUTZER_ID = B.BENUTZER_ID" + 
				" WHERE (";
				
			for (int i = 0; i < profilbean.meineInteressenAusDB.size(); i++) {
			Interesse interesse = profilbean.meineInteressenAusDB.get(i);	
			if (i == profilbean.meineInteressenAusDB.size()-1) {
					sql += "BI.INTERESSE ='"+ interesse.getArt() +"')";	
					}
			else {
			sql += "BI.INTERESSE ='"+ interesse.getArt() +"' OR ";
				}
			}
			sql += " AND NOT BI.BENUTZER_ID = "+profilbean.getBenutzer_id();
			
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while(dbRes.next()) {
		Benutzer neuerBenutzer = new Benutzer(dbRes.getInt("benutzer_id"), dbRes.getString("vorname"), dbRes.getString("nachname"), grund);
		this.benutzerMatching.add(neuerBenutzer);
		}

		this.istVorgeschlagenerBenutzerSchonFreund();
		this.AbgleichBenutzerMatchingMehrereGruende();
		dbConn.close();
	}
	}

	
//	Prüfung, ob vorgeschlagener Benutzer in ArrayList schon mit Benutzer befreundet ist. Dann soll er nicht vorgeschlagen werden.
	public void istVorgeschlagenerBenutzerSchonFreund() throws SQLException {
		
		profilbean.liesFreundschaftenUndFreundenamenVonBenutzerAusDB();
		
		for (int i = 0; i < benutzerMatching.size(); i++) {
		Benutzer benutzer = benutzerMatching.get(i);
		
		for (int j = 0; j < profilbean.meineFreunde.size(); j++) {
		Freund freund = profilbean.meineFreunde.get(j);
		
		if (benutzer.getBenutzeridVonArrayList() == freund.getFreund_id()) {
			benutzerMatching.remove(benutzer);
			
		}		
			}

				}
					}
	
	
// Gleicht ab, ob es doppelte Benutzerids in der ArrayList gibt mit unterschiedlichen Gründen.
// Dann werden die beiden Gründe zusammengefügt und ein Benutzer gelöscht, sodass der verbleibende Benutzer beide Gründe hat.
	public void AbgleichBenutzerMatchingMehrereGruende() {		
		
	for (int i = 0; i < benutzerMatching.size(); i++) { //komplette Liste durchlaufen
		Benutzer b1 = benutzerMatching.get(i); 
	for (int x = i + 1; x < benutzerMatching.size(); x++) { //Folgeeinträge durchlaufen
		Benutzer b2 = benutzerMatching.get(x);

	if (b1.getBenutzeridVonArrayList() == b2.getBenutzeridVonArrayList() && !(b1.getGrund().equals(b2.getGrund())) ) {
			String neuerGrund = b1.getGrund()+"<br/>"+b2.getGrund();
			b1.setGrund(neuerGrund);
			benutzerMatching.remove(b2); 
			}
		
		}	
			}

	}


	public String getHTMLAllerVorgeschlagenenPersonen() throws SQLException {

		String html = "";;
		
		if(benutzerMatching.isEmpty()) {
			
			html += "Leider gab es keine Matching-Treffer für Personen.<br/>Vielleicht musst du deine <a href='../profil/ProfilAppl.jsp?geheZu=ProfilBearbeitenView.jsp'>Einstellungen</a> ändern.";
		}
		else {
			
			for (int i = 0; i < benutzerMatching.size(); i++) {
				Benutzer neuerBenutzer = benutzerMatching.get(i);
				profilbean.richtigesBild(neuerBenutzer.getBenutzeridVonArrayList());
			
					html+= "<a href='../profil/ProfilAppl.jsp?geheZu=FreundeProfilView.jsp&ausgewaehlteFreund_id="+neuerBenutzer.getBenutzeridVonArrayList()+"' class=\"w3-button\"> <img src ='../img/"+profilbean.getBild()+"' alt='"+neuerBenutzer.getNachname()+"'"
							+"title='"+neuerBenutzer.getNachname()+"' width='50' height='50' /> "+neuerBenutzer.getVorname()+" "+neuerBenutzer.getNachname()
							+ "<br/><i>"+neuerBenutzer.getGrund()+"</i></a><br/>";
					
				}	
		}


	
		return html;	
}
	
// Veranstaltungsmatching	
	public void alleVeranstaltungsmatchings() throws SQLException {
		this.veranstaltungsMatching.clear();
		this.veranstaltungsmatchingWegenGleichenInteressen();
		this.veranstaltungsmatchingWegenNähe();

		
	}

//Veranstaltungsmatching aufgrund gleicher Interessen

	public void veranstaltungsmatchingWegenGleichenInteressen() throws SQLException {
		
		String grund = "Passt zu deinen Interessen";
		
		Connection dbConn = new PostgreSQLAccess().getConnection();
		
		String sql = "SELECT DISTINCT VI.VERANSTALTUNG_ID, V.VERANSTALTUNG_NAME, VI.INTERESSE" +
					 " FROM VERANSTALTUNG V"+
					 " INNER JOIN VERANSTALTUNG_INTERESSE VI"+
					 " ON VI.VERANSTALTUNG_ID = V.VERANSTALTUNG_ID"+
					 " INNER JOIN BENUTZER_INTERESSE BI"+
					 " ON BI.INTERESSE = VI.INTERESSE"+
					 " WHERE BENUTZER_ID = "+this.profilbean.getBenutzer_id();
	ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
	while(dbRes.next()) {
	Veranstaltung neueVeranstaltung = new Veranstaltung(dbRes.getInt("veranstaltung_id"), dbRes.getString("veranstaltung_name"), dbRes.getString("interesse"), grund);
		
	this.veranstaltungsMatching.add(neueVeranstaltung);
	}
			
		
		for (int i = 0; i < veranstaltungsMatching.size(); i++) { //komplette Liste durchlaufen
			Veranstaltung v1 = veranstaltungsMatching.get(i); 
		for (int x = i + 1; x < veranstaltungsMatching.size(); x++) { //Folgeeinträge durchlaufen
			Veranstaltung v2 = veranstaltungsMatching.get(x);

		if (v1.getVeranstaltung_id() == v2.getVeranstaltung_id()) {
				veranstaltungsMatching.remove(v2); 
				}
			
			}	
				}

	
	this.wirdVorgeschlageneVeranstaltungSchonBesucht();
	this.AbgleichVeranstaltungsMatchingMehrereGruende();
	dbConn.close();
	
	
}
	
//	Veranstaltungsmatching aufgrund Nähe
	public void veranstaltungsmatchingWegenNähe() throws SQLException {
	profilbean.liesStaedteAusDB(profilbean.getBenutzer_id());	
	
	String grund = "Passt zu deinen Städteinteressen";
	if (!profilbean.meineStaedteAusDB.isEmpty()) {
	Connection dbConn = new PostgreSQLAccess().getConnection();
	
	String sql = "SELECT DISTINCT VERANSTALTUNG_ID, VERANSTALTUNG_NAME" + 
			" FROM VERANSTALTUNG" + 
			" WHERE (";
			
		for (int i = 0; i < profilbean.meineStaedteAusDB.size(); i++) {
		String stadt = profilbean.meineStaedteAusDB.get(i);	
		if (i == profilbean.meineStaedteAusDB.size()-1) {
				sql += "ORT ='"+ stadt +"')";	
				}
		else {
		sql += "ORT ='"+stadt +"' OR ";
			}
		}
		
	ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
	while(dbRes.next()) {
		Veranstaltung neueVeranstaltung = new Veranstaltung(dbRes.getInt("veranstaltung_id"), dbRes.getString("veranstaltung_name"), grund);
		this.veranstaltungsMatching.add(neueVeranstaltung);
	}
	
	
	for (int i = 0; i < veranstaltungsMatching.size(); i++) { //komplette Liste durchlaufen
		Veranstaltung v1 = veranstaltungsMatching.get(i); 
	for (int x = i + 1; x < veranstaltungsMatching.size(); x++) { //Folgeeinträge durchlaufen
		Veranstaltung v2 = veranstaltungsMatching.get(x);

	if (v1.getVeranstaltung_id() == v2.getVeranstaltung_id()) {
			veranstaltungsMatching.remove(v2); 
			}
		
		}	
			}
	
	this.wirdVorgeschlageneVeranstaltungSchonBesucht();
	this.AbgleichVeranstaltungsMatchingMehrereGruende();
	dbConn.close();
	}
	}
	
	
// Gleicht ab, ob es doppelte Veranstaltungsids in der ArrayList gibt mit unterschiedlichen Gründen.
// Dann werden die beiden Gründe zusammengefügt und ein Benutzer gelöscht, sodass der verbleibende Benutzer beide Gründe hat.
		public void AbgleichVeranstaltungsMatchingMehrereGruende() {		
			
		for (int i = 0; i < veranstaltungsMatching.size(); i++) { //komplette Liste durchlaufen
			Veranstaltung v1 = veranstaltungsMatching.get(i); 
		for (int x = i + 1; x < veranstaltungsMatching.size(); x++) { //Folgeeinträge durchlaufen
			Veranstaltung v2 = veranstaltungsMatching.get(x);

		if (v1.getVeranstaltung_id() == v2.getVeranstaltung_id() && !(v1.getGrund().equals(v2.getGrund())) ) {
				String neuerGrund = v1.getGrund()+"<br/>"+v2.getGrund();
				v1.setGrund(neuerGrund);
				veranstaltungsMatching.remove(v2); 
				}
			
			}	
				}

		}
	
	
	
	
//	Prüfung, ob vorgeschlagene Veranstaltung schon von Benutzer besucht wird. Dann soll sie nicht vorgeschlagen werden.
	public void wirdVorgeschlageneVeranstaltungSchonBesucht() throws SQLException {
		
		veranstaltungbean.liesVeranstaltungenAusDBVonBenutzer();
		
		for (int i = 0; i < veranstaltungsMatching.size(); i++) {
		Veranstaltung veranstaltung = veranstaltungsMatching.get(i);
		
		for (int j = 0; j < veranstaltungbean.Veranstaltungen.size(); j++) {
		Veranstaltung meineVeranstaltung = veranstaltungbean.Veranstaltungen.get(j);
		
		if (veranstaltung.getVeranstaltung_id()== meineVeranstaltung.getVeranstaltung_id()) {
			veranstaltungsMatching.remove(veranstaltung);
			
		}		
			}
				}
					}
	
	

		
	
	
	
	public String getHTMLAllerVorgeschlagenenVeranstaltungen() {

		String html = "";;
		
		if(veranstaltungsMatching.isEmpty()) {
			
			html += "Leider gab es keine Matching-Treffer für Veranstaltungen.<br/>Vielleicht musst du deine <a href='../profil/ProfilAppl.jsp?geheZu=ProfilBearbeitenView.jsp'>Einstellungen</a> ändern.";
		}
		else {
			
			for (int i = 0; i < veranstaltungsMatching.size(); i++) {
				Veranstaltung neueVeranstaltung = veranstaltungsMatching.get(i);
			
			
					html+= "<a href='../veranstaltung/VeranstaltungAppl.jsp?geheZu=VeranstaltungProfilView.jsp&veranstaltung_id="+neueVeranstaltung.getVeranstaltung_id()+"' class=\"w3-button\"> <img src ='../img/veranstaltung.jpg' alt='"+neueVeranstaltung.getVeranstaltung_name()+"'"
							+"title='"+neueVeranstaltung.getVeranstaltung_name()+"' width='50' height='50' /> "+neueVeranstaltung.getVeranstaltung_name()
							+ "<br/><i>"+neueVeranstaltung.getGrund()+"</i></a><br/> <br/>";

				}	
		}

	
		return html;	
}
	
	
// Gruppenmatching	
		public void alleGruppenmatchings() throws SQLException {
			this.gruppenMatching.clear();
			this.gruppenmatchingWegenGleichenInteressen();
			
		}

	//Gruppenmatching aufgrund gleicher Interessen

		public void gruppenmatchingWegenGleichenInteressen() throws SQLException {
			
			String grund = "Passt zu deinen Interessen";
			
			Connection dbConn = new PostgreSQLAccess().getConnection();
			
			String sql = "SELECT DISTINCT GI.GRUPPE_ID, G.GRUPPE_NAME, GI.INTERESSE" +
						 " FROM GRUPPE G"+
						 " INNER JOIN GRUPPE_INTERESSE GI"+
						 " ON GI.GRUPPE_ID = G.GRUPPE_ID"+
						 " INNER JOIN BENUTZER_INTERESSE BI"+
						 " ON BI.INTERESSE = GI.INTERESSE"+
						 " WHERE BENUTZER_ID = "+this.profilbean.getBenutzer_id();
		ResultSet dbRes = dbConn.createStatement().executeQuery(sql);
		while(dbRes.next()) {
		Gruppe neueGruppe = new Gruppe(dbRes.getInt("gruppe_id"), dbRes.getString("gruppe_name"), dbRes.getString("interesse"), grund);
		this.gruppenMatching.add(neueGruppe);
		}
		
		for (int i = 0; i < gruppenMatching.size(); i++) { //komplette Liste durchlaufen
			Gruppe v1 = gruppenMatching.get(i); 
		for (int x = i + 1; x < gruppenMatching.size(); x++) { //Folgeeinträge durchlaufen
			Gruppe v2 = gruppenMatching.get(x);

		if (v1.getGruppe_id() == v2.getGruppe_id()) {
				gruppenMatching.remove(v2); 
				}
			
			}	
				}
		
		this.wirdVorgeschlageneGruppeSchonBesucht();
		dbConn.close();
		
		
	}
		
//		Prüfung, ob vorgeschlagene Gruppe schon von Benutzer besucht wird. Dann soll sie nicht vorgeschlagen werden.
		public void wirdVorgeschlageneGruppeSchonBesucht() throws SQLException {
			
			gruppebean.liesGruppenAusDBVonBenutzer();
			
			for (int i = 0; i < gruppenMatching.size(); i++) {
			Gruppe gruppe = gruppenMatching.get(i);
			
			for (int j = 0; j < gruppebean.Gruppen.size(); j++) {
			Gruppe meineGruppe = gruppebean.Gruppen.get(j);
			
			if (gruppe.getGruppe_id()==meineGruppe.getGruppe_id()) {
				gruppenMatching.remove(gruppe);
				
			}		
				}
					}
						}
		
		
		public String getHTMLAllerVorgeschlagenenGruppen() {

			String html = "";;
			
			if(gruppenMatching.isEmpty()) {
				
				html += "Leider gab es keine Matching-Treffer für Gruppen.<br/>Vielleicht musst du deine <a href='../profil/ProfilAppl.jsp?geheZu=ProfilBearbeitenView.jsp'>Einstellungen</a> ändern.";
			}
			else {
				
				for (int i = 0; i < gruppenMatching.size(); i++) {
					Gruppe neueGruppe = gruppenMatching.get(i);
								
						html += "<a href='../gruppe/GruppeAppl.jsp?geheZu=GruppeProfilView.jsp&gruppe_id=" + neueGruppe.getGruppe_id()
						+ "' class=\"w3-button\"> <img src ='../img/gruppe.jpg' alt='" + neueGruppe.getGruppe_name()
						+ "' title='" + neueGruppe.getGruppe_name() + "' width='50' height='50' />" + neueGruppe.getGruppe_name()
						+ "<br/><i>"+neueGruppe.getGrund()+"</i></a><br/><br/>";

						
					}	
			}

			return html;	
	}
				
}


