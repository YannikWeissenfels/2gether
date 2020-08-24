function setZumLoginGeklickt() {
	var myForm = document.forms[0];
	myForm.buttonClicked = "zum Login";
}
function setVeranstaltungSpeichernGeklickt() {
	var myForm = document.forms[0];
	myForm.buttonClicked = "speichern";
}

function setVeranstaltungErstellenGeklickt() {
	var myForm = document.forms[0];
	myForm.buttonClicked = "erstellen";
}
function setRegistrierenGeklickt() {
	var myForm = document.forms[0];
	myForm.buttonClicked = "Registrieren";
}
function setZumRegistrierenGeklickt() {
	var myForm = document.forms[0];
	myForm.buttonClicked = "zum Registrieren";
}
function setLoginGeklickt() {
	var myForm = document.forms[0];
	myForm.buttonClicked = "Login";
}
function setProfilSpeichernGeklickt() {
	var myForm = document.forms[0];
	myForm.buttonClicked = "Speichern";
}

function ueberpruefeVeranstaltung(myForm) {
	// alert(myForm.buttonClicked);
	var myOrt = myForm.ort.value;
	var myName = myForm.veranstaltung_name.value;
	var ortErr = ueberpruefeStadt(myOrt);
	var nameErr = ueberpruefeName(myName);

	document.getElementById("ortErrFeld").innerHTML = ortErr;
	document.getElementById("nameErrFeld").innerHTML = nameErr;

	var fehlerErr = ortErr + nameErr;
	if (fehlerErr)
		return false;
	else if (!fehlerErr)
		return true;

}

function ueberpruefeRegistrieren(myForm) {
	// alert(myForm.buttonClicked);
	if (myForm.buttonClicked == "zum Login") {
		return true;
	}
	var myVorname = myForm.vorname.value;
	var myNachname = myForm.nachname.value;
	var myEmail = myForm.email.value;
	var myPasswort = myForm.passwort.value;
	var myBestaetigtesPasswort = myForm.bestaetigtesPasswort.value;
	var myGeburtsTag = parseInt(myForm.geburtsTag.value);
	var myGeburtsMonat = parseInt(myForm.geburtsMonat.value);
	var myGeburtsJahr = myForm.geburtsJahr.value;

	var vornameErr = ueberpruefeVorname(myVorname);
	var nachnameErr = ueberpruefeNachame(myNachname);
	var emailErr = ueberpruefeEmail(myEmail);
	var passwortErr = ueberpruefePasswort(myPasswort);
	var bestaetigtesPasswortErr = ueberpruefeBestaetigtesPasswort(
			myBestaetigtesPasswort, myPasswort);
	var geburtstagErr = ueberpruefeGeburtstag(myGeburtsTag, myGeburtsMonat,
			myGeburtsJahr);

	document.getElementById("vornameErrFeld").innerHTML = vornameErr;
	document.getElementById("nachnameErrFeld").innerHTML = nachnameErr;
	document.getElementById("emailErrFeld").innerHTML = emailErr;
	document.getElementById("passwortErrFeld").innerHTML = passwortErr;
	document.getElementById("bestaetigtesPasswortErrFeld").innerHTML = bestaetigtesPasswortErr;
	document.getElementById("geburtstagErrFeld").innerHTML = geburtstagErr;

	var fehlerErr = vornameErr + nachnameErr + emailErr + passwortErr
			+ bestaetigtesPasswortErr + geburtstagErr;
	if (fehlerErr) {
		myForm.passwort.value = "";
		myForm.bestaetigtesPasswort.value = "";
		return false;
	} else
		return true;
}

