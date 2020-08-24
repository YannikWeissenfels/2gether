<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.NachrichtBean"%>
<%@page import="de.hs_lu.beans.DenullifizierBean"%>
<%@page import="de.hs_lu.beans.GruppeBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.sql.SQLException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="dnf" class="de.hs_lu.beans.DenullifizierBean"	scope="session" />
	<jsp:useBean id="gb" class="de.hs_lu.beans.GruppeBean" scope="session" />
	<jsp:useBean id="nb" class="de.hs_lu.beans.NachrichtBean" scope="session" />
	<jsp:useBean id="bb" class="de.hs_lu.beans.BenachrichtigungBean" scope="session" />
	<%
		request.setCharacterEncoding("UTF-8");
			//Einlesen & Denullify
			String geheZu = dnf.dnf(request.getParameter("geheZu"));
			String speichern = dnf.dnf(request.getParameter("speichern"));
			String entfernen = dnf.dnf(request.getParameter("entfernen"));
			String beitreten = dnf.dnf(request.getParameter("beitreten"));
			String news = dnf.dnf(request.getParameter("news"));
			String mitglieder = dnf.dnf(request.getParameter("mitglieder"));
			String bearbeiten = dnf.dnf(request.getParameter("bearbeiten"));
			String senden = dnf.dnf(request.getParameter("senden"));
			String nachricht = request.getParameter("nachricht");
			String profil = dnf.dnf(request.getParameter("profil"));
			String erstellen = dnf.dnf(request.getParameter("erstellen"));
			String verlassen = dnf.dnf(request.getParameter("verlassen"));
			String gruppe_name = dnf.dnf(request.getParameter("gruppe_name"));
			String beschreibung = dnf.dnf(request.getParameter("beschreibung"));
			String gruppe_id = dnf.dnf(request.getParameter("gruppe_id"));
			String gruppe_id_nachricht = request.getParameter("gruppe_id_nachricht");
			String loeschen = dnf.dnf(request.getParameter("loeschen"));
			String[] interessen = request.getParameterValues("interesse");
			if (interessen == null)
		interessen = new String[0];

			//Weiterleitung View
			if (geheZu.equals("GruppeView.jsp")) {
		bb.defaultNachricht();
		response.sendRedirect("GruppeView.jsp");
			} else if (geheZu.equals("GruppeProfilView.jsp") || profil.equals("Gruppe anzeigen")) {
		gb.setGruppe_id(gruppe_id);
		gb.ueberpruefenStatusInGruppe();
		bb.defaultNachricht();
		response.sendRedirect("GruppeProfilView.jsp"); 	
			} else if (profil.equals("Profil")) { 				//Profil ohne ID
		response.sendRedirect("GruppeProfilView.jsp"); 
		bb.defaultNachricht();
			} else if (news.equals("News")) { //News ohne ID
		nb.setGruppe_id_nachricht(gruppe_id_nachricht);
		nb.nachrichtenAuslesenFuerGruppe();
		bb.defaultNachricht();
		response.sendRedirect("GruppeNewsView.jsp"); 
			} else if (mitglieder.equals("Mitglieder")) { 		//Mitglieder ohne ID
		response.sendRedirect("GruppeMitgliederView.jsp"); 
			} else if (profil.equals("Profil anzeigen")) {
		bb.defaultNachricht();
		response.sendRedirect("ProfilAppl.jsp?geheZu=ProfilView.jsp"); //benutzerID?
			} else if (bearbeiten.equals("Gruppe bearbeiten")) { //gruppeID muss nicht mitgegeben werden?! da man schon auf der Gruppe drauf war?!
		gb.setView("bearbeiten");
		bb.defaultNachricht();
		response.sendRedirect("GruppeErstellView.jsp");
			} else if (erstellen.equals("neue Gruppe erstellen")) {
		gb.setView("erstellen");
		bb.defaultNachricht();
		response.sendRedirect("GruppeErstellView.jsp");

			//Weiterleitung Bean
		
			} else if (loeschen.equals("Gruppe löschen")) { //gruppeID?
		gb.gruppeEntfernen();
		bb.gruppeGelöscht();
		//Weiterleitung Refresh
		response.sendRedirect("GruppeView.jsp");
			} else if (senden.equals("senden")) {
		//Nachricht im Newsfeed speichern
		//Nachricht im Newsfeed speichern
		if (gb.isMitglied() || gb.isErsteller()) {
		nb.setNachrichtentext(nachricht);
		nb.nachrichtInGruppe();
		nb.nachrichtenAuslesenFuerGruppe();
		bb.NachrichtGesendet();
		} else {
		bb.keinMitgliedGruppe();
		}
		//Weiterleitung GruppeNewsView.jsp
		response.sendRedirect("GruppeNewsView.jsp");
			} else if (speichern.equals("Gruppe speichern")) {
		gb.setGruppe_name(gruppe_name);
		gb.setBeschreibung(beschreibung);
		gb.setInteressenFuerDB(interessen);	
		gb.gruppeBearbeiten();
		bb.gruppeBearbeitet();
		//Weiterleitung GruppeNewsView.jsp
		response.sendRedirect("GruppeProfilView.jsp");
			} else if (erstellen.equals("Gruppe erstellen")) {
		gb.setGruppe_name(gruppe_name);
		gb.setBeschreibung(beschreibung);
		gb.setInteressenFuerDB(interessen);
		if (gb.ueberpruefeGruppeExistiert() == true) {
			bb.gruppenNameBereitsVorhanden();
			//Fehlermeldung Gruppenname ändern
			response.sendRedirect("GruppeErstellView.jsp");
		} else {
			//Gruppe in DB schreiben (erstellen)			
			try {
		
				gb.gruppeErstellen();
			
				//Weiterleitung auf Profil
				bb.gruppeErstellt();
				response.sendRedirect("GruppeProfilView.jsp");
			} catch (SQLException se) {
				se.printStackTrace();
				//Exception aufgetreten: Admin informieren
				bb.unbekannterFehler();
				response.sendRedirect("../willkommen/WillkommenView.jsp");
			}
		}
			} else if (beitreten.equals("Gruppe beitreten")) {
		gb.gruppeBeitreten();
		gb.setMitglied(true);
		//Weiterleitung Refresh
		bb.gruppeBeigetreten();
		response.sendRedirect("GruppeProfilView.jsp");
			} else if (verlassen.equals("Gruppe verlassen")) {
		gb.gruppeVerlassen();
		gb.setMitglied(false);
		bb.gruppeVerlassen();
		//Weiterleitung Refresh
		response.sendRedirect("GruppeProfilView.jsp");
			}

			//Weiterleitung Fehler/Angriffe
			else {
		response.sendRedirect("../willkommen/WillkommenAppl.jsp?geheZu=WillkommenView.jsp");
			}
	%>

</body>
</html>