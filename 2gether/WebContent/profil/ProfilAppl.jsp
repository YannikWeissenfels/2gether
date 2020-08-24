<%@page import="de.hs_lu.beans.WillkommenBean"%>
<%@page import="de.hs_lu.beans.NachrichtBean"%>
<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.DenullifizierBean"%>
<%@page import="de.hs_lu.beans.ProfilBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="dnf" class="de.hs_lu.beans.DenullifizierBean" scope="session" />
<jsp:useBean id="profb" class="de.hs_lu.beans.ProfilBean" scope="session" />
<jsp:useBean id="bb" class="de.hs_lu.beans.BenachrichtigungBean" scope="session" />
<jsp:useBean id="nb" class="de.hs_lu.beans.NachrichtBean" scope="session" />
<jsp:useBean id="wb" class="de.hs_lu.beans.WillkommenBean" scope="session" />

	<% 
		request.setCharacterEncoding("UTF-8");
		//Einlesen & Denullify			
		String geheZu = dnf.dnf(request.getParameter("geheZu"));
		String bearbeiten = dnf.dnf(request.getParameter("bearbeiten"));
		String speichern = dnf.dnf(request.getParameter("speichern"));
		
		// Einlesen für Tabelle Benutzer
		String vorname = request.getParameter("vorname");
		String nachname = request.getParameter("nachname");
		String geschlecht = request.getParameter("geschlecht");
		String geburtstag = request.getParameter("geburtstag");
		String wohnort = request.getParameter("wohnort");
		String beziehungsstatus = request.getParameter("beziehungsstatus");
		String kurzprofil = request.getParameter("kurzprofil");
		

		//Einlesen für Tabelle Personeninteresse
		String maximalalter = request.getParameter("maximalalter");
		String mindestalter = request.getParameter("mindestalter");
		String geschlechtPers = request.getParameter("geschlechtPers");


		//Einlesen für Tabelle benutzer_veranstaltungsart
		String[] interessen = request.getParameterValues("interesse");
		if (interessen == null)
			interessen = new String[0];
		
		String[] stadt = request.getParameterValues("stadt");
		if (stadt == null)
			stadt = new String[0];
		
		//Einlesen für Freunde
		String ausgewaehlteFreund_id = dnf.dnf(request.getParameter("ausgewaehlteFreund_id"));
		String entfernen = dnf.dnf(request.getParameter("entfernen"));
		String zurueckziehen = dnf.dnf(request.getParameter("zurueckziehen"));
		String freundschaftsanfrage = dnf.dnf(request.getParameter("freundschaftsanfrage"));
		String annehmen = dnf.dnf(request.getParameter("annehmen"));
		String ablehnen = dnf.dnf(request.getParameter("ablehnen"));
		
		//Weiterleitungen View
		if (geheZu.equals("ProfilView.jsp")) {
			profb.persoenlicheDatenFuerEigenesProfil();
			profb.liesInteressenVonBenutzerAusDB();
			bb.defaultNachricht();
			//Weiterleitung zur ProfilView.jsp
			response.sendRedirect("ProfilView.jsp");
			
		} else if (geheZu.equals("ProfilBearbeitenView.jsp")) {
			profb.persoenlicheDatenFuerEigenesProfil();
			profb.liesAlleInteressenAusDB();
			profb.liesAlleStaedteAusDB();
			bb.defaultNachricht();
			//Weiterleitung zur ProfilBearbeitenView.jsp
			response.sendRedirect("ProfilBearbeitenView.jsp"); 
					
		} else if (geheZu.equals("FreundeView.jsp")) {
		profb.liesFreundschaftenUndFreundenamenVonBenutzerAusDB();
		profb.offeneAnfragenVonBenutzerGesendet();
		bb.defaultNachricht();
			response.sendRedirect("FreundeView.jsp");		
			
		} else if (bearbeiten.equals("Profil bearbeiten")) {
			profb.persoenlicheDatenFuerEigenesProfil();
			profb.liesAlleInteressenAusDB();
			profb.liesAlleStaedteAusDB();
			bb.defaultNachricht();
			response.sendRedirect("ProfilBearbeitenView.jsp");
			
			
		} else if (geheZu.equals("FreundeProfilView.jsp")) {
			profb.setAusgewaehlteFreund_id(ausgewaehlteFreund_id);
			profb.persoenlicheDatenFuerAndereBenutzer();
			profb.liesInteressenVonFreundenAusDB();
			bb.defaultNachricht();
			
			if (Integer.parseInt(ausgewaehlteFreund_id)==WillkommenBean.getBenutzer_id()) {
				response.sendRedirect("ProfilView.jsp");	
			}
			else {
			response.sendRedirect("FreundeProfilView.jsp"); 
			bb.defaultNachricht();
			}
			
//Weiterleitungen Bean

// Aufruf Methoden für Freunschaftsanfragen / Freund entfernen	
		} else if (freundschaftsanfrage.equals("Freundschaftsanfrage senden")) {
			profb.freundschaftsanfrageSenden();
			bb.freundschaftsanfrageGesendet();
			profb.offeneAnfragenVonBenutzerGesendet();
			//Weiterleitung Refresh
			response.sendRedirect("FreundeView.jsp");
			
		} else if (zurueckziehen.equals("Freundschaftsanfrage zurückziehen")) {
			profb.eigeneFreundschaftsanfrageZurueckziehen();	
			bb.anfrageZurueckgezogen();
			profb.offeneAnfragenVonBenutzerGesendet();
			response.sendRedirect("FreundeView.jsp"); 

			
		} else if (annehmen.equals("annehmen")) {
				profb.setAusgewaehlteFreund_id(ausgewaehlteFreund_id);
				profb.setAntwortFreundschaftsanfrage(annehmen);
				profb.freundschaftsanfrageAnnehmenUndSpeichern();
				bb.freundAngenommen();
				response.sendRedirect("../start/StartView.jsp");
							
					
			} else if (ablehnen.equals("ablehnen")) {
				profb.setAusgewaehlteFreund_id(ausgewaehlteFreund_id);
				profb.setAntwortFreundschaftsanfrage(ablehnen);
				profb.loescheFreundschaftsanfrage();
				bb.freundAbgelehnt();
				response.sendRedirect("../start/StartView.jsp");	
				
			} else if (annehmen.equals("Annehmen")) {
				profb.freundschaftsanfrageAnnehmenUndSpeichern();
				bb.freundAngenommen();
				response.sendRedirect("FreundeProfilView.jsp");
							
					
			} else if (ablehnen.equals("Ablehnen")) {
				profb.loescheFreundschaftsanfrage();
				bb.freundAbgelehnt();
				response.sendRedirect("FreundeProfilView.jsp");		
		
		} else if (entfernen.equals("Freund entfernen")) {
			profb.freundEntfernen();
			profb.liesFreundschaftenUndFreundenamenVonBenutzerAusDB();
			bb.freundGeloescht();
			response.sendRedirect("FreundeView.jsp");
//Aufruf Methoden für Anzeige der Daten aus Benutzer, Personeninteresse und benutzer_veranstaltungsart			
	
		} else if (speichern.equals("speichern")) {
//Profil speichern  
		//	Übertrag in Bean für Tabelle Benutzer
		profb.setVorname(vorname);
		profb.setNachname(nachname);
		profb.setGeschlechtBenutzer(geschlecht);
		profb.setGeburtstag(geburtstag);
		profb.setWohnort(wohnort);
		profb.setBeziehungsstatus(beziehungsstatus);
		profb.setKurzprofil(kurzprofil);
		// Übertrag in Bean für Tabelle Personeninteresse
		profb.setMindestalter(mindestalter);
		System.out.print(mindestalter);
		profb.setMaximalalter(maximalalter);
		profb.setGeschlechtPerson(geschlechtPers);
		// Übertrag in Bean für Tabelle benutzer_veranstaltungsart
		profb.setMeineInteressenFuerDB(interessen);				
		profb.setMeineStaedteFuerDB(stadt);
		// Aufruf Methoden für Speichern in Benutzer, Personeninteresse, benutzer_veranstaltungsart
		profb.profilBearbeiten();
		profb.schreibeAusgewaehlteInteressenVonBenutzerInDB();
		profb.schreibeAusgewaehlteStaedteVonBenutzerInDB();
		profb.liesInteressenVonBenutzerAusDB();
		bb.datenänderungErfolgreich();
		response.sendRedirect("ProfilView.jsp");
	

		//Weiterleitungen Fehler/Angriffe
		} else {
			response.sendRedirect("../willkommen/WillkommenAppl.jsp?geheZu=WillkommenView.jsp");
		}
	%>

</body>
</html>