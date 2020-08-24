<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Einstellungen</title>
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
	<!--Beans -->
	<jsp:useBean id="mb" class="de.hs_lu.beans.MenuBean" scope="session" />

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
	<br>
	<br>
	<br>
	<br>
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

			<!-- Content -->

			<!-- Middle Column -->
			<div class="w3-col m7">
				<div class="w3-row-padding">
					<div class="w3-col m12">
						<div class="w3-card w3-round w3-white">
							<div class="w3-container w3-padding">

								<!-- Content -->
								<form action="WillkommenAppl.jsp" method="get">

									<table class="w3-table">
										<tr>
											<td><h5>Profil</h5></td>
											<td><a
												href="../profil/ProfilAppl.jsp?geheZu=ProfilBearbeitenView.jsp"
												class="w3-button w3-theme-l4">Profil bearbeiten</a></td>
										</tr>
										<tr>
											<td colspan="3"><hr></td>
										</tr>
										<tr>
											<td><h5>Account</h5></td>
											<td>aktuelles Passwort:</td>
											<td><input type="password" name="altesPasswort" value=""></td>
										</tr>
										<tr>
											<td></td>
											<td>neues Passwort:</td>
											<td><input type="password" name="neuesPasswort" value=""></td>
										</tr>
										<tr>
											<td></td>
											<td>Passwort bestätigen:</td>
											<td><input type="password" name="bestaetigtesPasswort"
												value=""></td>
										</tr>
										<tr>
											<td></td>
											<td colspan="2"><hr></td>
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td><input type="submit" class="w3-button w3-theme-l4"
												name="accountEntfernen" value="Account entfernen"></td>
										</tr>
										<tr>
											<td><input type="submit" class="w3-button  w3-theme-l4"
												name="speichern" value="speichern"></td>
										</tr>
									</table>
								</form>
							</div>
						</div>
					</div>
				</div>
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


</body>
</html>