# 2gether
Die Idee war es, ein soziales Netzwerk aufzubauen, um neue Leute kennenzulernen, Veranstaltungen in der Region zu finden und diese gemeinsam zu besuchen.
Darüber hinaus gibt es Gruppen, die längerfristig sind und über die man sich verabreden kann und nicht wie Veranstaltungen eventuell nur einmalig stattfinden.
Aufgebaut wird das Ganze auf Interessen und Städteinteressen, die im Profil hinterlegt werden können. Über diese Interessen werden dann passende Vorschläge für Freunde, Veranstaltungen und Gruppen angezeigt.

Diese Funktionen sind in der Navigationsleiste der Anwendung zu finden:
-	Startseite (Vorschläge)
-	Benutzerprofil
-	Nachrichten
-	Veranstaltungen
-	Freunde
-	Gruppen
-	Einstellungen

Installation:

Die Anwendung besteht aus JSP-Dateien (HTML: Views und Appls), CSS-Dateien, einer JS-Datei, den verschiedenen Beans und separaten Klassen, die von den Beans verwendet werden.
Diese Dateien sind gemeinsam in einem Eclipse-Projekt gespeichert. Darüber hinaus greift die Anwendung auf eine Datenbank zu, um die Daten zu speichern. Diese kann z.B. über pgAdmin eingespielt werden kann.
Das Eclipse-Projekt ist zu importieren und der entsprechende TomCat Server einzubinden.
Darüber hinaus ist im Package JDBC in der Klasse PostgreSQLAccess in der Methode setDBParms die eigenen Zugangsdaten für die Datenbank einzugeben. Als Schema kann Webprojekt gewählt werden.
Die Befehle in der Datei pgAdminSQLSkript können kopiert und in pgAdmin ausgeführt werden.

Übersicht Appls / Views:

Im Eclipse-Projekt im Ordner Webcontent sind folgende Unterordner zu finden:
Gruppe, Nachricht, Profil, Start, Veranstaltung und Willkommen.
In diesen Unterordnern sind immer die zugehörige Appl und die darauf zugreifenden Views zu finden.
Darüber hinaus gibt es einen Unterordner CSS, der für die Oberfläche zuständig ist und einen Ordner JS, in dem der JavaScript-Teil programmiert ist.
Im Unterordnung IMG liegen die Avatarbilder für Benutzer, Veranstaltungen und Gruppen.

Zusammenspiel Appl / Views / Beans:

GruppeAppl sowie die GruppenViews greifen auf folgende Beans zu:
GruppeBean, NachrichtBean (wegen Darstellung der Nachrichten in den Gruppen)
NachrichtAppl sowie NachrichtView greifen auf folgende Beans zu:
NachrichtBean
ProfilAppl sowie die ProfilViews greifen auf folgende Beans zu:
ProfilBean
StartAppl sowie StartView greifen auf folgende Beans zu:
StartBean, ProfilBean (wegen neuen Freundschaftsanfragen), NachrichtBean (wegen Info neuer Nachrichten)
VeranstaltungAppl sowie die VeranstaltungViews greifen auf folgende Beans zu:
VeranstaltungBean, NachrichtBean (wegen Darstellung der Nachrichten in Veranstaltungen)
WillkommenAppl sowie die zugehörigen Views greifen auf folgende Beans zu:
WillkommenBean, ProfilBean (wegen Laden der bisherigen Benutzerdaten bei Registrierung und Auslesen neuer Freundschaftsanfragen  beim Login), StartBean (Auslesen der automatischen Vorschläge beim Login), NachrichtBean (wegen Info neuer Nachrichten beim Login), GruppeBean (Gruppenvariablen leer setzen), VeranstaltungBean (Veranstaltungsvariablen leer setzen)
Die DenullifizierBean, BenachrichtigungBean und MenuBean werden von allen Appls bzw. Views verwendet.

Übersicht Beans:
1.	WillkommenBean für Login / Registrierung / Einstellungen / Logout
Methoden für Registrierung, Login, Logout, Account löschen

2.	StartBean für Startseite / Vorschläge bzw. Matching
Methoden für automatische Vorschläge von Benutzern, Veranstaltungen, Gruppen

3.	GruppeBean für Gruppen
Methoden für Gruppenerstellung (Darstellung der Interessen), Mitglieder aus Gruppen auslesen, Gruppe löschen, Gruppenprofil auslesen, Gruppe bearbeiten, Gruppe beitreten/verlassen, eigene Gruppen darstellen

4.	NachrichtBean für Nachrichten Benutzer/Gruppen/Veranstaltungen
Methoden für Nachricht speichern und richtiger Tabelle in Datenbank zuordnen (Benutzer, Veranstaltung oder Gruppe), neue Nachrichten auslesen sowie gelesen markieren, alle Nachrichten auslesen, Nachrichtenbenutzer auslesen