function ueberpruefeStadt(myOrt) {
	if (myOrt == "--Bitte auswählen--") {
		return "Bitte wähle einen Ort aus";
	} else {
		return "";
	}
}
function ueberpruefeName(myName) {
	if (!myName) {
		return "Der Veranstaltungsname darf nicht leer sein.";
	} else {
		return "";
	}
}
function ueberpruefePasswort(myPasswort) {
	if (!myPasswort)
		return "Das Passwort darf nicht leer sein";
	if (myPasswort.length < 6)
		return "Das Passwort muss mindestens 6 Zeichen enthalten";
	if (myPasswort == myPasswort.toLowerCase()
			|| myPasswort == myPasswort.toUpperCase())
		return "Das Passwort muss mindestens einen Groß- und Kleinbuchstaben enthalten";
	return "";

}
function ueberpruefeLogin(myForm) {
	// alert(myForm.buttonClicked);
	if (myForm.buttonClicked == "zum Registrieren")
		return true;
	var myEmail = myForm.email.value;
	var myPasswort = myForm.passwort.value;

	var passwortUeberpruefung = true; // true: Passwort soll auf
	// Passwortanforderungen überprüft
	// werden; false: Passwort soll nicht
	// überprüft werden

	if (passwortUeberpruefung) {
		var emailErr = ueberpruefeEmail(myEmail);
		var passwortErr = ueberpruefePasswort(myPasswort);

		document.getElementById("emailErrFeld").innerHTML = emailErr;
		document.getElementById("passwortErrFeld").innerHTML = passwortErr;

		var fehlerErr = emailErr + passwortErr;
		if (fehlerErr) {
			return false;
		} else {
			return true;
		}
	} else {
		var emailErr = ueberpruefeEmail(myEmail);

		document.getElementById("emailErrFeld").innerHTML = emailErr;

		var fehlerErr = emailErr
		if (fehlerErr) {
			return false;
		} else {
			return true;
		}
	}
}

function ueberpruefeInteresseAlter(myForm) {
	// alert(myForm.buttonClicked);
	var myMindestalter = parseInt(myForm.mindestalter.value);
	var myMaximalalter = parseInt(myForm.maximalalter.value);
	var alterErr;
	if (myMindestalter > myMaximalalter) {
		alterErr = "Das Mindestalter darf nicht größer sein als das Maximalalter";
	} else {
		alterErr = "";
	}
	document.getElementById("alterErrFeld").innerHTML = alterErr;

	var fehlerErr = alterErr;
	if (fehlerErr) {
		return false;
	} else {
		return true;
	}

}

function ueberpruefeVorname(myVorname) {
	if (!myVorname) {
		return "Der Vorname darf nicht leer sein";
	} else if (myVorname.indexOf(" ") >= 0) {
		return "Der Vorname darf keine Leerzeichen enthalten";
	} else {
		return "";
	}
}

function ueberpruefeNachame(myNachname) {
	if (!myNachname) {
		return "Der Nachname darf nicht leer sein";
	} else if (myNachname.indexOf(" ") >= 0) {
		return "Der Nachname darf keine Leerzeichen enthalten";
	} else {
		return "";
	}
}

function ueberpruefeEmail(myEmail) {
	if (!myEmail)
		return "Die E-Mail-Adresse darf nicht leer sein";
	if (myEmail.indexOf('@') == -1)
		return "Die E-Mail-Adresse muss ein @-Zeichen enthalten";
	return "";
}

function ueberpruefeBestaetigtesPasswort(myBestaetigtesPasswort, myPasswort) {
	if (!myBestaetigtesPasswort)
		return "Das Passwort muss bestätigt werden";
	if (myBestaetigtesPasswort == myPasswort) {
		return "";
	} else {
		return "Die Passwörter stimmen nicht überein";
	}
}

function ueberpruefeGeburtstag(myGeburtsTag, myGeburtsMonat, myGeburtsJahr) {
	if (myGeburtsTag == "Tag" || myGeburtsMonat == "Monat"
			|| myGeburtsJahr == "Jahr") {
		return "Bitte wähle deinen Geburtstag aus.";
	}
	var tageInMonat;
	if ((myGeburtsMonat < 8 && myGeburtsMonat % 2 == 1) || myGeburtsMonat > 7
			&& myGeburtsMonat % 2 == 0) {
		tageInMonat = 31;
	} else if (myGeburtsMonat == 2) {
		if (myGeburtsJahr % 4 == 0
				&& (myGeburtsJahr % 100 != 0 || myGeburtsJahr % 400 == 0)) {
			tageInMonat = 29;
		} else {
			tageInMonat = 28;
		}
	} else {
		tageInMonat = 30;
	}
	if (myGeburtsTag > tageInMonat) {
		return "Das Datum " + myGeburtsTag + "." + myGeburtsMonat + "."
				+ myGeburtsJahr
				+ " gibt es nicht. Überprüfe nochmals deine Auswahl.";
	}
	return "";
}
