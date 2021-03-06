<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@page import="de.hs_lu.beans.MenuBean"%>
<%@page import="de.hs_lu.beans.ProfilBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Profil</title>

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
	<form action="ProfilAppl.jsp">

		<!--Beans -->
		<jsp:useBean id="mb" class="de.hs_lu.beans.MenuBean" scope="session" />
		<jsp:useBean id="profb" class="de.hs_lu.beans.ProfilBean"
			scope="session" />
		<jsp:useBean id="bb" class="de.hs_lu.beans.BenachrichtigungBean"
			scope="session" />

		<!-- Navbar -->
		<div class="w3-top">
			<div class="w3-bar w3-theme-d2 w3-left-align w3-large">

				<a
					class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-padding-large w3-hover-white w3-large w3-theme-d2">
					<jsp:getProperty name="mb" property="menu" /></a>

				<!--<a href="#" class="w3-bar-item w3-button w3-hide-small w3-right w3-padding-large w3-hover-white" title="My Account">
    <img src="/w3images/avatar2.png" class="w3-circle" style="height:23px;width:23px" alt="Avatar">
  </a> -->
			</div>
		</div>
		<!-- Navbar on small screens -->
		<div id="navDemo"></div>
		<br> <br> <br> <br> <br>

		<!-- Page Container: Linke erste Zelle: Profilansicht -->
		<div class="w3-container w3-content"
			style="max-width: 1400px; margin-top: 80px">
			<!-- The Grid -->
			<div class="w3-row">
				<!-- Left Column -->
				<div class="w3-col m3">
					<!-- Profile -->
					<div>
						<jsp:getProperty name="profb" property="buttonsFuerFreundschaft" /><br /></br>
						<jsp:getProperty name="profb" property="HTMLNachrichtSchreiben" />

					</div>
					<br> <br>
					<!-- End Left Column -->
				</div>

				<!-- Content -->

				<!-- Middle Column -->
				<div class="w3-col m7">

					<div class="w3-row-padding">
						<div class="w3-col m12">
							<div class="w3-card w3-round w3-white">
								<div class="w3-container w3-padding w3-left-align">
									<div>

										<!-- Content -->

										<b><jsp:getProperty name="bb" property="infoNachricht" /></b>

										<table>
											<tr>
												<td width="80%">
													<table>
														<tr>
															<td></td>
															<td><h5>Persönliche Daten</h5></td>
														</tr>

														<tr>
															<td></td>
															<td>Vorname</td>
															<td><jsp:getProperty name="profb" property="vorname" /></td>
														</tr>
														<tr>
															<td></td>
															<td>Nachname</td>
															<td><jsp:getProperty name="profb"
																	property="nachname" /></td>
														</tr>
														<tr>
															<td></td>
															<td>Geburtstag</td>
															<td><jsp:getProperty name="profb"
																	property="geburtstag" /></td>
														</tr>
														<tr>
															<td></td>
															<td>Alter</td>
															<td><jsp:getProperty name="profb"
																	property="alterFuerBenutzer" /></td>
														</tr>
														<tr>
															<td></td>
															<td>Geschlecht</td>
															<td><jsp:getProperty name="profb"
																	property="geschlechtBenutzer" /></td>
														</tr>
														<tr>
															<td></td>
															<td>Wohnort</td>
															<td><jsp:getProperty name="profb" property="wohnort" /></td>
														</tr>
														<tr>
															<td></td>
															<td>Beziehungsstatus</td>
															<td><jsp:getProperty name="profb"
																	property="beziehungsstatus" /></td>
														</tr>
														<tr>
															<td></td>
															<td>Kurzprofil</td>
															<td><jsp:getProperty name="profb"
																	property="kurzprofil" /></td>
														</tr>
														<tr>
															<td></td>
															<td><h5>Matching-Einstellungen</h5></td>
														</tr>

														<tr>
															<td></td>
															<td>Alter der Freunde</td>
															<td><jsp:getProperty name="profb"
																	property="mindestalter" /> bis <jsp:getProperty
																	name="profb" property="maximalalter" /> Jahre</td>
														</tr>
														<tr>
															<td></td>
															<td>Geschlecht der Freunde</td>
															<td><jsp:getProperty name="profb"
																	property="geschlechtPerson" /></td>

														</tr>
														<tr>
															<td></td>
															<td><h5>Städteinteressen</h5></td>
														</tr>
														<tr>
															<td></td>
															<td></td>
															<td><jsp:getProperty name="profb"
																	property="HTMLStaedteVonBenutzer" /></td>

														</tr>

														<tr>
															<td></td>
															<td><h5>Interessen</h5></td>
														</tr>
														<tr>
															<td></td>
															<td></td>
															<td><jsp:getProperty name="profb"
																	property="HTMLInteressenVonBenutzer" /></td>

														</tr>
													</table>
												</td>
												<td class="regRight"><div
														class="w3-container w3-padding w3-right-align">
														<jsp:getProperty name="profb" property="HTMLBildFreunde" />
													</div></td>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
					</br> </br> </br>
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
		<script>
			// Accordion
			function myFunction(id) {
				var x = document.getElementById(id);
				if (x.className.indexOf("w3-show") == -1) {
					x.className += " w3-show";
					x.previousElementSibling.className += " w3-theme-d1";
				} else {
					x.className = x.className.replace("w3-show", "");
					x.previousElementSibling.className = x.previousElementSibling.className
							.replace(" w3-theme-d1", "");
				}
			}

			// Used to toggle the menu on smaller screens when clicking on the menu button
			function openNav() {
				var x = document.getElementById("navDemo");
				if (x.className.indexOf("w3-show") == -1) {
					x.className += " w3-show";
				} else {
					x.className = x.className.replace(" w3-show", "");
				}
			}
		</script>

	</form>
</body>
</html>