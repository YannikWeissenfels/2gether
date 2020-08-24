<%@page import="javax.sound.midi.SysexMessage"%>
<%@page import="de.hs_lu.beans.NachrichtBean"%>
<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.GruppeBean"%>
<%@page import="de.hs_lu.beans.StartBean"%>
<%@page import="de.hs_lu.beans.DenullifizierBean"%>
<%@page import="de.hs_lu.beans.ProfilBean"%>
<%@page import="java.sql.SQLException"%>
<%@ page import="de.hs_lu.beans.WillkommenBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<jsp:useBean id="dnf" class="de.hs_lu.beans.DenullifizierBean" scope="session" />
<jsp:useBean id="wb" class="de.hs_lu.beans.WillkommenBean" scope="session" />
<jsp:useBean id="profb" class="de.hs_lu.beans.ProfilBean" scope="session" />
<jsp:useBean id="sb" class="de.hs_lu.beans.StartBean" scope="session" />
<jsp:useBean id="bb" class="de.hs_lu.beans.BenachrichtigungBean" scope="session" />
<jsp:useBean id="nb" class="de.hs_lu.beans.NachrichtBean" scope="session" />
<jsp:useBean id="gb" class="de.hs_lu.beans.GruppeBean" scope="session" />
<jsp:useBean id="vb" class="de.hs_lu.beans.VeranstaltungBean" scope="session" />

