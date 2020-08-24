package de.hs_lu.beans;

public class BenachrichtigungBean {

	String infoNachricht;
	String aktionNachricht;

	public BenachrichtigungBean() {
		this.defaultNachricht();
	}

	public void defaultNachricht() {
		infoNachricht = "";
		aktionNachricht = "";
	}

	// Start
	public void keineNeuenBenachrichtungen() {
		infoNachricht = "Du hast keine neuen Benachrichtigungen.";
	}
	
	
	// Registrierung
	public void doppelteEmail() {
		infoNachricht = "Diese E-Mail existiert bereits.";
		aktionNachricht = "Bitte verwende eine andere E-Mailadresse.";
	}

	// Login
	public void falscheAnmeldedaten() {
		infoNachricht = "E-Mailadresse oder Passwort ist nicht korrekt.";
		aktionNachricht = "Bitte versuche es erneut.";
	}
	
//Logout
	public void logoutErfolgreich() {
		infoNachricht = "Du hast dich erfolgreich abgemeldet.";
	}
	

	// Profil
	public void daten�nderungErfolgreich() {
		infoNachricht = "Deine Daten wurden erfolgreich ge�ndert.";
	}

	public void passwort�nderungErfolgreich() {
		infoNachricht = "Dein Passwort wurde erfolgreich ge�ndert.";
	}

	public void passwort�nderungNichtErfolgreich() {
		infoNachricht = "Passwort stimmt nicht �berein.";
		aktionNachricht = "Bitte versuche es erneut.";
	}

	public void benutzerGeloescht() {
		infoNachricht = "Dein Benutzer wurde gel�scht.";
	}

	// Freunde
	public void freundschaftsanfrageGesendet() {
		infoNachricht = "Deine Freundschaftsanfrage wurde erfolgreich gesendet.";
	}

	public void anfrageZurueckgezogen() {
		infoNachricht = "Du hast die Freundschaftsanfrage zur�ckgezogen.";
	}

	public void freundAngenommen() {
		infoNachricht = "Der Freund wurde deiner Freundesliste hinzugef�gt.";
	}

	public void freundAbgelehnt() {
		infoNachricht = "Du hast die Freundschaftsanfrage abgelehnt.";
	}

	public void freundGeloescht() {
		infoNachricht = "Der Freund wurde aus deiner Freundesliste entfernt.";
	}

	// Nachricht
	public void NachrichtGesendet() {
		infoNachricht = "Deine Nachricht wurde erfolgreich gesendet.";
	}

	public void keinMitgliedGruppe() {
		infoNachricht = "Du bist kein Mitglied in dieser Gruppe.";
		aktionNachricht = "Tritt der Gruppe bei und versuche es erneut.";
	}

	public void keinMitgliedVeranstaltung() {
		infoNachricht = "Du bist kein Mitglied in dieser Veranstaltung.";
		aktionNachricht = "Tritt der Veranstaltung bei und versuche es erneut.";
	}

	public void benutzerExistiertNicht() {
		infoNachricht = "Dieser Benutzer existiert nicht.";
		aktionNachricht = "Bitte versuche einen anderen Benutzer.";
	}

	// Gruppe

	public void gruppeErstellt() {
		infoNachricht = "Deine Gruppe wurde erfolgreich erstellt.";
	}

	public void gruppeBearbeitet() {
		infoNachricht = "Deine Gruppe wurde erfolgreich bearbeitet.";
	}

	public void gruppeGel�scht() {
		infoNachricht = "Deine Gruppe wurde erfolgreich gel�scht.";
	}

	public void gruppeBeigetreten() {
		infoNachricht = "Du bist der Gruppe erfolgreich beigetreten.";
	}

	public void gruppeVerlassen() {
		infoNachricht = "Du hast die Gruppe erfolgreich verlassen.";
	}
	
	public void gruppenNameBereitsVorhanden() {
		infoNachricht = "Dieser Gruppenname ist bereits vergeben.";
		aktionNachricht = "Bitte w�hle einen anderen Gruppennamen.";
	}

	// Veranstaltungen

	public void veranstaltungErstellt() {
		infoNachricht = "Deine Veranstaltung wurde erfolgreich erstellt.";
	}

	public void veranstaltungBearbeitet() {
		infoNachricht = "Deine Veranstaltung wurde erfolgreich bearbeitet.";
	}

	public void veranstaltungGel�scht() {
		infoNachricht = "Deine Veranstaltung wurde erfolgreich gel�scht.";
	}

	public void veranstaltungBeigetreten() {
		infoNachricht = "Du bist der Veranstaltung erfolgreich beigetreten.";
	}

	public void veranstaltungVerlassen() {
		infoNachricht = "Du hast die Veranstaltung erfolgreich verlassen.";
	}
	
	public void veranstaltungsNameBereitsVorhanden() {
		infoNachricht = "Dieser Veranstaltungsname ist bereits vergeben.";
		aktionNachricht = "Bitte w�hle einen anderen Veranstaltungsnamen.";
	}


	// Unbekannter Fehler
	public void unbekannterFehler() {
		infoNachricht = "Ein unbekannter Fehler ist aufgetreten.";
		aktionNachricht = "Bitte informiere deinen Admin.";
	}

	// Getter Setter
	public String getInfoNachricht() {
		return infoNachricht;
	}

	public void setInfoNachricht(String infoNachricht) {
		this.infoNachricht = infoNachricht;
	}

	public String getAktionNachricht() {
		return aktionNachricht;
	}

	public void setAktionNachricht(String aktionNachricht) {
		this.aktionNachricht = aktionNachricht;
	}

}
