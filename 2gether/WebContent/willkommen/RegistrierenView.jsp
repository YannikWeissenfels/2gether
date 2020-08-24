<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.ProfilBean"%>
<%@page import="de.hs_lu.beans.WillkommenBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registrieren</title>
<script type="text/javascript" src="../js/2gether.js"></script>
<link rel="stylesheet" type="text/css" href="../css/w3_css.css" />
<link rel="stylesheet" type="text/css"
	href="../css/w3_theme_blue_grey.css" />
<link rel='stylesheet' type="text/css"
	href="../css/family_open_sans.css" />
<link rel="stylesheet" href="../css/font-awesome-4.7.0/css/font-awesome.min.css">

<style>
html, body, h1, h2, h3, h4, h5 {
	font-family: "Open Sans", sans-serif
}
</style>
</head>

<body class="w3-theme-l5">
	<jsp:useBean id="profb" class="de.hs_lu.beans.ProfilBean"
		scope="session" />
	<jsp:useBean id="wb" class="de.hs_lu.beans.WillkommenBean"
		scope="session" />
	<jsp:useBean id="bb" class="de.hs_lu.beans.BenachrichtigungBean"
		scope="session" />

	<!-- Navbar -->
	<div class="w3-top">
		<div class="w3-bar w3-theme-d2 w3-left-align w3-large">
			<a
				class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-padding-large w3-hover-white w3-large w3-theme-d2"
				href="javascript:void(0);" onclick="openNav()"><i
				class="fa fa-bars"></i></a> <a href="#"
				class="w3-bar-item w3-button w3-padding-large w3-theme-d4">2gether</a>
		</div>
	</div>

	<!-- Navbar on small screens -->
	<div id="navDemo"
		class="w3-bar-block w3-theme-d2 w3-hide w3-hide-large w3-hide-medium w3-large">
		<a
			class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-padding-large w3-hover-white w3-large w3-theme-d2">
			2gether</a> <a href="#" class="w3-bar-item w3-button w3-padding-large">My
			Profile</a>
	</div>
	<br>

	<!-- Page Container: Linke erste Zelle: Profilansicht -->
	<div class="w3-container w3-content"
		style="max-width: 1400px; margin-top: 80px">
		<!-- The Grid -->
		<div class="w3-row">
			<!-- Left Column -->
			<div class="w3-col m2">
				<!-- Profile -->
				<div></div>
				<br> <br>
				<!-- End Left Column -->
			</div>

			<!-- Middle Column -->
			<div class="w3-col m7">

				<div class="w3-row-padding">
					<div class="w3-col m12">
						<div class="w3-card w3-round w3-white">

							<div class="w3-container w3-padding w3-center">


								<h3 class="">Werde ein Teil der Community!</h3>
								<h4 class="">Bitte registriere Dich</h4>
								<hr>
								<b><jsp:getProperty name="bb" property="infoNachricht" /><br />
									<b><jsp:getProperty name="bb" property="aktionNachricht" /></b>
								</b>
								<!-- CSS-Format anpassen-->
								<form action="./WillkommenAppl.jsp" method="get" onsubmit="return ueberpruefeRegistrieren(this)">
									<table class="regCSS">
										<tr>
											<td class="regLeft"></td>
											<td><input type="text" class="w3-center"
												placeholder="Vorname" name="vorname"
												value="<jsp:getProperty name='wb' property='vorname'/>"></td>
											<td class="regRight" id="vornameErrFeld"></td>
										</tr>
										<tr>
											<td class="regLeft"></td>
											<td><input type="text" class="w3-center"
												placeholder="Nachname" name="nachname"
												value="<jsp:getProperty name='wb' property='nachname'/>"></td>
											<td class="regRight" id="nachnameErrFeld"></td>
										</tr>
										<tr>
											<td class="regLeft"></td>
											<td class="w3-center">Geschlecht: <br><jsp:getProperty
													name="profb" property="geschlechtBenutzerOption" /></td>
											<td class="regRight"></td>
										</tr>
										<tr>
											<td class="regLeft"></td>
											<td class="w3-center"><jsp:getProperty name="wb"
													property="geburtstagAuswahl" /></td>
											<td class="regRight" id="geburtstagErrFeld"></td>
										</tr>
										<tr>
											<td class="regLeft"></td>
											<td><input type="text" class="w3-center"
												placeholder="E-Mailadresse" name="email"
												value="<jsp:getProperty name='wb' property='email'/>"></td>
											<td class="regRight" id="emailErrFeld"></td>
										</tr>
										<tr>
											<td class="regLeft"><b class="w3-button w3-circle w3-theme-l4" 
												title="Das Passwort muss mindestens 6 Zeichen enthalten.
Das Passwort muss mindestens einen Groß- und Kleinbuchstaben enthalten.">?</b></td>
											<td><input type="password" class="w3-center"
												placeholder="Passwort" name="passwort" value=""></td>
											<td class="regRight" id="passwortErrFeld"></td>
										</tr>
										<tr>
											<td class="regLeft"></td>
											<td><input type="password" class="w3-center"
												placeholder="Passwort bestätigen"
												name="bestaetigtesPasswort" value=""></td>
											<td class="regRight" id="bestaetigtesPasswortErrFeld"></td>
										</tr>
										<tr>
											<td colspan="3"><input type="submit"
												class="w3-button w3-theme-l4" name="login" value="zum Login"
												onclick="setZumLoginGeklickt()"> <input
												type="submit" class="w3-button w3-theme-l4"
												name="registrieren" value="Registrieren"
												onclick="setRegistrierenGeklickt()"></td>


										</tr>
									</table>
								</form>
							</div>
						</div>
					</div>
								</div>
				<br> <br> <br>
				<!-- End Middle Column -->
			</div>
			<!-- Right Column -->
			<div class="w3-col m2">
				<br>

				<!-- End Right Column -->
			</div>
			<!-- End Grid -->
		</div>
		<!-- End Page Container -->
	</div>
	<br>

<!-- Footer -->
	<div class="w3-bottom">
		<footer class="w3-container w3-theme-d5 w3-center">
		<p>
			2gether - Sei dabei und triff neue Leute auf Veranstaltungen in Deiner Region
		</p>
		</footer>
	</div>

</body>
</html>