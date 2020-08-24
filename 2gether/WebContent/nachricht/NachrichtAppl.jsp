<%@page import="de.hs_lu.beans.DenullifizierBean"%>
<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.ProfilBean"%>
<%@page import="de.hs_lu.beans.NachrichtBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="dnf" class="de.hs_lu.beans.DenullifizierBean"
		scope="session" />
	<jsp:useBean id="nb" class="de.hs_lu.beans.NachrichtBean"
		scope="session" />
	<jsp:useBean id="profb" class="de.hs_lu.beans.ProfilBean"
		scope="session" />
	<jsp:useBean id="bb" class="de.hs_lu.beans.BenachrichtigungBean"
		scope="session" />
	<%
		request.setCharacterEncoding("UTF-8");

		//Einlesen & Denullify			
		String geheZu = dnf.dnf(request.getParameter("geheZu"));
		String senden = dnf.dnf(request.getParameter("senden"));
		String nachricht = request.getParameter("nachricht");
		String empfaenger_id = dnf.dnf(request.getParameter("empfaenger_id"));
		String nachrichtallebenutzer = dnf.dnf(request.getParameter("nachrichtallebenutzer"));
		String neuenachricht = dnf.dnf(request.getParameter("neuenachricht"));
		String vornamenachricht = dnf.dnf(request.getParameter("vornamenachricht"));
		String nachnamenachricht = dnf.dnf(request.getParameter("nachnamenachricht"));

		//Auswertung
		if (geheZu.equals("NachrichtView.jsp")) {
			nb.nachrichtGelesen();
			nb.benutzerAuslesenMitDenenGeschriebenWird();
			if (empfaenger_id.equals("")) {
				empfaenger_id = "0";
				nb.setEmpfaenger_id(empfaenger_id);
			} else {
				nb.setEmpfaenger_id(empfaenger_id);
				nb.nachrichtenAuslesenFuerBenutzer();
			}
			bb.defaultNachricht();
			response.sendRedirect("NachrichtView.jsp");

		} else if (senden.equals("senden")) {
			System.out.println("Nachricht: " + nachricht);
			nb.setNachnamenachricht(nachnamenachricht);
			nb.setVornamenachricht(vornamenachricht);
			if (!nb.benutzeridAuslesenVonBenutzerNeueNachricht() && nb.getEmpfaenger_id() == 0) {
				bb.benutzerExistiertNicht();
			} else {
				nb.setNachrichtentext(nachricht);
				nb.nachrichtAnEinenBenutzer();
				bb.NachrichtGesendet();
				nb.nachrichtenAuslesenFuerBenutzer();
				nb.benutzerAuslesenMitDenenGeschriebenWird();
				nb.nachrichtGelesen();
			}
			//Weiterleitung Refresh
			response.sendRedirect("NachrichtView.jsp");
		} else if (neuenachricht.equals("Neue Nachricht")) {
			bb.defaultNachricht();
			nb.setEmpfaenger_id("0");
			response.sendRedirect("NachrichtView.jsp");
		} else {
			response.sendRedirect("../willkommen/WillkommenAppl.jsp?geheZu=WillkommenView.jsp");
		}
	%>

</body>
</html>