<%		
		request.setCharacterEncoding("UTF-8");

		//Einlesen & Denullify
		String benutzer_id = request.getParameter("benutzer_id");
		String vorname = request.getParameter("vorname");
		String nachname = request.getParameter("nachname");
		String geschlecht = request.getParameter("geschlecht");
		String geburtsTag = request.getParameter("geburtsTag");
		String geburtsMonat = request.getParameter("geburtsMonat");
		String geburtsJahr = request.getParameter("geburtsJahr");
		String wohnort = request.getParameter("wohnort");
		String email = request.getParameter("email");
		String passwort = request.getParameter("passwort");
		String altesPasswort = request.getParameter("altesPasswort");
		String neuesPasswort = request.getParameter("neuesPasswort");
		String bestaetigtesPasswort = request.getParameter("bestaetigtesPasswort");
		String registrieren = dnf.dnf(request.getParameter("registrieren"));
		String geheZu = dnf.dnf(request.getParameter("geheZu"));
		String login = dnf.dnf(request.getParameter("login"));
		String logout = dnf.dnf(request.getParameter("logout"));
		String speichern = dnf.dnf(request.getParameter("speichern"));
		String entfernen = dnf.dnf(request.getParameter("accountEntfernen"));

		//Weiterleitungen View
		if (geheZu.equals("RegistrierenView.jsp")) {
			//Weiterleitung zur RegistrierenView.jsp
			bb.defaultNachricht();
			response.sendRedirect("RegistrierenView.jsp");
		} else if (geheZu.equals("WillkommenView.jsp")) {
			//Weiterleitung zur WillkommenView.jsp
			bb.defaultNachricht();
			response.sendRedirect("WillkommenView.jsp");
		} else if (geheZu.equals("LoginView.jsp")) {
			//Weiterleitung zur LoginView.jsp
			bb.defaultNachricht();
			response.sendRedirect("LoginView.jsp");
		} else if (geheZu.equals("EinstellungenView.jsp")) {
			bb.defaultNachricht();	
			//Weiterleitung zur OptionenView.jsp
			response.sendRedirect("EinstellungenView.jsp");
		} else if (registrieren.equals("zum Registrieren")) {
			//Weiterleitung zur RegistrierenView.jsp
			bb.defaultNachricht();
			response.sendRedirect("RegistrierenView.jsp");
		} else if (login.equals("zum Login")) {
			//Weiterleitung zur LoginView.jsp
			bb.defaultNachricht();
			response.sendRedirect("LoginView.jsp");
		}

		//Weiterleitungen Bean
		else if (registrieren.equals("Registrieren")) {

			//Aktion registrieren: Registrieren: Benutzer in die DB schreiben
			wb.setVorname(vorname);
			wb.setNachname(nachname);
			wb.setGeschlecht(geschlecht);
			wb.setGeburtsTag(geburtsTag);
			wb.setGeburtsMonat(geburtsMonat);
			wb.setGeburtsJahr(geburtsJahr);
			wb.setWohnort(wohnort);
			wb.setEmail(email);
			wb.setPasswort(passwort);
			try {
				boolean eingefuegt = wb.benutzerAnlegenWennNichtExistiert();
				if (eingefuegt) {
					//Weiterleitung bei erfolgreicher Registrierung zur ProfEditView.jsp
					profb.persoenlicheDatenFuerEigenesProfil();
					response.sendRedirect("../profil/ProfilAppl.jsp?geheZu=ProfilBearbeitenView.jsp");
				} else {
					//Rückmeldung: gibt's schon
					bb.doppelteEmail();
					//Weiterleiten: Zurück zur RegistrierenView.jsp
					response.sendRedirect("./RegistrierenView.jsp");
				}
			} catch (SQLException se) {
				se.printStackTrace();
				//Exception aufgetreten: Admin informieren
				bb.unbekannterFehler();
				response.sendRedirect("./WillkommenView.jsp");
			}
		} else if (login.equals("Login")) {
			wb.setEmail(email);
			wb.setPasswort(passwort);
			try {
				boolean gefunden = wb.ueberpruefenEMailPasswort();
				if (gefunden) {
					wb.setIstEingeloggt(true);
					profb.freundschaftsanfragenAusDBAuslesen();
					sb.allePersonenmatchings();
					sb.alleVeranstaltungsmatchings();
					sb.alleGruppenmatchings();
					nb.neueNachrichten();
					if (!nb.neueNachrichten() && !profb.freundschaftsanfragenAusDBAuslesen()) {
						bb.keineNeuenBenachrichtungen(); }
					else {
						bb.defaultNachricht();
					}
					//Weiterleitung bei erfolgreicher Anmeldung zur HomeView.jsp
					response.sendRedirect("../start/StartAppl.jsp?geheZu=StartView.jsp");
				} else {
					//Login hat nicht geklappt
					bb.falscheAnmeldedaten();
					response.sendRedirect("./LoginView.jsp");
				}
			} catch (SQLException se) {
				se.printStackTrace();
				//Exception aufgetreten: Admin informieren
				bb.unbekannterFehler();
				response.sendRedirect("./WillkommenView.jsp");
			}
		} else if (logout.equals("Logout")) {
			wb.logout();
			gb.setErsteller(false);
			gb.setMitglied(false);
			gb.setGruppe_id(0);
			vb.setErsteller(false);
			vb.setMitglied(false);
			vb.setVeranstaltung_id(0);
			bb.logoutErfolgreich();
			//Weiterleitung zur LoginView.jsp
			response.sendRedirect("LoginView.jsp");
		} else if (speichern.equals("speichern")) {
			//Passwort ändern
			wb.setAltesPasswort(altesPasswort);
			wb.setNeuesPasswort(neuesPasswort);
			wb.setBestaetigtesPasswort(bestaetigtesPasswort);
			if (wb.ueberpruefePasswort()) {
				wb.bearbeitenPasswort();
				bb.passwortänderungErfolgreich();
		
			} else {

				bb.passwortänderungNichtErfolgreich();
				}
			//Weiterleitung zur OptionenView.jsp
			response.sendRedirect("EinstellungenView.jsp");
		} else if (entfernen.equals("Account entfernen")){
			wb.accountEntfernen();
			gb.setErsteller(false);
			gb.setMitglied(false);
			gb.setGruppe_id(0);
			vb.setErsteller(false);
			vb.setMitglied(false);
			vb.setVeranstaltung_id(0);
			bb.benutzerGeloescht();
			//Weiterleitung zur WillkommenView.jsp
			response.sendRedirect("WillkommenView.jsp");
		}

		//Weiterleitungen Fehler/Angriffe
		else {
			//Weiterleitung zur WillkommenView.jsp
			bb.defaultNachricht();
			response.sendRedirect("WillkommenView.jsp");
		}
	%>
</body>
</html>