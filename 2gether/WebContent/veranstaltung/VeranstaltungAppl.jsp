<%@page import="de.hs_lu.beans.NachrichtBean"%>
<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.VeranstaltungBean"%>
<%@page import="de.hs_lu.beans.DenullifizierBean"%>
<%@page import="java.sql.SQLException"%>
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
<jsp:useBean id="vb" class="de.hs_lu.beans.VeranstaltungBean" scope="session" />
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
		String veranstaltung_name = dnf.dnf(request.getParameter("veranstaltung_name"));
		String beschreibung = dnf.dnf(request.getParameter("beschreibung"));
		String veranstaltung_id = dnf.dnf(request.getParameter("veranstaltung_id"));
		String veranstaltung_id_nachricht = request.getParameter("veranstaltung_id_nachricht");
		String ort = dnf.dnf(request.getParameter("ort"));
		String datum = dnf.dnf(request.getParameter("datum"));
		String beginn = dnf.dnf(request.getParameter("beginn"));
		String ende = dnf.dnf(request.getParameter("ende"));
		String plz = dnf.dnf(request.getParameter("plz"));

		String loeschen = dnf.dnf(request.getParameter("loeschen"));
		String[] interessen = request.getParameterValues("interesse");
		if (interessen == null)
			interessen = new String[0];

		//Weiterleitung View
		if (geheZu.equals("VeranstaltungView.jsp")) {
			bb.defaultNachricht();
			response.sendRedirect("VeranstaltungView.jsp");
		} else if (geheZu.equals("VeranstaltungProfilView.jsp") || profil.equals("Veranstaltung anzeigen")) {
			vb.setVeranstaltung_id(veranstaltung_id);
			bb.defaultNachricht();
			response.sendRedirect("VeranstaltungProfilView.jsp"); //veranstaltungID?
		} else if (geheZu.equals("VeranstaltungNewsView.jsp")) {
			nb.nachrichtenAuslesenFuerVeranstaltungen();
			bb.defaultNachricht();
			response.sendRedirect("VeranstaltungNewsView.jsp"); //veranstaltungID?
		} else if (geheZu.equals("VeranstaltungMitgliederView.jsp")) {
			response.sendRedirect("VeranstaltungMitgliederView.jsp"); //veranstaltungID?
		} else if (profil.equals("Profil")) { 		
			bb.defaultNachricht();				//Profil ohne ID
			response.sendRedirect("VeranstaltungProfilView.jsp"); 
		} else if (news.equals("News")) { 					//News ohne ID
			nb.setVeranstaltung_id_nachricht(veranstaltung_id_nachricht);
			nb.nachrichtenAuslesenFuerVeranstaltungen();
			bb.defaultNachricht();
			response.sendRedirect("VeranstaltungNewsView.jsp"); 
		} else if (mitglieder.equals("Mitglieder")) { 		//Mitglieder ohne ID
			response.sendRedirect("VeranstaltungMitgliederView.jsp"); 
		} else if (profil.equals("Profil anzeigen")) {
			bb.defaultNachricht();
			response.sendRedirect("ProfilAppl.jsp?geheZu=ProfilView.jsp"); //benutzerID?
		} else if (bearbeiten.equals("Veranstaltung bearbeiten")) { //veranstaltungID muss nicht mitgegeben werden?! da man schon auf der Veranstaltung drauf war?!
			vb.setView("bearbeiten");
			bb.defaultNachricht();
			response.sendRedirect("VeranstaltungErstellView.jsp");
		} else if (erstellen.equals("neue Veranstaltung erstellen")) {
			vb.setView("erstellen");
			bb.defaultNachricht();
			response.sendRedirect("VeranstaltungErstellView.jsp");

		//Weiterleitung Bean
		
		} else if (loeschen.equals("Veranstaltung löschen")) { //veranstaltungID?
			vb.veranstaltungEntfernen();
			bb.veranstaltungGelöscht();
			//Weiterleitung Refresh
			response.sendRedirect("VeranstaltungView.jsp");
		} else if (senden.equals("senden")) {
			//Nachricht im Newsfeed speichern
			if (vb.isMitglied() || vb.isErsteller()) { 
			nb.setNachrichtentext(nachricht);
			nb.nachrichtInVeranstaltung();
			nb.nachrichtenAuslesenFuerVeranstaltungen();
			bb.NachrichtGesendet();
			}
			else {
			bb.keinMitgliedVeranstaltung();
			}
			//Weiterleitung VeranstaltungNewsView.jsp
			response.sendRedirect("VeranstaltungNewsView.jsp");
		} else if (speichern.equals("Veranstaltung speichern")) {
			vb.setVeranstaltung_name(veranstaltung_name);
			vb.setBeschreibung(beschreibung);
			vb.setBeginn(beginn);
			vb.setOrt(ort);
			vb.setDatum(datum);
			vb.setBeginn(beginn);
			vb.setEnde(ende);
			vb.setInteressenFuerDB(interessen);	
			vb.veranstaltungBearbeiten();
			bb.veranstaltungBearbeitet();
			//Weiterleitung VeranstaltungNewsView.jsp
			response.sendRedirect("VeranstaltungProfilView.jsp");
		} else if (erstellen.equals("Veranstaltung erstellen")) {
			
			vb.setVeranstaltung_name(veranstaltung_name);
			vb.setBeschreibung(beschreibung);
			vb.setBeginn(beginn);
			vb.setOrt(ort);
			vb.setDatum(datum);
			vb.setBeginn(beginn);
			vb.setEnde(ende);
			vb.setInteressenFuerDB(interessen);

			if (vb.ueberpruefeVeranstaltungExistiert() == true) {
		bb.veranstaltungsNameBereitsVorhanden();
		//Fehlermeldung Veranstaltungnname ändern
		response.sendRedirect("VeranstaltungErstellView.jsp");
			} else {
		//Veranstaltung in DB schreiben (erstellen)			
		try {
	
			vb.veranstaltungErstellen();
	
			//Weiterleitung auf Profil
			bb.veranstaltungErstellt();
			response.sendRedirect("VeranstaltungProfilView.jsp");
		} catch (SQLException se) {
			se.printStackTrace();
			//Exception aufgetreten: Admin informieren
			bb.unbekannterFehler();
			response.sendRedirect("../willkommen/WillkommenView.jsp");
		}
			}
		} else if (beitreten.equals("Veranstaltung beitreten")) {
			vb.veranstaltungBeitreten();
			vb.setMitglied(true);
			bb.veranstaltungBeigetreten();
			//Weiterleitung Refresh
			response.sendRedirect("VeranstaltungProfilView.jsp");
		} else if (verlassen.equals("Veranstaltung verlassen")) {
			vb.veranstaltungVerlassen();
			vb.setMitglied(false);
			//Weiterleitung Refresh
			bb.veranstaltungVerlassen();
			response.sendRedirect("VeranstaltungProfilView.jsp");
		}

		//Weiterleitung Fehler/Angriffe
		else {
			response.sendRedirect("../willkommen/WillkommenAppl.jsp?geheZu=WillkommenView.jsp");
		}
	%>

</body>
</html>