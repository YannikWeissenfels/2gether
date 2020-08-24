<%@page import="de.hs_lu.beans.BenachrichtigungBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Einloggen</title>
<link rel="stylesheet" type="text/css" href="../css/w3_css.css" />
<link rel="stylesheet" type="text/css"
	href="../css/w3_theme_blue_grey.css" />
<link rel='stylesheet' type="text/css"
	href="../css/family_open_sans.css" />
<link rel="stylesheet" href="../css/font-awesome-4.7.0/css/font-awesome.min.css">



<script type="text/javascript" src="../js/2gether.js"></script>
<style>
html, body, h1, h2, h3, h4, h5 {
	font-family: "Open Sans", sans-serif
}
</style>

</head>
<body class="w3-theme-l5">
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
	</br>
	</br>
	</br>

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
								<h3 class="">Schön, dass Du wieder da bist!</h3>
								<h4 class="">Bitte melde Dich an</h4>
								<hr>
								<!--<p contenteditable="true" class="w3-border w3-padding">Status: Feeling Blue</p> -->
								<!-- CSS-Format anpassen-->
								<form action="WillkommenAppl.jsp" method="get" onsubmit="return ueberpruefeLogin(this)">
									<table class="regCSS">
										<tr>
											<td colspan="3"><b><jsp:getProperty name="bb"
														property="infoNachricht" /><br /> <b><jsp:getProperty
															name="bb" property="aktionNachricht" /></b> </b></td>
										<tr>
											<td class="regLeft"></td>
											<td><input type="text" placeholder="E-Mail eingeben"
												class="w3-center" name="email" value=""></td>
											<td class="regRight" id="emailErrFeld" "></td>
										</tr>
										<tr>
											<td class="regLeft"></td>
											<td><input type="password" class="w3-center"
												placeholder="Passwort eingeben" name="passwort" value=""></td>
											<td class="regRight" id="passwortErrFeld" "></td>
										</tr>
										<tr>
											<td colspan="3"><input type="submit"
												class="w3-button w3-theme-l4" name="login" value="Login" onclick="setLoginGeklickt()">
												<input type="submit" class="w3-button w3-theme-l4"
												name="registrieren" value="zum Registrieren" onclick="setZumRegistrierenGeklickt()"></td>
										</tr>
									</table>
									<!-- <label>
   			   <input type="checkbox" name="remember"> Remember me
   			 </label> -->
								</form>

							</div>
							<!--   <form action="./WillkommenAppl.jsp" method="get">
  			<input type="submit" class="w3-button w3-yellow" name="zurueck" value="Zurück">
             </form>-->
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


</body>
</html>