5.	ProfilBean für eigenes Profil sowie anderer Benutzer
Methoden für Benutzerdaten auslesen, Interessendaten auslesen, Profil bearbeiten, Darstellung der Interessen im Profil / Profil Bearbeiten Modus, Freunde auslesen und darstellen, Buttons für Freundschaftsanfragen, neue Anfragen, Freund annehmen, entfernen, Alter berechnen anhand des Geburtstags, richtiger Avatar anhand des Geschlechts zuweisen

6.	VeranstaltungBean für Veranstaltungen
Methoden für Interessendarstellung von Veranstaltungen im Profil bzw. Veranstaltung bearbeiten Modus, Mitglieder auslesen, Veranstaltung löschen, Veranstaltungsdetails auslesen, Veranstaltung bearbeiten/verlassen/beitreten, eigene Veranstaltungen darstellen

7.	MenuBean für Navigationsleiste der Anwendung
Methode zur Darstellung der Menüleiste

8.	DenullifizierBean zum Denullifizieren der Parameter

9.	BenachrichtigungBean für Benachrichtungen der Benutzer
Verschiedene Methoden zur Kommunikation System -> Benutzer

Techniken:

Sämtliche Datenbankabrufe, Speicherungen und Datenbank-Änderungen werden durch JDBC-Befehle realisiert. Zum Teil werden Joins verwendet, um lokale Array-Variablen zu sparen.
CSS wird zur Darstellung der Oberfläche verwendet, für die Textausrichtung, Scrollbalken sowie Darstellung der Buttons. Das verwendete W3 Framework wurde auf unsere Bedürfnisse angepasst bzw. zerstückelt und in die Beans verschoben, um die Darstellung für alle Views gleich zu gestalten.
JavaScript wird für die Fehlermeldungen bzw. Hinweise bei der Registrierung verwendet. Die Anforderungen sind, dass die Felder nicht leer sein dürfen.
Über den Hilfe-Button können die Anforderungen an das Passwort gelesen werden.
Weiterhin wird das ausgewählte Geburtsdatum auf Korrektheit geprüft.
Außerdem gibt es eine Prüfung, ob bei der Profilbearbeitung bei der Altersauswahl der Benutzer das Mindestalter korrekterweise kleiner ist als das Maximalalter.
Im Package de.hs_lu.beans sind folgende Klassen zu finden:
Benutzer, Freund, Gruppe, Interesse, Mitglied, Nachricht und Veranstaltung.
Diese Klassen enthalten Attribute und mehrere Konstruktoren, die verschiedene Parameter übergeben. Die Konstruktoren werden in den zugehörigen Beans verwendet, um den jeweiligen Benutzer/Freund/Gruppe etc. in einer ArrayList zu speichern. Die Ausgabe der Objekte erfolgt in der gesamten Anwendung größtenteils über Auslesen von ArrayLists.

Besonderheiten:
In WillkommenBean wird die Benutzer-ID des Benutzers anhand seiner E-Mail von der Datenbank abgerufen. Diese wird in einer statischen Klassenvariable in der WillkommenBean gespeichert.
In den anderen Beans wird dann, wenn diese benötigt wird, die Benutzer-ID des aktuellen Benutzers durch die Variable WillkommenBean.getBenutzer_id abgerufen und in einer lokalen Variable in der entsprechenden Bean gespeichert, um in dieser Bean mit der ID zu arbeiten.
Hierüber wird realisiert, dass durch die Anwendung hinweg in allen Views, in denen der Benutzer unterwegs ist, sichergestellt ist, dass immer die eigene Benutzer-ID verfügbar ist.
Die Funktion für die Auswahl der Benutzer/Gruppen/Veranstaltungen, die normalerweise über Radio Buttons mit dem entsprechenden Button z.B. “Profil anzeigen“ gelöst wird, wird ersetzt, indem Links verwendet werden. Alle Links wurden mit CSS als Button verkleidet, sodass nur noch auf den gewünschten Link/Button geklickt werden muss, um zum Profil zu gelangen.
Dieser Linkt enthält die Weiterleitung zur entsprechenden Appl und dann zur gewünschten View sowie die ID des jeweiligen Benutzers. Der Link wird in der Bean entsprechend mit der Variable ausgewählteFreund_id gefüllt, die über eine ArrayList der Klasse Freund gesetzt wird, in der alle Freunde gespeichert werden. Zusätzlich wird der passende Avatar (männlich oder weiblich) mitgegeben und in den Views dargestellt.

Diese Links wurden für folgende Funktionen verwendet:
Mitgeben der Freund-ID:
- Profil von Benutzern/Freunden anzeigen
- Annehmen/Ablehnen von Freundschaftsanfragen
- Benutzern eine Nachricht schreiben über den Link im Profil
- Info über neue Nachrichten, Weiterleitung zu dem richtigen Chat

Hier wird entsprechend die Gruppe- bzw. Veranstaltung-ID mitgegeben:
- Gruppenprofil anzeigen
- Gruppennachrichten anzeigen
- Gruppenmitglieder anzeigen
- Veranstaltungsprofil anzeigen
- Veranstaltungsnachrichten anzeigen
- Veranstaltungsmitglieder anzeigen
