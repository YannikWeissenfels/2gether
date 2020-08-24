<%@page import="de.hs_lu.beans.NachrichtBean"%>
<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.StartBean"%>
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
<jsp:useBean id="sb" class="de.hs_lu.beans.StartBean" scope="session" />
<jsp:useBean id="bb" class="de.hs_lu.beans.BenachrichtigungBean" scope="session" />
<jsp:useBean id="nb" class="de.hs_lu.beans.NachrichtBean" scope="session" />
	<%
		request.setCharacterEncoding("UTF-8");
		//Einlesen & Denullify			
		String geheZu = dnf.dnf(request.getParameter("geheZu"));
			

			//Auswertung
			if(geheZu.equals("StartView.jsp")){
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
			response.sendRedirect("StartView.jsp");	
				
			}else{
		response.sendRedirect("../willkommen/WillkommenAppl.jsp?geheZu=WillkommenView.jsp");
			}
	%>

</body>
</html>